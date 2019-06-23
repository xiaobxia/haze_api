package com.vxianjin.gringotts.pay.common.util.bill99;


import com.vxianjin.gringotts.pay.model.bill99.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;


/**
 * 工具类
 * @author zhiwei.ma
 *
 */
public class CCSUtil {
	
	/**
	 * 创建request
	 * @return
	 */
	public static Pay2bankRequest genRequest(String membercode_head , String version){
		Pay2bankRequest request = new Pay2bankRequest();
		Pay2bankHead head = new Pay2bankHead();
		head.setMemberCode(membercode_head);
		head.setVersion(version);
		RequestBody requestBody = new RequestBody();
		SealDataType sealDataType = new SealDataType();
		requestBody.setSealDataType(sealDataType);
		request.setPay2bankHead(head);
		request.setRequestBody(requestBody);
		return request;
	}
	
	/**
	 * 随机生成一笔订单
	 * @return
	 */
	public static Pay2bankOrder genOrder(){
		Pay2bankOrder order = new Pay2bankOrder();
		//商家订单号 必填
		order.setOrderId(genOrderId());
		//金额（分） 必填
		order.setAmount("100");
		//银行名称 必填
		order.setBankName("招商银行");
		//开户行  非必填
		//order.setBranchName("城东支行");
		//收款人姓名  必填
		order.setCreditName("张三");
		//收款人手机号  非必填
		//order.setMobile("18700000000");
		//银行卡号 必填
		order.setBankAcctId("6214832506113617");
//		//省份城市 非必填
//		order.setProvince("江苏");
//		order.setCity("南京");
		//备注 非必填
		//order.setRemark("5月工资");
		//手续费作用方：0收款方付费1付款方付费  非必填 默认1
		//order.setFeeAction("1");
		return order;
	}

	/**
	 * 生成一个随机数作为商家订单号
	 * @return
	 */
	private static String genOrderId() {
		return "KQ" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
	}

	/** 
     * JavaBean转换成xml 
     * @param obj 
     * @param encoding  
     * @return  
     */  
    public static String convertToXml(Object obj, String encoding) {  
        String result = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller();  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
  
            StringWriter writer = new StringWriter();  
            marshaller.marshal(obj, writer);  
            result = writer.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }  
    
    /** 
     * xml转换成JavaBean 
     * @param xml 
     * @param c 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T converyToJavaBean(String xml, Class<T> c) {  
        T t = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(c);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            t = (T) unmarshaller.unmarshal(new StringReader(xml));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return t;  
    }  
	
}
