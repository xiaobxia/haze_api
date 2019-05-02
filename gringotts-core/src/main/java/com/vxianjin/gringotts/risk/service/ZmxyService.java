package com.vxianjin.gringotts.risk.service;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.risk.pojo.RiskOrders;
import com.vxianjin.gringotts.risk.utils.ConstantRisk;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 类描述：芝麻信用相关接口 <br>
 * 每次发出请求之前插入订单信息，返回后更新订单信息<br>
 * 此类不做任何业务处理，仅拼接参数请求第三方，必须使用trycath，并且不向上抛出异常以保证插入或更新的订单不会回滚<br>
 * 创建人：wison<br>
 * 创建时间：2017-10-10 上午11:08:28 <br>
 */
@Service
public class ZmxyService implements IZmxyService {
    private static final Integer timeOut = 10000;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IRiskOrdersService riskOrdersService;

    public static byte[] toByteArray3(String filename) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String sendPost(boolean isSsl, String param, String url) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        URL targetUrl = null;
        StringBuffer result = new StringBuffer();

        try {
            if (isSsl) {
                // 信任所有证书
                targetUrl = new URL(url);
                //   SslUtils.ignoreSsl();
            } else {
                targetUrl = new URL(url);
            }

            // 打开和URL之间的连接
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            // 设置超时
            httpConnection.setConnectTimeout(timeOut);
            httpConnection.setReadTimeout(timeOut);
            // 设置通用的请求属性
            httpConnection.setRequestProperty("connection", "Keep-Alive");
            httpConnection.setRequestProperty("Charset", "UTF-8");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            // 发送POST请求
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            out = new PrintWriter(httpConnection.getOutputStream());
            out.print(param);
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        }
        //使用finally块来关闭输出流、输入流
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    @Override
    public ResponseContent getOpenId(HashMap<String, Object> params) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        try {
            String userName = (String) params.get("userName");//用户真实姓名
            String cardNum = (String) params.get("cardNum");//用户身份证号
            Object userId = params.get("userId");//用户ID
            if (userName != null && cardNum != null && userId != null) {
                Map<String, String> keys = SysCacheUtils
                        .getConfigParams(BackConfigParams.ZMXY);
                //TODO wison 白骑士-芝麻信用查询接口相关配置
                String zmUrl = "https://api.baiqishi.com/credit/zhima/search";

                JSONObject req = new JSONObject();
                req.put("partnerId", "luanniao"); //luanniao
                req.put("verifyKey", "e8561377a71b40d0bf64c932b1c13d94"); //e8561377a71b40d0bf64c932b1c13d94
                req.put("linkedMerchantId", "2088821306257161"); //2088821306257161
                req.put("productId", "102002");

                JSONObject extParam = new JSONObject();
                extParam.put("identityType", "2");
                extParam.put("name", userName);
                extParam.put("certNo", cardNum);

                req.put("extParam", extParam);

                RiskOrders orders = new RiskOrders();
                orders.setUserId(userId + "");
                orders.setOrderType(ConstantRisk.ZMXY);
                orders.setAct(ConstantRisk.GET_OPENID);
                orders.setOrderNo(UUID.randomUUID().toString());
                orders.setReqParams(req.toJSONString());
                logger.info("send to get request=" + req.toJSONString());
                riskOrdersService.insert(orders);
                String resp = sendPost(false, req.toJSONString(), zmUrl);
                logger.info("get openId response=" + resp);

                JSONObject respJson = JSONObject.parseObject(resp);
                JSONObject respBody = JSONObject.parseObject(respJson.getString("resultData"));
                Boolean isSuccess = respBody.getBoolean("success");
                if (isSuccess) {
                    Boolean isAuthorized = respBody.getBoolean("authorized");
                    if (isAuthorized) {//已授权
                        serviceResult = new ResponseContent(
                                ResponseContent.SUCCESS, respBody.getString("openId"));
                        orders.setStatus(RiskOrders.STATUS_SUC);
                    } else {//未授权
                        serviceResult = new ResponseContent("100",
                                "用户未授权，请发起授权请求！");
                    }

                } else {
                    serviceResult = new ResponseContent("300", respBody.getString("errorMessage"));
                }

                orders.setReturnParams(req.toJSONString());
                riskOrdersService.update(orders);
            } else {
                serviceResult = new ResponseContent("400", "必要参数不足！");
            }
        } catch (Exception e) {
            logger.error("getOpenId error ,params=" + params, e);
        }
        return serviceResult;
    }

    @Override
    public ResponseContent getURL(HashMap<String, Object> params,
                                  HttpServletRequest request) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        try {

            Object userName = params.get("userName");
            Object cardNum = params.get("cardNum");
            Object userId = params.get("userId");
            if (userName != null && cardNum != null && userId != null) {
                Map<String, String> keys = SysCacheUtils
                        .getConfigParams(BackConfigParams.ZMXY);

                //TODO wison 白骑士-芝麻信用授权接口相关配置
                String zmUrl = "https://api.baiqishi.com/credit/zhima/search";
                String callbackUrl = PropertiesConfigUtil.get("APP_HOST_API") + "/zhima/callBack/" + userId;

                JSONObject req = new JSONObject();
                req.put("partnerId", "luanniao");
                req.put("verifyKey", "e8561377a71b40d0bf64c932b1c13d94");
                req.put("linkedMerchantId", "2088821306257161");
                req.put("productId", "102001");


                JSONObject extParam = new JSONObject();
                extParam.put("identityType", "2");
                extParam.put("name", userName);
                extParam.put("certNo", cardNum);
                extParam.put("channel", "app");
                extParam.put("callbackUrl", callbackUrl);

                req.put("extParam", extParam);


                RiskOrders orders = new RiskOrders();
                orders.setUserId(userId + "");
                orders.setOrderType(ConstantRisk.ZMXY);
                orders.setAct(ConstantRisk.GET_URL);
                orders.setOrderNo(UUID.randomUUID().toString());
                orders.setReqParams(req.toJSONString());
                logger.info("getAuthorizeUrl request=" + req.toJSONString());
                riskOrdersService.insert(orders);
                String resp = sendPost(false, req.toJSONString(), zmUrl);
                logger.info("getAuthorizeUrl response=" + resp);

                JSONObject respBody = JSONObject.parseObject(JSONObject.parseObject(resp).getString("resultData"));

                String url = respBody.getString("authInfoUrl");

                logger.info("getAuthorizeUrl url=" + url);
                orders.setStatus(RiskOrders.STATUS_SUC);
                orders.setReturnParams(url);
                riskOrdersService.update(orders);
                serviceResult = new ResponseContent(ResponseContent.SUCCESS, url);
            } else {
                serviceResult = new ResponseContent("400", "必要参数不足！");
            }

        } catch (Exception e) {
            logger.error("getOpenId error ,params=" + params, e);
        }
        return serviceResult;
    }

    @Override
    public ResponseContent getZmScore(HashMap<String, Object> params) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");

        try {
            Object userId = params.get("userId");
            Object openId = params.get("openId");
            if (userId != null && openId != null) {
                Map<String, String> keys = SysCacheUtils
                        .getConfigParams(BackConfigParams.ZMXY);
                //TODO wison 白骑士-芝麻信用评分查询接口相关配置
                String zmUrl = "https://api.baiqishi.com/credit/zhima/search";

                JSONObject req = new JSONObject();
                req.put("partnerId", "luanniao");
                req.put("verifyKey", "e8561377a71b40d0bf64c932b1c13d94");
                req.put("linkedMerchantId", "2088821306257161");
                req.put("productId", "102004");

                JSONObject extParam = new JSONObject();
                extParam.put("openId", openId);
                req.put("extParam", extParam);

                RiskOrders orders = new RiskOrders();
                orders.setUserId(userId + "");
                orders.setOrderType(ConstantRisk.ZMXY);
                orders.setAct(ConstantRisk.GET_SCORE);
                orders.setOrderNo(UUID.randomUUID().toString());
                orders.setReqParams(req.toJSONString());
                logger.info("getZmScore request = " + req.toJSONString());
                riskOrdersService.insert(orders);
                String resp = sendPost(false, req.toJSONString(), zmUrl);
                logger.info("getZmScore response =" + resp);

                JSONObject respJson = JSONObject.parseObject(resp);
                JSONObject respBody = JSONObject.parseObject(respJson.getString("resultData"));
                Boolean isSuccess = respBody.getBoolean("success");

                if (isSuccess) {
                    serviceResult = new ResponseContent(ResponseContent.SUCCESS,
                            respBody.getString("zmScore"));
                    orders.setStatus(RiskOrders.STATUS_SUC);
                } else {
                    serviceResult = new ResponseContent("300", respBody
                            .getString("errorMessage"));
                }
                orders.setReturnParams(resp);
                riskOrdersService.update(orders);
            } else {
                serviceResult = new ResponseContent("400", "必要参数不足！");
            }
        } catch (Exception e) {
            logger.error("getZmScore error ,params=" + params, e);
        }


        return serviceResult;
    }

    @Override
    public ResponseContent getCreditWatchList(HashMap<String, Object> params) {
        ResponseContent serviceResult = new ResponseContent("500", "未知异常，请稍后重试！");
        try {
            Object userId = params.get("userId");
            Object openId = params.get("openId");
            if (userId != null && openId != null) {
                Map<String, String> keys = SysCacheUtils
                        .getConfigParams(BackConfigParams.ZMXY);
                //TODO wison 白骑士-芝麻信用行业关注名单查询接口相关配置
                String zmUrl = "https://api.baiqishi.com/credit/zhima/search";

                JSONObject req = new JSONObject();
                req.put("partnerId", "luanniao");
                req.put("verifyKey", "e8561377a71b40d0bf64c932b1c13d94");
                req.put("linkedMerchantId", "2088821306257161");
                req.put("productId", "102006");

                JSONObject extParam = new JSONObject();
                extParam.put("openId", openId);
                req.put("extParam", extParam);

                RiskOrders orders = new RiskOrders();
                orders.setUserId(userId + "");
                orders.setOrderType(ConstantRisk.ZMXY);
                orders.setAct(ConstantRisk.GET_INDUSTY);
                orders.setOrderNo(UUID.randomUUID().toString());
                orders.setReqParams(req.toJSONString());
                logger.info("getCreditWatchList request = " + req.toJSONString());
                riskOrdersService.insert(orders);
                String resp = sendPost(false, req.toJSONString(), zmUrl);
                logger.info("getCreditWatchList response =" + resp);

                JSONObject respJson = JSONObject.parseObject(resp);
                JSONObject respBody = JSONObject.parseObject(respJson.getString("resultData"));
                Boolean isSuccess = respBody.getBoolean("success");

                if (isSuccess) {
                    serviceResult = new ResponseContent(ResponseContent.SUCCESS,
                            respBody.toJSONString());
                    orders.setStatus(RiskOrders.STATUS_SUC);
                } else {
                    serviceResult = new ResponseContent("300", respBody
                            .getString("errorMessage"));
                }
                orders.setReturnParams(resp);
                riskOrdersService.update(orders);
            } else {
                serviceResult = new ResponseContent("400", "必要参数不足！");
            }
        } catch (Exception e) {
            logger.error("getCreditWatchList error ,params=" + params, e);
        }


        return serviceResult;
    }

}
