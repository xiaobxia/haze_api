package com.vxianjin.gringotts.web.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

/**
 * gson工具类
 *
 * @author wuxj
 */
public class GsonUtil {

    /**
     * @param isSerializeNulls      不处理null字段
     * @param isDisableHtmlEscaping 不处理url地址特殊符号
     * @return
     */
    private static Gson getInstants(boolean isSerializeNulls, boolean isDisableHtmlEscaping) {
        GsonBuilder builder = new GsonBuilder();

        if (isSerializeNulls) {
            builder.serializeNulls();
        }

        if (isSerializeNulls) {
            builder.disableHtmlEscaping();
        }

        //格式化金额
        builder.registerTypeAdapter(Double.class, new DoubleTypeAdapter()).disableHtmlEscaping();

        Gson gson = builder.create();
        return gson;
    }

    /**
     * 对象转换成json
     *
     * @param bean
     * @return
     */
    public static String toJson(Object bean) {
        Gson gson = getInstants(false, false);
        return gson.toJson(bean);
    }

    /**
     * 对象转换成json
     *
     * @param bean
     * @param isSerializeNulls
     * @param isDisableHtmlEscaping
     * @return
     */
    public static String toJson(Object bean, boolean isSerializeNulls, boolean isDisableHtmlEscaping) {
        Gson gson = getInstants(isSerializeNulls, isDisableHtmlEscaping);
        return gson.toJson(bean);
    }

    /**
     * 对象转换成json
     *
     * @param bean
     * @param type
     * @return
     */
    public static String toJson(Object bean, Type type) {
        Gson gson = getInstants(false, false);
        return gson.toJson(bean, type);
    }

    /**
     * 转换成json对象
     *
     * @param <T>
     * @param json
     * @param type
     * @return T 返回类型
     * @Title: fromJson
     * @throws：
     */
    public static <T> T fromJson(String json, Type type) {
        Gson gson = getInstants(false, false);
        return gson.fromJson(json, type);
    }

    /**
     * 转换成json对象
     *
     * @param <T>
     * @param json
     * @param classOfT
     * @return T 返回类型
     * @Title: fromJson
     * @throws：
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson = getInstants(false, false);
        return gson.fromJson(json, classOfT);
    }
}

/**
 * 格式化金额类
 *
 * @author wuxj
 */
class DoubleTypeAdapter implements JsonSerializer<Double> {
    @Override
    public JsonElement serialize(Double d, Type type, JsonSerializationContext context) {
        DecimalFormat format = new DecimalFormat("##0.00");
        String temp = format.format(d);
        JsonPrimitive pri = new JsonPrimitive(temp);
        return pri;
    }

}
