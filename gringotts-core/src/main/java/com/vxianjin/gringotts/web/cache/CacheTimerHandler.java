package com.vxianjin.gringotts.web.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 缓存操作类，对缓存进行管理,清除方式采用Timer定时的方式
 *
 * @author：gaoyuhai
 */
@Deprecated
public class CacheTimerHandler {
    private static final int DEFUALT_VALIDITY_TIME = 30000;// 默认过期时间
    private static final Timer timer;
    private static final Map<String, CacheEntity> map = new ConcurrentHashMap<>();
    private static Logger loger = LoggerFactory.getLogger(CacheTimerHandler.class);

    static {
        timer = new Timer();
    }

    /**
     * 增加缓存对象
     *
     * @param key
     * @param CacheEntity
     */
    public static void addCache(String key, CacheEntity ce) {
        addCache(key, ce, DEFUALT_VALIDITY_TIME);
        // map.put(key, ce);
    }

    /**
     * 增加缓存对象
     *
     * @param key
     * @param CacheEntity
     * @param validityTime 有效时间 (秒)
     */
    public static synchronized void addCache(String key, CacheEntity ce,
                                             int validityTime) {
        map.put(key, ce);
        // 添加过期定时
        timer.schedule(new TimeoutTimerTask(key), validityTime * 1000);
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @return
     */
    public static synchronized CacheEntity getCache(String key) {
        return map.get(key);
    }

    /**
     * 检查是否含有制定key的缓冲
     *
     * @param key
     * @return
     */
    public static synchronized boolean isConcurrent(String key) {
        return map.containsKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static synchronized void removeCache(String key) {
        map.remove(key);
    }

    /**
     * 获取缓存大小
     *
     * @param key
     */
    public static int getCacheSize() {
        return map.size();
    }

    /**
     * 清除全部缓存
     */
    public static synchronized void clearCache() {
        if (null != timer) {
            timer.cancel();
        }
        map.clear();
//		System.out.println("clear cache");
    }

    /**
     * 清除超时缓存定时服务类
     */
    static class TimeoutTimerTask extends TimerTask {
        private String ceKey;

        public TimeoutTimerTask(String key) {
            this.ceKey = key;
        }

        @Override
        public void run() {
            CacheTimerHandler.removeCache(ceKey);
            loger.info("------------------------remove:" + ceKey);
        }
    }
}
