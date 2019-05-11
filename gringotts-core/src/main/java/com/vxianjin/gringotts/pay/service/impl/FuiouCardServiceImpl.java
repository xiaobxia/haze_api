package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.QbmBankEnums;
import com.vxianjin.gringotts.pay.common.enums.ErrorCode;
import com.vxianjin.gringotts.pay.component.FuiouService;
import com.vxianjin.gringotts.pay.component.YeepayService;
import com.vxianjin.gringotts.pay.model.ResultModel;
import com.vxianjin.gringotts.pay.model.YPBindCardConfirmReq;
import com.vxianjin.gringotts.pay.service.FuiouCardService;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.web.pojo.OutOrders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : chenkai
 * @date : 2018/7/18 18:29
 */
@Service
public class FuiouCardServiceImpl implements FuiouCardService {

    private final static Logger logger = LoggerFactory.getLogger(FuiouCardServiceImpl.class);

    @Resource
    private IOutOrdersService   outOrdersService;

    @Resource
    private FuiouService        fuiouService;

    @Resource
    private JedisCluster        jedisCluster;
    /**
     * 当天失败认证次数
     */
    private final static String YEEPAY_BIND_FAIL_COUNT = "YEEPAY_BIND_FAIL_COUNT_";

    @Override
    public ResultModel<String> userBankConfirm(YPBindCardConfirmReq bindCardConfirmReq) {

        ResultModel resultModel = new ResultModel(false);
        String userId = bindCardConfirmReq.getUserId();
        try {

            //【1】init订单记录
//            Date date = new Date();
            OutOrders outOrders = new OutOrders();
            outOrders.setUserId(bindCardConfirmReq.getUserId());
            outOrders.setOrderType("YEEPAY");
            outOrders.setOrderNo(GenerateNo.nextOrdId());
            outOrders.setAct("BINDCARD_CONFIRM");
            outOrders.setReqParams(JSON.toJSONString(bindCardConfirmReq.getDataMap()));
            outOrders.setStatus(OutOrders.STATUS_WAIT);

            outOrdersService.insert(outOrders);

            //【2】发送银行卡绑卡确认
            ResultModel<Map<String, Object>> result = fuiouService.getBindCardConfirm(bindCardConfirmReq);

            Map<String, Object> resultMap = result.getData();
            logger.info("YeepayBindCardController userBankConfirm userId=" + userId + " resultMap="
                        + JSON.toJSONString(resultMap));

            OutOrders newOutOrder = new OutOrders();
            //当前日期
            String currentDate = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
            //认证失败次数
            String failCount = jedisCluster.get(YEEPAY_BIND_FAIL_COUNT + currentDate + "_" + userId);
            //失败次数超过3次，则次日再请求
            if (failCount != null && Integer.parseInt(failCount) >= 3) {
                resultModel.setCode(ResponseStatus.FAILD.getName());
                resultModel.setMessage("今日认证失败次数已超过3次，请明日再试");
                return resultModel;
            }

            if (result.isSucc()) {
                newOutOrder.setReturnParams(JSON.toJSONString(result.getData()));
                newOutOrder.setStatus(OutOrders.STATUS_SUC);
                outOrdersService.updateByOrderNo(newOutOrder);

                Map<String, String> infoMap = new HashMap<>();
                if (result.getData().get("agreeno") != null) {
                    infoMap.put("agreeno", result.getData().get("agreeno").toString());
                }
                infoMap.put("user_id", bindCardConfirmReq.getUserId());
                infoMap.put("card_no", bindCardConfirmReq.getCardNo());
                infoMap.put("phone", bindCardConfirmReq.getPhone());
                infoMap.put("real_name", bindCardConfirmReq.getUserName());
                infoMap.put("request_no", bindCardConfirmReq.getRequestNo());

                infoMap.put("bank_id", bindCardConfirmReq.getBankId());
                infoMap.put("open_bank", "");

                //【3】更新数据库中的银行卡信息
                ResultModel updateResult = fuiouService.updateUserBankInfo(infoMap);

                resultModel.setSucc(updateResult.isSucc());
                resultModel.setCode(updateResult.getCode());
                resultModel.setMessage(updateResult.getMessage());
                resultModel.setData(bindCardConfirmReq.getRequestNo());
            } else {
                newOutOrder.setOrderNo(bindCardConfirmReq.getOrderNo());
                newOutOrder.setStatus(OutOrders.STATUS_OTHER);
                outOrdersService.updateByOrderNo(newOutOrder);
                //失败次数加1
                int count = failCount != null ? Integer.parseInt(failCount) + 1 : 1;

                jedisCluster.setex(YEEPAY_BIND_FAIL_COUNT + currentDate + "_" + userId,
                    60 * 60 * 24, count + "");

                resultModel.setCode(ResponseStatus.FAILD.getName());
                if (count == 3) {
                    resultModel.setMessage(result.getMessage() + "（请明日再试）");
                } else {
                    resultModel.setMessage(
                        result.getMessage() + "（今日剩余可认证次数" + (3 - count) + "次）");
                }
            }
            return resultModel;
        } catch (Exception e) {
            logger.error("YeepayBindCardController userBankConfirm userId=" + userId + " error", e);
            resultModel.setSucc(false);
            resultModel.setMessage(ErrorCode.ERROR_500.getCode());
            resultModel.setMessage(ErrorCode.ERROR_500.getMsg());
            return resultModel;
        } finally {
            logger.info("YeepayBindCardController userBankConfirm userId=" + userId + " message="
                        + resultModel.getMessage());
        }
    }
}
