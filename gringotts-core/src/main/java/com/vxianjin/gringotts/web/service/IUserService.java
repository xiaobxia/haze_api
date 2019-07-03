package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.pojo.UserCertification;
import com.vxianjin.gringotts.web.pojo.risk.StrongRiskResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从core中提取出来
 * Created by wukun on 2018/1/30
 */
public interface IUserService {
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
     * 后台查询 分页
     */
    PageConfig<User> getUserPage(HashMap<String, Object> params);

    /**
     * 查询用户认证列表
     *
     * @return
     */
    List<UserCertification> findCerticationList();

    /**
     * 验证用户已认证的选项
     */
    Map<String, Object> checkUserCalendar(Integer id);

    /**
     * 查询用户银行卡信息
     *
     * @param id
     * @return
     */
    UserCardInfo findUserBankCard(Integer id);

    UserCardInfo findUserBankCardNew(Integer id);

    /**
     * 查询实名认证列表
     *
     * @param params
     * @return
     */
    PageConfig<Map<String, String>> realNmaeList(HashMap<String, Object> params);

    /**
     * 查询用户认证状态列表
     *
     * @param params
     * @return
     */
    PageConfig<Map<String, String>> certificationList(HashMap<String, Object> params);

    /**
     * 根据身份证查询用户是否存在
     *
     * @param idCard
     * @return
     */
    User searchByUserIDCard(String idCard);

    /**
     * 根据inviteUserid，查询pushId
     *
     * @return
     */
    HashMap<String, Object> selectPushId(Integer userId);

    /**
     * 修改认证次数
     *
     * @param user
     */
    int updateRealCount(User user);


    /**
     * 修改地推系统注册用户ID为推广员
     */
    void insertChanelUserPush(HashMap<String, Object> params);

    /**
     * 查询地推系统推送ID在推广员表中COUNT
     *
     * @param params
     * @return
     */
    Integer selectChanelUserPushCount(HashMap<String, Object> params);

    /**
     * 更新用户同盾运营商信息
     *
     * @param newUser
     */
    Integer updateTd(User newUser);

    /**
     * 更新用户芝麻认证信息
     *
     * @param userNew
     */
    Integer updateZm(User userNew);

    /**
     * 加分用户更新
     *
     * @param newUser
     * @return
     */
    Integer updateUserScoreAuth(User newUser);

    /**
     * 查询用户银行卡列表信息
     *
     * @param id
     * @return
     */
    List<UserCardInfo> findUserBankCardList(Integer id);

    /**
     * 根据ID查询该用户银行卡信息
     *
     * @param id
     * @return
     */
    UserCardInfo findBankCardByCardId(Integer id) throws Exception;

    /**
     * 根据银行卡号查询该用户银行卡信息
     *
     * @param cardNo
     * @return
     */
    UserCardInfo findBankCardByCardNo(String cardNo) throws Exception;

    /**
     * 保存强风控
     * @param strongRiskResult strongRiskResult
     */
    void insertUserStrongRiskResult(StrongRiskResult strongRiskResult);

    Integer defaultCardCount(Integer userId);

    void saveOrUpdateUdcredit(String sessionId, String userId, Integer type, String livingImageUrl);
}
