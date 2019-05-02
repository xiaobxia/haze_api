package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.show.pojo.IndexNum;
import com.vxianjin.gringotts.show.pojo.Province;
import com.vxianjin.gringotts.show.pojo.UserShow;
import com.vxianjin.gringotts.show.service.IShowService;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("show/")
public class ShowController extends BaseController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IShowService showService;

    /**
     * 全国地图屏幕上方的四个数据和用户分析页面右边的两个数据
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("findIndexNum")
    public void findIndexNum(HttpServletRequest request, HttpServletResponse response, Model model) {
        Integer personTotal = 0;
        Integer waitPerson = 0;
        Integer borrowSucToday = 0;
        Integer borrowTotalToday = 0;
        Integer borrowTodayCnt = 0;
        BigDecimal borrowTotal = BigDecimal.ZERO;
        BigDecimal borrowToday = BigDecimal.ZERO;
        BigDecimal waitMoney = BigDecimal.ZERO;
        Integer xqTodayCnt = 0;
        BigDecimal xqTodayMoney = BigDecimal.ZERO;
        Integer xqTotalCnt = 0;
        BigDecimal xqTotalMoney = BigDecimal.ZERO;
        IndexNum indexNum = null;
        String call = request.getParameter("callback");
        try {
            long a = System.currentTimeMillis();
            List<HashMap<String, Object>> list = null;//showService.findIndexNum();
            if (list != null && list.size() > 0) {
                HashMap<String, Object> map0 = list.get(0);
                personTotal = Integer.valueOf(map0.get("cnt") + "");
                borrowTotal = new BigDecimal(map0.get("sm") + "").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                HashMap<String, Object> map1 = list.get(1);
                waitPerson = Integer.valueOf(map1.get("cnt") + "");
                waitMoney = new BigDecimal(map1.get("sm") + "").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                HashMap<String, Object> map2 = list.get(2);
                borrowTodayCnt = Integer.valueOf(map2.get("cnt") + "");
                borrowToday = new BigDecimal(map2.get("sm") + "").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                HashMap<String, Object> map3 = list.get(3);
                borrowTotalToday = Integer.valueOf(map3.get("cnt") + "");
                HashMap<String, Object> map4 = list.get(4);
                borrowSucToday = Integer.valueOf(map4.get("cnt") + "");
                HashMap<String, Object> map5 = list.get(5);
                xqTodayCnt = Integer.valueOf(map5.get("cnt") + "");
                xqTodayMoney = new BigDecimal(map5.get("sm") + "").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                HashMap<String, Object> map6 = list.get(6);
                xqTotalCnt = Integer.valueOf(map6.get("cnt") + "");
                xqTotalMoney = new BigDecimal(map6.get("sm") + "").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                borrowTodayCnt += xqTodayCnt;
                borrowSucToday += xqTodayCnt;
                borrowToday = borrowToday.add(xqTodayMoney);
                personTotal += xqTotalCnt;
                borrowTotal = borrowTotal.add(xqTotalMoney);
            }
            long b = System.currentTimeMillis();
            System.out.println("findIndexNum======" + (b - a));
        } catch (Exception e) {
            logger.error("findIndexNum error ", e);
        } finally {
            indexNum = new IndexNum(personTotal, borrowTotal, waitPerson, waitMoney, borrowToday, borrowTodayCnt, borrowTotalToday, borrowSucToday);
            String result = JSONUtil.beanToJson(indexNum);
            if (StringUtils.isNotBlank(call)) {
                result = call + "(" + result + ")";
            }
            SpringUtils.renderJson(response, result);
        }
    }

    /**
     * 用户分析页和全国地图页用户列表
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("findUserList")
    public void findUserList(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<UserShow> list = new ArrayList<UserShow>();
        String call = request.getParameter("callback");
        try {
            long a = System.currentTimeMillis();
//			list = showService.findUserList();
            long b = System.currentTimeMillis();
            System.out.println("findUserList======" + (b - a));
        } catch (Exception e) {
            logger.error("findUserList error ", e);
        } finally {
            String result = JSONUtil.beanListToJson(list);
            if (StringUtils.isNotBlank(call)) {
                result = call + "(" + result + ")";
            }
            SpringUtils.renderJson(response, result);
        }
    }

    /**
     * 全国地图页省份列表
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("findProvinceList")
    public void findProvinceList(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<Province> list = new ArrayList<Province>();
        String call = request.getParameter("callback");
        try {
            long a = System.currentTimeMillis();
//			list = showService.findProvinceList();
            long b = System.currentTimeMillis();
            System.out.println("findProvinceList======" + (b - a));
        } catch (Exception e) {
            logger.error("findProvinceList error ", e);
        } finally {
            String result = JSONUtil.beanListToJson(list);
            if (StringUtils.isNotBlank(call)) {
                result = call + "(" + result + ")";
            }
            SpringUtils.renderJson(response, result);
        }
    }
}
