package com.vxianjin.gringotts.util;



import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.util.security.MD5Util;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class TokenUtil {

    public static String token(TreeMap<String, String> params, String security) {
        if (StringUtils.isEmpty(security) || ArrayUtil.isEmpty(params)) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (Objects.equals(entry.getKey(), "_at")) {
                continue;
            }
            if (Objects.equals(entry.getKey(), "_sign")) {
                continue;
            }
            if (Objects.equals(entry.getKey(), "_")) {
                continue;
            }
            if (Objects.equals(entry.getKey(), "callback")) {
                continue;
            }
            sb.append(entry.getKey());
            sb.append(entry.getValue());
        }
        String md5 = MD5Util.md5(sb.toString());
        return MD5Util.md5(md5 + security);

    }

    public static TreeMap<String, String> getParameterMap(JSONObject  params) {
        TreeMap<String, String> returnMap = new TreeMap<String, String>();
        Iterator<?> entries = params.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

}
