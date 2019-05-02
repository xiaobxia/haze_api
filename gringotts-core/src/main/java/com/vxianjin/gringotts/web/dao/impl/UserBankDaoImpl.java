package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserBankDao;
import com.vxianjin.gringotts.web.pojo.BankAllInfo;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userBankDaoImpl")
public class UserBankDaoImpl extends BaseDao implements IUserBankDao {
    @Override
    public List<Map<String, Object>> findAllBankInfo() {
        return getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IBankAllInfoDao.selectBankAll");
    }

    /**
     * 查询银行卡列表
     *
     * @return
     */
    @Override
    public List<BankAllInfo> findAllBankInfos() {
        return getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IBankAllInfoDao.selectBankAlls");
    }

    @Override
    public boolean saveUserbankCard(UserCardInfo cardInfo) {
        return getSqlSessionTemplate().insert("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.saveUserCardInfo", cardInfo) > 0;
    }

    @Override
    public boolean updateUserBankCard(UserCardInfo cardInfo) {
        return getSqlSessionTemplate().update("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.updateUserCardInfo", cardInfo) > 0;
    }

    @Override
    public Map<String, String> selectByPrimaryKey(Integer id) throws DataAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getSqlSessionTemplate().selectOne("com.vxianjin.gringotts.web.dao.IBankAllInfoDao.selectBankInfo", params);
    }

    @Override
    public List<UserCardInfo> findUserCardByUserId(HashMap<String, Object> params) {
        return getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.findUserCardByUserId", params);
    }

    @Override
    public UserCardInfo findUserBankCard(Integer id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        List<UserCardInfo> lsit = getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.selectUserBankCard", params);
        if (lsit != null && lsit.size() > 0) {
            return lsit.get(0);
        }
        return null;
    }

    /**
     * 查询用户非默认银行卡信息
     * @param id
     * @return
     */
    @Override
    public List<UserCardInfo> findUserBankCardNotDefault(Integer id){
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("id", id);
        List<UserCardInfo> lsit= getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.selectUserBankCardNotDefault", params);
        return lsit;
    }

    @Override
    public List<UserCardInfo> findUserBankListByIsBand(HashMap<String, Object> params) {
        return getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.findUserBankListByIsBand", params);
    }

    /**
     * 查询用户银行卡绑定列表信息
     *
     * @param id
     * @return
     */
    @Override
    public List<UserCardInfo> findUserBankCardList(Integer id) {
        List<UserCardInfo> lsit = getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.findUserBankCardList", id);
        if (lsit != null && lsit.size() > 0) {
            return lsit;
        }
        return null;
    }

    /**
     * 根据绑卡ID查询绑卡信息
     *
     * @param bankId
     * @return
     */
    @Override
    public UserCardInfo findBankCardByCardId(Integer bankId) {
        return getSqlSessionTemplate().selectOne("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.findBankCardByCardId", bankId);
    }

    /**
     * 根据银行卡号查询绑卡信息
     *
     * @param cardNo
     * @return
     */
    public UserCardInfo findBankCardByCardNo(String cardNo) {
        List<UserCardInfo> list = getSqlSessionTemplate().selectList("com.vxianjin.gringotts.web.dao.IUserCardInfoDao.findBankCardByCardNo", cardNo);
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
}
