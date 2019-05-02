package com.vxianjin.gringotts.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 产生各种单号
 *
 * @author zhushuai
 */
public class GenerateNo {

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private static int index = 10;

    private static synchronized int nextIndx() {
        if (index > 999) {
            index = 10;
        }
        return index++;
    }

    public static String payRecordNo(String pre) {
        try {
            if (pre == null) {
                pre = "E";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = sdf.format(new Date());
            String randomString = String.valueOf(Math.random()).substring(2).substring(0, 6);

            return pre + dateString + randomString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String nextOrdId() {
        long time = System.currentTimeMillis();
        int end = new Random().nextInt(11);
        String str = String.valueOf(nextIndx()).concat(String.valueOf(time)).concat(String.valueOf(end));
        return str.length() == 16 ? str : (str.length() == 15 ? str + "1" : str.substring(0, 15));
    }

    public static String generateShortUuid(int num) {

        if (num <= 0) {
            return null;
        }
        try {
            StringBuilder builder = new StringBuilder();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            for (int i = 0; i < num; i++) {
                String str = uuid.substring(i, i + 4);
                int x = Integer.parseInt(str, 16);
                builder.append(chars[x % 0x3E]);
            }
            return builder.toString();
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }
}
