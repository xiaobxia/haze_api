package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BankAllInfo;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IUserBankDao {
    /**
     * 保存银行卡
     *
     * @param bank
     * @return
     */
    boolean saveUserbankCard(UserCardInfo cardInfo);

    /**
     * 修改银行卡
     *
     * @param bank
     * @return
     */
    boolean updateUserBankCard(UserCardInfo cardInfo);

    /**
     * 查询银行卡列表
     *
     * @return
     */
    List<Map<String, Object>> findAllBankInfo();

    /**
     * 查询银行卡列表
     *
     * @return
     */
    List<BankAllInfo> findAllBankInfos();

    /**
     * 更加id查询银行名
     *
     * @param sqlId
     * @param id
     * @return
     * @throws DataAccessException
     */
    Map<String, String> selectByPrimaryKey(Integer id) throws DataAccessException;

    /**
     * 查询用户下的银行卡信息
     *
     * @param params
     * @return
     */
    List<UserCardInfo> findUserCardByUserId(HashMap<String, Object> params);

    /**
     * 查询用户银行卡信息
     *
     * @param id
     * @return
     */
    UserCardInfo findUserBankCard(Integer id);

    /**
     * 查询用户非默认银行卡信息
     * @param id
     * @return
     */
    public List<UserCardInfo> findUserBankCardNotDefault(Integer id);

    List<UserCardInfo> findUserBankListByIsBand(HashMap<String, Object> params);

    /**
     * 查询用户银行卡绑定列表信息
     *
     * @param id
     * @return
     */
    List<UserCardInfo> findUserBankCardList(Integer id);

    /**
     * 根据绑卡ID查询绑卡信息
     *
     * @param bankId
     * @return
     */
    UserCardInfo findBankCardByCardId(Integer bankId);

    /**
     * 根据银行卡号查询绑卡信息
     *
     * @param cardNo
     * @return
     */
    UserCardInfo findBankCardByCardNo(String cardNo);


}
