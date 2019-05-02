package com.vxianjin.gringotts.util;

/**
 * Created by giozola on 2018/10/19.
 */
public class VersionUtil {

    /**
     * 版本比较
     * 当v1 > v2  返回 1
     * v1 = v2  返回 0
     * v1 < v2 返回 -1
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Integer versionCompare(String v1, String v2) {
        Integer result = -1;
        String[] v1s = v1.split("\\.");
        String[] v2s = v2.split("\\.");
        Integer length = v1s.length;
        //获取版本号中格式较长的版本
        if (length < v2s.length) {
            length = v2s.length;
        }
        for (int i = 0; i < length; i++) {
            //长度判断
            int number1 = 0;
            int number2 = 0;
            if (v1s.length > i){
                number1 = Integer.parseInt(v1s[i]);
            }
            if (v2s.length > i){
                number2 = Integer.parseInt(v2s[i]);
            }
            //比较
            if (number1 > number2) {
                result = 1;
                break;
            } else if (number1 < number2) {
                break;
            } else if (number1 == number2) {
                if (i == length - 1) {
                    result = 0;
                }
                continue;
            }
        }
        return result;
    }
}
