package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.web.common.Certification.IHttpCertification;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.dao.IUserBankDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.dao.UserCerticationDao;
import com.vxianjin.gringotts.web.pojo.BankAllInfo;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.IUserBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserBankService implements IUserBankService {
    Logger logger = LoggerFactory.getLogger(UserBankService.class);
    @Autowired
    JedisCluster jedisCluster;
    @Autowired
    private IPaginationDao paginationDao;
    @Autowired
    @Qualifier("userBankDaoImpl")
    private IUserBankDao userBankDao;
    @Autowired
    @Qualifier("userCertication")
    private UserCerticationDao userCerticationDao;
    @Autowired
    private IHttpCertification httpCertification;
    @Autowired
    private IUserDao userDao;

    /**
     * 验证绑定银行卡参数
     *
     * @param params
     * @return
     */
    public static ResponseContent checkBankParmas(Map<String, String> params) {
        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), ResponseStatus.FAILD.getValue());
        if (params.get("bank_id") == null || "".equals(params.get("bank_id").toString())) {
            result.setMsg("银行编号不能为空");
        } else if (params.get("card_no") == null || "".equals(params.get("card_no").toString())) {
            result.setMsg("银行卡不能为空");
        } else if (params.get("phone") == null || "".equals(params.get("phone").toString())) {
            result.setMsg("预留手机号码不能为空");
        } else {
            result = new ResponseContent(ResponseStatus.SUCCESS.getName(), ResponseStatus.SUCCESS.getValue());
        }
        return result;
    }

    @Override
    public Map<String, String> selectBankByPrimaryKey(Integer id) {
        return userBankDao.selectByPrimaryKey(id);
    }

    @Override
    public ResponseContent saveUserBankCard(Map<String, String> params) {
        ResponseContent result = checkBankParmas(params);
        if (result.isSuccessed()) {
            result = httpCertification.bankCard(params);
            if (result.isSuccessed()) {
                Map<String, String> bankInfo = userBankDao.selectByPrimaryKey(Integer.parseInt(params.get("bank_id")));
                HashMap<String, Object> bankMap = new HashMap<String, Object>();
                bankMap.put("userId", Integer.parseInt(params.get("userId")));
                List<UserCardInfo> cardListr = userBankDao.findUserCardByUserId(bankMap);
                UserCardInfo cardInfo = new UserCardInfo();
                cardInfo.setBank_id(Integer.parseInt(params.get("bank_id")));
                cardInfo.setCard_no(params.get("card_no"));
                cardInfo.setPhone(params.get("phone"));
                cardInfo.setUserId(Integer.parseInt(params.get("userId")));
                cardInfo.setOpenName(params.get("realName"));
                cardInfo.setBankName(bankInfo.get("bankName") == null ? "" : bankInfo.get("bankName"));
                cardInfo.setStatus(UserCardInfo.STATUS_DEFAULT);
                cardInfo.setMainCard(UserCardInfo.MAINCARD_Z);
                cardInfo.setType(UserCardInfo.TYPE_DEBIT);
                cardInfo.setCreateTime(new Date());
                boolean falge = false;
                if (cardListr != null && cardListr.size() > 0) {
                    falge = userBankDao.updateUserBankCard(cardInfo);
                } else {
                    falge = userBankDao.saveUserbankCard(cardInfo);
                }
                if (!falge) {
                    result.setCode("-1");
                    result.setMsg("保存失败");
                } else {
//					try {
//						User user = userDao.searchByUserid(Integer.parseInt(params.get("userId")));
//						String invitePhone = null;
//						if (user.getInviteUserid() != null) {
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("id", Integer.parseInt(user.getInviteUserid()));
//							User userInvit = userDao.searchByUphoneAndUid(map);
//							invitePhone = userInvit.getUserPhone();
//						}
//						HashMap<String, String> stepMap = new HashMap<String, String>();
//						stepMap.put("userName", user.getRealname());
//						stepMap.put("cardNum", user.getIdNumber());
//						stepMap.put("userPhone", user.getUserPhone());
//						stepMap.put("awardType", "APPROVE");
//						stepMap.put("invitePhone", invitePhone);
//
//						logger.info(" addUserStep   APPROVE 请求参数：" + stepMap.toString());
//						ServiceResult serviceResult = jsStepRecordService.addUserStep(stepMap);
//						logger.info("addUserStep 抽奖返回>>>：" + serviceResult.getCode() + ":" + serviceResult.getMsg());
//
//					} catch (Exception e) {
//						logger.info("addUserStep error APPROVE", e);
//					}
                }
            }
        }
        return result;
    }

    @Override
    public ResponseContent updateUpserBankCard(Map<String, String> params) {
        ResponseContent result = checkBankParmas(params);
        if (result.isSuccessed()) {
            result = httpCertification.bankCard(params);
            if (result.isSuccessed()) {
                Map<String, String> bankInfo = userBankDao.selectByPrimaryKey(Integer.parseInt(params.get("bank_id")));
                UserCardInfo cardInfo = new UserCardInfo();
                cardInfo.setId(params.get("id") == null ? 0 : Integer.parseInt(params.get("id")));
                cardInfo.setBank_id(Integer.parseInt(params.get("bank_id")));
                cardInfo.setCard_no(params.get("card_no"));
                cardInfo.setPhone(params.get("phone"));
                cardInfo.setUserId(Integer.parseInt(params.get("userId")));
                cardInfo.setOpenName(params.get("realName"));
                cardInfo.setBankName(bankInfo.get("bankName") == null ? "" : bankInfo.get("bankName"));
                cardInfo.setStatus(UserCardInfo.STATUS_DEFAULT);
                cardInfo.setMainCard(UserCardInfo.MAINCARD_Z);
                cardInfo.setType(UserCardInfo.TYPE_DEBIT);
                cardInfo.setUpdateTime(new Date());
                result.setExt(cardInfo);
                // boolean flage=userBankDao.updateUserBankCard(cardInfo);
                // if(flage){
                // }else{
                // result.setMsg("重新绑定卡失败");
                // result.setCode("-1");
                // }
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findAllBankInfo() {
        return this.userBankDao.findAllBankInfo();
    }

    @Override
    public List<BankAllInfo> findAllBankInfos() {
        return this.userBankDao.findAllBankInfos();
    }

    /**
     * 根据绑卡ID查询银行卡信息
     *
     * @param id
     */
    @Override
    public UserCardInfo findBankCardByCardId(Integer id) {
        return userBankDao.findBankCardByCardId(id);
    }

    @Override
    public List<UserCardInfo> findUserCardByUserId(HashMap<String, Object> params) {
        return userBankDao.findUserCardByUserId(params);
    }

    @Override
    public PageConfig<Map<String, String>> findAllUserBankCardList(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "UserCardInfo");
        return paginationDao.findPage("findUserBankList", "findUserBankTotal", params, "web");
    }

    @Override
    public boolean updateUserBankCard(UserCardInfo cardInfo) {
        return userBankDao.updateUserBankCard(cardInfo);
    }

    @Override
    public List<UserCardInfo> findUserBankListByIsBand(HashMap<String, Object> param) {
        return userBankDao.findUserBankListByIsBand(param);
    }
}
