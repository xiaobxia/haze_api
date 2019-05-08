package com.vxianjin.gringotts.pay.model.fuiou;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "payforreq")
public class PayforreqXmlBeanReq {
	
	@XNode("ver")
	private String ver;
	@XNode("merdt")
	private String merdt;
	@XNode("orderno")
	private String orderNo;
	@XNode("accntno")
	private String accntNo;
	@XNode("accntnm")
	private String accnTnm;
	@XNode("amt")
	private String amt;
	@XNode("addDesc")
	private String addDesc;

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getMerdt() {
		return merdt;
	}

	public void setMerdt(String merdt) {
		this.merdt = merdt;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAccntNo() {
		return accntNo;
	}

	public void setAccntNo(String accntNo) {
		this.accntNo = accntNo;
	}

	public String getAccnTnm() {
		return accnTnm;
	}

	public void setAccnTnm(String accnTnm) {
		this.accnTnm = accnTnm;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getAddDesc() {
		return addDesc;
	}

	public void setAddDesc(String addDesc) {
		this.addDesc = addDesc;
	}
}
