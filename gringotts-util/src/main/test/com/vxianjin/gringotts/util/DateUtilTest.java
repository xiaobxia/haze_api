package com.vxianjin.gringotts.util;

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
public class DateUtilTest {

    @Test
    public void safeThreadTest() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DateUtil.getSimpleDateFormat("yyyy-MM-dd");
                System.out.println("invoke from #" + Thread.currentThread().getId());
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                DateUtil.getSimpleDateFormat("yyyy-MM-dd");
                System.out.println("invoke from #" + Thread.currentThread().getId());
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                DateUtil.getSimpleDateFormat("yyyy-MM-dd");
                DateUtil.getSimpleDateFormat("yyyy-MM-dd");
                DateUtil.getSimpleDateFormat("yyyy-MM-dd");
                System.out.println("invoke from #" + Thread.currentThread().getId());
            }
        }.start();
    }

    @Test
    public void formatDate() {
        System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.formatDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
    }
}
