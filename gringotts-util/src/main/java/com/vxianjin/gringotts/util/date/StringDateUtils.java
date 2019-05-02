package com.vxianjin.gringotts.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用String表示Date的工具类
 * Created by Phi on 2017/7/27.
 */
@Deprecated
public class StringDateUtils {

    public static void main(String[] args) {
        String lastDay = getLastDay("yyyy-MM-dd", 2017, 12);
        System.out.println();
    }

    /**
     * 获取某年某月最后一天
     *
     * @param sdfString
     * @param year
     * @param month
     * @return
     */
    public static String getLastDay(String sdfString, int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //防止不足31号的月出现bug
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(sdfString);
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    public static long dayMinusDay(String sdfString, String stringDate1, String stringDate2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfString);
        //跨年不会出现问题
        //如果时间为：2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 0
        Date fDate = sdf.parse(stringDate1);
        Date oDate = sdf.parse(stringDate2);
        long days = (oDate.getTime() - fDate.getTime()) / (1000 * 3600 * 24);
        return days;
    }

    /**
     * 日期加天数 减天数为负数
     *
     * @param sdfString  时间格式
     * @param stringDate 时间字符串
     * @param days       天数 加天数为正数 减天数为负数
     * @return
     */
    public static String daysAdd(String sdfString, String stringDate, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfString);
        String res = "";
        try {
            Date sdfDate = sdf.parse(stringDate);
            res = sdf.format(daysAddDays(sdfDate, days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 两时间相同返回true
     *
     * @param sdfString 时间格式
     * @param first     第一个时间
     * @param second    第二个时间
     * @return
     */
    public static boolean equalsFlag(String sdfString, String first, String second) {
        Date f = stringToDate(sdfString, first);
        Date s = stringToDate(sdfString, second);
        return f.equals(s);
    }

    /**
     * 前者时间大返回true
     *
     * @param sdfString 时间格式
     * @param first     第一个时间
     * @param second    第二个时间
     * @return
     */
    public static boolean afterFlag(String sdfString, String first, String second) {
        Date f = stringToDate(sdfString, first);
        Date s = stringToDate(sdfString, second);
        return f.after(s);
    }

    /**
     * 前者时间小返回true
     *
     * @param sdfString 时间格式
     * @param first     第一个时间
     * @param second    第二个时间
     * @return
     */
    public static boolean beforeFlag(String sdfString, String first, String second) {
        Date f = stringToDate(sdfString, first);
        Date s = stringToDate(sdfString, second);
        return f.before(s);
    }

    /**
     * 前者时间大返回：1，时间小返回：-1，相同返回：0
     *
     * @param sdfString 时间格式
     * @param first     第一个时间
     * @param second    第二个时间
     * @return
     */
    public static int compare(String sdfString, String first, String second) {
        Date f = stringToDate(sdfString, first);
        Date s = stringToDate(sdfString, second);
        //前者时间较大
        if (f.after(s)) {
            return 1;
        }
        //前者时间较小
        if (f.before(s)) {
            return -1;
        }
        //相同
        return 0;
    }

    /**
     * 时间转字符串
     *
     * @param sdfString 时间格式
     * @param date      时间
     * @return
     */
    public static String dateToString(String sdfString, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfString);
        return sdf.format(date);
    }

    /**
     * 字符串转时间
     *
     * @param sdfString 时间格式
     * @param s         时间字符串
     * @return
     */
    public static Date stringToDate(String sdfString, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfString);
        Date res = new Date();
        try {
            res = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static Date daysAddDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, days);
            return calendar.getTime();
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 字符串类型日期之间的格式转化
     *
     * @param strDate      字符串类型日期
     * @param sourceFormat 源格式
     * @param targetFormat 目标格式
     * @return
     */
    public static String strDateToStrDate(String strDate, String sourceFormat, String targetFormat) {
        Date tmp = stringToDate(sourceFormat, strDate);
        String target = dateToString(targetFormat, tmp);
        return target;
    }

//    public static void main(String[] args) {
//        String sdf = "yyyy年MM月dd日";
//
//        String resA = daysAdd(sdf,"2017年07月27日",6);
//        String resB = daysAdd(sdf,"2017年07月27日",-6);
//
//        boolean a = beforeFlag(sdf,"2017年07月27日","2017年07月28日");
//        boolean b = beforeFlag(sdf,"2017年07月29日","2017年07月28日");
//        boolean c = beforeFlag(sdf,"2017年07月27日","2017年07月27日");
//
//        boolean aa = afterFlag(sdf,"2017年07月27日","2017年07月28日");
//        boolean bb = afterFlag(sdf,"2017年07月29日","2017年07月28日");
//        boolean cc = afterFlag(sdf,"2017年07月27日","2017年07月27日");
//
//        boolean aaaa = equalsFlag(sdf,"2017年07月27日","2017年07月28日");
//        boolean bbbb = equalsFlag(sdf,"2017年07月29日","2017年07月28日");
//        boolean cccc = equalsFlag(sdf,"2017年07月27日","2017年07月27日");
//
//        int aaa = compare(sdf,"2017年07月27日","2017年07月28日");
//        int bbb = compare(sdf,"2017年07月29日","2017年07月28日");
//        int ccc = compare(sdf,"2017年07月27日","2017年07月27日");
//
//        System.out.println();
//    }
}
