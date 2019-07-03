package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.*;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.risk.StrongRiskResult;
import com.vxianjin.gringotts.web.service.IInfoIndexService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    private Logger loger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserDao userDao;
    @Autowired
    private IPaginationDao paginationDao;
    @Autowired
    @Qualifier("userCertication")
    private UserCerticationDao userCerticationDao;
    @Autowired
    @Qualifier("userBankDaoImpl")
    private IUserBankDao userBankDao;
    @Autowired
    private IInfoIndexService infoIndexService;
    @Resource
    private IUserBlackDao userBlackDao;
    @Resource
    private IUserUdcreditInfoDao userUdcreditInfoDao;

    /**
     * 查询用户是否存在
     *
     * @param map
     * @return
     */
    @Override
    public User searchUserByCheckTel(HashMap<String, Object> map) {
        return this.userDao.searchUserByCheckTel(map);
    }

    /**
     * 根据ID查询用户
     *
     * @param map
     * @return
     */
    @Override
    public User searchByUserid(int id) {
        return this.userDao.searchByUserid(id);
    }

    /**
     * 根据ID查询用户
     *
     * @param id
     * @return
     */
    @Override
    public User selectCollectionByUserId(int id) {
        return this.userDao.selectCollectionByUserId(id);
    }

    /**
     * 根据用户ID查询验证码是否存在
     *
     * @param map
     * @return
     */
    @Override
    public User searchByInviteUserid(Map<String, String> map) {
        return this.userDao.searchByInviteUserid(map);
    }

    /**
     * 根据用户ID,手机号,证件号查询用户是否存在
     *
     * @param map
     * @return
     */
    @Override
    public User searchByUphoneAndUid(Map<String, Object> map) {
        return this.userDao.searchByUphoneAndUid(map);
    }


    /**
     * 用户注册
     *
     * @param user
     */
    @Override
    public void saveUser(User user) {
        UserBlack userBlack = userBlackDao.findSelective(new HashMap<String, Object>() {{
            put("userPhone", user.getUserName());
        }});
        if (userBlack != null && userBlack.getUserType().intValue() == 0) user.setStatus("2");
        this.userDao.saveUser(user);
    }

    /**
     * 用户登录
     *
     * @param map
     * @return
     */
    @Override
    public User searchUserByLogin(HashMap<String, Object> map) {
        return this.userDao.searchUserByLogin(map);
    }

    /***
     * 修改用户信息
     */
    @Override
    public int updateByPrimaryKeyUser(User user) {
        return this.userDao.updateByPrimaryKeyUser(user);
    }

    /**
     * 后台查询 分页
     */
    @Override
    public PageConfig<User> getUserPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "User");
        return paginationDao.findPage("selectUserPage", "selectUserCount", params, "web");
    }

    @Override
    public List<UserCertification> findCerticationList() {
        return this.userCerticationDao.findCerticationList();
    }

    @Override
    public Map<String, Object> checkUserCalendar(Integer id) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return userCerticationDao.checkUserCalendar(params);
    }

    @Override
    public UserCardInfo findUserBankCard(Integer id) {
        UserCardInfo userBankCard = userBankDao.findUserBankCard(id);

        //如果只有一张卡 且当前为非默认卡 则将其置为默认卡
        if (null == userBankCard) {
            List<UserCardInfo> userBankCardNotDefault = userBankDao.findUserBankCardNotDefault(id);
            if (null != userBankCardNotDefault && 1 == userBankCardNotDefault.size()) {
                userBankCardNotDefault.get(0).setCardDefault(1);
                userBankCard = userBankCardNotDefault.get(0);
                userBankDao.updateUserBankCard(userBankCardNotDefault.get(0));

            }
        }
        return userBankCard;

    }

    @Override
    public UserCardInfo findUserBankCardNew(Integer id) {
        UserCardInfo userBankCard = userBankDao.findUserBankCardNew(id);

        //如果只有一张卡 且当前为非默认卡 则将其置为默认卡
        if (null == userBankCard) {
            List<UserCardInfo> userBankCardNotDefault = userBankDao.findUserBankCardNotDefault(id);
            if (null != userBankCardNotDefault && 1 == userBankCardNotDefault.size()) {
                userBankCardNotDefault.get(0).setCardDefault(1);
                userBankCard = userBankCardNotDefault.get(0);
                userBankDao.updateUserBankCard(userBankCardNotDefault.get(0));

            }
        }
        return userBankCard;
    }

    @Override
    public PageConfig<Map<String, String>> realNmaeList(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "User");
        return paginationDao.findPage("selectUserRealNamePage", "selectUserReanlNameCount", params, "web");
    }

    @Override
    public PageConfig<Map<String, String>> certificationList(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "User");
        return paginationDao.findPage("selectCertificationPage", "selectCertificationCount", params, "web");
    }

    @Override
    public User searchByUserIDCard(String idCard) {
        return this.userDao.searchByUserIDCard(idCard);
    }

    @Override
    public int updateRealCount(User user) {
        return this.userDao.updateRealCount(user);
    }

    @Override
    public Integer selectChanelUserPushCount(HashMap<String, Object> params) {
        return this.userDao.selectChanelUserPushCount(params);

    }

    @Override
    public void insertChanelUserPush(HashMap<String, Object> params) {
        this.userDao.insertChanelUserPush(params);
    }

    @Override
    public HashMap<String, Object> selectPushId(Integer userId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        return userDao.selectPushId(params);
    }

    @Override
    public Integer updateTd(User user) {
        loger.info("updateTd start");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userId", user.getId());
        loger.info("updateTd td_status = " + user.getTdStatus());
        if ("2".equals(user.getTdStatus())) {
            this.infoIndexService.authMobile(map);// 初始设置
        }
        loger.info("updateTd userId = " + user.getId());
        return userDao.updateUserTd(user);
    }

    @Override
    public Integer updateZm(User user) {
        loger.info("updateZm start");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userId", user.getId());
        if ("2".equals(user.getZmStatus())) {
            this.infoIndexService.authSesame(map);
        }
        loger.info("updateZm userId=" + user.getId());
        return userDao.updateUserZm(user);
    }

    /**
     * 加分认证更新信息
     *
     * @param user
     * @return
     */
    @Override
    public Integer updateUserScoreAuth(User user) {
        return userDao.updateInScoreAuth(user);
    }

    /**
     * 查询用户银行卡列表信息
     *
     * @param id
     * @return
     */
    @Override
    public List<UserCardInfo> findUserBankCardList(Integer id) {

        List<UserCardInfo> userBankCardList = userBankDao.findUserBankCardList(id);
        //如果只有一张卡 且当前为非默认卡 则将其置为默认卡
        if (userBankCardList != null && !userBankCardList.isEmpty()) {
            if (1 == userBankCardList.size() && 0 == userBankCardList.get(0).getCardDefault()) {
                userBankCardList.get(0).setCardDefault(1);
                userBankDao.updateUserBankCard(userBankCardList.get(0));
            }
        }
        return userBankCardList;
    }

    /**
     * 根据ID查询该用户银行卡信息
     *
     * @param bankId
     * @return
     */
    @Override
    public UserCardInfo findBankCardByCardId(Integer bankId) throws Exception {
        return userBankDao.findBankCardByCardId(bankId);
    }

    /**
     * 根据银行卡号查询该用户银行卡信息
     *
     * @param cardNo
     * @return
     */
    @Override
    public UserCardInfo findBankCardByCardNo(String cardNo) {
        return userBankDao.findBankCardByCardNo(cardNo);
    }

    @Override
    public void insertUserStrongRiskResult(StrongRiskResult strongRiskResult) {
        this.userDao.insertUserStrongRiskResult(strongRiskResult);
    }

    @Override
    public Integer defaultCardCount(Integer userId) {
        return this.userDao.defaultCardCount(userId);
    }

    @Override
    public void saveOrUpdateUdcredit(String sessionId, String userId, Integer type, String livingImageUrl) {
        Date now = new Date();
        UserUdcreditInfo udcreditInfo = userUdcreditInfoDao.findSelective(new HashMap() {{
            put("userId", userId);
        }});
        if (udcreditInfo != null) {
            UserUdcreditInfo userUdcreditInfo = new UserUdcreditInfo();
            userUdcreditInfo.setId(udcreditInfo.getId());
            userUdcreditInfo.setUserId(Integer.parseInt(userId));
            if (type == 1) {
                userUdcreditInfo.setLivingSession(sessionId);
                userUdcreditInfo.setLivingImageUrl(livingImageUrl);
            } else {
                userUdcreditInfo.setHeaderSession(sessionId);
            }
            userUdcreditInfo.setUpdateTime(now);
            userUdcreditInfoDao.updateSelective(userUdcreditInfo);
        } else {
            UserUdcreditInfo userUdcreditInfo = new UserUdcreditInfo();
            userUdcreditInfo.setUserId(Integer.parseInt(userId));
            if (type == 1) {
                userUdcreditInfo.setLivingSession(sessionId);
                userUdcreditInfo.setLivingImageUrl(livingImageUrl);
            } else {
                userUdcreditInfo.setHeaderSession(sessionId);
            }
            userUdcreditInfo.setCreateTime(now);
            userUdcreditInfoDao.saveRecord(userUdcreditInfo);
        }
    }

}
