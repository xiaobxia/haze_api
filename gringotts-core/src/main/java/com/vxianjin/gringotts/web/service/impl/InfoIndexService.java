package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.dao.IIndexDao;
import com.vxianjin.gringotts.web.pojo.InfoIndexInfo;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.pojo.UserInfoOld;
import com.vxianjin.gringotts.web.service.IInfoIndexService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 用户动态数据设置
 *
 * @author gaoyuhai
 * 2016-12-17 下午05:59:16
 */
@Service
public class InfoIndexService implements IInfoIndexService {

    private static final Logger loger = LoggerFactory.getLogger(InfoIndexService.class);
    @Resource
    private IIndexDao indexDao;
    @Resource
    private IUserService userService;

    /**
     * 添加infoindexinfo
     *
     * @param user1
     */
    @Override
    public void addInfoIndexInfo(User user1) {
        try {

            loger.info("addInfoIndexInfo start...");
            HashMap<String, Object> map = new HashMap<>();
            map.put("USER_ID", user1.getId());

            //查询最新user
            User user = userService.searchByUserid(Integer.parseInt(user1.getId()));

            InfoIndexInfo infoIndexInfo = this.indexDao.searchInfoIndexInfo(map);

            if (null == infoIndexInfo) {//初始表中无记录
                InfoIndexInfo indexInfo = new InfoIndexInfo();
                indexInfo.setUserId(Integer.parseInt(user.getId()));
                String amountMax = user.getAmountMax();
                String amountMin = user.getAmountMin();
                if (StringUtils.isBlank(amountMax)) {
                    amountMax = String.valueOf(Constant.AMOUNT_MAX);
                }
                if (StringUtils.isBlank(amountMin)) {
                    amountMin = String.valueOf(Constant.AMOUNT_MIN);
                }
                indexInfo.setCardAmount(amountMax);
                indexInfo.setAmountMin(amountMin);
                indexInfo.setRate(Constant.RATE_MIN + "," + Constant.RATE_MAX);
                indexInfo.setAuthInfo(getAuthInfo(user));
                indexInfo.setAuthContacts(getAuthContacts(user));
                indexInfo.setAuthBank(0);
                indexInfo.setBankNo(null);
                String idCard = getAuthBank(user);
                loger.info("保存infoindexinfo：idcard=" + idCard + "  userid=" + user.getId());
                if (StringUtils.isNotBlank(idCard)) {
                    indexInfo.setAuthBank(Constant.STATUS_INT_VALID);
                    indexInfo.setBankNo(idCard);
                }
                indexInfo.setAuthSesame(Constant.STATUS_VALID.equals(user.getZmStatus()) ? 0 : 1);
                indexInfo.setAuthMobile(Constant.STATUS_VALID.equals(user.getTdStatus()) ? 0 : 1);

                this.indexDao.saveInfoIndexInfo(indexInfo);
                sendDynamic(indexInfo.getUserId());
            } else {
                InfoIndexInfo indexInfo = new InfoIndexInfo();
                indexInfo.setUserId(Integer.parseInt(user.getId()));
                String amountMax = user.getAmountMax();
                String amountMin = user.getAmountMin();
                if (StringUtils.isBlank(amountMax)) {
                    amountMax = String.valueOf(Constant.AMOUNT_MAX);
                }
                if (StringUtils.isBlank(amountMin)) {
                    amountMin = String.valueOf(Constant.AMOUNT_MIN);
                }
                indexInfo.setCardAmount(amountMax);
                indexInfo.setAmountMin(amountMin);
                indexInfo.setAuthInfo(getAuthInfo(user));
                indexInfo.setAuthContacts(getAuthContacts(user));
                String idCard = getAuthBank(user);
                loger.info("更新infoindexinfo：idcard=" + idCard + "  userid=" + user.getId());
                if (StringUtils.isNotBlank(idCard)) {
                    indexInfo.setAuthBank(Constant.STATUS_INT_VALID);
                    indexInfo.setBankNo(idCard);
                }
                indexInfo.setAuthSesame(Constant.STATUS_VALID.equals(user.getZmStatus()) ? 0 : 1);
                indexInfo.setAuthMobile(Constant.STATUS_VALID.equals(user.getTdStatus()) ? 0 : 1);

                this.indexDao.updateIndexInfoByUserIdOld(indexInfo);
                sendDynamic(indexInfo.getUserId());
            }
        } catch (Exception e) {
            loger.error("addInfoIndexInfo error userId=" + user1.getId(), e);
        }
    }

    private int getAuthInfo(User user) {

        String reNameStatus = user.getRealnameStatus();//1已认证
        String idCard = user.getIdNumber();//身份证
        if (StringUtils.isNotBlank(reNameStatus) && StringUtils.isNotBlank(idCard)) {
            if (Constant.STATUS_VALID.equals(reNameStatus)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }

    private int getAuthContacts(User user) {
        String firstName = user.getFirstContactName();
        String secondName = user.getSecondContactName();
        if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(secondName)) {
            return 1;
        } else {
            return 0;
        }
    }

    private String getAuthBank(User user) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("USER_ID", user.getId());
        UserCardInfo cardInfo = this.indexDao.searchUserCardInfo(map);
        if (null != cardInfo) {
            if (StringUtils.isNotBlank(cardInfo.getCard_no())) {
                return cardInfo.getCard_no().trim();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 用户信息认证
     *
     * @param map
     */
    @Override
    public void authInfo(HashMap<String, Object> map) {
        loger.info("authInfo start...map:" + map);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));
        indexInfo.setAuthInfo(Constant.STATUS_INT_VALID);
        this.indexDao.updateIndexInfoByUserId(indexInfo);
        sendDynamic(indexInfo.getUserId());
    }

    /**
     * 紧急联系人认证
     *
     * @param map
     */
    @Override
    public void authContacts(HashMap<String, Object> map) {
        loger.info("authContacts start...map:" + map);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));
        indexInfo.setAuthContacts(Constant.STATUS_INT_VALID);
        this.indexDao.updateIndexInfoByUserId(indexInfo);
        sendDynamic(indexInfo.getUserId());
    }

    /**
     * 用户银行卡认证
     *
     * @param map
     */
    @Override
    public void authBankOld(HashMap<String, Object> map) {
        loger.info("authBank start...map:" + map);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));

        HashMap<String, Object> bankMap = new HashMap<String, Object>();
        bankMap.put("USER_ID", Integer.parseInt(String.valueOf(map.get("userId"))));
        UserCardInfo cardInfo = this.indexDao.searchUserCardInfo(bankMap);
        if (null != cardInfo) {
            if (StringUtils.isNotBlank(cardInfo.getCard_no())) {
                indexInfo.setBankNo(cardInfo.getCard_no());
            }
        }
        indexInfo.setAuthBank(Constant.STATUS_INT_VALID);
        this.indexDao.updateIndexInfoByUserIdOld(indexInfo);
        sendDynamic(indexInfo.getUserId());
    }

    /**
     * 用户银行卡认证
     *
     * @param map
     */
    @Override
    public void authBank(HashMap<String, Object> map) {
        loger.info("authBank start...map:" + map);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));

        HashMap<String, Object> bankMap = new HashMap<String, Object>();
        bankMap.put("USER_ID", Integer.parseInt(String.valueOf(map.get("userId"))));
        UserCardInfo cardInfo = this.indexDao.searchUserCardInfo(bankMap);
        if (null != cardInfo) {
            if (StringUtils.isNotBlank(cardInfo.getCard_no())) {
                indexInfo.setBankNo(cardInfo.getCard_no());
            }
        }
        indexInfo.setAuthBank(Constant.STATUS_INT_VALID);
        this.indexDao.updateIndexInfoByUserId(indexInfo);
        sendDynamic(indexInfo.getUserId());
    }

    /**
     * 用户芝麻认证
     *
     * @param map
     */
    @Override
    public void authSesame(HashMap<String, Object> map) {
        loger.info("authSesame start...map:" + map);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));
        indexInfo.setAuthSesame(Constant.STATUS_INT_VALID);
        this.indexDao.updateIndexInfoByUserId(indexInfo);
        sendDynamic(indexInfo.getUserId());
    }

    /**
     * 用户手机认证
     *
     * @param map
     */
    @Override
    public void authMobile(HashMap<String, Object> map) {
        loger.info("authMobile start...map:" + map);
        InfoIndexInfo indexInfo = new InfoIndexInfo();
        indexInfo.setUserId(Integer.parseInt(String.valueOf(map.get("userId"))));
        indexInfo.setAuthMobile(Constant.STATUS_INT_VALID);
        this.indexDao.updateIndexInfoByUserId(indexInfo);
        sendDynamic(indexInfo.getUserId());
    }

    /**
     * 设置用户动态数据
     */
    @Override
    public void sendDynamic(Integer userId) {
//		loger.info("infoindexservice-sendDynamic start...");
//		try{
//			String indexJson = jedisCluster.get(Constant.CACHE_INDEX_KEY);//获取默认数据-备用
//			
//			HashMap<String,Object> map = new HashMap<String,Object>();
//			map.put("USER_ID", userId);
//			InfoIndexInfo indexInfo = this.indexDao.searchInfoIndexInfo(map);
//			
//			String indexJsonRe = getJsonStr(indexJson, indexInfo);
//			
////			jedisCluster.set(Constant.CACHE_INDEX_INFO_KEY+userId,indexJsonRe);
//		}catch(Exception e){
//			
//		}
    }

    /**
     * 更新用户额度
     *
     * @param map
     */
    @Override
    public void changeUserAmount(HashMap<String, Object> map) {
        loger.info("提升额度...");
        String userId = null;
        try {
            userId = String.valueOf(map.get("userId"));
        } catch (Exception e) {
            loger.info("changeUserAmount map.getkey is error:" + map);
        }
        if (StringUtils.isNotBlank(userId)) {
            loger.info("提升额度用户 userId=" + userId);
            Integer uId = Integer.parseInt(userId);
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("USER_ID", uId);
            User user = this.indexDao.searchUserByIndex(userMap);
            loger.info("changeUserAmount user:" + user);
            if (null != user) {
                InfoIndexInfo indexInfo = new InfoIndexInfo();
                indexInfo.setUserId(uId);
                indexInfo.setCardAmount(user.getAmountMax());
                indexInfo.setAmountMin(user.getAmountMin());
                loger.info("额度提升为:" + user.getAmountMax() + " -- " + user.getAmountMin());
                int updateRow = this.indexDao.updateIndexInfoByUserId(indexInfo);
                loger.info("changeUserAmount updateRow:" + updateRow);
            }
        } else {
            loger.info("提升额度用户 userId is null");
        }
    }

    /**
     * 查询老用户
     *
     * @param map
     * @return
     */
    @Override
    public UserInfoOld searchUserInfoOld(HashMap<String, Object> map) {
        return this.indexDao.searchUserInfoOld(map);
    }

    /**
     * 更新老用户
     *
     * @param map
     */
    @Override
    public void updateUserInfoOld(HashMap<String, Object> map) {
        this.indexDao.updateUserInfoOld(map);
    }
}
