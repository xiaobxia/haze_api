package com.vxianjin.gringotts.util.date;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理类
 *
 * @author tgy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CalendarUtil {

    /**
     * 示例：若日期为2018-07-03 12:00:00，若设置dayOffset=1，则返回结果为2018-07-04 12:00:00。
     *
     * @param dayOffset 天偏移量
     * @param date      日期
     * @return
     */
    public static Date plusDay4Date(int dayOffset, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dayOffset);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的00:00
     *
     * @param date 日期
     * @return
     */
    public static Date getIntraday0Hour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的次日的00:00
     *
     * @param date 日期
     * @return
     */
    public static Date getMorrow0Hour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(plusDay4Date(1, date));
        return getIntraday0Hour(calendar.getTime());
    }

    /**
     * 以天为单位进行偏移
     *
     * @param date   指定日期
     * @param offset 偏移量
     * @return
     */
    public static Date shiftInDay(Date date, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        return calendar.getTime();
    }

    /**
     * 两个日期之间的间隔天，会取两个日期中较大的日期并获取到当天凌晨的时间戳
     *
     * @return 间隔天数≥0
     */
    private static long intervalInMillis(Date s, Date e) {
        Date bigger = s.after(e) ? s : e;
        Date lesser = s.after(e) ? e : s;
        Date bigger0Hour = getIntraday0Hour(bigger);
        return Math.abs((bigger0Hour.getTime() - lesser.getTime()));
    }

    private static int intervalInUnit(Date s, Date e, Integer unit) {
        double value = intervalInMillis(s, e) / (unit * 1.0d);
        return (int) Math.ceil(value);
    }

    /**
     * 两个日期之间的间隔天
     *
     * @return 间隔天数≥0
     */
    public static int intervalInDay(Date s, Date e) {
        return intervalInUnit(s, e, 86400000);
    }

    public static int intervalInMinute(Date s, Date e) {
        return intervalInUnit(s, e, 60000);
    }

    public static int intervalInHour(Date s, Date e) {
        return intervalInUnit(s, e, 3600000);
    }
}
