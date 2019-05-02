package com.vxianjin.gringotts.show.service;

import com.vxianjin.gringotts.show.dao.IShowDao;
import com.vxianjin.gringotts.show.pojo.Province;
import com.vxianjin.gringotts.show.pojo.UserShow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ShowService implements IShowService {
    @Autowired
    private IShowDao showDao;


    @Override
    public List<HashMap<String, Object>> findIndexNum() {
        List<HashMap<String, Object>> list = showDao.findIndexNum();
        list.addAll(showDao.findXq());
        return list;
    }


    @Override
    public List<UserShow> findUserList() {
        List<UserShow> list2 = new ArrayList<UserShow>();
        long a = System.currentTimeMillis();
        List<String> list = showDao.findAssetIds();
        long b = System.currentTimeMillis();
        System.out.println("findAssetIds======" + (b - a));
        if (list != null && list.size() > 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            long a1 = System.currentTimeMillis();
            for (String assetId : list) {
                String riskCalSuf = showDao.findSuf(assetId);
                params.put("assetId", assetId);
                UserShow userShow = showDao.findUserList(params);
                params.put("riskCalSuf", riskCalSuf);
                params.put("ruleId", "157");
                BigDecimal xyScore = BigDecimal.ZERO;
                String value1 = showDao.findRuleValue(params);
                if (StringUtils.isNotBlank(value1)) {
                    xyScore = new BigDecimal(value1).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                userShow.setXyScore(xyScore);
                params.put("ruleId", "78");
                BigDecimal qzScore = BigDecimal.ZERO;
                String value2 = showDao.findRuleValue(params);
                if (StringUtils.isNotBlank(value2)) {
                    qzScore = new BigDecimal(value2).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                userShow.setQzScore(qzScore);
                params.put("ruleId", "121");
                String value3 = showDao.findRuleValue(params);
                if ("1".equals(value3)) {
                    value3 = "通过";
                } else {
                    value3 = "命中黑名单";
                }
                userShow.setForbidden(value3);
                list2.add(userShow);
            }
            long b1 = System.currentTimeMillis();
            System.out.println("for======" + (b1 - a1));
        }
        return list2;
    }


    @Override
    public List<Province> findProvinceList() {
        List<Province> list = showDao.findProvinceList();
        for (Province province : list) {
            province.setBorrowTotal(province.getBorrowTotal().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            province.setBorrowToday(province.getBorrowToday().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        }
        return list;
    }

}
