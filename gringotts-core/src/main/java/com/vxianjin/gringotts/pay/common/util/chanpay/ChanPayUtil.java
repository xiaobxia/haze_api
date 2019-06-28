package com.vxianjin.gringotts.pay.common.util.chanpay;

import com.vxianjin.gringotts.pay.common.enums.ErrorCode;
import com.vxianjin.gringotts.pay.common.exception.PayException;
import com.vxianjin.gringotts.pay.model.chanpay.TradeNotify;
import com.vxianjin.gringotts.web.utils.GsonUtil;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


public class ChanPayUtil {

	private static final Logger logger = LoggerFactory.getLogger(ChanPayUtil.class);

	public final static List<String> SUCCESSCODE = new ArrayList(){{
		add("QT000000");
		add("QT000001");
		add("QT100000");
	}};
	
	/**
	 * 向测试服务器发送post请求
	 * 
	 * @param origMap
	 *            参数map
	 * @param charset
	 *            编码字符集
	 * @param MERCHANT_PRIVATE_KEY
	 *            私钥
	 */
	public static String sendPost(Map<String, String> origMap, String charset,
			String MERCHANT_PRIVATE_KEY) {
		String result = null;
		try {
			Map<String, String> sPara = ChanPayUtil.buildRequestPara(origMap,
					"RSA", MERCHANT_PRIVATE_KEY, charset);
			result = ChanPayUtil.buildRequest(sPara, "RSA",
					MERCHANT_PRIVATE_KEY, charset, BaseConstant.GATEWAY_URL);
		} catch (Exception e) {
			System.out.println("发demo出现异常----"+e);
			e.printStackTrace();
		}
		return result;
	}
	
	public static void sendFilePost(Map<String, String> origMap,
			String charset, String MERCHANT_PRIVATE_KEY,
			String strParaFileName, String strFilePath) {
		try {
			
			Map<String, String> sPara = ChanPayUtil.buildRequestPara(origMap,
					"RSA", MERCHANT_PRIVATE_KEY, charset);
			String resultString = ChanPayUtil.buildFileRequest(sPara, "RSA",
					MERCHANT_PRIVATE_KEY, charset,
					BaseConstant.BATCH_FILE_GATEWAY_URL, strParaFileName,
					strFilePath);
			System.out.println(resultString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密，部分接口，有参数需要加密
	 * 
	 * @param src
	 *            原值
	 * @param publicKey
	 *            畅捷支付发送的平台公钥
	 * @param charset
	 *            UTF-8
	 * @return RSA加密后的密文
	 */
	public static String encrypt(String src, String publicKey, String charset) throws Exception{
		byte[] bytes = RSA.encryptByPublicKey(src.getBytes(charset),
				publicKey);
		return Base64.encodeBase64String(bytes);
	}

	public static String createLinkString(Map<String, String> params,
			boolean encode) {

		params = paraFilter(params);

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		String charset = params.get(BaseConstant.INPUT_CHARSET);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (encode) {
				try {
					value = URLEncoder.encode(value, charset);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	public static String lingString(Map<String, String> params, boolean encode) {

		params = paraFilter(params);

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		String charset = params.get(BaseConstant.INPUT_CHARSET);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (encode) {
				try {
					value = URLEncoder.encode(value, charset);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}
	
	public static String generateOutTradeNo() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ String.valueOf(new Double(
						Math.round(Math.random() * 1000000000)).longValue());
	}

	/**
	 * 建立请求，以模拟远程HTTP的POST请求方式构造并获取钱包的处理结果
	 * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("",
	 * "",sParaTemp)
	 *
	 * @param signType
	 *            文件类型的参数名
	 * @param gatewayUrl
	 *            文件路径
	 * @param sParaTemp
	 *            请求参数数组
	 * @return 钱包处理结果
	 * @throws Exception
	 */
	public static String buildRequest(Map<String, String> sParaTemp,
			String signType, String key, String inputCharset, String gatewayUrl)
					throws Exception {
		
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler
				.getInstance();
		
		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(inputCharset);
		
		request.setMethod(HttpRequest.METHOD_POST);
		
		request.setParameters(generatNameValuePair(sParaTemp, inputCharset));
		request.setUrl(gatewayUrl);
		
		HttpResponse response = httpProtocolHandler
				.execute(request, null, null);
		if (response == null) {
			System.out.println("收到返回信息为null");
			return null;
		}
		
		String strResult = response.getStringResult();
		//下载对账文件
		byte[] byteResult = response.getByteResult();
		Header[] responseHeaders = response.getResponseHeaders();
		String fileName = "";
		for(Header header : responseHeaders){
			if("content-disposition".equals(header.getName())){
				System.out.println(header.getValue());
				fileName = header.getValue();
			}
		}
		if(!"".equals(fileName)){
			File file  = new File("C:/"+fileName.substring(20));
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(byteResult);
		}
			
		return strResult;
	}
	
	public static String buildFileRequest(Map<String, String> sParaTemp,
			String signType, String key, String inputCharset,
			String gatewayUrl, String strParaFileName, String strFilePath)
			throws Exception {

		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler
				.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(inputCharset);

		request.setMethod(HttpRequest.METHOD_POST);

		request.setParameters(generatNameValuePair(sParaTemp, inputCharset));
		request.setUrl(gatewayUrl);

		HttpResponse response = httpProtocolHandler.execute(request,
				strParaFileName, strFilePath);
		if (response == null) {
			return null;
		}

		String strResult = response.getStringResult();

		return strResult;
	}

	/**
	 * 除去数组中的空值和签名参数
	 *
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("")
					|| key.equalsIgnoreCase(BaseConstant.SIGN)
					|| key.equalsIgnoreCase(BaseConstant.SIGN_TYPE)) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 生成要请求给钱包的参数数组
	 *
	 * @param sParaTemp
	 *            请求前的参数数组
	 * @return 要请求的参数数组
	 */
	public static Map<String, String> buildRequestPara(
			Map<String, String> sParaTemp, String signType, String key,
			String inputCharset) throws Exception {
		// 除去数组中的空值和签名参数
		Map<String, String> sPara = paraFilter(sParaTemp);
		// 生成签名结果
		String mysign = "";
		if (BaseConstant.MD5.equalsIgnoreCase(signType)) {
			mysign = buildRequestByMD5(sPara, key, inputCharset);
		} else if (BaseConstant.RSA.equalsIgnoreCase(signType)) {
			mysign = buildRequestByRSA(sPara, key, inputCharset);
		}

		// 签名结果与签名方式加入请求提交参数组中
		sPara.put(BaseConstant.SIGN, mysign);
		sPara.put(BaseConstant.SIGN_TYPE, signType);

		return sPara;
	}

	/**
	 * 生成MD5签名结果
	 *
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildRequestByMD5(Map<String, String> sPara,
			String key, String inputCharset) throws Exception {
		String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = MD5.sign(prestr, key, inputCharset);
		return mysign;
	}

	/**
	 * 生成RSA签名结果
	 *
	 * @param sPara
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildRequestByRSA(Map<String, String> sPara,
			String privateKey, String inputCharset) throws Exception {
		String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String mysign = RSA.sign(prestr, privateKey, inputCharset);
		System.out.println("demo待签名内容：" + prestr);
		System.out.println("demo签名：" + mysign);
		return mysign;
	}

	/**
	 * MAP类型数组转换成NameValuePair类型
	 *
	 * @param properties
	 *            MAP类型数组
	 * @return NameValuePair类型数组
	 */
	private static NameValuePair[] generatNameValuePair(
			Map<String, String> properties, String charset) throws Exception {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(),
					URLEncoder.encode(entry.getValue(), charset));
			// nameValuePair[i++] = new NameValuePair(entry.getKey(),
			// entry.getValue());
		}

		return nameValuePair;
	}

	/**
	 * 验签
	 *
	 * @param out       返回报文
	 * @param publicKey 公钥
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String out, String publicKey) {
		Map paramMap = GsonUtil.fromJson(out, Map.class);
		return verify(paramMap, publicKey);
	}

	public static void main(String[] args) {
		TradeNotify tradeNotify = new TradeNotify();
		tradeNotify.setExtension("{\"bankCode\":\"00000\",\"bankMessage\":\"支付成功\",\"unityResultCode\":\"S0001\",\"channelTransTime\":\"20190628\",\"payerId\":\"haze204231\",\"apiResultcode\":\"D\",\"paymentSeqNo\":\"20190628FI178006732\",\"sendBankReq\":\"\",\"apiResultMsg\":\"交易成功\",\"unityResultMessage\":\"交易成功\",\"wjReq\":\"201906282098864161\",\"chnlNo\":\"0400100029\"}");
		tradeNotify.setTrade_amount("0.01");
		tradeNotify.setGmt_create("20190628151634");
		tradeNotify.setGmt_payment("20190628151634");
		tradeNotify.setInner_trade_no("101156170619070551747");
		tradeNotify.setNotify_id("e3a5f2624cf24af8971233356148b25a");
		tradeNotify.setNotify_time("20190628152953");
		tradeNotify.setNotify_type("trade_status_sync");
		tradeNotify.setOuter_trade_no("1015617061951917");
		tradeNotify.setSign("CJsLT35e21yLb61BeiOGBOXdH2uxTRilYaWUAwDjN+Os8QIIUQsFmmJMdY3NZPJZuH/fva41uAo3Jt+JHrSlHhbxyXpvy9pt0jTvLH5+euDVSpqd12/KTa0yFDXNTK2P4M564tljtNUhTQdsRV+LKIz32EGKH7RtJGnAxv/d33A=");
		tradeNotify.setSign_type("RSA");
		tradeNotify.set_input_charset("UTF-8");
		tradeNotify.setTrade_status("TRADE_SUCCESS");
		tradeNotify.setVersion("1.0");
		boolean verify = verify(GsonUtil.toJson(tradeNotify), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPq3oXX5aFeBQGf3Ag/86zNu0VICXmkof85r+DDL46w3vHcTnkEWVbp9DaDurcF7DMctzJngO0u9OG1cb4mn+Pn/uNC1fp7S4JH4xtwST6jFgHtXcTG9uewWFYWKw/8b3zf4fXyRuI/2ekeLSstftqnMQdenVP7XCxMuEnnmM1RwIDAQAB");
		System.out.println(verify);
	}

	/**
	 * 验签
	 *
	 * @param paramMap  返回报文
	 * @param publicKey 公钥
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(Map paramMap, String publicKey) {
		try {
			if (null != paramMap.get("sign")) {
				String sign = (String) paramMap.get("sign");
				paramMap.remove("sign");
				paramMap.remove("sign_type");
				if (null != paramMap.get("extension")) {
					if (!paramMap.get("extension").getClass().getName().equals("java.lang.String")) {
						paramMap.put("extension", GsonUtil.toJson(paramMap.get("extension")));
					}
				}
				String text = createLinkString(paramMap, false);
				System.out.println(text);
				boolean verify = RSA.verify(text, sign, publicKey, "UTF-8");
				return verify;
			} else {
				String sign = (String) paramMap.get("Sign");
				paramMap.remove("Sign");
				paramMap.remove("SignType");
				if (null != paramMap.get("Extension")) {
					if (!paramMap.get("Extension").getClass().getName().equals("java.lang.String")) {
						paramMap.put("Extension", GsonUtil.toJson(paramMap.get("Extension")));
					}
				}
				String text = createLinkString(paramMap, false);
				boolean verify = RSA.verify(text, sign, publicKey, "UTF-8");
				return verify;
			}

		} catch (Exception e) {
			throw new PayException(ErrorCode.ERROR_500.getMsg());
		}
	}
}
