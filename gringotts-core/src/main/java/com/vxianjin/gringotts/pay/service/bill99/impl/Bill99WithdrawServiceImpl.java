package com.vxianjin.gringotts.pay.service.bill99.impl;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.CollectionConstant;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseConstant;
import com.vxianjin.gringotts.pay.common.util.chanpay.ChanPayUtil;
import com.vxianjin.gringotts.pay.component.Bill99Service;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.enums.OrderChangeAction;
import com.vxianjin.gringotts.pay.model.NeedPayInfo;
import com.vxianjin.gringotts.pay.model.bill99.Pay2bankOrder;
import com.vxianjin.gringotts.pay.model.chanpay.CjtDsfT10000Notify;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.pay.service.base.WithdrawService;
import com.vxianjin.gringotts.pay.service.bill99.Bill99WithdrawService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.impl.BorrowOrderService;
import com.vxianjin.gringotts.web.utils.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  fully
 * @date 2019/6/22 14:34
 */
@Service
public class Bill99WithdrawServiceImpl implements Bill99WithdrawService {

    private static final Logger logger = LoggerFactory.getLogger(Bill99WithdrawServiceImpl.class);
    @Resource
    private BorrowOrderService borrowOrderService;
    @Resource
    private Bill99Service bill99Service;
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
    public void payWithdrawCallback(String orderId, String outTradeNo, String code, String payStatus, String desc) {
        logger.info("Bill99WithdrawCallback params:【orderId:{},outTradeNo:{},code:{},payStatus:{},desc:{}】", orderId, outTradeNo, code, payStatus, desc);
        try {
            if (StringUtils.isNoneBlank(orderId, outTradeNo, code, payStatus, desc)) {
                logger.error("Bill99WithdrawServiceImpl.payWithdrawCallback  error resultMap is null");
                return;
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("serialNo", orderId);
            BorrowOrder borrowOrder = borrowOrderDao.selectBorrowByParams(paramMap);
            logger.info("Bill99WithdrawCallback borrowOrder=" + (borrowOrder != null ? JSON.toJSONString(borrowOrder) : "null"));
            if (borrowOrder == null) {
                logger.error("payWithdrawCallback error borrowOrder is null orderId=" + orderId);
            }
            logger.info("borrowOrder " + orderId + " borrowOrder status is" + borrowOrder.getStatus());

            //订单处于放款中或放款失败状态且非放款成功状态
            if (!borrowOrder.getPaystatus().equals(BorrowOrder.SUB_PAY_SUCC) && (BorrowOrder.STATUS_FKZ.equals(borrowOrder.getStatus())
                    || BorrowOrder.STATUS_FKSB.equals(borrowOrder.getStatus()))) {
                if ("111".equals(payStatus) && "0000".equals(code)) {
                    borrowOrder.setOutTradeNo(outTradeNo);
                    borrowOrderService.updateLoanNew(borrowOrder, "SUCCESS", "支付成功");
                } else {
                    logger.info("fangkuan fail borrowOrder:" + orderId + " userId:" + borrowOrder.getUserId()
                            + " userPhone:" + borrowOrder.getUserPhone() + " msg:" + desc, "fangkuan");
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
    public ResponseContent payWithdraw(String userId, String borrowId, String uuid, String sign) throws Exception {
        logger.info("payWithdraw userId=" + userId + " borrowId=" + borrowId + " uuid=" + uuid + " sign=" + sign);
        ResponseContent result;
        //验证签名
        if (!MD5Util.MD5(AESUtil.encrypt("" + userId + borrowId + uuid, CollectionConstant.getCollectionSign())).equals(sign)) {
            return new ResponseContent("-101", "代付失败,请求参数非法");
        }
        // 获取代付相关信息
        NeedPayInfo needPayInfo = withdrawService.getNeedPayInfo(userId, borrowId);

        //请求代付参数
        Map<String, String> paramMap = prepareParamsToBill99(needPayInfo.getUser(), needPayInfo.getBorrowOrder(), needPayInfo.getUserCardInfo());

        logger.info("Bill99WithdrawServiceImpl payWithdraw 处理后paramMap:{}", JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = null;
        try {
            //发送提现请求
            resultMap = bill99Service.getWithdrawRequest(paramMap);
        } catch (Exception e) {
            //异常状态下，解除该笔交易锁定
            withdrawService.removePayKey(borrowId);
        }

        BorrowOrder orderNew = new BorrowOrder();
        orderNew.setId(needPayInfo.getBorrowOrder().getId());

        orderNew.setSerialNo(paramMap.get("orderId"));
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

    private Map<String, String> prepareParamsToBill99(User user, BorrowOrder order, UserCardInfo info){
        logger.info("Bill99WithdrawServiceImpl prepareParamsToChanPay order:{}, userCardInfo:{}", order, info);
        /*Pay2bankOrder pay2bankOrder = new Pay2bankOrder();
        //商家订单号 必填
        pay2bankOrder.setOrderId(GenerateNo.generateShortUuid(10));
        //金额（分） 必填
        pay2bankOrder.setAmount(order.getIntoMoney().toString());
        //银行名称 必填
        pay2bankOrder.setBankName(info.getBankName());
        //收款人姓名  必填
        pay2bankOrder.setCreditName(user.getRealname());
        //银行卡号 必填
        pay2bankOrder.setBankAcctId(info.getCard_no());*/
        return new HashMap(){{
            put("orderId", GenerateNo.generateShortUuid(10));
            put("amount", order.getIntoMoney().toString());
            put("bankName", info.getBankName());
            put("creditName", user.getRealname());
            put("bankAcctId", info.getCard_no());
        }};
    }

}
