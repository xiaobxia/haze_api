package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.dao.UserQuotaSnapshotDao;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.IdUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IMoneyLimitService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SpringUtils;
import com.vxianjin.gringotts.web.utils.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 淘宝认证
 * @author zed
 */
@Controller
public class UserTaobaoApproveController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserTaobaoApproveController.class);
//    @Resource
//    private ITaobaoService taobaoService;
    @Resource
    private IUserService userService;

    @Resource
    private IUserDao userDao;

    @Resource
    private IBorrowOrderService borrowOrderService;

    @Resource
    private IMoneyLimitService moneyLimitService;

//    @Autowired
//    private UserQuotaSnapshotService quotaSnapshotService;

    @Resource
    private BorrowProductConfigDao borrowProductConfigDao;

    @Resource
    private UserQuotaSnapshotDao userQuotaSnapshotDao;

    /**
     * 淘宝认证授权页面
     *
     * @param request request
     * @param model model
     * @return str
     */
    @RequestMapping("/creditreport/taobao-mobile-api")
    public String createTaobaoPage(HttpServletRequest request, Model model) {
        String code = ResponseStatus.FAILD.getName();
        String msg = "查询失败！";
        String record = request.getParameter("record");
        String mobilePhone = request.getParameter("mobilePhone");
        String clientType = request.getParameter("clientType");
        String deviceId = request.getParameter("deviceId");
        Integer userId = null;
        try {
            User user = this.loginFrontUserByDeiceId(request);
            if (user != null) {
                userId = Integer.valueOf(user.getId());
            } else {
                if (request.getParameter("userId") != null) {
                    userId = Integer.valueOf(request.getParameter("userId"));
                }
            }

            if (userId != null) {
                user = userService.searchByUserid(userId);
                //已认证状态
                if ("2".equals(user.getZmStatus())) {
                    code = "200";
                    msg = "已授权";
                    //认证中状态
                } else if ("3".equals(user.getZmStatus())) {
                    code = "300";
                    msg = "授权中";
                    if (DateUtil.minutesBetween(DateUtil.dateFormat(user.getZmScoreTime(), "yyyy-MM-dd HH:mm:ss"), new Date()) > 3) {
                        code = "100";
                        msg = "未授权";
                        User userNew = new User();
                        userNew.setId(user.getId());
                        userNew.setZmStatus("1");
                        userDao.updateUserZm(userNew);
                    }

                } else {
                    //未认证中状态
                    code = "100";
                    msg = "";
                    String zmToken = user.getZmToken();
                    if (StringUtils.isNotEmpty(zmToken)) {
                        JSONObject obj = JSONObject.parseObject(zmToken);
                        if (obj.containsKey("status") && "FAIL".equals(obj.getString("status"))) {
                            msg = obj.getString("msg");
                        }
                    }
                }
                logger.info("createTaobaoPage success");
            } else {
                code = "300";
                msg = "退出请重新登录";
            }
        } catch (Exception e) {
            logger.error("createTaobaoPage error=", e);
        }

        model.addAttribute("clientType", clientType);
        model.addAttribute("mobilePhone", mobilePhone);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("userId", userId + "");
        model.addAttribute("record", record);
        model.addAttribute("code", code);
        model.addAttribute("msg", msg);
//        model.addAttribute("riskUrl", PropertiesConfigUtil.get("risk_taobao_url"));
//        model.addAttribute("orderNo", IdUtil.getUUID());
//        model.addAttribute("consumerNo", PropertiesConfigUtil.get("RISK_BUSINESS")+userId);
//        model.addAttribute("consumerNo", "123");
        return "userinfo/taobao/taobaoPage";
    }

    /**
     * 淘宝认证授权请求
     *
     * @param response res
     * @param userId userId
     */
    @RequestMapping("creditreport/taobao-request")
    public void createTaobaoRequest(HttpServletResponse response, Integer userId) {
        logger.info("createTaobaoRequest start");
        logger.info("createTaobaoRequest userId=" + userId);
        ResponseContent result = null;
        if (userId == null || StringUtils.isBlank(userId.toString())) {
            result = new ResponseContent("-101", "请求参数非法");
            SpringUtils.renderJson(response, result);
            return;
        }

        User user = userService.searchByUserid(userId);
        if (null == user) {
            result = new ResponseContent("-101", "获取用户信息失败");
            SpringUtils.renderJson(response, result);
            return;
        }
//        ResponseContent serviceResult = taobaoService.getToken(resMap);
//        logger.info("createTaobaoPage serviceResult" + JSON.toJSONString(serviceResult));
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo",System.currentTimeMillis()+IdUtil.generateRandomStr(6));
        map.put("consumerNo",PropertiesConfigUtil.get("RISK_BUSINESS")+userId);
        JSONObject riskObject = JSON.parseObject(HttpUtil.sendPost(PropertiesConfigUtil.get("risk_taobao_url"),map));
        if (null != riskObject) {
            if ("0000".equals(riskObject.getString("code"))) {
                String requestUrl = riskObject.getJSONObject("data").getString("url");
                logger.info("createTaobaoPage requestUrl=" + requestUrl);
                if (StringUtils.isNotEmpty(requestUrl)) {
                    result = new ResponseContent("0", requestUrl);
                } else {
                    result = new ResponseContent("-101", "操作失败，请稍后重试");
                }
            } else {
                result = new ResponseContent("-101", riskObject.getString("msg"));
            }
        } else {
            result = new ResponseContent("-101", "操作失败，请稍后重试");
        }

        SpringUtils.renderJson(response, result);
    }

    /**
     * 淘宝认证授权同步回调
     *
     * @param request req
     * @param model model
     * @return str
     */
    @RequestMapping("creditreport/taobao-return-back")
    public String createTaobaoReturnCallback(HttpServletRequest request,Model model) {
        logger.info("createTaobaoReturnCallback start");
        String code = ResponseStatus.FAILD.getName();
        String msg = "查询失败！";
        String record = request.getParameter("record");
        String mobilePhone = request.getParameter("mobilePhone");
        String clientType = request.getParameter("clientType");
        String deviceId = request.getParameter("deviceId");
        String userId = request.getParameter("userId");
        try {
            User user = null;

            if (userId != null) {
                user = userService.searchByUserid(Integer.parseInt(userId));
                //已认证状态
                if ("2".equals(user.getZmStatus())) {
                    code = "200";
                    msg = "已授权";
                } else {//认证中状态
                    code = "300";
                    msg = "授权中";
                    User userNew = new User();
                    userNew.setId(user.getId());
                    //授权中状态
                    userNew.setZmStatus("3");
                    //授权时间时间
                    userNew.setZmScoreTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    logger.info("createTaobaoReturnCallback userNew" + JSON.toJSONString(userNew));
                    userDao.updateUserZm(userNew);
                }
                logger.info("createTaobaoReturnCallback success");
            } else {
                code = "300";
                msg = "退出请重新登录";
            }
        } catch (Exception e) {
            logger.error("createTaobaoPage error=", e);
        }

        model.addAttribute("clientType", clientType);
        model.addAttribute("mobilePhone", mobilePhone);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("userId", userId + "");
        model.addAttribute("record", record);
        model.addAttribute("code", code);
        model.addAttribute("msg", msg);
        return "userinfo/taobao/taobaoPage";
    }

    /**
     * 淘宝认证授权异步回调
     *
     * @param request res
     * @param response res
     *     http://116.62.40.106:8088/creditreport/taobao-notify-back
     */
    @PostMapping(value = "creditreport/taobao-notify-back")
    public void createTaobaoNotifyCallback(HttpServletRequest request,
                                           HttpServletResponse response) {
        logger.info("createTaobaoNotifyCallback start");
        try {
            String code = request.getParameter("code");
            logger.info("data str:{}",request.getParameter("data"));
            JSONObject jsonObject = JSON.parseObject(request.getParameter("data"));
            logger.info("jsonObject:{}",jsonObject.toJSONString());
            String msg = request.getParameter("msg");
            String status = jsonObject.getString("result");
            String consumerNo = jsonObject.getString("consumerNo");
            logger.info("createTaobaoNotifyCallback requestStr=" + JSON.toJSONString(request.getParameterMap()));
            if (null == status || StringUtils.isEmpty(status)) {
                HashMap<String, String> resMap = new HashMap<>();
                resMap.put("code", "500");
                resMap.put("message", "status 参数解析失败");
                logger.error("createTaobaoNotifyCallback resMap = " + JSON.toJSONString(resMap));
                JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
                return;
            }
            String userId = consumerNo.replace(PropertiesConfigUtil.get("RISK_BUSINESS"),"");
            User user = userService.searchByUserid(Integer.valueOf(userId));
            if (null == user) {
                HashMap<String, String> resMap = new HashMap<>();
                resMap.put("code", "500");
                resMap.put("message", "获取用户信息失败");
                logger.error("createTaobaoNotifyCallback resMap = " + JSON.toJSONString(resMap));
                JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
                return;
            }
            //用户id
            if ("2".equals(user.getZmStatus())) {
                HashMap<String, String> resMap = new HashMap<>();
                resMap.put("code", "0");
                resMap.put("message", "success");
                logger.info("createTaobaoNotifyCallback chongfu userId = " + user.getId());
                JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
                return;
            }

            logger.info("createTaobaoNotifyCallback userId=" + userId + " status=" + status + " msg=" + msg);
            //授权成功
            if ("0000".equals(code) && "10".equals(status)) {
                User userNew = new User();
                userNew.setId(user.getId());
                userNew.setZmStatus("2");
                userNew.setZmScoreTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                logger.info("createTaobaoNotifyCallback newUser = " + userNew.getZmStatus());
                userService.updateZm(userNew);
                //判断芝麻认证和同盾运营商认证是否都认证完，如果都认证完，则给予默认额度
                logger.info("createTaobaoNotifyCallback userId=" + user.getId() + " zmStatus=" + userNew.getZmStatus() + " tdStatus=" + user.getTdStatus());
                if ("2".equals(userNew.getZmStatus()) && "2".equals(user.getTdStatus())) {
                    //更新newFlag
                    userDao.updateUserNewFlagById(userNew);
                    //初始额度
                    String amountMax = "1000";
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());

                    map.put("newAmountMax", new BigDecimal(amountMax).multiply(
                            new BigDecimal(100)).intValue());

                    if ("1".equals(user.getNewFlag())) {
                        // 用户额度更新 认证完
                       /* logger.info("update user quotaSnapShot start userId: " + user.getId());
                        quotaSnapshotService.updateUserQuotaSnapshots(user);
                        logger.info("update user quotaSnapShot end userId: " + user.getId());*/

                        logger.info("createTaobaoNotifyCallback changeUserLimit start userId=" + userId);
                        borrowOrderService.changeUserLimit(map);
                        logger.info("createTaobaoNotifyCallback changeUserLimit end userId=" + userId);
                        try{
                            //初始化用户额度配置
                            HashMap<String, String> params = new HashMap<>();
                            params.put("borrowAmount","100000");
                            params.put("borrowDay","7");
                            int configId = borrowProductConfigDao.selectConfigId(params);
                            logger.info("addUserQuota params:userId" + user.getId() + ";configId:" + configId + ";moneyLimit:" + 100000);
                            userQuotaSnapshotDao.addUserQuota(Integer.valueOf(user.getId()),configId,new BigDecimal("100000"),7);
                        }catch (Exception e){
                            logger.error("addUserQuota has error:{}" , e);
                        }
                    }
                    logger.info("createTaobaoNotifyCallback success");

                    final int USER_ID = Integer.parseInt(userId);

                    ThreadPool.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            logger.info("createTaobaoNotifyCallback thread run start userId = " + USER_ID);
                            //发送风控信审操作
                            moneyLimitService.dealEd(USER_ID + "");
                        }
                    });
                }

                HashMap<String, String> resMap = new HashMap<>();
                resMap.put("code", "0");
                resMap.put("message", "success");

                logger.info("createTaobaoNotifyCallback respMap = " + JSON.toJSONString(resMap));
                logger.info("createTaobaoNotifyCallback end");
                JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
                return;
            }else{
                Map<String, String> stu = new HashMap<>();
                stu.put("status", "FAIL");
                stu.put("msg", msg);

                User userNew = new User();
                userNew.setId(user.getId());
                userNew.setZmStatus("1");
                userNew.setZmToken(JSON.toJSONString(stu));
                logger.info("createTaobaoNotifyCallback fail newUser = " + userNew.getZmStatus());
                userService.updateZm(userNew);
            }

        } catch (Exception e) {
            logger.error("createTaobaoNotifyCallback error:", e);
        }

        HashMap<String, String> resMap = new HashMap<>();
        resMap.put("code", "0");
        resMap.put("message", "success");
        JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
    }

}
