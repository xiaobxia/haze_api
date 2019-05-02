package com.vxianjin.gringotts.web.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.constant.SmsConfigConstant;
import com.vxianjin.gringotts.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信
 *
 * @author gaoyuhai
 * 2016-6-17 下午03:50:44
 */
public class SendSmsUtil {

    private static final Logger loger = LoggerFactory.getLogger(SendSmsUtil.class);
    private static String apiUrl = SmsConfigConstant.getConstant("apiurl");

    private static String account = SmsConfigConstant.getConstant("account");
    private static String pswd = SmsConfigConstant.getConstant("pswd");
    private static String sign = SmsConfigConstant.getConstant("sign");
    private static String templateld = SmsConfigConstant.getConstant("templateld");

    public static boolean sendSmsCL(String telephone, String sms){
        loger.info("sendSms:" + telephone + "   sms=" + sms);

        try{
            // String msg = Constant.SMS_VERIFY_CONTENT.replace("#cont#", sms);

            //做URLEncoder - UTF-8编码
            String sm = URLEncoder.encode(sms, "utf8");
            //将参数进行封装
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("accesskey", account);
            paramMap.put("secret", pswd);
            paramMap.put("sign", sign);
            paramMap.put("templateId", templateld);

            //单一内容时群发  将手机号用;隔开
            paramMap.put("mobile", telephone);
            paramMap.put("content", sm);
            String result = HttpUtil.sendPost(apiUrl,paramMap);
            System.out.println("result="+result);
            JSONObject jsonObject = JSON.parseObject(result);
            return jsonObject!=null && jsonObject.getInteger("code") == 0;
        }catch (Exception e){
            loger.error("sendSmsCL error:{} ",e);
            return false;
        }
    }

    /**
     * 创蓝 自定义短信内容
     *
     * @param telephone 手机号
     * @param content   内容
     * @return boolean b
     */
    public static boolean sendSmsDiyCL(String telephone, String content) {
        loger.info("sendSms:" + telephone + "   sms=" + content);
        try{
            String sm = URLEncoder.encode(content, "utf8");
            //将参数进行封装
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("un", account);
            paramMap.put("pw", pswd);

            //单一内容时群发  将手机号用;隔开
            paramMap.put("da", telephone);
            paramMap.put("sm", sm);
            String result = HttpUtil.sendPost(apiUrl,paramMap);
            JSONObject jsonObject = JSON.parseObject(result);
            return jsonObject!=null && jsonObject.getBoolean("success");
        }catch (Exception e){
            loger.error("send sms error:{}",e);
            return false;
        }

    }

    public static boolean sendSmsVoiceCode(String telephone, String content) {

//        Map<String, String> map = new HashMap<String, String>();
//        map.put("account", sms_voice_account);
//        map.put("pswd", sms_voice_pswd);
//        map.put("mobile", telephone);
//        map.put("msg", content);
//        map.put("needstatus", "true");
//
//        IZz253ApiService service = CloseableOkHttp.obtainRemoteService(
//                api, IZz253ApiService.class
//        );
//        try {
//            Response<ResponseBody> response = service.msgHttpBatchSendSM(map).execute();
//            if (response.isSuccessful()) {
//                ResponseBody body = response.body();
//                String string = null;
//                if (body != null) {
//                    string = body.string();
//
//                    String[] split = string.split("\n");
//                    String[] array = split[0].split(",");
//                    if (array.length >= 2) {
//                        if ("0".equals(array[1])) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
        return false;
    }

//    static class SmsSendRequest {
//        /**
//         * 用户账号，必填
//         */
//        private String account;
//        /**
//         * 用户密码，必填
//         */
//        private String password;
//        /**
//         * 短信内容。长度不能超过536个字符，必填
//         */
//        private String msg;
//        /**
//         * 机号码。多个手机号码使用英文逗号分隔，必填
//         */
//        private String phone;
//
//        public SmsSendRequest(String account, String password, String msg, String phone) {
//            super();
//            this.account = account;
//            this.password = password;
//            this.msg = msg;
//            this.phone = phone;
//        }
//
//
//        public String getAccount() {
//            return account;
//        }
//
//        public void setAccount(String account) {
//            this.account = account;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//
//        public void setPassword(String password) {
//            this.password = password;
//        }
//
//        public String getMsg() {
//            return msg;
//        }
//
//        public void setMsg(String msg) {
//            this.msg = msg;
//        }
//
//        public String getPhone() {
//            return phone;
//        }
//
//        public void setPhone(String phone) {
//            this.phone = phone;
//        }
//
//    }

}
