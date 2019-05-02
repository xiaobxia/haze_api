package com.vxianjin.gringotts.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * Created by Phi on 2017/12/7.
 */
public class PhoneUtil {
    /**
     * 获取手机号码归属地
     *
     * @param phoneNumber
     * @return
     */
    public static String phoneLocation(String phoneNumber) throws NumberParseException {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        PhoneNumberOfflineGeocoder phoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();

        String language = "CN";
        Phonenumber.PhoneNumber referencePhonenumber = null;

        String phoneNum = phoneNumber;

        referencePhonenumber = phoneUtil.parse(phoneNum, language);

        //手机号码归属城市 referenceRegion
        String referenceRegion = phoneNumberOfflineGeocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);
        return referenceRegion;
    }

    /**
     * 判断传入的参数号码为哪家运营商
     *
     * @param mobile
     * @return 运营商名称
     */
    public static String phoneNetMark(String mobile) {
        String returnString = "";
        if (mobile == null || mobile.trim().length() != 11) {
            return "无效的手机号码";        //mobile参数为空或者手机号码长度不为11，错误！
        }
        if (mobile.trim().substring(0, 3).equals("134") || mobile.trim().substring(0, 3).equals("135") ||
                mobile.trim().substring(0, 3).equals("136") || mobile.trim().substring(0, 3).equals("137")
                || mobile.trim().substring(0, 3).equals("138") || mobile.trim().substring(0, 3).equals("139") || mobile.trim().substring(0, 3).equals("150") ||
                mobile.trim().substring(0, 3).equals("151") || mobile.trim().substring(0, 3).equals("152")
                || mobile.trim().substring(0, 3).equals("157") || mobile.trim().substring(0, 3).equals("158") || mobile.trim().substring(0, 3).equals("159")
                || mobile.trim().substring(0, 3).equals("187") || mobile.trim().substring(0, 3).equals("188")) {
            returnString = "中国移动";   //中国移动
        }
        if (mobile.trim().substring(0, 3).equals("130") || mobile.trim().substring(0, 3).equals("131") ||
                mobile.trim().substring(0, 3).equals("132") || mobile.trim().substring(0, 3).equals("156")
                || mobile.trim().substring(0, 3).equals("185") || mobile.trim().substring(0, 3).equals("186")) {
            returnString = "中国联通";   //中国联通
        }
        if (mobile.trim().substring(0, 3).equals("133") || mobile.trim().substring(0, 3).equals("153") ||
                mobile.trim().substring(0, 3).equals("180") || mobile.trim().substring(0, 3).equals("189")) {
            returnString = "中国电信";   //中国电信
        }
        if (returnString.trim().equals("")) {
            returnString = "未知运营商";   //未知运营商
        }
        return returnString;
    }

    public static void main(String[] arg) {
        PhoneUtil util = new PhoneUtil();
        System.out.println(phoneNetMark("13999889090"));
        System.out.println(phoneNetMark("13418170986"));
        System.out.println(phoneNetMark("15392496493"));
        System.out.println(phoneNetMark("13399889090"));
        System.out.println(phoneNetMark("17621226320"));
        System.out.println(phoneNetMark("erot4543545"));
        System.out.println(phoneNetMark("erot543545"));
    }
}
