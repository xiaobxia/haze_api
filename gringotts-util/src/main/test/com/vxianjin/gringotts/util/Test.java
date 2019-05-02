package com.vxianjin.gringotts.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: kiro
 * @Date: 2018/7/6
 * @Description:
 */
public class Test {

    public static void main(String[] args) {
        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("2");
        for (String temp : a) {
            if ("2".equals(temp)) {
                a.remove(temp);
            }
        }
        System.out.println(Arrays.toString(a.toArray()));
    }
}
