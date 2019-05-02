package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.common.result.Status;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.pojo.UserCertification;
import com.vxianjin.gringotts.web.service.IUserBankService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 用户银行卡信息
 *
 * @name xieyaling
 * @date 20161209
 */
@Controller
public class UserBanKController extends BaseController {
    Logger logger = LoggerFactory.getLogger(UserBanKController.class);
    @Autowired
    JedisCluster jedisCluster;
    @Autowired
    private IUserBankService userBankService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserDao userDao;

    /**
     * 查询银行列表
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("credit-card/bank-card-info")
    public void selectBankCardList(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> json = new HashMap<String, Object>();
        String code = Status.FAILD.getName();
        String msg = "获取失败";
        try {
            List<Map<String, Object>> mapList = userBankService.findAllBankInfo();
            if (mapList != null) {
                List<Map<String, String>> restulList = new ArrayList<Map<String, String>>();
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> listMap = mapList.get(i);
                    HashMap<String, String> resultMap = new HashMap<String, String>();
                    resultMap.put("bank_id", listMap.get("id") + "");
                    resultMap.put("bank_name", listMap.get("bankName") + "");
                    resultMap.put("url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/bank/bank_" + listMap.get("bankCode") + ".png");
                    restulList.add(resultMap);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("item", restulList);
                map.put("tips", "由于邮政储蓄银行不支持还款代扣，建议优先选择其他银行卡。");
                json.put("data", map);
                code = Status.SUCCESS.getName();
                msg = "获取成功";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }

    /**
     * 获取认证列表
     *
     * @param request
     * @param response
     */
    @RequestMapping("credit-card/get-verification-info")
    public void getVerificationInfo(HttpServletRequest request, HttpServletResponse response) {
        String telephone = request.getParameter("mobilePhone");
        Map<String, Object> json = new HashMap<String, Object>();
        String code = Status.FAILD.getName();
        String msg = "获取失败";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {

                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                //认证需求列表
                List<UserCertification> mapList = userService.findCerticationList();
                if (mapList != null) {
                    int mustBeCount = 0;
                    //验证用户已认证的选项
                    Map<String, Object> cret = userService.checkUserCalendar(Integer.parseInt(logUser.getId()));
                    //认证结果列表
                    List<Map<String, Object>> restulList = new ArrayList<Map<String, Object>>();
                    //必须认证的选项
                    List<Map<String, Object>> isMustBeList = new ArrayList<Map<String, Object>>();
                    //非必须认证的选项
                    List<Map<String, Object>> noMustBeList = new ArrayList<Map<String, Object>>();
                    for (UserCertification certi : mapList) {
                        HashMap<String, Object> resultMap = new HashMap<String, Object>();
                        resultMap.put("title", certi.getTitle());//标题
                        resultMap.put("subtitle", certi.getDescribe());//描述
                        resultMap.put("tag", certi.getTag());//跳转页面标签
                        //是否有外链
                        if (certi.getUrl() != null && !"".equals(certi.getUrl())) {
                            resultMap.put("url", certi.getUrl());//外链
                        }
                        //银行卡认证页面
                        if (null != telephone && "cardInfo".equals(certi.getCode())) {
                            resultMap.put("url", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/lianlianBindCard/credit-card/firstUserBank");
                        }
                        String operator = checkCertiPams(cret, certi.getCode());//判断是否已认证：已填写/未完善
                        //common/web/images/certification/more_info_logo.png;;common/web/images/certification/more_info_logo2.png
                        String[] logoImg = certi.getLogoImg().split(";;");//认证选项的logo
                        String logoImgStr = "";
                        if ("未完善".equals(operator)) {
                            resultMap.put("status", 0);
                            resultMap.put("operator", operator);
                            if (logoImg.length >= 2) {
                                logoImgStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + logoImg[1];
                            } else {
                                logoImgStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + logoImg[0];
                            }

                            if ("phoneInfo".equals(certi.getCode())) {//手机运营商
                                if ("3".equals(user.getTdStatus())) {
                                    resultMap.put("operator", "认证中");
                                }
                            }

                            if ("riskCarditInfo".equals(certi.getCode())) {//芝麻授权
                                if ("3".equals(user.getZmStatus())) {
                                    if (DateUtil.minutesBetween(DateUtil.getDate(user.getZmScoreTime(), "yyyy-MM-dd HH:mm:ss"), new Date()) > 3) {
                                        User userNew = new User();
                                        userNew.setId(user.getId());
                                        userNew.setZmStatus("1");
                                        userDao.updateUserZm(userNew);
                                    } else {
                                        resultMap.put("operator", "认证中");
                                    }

                                }
                            }

                        } else {//已填写
                            if ("cardInfo".equals(certi.getCode())) {
                                UserCardInfo info = userService.findUserBankCard(Integer.parseInt(logUser.getId()));
                                if (info != null) {
                                    String idCard = info.getCard_no();
                                    if (idCard.length() > 4) {
                                        idCard = info.getBankName() + "(" + idCard.substring(idCard.length() - 4, idCard.length()) + ")";
                                    } else {
                                        idCard = info.getBankName() + "(" + idCard + ")";
                                    }
                                    resultMap.put("operator", "<font color=\"#ff5145\" size=\"3\">" + idCard + "</font>");
                                }
                            } else {
                                resultMap.put("operator", "<font color=\"#ff5145\" size=\"3\">已填写</font>");
                            }
                            if (certi.getMustBe() == 1) {  //为必填已填写统计填写认证数量
                                if (!"收款银行卡".equals(certi.getTitle())) {
                                    mustBeCount++;
                                }
                            }
                            logoImgStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" + logoImg[0];
                            resultMap.put("status", 1);

                            if ("riskCarditInfo".equals(certi.getCode()) || "phoneInfo".equals(certi.getCode())) {
                                resultMap.put("operator", "<font color=\"#ff5145\" size=\"3\">已认证</font>");
                            }


                        }
                        resultMap.put("logo", logoImgStr);//认证选项logo
                        if (certi.getMustBe() == 1) {
                            resultMap.put("title_mark", "<font color=\"#ff8003\" size=\"3\">(必填)</font>");
                            if (!"收款银行卡".equals(certi.getTitle())) {  //2.2.0版本认证中心不需要收款银行卡
                                isMustBeList.add(resultMap);
                            }
                        } else {
                            resultMap.put("title_mark", "<font color=\"#999999\" size=\"3\">(选填)</font>");
                            noMustBeList.add(resultMap);
                        }
                        restulList.add(resultMap);
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    Map<String, Object> listMap = new HashMap<String, Object>();
                    listMap.put("list", restulList);
                    listMap.put("isMustBeList", isMustBeList);
                    listMap.put("noMustBeList", noMustBeList);
                    if (mustBeCount == 1) {
                        mustBeCount = 25;
                    } else if (mustBeCount == 2) {
                        mustBeCount = 50;
                    } else if (mustBeCount == 3) {
                        mustBeCount = 75;
                    } else if (mustBeCount >= 4) {
                        mustBeCount = 100;
                    }
                    listMap.put("mustBeCount", mustBeCount);
                    if ("1".equals(user.getRealnameStatus())) {
                        listMap.put("real_verify_status", "1");
                    } else {
                        listMap.put("real_verify_status", "0");
                    }
                    if ("1".equals(cret.get("emergencyInfo") == null ? "" : cret.get("emergencyInfo").toString())) {
                        listMap.put("contacts_status", "1");
                    } else {
                        listMap.put("contacts_status", "0");
                    }

                    if ("2".equals(user.getZmStatus())) {
                        listMap.put("zhima_status", 1);
                    } else {
                        listMap.put("zhima_status", 0);
                    }

                    if ("2".equals(user.getTdStatus())) {
                        listMap.put("mobile_status", 1);
                    } else {
                        listMap.put("mobile_status", 0);
                    }

                    map.put("item", listMap);
                    json.put("data", map);
                    code = Status.SUCCESS.getName();
                    msg = "success";
                }
            } else {
                code = Status.LOGIN.getName();
                msg = Status.LOGIN.getValue();
            }
        } catch (Exception e) {
            logger.error("getVerificationInfo error=", e);
        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }

    /**
     * 验证用户认证选项是否已认证
     *
     * @param certiMap 用户认证选项列表
     * @param key      认证选项的唯一识别码
     * @return String 已填写or未完善
     */
    public String checkCertiPams(Map<String, Object> certiMap, String key) {
        String operator = "未完善";
        if (certiMap != null) {
            int switchKey = certiMap.get(key) == null || "".equals(certiMap.get(key).toString()) ? 0 : Integer.parseInt(certiMap.get(key).toString());
            switch (switchKey) {
                case 1:
                    operator = "已填写";
                    break;
                default:
                    break;
            }
        }
        return operator;
    }

    /**
     * 去向已绑定银行卡页面
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("credit-card/card-list")
    public String toUerbankCard(HttpServletRequest request, HttpServletResponse response, Model model) {
        User logUser = this.loginFrontUserByDeiceId(request);
        if (logUser != null) {
            UserCardInfo info = userService.findUserBankCard(Integer.parseInt(logUser.getId()));
            model.addAttribute("bankName", info.getBankName());
            model.addAttribute("deviceId", request.getParameter("deviceId"));
            model.addAttribute("mobilePhone", request.getParameter("mobilePhone"));
            String idCard = info.getCard_no();
            if (idCard != null && idCard.length() > 5) {
                idCard = idCard.substring(0, 4) + "****" + idCard.substring(idCard.length() - 4, idCard.length());
            }
            model.addAttribute("bankCard", idCard);
            String bankPhone = info.getPhone();
            if (idCard != null && idCard.length() > 5) {
                bankPhone = bankPhone.substring(0, 3) + "****" + bankPhone.substring(bankPhone.length() - 4, bankPhone.length());
            }
            model.addAttribute("bankPhone", bankPhone);
        } else {
            model.addAttribute("msg", "登录已失效,请重新登录");
        }
        return "userinfo/userBank";
    }

    /**
     * 重新绑定银行卡页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("credit-card/againUserBank")
    public String againUserBank(HttpServletRequest request, HttpServletResponse response, Model model) {
        User logUser = this.loginFrontUserByDeiceId(request);
        List<Map<String, Object>> mapList = userBankService.findAllBankInfo();
        model.addAttribute("bankList", mapList);
        if (logUser != null) {
            UserCardInfo info = userService.findUserBankCard(Integer.parseInt(logUser.getId()));
            model.addAttribute("deviceId", request.getParameter("deviceId"));
            model.addAttribute("mobilePhone", request.getParameter("mobilePhone"));
            User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
            model.addAttribute("realName", user.getRealname());
            model.addAttribute("cardId", info.getId());
        } else {
            model.addAttribute("msg", "登录已失效,请重新登录");
        }
        return "userinfo/againUserBank";
    }


    /**
     * 更多信息页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("credit-web/more")
    public String more(HttpServletRequest request, HttpServletResponse response, Model model) {
        User logUser = this.loginFrontUserByDeiceId(request);
        if (logUser != null) {
            model.addAttribute("deviceId", request.getParameter("deviceId"));
            model.addAttribute("mobilePhone", request.getParameter("mobilePhone"));
            User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
            model.addAttribute("qq", user.getQq());
            model.addAttribute("tobaoaccount", user.getTaobaoAccount());
            model.addAttribute("tobaoaccount", user.getTaobaoAccount());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("wechatAccount", user.getWechatAccount());
        } else {
            model.addAttribute("msg", "登录已失效,请重新登录");
        }
        return "userinfo/userMore";
    }

    /**
     * 保存用户更多信息
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("credit-card/saveMore")
    public void saveMore(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> json = new HashMap<String, Object>();
        String code = Status.FAILD.getName();
        String msg = "保存失败";
        try {
            Map<String, String> params = this.getParameters(request);
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                user.setQq(params.get("qq"));
                user.setWechatAccount(params.get("wechatAccount"));
                user.setTaobaoAccount(params.get("tobaoaccount"));
                user.setEmail(params.get("email"));
                userService.updateByPrimaryKeyUser(user);
                code = "0";
                msg = "信息保存成功";
                String telephone = request.getParameter("mobilePhone");
                if (StringUtils.isNotBlank(telephone)) {
                    json.put("isNew", "true");
                }
            } else {
                code = "-2";
                msg = "登录已失效.请重新登录";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }

    /**
     * 提额列表，目前写死的，前端只是展示
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("credit-card/get-card-info")
    public void selectCardInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> json = new HashMap<String, Object>();

        String code = Status.FAILD.getName();
        String msg = "获取失败";
        String resultJson = "";
        try {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {

                resultJson = "{\"code\":0,\"message\":\"success\",\"data\":{\"item\":{\"my_amount\":" + logUser.getAmountMax() + ",\"amount_list\":[{\"amount\":2000,\"lock\":1},{\"amount\":3000,\"lock\":1},{\"amount\":4000,\"lock\":1},{\"amount\":5000,\"lock\":1}],\"url\":\"http://www.hao123.com/?amount=\"}}}";

            } else {
                resultJson = "{\"code\":0,\"message\":\"success\",\"data\":{\"item\":{\"my_amount\":1000,\"amount_list\":[{\"amount\":2000,\"lock\":1},{\"amount\":3000,\"lock\":1},{\"amount\":4000,\"lock\":1},{\"amount\":5000,\"lock\":1}],\"url\":\"http://www.hao123.com/?amount=\"}}}";

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//			json.put("code",code);
//			json.put("message",msg);
            JSONUtil.toObjectJson(response, resultJson);
//			JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }
}
