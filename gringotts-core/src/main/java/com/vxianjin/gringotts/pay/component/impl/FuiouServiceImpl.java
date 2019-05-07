package com.vxianjin.gringotts.pay.component.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfca.util.pki.cipher.Session;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.vxianjin.gringotts.pay.common.constants.FuiouConstants;
import com.vxianjin.gringotts.pay.common.constants.PayConstants;
import com.vxianjin.gringotts.pay.common.enums.ErrorCode;
import com.vxianjin.gringotts.pay.common.enums.PayRecordStatus;
import com.vxianjin.gringotts.pay.common.util.*;
import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouApiUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouUtil;
import com.vxianjin.gringotts.pay.common.util.fuiou.HttpPoster;
import com.vxianjin.gringotts.pay.common.util.fuiou.XMapUtil;
import com.vxianjin.gringotts.pay.component.FuiouService;
import com.vxianjin.gringotts.pay.model.*;
import com.vxianjin.gringotts.pay.model.fuiou.NewProtocolBindXmlBeanReq;
import com.vxianjin.gringotts.pay.model.fuiou.PayforreqXmlBeanReq;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.fuiou.MD5Util;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IUserBankDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.OutOrders;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zed 易宝支付服务类
 */
@Service
public class FuiouServiceImpl implements FuiouService {

    private static final Logger log = LoggerFactory.getLogger(FuiouServiceImpl.class);

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
        log.info("YeepayService getBindCardRequest start");
        log.info("YeepayService getBindCardRequest paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String merchantNo = FuiouConstants.API_MCHNT_CD;
        String requestUrl = FuiouConstants.NEW_PROTOCOL_BINDMSG_URL;
        String requestNo = FuiouApiUtil.formatString(paramMap.get("requestNo"));
        String userId = FuiouApiUtil.formatString(paramMap.get("userId"));
        String realName = FuiouApiUtil.formatString(paramMap.get("realName"));
        String idNo = FuiouApiUtil.formatString(paramMap.get("idNo"));
        String cardNo = FuiouApiUtil.formatString(paramMap.get("cardNo"));
        String phone = FuiouApiUtil.formatString(paramMap.get("phone"));

        log.info("YeepayService getBindCardRequest userId=" + userId);
        try {
            NewProtocolBindXmlBeanReq beanReq = new NewProtocolBindXmlBeanReq();
            beanReq.setVersion("1.0");
            beanReq.setTradeDate(DateUtil.formatDate(new Date(), "yyyyMMdd"));
            beanReq.setMchntCd(merchantNo);
            beanReq.setUserId(PropertiesConfigUtil.get("RISK_BUSINESS")+userId);
            beanReq.setAccount(realName);
            beanReq.setCardNo(cardNo);
            beanReq.setIdType("0");
            beanReq.setIdCard(idNo);
            beanReq.setMobileNo(phone);
            beanReq.setMchntSsn(requestNo);
            beanReq.setSign(FuiouUtil.getSign(beanReq.sendMsgSignStr(FuiouConstants.API_MCHNT_KEY), "MD5", FuiouConstants.privatekey));

            log.info("YeepayService getBindCardRequest requestUrl=" + requestUrl);
            log.info("YeepayService getBindCardRequest dataMap=" + JSON.toJSONString(beanReq));

            String orderNo = GenerateNo.nextOrdId();
            OutOrders outOrders = new OutOrders();
            outOrders.setUserId(userId);
            outOrders.setOrderType("FUIOU");
            outOrders.setOrderNo(GenerateNo.nextOrdId());
            outOrders.setAct("BINDCARD_MSG");
            outOrders.setReqParams(JSON.toJSONString(beanReq));
            outOrders.setStatus(OutOrders.STATUS_WAIT);

            outOrdersService.insert(outOrders);

            //调用易宝api,获取返回结果
    //        resultMap = YeepayApiUtil.httpExecuteResult(dataMap, userId, requestUrl, "getBindCardRequest");
            //Map<String,String> map = new HashMap<String, String>();
            String APIFMS = XMapUtil.toXML(beanReq, FuiouConstants.charset);
            APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));
            //map.put("MCHNTCD",merchantNo);
            //map.put("APIFMS", APIFMS);
            //String result = new HttpPoster(requestUrl).postStr(map);
            //result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

            resultMap = FuiouApiUtil.FuiouYOP(APIFMS, requestUrl);

            OutOrders newOutOrder = new OutOrders();
            newOutOrder.setOrderNo(orderNo);

            log.info("YeepayService getBindCardRequest resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
            if (resultMap == null) {
                resultMap = new HashMap<>();
                resultMap.put("code", "400");
                resultMap.put("msg", "绑卡失败，请重试");
                newOutOrder.setStatus(OutOrders.STATUS_OTHER);
                outOrdersService.updateByOrderNo(newOutOrder);
                return resultMap;
            }

            Object orderStatus = resultMap.get("status");
            log.info("YeepayService getBindCardRequest userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
            //待短验
            if (orderStatus != null && "0000".equals(orderStatus.toString())) {
                resultMap.put("code", "0000");
                resultMap.put("msg", "请求成功");
                newOutOrder.setStatus(OutOrders.STATUS_SUC);
                outOrdersService.updateByOrderNo(newOutOrder);
                return resultMap;
            }
            log.info("YeepayService getBindCardRequest userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
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
        }catch (Exception e){
            log.error("post yeepay error:{}",e);
        }
        return resultMap;
    }

    /**
     * 绑卡确认请求v1.12
     *
     * @param bindCardConfirmReq req
     * @return result
     */
    @Override
    public ResultModel<Map<String, Object>> getBindCardConfirm(YPBindCardConfirmReq bindCardConfirmReq) {
        log.info("YeepayService getBindCardConfirm start");
        log.info("YeepayService getBindCardConfirm paramMap=" + JSON.toJSONString(bindCardConfirmReq));

        ResultModel<Map<String, Object>> resultModel = new ResultModel<>(false);

        if (bindCardConfirmReq.getDataMap() == null || bindCardConfirmReq.getDataMap().size() <= 0) {
            resultModel.setMessage("datamap不允许为空");
            return resultModel;
        }

        String userId = bindCardConfirmReq.getUserId();
        log.info("YeepayService getBindCardConfirm userId=" + userId);

        //请求易宝api,获得返回结果
//        Map<String, Object> resultMap = YeepayApiUtil.httpExecuteResult(bindCardConfirmReq.getDataMap(), userId, requestUrl, "getBindCardConfirm");
        Map<String,Object> resultMap = new HashMap<>();
        try{

            User user = userService.searchByUserid(Integer.parseInt(userId));

            NewProtocolBindXmlBeanReq beanReq = new NewProtocolBindXmlBeanReq();
            beanReq.setVersion("1.0");
            beanReq.setTradeDate(DateUtil.formatDate(new Date(), "yyyyMMdd"));
            beanReq.setMchntCd(FuiouConstants.API_MCHNT_CD);
            beanReq.setUserId(PropertiesConfigUtil.get("RISK_BUSINESS")+userId);
            beanReq.setAccount(user.getRealname());
            beanReq.setCardNo(bindCardConfirmReq.getCardNo());
            beanReq.setIdType("0");
            beanReq.setIdCard(user.getIdNumber());
            beanReq.setMobileNo(bindCardConfirmReq.getPhone());
            beanReq.setMchntSsn(bindCardConfirmReq.getRequestNo());
            beanReq.setMsgCode(bindCardConfirmReq.getSmsCode());
            beanReq.setSign(FuiouUtil.getSign(beanReq.sendMsgSignStr(FuiouConstants.API_MCHNT_KEY), "MD5", FuiouConstants.privatekey));

            String APIFMS = XMapUtil.toXML(beanReq, FuiouConstants.charset);
            APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(FuiouConstants.API_MCHNT_KEY));

            resultMap = FuiouApiUtil.FuiouYOP(APIFMS,FuiouConstants.NEW_PROTOCOL_BINDCOMMIT_URL);

        }catch (Exception e){
            log.error("post bindCard confirm error:{}",e);
        }
        if (resultMap == null || resultMap.size() <= 0) {
            resultModel.setCode(ErrorCode.ERROR_400.getCode());
            resultModel.setMessage(ErrorCode.ERROR_400.getMsg());
            return resultModel;
        }

        resultModel.setData(resultMap);
        log.info("YeepayService getBindCardConfirm userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
        if (resultMap.containsKey("errorcode") && !"".equals(resultMap.get("errorcode"))) {
            resultModel.setCode(String.valueOf(resultMap.get("errorcode")));
            resultModel.setMessage(String.valueOf(resultMap.get("errormsg")));
            return resultModel;
        }

        String orderStatus = String.valueOf(resultMap.get("status"));
        log.info("YeepayService getBindCardConfirm userId=" + userId + " status=" + orderStatus);
        //绑卡
        if (!"null".equals(orderStatus) && "0000".equals(orderStatus)) {
            resultModel.setSucc(true);
            resultModel.setCode(ErrorCode.ERROR_0000.getCode());
            resultModel.setMessage(ErrorCode.ERROR_0000.getMsg());
            return resultModel;
        }
        resultModel.setCode(ErrorCode.ERROR_400.getCode());
        resultModel.setMessage(ErrorCode.ERROR_400.getMsg());
        return resultModel;
    }


    @Override
    public Map<String, Object> getBindCardSmsCode(Map<String, String> paramMap) {
        log.info("YeepayService getBindCardSmsCode paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        log.info("YeepayService getBindCardSmsCode userId=" + userId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        log.info("YeepayService getBindCardSmsCode dataMap=" + JSON.toJSONString(dataMap));

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
        log.info("YeepayService getBindCardSmsCode resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));

        if (resultMap == null) {
            resultMap = new HashMap<>();
            resultMap.put("code", "400");
            resultMap.put("msg", "操作异常，请刷新页面重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }
        Object orderStatus = resultMap.get("status");
        log.info("YeepayService getBindCardSmsCode userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
        //待短验
        if (orderStatus != null && "TO_VALIDATE".equals(orderStatus.toString())) {
            resultMap.put("code", "0000");
            resultMap.put("msg", "请求成功");
            outOrders.setStatus(OutOrders.STATUS_SUC);
            outOrdersService.updateByOrderNo(outOrders);
            return resultMap;
        }
        log.info("YeepayService getBindCardSmsCode userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
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
        log.info("YeepayService updateUserBankInfo start");
        log.info("YeepayService updateUserBankInfo paramMap=" + JSON.toJSONString(paramMap));

        ResultModel resultModel = new ResultModel(true, "0", "绑卡成功");

        String userId = paramMap.get("user_id");
        String bankId = paramMap.get("bank_id");
        String cardNo = paramMap.get("card_no");
        String phone = paramMap.get("phone");
        String realName = paramMap.get("real_name");
        String agreeno = paramMap.get("agreeno");

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
            cardInfo.setAgreeno(agreeno);
            boolean flag = userBankDao.saveUserbankCard(cardInfo);
            log.info("YeepayService updateUserBankInfo userId=" + userId + " new flag=" + flag);

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
            //地推 绑卡
//            ThreadPool pool = ThreadPool.getInstance();
//            pool.execute(new DtThread(UserPushUntil.BANKAPPROVE, Integer.valueOf(userId), null, new Date(), userService, pushUserService, borrowOrderService, null));

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
            boolean flag = userBankService.updateUserBankCard(cardInfoSave);

            log.info("YeepayService updateUserBankInfo userId=" + userId + " old flag=" + flag);

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

        log.info("YeepayService updateUserBankInfo end resultMap=" + JSON.toJSONString(resultModel));
        return resultModel;
    }

    /**
     * 提现
     * @param paramMap map
     * @return map
     */
    @Override
    public Map<String, Object> getWithdrawRequest(Map<String, String> paramMap) throws Exception {
        log.info("YeepayService getWithdrawRequest start");
        log.info("YeepayService getWithdrawRequest paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        Map<String, Object> params = new HashMap<>();
        //集团编号
        params.put("customerNumber", FuiouConstants.API_MCHNT_CD);
        //商户编号
        params.put("groupNumber", PayConstants.GROUP_ID);
        //批次号
        params.put("batchNo", paramMap.get("batchNo"));
        //借款订单编号
        params.put("orderId", paramMap.get("orderId"));
        //提现金额（格式：0.00）元
        params.put("amount", paramMap.get("amount"));
        //加急
        params.put("urgency", "1");
        //银行卡账户名称
        params.put("accountName", paramMap.get("cardName"));
        //银行卡号
        params.put("accountNumber", paramMap.get("cardNo"));
        //银行编号
        params.put("bankCode", paramMap.get("bankCode"));
        params.put("feeType", "SOURCE");
//        pairs.put("desc", PropertiesConfigUtil.get("APP_NAME") + "放款");

        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(paramMap.get("userId"));
        outOrders.setOrderType("FUIOU");
        outOrders.setOrderNo(GenerateNo.nextOrdId());
        outOrders.setAct("WITHDRAW");
        outOrders.setReqParams(JSON.toJSONString(params));
        //请求时间
        outOrders.setAddTime(new Date());
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        log.info(" before insert into outOrder,outOrder no is:" + outOrders.getOrderNo());
        outOrdersService.insert(outOrders);
        log.info(" after insert into outOrder,outOrder no is:" + outOrders.getOrderNo());

        PayforreqXmlBeanReq payforreqXmlBeanReq = new PayforreqXmlBeanReq();
        //payforreqXmlBeanReq.setAccnTnm();

        String xml = XMapUtil.toXML(payforreqXmlBeanReq, FuiouConstants.charset);

        String macSource = FuiouConstants.API_MCHNT_CD + "|" + FuiouConstants.API_MCHNT_KEY + "|"+ FuiouConstants.PAYFORREQ + "|" + xml;
        String mac = MD5Util.encode(macSource, "UTF-8").toUpperCase();
        String url = FuiouConstants.PAYFORREQ_SINCOMEFORREQ_URL;
        Map<String, String> map = new HashMap();
        map.put("merid", FuiouConstants.API_MCHNT_CD);
        map.put("reqtype", "payforreq");
        map.put("xml", xml);
        map.put("mac", mac);

        //发送https请求
        Map<String,Object> yopresponsemap = new HashMap<>();
        try{
            yopresponsemap	=	FuiouApiUtil.FuiouPS(map, url);
            log.info("yopresponseMap :{}",JSON.toJSONString(yopresponsemap));
        }catch (Exception e){
            log.error("request yeepay error:{}",e);
        }
//        // 更新订单
        OutOrders ordersNew = new OutOrders();
        ordersNew.setId(outOrders.getId());
        if(yopresponsemap!=null){

            if(!"BAC001".equals(yopresponsemap.get("errorCode"))){
                resultMap = new HashMap<>();
                resultMap.put("code", "400");
                resultMap.put("msg", "代付失败" + yopresponsemap.get("errorCode").toString() + ":【" + yopresponsemap.get("errorMsg").toString() + "】");
                ordersNew.setStatus(OutOrders.STATUS_OTHER);
                outOrdersService.updateByOrderNo(ordersNew);
                return resultMap;
            }
            if ("0025".equals(yopresponsemap.get("transferStatusCode").toString())
                    || "0029".equals(yopresponsemap.get("transferStatusCode").toString()) ||
                    "0030".equals(yopresponsemap.get("transferStatusCode").toString()) ||
                    "0026".equals(yopresponsemap.get("transferStatusCode").toString())) {
                resultMap = new HashMap<>();
                resultMap.put("code", BorrowOrder.SUB_SUBMIT);
                resultMap.put("msg", "支付正在处理中");
                ordersNew.setStatus(OutOrders.STATUS_SUC);
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
        log.info("YeepayService getUnSendBindCardRequest start");
        log.info("YeepayService getUnSendBindCardRequest paramMap=" + JSON.toJSONString(paramMap));

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

        log.info("YeepayService getUnSendBindCardRequest requestUrl=" + requestUrl);
        log.info("YeepayService getUnSendBindCardRequest dataMap=" + JSON.toJSONString(dataMap));

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

        log.info("YeepayService getUnSendBindCardRequest resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
        if (resultMap == null) {
            resultMap = new HashMap<String, Object>();
            resultMap.put("code", "400");
            resultMap.put("msg", "绑卡失败，请重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }

        Object orderStatus = resultMap.get("status");
        log.info("YeepayService getUnSendBindCardRequest userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
        if (orderStatus != null && "BIND_SUCCESS".equals(orderStatus.toString())) {
            resultMap.put("code", "0000");
            resultMap.put("msg", "绑卡成功");
            resultMap.put("bankcode", resultMap.get("bankcode"));
            newOutOrder.setStatus(OutOrders.STATUS_SUC);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }
        log.info("YeepayService getUnSendBindCardRequest userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
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
        log.info("YeepayService getRechargeSmsCode start");
        log.info("YeepayService getRechargeSmsCode paramMap=" + JSON.toJSONString(paramMap));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "500");
        resultMap.put("msg", "请求异常");
        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        log.info("YeepayService getRechargeSmsCode userId=" + userId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        dataMap.put("advicesmstype", "MESSAGE");
        log.info("YeepayService getRechargeSmsCode dataMap=" + JSON.toJSONString(dataMap));

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

        log.info("YeepayService getRechargeSmsCode resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
        if (resultMap == null) {
            resultMap = new HashMap<>();
            resultMap.put("code", "400");
            resultMap.put("msg", "操作异常，请刷新页面重试");
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            return resultMap;
        }

        Object orderStatus = resultMap.get("status");
        log.info("YeepayService getBindCardSmsCode userId=" + userId + " status=" + (orderStatus != null ? orderStatus.toString() : "null"));
        //待短验
        if (orderStatus != null && "TO_VALIDATE".equals(orderStatus.toString())) {
            resultMap.put("code", "0000");
            resultMap.put("msg", "请求成功");
            outOrders.setStatus(OutOrders.STATUS_SUC);
            outOrdersService.updateByOrderNo(outOrders);
            return resultMap;
        }
        log.info("YeepayService getBindCardSmsCode userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
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
        log.info("YeepayService getRechargeConfirm start");
        log.info("YeepayService getRechargeConfirm paramMap=" + JSON.toJSONString(paramMap));

        //商户编号
        String merchantNo = PayConstants.MERCHANT_NO;
        //商户私钥
//        String merchantPrivateKey = PayConstants.MERCHANT_PRIVATE_KEY;
//        String requestUrl = PayConstants.AUTOPAY_CONFIRM_URL;

        String requestNo = YeepayApiUtil.formatString(paramMap.get("requestNo"));
        String userId = YeepayApiUtil.formatString(paramMap.get("userId"));
        String smsCode = YeepayApiUtil.formatString(paramMap.get("smsCode"));

        log.info("YeepayService getRechargeConfirm userId=" + userId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", merchantNo);
        dataMap.put("requestno", requestNo);
        dataMap.put("validatecode", smsCode);

//        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//        dataMap.put("sign", sign);

//        log.info("YeepayService getRechargeConfirm requestUrl=" + requestUrl);
        log.info("YeepayService getRechargeConfirm dataMap=" + JSON.toJSONString(dataMap));

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
        log.info("YeepayService getRechargeConfirm resultMap=" + (resultMap != null ? JSON.toJSONString(resultMap) : "null"));
        if (resultMap == null) {
            newOutOrder.setStatus(OutOrders.STATUS_OTHER);
            outOrdersService.updateByOrderNo(newOutOrder);
            resultModel.setCode(ErrorCode.ERROR_400.getCode());
            resultModel.setMessage(ErrorCode.ERROR_400.getMsg());
            return resultModel;
        }

        log.info("YeepayService getRechargeConfirm userId=" + userId + " errorcode=" + resultMap.get("errorcode") + " errormsg=" + resultMap.get("errormsg"));
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
            log.info("YeepayService getRechargeConfirm userId=" + userId + " status=" + orderStatus);
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
            log.info("YeepayService getRechargeConfirm userId=" + userId + " status=null");
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
        log.info("YeepayService getYBPayResult ypBatchPayResultReq=" + JSON.toJSONString(ypBatchPayResultReq));

        PageResultModel<YPBatchPayResultModel> result = new PageResultModel<>(false);
        //商户私钥
        String hmacKey = PayConstants.HMAC_KEY;
        String requestUrl = PayConstants.BATCH_DETAIL_QUERY_REQURL;

        //【1】将请求的数据和商户自己的密钥拼成一个字符串,获得加密后的请求hmac
        String hmacStr = YeepayUtil.getBatchDetailQueryStr(ypBatchPayResultReq, hmacKey);

        log.info("YeepayService getYBPayResult 签名之前的源数据=" + hmacStr);
        //获得签名
        Map<String, Object> signMap = YeepayUtil.getSign(hmacStr);

        log.info("YeepayService getYBPayResult 经过md5和数字证书签名之后的数据=" + signMap.get("sign").toString());
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
        log.info("YeepayService getYBPayResult 完整xml请求报文==>:" + document.asXML());

        log.info("YeepayService getYBPayResult requestUrl=" + requestUrl);

        String responseMsg = CallbackUtils.httpRequest(requestUrl, document.asXML(), "POST", "gbk", "text/xml ;charset=gbk", false);
        log.info("YeepayService getYBPayResult 服务器响应xml报文:" + responseMsg);


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
        log.info("YeepayService getYBPayResult resultMap=" + JSONObject.toJSONString(responseHeadMap));


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
    public ResultModel<YPRepayResultModel> getYBRepayResult(YPRepayRecordReq ypRepayRecordReq, String userId) {
        log.info("YeepayService getYBRepayResult start");
        log.info("YeepayService getYBRepayResult paramMap=" + JSON.toJSONString(ypRepayRecordReq));
        ResultModel<YPRepayResultModel> resultModel = new ResultModel<>(false);
        //商户私钥
//        String merchantPrivateKey = PayConstants.MERCHANT_PRIVATE_KEY;
//        String requestUrl = ;
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("merchantno", ypRepayRecordReq.getMerchantNo());
        dataMap.put("requestno", ypRepayRecordReq.getRequestNo());
        log.info("YeepayService getYBRepayResult reqData=" + JSON.toJSONString(ypRepayRecordReq));
        //调用易宝api,获取返回map
//        Map<String, Object> resultMap = YeepayApiUtil.httpExecuteResult(dataMap, userId, requestUrl, "getYBRepayResult");
        Map<String, Object> resultMap = new HashMap<>();
        try{
            resultMap = YeepayApiUtil.yeepayYOP(dataMap,PayConstants.REPAY_RECORD_QUERY_REQURL);
        }catch (Exception e){
            log.error("getYBRepayResult error:{}",e);
        }
        if (resultMap == null) {
            log.info("YeepayService getYBRepayResult resultMap= null");
            resultModel.setCode(ErrorCode.ERROR_400.getCode());
            resultModel.setMessage("绑卡失败，请重试");
            return resultModel;
        }
        log.info("YeepayService getYBRepayResult resultMap= " + JSON.toJSONString(resultMap));
        resultModel.setSucc(true);
        resultModel.setCode(resultMap.get("errorcode") == null ? null : String.valueOf(resultMap.get("errorcode")));
        resultModel.setMessage(resultMap.get("errormsg") == null ? null : String.valueOf(resultMap.get("errormsg")));

        YPRepayResultModel repayResultModel = new YPRepayResultModel();
        repayResultModel.setMerchantNo(resultMap.get("merchantno") == null ? null : String.valueOf(resultMap.get("merchantno")));
        repayResultModel.setRequestNo(resultMap.get("requestno") == null ? null : String.valueOf(resultMap.get("requestno")));
        repayResultModel.setYbOrderId(resultMap.get("yborderid") == null ? null : String.valueOf(resultMap.get("yborderid")));
        repayResultModel.setStatus(resultMap.get("status") == null ? null : String.valueOf(resultMap.get("status")));
        repayResultModel.setAmount(resultMap.get("amount") == null ? null : String.valueOf(resultMap.get("amount")));
        repayResultModel.setCardTop(resultMap.get("cardtop") == null ? null : String.valueOf(resultMap.get("cardtop")));
        repayResultModel.setCardLast(resultMap.get("cardlast") == null ? null : String.valueOf(resultMap.get("cardlast")));
        repayResultModel.setBankCode(resultMap.get("bankcode") == null ? null : String.valueOf(resultMap.get("bankcode")));
        repayResultModel.setSign(resultMap.get("sign") == null ? null : String.valueOf(resultMap.get("sign")));
        resultModel.setData(repayResultModel);
        return resultModel;
    }


    @Autowired
    @Override
    public String getHmacKey() {
        //商户私钥
        return Configuration.getInstance().getValue("hmacKey");
    }

//    public static void main(String[] args) {

        //http://116.62.40.106:8080/pay/withdrawCallback

//        String host_api = "http://apitest.vxianjin.com";
//        String user_id = "312";
//        String borrow_id = "7";
//        String uuid = IdGen.uuid();
//        String sign_key = "621c76126e4940e8d7de8b5cce65bf7c";

//        String sign = MD5coding.MD5(AESUtil.encrypt(user_id+borrow_id+uuid,sign_key));
//        String request_url = host_api + "/yeepay/withdraw/" + user_id + "/" + borrow_id + "/" + uuid + "/" + sign;
//        String request_url = host_api + "/yeepayBindCard/credit-card/userUnSendBindBankRequest?isBand=0&userId=10377&status=0&page=0&pageSize=1";
//        String request_url = "http://api.jx-money.com/pay/withdraw/9175/5342/25c1e8ea48274acfb6f5d16254763c11/b9204968f99317fcdf73ef6fb6f05ace";
//        String request_url = "http://apitest.vxianjin.com/yeepay/withdrawCallback";
//        String result_str = HttpUtil.getHttpMess(request_url + "", "<?xml version=\"1.0\" encoding=\"GBK\"?><data><cmd>TransferNotify</cmd><version>1.1</version><mer_Id>10022339124</mer_Id><batch_No>111528355611012</batch_No><order_Id>342MbPZGSY</order_Id><status>S</status><message></message><hmac>MIIE6QYJKoZIhvcNAQcCoIIE2jCCBNYCAQExCzAJBgUrDgMCGgUAMC8GCSqGSIb3DQEHAaAiBCAyYzExZGY5OGY2OWQ0Mzk2YzRjOGZkZjk2MGY2MDU4NKCCA7EwggOtMIIDFqADAgECAhAuyfCgPbMcigfvVyGazeQRMA0GCSqGSIb3DQEBBQUAMCQxCzAJBgNVBAYTAkNOMRUwEwYDVQQKEwxDRkNBIFRFU1QgQ0EwHhcNMTExMTI4MDcwOTUzWhcNMTMxMTI4MDcwOTUzWjBzMQswCQYDVQQGEwJDTjEVMBMGA1UEChMMQ0ZDQSBURVNUIENBMQ8wDQYDVQQLEwZZRUVQQVkxEjAQBgNVBAsTCUN1c3RvbWVyczEoMCYGA1UEAxQfMDQxQFoxMjNxd2VAemhpd2VuLm1laUAwMDAwMDAwMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAzrITNElBaFF7xPXtPguWeTnGOI1gVMMkUDI57ZQz+Gg9PPcfF+ExrtDgMQEOwfRs7X4XiraPE2l6ub0Xkpl0ftu8ELnii91wUKAqsvp88NIdAdLQnC7PeveWlquVSAf//2WtAkdBI7xnhXaL/ObUkhHheT0aR5miYmDyLAkTBj8CAwEAAaOCAY8wggGLMB8GA1UdIwQYMBaAFEZy3CVynwJOVYO1gPkL2+mTs/RFMB0GA1UdDgQWBBS0k6A7ZSwLRwbIhFsgcChrYd27PDALBgNVHQ8EBAMCBaAwDAYDVR0TBAUwAwEBADA7BgNVHSUENDAyBggrBgEFBQcDAQYIKwYBBQUHAwIGCCsGAQUFBwMDBggrBgEFBQcDBAYIKwYBBQUHAwgwgfAGA1UdHwSB6DCB5TBPoE2gS6RJMEcxCzAJBgNVBAYTAkNOMRUwEwYDVQQKEwxDRkNBIFRFU1QgQ0ExDDAKBgNVBAsTA0NSTDETMBEGA1UEAxMKY3JsMTI2XzE5NDCBkaCBjqCBi4aBiGxkYXA6Ly90ZXN0bGRhcC5jZmNhLmNvbS5jbjozODkvQ049Y3JsMTI2XzE5NCxPVT1DUkwsTz1DRkNBIFRFU1QgQ0EsQz1DTj9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Y2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwDQYJKoZIhvcNAQEFBQADgYEAKX4CXCPQEE4RWGsZTXZXLBct2gcPYEjqpgPZ5ERiUrYLTDGuIT90ECfSoxCrcTJEeY7EJBojig9gLRoMn/4xXW/XscGarQ3XxyZw8VxTMFkotuUkAPoaacYlIrc34t2DR0DqvU6umgFL3yTMYxl5WLjOh47OH7Aw7VPscmrtzEIxgd0wgdoCAQEwODAkMQswCQYDVQQGEwJDTjEVMBMGA1UEChMMQ0ZDQSBURVNUIENBAhAuyfCgPbMcigfvVyGazeQRMAkGBSsOAwIaBQAwDQYJKoZIhvcNAQEBBQAEgYCz3axGYIaciQZJZVT0637Dd8wGgA4f1q0I8GrX82g1sJK3JLbmyLboRk40brMO9B/Lykl3jlbTWUYANhkhXv/pU0qUpmanTcwJfmcZSJllWhGloA+Audic4+0j3MWNMRpwFNuR7OppDm4MMDmiL+oA3JG/viowA2OugemDAMyvLQ==</hmac></data>", "POST", "UTF-8");
//        System.out.println(result_str);
//        System.out.println(System.currentTimeMillis());

//    }

}
