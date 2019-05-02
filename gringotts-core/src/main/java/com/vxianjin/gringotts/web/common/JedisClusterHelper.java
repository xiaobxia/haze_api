package com.vxianjin.gringotts.web.common;

import redis.clients.jedis.JedisCluster;

/**
 * redis缓存帮助类
 * Created by jintian on 2018/7/17.
 */
public class JedisClusterHelper {

    public static Long checkForFront(JedisCluster jedisCluster, String key, String flag, int time) {
        String expireKey = key + flag;
        Long remainTime = jedisCluster.ttl(expireKey);
        if (remainTime <= 0) {
            jedisCluster.setnx(expireKey, "1");
            jedisCluster.expire(expireKey, time);
        }
        return remainTime;
    }
}
