package com.vxianjin.gringotts.util;

import org.apache.log4j.MDC;

/**
 * @Author: chenkai
 * @Date: 2018/8/8 17:24
 * @Description:
 */
public class TimeKey {
    public static final String KEY_PREFIX          = "【TimeKey=";
    public static final String KEY_SUFFIX          = "】";
//    spring boot 引入slueth可以使用traceId跟踪链路
//    public static final String KEY_TRACE_ID_PREFIX = "【TraceId=";
//    public static final String KEY_TRACE_ID_SUFFIX = "】";

    private TimeKey() {

    }

    public static void start() {
        MDC.remove("timeKey");
        MDC.put("timeKey",createTimeKey());
    }

    public static String createTimeKey() {
        String timeKey = KEY_PREFIX + UUIDGenerator.getUUIDSlip() + KEY_SUFFIX;
        return timeKey;
    }

    public static String getTimeKey() {
        return (String)MDC.get("timeKey");
    }

    public static void clear() {
        MDC.clear();
    }
}
