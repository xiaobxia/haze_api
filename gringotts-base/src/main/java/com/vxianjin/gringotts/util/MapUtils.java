package com.vxianjin.gringotts.util;

import java.util.HashMap;


/**
 * 使用 {@link com.vxianjin.gringotts.base.Result<> 替代本类}
 *
 * @author gaoyuhai
 * 2016-11-18 下午06:02:52
 */
//@Deprecated
public class MapUtils {
    /**
     * 获取map
     *
     * @param code    0:成功  其他不成功
     * @param message message
     * @return map
     */
    public static HashMap<String, Object> getResultMap(String code, String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        return map;
    }

    /**
     * 获取map-error
     *
     * @param code    0:成功  其他不成功
     * @param message message
     * @return map
     */
    public static HashMap<String, Object> getResultMapByError(String code, String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        map.put("data", "{}");
        return map;
    }

}
