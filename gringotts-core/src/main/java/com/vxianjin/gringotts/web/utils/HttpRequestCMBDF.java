package com.vxianjin.gringotts.web.utils;


import com.vxianjin.gringotts.common.ServiceResult;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 招行 代发付款
 *
 * @author zhangjb
 * @version V1.0
 * @ClassName: HttpRequestHL.java
 * @Description: TODO
 * @Date 2016年12月29日 上午9:54:38
 */
public class HttpRequestCMBDF {

    public static final String userName = "凌融银企";
    public static final String DBTACC = "121914245410502";// 银行账号
    public static final String BBKNBR = "21";// 银行号
    public static final String go_url = "http://180.173.0.188:4808";// 请求地址

//	public static final String userName=PropertiesUtil.get("userName_DF");
//	public static final String DBTACC=PropertiesUtil.get("DBTACC_DF");// 银行账号
// 
//	public static final String BBKNBR="21";// 银行号
//	public static final String go_url=PropertiesUtil.get("go_url_DF");// 请求地址
//	public static final String go_url="http://192.168.1.207:8080";// 请求地址

    /**
     * 生成请求报文正式   付款NTIBCOPR
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    public static String getRequestNTIBCOPRStr(List<BorrowOrder> borrowOrders) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

        // 构造支付的请求报文
        XmlPacket xmlPkt = new XmlPacket("AgentRequest", userName);

        Integer sumMoney = 0;

        for (BorrowOrder borrowOrder : borrowOrders) {
            sumMoney += borrowOrder.getIntoMoney();
        }

        Map mpPodInfo = new Properties();

        mpPodInfo.put("BUSCOD", "N03020");//
        mpPodInfo.put("BUSMOD", "00001");//业务模式编号
        mpPodInfo.put("TRSTYP", "BYBK");//交易代码
        mpPodInfo.put("C_TRSTYP", "代发其他");//交易代码名称

        mpPodInfo.put("DBTACC", DBTACC);//代发为转出账号 121914245410502
        mpPodInfo.put("BBKNBR", BBKNBR);//分行代码 附录A.1
        mpPodInfo.put("SUM", String.valueOf(sumMoney / 100));//总金额
        mpPodInfo.put("TOTAL", String.valueOf(borrowOrders.size()));//总笔数
        mpPodInfo.put("CCYNBR", "10");//币种代码
        mpPodInfo.put("YURREF", borrowOrders.get(0).getYurref());//业务参考号
        mpPodInfo.put("MEMO", "代发");//用途

        xmlPkt.putProperty("SDKATSRQX", mpPodInfo);

        for (BorrowOrder borrowOrder : borrowOrders) {
            Map mpPayInfo = new Properties();
            mpPayInfo.put("ACCNBR", borrowOrder.getCardNo());//
            mpPayInfo.put("CLTNAM", borrowOrder.getRealname());//户名
            mpPayInfo.put("TRSAMT", String.valueOf(borrowOrder.getIntoMoney() / 100));// 金额borrowOrder.getIntoMoney()/100


            xmlPkt.putProperty("SDKATDRQX", mpPayInfo);
        }
        return xmlPkt.toXmlString();
    }

    /**
     * 生成请求报文正式  交易查询NTQRYEBP
     *
     * @return
     */
    public static String getRequestNTQRYEBPStr() {
        String beginDate = DateUtil.fyFormatDate(DateUtil.addDay(new Date(), -1));
        String endDate = DateUtil.fyFormatDate(DateUtil.addDay(new Date(), 1));
        // 构造支付的请求报文
        XmlPacket xmlPkt = new XmlPacket("GetAgentInfo", userName);
        Map mpPayInfo = new Properties();

        mpPayInfo.put("BUSCOD", "N03020");// 网银贷记
        mpPayInfo.put("BGNDAT", beginDate);// 开始时间
        mpPayInfo.put("ENDDAT", endDate);// 结束时间

        xmlPkt.putProperty("SDKATSQYX", mpPayInfo);
        return xmlPkt.toXmlString();
    }

    /*
     * 连接前置机，发送请求报文，获得返回报文
     *
     * @param data
     *
     * @return
     *
     * @throws MalformedURLException
     */
    public static String sendRequest(String data) {
        String result = "";
        try {
            URL url;
            url = new URL(go_url);

            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("contentType", "gbk");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os;
            os = conn.getOutputStream();
            os.write(data.toString().getBytes("gbk"));
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "gbk"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println(result);
            br.close();
        } catch (Exception e) {
            System.out.println("HttpRequestCMBDF   connect error   代发uk链接异常  ee ");
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 处理返回的结果
     *
     * @param result
     */
    public static void processResult(String result) {
        if (result != null && result.length() > 0) {
            XmlPacket pktRsp = XmlPacket.valueOf(result);
            if (pktRsp != null) {
                String sRetCod = pktRsp.getRETCOD();
                if (sRetCod.equals("0")) {
                    Map propPayResult = pktRsp.getProperty("NTQPAYRQZ", 0);
                    String sREQSTS = (String) propPayResult.get("REQSTS");
                    String sRTNFLG = (String) propPayResult.get("RTNFLG");
                    if (sREQSTS.equals("FIN") && sRTNFLG.equals("F")) {
                        System.out.println("支付失败："
                                + propPayResult.get("ERRTXT"));
                    } else {
                        System.out.println("支付已被银行受理（支付状态：" + sREQSTS + "）");
                    }
                } else if (sRetCod.equals("-9")) {
                    System.out.println("支付未知异常，请查询支付结果确认支付状态，错误信息："
                            + pktRsp.getERRMSG());
                } else {
                    System.out.println("支付失败：" + pktRsp.getERRMSG());
                }
            } else {
                System.out.println("响应报文解析失败");
            }
        }
    }


    public static ServiceResult processPayResult(String result) {

        ServiceResult serviceResult = new ServiceResult(BorrowOrder.SUB_ERROR, "未知异常，请稍后重试！");
        try {

            if (result != null && result.length() > 0) {

                Map params = XmlConvertUtil.xml2Map(result);

                System.out.println("params:" + params);

                JSONObject INFO = JSONObject.fromObject(params.get("INFO"));
                int RETCOD = INFO.getInt("RETCOD");// 返回代码

                if (RETCOD == 0) {
                    serviceResult.setCode(BorrowOrder.SUB_SUBMIT);
                    serviceResult.setMsg(INFO.getString("ERRMSG"));
                } else {
                    serviceResult.setCode(BorrowOrder.SUB_PAY_FAIL);
                    serviceResult.setMsg(INFO.getString("ERRMSG"));
                }

                System.out.println(serviceResult.getCode() + ">>" + serviceResult.getMsg());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HLrequest  error  互联请求银行报错");
        }
        return serviceResult;
    }


    public static void query() {
        HttpRequestCMBDF request = new HttpRequestCMBDF();
        String data = getRequestNTQRYEBPStr();
        String result = sendRequest(data);


        System.out.println("return:" + result);
        // 更新订单

        // 处理返回的结果
        ServiceResult serviceResult = processPayResult(result);

    }

    public static void main(String[] args) {

        query();
    }

    public static void main111(String[] args) {
        //1..............................................................放款
        /*try {
            HttpRequestCMBDF request = new HttpRequestCMBDF();
			List<BorrowOrder> borrowOrders = new ArrayList<BorrowOrder>();

			BorrowOrder b1 = new BorrowOrder();
			b1.setIntoMoney(10000);
			b1.setRealname("范银川");
			b1.setOutTradeNo(GenerateNo.generateShortUuid(10));
			b1.setUserPhone("18917583863");
			b1.setCardNo("6226090218981542");// 6222021001081658020  
			b1.setBankNumber("102100099996");
			b1.setBankIscmb(2);

			BorrowOrder b2 = new BorrowOrder();
			b2.setIntoMoney(10000);
			b2.setRealname("张剑波");
			b2.setOutTradeNo(GenerateNo.generateShortUuid(10));
			b2.setUserPhone("17717367502");
			b2.setCardNo("6226091212357283");
			b2.setBankNumber("102100099996");
			b2.setBankIscmb(2);
			
			borrowOrders.add(b1);
			borrowOrders.add(b2);

			// 生成请求报文
			String data = getRequestNTIBCOPRStr(borrowOrders);
			// 插入订单
			System.out.println("request:" + data);
			// 连接前置机，发送请求报文，获得返回报文
			String result = request.sendRequest(data);
			System.out.println("return:" + result);
			// 更新订单

			// 处理返回的结果
			ServiceResult serviceResult =processPayResult(result);
			// 更新借款表
			System.out.println("result:" + serviceResult.getCode() + ">>"
					+ serviceResult.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}*/

        //2..............................................................返回结果
        /*String reult ="<CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG/><FUNNAM>AgentRequest</FUNNAM><LGNNAM>凌融银企</LGNNAM><RETCOD>0</RETCOD></INFO><NTREQNBRY><REQNBR>0749155550</REQNBR><RSV50Z>BNK</RSV50Z></NTREQNBRY></CMBSDKPGK>";
        ServiceResult aa =  processPayResult(reult);
		System.out.println(aa);*/

        //3。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
        try {


//			String   result="<?xml version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG></ERRMSG><FUNNAM>GetAgentInfo</FUNNAM><LGNNAM>凌融银企</LGNNAM><RETCOD>0</RETCOD></INFO><NTQATSQYZ><ACCNBR>121914245410502</ACCNBR><ATHFLG>N</ATHFLG><BBKNBR>21</BBKNBR><BUSCOD>N03020</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><C_CCYNBR>人民币</C_CCYNBR><C_TRSTYP>代发其他</C_TRSTYP><EPTDAT>20161230</EPTDAT><EPTTIM>000000</EPTTIM><NUSAGE>代发测试</NUSAGE><OPRDAT>20161230</OPRDAT><REQNBR>0749143815</REQNBR><REQSTA>FIN</REQSTA><RTNFLG>S</RTNFLG><SUCAMT>10.00</SUCAMT><SUCNUM>00001</SUCNUM><TOTAMT>10.00</TOTAMT><TRSNUM>00001</TRSNUM><TRSTYP>BYBK</TRSTYP><YURREF>TEST201612301707</YURREF></NTQATSQYZ><NTQATSQYZ><ACCNBR>121914245410502</ACCNBR><ATHFLG>N</ATHFLG><BBKNBR>21</BBKNBR><BUSCOD>N03020</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><C_CCYNBR>人民币</C_CCYNBR><C_TRSTYP>代发其他</C_TRSTYP><EPTDAT>20161230</EPTDAT><EPTTIM>000000</EPTTIM><NUSAGE>代发测试</NUSAGE><OPRDAT>20161230</OPRDAT><REQNBR>0749155550</REQNBR><REQSTA>FIN</REQSTA><RTNFLG>S</RTNFLG><SUCAMT>0.01</SUCAMT><SUCNUM>00001</SUCNUM><TOTAMT>0.01</TOTAMT><TRSNUM>00001</TRSNUM><TRSTYP>BYBK</TRSTYP><YURREF>TEST201612301701</YURREF></NTQATSQYZ><NTQATSQYZ><ACCNBR>121914245410502</ACCNBR><ATHFLG>N</ATHFLG><BBKNBR>21</BBKNBR><BUSCOD>N03020</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><C_CCYNBR>人民币</C_CCYNBR><C_TRSTYP>代发其他</C_TRSTYP><EPTDAT>20161230</EPTDAT><EPTTIM>000000</EPTTIM><NUSAGE>代发测试</NUSAGE><OPRDAT>20161230</OPRDAT><REQNBR>0749158153</REQNBR><REQSTA>FIN</REQSTA><RTNFLG>S</RTNFLG><SUCAMT>0.01</SUCAMT><SUCNUM>00001</SUCNUM><TOTAMT>0.01</TOTAMT><TRSNUM>00001</TRSNUM><TRSTYP>BYBK</TRSTYP><YURREF>TEST201612301797</YURREF></NTQATSQYZ><NTQATSQYZ><ACCNBR>121914245410502</ACCNBR><ATHFLG>N</ATHFLG><BBKNBR>21</BBKNBR><BUSCOD>N03020</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><C_CCYNBR>人民币</C_CCYNBR><C_TRSTYP>代发其他</C_TRSTYP><EPTDAT>20161230</EPTDAT><EPTTIM>000000</EPTTIM><NUSAGE>代发测试</NUSAGE><OPRDAT>20161230</OPRDAT><REQNBR>0749168922</REQNBR><REQSTA>FIN</REQSTA><RTNFLG>S</RTNFLG><SUCAMT>0.02</SUCAMT><SUCNUM>00002</SUCNUM><TOTAMT>0.02</TOTAMT><TRSNUM>00002</TRSNUM><TRSTYP>BYBK</TRSTYP><YURREF>TEST201612301796</YURREF></NTQATSQYZ></CMBSDKPGK>";
            String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG></ERRMSG><FUNNAM>GetAgentInfo</FUNNAM><LGNNAM>凌融银企</LGNNAM><RETCOD>0</RETCOD></INFO><NTQATSQYZ><ACCNBR>121914245410502</ACCNBR><ATHFLG>N</ATHFLG><BBKNBR>21</BBKNBR><BUSCOD>N03020</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><C_CCYNBR>人民币</C_CCYNBR><C_TRSTYP>代发其他</C_TRSTYP><EPTDAT>20170102</EPTDAT><EPTTIM>000000</EPTTIM><NUSAGE>代发</NUSAGE><OPRDAT>20170102</OPRDAT><REQNBR>0750150563</REQNBR><REQSTA>FIN</REQSTA><RTNFLG>S</RTNFLG><SUCAMT>0.01</SUCAMT><SUCNUM>00001</SUCNUM><TOTAMT>0.01</TOTAMT><TRSNUM>00001</TRSNUM><TRSTYP>BYBK</TRSTYP><YURREF>C20170102145616348292</YURREF></NTQATSQYZ></CMBSDKPGK>";
            String jsonstring = HttpRequestCMBDF.xml2JSON(result);


            JSONObject jsonObj = JSONObject.fromObject(jsonstring);

            Object objj = jsonObj.get("NTQATSQYZ");

            JSONArray arrays = null;
            if (objj instanceof JSONObject) {
                arrays = new JSONArray();
                arrays.add(objj);

            } else if (objj instanceof JSONArray) {

                arrays = jsonObj.getJSONArray("NTQATSQYZ");
            }


            for (int i = 0; i < arrays.size(); i++) {
                JSONObject obj = arrays.getJSONObject(i);

                String sYURREF = obj.getString("YURREF");
                String sREQSTA = obj.getString("REQSTA");
                String sRTNFLG = obj.getString("RTNFLG");


                if (sREQSTA.equals("FIN") && sRTNFLG.equals("F")) {
//				serviceResult.setCode(BorrowOrder.SUB_PAY_FAIL);
//				serviceResult.setMsg("支付失败：" + pktRsp.getERRMSG());
//				System.out.println(serviceResult.getCode() + ">>" + serviceResult.getMsg());
//				serviceResult.setMsg("银行支付失败：" + propPayResult.get("ERRTXT"));
                    System.out.println("2222");
                } else if (sREQSTA.equals("FIN") && sRTNFLG.equals("S")) {
//				serviceResult.setCode(BorrowOrder.SUB_PAY_SUCC);
//				serviceResult.setMsg("银行支付成功");
//				System.out.println(serviceResult.getCode() + ">>" + serviceResult.getMsg());
                    System.out.println("000");
                }


            }

//		 System.out.println(xxx.size());

//			HttpRequestCMBDF request = new HttpRequestCMBDF();
//			String queryRequest =  getRequestNTQRYEBPStr();
//			System.out.println("queryRequest:"+queryRequest);
//			String queryResult = request.sendRequest(queryRequest);
//			System.out.println("queryResult:"+queryResult);
//			if (queryResult != null && queryResult.length() > 0) {
//				String json = xml2JSON(queryResult);
//				System.out.println(json);
//			}
//String xxx="<?xml version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG></ERRMSG><FUNNAM>NTIBCOPR</FUNNAM><LGNNAM>NO36339064</LGNNAM><RETCOD>0</RETCOD></INFO><NTOPRDRTZ><RTNTIM>006</RTNTIM></NTOPRDRTZ><NTOPRRTNZ><ERRCOD>SUC0000</ERRCOD><REQNBR>0744390293</REQNBR><REQSTS>BNK</REQSTS><SQRNBR>123456</SQRNBR></NTOPRRTNZ></CMBSDKPGK>";
//String json = xml2JSON(xxx);
//System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String xml2JSON(String xml) {
        return new XMLSerializer().read(xml).toString();
    }


}