package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.risk.StrongRiskResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public interface IUserDao {

    /**
     * 查询用户是否存在
     *
     * @param map
     * @return
     */
    User searchUserByCheckTel(HashMap<String, Object> map);

    /**
     * 根据ID查询用户
     *
     * @param map
     * @return
     */
    User searchByUserid(int id);

    User selectCollectionByUserId(int id);


    /**
     * 根据用户ID查询验证码是否存在
     *
     * @param map
     * @return
     */
    User searchByInviteUserid(Map<String, String> map);

    /**
     * 根据用户ID,手机号,证件号查询用户是否存在
     *
     * @param map
     * @return
     */
    User searchByUphoneAndUid(Map<String, Object> map);

    /**
     * 用户注册
     *
     * @param user
     */
    void saveUser(User user);

    /**
     * 用户登录
     *
     * @param map
     * @return
     */
    User searchUserByLogin(HashMap<String, Object> map);

    /***
     * 修改用户信息
     */
    int updateByPrimaryKeyUser(User user);

    /**
     * 根据身份证查询用户是否存在
     *
     * @param idCard
     * @return
     */
    User searchByUserIDCard(String idCard);

    /**
     * 修改用户实名认证次数
     *
     * @param user
     * @return
     */
    int updateRealCount(User user);

    int updateAmountAvailableByUserId(User user);

    int updateAmountByUserId(User user);

    Integer selectChanelUserPushCount(HashMap<String, Object> map);

    void insertChanelUserPush(HashMap<String, Object> map);

    HashMap<String, Object> selectPushId(HashMap<String, Object> map);

    /**
     * 更新用户同盾运营商信息
     *
     * @param user
     */
    int updateUserTd(User user);

    /**
     * 更新用户new_flag状态
     *
     * @return
     */
    int updateTdNewFlag(User user);

    int updateUserZm(User userNew);


    int updateTdStatus(User userNew);

    int updateUserNewFlagById(User user);

    /**
     * 加分认证更改用户
     *
     * @param user
     * @return
     */
    int updateInScoreAuth(User user);


    int updateUserLimit(int id, int bigDecimal , BigDecimal availableAmount);

    /**
     * 更新用户可借额度
     *
     * @param userId 用户id
     * @param money  需加上的可借额度（还款金额）
     * @return
     */
    int addAvailableAmount(Integer userId, int money);

    /**
     * 更新用户为老用户
     *
     * @param userId 用户id
     * @return
     */
    int updateUserToOld(String userId);

    /**
     * 保存强风控结果
     * @param strongRiskResult strongRiskResult
     */
    void insertUserStrongRiskResult(StrongRiskResult strongRiskResult);

    /**
     * 根据用户id 查询强风控结果
     * @param userId userId
     * @return result
     */
    StrongRiskResult getStrongRiskResultByUserId(String userId);
}
