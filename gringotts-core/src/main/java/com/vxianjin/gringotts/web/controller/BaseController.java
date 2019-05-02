package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.SerializeUtil;
import com.vxianjin.gringotts.web.common.JedisClusterHelper;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.BackUser;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IInfoIndexService;
import com.vxianjin.gringotts.web.utils.RequestUtils;
import com.vxianjin.gringotts.web.utils.SpringUtils;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author zed
 */
public class BaseController {

    public static final String MESSAGE = "message";
    public static final String LOGING_DEVICE_PREFIX = "device_";
    public static final String UNDERLINE = "_";
    //3*24*60*60
    public static final int FRONT_USER_EXIST_TIME = 259200;
    //3*24*60*60
    public static final int BACK_USER_EXIST_TIME = 259200;

//    public static final int CODE_ERROR = -1 ;//错误信息
//    public static final int CODE_WAITING = -3 ;//等待信息
//    public static final int CODE_UNLOGIN = -2 ;//未登录
//    public static final int CODE_SUCCESS = 0 ;//请求成功

    private static Logger loger = LoggerFactory.getLogger(BaseController.class);

    @Resource
    protected JedisCluster jedisCluster;
    @Resource
    private IInfoIndexService infoIndexService;

    /**
     * 将cookie封装到Map里面
     *
     * @param request req
     * @return map
     */
    private static Map<String, String> readCookieMap(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }

    /**
     * 返回未登录状态
     */
    private static void renderUnLogin(HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ResponseStatus.LOGIN.getName());
        result.put("message", "请先登录");
        SpringUtils.renderJson(response, result);
    }

    /**
     * 获取IP地址
     *
     * @param request req
     * @return map
     */
    public String getIpAddr(HttpServletRequest request) {
        return RequestUtils.getIpAddr(request);
    }
    public static void renderAppJson(HttpServletResponse response, Object obj, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", ResponseStatus.SUCCESS.getName());
        result.put("message", null != message ? message : "请求成功");
        result.put("data", obj);
        SpringUtils.renderJson(response, result);
    }

    public static void renderAppJson(HttpServletResponse response, Object obj) {
        renderAppJson(response, obj, null);
    }
    /**
     * 得到session中的admin user对象
     */
    public BackUser loginAdminUser(HttpServletRequest request) {


        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        byte[] userInfo = jedisCluster.get(request.getSession().getId().getBytes());
        return (BackUser) SerializeUtil.unserialize(userInfo);
    }

    /**
     * 获取APP的配置参数
     *
     * @param appName appName
     * @param param param
     * @return str
     */
    public String getAppConfig(String appName, String param) {
        if (StringUtils.isBlank(appName)) {
            appName = "mld";
        }
        Map<String, String> map = SysCacheUtils.getConfigMap(appName);
        return map.get(param + "_" + appName);
    }

    public ResponseContent check(String phone) {
        Map<String, String> map = SysCacheUtils.getConfigMap(BackConfigParams.SMSCODE);
        int phoneLimit = Integer.valueOf(map.get("PHONE_LIMIT"));
        int ipLimit = Integer.valueOf(map.get("IP_LIMIT"));
        String ip = RequestUtils.getIpAddr();
        String key = "CODE_TYPE_PHONE_" + phone;
        int time = 60 * 60 * 24;
        String phoneTimes = jedisCluster.get(key);
        int phones = 0;
        if (phoneTimes != null) {
            phones = Integer.valueOf(phoneTimes);
            if (phones >= phoneLimit) {
                loger.info("phone limit " + phone);
                return new ResponseContent("500", "当日验证码发送达到上限，请24小时后重试！");
            }
        }
        phones += 1;
        String key2 = "CODE_TYPE_IP_" + ip;
        String ipTimes = jedisCluster.get(key2);
        int ips = 0;
        if (ipTimes != null) {
            ips = Integer.valueOf(ipTimes);
            if (ips >= ipLimit) {
                loger.info("ip limit " + ip);
                return new ResponseContent("500", "IP受限，请24小时后重试！");
            }
        }
        ips += 1;
        jedisCluster.setex(key, time, phones + "");
        jedisCluster.setex(key2, time, ips + "");
        return new ResponseContent(ResponseContent.SUCCESS, "允许发送");
    }

    public User loginFrontUserByDeiceId(HttpServletRequest request, HttpServletResponse response) {
        String deviceId = request.getParameter("deviceId");
        String telephone = request.getParameter("mobilePhone");
        loger.info("设备号deviceId=" + deviceId);
        loger.info("手机号码：" + telephone);
        if (StringUtils.isBlank(deviceId)) {
            renderUnLogin(response);
            return null;
        }
        //缓存KEY
        String userRedisKey = getUserRedisKey(deviceId, telephone);
        String redisUser = jedisCluster.get(userRedisKey);

        if (StringUtils.isBlank(redisUser)) {
            renderUnLogin(response);
            return null;
        }
        loger.info("该用户当前的缓存信息: redis userInfo = " + redisUser);

        User zbUser = new User();
        String userId = "";
        try {
            //判断用户缓存是否已经变为userid形式存储,如果否，此处会报异常
            if (Integer.valueOf(redisUser) > 0) {
                userId = redisUser;
            }
        } catch (Exception e) {
            loger.error("loginFrontUserByDeiceId error:", e);
            renderUnLogin(response);
            return null;
        }
        zbUser.setId(userId);
        loger.info("loginFrontUserByDeiceId key :" + (userRedisKey) + ",userId:" + userId);
        loger.info("loginFrontUserByDeiceId end");
        return zbUser;
    }

    public User loginFrontUserByDeiceId(HttpServletRequest request) {
        loger.info("loginFrontUserByDeiceId2 start");
        String deviceId = request.getParameter("deviceId");
        String telephone = request.getParameter("mobilePhone");
        loger.info("设备号deviceId=" + deviceId);
        loger.info("手机号码：" + telephone);
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(telephone)) {
            return null;
        }
        //缓存KEY
        String userRedisKey = getUserRedisKey(deviceId, telephone);

        String redisUser = jedisCluster.get(userRedisKey);

        if (StringUtils.isBlank(redisUser)) {
            return null;
        }

        loger.info("该用户当前的缓存信息: redis userInfo = " + redisUser);

        User zbUser = new User();
        String userId = "";
        try {
            //判断用户缓存是否已经变为userid形式存储,如果否，此处会报异常
            if (Integer.valueOf(redisUser) > 0) {
                userId = redisUser;
            }
        } catch (Exception e) {
            loger.error("loginFrontUserByDeiceId2 error:", e);
            return null;
        }
        zbUser.setId(userId);

        loger.info("loginFrontUserByDeiceId2 key :" + (userRedisKey) + ",userId:" + userId);
        loger.info("loginFrontUserByDeiceId2 end");
        return zbUser;
    }

    public void loginSucc(HttpServletRequest request, User user) {
        String deviceId = request.getParameter("deviceId");
        String telephone = user.getUserName();

        loger.info(MessageFormat.format("用户登陆login,deviceId: {0}, mobilePhone: {1}", deviceId, telephone));

        if (StringUtils.isBlank(deviceId)) {
            return;
        }
        User userNew = new User();
        userNew.setId(user.getId());

        String userRedisKey = getUserRedisKey(deviceId, telephone);

        jedisCluster.set(userRedisKey, user.getId());
        jedisCluster.expire(userRedisKey, FRONT_USER_EXIST_TIME);
        loger.info("loginSucc redis user key :" + userRedisKey + ",userId:" + user.getId());
        //初始化动态表
        this.infoIndexService.addInfoIndexInfo(userNew);
    }

    public void loginOut(HttpServletRequest request) {
        String deviceId = request.getParameter("deviceId");
        String telephone = request.getParameter("mobilePhone");
        loger.info(MessageFormat.format("用户登出loginout,deviceId: {0}, mobilePhone: {1}", deviceId, telephone));

        if (StringUtils.isBlank(deviceId)) {
            return;
        }
        //缓存key
        String userRedisKey = getUserRedisKey(deviceId, telephone);
        String redisUser = jedisCluster.get(userRedisKey);
        String userId = "";
        if (redisUser != null) {

            loger.info("登出用户当前的缓存信息: redis loginout userInfo = " + redisUser);
            //删除redis中用户动态数据
            jedisCluster.del(userRedisKey);

            //判断用户缓存是否已经变为userid形式存储,如果否，此处会报异常
            try {
                if (Integer.valueOf(redisUser) > 0) {
                    userId = redisUser;
                }
            } catch (Exception e) {
                loger.error("loginOut error:", e);
            }

        }
        //删除缓存中的用户信息
        if (StringUtils.isNotBlank(userId)) {
            jedisCluster.del(Constant.CACHE_INDEX_INFO_KEY + userId);
        }
    }

    private String getUserRedisKey(String deviceId, String telephone) {
        if (StringUtils.isBlank(telephone)) {
            return LOGING_DEVICE_PREFIX + deviceId;
        } else {
            return LOGING_DEVICE_PREFIX + deviceId + UNDERLINE + telephone;
        }
    }

    /**
     * 获得request中的参数
     *
     * @param request req
     * @return string object类型的map
     */
    public HashMap<String, Object> getParametersO(HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {
            Map<String, Object> p = new HashMap<>();
            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v = null;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    if (((Object[]) value).length > 1) {
                        v = ((Object[]) value);
                    } else {
                        v = ((Object[]) value)[0];
                    }
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = ((String) v).trim();
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            hashMap.putAll(p);
            // 读取cookie
            hashMap.putAll(readCookieMap(request));

        }
        return hashMap;
    }

    /**
     * 得到页面传递的参数封装成map
     */

    public Map<String, String> getParameters(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        Map<String, String> p = new HashMap<>();
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {

            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v = null;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    v = ((Object[]) value)[0];
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = (String) v;
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            //读取cookie
            p.putAll(readCookieMap(request));
            return p;
        }
        return p;
    }

    /**
     * @param key key
     * @param flag flag
     * @return true:设置成功（代表是没有超发），false：设置失败（超发）
     */
    public Long checkForFront(String key, String flag) {
        return JedisClusterHelper.checkForFront(jedisCluster, key, flag, 15);
    }

    /**
     * @param key key
     * @param flag flag
     * @return true:设置成功（代表是没有超发），false：设置失败（超发）
     */
    public Long checkForFront(String key, String flag, int time) {
        return JedisClusterHelper.checkForFront(jedisCluster, key, flag, time);
    }

    public boolean delCheckForFront(String key, String flag) {
        /*loger.info("delCheckForFront key:"+key+",flag:"+flag);
        String expireKey = key+flag;
		loger.info("delCheckForFront expireKey:"+expireKey);
		jedisCluster.del(expireKey);*/
        return true;
    }

    /**
     * 获取配置数据
     * @param list list
     * @return map
     */
    public Map<String,String> getBackConfigParamsMap(List<BackConfigParams> list){
        Map<String,String> intervalMap = new HashMap<>();

        if(null != list && list.size() > 0){
            for (BackConfigParams param : list) {
                if(com.vxianjin.gringotts.util.StringUtils.isNotBlank(param.getSysValue())){
                    intervalMap.put(param.getSysKey(),param.getSysValue());
                }else{
                    intervalMap.put(param.getSysKey(),param.getSysValueBig());
                }
            }
        }
        loger.info("BaseController BackConfigParamsMap=" + JSONObject.toJSONString(intervalMap));
        return intervalMap;
    }

}

