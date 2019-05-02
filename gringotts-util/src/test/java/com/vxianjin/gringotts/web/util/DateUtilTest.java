package com.vxianjin.gringotts.web.util;

import com.vxianjin.gringotts.util.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.util.Date;

/**
 * @Author: kiro
 * @Date: 2018/7/18
 * @Description:
 */
@RunWith(JUnit4.class)
public class DateUtilTest {

    @Test
    public void daysBetween() throws ParseException {
        Date originalDate = DateUtil.dateFormat("2018-07-18 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date destinationDate = DateUtil.dateFormat("2018-07-25 00:00:00", "yyyy-MM-dd HH:mm:ss");
        int intervalDay = DateUtil.daysBetween(originalDate, destinationDate);
        Assert.assertEquals(7, intervalDay);
        print(originalDate, destinationDate, intervalDay);

        originalDate = DateUtil.dateFormat("2018-07-18 23:59:59", "yyyy-MM-dd HH:mm:ss");
        intervalDay = DateUtil.daysBetween(originalDate, destinationDate);
        Assert.assertEquals(7, intervalDay);
        print(originalDate, destinationDate, intervalDay);

        originalDate = DateUtil.dateFormat("2018-07-19 00:00:00", "yyyy-MM-dd HH:mm:ss");
        intervalDay = DateUtil.daysBetween(originalDate, destinationDate);
        Assert.assertEquals(6, intervalDay);
        print(originalDate, destinationDate, intervalDay);


        originalDate = DateUtil.dateFormat("2018-07-19 09:59:59", "yyyy-MM-dd HH:mm:ss");
        destinationDate = DateUtil.dateFormat("2018-07-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
        intervalDay = DateUtil.daysBetween(originalDate, destinationDate);
        Assert.assertEquals(6, intervalDay);
        print(originalDate, destinationDate, intervalDay);


        originalDate = DateUtil.dateFormat("2018-07-19 10:00:00", "yyyy-MM-dd HH:mm:ss");
        destinationDate = DateUtil.dateFormat("2018-07-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
        intervalDay = DateUtil.daysBetween(originalDate, destinationDate);
        Assert.assertEquals(6, intervalDay);
        print(originalDate, destinationDate, intervalDay);

        originalDate = DateUtil.dateFormat("2018-07-19 10:00:01", "yyyy-MM-dd HH:mm:ss");
        destinationDate = DateUtil.dateFormat("2018-07-25 10:00:00", "yyyy-MM-dd HH:mm:ss");
        intervalDay = DateUtil.daysBetween(originalDate, destinationDate);
        Assert.assertEquals(6, intervalDay);
        print(originalDate, destinationDate, intervalDay);

    }

    private void print(Date a, Date b, int interval) {
        String format = "yyyy-MM-dd HH:mm:ss";
        System.out.println(String.format("%s 与 %s 差 %d 天",
                DateUtil.formatDate(a, format),
                DateUtil.formatDate(b, format),
                interval));
    }

    @Test
    public void testDayBetweenFlip() throws ParseException {
        Date date = DateUtil.dateFormat("2018-07-23 15:48:30", "yyyy-MM-dd HH:mm:ss");
        int interval = DateUtil.daysBetween(date, new Date());
        System.out.println("大，小:" + interval);
        interval = DateUtil.daysBetween(new Date(), date);
        System.out.println("小，大:" + interval);
    }
}
