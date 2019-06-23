package com.vxianjin.gringotts.pay.model.bill99;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement  
@XmlType(name = "pay2bankHead", propOrder = {"version", "memberCode"})  
public class Pay2bankHead {

	@XmlElement(required = true) 
	private String version;
	
	@XmlElement(required = true) 
	private String memberCode;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	
}
