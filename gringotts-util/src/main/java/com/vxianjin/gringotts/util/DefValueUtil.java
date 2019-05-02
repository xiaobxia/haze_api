package com.vxianjin.gringotts.util;

/**
 * @author kiro
 */
public class DefValueUtil {

    @SuppressWarnings("unchecked")
    public static <T> T def(Object object, T defValue) {
        if (object == null) {
            return defValue;
        } else {
            try {
                return (T) object;
            } catch (Exception e) {
                return defValue;
            }
        }
    }
}
