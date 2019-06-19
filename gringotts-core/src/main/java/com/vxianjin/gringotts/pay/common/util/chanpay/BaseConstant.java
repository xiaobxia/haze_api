package com.vxianjin.gringotts.pay.common.util.chanpay;

import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * <p>
 * 定义请求的参数名称
 * </p>
 * 
 * @author yanghta@chenjet.com
 * @version $Id: BaseConstant.java, v 0.1 2017-05-03 下午5:25:44
 */
public class BaseConstant {

	// 基础参数
	public static final String SERVICE = "Service";
	public static final String VERSION = "Version";
	public static final String PARTNER_ID = "PartnerId";
	// 日期
	public static final String TRADE_DATE = "TradeDate";
	public static final String TRADE_TIME = "TradeTime";
	public static final String INPUT_CHARSET = "InputCharset";
	public static final String SIGN = "Sign";
	public static final String SIGN_TYPE = "SignType";
	public static final String MEMO = "Memo";

	public static final String MD5 = "MD5";
	public static final String RSA = "RSA";
	
	
	/**
	 * 畅捷支付平台公钥
	 */
	public static final String MERCHANT_PUBLIC_KEY = PropertiesConfigUtil.get("MERCHANT_PUBLIC_KEY");

	/**
	 * 商户私钥
	 */
	public static final String MERCHANT_PRIVATE_KEY= PropertiesConfigUtil.get("MERCHANT_PRIVATE_KEY");
	
	
	
	/**
	 * 编码类型
	 */
	public static final String CHARSET = "UTF-8";
	public final static String GATEWAY_URL = PropertiesConfigUtil.get("GATEWAY_URL");
	public final static String BATCH_FILE_GATEWAY_URL = PropertiesConfigUtil.get("BATCH_FILE_GATEWAY_URL");
	public static String DATE = new SimpleDateFormat("yyyyMMdd").format(new Date());
	public static String TIME = new SimpleDateFormat("HHmmss").format(new Date());

}
