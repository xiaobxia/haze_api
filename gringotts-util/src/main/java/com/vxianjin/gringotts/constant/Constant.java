package com.vxianjin.gringotts.constant;


import java.util.HashMap;
import java.util.Map;

public class Constant {
    /*********************************************/
    /**
     * 默认密码
     */
    public static final String DEFAULT_PWD = "DEFAULT_PWD";
    /**
     * 下载模板默认密码
     */
    public static final String EXCEL_PWD = "EXCEL_PWD";
    /**
     * 命名空间编码
     */
    public static final String NAME_SPACE = "nameSpace";
    /*** UTF-8编码 */
    public static final String UTF8 = "UTF-8";
    /**
     * 后台请求路径
     */
    public final static String BACK_URL = "BACK_URL";
    /**
     * 后台session名
     */
    public static final String BACK_USER = "BACK_USER";
    /**
     * 后台超级管理员主键ID，该用户不用进行权限判断
     */
    public static final Integer ADMINISTRATOR_ID = 10000;
    public static final String ADMINISTRATOR_IDS = "10000,10027,10073";
    /**
     * 当前页数
     */
    public final static String CURRENT_PAGE = "pageNum";
    /**
     * 每页显示多少条
     */
    public static final String PAGE_SIZE = "numPerPage";
    /**
     * 系统参数中返回list时使用的key的后缀
     */
    public static final String SYS_CONFIG_LIST = "_LIST";
    /**
     * 判断是否是http开头
     */
    public static final String HTTP = "http://";
    /**
     * 判断是否是https开头
     */
    public static final String HTTPS = "https://";
    /**
     * 上传附件地址-core
     */
    public static final String FILEPATH_CORE = "mnt/img";
    /**
     * 上传附件地址-back
     */
    public static final String FILEPATH_BACK = "files";

    /**
     * 上传附件二维码地址
     */
    public static final String QR_FILEPATH = "QR_files";
    /**
     * 前端渠道 缓存
     */
    public static final String CHANNEL_INFO = "CHANNEL_INFO";

    /**
     * 前台用户缓存
     */
    public static final String FRONT_USER = "FRONT_USER";

    /**
     * 绑定手机号的验证码
     */
    public static final String SMS_BIND_PHONE = "SMS_BIND_PHONE";
    /**
     * 找回密码的验证码
     */
    public static final String SMS_RESET_PWD = "SMS_RESET_PWD";
    /**
     * 注册的验证码
     */
    public static final String SMS_REGISTER = "SMS_REGISTER";
    /**
     * 解绑银行卡的验证码
     */
    public static final String SMS_DEL_BANK = "SMS_DEL_BANK";
    /**
     * 设置交易密码的验证码
     */
    public static final String SMS_SET_PAY = "SMS_SET_PAY";
    /**
     * 修改手机号的时候原手机号的验证码
     */
    public static final String SMS_SET_PHONE_OLD = "SMS_SET_PHONE_OLD";
    /**
     * 修改手机号的时候新手机号的验证码
     */
    public static final String SMS_SET_PHONE_NEW = "SMS_SET_PHONE_NEW";
    /**
     * 前台用户发布需求的缓存标识
     */
    public static final String PUB_INFO = "PUB_NES_";
    /**
     * 前台用户信息点赞的缓存标识
     */
    public static final String PUB_INFO_LIKE = "PUB_NES_LIKE_";
    // *********************************爬虫相关**************************//
    /**
     * 爬取规则
     */
//    public static final String SCHEME = SpiderConstant.getConstant("scheme");
    /**
     * 爬取规则不可用或者是详情页的规则
     */
    public static final String SCHEME_STATUS_ZERO = "0";
    /**
     * 爬取规则可用或者网贷天眼的规则或者列表页
     */
    public static final String SCHEME_STATUS_ONE = "1";
    /**
     * 爬取规则：网贷之家的规则
     */
    public static final String SCHEME_STATUS_TWO = "2";
    /**
     * 爬取规则的列表页
     */
    public static final String SCHEME_TYPE_LIST = "1";
    /**
     * 爬取规则的详情页
     */
    public static final String SCHEME_TYPE_DETAIL = "0";
    /**
     * 爬取规则用到的属性-ID
     */
    public static final String SCHEME_ID = "id";
    /**
     * 爬取规则的属性-获得某个标签下非子标签的文本内容
     */
    public static final String SCHEME_OWNTEXT = "ownText";
    /**
     * 爬取规则的属性-样式
     */
    public static final String SCHEME_CLASS = "class";
    /**
     * 爬取规则的属性-属性
     */
    public static final String SCHEME_ATTR = "attributes";
    /**
     * 爬取规则的属性-html
     */
    public static final String SCHEME_HTML = "html";
    /**
     * 爬取规则的属性-text
     */
    public static final String SCHEME_TEXT = "text";
    /**
     * 爬取规则???
     */
    public static final String SCHEME_SELECT = "select";
    /**
     * 爬取规则的关键字
     */
    public static final String KEYWORD_MAP_KEY = "keyWord";
    /**
     * 爬取规则???
     */
    public static final String SOURCES_MAP_KEY = "sources";
    /**
     * 爬取链接-网贷天眼
     */
//    public static final String WDTY_LIST = SpiderConstant.getConstant("wdty_list");
    /**
     * 爬取链接-网贷之家
     */
//    public static final String WDZJ_LIST = SpiderConstant.getConstant("wdzj_list");
    /**
     * 短信类型：验证码类
     */
    public static final String VERIFY_CODE = "verify_code";
    /**
     * 短信类型：通知类类
     */
    public static final String NOTICE = "notice";
    /**
     * 短信类型：营销类
     */
    public static final String ADVERT = "advert";
    public static final String SMS_VERIFY_CONTENT = "尊敬的用户，本次验证码为#cont#请勿泄露，5分钟内有效！";
    public static final String SMS_SEND_SUCC = "0";
    /**
     * 状态
     */
    public static final String STATUS_VALID = "1";// 有效
    public static final Integer STATUS_INT_VALID = 1;// 有效
    public static final String STATUS_UN_VALID = "0";// 无效
    public static final Integer STATUS_INT_UN_VALID = 0;// 无效
    public static final int INDEX_LIMIT = 20;// 有效

    /**
     * index
     */
    public static final String CARD_VERIFY_STEP = "认证";

    public static final String CACHE_INDEX_KEY = "CASH_MAN_INDEX";// 默认缓存KEY
    public static final String CACHE_INDEX_INFO_KEY = "INDEX_INFO_";// 动态缓存KEY

    public static final int FOR_BASE = 10000;// 默认基数
    public static final int AMOUNT_MAX = 100000;// 默认最大金额
    public static final int AMOUNT_MIN = 20000;// 默认最小金额

    public static final double RATE_MIN = 0.098;// 默认7天-1000利率
    public static final double RATE_MAX = 0.15;// 默认14天1500利率

    public static final double CREDIT_RATE_MIN = 0.05;//默认7天-信审查询费
    public static final double CREDIT_RATE_MAX = 0.08;//默认14天-信审查询费

    public static final double ACCOUNT_MANAGE_RATE_MIN = 0.0438;//默认7天-账户管理费
    public static final double ACCOUNT_MANAGE_RATE_MAX = 0.063;//默认14天-账户管理费

    public static final double ACCRUAL_RATE_MIN = 0.0042;//默认7天-利息
    public static final double ACCRUAL_RATE_MAX = 0.007;//默认14天-利息

    public static final int DEFAULT_DAYS = 7;// 默认下次再申请的天数

    public static final int LOAN_DAYS_14 = 14;// 借款14天
    public static final int LOAN_DAYS_7 = 7;// 借款7天

    public static final String RENEWAL_MAX = "RENEWAL_MAX";// 最大申请续期次数
    public static final String COLLECTION_PATH = "COLLECTION_PATH";// 催收地址
    public static final String LUCKY_DRAW = "LUCKY_DRAW";// 抽奖接口地址

    public static final double XJX_RATE = 0.014;

    //诚安聚立
    public static final Integer CAJL_DEFAULT = 1; //未进行认证状态
    public static final Integer CAJL_FAIL = 2; //已认证但未通过
    public static final Integer CAJL_SUCCESS = 3; //认证通过，可借款

    public static final String BUTTON_MSG = "朕知道了";
    public static final String ALLOW_IP = "ALLOW_IP";
    /**
     * 魔蝎异步回调taskId的前缀(成功)
     */
    public static final String MX_CALL_BACK_SUC = "MX_CALL_BACK_SUC_";
    /**
     * 魔蝎异步回调taskId的前缀(失败)
     */
    public static final String MX_CALL_BACK_FAIL = "MX_CALL_BACK_FAIL_";

    public static final String XJX_AWARD_MONEY_URL = "http://www.xianjinxia.com:8086/xjx-platform/a/jsaward/awardCenterWeb/drawAwardIndexList";
    public static final Map<String, String> CARD_PROVINCE = new HashMap<String, String>();
    //芝麻反馈
    public static final Map<String, String> ZM_FEED_BACK_ORDER_STATUS = new HashMap<String, String>();
    /**
     * session id 的cookie name
     **/
    public static final String SESSION_ID = "JESSIONID_OSS";
    /**
     * 派发类型
     */
    public static final Integer ASSIGN_TYPE_SYSTEM = 0;//系统派发
    public static final Integer ASSIGN_TYPE_MANUAL = 1;//人工派发
    public static final Integer ROLE_CUSTOMER_SERVER = 10044;
    public static final Integer ROLE_CUSTOMER_SERVER_HEAD = 10043;
    /**
     * 用户备注字典
     */
    public static final String USER_REMARK = "user_remark";
    // 手机运营商手机号
    public static String JXL_PHONE_LT = "10010"; // 联通
    public static String JXL_PHONE_DX = "10001"; // 电信
    public static String JXL_PHONE_YD = "10086"; // 移动
    public static String JXL_FIND_MOBILE_BELONG_KEY = "1bbdbb9368470";
    //feedback
    public static String FEED_BACK_DATA_ID_TYPE = "0";//身份证
    public static String FEED_BACK_DATA_BIZ_TYPE = "1";//贷款

    static {
        CARD_PROVINCE.put("11", "北京市");
        CARD_PROVINCE.put("12", "天津市");
        CARD_PROVINCE.put("13", "河北省");
        CARD_PROVINCE.put("14", "山西省");
        CARD_PROVINCE.put("15", "内蒙古自治区");
        CARD_PROVINCE.put("21", "辽宁省");
        CARD_PROVINCE.put("22", "吉林省");
        CARD_PROVINCE.put("23", "黑龙江省");
        CARD_PROVINCE.put("31", "上海市");
        CARD_PROVINCE.put("32", "江苏省");
        CARD_PROVINCE.put("33", "浙江省");
        CARD_PROVINCE.put("34", "安徽省");
        CARD_PROVINCE.put("35", "福建省");
        CARD_PROVINCE.put("36", "江西省");
        CARD_PROVINCE.put("37", "山东省");
        CARD_PROVINCE.put("41", "河南省");
        CARD_PROVINCE.put("42", "湖北省");
        CARD_PROVINCE.put("43", "湖南省");
        CARD_PROVINCE.put("44", "广东省");
        CARD_PROVINCE.put("45", "广西壮族自治区");
        CARD_PROVINCE.put("46", "海南省");
        CARD_PROVINCE.put("50", "重庆市");
        CARD_PROVINCE.put("51", "四川省");
        CARD_PROVINCE.put("52", "贵州省");
        CARD_PROVINCE.put("53", "云南省");
        CARD_PROVINCE.put("54", "西藏自治区");
        CARD_PROVINCE.put("61", "陕西省");
        CARD_PROVINCE.put("62", "甘肃省");
        CARD_PROVINCE.put("63", "青海省");
        CARD_PROVINCE.put("64", "宁夏回族自治区");
        CARD_PROVINCE.put("65", "新疆维吾尔自治区");
        CARD_PROVINCE.put("71", "台湾省");
        CARD_PROVINCE.put("81", "香港特别行政区");
        CARD_PROVINCE.put("91", "澳门特别行政区");
    }

    static {
        ZM_FEED_BACK_ORDER_STATUS.put("0", "01");
        ZM_FEED_BACK_ORDER_STATUS.put("-3", "02");
        ZM_FEED_BACK_ORDER_STATUS.put("1", "01");
        ZM_FEED_BACK_ORDER_STATUS.put("-4", "02");
        ZM_FEED_BACK_ORDER_STATUS.put("20", "01");
        ZM_FEED_BACK_ORDER_STATUS.put("-5", "02");
        ZM_FEED_BACK_ORDER_STATUS.put("22", "04");
        ZM_FEED_BACK_ORDER_STATUS.put("-10", "03");
        ZM_FEED_BACK_ORDER_STATUS.put("21", "04");
        ZM_FEED_BACK_ORDER_STATUS.put("23", "04");
        ZM_FEED_BACK_ORDER_STATUS.put("30", "04");
        ZM_FEED_BACK_ORDER_STATUS.put("-11", "04");
        ZM_FEED_BACK_ORDER_STATUS.put("-20", "04");
        ZM_FEED_BACK_ORDER_STATUS.put("34", "04");
    }
}
