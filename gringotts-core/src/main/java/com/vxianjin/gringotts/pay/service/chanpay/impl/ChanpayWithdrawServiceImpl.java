package com.vxianjin.gringotts.pay.service.chanpay.impl;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.CollectionConstant;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseConstant;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseParameter;
import com.vxianjin.gringotts.pay.common.util.chanpay.ChanPayUtil;
import com.vxianjin.gringotts.pay.component.ChanpayService;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.model.NeedPayInfo;
import com.vxianjin.gringotts.pay.model.chanpay.CjtDsfT10000Notify;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.base.WithdrawService;
import com.vxianjin.gringotts.pay.service.chanpay.ChanpayWithdrawService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.impl.BorrowOrderService;
import com.vxianjin.gringotts.web.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  chenkai
 * @date 2018/7/18 20:32
 */
@Service
public class ChanpayWithdrawServiceImpl implements ChanpayWithdrawService {

    private static final Logger logger = LoggerFactory.getLogger(ChanpayWithdrawServiceImpl.class);
    @Resource
    private BorrowOrderService borrowOrderService;
    @Resource
    private ChanpayService chanpayService;
    @Resource
    private IBorrowOrderDao borrowOrderDao;
    @Resource
    private WithdrawService withdrawService;
    @Resource
    private OrderLogComponent orderLogComponent;
    /**
     * 用户提现（代付）回调接口
     */
    @Transactional(rollbackFor = PayException.class)
    @Override
    public void payWithdrawCallback(CjtDsfT10000Notify cjtDsfT10000Notify) {
        logger.info("ChanPayWithdrawCallback params:【request:" + cjtDsfT10000Notify.toString() + "】");
        if (ChanPayUtil.verify(GsonUtil.toJson(cjtDsfT10000Notify), BaseConstant.MERCHANT_PUBLIC_KEY)) {
            try {
                if (cjtDsfT10000Notify == null) {
                    logger.error("ChanPayWithdrawServiceImpl.payWithdrawCallback  error resultMap is null");
                }
                //订单编号
                String orderId = cjtDsfT10000Notify.getOuter_trade_no();

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("serialNo", orderId);
                BorrowOrder borrowOrder = borrowOrderDao.selectBorrowByParams(paramMap);
                logger.info("ChanPayWithdrawCallback borrowOrder=" + (borrowOrder != null ? JSON.toJSONString(borrowOrder) : "null"));
                if (borrowOrder == null) {
                    logger.error("payWithdrawCallback error borrowOrder is null orderId=" + orderId);
                }
                logger.info("borrowOrder " + orderId + " borrowOrder status is" + borrowOrder.getStatus());

                //订单处于放款中或放款失败状态且非放款成功状态
                if (!borrowOrder.getPaystatus().equals(BorrowOrder.SUB_PAY_SUCC) && (BorrowOrder.STATUS_FKZ.equals(borrowOrder.getStatus())
                        || BorrowOrder.STATUS_FKSB.equals(borrowOrder.getStatus()))) {
                    if ("WITHDRAWAL_SUCCESS".equals(cjtDsfT10000Notify.getWithdrawal_status())) {
                        borrowOrder.setOutTradeNo(cjtDsfT10000Notify.getInner_trade_no());
                        borrowOrderService.updateLoanNew(borrowOrder, "SUCCESS", "支付成功");
                    } else {
                        logger.info("fangkuan fail borrowOrder:" + orderId + " userId:" + borrowOrder.getUserId()
                                + " userPhone:" + borrowOrder.getUserPhone() + " msg:" + cjtDsfT10000Notify.getFail_reason(), "fangkuan");
                        //borrowOrder.setStatus(BorrowOrder.STATUS_FKSB);
                        borrowOrderService.updateLoanNew(borrowOrder, "FAIL", "支付失败");
                    }
                }
                withdrawService.removePayKey(borrowOrder.getId().toString());
            } catch (Exception e) {
                logger.error("payWithdrawCallback error:", e);
                throw new PayException("系统异常");
            }
        }
    }

    /**
     * 用户提现（代付）请求接口
     *
     * @param userId   借款用户id
     * @param borrowId 借款订单id
     * @param uuid     此次交易的随机编号
     * @param sign     加密签名，用于数据校验，以防数据被篡改
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    @Override
    public ResponseContent payWithdraw(String userId, String borrowId, String uuid, String sign) throws BizException {
        logger.info("payWithdraw userId=" + userId + " borrowId=" + borrowId + " uuid=" + uuid + " sign=" + sign);
        ResponseContent result;
        //验证签名
        if (!MD5Util.MD5(AESUtil.encrypt("" + userId + borrowId + uuid, CollectionConstant.getCollectionSign())).equals(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数非法");
        }
        // 获取代付相关信息
        NeedPayInfo needPayInfo = withdrawService.getNeedPayInfo(userId, borrowId);

        //请求代付参数
        //Map<String, String> paramMap = prepareParamsToChanPay(needPayInfo.getUser(), needPayInfo.getBorrowOrder(), needPayInfo.getUserCardInfo());
        String card_no = needPayInfo.getUserCardInfo().getCard_no();
        String realname = needPayInfo.getUser().getRealname();
        String bankName = needPayInfo.getUserCardInfo().getBankName();
        String random = GenerateNo.generateShortUuid(10);
        logger.info("AcctNo:{}, AcctName:{}, bankName:{}, random:{}, PUBLICKEY:{}", card_no, realname, bankName, random, BaseConstant.MERCHANT_PUBLIC_KEY);
        String cardNoEncrypt = ChanPayUtil.encrypt(card_no, BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET);
        String realNameEncrypt = ChanPayUtil.encrypt(realname, BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET);
        logger.info("cardNoEncrypt:{}, realNameEncrypt:{}", cardNoEncrypt, realNameEncrypt);
        Map<String, String> paramMap = BaseParameter.requestBaseParameter("cjt_dsf");
        paramMap.put("TransCode", "T10000"); // 交易码
        paramMap.put("OutTradeNo", random); // 商户网站唯一订单号
        paramMap.put("BusinessType", "0"); // 业务类型：0对私 1对公
        paramMap.put("BankCommonName", bankName); // 通用银行名称
        paramMap.put("AcctNo", cardNoEncrypt); // 对手人账号(此处需要用真实的账号信息)
        paramMap.put("AcctName", realNameEncrypt); // 对手人账户名称
        paramMap.put("TransAmt", "0.01"); //order.getIntoMoney().toString());
        paramMap.put("CorpPushUrl", PropertiesConfigUtil.get("APP_HOST_API") + "/chanpay/withdrawCallback");

        logger.info("ChanpayWithdrawServiceImpl payWithdraw 处理后paramMap:{}", JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = null;
        try {
            //发送提现请求
            resultMap = chanpayService.getWithdrawRequest(paramMap);
        } catch (Exception e) {
            //异常状态下，解除该笔交易锁定
            withdrawService.removePayKey(borrowId);
        }

        BorrowOrder orderNew = new BorrowOrder();
        orderNew.setId(needPayInfo.getBorrowOrder().getId());

        orderNew.setSerialNo(paramMap.get("OutTradeNo"));
        orderNew.setPaystatus(String.valueOf(resultMap.get("code")));
        orderNew.setPayRemark(String.valueOf(resultMap.get("msg")));
        orderNew.setUpdatedAt(new Date());

        //【2】订单日志记录
        OrderLogModel logModel = new OrderLogModel();

        logModel.setUserId(userId);
        logModel.setBorrowId(String.valueOf(orderNew.getId()));
        logModel.setOperateType(OperateType.BORROW.getCode());
        logModel.setAction(OrderChangeAction.BORROW_ACTION.getCode());
        logModel.setBeforeStatus(String.valueOf(needPayInfo.getBorrowOrder().getStatus()));

        if (BorrowOrder.SUB_SUBMIT.equals(String.valueOf(resultMap.get("code")))) {
            result = new ResponseContent("0000", "支付正在处理中");
            logModel.setAfterStatus(BorrowOrder.STATUS_FKZ.toString());
            logModel.setRemark("放款中");
        } else {
            result = new ResponseContent(String.valueOf(resultMap.get("code")), String.valueOf(resultMap.get("msg")));
            BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(Integer.parseInt(borrowId));
            borrowOrderService.updateLoanNew(borrowOrder, "FAIL", "支付失败");

            logModel.setAfterStatus(BorrowOrder.STATUS_FKSB.toString());
            logModel.setRemark("放款失败");
        }
        orderLogComponent.addNewOrderLog(logModel);
        //更新借款订单
        borrowOrderService.updateById(orderNew);
        return result;
    }

    private Map<String, String> prepareParamsToChanPay(User user, BorrowOrder order, UserCardInfo info) {
        logger.info("ChanpayWithdrawServiceImpl prepareParamsToChanPay order:{}, userCardInfo:{}", order, info);
        Map<String, String> paramMap = BaseParameter.requestBaseParameter("cjt_dsf");
        paramMap.put("TransCode", "T10000"); // 交易码
        paramMap.put("OutTradeNo", GenerateNo.generateShortUuid(10)); // 商户网站唯一订单号
        paramMap.put("BusinessType", "0"); // 业务类型：0对私 1对公
        paramMap.put("BankCommonName", info.getBankName()); // 通用银行名称
        paramMap.put("AcctNo", ChanPayUtil.encrypt(info.getCard_no(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET)); // 对手人账号(此处需要用真实的账号信息)
        paramMap.put("AcctName", ChanPayUtil.encrypt(user.getRealname(), BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET)); // 对手人账户名称
        paramMap.put("TransAmt", "0.01");//order.getIntoMoney().toString());
        paramMap.put("CorpPushUrl", PropertiesConfigUtil.get("APP_HOST_API") + "/chanpay/withdrawCallback");
        return paramMap;
    }

}
