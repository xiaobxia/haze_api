package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.util.NumberToCN;
import com.vxianjin.gringotts.util.date.CalendarUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.common.result.Status;
import com.vxianjin.gringotts.web.dao.ICfcaContractInfoDao;
import com.vxianjin.gringotts.web.dao.ICfcaHttpInfoDao;
import com.vxianjin.gringotts.web.pojo.CfcaContractInfo;
import com.vxianjin.gringotts.web.pojo.CfcaContractTemplate;
import com.vxianjin.gringotts.web.pojo.CfcaHttpInfo;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.ICfcaSignAndViewService;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 云法通合同签署状态同步回调
 *
 * @author tgy
 * @version [版本号, 2018年3月16日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Controller
@RequestMapping("/lnkj")
public class CfcaNotifyController extends BaseController {

    /**
     * 日志打印
     */
    private static final Logger logger = LoggerFactory.getLogger(CfcaNotifyController.class);

    /**
     * 成功回调
     */
    private static final String SUCCESS = "success";

    /**
     * 失败回调
     */
    private static final String FAIL = "fail";

    /**
     * 请求类型
     */
    private static final Integer reqType = 4;
    @Autowired
    JedisCluster jedisCluster;
    /**
     * 云法通支付令业务
     */
    @Autowired
    ICfcaSignAndViewService cfcaSignAndViewService;
    @Autowired
    private ICfcaContractInfoDao cfcaContractDao;
    @Autowired
    private ICfcaHttpInfoDao cfcaHttpDao;
    @Autowired
    private IBorrowOrderService borrowOrderService;

    @Autowired
    private BorrowProductConfigDao borrowProductConfigDao;

    @RequestMapping(value = "/goto_sign/{contractId}")
    public String gotoCfcaSign(HttpServletRequest request, Model model, @PathVariable String contractId) {

        CfcaContractInfo contractInfo = cfcaContractDao.selectByContractId(contractId);
        String signUrl = contractInfo.getUrl();
        String viewUrl = CfcaCommonUtil.SIGN_AND_VIEW_REQ_URL.replace("(1)", contractInfo.getCfcaContractId()).replace("(2)", contractInfo.getCfcaUserId());

        if (2 == contractInfo.getSignStatus()) {
            model.addAttribute("status", 4);
        } else {
            model.addAttribute("status", 3);
        }
        model.addAttribute("sign_url", signUrl);
        model.addAttribute("view_url", viewUrl);
        return "cfca/cfca_sign_status";
    }

    /**
     * 签署状态回调
     *
     * @param request
     * @param model
     */
    @RequestMapping(value = "/sign_status_notify/{clientType}/{deviceId}/{userId}")
    public String cfcaNotify(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable String clientType, @PathVariable String deviceId, @PathVariable String userId) {
        String contractId = request.getParameter("id");
        String cfcaUserId = request.getParameter("userid");
        String status = request.getParameter("status");
        String msg = request.getParameter("msg");
        CfcaContractInfo contractInfo = cfcaContractDao.selectByContractId(contractId);

        if (null == contractInfo.getSignStatus()) {
            contractInfo.setSignStatus(0);
        }

        CfcaHttpInfo httpInfo = new CfcaHttpInfo();
        httpInfo.setHttpRequest("");
        httpInfo.setHttpResponse("cfca notify handle:contractId:" + contractId + ",status:" + status + ",msg:" + msg + ",cfcaUserId:" + cfcaUserId);
        httpInfo.setReqType(reqType);
        httpInfo.setUserId(userId);
        cfcaHttpDao.insert(httpInfo);
        logger.info("cfca notify handle:contractId" + contractId + ",status:" + status + ",msg:" + msg + ",cfcaUserId:" + cfcaUserId);

        String signUrl = contractInfo.getUrl();
        String viewUrl = CfcaCommonUtil.SIGN_AND_VIEW_REQ_URL.replace("(1)", contractInfo.getCfcaContractId()).replace("(2)", contractInfo.getCfcaUserId());

        model.addAttribute("msg", msg);
        if (SUCCESS.equals(status)) {
            model.addAttribute("status", 2);
            //成功
            contractInfo.setSignStatus(2);
        } else if (FAIL.equals(status)) {
            model.addAttribute("status", 1);
            //失败
            contractInfo.setSignStatus(4);
        } else {
            //签署中
            contractInfo.setSignStatus(1);
        }
        cfcaContractDao.updateByPrimaryKeySelective(contractInfo);
        //处理完成
        response.setHeader("status", "SUCCESS");
        return "cfca/cfca_sign_status";
    }

    public void cfcaHandle(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> json = new HashMap<String, Object>();

        //总开关 1 关闭 0 开启
        String cfcaSwitch = jedisCluster.get("CFCA_SWITCH");
        CfcaContractInfo contractInfo = new CfcaContractInfo();
        CfcaContractTemplate template = new CfcaContractTemplate();
        String code = Status.FAILD.getName();
        String msg = "合同异常,可免签";
        try {
            Map<String, String> params = getParameters(request);
            User user = this.loginFrontUserByDeiceId(request);
            if (user  == null){
                return;
            }
            // 借款金额
            Integer loanMoney = Integer.parseInt(params.get("money"));

            HashMap<String, String> query = new HashMap<String, String>();
            query.put("borrowAmount",String.valueOf(loanMoney * 100));
            query.put("borrowDay",params.get("period"));
            BorrowProductConfig config =  borrowProductConfigDao.selectByBorrowMoneyAndPeriod(query);
            if (config == null){
                logger.error(MessageFormat.format("产品线配置不存在，借款金额{0},期限{1}",loanMoney * 100,params.get("period")));
                return ;
            }
            if (config.getTotalFeeRate() == null || config.getBorrowAmount() == null){
                logger.error(MessageFormat.format("产品线ID：{0}配置异常",config.getId()));
                return;
            }

            double apr_fee = config.getTotalFeeRate().divide(new BigDecimal("100")).intValue();

            if (0 == user.getSignSwitch() && "0".equals(cfcaSwitch)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                String clientType = request.getParameter("clientType");
                String deviceId = request.getParameter("deviceId");

                //应还金额
                BigDecimal allMye = new BigDecimal(loanMoney);
                //本金金额
                double mye2 = loanMoney - apr_fee;
                BigDecimal tureMye = new BigDecimal(mye2);
                //合同金额
                contractInfo.setContractPrice(allMye);
                //设置回调URL  RequestUtils.getRequestPath(null)
                contractInfo.setLinkedUrl("http://1707m30m34.iok.la:31870" + "/lnkj/sign_status_notify/" + clientType + "/" + deviceId + "/" + user.getId());
                //借款用途
                template.setUseage(CfcaCommonUtil.USAGE);
                //本金
                template.setPrice(allMye.toString());
                //本金大写
                template.setPriceUpper(NumberToCN.number2CNMontrayUnit(allMye));
                //应还金额
                template.setRepaymentPrice(allMye.toString());
                //服务费
                template.setServerPrice(String.valueOf(apr_fee));
                //年利率
                template.setRate(CfcaCommonUtil.RATE);
                //逾期利率
                template.setOverdueRate(CfcaCommonUtil.OVERDUE_RATE);
                //续借利率
                template.setAgainRate(CfcaCommonUtil.AGAIN_RATE);

                //还款方式
                template.setRepaymentMethod(CfcaCommonUtil.REPAYMENT_METHOD);
                //还款期数
                template.setRepaymentCount(CfcaCommonUtil.REPAYMENT_COUNT);

                //借款期限
                int payCount = Integer.valueOf(params.get("period"));
                Date now = new Date();
                Date endTime = CalendarUtil.plusDay4Date(payCount - 1, now);
                //借款起息日
                template.setStartTime(sdf.format(now));
                //借款到期日
                template.setEndTime(sdf.format(endTime));
                //还款日
                template.setRepaymentDate(sdf.format(endTime));
                //创建日期
                template.setCreateTime(sdf.format(now));
                contractInfo.setTemplate(template);
                cfcaSignAndViewService.generatePayToken(user.getId(), contractInfo);

                //json.put("contract_id", contractInfo.getCfcaContractId());

                //Integer status = cfcaSignAndViewService.cfcaQueryStatus(contractInfo.getCfcaContractId());
                //json.put("status", status);
                //是否显示签名链接0显示 1隐藏
                //支付令业务结束

            }
        } catch (Exception e) {
            logger.error("cfca happened error======", e);
            code = Status.ERROR.getName();
            msg = Status.ERROR.getValue();
        } finally {
            result.put("code", code);
            result.put("message", msg);
            data.put("item", json);
            result.put("data", data);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(result));
        }

    }
}
