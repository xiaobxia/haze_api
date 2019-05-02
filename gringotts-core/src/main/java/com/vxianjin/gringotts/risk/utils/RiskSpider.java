package com.vxianjin.gringotts.risk.utils;

import com.vxianjin.gringotts.constant.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class RiskSpider {
    public static RiskSpider riskSpider;
    Logger logger = LoggerFactory.getLogger(getClass());

    public static RiskSpider getInstance() {
        if (riskSpider == null) {
            riskSpider = new RiskSpider();
        }
        return riskSpider;
    }

    public static void main(String[] args) {
        System.out.println(new Date(1482495300000l));
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("cookieKey", "ALIPAYJSESSIONID");
//		map.put("cookieValue", "RZ05vRCbVmjbQUVSlcMgL5VC3bZa8oauthRZ05GZ00");
//		map.put("url", "https://my.alipay.com/portal/i.htm");
//		map.put("select", "div.amount-des strong.amount:eq(0)");
//		map.put("infoKey", "text");
//		System.out.println(RiskSpider.getInstance().spiderValue(map));
    }

    /**
     * 获取html及text内容
     *
     * @param ele
     * @param infoKey
     * @return
     */
    public String getTextAndHtml(Element ele, String infoKey) {
        if (Constant.SCHEME_TEXT.equals(infoKey)) {
            return ele.text();
        } else if (Constant.SCHEME_OWNTEXT.equals(infoKey)) {
            return ele.ownText();
        } else {
            return ele.html();
        }

    }

    /**
     * 爬取单个值
     *
     * @param params cookieKey cookie所需要的cookie的key<br>
     *               cookieValue cookie所需要的cookie的value<br>
     *               timeOut 超时时间，不传递默认为5秒<br>
     *               url要爬取的url<br>
     *               select能够唯一定位到目标的查询条件<br>
     *               infoKey要获取目标元素的text、ownText、html等<br>
     * @return
     */
    public String spiderValue(Map<String, String> params) {
        String result = null;
        try {
            Map<String, String> map = new HashMap<String, String>();
            String cookieKey = params.get("cookieKey");
            String cookieValue = params.get("cookieValue");
            map.put(cookieKey, cookieValue);
            int timeOut = 5000;
            if (params.containsKey("timeOut")) {
                timeOut = Integer.valueOf(params.get("timeOut"));
            }
//			logger.info("post alipay " + params);
            Document doc = Jsoup
                    .connect(params.get("url"))
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .cookies(map).timeout(timeOut).get();
//			logger.info("post alipay return doc=" + doc);
            String select = params.get("select");
            Element element = doc.select(select).first();
//			logger.info("post alipay return element=" + element);
            result = getTextAndHtml(element, params.get("infoKey"));
        } catch (Exception e) {
            logger.error("spiderValue error params=" + params, e);
        }
        return result;
    }
}
