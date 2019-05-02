package com.vxianjin.gringotts.risk.utils;

import com.vxianjin.gringotts.constant.ConfigConstant;
import org.springframework.web.context.ContextLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 风控管理常量类 类描述： <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-12-11 下午02:16:35 <br>
 */
public class ConstantRisk {
    /**
     * 用户借款额度是a160,建议复审（额度不足）
     */
    public static final String ED_LESS = "k";
    /**
     * 用户借款额度为0
     */
    public static final String ED_ZERO = "b";
    /**
     * 老用户额度不足
     */
    public static final String ED_LESS_OLD = "c";
    /**
     * 命中反欺诈
     */
    public static final String ED_QZ = "e";
    /**
     * 疑似反欺诈
     */
    public static final String ED_QZ_LIKE = "g";
    /**
     * 情况未知
     */
    public static final String ED_UNKNOWN = "h";
    /**
     * 芝麻信用请求
     */
    public static final String ZMXY = "ZMXY";
    /**
     * 获得芝麻积分
     */
    public static final String GET_SCORE = "GET_SCORE";
    /**
     * 获得IVS信息
     */
    public static final String GET_IVS = "GET_IVS";
    /**
     * 获得行业关注度
     */
    public static final String GET_INDUSTY = "GET_INDUSTY";
    /**
     * 获得查询芝麻信用授权URL
     */
    public static final String GET_URL = "GET_URL";
    /**
     * 获得openId
     */
    public static final String GET_OPENID = "GET_OPENID";
    /**
     * 芝麻行业关注度逾期记录
     */
    public static final List<String> ZM_OVER = Arrays.asList("AA001");
    /**
     * 芝麻行业关注度逾期未支付
     */
    public static final List<String> ZM_OVER_NO_PAY = Arrays.asList("AD001",
            "AE001");
    /**
     * 同盾请求
     */
    public static final String TD = "TD";
    /**
     * 个人贷前审核,生成报告
     */
    public static final String TD_PRELOAN_APPLY = "TD_PRELOAN_APPLY";
    /**
     * 个人贷前查询 ，查询报告
     */
    public static final String TD_PRELOAN_REPORT = "TD_PRELOAN_REPORT";
    /**
     * 同盾手机号命高低风险关注名单的标识
     */
    public static final String TD_PHONE_BLACK = "手机号命中高风险关注名单";
    /**
     * 同盾身份证命高低风险关注名单的标识
     */
    public static final String TD_CARD_BLACK = "身份证命中高风险关注名单";
    /**
     * 同盾1个月内申请人在多个平台申请借款 的标识
     */
    public static final String TD_MONTH1_BLACK = "1个月内申请人在多个平台申请借款";
    /**
     * 同盾7天内申请人在多个平台申请借款 的标识
     */
    public static final String TD_DAY7_BLACK = "7天内申请人在多个平台申请借款";
    /**
     * 同盾1个月内身份证使用过多设备申请 的标识
     */
    public static final String TD_MONTH1_CARD_NUM_DEVICE_BORROW = "1个月内身份证使用过多设备申请";
    /**
     * 同盾1个月内设备使用多身份证或手机号申请的标识
     */
    public static final String TD_DAY7_DEVICE_CARD_OR_PHONE_BORROW = "7天内设备使用过多的身份证或手机号进行申请";
    /**
     * 同盾7天内身份证关联设备数的标识
     */
    public static final String TD_DAY7_CARD_DEVICE = "7天内身份证关联设备数";
    /**
     * 同盾3个月内申请信息关联多个身份证的标识
     */
    public static final String TD_MONTH3_APPLY_CARD = "3个月内申请信息关联多个身份证";
    /**
     * 同盾3个月内身份证关联多个申请信息的标识
     */
    public static final String TD_MONTH3_CARD_APPLY = "3个月内身份证关联多个申请信息";
    /**
     * 白骑士请求
     */
    public static final String BQS = "BQS";
    /**
     * 白骑士风险名单
     */
    public static final String BQS_RISK = "BQS_RISK";
    /**
     * 91征信请求
     */
    public static final String JYZX = "JYZX";
    /**
     * 91征信借贷信息
     */
    public static final String JYZX_BORROW = "JYZX_BORROW";
    /**
     * 密罐请求
     */
    public static final String MG = "MG";
    /**
     * 密罐黑名单信息
     */
    public static final String MG_BLACK = "MG_BLACK";
    /**
     * 聚信立请求
     */
    public static final String JXL = "JXL";
    /**
     * 聚信立查询报告(详情)信息
     */
    public static final String GET_USER_REPORT = "GET_USER_REPORT";
    /**
     * 聚信立分析数据信息
     */
    public static final String JXL_ANALYSIS = "JXL_ANALYSIS";
    /**
     * 获得聚信立凭条
     */
    public static final String GET_TOKEN = "GET_TOKEN";
    /**
     * 聚信立提交数据源采集请求
     */
    public static final String APPLY_COLLECT = "APPLY_COLLECT";
    /**
     * 宜信请求
     */
    public static final String YX = "YX";
    /**
     * 宜信借款信息
     */
    public static final String YX_BORROW = "YX_BORROW";
    /**
     * 提交手机验证码
     */
    public static final String SUBMIT_CAPTCHA = "SUBMIT_CAPTCHA";
    /**
     * 提交手机查询密码
     */
    public static final String SUBMIT_QUERYPASSWORD = "SUBMIT_QUERY_PWD";
    /**
     * 等待风控审核的借款申请对应的状态
     */
    public static final Integer RISK_STATUS_WAIT = 1;
    /**
     * 风控审核通过的状态
     */
    public static final Integer RISK_STATUS_SUC = 2;
    /**
     * 风控审核不通过的状态
     */
    public static final Integer RISK_STATUS_FAIL = 3;
    /**
     * 风控审核建议复审的状态(集合接口状态可判定是征信失败建议复审还是决策树建议复审)
     */
    public static final Integer RISK_STATUS_REVIEW = 4;
    /**
     * 接口成功的状态
     */
    public static final Integer INTERFACE_SUC = 2;
    public static final Map<String, String> JC_RESULT = new HashMap<String, String>();
    /**
     * 原本征信表的记录与借款申请一一对应，但现在需要对新用户计算借款额度额度，为保证数据统一性，插入的借款标识为0
     */
    public static final Integer NO_ID = 0;
    public static final String YX_KEY = ContextLoader.getCurrentWebApplicationContext().getServletContext()
            .getRealPath(ConfigConstant.getConstant("YX_KEY"));
    /**
     * 魔蝎请求
     */
    public static final String MX = "MX";
    /**
     * 魔蝎创建运营商采集请求
     */
    public static final String MX_COLLECT = "MX_COLLECT";
    /**
     * 新用户计算额度的标识
     */
    public static final String USER_MONEY = "USER_MONEY_";
    /**
     * 聚信立查询报告的标识
     */
    public static final String JXL_REPORT = "JXL_REPORT_";
    /**
     * 机审或人工审核借款信息的标识
     */
    public static final String REVIEW_BORROW = "REVIEW_BORROW_";
    /**
     * 缓存标识失效时间
     */
    public static final Integer FLAG_SECOND = 30;
    /**
     * 规则唯一标识前缀符
     */
    public static String RULE_PREFIX = "a";
    /**
     * 解析表达式，包括加、减、乘、除、大于、小于、等于、不等、并且、或者、(、)、{、}、if、else、return、'、;结束。
     */
    public static String REGEX = "[+\\-*/><()&|=!{}ifelsereturn;]";
    /**
     * 判断是否是数字、包括正、小、负数
     */
    public static String REGEX_NUM = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
    /**
     * 当表达式中出现返回结果时，必须以此字符串包裹
     */
    public static String RETURN_CHAR = "'";
    /**
     * 基础规则对应的集合
     */
    public static String BASE_RULE = "BASE_RULE";
    /**
     * 通过jexl表达式返回的字段
     */
    // public static String PASS_FORMULA = "'1'";
    public static String PASS_FORMULA = "1";
    /**
     * 包含中文的校验
     */
    public static Pattern PATTERN_CN = Pattern.compile("[\u4e00-\u9fa5]");
    /**
     * 所有规则对应的集合
     */
    public static String ALL_RULE = "ALL_RULE";
    /**
     * 所有基础规则和属性对应集合
     */
    public static String BASE_RULE_PROPERTY = "ALL_RULE_PROPERTY";
    /**
     * 准入规则根节点的key
     */
    public static String RULE_ZR = "RULE_ZR";

    static {
        JC_RESULT.put(ED_LESS, "额度不足，建议复审");
        JC_RESULT.put(ED_ZERO, "用户借款额度为0 ");
        JC_RESULT.put(ED_LESS_OLD, "老用户额度不足");
        JC_RESULT.put(ED_QZ, "命中反欺诈");
        JC_RESULT.put(ED_QZ_LIKE, "疑似反欺诈");
        JC_RESULT.put(ED_UNKNOWN, "未知情况");
    }
}
