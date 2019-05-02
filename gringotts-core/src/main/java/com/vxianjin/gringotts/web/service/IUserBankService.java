package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.web.pojo.BankAllInfo;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IUserBankService {

    Map<String, String> selectBankByPrimaryKey(Integer id);

    /**
     * 绑定银行卡
     *
     * @return
     */
    ResponseContent saveUserBankCard(Map<String, String> params);

    /**
     * 修改银行卡
     *
     * @return
     */
    ResponseContent updateUpserBankCard(Map<String, String> params);

    /**
     * 查询银行卡列表
     *
     * @return
     */
    List<Map<String, Object>> findAllBankInfo();

    /**
     * 查询用户下的银行卡信息
     *
     * @param params
     * @return
     */
    List<UserCardInfo> findUserCardByUserId(HashMap<String, Object> params);

    /**
     * 查询所有用户银行卡列表
     *
     * @param params
     * @return
     */
    PageConfig<Map<String, String>> findAllUserBankCardList(HashMap<String, Object> params);

    /**
     * 修改银行卡
     *
     * @param cardInfo
     * @return
     */
    boolean updateUserBankCard(UserCardInfo cardInfo);

    List<UserCardInfo> findUserBankListByIsBand(HashMap<String, Object> param);

    /**
     * 查询银行卡列表
     *
     * @return
     */
    List<BankAllInfo> findAllBankInfos();

    /**
     * 根据绑卡ID查询银行卡信息
     */
    UserCardInfo findBankCardByCardId(Integer id);
}
