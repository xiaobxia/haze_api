package com.vxianjin.gringotts.pay.common.constants;

import com.vxianjin.gringotts.pay.common.util.fuiou.FuiouConfiguration;

/**
 * @Author: fully
 * @Date: 2019/5/4 14:40
 * @Description: 公共的常用变量,三方支付请求配置
 */
public class FuiouConstants {
    /**商户号 */
    public static final String API_MCHNT_CD = FuiouConfiguration.getInstance().getValue("api_mchnt_cd");
    /**商户密钥 */
    public static final String API_MCHNT_KEY = FuiouConfiguration.getInstance().getValue("api_mchnt_key");
    /**响应吗：成功 */
    public static final String RESP_CODE_SUCCESS = "0000";
    /**响应吗：已支付 */
    public static final String RESP_CODE_PAIED = "5185";

    public static final String FUIOU_PUB_KEY = FuiouConfiguration.getInstance().getValue("fuiou_pub_key");

    public static final String CHECK_ID_REQ_URL = FuiouConfiguration.getInstance().getValue("check_id_req_url");
    public static final String NEW_PROTOCOL_BINDMSG_URL = FuiouConfiguration.getInstance().getValue("new_protocol_bindmsg_url");
    public static final String NEW_PROTOCOL_BINDCOMMIT_URL = FuiouConfiguration.getInstance().getValue("new_protocol_bindcommit_url");
    public static final String NEW_PROTOCOL_ORDER_URL = FuiouConfiguration.getInstance().getValue("new_protocol_order_url");
    public static final String NEW_PROTOCOL_COMMIT_URL = FuiouConfiguration.getInstance().getValue("new_protocol_commit_url");
    public static final String NEW_PROTOCOL_SENDMSG_URL = FuiouConfiguration.getInstance().getValue("new_protocol_sendmsg_url");
    public static final String NEW_PROTOCOL_QUERY_URL = FuiouConfiguration.getInstance().getValue("new_protocol_query_url");
    public static final String NEW_PROTOCOL_QUERYLIST_URL = FuiouConfiguration.getInstance().getValue("new_protocol_querylist_url");
    public static final String NEW_PROTOCOL_UNBIND_URL = FuiouConfiguration.getInstance().getValue("new_protocol_unbind_url");

    public static String charset = "UTF-8";

    public static String mchntcd = FuiouConfiguration.getInstance().getValue("api_mchnt_cd");

    public static String key = FuiouConfiguration.getInstance().getValue("api_mchnt_key");

    public static String privatekey ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIbA52JWbirSYa2iTd/P7G6NGgOAAmgGFcTaktRVhHtgeeTHd24iT2MNTCIw/3ykcWu/55hbpHBcZIiLf/XZ940iaeSgIGmfoJa9xdVmZ5l4ElPUVtLJMntUfbPdAMP8SEwjMP8Nr6PvzjcKXS5GCfCuTW/F/dKz1mR1LOcxAkLBAgMBAAECgYBjkzBoLk4CPqwHTqQU+uRPXN0YMQOWMsjrSkittvPK56OrNuo97ASVwUG9Ek/4ntthL9HHeBCvJtbzP4Iy/fo6sevZVcaURNb3mn1R/gdIitwFur8bdF+VA5mZX8cTR4D4liZZvBHwx+UtvdWClzoOSeSpFZn7/6nMXpYzam3WQQJBALvXIHeAdPrtktmRtqmdVNYGqmgtE7jqkaqZ9VgUMcIt8W01oPEDp27NtmGTM06nneIk/ajagq97nsbc6JPa6PUCQQC3pm9RM782qnL/5fzNsv7HyTjFAlIg3Q+PNlSj1d3ekNlqRJ0hv4/aLiqrLqtqbfHu98aeGt4JsdilT/Z9rwMdAkEAlFgwFtBHEkh/Wf3ewRM0hZZcC8vVsIrnoVDXVskUBuNbsEDTKqQVHceuSl8C/RIY+Rj3jtuKq+W4HhsmPmZ65QJAbtbypG244ENreOrT80ou32Gg87Z83vzMoUDHQMKZT/TYY3zZ4T5+kc3/TqWyK2AD/photY+9pthByzRBroVsOQJAMUQ/c+Mngb8kKxU+mF/CwDSlwbL8/lM/xoDnT/qQxmxTiEysohd3jO98C0BA3+YHFswaXKtY7/Tp/H1VeX9EdA==";

    public static String signtp = "MD5";

    public static String backnotifyurl = FuiouConfiguration.getInstance().getValue("backnotifyurl");


}
