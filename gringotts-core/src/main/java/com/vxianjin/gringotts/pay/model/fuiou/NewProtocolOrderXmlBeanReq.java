package com.vxianjin.gringotts.pay.model.fuiou;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "REQUEST")
public class NewProtocolOrderXmlBeanReq {
	
	@XNode("VERSION")
	private String version;//VERSION
	@XNode("MCHNTCD")
	private String mchntCd;//MCHNTCD
	@XNode("MCHNTORDERID")
	private String mchntOrderId;//MCHNTORDERID
	@XNode("PROTOCOLNO")
	private String protocolNo;//PROTOCOLNO
	@XNode("USERID")
	private String userId;//USERID
	@XNode("AMT")
	private String amt;//AMT
	@XNode("BACKURL")
	private String backUrl;//BACKURL
	@XNode("REM1")
	private String rem1;//REM1
	@XNode("REM2")
	private String rem2;//REM2
	@XNode("REM3")
	private String rem3;//REM3
	@XNode("SIGNTP")
	private String signTp;//SIGNTP
	@XNode("SIGN")
	private String sign;//SIGN
	@XNode("TYPE")
	private String type;//TYPE
	@XNode("CVN")
	private String cvn;//CVN
	@XNode("USERIP")
	private String userIp;//USERIP
	@XNode("ORDERID")
	private String orderId;
	@XNode("ORDERALIVETIME")
	private String orderAliveTime;//ORDERALIVETIME
	@XNode("NEEDSENDMSG")
	private String needSendMsg;
	
	
	
	public String getNeedSendMsg() {
		return needSendMsg;
	}
	public void setNeedSendMsg(String needSendMsg) {
		this.needSendMsg = needSendMsg;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMchntCd() {
		return mchntCd;
	}
	public void setMchntCd(String mchntCd) {
		this.mchntCd = mchntCd;
	}
	public String getMchntOrderId() {
		return mchntOrderId;
	}
	public void setMchntOrderId(String mchntOrderId) {
		this.mchntOrderId = mchntOrderId;
	}
	public String getProtocolNo() {
		return protocolNo;
	}
	public void setProtocolNo(String protocolNo) {
		this.protocolNo = protocolNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getRem1() {
		return rem1;
	}
	public void setRem1(String rem1) {
		this.rem1 = rem1;
	}
	public String getRem2() {
		return rem2;
	}
	public void setRem2(String rem2) {
		this.rem2 = rem2;
	}
	public String getRem3() {
		return rem3;
	}
	public void setRem3(String rem3) {
		this.rem3 = rem3;
	}
	public String getSignTp() {
		return signTp;
	}
	public void setSignTp(String signTp) {
		this.signTp = signTp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCvn() {
		return cvn;
	}
	public void setCvn(String cvn) {
		this.cvn = cvn;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderAliveTime() {
		return orderAliveTime;
	}
	public void setOrderAliveTime(String orderAliveTime) {
		this.orderAliveTime = orderAliveTime;
	}
	
	public String signStr(String key) {
		StringBuffer buffer = new StringBuffer();
				//TYPE+"|"+VERSION+"|"+MCHNTCD+"|"+MCHNTORDERID+"|"+USERID+"|"+PROTOCOLNO+"|"+AMT+"|"+BACKURL+"|"+USERIP+"|"+"商户key"
				buffer.append(type)
				.append("|")
				.append(version)
				.append("|")
				.append(mchntCd)
				.append("|")
				.append(mchntOrderId)
				.append("|")
				.append(userId)
				.append("|")
				.append(protocolNo)
				.append("|")
				.append(amt)
				.append("|")
				.append(backUrl)
				.append("|")
				.append(userIp)
				.append("|")
				.append(key);
				System.out.println("返回信息明文-----"+buffer.toString());
				return buffer.toString();
	}
	
	public String signStrMsgReSend(String key) {
		StringBuffer buffer = new StringBuffer();
				//TYPE+"|"+VERSION+"|"+MCHNTCD+"|"+ORDERID+"|"+USERID+"|"+PROTOCOLNO+"|"+AMT+"|"+BACKURL+"|"+USERIP+"|"+"商户key"
				buffer.append(type)
				.append("|")
				.append(version)
				.append("|")
				.append(mchntCd)
				.append("|")
				.append(orderId)
				.append("|")
				.append(userId)
				.append("|")
				.append(protocolNo)
				.append("|")
				.append(amt)
				.append("|")
				.append(backUrl)
				.append("|")
				.append(userIp)
				.append("|")
				.append(key);
				System.out.println("返回信息明文-----"+buffer.toString());
				return buffer.toString();
	}
	
	public String md5StrPay(String key){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(mchntCd)
		.append("|")
		.append(mchntOrderId)
		.append("|")
		.append(userId)
		.append("|")
		.append(protocolNo)
		.append("|")
		.append(key);
		System.out.println("md5StrPay信息-----"+stringBuffer.toString());
		return stringBuffer.toString();
	}
	

}
