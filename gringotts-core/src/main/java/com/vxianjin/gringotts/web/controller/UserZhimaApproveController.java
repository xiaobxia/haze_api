package com.vxianjin.gringotts.web.controller;

import com.antgroup.zmxy.openplatform.api.internal.util.RSACoderUtil;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.risk.pojo.RiskCreditUser;
import com.vxianjin.gringotts.risk.service.IRiskCreditUserService;
import com.vxianjin.gringotts.risk.service.IZmxyService;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IInfoIndexService;
import com.vxianjin.gringotts.web.service.IPushUserService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户芝麻认证
 *
 * @author 2016年12月9日 16:51:09
 */
@Controller
public class UserZhimaApproveController extends BaseController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IZmxyService zmxyService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRiskCreditUserService riskCreditUserService;
    @Autowired
    private IInfoIndexService infoIndexService;
    @Autowired
    private IPushUserService pushUserService;

    /**
     * 芝麻信用回调地址
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/zhima/callBackbak")
    public String callBackbak(HttpServletRequest request,
                              HttpServletResponse response, Model model) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        Integer userId = null;
        try {
            Map<String, String> keys = SysCacheUtils
                    .getConfigParams(BackConfigParams.ZMXY);
            String privateKey = keys.get("zm_private_key");
            String charset = "utf-8";
            String params = request.getParameter("params");
            String decryptedParam = RSACoderUtil.decrypt(params, privateKey,
                    charset);// 通过浏览器返回时，不需要decode
            String result = URLDecoder.decode(decryptedParam, charset);
            String[] array = result.split("&");
            HashMap<String, Object> paramss = new HashMap<String, Object>();
            String openId = null;

            for (String string : array) {
                String[] arrays = string.split("=");
                if ("state".equals(arrays[0])) {
                    if (arrays[1] != null && !"".equals(arrays[1])) {
                        userId = Integer.valueOf(arrays[1]);
                    }
                } else if ("open_id".equals(arrays[0])) {
                    if (arrays[1] != null && !"".equals(arrays[1])) {
                        openId = arrays[1];
                    }
                }
            }
            if (openId != null && userId != null) {
                // upDateZhiMaBack(openId, userId);
            }
            logger.info("callBack decrypt result=" + result);
        } catch (Exception e) {
            logger.error("callBack error ", e);
        }
        return "redirect:/creditreport/zm-mobile-api?userId=" + userId;
    }

    /**
     * 芝麻信用回调地址
     *
     * @param request req
     * @return str
     */
    @RequestMapping("/zhima/callBack/{user_id}")
    public String callBack(HttpServletRequest request,@PathVariable("user_id") Integer user_id) {
        logger.info("zhimaCallBack start");
        Integer userId = null;
        try {
            String openId = request.getParameter("open_id");
            String errorCode = request.getParameter("error_code");
            String errorMessage = request.getParameter("error_message");
            userId = user_id;

            if (openId != null && userId != null) {
                // upDateZhiMaBack(openId, userId);
            }
            logger.info("callBack result errorCode =" + errorCode + " errorMessage = " + errorMessage);
        } catch (Exception e) {
            logger.error("callBack error ", e);
        }
        logger.info("zhimaCallBack end");
        return "redirect:/creditreport/zm-mobile-api?userId=" + userId + "&record=2";
    }

    /**
     * 芝麻认证授权请求
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/creditreport/zm-mobile-api")
    public String createZhiMaBack(HttpServletRequest request,Model model) {
        String code = ResponseStatus.FAILD.getName();
        String msg = "查询失败！";
        HashMap<String, Object> params = new HashMap<>();
        String record = request.getParameter("record");
        String clientType = request.getParameter("clientType");
        try {
            User user = this.loginFrontUserByDeiceId(request);
            Integer userId = null;
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
                    msg = "";
                } else {
                    //用户真实姓名
                    params.put("userName", user.getRealname());
                    //用户身份证号
                    params.put("cardNum", user.getIdNumber());
                    //用户ID
                    params.put("userId", user.getId());
                    ResponseContent serviceResult = zmxyService.getOpenId(params);
                    logger.error("serviceResult code = " + serviceResult.getCode());
                    if (serviceResult.isSuccessed()) {
                        code = "200";
                        msg = serviceResult.getMsg();
                        upDateZhiMaBack(serviceResult.getMsg(), Integer.valueOf(user
                                .getId()));
                    } else if ("100".equals(serviceResult.getCode())) {
                        ResponseContent serviceResults = zmxyService.getURL(params,
                                request);
                        logger.error("serviceResult code = " + serviceResult.getCode());
                        if ("200".equals(serviceResults.getCode())) {
                            code = "100";
                            msg = serviceResults.getMsg();
                        }
                    } else {
                        code = serviceResult.getCode();
                        msg = serviceResult.getMsg();
                    }
                }
                logger.info("createZhiMaBack success");
            } else {
                code = "300";
                msg = "退出请重新登录";
            }
        } catch (Exception e) {
            logger.error("createZhiMaBack error ", e);
        } finally {

        }
        model.addAttribute("clientType", clientType);
        model.addAttribute("record", record);
        model.addAttribute("code", code);
        model.addAttribute("msg", msg);
        return "userinfo/zmGetOpenId";
    }

    /**
     * 修改芝麻信息
     *
     * @param openId
     * @param userId
     */

    public void upDateZhiMaBack(String openId, Integer userId) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        HashMap<String, Object> paramss = new HashMap<String, Object>();
        if (userId != null && openId != null) {
            BigDecimal zmScore = BigDecimal.ZERO;
            String creditWatch = null;
            RiskCreditUser riUser = null;
            boolean flag = false;
            paramss.put("openId", openId);
            paramss.put("userId", userId);
            serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
            User user = userService.searchByUserid(Integer.valueOf(userId));
            /**
             * 判断芝麻分更新时间
             */
            if (user != null && "2".equals(user.getZmStatus())) {
                try {
                    Integer days = DateUtil.daysBetween(DateUtil.getDate(user
                            .getZmScoreTime(), "yyyy-MM-dd"), new Date());
                    /* 查询条件限制一个月 */
                    if (days >= 30) {
                        Calendar cal = Calendar.getInstance();
                        Integer day = cal.get(Calendar.DAY_OF_MONTH);
                        /* 芝麻分每月6号更新 */
                        if (day > 6) {
                            serviceResult = zmxyService.getZmScore(paramss);
                            flag = true;
                        } else {
                            serviceResult = new ResponseContent("200", "0");
                        }
                    } else {
                        serviceResult = new ResponseContent("200", "0");
                    }
                } catch (Exception e) {
                    logger.error("upDateZhiMaBack error ", e);
                }
            } else {
                //更新用户芝麻认证状态
                User userInfo = new User();
                userInfo.setZmStatus("2");
                userInfo.setId(String.valueOf(userId));
                userService.updateByPrimaryKeyUser(userInfo);

                serviceResult = zmxyService.getZmScore(paramss);
                flag = true;
            }
            if (serviceResult.isSuccessed()) {
                zmScore = new BigDecimal(serviceResult.getMsg());
                if (flag) {
                    /* 更新用户芝麻分信息 */
                    riUser = new RiskCreditUser(userId, zmScore);
                    //riskCreditUserService.updateZmScore(riUser);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("userId", userId);
                    this.infoIndexService.authSesame(map);// 设置状态
                    logger.info("callBack updateZmScore");

                    //地推 芝麻信用
//                    ThreadPool pool = ThreadPool.getInstance();
//                    pool.execute(new DtThread(UserPushUntil.ZHIMAAPPROVE, userId, null, new Date(), userService,
//                            pushUserService, null, null));
                }
                serviceResult = zmxyService.getCreditWatchList(paramss);
                if (serviceResult.isSuccessed()) {
                    creditWatch = serviceResult.getMsg();
                    if (creditWatch != null) {
                        /* 更新用户芝麻行业关注信息 */
                        //riskCreditUserService.updateZm(RiskCreditUserUtil.getInstance().createZm(userId, creditWatch));
                        logger.info("callBack updateZmIndusty");
                        // 调用计算额度方法
                    }
                } else {
                    logger.info("callBack updateZm  error ", serviceResult
                            .getMsg());
                }
            } else {
                logger.info("callBack updateZmScore error", serviceResult
                        .getMsg());
            }
        }
    }

}
