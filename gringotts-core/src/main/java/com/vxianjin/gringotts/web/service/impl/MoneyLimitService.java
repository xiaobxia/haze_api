package com.vxianjin.gringotts.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vxianjin.gringotts.risk.dao.IRiskCreditUserDao;
import com.vxianjin.gringotts.risk.pojo.RiskCreditUser;
import com.vxianjin.gringotts.risk.utils.ConstantRisk;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.IdUtil;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.util.security.RsaUtil;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.dao.IUserContactsDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserContacts;
import com.vxianjin.gringotts.web.pojo.risk.*;
import com.vxianjin.gringotts.web.service.IMoneyLimitService;
import com.vxianjin.gringotts.web.utils.ZhimiUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MoneyLimitService implements IMoneyLimitService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private IRiskCreditUserDao riskCreditUserDao;
    @Resource
    private IUserDao userDao;
    @Resource
    private IUserContactsDao userContactsDao;
    @Resource
    private IBorrowOrderDao borrowOrderDao;

    private String appId = "gxb79554ed8a02eeb28";
    private String appSecret = "da1855416a844cdba918800b9b0f1dc3";

    @Override
    public void dealEd(String userId) {
        logger.info(" MoneyLimitService dealEd start");
        logger.info(" MoneyLimitService dealEd userId = " + userId);
        User user = userDao.searchByUserid(Integer.valueOf(userId));
        Map<String,Object> map = new HashMap<>();
        RiskInfo riskInfo = new RiskInfo();
        riskInfo.setChannel(user.getUserFrom());
        riskInfo.setScene("22");
        riskInfo.setNotifyUrl(PropertiesConfigUtil.get("APP_HOST_API")+PropertiesConfigUtil.get("STRONG_RISK_CALL_BACK"));
        map.put("riskInfo", JSON.toJSONString(riskInfo));
        map.put("orderNo","sr"+ DateUtil.formatDateNow("yyyyMMddHHmmssSSS")+ IdUtil.generateRandomStr(6));
        map.put("consumerNo",PropertiesConfigUtil.get("RISK_BUSINESS")+userId);
        map.put("type",0);
        map.put("event","ALL");
        RiskUserInfo riskUserInfo = new RiskUserInfo();
        String privateKey = PropertiesConfigUtil.get("RISK_RSA_PRIVATE_kEY");
        riskUserInfo.setIdNo(RsaUtil.encrypt(privateKey,user.getIdNumber()));
        riskUserInfo.setPhone(RsaUtil.encrypt(privateKey,user.getUserPhone()));
        riskUserInfo.setRealName(RsaUtil.encrypt(privateKey,user.getRealname()));
        map.put("userInfo",JSON.toJSONString(riskUserInfo));
        logger.info("request risk params:{}",JSON.toJSONString(map));
        String result = HttpUtil.postForm(PropertiesConfigUtil.get("risk_approve_url"),map);
        logger.info("request risk result:{}",result);

//        riskInfo:{"channel":"fx","notifyUrl":"http://papi.letto8.cn/third/risk/registerStrongRisk","scene":"22","reqExt":""}  强风控策略
//        orderNo:srxRI7PG00041000000001  订单号
//        consumerNo:ad158   首字母小写+id
//        type:0  类型 0 小额现金贷 1 白领贷 2 消费贷
//        event:ALL    事件或类型插入修改等信息
//        userInfo:{"realName":"","phone":"","idNo":""}  用户信息，

        logger.info(" MoneyLimitService dealEd end");
    }

    @Override
    public void dealEd(String userId, String gxbToken) {
        logger.info(" MoneyLimitService dealEd start");
        logger.info(" MoneyLimitService dealEd userId = " + userId);

        int user_id = Integer.parseInt(userId);
        User user = userDao.searchByUserid(user_id);//用户实体
        List<UserContacts> userContacts = userContactsDao.selectUserContacts(new HashMap<String, Object>(){{
            put("userId", user_id);
        }});//用户通讯录列表

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", user_id);
        paramMap.put("status", BorrowOrder.STATUS_DCS);
        BorrowOrder borrowOrder = borrowOrderDao.selectBorrowByParams(paramMap);

        String model_name = "taoqianbao_v1";
        String apply_time = DateUtil.formatDate(borrowOrder.getCreatedAt(),"yyyy-MM-dd HH:mm:ss");
        String mobile = user.getUserName();
        String name = user.getRealname();
        String idcard = user.getIdNumber();
        String phone_os = "1".equals(borrowOrder.getClientType()) ? "ios" : "android";
        List<ZhimiEmergencyContact> e_contacts = new ArrayList<ZhimiEmergencyContact>();
        e_contacts.add(new ZhimiEmergencyContact(user.getFirstContactName(), user.getFirstContactPhone()));
        e_contacts.add(new ZhimiEmergencyContact(user.getSecondContactName(), user.getSecondContactPhone()));

        String GXBREPORT = "https://prod.gxb.io/crawler/data/report/%s?appId=%s&timestamp=%s&sign=%s";
        String GXBRAWDATA = "https://prod.gxb.io/crawler/data/rawdata/%s?appId=%s&timestamp=%s&sign=%s";
        String timestamp = new Date().getTime()+"";
        String md5Hex = DigestUtils.md5Hex(String.format("%s%s%s", this.appId, this.appSecret, timestamp));
        String reportUrl = String.format(GXBREPORT, gxbToken, this.appId, timestamp, md5Hex);
        String rawDataUrl = String.format(GXBRAWDATA, gxbToken, this.appId, timestamp, md5Hex);
        String gxb_report = HttpUtil.post(reportUrl, null);
        String gxb_raw = HttpUtil.post(rawDataUrl, null);

        Map<String, String> carrier_data = new HashMap<String, String>();
        carrier_data.put("gxb_report", gxb_report);
        carrier_data.put("gxb_raw", gxb_raw);

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

        String requestStr = JSON.toJSONString(request, SerializerFeature.WriteMapNullValue);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://47.93.185.26/risk/gzip/");
        post.setEntity(new ByteArrayEntity(ZhimiUtils.gzip(requestStr)));
        try {
            HttpResponse response = client.execute(post);
            String responseStr = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

            if (responseStr != null) {
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                RiskRecord riskRecord = new RiskRecord();
                riskRecord.setRequestId(jsonObject.getString("request_id"));
                riskRecord.setUserId(Integer.parseInt(userId));
                riskRecord.setReturnCode(jsonObject.getInteger("return_code"));
                riskRecord.setReturnInfo(jsonObject.getString("return_info"));
                riskRecord.setScore(jsonObject.getInteger("score"));
                riskRecord.setCreateTime(new Date());
                userDao.saveRiskRecord(riskRecord);


                StrongRiskResult riskResult = new StrongRiskResult();
                riskResult.setUserId(userId);
                riskResult.setAmount("1000");
                riskResult.setOrderNo("sr"+ DateUtil.formatDateNow("yyyyMMddHHmmssSSS")+ IdUtil.generateRandomStr(6));
                riskResult.setResult(jsonObject.getInteger("score") > 560 ? "10" : "30");
                riskResult.setRiskStatusType("px");
                riskResult.setType("0");
                riskResult.setConsumerNo(PropertiesConfigUtil.get("RISK_BUSINESS") + userId);
                userDao.insertUserStrongRiskResult(riskResult);

            }
        } catch(IOException e){
            e.printStackTrace();
        }
        logger.info(" MoneyLimitService dealEd end");
    }

    @Override
    public void testRiskRecord(){
        String userId="1";
        String responseStr="{\"return_code\":0,\"return_info\": 'success',\"request_id\": '20181218191308-e6566a56-02b5-11e9-80e2-00163e06bcb2', \"score\":541}";
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        RiskRecord riskRecord = new RiskRecord();
        riskRecord.setRequestId(jsonObject.getString("request_id"));
        riskRecord.setUserId( Integer.parseInt(userId));
        riskRecord.setReturnCode(jsonObject.getInteger("return_code"));
        riskRecord.setReturnInfo(jsonObject.getString("return_info"));
        riskRecord.setScore(jsonObject.getInteger("score"));
        riskRecord.setCreateTime(new Date());
        userDao.saveRiskRecord(riskRecord);
    }

    private void updateBorrowMoney(RiskCreditUser riskCreditUser2) {
        try {
            RiskCreditUser tmp = new RiskCreditUser();
            tmp.setUserId(riskCreditUser2.getUserId());//用户ID
            tmp.setAssetId(0);//
            //查询某个用户有没有辅助计算额度的记录 表：risk_credit_user user_id=#{userId} and asset_id=#{assetId}
            Integer id = riskCreditUserDao.findOneCal(tmp);
            logger.info("在risk_credit_user中是否有用户数据是，此处的id是否为空" + id);
            if (id == null) {
                // 查询数据库没有已计算记录则插入
                riskCreditUser2.setAssetId(ConstantRisk.NO_ID);
                riskCreditUser2.setLastDays(-1);
                riskCreditUser2.setRiskStatus(5);
                riskCreditUserDao.insertCalMoney(riskCreditUser2);
                id = riskCreditUser2.getId();
            }
        } catch (Exception e) {
            logger.error("updateBorrowMoney error riskCreditUser2=" + riskCreditUser2.toString(), e);
        }
    }

}

