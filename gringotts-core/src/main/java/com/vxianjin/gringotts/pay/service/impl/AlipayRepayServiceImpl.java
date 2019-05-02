package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.component.IAliPayService;
import com.vxianjin.gringotts.pay.component.impl.AliPayServiceImpl;
import com.vxianjin.gringotts.pay.model.NeedRenewalInfo;
import com.vxianjin.gringotts.pay.model.NeedRepayInfo;
import com.vxianjin.gringotts.pay.service.AlipayRepayService;
import com.vxianjin.gringotts.pay.service.base.RepayService;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.security.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: chenkai
 * @Date: 2018/7/18 20:37
 * @Description:
 */
@Service
public class AlipayRepayServiceImpl implements AlipayRepayService {
    private Logger         logger = LoggerFactory.getLogger(AlipayRepayServiceImpl.class);

    /**
     * 阿里app支付四方服务
     */
    @Autowired
    private IAliPayService alipayService;

    @Autowired
    private RepayService   repayService;

    /**
     * 支付宝回调处理
     *
     * @param msg 回调请求的参数内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String payAlipayCallback(String msg) {
        logger.info("payAlipayCallback start================>");
        logger.info("AlipayRepayServiceImpl.payAlipayCallback params; 【msg:" + msg + "】");
        String result = "failure";
        try {
            /**
             * type 1 普通还款 2 续期支付
             * 注：只有成功才会回调，可能多次回调
             */
            if (StringUtils.isEmpty(msg)) {
                logger
                    .info("AlipayRepayServiceImpl.payAlipayCallback msg is empty,return failure!");
                return result;
            }
            //验签字符串
            Map<String, String> map = new TreeMap<String, String>();
            //处理返回信息并验签
            if (!handleMsgAndVerify(msg, map)) {
                // 验签失败
                logger.warn(
                    "AlipayRepayServiceImpl.payAlipayCallback aliapp verify sign failure!check it please! params:"
                            + msg);
                return result;
            }
            //服务层不需要sign
            map.remove("sign");
            //服务层调用还款回调
            result = alipayService.payNotify(map, msg);
        } catch (Exception e) {
            logger.error(
                "AlipayRepayServiceImpl.payAlipayCallback has error ", e);
        }
        //业务处理完成为对方返回success字符串
        logger.info("payAlipayCallback end===============> result:"+ result);
        return result;
    }

    /**
     * 处理返回信息并验签
     *
     * @param msg
     * @param map
     * @return
     * @throws Exception
     * @author tgy
     */
    @Override
    public boolean handleMsgAndVerify(String msg, Map<String, String> map) throws Exception {
        //验签字符串
        StringBuilder verifySign = new StringBuilder("");
        String[] msgArr = msg.split("&");
        for (String val : msgArr) {
            // 防止数组越界不使用split
            String key = val.substring(0, val.indexOf("="));
            String value = val.substring(val.indexOf("=") + 1, val.length());
            map.put(key, URLDecoder.decode(value, "UTF-8"));
            if (!"id".equals(key) && !"aliNo".equals(key)) {
                verifySign.append(key + "=" + URLDecoder.decode(value, "UTF-8") + "&");
            }
        }
        String sign = map.get("sign");

        String verify = verifySign.substring(0, verifySign.indexOf("&sign="))
            .concat("&key=" + AliPayServiceImpl.KEY);

        String md5 = MD5Util.string2MD5(verify);
        return sign.equals(md5);
    }

    /**
     * 支付宝主动续期 处理
     *
     * @param id    借款订单号
     * @param money 续期金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseContent payAlipayRenewalRequest(Integer id, Long money) throws Exception {
        logger.debug("payAlipayRenewalRequest start===============>");
        logger.info("payAlipayRenewalRequest params: 【id:" + id + "  money:"+ money + "】");
        ResponseContent result = null;
        // 获取续期相关信息
        NeedRenewalInfo needRenewalInfo = repayService.getNeedRenewalInfo(id);

        if (!needRenewalInfo.getAllCount().equals(money)) {
            return new ResponseContent("-101", "您的费用已更新，请刷新当前页面");
        }

        String orderNo = "22" + GenerateNo.nextOrdId();//续期订单号
        // 续期发送第三方前处理
        repayService.beforeRenewalHandler(needRenewalInfo, orderNo);
        //发送请求
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("orderNo", orderNo);
            params.put("money", String.valueOf(needRenewalInfo.getAllCount()));//
            params.put("repaymentId", String.valueOf(needRenewalInfo.getRepayment().getId()));
            params.put("borrowId", String.valueOf(id));
            params.put("userId", needRenewalInfo.getUser().getId());
            params.put("type", "2");//还款操作
            logger.debug(
                "借款号为：" + id + ",还款金额为：" + money + "的获取支付宝主动续期连接的参数: " + JSON.toJSONString(params));
            //发起主动续期请求，返回请求链接
            String alipayUrl = alipayService.payRenewal(params);
            logger.info("借款号为:" + id + "获取支付宝主动续期连接为：" + alipayUrl);
            result = new ResponseContent("0", alipayUrl);
            result.setExt(new String(orderNo));
        } catch (Exception e) {
            throw e;
        }
        logger.info("payAlipayRenewalRequest end,result: " + JSON.toJSONString(result));
        return result;
    }

    /**
     * 主动请求支付宝还款
     *
     * @param id 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = BizException.class)
    public ResponseContent payAlipayWithholdRequest(Integer id) throws Exception {
        logger.info("payAlipayWithholdRequest start=================>");
        logger.info("payAlipayWithholdRequest params: 【id:" + id + "】");
        ResponseContent result = null;
        NeedRepayInfo needRepayInfo = null;
        try {
            // 获取需要还款的相关信息
            needRepayInfo = repayService.getNeedRepayInfo(id);
            // 占锁
            repayService.addRepaymentLock(needRepayInfo.getRepayment().getId() + "");
            //生成18位请求订单编号，唯一
            String orderNo = "11" + GenerateNo.nextOrdId();
            //构造请求参数
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("orderNo", orderNo);
            params.put("money", String.valueOf(needRepayInfo.getMoney()));
            params.put("repaymentId", String.valueOf(needRepayInfo.getRepayment().getId()));
            params.put("borrowId", String.valueOf(id));
            params.put("userId", needRepayInfo.getUser().getId());
            params.put("type", "1");//还款操作
            logger.debug("借款号为:" + id + "获取主动请求支付宝还款链接组装参数为：" + JSON.toJSONString(params));
            //获取支付宝跳转链接
            String alipayUrl = alipayService.payWithhold(params);
            logger.info("借款号为:" + id + "获取主动请求支付宝还款链接为：" + alipayUrl);
            result = new ResponseContent("0", alipayUrl);
            result.setExt(orderNo);
        } catch (BizException e) {
            result = new ResponseContent(e.getErrorCode(), e.getErrorMsg());
        } catch (Exception e) {
            logger.error("payAlipayWithholdRequest has error, params: 【id:"+ id + "】,error:",
                e);
            throw e;
        }
        logger.info("payAlipayWithholdRequest end=============> params: 【id:" + id + "】,result:" + result);
        return result;
    }

    /**
     * 支付宝app支付查询订单支付情况
     *
     * @param orderId 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseContent queryalipayRequest(String orderId) throws Exception {
        logger.info("queryalipayRequest start===============>");
        logger.info("queryalipayRequest params: 【orderId" + orderId + "】");
        JSONObject json = alipayService.queryResult(orderId);

        logger.info("queryalipayRequest json=" + (json != null ? JSON.toJSONString(json) : "null"));
        ResponseContent jsonResult = new ResponseContent("-101", "查询失败");
        try {
            if (json != null && !json.isEmpty() && json.containsKey("success")) {
                return new ResponseContent("0", "支付成功");
            }
            //本笔订单成功
            if (json != null && !json.isEmpty() && "1".equals(json.getString("payResult"))) {
                String orderMoney = json.getString("orderMoney");
                String orderNo = json.getString("orderNo");//第三方交易流水号
                String merchantOutOrderNo = json.getString("merchantOutOrderNo");
                String payTime = json.getString("payTime");
                String state = json.getString("payResult");
                Map<String, String> paraMap = new HashMap<String, String>();
                // 组装内容
                paraMap.put("orderMoney", orderMoney);
                paraMap.put("orderNo", orderNo);
                paraMap.put("merchantOutOrderNo", merchantOutOrderNo);
                paraMap.put("payTime", payTime);
                paraMap.put("responseData", json.toJSONString());
                paraMap.put("state", StringUtils.isEmpty(state) ? "0" : state);
                JSONObject resultJson = alipayService.queryHandleMth(paraMap);
                jsonResult.setCode("0");
                jsonResult.setMsg("支付成功");
            } else {//其他为失败或未支付,默认未支付,无需处理
                jsonResult = new ResponseContent("-101", "未查到支付结果，如有疑问，请联系在线客服");
            }
        } catch (Exception e) {
            logger.info("queryalipayRequest has error,params: 【orderId" + orderId
                        + "】,error:",
                e);
        }
        logger.info("queryalipayRequest end ==============>params: 【orderId" + orderId+ "】,result:" + jsonResult);
        return jsonResult;
    }
}
