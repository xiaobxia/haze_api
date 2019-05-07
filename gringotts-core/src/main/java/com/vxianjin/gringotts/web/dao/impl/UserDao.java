package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.risk.RiskRecord;
import com.vxianjin.gringotts.web.pojo.risk.StrongRiskResult;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao extends BaseDao implements IUserDao {

    /**
     * 查询用户是否存在
     *
     * @param map
     * @return
     */
    public User searchUserByCheckTel(HashMap<String, Object> map) {
        return this.getSqlSessionTemplate().selectOne("searchUserByCheckTel", map);
    }

    /**
     * 根据ID查询用户
     *
     * @param id
     * @return
     */
    public User searchByUserid(int id) {
        return this.getSqlSessionTemplate().selectOne("selectByUserId", id);
    }

    /**
     * 催收根据ID查询用户
     *
     * @param id
     * @return
     */
    public User selectCollectionByUserId(int id) {
        return this.getSqlSessionTemplate().selectOne("selectCollectionByUserId", id);
    }

    /**
     * 根据用户ID查询验证码是否存在
     *
     * @param map
     * @return
     */
    public User searchByInviteUserid(Map<String, String> map) {
        return this.getSqlSessionTemplate().selectOne("selectByUserIds", map);
    }

    /**
     * 根据用户ID,手机号,证件号查询用户是否存在
     *
     * @param map
     * @return
     */
    public User searchByUphoneAndUid(Map<String, Object> map) {
        return this.getSqlSessionTemplate().selectOne("selectByUPhoneAndUid", map);
    }

    /**
     * 用户注册
     *
     * @param user
     */
    public void saveUser(User user) {
        this.getSqlSessionTemplate().insert("saveUser", user);
    }

    /**
     * 用户登录
     *
     * @param map
     * @return
     */
    public User searchUserByLogin(HashMap<String, Object> map) {
        return this.getSqlSessionTemplate().selectOne("searchUserByLogin", map);
    }

    /***
     * 修改用户信息
     */
    public int updateByPrimaryKeyUser(User user) {
        return this.getSqlSessionTemplate().update("updateUser", user);
    }

    @Override
    public User searchByUserIDCard(String idCard) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("idCard", idCard);
        return this.getSqlSessionTemplate().selectOne("selectByUserIdCard", map);
    }

    @Override
    public int updateAmountAvailableByUserId(User user) {
        return this.getSqlSessionTemplate().update("updateAmountAvailableByUserId", user);
    }

    @Override
    public int updateAmountByUserId(User user) {
        return this.getSqlSessionTemplate().update("updateAmountByUserId", user);
    }

    @Override
    public int updateRealCount(User user) {
        return this.getSqlSessionTemplate().update("updateRealCount", user);
    }

    @Override
    public Integer selectChanelUserPushCount(HashMap<String, Object> map) {
        return this.getSqlSessionTemplate().selectOne("selectChanelUserPushCount", map);
    }

    @Override
    public void insertChanelUserPush(HashMap<String, Object> map) {
        this.getSqlSessionTemplate().insert("insertChanelUserPush", map);
    }

    @Override
    public HashMap<String, Object> selectPushId(HashMap<String, Object> map) {
        return this.getSqlSessionTemplate().selectOne("selectPushIdByInviteUserid", map);
    }

    @Override
    public void saveRiskRecord(RiskRecord riskRecord) {
        this.getSqlSessionTemplate().insert("saveRiskRecord",riskRecord);
    }

    @Override
    public int updateTdNewFlag(User user) {
        return this.getSqlSessionTemplate().update("updateUserNewFlagById", user);
    }

    @Override
    public int updateUserTd(User user) {
        return this.getSqlSessionTemplate().update("updateUserTdDetail", user);
    }

    @Override
    public int updateUserZm(User user) {
        return this.getSqlSessionTemplate().update("updateUserZmDetail", user);
    }

    @Override
    public int updateTdStatus(User user) {
        return this.getSqlSessionTemplate().update("updateUserTdStatus", user);
    }

    @Override
    public int updateUserNewFlagById(User user) {
        return this.getSqlSessionTemplate().update("updateUserNewFlagById", user);
    }

    @Override
    public int updateInScoreAuth(User user) {
        return this.getSqlSessionTemplate().update("updateInScoreAuth", user);
    }


    public int updateUserLimit(int userId, int maxLimit , BigDecimal availableAmount) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("maxLimit", maxLimit);
        map.put("availableAmount",availableAmount.intValue());
        return this.getSqlSessionTemplate().update("updateUserLimit", map);
    }

    @Override
    public int addAvailableAmount(Integer userId, int money) {
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("money", money);
        return this.getSqlSessionTemplate().update("addAvailableAmount", map);
    }

    @Override
    public int updateUserToOld(String userId) {
        return this.getSqlSessionTemplate().update("updateUserToOld", userId);
    }

    @Override
    public void insertUserStrongRiskResult(StrongRiskResult strongRiskResult) {
        this.getSqlSessionTemplate().insert("insertUserStrongRiskResult", strongRiskResult);
    }

    @Override
    public StrongRiskResult getStrongRiskResultByUserId(String userId) {
        return this.getSqlSessionTemplate().selectOne("getStrongRiskResultByUserId",userId);
    }
}
