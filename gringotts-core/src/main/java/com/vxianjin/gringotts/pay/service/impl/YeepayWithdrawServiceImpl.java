package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.CollectionConstant;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.util.YeepayApiUtil;
import com.vxianjin.gringotts.pay.common.util.YeepayUtil;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.component.YeepayService;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.model.NeedPayInfo;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.YeepayWithdrawService;
import com.vxianjin.gringotts.pay.service.base.WithdrawService;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.impl.BorrowOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  chenkai
 * @date 2018/7/18 20:32
 */
@Service
public class YeepayWithdrawServiceImpl implements YeepayWithdrawService {

    private static final Logger logger = LoggerFactory.getLogger(YeepayWithdrawServiceImpl.class);
    @Resource
    private BorrowOrderService borrowOrderService;
    @Resource
    private YeepayService yeepayService;
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
    public String payWithdrawCallback(String requestXml) {
        logger.info("payWithdrawCallback params:【requestXml:" + requestXml + "】");
        try {

            Map<String, Object> resultMap = YeepayApiUtil.getPayCallBackParamMap(requestXml);
            if (resultMap == null) {
                logger.error("YeepayWithdrawServiceImpl.payWithdrawCallback  error resultMap is null");
                return "数据解析异常";
            }
            //订单编号
            String orderId = resultMap.get("orderId").toString();

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("serialNo", orderId);
            BorrowOrder borrowOrder = borrowOrderDao.selectBorrowByParams(paramMap);
            logger.info("payWithdrawCallback borrowOrder=" + (borrowOrder != null ? JSON.toJSONString(borrowOrder) : "null"));
            if (borrowOrder == null) {
                logger.error("payWithdrawCallback error borrowOrder is null orderId=" + orderId);
                return "非法回调参数";
            }
            logger.info("borrowOrder " + orderId + " borrowOrder status is" + borrowOrder.getStatus());
            //订单处于放款中或放款失败状态且非放款成功状态
            if (!borrowOrder.getPaystatus().equals(BorrowOrder.SUB_PAY_SUCC) && (BorrowOrder.STATUS_FKZ.equals(borrowOrder.getStatus())
                    || BorrowOrder.STATUS_FKSB.equals(borrowOrder.getStatus()))) {
                if ("S".equals(resultMap.get("bankTrxStatusCode").toString())) {
                    borrowOrder.setOutTradeNo(resultMap.get("batchNo").toString());
                    borrowOrderService.updateLoanNew(borrowOrder, "SUCCESS", "支付成功");

                } else {
                    logger.info("fangkuan fail borrowOrder:" + orderId + " userId:" + borrowOrder.getUserId()
                            + " userPhone:" + borrowOrder.getUserPhone() + " msg:" + resultMap.get("message"), "fangkuan");

                    borrowOrderService.updateLoanNew(borrowOrder, "FAIL", "支付失败");
                }
            }
            withdrawService.removePayKey(borrowOrder.getId() + "");
            return "SUCCESS";
        } catch (Exception e) {
            logger.error("payWithdrawCallback error:", e);
            throw new PayException("系统异常");
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
        Map<String, String> paramMap = prepareParamsToYeepay(needPayInfo.getUser(), needPayInfo.getBorrowOrder(), needPayInfo.getUserCardInfo());
        Map<String, Object> resultMap = null;
        try {
            //发送提现请求
            resultMap = yeepayService.getWithdrawRequest(paramMap);
        } catch (Exception e) {
            //异常状态下，解除该笔交易锁定
            withdrawService.removePayKey(borrowId);
        }

        BorrowOrder orderNew = new BorrowOrder();
        orderNew.setId(needPayInfo.getBorrowOrder().getId());

        orderNew.setPaystatus(String.valueOf(resultMap.get("code")));
        orderNew.setPayRemark(String.valueOf(resultMap.get("msg")));
        orderNew.setUpdatedAt(new Date());
        orderNew.setOutTradeNo(paramMap.get("batchNo"));

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
            logger.info("borrowOrderId is" + borrowId + "payWithdraw fail to request yeePay,errorCode:" + resultMap.get("code") + " errorMsg:" + resultMap.get("msg"));
            //放款失败
            orderNew.setStatus(BorrowOrder.STATUS_FKSB);
            withdrawService.removePayKey(borrowId);
            logModel.setRemark("放款失败");
            logModel.setAfterStatus(BorrowOrder.STATUS_FKSB.toString());
            result = new ResponseContent(String.valueOf(resultMap.get("code")), String.valueOf(resultMap.get("msg")));
            //发送放款失败通知短信
            borrowOrderService.sendFangkuanFailNotice(needPayInfo.getUser(), String.valueOf(resultMap.get("msg")));
        }
        orderLogComponent.addNewOrderLog(logModel);
        //更新借款订单
        borrowOrderService.updateById(orderNew);
        return result;
    }

    @Override
    public String payWithdrawCallbackForOnline(String requestXml) {
        logger.info("payWithdrawCallbackForOnline params:【requestXml:" + requestXml + "】");
        try {
            Map<String, Object> resultMap = YeepayUtil.getResponseMap(requestXml);
            if (resultMap == null) {
                logger.error("payWithdrawCallbackForOnline error resultMap is null");
                return "数据解析异常";
            }
            //订单编号
            String orderId = resultMap.get("order_Id").toString();

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("serialNo", orderId);
            BorrowOrder borrowOrder = borrowOrderDao.selectBorrowByParams(paramMap);
            logger.info("payWithdrawCallbackForOnline borrowOrder=" + (borrowOrder != null ? JSON.toJSONString(borrowOrder) : "null"));
            if (borrowOrder == null) {
                logger.error("payWithdrawCallbackForOnline error borrowOrder is null orderId=" + orderId);
                return "非法回调参数";
            }
            //订单处于放款中或放款失败状态且非放款成功状态
            if (!borrowOrder.getPaystatus().equals(BorrowOrder.SUB_PAY_SUCC) && (BorrowOrder.STATUS_FKZ.equals(borrowOrder.getStatus())
                    || BorrowOrder.STATUS_FKSB.equals(borrowOrder.getStatus()))) {
                if ("S".equals(resultMap.get("status").toString())) {
                    borrowOrder.setOutTradeNo(resultMap.get("batch_No").toString());
                    borrowOrderService.updateLoanNew(borrowOrder, "SUCCESS", "支付成功");

                } else {
                    logger.info("fangkuan fail userId:" + borrowOrder.getUserId()
                            + " userPhone:" + borrowOrder.getUserPhone() + " msg:" + resultMap.get("message").toString(), "fangkuan");
                    borrowOrderService.updateLoanNew(borrowOrder, "FAIL", resultMap.get("message").toString());
                }
            }
            withdrawService.removePayKey(borrowOrder.getId() + "");
            return YeepayUtil.getNotifyResponseXml(resultMap, "");
        } catch (Exception e) {
            logger.error("payWithdrawCallback error:", e);
            throw new PayException("系统异常");
        }
    }

    private Map<String, String> prepareParamsToYeepay(User user, BorrowOrder order, UserCardInfo info) {
        Map<String, String> paramMap = new HashMap<>();
        //用户id
        paramMap.put("userId", user.getId());
        //借款订单编号
        paramMap.put("orderId", order.getSerialNo());
        //实际放款金额
        paramMap.put("amount", new DecimalFormat("######0.00").format(order.getIntoMoney() / 100.00));
        //银行卡号
        paramMap.put("cardNo", order.getCardNo());
        //银行卡开户人姓名
        paramMap.put("cardName", order.getRealname());
        paramMap.put("batchNo", "11" + System.currentTimeMillis() + "");
        paramMap.put("bankName", info.getBankName());
        paramMap.put("bankCode", info.getBankCode());
        return paramMap;
    }

}
