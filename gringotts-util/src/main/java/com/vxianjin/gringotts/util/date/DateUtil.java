package com.vxianjin.gringotts.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @author kiro
 */
public class DateUtil {

    private static final Object lockObj = new Object();
    private static ThreadLocal<HashMap<String, SimpleDateFormat>> threadLocal = new ThreadLocal<>();

    public static SimpleDateFormat getSimpleDateFormat(final String format) {
        HashMap<String, SimpleDateFormat> sdfMap = threadLocal.get();
        if (sdfMap == null) {
            synchronized (lockObj) {
                sdfMap = threadLocal.get();
                if (sdfMap == null) {
                    sdfMap = new HashMap<>();
                    sdfMap.put(format, new SimpleDateFormat(format));
                }
            }
        }
        return sdfMap.get(format);
    }
    public static Date getLoanEndTime(int offset){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,offset);
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static String formatDateNow(String format) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
        return simpleDateFormat.format(new Date());
    }

    public static String formatDate(Long millis, String format) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
        return simpleDateFormat.format(new Date(millis));
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date dateFormat(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(format);
        return simpleDateFormat.parse(date);
    }

    // --------------------------------------------------------------
    //
    //  即将废弃的工具方法
    //
    // --------------------------------------------------------------

    /**
     * Use {@link #formatDateNow(String)} instead of this.
     */
    @Deprecated
    public static String getDateFormat(String format) {
        return formatDateNow(format);
    }

    /**
     * Use {@link #formatDate(Date, String)} instead of this.
     */
    @Deprecated
    public static String getDateFormat(Date date, String format) {
        return formatDate(date, format);
    }

    /**
     * Use {@link CalendarUtil#shiftInDay(Date, int)} instead of this.
     */
    @Deprecated
    public static Date addDay(Date date, int offset) {
        return CalendarUtil.shiftInDay(date, offset);
    }

    /**
     * Use {@link #dateFormat(String, String)} instead of this.
     */
    @Deprecated
    public static Date getDate(String dateString, String format) {
        try {
            return dateFormat(dateString, format);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 若起始日期大于结束日期，则结果是负值，所以业务需要翻转结果。
     * 反之，则不用。
     *
     * @param s 起始日期
     * @param e 结束日期
     * @return boolean
     */
    private static boolean flip(Date s, Date e) {
        return s.after(e);
    }

    /**
     * Use {@link CalendarUtil#intervalInDay(Date, Date)} instead of this.
     */
//    @Deprecated
//    public static int daysBetween(Date s, Date e) {
//        if (flip(s, e)) {
//            return CalendarUtil.intervalInDay(s, e) * -1;
//        }
//        return CalendarUtil.intervalInDay(s, e);
//    }
//
//    /**
//     * Use {@link CalendarUtil#intervalInDay(Date, Date)} instead of this.
//     */
//    @Deprecated
//    public static int minutesBetween(Date s, Date e) {
//        if (flip(s, e)) {
//            return CalendarUtil.intervalInMinute(s, e) * -1;
//        }
//        return CalendarUtil.intervalInMinute(s, e);
//    }

    /**
     * Use {@link #formatDate(Date, String)} instead of this.
     */
    @Deprecated
    public static String fyFormatDate(Date date) {
        return formatDate(date, "yyyyMMdd");
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        }catch (Exception e){

        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差分钟数
     * @throws ParseException
     */
    public static int minutesBetween(Date smdate, Date bdate)
            throws ParseException {
        int seconds = (int) (bdate.getTime() - smdate.getTime()) / 1000;//除以1000是为了转换成秒
        return seconds / 60;
    }

}
