package com.vxianjin.gringotts.api;

import com.vxianjin.gringotts.api.service.IYeePayApiService;
import com.vxianjin.gringotts.lib.CallbackUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: kiro
 * @Date: 2018/7/5
 * @Description:
 */
public class YeePayApiServiceTest extends AbstractApiTest {

    public static String getRequestStr(Map<String, String> pmap, String hmacKey) {
        StringBuffer hmacStr = new StringBuffer();

        hmacStr.append(pmap.get("cmd"))
                .append(pmap.get("mer_Id"))
                .append(pmap.get("batch_No"))
                .append(pmap.get("order_Id"))
                .append(pmap.get("amount"))
                .append(pmap.get("account_Number"))
                .append(hmacKey);
        return hmacStr.toString();
    }

    public static String getRequestXmlSingle(Map<String, String> pmap) {
        StringBuffer xmlStr = new StringBuffer();
        xmlStr.append("<data>")
                .append("<cmd>" + pmap.get("cmd") + "</cmd>")
                .append("<version>" + pmap.get("version") + "</version>")
                .append("<mer_Id>" + pmap.get("mer_Id") + "</mer_Id>")
                .append("<group_Id>" + pmap.get("group_Id") + "</group_Id>")
                .append("<bank_Name>" + pmap.get("bank_Name") + "</bank_Name>")
                .append("<batch_No>" + pmap.get("batch_No") + "</batch_No>")
                .append("<order_Id>" + pmap.get("order_Id") + "</order_Id>")
                .append("<amount>" + pmap.get("amount") + "</amount>")
                .append("<account_Name>" + pmap.get("account_Name") + "</account_Name>")
                .append("<account_Number>" + pmap.get("account_Number") + "</account_Number>")
                .append("<fee_Type>" + pmap.get("fee_Type") + "</fee_Type>")
                .append("<remarksInfo>" + pmap.get("remarksInfo") + "</remarksInfo>")
                .append("<urgency>" + pmap.get("urgency") + "</urgency>")
                .append("<hmac>" + pmap.get("hmac") + "</hmac>")
                .append("</data>");
        return xmlStr.toString();
    }

    @Test
    public void groupTransfer() throws IOException {

        TreeMap<String, String> dataMap = new TreeMap<String, String>();
        dataMap.put("cmd", "TransferSingle");
        dataMap.put("version", "1.1");
        dataMap.put("mer_Id", "10000450379");
        dataMap.put("group_Id", "10000450379");
        dataMap.put("batch_No", "1");
        dataMap.put("order_Id", "1");
        dataMap.put("amount", "1");
        dataMap.put("account_Name", "陈俊棋");
        dataMap.put("account_Number", "424242424242424242");
        dataMap.put("fee_Type", "SOURCE");
        dataMap.put("remarksInfo", "测试放款");
        dataMap.put("urgency", "1");
        dataMap.put("bank_Name", "中国光大银行");
        String requestXml = getRequestXmlSingle(dataMap);


        IYeePayApiService service = CloseableOkHttp.obtainRemoteService("https://cha.yeepay.com/", IYeePayApiService.class);
        retrofit2.Response<ResponseBody> response =
                service.groupTransfer(RequestBody.create(MediaType.parse("Content-Type:text/xml;charset=gbk"), requestXml)).execute();
        System.out.println(response.code());
        System.out.println(response.message());
        System.out.println(new String(response.body().bytes(), Charset.forName("gbk")));
        System.out.println(response.errorBody());
    }

    @Test
    public void testRequest() throws IOException {

        String requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<data><cmd>TransferSingle</cmd><version>1.1</version>" +
                "<mer_Id>10021644500</mer_Id>" +
                "<group_Id>10021644500</group_Id>" +
                "<bank_Name>中国建设银行</bank_Name>" +
                "<batch_No>111532078542014</batch_No>" +
                "<order_Id>kyLikCWIqS</order_Id>" +
                "<amount>805.00</amount>" +
                "<account_Name>李志鹏</account_Name>" +
                "<account_Number>6236681540013110692</account_Number>" +
                "<fee_Type>SOURCE</fee_Type>" +
                "<remarksInfo>有零花放款</remarksInfo>" +
                "<urgency>1</urgency>" +
                "<hmac>MIIGzgYJKoZIhvcNAQcCoIIGvzCCBrsCAQExCzAJBgUrDgMCGgUAMC8GCSqGSIb3DQEHA" +
                "aAiBCA0M2Y5NmJkZmJiMGI1ZWVkYzVjOTFiZmY3YTMxNzFlNaCCBOowggTmMIIDzqADAgECAgVBEYZDEDANBgkqhkiG9w0BAQsFADB" +
                "YMQswCQYDVQQGEwJDTjEwMC4GA1UECgwnQ2hpbmEgRmluYW5jaWFsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRcwFQYDVQQDDA" +
                "5DRkNBIEFDUyBPQ0EzMTAeFw0xODA1MjIwODIxMThaFw0yMDA1MjIwODIxMThaMIGUMQswCQYDVQQGEwJDTjEXMBUGA1UECgwOQ0ZDQSBBQ1" +
                "MgT0NBMzExDzANBgNVBAsMBllFRVBBWTEZMBcGA1UECwwQT3JnYW5pemF0aW9uYWwtMTFAMD4GA1UEAww3MDUxQOadreW3nuS7keiAhee9kee7nOen" +
                "keaKgOaciemZkOWFrOWPuEBOMTAwMjE2NDQ1MDBAMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANBQzbxqQQ6H+/+LNPJi9NwFfwZlQ2Aklu0WAHk" +
                "ENyF1xYwbEn1iOD7O4wxcmwqCohVhEVJAKck7JS5tJNOwCpKJ/mWouu01okHzsoiLibxCYHsqPkaTuOiUjD8Ja4QHKTHKI7TceYS/JcsuXtJHf45DFpWFe+k677d" +
                "nlvr9ks5YmOlQ6ixDJsEKrt4S/48lnifOaPmKomDpb84soHUKS8NBT+YQJ2OhVBIsd+E+T8OKDbyeN4NUHtt8cHFt38TGdUvODxTnikrP3eoB84A00ewyduzfTob4wm" +
                "nTjLtHnvs8wUsphpwReI/w1mjZc0Lml7vZRP/AnQINTNZxjzywxOUCAwEAAaOCAXgwggF0MGwGCCsGAQUFBwEBBGAwXjAoBggrBgEFBQcwAYYcaHR0cDovL29jc3AuY2Zj" +
                "YS5jb20uY24vb2NzcDAyBggrBgEFBQcwAoYmaHR0cDovL2NybC5jZmNhLmNvbS5jbi9vY2EzMS9vY2EzMS5jZXIwHwYDVR0jBBgwFoAU4rQJy81hoXNKeX/xioML3bR+jB0wDAYDVR0TAQ" +
                "H/BAIwADBIBgNVHSAEQTA/MD0GCGCBHIbvKgEEMDEwLwYIKwYBBQUHAgEWI2h0dHA6Ly93d3cuY2ZjYS5jb20uY24vdXMvdXMtMTQuaHRtMD0GA1UdHwQ2MDQwMqAwoC6GLGh0dHA6Ly9j" +
                "cmwuY2ZjYS5jb20uY24vb2NhMzEvUlNBL2NybDExMzMuY3JsMA4GA1UdDwEB/wQEAwIGwDAdBgNVHQ4EFgQUkTYaaRKF+hc/kVCABUYpGoUYiVYwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQU" +
                "FBwMEMA0GCSqGSIb3DQEBCwUAA4IBAQA6o7pzGROq6IB5BLOu0L9Hhbd4fpDhJGC8MmoByzIw/0rXeI3UEPVZ2QQVuNk8yKddxXY57mC95Ghazvq0eLWVfB4tII080xFXEUd9PL99BuLx3kPN" +
                "iHWCSGREqA1hN7RPe7x+jgWLSlFmdoxWVGywmJ7dD4WzwWRyhJ3XmZocR79bCCt2mvEF8+eGC05s8cu2gcGnfa+Q48leXJhgssMHCCmzOFqEvz7FGfL1DvU4FqmYH8reCBzWJAqyNWrLlAOizn6wM" +
                "NupmuSGP1aIl4czxlc4uYY1iWLeaRLE68r2EHJOf4JL3lv91uXwTL11hqH31bxOWF4LZxmYtN5oxaJUMYIBiDCCAYQCAQEwYTBYMQswCQYDVQQGEwJDTjEwMC4GA1UEChMnQ2hpbmEgRmluYW5jaWFs" +
                "IENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRcwFQYDVQQDEw5DRkNBIEFDUyBPQ0EzMQIFQRGGQxAwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQA10lOI/4Fhc3W7wrQ3sRUeTcsgUPxqPi9e/vR" +
                "ySWUNfoJxFODmplrDXAO7NcF9VH7N6rLvUFBendzmzr953zIEOadc9B51KMEYFJ/Zo8xclkqHrTRyijV3IRiXv8JCxrK43diknPtY8+CFa4IWN4OHJYZTFrHP/1DZ2egfMKY2FuqxWBuKtr/onXoBH4l" +
                "mUcDQQmhdRQu96NSiKf/dTAQzG1TMgzXCYPHJyZiOXnUYbt7+8KfEQh2eJU8aNiXx5w2r7162XNqFzV2/u6M0WOJmDsddx5ChbfNHC8biiGi1anWyr2QBy9J0NDbOl5Df7L0VyXQZMiEojVTdorHyhf+W</hmac>" +
                "</data>";
        String value = new String(requestXML.getBytes("gbk"), "gbk");
        System.out.println("gbk编码长度：" + value.getBytes().length);

        IYeePayApiService service = CloseableOkHttp.obtainRemoteService("http://cha.yeepay.com/", IYeePayApiService.class);
        retrofit2.Response<ResponseBody> response =
                service.groupTransfer(RequestBody.create(MediaType.parse("Content-Type:text/xml;charset=gbk"), requestXML.getBytes("gbk"))).execute();
        System.out.println(response.code());
        System.out.println(response.message());
//        System.out.println(">>>:" + new String(response.body().bytes(), "gbk"));

        assert response.body() != null;
//        assert response.body().bytes() != null;

        ResponseBody body = response.body();
        if (body != null) {
            String responseMsg = new String(body.bytes(), Charset.forName("gbk"));
            System.out.println("==========================");
            System.out.println("==========================");
            System.out.println("==========================");
            System.out.println("==========================");
            System.out.println(responseMsg);
        }

    }

    @Test
    public void testRequestOld() {
        String requestXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<data><cmd>TransferSingle</cmd><version>1.1</version>" +
                "<mer_Id>10021644500</mer_Id>" +
                "<group_Id>10021644500</group_Id>" +
                "<bank_Name>中国建设银行</bank_Name>" +
                "<batch_No>111532078542014</batch_No>" +
                "<order_Id>kyLikCWIqS</order_Id>" +
                "<amount>805.00</amount>" +
                "<account_Name>李志鹏</account_Name>" +
                "<account_Number>6236681540013110692</account_Number>" +
                "<fee_Type>SOURCE</fee_Type>" +
                "<remarksInfo>有零花放款</remarksInfo>" +
                "<urgency>1</urgency>" +
                "<hmac>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</hmac></data>";
        String responseMsg = CallbackUtils.httpRequest(
                "http://cha.yeepay.com/app-merchant-proxy/groupTransferController.action",
                requestXML,
                "POST",
                "gbk",
                "text/xml;charset=gbk",
                false);
        System.out.println(responseMsg);
    }

    @Test
    public void condition() throws UnsupportedEncodingException {
        String body = "<data>  <cmd>TransferSingle</cmd>  <ret_Code>1</ret_Code>  <order_Id>kIvEeRt4K6</order_Id>  <r1_Code>0025</r1_Code>\n" +
                "  <bank_Status>I</bank_Status>  <error_Msg></error_Msg>  <hmac>MIIE6QYJKoZIhvcNAQcCoIIE2jCCBNYCAQExCzAJBgUrDgMCGgUAMC8GCSqGSIb3DQEHAaAiBCBmYTUxOTlhN2RmMmIwMWE4NjNlYTQ3ZjJiMWY0ZmQyMKCCA7EwggOtMIIDFqADAgECAhAuyfCgPbMcigfvVyGazeQRMA0GCSqGSIb3DQEBBQUAMCQxCzAJBgNVBAYTAkNOMRUwEwYDVQQKEwxDRkNBIFRFU1QgQ0EwHhcNMTExMTI4MDcwOTUzWhcNMTMxMTI4MDcwOTUzWjBzMQswCQYDVQQGEwJDTjEVMBMGA1UEChMMQ0ZDQSBURVNUIENBMQ8wDQYDVQQLEwZZRUVQQVkxEjAQBgNVBAsTCUN1c3RvbWVyczEoMCYGA1UEAxQfMDQxQFoxMjNxd2VAemhpd2VuLm1laUAwMDAwMDAwMTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAzrITNElBaFF7xPXtPguWeTnGOI1gVMMkUDI57ZQz+Gg9PPcfF+ExrtDgMQEOwfRs7X4XiraPE2l6ub0Xkpl0ftu8ELnii91wUKAqsvp88NIdAdLQnC7PeveWlquVSAf//2WtAkdBI7xnhXaL/ObUkhHheT0aR5miYmDyLAkTBj8CAwEAAaOCAY8wggGLMB8GA1UdIwQYMBaAFEZy3CVynwJOVYO1gPkL2+mTs/RFMB0GA1UdDgQWBBS0k6A7ZSwLRwbIhFsgcChrYd27PDALBgNVHQ8EBAMCBaAwDAYDVR0TBAUwAwEBADA7BgNVHSUENDAyBggrBgEFBQcDAQYIKwYBBQUHAwIGCCsGAQUFBwMDBggrBgEFBQcDBAYIKwYBBQUHAwgwgfAGA1UdHwSB6DCB5TBPoE2gS6RJMEcxCzAJBgNVBAYTAkNOMRUwEwYDVQQKEwxDRkNBIFRFU1QgQ0ExDDAKBgNVBAsTA0NSTDETMBEGA1UEAxMKY3JsMTI2XzE5NDCBkaCBjqCBi4aBiGxkYXA6Ly90ZXN0bGRhcC5jZmNhLmNvbS5jbjozODkvQ049Y3JsMTI2XzE5NCxPVT1DUkwsTz1DRkNBIFRFU1QgQ0EsQz1DTj9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Y2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwDQYJKoZIhvcNAQEFBQADgYEAKX4CXCPQEE4RWGsZTXZXLBct2gcPYEjqpgPZ5ERiUrYLTDGuIT90ECfSoxCrcTJEeY7EJBojig9gLRoMn/4xXW/XscGarQ3XxyZw8VxTMFkotuUkAPoaacYlIrc34t2DR0DqvU6umgFL3yTMYxl5WLjOh47OH7Aw7VPscmrtzEIxgd0wgdoCAQEwODAkMQswCQYDVQQGEwJDTjEVMBMGA1UEChMMQ0ZDQSBURVNUIENBAhAuyfCgPbMcigfvVyGazeQRMAkGBSsOAwIaBQAwDQYJKoZIhvcNAQEBBQAEgYA+paNTzSOQPAn4Ea+VTQhBsW4FMjS1uT1f2i1AYvNunwG5kn7f370VkovmCSOwa0TbssfvmPjw/LvylpcuOJxZVnxQ5yd7d7HbQ6NT+TD2k2R1rMKU+GjsUTq35ODUaISw4/Xub22G9GDvcL32bj6p2wt3lABpx6Jk2NME2Kt14w==</hmac></data>";
        String responseMsg = new String(body.getBytes("gbk"), Charset.forName("gbk"));

    }
}
