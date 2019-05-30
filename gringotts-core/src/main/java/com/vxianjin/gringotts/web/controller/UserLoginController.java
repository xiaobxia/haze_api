package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.ThreadPool;
import com.vxianjin.gringotts.attach.pojo.UserInfoTude;
import com.vxianjin.gringotts.attach.service.IUserInfoTudeService;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.risk.dao.IRiskOrdersDao;
import com.vxianjin.gringotts.risk.pojo.RiskOrders;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.printer.Printer;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.util.security.RsaUtil;
import com.vxianjin.gringotts.web.common.Certification.IHttpCertification;
import com.vxianjin.gringotts.web.common.result.Status;
import com.vxianjin.gringotts.web.dao.impl.UserContactsDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.*;
import com.vxianjin.gringotts.web.service.impl.ChannelReportService;
import com.vxianjin.gringotts.web.util.ConfigLoader;
import com.vxianjin.gringotts.web.util.SendSmsUtil;
import com.vxianjin.gringotts.web.util.TokenUtils;
import com.vxianjin.gringotts.web.utils.RequestUtils;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.borrowStatusMap_shenheFail;

/***
 * 注册用户 用户登录 修改密码 退出登录
 *
 * @author Administrator
 */
@Controller
public class UserLoginController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
    private static Pattern PATTERN_USER_ID = Pattern.compile("^-?[1-9]\\d*$");
    private static Pattern PATTERN_NAME = Pattern.compile("[\\u4e00-\\u9fa5][\\u4e00-\\u9fa5\\·]{0,40}[\\u4e00-\\u9fa5]|[\\u4e00-\\u9fa5]");
    private static Pattern PATTERN_PHONE = Pattern.compile("^1\\d{10}$");

    /**
     *  Redis注册key前缀
     */
    private final static String SMS_REGISTER_PREFIX = "newPhoneCode_";
    /**
     * Redis找回密码key前缀
     */
    private final static String SMS_FIND_PREFIX = "updatePhoneCode_";
    /**
     * Redis找回交易密码key前缀
     */
    private final static String SMS_TRADE_FIND_PREFIX = "updatePayPasswordCode_";
    /**
     * Redis某个手机号1分钟内已发送验证码key前缀
     */
    private final static String SMS_SEND_IN_ONE_MINUTE = "SMS_SEND_IN_ONE_MINUTE_";
    /**
     * 短信默认有效时间300秒
     */
    private final static int INFECTIVE_SMS_TIME = 300;
    /**
     * Redis注册次数
     */
    private final static String REGISTER_COUNT = "registerCount_";
    private final static int ERROR_TIME = 60;
    /**
     * 判断第一次注册获取短信操作时间
     */
    public final static String REGISTER_UPDATE_TIME = "registerUpdateTime_";
    /**
     * 找回密码发送验证码次数
     */
    private final static String BACK_PASS_COUNT = "backPassCount_";
    /**
     * 判断第一次找回密码操作时间
     */
    private final static String BACK_PASS_UPDATE_TIME = "backPassUpdateTime_";
    /**
     *  Redis某个手机号1分钟内已发送语音验证码key前缀
     */
    private final static String SMS_VOICE_SEND_IN_ONE_MINUTE = "SMS_VOICE_SEND_IN_ONE_MINUTE_";

    //    private static String appCode = "";
    private static String domainOfBucket = "";
    private static PropertiesConfiguration configuration;

    static {
//        appCode = PropertiesConfigUtil.get("RISK_BUSINESS");

        configuration = ConfigLoader.getInstance().getAliConfigurations();

        if (null != configuration) {
            domainOfBucket = configuration.getString("domainOfBucket");
        }
    }

    @Resource
    ICfcaSignAndViewService cfcaSignAndViewService;
    @Resource
    private IUserService userService;
    @Resource
    private IUserLoginLogService userLoginLogService;
    @Resource
    private IUserSendMessageService userSendMessageService;
    @Resource
    private IUserInfoImageService userInfoImageService;
    @Resource
    private IPlatfromAdviseService platfromAdviseService;
    @Resource
    private IUserContactsService userContactsService;
    @Resource
    private IHttpCertification httpCertification;
    @Resource
    private IObtainUserShortMessageService obtainUserShortMessageService;
    @Resource
    private IUserAppSoftwareService userAppSoftwareService;
    @Resource
    private IInfoIndexService infoIndexService;
    @Resource
    private IChannelInfoService channelInfoService;
    @Resource
    private IUserInfoTudeService userInfoTudeService;
    @Resource
    private IBorrowOrderService borrowOrderService;
    @Resource
    private IRiskOrdersDao riskOrdersDao;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private RepaymentService repaymentService;

    @Resource
    private BorrowProductConfigDao borrowProductConfigDao;
    private String result;

    @Resource
    private ChannelReportService channelReportService;
    @Resource
    private IAppDownloadConfigService appDownloadConfigService;

    /**
     * 注册 生成手机认证码
     */
    @RequestMapping("credit-user/reg-get-code")
    public void sendSmsCode(HttpServletRequest request, HttpServletResponse response) {
        // 生成手机认证码频繁标识key
        String generateRegisterCode = "check_generate_register_code_";
        String clientType = request.getParameter("clientType");
        //v2>=220
        if ("ios".equals(clientType)) {
            JSONObject json = new JSONObject();
            Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
            HashMap<String, Object> resultMap = new HashMap<>();
            HashMap<String, Object> map = new HashMap<>();
            String code = "-1";
            String msg = "";
            UserSendMessage message = new UserSendMessage();
            String userPhone = "";
            try {
                Map<String, Object> param = getParametersO(request);
                userPhone = null == param.get("phone") ? "" : param.get("phone").toString();
                log.info("regest:phone=" + userPhone + " clientType=" + clientType);
                map.put("userPhone", userPhone);
                if (StringUtils.isBlank(userPhone)) {
                    msg = "请输入手机号码";
                    return;
                }
                // 查询手机号码是否存在
                User user = userService.searchUserByCheckTel(map);
                // 6位固定长度
                String rand = "123456";
                if ("online".equals(PropertiesConfigUtil.get("profile"))) {
                    rand = String.valueOf(Math.random()).substring(2).substring(0, 6);
                }
                String content = "";
                // 被注销的账户
                if (user == null || user.getStatus().equals(User.USER_STATUS_THREE)) {
                    if ("1".equals(jedisCluster.get("app_register_status"))) {
                        msg = "服务器正在维护中，请稍后重试";
                        return;
                    }
                    Long remainTime = checkForFront(generateRegisterCode, userPhone);
                    if (remainTime > 0) {
                        code = Status.FREQUENT.getName();
                        json.put("time", remainTime);
                        msg = Status.FREQUENT.getValue();
                        return;
                    }
                    ResponseContent serviceResult = check(userPhone);
                    if (serviceResult.isFail()) {
                        msg = serviceResult.getMsg();
                    } else {
                        // 存入redis
                        jedisCluster.set(SMS_REGISTER_PREFIX + userPhone, rand);
                        jedisCluster.expire(SMS_REGISTER_PREFIX + userPhone, INFECTIVE_SMS_TIME);
                        content = rand + "##正在注册" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户";
                        code = "0";
                        msg = "成功获取验证码";
                        Date sendTime = new Date();
                        // 手机号
                        message.setPhone(userPhone);
                        message.setMessageCreateTime(sendTime);
                        message.setMessageContent(content);
                        // 发送短信的ip
                        message.setSendIp(this.getIpAddr(request));
                        try {
                            SendSmsUtil.sendSmsCL(userPhone, content);
                            log.info("短信发送是否成功=" + code + "***" + msg);
                            msg = "成功获取验证码";
                        } catch (Exception e) {
                            log.error("sendSmsCode error:", e);
                            code = "-1";
                            msg = "信息发送失败稍后重试";
                        }
                        // 发送成功
                        message.setMessageStatus(UserSendMessage.STATUS_SUCCESS);
                        // 添加短息记录
                        userSendMessageService.saveUserSendMsg(message);
                        log.info("注册验证码sendSms:" + userPhone + "-->" + rand);
                    }
                } else {
                    // 正常用户登录
                    if (user.getStatus().equals(User.USER_STATUS_WHITE)) {
                        code = "1000";
                        msg = "请输入您的登录密码";
                        // 黑名单用户
                    } else if (user.getStatus().equals(User.USER_STATUS_BLACK)) {
                        msg = "黑名单用户禁止登录";
                    }
                }
            } catch (Exception e) {
                code = "500";
                msg = "系统异常";
                log.error("Send sms error!", e);
            } finally {
                delCheckForFront(generateRegisterCode, userPhone);
                dataMap.put("item", resultMap);
                json.put("code", code);
                json.put("message", msg);
                json.put("data", dataMap);
                JSONUtil.toObjectJson(response, json.toString());
            }
        } else {
            sendSmsCodeV211(request, generateRegisterCode, response);
        }
    }

    /**
     * 注册 生成手机认证码,没有版本管理该方法在2.2.0版本之前用
     *
     * @param request req
     * @param codeCheck code
     * @param response res
     */
    private void sendSmsCodeV211(HttpServletRequest request, String codeCheck, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        String code = "-1";
        String msg = "";
        UserSendMessage message = new UserSendMessage();
        String userPhone = "";
        String captchaUrl = "";
        //0为进入自动发送验证码，1为点击获取验证码
        String type = "";
        String captchaKey = "";
        String userIp = "";
        try {
            Map<String, Object> param = getParametersO(request);
            userPhone = null == param.get("phone") ? "" : param.get("phone").toString();
            captchaKey = null == param.get("RCaptchaKey") ? "R" + request.getSession().getId() : param.get("RCaptchaKey").toString();
            type = null != param.get("type") && "0".equals(param.get("type").toString()) ? "0" : "1";
            userIp = RequestUtils.getIpAddr();
            log.info("regest:phone=" + userPhone + " regip:" + userIp);
            map.put("userPhone", userPhone);
            if (StringUtils.isBlank(userPhone)) {
                msg = "请输入手机号码";
                return;
            }
            // 查询手机号码是否存在
            User user = userService.searchUserByCheckTel(map);
            // 正常用户登录
            if (null != user) {
                code = "1000";
                msg = "请输入您的登录密码";
                return;
            }

            captchaUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/captcha.svl?RCaptchaKey=" + captchaKey;
            if ("0".equals(type)) {
                code = "0";
                msg = "请求成功";
                return;
            }

            if (!validateSubmitAPP(request, response)) {
                msg = "图形验证码错误";
                return;
            }

            log.info("sendSmsCodeV211 phone=" + userPhone + " type=" + type);

            // 6位固定长度
            String rand = "123456";
            if ("online".equals(PropertiesConfigUtil.get("profile"))) {
                rand = String.valueOf(Math.random()).substring(2).substring(0, 6);
            }
            String content = "";
            // 被注销的账户
            Long remainTime = checkForFront(codeCheck, userPhone);
            if (remainTime > 0) {

                code = Status.FREQUENT.getName();
                json.put("time", remainTime);
                msg = Status.FREQUENT.getValue();
                return;
            }
            ResponseContent serviceResult = check(userPhone);
            log.info("sendSmsCodeV211 phone=" + userPhone + " serviceResult=" + JSON.toJSONString(serviceResult));
            if (serviceResult.isFail()) {
                msg = serviceResult.getMsg();
            } else {
                code = "0";
                // 存入redis
                jedisCluster.set(SMS_REGISTER_PREFIX + userPhone, rand);
                jedisCluster.expire(SMS_REGISTER_PREFIX + userPhone, INFECTIVE_SMS_TIME);

                content = rand + "##正在注册" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户";
                msg = "成功获取验证码";
                Date sendTime = new Date();
                // 手机号
                message.setPhone(userPhone);
                message.setMessageCreateTime(sendTime);
                message.setMessageContent(content);
                // 发送短信的ip
                message.setSendIp(this.getIpAddr(request));
                try {
                    SendSmsUtil.sendSmsCL(userPhone, content);
                    log.info("短信发送是否成功=" + code + "***" + msg);
                    msg = "成功获取验证码";
                } catch (Exception e) {
                    log.error("error", e);
                    code = "-1";
                    msg = "信息发送失败稍后重试";
                }
                // 发送成功
                message.setMessageStatus(UserSendMessage.STATUS_SUCCESS);
                // 添加短息记录
                userSendMessageService.saveUserSendMsg(message);
                log.info("注册验证码sendSms:" + userPhone + "-->" + rand);
            }
        } catch (Exception e) {
            log.error("sendSmsCodeV211 phone=" + userPhone + " error=", e);
            code = "500";
            msg = "系统异常";
        } finally {
            resultMap.put("captchaUrl", captchaUrl);
            resultMap.put("RCaptchaKey", captchaKey);
            delCheckForFront(codeCheck, userPhone);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 找回密码 生成手机认证码
     * @param request req
     * @param response res
     */
    @RequestMapping("credit-user/reset-pwd-code")
    public void resetPwdCode(HttpServletRequest request, HttpServletResponse response) {
        // 生成找回手机认证码频繁标识key
        String checkFindRegisterCode = "check_find_register_code_";
        String clientType = request.getParameter("clientType");
        if ( "ios".equals(clientType)) {
            JSONObject json = new JSONObject();
            Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
            HashMap<String, Object> resultMap = new HashMap<>();
            HashMap<String, Object> map = new HashMap<>();
            String code = "-1";
            String msg = "";
            String userPhone = "";
            String captchaUrl = "";
            String type2 = "";
            UserSendMessage message = new UserSendMessage();
            try {
                if ("1".equals(jedisCluster.get("app_pass_status"))) {
                    msg = "服务器正在维护中，请稍后重试";
                    return;
                }
                Map<String, Object> param = getParametersO(request);
                userPhone = param.get("phone").toString();
                type2 = null == param.get("type2") ? "" : param.get("type2").toString();
                if (StringUtils.isBlank(userPhone)) {
                    msg = "请输入手机号码";
                    return;
                }

                String equipmentNumber = param.get("deviceId") == null ? "" : param.get("deviceId").toString();
                //获取redis用户第一次操作时间
                String registerUpdateTime = jedisCluster.get(BACK_PASS_UPDATE_TIME + equipmentNumber);
                //获取redis用户注册验证码输错次数
                String errorCount = jedisCluster.get(BACK_PASS_COUNT + equipmentNumber);
                addErrorCount(errorCount, equipmentNumber, BACK_PASS_COUNT);
                if (StringUtils.isNotBlank(registerUpdateTime)) {
                    Date date = new Date();
                    Date date2 = DateUtil.dateFormat(registerUpdateTime, "yyyy-MM-dd HH:mm:ss");
                    //2次操作相隔时间大于60秒重置操作时间和操作错误次数
                    if ((date.getTime() - date2.getTime()) / 1000 > ERROR_TIME) {
                        //将第一次操作时间存入redis
                        jedisCluster.set(BACK_PASS_UPDATE_TIME + equipmentNumber, DateUtil.formatDateNow("yyyy-MM-dd HH:mm:ss"));
                        if ("0".equals(type2)) {
                            jedisCluster.del(BACK_PASS_COUNT + equipmentNumber);
                            errorCount = jedisCluster.get(BACK_PASS_COUNT + equipmentNumber);
                            addErrorCount(errorCount, equipmentNumber, BACK_PASS_COUNT);
                        }
                    }
                } else {
                    //将第一次操作时间存入redis
                    jedisCluster.set(BACK_PASS_UPDATE_TIME + equipmentNumber, DateUtil.formatDateNow("yyyy-MM-dd HH:mm:ss"));
                }
                boolean isSend = true;
                if (StringUtils.isNotBlank(errorCount) && Integer.parseInt(errorCount) >= 3) {
                    captchaUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/captcha.svl?RCaptchaKey=R" + request.getSession().getId();
                    if ("1".equals(type2)) {
                        if (!validateSubmitAPP(request, response)) {
                            msg = "图形验证码错误";
                            return;
                        }
                    } else {
                        code = "0";
                        isSend = false;
                    }
                }

                map.put("userPhone", userPhone);
                // 查询手机号码是否存在
                User user = userService.searchUserByCheckTel(map);
                if (null == user) {
                    code = "-1";
                    msg = "请输入手机号码";
                    return;
                }
                ResponseContent serviceResult = check(userPhone);
                if (serviceResult.isFail()) {
                    code = "-1";
                    msg = serviceResult.getMsg();
                } else {
                    if (isSend) {
                        String rand = "123456";
                        if ("online".equals(PropertiesConfigUtil.get("profile"))) {
                            rand = String.valueOf(Math.random()).substring(2).substring(0, 6);// 6位固定长度
                        }
                        //String rand = String.valueOf(Math.random()).substring(2).substring(0, 6);
                        String content = "";
                        String messageTitle = "手机短信认证";
                        // 登录密码找回验证码
                        if (param.containsKey("type") && "find_pwd".equals(param.get("type"))) {
                            // 存入redis
                            jedisCluster.set(SMS_FIND_PREFIX + userPhone, rand);
                            jedisCluster.expire(SMS_FIND_PREFIX + userPhone, INFECTIVE_SMS_TIME);

                            content = rand + "##正在找回" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户登录密码";
                            code = "0";
                            msg = "发送验证码成功";
                            // 接收的用户
                            message.setPhone(userPhone);
                            // 交易密码找回验证码
                        } else if (param.containsKey("type") && "find_pay_pwd".equals(param.get("type"))) {
                            // 存入redis
                            jedisCluster.set(SMS_TRADE_FIND_PREFIX + userPhone, rand);
                            jedisCluster.expire(SMS_TRADE_FIND_PREFIX + userPhone, INFECTIVE_SMS_TIME);

                            content = rand + "##正在找回" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户交易密码";
                            code = "0";
                            msg = "发送验证码成功";
                            // 接收的用户
                            message.setPhone(userPhone);
                        }
                        Date sendTime = new Date();
                        message.setPhone(userPhone);// 手机号
                        message.setMessageCreateTime(sendTime);
                        message.setMessageContent(content);
                        message.setSendIp(this.getIpAddr(request));
                        try {
                            SendSmsUtil.sendSmsCL(userPhone, content);
                            msg = "发送验证码成功";
                            resultMap.put("item", "1");
                        } catch (Exception e) {
                            log.error("resetPwdCode error:", e);
                            code = "-1";
                            msg = "信息发送失败稍后重试";
                        }
                        message.setMessageStatus(UserSendMessage.STATUS_SUCCESS);
                        userSendMessageService.saveUserSendMsg(message);
                        log.info("找回密码sendSms:" + userPhone + "-->" + rand);
                    }
                }
            } catch (Exception e) {
                log.error("resetPwdCode error:", e);
                code = "500";
                msg = "系统异常";
            } finally {
                delCheckForFront(checkFindRegisterCode, userPhone);
                resultMap.put("captchaUrl", captchaUrl);
                dataMap.put("item", resultMap);
                json.put("code", code);
                json.put("message", msg);
                json.put("data", dataMap);
                JSONUtil.toObjectJson(response, json.toString());
                log.info("返回参数:" + dataMap);
            }
        } else {
            resetPwdCodeV211(request, checkFindRegisterCode, response);
        }
    }

    /**
     * 找回密码 生成手机认证码,没有版本管理该方法在2.2.0版本之前用
     *
     * @param request req
     * @param findCodeCheck code
     * @param response res
     */
    private void resetPwdCodeV211(HttpServletRequest request, String findCodeCheck, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        HashMap<String, Object> resultMap = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();
        String code = "-1";
        String msg = "";
        String userPhone = "";
        String captchaUrl = "";
        //0为进入自动发送验证码，1为点击获取验证码
        String type2 = "";
        String captchaKey = "";
        UserSendMessage message = new UserSendMessage();
        try {
            Map<String, Object> param = getParametersO(request);
            userPhone = param.get("phone").toString();
            captchaKey = null == param.get("RCaptchaKey") ? "R" + request.getSession().getId() : param.get("RCaptchaKey").toString();
            type2 = null != param.get("type2") && "0".equals(param.get("type2").toString()) ? "0" : "1";
            if (StringUtils.isBlank(userPhone)) {
                msg = "请输入手机号码";
                return;
            }

            log.info("resetPwdCodeV211 phone=" + userPhone + " RCaptchaKey=" + captchaKey);
            captchaUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/captcha.svl?RCaptchaKey=" + captchaKey;

            log.info("resetPwdCodeV211 phone=" + userPhone + " type=" + param.get("type") + " type2=" + type2);
            if ("0".equals(type2)) {
                code = "0";
                msg = "请求成功";
                return;
            }
            if (!validateSubmitAPP(request, response)) {
                msg = "图形验证码错误";
                return;
            }

            Long remainTime = checkForFront(findCodeCheck, userPhone, 60);
            if (remainTime > 0) {
                code = Status.FREQUENT.getName();
                json.put("time", remainTime);
                msg = Status.FREQUENT.getValue();
                return;
            }

            map.put("userPhone", userPhone);
            // 查询手机号码是否存在
            User user = userService.searchUserByCheckTel(map);
            if (null == user) {
                msg = "请输入手机号码";
                return;
            }

            ResponseContent serviceResult = check(userPhone);
            log.info("resetPwdCodeV211 phone=" + userPhone + " serviceResult=" + JSON.toJSONString(serviceResult));
            if (serviceResult.isFail()) {
                msg = serviceResult.getMsg();
            } else {
                code = "0";
                // 6位固定长度
                String rand = "123456";
                if ("online".equals(PropertiesConfigUtil.get("profile"))) {
                    rand = String.valueOf(Math.random()).substring(2).substring(0, 6);// 6位固定长度
                }
                String content = "";
                // 登录密码找回验证码
                if (param.containsKey("type") && "find_pwd".equals(param.get("type"))) {
                    // 存入redis
                    jedisCluster.set(SMS_FIND_PREFIX + userPhone, rand);
                    jedisCluster.expire(SMS_FIND_PREFIX + userPhone, INFECTIVE_SMS_TIME);

                    content = rand + "##正在找回" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户登录密码";
                    code = "0";
                    msg = "发送验证码成功";
                    // 接收的用户
                    message.setPhone(userPhone);
                    // 交易密码找回验证码
                } else if (param.containsKey("type") && "find_pay_pwd".equals(param.get("type"))) {
                    // 存入redis
                    jedisCluster.set(SMS_TRADE_FIND_PREFIX + userPhone, rand);
                    jedisCluster.expire(SMS_TRADE_FIND_PREFIX + userPhone, INFECTIVE_SMS_TIME);

                    content = rand + "##正在找回" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户交易密码";
                    code = "0";
                    msg = "发送验证码成功";
                    message.setPhone(userPhone);
                }
                Date sendTime = new Date();
                message.setPhone(userPhone);
                message.setMessageCreateTime(sendTime);
                message.setMessageContent(content);
                message.setSendIp(this.getIpAddr(request));
                try {
                    SendSmsUtil.sendSmsCL(userPhone, content);
                    msg = "发送验证码成功";
                    resultMap.put("item", "1");
                } catch (Exception e) {
                    log.error("resetPwdCodeV211 error:", e);
                    code = "-1";
                    msg = "信息发送失败稍后重试";
                }
                // 发送成功 2
                message.setMessageStatus(UserSendMessage.STATUS_SUCCESS);
                // 添加短息记录
                userSendMessageService.saveUserSendMsg(message);
                log.info("找回密码sendSms:" + userPhone + "-->" + rand);
            }
        } catch (Exception e) {
            log.error("resetPwdCodeV211 error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            resultMap.put("captchaUrl", captchaUrl);
            resultMap.put("RCaptchaKey", captchaKey);
            delCheckForFront(findCodeCheck, userPhone);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", resultMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /***
     * 用户注册
     *
     */
    @RequestMapping("credit-user/registers")
    public void registers() {
        String key = jedisCluster.get("asasas");
        if (StringUtils.isNotBlank(key)) {
            int i = Integer.parseInt(key);
            i++;
            jedisCluster.set("asasas", String.valueOf(i));
        } else {
            jedisCluster.set("asasas", "1");
        }
        System.out.println(key);
    }

    /***
     * 用户注册
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("credit-user/register")
    public void register(HttpServletRequest request, HttpServletResponse response) {
        // 注册验证是否重复提交
        String registerCheck = "register_check_";
        registerV211(request, registerCheck, response);
    }

    /**
     * 用户注册 ,没有版本管理该方法在2.2.0版本之前用
     *
     * @param request req
     * @param registerUserCheck code
     * @param response res
     */
    private void registerV211(HttpServletRequest request, String registerUserCheck, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        String msg = "";
        String code = "-1";
        String userPhone = "";
        try {
            userPhone = request.getParameter("phone");
            // 获取密码
            String passWord = request.getParameter("password");
            // 获取手机验证码
            String smsCode = request.getParameter("code");
            // 验证码
            String inviteUserid = request.getParameter("invite_code");
            // 验证码
            String userFrom = request.getParameter("user_from");
            // APP名称
            String appName = request.getParameter("appName");
            String clientType = request.getParameter("clientType");
            // 手机验证
            if (StringUtils.isBlank(userPhone)) {
                msg = "手机号不能为空。";
                return;
            }

            // 手机验证码验证
            if (StringUtils.isBlank(smsCode)) {
                msg = "手机验证码不能为空。";
                return;
            }

            userPhone = userPhone.trim();
            Long remainTime = checkForFront(registerUserCheck, userPhone);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }

            String sendSmsCode = jedisCluster.get(SMS_REGISTER_PREFIX + userPhone);
            if ("".equals(smsCode)) {
                msg = "验证码不能为空";
                return;
            } else if (StringUtils.isBlank(sendSmsCode)) {
                msg = "验证码已失效，请重新获取 ";
                return;
            } else if (!sendSmsCode.equals(smsCode)) {
                msg = "验证码校验失败";
                return;
            }
            User invitetUser;
            User user = new User();
            String Md5 = MD5Util.MD5(AESUtil.encrypt(passWord, ""));
            String equipmentNumber = request.getParameter("deviceId");
            if (passWord != null) {
                user.setUserName(userPhone);
                // 加密后的登录密码
                user.setPassword(Md5);
                // 用户手机号码
                user.setUserPhone(userPhone);
                // 注册时的IP地址
                user.setCreateIp(this.getIpAddr(request));
                // 设备号
                user.setEquipmentNumber(equipmentNumber);
                user.setUserFrom("0");

                // 除了极速现金侠的来源，其他的APP版本注册都保存用户来源
                Integer channelId = null;
                if (StringUtils.isNotBlank(appName) && !"jsxjx".equals(appName)) {
                    channelId = channelInfoService.findChannelIdByCode(appName);
                    if (null != channelId) {
                        user.setUserFrom(channelId.toString());
                    }
                }
                // 邀请注册时设置邀请人Id
                if (StringUtils.isNotBlank(inviteUserid)) {
                    String decodeId = new String(Base64.getDecoder().decode(inviteUserid.getBytes()));
                    // 邀请码的验证
                    Map<String, String> maps = new HashMap<>();
                    maps.put("id", decodeId);
                    invitetUser = this.userService.searchByInviteUserid(maps);
                    if (invitetUser == null) {
                        msg = "该邀请人不存在";
                        return;
                    }
                    // 邀请好友注册的ID
                    user.setInviteUserid(decodeId);
                } else if (null != channelId) {
                    Integer userI = channelInfoService.findUserIdByChannelId(channelId);
                    if (null != userI) {
                        // 邀请好友注册的ID
                        user.setInviteUserid(userI.toString());
                    }
                }

                /*用户来源,用于apk单独统计  2017-03-10*/
                if (StringUtils.isNotBlank(userFrom)) {
                    user.setUserFrom(userFrom);
                }
                HashMap<String, Object> queryParams = new HashMap<>();
                // 手机号码
                queryParams.put("userPhone", userPhone);
                // 查询手机号码是否存在
                User checkUser = userService.searchUserByCheckTel(queryParams);
                if (null != checkUser) {
                    msg = "手机号已经存在";
                    return;
                }

                int client_type = 1;
                int browerType = 3;
                //android终端
                if ("android".equals(clientType)) {
                    client_type = 1;
                    browerType = 1;
                    //ios终端
                } else if ("ios".equals(clientType)) {
                    client_type = 2;
                    browerType = 2;
                    //wap终端
                } else if ("wap".equals(clientType)) {
                    client_type = 3;
                    //pc终端
                } else if ("pc".equals(clientType)) {
                    client_type = 4;
                }
                // 客户端类型（1、Android 2、ios 3、wap、4、pc）
                user.setClientType(client_type);
                // 浏览器类型（1、Android 2、ios 3、pc）
                user.setBrowerType(browerType);
                // 注册保存新用户
                userService.saveUser(user);
                // TODO 情况1：注册成功，注入



                loginSucc(request, user);
                msg = "注册成功";
                code = "0";
                User userNew = this.userService.searchByUserid(Integer.parseInt(user.getId()));
                resultMap.put("uid", userNew.getId());
                resultMap.put("username", userNew.getUserName());
                String realname = "";
                if (userNew.getRealname() != null) {
                    realname = userNew.getRealname();
                }
                String realVerifyStatus = "0";
                if (userNew.getRealnameStatus() != null) {
                    realVerifyStatus = userNew.getRealnameStatus();
                }
                String idCard = "";
                if (userNew.getIdNumber() != null) {
                    idCard = userNew.getIdNumber();
                }
                resultMap.put("realname", realname);
                resultMap.put("real_verify_status", realVerifyStatus);
                resultMap.put("id_card", idCard);
                resultMap.put("user_sign", Base64.getEncoder().encodeToString(userNew.getId().getBytes()));
                // app用户session
                HttpSession session = request.getSession();
                resultMap.put("sessionid", session.getId());
            } else {
                msg = "请输入6-20位密码";
            }
        } catch (Exception e) {
            code = "500";
            msg = "系统异常，请稍后再试";
            log.error("registerV211 error:", e);
        } finally {
            delCheckForFront(registerUserCheck, userPhone);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * goto登录
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("credit-user/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        // 登录验证是否重复提交
        String loginUserCheck = "login_check_";
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        String code = "-1";
        String msg = "";
        HttpSession session = request.getSession();
        String equipmentNumber = "";
        try {
            HashMap<String, Object> param = this.getParametersO(request);
            // 设备号
            equipmentNumber = request.getParameter("deviceId");
            if (!param.containsKey("username")) {
                msg = "用户名不能为空";
                return;
            }
            if (!param.containsKey("password")) {
                msg = "密码不能为空";
                return;
            }
            String username = param.get("username").toString();
            String password = param.get("password").toString();
            // 加密
            String checkPassWord = MD5Util.MD5(AESUtil.encrypt(password, ""));
            param.put("username", username);
            param.put("password", checkPassWord);
            // 登录
            User user = userService.searchUserByLogin(param);
            if (null != user) {
                // 正常用户登录
                if (User.USER_STATUS_WHITE.equals(user.getStatus())) {
                    if (checkPassWord.equals(user.getPassword())) {
                        loginSucc(request, user);
                        // 登录成功
                        code = "0";
                        msg = "登录成功";
                        resultMap.put("uid", user.getId());
                        resultMap.put("username", user.getUserName());
                        String realname = "";
                        if (user.getRealname() != null) {
                            realname = user.getRealname();
                        }
                        resultMap.put("realname", realname);
                        String generateToken = TokenUtils.generateToken(user);
                        resultMap.put("token", generateToken);
                        resultMap.put("sessionid", session.getId());
                        Map<String, String> params = new HashMap<>();
                        params.put("deviceId", equipmentNumber);
                        params.put("userName", user.getUserName());
                        params.put("userId", String.valueOf(user.getId()));
                        // 查询是否有登录记录
                        UserLoginLog loginLog = this.userLoginLogService.selectByParams(params);
                        if (loginLog == null) {
                            loginLog = new UserLoginLog();
                            loginLog.setUserId(user.getId());
                            loginLog.setUserName(user.getUserName());
                            loginLog.setPassword(user.getPassword());
                            loginLog.setLoginTime(new Date());
                            loginLog.setLoginIp(this.getIpAddr(request));
                            Date effTime = DateUtil.addDay(new Date(), 3);
                            loginLog.setEffTime(effTime);
                            loginLog.setToken(generateToken);
                            loginLog.setEquipmentNumber(equipmentNumber);
                            userLoginLogService.saveUserLoginLog(loginLog);
                        } else {
                            loginLog.setUserName(user.getUserName());
                            loginLog.setPassword(user.getPassword());
                            loginLog.setLoginTime(new Date());
                            loginLog.setLoginIp(this.getIpAddr(request));
                            Date effTime = DateUtil.addDay(new Date(), 3);
                            loginLog.setEffTime(effTime);
                            loginLog.setToken(generateToken);
                            loginLog.setEquipmentNumber(equipmentNumber);
                            userLoginLogService.updateUserLoginLog(loginLog);
                        }

                        log.info("login user_id=" + user.getId() + " userLogin=" + JSON.toJSONString(loginLog));
                    } else if (user.getStatus().equals(User.USER_STATUS_BLACK)) {
                        msg = "密码不正确，请重新输入。";
                    }
                } else {
                    msg = "黑名单用户禁止登录";
                }
            } else {
                msg = "你输入的用户或密码不正确，请重新输入。";
            }
        } catch (Exception e) {
            log.error("registerV211 error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(loginUserCheck, equipmentNumber);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 将用户输错密码次数放入redis
     * @param errorCount c
     * @param equipmentNumber n
     * @param key k
     */
    private void addErrorCount(String errorCount, String equipmentNumber, String key) {
        if (StringUtils.isNotBlank(errorCount)) {
            int i = Integer.parseInt(errorCount);
            i++;
            jedisCluster.set(key + equipmentNumber, String.valueOf(i));
        } else {
            jedisCluster.set(key + equipmentNumber, "1");
        }
    }

    /***
     * 用户界面
     */
    @RequestMapping("credit-user/get-info")
    public void userInfoIndex(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        Map<String, Object> verifyInfoMap = new HashMap<>();
        String telephone = request.getParameter("mobilePhone");
        String code = "-1";
        String msg = "获取失败";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                code = "0";
                msg = "获取成功";
                // 信用额度
                resultMap.put("userId", user.getId());
                String phone = user.getUserName() == null ? "" : user.getUserName();
                if (phone.length() > 4) {
                    phone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
                }
                resultMap.put("phone", phone);
                String inviteCode = Base64.getEncoder().encodeToString(user.getId().getBytes());
                String invite = inviteCode.replaceAll("=", "");
                resultMap.put("invite_code", invite);
                String realNameStatus = user.getRealnameStatus();
                verifyInfoMap.put("real_verify_status", realNameStatus);

                String payPasswordStatus = "0";
                if (user.getPayPassword() != null) {
                    payPasswordStatus = "1";
                }
                verifyInfoMap.put("real_pay_pwd_status", payPasswordStatus);// 用户交易密码状态
                String bankStatus = "0";
                String bankUrl = "";// 未绑定跳转到银行卡页面路径

                resultMap.put("userId", user.getId());// 用户

                UserCardInfo cardInfo = userService.findUserBankCard(Integer.parseInt(user.getId()));
                HashMap<String, String> cardMap = new HashMap<String, String>();
                String card_amount = user.getAmountMax();
                String card_unused_amount = user.getAmountAvailable();
                cardMap.put("card_amount", card_amount);// 总额度
                cardMap.put("card_unused_amount", card_unused_amount);// 剩余额度
                String cardName = "";
                String cardEndNO = "";
                Map<String, Object> map = new HashMap<String, Object>();
                if (cardInfo != null) {
                    // cardMap.put("card_no", cardInfo.getCard_no());//银行卡
                    String card = cardInfo.getCard_no();
                    cardEndNO = card.substring(card.length() - 4, card.length());// 银行卡号码
                    cardName = cardInfo.getBankName();// 银行名称
                    bankStatus = "1";
                }
                map.put("card_no_end", cardEndNO);
                map.put("bank_name", cardName);
                verifyInfoMap.put("real_bind_bank_card_status", bankStatus);// 用户银行卡状态

                //获取用户最近借款状态 代替风控
                // 如果最近借款被拒绝, 那么就显示问号, 跳转去其他的app
                // 0 不显示 1显示
                cardMap.put("risk_status", "0");
                cardMap.put("shop_url", PropertiesConfigUtil.get("SHOP_URL"));
                BorrowOrder bo = borrowOrderService.selectBorrowOrderNowUseId(Integer.valueOf(user.getId()));
                if (bo != null) {

                    //借款审核被拒绝 添加全局控制开关 1 打开 0 关闭
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("sysType", "SYS_NOCACHE");
                    params.put("syskey", "SYJ_SWITCH");
                    //params.put("sysName","");
                    List<BackConfigParams> list = backConfigParamsService.findParams(params);
                    String offon = "1";
                    if (list.size() == 1) {
                        offon = list.get(0).getSysValue();
                    }

                    //借款审核被拒绝
                    if ((borrowStatusMap_shenheFail.containsKey(bo.getStatus()))
                            && "1".equals(offon)) {
                        cardMap.put("shop_url", PropertiesConfigUtil.get("SHOP_URL_FK"));
                        cardMap.put("risk_status", "1");
                    }
                    log.info("bo.getStatus1=" + bo.getStatus());
                    //逾期情况下显示
                    if (BorrowOrder.STATUS_YYQ.equals(bo.getStatus())) {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("assetOrderId", bo.getId());
                        param.put("userId", bo.getUserId());
                        log.info("param = " + JSON.toJSONString(param));
                        Repayment repayment = repaymentService.findOneRepayment(param);

                        Integer overdue_days = Integer.parseInt(PropertiesConfigUtil.get("SHOP_URL_OVERDUE_DAYS"));
                        log.info("overdue_days=" + overdue_days + " lateDays=" + repayment.getLateDay() + " flag=" + (repayment.getLateDay() >= overdue_days));
                        if (null != repayment && "1".equals(offon) && BorrowOrder.STATUS_YYQ.equals(repayment.getStatus()) && repayment.getLateDay() >= overdue_days) {
                            cardMap.put("shop_url", PropertiesConfigUtil.get("SHOP_URL_YQ"));
                            cardMap.put("risk_status", "1");
                        }
                    }
                }

                resultMap.put("credit_info", cardMap);// 额度信息
                resultMap.put("card_info", map);// 银行卡信息

                bankUrl = PropertiesConfigUtil.get("APP_HOST_API") + "/";// 已绑定跳转银行卡页面路径
                if (null != telephone) {
//					resultMap.put("card_url", bankUrl + "lianlianBindCard/credit-card/firstUserBank");// 银行卡页面
                    resultMap.put("card_url", bankUrl + PropertiesConfigUtil.get("BIND_CARD_URL") + "/credit-card/firstUserBank");// 银行卡页面
                } else {
                    resultMap.put("card_url", bankUrl + "credit-card/card-list");// 银行卡页面
                }
                resultMap.put("verify_info", verifyInfoMap);// 认证状态

                //String path = "/common/web/images/logo_share_xjx.png";
                String path = getAppConfig(request.getParameter("appName"), "XJX_LOGO");  //分享logo地址
                // 分享链接
                resultMap.put("share_body", "2分钟认证，3分钟放款，无抵押，纯信用贷。时下最流行的移动贷款APP。国内首批利用大数据、人工智能实现风控审批的信贷服务平台。");
                resultMap.put("share_logo", bankUrl + path);
                resultMap.put("share_title", getAppConfig(request.getParameter("appName"), "APP_TITLE"));
                String user_from = "0";
                if (StringUtils.isNotBlank(user.getUserFrom()) && !"null".equals(user.getUserFrom())) {
                    user_from = user.getUserFrom();
                }
                String share_url = bankUrl + "act/little-fish-register?invite_code=" + invite + "&user_from=" + user_from;
                if (StringUtils.isNotBlank(request.getParameter("appName"))) {
                    share_url += "&appName=" + request.getParameter("appName");
                }
                resultMap.put("share_url", share_url);


                //获取网站配置数据
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("sysType", "WEBSITE");
                List<BackConfigParams> list = backConfigParamsService.findParams(params);

                // zhangsh 2017年3月6日 动态客服数据
                Map<String, Object> serviceMap = new HashMap<String, Object>();
                Map<String, String> intervalMap = getBackConfigParamsMap(list);
                serviceMap.put("qq_group", intervalMap.get("services_qq"));
                List<String> servicesQQ = new ArrayList<String>();
                servicesQQ.add(intervalMap.get("services_qq"));
                serviceMap.put("services_qq", servicesQQ);
                serviceMap.put("service_phone", intervalMap.get("service_phone"));
                serviceMap.put("peacetime", intervalMap.get("peacetime"));
                serviceMap.put("holiday", intervalMap.get("holiday"));

                resultMap.put("service", serviceMap);
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
            }
        } catch (Exception e) {
            log.error("userInfoIndex error phone=" + telephone, e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 修改登录密码
     */
    @RequestMapping("credit-user/change-pwd")
    public void loginUpdatePassWord(HttpServletRequest request, HttpServletResponse response) {
        String EDIT_PASSWD_CHECK = "edit_passwd_check_";
        JSONObject json = new JSONObject();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String code = "-1";
        String msg = "";

        AESUtil desEncrpt = new AESUtil();
        AESUtil aesEncrypt = new AESUtil();

        String flage = "false";
        String deviceId = request.getParameter("deviceId");
        try {

            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                String passWordOld1 = request.getParameter("old_pwd");// 原密码
                String passWordNew1 = request.getParameter("new_pwd");// 新密码
                String checkPassWordOld = MD5Util.MD5(AESUtil.encrypt(passWordOld1, ""));// 加密

                if (user.getPassword().equals(checkPassWordOld)) {// 判断输入的密码是否与原密码一致
                    String checkPassWord = MD5Util.MD5(AESUtil.encrypt(passWordNew1, ""));// 加密
                    User userNew = new User();
                    userNew.setId(user.getId());
                    userNew.setPassword(checkPassWord);// 新密码
                    int count = userService.updateByPrimaryKeyUser(userNew);
                    if (count > 0) {
                        code = "0";
                        msg = "修改成功";
                        flage = "true";
                        userNew.setUserName(user.getUserName());
                        loginSucc(request, userNew);
                    } else {
                        code = "-1";
                        msg = "修改失败";
                        return;
                    }
                } else {
                    code = "-1";
                    msg = "原密码错误";
                    return;

                }
            } else {
                code = "-2";
                msg = "请先登录";
                return;
            }
        } catch (Exception e) {
            log.error("loginUpdatePassWord error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(EDIT_PASSWD_CHECK, deviceId);
            resultMap.put("item", flage);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", resultMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 修改密码 验证码是否通过
     */
    @RequestMapping("credit-user/verify-reset-password")
    public void loginForgotUpdatePassWordOne(HttpServletRequest request, HttpServletResponse response) {
        String appVersion = request.getParameter("appVersion");
//		String[] str = appVersion.split("\\.");
//		String v = "";
//		if (str[0].toLowerCase().indexOf("V")!=-1) {
//			v = str[0].substring(1, str[0].length()-1);
//		}else{
//			v = str[0];
//		}
//		int v2 = Integer.parseInt(v+str[1]+str[2]);
        if (false) {
            JSONObject json = new JSONObject();
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            Map<String, Object> param = getParametersO(request);
            HashMap<String, Object> map = new HashMap<String, Object>();

            String code = "-1";
            String msg = "";
            String item = "";
            try {
                String userPhone = request.getParameter("phone");// 手机号码
                String smsCode = request.getParameter("code");// 验证码
                map.put("userPhone", userPhone);
                User user = this.userService.searchUserByCheckTel(map);// 根据手机号查询
                if (user != null) {
                    if (param.containsKey("type") && "find_pwd".equals(param.get("type"))) {// 登录密码找回
                        String deviceId = request.getParameter("deviceId");
                        String errorCount = jedisCluster.get(BACK_PASS_COUNT + deviceId); //获取redis用户注册验证码输错次数
                        if (StringUtils.isNotBlank(errorCount) && Integer.parseInt(errorCount) >= 3) {
                            if (!validateSubmitAPP(request, response)) {
                                msg = "图形验证码错误";
                                return;
                            }
                        }
                        String sendSmsCode = jedisCluster.get(SMS_FIND_PREFIX + userPhone);
                        if ("".equals(smsCode)) {
                            msg = "验证码不能为空";
                        } else if (StringUtils.isBlank(sendSmsCode)) {
                            msg = "验证码已失效，请重新获取";
                        } else if (!sendSmsCode.equals(smsCode)) {
                            msg = "验证码不正确";
                        } else if (sendSmsCode.equals(smsCode)) {
                            jedisCluster.del(BACK_PASS_COUNT + deviceId); //清空图形验证码输错次数
                            code = "0";
                            msg = "成功找回密码";
                            item = "1";
                            resultMap.put("item", item);
                        }
                    } else if (param.containsKey("type") && "find_pay_pwd".equals(param.get("type"))) {// 交易密码找回

                        String sendSmsCode = jedisCluster.get(SMS_TRADE_FIND_PREFIX + userPhone);

                        String name = param.get("realname") + "";// 真实姓名
                        String id_number = param.get("id_card") + "";// 身份证号码

                        if (user.getRealname().equals(name)) {
                            if (user.getIdNumber().equals(id_number)) {
                                if (StringUtils.isBlank(sendSmsCode)) {
                                    msg = "验证码已失效，请重新获取";
                                } else if ("".equals(smsCode)) {
                                    msg = "验证码不能为空";
                                } else if (!sendSmsCode.equals(smsCode)) {
                                    msg = "验证码不正确";
                                } else if (sendSmsCode.equals(smsCode)) {
                                    code = "0";
                                    msg = "成功找回密码";
                                    item = "1";
                                    resultMap.put("item", item);
                                    resultMap.put("code", smsCode);
                                }
                            } else {
                                code = "-1";
                                msg = "身份证号码不正确";
                            }
                        } else {
                            code = "-1";
                            msg = "真实姓名不正确";
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                code = "500";
                msg = "系统异常";
            } finally {
                json.put("code", code);
                json.put("message", msg);
                json.put("data", resultMap);
                JSONUtil.toObjectJson(response, json.toString());
            }
        } else {
            loginForgotUpdatePassWordOneV211(request, response);
        }
    }

    /**
     * 修改密码 验证码是否通过,没有版本管理该方法在2.2.0版本之前用
     *
     * @param request req
     * @param response res
     */
    private void loginForgotUpdatePassWordOneV211(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> param = getParametersO(request);
        HashMap<String, Object> map = new HashMap<String, Object>();

        String code = "-1";
        String msg = "";
        String item = "";
        try {
            String userPhone = request.getParameter("phone");// 手机号码
            String smsCode = request.getParameter("code");// 验证码
            map.put("userPhone", userPhone);
            User user = this.userService.searchUserByCheckTel(map);// 根据手机号查询
            if (user != null) {
                if (param.containsKey("type") && "find_pwd".equals(param.get("type"))) {// 登录密码找回
                    String sendSmsCode = jedisCluster.get(SMS_FIND_PREFIX + userPhone);
                    if ("".equals(smsCode)) {
                        msg = "验证码不能为空";
                    } else if (StringUtils.isBlank(sendSmsCode)) {
                        msg = "验证码已失效，请重新获取";
                    } else if (!sendSmsCode.equals(smsCode)) {
                        msg = "验证码不正确";
                    } else if (sendSmsCode.equals(smsCode)) {
                        code = "0";
                        msg = "成功找回密码";
                        item = "1";
                        resultMap.put("item", item);
                    }
                } else if (param.containsKey("type") && "find_pay_pwd".equals(param.get("type"))) {// 交易密码找回

                    String sendSmsCode = jedisCluster.get(SMS_TRADE_FIND_PREFIX + userPhone);

                    String name = param.get("realname") + "";// 真实姓名
                    String id_number = param.get("id_card") + "";// 身份证号码

                    if (user.getRealname().equals(name)) {
                        if (user.getIdNumber().equals(id_number)) {
                            if (StringUtils.isBlank(sendSmsCode)) {
                                msg = "验证码已失效，请重新获取";
                            } else if ("".equals(smsCode)) {
                                msg = "验证码不能为空";
                            } else if (!sendSmsCode.equals(smsCode)) {
                                msg = "验证码不正确";
                            } else if (sendSmsCode.equals(smsCode)) {
                                code = "0";
                                msg = "成功找回密码";
                                item = "1";
                                resultMap.put("item", item);
                                resultMap.put("code", smsCode);
                            }
                        } else {
                            code = "-1";
                            msg = "身份证号码不正确";
                        }
                    } else {
                        code = "-1";
                        msg = "真实姓名不正确";
                    }
                }
            }
        } catch (Exception e) {
            log.error("loginForgotUpdatePassWordOneV211 error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            json.put("code", code);
            json.put("message", msg);
            json.put("data", resultMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 忘记修改密码第二步 提交
     */
    @RequestMapping("credit-user/reset-password")
    public void loginForgotUpdatePassWordtWO(HttpServletRequest request, HttpServletResponse response) {
        String RESET_PASSWD_CHECK = "rest_passwd_check_";

        JSONObject json = new JSONObject();
        Map<String, Map<String, Object>> dataMap = new HashMap<String, Map<String, Object>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String code = "-1";
        String msg = "";
        String item = "0";
//        AESUtil desEncrpt = new AESUtil();
//        AESUtil aesEncrypt = new AESUtil();
        String userPhone = "";
        try {
            // 未登录
            userPhone = request.getParameter("phone");// 手机号码
            String passWordNew = request.getParameter("password");// 新密码
            if (StringUtils.isBlank(userPhone)) {
                code = "-1";
                msg = "手机号码不能为空";
                return;
            }
            Long remainTime = checkForFront(RESET_PASSWD_CHECK, userPhone);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            if (StringUtils.isBlank(passWordNew)) {
                code = "-1";
                msg = "请输入新密码";
                return;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", userPhone);
            User user = this.userService.searchUserByCheckTel(map);
            if (user != null) {
                String checkPassWord = MD5Util.MD5(AESUtil.encrypt(passWordNew, ""));// 加密
                User userNew = new User();
                userNew.setId(user.getId());
                userNew.setPassword(checkPassWord);// 新密码

                int count = userService.updateByPrimaryKeyUser(userNew);
                if (count > 0) {
                    code = "0";
                    msg = "设置成功";
                    item = "1";
                    userNew.setUserName(user.getUserName());
                    loginSucc(request, userNew);
                } else {
                    code = "-1";
                    msg = "设置失败";
                }
            } else {
                code = "-1";
                msg = "手机号码不存在";
                return;
            }
        } catch (Exception e) {
            log.error("loginForgotUpdatePassWordtWO error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(RESET_PASSWD_CHECK, userPhone);
            resultMap.put("item", item);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", resultMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 设置交易密码
     */
    @RequestMapping("credit-user/set-paypassword")
    public void saveUpdatePaypassword(HttpServletRequest request, HttpServletResponse response) {
        String RESET_PAY_PASSWD_CHECK = "rest_pay_passwd_check_";
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String code = "-1";
        String msg = "";

//        AESUtil desEncrpt = new AESUtil();
//        AESUtil aesEncrypt = new AESUtil();
        String deviceId = request.getParameter("deviceId");
        try {
            Long remainTime = checkForFront(RESET_PAY_PASSWD_CHECK, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                if ("1".equals(user.getRealnameStatus())) {// 实名已认证
                    String payPassword = request.getParameter("password");// 交易密码
                    if (!"".equals(payPassword)) {
                        String checkPassWord = MD5Util.MD5(AESUtil.encrypt(payPassword, ""));// 加密
                        User userNew = new User();
                        userNew.setId(user.getId());
                        userNew.setPayPassword(checkPassWord);// 新密码
                        int count = userService.updateByPrimaryKeyUser(userNew);
                        if (count > 0) {
                            code = "0";
                            msg = "成功保存交易密码";
                            resultMap.put("true", true);
                            userNew.setUserName(user.getUserName());
                            loginSucc(request, userNew);
                        } else {
                            code = "-1";
                            msg = "保存交易密码失败";
                            resultMap.put("false", false);
                        }
                    } else {
                        code = "-1";
                        msg = "请输入交易密码";
                    }
                } else {
                    code = "-1";
                    msg = "请先完善个人信息";
                }
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("saveUpdatePaypassword error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(RESET_PAY_PASSWD_CHECK, deviceId);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 登录时 修改交易密码
     */
    @RequestMapping("credit-user/change-paypassword")
    public void loginUpdatePaypassword(HttpServletRequest request, HttpServletResponse response) {
        String CHANGE_PAY_PASSWD = "change_pay_passwd_";
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        String code = "-1";
        String msg = "";

        AESUtil desEncrpt = new AESUtil();
        AESUtil aesEncrypt = new AESUtil();
        String deviceId = request.getParameter("deviceId");
        try {
            Long remainTime = checkForFront(CHANGE_PAY_PASSWD, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                String passWordOld1 = request.getParameter("old_pwd");// 原密码
                String passWordNew1 = request.getParameter("new_pwd");// 新密码
                String checkPassWordOld = MD5Util.MD5(AESUtil.encrypt(passWordOld1, ""));// 加密

                if (user.getPayPassword().equals(checkPassWordOld)) {// 判断输入的密码是否与原密码一致
                    String checkPassWord = MD5Util.MD5(AESUtil.encrypt(passWordNew1, ""));// 加密
                    User userNew = new User();
                    userNew.setId(user.getId());
                    userNew.setPayPassword(checkPassWord);// 新密码
                    int count = userService.updateByPrimaryKeyUser(userNew);
                    if (count > 0) {
                        code = "0";
                        msg = "修改交易密码成功";
                        resultMap.put("true", true);
                        userNew.setUserName(user.getUserName());
                        loginSucc(request, userNew);
                    } else {
                        code = "-1";
                        msg = "修改交易密码失败";
                        resultMap.put("false", false);
                    }
                } else {
                    code = "-1";
                    msg = "原密码错误";
                }
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("loginUpdatePaypassword error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(CHANGE_PAY_PASSWD, deviceId);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 忘记交易密码 第二步 提交
     */
    @RequestMapping("credit-user/reset-pay-password")
    public void loginForgotUpdatePaypasswordTwo(HttpServletRequest request, HttpServletResponse response) {
        String RESET_PAY_PASSWD_SECOND_CHECK = "reset_pay_passwd_second_check_";
        JSONObject json = new JSONObject();
        Map<String, Map<String, Object>> dataMap = new HashMap<String, Map<String, Object>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String code = "-1";
        String msg = "";
        String item = "0";
//        AESUtil desEncrpt = new AESUtil();
//        AESUtil aesEncrypt = new AESUtil();
        String deviceId = request.getParameter("deviceId");
        try {
            Long remainTime = checkForFront(RESET_PAY_PASSWD_SECOND_CHECK, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            // 未登录
            String userPhone = request.getParameter("phone");// 手机号码
            String passWordNew = request.getParameter("password");// 新交易密码
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("userPhone", userPhone);
            User user = this.userService.searchUserByCheckTel(map);// 用户是否存在
            if (user != null) {
                String checkPassWord = MD5Util.MD5(AESUtil.encrypt(passWordNew, ""));// 加密
                User userNew = new User();
                userNew.setId(user.getId());
                userNew.setPayPassword(checkPassWord);// 新交易密码
                int count = userService.updateByPrimaryKeyUser(userNew);
                if (count > 0) {
                    code = "0";
                    msg = "设置成功";
                    item = "1";
                    userNew.setUserName(user.getUserName());
                    loginSucc(request, userNew);
                } else {
                    code = "-1";
                    msg = "设置失败";
                }
            } else {
                code = "-1";
                msg = "手机号码不存在";
                return;
            }

        } catch (Exception e) {
            log.error("loginForgotUpdatePaypasswordTwo error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(RESET_PAY_PASSWD_SECOND_CHECK, deviceId);
            resultMap.put("item", item);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", resultMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 退出
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("credit-user/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        Map<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        JSONObject json = new JSONObject();
        String code = "-1";
        String msg = "";
        try {
            loginOut(request);
            code = "0";
            msg = "成功退出";
            resultMap.put("true", true);
        } catch (Exception e) {
            log.error("logout error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("result", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 获取认证列表中--》获取个人信息
     */
    @RequestMapping("credit-card/get-person-info")
    public void getPersonInfo(HttpServletRequest request, HttpServletResponse response) {
        log.info("credit-card/get-person-info start");
        long startTime = System.currentTimeMillis();
        String GET_PERSON_INFO_CHECK = "get_person_info_check_";

        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
        List<Map<String, String>> degrees_all = new ArrayList<>();
        List<Map<String, String>> marriage_all = new ArrayList<>();
        List<Map<String, String>> live_time_type_all = new ArrayList<>();

        String code = "-1";
        String msg = "";
        String deviceId = request.getParameter("deviceId");
        try {
            code = "0";
            msg = "成功获取身份信息";
            User logUser = this.loginFrontUserByDeiceId(request);

            if (logUser != null) {
                log.info("credit-card/get-person-info logUser userId:" + logUser.getId());

                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));

                String name = StringUtils.isNotBlank(user.getRealname()) ? user.getRealname() : "";
                String id_number = StringUtils.isNotBlank(user.getIdNumber()) ? user.getIdNumber() : "";
                String address = StringUtils.isNotBlank(user.getPresentAddress()) ? user.getPresentAddress() : "";
                String live_period = StringUtils.isNotBlank(user.getPresentPeriod()) ? user.getPresentPeriod() : "";
                String address_distinct = StringUtils.isNotBlank(user.getPresentAddressDistinct()) ? user.getPresentAddressDistinct() : "";
                String longitude = StringUtils.isNotBlank(user.getPresentLongitude()) ? user.getPresentLongitude() : "";
                String latitude = StringUtils.isNotBlank(user.getPresentLatitude()) ? user.getPresentLatitude() : "";
                String face_recognition_picture = "";
                String id_number_z_picture = "";
                String id_number_f_picture = "";
                if (StringUtils.isNotBlank(user.getHeadPortrait())) {
//					face_recognition_picture = Constant.HTTP + domainOfBucket + "/" + user.getHeadPortrait();
                    face_recognition_picture = Constant.HTTP + domainOfBucket + user.getHeadPortrait();
                }
                if (StringUtils.isNotBlank(user.getIdcardImgZ())) {
                    id_number_z_picture = Constant.HTTP + domainOfBucket + user.getIdcardImgZ();
                }
                if (StringUtils.isNotBlank(user.getIdcardImgF())) {
                    id_number_f_picture = Constant.HTTP + domainOfBucket + user.getIdcardImgF();
                }
                String degrees = StringUtils.isNotBlank(user.getEducation()) ? user.getEducation() : "";
                String marriage = StringUtils.isNotBlank(user.getMaritalStatus()) ? user.getMaritalStatus() : "";

                for (Entry<String, String> entry : User.EDUCATION_TYPE.entrySet()) {// 学历
                    Map<String, String> map = new HashMap<String, String>();
//					System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
                    map.put("degrees", entry.getKey());
                    map.put("name", entry.getValue());
                    degrees_all.add(map);
                    resultMap.put("degrees_all", degrees_all);
                }
                for (Entry<String, String> entry : User.MARRIAGE_STATUS.entrySet()) {// 婚姻状况
                    Map<String, String> map = new HashMap<String, String>();
//					System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
                    map.put("marriage", entry.getKey());
                    map.put("name", entry.getValue());
                    marriage_all.add(map);
                    resultMap.put("marriage_all", marriage_all);
                }
                for (Entry<String, String> entry : User.RESIDENCE_DATE.entrySet()) {// 居住时长
                    Map<String, String> map = new HashMap<String, String>();
//					System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
                    map.put("live_time_type", entry.getKey());
                    map.put("name", entry.getValue());
                    live_time_type_all.add(map);
                    resultMap.put("live_time_type_all", live_time_type_all);
                }
                resultMap.put("id", user.getId());// ID
                resultMap.put("name", name);// 真实姓名
                resultMap.put("id_number", id_number);// 身份证号码
//				resultMap.put("degrees", degrees);// 学历
                resultMap.put("marriage", marriage);// 婚姻状况
                resultMap.put("address", address);// 居住地址
                resultMap.put("live_period", live_period);// 居住时长
                resultMap.put("address_distinct", address_distinct);// 居住详细地址
                resultMap.put("longitude", longitude);// 纬度
                resultMap.put("latitude", latitude);// 经度
                resultMap.put("real_verify_status", user.getRealnameStatus());// 实名认证状态
                resultMap.put("face_recognition_picture", face_recognition_picture);// 头像
                resultMap.put("id_number_z_picture", id_number_z_picture);// 身份证正面
                resultMap.put("id_number_f_picture", id_number_f_picture);// 身份证反面
                resultMap.put("qq", user.getQq());//QQ
                resultMap.put("email", user.getEmail());//邮箱地址
                log.info("credit-card/get-person-info logUser userId:" + logUser.getId() + ",resultMap:" + resultMap);
            } else {
                code = "-2";
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("getPersonInfo error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(GET_PERSON_INFO_CHECK, deviceId);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
            log.info("credit-card/get-person-info end,cost:" + (System.currentTimeMillis() - startTime) / 1000 + "s");
        }
    }

    /**
     * 获取认证列表中--》未实名认证 保存个人信息
     */
    @RequestMapping("credit-card/get-person-infos")
    public void getPersonInfos(HttpServletRequest request, HttpServletResponse response) {
        String GET_PERSON_INFOS_CHECK = "get_person_infos_check_";
        log.info("credit-card/get-person-infos start");
        long startTime = System.currentTimeMillis();

        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        String code = "-1";
        String msg = "";
        String deviceId = request.getParameter("deviceId");
        String tdDevice = request.getParameter("td_device");
        String clientType = request.getParameter("clientType");
        try {
            Long remainTime = checkForFront(GET_PERSON_INFOS_CHECK, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);

            if (logUser != null) {
                log.info("credit-card/get-person-infos logUser userId:" + logUser.getId());
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));

                User usr = new User();
                String name = request.getParameter("name");// 姓名
                String idNumber = request.getParameter("id_number");// 身份证号码
//                String status = request.getParameter("real_verify_status");// 认证状态
//                String education = request.getParameter("degrees");// 学历
                String address = request.getParameter("address");// 地址
                String addressDistinct = request.getParameter("address_distinct");// 详细地址
                String liveTime = request.getParameter("live_time_type");// 居住时长
                String longitude = request.getParameter("longitude");// 纬度
                String latitude = request.getParameter("latitude");// 经度
                String headPortrait = request.getParameter("face_recognition_picture");// 人脸识别
                String marriage = request.getParameter("marriage");// 婚姻状况
                String qq = request.getParameter("qq");
                String email = request.getParameter("email");
                Map<String, String> params = new HashMap<>();

                log.info("getPersonInfos headPortrait:" + headPortrait + ",name:" + name + ",idNumber:" + idNumber);

                if (StringUtils.isBlank(user.getHeadPortrait()) || StringUtils.isBlank(user.getIdcardImgZ()) || StringUtils.isBlank(user.getIdcardImgF())) {
                    log.info("credit-card/get-person-infos logUser userId:" + logUser.getId() + ",required ID card peicture is null");
                    code = "-1";
                    msg = "请先上传头像和身份证正反面";
                    return;
                }

                if (StringUtils.isBlank(name) || StringUtils.isBlank(idNumber)) {
                    log.info("credit-card/get-person-infos logUser userId:" + logUser.getId() + ",required name or idNumber is null");
                    code = "-1";
                    msg = "请输入完整的信息";
                    return;
                }

                //String realPath = request.getSession().getServletContext().getRealPath("");
                params.put("idcard_name", name);
                params.put("idcard_number", idNumber);
//				params.put("faceImageAttach", domainOfBucket+"/"+user.getHeadPortrait());
                params.put("faceImageAttach", domainOfBucket + user.getHeadPortrait());
                params.put("userId", user.getId());
                String apiKey = getAppConfig(request.getParameter("appName"), "XJX_API_KEY");
                String apiSecret = getAppConfig(request.getParameter("appName"), "XJX_API_SECRET");
                ResponseContent result = httpCertification.face(user, params, apiKey, apiSecret);

                log.info("credit-card/get-person-infos logUser userId:" + logUser.getId() + ",result:" + result.toString());

                if ("online".equals(PropertiesConfigUtil.get("profile"))) {//只有正式环境才需要走这段代码
                    if (!result.isSuccessed()) {
                        log.info("credit-card/get-person-infos logUser userId:" + logUser.getId() + ",result successed is false");
                        code = "-1";
                        msg = result.getMsg();
                        return;
                    }
                }

                // 已认证
                usr.setRealnameStatus("1");
                // 实名认证时间
                usr.setRealnameTime(new Date());

                Integer birthDay = Integer.valueOf(idNumber.substring(6, 10));
                // 年龄
                usr.setUserAge(Calendar.getInstance().get(Calendar.YEAR) - birthDay);
                usr.setId(user.getId());
                usr.setRealname(name);
                usr.setIdNumber(idNumber);
                usr.setPresentAddress(address);
                // 居住时长
                usr.setPresentPeriod(liveTime);
//				usr.setEducation(education);
                usr.setPresentLongitude(longitude);
                usr.setPresentLatitude(latitude);
                usr.setMaritalStatus(marriage);
                usr.setPresentAddressDistinct(addressDistinct);
                usr.setQq(qq);
                usr.setEmail(email);
                int count = this.userService.updateByPrimaryKeyUser(usr);

                if (count <= 0) {
                    log.info("credit-card/get-person-infos logUser userId:" + logUser.getId() + ",update count < 0,update fail");
                    code = "-1";
                    msg = "保存失败";
                    return;
                }

                /*
                 * 保存成功 发送数据到 风控
                 */
                verifiedAndPlusRequest(usr,tdDevice,clientType);
                code = "0";
                msg = "成功保存身份信息";
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", Integer.parseInt(usr.getId()));
                // 初始设置
                this.infoIndexService.authInfo(map);

//                //地推 实名认证
//                HashMap<String, Object> maps = new HashMap<>();
//                maps.put("realName", usr.getRealname());
//                ThreadPool pool = ThreadPool.getInstance();
//                pool.execute(new DtThread(UserPushUntil.REALNAME, Integer.parseInt(usr.getId()), null, new Date(), userService,
//                        pushUserService, null, maps));

                User userNow = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                loginSucc(request, userNow);

            } else {
                code = "-2";
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("getPersonInfos error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(GET_PERSON_INFOS_CHECK, deviceId);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
            log.info("credit-card/getPersonInfos end,cost:" + (System.currentTimeMillis() - startTime) / 1000 + "s");
        }
    }


    /**
     * 实名认证及加分认证时请求method
     * @param usr u
     */
    private void verifiedAndPlusRequest(User usr,String tdDevice,String clientType) {
        String id = usr.getId();
        if (StringUtils.isNotBlank(id)){
            log.info("start----------实名认证及加分认证时请求,用户ID:{}", id);
            User user1 = this.userService.searchByUserid(Integer.valueOf(id));
            HashMap<String, Object> map = new HashMap<>();
            String privateKeyStr = PropertiesConfigUtil.get("RISK_RSA_PRIVATE_kEY");
            map.put("consumerNo", PropertiesConfigUtil.get("RISK_BUSINESS")+ id);
            String realname = user1.getRealname();
            if(StringUtils.isNotBlank(realname)){
                map.put("realName",RsaUtil.encrypt(privateKeyStr, realname));
            }
            //对需求字段明文进行rsa加密（realName,idNo,phone,email）
            String userPhone = user1.getUserPhone();
            if (StringUtils.isNotBlank(userPhone)){
                map.put("phone", RsaUtil.encrypt(privateKeyStr, userPhone));
            }

            map.put("age", user1.getUserAge());
            String idNumber = user1.getIdNumber();
            if (StringUtils.isNotBlank(idNumber)){
                map.put("idNo", RsaUtil.encrypt(privateKeyStr,idNumber));
            }
            String email = user1.getEmail();
            if (StringUtils.isNotBlank(email)){
                map.put("email", RsaUtil.encrypt(privateKeyStr, email.trim()));
            }
            map.put("qq", user1.getQq());
            map.put("address", user1.getPresentAddress());
            map.put("channelId", Integer.valueOf(user1.getUserFrom()));
            //todo渠道名称
            map.put("userType", Integer.valueOf(user1.getCustomerType()));
            map.put("tradeName", user1.getWorkIndustry());
            map.put("companyName", user1.getCompanyName());
            map.put("companyAddrDetail", user1.getCompanyAddress());
            map.put("gender", "男".equals(user1.getUserSex())?1:0);
            map.put("province", user1.getProvince());
            if("android".equals(clientType)){
                map.put("appName","sdhb_and");
            }else{
                map.put("appName","sdhb_ios");
            }
            map.put("blackBox",tdDevice);
            ThreadPool.getInstance().execute(() -> {
                String resultOfUpdateUser = HttpUtil.postForm(PropertiesConfigUtil.get("RISK_USERINFO_URL"), map);
                log.info("verifiedAndPlusRequest risk result:{} ",resultOfUpdateUser);
            });

        }else {
            log.error("调用实名认证及加分认证时请求接口时，用户ID为null");
        }
        log.info("end----------实名认证及加分认证时请求,用户ID:{}",id);
    }


    @Autowired
    private UserContactsDao userContactsDao;

    /**
     *紧急联系人时上传通讯相关信息
     * @param user
     */
    private void sendContactInfo(User user){
        int id = Integer.valueOf(user.getId());
        log.info("start-----------紧急联系人时上传通讯相关信息,用户ID={}",id);
        User user1 = this.userService.searchByUserid(id);
        String realnameStatus = user1.getRealnameStatus();
        if (realnameStatus == null || !"1".equals(realnameStatus)){
            log.info("该用户没有进行实名认证，id={},调紧急联系人接口失败",id);
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", id);
        List<UserContacts> list = this.userContactsDao.selectUserContacts(hashMap);
        StringBuilder data = new StringBuilder();

        if (list != null){
            for (UserContacts contacts : list) {
                int flag = 0;
                data.append(contacts.getContactName()).append(":").append(contacts.getContactPhone()).append(",");
            }
        }
        String dataStr = null;
        if (StringUtils.isNotBlank(data)){
            dataStr = data.substring(0, data.length() - 1);
        }else {
            log.info("通讯录未查到，用户ID为{}",id);
        }

//        UserAddrBookInfo userAddrBookInfo = new UserAddrBookInfo();
//        userAddrBookInfo.setConsumerNo(PropertiesConfigUtil.get("RISK_BUSINESS")+id);
//        userAddrBookInfo.setData(dataStr);
        StringBuilder contactInfo = new StringBuilder();
        String fristContactRelation = user1.getFristContactRelation();
        String secondContactRelation = user1.getSecondContactRelation();
        if(fristContactRelation != null && secondContactRelation != null){
            String firstContactRelationName  = null;
            switch (fristContactRelation){
                case "1" :
                    firstContactRelationName = "父亲";
                    break;
                case "2" :
                    firstContactRelationName = "母亲";
                    break;
                case "3" :
                    firstContactRelationName = "儿子";
                    break;
                case "4" :
                    firstContactRelationName = "女儿";
                    break;
                case "5" :
                    firstContactRelationName = "配偶";
                    break;
                case "6" :
                    firstContactRelationName = "兄弟";
                    break;
                case "7" :
                    firstContactRelationName = "姐妹";
                    break;
                default:
            }

            String secondContactRelationName = null;
            switch (secondContactRelation){
                case "1" :
                    secondContactRelationName = "同学";
                    break;
                case "2" :
                    secondContactRelationName = "亲戚";
                    break;
                case "3" :
                    secondContactRelationName = "同事";
                    break;
                case "4" :
                    secondContactRelationName = "朋友";
                    break;
                case "5" :
                    secondContactRelationName = "其他";
                    break;
                default:
            }
            contactInfo.append(firstContactRelationName).append("_").append(user1.getFirstContactName())
                    .append("_").append(user1.getFirstContactPhone()).append(",").append(secondContactRelationName).append("_").append(user1.getSecondContactName()).append("_").append(user1.getSecondContactPhone());
//            userAddrBookInfo.setContactInfo(contactInfo.toString());
        }else {
            log.info("查询紧急联系人为空，用户ID为{}",id);
        }
//        System.out.println(contactInfo);

//        BeanMap map = new BeanMap(userAddrBookInfo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("consumerNo", PropertiesConfigUtil.get("RISK_BUSINESS")+id);
        map.put("data", dataStr);
        if (StringUtils.isNotBlank(contactInfo)){
            map.put("contactInfo", contactInfo.toString());
        }else {
            map.put("contactInfo", null);
        }
        ThreadPool.getInstance().execute(() -> {
            String resultOfUpdateUser = HttpUtil.postForm(PropertiesConfigUtil.get("RISK_ADDR_BOOK_URL"), map);
            log.info("verifiedAndPlusRequest risk result:{} ",resultOfUpdateUser);
        });
//        log.info("resultOfUpdateUser:{}",resultOfUpdateUser);
//        Map resultMap = JSON.parseObject(resultOfUpdateUser);
//        String code = (String) resultMap.get("code");
//        String result = (String) resultMap.get("msg");
//        if (code == null || !"0000".equals(code)){
//                log.info("紧急联系人时上传通讯相关信息操作失败:{}",result);
//            }
//        log.info("紧急联系人时上传通讯相关信息操作成功,返回结果：{}",result);
        log.info("end-----------紧急联系人时上传通讯相关信息,用户ID={}",id);
    }

    /**
     * 查询用户的加分信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("credit-web/score-auth")
    public void scoreAuth(HttpServletRequest request, HttpServletResponse response) {
        User logUser = this.loginFrontUserByDeiceId(request);
        JSONObject json = new JSONObject();
        Map<String, HashMap<String, Object>> dataMap = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String code = "-1";
        String msg = "";
        String GET_SCORE_AUTH = "get_score_auth_";
        String deviceId = request.getParameter("deviceId");
        try {
//            Long remainTime = checkForFront(GET_SCORE_AUTH, deviceId);
            if (logUser != null) {
                log.info("credit-web/score-auth begin,logUser userId:" + logUser.getId());
                code = "0";
                msg = "成功获取加分信息";
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                List<Map<String, String>> marriage_all = new ArrayList<Map<String, String>>();
                List<Map<String, String>> live_time_type_all = new ArrayList<Map<String, String>>();

                String maritalStatus = user.getMaritalStatus();
                String presentPeriod = user.getPresentPeriod();
                String companyName = user.getCompanyName();
                String companyAddress = user.getCompanyAddress();
                String workIndustry = user.getWorkIndustry();

                // 婚姻状况 列表
                for (Entry<String, String> entry : User.MARRIAGE_STATUS.entrySet()) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("marriage", entry.getKey());
                    map.put("name", entry.getValue());
                    marriage_all.add(map);
                    resultMap.put("marriage_all", marriage_all);
                }
                // 居住时长 列表
                for (Entry<String, String> entry : User.RESIDENCE_DATE.entrySet()) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("live_time_type", entry.getKey());
                    map.put("name", entry.getValue());
                    live_time_type_all.add(map);
                    resultMap.put("live_time_type_all", live_time_type_all);
                }

                // 婚姻状况 列表
                resultMap.put("marriage", maritalStatus);
                // 居住时长
                resultMap.put("live_period", presentPeriod);
                //公司名称
                resultMap.put("company_name", companyName);
                //公司地址
                resultMap.put("company_address", companyAddress);
                //工作行业
                resultMap.put("work_industry", workIndustry);
                log.info("credit-web/score-auth finish,logUser userId:" + logUser.getId() + ",resultMap:" + resultMap);

            } else {
                code = "-2";
                msg = "请先登录";
            }

        } catch (Exception e) {
            code = "500";
            msg = "系统异常";
            log.error("credit-web/score-auth error,logUser userId:" + logUser.getId() + ",resultMap:" + resultMap, e);
        } finally {
            delCheckForFront(GET_SCORE_AUTH, deviceId);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 保存用户的加分信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("credit-info/save-score-auth")
    public void saveScoreAuth(HttpServletRequest request, HttpServletResponse response) {
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
//        String deviceId = request.getParameter("deviceId");
        String code = "-1";
        String msg = "";
        String tdDevice = request.getParameter("td_device");
        String clientType = request.getParameter("clientType");
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                log.info("credit-info/save-score-auth begin,logUser userId:" + logUser.getId());
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                // 居住时长
                String liveTime = request.getParameter("live_period");
                // 婚姻状况
                String marriage = request.getParameter("marriage");
                //公司名称
                String companyName = request.getParameter("company_name");
                //公司地址
                String companyAddress = request.getParameter("company_address");
                //工作行业
                String workIndustry = request.getParameter("work_industry");

                User userNew = new User();
                userNew.setId(user.getId());
                // 居住时长
                if(liveTime!=null){
                    userNew.setPresentPeriod(liveTime);
                }
                // 婚姻状况
                if(marriage!=null){
                    userNew.setMaritalStatus(marriage);
                }
                userNew.setCompanyAddress(companyAddress);
                userNew.setCompanyName(companyName);
                userNew.setWorkIndustry(workIndustry);

                int count = this.userService.updateUserScoreAuth(userNew);
                if("1".equals(user.getRealnameStatus())){
                    verifiedAndPlusRequest(userNew,tdDevice,clientType);
                }
                if (count > 0) {
                    code = "0";
                    msg = "成功保存加分信息";
                    userNew.setUserName(user.getUserName());
                    loginSucc(request, userNew);
                } else {
                    code = "-1";
                    msg = "保存失败";
                }
                log.info("credit-info/save-score-auth finish,logUser userId:" + logUser.getId() + ",count:" + count);
            } else {
                code = "-2";
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("credit-info/save-score-auth error", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 获取认证列表中--》已经名认证 保存个人信息
     */
    @RequestMapping("credit-info/save-person-info")
    public void savePersonInfos(HttpServletRequest request, HttpServletResponse response) {
        String SAVE_PERSON_INFO_CHECK = "save_person_info_check_";
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
        String deviceId = request.getParameter("deviceId");
        String tdDevice = request.getParameter("td_device");
        String clientType = request.getParameter("clientType");
        String code = "-1";
        String msg = "";
        try {
            Long remainTime = checkForFront(SAVE_PERSON_INFO_CHECK, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                // User usr=new User();
//                String education = request.getParameter("degrees");// 学历
                // 地址
                String address = request.getParameter("address");
                // 详细地址
                String addressDistinct = request.getParameter("address_distinct");
                // 居住时长
                String liveTime = request.getParameter("live_time_type");
                // 纬度
                String longitude = request.getParameter("longitude");
                // 经度
                String latitude = request.getParameter("latitude");
                // 婚姻状况
                String marriage = request.getParameter("marriage");
                //QQ号
                String qq = request.getParameter("qq");
                //邮箱
                String email = request.getParameter("email");

                User userNew = new User();
                userNew.setId(user.getId());
                // user.setId(user.getId());
                userNew.setPresentAddress(address);
                // 居住时长
                userNew.setPresentPeriod(liveTime);
//				userNew.setEducation(education);
                userNew.setPresentLongitude(longitude);
                userNew.setPresentLatitude(latitude);
                userNew.setMaritalStatus(marriage);
                userNew.setPresentAddressDistinct(addressDistinct);
                userNew.setQq(qq);
                userNew.setEmail(email);
                int count = this.userService.updateByPrimaryKeyUser(userNew);

                if("1".equals(user.getRealnameStatus())){
                    verifiedAndPlusRequest(userNew,tdDevice,clientType);
                }
                if (count > 0) {
                    code = "0";
                    msg = "成功保存身份信息";
                    userNew.setUserName(user.getUserName());
                    loginSucc(request, userNew);
                } else {
                    code = "-1";
                    msg = "保存失败";
                }
            } else {
                code = "-2";
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("savePersonInfos error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            delCheckForFront(SAVE_PERSON_INFO_CHECK, deviceId);
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 获取认证列表中--》紧急联系人
     */
    @RequestMapping("credit-card/get-contacts")
    public void getContacts(HttpServletRequest request, HttpServletResponse response) {
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        List<Map<String,String>> linealList = new ArrayList<>();
        List<Map<String,String>> otherList = new ArrayList<>();

        String code = "-1";
        String msg = "";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                code = "0";
                msg = "成功获取紧急联系人";
                // 直系亲属联系人
                for (Entry<String, String> entry : User.CONTACTS_FAMILY.entrySet()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("type", entry.getKey());
                    map.put("name", entry.getValue());
                    linealList.add(map);
                    resultMap.put("lineal_list", linealList);
                }
                // 其他联系人
                for (Entry<String, String> entry : User.CONTACTS_OTHER.entrySet()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("type", entry.getKey());
                    map.put("name", entry.getValue());
                    otherList.add(map);
                    resultMap.put("other_list", otherList);
                }
                // 直系亲属
                resultMap.put("lineal_relation", user.getFristContactRelation() != null?user.getFristContactRelation():"");
                // 直系亲属称呼
                resultMap.put("lineal_name", user.getFirstContactName() != null?user.getFirstContactName():"");
                // 直系亲属电话
                resultMap.put("lineal_mobile", user.getFirstContactPhone() != null?user.getFirstContactPhone():"");
                // 其他联系
                resultMap.put("other_relation", user.getSecondContactRelation() != null?user.getSecondContactRelation():"");
                // 其他联系称呼
                resultMap.put("other_name", user.getSecondContactName() != null?user.getSecondContactName():"");
                // 其他联系电话
                resultMap.put("other_mobile", user.getSecondContactPhone() != null?user.getSecondContactPhone():"");
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = "请先登录";
            }

        } catch (Exception e) {
            log.error("getContacts error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    private String handleMoblile(String mobile) {
        mobile = mobile.trim();
        if (mobile.startsWith("86")) {
            mobile = mobile.substring(2, mobile.length());
        } else if (mobile.startsWith("+86")) {
            mobile = mobile.substring(3, mobile.length());
        }
        Matcher linealMobileMaths = PATTERN_PHONE.matcher(mobile);
        if (linealMobileMaths.matches()) {
            return mobile;
        } else {
            return "";
        }

    }

    /**
     * 获取认证列表中--》保存紧急联系人
     */
    @RequestMapping("credit-card/get-contactss")
    public void getContactss(HttpServletRequest request, HttpServletResponse response) {
        String getContactsCheck = "get_contacts_check_";
        String deviceId = request.getParameter("deviceId");
        String clientType = request.getParameter("clientType");

        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        List<Map<String,String>> linealList = new ArrayList<>();
        List<Map<String,String>> otherList = new ArrayList<>();

        String code = "-1";
        String msg = "";
        try {
            Long remainTime = checkForFront(getContactsCheck, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                if (!"2".equals(user.getTdStatus())) {
                    // 直系联系人标识
                    String linealRelation = request.getParameter("type");
                    // 直系联系人
                    String linealName = request.getParameter("name");
                    // 直系联系人电话
                    String linealMobile = request.getParameter("mobile");
                    // 其他联系人标识
                    String otherRelation = request.getParameter("relation_spare");
                    // 其他联系人
                    String otherName = request.getParameter("name_spare");
                    // 其他联系人电话
                    String otherMobile = request.getParameter("mobile_spare");

                    // 用户表
                    // User usr=new User();
                    // usr.setId(user.getId());
                    User userNew = new User();
                    userNew.setId(user.getId());
                    // 直系联系人
                    userNew.setFristContactRelation(linealRelation);
                    Matcher m = PATTERN_NAME.matcher(linealName);
                    // 当联系人名称为汉字时入库
                    if (m.matches()) {
                        userNew.setFirstContactName(linealName);
                    } else {
                        code = "-1";
                        msg = "直接联系人名称有误";
                        return;
                    }

                    log.info("linealMobile:" + linealMobile);

                    if (StringUtils.isNotBlank(linealMobile)) {
                        if (linealMobile.contains(":")) {
                            String[] mobleArray = linealMobile.split(":");
                            if (null != mobleArray && 0 < mobleArray.length) {
                                for (String oneMobile : mobleArray) {
                                    if (StringUtils.isNotBlank(oneMobile)) {
                                        String contacMobile = handleMoblile(oneMobile);
                                        if (StringUtils.isNotBlank(contacMobile)) {
                                            if (contacMobile.length() != 11) {
                                                code = "-1";
                                                msg = "直接联系人手机号码格式不正确";
                                                return;
                                            }
                                            userNew.setFirstContactPhone(contacMobile);
                                            break;
                                        }

                                    }
                                }
                            }
                        } else {
                            String contactMobile = handleMoblile(linealMobile);
                            if (contactMobile.length() != 11) {
                                code = "-1";
                                msg = "直接联系人手机号码格式不正确";
                                return;
                            }
                            userNew.setFirstContactPhone(contactMobile);
                        }

                        if (StringUtils.isBlank(userNew.getFirstContactPhone())) {
                            code = "-1";
                            msg = "紧急直接联系人为空";
                            return;
                        }

                    } else {
                        code = "-1";
                        msg = "紧急直接联系人参数为空";
                        return;
                    }

                    // 其他联系人
                    userNew.setSecondContactRelation(otherRelation);
                    Matcher mm = PATTERN_NAME.matcher(otherName);
                    // 当联系人名称为汉字时入库
                    if (mm.matches()) {
                        userNew.setSecondContactName(otherName);
                    } else {
                        code = "-1";
                        msg = "其他联系人名称有误";
                        return;
                    }
                    // 00000000000000000000000000000000000000000000000000000

                    log.info("otherMobile:" + otherMobile);

                    if (StringUtils.isNotBlank(otherMobile)) {
                        if (otherMobile.contains(":")) {
                            String[] mobleArray = otherMobile.split(":");
                            if (null != mobleArray && 0 < mobleArray.length) {
                                for (String oneMobile : mobleArray) {
                                    if (StringUtils.isNotBlank(oneMobile)) {
                                        String contacMobile = handleMoblile(oneMobile);
                                        if (StringUtils.isNotBlank(contacMobile)) {
                                            if (contacMobile.length() != 11) {
                                                code = "-1";
                                                msg = "紧急直接联系人手机号码格式不正确";
                                                return;
                                            }
                                            userNew.setSecondContactPhone(contacMobile);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            String contactMobile = handleMoblile(otherMobile);
                            if (contactMobile.length() != 11) {
                                code = "-1";
                                msg = "紧急直接联系人手机号码格式不正确";
                                return;
                            }
                            userNew.setSecondContactPhone(contactMobile);
                        }

                        if (StringUtils.isBlank(userNew.getSecondContactPhone())) {
                            code = "-1";
                            msg = "紧急其他联系人为空";
                            return;
                        }
                    } else {
                        code = "-1";
                        msg = "紧急其他联系人参数为空";
                        return;
                    }

                    // 00000000000000000000000000000000000000000000000000000
                    log.info("firstContactPhone:" + userNew.getFirstContactPhone() + ",SecondContactPhone:" + userNew.getSecondContactPhone());


                    int count = this.userService.updateByPrimaryKeyUser(userNew);
                    //总开关 1 关闭 0 开启
                    try{
                        sendContactInfo(userNew);
                    }
                    catch (Exception e){
                        log.error(" send risk error:{}",e);
                    }
                    String cfcaSwitch = jedisCluster.get("CFCA_SWITCH");
                    if (count > 0) {
                        if (0 == user.getSignSwitch() && "0".equals(cfcaSwitch)) {
                            try {
                                //在此处新增对支付令的业务处理
                                cfcaSignAndViewService.feachCfcaUserModule(userNew.getId());
                            } catch (Exception cfcae) {
                                log.error("user:" + userNew.getId() + " is null", cfcae);
                            }
                        }
                        code = "0";
                        msg = "成功保存紧急联系人";
                        userNew.setUserName(user.getUserName());
                        loginSucc(request, userNew);

                        HashMap<String, Object> cmap = new HashMap<>();
                        cmap.put("userId", Integer.parseInt(userNew.getId()));
                        // 设置状态
                        this.infoIndexService.authContacts(cmap);
                        if ("ios".equals(clientType)) {
                            User zmUser = new User();
                            zmUser.setId(user.getId());
                            zmUser.setZmStatus("2");
                            this.userService.updateByPrimaryKeyUser(zmUser);
                            // 设置芝麻信用状态
                            this.infoIndexService.authSesame(cmap);
                        }


//                        //地推 紧急联系人认证
//                        ThreadPool pool = ThreadPool.getInstance();
//                        pool.execute(new DtThread(UserPushUntil.CONTACTAPPROVE, Integer.valueOf(user.getId()), null, new Date(), userService,
//                                pushUserService, null, null));
                        // 直系亲属联系人
                        for (Entry<String, String> entry : User.WORK_DATE.entrySet()) {
                            Map<String,String> map = new HashMap<>();
//							System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
                            map.put("type", entry.getKey());
                            map.put("name", entry.getValue());
                            linealList.add(map);
                            resultMap.put("lineal_list", linealList);
                        }
                        // 其他联系人
                        for (Entry<String, String> entry : User.CONTACTS_OTHER.entrySet()) {
                            Map<String,String> map = new HashMap<>();
//							System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
                            map.put("type", entry.getKey());
                            map.put("name", entry.getValue());
                            otherList.add(map);
                            resultMap.put("other_list", otherList);
                        }

                        resultMap.put("lineal_relation", linealRelation);
                        resultMap.put("lineal_name", linealName);
                        resultMap.put("lineal_mobile", linealMobile);
                        resultMap.put("other_relation", otherRelation);
                        resultMap.put("other_name", otherName);
                        resultMap.put("other_mobile", otherMobile);
                    } else {
                        code = "-1";
                        msg = "保存紧急联系人失败";
                    }
                } else {
                    code = "-1";
                    msg = "您的紧急联系人已通过认证不能再修改";
                }
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("getContactss error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }


    /**
     * 获取认证列表中--》保存紧急联系人
     */
    @RequestMapping("credit-card/v2/get-contactss")
    public void getContactssV2(HttpServletRequest request, HttpServletResponse response) {
        String getContactsCheck = "get_contacts_check_";
        String deviceId = request.getParameter("deviceId");

        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        List<Map<String,String>> linealList = new ArrayList<>();
        List<Map<String,String>> otherList = new ArrayList<>();

        String code = "-1";
        String msg = "";
        try {
            Long remainTime = checkForFront(getContactsCheck, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                if (!"2".equals(user.getTdStatus())) {
                    // 直系联系人标识
                    String linealRelation = request.getParameter("type");
                    // 直系联系人
                    String linealName = request.getParameter("name");
                    // 直系联系人电话
                    String linealMobile = request.getParameter("mobile");
                    // 其他联系人标识
                    String otherRelation = request.getParameter("relation_spare");
                    // 其他联系人
                    String otherName = request.getParameter("name_spare");
                    // 其他联系人电话
                    String otherMobile = request.getParameter("mobile_spare");

                    // 用户表
                    // User usr=new User();
                    // usr.setId(user.getId());
                    User userNew = new User();
                    userNew.setId(user.getId());
                    // 直系联系人
                    userNew.setFristContactRelation(linealRelation);
                    Matcher m = PATTERN_NAME.matcher(linealName);
                    // 当联系人名称为汉字时入库
                    if (m.matches()) {
                        userNew.setFirstContactName(linealName);
                    } else {
                        code = "-1";
                        msg = "直接联系人名称有误";
                        return;
                    }

                    log.info("linealMobile:" + linealMobile);

                    if (StringUtils.isNotBlank(linealMobile)) {
                        if (linealMobile.contains(":")) {
                            String[] mobleArray = linealMobile.split(":");
                            if (null != mobleArray && 0 < mobleArray.length) {
                                for (String oneMobile : mobleArray) {
                                    if (StringUtils.isNotBlank(oneMobile)) {
                                        String contacMobile = handleMoblile(oneMobile);
                                        if (StringUtils.isNotBlank(contacMobile)) {
                                            if (contacMobile.length() != 11) {
                                                code = "-1";
                                                msg = "直接联系人手机号码格式不正确";
                                                return;
                                            }
                                            userNew.setFirstContactPhone(contacMobile);
                                            break;
                                        }

                                    }
                                }
                            }
                        } else {
                            String contactMobile = handleMoblile(linealMobile);
                            if (contactMobile.length() != 11) {
                                code = "-1";
                                msg = "直接联系人手机号码格式不正确";
                                return;
                            }
                            userNew.setFirstContactPhone(contactMobile);
                        }

                        if (StringUtils.isBlank(userNew.getFirstContactPhone())) {
                            code = "-1";
                            msg = "紧急直接联系人为空";
                            return;
                        }

                    } else {
                        code = "-1";
                        msg = "紧急直接联系人参数为空";
                        return;
                    }

                    // 其他联系人
                    userNew.setSecondContactRelation(otherRelation);
                    Matcher mm = PATTERN_NAME.matcher(otherName);
                    // 当联系人名称为汉字时入库
                    if (mm.matches()) {
                        userNew.setSecondContactName(otherName);
                    } else {
                        code = "-1";
                        msg = "其他联系人名称有误";
                        return;
                    }
                    // 00000000000000000000000000000000000000000000000000000

                    log.info("otherMobile:" + otherMobile);

                    if (StringUtils.isNotBlank(otherMobile)) {
                        if (otherMobile.contains(":")) {
                            String[] mobleArray = otherMobile.split(":");
                            if (null != mobleArray && 0 < mobleArray.length) {
                                for (String oneMobile : mobleArray) {
                                    if (StringUtils.isNotBlank(oneMobile)) {
                                        String contacMobile = handleMoblile(oneMobile);
                                        if (StringUtils.isNotBlank(contacMobile)) {
                                            if (contacMobile.length() != 11) {
                                                code = "-1";
                                                msg = "紧急直接联系人手机号码格式不正确";
                                                return;
                                            }
                                            userNew.setSecondContactPhone(contacMobile);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            String contactMobile = handleMoblile(otherMobile);
                            if (contactMobile.length() != 11) {
                                code = "-1";
                                msg = "紧急直接联系人手机号码格式不正确";
                                return;
                            }
                            userNew.setSecondContactPhone(contactMobile);
                        }

                        if (StringUtils.isBlank(userNew.getSecondContactPhone())) {
                            code = "-1";
                            msg = "紧急其他联系人为空";
                            return;
                        }
                    } else {
                        code = "-1";
                        msg = "紧急其他联系人参数为空";
                        return;
                    }

                    // 00000000000000000000000000000000000000000000000000000
                    log.info("firstContactPhone:" + userNew.getFirstContactPhone() + ",SecondContactPhone:" + userNew.getSecondContactPhone());

                    int count = this.userService.updateByPrimaryKeyUser(userNew);
                    //总开关 1 关闭 0 开启
                    String cfcaSwitch = jedisCluster.get("CFCA_SWITCH");

                    try{
                        sendContactInfo(userNew);
                    }
                    catch (Exception e){
                        log.error(" send risk error:{}",e);
                    }
                    if (count > 0) {
                        if (0 == user.getSignSwitch() && "0".equals(cfcaSwitch)) {
                            try {
                                //在此处新增对支付令的业务处理
                                cfcaSignAndViewService.feachCfcaUserModule(userNew.getId());
                            } catch (Exception cfcae) {
                                log.error("user:" + userNew.getId() + " is null", cfcae);
                            }
                        }
                        code = "0";
                        msg = "成功保存紧急联系人";
                        userNew.setUserName(user.getUserName());
                        loginSucc(request, userNew);

                        HashMap<String, Object> cmap = new HashMap<>();
                        cmap.put("userId", Integer.parseInt(userNew.getId()));
                        // 设置状态
                        this.infoIndexService.authContacts(cmap);

                        //地推 紧急联系人认证
//                        ThreadPool pool = ThreadPool.getInstance();
//                        pool.execute(new DtThread(UserPushUntil.CONTACTAPPROVE, Integer.valueOf(user.getId()), null, new Date(), userService,
//                                pushUserService, null, null));
                        // 直系亲属联系人
                        for (Entry<String, String> entry : User.WORK_DATE.entrySet()) {
                            Map<String,String> map = new HashMap<>();
//							System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
                            map.put("type", entry.getKey());
                            map.put("name", entry.getValue());
                            linealList.add(map);
                            resultMap.put("lineal_list", linealList);
                        }
                        // 其他联系人
                        for (Entry<String, String> entry : User.CONTACTS_OTHER.entrySet()) {
                            Map<String,String> map = new HashMap<>();
                            map.put("type", entry.getKey());
                            map.put("name", entry.getValue());
                            otherList.add(map);
                            resultMap.put("other_list", otherList);
                        }

                        resultMap.put("lineal_relation", linealRelation);
                        resultMap.put("lineal_name", linealName);
                        resultMap.put("lineal_mobile", linealMobile);
                        resultMap.put("other_relation", otherRelation);
                        resultMap.put("other_name", otherName);
                        resultMap.put("other_mobile", otherMobile);
                    } else {
                        code = "-1";
                        msg = "保存紧急联系人失败";
                    }
                } else {
                    code = "-1";
                    msg = "您的紧急联系人已通过认证不能再修改";
                }
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("getContactss error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 获取认证列表中--》获取工作详细信息
     */
    @RequestMapping("credit-card/get-work-info")
    public void getWorkInfo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        List<Map<String,String>> companyPeriodList = new ArrayList<>();
        List<Map<String,String>> companyWorkTypeList = new ArrayList<>();

        String code = "-1";
        String msg = "";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                code = "0";
                msg = "成功获取工作信息";
                // 工作时长
                for (Entry<String, String> entry : User.WORK_DATE.entrySet()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("entry_time_type", entry.getKey());
                    map.put("name", entry.getValue());
                    companyPeriodList.add(map);
                    resultMap.put("company_period_list", companyPeriodList);
                }
                // 工作类型
                for (Entry<String, String> entry : User.WORK_TYPE.entrySet()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("work_type_id", entry.getKey());
                    map.put("name", entry.getValue());
                    companyWorkTypeList.add(map);
                    resultMap.put("company_worktype_list", companyWorkTypeList);
                }

                // 公司名称
                resultMap.put("company_name", user.getCompanyName() != null?user.getCompanyName():"");
                // 工作地址
                resultMap.put("company_address", user.getCompanyAddress() != null?user.getCompanyAddress():"");
                // 经度
                resultMap.put("company_longitude", user.getCompanyLongitude() != null?user.getCompanyLongitude():"");
                // 经度
                resultMap.put("company_latitude", user.getCompanyLatitude() != null?user.getCompanyLatitude():"");
                // 详细地址
                resultMap.put("company_address_distinct", user.getCompanyAddressDistinct() != null?user.getCompanyAddressDistinct():"");
                // 公司电话
                resultMap.put("company_phone", user.getCompanyPhone() != null?user.getCompanyPhone():"");
                // 工作时长
                resultMap.put("company_period", user.getCompanyPeriod() != null?user.getCompanyPeriod():"");
                List<UserInfoImage> userInfoImage = this.userInfoImageService.selectImageByUserId(Integer.parseInt(user.getId()));
                // 是否有工作照
                resultMap.put("company_picture", userInfoImage != null && userInfoImage.size() > 0?"1":"0");
            } else {
                msg = "失败";
            }
        } catch (Exception e) {
            log.error("getWorkInfo error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 获取认证列表中--》保存工作详细信息
     */
    @RequestMapping("credit-card/save-work-info")
    public void saveWorkInfo(HttpServletRequest request, HttpServletResponse response) {
        String saveWorkInfoCheck = "save_work_info_check_";
        String deviceId = request.getParameter("deviceId");

        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        List<Map<String,String>> companyPeriodList = new ArrayList<>();
        List<Map<String,String>> companyWorkTypeList = new ArrayList<>();

        String code = "-1";
        String msg = "";
        try {
            Long remainTime = checkForFront(saveWorkInfoCheck, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                // 单位名称
                String companyName = request.getParameter("company_name");
                // 单位电话
                String companyPhone = request.getParameter("company_phone");
                // 单位地址
                String companyAddress = request.getParameter("company_address");
                // 详细地址
                String companyAddressDistinct = request.getParameter("company_address_distinct");
                // 纬度
                String latitude = request.getParameter("latitude");
                // 经度
                String longitude = request.getParameter("longitude");
                // 是否有工作证照
                String companyPicture = request.getParameter("company_picture");
                // 工作时长
                String companyPeriod = request.getParameter("company_period");

                User userNew = new User();
                userNew.setId(user.getId());
                userNew.setCompanyName(companyName);
                userNew.setCompanyPhone(companyPhone);
                userNew.setCompanyAddress(companyAddress);
                userNew.setCompanyAddressDistinct(companyAddressDistinct);
                userNew.setCompanyPeriod(companyPeriod);

                int count = this.userService.updateByPrimaryKeyUser(userNew);
                if (count > 0) {
                    code = "0";
                    msg = "成功保存工作信息";
                    userNew.setUserName(user.getUserName());
                    loginSucc(request, userNew);

                    //地推 工作信息
//                    ThreadPool pool = ThreadPool.getInstance();
//                    pool.execute(new DtThread(UserPushUntil.COMPANYAPPROVE, Integer.parseInt(user.getId()), null, new Date(), userService,
//                            pushUserService, null, null));

                } else {
                    code = "-1";
                    msg = "保存工作信息失败";
                }
                resultMap.put("company_name", companyName);
                resultMap.put("company_address", companyAddress);
                resultMap.put("company_longitude", longitude);
                resultMap.put("company_latitude", latitude);
                resultMap.put("company_address_distinct", companyAddressDistinct);
                resultMap.put("company_phone", companyPhone);
                resultMap.put("company_period", companyPeriod);
                resultMap.put("company_picture", companyPicture!=null?"1":"0");
                // 工作时长
                for (Entry<String, String> entry : User.WORK_DATE.entrySet()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("entry_time_type", entry.getKey());
                    map.put("name", entry.getValue());
                    companyPeriodList.add(map);
                    resultMap.put("company_period_list", companyPeriodList);
                }

                for (Entry<String, String> entry : User.WORK_TYPE.entrySet()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("work_type_id", entry.getKey());
                    map.put("name", entry.getValue());
                    companyWorkTypeList.add(map);
                    resultMap.put("company_worktype_list", companyWorkTypeList);
                }
            } else {
                code = "-2";
                msg = "请先登录";
            }
        } catch (Exception e) {
            log.error("saveWorkInfo error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 获取认证列表中--》保存工作详细信息--》获取工作证件照
     */
    @RequestMapping("picture/get-pic-list")
    public void getPicList(HttpServletRequest request, HttpServletResponse response) {

        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
        // 图片集合
        List<Map<String,Object>> imgLists = new ArrayList<>();

        String code = "-1";
        String msg = "";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                List<UserInfoImage> imgList = this.userInfoImageService.selectImageByUserId(Integer.parseInt(user.getId()));
                if (imgList.size() > 0) {
                    for (int i = 0; i < imgList.size(); i++) {
                        Map map = (Map) imgList.get(i);
                        Map<String,Object> redMap = new HashMap<>();
                        String id = "";
                        if (map.get("id") != null) {
                            id = map.get("id") + "";
                        }
                        // 图片id
                        redMap.put("id", id);

                        String picName = "";
                        if (map.get("id") != null) {
                            picName = map.get("picName") + "";
                        }
                        // 图片名称
                        redMap.put("pic_name", picName);

                        String type = "";
                        if (map.get("type") != null) {
                            type = map.get("type") + "";
                        }
                        // 图片类型
                        redMap.put("type", type);

                        String url = "";
                        if (map.get("url") != null) {
                            //url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + map.get("url");
                            url = Constant.HTTP + domainOfBucket + "/" + map.get("url");
                        }
                        // 图片路径
                        redMap.put("url", url);
                        imgLists.add(redMap);
                    }
                    code = "0";
                    msg = "成功获取";
                    resultMap.put("data", imgLists);
                } else {
                    code = "0";
                    msg = "暂无数据";
                    resultMap.put("data", imgLists);
                }
            } else {
                code = "-2";
                msg = "请先登录";
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = "500";
            msg = "系统异常";
        } finally {
            resultMap.put("type", "6");
            resultMap.put("max_pictures", "3");
            resultMap.put("title", "工作证照");
            resultMap.put("notice", "请提供可以证明您在此公司工作的照片，如含本人照片的工牌照、名片、与公司Logo合影照等");
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 意见反馈
     */
    @RequestMapping("credit-info/feedback")
    public void feedback(HttpServletRequest request, HttpServletResponse response) {
        String feedBackCheck = "feed_back_check_";
        String deviceId = request.getParameter("deviceId");
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();

        String code = "-1";
        String msg = "";
        try {
            Long remainTime = checkForFront(feedBackCheck, deviceId);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                // 反馈内容
                String content = request.getParameter("content");
                if (StringUtils.isBlank(content)) {
                    msg = "请输入反馈内容";
                    return;
                }
                String equipmentNumber = request.getParameter("deviceId");
                PlatfromAdvise advise = new PlatfromAdvise();
                advise.setUserPhone(user.getUserPhone());
                advise.setAdviseContent(content);
                advise.setAdviseAddtime(new Date());
                advise.setAdviseAddip(equipmentNumber);
                int count = this.platfromAdviseService.searchPlatfromAdvise(advise);
                if (count > 0) {
                    code = "0";
                    msg = "反馈成功";
                    loginSucc(request, user);
                } else {
                    code = "-1";
                    msg = "反馈失败";
                }
            } else {
                code = "-2";
                msg = "登录已失效";
            }
        } catch (Exception e) {
            log.error("feedback error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 关于我们
     */
    @RequestMapping("page/detailAbout")
    public String detailAbout(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                Map<String, String> intervalMap = SysCacheUtils.getConfigMap(BackConfigParams.WEBSITE);
                System.out.println("##map:" + intervalMap);
                model.addAttribute("content", getAppConfig(request.getParameter("appName"), "ABOUT_ME"));
                model.addAttribute("introduce", getAppConfig(request.getParameter("appName"), "ABOUT_INTRODUCE"));
                // 客服电话
                model.addAttribute("tel", intervalMap.get("service_phone"));
                // qq群
                model.addAttribute("qq", intervalMap.get("qq_group"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "content/about";
    }

    /**
     * 我的邀请
     */
    @RequestMapping("page/detail")
    public String userInvitation() {
        return "content/invitation";
    }

    private HashMap<String, Object> createMap(String userId, String userName, String userPhone, String data) {
        List<UserContactsVo> list = JSONUtil.jsonArrayToBean(data, UserContactsVo.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("userPhone", userPhone);
        map.put("concatList", list);
        map.put("createTime", new Date());
        return map;
    }

    /**
     * IOS-通讯表
     */
    @RequestMapping("credit-info/up-load-contacts")
    public void userContacts(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Map<String, Object>> dataMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
        String code = "-1";
        String msg = "";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                try {
                    String data = request.getParameter("data");
                    if (data != null && !"".equals(data)) {
                        String userName;
                        if (user.getRealname() != null) {
                            userName = user.getRealname();
                        } else {
                            userName = user.getUserPhone();
                        }
                        User userNew = new User();
                        userNew.setId(user.getId());
                        // 把联系人数量入库user_info
                        userNew.setUserContactSize(userContactsService.batchInsert(createMap(user.getId(), userName, user.getUserPhone(), data)) + "");
                        this.userService.updateByPrimaryKeyUser(userNew);
                        try{
                            sendContactInfo(userNew);
                        }catch (Exception e){
                            log.error(" send risk error:{}",e);
                        }

                    }
                    code = "0";
                    msg = "success";
                } catch (Exception e) {
                    log.error("userContacts error:", e);
                }
            }
        } catch (Exception e) {
            log.error("userContacts error:", e);
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * ANDROID-1.短信，2.APP，3.通讯录
     */
    @RequestMapping("credit-info/up-load-contents")
    public void upLoadContents(HttpServletRequest request, HttpServletResponse response) {
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
        String code = "-1";
        String msg = "失败";
        try {

            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                String data = request.getParameter("data");
                Integer userId = Integer.parseInt(logUser.getId());

                log.info("upLoadContents userId:" + userId);

                User user = userService.searchByUserid(userId);
                log.info("upLoadContents,userId:" + user.getId() + ",type:" + request.getParameter("type"));
                // 短信
                if ("1".equals(request.getParameter("type"))) {
                    int count = this.obtainUserShortMessageService.selectObtainUserShortMessageCount(userId);
                    // 先去库里查询用户是否有短信存在 有就删除再入库
                    if (count > 0) {
                        if (data != null && !"".equals(data)) {
                            this.obtainUserShortMessageService.delObtainUserShortMessage(userId);
                            JSONArray jsonArr = JSONArray.fromObject(data);
                            List<ObtainUserShortMessage> shortMsgList = new ArrayList<>();
                            for (int i = 0; i < jsonArr.size(); i++) {
                                JSONObject jsonObj = JSONObject.fromObject(jsonArr.get(i));
                                ObtainUserShortMessage shortMsg = new ObtainUserShortMessage();
                                shortMsg.setUserId(logUser.getId());
                                shortMsg.setPhone(jsonObj.get("phone") + "");
                                shortMsg.setMessageContent(UserContactsVo.setContactName(jsonObj.get("messageContent") == null ? "" : jsonObj.get("messageContent").toString().replaceAll("[^\u4E00-\u9FA5||0-9||a-zA-Z||\\pP]", "")));
                                shortMsg.setMessageDate(jsonObj.get("messageDate") + "");

                                shortMsgList.add(shortMsg);
                            }
                            if ( 0 < shortMsgList.size()) {
                                this.obtainUserShortMessageService.saveObtainUserShortMessage(shortMsgList);
                                code = "0";
                                msg = "success";
                                loginSucc(request, user);
                                log.info("用户" + user.getId() + "，短信上传Size:" + shortMsgList.size());
                            }
                        }
                    } else {// 短信 新增 入库
                        if (data != null && !"".equals(data)) {
                            JSONArray jsonArr = JSONArray.fromObject(data);
                            List<ObtainUserShortMessage> shortMsgList = new ArrayList<>();
                            for (int i = 0; i < jsonArr.size(); i++) {
                                JSONObject jsonObj = JSONObject.fromObject(jsonArr.get(i));
                                ObtainUserShortMessage shortMsg = new ObtainUserShortMessage();
                                shortMsg.setUserId(logUser.getId());
                                shortMsg.setPhone(jsonObj.get("phone") + "");
                                shortMsg.setMessageContent(UserContactsVo.setContactName(jsonObj.get("messageContent") == null ? "" : jsonObj.get("messageContent").toString().replaceAll("[^\u4E00-\u9FA5||0-9||a-zA-Z||\\pP]", "")));
                                shortMsg.setMessageDate(jsonObj.get("messageDate") + "");

                                shortMsgList.add(shortMsg);
                            }
                            if ( 0 < shortMsgList.size()) {
                                this.obtainUserShortMessageService.saveObtainUserShortMessage(shortMsgList);
                                code = "0";
                                msg = "success";
                                loginSucc(request, user);
                                log.info("用户" + user.getId() + "，短信上传Size:" + shortMsgList.size());
                            }
                        }
                    }
                    // APP应用
                } else if ("2".equals(request.getParameter("type"))) {
                    int count = this.userAppSoftwareService.selectUserAppSoftwareCount(userId);
                    // 先去库里查询用户是否有APP应用存在 有就删除再入库
                    if (count > 0) {
                        if (data != null && !"".equals(data)) {
                            this.userAppSoftwareService.delUserAppSoftware(userId);
                            JSONArray jsonArr = JSONArray.fromObject(data);
                            List<UserAppSoftware> appSoftList = new ArrayList<>();
                            for (int i = 0; i < jsonArr.size(); i++) {
                                JSONObject jsonObj = JSONObject.fromObject(jsonArr.get(i));
                                UserAppSoftware appSoft = new UserAppSoftware();
                                appSoft.setUserId(logUser.getId());
                                appSoft.setAppName(jsonObj.get("appName") + "");
                                appSoft.setPackageName(jsonObj.get("packageName") + "");
                                appSoft.setVersionName(jsonObj.get("versionName") + "");
                                appSoft.setVersionCode(jsonObj.get("versionCode") + "");

                                appSoftList.add(appSoft);
                            }
                            if ( 0 < appSoftList.size()) {
                                this.userAppSoftwareService.saveUserAppSoftware(appSoftList);
                                code = "0";
                                msg = "success";
                                loginSucc(request, user);
                                log.info("用户" + user.getId() + "，App应用Size:" + appSoftList.size());
                            }
                        }
                    } else {// App应用 新增 入库
                        if (data != null && !"".equals(data)) {
                            JSONArray jsonArr = JSONArray.fromObject(data);
                            List<UserAppSoftware> appSoftList = new ArrayList<>();
                            for (int i = 0; i < jsonArr.size(); i++) {
                                JSONObject jsonObj = JSONObject.fromObject(jsonArr.get(i));
                                UserAppSoftware appSoft = new UserAppSoftware();
                                appSoft.setUserId(logUser.getId());
                                appSoft.setAppName(jsonObj.get("appName") + "");
                                appSoft.setPackageName(jsonObj.get("packageName") + "");
                                appSoft.setVersionName(jsonObj.get("versionName") + "");
                                appSoft.setVersionCode(jsonObj.get("versionCode") + "");

                                appSoftList.add(appSoft);
                            }
                            if ( 0 < appSoftList.size()) {
                                this.userAppSoftwareService.saveUserAppSoftware(appSoftList);
                                code = "0";
                                msg = "success";
                                loginSucc(request, user);
                                log.info("用户" + user.getId() + "，App应用Size:" + appSoftList.size());
                            }
                        }
                    }
                } else {// 通讯录
                    if (data != null && !"".equals(data)) {
                        String userName = null;
                        if (user.getRealname() != null) {
                            userName = user.getRealname();
                        } else {
                            userName = user.getUserPhone();
                        }
                        User userNew = new User();
                        userNew.setId(user.getId());
                        // 把联系人数量入库user_info
                        userNew.setUserContactSize(userContactsService.batchInsert(createMap(user.getId(), userName, user.getUserPhone(), data)) + "");
                        this.userService.updateByPrimaryKeyUser(userNew);
                        try{
                            sendContactInfo(userNew);
                        }catch (Exception e){
                            log.error(" send risk error:{}",e);
                        }
                        code = "0";
                        msg = "success";
                    }
                }
            }
        } catch (Exception e) {
            log.error("android请求类型：" + request.getParameter("type") + " error:", e);
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 修改收集建议的状态
     */
    @RequestMapping("credit-info/collection_advise")
    public void knowUpdate(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String code = "-1";
        String msg = "";
        try {
            String id = request.getParameter("id");
            String csjy = request.getParameter("collection_advise");
            log.info("催收建议 id=" + id + " csjx=" + csjy);
            if (StringUtils.isBlank(id)) {
                msg = "无用户ID";
                return;
            }
            if (StringUtils.isBlank(csjy)) {
                msg = "无修改状态";
                return;
            }
            Integer uId = null;
            try {
                uId = Integer.parseInt(id);
            } catch (Exception e) {
                log.info("knowUpdate - exception uId = " + id);
            }
            User user = null;
            if (null != uId) {
                user = userService.searchByUserid(uId);
            }
            if (user != null) {
                user.setCsjy(csjy);
                int count = this.userService.updateByPrimaryKeyUser(user);
                if (count > 0) {
                    code = "0";
                    msg = "修改成功";
                    loginSucc(request, user);
                } else {
                    code = "-1";
                    msg = "修改失败";
                }
            } else {
                msg = "用户不存在";
            }
        } catch (Exception e) {
            log.error("knowUpdate error:{}",e);
        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 上传地址经伟度
     */
    @RequestMapping("credit-info/upload-location")
    public void userAddressLocation(HttpServletRequest request, HttpServletResponse response) {
        Map<String, HashMap<String, Object>> dataMap = new HashMap<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        JSONObject json = new JSONObject();
        String code = "-1";
        String msg = "";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                String longitude = request.getParameter("longitude") == null ? "" : request.getParameter("longitude");
                String latitude = request.getParameter("latitude") == null ? "" : request.getParameter("latitude");
                String address = request.getParameter("address") == null ? "" : request.getParameter("address");
                String time = request.getParameter("time") == null ? "" : request.getParameter("time");
                if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
                    User updateUser = new User();
                    updateUser.setPresentLongitude(longitude);
                    updateUser.setPresentLatitude(latitude);
                    updateUser.setId(user.getId());
                    // user.setPresentAddress(address);
                    userService.updateByPrimaryKeyUser(updateUser);
                    //原来更新坐标信息更改为增量添加坐标信息
                    UserInfoTude userInfoTude = new UserInfoTude();
                    userInfoTude.setUserId(Integer.parseInt(logUser.getId()));
                    userInfoTude.setPresentLatitude(latitude);
                    userInfoTude.setPresentLongitude(longitude);
                    userInfoTude.setPresentAddressDistinct(address);
                    if (StringUtils.isNotBlank(time)) {
                        userInfoTude.setAppCreatTime(DateUtil.getDate(time, "yyyy-MM-dd HH:mm:ss"));
                    }
                    userInfoTudeService.insertUserInfoTude(userInfoTude);
                    code = ResponseStatus.SUCCESS.getName();
                    msg = ResponseStatus.SUCCESS.getValue();
                } else {
                    msg = "请传入完整的信息";
                }
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = ResponseStatus.LOGIN.getValue();
            }
        } catch (Exception e) {
            log.error("userAddressLocation error:", e);
        } finally {
            dataMap.put("item", resultMap);
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * H5 注册界面
     */
    @RequestMapping("act/little-fish-register")
    public String gotoRegisterNew(HttpServletRequest request, Model model) {
        try {
            String inviteUserid = request.getParameter("invite_code");
            String userFrom = request.getParameter("user_from");
            model.addAttribute("user_from", userFrom);
            model.addAttribute("invite_code", inviteUserid);
            String token = request.getParameter("token");
            if (StringUtils.isNotBlank(token)) {
                model.addAttribute("token", token);
            }
            //添加uv 统计功能
            String channelId = AESUtil.decrypt(userFrom,AESUtil.KEY_USER_FROM);
            String ip = RequestUtils.getIpAddr(request);
            String key = jedisCluster.get("UV_REPORT_" + ip);

            if (StringUtils.isBlank(key)) {
                int updateUV = channelReportService.updateUVCountByChannelId(channelId);
                if (updateUV > 0)
                    jedisCluster.set("UV_REPORT_" + ip, "true");
            }

            //判断该渠道是否是开启状态
            ChannelInfo channelInfo = channelInfoService.findById(Integer.valueOf(channelId));
             if(channelInfo != null){
                 model.addAttribute("status",channelInfo.getStatus());
                 model.addAttribute("qqStatus",channelInfo.getQqStatus());
                 model.addAttribute("wechatStatus",channelInfo.getWechatStatus());
             }
            /*ChannelReport channelReport = channelReportService.findChannelReportById(Integer.valueOf(channelId));
            if(null != channelReport.getUvCount()){
                if(channelReport.getUvCount() == ' '){
                    channelReport.setUvCount(0);
                }
                channelReport.setUvCount(channelReport.getUvCount()+1);
                channelReportService.insert(channelReport);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("appKey", request.getParameter("appName"));
        model.addAttribute("appName", getAppConfig(request.getParameter("appName"), "APP_TITLE"));
        model.addAttribute("companyTitle", getAppConfig(request.getParameter("appName"), "COMPANY_TITLE"));
        //备案号
        model.addAttribute("bah", getAppConfig(request.getParameter("appName"), "XJX_BAH"));
        model.addAttribute("RCaptchaKey", "R" + request.getSession().getId());
        AppDownloadConfig config = appDownloadConfigService.selectAppConfigSelective(new HashMap() {{
            put("status", "1");
        }});
        model.addAttribute("androidUrl", config.getAndroidUrl());
        model.addAttribute("iosUrl", config.getIosUrl());
        return "user/register";
    }

    /**
     * H5 注册验证码
     */
    @RequestMapping("act/light-loan-xjx/registerCode")
    public void registerCode(HttpServletRequest request, HttpServletResponse response) {
        String registerCodeCheck = "h5_register_code_check";
        JSONObject json = new JSONObject();
        Map<String, Object> dataMap = this.getParametersO(request);
        Printer.print("UserLoginController#registerCode", dataMap);
        HashMap<String, Object> map = new HashMap<>();
      /* //判断该渠道是否是开启状态
        String userFrom = request.getParameter("user_from");
        String channelId = AESUtil.decrypt(userFrom,AESUtil.KEY_USER_FROM);
        ChannelInfo channelInfo = channelInfoService.findById(Integer.valueOf(channelId));
        if(channelInfo != null){
            json.put("status",channelInfo.getStatus());
        }*/
        String msg = "";
        String code = "-1";
        String userPhone = "";

        try {
            userPhone = null == request.getParameter("phone") ? "" : request.getParameter("phone");
            map.put("userPhone", userPhone);
            if (StringUtils.isBlank(userPhone)) {
                msg = "请输入手机号码";
                return;
            }

            Long remainTime = checkForFront(registerCodeCheck, userPhone);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            User phone = new User();
            phone.setUserPhone(userPhone);
            // 查询手机号码是否存在
            User user = userService.searchUserByCheckTel(map);
            // 6位固定长度
            String rand = "123456";
            if ("online".equals(PropertiesConfigUtil.get("profile"))) {
                rand = String.valueOf(Math.random()).substring(2).substring(0, 6);
            }
            String content = "";
            // 注册验证码
            if (user == null) {

                // 一分钟是否已经发送过验证码的key
                String hasSendOneMinKey = SMS_SEND_IN_ONE_MINUTE + userPhone;
                if (jedisCluster.get(hasSendOneMinKey) != null) {
                    msg = "请一分钟后再尝试发送";
                } else {
                    ResponseContent serviceResult = check(userPhone);
                    if (serviceResult.isFail()) {
                        msg = serviceResult.getMsg();
                    } else {
                        jedisCluster.setex(hasSendOneMinKey, 60, userPhone);
                        // 存入redis
                        jedisCluster.set(SMS_REGISTER_PREFIX + userPhone, rand);
                        jedisCluster.expire(SMS_REGISTER_PREFIX + userPhone, INFECTIVE_SMS_TIME);
                        content = rand + "##正在注册" + getAppConfig(request.getParameter("appName"), "APP_TITLE") + "账户";

                        code = "0";
                        msg = "成功获取验证码";
                        try {
                            SendSmsUtil.sendSmsCL(userPhone, content);
                            msg = "成功获取验证码";
                        } catch (Exception e) {
                            log.error("registerCode send error:", e);
                            code = "-1";
                            msg = "信息发送失败稍后重试";
                        }
                    }
                }
            } else {
                msg = "手机号码已被注册";
            }
        } catch (Exception e) {
            log.error("registerCode error:", e);
            code = "500";
            msg = "系统异常";
        } finally {
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());

        }

    }

    /**
     * 语音短信验证码发送
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("act/light-loan-xjx/getsmsvoicecode")
    public void getSmsVoiceCode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        HashMap<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = this.getParametersO(request);
        String registerVoiceCode = "h5_register_voice_code_check";
        String code = "-1";
        String msg = "";
       /* //判断该渠道是否是开启状态
        String userFrom = request.getParameter("user_from");
        String channelId = AESUtil.decrypt(userFrom,AESUtil.KEY_USER_FROM);
        ChannelInfo channelInfo = channelInfoService.findById(Integer.valueOf(channelId));
        if(channelInfo != null){
            json.put("status",channelInfo.getStatus());
        }*/
        String userPhone = null == request.getParameter("phone") ? "" : request.getParameter("phone");
        // 4位语音验证码固定长度
        String voice_code = String.valueOf(Math.random()).substring(2).substring(0, 4);
        try {
            map.put("userPhone", userPhone);
            log.info("check userPhone!");
            if (StringUtils.isBlank(userPhone)) {
                msg = "请输入手机号码";
                return;
            }
            log.info("check validateCode!");

            log.info("check remainTime!");
            Long remainTime = checkForFront(registerVoiceCode, userPhone);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            // 查询手机号码是否存在
            User user = userService.searchUserByCheckTel(map);
            log.info("check user!");
            // 注册验证码
            if (user == null) {
                // 一分钟是否已经发送过验证码的key
                String hasSendOneMinKey = SMS_VOICE_SEND_IN_ONE_MINUTE + userPhone;
                if (jedisCluster.get(hasSendOneMinKey) != null) {
                    Long ttl = jedisCluster.ttl(hasSendOneMinKey);
                    code = "-1";
                    msg = "请" + ttl + "秒后再尝试发送";
                } else {
                    ResponseContent serviceResult = check(userPhone);
                    if (serviceResult.isFail()) {
                        msg = serviceResult.getMsg();
                        log.info("serviceResult not pass!msg:" + msg);
                    } else {
                        jedisCluster.setex(hasSendOneMinKey, 60, userPhone);
                        // 存入redis
                        jedisCluster.set(SMS_REGISTER_PREFIX + userPhone, voice_code);
                        jedisCluster.expire(SMS_REGISTER_PREFIX + userPhone, INFECTIVE_SMS_TIME);
                        String content = "您的语音验证码是" + voice_code;
                        Date sendTime = new Date();
                        try {
                            /*if (SendSmsUtil.sendSmsVoiceCode(userPhone, content)) {
                                code = "0";
                                msg = "语音验证码即将发送，请注意接听";
                                log.info("语音验证码发送成功=" + voice_code + "***" + msg);
                                log.info("注册语音验证码sendSms:" + userPhone + "-->" + voice_code);
                            } else {*/
                                msg = "获取验证码失败";
                            //}

                        } catch (Exception e) {
                            log.error("send voice code error", e);
                            code = "-1";
                            msg = "信息发送失败稍后重试";
                        }
                    }
                }
            } else {
                msg = "手机号码已被注册";
            }
        } catch (Exception e) {
            log.error("sms voice error", e);
            code = "500";
            msg = "系统异常";
        } finally {
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            log.info("sms voice return ,code:" + code + ",msg:" + msg);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * H5 用户注册
     *
     * @param request req
     * @param response res
     */
    @RequestMapping("act/light-loan-xjx/register")
    public void registerH5(HttpServletRequest request, HttpServletResponse response) {
        String registerCheck = "h5_register_check";
        JSONObject json = new JSONObject();
        Map<String, Object> dataMap = this.getParametersO(request);
        String msg = "";
        String code = "-1";
        String userPhone;
        try {
            String  qq_wechat = dataMap.get("qq_wechat")+"";
            //判断该渠道是否是开启状态
            String userFroms = request.getParameter("user_from");
            String channelIds = AESUtil.decrypt(userFroms,AESUtil.KEY_USER_FROM);
            ChannelInfo channelInfo = channelInfoService.findById(Integer.valueOf(channelIds));
            if(channelInfo != null){
                json.put("status",channelInfo.getStatus());
                //判断该渠道是否是关闭状态
                if(channelInfo.getStatus() == 2){
                    msg="该渠道已关闭,不能注册";
                    json.put("message", msg);
                    return;
                }
                //判断该渠道是否是从qq进入
                if(channelInfo.getQqStatus() == 1 && qq_wechat.equals("1")){
                   msg="该渠道暂不可用";
                   json.put("message",msg);
                   return;
                }
                //判断该渠道是否是从微信进入
                if(channelInfo.getWechatStatus() == 1 && qq_wechat.equals("2")){
                    msg="该渠道暂不可用";
                    json.put("message",msg);
                    return;
                }
            }
            userPhone = dataMap.get("phone") + "";
            // 获取密码
            String passWord = dataMap.get("password") + "";
            // 获取手机验证码
            String smsCode = dataMap.get("code") + "";
            // 邀请码
            String inviteUserid = dataMap.get("invite_code") + "";
            // 用户注册来源
            String userFrom = dataMap.get("user_from") + "";
            // 地推系统推送ID
            Object pushId = dataMap.get("pushId");
            //浏览器类型（1、android 2、ios 3、pc）
            String brower_type = dataMap.get("brower_type") + "";
            // 手机验证码验证
            if (StringUtils.isBlank(smsCode)) {
                msg = "手机验证码不能为空";
                return;
            }
            Long remainTime = checkForFront(registerCheck, userPhone, 2);
            if (remainTime > 0) {
                code = ResponseStatus.FREQUENT.getName();
                json.put("time", remainTime);
                msg = ResponseStatus.FREQUENT.getValue();
                return;
            }
            String sendSmsCode = jedisCluster.get(SMS_REGISTER_PREFIX + userPhone);
            if ("".equals(smsCode)) {
                msg = "验证码不能为空";
                return;
            } else if (StringUtils.isBlank(sendSmsCode)) {
                msg = "验证码已失效，请重新获取 ";
                return;
            } else if (!sendSmsCode.equals(smsCode)) {
                msg = "验证码校验失败";
                return;
            }
            User invitetUser = null;
            User user = new User();
            String md5 = MD5Util.MD5(AESUtil.encrypt(passWord, ""));
            // 密码不为空之后 做插入操作
            HashMap<String, Object> map = new HashMap<>();
            map.put("userPhone", userPhone);
            // 查询手机号码是否存在
            User users = this.userService.searchUserByCheckTel(map);
            if (users != null) {
                msg = "手机号已经存在";
                return;
            }
            user.setUserName(userPhone);
            user.setPassword(md5);
            user.setUserPhone(userPhone);
            user.setCreateIp(this.getIpAddr(request));
            user.setEquipmentNumber(userPhone);
            // 注册来源
//            user.setUserFrom(StringUtils.isNotBlank(userFrom)&&!"null".equals(userFrom)?userFrom:"0");
            if(StringUtils.isNotBlank(userFrom)){
                String channelId = AESUtil.decrypt(userFrom,AESUtil.KEY_USER_FROM);
                if(channelId==null){
                    log.info("old register userFrom :{}",userFrom);
                }
                user.setUserFrom(channelId==null?"-1":channelId);
            }else{
                /*
                 * 没有渠道来源 则为自然流量
                 */
                user.setUserFrom("0");
            }

            // 邀请注册时设置邀请人Id
            if (pushId != null) {
                user.setInviteUserid(pushId + "");
            } else {
                if (StringUtils.isNotBlank(inviteUserid) && !"null".equals(inviteUserid)) {
                    //Pattern.compile("^-?[1-9]\\d*$").matcher(Base64Utils.decodeStr(inviteUserid)).find()
                    String userId = new String(Base64.getDecoder().decode(inviteUserid.getBytes()));
                    if (PATTERN_USER_ID.matcher(userId).find()) {
                        // 邀请码的验证
                        Map<String, String> maps = new HashMap<>();
                        maps.put("id", userId);
                        invitetUser = this.userService.searchByInviteUserid(maps);
                    }

                    if (invitetUser == null) {
                        log.info("inviteUserid is null");
//							msg = "该邀请人不存在";
//							return;
                    } else {
                        // 邀请好友注册的ID
                        user.setInviteUserid(userId);
                    }
                }
            }
            String token = request.getParameter("token");
            if (StringUtils.isNotBlank(token)) {
                user.setTgFlag(token);
            }

            if (StringUtils.isNotBlank(brower_type)) {
                user.setBrowerType(Integer.parseInt(brower_type));
            }
            //注册终端类型（设备类型 1、Android 2、ios 3、wap 4、pc）
            user.setClientType(3);
            // 注册保存新用户
            //添加渠道来源 0 都不是 1 qq 2 微信
            user.setQqWechat(qq_wechat);
            userService.saveUser(user);
            /*添加地推用户，推广ID(地推系统推送的PUSHID)*/
//            try {
//                if (pushId != null) {
//
//                    //地推 注册
//                    HashMap<String, Object> maps = new HashMap<>();
//                    maps.put("userFrom", user.getUserFrom());
//                    maps.put("pushId", pushId);
//                    maps.put("userPhone", user.getUserPhone());
//                    ThreadPool pool = ThreadPool.getInstance();
//                    pool.execute(new DtThread(UserPushUntil.REGISTER, Integer.parseInt(user.getId()), null, new Date(), userService,
//                            pushUserService, null, maps));
//                }
//            } catch (Exception e) {
//                log.error("insertChanelUserPush error ", e);
//            }
            msg = "注册成功";
            code = "0";
        } catch (Exception e) {
            log.error("registerH5 error:", e);
            code = "500";
            msg = "系统异常，请稍后再试";
        } finally {
            json.put("code", code);
            json.put("message", msg);
            json.put("data", dataMap);
            JSONUtil.toObjectJson(response, json.toString());
        }
    }

    /**
     * 地推H5用户注册
     */
    @RequestMapping("act/light-loan-xjx-push")
    public String gotoRegisterNewPush(HttpServletRequest request, Model model) {
        try {
            // 来源
            String userFrom = request.getParameter("user_from");
            // 来源
            String pushId = request.getParameter("pushId");
            // 来源
            model.addAttribute("user_from", userFrom);
            // 邀请码
            model.addAttribute("invite_code", pushId);
            // 邀请码
            model.addAttribute("pushId", pushId);
            model.addAttribute("RCaptchaKey", "R" + request.getSession().getId());
        } catch (Exception e) {
            log.error("gotoRegisterNewPush error:{}",e);
        }
        return "user/registerPush";
    }

    /**
     * H5 注册成功界面
     */
    @RequestMapping("act/light-loan-xjx/succ")
    public String registerSuccs(Model model) {
        AppDownloadConfig config = appDownloadConfigService.selectAppConfigSelective(new HashMap() {{
            put("status", "1");
        }});
        model.addAttribute("androidUrl", config.getAndroidUrl());
        model.addAttribute("iosUrl", config.getIosUrl());
        return "user/registerSucc";
    }


    /**
     * 服务费计算
     * @param response res
     * @param loanTerm loanTerm
     * @param moneyAmount money
     */
    @RequestMapping("credit-user/service-charge")
    public void serviceCharge(HttpServletResponse response, Integer loanTerm, Integer moneyAmount) {
        try {
            //用户不登录 也让看到各种费用信息
            List<Map<String, Object>> list = new ArrayList<>();
            if (null == moneyAmount || moneyAmount == 0) {
                Map<String, Object> fees = new HashMap<>();
                fees.put("name", "互保准备金");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "快速信审费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "平台使用费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "代收通道费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "账户管理费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "合计");
                fees.put("value", "0.00");
                list.add(fees);
            } else {
                DecimalFormat df = new DecimalFormat("######0.00");
                moneyAmount = moneyAmount * 100;
                if (Constant.LOAN_DAYS_7 == loanTerm) {
                    Map<String, Object> fees = new HashMap<>();
                    fees.put("name", "互保准备金");
                    fees.put("value", df.format((moneyAmount / 100) * 0.005));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "快速信审费");
                    fees.put("value", df.format((moneyAmount / 100) * 0.04));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "平台使用费");
                    fees.put("value", df.format((moneyAmount / 100) * 0.04));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "代收通道费");
                    fees.put("value", df.format((moneyAmount / 100) * 0.07));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "账户管理费");
                    fees.put("value", df.format((moneyAmount / 100) * 0.04));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "合计");
                    fees.put("value", df.format((moneyAmount / 100) * 0.195));
                    list.add(fees);
                } else {
                    Map<String, Object> fees = new HashMap<>();
                    fees.put("name", "互保准备金");
                    fees.put("value", df.format((moneyAmount / 100 - 2) * 0.7 + 1.4));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "额度审核费");
                    fees.put("value", df.format((moneyAmount / 100 - 2) * 2 + 5));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "征信查询费");
                    fees.put("value", df.format((moneyAmount / 100 - 2) * 4 + 6));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "贷后管理费");
                    fees.put("value", df.format((moneyAmount / 100 - 2) * 4 + 8));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "介绍费");
                    fees.put("value", df.format((moneyAmount / 100 - 2) * 4.3 + 9.6));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "合计");
                    fees.put("value", df.format((moneyAmount / 100 - 2) * 15 + 30));
                    list.add(fees);
                }
            }
            renderAppJson(response, list);
        }
        catch (Exception e) {
            log.error("获取服务费计算错误 loanTerm=" + loanTerm + " moneyAmount=" + moneyAmount, e);
        }
    }


    @RequestMapping("credit-user/v2/service-charge")
    public void serviceNewCharge(HttpServletResponse response, Integer loanTerm, Integer moneyAmount) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            if (null != moneyAmount && moneyAmount != 0) {
                DecimalFormat df = new DecimalFormat("######0.00");
                moneyAmount = moneyAmount * 100;
                BorrowProductConfig borrowProductConfig = borrowProductConfigDao.queryByBorrowDayAndAmount(new BigDecimal(moneyAmount), loanTerm);
                if (borrowProductConfig != null) {
                    Map<String, Object> fees = new HashMap<>();
                    fees.put("name", "互保准备金");
                    fees.put("value", df.format(borrowProductConfig.getBorrowInterest().divide(new BigDecimal(100))));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "快速信审费");
                    fees.put("value", df.format(borrowProductConfig.getTurstTrial().divide(new BigDecimal(100))));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "平台使用费");
                    fees.put("value", df.format(borrowProductConfig.getPlatformLicensing().divide(new BigDecimal(100))));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "代收通道费");
                    fees.put("value", df.format(borrowProductConfig.getCollectChannelFee().divide(new BigDecimal(100))));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "账户管理费");
                    fees.put("value", df.format(borrowProductConfig.getAccountManagerFee().divide(new BigDecimal(100))));
                    list.add(fees);
                    fees = new HashMap<>();
                    fees.put("name", "合计");
                    fees.put("value", df.format(borrowProductConfig.getTotalFeeRate().divide(new BigDecimal(100))));
                    list.add(fees);
                }
            }
            if (list.size() == 0) {
                Map<String, Object> fees = new HashMap<>();
                fees.put("name", "互保准备金");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "快速信审费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "平台使用费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "代收通道费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "账户管理费");
                fees.put("value", "0.00");
                list.add(fees);
                fees = new HashMap<>();
                fees.put("name", "合计");
                fees.put("value", "0.00");
                list.add(fees);
            }
            renderAppJson(response, list);
        }
        catch (Exception e) {
            log.error("获取服务费计算错误v2 loanTerm=" + loanTerm + " moneyAmount=" + moneyAmount, e);
        }
    }

    /**
     * 验证码
     *
     * @param request req
     * @param response res
     * @return b
     */
    private boolean validateSubmitAPP(HttpServletRequest request, HttpServletResponse response) {
        try {
            String captchaKey = request.getParameter("RCaptchaKey");
            String captcha = jedisCluster.get(captchaKey);
            log.info("RCaptchaKey=" + captchaKey + " captcha1=" + captcha + "   captcha2=" + request.getParameter("captcha").toLowerCase());
            if (captcha != null && captcha.equals(request.getParameter("captcha").toLowerCase())) {
                jedisCluster.del(captchaKey);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("validateSubmitAPP error:", e);
            return false;
        }
    }

    @RequestMapping(value = "getTddata")
    public void getTdrawdata(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        if (StringUtils.isBlank(userId)) {
            JSONUtil.toObjectJson(response, "请输入参数userId");
            return;
        }

        User user = userService.searchByUserid(Integer.parseInt(userId));
        if (user == null) {
            JSONUtil.toObjectJson(response, "无法获取该用户信息");
            return;
        }

        JSONUtil.toObjectJson(response, user.getTdRawData());
    }

    @RequestMapping(value = "getRiskdata")
    public void getRiskdata(HttpServletRequest request, HttpServletResponse response) {
        String borrowId = request.getParameter("borrowId");
        if (StringUtils.isBlank(borrowId)) {
            JSONUtil.toObjectJson(response, "请输入参数borrowId");
            return;
        }

        RiskOrders riskOrders = riskOrdersDao.selectCreditReportByBorrowId(Integer.parseInt(borrowId));
        if (riskOrders == null) {
            JSONUtil.toObjectJson(response, "无法获取数据");
            return;
        }

        JSONUtil.toObjectJson(response, riskOrders.getReturnParams());
    }
}
