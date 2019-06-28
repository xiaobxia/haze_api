package com.vxianjin.gringotts.pay.common.util.chanpay;

import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;

import java.util.HashMap;
import java.util.Map;

public class BaseParameter {

	public final static String CJT_DSF = "cjt_dsf";//代付
	public final static String NMG_BIZ_API_AUTH_REQ = "nmg_biz_api_auth_req";//绑卡请求
	public final static String NMG_API_AUTH_SMS = "nmg_api_auth_sms";//绑卡请求
	public final static String NMG_BIZ_API_QUICK_PAYMENT = "nmg_biz_api_quick_payment";//协议快捷支付
	public final static String NMG_ZFT_API_QUICK_PAYMENT = "nmg_zft_api_quick_payment";//直接支付
	public final static String NMG_API_QUICK_PAYMENT_SMSCONFIRM = "nmg_api_quick_payment_smsconfirm";//直接支付短信确认

	/**
	 * 请求
	* @Title: requestBaseParam 
	* @param @return    设定文件
	* @return Map<String,String>    返回类型 
	* @throws
	 */
	public static Map<String,String> requestBaseParameter(String service){
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put(BaseConstant.SERVICE, service);// 
		origMap.put(BaseConstant.VERSION, "1.0");
		origMap.put(BaseConstant.PARTNER_ID, PropertiesConfigUtil.get("PARTNER_ID")); // 畅捷支付分配的商户号
		origMap.put(BaseConstant.TRADE_DATE, BaseConstant.DATE);
		origMap.put(BaseConstant.TRADE_TIME, BaseConstant.TIME);
		origMap.put(BaseConstant.INPUT_CHARSET, BaseConstant.CHARSET);// 字符集
		origMap.put(BaseConstant.MEMO, "");// 备注
		return origMap;
		
	}
}
