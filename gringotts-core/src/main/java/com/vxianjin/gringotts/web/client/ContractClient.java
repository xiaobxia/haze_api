package com.vxianjin.gringotts.web.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IYunFaTongApiService;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * 生成合同接口类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class ContractClient extends BaseClient {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractClient.class);
    /**
     * 请求类型
     */
    private static final Integer reqType = 3;


    /**
     * 与云证交互通信接口并返回结果
     *
     * @param contract
     * @param userInfo
     * @return
     */
    public String generateContract(CfcaContractInfo contract, CfcaUserInfo userInfo) throws IOException {

        String jsonValue = buildParams(contract, userInfo);
        Map<String, String> headerMap = CfcaCommonUtil.getHeaderMap(jsonValue);

        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongApiService.class);
        Response<JSONObject> response = service.contractCreate(headerMap,
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonValue)).execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                saveHttpMessage(jsonValue, response.body().toJSONString(), reqType, userInfo.getUser().getId());
                return response.body().toJSONString();
            } else {
                saveHttpMessage(jsonValue, null, reqType, userInfo.getUser().getId());
            }
        } else {
            saveHttpMessage(jsonValue, null, reqType, userInfo.getUser().getId());
        }
        return null;
    }

    /**
     * 构建json发送对象类
     *
     * @param contract
     * @param userInfo
     * @return
     */
    private String buildParams(CfcaContractInfo contract, CfcaUserInfo userInfo) {

        String title = contract.getTitle();
        String templateId = contract.getTemplateId();
        String sginRegionName = contract.getSginRegionName();
        String courtRegionName = contract.getCourtRegionName();
        String typeName = contract.getTypeName();

        JSONObject json = new JSONObject();
        json.put("contractNo", contract.getContractNo());
        json.put("title", StringUtils.isEmpty(title) ? CfcaCommonUtil.TITLE : title);
        json.put("templateId", StringUtils.isEmpty(templateId) ? CfcaCommonUtil.TEMPLATE_ID : templateId);
        json.put("sginRegionName", StringUtils.isEmpty(sginRegionName) ? CfcaCommonUtil.SGIN_REGIN_NAME : sginRegionName);
        json.put("courtRegionName", StringUtils.isEmpty(courtRegionName) ? CfcaCommonUtil.COURT_REGION_NAME : courtRegionName);
        json.put("typeName", StringUtils.isEmpty(typeName) ? CfcaCommonUtil.TYPE_NAME : typeName);
        json.put("contractPrice", contract.getContractPrice());
        json.put("linkUrl", contract.getLinkedUrl());
        JSONArray signJson = buildSignJson(contract, userInfo);
        json.put("signJson", signJson);
        JSONObject contractJson = buildContractJson(contract, userInfo);
        json.put("contractJson", contractJson);

        contract.setSignJson(signJson.toJSONString());
        contract.setContractJson(contractJson.toJSONString());

        return json.toJSONString();
    }

    /**
     * 构建签署人信息
     *
     * @param contract
     * @param userInfo
     * @return
     */
    private JSONArray buildSignJson(CfcaContractInfo contract, CfcaUserInfo userInfo) {

        //模板信息是否存了债权人信息，若空则使用公共的
        String userIdTwo = contract.getTemplate().getUserIdTwo();
        String userIdThree = contract.getTemplate().getUserIdThree();

        if (StringUtils.isEmpty(userIdTwo)) {
            userIdTwo = CfcaCommonUtil.USER_ID_TWO;
        }
        if (StringUtils.isEmpty(userIdThree)) {
            userIdThree = CfcaCommonUtil.USER_ID_THREE;
        }
        JSONArray signArray = new JSONArray();
        JSONObject signAJson = new JSONObject();
        //合同签署用户 a为第一个用户 b为第二个用户 c为第三个用户
        signAJson.put("signatory", "a");
        signAJson.put("userid", userInfo.getCfcaUserId());
        signArray.add(signAJson);
        JSONObject signBJson = new JSONObject();
        signBJson.put("signatory", "b");
        signBJson.put("userid", userIdTwo);
        signArray.add(signBJson);
        JSONObject signCJson = new JSONObject();
        signCJson.put("signatory", "c");
        signCJson.put("userid", userIdThree);
        signArray.add(signCJson);

        return signArray;
    }

    /**
     * 根据合同模板构建合同信息
     *
     * @param contract
     * @param userInfo
     * @return
     */
    private JSONObject buildContractJson(CfcaContractInfo contract, CfcaUserInfo userInfo) {

        CfcaContractTemplate template = contract.getTemplate();
        User user = userInfo.getUser();
        UserCardInfo cardInfo = userInfo.getCardInfo();

        contract.setUserId(user.getId());
        contract.setCfcaUserId(userInfo.getCfcaUserId());

        //合同信息 根据模板获取，后期可能变更
        JSONObject contractJson = new JSONObject();
        //合同编号
        contractJson.put("${contract_no}", contract.getContractNo());
        //借款用途
        contractJson.put("${contract_use}", template.getUseage());//
        //本金金额人民币
        contractJson.put("${contract_price}", template.getPrice());
        //大写人民币
        contractJson.put("${contract_price_upper}", template.getPriceUpper());
        //还款金额
        contractJson.put("${contract_repayment_price}", template.getRepaymentPrice());
        //综合服务费
        contractJson.put("${contract_server_price}", template.getServerPrice());
        //年利率
        contractJson.put("${contract_rate}", template.getRate());
        //逾期利率
        contractJson.put("${default_money}", template.getOverdueRate());

        //还款方式
        contractJson.put("${repayment_method}", template.getRepaymentMethod());
        //期数
        contractJson.put("${repayment_count}", template.getRepaymentCount());

        //甲方姓名
        contractJson.put("${a_user_name}", user.getRealname());
        //甲方证件号
        contractJson.put("${a_idcard_no}", user.getIdNumber());

        if (cardInfo != null) {
            String cardNo = cardInfo.getCard_no();
            //甲方帐号
            contractJson.put("${a_accounts}", StringUtils.isNotEmpty(cardNo) ? cardNo : "无");
        } else {
            //无银行卡号传无
            contractJson.put("${a_accounts}", "无");
        }

        //甲方地址
        contractJson.put("${a_address}", user.getPresentAddressDistinct().concat(user.getPresentAddress()));
        //甲方手机号码
        contractJson.put("${a_mobile}", user.getUserPhone());
        //甲方QQ号
        contractJson.put("${a_qq}", user.getQq());
        //甲方邮箱
        contractJson.put("${a_email}", user.getEmail());
        //甲方紧急联系电话
        contractJson.put("${a_emergency_mobile}", user.getFirstContactPhone());

        //乙方姓名
        String nameb = template.getNameTwo();
        contractJson.put("${b_user_name}", StringUtils.isEmpty(nameb) ? CfcaCommonUtil.NAME_TWO : nameb);
        //乙方证件号
        String idCardNob = template.getIdCardTwo();
        contractJson.put("${b_idcard_no}", StringUtils.isEmpty(idCardNob) ? CfcaCommonUtil.ID_CARD_TWO : idCardNob);
        //乙方联系地址
        String addressb = template.getAddressTwo();
        contractJson.put("${b_address}", StringUtils.isEmpty(addressb) ? CfcaCommonUtil.ADDRESS_TWO : addressb);
        //丙方名称
        String namec = template.getNameThree();
        contractJson.put("${c_user_name}", StringUtils.isEmpty(namec) ? CfcaCommonUtil.NAME_THREE : namec);
        //丙方名称
        //contractJson.put("${ent_name}", StringUtils.isEmpty(namec) ? CfcaCommonUtil.NAME_THREE : namec);

        //丙方证件号
        String idCardIdc = template.getIdCardThree();
        contractJson.put("${c_idcard_no}", StringUtils.isEmpty(idCardIdc) ? CfcaCommonUtil.ID_CARD_THREE : idCardIdc);
        //借款起息日
        contractJson.put("${contract_start_time}", template.getStartTime());
        //借款到期日
        contractJson.put("${contract_end_time}", template.getEndTime());
        //还款日
        contractJson.put("${repayment_date}", template.getRepaymentDate());
        //创建日期
        contractJson.put("${create_time}", template.getCreateTime());
        //续借利息
        contractJson.put("${again_rate}", template.getAgainRate());

        return contractJson;

    }

    @Override
    public Logger getLOGGER() {
        return LOGGER;
    }
}
