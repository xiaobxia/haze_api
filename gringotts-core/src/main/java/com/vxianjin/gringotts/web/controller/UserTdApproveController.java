package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.dao.UserQuotaSnapshotDao;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.IdUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IBackConfigParamsService;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IMoneyLimitService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.util.SendSmsUtil;
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
import java.util.*;


/**
 * 同盾用户运营商认证
 *
 * @author zed
 */
@Controller
public class UserTdApproveController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserTdApproveController.class);

    @Resource
    private IUserService userService;
    @Resource
    private IBorrowOrderService borrowOrderService;

    @Resource
    private IMoneyLimitService moneyLimitService;
    @Resource
    private IUserDao userDao;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private BorrowProductConfigDao borrowProductConfigDao;
    @Resource
    private UserQuotaSnapshotDao userQuotaSnapshotDao;

    /**
     * 运营商认证请求接口
     *
     * @param request req
     * @param model model
     * @return str
     */
    @RequestMapping("/credit-web/verification-td")
    public String verificationTd(HttpServletRequest request, Model model) {
        logger.info("UserTdApproveController verificationTd start");
        String recode = request.getParameter("recode");
        String code = "500";
        String clientType = request.getParameter("clientType");
        User user = this.loginFrontUserByDeiceId(request);
        String deviceId = request.getParameter("deviceId");
        String mobilePhone = request.getParameter("mobilePhone");
        String userId = user.getId();
        if (clientType != null && "ios".equals(clientType)) {
            clientType = "ios";
        } else {
            clientType = "andriod";
        }
        try {
            //判断用户是否登录
            if (user == null) {
                code = "400";
                model.addAttribute("recode", recode);
                model.addAttribute("code", code);
                model.addAttribute("clientType", clientType);
                return "userinfo/userTdApproveSucc";
            }
            user = userService.searchByUserid(Integer.valueOf(user.getId()));
            //同盾运营商已认证状态
            if ("2".equals(user.getTdStatus())) {
                code = "200";
                model.addAttribute("code", code);
                model.addAttribute("recode", recode);
                model.addAttribute("clientType", clientType);
                return "userinfo/userTdApproveSucc";
            }
            //同盾运营商认证中状态
            if ("3".equals(user.getTdStatus())) {
                code = "700";
                model.addAttribute("code", code);
                model.addAttribute("recode", 2);
                model.addAttribute("clientType", clientType);
                //当用户运营商认证时间超过3分钟，自动设置为1，未认证
                if (DateUtil.minutesBetween(user.getTdVerifyTime(), new Date()) > 3) {
                    User userNew = new User();
                    userNew.setId(user.getId());
                    userNew.setTdStatus("1");
                    userDao.updateTdStatus(userNew);
                }
                return "userinfo/userTdApproveSucc";
            }
            //认证失败
            if ("-1".equals(user.getTdStatus()) && null != user.getTdVerifyTime()) {

                if (DateUtil.daysBetween(user.getTdVerifyTime(), new Date()) >= 1) {
                    code = "-1";
                } else {
                    code = "-2";
                }
                recode = "2";
            }
            //同盾运营商未认证状态
            if ("1".equals(user.getTdStatus())) {
                recode = "2";
                code = "0";
            }

        } catch (Exception e) {
            logger.error("UserTdApproveController verificationTd error deviceId=" + deviceId, e);
            code = "500";
        }
        String mobileUrl = "";
        Map<String,Object> map = new HashMap<>();
        map.put("orderNo", System.currentTimeMillis()+IdUtil.generateRandomStr(6));
        map.put("consumerNo",PropertiesConfigUtil.get("RISK_BUSINESS")+userId);

        JSONObject riskObject = JSON.parseObject(HttpUtil.sendPost(PropertiesConfigUtil.get("risk_yys_url"),map));
        if (null != riskObject) {
            if ("0000".equals(riskObject.getString("code"))) {
                String requestUrl = riskObject.getJSONObject("data").getString("url");
                logger.info("UserTdApproveController requestUrl=" + requestUrl);
                if (StringUtils.isNotEmpty(requestUrl)) {
                    mobileUrl =requestUrl;
                }
            }
        }
        logger.info("UserTdApproveController verificationTd code = " + code);
        logger.info("UserTdApproveController verificationTd status = " + user.getTdStatus());
        model.addAttribute("clientType", clientType);
        model.addAttribute("mobilePhone", mobilePhone);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("recode", recode);
        model.addAttribute("code", code);
        model.addAttribute("mobileUrl", mobileUrl);
        model.addAttribute("userPhone", user.getUserPhone());
        return "userinfo/userTdApproveSucc";
    }

    /**
     * 运营商认证完着陆页面
     *
     * @param request req
     * @param model model
     * @return str
     */
    @RequestMapping("/credit-web/verification-td-callback")
    public String verificationTdCallback(HttpServletRequest request, Model model) {
        logger.info("UserTdApproveController verification-td-callback start");
        String recode = request.getParameter("recode");
        String code = "500";
        String clientType = request.getParameter("clientType");
        String deviceId = request.getParameter("deviceId");
        String mobilePhone = request.getParameter("mobilePhone");
        //同盾运营商任务查询id
        String taskId = request.getParameter("task_id");
        User user = this.loginFrontUserByDeiceId(request);
        logger.info("UserTdApproveController verification-td-callback userId = " + user.getId());
        logger.info("UserTdApproveController verification-td-callback task_id = " + taskId);
        if (clientType != null && "ios".equals(clientType)) {
            clientType = "ios";
        } else {
            clientType = "andriod";
        }
        //判断用户是否登录
        if (user == null) {
            code = "400";
            model.addAttribute("recode", recode);
            model.addAttribute("code", code);
            model.addAttribute("clientType", clientType);
            return "userinfo/userTdApproveSucc";
        }

        user = userService.searchByUserid(Integer.valueOf(user.getId()));

        logger.info("UserTdApproveController verification-td-callback tdStatus = " + user.getTdStatus());
        //同盾运营商已认证
        if ("2".equals(user.getTdStatus())) {
            code = "200";
            model.addAttribute("code", code);
            model.addAttribute("recode", "3");
            model.addAttribute("clientType", clientType);
            return "userinfo/userTdApproveSucc";
        }
        try {
            if (StringUtils.isNotBlank(taskId)) {
                code = "700";
                User newUser = new User();
                newUser.setId(user.getId());
                //认证中
                newUser.setTdStatus("3");
                newUser.setTdTaskId(taskId);
                newUser.setTdVerifyTime(new Date());
                newUser.setTdVerifyNextTime(DateUtil.addDay(newUser.getTdVerifyTime(), 30));
                //修改用户状态
                userService.updateTd(newUser);
            }
        } catch (Exception e) {
            logger.error("UserTdApproveController verification-td-callback error : ", e);
        }

        model.addAttribute("clientType", clientType);
        model.addAttribute("mobilePhone", mobilePhone);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("code", code);
        model.addAttribute("userPhone", user.getUserPhone());
        model.addAttribute("recode", recode);
        return "userinfo/userTdApproveSucc";
    }

    /**
     * 运营商认证异步回调接口
     *
     * @param request req
     * @param response res
     */
    @PostMapping(value = "/credit-web/mobileYysCallback")
    public void mobileTdCallback(HttpServletRequest request,
                                 HttpServletResponse response) {
        logger.info("mobileTdCallback start");
        try {
            String code = request.getParameter("code");
            logger.info("data str:{}",request.getParameter("data"));
            JSONObject jsonObject = JSON.parseObject(request.getParameter("data"));
            logger.info("jsonObject:{}",jsonObject.toJSONString());
            String status = jsonObject.getString("result");
            String consumerNo = jsonObject.getString("consumerNo");
            String gxbToken = jsonObject.getString("gxbToken");
            logger.info("mobileTdCallback code:{}",code);
            logger.info("data:{}",request.getParameter("data"));
            String userId = consumerNo.replace(PropertiesConfigUtil.get("RISK_BUSINESS"),"");
            logger.info("mobileTdCallback userId = " + userId);
            User user = userService.searchByUserid(Integer.parseInt(userId));

            if (user == null) {
                HashMap<String, String> resMap = new HashMap<>();
                resMap.put("code", "500");
                resMap.put("message", "无效的用户参数");
                logger.error("mobileTdCallback resMap = " + JSON.toJSONString(resMap));
                JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
                return;
            }

            //成功或者失败返回，避免重复回调
            if (("2".equals(user.getNewFlag()) && "2".equals(user.getTdStatus())) || "-1".equals(user.getTdStatus())) {
                HashMap<String, String> resMap = new HashMap<>();
                resMap.put("code", "0");
                resMap.put("message", "success");
                logger.info("mobileTdCallback chongfu userId = " + userId);
                JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
                return;
            }
            //认证成功
            if ("0000".equals(code) && "10".equals(status)) {
                //认证成功
                User newUser = new User();
                newUser.setId(userId);
                newUser.setTdStatus("2");
                newUser.setTdRawData("");
                newUser.setTdVerifyTime(new Date());
                newUser.setTdVerifyNextTime(DateUtil.addDay(newUser.getTdVerifyTime(), 30));
                logger.info("mobileTdCallback newUser = " + newUser.getTdStatus());
                userService.updateTd(newUser);

                //判断芝麻认证和同盾运营商认证是否都认证完，如果都认证完，则给予默认额度
                logger.info("mobileTdCallback userId=" + user.getId() + " zmStatus=" + user.getZmStatus() + " tdStatus=" + newUser.getTdStatus());
                /*if ("2".equals(newUser.getTdStatus())) {
                    //更新newFlag
                    userDao.updateUserNewFlagById(newUser);
                    //初始额度
                    String amountMax = "1000";

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());

                    map.put("newAmountMax", new BigDecimal(amountMax).multiply(
                            new BigDecimal(100)).intValue());

                    if ("1".equals(user.getNewFlag())) {
                        // 用户额度更新
                            *//*logger.info("update user quotaSnapShot start userId: " + user.getId());
                            quotaSnapshotService.updateUserQuotaSnapshots(user);
                            logger.info("update user quotaSnapShot end userId: " + user.getId());*//*
                        logger.info("mobileTdCallback changeUserLimit start userId=" + userId);
                        borrowOrderService.changeUserLimit(map);
                        //初始化用户额度配置
                        try {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("borrowAmount", "100000");
                            params.put("borrowDay", "7");
                            int configId = borrowProductConfigDao.selectConfigId(params);
                            logger.info("addUserQuota params:userId" + user.getId() + ";configId:" + configId + ";moneyLimit:" + 100000);
                            userQuotaSnapshotDao.addUserQuota(Integer.valueOf(user.getId()), configId, new BigDecimal("100000"), 7);
                        } catch (Exception e) {
                            logger.error("addUserQuota has error:{}", e);
                        }
                    }
                    logger.info("mobileTdCallback success");

                    final int USER_ID = Integer.parseInt(userId);

                    ThreadPool.getInstance().execute(() -> {
                        logger.info("mobileTdCallback thread run start userId = " + USER_ID);
                        //发送风控信审操作
                        moneyLimitService.dealEd(USER_ID + "", gxbToken);
                    });
                }*/
                BorrowProductConfig borrowProductConfig = borrowProductConfigDao.queryByBorrowByStatus(0);
                if ("2".equals(newUser.getTdStatus())) {
                    //更新newFlag
                    userDao.updateUserNewFlagById(newUser);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());
                    map.put("newAmountMax", borrowProductConfig.getBorrowAmount().intValue());

                    if ("1".equals(user.getNewFlag())) {
                        // 用户额度更新 认证完
                       /* logger.info("update user quotaSnapShot start userId: " + user.getId());
                        quotaSnapshotService.updateUserQuotaSnapshots(user);
                        logger.info("update user quotaSnapShot end userId: " + user.getId());*/

                        logger.info("createGXBNotifyCallback changeUserLimit start userId=" + userId);
                        borrowOrderService.changeUserLimit(map);
                        logger.info("createGXBNotifyCallback changeUserLimit end userId=" + userId);
                        try{
                            //初始化用户额度配置
                            /*HashMap<String, String> params = new HashMap<>();
                            params.put("borrowAmount","160000");
                            params.put("borrowDay","7");*/
                            logger.info("addUserQuota params:userId" + user.getId() + ";configId:" + borrowProductConfig.getId() + ";moneyLimit:" + borrowProductConfig.getBorrowAmount());
                            userQuotaSnapshotDao.addUserQuota(Integer.valueOf(user.getId()),borrowProductConfig.getId(),borrowProductConfig.getBorrowAmount(),borrowProductConfig.getBorrowDay());
                        }catch (Exception e){
                            logger.error("addUserQuota has error:{}" , e);
                        }
                    }
                    logger.info("createGXBNotifyCallback success");

                    final int USER_ID = Integer.parseInt(userId);

                    ThreadPool.getInstance().execute(() -> {
                        logger.info("createGXBNotifyCallback thread run start userId = " + USER_ID);
                        //发送风控信审操作
                        moneyLimitService.dealEd(USER_ID + "", gxbToken);
                    });
                }

            }else{
                //将认证中的状态改为认证失败状态
                if ("3".equals(user.getTdStatus())) {
                    User newUser = new User();
                    newUser.setId(userId);
                    //认证失败
                    newUser.setTdStatus("-1");
                    newUser.setTdRawData("");
                    newUser.setTdVerifyTime(new Date());
                    newUser.setTdVerifyNextTime(DateUtil.addDay(newUser.getTdVerifyTime(), 30));
                    userService.updateTd(newUser);
                }
                //客服电话
                HashMap<String, Object> params = new HashMap<>();
                params.put("sysType", "WEBSITE");
                params.put("syskey", "service_phone");
                List<BackConfigParams> list = backConfigParamsService.findParams(params);

                final String msg = user.getRealname();
                ThreadPool.getInstance().execute(() -> SendSmsUtil.sendSmsDiyCL(user.getUserPhone(), SendSmsUtil.templateld45235, msg));
            }

            //将认证中的状态改为认证失败状态
            if ("3".equals(user.getTdStatus())) {
                User newUser = new User();
                newUser.setId(userId);
                //认证失败
                newUser.setTdStatus("1");
                newUser.setTdRawData("");
                newUser.setTdVerifyTime(new Date());
                newUser.setTdVerifyNextTime(DateUtil.addDay(newUser.getTdVerifyTime(), 30));
                userService.updateTd(newUser);
            }
            HashMap<String, String> resMap = new HashMap<>();
            resMap.put("code", "0");
            resMap.put("message", "success");

            logger.info("mobileTdCallback respMap = " + JSON.toJSONString(resMap));
            logger.info("mobileTdCallback end");
            JSONUtil.toObjectJson(response, JSON.toJSONString(resMap));
        } catch (Exception e) {
            logger.error("mobileTdCallback error:", e);
        }
    }

}
