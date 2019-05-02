package com.vxianjin.gringotts.web.utils;

import com.vxianjin.gringotts.common.ServiceResult;
import com.vxianjin.gringotts.util.GenerateNo;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 招行 互联付款
 *
 * @author zhangjb
 * @version V1.0
 * @ClassName: HttpRequestHL.java
 * @Description: TODO
 * @Date 2016年12月29日 上午9:54:38
 */
public class HttpRequestCMBHL {


    public static final String userName = "NO36339064";
    public static final String ACCNBR = "121918527510607";// 银行账号
    public static final String CNVNBR = "0000001955";// // 协议号
    public static final String BBKNBR = "CB";// 银行号
    public static final String go_url = "http://180.173.0.188:9090";// 请求地址
//	public static final String BBKNBR = "CB";// 银行号
//	public static final String userName = PropertiesUtil.get("userName_HL");
//	public static final String ACCNBR = PropertiesUtil.get("ACCNBR_HL");// 银行账号
//	public static final String CNVNBR = PropertiesUtil.get("CNVNBR_HL");// // 协议号
//	public static final String go_url = PropertiesUtil.get("go_url_HL");// 请求地址

    /**
     * 生成请求报文正式 付款NTIBCOPR
     *
     * @return
     */
    public static String getRequestNTIBCOPRStr(List<BorrowOrder> borrowOrders) {

        String CCYNBR = "10";// 币种
        String TRSTYP = "C210";// 业务类型编码？？
        String TRSCAT = "09001";// 业务种类编码？？
        // 构造支付的请求报文
        XmlPacket xmlPkt = new XmlPacket("NTIBCOPR", userName);
        Map mpPodInfo = new Properties();
        mpPodInfo.put("BUSMOD", "00004");// 跨行付款？
        xmlPkt.putProperty("NTOPRMODX", mpPodInfo);
        List<Map> xxx = new ArrayList<Map>();
        for (BorrowOrder bo : borrowOrders) {

            Map mpPayInfo = new Properties();
            mpPayInfo.put("SQRNBR", GenerateNo.generateShortUuid(10));// 流水
            mpPayInfo.put("BBKNBR", BBKNBR);// 银行号
            mpPayInfo.put("ACCNBR", ACCNBR);// 银行账号

            mpPayInfo.put("CNVNBR", CNVNBR);// 协议号？？？
            mpPayInfo.put("YURREF", bo.getYurref());// 业务参考号？？？？ 订单号

            mpPayInfo.put("CCYNBR", CCYNBR);// 币种

            mpPayInfo.put("TRSAMT", String.valueOf(bo.getIntoMoney() / 100));// 金额//bo.getIntoMoney()/100
            mpPayInfo.put("CRTSQN", "");// 收方编号
            mpPayInfo.put("NTFCH1", bo.getUserPhone());// 通知方式一
            mpPayInfo.put("NTFCH2", "");// 通知方式二

            mpPayInfo.put("CDTNAM", bo.getRealname());// 收款人户名
            mpPayInfo.put("CDTEAC", bo.getCardNo());// 收款人账号
            mpPayInfo.put("CDTBRD", bo.getBankNumber());// 收款行行号

            mpPayInfo.put("TRSTYP", TRSTYP);// 业务类型编码？？
            mpPayInfo.put("TRSCAT", TRSCAT);// 业务种类编码？？
            mpPayInfo.put("RMKTXT", "现金侠放款打款");// 附言
            // mpPayInfo.put("RSV30Z", String.valueOf(bo.getId()));// 借款订单id

            xmlPkt.putProperty("NTIBCOPRX", mpPayInfo);
        }

        return xmlPkt.toXmlString();
    }

    /**
     * 生成请求报文正式 付款NTIBCOPR
     * yurref,userPhone,realname,cardNo,bankNumber,borrowId,intoMoney
     *
     * @return
     */
    public static String getRequestNTIBCOPRStrByMap(List<HashMap<String, String>> params) {

        String CCYNBR = "10";// 币种
        String TRSTYP = "C210";// 业务类型编码？？
        String TRSCAT = "09001";// 业务种类编码？？
        // 构造支付的请求报文
        XmlPacket xmlPkt = new XmlPacket("NTIBCOPR", userName);
        Map mpPodInfo = new Properties();
        mpPodInfo.put("BUSMOD", "00004");// 跨行付款？
        xmlPkt.putProperty("NTOPRMODX", mpPodInfo);

        for (HashMap<String, String> bo : params) {

            Map mpPayInfo = new Properties();
            mpPayInfo.put("SQRNBR", GenerateNo.generateShortUuid(10));// 流水
            mpPayInfo.put("BBKNBR", BBKNBR);// 银行号
            mpPayInfo.put("ACCNBR", ACCNBR);// 银行账号

            mpPayInfo.put("CNVNBR", CNVNBR);// 协议号？？？
            mpPayInfo.put("YURREF", bo.get("yurref"));// 业务参考号？？？？ 订单号

            mpPayInfo.put("CCYNBR", CCYNBR);// 币种

            mpPayInfo.put("TRSAMT", bo.get("intoMoney"));// 金额//bo.getIntoMoney()/100
            mpPayInfo.put("CRTSQN", "");// 收方编号
            mpPayInfo.put("NTFCH1", bo.get("userPhone"));// 通知方式一
            mpPayInfo.put("NTFCH2", "");// 通知方式二

            mpPayInfo.put("CDTNAM", bo.get("realname"));// 收款人户名
            mpPayInfo.put("CDTEAC", bo.get("cardNo"));// 收款人账号
            mpPayInfo.put("CDTBRD", bo.get("bankNumber"));// 收款行行号

            mpPayInfo.put("TRSTYP", TRSTYP);// 业务类型编码？？
            mpPayInfo.put("TRSCAT", TRSCAT);// 业务种类编码？？
            mpPayInfo.put("RMKTXT", "现金侠放款打款");// 附言
            mpPayInfo.put("RSV30Z", bo.get("RSV30Z"));// 暂时没用

            xmlPkt.putProperty("NTIBCOPRX", mpPayInfo);
        }

        return xmlPkt.toXmlString();
    }

    /**
     * 生成请求报文正式 交易查询NTQRYEBP
     *
     * @return
     */
    public static String getRequestNTQRYEBPStr() {
        String beginDate = DateUtil.fyFormatDate(DateUtil.addDay(new Date(), -1));
        String endDate = DateUtil.fyFormatDate(DateUtil.addDay(new Date(), 1));
        // 构造支付的请求报文
        XmlPacket xmlPkt = new XmlPacket("NTQRYEBP", userName);
        Map mpPayInfo = new Properties();

        mpPayInfo.put("BUSCOD", "N31010");// 网银贷记
        mpPayInfo.put("BGNDAT", beginDate);// 开始时间
        mpPayInfo.put("ENDDAT", endDate);// 结束时间
        xmlPkt.putProperty("NTWAUEBPY", mpPayInfo);
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

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }

            System.out.println(result);
            br.close();
        } catch (Exception e) {
            System.out.println("HttpRequestCMBHL   connect error   互联uk链接异常  ee ");
            e.printStackTrace();
        }
        return result;
    }

    public static ServiceResult processPayResult(String result) {

        ServiceResult serviceResult = new ServiceResult(BorrowOrder.SUB_ERROR, "未知异常，请稍后重试！");
        try {

            if (result != null && result.length() > 0) {

                String jsonstring = HttpRequestCMBDF.xml2JSON(result);

                JSONObject jsonObj = JSONObject.fromObject(jsonstring);

                JSONObject INFO = jsonObj.getJSONObject("INFO");
                String RETCOD = INFO.getString("RETCOD");// 返回代码

                if (RETCOD.equals("0")) {
                    JSONObject propPayResult = jsonObj.getJSONObject("NTOPRRTNZ");
                    String sREQSTS = propPayResult.getString("REQSTS");
                    serviceResult.setCode(BorrowOrder.SUB_SUBMIT);
                    serviceResult.setMsg("支付已被银行受理（支付状态：" + sREQSTS + "）");
                    System.out.println(serviceResult.getCode() + ">>" + serviceResult.getMsg());

                } else if (RETCOD.equals("-9")) {
                    serviceResult.setCode(BorrowOrder.SUB_SUBMIT);
                    serviceResult.setMsg("支付未知异常，请查询支付结果确认支付状态，错误信息：" + INFO.getString("ERRMSG"));
                    System.out.println(serviceResult.getCode() + ">>" + serviceResult.getMsg());
                } else {
                    serviceResult.setCode(BorrowOrder.SUB_SUB_FAIL);
                    serviceResult.setMsg("支付失败：" + INFO.getString("ERRMSG"));
                    System.out.println(serviceResult.getCode() + ">>" + serviceResult.getMsg());
                }

            }

        } catch (Exception e) {
            System.out.println("HLrequest  error  互联请求银行报错");
        }
        return serviceResult;
    }

    public static void main(String[] args) {
        try {
            HttpRequestCMBHL request = new HttpRequestCMBHL();
            // List<BorrowOrder> borrowOrders = new ArrayList<BorrowOrder>();
            //
            // BorrowOrder b1 = new BorrowOrder();
            // b1.setIntoMoney(10000);
            // b1.setRealname("范银川");
            // b1.setOutTradeNo(GenerateNo.generateShortUuid(10));
            // b1.setUserPhone("18917583863");
            // b1.setCardNo("6222021001081658020");
            // b1.setBankNumber("102100099996");
            // b1.setBankIscmb(2);
            //
            // BorrowOrder b2 = new BorrowOrder();
            // b2.setIntoMoney(10000);
            // b2.setRealname("张剑波");
            // b2.setOutTradeNo(GenerateNo.generateShortUuid(10));
            // b2.setUserPhone("17717367502");
            // b2.setCardNo("6212251001002321486");
            // b2.setBankNumber("102100099996");
            // b2.setBankIscmb(2);
            // borrowOrders.add(b1);
            // borrowOrders.add(b2);
            //
            // // 生成请求报文
            // String data = getRequestNTIBCOPRStr(borrowOrders);
            // // 插入订单
            // System.out.println("request:" + data);
            // // 连接前置机，发送请求报文，获得返回报文
            // String result = request.sendRequest(data);
            // System.out.println("return:" + result);
            // // 更新订单
//			String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG></ERRMSG><FUNNAM>NTIBCOPR</FUNNAM><LGNNAM>NO36339064</LGNNAM><RETCOD>0</RETCOD></INFO><NTOPRDRTZ><RTNTIM>006</RTNTIM></NTOPRDRTZ><NTOPRRTNZ><ERRCOD>SUC0000</ERRCOD><REQNBR>0750142306</REQNBR><REQSTS>BNK</REQSTS><SQRNBR>2lOVIaWSY0</SQRNBR></NTOPRRTNZ></CMBSDKPGK>";
//			// 处理返回的结果
//			ServiceResult serviceResult = processPayResult(result);
//			// 更新借款表
//			System.out.println("result:" + serviceResult.getCode() + ">>" + serviceResult.getMsg());

            String data = getRequestNTQRYEBPStr();
            System.out.println(data);
            String result = sendRequest(data);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}