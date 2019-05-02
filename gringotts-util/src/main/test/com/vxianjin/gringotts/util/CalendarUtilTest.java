package com.vxianjin.gringotts.util;

import com.vxianjin.gringotts.util.date.CalendarUtil;
import com.vxianjin.gringotts.util.date.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

/**
 * @Author: kiro
 * @Date: 2018/7/3
 * @Description:
 */
@RunWith(JUnit4.class)
public class CalendarUtilTest {

    private static final Date date = DateUtil.getDate("2018-07-01 12:00:00", "yyyy-MM-dd HH:mm:ss");

    @Test
    public void plusDay4Date() {
        assert CalendarUtil.plusDay4Date(0, date).getTime() == date.getTime() : "day offset is 0";
        assert DateUtil.getDateFormat(CalendarUtil.plusDay4Date(1, date), "yyyy-MM-dd HH:mm:ss")
                .equals("2018-07-02 12:00:00") : "day offset is 1";
        assert DateUtil.getDateFormat(CalendarUtil.plusDay4Date(-1, date), "yyyy-MM-dd HH:mm:ss")
                .equals("2018-06-30 12:00:00") : "day offset is -1";
    }

    @Test
    public void getIntraday0Hour() {
        assert CalendarUtil.getIntraday0Hour(date).equals(DateUtil.getDate("2018-07-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        assert CalendarUtil.getMorrow0Hour(date).equals(DateUtil.getDate("2018-07-02 00:00:00", "yyyy-MM-dd HH:mm:ss"));
    }
}
