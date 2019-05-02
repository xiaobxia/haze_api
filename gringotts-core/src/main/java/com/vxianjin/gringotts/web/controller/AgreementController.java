package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.IBorrowContractInfoService;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserBankService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 相关协议h5页面
 *
 * @author Administrator
 */
@Controller
public class AgreementController extends BaseController {

    private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
    Logger logger = LoggerFactory.getLogger(AgreementController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserBankService userBankService;
    @Autowired
    private IBorrowOrderService borrowOrderService;
    @Autowired
    private IBorrowContractInfoService borrowContractInfoService;

    public static String change(double v) {
        long l = Math.round(v * 100);
        if (l == 0) {
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }

    /**
     * H5 注册协议
     */
    @RequestMapping("act/light-loan-xjx/agreement")
    public String registerAgreement(HttpServletRequest request, HttpServletResponse response, Model model) {
        //获取app的公司或者名称信息
        getAppInfo(request, model);
        return "agreement/registerAgreement";
    }

    /**
     * H5 借款协议
     */
    @RequestMapping("credit-loan/agreement")
    public String borrowAgreement(HttpServletRequest request, HttpServletResponse response, Model model) {
        String sign = request.getParameter("sign");
        String userId = request.getParameter("userId");
        String resultUrl = "agreement/borrowAgreement";
        String borrowId = request.getParameter("borrowId") == null ? "" : request.getParameter("borrowId");
        if (StringUtils.isNotBlank(sign)) {
            Map<String, String> params = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
            String[] params2 = AESUtil.decrypt(sign, params.get("TG_SERVER_KEY")).split("QQ");
            userId = params2[0];
            borrowId = params2[1];
        } else {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                userId = logUser.getId();
            }
        }
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.searchByUserid(Integer.parseInt(userId));
            String indCardNumber = user.getIdNumber().substring(0, 5) + "****" + user.getIdNumber().substring(user.getIdNumber().length() - 4, user.getIdNumber().length());
            logger.info("borrowAgreement indCardNumber=" + indCardNumber);
            user.setIdNumber(indCardNumber);
            model.addAttribute("user", user);
            if (borrowId != null && !"".equals(borrowId)) {
                BorrowOrder borrowOrderR = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
                model.addAttribute("borrow", borrowOrderR);
                String borrowMonery = change(borrowOrderR.getMoneyAmount() / 100);
                model.addAttribute("borrowMonery", borrowMonery);
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-25");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (borrowOrderR.getLoanTime() != null && !"".equals(borrowOrderR.getLoanTime())
                        && borrowOrderR.getLoanTime().before(date)) { //2017-3-25(含)号以后才采取查询方式查出资人表
                    model.addAttribute("capitalName", "上海富蜀商务信息咨询有限公司"); //丁方公司名称
                    model.addAttribute("capitalCity", "上海市"); //丁方城市
                    model.addAttribute("realName", "蔡坤燚"); //乙方
                    model.addAttribute("idcard", "31010****0414"); //乙方
                } else {
                    //添加乙方和丁方
                    BorrowContractInfo params = new BorrowContractInfo();
                    params.setAssetOrderId(borrowOrderR.getId());
                    List<BorrowContractInfo> borrowContractlist = borrowContractInfoService.findBorrowContractInfo(params);
                    if (borrowContractlist != null && borrowContractlist.size() > 0) {
                        BorrowContractInfo borrowContractInfo = borrowContractlist.get(0);
                        if (StringUtils.isNotBlank(borrowContractInfo.getCapitalName())) {
                            String[] str = borrowContractInfo.getCapitalName().split(";;");
                            model.addAttribute("capitalName", StringUtils.isNotBlank(str[0]) ? str[0] : "**订单生成后可见**"); //丁方公司名称
                            model.addAttribute("capitalCity", StringUtils.isNotBlank(str[1]) ? str[1] : "**订单生成后可见**"); //丁方城市
                        } else {
                            model.addAttribute("capitalName", "**订单生成后可见**"); //丁方公司名称
                            model.addAttribute("capitalCity", "**订单生成后可见**"); //丁方城市
                        }
                        if (StringUtils.isNotBlank(borrowContractInfo.getRealName())) {
                            model.addAttribute("realName", borrowContractInfo.getRealName()); //乙方
                        } else {
                            model.addAttribute("realName", "**订单生成后可见**"); //乙方
                        }
                    } else {
                        model.addAttribute("capitalName", "**订单生成后可见**"); //丁方公司名称
                        model.addAttribute("capitalCity", "**订单生成后可见**"); //丁方城市
                        model.addAttribute("realName", "**订单生成后可见**"); //乙方
                    }
                }
                if ((borrowOrderR.getStatus() >= 21 || borrowOrderR.getStatus() == -11) && borrowOrderR.getStatus() != 22) {
                    resultUrl = "agreement/borrowSuccessAgreement";
                }

            }
        }
        //获取app的公司或者名称信息
        getAppInfo(request, model);
        return resultUrl;
    }

    /**
     * 平台服务协议2017/04之前用
     */
    @RequestMapping("agreement/platformService")
    public String platformService(HttpServletRequest request, HttpServletResponse response, Model model) {
        String sign = request.getParameter("sign");
        String userId = request.getParameter("userId");
        String resultUrl = "agreement/platformservice";
        String borrowId = request.getParameter("borrowId") == null ? "" : request.getParameter("borrowId");
        if (StringUtils.isNotBlank(sign)) {
            Map<String, String> params = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
            String[] params2 = AESUtil.decrypt(sign, params.get("TG_SERVER_KEY")).split("QQ");
            userId = params2[0];
            borrowId = params2[1];
        } else {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                userId = logUser.getId();
            }
        }
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.searchByUserid(Integer.parseInt(userId));
            String indCardNumber = user.getIdNumber().substring(0, 5) + "****" + user.getIdNumber().substring(user.getIdNumber().length() - 4, user.getIdNumber().length());
            user.setIdNumber(indCardNumber);
            model.addAttribute("user", user);
            if (borrowId != null && !"".equals(borrowId)) {
                BorrowOrder borrowOrderR = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
                model.addAttribute("borrow", borrowOrderR);
                if ((borrowOrderR.getStatus() >= 21 || borrowOrderR.getStatus() == -11) && borrowOrderR.getStatus() != 22) {
                    resultUrl = "agreement/borrowPlatformService";
                }

            }
        }
        //获取app的公司或者名称信息
        getAppInfo(request, model);
        return resultUrl;
    }

    /**
     * 平台服务协议new2017/04之后启用
     */
    @RequestMapping("agreement/platformServiceNew")
    public String platformServiceNew(HttpServletRequest request, HttpServletResponse response, Model model) {
        String sign = request.getParameter("sign");
        String userId = request.getParameter("userId");
        String resultUrl = "agreement/platformserviceNew";
        String borrowId = request.getParameter("borrowId") == null ? "" : request.getParameter("borrowId");
        if (StringUtils.isNotBlank(sign)) {
            Map<String, String> params = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
            String[] params2 = AESUtil.decrypt(sign, params.get("TG_SERVER_KEY")).split("QQ");
            userId = params2[0];
            borrowId = params2[1];
        } else {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                userId = logUser.getId();
            }
        }
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.searchByUserid(Integer.parseInt(userId));
            String indCardNumber = user.getIdNumber().substring(0, 5) + "****" + user.getIdNumber().substring(user.getIdNumber().length() - 4, user.getIdNumber().length());
            user.setIdNumber(indCardNumber);
            model.addAttribute("user", user);
            if (borrowId != null && !"".equals(borrowId)) {
                BorrowOrder borrowOrderR = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
                model.addAttribute("borrow", borrowOrderR);
                if ((borrowOrderR.getStatus() >= 21 || borrowOrderR.getStatus() == -11) && borrowOrderR.getStatus() != 22) {
                    resultUrl = "agreement/borrowPlatformServiceNew";
                }

            }
        }
        //获取app的公司或者名称信息
        getAppInfo(request, model);
        return resultUrl;
    }

    /**
     * 代扣授权协议
     */
    @RequestMapping("agreement/withholdAuthorization")
    public String withholdAuthorization(HttpServletRequest request, HttpServletResponse response, Model model) {
        String sign = request.getParameter("sign");
        String userId = request.getParameter("userId");
        String borrowId = request.getParameter("borrowId") == null ? "" : request.getParameter("borrowId");
        if (StringUtils.isNotBlank(sign)) {
            Map<String, String> params = SysCacheUtils.getConfigMap(BackConfigParams.TG_SERVER);
            String[] params2 = AESUtil.decrypt(sign, params.get("TG_SERVER_KEY")).split("QQ");
            userId = params2[0];
            borrowId = params2[1];
        } else {
            User logUser = this.loginFrontUserByDeiceId(request);
            if (logUser != null) {
                userId = logUser.getId();
            }
        }
        if (StringUtils.isNotBlank(userId)) {
            User user = userService.searchByUserid(Integer.parseInt(userId));
            UserCardInfo cardInfo = userService.findUserBankCard(Integer.parseInt(user.getId()));
            String indCardNumber = user.getIdNumber().substring(0, 5) + "****" + user.getIdNumber().substring(user.getIdNumber().length() - 4, user.getIdNumber().length());
            user.setIdNumber(indCardNumber);
            if (borrowId != null && !"".equals(borrowId)) {
                BorrowOrder borrowOrderR = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
                if (borrowOrderR.getStatus() >= 21 && borrowOrderR.getStatus() != 22) {
                    model.addAttribute("bo", borrowOrderR);
                }
            }
            model.addAttribute("user", user);
            model.addAttribute("cardInfo", cardInfo);
        }
        //获取app的公司或者名称信息
        getAppInfo(request, model);
        return "agreement/withholdAuthorization";
    }

    /**
     * 信用查询协议
     */
    @RequestMapping("agreement/creditExtension")
    public String creditExtension(HttpServletRequest request, HttpServletResponse response, Model model) {
        //获取app的公司或者名称信息
        getAppInfo(request, model);
        return "agreement/creditExtension";
    }

    /**
     * 获取app的公司或者名称信息
     *
     * @param request
     * @param model
     */
    private void getAppInfo(HttpServletRequest request, Model model) {
        String appName = getAppConfig(request.getParameter("appName"), "APP_TITLE");
        String companyTitle = getAppConfig(request.getParameter("appName"), "COMPANY_TITLE");
        String companyShortTitle = getAppConfig(request.getParameter("appName"), "COMPANY_TITLE_SHORT");
        model.addAttribute("companyTitle", companyTitle);
        model.addAttribute("appName", appName);
        model.addAttribute("companyShortTitle", companyShortTitle);
    }
}
