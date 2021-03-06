package com.vxianjin.gringotts.pay.component.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfca.util.pki.cipher.Session;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.enums.ErrorCode;
import com.vxianjin.gringotts.pay.common.enums.PayRecordStatus;
import com.vxianjin.gringotts.pay.common.util.CallbackUtils;
import com.vxianjin.gringotts.pay.common.util.YeepayApiUtil;
import com.vxianjin.gringotts.pay.common.util.YeepayUtil;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseConstant;
import com.vxianjin.gringotts.pay.common.util.chanpay.BaseParameter;
import com.vxianjin.gringotts.pay.common.util.chanpay.ChanPayUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouApiUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.XMapUtil;
import com.vxianjin.gringotts.pay.component.ChanpayService;
import com.vxianjin.gringotts.pay.model.*;
import com.vxianjin.gringotts.pay.model.chanpay.ChanpayResult;
import com.vxianjin.gringotts.pay.model.fuiou.FuiouRepayResultModel;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolBindXmlBeanReq;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolCheckResultXmlBeanReq;
import com.vxianjin.gringotts.pay.model.fuiou.PayforreqXmlBeanReq;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.dao.IUserBankDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.OutOrders;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import com.vxianjin.gringotts.util.fuiou.MD5Util;

/**
 * @author zed 易宝支付服务类
 */
@Service
public class ChanpayServiceImpl implements ChanpayService {

    private static final Logger log = LoggerFactory.getLogger(ChanpayServiceImpl.class);

    @Resource
    private IOutOrdersService outOrdersService;

    @Resource
    private IUserBankService userBankService;

    @Resource
    @Qualifier("userBankDaoImpl")
    private IUserBankDao userBankDao;

    @Resource
    private IInfoIndexService infoIndexService;

    @Resource
    private IUserService userService;

    @Resource
    private IPushUserService pushUserService;

    @Resource
    private IBorrowOrderService borrowOrderService;


    /**
     * 绑卡请求
     * @param paramMap map
     * @return map
     */
    @Override
    public Map<String, Object> getBindCardRequest(Map<String, String> paramMap) {
        log.info("ChanpayService getBindCardRequest start");
        log.info("ChanpayService getBindCardRequest paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String userId = FuiouApiUtil.formatString(paramMap.get("userId"));

        log.info("ChanpayService getBindCardRequest userId=" + userId);
        try {
            log.info("ChanpayService getBindCardRequest dataMap=" + JSON.toJSONString(paramMap));

            String orderNo = GenerateNo.nextOrdId();
            OutOrders outOrders = new OutOrders();
            outOrders.setUserId(userId);
            outOrders.setOrderType("CHANPAY");
            outOrders.setOrderNo(GenerateNo.nextOrdId());
            outOrders.setAct("BINDCARD_MSG");
            outOrders.setReqParams(JSON.toJSONString(paramMap));
            outOrders.setStatus(OutOrders.STATUS_WAIT);
            outOrdersService.insert(outOrders);

            String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);

            OutOrders newOutOrder = new OutOrders();
            newOutOrder.setOrderNo(orderNo);

            if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {
                log.info("chanpayResponseMap :{}",result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                jsonObject.get("AppRetcode");//返回码
                jsonObject.get("AppRetMsg");//返回信息
                jsonObject.get("AcceptStatus");//返回码
                if ("S".equals(jsonObject.get("AcceptStatus")) && "S0001".equals(jsonObject.get("RetCode"))) {
                    resultMap.put("code", "0000");
                    resultMap.put("msg", "请求成功");
                    newOutOrder.setStatus(OutOrders.STATUS_SUC);
                    outOrdersService.updateByOrderNo(newOutOrder);
                    return resultMap;
                } else {
                    resultMap.put("code", "400");
                    resultMap.put("msg", jsonObject.get("RetMsg"));
                    newOutOrder.setStatus(OutOrders.STATUS_OTHER);
                    outOrdersService.updateByOrderNo(newOutOrder);
                    return resultMap;
                }
            }
            resultMap.put("code", "400");
            resultMap.put("msg", "绑卡请求失败，请重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }catch (Exception e){
            log.error("post yeepay error:{}",e);
        }
        return resultMap;
    }

    /**
     * 绑卡确认请求
     *
     * @param bindCardConfirmReq req
     * @return result
     */
    @Override
    public ResultModel<String> getBindCardConfirm(YPBindCardConfirmReq bindCardConfirmReq) {
        log.info("ChanpayService getBindCardConfirm start");
        log.info("ChanpayService getBindCardConfirm paramMap=" + JSON.toJSONString(bindCardConfirmReq));

        ResultModel<String> resultModel = new ResultModel<>(false);

        if (bindCardConfirmReq.getDataMap() == null || bindCardConfirmReq.getDataMap().size() <= 0) {
            resultModel.setMessage("datamap不允许为空");
            return resultModel;
        }

        String userId = bindCardConfirmReq.getUserId();
        log.info("ChanpayService getBindCardConfirm userId=" + userId);

        try{
            Map<String, String> paramMap = BaseParameter.requestBaseParameter(BaseParameter.NMG_API_AUTH_SMS);
            paramMap.put("TrxId", bindCardConfirmReq.getOrderNo());
            paramMap.put("OriAuthTrxId", bindCardConfirmReq.getRequestNo());
            paramMap.put("SmsCode", bindCardConfirmReq.getSmsCode());
            //paramMap.put("NotifyUrl", PropertiesConfigUtil.get("APP_HOST_API") + "/chanpay/bindCardCallback");//异步通知url

            String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);
            if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {
                ChanpayResult chanpayResult = JSONObject.parseObject(result, ChanpayResult.class);

                if (chanpayResult == null || StringUtils.isBlank(result)) {
                    resultModel.setCode(ErrorCode.ERROR_400.getCode());
                    resultModel.setMessage(ErrorCode.ERROR_400.getMsg());
                    return resultModel;
                }

                resultModel.setData(result);
                log.info("ChanpayService getBindCardConfirm userId=" + userId + " errorcode=" + chanpayResult.getRetCode() + " errormsg=" + chanpayResult.getRetMsg());

                String orderStatus = String.valueOf(chanpayResult.getStatus());
                log.info("ChanpayService getBindCardConfirm userId=" + userId + " status=" + orderStatus);
                //绑卡
                if (StringUtils.isNotBlank(orderStatus) && "S".equals(orderStatus) && "S0001".equals(chanpayResult.getRetCode())) {
                    resultModel.setSucc(true);
                    resultModel.setCode(ErrorCode.ERROR_0000.getCode());
                    resultModel.setMessage(ErrorCode.ERROR_0000.getMsg());
                    return resultModel;
                } else {
                    resultModel.setCode(chanpayResult.getRetCode());
                    resultModel.setMessage(chanpayResult.getRetMsg());
                    return resultModel;
                }
            }
        }catch (Exception e){
            log.error("post bindCard confirm error:{}",e);
        }
        resultModel.setCode(ErrorCode.ERROR_400.getCode());
        resultModel.setMessage(ErrorCode.ERROR_400.getMsg());
        return resultModel;
    }


    @Override
    public Map<String, Object> getBindCardSmsCode(Map<String, String> paramMap) {
        log.info("ChanpayService getBindCardSmsCode paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        log.info("ChanpayService getBindCardSmsCode userId=" + userId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        log.info("ChanpayService getBindCardSmsCode dataMap=" + JSON.toJSONString(dataMap));

        String orderNo = GenerateNo.nextOrdId();
        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(userId);
        outOrders.setOrderType("YEEPAY");
        outOrders.setOrderNo(GenerateNo.nextOrdId());
        outOrders.setAct("BINDCARD_RESENDSMS");
        outOrders.setReqParams(JSON.toJSONString(dataMap));
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        outOrdersService.insert(outOrders);

        OutOrders newOutOrder = new OutOrders();
        newOutOrder.setOrderNo(orderNo);
        //调用易宝api,获取返回结果
        try{
            resultMap = YeepayApiUtil.yeepayYOP(dataMap,PayConstants.BIND_CARDRESENDSMS_URL);
        }catch (Exception e){
            log.error("post bindCard confirm resend error:{}",e);
        }
        log.info("ChanpayService getBindCardSmsCode resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));

        if (resultMap == null) {
            resultMap = new HashMap<>();
            resultMap.put("code", "400");
            resultMap.put("msg", "操作异常，请刷新页面重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }
        Object orderStatus = resultMap.get("status");
        log.info("ChanpayService getBindCardSmsCode userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
        //待短验
        if (orderStatus != null && "TO_VALIDATE".equals(orderStatus.toString())) {
            resultMap.put("code", "0000");
            resultMap.put("msg", "请求成功");
            outOrders.setStatus(OutOrders.STATUS_SUC);
            outOrdersService.updateByOrderNo(outOrders);
            return resultMap;
        }
        log.info("ChanpayService getBindCardSmsCode userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
        if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
            resultMap.put("code", "400");
            resultMap.put("msg", resultMap.get("errormsg"));
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }
        resultMap.put("code", "400");
        resultMap.put("msg", "操作异常，请刷新页面重试");

        newOutOrder.setStatus(OutOrders.STATUS_OTHER);
        outOrdersService.updateByOrderNo(newOutOrder);
        return resultMap;
    }

    @Override
    public ResultModel updateUserBankInfo(Map<String, String> paramMap) throws Exception {
        log.info("ChanpayService updateUserBankInfo start");
        log.info("ChanpayService updateUserBankInfo paramMap=" + JSON.toJSONString(paramMap));

        ResultModel resultModel = new ResultModel(true, "0", "绑卡成功");

        String userId = paramMap.get("user_id");
        String bankId = paramMap.get("bank_id");
        String cardNo = paramMap.get("card_no");
        String phone = paramMap.get("phone");
        String realName = paramMap.get("real_name");
        //String agreeno = paramMap.get("agreeno");

        UserCardInfo bankCardByCardNo = userService.findBankCardByCardNo(cardNo);

        Map<String, String> bankInfo = userBankDao.selectByPrimaryKey(Integer.parseInt(bankId));
        if (bankCardByCardNo == null) {
            UserCardInfo cardInfo = new UserCardInfo();
            cardInfo.setBank_id(Integer.parseInt(bankId));
            cardInfo.setCard_no(cardNo);
            cardInfo.setPhone(phone);
            cardInfo.setUserId(Integer.parseInt(userId));
            cardInfo.setOpenName(realName);
            cardInfo.setBankName(bankInfo.get("bankName") == null ? "" : bankInfo.get("bankName"));
            cardInfo.setStatus(UserCardInfo.STATUS_SUCCESS);
            cardInfo.setMainCard(UserCardInfo.MAINCARD_Z);
            cardInfo.setType(UserCardInfo.TYPE_DEBIT);
            cardInfo.setCreateTime(new Date());
            cardInfo.setIsBand(1);
            if (userService.defaultCardCount(Integer.parseInt(userId)) <= 0) {
                cardInfo.setCardDefault(1);
            }
            cardInfo.setAgreeno("CHANPAY_CARD");
            boolean flag = userBankDao.saveUserbankCard(cardInfo);
            log.info("ChanpayService updateUserBankInfo userId=" + userId + " new flag=" + flag);

            if (!flag) {
                resultModel.setSucc(flag);
                resultModel.setCode("9999");
                resultModel.setMessage("帐户银行信息保存失败");
                return resultModel;
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            //设置状态-第一次绑卡
            infoIndexService.authBank(map);

        } else {
            UserCardInfo cardInfoSave = new UserCardInfo();
            cardInfoSave.setId(bankCardByCardNo.getId());
            cardInfoSave.setBank_id(Integer.parseInt(bankId));
            cardInfoSave.setCard_no(cardNo);
            cardInfoSave.setPhone(phone);
            cardInfoSave.setUserId(Integer.parseInt(userId));
            cardInfoSave.setOpenName(realName);
            cardInfoSave.setBankName(bankInfo.get("bankName") == null ? "" : bankInfo.get("bankName"));
            cardInfoSave.setStatus(UserCardInfo.STATUS_SUCCESS);
            cardInfoSave.setMainCard(UserCardInfo.MAINCARD_Z);
            cardInfoSave.setType(UserCardInfo.TYPE_DEBIT);
            cardInfoSave.setUpdateTime(new Date());
            cardInfoSave.setIsBand(1);
            if (userService.defaultCardCount(Integer.parseInt(userId)) <= 0) {
                cardInfoSave.setCardDefault(1);
            }
            boolean flag = userBankService.updateUserBankCard(cardInfoSave);

            log.info("ChanpayService updateUserBankInfo userId=" + userId + " old flag=" + flag);

            if (!flag) {
                resultModel.setSucc(flag);
                resultModel.setCode("9999");
                resultModel.setMessage("帐户银行信息保存失败");
                return resultModel;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            //设置状态---重新绑卡
            infoIndexService.authBankOld(map);
        }

        log.info("ChanpayService updateUserBankInfo end resultMap=" + JSON.toJSONString(resultModel));
        return resultModel;
    }

    /**
     * 提现
     * @param paramMap map
     * @return map
     */
    @Override
    public Map<String, Object> getWithdrawRequest(Map<String, String> paramMap) throws Exception {
        log.info("ChanpayService getWithdrawRequest start");
        log.info("ChanpayService getWithdrawRequest paramMap=" + JSON.toJSONString(paramMap));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
//        pairs.put("desc", PropertiesConfigUtil.get("APP_NAME") + "放款");

        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(paramMap.get("userId"));
        outOrders.setOrderType("CHANPAY");
        outOrders.setOrderNo(GenerateNo.nextOrdId());
        outOrders.setAct("WITHDRAW");
        outOrders.setReqParams(JSON.toJSONString(paramMap));
        outOrders.setAddTime(new Date());
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        log.info("ChanPayWithdraw before insert into outOrder,outOrder no is:" + outOrders.getOrderNo());
        outOrdersService.insert(outOrders);
        log.info("ChanPayWithdraw after insert into outOrder,outOrder no is:" + outOrders.getOrderNo());

        //发送https请求
        Map<String,Object> yopresponsemap = new HashMap<>();
        try{
            String result = ChanPayUtil.sendPost(paramMap, BaseConstant.CHARSET, BaseConstant.MERCHANT_PRIVATE_KEY);
            log.info("ChanPayWithdraw result=" + result);
            yopresponsemap.put("errorCode", "-1");
            if (ChanPayUtil.verify(result, BaseConstant.MERCHANT_PUBLIC_KEY)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                jsonObject.get("AppRetcode");//返回码
                jsonObject.get("AppRetMsg");//返回信息
                jsonObject.get("AcceptStatus");//返回码
                if (jsonObject.get("AcceptStatus").equals("S")) {
                    yopresponsemap.put("errorCode", "0000");
                }
                yopresponsemap.put("errorMsg", jsonObject.get("AppRetMsg") == null ? jsonObject.get("Memo") : jsonObject.get("AppRetMsg"));
                log.info("yopresponseMap :{}",JSON.toJSONString(yopresponsemap));
            }

        }catch (Exception e){
            log.error("request yeepay error:{}",e);
        }
//        // 更新订单
        OutOrders ordersNew = new OutOrders();
        ordersNew.setId(outOrders.getId());
        if(yopresponsemap != null){

            if(yopresponsemap.get("errorCode") != null &&  !"0000".equals(yopresponsemap.get("errorCode"))){
                resultMap = new HashMap<>();
                resultMap.put("code", "400");
                resultMap.put("msg", "代付失败" + yopresponsemap.get("errorCode").toString() + ":【" + yopresponsemap.get("errorMsg").toString() + "】");
                ordersNew.setStatus(OutOrders.STATUS_OTHER);
                outOrdersService.updateByOrderNo(ordersNew);
                return resultMap;
            }
            if ("0000".equals(yopresponsemap.get("errorCode").toString())) {
                resultMap = new HashMap<>();
                resultMap.put("code", BorrowOrder.SUB_SUBMIT);
                resultMap.put("msg", "支付正在处理中");
                ordersNew.setStatus(OutOrders.STATUS_WAIT);
                outOrdersService.updateByOrderNo(ordersNew);
                return resultMap;
            }
        }
        ordersNew.setStatus(OutOrders.STATUS_OTHER);
        outOrdersService.updateByOrderNo(ordersNew);
        return resultMap;
    }

    /**
     * 无短验绑卡请求
     * @param paramMap map
     * @return map
     */
    @Override
    public Map<String, Object> getUnSendBindCardRequest(Map<String, String> paramMap) {
        log.info("ChanpayService getUnSendBindCardRequest start");
        log.info("ChanpayService getUnSendBindCardRequest paramMap=" + JSON.toJSONString(paramMap));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        String requestUrl = PayConstants.UNSENDBIND_CARD_REQUEST_URL;
        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        String cardNo = YeepayApiUtil.formatString(paramMap.get("cardNo"));
        String idCardNo = YeepayApiUtil.formatString(paramMap.get("idCardNo"));
        String realName = YeepayApiUtil.formatString(paramMap.get("realName"));
        String phone = YeepayApiUtil.formatString(paramMap.get("phone"));

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        dataMap.put("identityid", PropertiesConfigUtil.get("RISK_BUSINESS")+userId);
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("cardno", cardNo);
        dataMap.put("idcardno", idCardNo);
        dataMap.put("idcardtype", "ID");
        dataMap.put("username", realName);
        dataMap.put("phone", phone);
        dataMap.put("requesttime", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));

        log.info("ChanpayService getUnSendBindCardRequest requestUrl=" + requestUrl);
        log.info("ChanpayService getUnSendBindCardRequest dataMap=" + JSON.toJSONString(dataMap));

        String orderNo = GenerateNo.nextOrdId();
        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(userId);
        outOrders.setOrderType("YEEPAY");
        outOrders.setOrderNo(orderNo);
        outOrders.setAct("BINDCARD_REQUEST");
        outOrders.setReqParams(JSON.toJSONString(dataMap));
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        outOrdersService.insert(outOrders);

        OutOrders newOutOrder = new OutOrders();
        newOutOrder.setOrderNo(orderNo);
        //调用易宝api,获取返回map
//        resultMap = YeepayApiUtil.httpExecuteResult(dataMap, userId, requestUrl, "getUnSendBindCardRequest");
        try{
            resultMap = YeepayApiUtil.yeepayYOP(dataMap,requestUrl);
        }catch (Exception e){
            log.error("getUnSendBindCardRequest error:{}",e);
        }

        log.info("ChanpayService getUnSendBindCardRequest resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
        if (resultMap == null) {
            resultMap = new HashMap<String, Object>();
            resultMap.put("code", "400");
            resultMap.put("msg", "绑卡失败，请重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }

        Object orderStatus = resultMap.get("status");
        log.info("ChanpayService getUnSendBindCardRequest userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
        if (orderStatus != null && "BIND_SUCCESS".equals(orderStatus.toString())) {
            resultMap.put("code", "0000");
            resultMap.put("msg", "绑卡成功");
            resultMap.put("bankcode", resultMap.get("bankcode"));
            newOutOrder.setStatus(OutOrders.STATUS_SUC);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }
        log.info("ChanpayService getUnSendBindCardRequest userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
        if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
            resultMap.put("code", "400");
            resultMap.put("msg", resultMap.get("errormsg"));
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }

        resultMap.put("code", "400");
        resultMap.put("msg", "绑卡失败，请重试");

        newOutOrder.setStatus(OutOrders.STATUS_OTHER);
        outOrdersService.updateByOrderNo(newOutOrder);
        return resultMap;
    }


    /**
     * 有短验支付短信请求
     * @param paramMap map
     * @return map
     */
    @Override
    public Map<String, Object> getRechargeSmsCode(Map<String, String> paramMap) {
        log.info("ChanpayService getRechargeSmsCode start");
        log.info("ChanpayService getRechargeSmsCode paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        log.info("ChanpayService getRechargeSmsCode userId=" + userId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        dataMap.put("advicesmstype", "MESSAGE");
        log.info("ChanpayService getRechargeSmsCode dataMap=" + JSON.toJSONString(dataMap));

        String orderNo = GenerateNo.nextOrdId();

        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(userId);
        outOrders.setOrderType("YEEPAY");
        outOrders.setOrderNo(GenerateNo.nextOrdId());
        outOrders.setAct("WITHHOLD_RESENDSMS");
        outOrders.setReqParams(JSON.toJSONString(dataMap));
        outOrders.setStatus(OutOrders.STATUS_WAIT);

        outOrdersService.insert(outOrders);

        OutOrders newOutOrder = new OutOrders();
        newOutOrder.setOrderNo(orderNo);
        //调用易宝api,获取返回map
//        resultMap = YeepayApiUtil.httpExecuteResult(dataMap, userId, PayConstants.AUTOPAY_RESENDSMS_URL, "getRechargeSmsCode");
        try{
            resultMap = YeepayApiUtil.yeepayYOP(dataMap,PayConstants.AUTOPAY_RESENDSMS_URL);

        }catch (Exception e ){
            log.error("getRechargeSmsCode post yeepay error:{}",e);
        }

        log.info("ChanpayService getRechargeSmsCode resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
        if (resultMap == null) {
            resultMap = new HashMap<>();
            resultMap.put("code", "400");
            resultMap.put("msg", "操作异常，请刷新页面重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }

        Object orderStatus = resultMap.get("status");
        log.info("ChanpayService getBindCardSmsCode userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
        //待短验
        if (orderStatus != null && "TO_VALIDATE".equals(orderStatus.toString())) {
            resultMap.put("code", "0000");
            resultMap.put("msg", "请求成功");
            outOrders.setStatus(OutOrders.STATUS_SUC);
            outOrdersService.updateByOrderNo(outOrders);
            return resultMap;
        }
        log.info("ChanpayService getBindCardSmsCode userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
        if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
            resultMap.put("code", "400");
            resultMap.put("msg", resultMap.get("errormsg"));
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }

        resultMap.put("code", "400");
        resultMap.put("msg", "操作异常，请刷新页面重试");

        newOutOrder.setStatus(OutOrders.STATUS_OTHER);
        outOrdersService.updateByOrderNo(newOutOrder);
        return resultMap;
    }

    /**
     * 有短验支付确认请求
     * @param paramMap map
     * @return result
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultModel<Map<String, Object>> getRechargeConfirm(Map<String, String> paramMap) {

        ResultModel resultModel = new ResultModel(false, ErrorCode.ERROR_500.getCode(), ErrorCode.ERROR_500.getMsg());
        log.info("ChanpayService getRechargeConfirm start");
        log.info("ChanpayService getRechargeConfirm paramMap=" + JSON.toJSONString(paramMap));

        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        //商户私钥
//        String merchantPrivateKey = PayConstants.MERCHANT_PRIVATE_KEY;
//        String requestUrl = PayConstants.AUTOPAY_CONFIRM_URL;

        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        String smsCode = YeepayApiUtil.formatString(paramMap.get("smsCode"));

        log.info("ChanpayService getRechargeConfirm userId=" + userId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        dataMap.put("validatecode", smsCode);

//        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//        dataMap.put("sign", sign);

//        log.info("ChanpayService getRechargeConfirm requestUrl=" + requestUrl);
        log.info("ChanpayService getRechargeConfirm dataMap=" + JSON.toJSONString(dataMap));

        String orderNo = GenerateNo.nextOrdId();

        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(userId);
        outOrders.setOrderType("YEEPAY");
        outOrders.setOrderNo(GenerateNo.nextOrdId());
        outOrders.setAct("WITHHOLD_CONFIRM");
        outOrders.setReqParams(JSON.toJSONString(dataMap));
        outOrders.setStatus(OutOrders.STATUS_WAIT);

        outOrdersService.insert(outOrders);

        OutOrders newOutOrder = new OutOrders();
        newOutOrder.setOrderNo(orderNo);

        //调用易宝api,获取返回map
        Map<String, Object> resultMap = new HashMap<>();
//        Map<String, Object> resultMap = YeepayApiUtil.httpExecuteResult(dataMap, userId, PayConstants.AUTOPAY_CONFIRM_URL, "getRechargeConfirm");
        try{
            resultMap = YeepayApiUtil.yeepayYOP(dataMap,PayConstants.AUTOPAY_CONFIRM_URL);

        }catch (Exception e){
            log.error("getRechargeConfirm post yeepay error:{}",e);
        }
        log.info("ChanpayService getRechargeConfirm resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
        if (resultMap == null) {
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            resultModel.setCode(ErrorCode.ERROR_400.getCode());
            resultModel.setMessage(ErrorCode.ERROR_400.getMsg());
            return resultModel;
        }

        log.info("ChanpayService getRechargeConfirm userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
        if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
            resultModel.setCode(ErrorCode.ERROR_400.getCode());
            if ("TZ0200002".equals(resultMap.get("errorcode"))) {
                resultModel.setMessage("银行卡已失效，请重新绑定");
            } else if ("TZ2010032".equals(resultMap.get("errorcode"))) {
                resultModel.setMessage("卡内余额不足");
            } else {
                resultModel.setMessage(String.valueOf(resultMap.get("errormsg")));
            }
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultModel;
        }

        String orderStatus = String.valueOf(resultMap.get("status"));

        if (!"null".equals(orderStatus)) {
            log.info("ChanpayService getRechargeConfirm userId=" + userId + " status=" + orderStatus);
            switch (PayRecordStatus.getByCode(orderStatus)) {
                case TO_VALIDATE:
                    resultModel.setCode(ErrorCode.ERROR_400.getCode());
                    resultModel.setMessage(String.valueOf(resultMap.get("errormsg")));
                    break;
                case PROCESSING:
                    resultModel.setSucc(true);
                    resultModel.setCode(ErrorCode.ERROR_0000.getCode());
                    resultModel.setMessage(ErrorCode.ERROR_0000.getMsg());
                    break;
                case TIME_OUT:
                case PAY_FAIL:
                case FAIL:
                    resultModel.setCode(ErrorCode.ERROR_400.getCode());
                    resultModel.setMessage(String.valueOf(resultMap.get("errormsg")));
                    break;
                default:
                    resultModel.setCode(ErrorCode.ERROR_400.getCode());
                    resultModel.setMessage("支付失败，请重试");
                    break;
            }
        } else {
            log.info("ChanpayService getRechargeConfirm userId=" + userId + " status=null");
        }
        return resultModel;
    }

    /**
     * 易宝支付结果查询接口
     * @param ypBatchPayResultReq req
     * @return result
     */
    @Override
    public PageResultModel<YPBatchPayResultModel> getYBPayResult(YPBatchPayResultReq ypBatchPayResultReq) {
        log.info("ChanpayService getYBPayResult ypBatchPayResultReq=" + JSON.toJSONString(ypBatchPayResultReq));

        PageResultModel<YPBatchPayResultModel> result = new PageResultModel<>(false);
        //商户私钥
        String hmacKey = PayConstants.HMAC_KEY;
        String requestUrl = PayConstants.BATCH_DETAIL_QUERY_REQURL;

        //【1】将请求的数据和商户自己的密钥拼成一个字符串,获得加密后的请求hmac
        String hmacStr = YeepayUtil.getBatchDetailQueryStr(ypBatchPayResultReq, hmacKey);

        log.info("ChanpayService getYBPayResult 签名之前的源数据=" + hmacStr);
        //获得签名
        Map<String, Object> signMap = YeepayUtil.getSign(hmacStr);

        log.info("ChanpayService getYBPayResult 经过md5和数字证书签名之后的数据=" + signMap.get("sign").toString());
        ypBatchPayResultReq.setHmac(String.valueOf(signMap.get("sign")));

        //【2】构建请求报文xml，并发送请求
        String requestXml = YeepayUtil.getBatchPayDetailResponseXml(ypBatchPayResultReq);

        Session tempsession = null;
        tempsession = (Session) signMap.get("session");


        Document document = null;
        try {
            document = DocumentHelper.parseText(requestXml);
        } catch (DocumentException e) {
        }

        document.setXMLEncoding("GBK");
        log.info("ChanpayService getYBPayResult 完整xml请求报文==>:" + document.asXML());

        log.info("ChanpayService getYBPayResult requestUrl=" + requestUrl);

        String responseMsg = CallbackUtils.httpRequest(requestUrl, document.asXML(), "POST", "gbk", "text/xml ;charset=gbk", false);
        log.info("ChanpayService getYBPayResult 服务器响应xml报文:" + responseMsg);


        //【3】解析返回结构报文
        Element rootEle = null;
        try {
            document = DocumentHelper.parseText(responseMsg);
        } catch (DocumentException e) {
        }
        rootEle = document.getRootElement();
        String cmdValue = rootEle.elementText("hmac");


        //对服务器响应报文进行验证签名
        if (StringUtils.isBlank(cmdValue)) {
            result.setErrorCode("400");
            result.setErrorMessage("代付失败【代付方系统异常】");
            return result;
        }

        Map<String, Object> responseHeadMap = YeepayUtil.getVerifySignMap(responseMsg, tempsession, hmacKey);

        if (responseHeadMap == null || responseHeadMap.size() <= 0) {
            result.setErrorCode("400");
            result.setErrorMessage("代付失败【签名验证失败】");
            return result;
        }
        log.info("ChanpayService getYBPayResult resultMap=" + JSONObject.toJSONString(responseHeadMap));


        // List<YPBatchPayResultModel> resultList = new ArrayList<>(responseMap.size());

        YPBatchPayResultModel ypBatchPayResultModel = new YPBatchPayResultModel();
        ypBatchPayResultModel.setOrderId("");
        ypBatchPayResultModel.setRlCode("");
        ypBatchPayResultModel.setBankStatus("");
        ypBatchPayResultModel.setRequestDate("");
        ypBatchPayResultModel.setPayeeName("");
        ypBatchPayResultModel.setPayeeBankName("");
        ypBatchPayResultModel.setPayeeBankAccount("");
        ypBatchPayResultModel.setAmount("");
        ypBatchPayResultModel.setNote("");
        ypBatchPayResultModel.setFee("");
        ypBatchPayResultModel.setRealPayAmount("");
        ypBatchPayResultModel.setCompleteDate("");
        ypBatchPayResultModel.setRefundDate("");
        ypBatchPayResultModel.setFailDesc("");
        ypBatchPayResultModel.setAbstractInfo("");
        // ypBatchPayResultModel.setRemarksInfo(String.valueOf(.get("1")));


        return result;
    }

    /**
     * 易宝用户还款结果查询接口
     * @param ypRepayRecordReq req
     * @param userId userId
     * @return result
     */
    @Override
    public ResultModel<FuiouRepayResultModel> getFuiouRepayResult(YPRepayRecordReq ypRepayRecordReq, String userId) {
        log.info("ChanpayService getYBRepayResult start");
        log.info("ChanpayService getYBRepayResult paramMap=" + JSON.toJSONString(ypRepayRecordReq));
        ResultModel<FuiouRepayResultModel> resultModel = new ResultModel<>(false);
        //商户私钥
//        String merchantPrivateKey = PayConstants.MERCHANT_PRIVATE_KEY;
//        String requestUrl = ;
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", ypRepayRecordReq.getMerchantNo());
        dataMap.put("requestno", ypRepayRecordReq.getRequestNo());
        log.info("ChanpayService getYBRepayResult reqData=" + JSON.toJSONString(ypRepayRecordReq));
        //调用易宝api,获取返回map
//        Map<String, Object> resultMap = YeepayApiUtil.httpExecuteResult(dataMap, userId, requestUrl, "getYBRepayResult");
        Map<String, Object> resultMap = new HashMap<>();
        try{

            NewProtocolCheckResultXmlBeanReq beanReq = new NewProtocolCheckResultXmlBeanReq();
            beanReq.setVersion("3.0");
            beanReq.setMchntCd(FuiouConstants.API_MCHNT_CD);
            beanReq.setMchntOrderId(ypRepayRecordReq.getRequestNo());
            beanReq.setSign(FuiouUtil.getSign(beanReq.signStr(FuiouConstants.API_MCHNT_KEY), "MD5", FuiouConstants.privatekey));

            String APIFMS = XMapUtil.toXML(beanReq, FuiouConstants.charset);
            //APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

            resultMap = FuiouApiUtil.FuiouYOP(APIFMS,FuiouConstants.NEW_PROTOCOL_CHECKRESULT_URL);
        }catch (Exception e){
            log.error("getYBRepayResult error:{}",e);
        }
        if (resultMap == null) {
            log.info("ChanpayService getYBRepayResult resultMap= null");
            resultModel.setCode(ErrorCode.ERROR_400.getCode());
            resultModel.setMessage("查询失败，请重试");
            return resultModel;
        }
        log.info("ChanpayService getYBRepayResult resultMap= " + JSON.toJSONString(resultMap));
        resultModel.setSucc(true);
        resultModel.setCode(resultMap.get("errorcode") == null ? null : String.valueOf(resultMap.get("errorcode")));
        resultModel.setMessage(resultMap.get("errormsg") == null ? null : String.valueOf(resultMap.get("errormsg")));

        FuiouRepayResultModel repayResultModel = new FuiouRepayResultModel();
        repayResultModel.setRequestNo(ypRepayRecordReq.getRequestNo());

        repayResultModel.setRequestNo(resultMap.get("mchntorderId") == null ? null : String.valueOf(resultMap.get("mchntorderId")));
        repayResultModel.setStatus(resultMap.get("payStatus") == null ? null : String.valueOf(resultMap.get("payStatus")));
        resultModel.setData(repayResultModel);
        return resultModel;
    }

}
