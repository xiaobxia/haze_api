package com.vxianjin.gringotts.web.util;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.borrowStatusMap_shenheFail;
import static com.vxianjin.gringotts.web.pojo.BorrowOrder.borrowStatusMap_shenhezhong;

public class IndexUtil {

    private static Logger loger = LoggerFactory.getLogger(IndexUtil.class);

    /**
     * 获取公告
     *
     * @param noticeList
     * @return
     */
    public static Object getUserLoanLogList(List<InfoNotice> noticeList) {
        List<String> userLoanLogList = new ArrayList<String>();
        for (InfoNotice notice : noticeList) {
            userLoanLogList.add(notice.getNotContent());
        }
        return userLoanLogList;
    }

    /**
     * 获取首页图片
     *
     * @param infoImageList
     * @return
     */
    public static Object getInfoImageList(List<InfoImage> infoImageList) {
        List<HashMap<String, Object>> infoImageLists = new ArrayList<HashMap<String, Object>>();
        for (InfoImage infoImage : infoImageList) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("url", infoImage.getUrl());
            map.put("title", infoImage.getTitle());
            map.put("sort", infoImage.getSort());
            map.put("reurl", infoImage.getReurl());
            infoImageLists.add(map);
        }
        return infoImageLists;
    }

    /**
     * 获取金额列表
     *
     * @param index
     * @return
     */
    public static Object getAmounts(InfoIndex index) {
        List<String> amountList = new ArrayList<String>();
        Integer max = null;
        Integer min = null;
        try {
            max = Integer.parseInt(index.getAmount_max());
            min = Integer.parseInt(index.getAmount_min());
        } catch (Exception e) {
            max = Constant.AMOUNT_MAX;
            min = Constant.AMOUNT_MIN;
        }
        if (max == null) {
            max = Constant.AMOUNT_MAX;
        }
        if (min == null) {
            min = Constant.AMOUNT_MIN;
        }
        for (int i = min; i <= max; i = i + Constant.FOR_BASE) {
            amountList.add(String.valueOf(i));
        }
        return amountList;
    }

    /**
     * 获取金额列表
     *
     * @param max
     * @return
     */
    public static Object getAmounts(Integer min, Integer max) {
        List<String> amountList = new ArrayList<String>();
        if (1 == max) {
            amountList.add("0");
            amountList.add("1");
            return amountList;
        }
        boolean bool = true;
        if ((max / Constant.FOR_BASE) != 0) {
            bool = false;
        }
        Integer maxs = max / Constant.FOR_BASE * Constant.FOR_BASE;

        for (int i = min; i <= maxs; i = i + Constant.FOR_BASE) {
            amountList.add(String.valueOf(i));
        }
        if (!bool) {
            amountList.add(String.valueOf(max));
        }
        return amountList;
    }

    /**
     * 获取到账金额、佣金金额
     *
     * @param index
     * @return
     */
    public static Object getInterests(InfoIndex index) {
        List<String> interestList = new ArrayList<String>();
        interestList.add(index.getAmount());
        interestList.add(index.getService_amount());
        return interestList;
    }

    /**
     * 获取到账金额、佣金金额
     *
     * @param indexInfo
     * @return
     */
    public static Object getInterests(InfoIndexInfo indexInfo) {
        loger.info("获取到账金额、佣金金额:Rate:" + indexInfo.getRate() + " CardAmount:" + indexInfo.getCardAmount() + " AmountMin:" + indexInfo.getAmountMin());
        Integer amountMax = null;
        Double rateMax = null;
        Double rateMin = null;
        try {
            String[] rateArr = indexInfo.getRate().split(",");//目前默认7，14天的利率
            amountMax = Integer.parseInt(indexInfo.getCardAmount());

            if (null != rateArr && 0 < rateArr.length && rateArr.length == 2) {
                rateMax = Double.parseDouble(rateArr[1]);
                rateMin = Double.parseDouble(rateArr[0]);
            } else {
                rateMax = Constant.RATE_MAX;
                rateMin = Constant.RATE_MIN;
            }
        } catch (Exception e) {
            amountMax = Constant.AMOUNT_MAX;
            rateMax = Constant.RATE_MAX;
            rateMin = Constant.RATE_MIN;
        }

        List<String> interestList = new ArrayList<String>();

        String amountMinRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMin);
        String amountMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMax);

        interestList.add(amountMinRate.substring(0, amountMinRate.indexOf(".")));
        interestList.add(amountMaxRate.substring(0, amountMaxRate.indexOf(".")));
        return interestList;
    }

    /**
     * 获取利息
     *
     * @param indexInfo
     * @param user
     * @return
     */
    public static Object getAccrual(InfoIndexInfo indexInfo, User user, JSONArray days) {
        loger.info("获取账户管理费使用user");
        Integer amountMax = null;
        Double accrualMax = null;
        Double accrualMin = null;
        try {
            if (null != user) {
                loger.info("获取账户管理费使用userId=" + user.getId() + " amountmax=" + user.getAmountMax());
                amountMax = Integer.parseInt(user.getAmountMax());
            }
            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 借款利息费配置
            String accrualrateTypes = "7:0.005;14:0.007";//keys.get("ACCRUAL_RATE");
            String accrualRateTypesArr[] = accrualrateTypes.split(";");
            for (String a : accrualRateTypesArr) {
                String accrualRates[] = a.split(":");
                if (days.get(0).toString().equals(accrualRates[0])) {//判断是否是7天
                    accrualMin = Double.valueOf(accrualRates[1]);
                } else if (days.get(1).toString().equals(accrualRates[0])) {//判断是否是14天
                    accrualMax = Double.valueOf(accrualRates[1]);
                } else {
                    accrualMax = Constant.ACCRUAL_RATE_MAX;
                    accrualMin = Constant.ACCRUAL_RATE_MIN;
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            accrualMax = Constant.ACCRUAL_RATE_MAX;
            accrualMin = Constant.ACCRUAL_RATE_MIN;
        }
        List<String> accrualList = new ArrayList<String>();
        String creditMinRate = String.valueOf(Double.parseDouble(amountMax + "") * accrualMin);
        String creditMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * accrualMax);
        accrualList.add(creditMinRate.substring(0, creditMinRate.indexOf(".")));
        accrualList.add(creditMaxRate.substring(0, creditMaxRate.indexOf(".")));
        return accrualList;
    }

    /**
     * 获取账户管理费
     *
     * @param indexInfo
     * @param user
     * @return
     */
    public static Object getAccountManage(InfoIndexInfo indexInfo, User user, JSONArray days) {
        loger.info("获取账户管理费使用user");
        Integer amountMax = null;
        Double accountManageMax = null;
        Double accountManageMin = null;
        try {
            if (null != user) {
                loger.info("获取账户管理费使用userId=" + user.getId() + " amountmax=" + user.getAmountMax());
                amountMax = Integer.parseInt(user.getAmountMax());
            }
            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 账户管理费配置
            String accountManageRateTypes = "7:0.04;14:0.063";//keys.get("ACCOUNT_MANAGE_RATE");
            String accountManageRateTypesArr[] = accountManageRateTypes.split(";");
            for (String a : accountManageRateTypesArr) {
                String accountManageRates[] = a.split(":");
                if (days.get(0).toString().equals(accountManageRates[0])) {//判断是否是7天
                    accountManageMin = Double.valueOf(accountManageRates[1]);
                } else if (days.get(1).toString().equals(accountManageRates[0])) {//判断是否是14天
                    accountManageMax = Double.valueOf(accountManageRates[1]);
                } else {
                    accountManageMax = Constant.ACCOUNT_MANAGE_RATE_MAX;
                    accountManageMin = Constant.ACCOUNT_MANAGE_RATE_MIN;
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            accountManageMax = Constant.ACCOUNT_MANAGE_RATE_MAX;
            accountManageMin = Constant.ACCOUNT_MANAGE_RATE_MIN;
        }

        List<String> accountManageList = new ArrayList<String>();

        String creditMinRate = String.valueOf(Double.parseDouble(amountMax + "") * accountManageMin);
        String creditMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * accountManageMax);

        accountManageList.add(creditMinRate.substring(0, creditMinRate.indexOf(".")));
        accountManageList.add(creditMaxRate.substring(0, creditMaxRate.indexOf(".")));
        return accountManageList;
    }

    /**
     * 获取信审查询费用
     *
     * @param indexInfo
     * @param user
     * @return
     */
    public static Object getCreditVet(InfoIndexInfo indexInfo, User user, JSONArray days) {
        loger.info("获取信审查询费使用user");
        Integer amountMax = null;
        Double creditRateMax = null;
        Double creditRateMin = null;
        try {
            if (null != user) {
                loger.info("获取信审查询费使用userId=" + user.getId() + " amountmax=" + user.getAmountMax());
                amountMax = Integer.parseInt(user.getAmountMax());
            }
            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 信审查询费配置
            String creditRateTypes = "7:0.04;14:0.08";//keys.get("CREDIT_RATE");
            String creditRateTypesArr[] = creditRateTypes.split(";");
            for (String c : creditRateTypesArr) {
                String creditRates[] = c.split(":");
                if (days.get(0).toString().equals(creditRates[0])) {//判断是否是7天
                    creditRateMin = Double.valueOf(creditRates[1]);
                } else if (days.get(1).toString().equals(creditRates[0])) {//判断是否是14天
                    creditRateMax = Double.valueOf(creditRates[1]);
                } else {
                    creditRateMax = Constant.CREDIT_RATE_MAX;
                    creditRateMin = Constant.CREDIT_RATE_MIN;
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            creditRateMax = Constant.CREDIT_RATE_MAX;
            creditRateMin = Constant.CREDIT_RATE_MIN;
        }

        List<String> creditVetList = new ArrayList<String>();

        String creditMinRate = String.valueOf(Double.parseDouble(amountMax + "") * creditRateMin);
        String creditMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * creditRateMax);

        creditVetList.add(creditMinRate.substring(0, creditMinRate.indexOf(".")));
        creditVetList.add(creditMaxRate.substring(0, creditMaxRate.indexOf(".")));
        return creditVetList;
    }

    /**
     * 获取平台使用费
     *
     * @param indexInfo
     * @param user
     * @param days
     * @return
     */
    public static Object getPlatformUse(InfoIndexInfo indexInfo, User user, JSONArray days) {
        loger.info("获取平台使用费使用user");
        Integer amountMax = null;
        Double rateMax = null;
        Double rateMin = null;
        try {
            if (null != user) {
                loger.info("获取平台使用费使用userId=" + user.getId() + " amountmax=" + user.getAmountMax());
                amountMax = Integer.parseInt(user.getAmountMax());
            }
            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 平台使用费配置
            String platformUseRateTypes = "7:0.04;14:0.007";//keys.get("PLATFORM_USE_RATE");
            String platformUseRateTypesArr[] = platformUseRateTypes.split(";");
            for (String c : platformUseRateTypesArr) {
                String platformUseRates[] = c.split(":");
                if (days.get(0).toString().equals(platformUseRates[0])) {//判断是否是7天
                    rateMin = Double.valueOf(platformUseRates[1]);
                } else if (days.get(1).toString().equals(platformUseRates[0])) {//判断是否是14天
                    rateMax = Double.valueOf(platformUseRates[1]);
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        }

        List<String> platformUseList = new ArrayList<String>();

        String platformUseMinRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMin);
        String platformUseMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMax);

        platformUseList.add(platformUseMinRate.substring(0, platformUseMinRate.indexOf(".")));
        platformUseList.add(platformUseMaxRate.substring(0, platformUseMaxRate.indexOf(".")));
        return platformUseList;
    }

    /**
     * 获取代收通道费
     *
     * @param indexInfo
     * @param user
     * @param days
     * @return
     */
    public static Object getCollectionChannel(InfoIndexInfo indexInfo, User user, JSONArray days) {
        loger.info("获取代收通道费使用user");
        Integer amountMax = null;
        Double rateMax = null;
        Double rateMin = null;
        try {
            if (null != user) {
                loger.info("获取代收通道费使用userId=" + user.getId() + " amountmax=" + user.getAmountMax());
                amountMax = Integer.parseInt(user.getAmountMax());
            }
            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 代收通道费配置
            String collectionChannelRateTypes = "7:0.07;14:0.007";//keys.get("COLLECTION_CHANNEL_RATE");
            String collectionChannelRateTypesArr[] = collectionChannelRateTypes.split(";");
            for (String c : collectionChannelRateTypesArr) {
                String collectionChannelUseRates[] = c.split(":");
                if (days.get(0).toString().equals(collectionChannelUseRates[0])) {//判断是否是7天
                    rateMin = Double.valueOf(collectionChannelUseRates[1]);
                } else if (days.get(1).toString().equals(collectionChannelUseRates[0])) {//判断是否是14天
                    rateMax = Double.valueOf(collectionChannelUseRates[1]);
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        }

        List<String> collectionChannelUseList = new ArrayList<String>();

        String collectionChannelMinRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMin);
        String collectionChannelMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMax);

        collectionChannelUseList.add(collectionChannelMinRate.substring(0, collectionChannelMinRate.indexOf(".")));
        collectionChannelUseList.add(collectionChannelMaxRate.substring(0, collectionChannelMaxRate.indexOf(".")));
        return collectionChannelUseList;
    }

    /**
     * 获取到账金额、佣金金额
     *
     * @param indexInfo
     * @return
     */
    public static Object getInterests(InfoIndexInfo indexInfo, User user) {
        loger.info("获取到账金额、佣金金额使用user");
        Integer amountMax = null;
        Double rateMax = null;
        Double rateMin = null;
        try {
            if (null != user) {
                loger.info("获取到账金额、佣金金额使用userId=" + user.getId() + " amountmax=" + user.getAmountMax());
                amountMax = Integer.parseInt(user.getAmountMax());
            }
            String[] rateArr = indexInfo.getRate().split(",");//目前默认7，14天的利率

            if (0 < rateArr.length && rateArr.length == 2) {
                rateMax = Double.parseDouble(rateArr[1]);
                rateMin = Double.parseDouble(rateArr[0]);
            } else {
                rateMax = Constant.RATE_MAX;
                rateMin = Constant.RATE_MIN;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(indexInfo.getCardAmount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            rateMax = Constant.RATE_MAX;
            rateMin = Constant.RATE_MIN;
        }

        List<String> interestList = new ArrayList<String>();

        String amountMinRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMin);
        String amountMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * rateMax);

        interestList.add(amountMinRate.substring(0, amountMinRate.indexOf(".")));
        interestList.add(amountMaxRate.substring(0, amountMaxRate.indexOf(".")));
        return interestList;
    }

    /**
     * 获取借款日期
     *
     * @param index
     * @return
     */
    public static Object getDays(InfoIndex index) {
        String[] strArr = index.getDay().split(",");
        loger.info("获取借款日期:" + strArr.toString());
        List<String> daysList = new ArrayList<String>();
        for (String string : strArr) {
            daysList.add(string);
        }
        return daysList;
    }

    /**
     * 获取认证
     *
     * @param pass1
     * @param pass2
     * @return
     */
    public static Object getVerifyLoanPass(String pass1, String pass2) {
        loger.info("获取认证getVerifyLoanPass-pass1=" + pass1 + " pass2=" + pass2);
        if (StringUtils.isNotBlank(pass1) && StringUtils.isNotBlank(pass2)) {
            if (pass1.equals(pass2)) {//认证完成(需要完成必填项的5项)
                return 1;
            }
        }
        return 0;
    }

    /**
     * 2.2.0版本认证
     *
     * @param authSum
     * @param authCount
     * @param authBank
     * @return
     */
    public static Object getVerifyLoanPass2(String authSum, String authCount, String authBank) {
        loger.info("获取认证getVerifyLoanPass2-authSum=" + authSum + " authCount=" + authCount + "authBank=" + authBank);
        if (StringUtils.isNotBlank(authSum) && StringUtils.isNotBlank(authCount)) {
            if (Integer.valueOf(authSum) == 4 && Integer.valueOf(authBank) == 0) {//认证完成(完成除银行卡的必填项4项外)
                return 1;
            }
            if (authSum.equals(authCount)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 获取用户借款信息
     *
     * @param bo
     * @return
     */
    public static String getLoanInfos(InfoIndexInfo indexInfo, BorrowOrder bo, RepaymentService repaymentService) {
        loger.info("getLoanInfos-indexInfo:" + (indexInfo == null?"null":indexInfo.toString()));
        loger.info("getLoanInfos-BorrowOrder:" +(bo == null?"null":bo.toString()));
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> buttonMap = null;//当审核拒绝时，显示button
        String intoMoney = null;//借款金额
        String loanEndTime = null;//loanEndTime 还款时间
        String lastRepaymentD = null;//lastRepaymentD 剩余还款天数
        String header_tip = "申请提交成功";
        map.put("tag", "0");
        map.put("title", "申请提交成功" + DateUtil.getDateFormat(bo.getCreatedAt(), "yyyy-MM-dd HH:mm"));
        map.put("body", "申请借款" + (bo.getMoneyAmount() / 100.00) + "元，期限" + bo.getLoanTerm() + BorrowOrder.loanMethed.get(bo.getLoanMethod()) + "，手续费" + (bo.getLoanInterests() / 100.00) + "元");
        list.add(map);
        map = new HashMap<String, String>();
        // 如果是 待初审(待机审)、初审通过(机审通过)，待复审  显示审核中
//		if(bo.getStatus().equals(BorrowOrder.STATUS_DCS) || bo.getStatus().equals(BorrowOrder.STATUS_CSTG)){
        loger.info("bo.getStatus=" + bo.getStatus());
        if (borrowStatusMap_shenhezhong.containsKey(bo.getStatus())) {

            map.put("tag", "1");
            map.put("title", "审核中");
            map.put("body", "已进入风控审核状态，请您耐心等待");
            list.add(map);
            header_tip = "风控审核中，请您耐心等待";
        } else if(bo.getStatus().equals(BorrowOrder.STATUS_AI)||bo.getStatus().equals(BorrowOrder.STATUS_AI_FAIL)) {
            map.put("tag", "1");
            map.put("title", "审核中");
            map.put("body", "稍后会有审核人员联系你，请保持电话畅通，耐心等待审核");
            list.add(map);
            header_tip = "风控审核中，请您耐心等待";
            // 如果是 机审拒绝、初审驳回、复审驳回  显示审核不通过
        }else if (borrowStatusMap_shenheFail.containsKey(bo.getStatus())) {
            map.put("tag", "2");
            String shTimeStr = bo.getVerifyTrialTime() == null ? "" : DateUtil.getDateFormat(bo.getVerifyTrialTime(), "yyyy-MM-dd HH:mm");
            if (bo.getVerifyReviewTime() != null) {
                shTimeStr = DateUtil.getDateFormat(bo.getVerifyReviewTime(), "yyyy-MM-dd HH:mm");
            }
            map.put("title", "审核不通过" + shTimeStr);
            if (bo.getStatus().equals(BorrowOrder.STATUS_CSBH)) {
                map.put("body", "信用评分不足");
                //<--
                Date vDate = DateUtil.addDay(bo.getVerifyTrialTime(), Constant.DEFAULT_DAYS);//默认再借时间
                String scdDays = DateUtil.getDateFormat(DateUtil.addDay(new Date(), Constant.DEFAULT_DAYS), "yyyy-MM-dd");
                if (null != vDate) {
                    scdDays = DateUtil.getDateFormat(vDate, "yyyy-MM-dd");
                }
                map.put("days", scdDays);
                //-->
                buttonMap = new HashMap<String, String>();
                buttonMap.put("msg", Constant.BUTTON_MSG);
                buttonMap.put("id", indexInfo.getUserId() + "");
            }
            // 如果是 复审拒绝  显示复审备注
            else if (bo.getStatus().equals(BorrowOrder.STATUS_FSBH)) {
                map.put("body", bo.getVerifyReviewRemark());
                buttonMap = new HashMap<String, String>();
                buttonMap.put("msg", Constant.BUTTON_MSG);
                buttonMap.put("id", indexInfo.getUserId() + "");
                //<--
                Date vDate = DateUtil.addDay(bo.getVerifyReviewTime(), Constant.DEFAULT_DAYS);//默认再借时间
                String scdDays = DateUtil.getDateFormat(DateUtil.addDay(new Date(), Constant.DEFAULT_DAYS), "yyyy-MM-dd");
                if (null != vDate) {
                    scdDays = DateUtil.getDateFormat(vDate, "yyyy-MM-dd");
                }
                map.put("days", scdDays);
                //-->
            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FKBH)) {
                map.put("body", bo.getVerifyLoanRemark());
                buttonMap = new HashMap<String, String>();
                buttonMap.put("msg", Constant.BUTTON_MSG);
                buttonMap.put("id", indexInfo.getUserId() + "");
                //<--
                Date vDate = DateUtil.addDay(bo.getVerifyLoanTime(), Constant.DEFAULT_DAYS);//默认再借时间
                String scdDays = DateUtil.getDateFormat(DateUtil.addDay(new Date(), Constant.DEFAULT_DAYS), "yyyy-MM-dd");
                if (null != vDate) {
                    scdDays = DateUtil.getDateFormat(vDate, "yyyy-MM-dd");
                }
                map.put("days", scdDays);
                //-->
            } else {
                map.put("body", "经审核您不符合借款要求");
            }
            map.put("body", "信用评分不足");
            list.add(map);
            header_tip = "审核未通过";

        } else {
            map.put("tag", "0");
            map.put("title", "审核通过" + DateUtil.getDateFormat(bo.getVerifyReviewTime(), "yyyy-MM-dd HH:mm"));
            map.put("body", "恭喜通过风控审核");
            list.add(map);
            header_tip = "审核通过";
            map = new HashMap<String, String>();
            if (bo.getStatus().equals(BorrowOrder.STATUS_FSTG)) {
                map.put("tag", "1");
                map.put("title", "打款审核中");
                map.put("body", "已进入打款审核状态，请您耐心等待");
                list.add(map);
                header_tip = "打款审核中，请耐心等待";
            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FKZ) || bo.getStatus().equals(BorrowOrder.STATUS_FKSB)) {
                if (bo.getPaystatus().equals(BorrowOrder.SUB_SUBMIT)) {
                    map.put("tag", "1");
                    map.put("title", "打款中");
                    map.put("body", "已进入银行处理流程，请您耐心等待");
                    list.add(map);
                    header_tip = "打款中，请耐心等待";
                } else {
                    map.put("tag", "1");
                    map.put("title", "打款中");
                    map.put("body", "已进入打款流程，请您耐心等待");
                    list.add(map);
                    header_tip = "打款中，请耐心等待";
                }
            } else if (bo.getStatus().equals(BorrowOrder.STATUS_FKBH)) {
                map.put("tag", "2");
                map.put("title", "打款审核不通过");
                map.put("body", bo.getVerifyLoanRemark());
                list.add(map);
                header_tip = "审核未通过";
                buttonMap = new HashMap<String, String>();
                buttonMap.put("msg", Constant.BUTTON_MSG);
                buttonMap.put("id", indexInfo.getUserId() + "");
                //<--
                Date vDate = DateUtil.addDay(bo.getVerifyLoanTime(), Constant.DEFAULT_DAYS);//默认再借时间
                String scdDays = DateUtil.getDateFormat(DateUtil.addDay(new Date(), Constant.DEFAULT_DAYS), "yyyy-MM-dd");
                if (null != vDate) {
                    scdDays = DateUtil.getDateFormat(vDate, "yyyy-MM-dd");
                }
                map.put("days", scdDays);
                //-->

            } else{
                map.put("tag", "0");
                map.put("title", "打款成功");
                map.put("body", "打款至银行卡尾号为" + indexInfo.getBankNo().substring(indexInfo.getBankNo().length() - 4, indexInfo.getBankNo().length()));
                list.add(map);
                map = new HashMap<String, String>();
                map.put("tag", "3");
                Map<String, Object> mapT = new HashMap<String, Object>();
                mapT.put("assetOrderId", bo.getId());
                Repayment repayment = repaymentService.findOneRepayment(mapT);
                Long moneyAmount = repayment.getRepaymentAmount() - repayment.getRepaymentedAmount();
                moneyAmount = moneyAmount < 0 ? 0 : moneyAmount;
                intoMoney = String.valueOf(moneyAmount / 100.00);
                loanEndTime = DateUtil.getDateFormat(repayment.getRepaymentTime(), "yyyy-MM-dd");
                lastRepaymentD = String.valueOf(DateUtil.daysBetween(new Date(), repayment.getRepaymentTime()));
                if (bo.getStatus().equals(BorrowOrder.STATUS_HKZ) || bo.getStatus().equals(BorrowOrder.STATUS_BFHK)) {
                    int days = 14;
                    try {
                        days = DateUtil.daysBetween(new Date(), repayment.getRepaymentTime());
                        lastRepaymentD = String.valueOf(days);
                    } catch (Exception e) {
                        lastRepaymentD = "0";
                        loger.error("计算剩余还款天数错误 repayId=" + repayment.getId(), e);
                    }
                    map.put("title", days + "天后还款");
                    map.put("body", "请于" + DateUtil.getDateFormat(repayment.getRepaymentTime(), "yyyy-MM-dd") + "前将还款金额存入银行卡中");
                } else if (bo.getStatus().equals(BorrowOrder.STATUS_BFHK)) {
                    map.put("title", "部分还款");
                    map.put("body", "请于" + DateUtil.getDateFormat(repayment.getRepaymentTime(), "yyyy-MM-dd") + "前将还款金额存入银行卡中");
                } else if (bo.getStatus().equals(BorrowOrder.STATUS_YYQ)) {
                    map.put("title", "已逾期");
                    map.put("body", "您的借款已逾期，请尽快完成还款操作");
                } else if (bo.getStatus().equals(BorrowOrder.STATUS_YHZ)) {
                    map.put("title", "已坏账");
                    map.put("body", "您的借款已坏账，详情请联系客服");
                } else {// if(bo.getStatus().equals(BorrowOrder.STATUS_YHK))
                    map.put("title", "已还款");
                    map.put("body", "恭喜还款成功，又积攒了一份信用");
                }
                list.add(map);
                header_tip = "还款中，请按时还款";
            }
        }

        Collections.reverse(list);//倒序排列
        reMap.put("lists", list);
        reMap.put("header_tip", header_tip);
        if (null != buttonMap) {
            reMap.put("button", buttonMap);
        }
        if (StringUtils.isNotBlank(intoMoney)) {
            reMap.put("intoMoney", intoMoney);
        }
        if (StringUtils.isNotBlank(loanEndTime)) {
            reMap.put("loanEndTime", loanEndTime);
        }
        if (null != lastRepaymentD) {
            reMap.put("lastRepaymentD", lastRepaymentD);
        }
        return JSONUtil.beanToJson(reMap);
    }


    //多米优可能会用到

    /**
     * 获取利息
     *
     * @param index
     * @return
     */
    public static Object getAccrualDays(InfoIndex index) {
        loger.info("获取借款利息费使用user");
        Integer amountMax = null;
        Double accrualMax = null;
        Double accrualMin = null;
        try {

            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 借款利息费配置
            String accrualrateTypes = "7:0.005;14:0.007";//keys.get("ACCRUAL_RATE");
            String accrualRateTypesArr[] = accrualrateTypes.split(";");
            String days[] = index.getDay().split(",");
            for (String a : accrualRateTypesArr) {
                String accrualRates[] = a.split(":");
                if (days[0].toString().equals(accrualRates[0])) {//判断是否是7天
                    accrualMin = Double.valueOf(accrualRates[1]);
                } else if (days[1].toString().equals(accrualRates[0])) {//判断是否是14天
                    accrualMax = Double.valueOf(accrualRates[1]);
                } else {
                    accrualMax = Constant.ACCRUAL_RATE_MAX;
                    accrualMin = Constant.ACCRUAL_RATE_MIN;
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            accrualMax = Constant.ACCRUAL_RATE_MAX;
            accrualMin = Constant.ACCRUAL_RATE_MIN;
        }
        List<String> accrualList = new ArrayList<String>();
        String creditMinRate = String.valueOf(Double.parseDouble(amountMax + "") * accrualMin);
        String creditMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * accrualMax);
        accrualList.add(creditMinRate.substring(0, creditMinRate.indexOf(".")));
        accrualList.add(creditMaxRate.substring(0, creditMaxRate.indexOf(".")));
        return accrualList;
    }


    /**
     * 获取账户管理费
     *
     * @param index
     * @return
     */
    public static Object getAccountManageDays(InfoIndex index) {
        loger.info("获取账户管理费使用user");
        Integer amountMax = null;
        Double accountManageMax = null;
        Double accountManageMin = null;
        try {

            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 账户管理费配置
            String accountManageRateTypes = "7:0.04;14:0.063";//keys.get("ACCOUNT_MANAGE_RATE");
            String accountManageRateTypesArr[] = accountManageRateTypes.split(";");
            String days[] = index.getDay().split(",");
            for (String a : accountManageRateTypesArr) {
                String accountManageRates[] = a.split(":");
                if (days[0].toString().equals(accountManageRates[0])) {//判断是否是7天
                    accountManageMin = Double.valueOf(accountManageRates[1]);
                } else if (days[1].toString().equals(accountManageRates[0])) {//判断是否是14天
                    accountManageMax = Double.valueOf(accountManageRates[1]);
                } else {
                    accountManageMax = Constant.ACCOUNT_MANAGE_RATE_MAX;
                    accountManageMin = Constant.ACCOUNT_MANAGE_RATE_MIN;
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            accountManageMax = Constant.ACCOUNT_MANAGE_RATE_MAX;
            accountManageMin = Constant.ACCOUNT_MANAGE_RATE_MIN;
        }

        List<String> accountManageList = new ArrayList<String>();

        String creditMinRate = String.valueOf(Double.parseDouble(amountMax + "") * accountManageMin);
        String creditMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * accountManageMax);

        accountManageList.add(creditMinRate.substring(0, creditMinRate.indexOf(".")));
        accountManageList.add(creditMaxRate.substring(0, creditMaxRate.indexOf(".")));
        return accountManageList;
    }

    /**
     * 获取信审查询费用
     *
     * @param index
     * @return
     */
    public static Object getCreditVetDays(InfoIndex index) {
        loger.info("获取信审查询费使用user");
        loger.info("index = " + JSON.toJSONString(index));
        Integer amountMax = null;
        Double creditRateMax = null;
        Double creditRateMin = null;
        try {

            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 信审查询费配置
            String creditRateTypes = "7:0.04;14:0.08";//keys.get("CREDIT_RATE");
            String creditRateTypesArr[] = creditRateTypes.split(";");
            String days[] = index.getDay().split(",");
            for (String c : creditRateTypesArr) {
                String creditRates[] = c.split(":");
                if (days[0].toString().equals(creditRates[0])) {//判断是否是7天
                    creditRateMin = Double.valueOf(creditRates[1]);
                } else if (days[1].toString().equals(creditRates[0])) {//判断是否是14天
                    creditRateMax = Double.valueOf(creditRates[1]);
                } else {
                    creditRateMax = Constant.CREDIT_RATE_MAX;
                    creditRateMin = Constant.CREDIT_RATE_MIN;
                }
            }

            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }

        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
            creditRateMax = Constant.CREDIT_RATE_MAX;
            creditRateMin = Constant.CREDIT_RATE_MIN;
        }

        List<String> creditVetList = new ArrayList<String>();

        String creditMinRate = String.valueOf(Double.parseDouble(amountMax + "") * creditRateMin);
        String creditMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * creditRateMax);
        creditVetList.add(creditMinRate.substring(0, creditMinRate.indexOf(".")));
        creditVetList.add(creditMaxRate.substring(0, creditMaxRate.indexOf(".")));
        loger.info("creditVetList = " + JSON.toJSONString(creditVetList));
        return creditVetList;
    }

    /**
     * 获取平台使用费
     *
     * @param index
     * @return
     */
    public static Object getPlatformUse(InfoIndex index) {
        loger.info("获取平台使用费user");
        Integer amountMax = null;
        Double platformUseMax = null;
        Double platformUseMin = null;
        try {

            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 平台使用费配置
            String platformUseRateTypes = "7:0.04;14:0.007";//keys.get("PLATFORM_USE_RATE")
            String platformUseRateRateTypesArr[] = platformUseRateTypes.split(";");
            String days[] = index.getDay().split(",");
            for (String a : platformUseRateRateTypesArr) {
                String platformUseRates[] = a.split(":");
                if (days[0].toString().equals(platformUseRates[0])) {//判断是否是7天
                    platformUseMin = Double.valueOf(platformUseRates[1]);
                } else if (days[1].toString().equals(platformUseRates[0])) {//判断是否是14天
                    platformUseMax = Double.valueOf(platformUseRates[1]);
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        }

        List<String> platformUseList = new ArrayList<String>();

        String platformUseMinRate = String.valueOf(Double.parseDouble(amountMax + "") * platformUseMin);
        String platformUseMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * platformUseMax);

        platformUseList.add(platformUseMinRate.substring(0, platformUseMinRate.indexOf(".")));
        platformUseList.add(platformUseMaxRate.substring(0, platformUseMaxRate.indexOf(".")));
        return platformUseList;
    }

    /**
     * 获取代收通道费
     *
     * @param index
     * @return
     */
    public static Object getCollectionChannel(InfoIndex index) {
        loger.info("获取代收通道费user");
        Integer amountMax = null;
        Double collectionChannelMax = null;
        Double collectionChannelMin = null;
        try {

            Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.SYS_FEE);
            //TODO wison 代收通道费配置
            String collectionChannelRateTypes = "7:0.07;14:0.007";//keys.get("COLLECTION_CHANNEL_RATE");
            String collectionChannelRateRateTypesArr[] = collectionChannelRateTypes.split(";");
            String days[] = index.getDay().split(",");
            for (String a : collectionChannelRateRateTypesArr) {
                String collectionChannelRates[] = a.split(":");
                if (days[0].toString().equals(collectionChannelRates[0])) {//判断是否是7天
                    collectionChannelMin = Double.valueOf(collectionChannelRates[1]);
                } else if (days[1].toString().equals(collectionChannelRates[0])) {//判断是否是14天
                    collectionChannelMax = Double.valueOf(collectionChannelRates[1]);
                }
            }
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        } catch (Exception e) {
            try {
                if (null == amountMax) {
                    amountMax = Integer.parseInt(index.getCard_amount());
                }
            } catch (Exception e0) {
                amountMax = Constant.AMOUNT_MAX;
            }
        }

        List<String> collectionChannelList = new ArrayList<String>();

        String collectionChannelMinRate = String.valueOf(Double.parseDouble(amountMax + "") * collectionChannelMin);
        String collectionChannelMaxRate = String.valueOf(Double.parseDouble(amountMax + "") * collectionChannelMax);

        collectionChannelList.add(collectionChannelMinRate.substring(0, collectionChannelMinRate.indexOf(".")));
        collectionChannelList.add(collectionChannelMaxRate.substring(0, collectionChannelMaxRate.indexOf(".")));
        return collectionChannelList;
    }

}
