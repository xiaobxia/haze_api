package com.vxianjin.gringotts.web.util.cfcautil;

import com.vxianjin.gringotts.pay.common.util.PropertiesUtil;
import com.vxianjin.gringotts.util.security.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付令公用参数类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CfcaCommonUtil {


    /**
     * 类名
     */
    public static final String CHANNEL_NAME = CfcaCommonUtil.class.getSimpleName();
    /**
     * 默认编码格式utf-8
     */
    public static final String UTF8 = "UTF-8";
    /**
     * 编码格式gbk
     */
    public static final String GBK = "GBK";
    /**
     * CONTENT_TYPE json
     */
    public static final String CONTENT_TYPE_JSON = "application/json";
    /**
     * CONTENT_TYPE xml
     */
    public static final String CONTENT_TYPE_XML = "application/xml";
    /**
     * CONTENT_TYPE text/html
     */
    public static final String CONTENT_TYPE_HTML = "text/html";
    /**
     * CONTENT_TYPE zip
     */
    public static final String CONTENT_TYPE_ZIP = "application/zip";
    /**
     * result 成功
     */
    public static final String RET_SUCCESS = "SUCCESS";
    /**
     * result 錯誤
     */
    public static final String RET_ERROR = "ERROR";
    /**
     * result 失敗
     */
    public static final String RET_FAILURE = "FAILURE";
    /**
     * result 警告
     */
    public static final String RET_WARN = "WARN";
    /**
     * code 成功
     */
    public static final String CODE_SUCCESS = "000000";
    /**
     * code 失敗
     */
    public static final String CODE_FAILURE = "XXXXXX";
    /**
     * code other
     */
    public static final String CODE_OTHER = "999999";
    /**
     * code已存在
     */
    public static final String CODE_EXIST = "111111";

    /**
     * 主体类型 债权人 0
     */
    public static final String USER_DEPT_TYPE = "1";

    /**
     * 主体类型 债务人 1
     */
    public static final String USER_CUSTOMER_TYPE = "1";

    /**
     * 客户类型 企业 0
     */
    public static final String BUSINESS_DEPT_TYPE = "0";
    /**
     * 客户类型 个人 1
     */
    public static final String BUSINESS_CUSTOMER_TYPE = "0";

    /**
     * 文件上传请求URL
     */
    public static final String FILE_UPLOAD_REQ_URL = getProperties("file_url");
    /**
     * 借款人信息录入请求URL
     */
    public static final String CUSTOM_REQ_URL = getProperties("customer_url");
    /**
     * 生成合同请求URL
     */
    public static final String CONTRACT_REQ_URL = getProperties("contract_url");
    /**
     * 获取Token请求URL
     */
    public static final String TOKEN_REQ_URL = getProperties("token_url");
    /**
     * 合同签名与查看请求URL
     */
    public static final String SIGN_AND_VIEW_REQ_URL = getProperties("signandview_url");

    /**
     * 合同标题
     */
    public static final String TITLE = getProperties("contract_title");
    /**
     * 合同模板id
     */
    public static final String TEMPLATE_ID = getProperties("contract_templateId");
    /**
     * 合同签署地
     */
    public static final String SGIN_REGIN_NAME = getProperties("sginRegionName");
    /**
     * 合同管辖地
     */
    public static final String COURT_REGION_NAME = getProperties("courtRegionName");
    /**
     * 债权人id
     */
    public static final String USER_ID_TWO = getProperties("userIdTwo");
    /**
     * 债权人姓名
     */
    public static final String NAME_TWO = getProperties("nameTwo");
    /**
     * 债权人身份证
     */
    public static final String ID_CARD_TWO = getProperties("idCardTwo");
    /**
     * 债权人地址
     */
    public static final String ADDRESS_TWO = getProperties("addressTwo");
    /**
     * 居间方id
     */
    public static final String USER_ID_THREE = getProperties("userIdThree");
    /**
     * 居间方证件号
     */
    public static final String ID_CARD_THREE = getProperties("idCardThree");
    /**
     * 居间方名
     */
    public static final String NAME_THREE = getProperties("nameThree");

    /**
     * 文件上传临时目录路径
     */
    public static final String TMP_FILE_PATH = getProperties("tmppath");

    /**
     * 云证系统用户代码
     */
    public static final String USER_CODE = getProperties("usercode");

    /**
     * 云证系统提供的代码秘钥
     */
    public static final String USER_KEY = getProperties("userkey");

    /**
     * 借贷类型
     */
    public static final String TYPE_NAME = getProperties("type_name");

    /**
     * 查询合同签署状态
     */
    public static final String CONFIRM_URL = getProperties("confirm_url");

    /**
     * api host address
     */
    public static final String API = getProperties("api");
    /**
     * 年利率
     */
    public static final String RATE = getProperties("rate");

    /**
     * 借款用途
     */
    public static final String USAGE = getProperties("cantract_usage");
    /**
     * 逾期利率
     */
    public static final String OVERDUE_RATE = getProperties("overdue_rate");
    /**
     * 还款方式
     */
    public static final String REPAYMENT_METHOD = getProperties("repayment_method");
    /**
     * 还款期数
     */
    public static final String REPAYMENT_COUNT = getProperties("repayment_count");

    /**
     * 续借利息
     */
    public static final String AGAIN_RATE = getProperties("again_rate");

    /**
     * 同步回调URL
     */
    public static final String NOTIFY_URL = getProperties("notify_url");

    /**
     * 获取properties属性值
     *
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String getProperties(String key) {
        return PropertiesUtil.getValueByKey(key,
                PropertiesUtil.getConfigPath("thirdchannel", CHANNEL_NAME));
    }

    public static Map<String, String> getHeaderMap(String json) {
        String utime = String.valueOf(System.currentTimeMillis());
        String sign = getSign(utime, json);
        Map<String, String> map = new HashMap<String, String>();
        map.put("utime", utime);
        map.put("userCode", USER_CODE);
        map.put("sign", sign);
        return map;

    }

    public static String getSign(String utime, String json) {
        return MD5Util.MD5(USER_CODE + USER_KEY + utime + json);
    }
}
