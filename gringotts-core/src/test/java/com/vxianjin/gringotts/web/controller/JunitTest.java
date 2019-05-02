package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.util.security.AESUtil;
import com.vxianjin.gringotts.util.security.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

/**
 * @Author: kiro
 * @Date: 2018/7/23
 * @Description:
 */
@RunWith(JUnit4.class)
public class JunitTest {

    @Test
    public void test() {
        int uid = 125;
        int oid = 73655;
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);
        String onlineULH = "621c76126e4940e8d7de8b5cce65bf7c";
        String sign = MD5Util.MD5(AESUtil.encrypt(String.format("%d%d%s", uid, oid, uuid), onlineULH));
        System.out.println(sign);
        System.out.println(String.format("http://apitest.ulinghua.com/yeepay/withdraw/%d/%d/%s/%s", uid, oid, uuid, sign));
    }
}
