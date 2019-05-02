package com.vxianjin.gringotts.web.listener;

import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.MapUtils;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.pojo.InfoImage;
import com.vxianjin.gringotts.web.pojo.InfoIndex;
import com.vxianjin.gringotts.web.pojo.InfoNotice;
import com.vxianjin.gringotts.web.pojo.index.IndexDto;
import com.vxianjin.gringotts.web.service.IIndexService;
import com.vxianjin.gringotts.web.util.IndexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;
import redis.clients.jedis.JedisCluster;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 首页init
 *
 * @author gaoyuhai 2016-12-10 下午05:23:32
 */
public class IndexInit implements ServletContextListener {

    private static Logger loger = LoggerFactory.getLogger(IndexInit.class);
    private List<Object> starters = new ArrayList<Object>();

    public IndexInit() {
        addStarter(new SystemConfigStarter());

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
        IIndexService indexService = (IIndexService) ctx.getBean("indexService");
        JedisCluster jedisCluster = (JedisCluster) ctx.getBean("jedisCluster");
        initIndex(indexService, jedisCluster);
        Iterator<Object> it = starters.iterator();
        while (it.hasNext()) {
            Starter s = (Starter) it.next();
            s.init(arg0.getServletContext());
        }
    }

    /**
     * 初始化index
     *
     * @param indexService
     */
    private void initIndex(IIndexService indexService, JedisCluster jedisCluster) {
        loger.info("******************************************************************************");
        loger.info("**                     ...start index init...                               **");
        loger.info("******************************************************************************");

        HashMap<String, Object> resultMap = MapUtils.getResultMap("0", "访问首页成功");
        HashMap<String, Object> dataMap = new HashMap<String, Object>();// 存放数据

        // 2 现金侠 100000 100000 10000 4 5 1120 10 7,14 9800 15000 0.098,0.15
        // www.baidu.com www.baidu1.com www.baidu2.com 1
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("NOT_SELECT", Constant.STATUS_VALID);
        //表：info_index
        InfoIndex index = indexService.searchInfoIndex(map);// 查询首页内容

        List<InfoNotice> noticeList = null;
        List<InfoImage> imageList = null;

        if (null != index) {
            HashMap<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("card_title", index.getCard_title());
            itemMap.put("card_amount", index.getCard_amount());
            itemMap.put("card_verify_step", Constant.CARD_VERIFY_STEP + index.getAuth_min() + "/" + index.getAuth_max());
            itemMap.put("verify_loan_pass", IndexUtil.getVerifyLoanPass(index.getAuth_min(), index.getAuth_max()));
            itemMap.put("verify_loan_nums", index.getAuth_min());
            dataMap.put("item", itemMap);

            HashMap<String, Object> amountDaysListMap = new HashMap<String, Object>();

            amountDaysListMap.put("days", IndexUtil.getDays(index));// 借款日期

            amountDaysListMap.put("interests", IndexUtil.getInterests(index));// 到账金额

            amountDaysListMap.put("amounts", IndexUtil.getAmounts(index));// 金额列表

            amountDaysListMap.put("creditVet", IndexUtil.getCreditVetDays(index));//信审查询费
            amountDaysListMap.put("accountManage", IndexUtil.getAccountManageDays(index));//账户管理费
            amountDaysListMap.put("accrual", IndexUtil.getAccrualDays(index));//利息
            amountDaysListMap.put("platformUse", IndexUtil.getPlatformUse(index));//平台使用费
            amountDaysListMap.put("collectionChannel", IndexUtil.getCollectionChannel(index));//代收通道费


            dataMap.put("amount_days_list", amountDaysListMap);

            Integer noticeSize = null;
            try {
                noticeSize = Integer.parseInt(index.getNotice_size());
            } catch (Exception e) {

            }
            if (noticeSize == null) {
                noticeSize = Constant.INDEX_LIMIT;
            }

            map.put("COUNT_XJX", noticeSize);
            noticeList = indexService.searchInfoNoticeByIndex(map);// 查询公告
            loger.info("noticeList size = " + noticeList.size());
            dataMap.put("user_loan_log_list", IndexUtil.getUserLoanLogList(noticeList));

            map.put("STATUS", Constant.STATUS_VALID);
            imageList = indexService.searchInfoImage(map);//查询图片
            dataMap.put("index_images", IndexUtil.getInfoImageList(imageList));

            dataMap.put("today_last_amount", index.getToday_last_amount());

            resultMap.put("data", dataMap);

            String indexJson = "";
            try {
                indexJson = JSONUtil.beanToJson(resultMap);
                IndexDto indexDto = new IndexDto(indexJson, Constant.STATUS_VALID);
                indexService.saveIndexDto(indexDto);
                jedisCluster.set(Constant.CACHE_INDEX_KEY, indexJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            loger.info("JSONUtil.beanToJson(resultMap):" + indexJson);
        } else {
            loger.error("-----------------INDEX-ERROR-ISNULL-----------------");
        }
        loger.info("******************************************************************************");
        loger.info("**                     ...start index end...                                **");
        loger.info("******************************************************************************");
    }

    void addStarter(Starter startup) {
        starters.add(startup);
    }

    /**
     * 更新所有缓存
     */
    public void updateCache() {
        ServletContext ctx = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        Iterator<Object> it = starters.iterator();
        while (it.hasNext()) {
            Starter s = (Starter) it.next();
            s.init(ctx);
        }
    }

    public void updateRiskCache() {
        ServletContext ctx = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        Iterator<Object> it = starters.iterator();
        while (it.hasNext()) {
            Starter s = (Starter) it.next();
            s.risk(ctx);
        }
    }
}
