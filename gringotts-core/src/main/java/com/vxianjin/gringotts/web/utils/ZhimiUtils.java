package com.vxianjin.gringotts.web.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.web.pojo.risk.ZhimiContact;
import com.vxianjin.gringotts.web.pojo.risk.ZhimiEmergencyContact;
import com.vxianjin.gringotts.web.pojo.risk.ZhimiRiskRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZhimiUtils  {
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";
    public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";


    public static byte[] gzip(String str){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(GZIP_ENCODE_UTF_8));
        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if(gzip != null){
                try{
                    gzip.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return out.toByteArray();
    }

    public static String uncompress(InputStream gzippedResponse) throws IOException {

        InputStream decompressedResponse = new GZIPInputStream(gzippedResponse);
        Reader reader = new InputStreamReader(decompressedResponse, "UTF-8");
        StringWriter writer = new StringWriter();

        char[] buffer = new char[10240];
        for(int length = 0; (length = reader.read(buffer)) > 0;){
            writer.write(buffer, 0, length);
        }

        writer.close();
        reader.close();
        decompressedResponse.close();
        gzippedResponse.close();

        return writer.toString();
    }

    public static byte[] compress(String str) throws IOException {
        return gzip(str);
    }

    public static void main(String[] args) throws IOException {

        String appId = "gxb79554ed8a02eeb28";
        String appSecret = "da1855416a844cdba918800b9b0f1dc3";


        String GXBREPORT = "https://prod.gxb.io/crawler/data/report/%s?appId=%s&timestamp=%s&sign=%s";
        String GXBRAWDATA = "https://prod.gxb.io/crawler/data/rawdata/%s?appId=%s&timestamp=%s&sign=%s";
        String timestamp = new Date().getTime()+"";
        String md5Hex = DigestUtils.md5Hex(String.format("%s%s%s", appId, appSecret, timestamp));
        String reportUrl = String.format(GXBREPORT, "", appId, timestamp, md5Hex);
        String rawDataUrl = String.format(GXBRAWDATA, "", appId, timestamp, md5Hex);
        String gxb_report = HttpUtil.post(reportUrl, null);
        String gxb_raw = HttpUtil.post(rawDataUrl, null);

        /*for (int i = 0; i < 1; i++) {
            String fileContent = null;
            try {
                fileContent = IOUtils.toString(new FileInputStream("sample_data"), StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject sampleObject = JSON.parseObject(String.valueOf(fileContent));
            String model_name = "test_v1";
            String product = "test";
            String channel = "test";
            String apply_time = sampleObject.getString("apply_time");
            String mobile = sampleObject.getString("mobile");
            String name = sampleObject.getString("name");
            String idcard = sampleObject.getString("idcard");
            String phone_os = "android";
            String user_address = sampleObject.getString("user_address");
            List<ZhimiEmergencyContact> e_contacts = new ArrayList<ZhimiEmergencyContact>();
            e_contacts.add(new ZhimiEmergencyContact(sampleObject.getString("contact1_name"), sampleObject.getString("contact1_phone")));
            e_contacts.add(new ZhimiEmergencyContact(sampleObject.getString("contact2_name"), sampleObject.getString("contact2_phone")));

            Map<String, String> carrier_data = new HashMap<String, String>();
            JSONObject carrierDataObject = sampleObject.getJSONObject("carrier_data");
            carrier_data.put("mx_report", JSON.toJSONString(carrierDataObject.getJSONObject("mx_report"), SerializerFeature.WriteMapNullValue));
            carrier_data.put("mx_raw", JSON.toJSONString(carrierDataObject.getJSONObject("mx_raw"), SerializerFeature.WriteMapNullValue));

            List<ZhimiContact> contact = new ArrayList<ZhimiContact>();
            JSONArray contactArray = sampleObject.getJSONArray("contact");
            for (int j = 0; j < contactArray.size(); j++) {
                JSONObject contactItemObject = contactArray.getJSONObject(j);
                contact.add(new ZhimiContact(contactItemObject.getString("contact_name"), contactItemObject.getString("contact_phone"), contactItemObject.getString("update_time")));
            }

            ZhimiRiskRequest request = new ZhimiRiskRequest();
            request.setModel_name(model_name);
            request.setProduct(product);
            request.setChannel(channel);
            request.setApply_time(apply_time);
            request.setMobile(mobile);
            request.setName(name);
            request.setIdcard(idcard);
            request.setPhone_os(phone_os);
            request.setUser_address(user_address);
            request.setCarrier_data(carrier_data);
            request.setE_contacts(e_contacts);
            request.setContact(contact);


            String requestStr = JSON.toJSONString(request, SerializerFeature.WriteMapNullValue);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://47.93.185.26/risk/gzip/");
            post.setEntity(new ByteArrayEntity(gzip(requestStr)));
            try {
                HttpResponse response = client.execute(post);
                String responseStr = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
                System.out.println(responseStr);
            } catch(IOException e){
                e.printStackTrace();
            }
        }*/
    }
}
