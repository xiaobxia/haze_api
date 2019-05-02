package com.vxianjin.gringotts.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @Author: kiro
 * @Date: 2018/7/3
 * @Description:
 */
@RunWith(JUnit4.class)
public class GenerateNoTest {

    @Test
    public void payRecordNo() {
        System.out.println(GenerateNo.payRecordNo("1"));
    }

    @Test
    public void nextOrdId() {
        System.out.println(GenerateNo.nextOrdId());
    }

    @Test
    public void generateShortUuid() {
        System.out.println(GenerateNo.generateShortUuid(-10));
        System.out.println(GenerateNo.generateShortUuid(0));
        System.out.println(GenerateNo.generateShortUuid(1));
        System.out.println(GenerateNo.generateShortUuid(4));
        System.out.println(GenerateNo.generateShortUuid(8));
        System.out.println(GenerateNo.generateShortUuid(16));
        System.out.println(GenerateNo.generateShortUuid(28));
        System.out.println(GenerateNo.generateShortUuid(29));
        System.out.println(GenerateNo.generateShortUuid(30));
    }
}
