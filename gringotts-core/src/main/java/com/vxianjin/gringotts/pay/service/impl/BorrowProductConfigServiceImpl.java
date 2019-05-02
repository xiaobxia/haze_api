package com.vxianjin.gringotts.pay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.pay.dao.BorrowProductConfigDao;
import com.vxianjin.gringotts.pay.model.BorrowProductConfig;
import com.vxianjin.gringotts.pay.model.UserQuotaSnapshot;
import com.vxianjin.gringotts.pay.service.BorrowProductConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jintian
 * @date 13:52
 */
@Service
public class BorrowProductConfigServiceImpl implements BorrowProductConfigService {

    private Logger log = LoggerFactory.getLogger(BorrowProductConfigServiceImpl.class);

    @Autowired
    private BorrowProductConfigDao borrowProductConfigMapper;

    @Override
    public List<BorrowProductConfig> queryAllBorrowProductConfig() {
        return borrowProductConfigMapper.queryAllBorrowProductConfig();
    }

    @Override
    public List<BorrowProductConfig> queryUserBorrowProductConfig(UserQuotaSnapshot userQuotaSnapshot) {
        return borrowProductConfigMapper.queryAllConfigByMaxBorrowProductId(userQuotaSnapshot.getBorrowProductId());
    }

    @Override
    public JSONArray queryIndexAmountDayList() {
        JSONArray jsonArray = new JSONArray();
        List<BorrowProductConfig> borrowProductConfigs = queryAllBorrowProductConfig();
        if (borrowProductConfigs == null || borrowProductConfigs.size() == 0) {
            return jsonArray;
        }
        Integer borrowDay = null;
        JSONObject amountDayList = null;
        JSONArray amountList = null;
        JSONObject amount_free = null;
        for (BorrowProductConfig borrowProductConfig : borrowProductConfigs) {
            amount_free = new JSONObject();
            // 判断是否是第一次循环
            if (borrowDay == null) {
                amountDayList = new JSONObject();
                amountDayList.put("day", borrowProductConfig.getBorrowDay());
                amountList = new JSONArray();
                borrowDay = borrowProductConfig.getBorrowDay();
            }
            // 判断是否是另外一个期限的产品
            if (borrowDay != null && !borrowDay.equals(borrowProductConfig.getBorrowDay())) {
                amountDayList.put("amount_free", amountList);
                jsonArray.add(amountDayList);
                amountDayList = new JSONObject();
                amountDayList.put("day", borrowProductConfig.getBorrowDay());
                borrowDay = borrowProductConfig.getBorrowDay();
            }
            // 设置产品信息
            amount_free.put("amount", borrowProductConfig.getBorrowAmount());
            amount_free.put("creditVet", borrowProductConfig.getTurstTrial());
            amount_free.put("accountManage", borrowProductConfig.getAccountManagerFee());
            amount_free.put("accrual", borrowProductConfig.getBorrowInterest());
            amount_free.put("platformUse", borrowProductConfig.getPlatformLicensing());
            amount_free.put("collectionChannel", borrowProductConfig.getCollectChannelFee());
            amount_free.put("totalFee", borrowProductConfig.getTotalFeeRate());
            amount_free.put("arrivalMoney",borrowProductConfig.getBorrowAmount().subtract(borrowProductConfig.getTotalFeeRate()));
            // 将产品添加到产品期限列表中
            amountList.add(amount_free);
        }
        amountDayList.put("amount_free", amountList);
        jsonArray.add(amountDayList);
        return jsonArray;
    }

    @Override
    public JSONArray queryIndexUserAllowAmountDayList(List<UserQuotaSnapshot> userQuotaSnapshots) {
        JSONArray jsonArray = new JSONArray();
        if (userQuotaSnapshots == null || userQuotaSnapshots.size() == 0) {
            return jsonArray;
        }
        JSONObject amountDayList = null;
        JSONArray amountList = null;
        JSONObject amount_free = null;
        for (UserQuotaSnapshot userQuotaSnapshot : userQuotaSnapshots) {
            amountDayList = new JSONObject();
            amountList = new JSONArray();
            List<BorrowProductConfig> borrowProductConfigs = queryUserBorrowProductConfig(userQuotaSnapshot);
            for (BorrowProductConfig borrowProductConfig : borrowProductConfigs) {
                amountDayList.put("day", borrowProductConfig.getBorrowDay());
                amount_free = new JSONObject();
                // 设置产品信息
                amount_free.put("amount", borrowProductConfig.getBorrowAmount());
                amount_free.put("creditVet", borrowProductConfig.getTurstTrial());
                amount_free.put("accountManage", borrowProductConfig.getAccountManagerFee());
                amount_free.put("accrual", borrowProductConfig.getBorrowInterest());
                amount_free.put("platformUse", borrowProductConfig.getPlatformLicensing());
                amount_free.put("collectionChannel", borrowProductConfig.getCollectChannelFee());
                amount_free.put("totalFee", borrowProductConfig.getTotalFeeRate());
                amount_free.put("arrivalMoney",borrowProductConfig.getBorrowAmount().subtract(borrowProductConfig.getTotalFeeRate()));
                // 将产品添加到产品期限列表中
                amountList.add(amount_free);
            }
            amountDayList.put("amount_free", amountList);
            jsonArray.add(amountDayList);
        }
        return jsonArray;
    }

    @Override
    public BorrowProductConfig queryUserConfigByUserIdAndDay(int userId, int day) {
        return borrowProductConfigMapper.queryUserConfigByUserIdAndDay(userId, day);
    }

    @Override
    public BorrowProductConfig queryMaxLimitProduct(int nowLimit, int borrowDay) {
        Map map = new HashMap();
        map.put("nowLimit", nowLimit);
        map.put("borrowDay", borrowDay);
        return borrowProductConfigMapper.queryMaxLimitProduct(map);
    }
}
