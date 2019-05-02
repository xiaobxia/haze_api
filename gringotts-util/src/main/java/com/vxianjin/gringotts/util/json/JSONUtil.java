package com.vxianjin.gringotts.util.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * JSON工具类
 */
public class JSONUtil {

    /**
     * json字符串转换成map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * JSON 对象字符格式转换成java对象
     *
     * @param jsonString jsonString
     * @param beanClass beanClass
     * @return t
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonString, Class<T> beanClass) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        return (T) JSONObject.toBean(jsonObject, beanClass);
    }

    /**
     * java对象转换成json字符串
     *
     * @param bean
     * @return
     */
    public static String beanToJson(Object bean) {
        JSONObject json = JSONObject.fromObject(bean);
        return json.toString();
    }

    /**
     * java对象List集合转换成json字符串
     *
     * @param beans
     * @return
     */

    @SuppressWarnings("rawtypes")
    public static String beanListToJson(List beans) {
        StringBuffer rest = new StringBuffer();
        rest.append("[");
        int size = beans.size();
        for (int i = 0; i < size; i++) {
            rest.append(beanToJson(beans.get(i)) + ((i < size - 1) ? "," : ""));
        }
        rest.append("]");
        return rest.toString();
    }

    /**
     * String 转List<T>
     *
     * @param <T> t
     * @param jsonArray  jsonArray
     * @param  beanClsss  beanClsss
     * @return t
     */
    public static <T> List<T> jsonArrayToBean(String jsonArray,
                                              Class<T> beanClsss) {
        JSONArray jsonArr = JSONArray.fromObject(jsonArray);
        List<T> beanList = new ArrayList<T>();
        for (int i = 0; i < jsonArr.size(); i++) {
            T bean = jsonToBean(String.valueOf(jsonArr.get(i)), beanClsss);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 输出json
     *
     * @param response res
     * @param json json
     */
//    @Deprecated
    public static void toObjectJson(HttpServletResponse response, String json) {
        try {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {


        }
    }

}
