package com.vxianjin.gringotts.web.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.IdUtil;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.common.ud.UdRequestUtils;
import com.vxianjin.gringotts.web.dao.IUserBlackDao;
import com.vxianjin.gringotts.web.dao.IUserContactsDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserBlack;
import com.vxianjin.gringotts.web.pojo.UserContacts;
import com.vxianjin.gringotts.web.pojo.risk.*;
import com.vxianjin.gringotts.web.service.IMoneyLimitService;
import com.vxianjin.gringotts.web.utils.ZhimiUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lucy on 2019/5/3.
 */
@Controller
@RequestMapping("testController")
public class TestController extends BaseController{
    @Autowired
    private IMoneyLimitService moneyLimitService;

    @Resource
    private IUserDao userDao;

    @Resource
    private IUserBlackDao userBlackDao;

    @Resource
    private IUserContactsDao userContactsDao;



    @RequestMapping("saveUserClientInfo")
    public void testSaveRiskRecord() {
        moneyLimitService.testRiskRecord();
    }

    @RequestMapping(value = "manual-dealEd", method = RequestMethod.GET)
    public void dealEd(@RequestParam String userId, @RequestParam String mxRawUrl, @RequestParam String mxReportUrl, @RequestParam String DataHtmlUrl) {
        int user_id = Integer.parseInt(userId);
        User user = userDao.searchByUserid(user_id);//用户实体

        UserBlack userBlack = userBlackDao.findSelective(new HashMap<String, Object>() {{
            put("userPhone", user.getUserPhone());
        }});

        if (userBlack == null) {
            List<UserContacts> userContacts = userContactsDao.selectUserContacts(new HashMap<String, Object>(){{
                put("userId", user_id);
            }});//用户通讯录列表

            String model_name = PropertiesConfigUtil.get("ZHIMI_MODEL_NAME");
            int model_version_code = Integer.parseInt(model_name.substring(model_name.length() - 1));
            String apply_time = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
            String mobile = user.getUserName();
            String name = user.getRealname();
            String idcard = user.getIdNumber();
            String phone_os = "2".equals(user.getBrowerType()) ? "ios" : "android";
            List<ZhimiEmergencyContact> e_contacts = new ArrayList<ZhimiEmergencyContact>();
            e_contacts.add(new ZhimiEmergencyContact(user.getFirstContactName(), user.getFirstContactPhone()));
            e_contacts.add(new ZhimiEmergencyContact(user.getSecondContactName(), user.getSecondContactPhone()));

            String Authorization = "token " + PropertiesConfigUtil.get("MX_TOKEN");
            String gxb_report = null;
            String gxb_raw = null;
            String udcredit_portrait = null;
            try {
                gxb_report = ZhimiUtils.uncompress(HttpUtil.MxGet(mxReportUrl, Authorization));
                gxb_raw = ZhimiUtils.uncompress(HttpUtil.MxGet(mxRawUrl, Authorization));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_no", user.getIdNumber());
                if (model_version_code >= 4) {
                    udcredit_portrait = UdRequestUtils.dataservice(jsonObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, String> carrier_data = new HashMap<String, String>();
            carrier_data.put("mx_report", gxb_report);
            carrier_data.put("mx_raw", gxb_raw);

            List<ZhimiContact> contact = userContacts.stream().map(uc -> new ZhimiContact(uc.getContactName(), uc.getContactPhone(), DateUtil.formatDate(uc.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))).collect(Collectors.toList());

            ZhimiRiskRequest request = new ZhimiRiskRequest();
            request.setModel_name(model_name);
            request.setApply_time(apply_time);
            request.setMobile(mobile);
            request.setName(name);
            request.setIdcard(idcard);
            request.setPhone_os(phone_os);
            request.setCarrier_data(carrier_data);
            request.setE_contacts(e_contacts);
            request.setContact(contact);
            if (model_version_code >= 4) {
                request.setUdcredit_portrait(udcredit_portrait);
            }

            String requestStr = JSON.toJSONString(request, SerializerFeature.WriteMapNullValue);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://47.93.185.26/risk/gzip/");
            post.setEntity(new ByteArrayEntity(ZhimiUtils.gzip(requestStr)));
            try {
                HttpResponse response = client.execute(post);
                String responseStr = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

                System.out.println("指迷返回：" + responseStr);

                if (responseStr != null) {
                    JSONObject jsonObject = JSONObject.parseObject(responseStr);
                    RiskRecord riskRecord = new RiskRecord();
                    riskRecord.setRequestId(jsonObject.getString("request_id"));
                    riskRecord.setUserId(Integer.parseInt(userId));
                    riskRecord.setReturnCode(jsonObject.getInteger("return_code"));
                    riskRecord.setReturnInfo(jsonObject.getString("return_info"));
                    riskRecord.setGxbReportUrl(DataHtmlUrl);
                    riskRecord.setGxbToken(mxReportUrl);
                    riskRecord.setScore(jsonObject.getInteger("score"));
                    riskRecord.setCreateTime(new Date());
                    riskRecord.setHistoryApply(jsonObject.getString("history_apply"));
                    userDao.saveRiskRecord(riskRecord);

                    //原有的强风控回调储存改为同步得到结果判断结果，进行持久化
                    StrongRiskResult riskResult = new StrongRiskResult();
                    riskResult.setUserId(userId);
                    riskResult.setAmount("1600");
                    riskResult.setOrderNo("sr"+ DateUtil.formatDateNow("yyyyMMddHHmmssSSS")+ IdUtil.generateRandomStr(6));
                    Integer score = jsonObject.getInteger("score");
                    String result = score >= 530 ? (score > 560 ? "10" : "20") : "30";
                    riskResult.setResult(result);
                    riskResult.setRiskStatusType("px");
                    riskResult.setType("2");
                    riskResult.setConsumerNo(PropertiesConfigUtil.get("RISK_BUSINESS") + userId);
                    userDao.insertUserStrongRiskResult(riskResult);
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        } else {
            String no = "sr"+ DateUtil.formatDateNow("yyyyMMddHHmmssSSS")+ IdUtil.generateRandomStr(6);
            RiskRecord riskRecord = new RiskRecord();
            riskRecord.setRequestId(no);
            riskRecord.setUserId(Integer.parseInt(userId));
            riskRecord.setReturnCode(0);
            riskRecord.setReturnInfo("success");
            riskRecord.setGxbReportUrl(DataHtmlUrl);
            riskRecord.setGxbToken(mxReportUrl);
            riskRecord.setScore(0);
            riskRecord.setCreateTime(new Date());
            userDao.saveRiskRecord(riskRecord);

            StrongRiskResult riskResult = new StrongRiskResult();
            riskResult.setUserId(userId);
            riskResult.setAmount("1600");
            riskResult.setOrderNo(no);
            riskResult.setRiskStatusType("px");
            riskResult.setType("2");
            riskResult.setConsumerNo(PropertiesConfigUtil.get("RISK_BUSINESS") + userId);

            if (userBlack.getUserType() == 0) {//黑名单直接拒绝
                riskResult.setResult("30");
            } else {
                riskResult.setResult("20");//白名单进入初审列表
            }
            userDao.insertUserStrongRiskResult(riskResult);
        }
    }
}
