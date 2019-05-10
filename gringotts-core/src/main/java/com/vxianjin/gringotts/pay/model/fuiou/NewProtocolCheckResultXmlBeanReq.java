package com.vxianjin.gringotts.pay.model.fuiou;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "REQUEST")
public class NewProtocolCheckResultXmlBeanReq {
	
	@XNode("VERSION")
	private String version;//VERSION
	@XNode("MCHNTCD")
	private String mchntCd;//MCHNTCD
	@XNode("MCHNTORDERID")
	private String mchntOrderId;//MCHNTORDERID
	@XNode("REM1")
	private String rem1;//REM1
	@XNode("REM2")
	private String rem2;//REM2
	@XNode("REM3")
	private String rem3;//REM3
	@XNode("SIGN")
	private String sign;//SIGN

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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String signStr(String key) {
		StringBuffer buffer = new StringBuffer();
				buffer.append(version)
				.append("|")
				.append(mchntCd)
				.append("|")
				.append(mchntOrderId)
				.append("|")
				.append(key);
				System.out.println("返回信息明文-----"+buffer.toString());
				return buffer.toString();
	}

}
