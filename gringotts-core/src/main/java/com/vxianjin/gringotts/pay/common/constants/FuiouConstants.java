package com.vxianjin.gringotts.pay.common.constants;

import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouConfiguration;

/**
 * @Author: fully
 * @Date: 2019/5/4 14:40
 * @Description: 公共的常用变量,三方支付请求配置
 */
public class FuiouConstants {
    /**商户号 */
    public static final String API_MCHNT_CD = "0002900F0096235";//FuiouConfiguration.getInstance().getValue("apimchntcd");
    /**商户密钥 */
    public static final String API_MCHNT_KEY = "5old71wihg2tqjug9kkpxnhx9hiujoqj";//FuiouConfiguration.getInstance().getValue("apimchntkey");

    public static final String PAYFORREQ = "payforreq";

    public static final String SINCOMEFORREQ = "sincomeforreq";

    /**响应吗：成功 */
    public static final String RESP_CODE_SUCCESS = "0000";
    /**响应吗：已支付 */
    public static final String RESP_CODE_PAIED = "5185";

    public static final String FUIOU_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBCcvUDkw3ONsVx7Rzh9IJoKKurwBnKSjJEJbLXQWDKIPZMtmxcHa5jNu6OgpQ0BatOYl4p4BmgH3HzVwWyn6iDOsDlxwZezFzArtPjtECq241nfmoGhbz9lMr7T56yY5PhATws32Dm1ZQbY8DvsFvTe2hKgmIGbZQ030seRnfSwIDAQAB";//FuiouConfiguration.getInstance().getValue("fuioupubkey");

    public static final String CHECK_ID_REQ_URL = "http://www-1.fuiou.com:18670/mobile_pay/checkInfo/checkIdentity.pay";//FuiouConfiguration.getInstance().getValue("checkidrequrl");
    public static final String NEW_PROTOCOL_BINDMSG_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindMsg.pay";//FuiouConfiguration.getInstance().getValue("newprotocolbindmsgurl");
    public static final String NEW_PROTOCOL_BINDCOMMIT_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindCommit.pay";//FuiouConfiguration.getInstance().getValue("newprotocolbindcommiturl");
    public static final String NEW_PROTOCOL_ORDER_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/order.pay";//FuiouConfiguration.getInstance().getValue("newprotocolorderurl");
    public static final String NEW_PROTOCOL_COMMIT_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/commit.pay";//FuiouConfiguration.getInstance().getValue("newprotocolcommiturl");
    public static final String NEW_PROTOCOL_SENDMSG_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/sendMsg.pay";//FuiouConfiguration.getInstance().getValue("newprotocolsendmsgurl");
    public static final String NEW_PROTOCOL_QUERY_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindQuery.pay";//FuiouConfiguration.getInstance().getValue("newprotocolqueryurl");
    public static final String NEW_PROTOCOL_QUERYLIST_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindQueryList.pay";//FuiouConfiguration.getInstance().getValue("newprotocolquerylisturl");
    public static final String NEW_PROTOCOL_UNBIND_URL = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/unbind.pay";//FuiouConfiguration.getInstance().getValue("newprotocolunbindurl");
    public static final String NEW_PROTOCOL_CHECKRESULT_URL = "http://www-1.fuiou.com:18670/mobile_pay/checkInfo/checkResult.pay";//FuiouConfiguration.getInstance().getValue("newprotocolcheckResulturl");

    public static final String PAYFORREQ_SINCOMEFORREQ_URL = "https://fht-test.fuioupay.com/fuMer/req.do";//FuiouConfiguration.getInstance().getValue("payforreqsincomeforrequrl");

    public static String charset = "UTF-8";

    public static String privatekey ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIbA52JWbirSYa2iTd/P7G6NGgOAAmgGFcTaktRVhHtgeeTHd24iT2MNTCIw/3ykcWu/55hbpHBcZIiLf/XZ940iaeSgIGmfoJa9xdVmZ5l4ElPUVtLJMntUfbPdAMP8SEwjMP8Nr6PvzjcKXS5GCfCuTW/F/dKz1mR1LOcxAkLBAgMBAAECgYBjkzBoLk4CPqwHTqQU+uRPXN0YMQOWMsjrSkittvPK56OrNuo97ASVwUG9Ek/4ntthL9HHeBCvJtbzP4Iy/fo6sevZVcaURNb3mn1R/gdIitwFur8bdF+VA5mZX8cTR4D4liZZvBHwx+UtvdWClzoOSeSpFZn7/6nMXpYzam3WQQJBALvXIHeAdPrtktmRtqmdVNYGqmgtE7jqkaqZ9VgUMcIt8W01oPEDp27NtmGTM06nneIk/ajagq97nsbc6JPa6PUCQQC3pm9RM782qnL/5fzNsv7HyTjFAlIg3Q+PNlSj1d3ekNlqRJ0hv4/aLiqrLqtqbfHu98aeGt4JsdilT/Z9rwMdAkEAlFgwFtBHEkh/Wf3ewRM0hZZcC8vVsIrnoVDXVskUBuNbsEDTKqQVHceuSl8C/RIY+Rj3jtuKq+W4HhsmPmZ65QJAbtbypG244ENreOrT80ou32Gg87Z83vzMoUDHQMKZT/TYY3zZ4T5+kc3/TqWyK2AD/photY+9pthByzRBroVsOQJAMUQ/c+Mngb8kKxU+mF/CwDSlwbL8/lM/xoDnT/qQxmxTiEysohd3jO98C0BA3+YHFswaXKtY7/Tp/H1VeX9EdA==";

    public static String signtp = "MD5";

    public static String backnotifyurl = "";//FuiouConfiguration.getInstance().getValue("backnotifyurl");


}
