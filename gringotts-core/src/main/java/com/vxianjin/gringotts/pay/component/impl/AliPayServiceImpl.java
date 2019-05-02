package com.vxianjin.gringotts.pay.component.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tools.mq.producer.CommonProducer;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IAliPayApiService;
import com.vxianjin.gringotts.constant.Commons;
import com.vxianjin.gringotts.pay.common.enums.EventTypeEnum;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.common.publish.PublishAdapter;
import com.vxianjin.gringotts.pay.common.publish.PublishFactory;
import com.vxianjin.gringotts.pay.common.util.PropertiesUtil;
import com.vxianjin.gringotts.pay.component.IAliPayService;
import com.vxianjin.gringotts.pay.model.GeTuiJson;
import com.vxianjin.gringotts.pay.service.RenewalRecordService;
import com.vxianjin.gringotts.pay.service.RepaymentDetailService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.pay.service.base.RepayService;
import com.vxianjin.gringotts.risk.service.IOutOrdersService;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import com.vxianjin.gringotts.web.dao.IOrderInfoDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.service.impl.UserClientInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 阿里app四方支付服务实现类
 */
@Service(value = "alipayService")
public class AliPayServiceImpl implements IAliPayService {

    private final static Logger    logger       = LoggerFactory.getLogger(AliPayServiceImpl.class);

    @Autowired
    private CommonProducer         producer;

    /**
     * 配置文件名
     */
    private static final String    CHANNEL_NAME = AliPayServiceImpl.class.getSimpleName();
    /**
     * 回调URL
     */
    public static final String     KEY          = getProperties("key");
    /**
     * 请求URL
     */
    private static final String    REQ_URL      = getProperties("request_url");

    /**
     * 商户号
     */
    private static final String    MER_ID       = getProperties("merid");

    /**
     * 回调URL
     */
    private static final String    NOTIFY_URL   = getProperties("notify_url");

    /**
     * 查询URL
     */
    private static final String    QUERY_URL    = getProperties("query_url");
    private static final String    API          = getProperties("api");

    /**
     * 编码格式
     */
    private static final String    CHARSET_UTF8 = "UTF-8";

    @Value("#{mqSettings['topic']}")
    private String                 ossMqTopic;

    @Value("#{mqSettings['target']}")
    private String                 ossMqTarget;

    @Autowired
    private UserClientInfoService  userClientInfoService;

    @Resource
    private IOrderInfoDao          orderInfoDao;

    @Autowired
    private RepaymentService       repaymentService;

    @Autowired
    private RepaymentDetailService repaymentDetailService;

    @Autowired
    private IOutOrdersService      outOrdersService;

    @Autowired
    private IUserDao               userDao;

    @Autowired
    private RenewalRecordService   renewalRecordService;

    @Autowired
    private RepayService           repayService;

    @Autowired
    private ApplicationContext     applicationContext;

    /**
     * 获取properties属性值
     *
     * @param key key
     * @return str
     * @see [类、类#方法、类#成员]
     */
    private static String getProperties(String key) {
        return PropertiesUtil.getValueByKey(key,
            PropertiesUtil.getConfigPath("thirdchannel", CHANNEL_NAME));
    }

    /**
     * app还款支付
     *
     * @return str
     * @throws Exception ex
     */
    @Override
    public String payWithhold(HashMap<String, String> params) throws Exception {

        logger.info("AliPayService payWithhold param=" + JSON.toJSONString(params));
        //还款金额，单位：分
        String money = params.get("money");

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> paraMap = new TreeMap<>();

        BigDecimal mey = (new BigDecimal(money.trim())).divide(new BigDecimal("100")).setScale(2);

        String repaymentId = params.get("repaymentId");
        String borrowId = params.get("borrowId");
        //订单请求编号
        String orderNo = params.get("orderNo");
        String userId = params.get("userId");
        String type = params.get("type");

        paraMap.put("merchantOutOrderNo", orderNo);
        paraMap.put("merid", MER_ID);
        paraMap.put("noncestr", "JXAPP_PAY");
        paraMap.put("orderMoney", mey.toString());
        paraMap.put("orderTime", sdf.format(now));
        paraMap.put("notifyUrl", NOTIFY_URL);

        String value = HttpUtil.formatMap(paraMap, false);
        String sign = MD5Util.md5(value + "key=" + KEY);
        String url = URLDecoder.decode(value.concat("sign=" + sign), CHARSET_UTF8);
        String reqUrl = REQ_URL.concat("?" + url + "&id=" + userId);

        // 生成订单
        OutOrders outOrders = getRepayOutOrders(userId, repaymentId, orderNo, reqUrl);
        //生成待还信息记录
        RepaymentDetail detail = getRepaymentDetail(userId, repaymentId, money, orderNo, borrowId);
        // 代付前处理
        repayService.beforeRepayHandler(outOrders, detail);

        OrderInfo order = new OrderInfo();
        order.setOrderId(borrowId);
        order.setMoney(mey);
        order.setRequestData(reqUrl);
        order.setCreateTime(now);
        order.setUserId(userId);
        order.setOrderNo(orderNo);
        order.setOrderType(Integer.valueOf(type.trim()));
        //新增一笔订单
        orderInfoDao.insertSelective(order);
        logger.info("payWithhold userId=" + userId + " reqUrl=" + reqUrl);
        return reqUrl;
    }

    private OutOrders getRepayOutOrders(String userId, String repaymentId, String orderNo,
                                        String reqParams) {
        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(userId);
        outOrders.setAssetOrderId(Integer.parseInt(repaymentId));
        outOrders.setOrderType(OutOrders.TYPE_ALIPAY);
        outOrders.setOrderNo(orderNo);
        outOrders.setAct(OutOrders.ACT_REPAY_DEBIT);
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        outOrders.setReqParams(reqParams);
        return outOrders;
    }

    /**
     * 生成支付宝代付还款详情
     *
     * @param userId      用户id
     * @param repaymentId 还款id
     * @param money       还款金额
     * @param orderNo     订单号
     * @param borrowId    借款编号
     * @return detail
     */
    private RepaymentDetail getRepaymentDetail(String userId, String repaymentId, String money,
                                               String orderNo, String borrowId) {
        RepaymentDetail detail = new RepaymentDetail();
        detail.setUserId(Integer.parseInt(userId));//还款用户ID
        detail.setAssetRepaymentId(Integer.parseInt(repaymentId));//总还款记录ID
        detail.setTrueRepaymentMoney(Long.parseLong(money));//还款金额
        detail.setOrderId(orderNo);//还款编号
        //		detail.setRepaymentType(withholdType);//还款方式
        detail.setRemark("支付宝还款");
        detail.setStatus(RepaymentDetail.STATUS_WAIT);//还款初始状态
        detail.setAssetOrderId(Integer.parseInt(borrowId));//借款记录ID
        detail.setCreatedAt(new Date());//创建时间
        return detail;
    }

    @Override
    public String payRenewal(HashMap<String, String> params) throws Exception {
        logger.info("AliPayService payRenewal param=" + JSON.toJSONString(params));
        //还款金额，单位：分
        String money = params.get("money");
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> paraMap = new TreeMap<>();

        BigDecimal mey = (new BigDecimal(money.trim())).divide(new BigDecimal("100")).setScale(2);

        String repaymentId = params.get("repaymentId");
        String borrowId = params.get("borrowId");
        //订单请求编号
        String orderNo = params.get("orderNo");
        String userId = params.get("userId");
        String type = params.get("type");

        paraMap.put("merchantOutOrderNo", orderNo);
        paraMap.put("merid", MER_ID);
        paraMap.put("noncestr", "JXAPP_PAY");
        paraMap.put("orderMoney", mey.toString());
        paraMap.put("orderTime", sdf.format(now));
        paraMap.put("notifyUrl", NOTIFY_URL);

        String value = HttpUtil.formatMap(paraMap, false);
        String sign = MD5Util.md5(value + "key=" + KEY);
        String url = URLDecoder.decode(value.concat("sign=" + sign), CHARSET_UTF8);
        String reqUrl = REQ_URL.concat("?" + url + "&id=" + userId);

        // 生成订单
        OutOrders outOrders = new OutOrders();
        outOrders.setUserId(userId);
        outOrders.setAssetOrderId(Integer.parseInt(repaymentId));
        outOrders.setOrderType(OutOrders.TYPE_ALIPAY);
        outOrders.setOrderNo(orderNo);
        outOrders.setAct(OutOrders.ACT_RENEWAL);
        outOrders.setStatus(OutOrders.STATUS_WAIT);
        outOrders.setReqParams(reqUrl);
        outOrdersService.insert(outOrders);

        OrderInfo order = new OrderInfo();
        order.setOrderId(borrowId);
        order.setMoney(mey);
        order.setRequestData(reqUrl);
        order.setCreateTime(now);
        order.setUserId(userId);
        order.setOrderNo(orderNo);
        order.setOrderType(Integer.valueOf(type.trim()));
        //新增一笔订单
        orderInfoDao.insertSelective(order);

        logger.info("payRenewal userId=" + userId + " reqUrl=" + reqUrl);
        return reqUrl;
    }

    /**
     * app还款支付回调
     *
     * @return str
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String payNotify(Map<String, String> respMap, String msg) {

        logger.info("payNotify respMap=" + JSON.toJSONString(respMap));

        // 校验此通知是否是本方商户
        if (!MER_ID.equals(respMap.get("merid"))) {
            logger.warn("yizhi send other merchant to us,and refuse");
            return Commons.FAILURE;
        }
        try {
            // 只通知成功
            if ("1".equals(respMap.get("payResult"))) {
                // 通过订单ID找到那笔订单
                //请求订单编号
                String merchantOrd = respMap.get("merchantOutOrderNo");
                String yzOrderId = respMap.get("orderNo");

                HashMap<String, String> params = new HashMap<>();
                params.put("orderNo", merchantOrd);
                OrderInfo order = orderInfoDao.selectByOrdOrYzOrd(params);

                logger
                    .info("payNotify order=" + (order != null ? JSON.toJSONString(order) : "null"));

                if (order != null) {

                    //请求重发判断
                    if ("1".equals(order.getState().toString())) {
                        //该笔订单已是成功状态，属于重发
                        return Commons.SUCCESS;
                    }

                    // 更改订单状态 0未支付 1成功 2失败 3处理中
                    order.setState(1);
                    order.setYzOrderId(yzOrderId);
                    order.setPayTime(new Date());
                    order.setResponseData(msg);
                    String payMoney = JSON.parseObject(respMap.get("msg")).getString("payMoney")
                        .trim();
                    order.setPayMoney(new BigDecimal(payMoney));
                    orderInfoDao.updateByPrimaryKeySelective(order);

                    OutOrders outOrders = outOrdersService.findByOrderNo(order.getOrderNo());
                    outOrders.setNotifyParams(msg);
                    outOrders.setStatus(OutOrders.STATUS_SUC);
                    //orderType 1还款 2续期
                    if ("1".equals(order.getOrderType().toString())) {
                        logger.info("payNotify 还款处理");
                        refundPayNotify(outOrders, payMoney);
                    } else if ("2".equals(order.getOrderType().toString())) {
                        logger.info("payNotify 续期处理");
                        continuationPayNotify(outOrders);
                    }
                    return Commons.SUCCESS;
                }
            }
        } catch (Exception e) {
            logger.error("payNotify error:", e);
        }
        return Commons.FAILURE;
    }

    /**
     * 订单查询
     *
     * @param orderId id
     * @return json
     * @throws Exception ex
     */
    @Override
    public JSONObject queryResult(String orderId) throws Exception {
        logger.info("queryResult orderId=" + orderId);
        HashMap<String, String> params = new HashMap<>();
        params.put("orderNo", orderId);
        OrderInfo order = orderInfoDao.selectByOrdOrYzOrd(params);
        logger.info("queryResult OrderInfo=" + (order != null ? JSON.toJSONString(order) : "null"));
        JSONObject json = null;
        if (order != null && "1".equals(String.valueOf(order.getState()))) {
            json = new JSONObject();
            json.put("success", "success");
        } else {
            Map<String, String> paraMap = new TreeMap<>();
            paraMap.put("merchantOutOrderNo", orderId);
            paraMap.put("merid", MER_ID);
            paraMap.put("noncestr", "JXAPPQUERY");

            String value = HttpUtil.formatMap(paraMap, false);
            String sign = MD5Util.md5(value + "key=" + KEY);
            paraMap.put("sign", sign);
            try {
                IAliPayApiService service = CloseableOkHttp.obtainRemoteService(API,
                    IAliPayApiService.class);
                Response<JSONObject> response = service.queryOrder(paraMap).execute();
                if (response.isSuccessful() && response.body() != null) {
                    json = response.body();
                }
            } catch (Exception e) {
                logger.error("queryResult sendPost error:", e);
            }
        }
        return json;
    }

    /**
     * 查询结果处理
     *
     * @param paraMap map
     * @return jsonObject
     */
    @Override
    public JSONObject queryHandleMth(Map<String, String> paraMap) throws Exception {

        logger.info("queryHandleMth paraMap=" + JSON.toJSONString(paraMap));

        JSONObject json = new JSONObject();

        String payMoney = paraMap.get("orderMoney");
//        String orderId = paraMap.get("orderNo");//第三方支付交易流水

        String yzOrderId = paraMap.get("merchantOutOrderNo");
        String payTime = paraMap.get("payTime");
        String responseData = paraMap.get("responseData");
        String state = paraMap.get("state");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date payDate = sdf.parse(payTime);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("orderNo", yzOrderId);
        //获取订单
        OrderInfo order = orderInfoDao.selectByOrdOrYzOrd(params);
        //避免重复处理订单
        if ("1".equals(order.getState().toString())) {
            json.put("success", "success");
            //该笔订单已是成功状态，属于重发
            return json;
        }

        //获取还款orderNo
        String orderNo = order.getOrderNo();
        Integer orderType = order.getOrderType();

        BigDecimal mye = new BigDecimal(payMoney.trim());

        OutOrders outOrders = outOrdersService.findByOrderNo(orderNo);
        order.setPayMoney(mye);
        order.setState(1);
        order.setYzOrderId(yzOrderId);
        order.setPayTime(payDate);
        order.setResponseData(responseData);
        order.setState(Integer.valueOf(state));
        orderInfoDao.updateByPrimaryKeySelective(order);

        outOrders.setStatus(OutOrders.STATUS_SUC);
        outOrders.setNotifyParams(JSON.toJSONString(paraMap));

        if ("1".equals(orderType.toString())) {
            refundPayNotify(outOrders, mye.multiply(new BigDecimal("100")).toString());
        } else if ("2".equals(orderType.toString())) {
            continuationPayNotify(outOrders);
        }
        json.put("success", "success");
        return json;
    }

    /**
     * 还款详情处理
     *
     * @param outOrders out
     * @param payMoney money
     */
    private void refundPayNotify(OutOrders outOrders, String payMoney) {

        logger.info("refundPayNotify outOrders=" + JSON.toJSONString(outOrders));
        logger.info("refundPayNotify payMoney=" + payMoney);
        //还款处理
        if (outOrders != null) {
            //获取还款记录
            Repayment re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());
            //获取还款详情记录
            RepaymentDetail detail = repaymentDetailService.selectByOrderId(outOrders.getOrderNo());

            User user = userDao.searchByUserid(re.getUserId());
            repayService.repayCallBackHandler(re, detail, outOrders, payMoney, true, "", "支付宝",user);

            //给用户发送通知短信,最好使用阻塞队列
            final String phone = user.getUserPhone();
            final String realName = user.getRealname();
//            final String appCode = PropertiesConfigUtil.get("RISK_BUSINESS");

            String content = "";
            content = "尊敬的" + realName + "：您的" + (payMoney)
                    + "元借款已经还款成功，您的该笔交易将计入您的信用记录，好的记录将有助于提升您的可用额度。";
            String userClientId = userClientInfoService
                .queryClientIdByUserId(re.getUserId());
            String mqMsg = "您好，您的" + payMoney + "元借款已经还款成功，该笔交易将有助于提升您的信用额。";
            logger.info("prepared send mqMsg:" + mqMsg + " userClientId:" + userClientId);

            // 推送消息到mq中,便于运营平台个推
            producer.sendMessage(ossMqTopic, ossMqTarget,
                JSON.toJSONString(new GeTuiJson(2, userClientId, "还款成功", mqMsg, mqMsg)));

            try {
                PublishAdapter publishAdapter = PublishFactory
                    .getPublishAdapter(EventTypeEnum.REPAY.getCode());
                publishAdapter.publishMsg(applicationContext, EventTypeEnum.REPAY.getCode(),
                    content, phone);
            } catch (PayException e) {
                logger.error(
                    MessageFormat.format("用户{0},还款成功提示短信发送失败====>e :{1}", phone, e.getMessage()));
            }
        }
    }

    /**
     * 续期处理
     *
     * @param outOrders orders
     */
    private void continuationPayNotify(OutOrders outOrders) {

        logger.info("continuationPayNotify outOrders=" + JSON.toJSONString(outOrders));

        if (outOrders != null) {
            //续期处理
            RenewalRecord renewalRecord = renewalRecordService
                .getRenewalRecordByOrderId(outOrders.getOrderNo());
            //获取还款记录
            Repayment re = repaymentService.selectByPrimaryKey(outOrders.getAssetOrderId());
            repayService.continuCallBackHandler(outOrders, renewalRecord, re, true, "", "支付宝");
        }
    }

    /**
     * 生成18位订单ID
     * 只允许18位
     *
     * @return
     */
//    private String generateCodeNo(Date now) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        return "JX".concat(sdf.format(now)).concat(AliPayRandomUtil.generateLowerString(2));
//    }

    /**
     * 格式化数据
     *
     * @param paraMap
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    private String formatMap(Map<String, String> paraMap,
                             Boolean encode) throws UnsupportedEncodingException {
        StringBuilder value = new StringBuilder("");

        for (String key : paraMap.keySet()) {
            if (encode) {
                value.append(key + "=" + URLEncoder.encode(paraMap.get(key), CHARSET_UTF8) + "&");
            } else {
                value.append(key + "=" + paraMap.get(key) + "&");
            }
        }
        // String values = value.substring(0, value.length() - 1);
        return value.toString();
    }

    /**
     * 生成多少位字符，不足前面补0
     *
     * @param code
     * @param num
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private String autoGenericCode(String code, int num) throws Exception {
        if (null == code) {
            logger.error("that's need orderid!");
            throw new Exception("orderId must not null");
        }
        String result = "";
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.valueOf(code));
        return result;
    }
}
