package com.vxianjin.gringotts.web.util.autorisk;


import com.google.gson.*;
import com.vxianjin.gringotts.risk.pojo.Advice;
import com.vxianjin.gringotts.risk.pojo.Reason;
import com.vxianjin.gringotts.risk.pojo.Supplier;
import com.vxianjin.gringotts.util.PhoneUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.StringDateUtils;
import com.vxianjin.gringotts.web.pojo.autorisk.CallInfo;
import com.vxianjin.gringotts.web.pojo.autorisk.NumberCallInfo;
import com.vxianjin.gringotts.web.pojo.autorisk.ShuJuMoHeVo;
import com.vxianjin.gringotts.web.pojo.autorisk.TdTopTen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class ParseSjmhUntil {
    private static Logger LOG = LoggerFactory.getLogger(ParseSjmhUntil.class);

    /**
     * 运营商top10
     *
     * @param numberCallInfoMap
     * @param numberMap
     * @return
     */
    private static List<TdTopTen> getTdTopTenList(Map<String, NumberCallInfo> numberCallInfoMap,
                                                  Map<String, String> numberMap) {

        List<NumberCallInfo> numberCallInfoList = new ArrayList<>();
        List<TdTopTen> tdTopTens = new ArrayList<>();
        List<String> callInList = new ArrayList<>();
        List<String> callOutList = new ArrayList<>();

        numberCallInfoMap.forEach((k, v) -> {
            if (!"未知".equals(k)) {
                numberCallInfoList.add(v);
            }
        });

        //呼入倒排序
        numberCallInfoList.sort((NumberCallInfo n1, NumberCallInfo n2) -> -((Integer) n1.getFrequencyIn()).compareTo(n2.getFrequencyIn()));

        for (int i = 0; i < 10; i++) {
            if (numberCallInfoList.size() > i) {
                TdTopTen tdTopTen = new TdTopTen();
                tdTopTen.setRank(i + 1);
                tdTopTen.setCallInNumber(numberCallInfoList.get(i).getCallNumber());
                callInList.add(numberCallInfoList.get(i).getCallNumber());
                tdTopTen.setCallInFrequency(numberCallInfoList.get(i).getFrequencyIn() + "");
                if (numberMap != null && numberMap.containsKey(numberCallInfoList.get(i).getCallNumber())) {
                    tdTopTen.setCallInPerson(numberMap.get(numberCallInfoList.get(i).getCallNumber()));
                }
                tdTopTens.add(tdTopTen);
            }
        }

        //呼出倒排序
        numberCallInfoList.sort((NumberCallInfo n1, NumberCallInfo n2) -> -((Integer) n1.getFrequencyOut()).compareTo(n2.getFrequencyOut()));

        for (int i = 0; i < 10; i++) {
            if (numberCallInfoList.size() > i && i < tdTopTens.size()) {
                TdTopTen tdTopTen = tdTopTens.get(i);
                tdTopTen.setCallOutNumber(numberCallInfoList.get(i).getCallNumber());
                callOutList.add(numberCallInfoList.get(i).getCallNumber());
                tdTopTen.setCallOutFrequency(numberCallInfoList.get(i).getFrequencyOut() + "");
                if (numberMap != null && numberMap.containsKey(numberCallInfoList.get(i).getCallNumber())) {
                    tdTopTen.setCallOutPerson(numberMap.get(numberCallInfoList.get(i).getCallNumber()));
                }
            }
        }

        //碰撞率
        for (TdTopTen tdTopTen : tdTopTens) {
            if (callOutList.contains(tdTopTen.getCallInNumber())) {
                tdTopTen.setCallInSameFlag(true);
            } else {
                tdTopTen.setCallInSameFlag(false);
            }
            if (callInList.contains(tdTopTen.getCallOutNumber())) {
                tdTopTen.setCallOutSameFlag(true);
            } else {
                tdTopTen.setCallOutSameFlag(false);
            }
        }
        return tdTopTens;
    }

    private static Set<String> getNumberSet(Map<String, String> numberMap) {
        Set<String> resSet = new HashSet<>();
        numberMap.forEach((k, v) -> {
            resSet.add(k);
        });
        return resSet;
    }

    public ShuJuMoHeVo getShuJuMoHeVo(String response, Map<String, String> phoneMap, String firstCall, String secondCall) {
        ShuJuMoHeVo shuJuMoHeVo = new ShuJuMoHeVo();
        Reason reason = new Reason();

        if (StringUtils.isBlank(response)) {
            LOG.error("解析数据魔盒运营商数据失败, 返回报文为空");
            return null;
        }

        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(response).getAsJsonObject();

        //item
        Gson gson = new Gson();

        JsonObject data = root.get("data").getAsJsonObject();

        if (!root.has("data") || !root.get("data").getAsJsonObject().has("task_data")) {
            LOG.error("解析数据魔盒运营商数据失败, creditMessage.responseBody=" + response);
            return null;
        }
        JsonObject taskData = root.get("data").getAsJsonObject().get("task_data").getAsJsonObject();

        if (!taskData.has("call_info")) {
            LOG.error("解析数据魔盒运营商数据失败, creditMessage.responseBody=" + response);
            return null;
        }

        //supplier
        reason.setSupplier(Supplier.SJMH.toString());

        //通讯录人数
        int person = 0;

        //通讯录撞库率
        double sameRate = 0.0;


        JsonObject accontInfo = taskData.get("account_info").getAsJsonObject();

        shuJuMoHeVo.setPhoneNumber(data.get("user_mobile").getAsString());
        String mobileStatus = accontInfo.get("mobile_status").getAsString();
        shuJuMoHeVo.setAccountType(accontInfo.get("mobile_status").getAsString());
        shuJuMoHeVo.setAccountStart(accontInfo.get("credit_level").getAsString());

        JsonArray callInfoArray = taskData.get("call_info").getAsJsonArray();
        JsonArray billInfoArray = taskData.get("bill_info").getAsJsonArray();

        // 月份列表，倒排序
        List<String> monthList = getMonthList(callInfoArray);
        reason.getItems().put("月份列表", gson.toJson(monthList));

        if (monthList == null || monthList.size() == 0) {
            LOG.error("解析数据魔盒运营商数据失败, creditMessage.responseBody=" + response);
            String message = "数据缺失";
            return null;
        }

        Map<String, Map<String, List<CallInfo>>> monthCallInfoMap = getMonthCallInfoMap(callInfoArray);
        reason.getItems().put("按月汇总通话记录", gson.toJson(monthCallInfoMap));

        Map<String, NumberCallInfo> numberCallInfoMap = getNumberCallInfoMap(monthCallInfoMap);
        reason.getItems().put("按号码汇总通话记录", gson.toJson(numberCallInfoMap));

        List<TdTopTen> tdTopTenList = getTdTopTenList(numberCallInfoMap, phoneMap);
        int tdTopTenSame = 0;
        for (TdTopTen tdTopTen : tdTopTenList) {
            if (tdTopTen.getCallInSameFlag()) {
                tdTopTenSame++;
            }
        }

        shuJuMoHeVo.setTdTopTenList(tdTopTenList);
        shuJuMoHeVo.setTdTopTenSame(tdTopTenSame + "");

        Set<String> callListSet = new HashSet<>();
        if (phoneMap != null) {
            callListSet = getNumberSet(phoneMap);
        }
        //通讯录人数
        person = callListSet.size();
        int sameNumber = 0;
        for (String s : callListSet) {
            if (numberCallInfoMap.containsKey(s)) {
                System.out.println();
                sameNumber++;
            }
        }
        //通讯录撞库率
        if (person != 0) {
            sameRate = (double) sameNumber / (double) person;
            reason.getItems().put("通讯录撞库率", sameRate + "");
            shuJuMoHeVo.setSameRate(String.format("%.2f", sameRate * 100) + "");
        } else {
            shuJuMoHeVo.setSameRate(0 + "");
        }

        //todo
        String channel = "无数据";
        if (data.has("channel_src")) {
            channel = data.get("channel_src").getAsString();
        }
        shuJuMoHeVo.setChannel(channel);
        //月度话费实际消费
        Map<String, String> billInfoMap = getBillInfoMap(billInfoArray, channel);
        reason.getItems().put("话费", gson.toJson(billInfoMap));

        //手机静默天数
        Map<String, Integer> silenceDays = new HashMap<>();
        //手机连续静默最大天数
        Map<String, Integer> maxSilenceDays = new HashMap<>();
        //月度通话号码数量
        Map<String, Integer> monthNumberSize = new HashMap<>();
        //月度通话总时长
        Map<String, Integer> monthDayDuration = new HashMap<>();
        //月度晚间通话活跃度
        Map<String, String> monthNightFrequency = new HashMap<>();

        StringDateUtils stringDateUtils = new StringDateUtils();
        monthCallInfoMap.forEach((k, v) -> {//月循环

            Set<Integer> callSet = new HashSet<>();
            Set<String> monthNumber = new TreeSet<>();
            List<Integer> dayDuration = new ArrayList<>();
            List<CallInfo> nightCall = new ArrayList<>();
            v.forEach((kk, vv) -> {//号码循环
                monthNumber.add(kk);
                for (CallInfo callInfo : vv) {//通话记录循环
                    callSet.add(Integer.parseInt(callInfo.getTime().substring(8, 10)));
                    if (callInfo.getNightFlag()) {
                        nightCall.add(callInfo);
                    }
                    dayDuration.add(callInfo.getDuration());
                }
            });

            String lastDay = StringDateUtils.getLastDay("yyyy-MM-dd", Integer.parseInt(k.substring(0, 4)), Integer.parseInt(k.substring(5, 7)));
            String today = StringDateUtils.dateToString("yyyy-MM-dd", new Date());
            int monthDays = 0;
            if (lastDay.compareTo(today) >= 1) {
                monthDays = Integer.parseInt(today.substring(8, 10));//今天
            } else {
                monthDays = Integer.parseInt(lastDay.substring(8, 10));//当月最后一天
            }
            int days;
            days = monthDays - callSet.size();
            silenceDays.put(k, days);//手机静默天数
            monthNumberSize.put(k, monthNumber.size()); //月度通话号码数量
            monthNightFrequency.put(k, nightCall.size() + "/" + dayDuration.size());//月度晚间通话活跃度

            int monthdayDurationValue = 0;
            for (Integer integer : dayDuration) {
                monthdayDurationValue += integer;
            }
            monthDayDuration.put(k, monthdayDurationValue / 60);//月度通话总时长

            List<Integer> callList = new ArrayList<>();
            callList.addAll(callSet);
            callList.sort((Integer s1, Integer s2) -> -s1.compareTo(s2));

            int tmp = monthDays;
            int maxDays = 0;
            for (Integer i : callList) {
                if (i < tmp) {
                    int theDays = tmp - i - 1;
                    if (theDays > 0 && theDays > maxDays) {
                        maxDays = theDays;
                    }
                    tmp = i;
                }
            }
            maxSilenceDays.put(k, maxDays); //手机连续静默最大天数
        });

        reason.getItems().put("手机静默天数", gson.toJson(silenceDays));
        reason.getItems().put("手机连续静默最大天数", gson.toJson(maxSilenceDays));
        reason.getItems().put("通话号码数量", gson.toJson(monthNumberSize));
        reason.getItems().put("通话总时长", gson.toJson(monthDayDuration));
        reason.getItems().put("晚间通话活跃度", gson.toJson(monthNightFrequency));

        //与第一紧急联系人最后一次通话时间
        Map<String, String> lastCallTimeFirst = new HashMap<>();
        //与第二紧急联系人最后一次通话时间
        Map<String, String> lastCallTimeSecond = new HashMap<>();
        // TODO: 2017/12/21 String
        //与第一紧急联系人通话次数
        Map<String, String> frequencyFirst = new HashMap<>();
        //与第二紧急联系人通话次数
        Map<String, String> frequencySecond = new HashMap<>();
        //与第一联系人通话次数排名
        Map<String, String> frequencyRankFirst = new HashMap<>();
        //与第二联系人通话次数排名
        Map<String, String> frequencyRankSecond = new HashMap<>();
//todo


        for (String month : monthList) {
            Map<String, List<CallInfo>> monthMap = monthCallInfoMap.get(month);

            List<CallInfo> firstCallArray = monthMap.get(firstCall);
//todo
            if (firstCallArray != null) {
                if (firstCallArray.size() > 0) {
                    lastCallTimeFirst.put(month, firstCallArray.get(0).getTime());
                    frequencyFirst.put(month, firstCallArray.size() + "");
                } else {
                    lastCallTimeFirst.put(month, "0");
                    frequencyFirst.put(month, "0");
                }
            } else {
                lastCallTimeFirst.put(month, "0");
                frequencyFirst.put(month, "0");
            }

            if (monthMap.size() == 0) {
                lastCallTimeFirst.put(month, "无数据");
                frequencyFirst.put(month, "无数据");
            }

            List<CallInfo> secondCallArray = monthMap.get(secondCall);

            if (secondCallArray != null) {
                if (secondCallArray.size() > 0) {
                    lastCallTimeSecond.put(month, secondCallArray.get(0).getTime());
                    frequencySecond.put(month, secondCallArray.size() + "");
                } else {
                    lastCallTimeSecond.put(month, "0");
                    frequencySecond.put(month, "0");
                }
            } else {
                lastCallTimeSecond.put(month, "0");
                frequencySecond.put(month, "0");
            }

            if (monthMap.size() == 0) {
                lastCallTimeSecond.put(month, "无数据");
                frequencySecond.put(month, "无数据");
            }

            //todo
            Map<String, List<CallInfo>> monthArray = monthCallInfoMap.get(month);

            List<String> rankFirstList = new ArrayList<>();
            List<String> rankSecondList = new ArrayList<>();
            monthArray.forEach((k, v) -> {
                if (v.size() > (firstCallArray != null ? firstCallArray.size() : 0)) {
                    rankFirstList.add(k);
                }
                if (v.size() > (secondCallArray != null ? secondCallArray.size() : 0)) {
                    rankSecondList.add(k);
                }
            });

            // TODO: 2017/12/21    frequencyRankFirst.put(month, rankFirstList != null ? rankFirstList.size()+1 : 无);
            frequencyRankFirst.put(month, rankFirstList != null ? (rankFirstList.size() + 1) + "" : "无数据");

            frequencyRankSecond.put(month, rankSecondList != null ? (rankSecondList.size() + 1) + "" : "无数据");
        }

        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("与第一紧急联系人最后一次通话时间", gson.toJson(lastCallTimeFirst));
        resultMap.put("与第二紧急联系人最后一次通话时间", gson.toJson(lastCallTimeSecond));
        resultMap.put("与第一紧急联系人通话次数", gson.toJson(frequencyFirst));
        resultMap.put("与第二紧急联系人通话次数", gson.toJson(frequencySecond));
        resultMap.put("与第一联系人通话次数排名", gson.toJson(frequencyRankFirst));
        resultMap.put("与第二联系人通话次数排名", gson.toJson(frequencyRankSecond));

        //todo
        int monthFee = 0;
        int silenceAverage = 0;
        for (String s : monthList) {
            if (billInfoMap.containsKey(s)) {
                try {
                    monthFee += Integer.parseInt(billInfoMap.get(s));
                } catch (Exception e) {
                }
            }
            if (silenceDays.containsKey(s)) {
                silenceAverage += silenceDays.get(s);
            }
        }
        //todo

        //月均消费
        monthFee = monthFee / (monthList.size());
        reason.getItems().put("月均消费", monthFee + "");

        //月均静默天数
        silenceAverage = silenceAverage / (monthList.size());
        reason.getItems().put("月均静默天数", silenceAverage + "");

        JsonObject accountInfo = taskData.get("account_info").getAsJsonObject();
        //话费余额
        int accountBalance;
        try {
            accountBalance = accountInfo.get("account_balance").getAsInt();
        } catch (Exception e) {
            accountBalance = 0;
        }
        reason.getItems().put("话费余额", accountBalance / 100 + "");


        //todo
        //入网时长
        String netAge;
        try {
            netAge = accountInfo.get("net_age").getAsInt() + "";
        } catch (Exception e) {
            netAge = "无数据";
        }
        reason.getItems().put("入网时长", netAge + "");
        //todo

        //advice
        List<Advice> adviceList = new ArrayList<>();
        //通讯录少于30人
        adviceList.add(adviceTerm0(person));
        //手机使用时长低于6个月
        adviceList.add(adviceTerm1(netAge));
        //月均消费低于30元
        adviceList.add(adviceTerm2(monthFee));
//        //手机号是欠费状态
//        adviceList.add(adviceTerm3(accountBalance));
        //手机号是停机、销户、未激活、未知
        adviceList.add(adviceTerm31(mobileStatus));
        //撞库率异常
        adviceList.add(adviceTerm4(person, sameRate));
        //手机月均静默天数高于20天
        adviceList.add(adviceTerm5(silenceAverage));

        //评估器汇总建议
        reason.setAdvice(evaluatorStrategy(adviceList));

        shuJuMoHeVo.setNetAge(netAge + "");
        shuJuMoHeVo.setAccountBalance((accountBalance / 100) + "");


        Integer frequencyFirstInt = 0;
        Integer frequencySecondInt = 0;

        Integer silenceDaysInt = 0;
        Integer maxSilenceDaysInt = 0;
        Integer monthNumberSizeInt = 0;

        String monthNightFrequencyInit = "";

        Integer billInfoMapInt = 0;
        Integer monthDayDurationInt = 0;

        for (String s : monthList) {
            //todo
            if (frequencyFirst.containsKey(s)) {
                try {
                    frequencyFirstInt += Integer.parseInt(frequencyFirst.get(s));
                } catch (Exception e) {

                }

            }
            if (frequencySecond.containsKey(s)) {
                try {
                    frequencySecondInt += Integer.parseInt(frequencySecond.get(s));
                } catch (Exception e) {

                }

            }
            //todo

            if (silenceDays.containsKey(s)) {
                silenceDaysInt += silenceDays.get(s);
            }
            if (maxSilenceDays.containsKey(s)) {
                maxSilenceDaysInt += maxSilenceDays.get(s);
            }
            if (monthNumberSize.containsKey(s)) {
                monthNumberSizeInt += monthNumberSize.get(s);
            }

            if (monthNightFrequencyInit.equals("")) {
                monthNightFrequencyInit = monthNightFrequency.get(s);
            } else {
                if (monthNightFrequency.containsKey(s)) {
                    try {
                        monthNightFrequencyInit =
                                (Integer.parseInt(monthNightFrequencyInit.split("/")[0]) +
                                        Integer.parseInt(monthNightFrequency.get(s).split("/")[0])) + "/" +
                                        (Integer.parseInt(monthNightFrequencyInit.split("/")[1]) +
                                                Integer.parseInt(monthNightFrequency.get(s).split("/")[1])) + "";
                    } catch (Exception e) {
                    }

                }
            }
            if (billInfoMap.containsKey(s)) {
                try {
                    billInfoMapInt += Integer.parseInt(billInfoMap.get(s));
                } catch (Exception e) {
                }
            }
            if (monthDayDuration.containsKey(s)) {
                monthDayDurationInt += monthDayDuration.get(s);
            }
        }

        frequencyFirst.put("all", frequencyFirstInt + "");
        frequencySecond.put("all", frequencySecondInt + "");


        silenceDays.put("all", silenceDaysInt);
        maxSilenceDays.put("all", maxSilenceDaysInt);
        monthNumberSize.put("all", monthNumberSizeInt);
        monthNightFrequency.put("all", monthNightFrequencyInit);

        billInfoMap.put("all", billInfoMapInt + "");
        monthDayDuration.put("all", monthDayDurationInt);

        shuJuMoHeVo.setLastCallTimeFirst(lastCallTimeFirst);
        shuJuMoHeVo.setLastCallTimeSecond(lastCallTimeSecond);
        shuJuMoHeVo.setFrequencyFirst(frequencyFirst);
        shuJuMoHeVo.setFrequencySecond(frequencySecond);
        shuJuMoHeVo.setFrequencyRankFirst(frequencyRankFirst);
        shuJuMoHeVo.setFrequencyRankSecond(frequencyRankSecond);

        shuJuMoHeVo.setSilenceDays(silenceDays);
        shuJuMoHeVo.setMaxSilenceDays(maxSilenceDays);
        shuJuMoHeVo.setMonthNumberSize(monthNumberSize);
        shuJuMoHeVo.setMonthNightFrequency(monthNightFrequency);

        shuJuMoHeVo.setBillInfoMap(billInfoMap);
        shuJuMoHeVo.setMonthDayDuration(monthDayDuration);

        monthList.add("all");
        shuJuMoHeVo.setMonthList(monthList);

        if (Advice.REJECT.equals(adviceTerm0(person))) {
            shuJuMoHeVo.getAutoList().add("通讯录少于30人");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }
//        if (Advice.REJECT.equals(adviceTerm1(netAge))) {
//            shuJuMoHeVo.getAutoList().add("手机使用时长低于6个月");
//            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
//        }
        if (Advice.REJECT.equals(adviceTerm31(mobileStatus))) {
            shuJuMoHeVo.getAutoList().add("手机号状态：停机");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }
        if (Advice.REJECT.equals(adviceTerm32(mobileStatus))) {
            shuJuMoHeVo.getAutoList().add("手机号状态：销户");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }
        if (Advice.REJECT.equals(adviceTerm33(mobileStatus))) {
            shuJuMoHeVo.getAutoList().add("手机号状态：未激活");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }
        if (Advice.REVIEW.equals(adviceTerm34(mobileStatus))) {
            shuJuMoHeVo.getAutoList().add("手机号状态：未知");
            shuJuMoHeVo.getAdviceList().add(Advice.REVIEW);
        }


        if (Advice.REJECT.equals(adviceTerm42(person, sameRate))) {
            shuJuMoHeVo.getAutoList().add("撞库率低于15%");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }
        if (Advice.REJECT.equals(adviceTerm43(person, sameRate))) {
            shuJuMoHeVo.getAutoList().add("撞库率高于90%");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }


        if (Advice.REJECT.equals(adviceTerm5(silenceAverage))) {
            shuJuMoHeVo.getAutoList().add("手机月均静默天数高于20天");
            shuJuMoHeVo.getAdviceList().add(Advice.REJECT);
        }
        return shuJuMoHeVo;
    }

    /**
     * 机审硬性指标
     * 通讯录少于30人
     *
     * @param person
     * @return
     */
    private Advice adviceTerm0(Integer person) {
        if (person < 30) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机使用时长低于6个月
     *
     * @param
     * @return
     */
    private Advice adviceTerm1(String netAge) {
        Integer Age;
        try {
            Age = Integer.parseInt(netAge);
            if (Age < 6) {
                return Advice.REJECT;
            }
        } catch (Exception e) {
            return Advice.PASS;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 月均消费低于30元
     *
     * @param fee
     * @return
     */
    private Advice adviceTerm2(Integer fee) {
        if (fee < 30) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机号是欠费状态
     *
     * @param accountBalance
     * @return
     */
    private Advice adviceTerm3(Integer accountBalance) {
        if (accountBalance < 0) {
            return Advice.REJECT;
        }
        if (accountBalance == 0) {
            return Advice.REVIEW;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机号状态
     *
     * @param mobileStatus
     * @return
     */
    private Advice adviceTerm31(String mobileStatus) {
        if ("停机".equals(mobileStatus)) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机号状态
     *
     * @param mobileStatus
     * @return
     */
    private Advice adviceTerm32(String mobileStatus) {
        if ("销户".equals(mobileStatus)) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机号状态
     *
     * @param mobileStatus
     * @return
     */
    private Advice adviceTerm33(String mobileStatus) {
        if ("未激活".equals(mobileStatus)) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机号状态
     *
     * @param mobileStatus
     * @return
     */
    private Advice adviceTerm34(String mobileStatus) {
        if ("未知".equals(mobileStatus)) {
            return Advice.REVIEW;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 撞库率异常
     * 撞库率低于25%（通讯录人数30-70）， 撞库率低于20%（通讯录人数71以上）,撞库率高于90%
     *
     * @param person
     * @param sameRate
     * @return
     */
    private Advice adviceTerm4(Integer person, Double sameRate) {
        if (person > 30 && person < 70 && sameRate < 0.25) {
            return Advice.REJECT;
        }
        if (person > 70 && sameRate < 0.2) {
            return Advice.REJECT;
        }
        if (sameRate > 0.9) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    private Advice adviceTerm41(Integer person, Double sameRate) {
        if (person > 30 && person < 101 && sameRate < 0.2) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    private Advice adviceTerm42(Integer person, Double sameRate) {
        if (sameRate < 0.15) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    private Advice adviceTerm43(Integer person, Double sameRate) {
        if (sameRate > 0.9) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 机审硬性指标
     * 手机月均静默天数高于20天
     *
     * @param silenceAverage
     * @return
     */
    private Advice adviceTerm5(Integer silenceAverage) {
        if (silenceAverage > 20) {
            return Advice.REJECT;
        }
        return Advice.PASS;
    }

    /**
     * 评估器汇总建议
     *
     * @param reasons
     * @return
     */
    private Advice evaluatorStrategy(List<Advice> reasons) {
        if (reasons != null && reasons.contains(Advice.REJECT)) {
            return Advice.REJECT;
        }
        if (reasons != null && reasons.contains(Advice.REVIEW)) {
            return Advice.REVIEW;
        }
        return Advice.PASS;
    }

    /**
     * @param callInfoArray callInfoArray
     * @return 月份列表，倒排序
     */
    private List<String> getMonthList(JsonArray callInfoArray) {
        List<String> month = new ArrayList<>();

        for (JsonElement element : callInfoArray) {
            JsonObject item = element.getAsJsonObject();
            String monthString = item.get("call_cycle").getAsString();
            month.add(monthString);
        }

        month.sort((String d1, String d2) -> -d1.compareTo(d2));
        return month;
    }

    /**
     * 按月汇总通话记录
     *
     * @param callInfoArray call_info
     * @return Map<月份 ,   Map < 电话 ,   List < 通话记录>>>   List<通话记录> 按照通话时间倒排序
     */
    private Map<String, Map<String, List<CallInfo>>> getMonthCallInfoMap(JsonArray callInfoArray) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        //key: 月份
        Map<String, Map<String, List<CallInfo>>> monthCallInfoMap = new HashMap<>();
        //月度报告循环
        for (JsonElement element : callInfoArray) {
            JsonObject item = element.getAsJsonObject();
            String month = item.get("call_cycle").getAsString();

            JsonArray callRecord = item.get("call_record").getAsJsonArray();
            //key: 目标号码   value: 该号码的所有通话记录列表
            Map<String, List<CallInfo>> monthNumberCallInfoMap = new HashMap<>();

            //通话列表循环
            for (JsonElement jsonElement : callRecord) {
                JsonObject jsonItem = jsonElement.getAsJsonObject();

                String callNumber = jsonItem.get("call_other_number").getAsString();
                String inOrOut = jsonItem.get("call_type_name").getAsString();
                String time = jsonItem.get("call_start_time").getAsString();

                //剔除数据魔盒提供的月份混乱的数据
                try {
                    sdf.format(sdf.parse(time));
                } catch (Exception e) {
                    continue;
                }
                String numberMonth = time.substring(0, 7);
                if (!month.equals(numberMonth)) {
                    continue;
                }

                int duration = jsonItem.get("call_time").getAsInt();
                boolean nightFlag = getNightFlag(time);

                CallInfo info = new CallInfo();
                info.setCallNumber(callNumber);
                info.setInOrOut(inOrOut);
                info.setTime(time);
                info.setDuration(duration);
                info.setNightFlag(nightFlag);

                if (monthNumberCallInfoMap.containsKey(callNumber)) {
                    List<CallInfo> callInfoList = monthNumberCallInfoMap.get(callNumber);
                    callInfoList.add(info);
                } else {
                    List<CallInfo> callInfos = new ArrayList<>();
                    callInfos.add(info);
                    monthNumberCallInfoMap.put(callNumber, callInfos);
                }
            }

            //每个号码的列表根据通话时间倒排序
            monthNumberCallInfoMap.forEach((k, v) -> {
                v.sort((CallInfo c1, CallInfo c2) -> -c1.getTime().compareTo(c2.getTime()));
            });

            monthCallInfoMap.put(month, monthNumberCallInfoMap);
        }
        return monthCallInfoMap;
    }


    //todo

    /**
     * @param time
     * @return 夜间活跃标识
     */
    private boolean getNightFlag(String time) {
        String hhmm = time.substring(11, 16);
        return hhmm.compareTo("06:00") < 0 || hhmm.compareTo("21:00") > 0;
    }
    //todo

    /**
     * 按号码汇总通话记录
     *
     * @param monthCallInfoMap
     * @return
     */
    private Map<String, NumberCallInfo> getNumberCallInfoMap(Map<String, Map<String, List<CallInfo>>> monthCallInfoMap) {
        Map<String, NumberCallInfo> map = new HashMap<>();
        monthCallInfoMap.forEach((k, v) -> {
            v.forEach((kk, vv) -> {
                for (CallInfo callInfo : vv) {
                    if (map.containsKey(kk)) {
                        NumberCallInfo info = map.get(kk);
                        info.setDuration(info.getDuration() + callInfo.getDuration());
                        info.setFrequency(info.getFrequency() + 1);
                        if (callInfo.getNightFlag()) {
                            info.setNightFrequency(info.getNightFrequency() + 1);
                        }
                        if ("被叫".equals(callInfo.getInOrOut())) {
                            info.setDurationIn(info.getDurationIn() + callInfo.getDuration());
                            info.setFrequencyIn(info.getFrequencyIn() + 1);
                        } else if ("主叫".equals(callInfo.getInOrOut())) {
                            info.setDurationOut(info.getDurationOut() + callInfo.getDuration());
                            info.setFrequencyOut(info.getFrequencyOut() + 1);
                        }
                    } else {
                        NumberCallInfo newInfo = new NumberCallInfo();
                        newInfo.setCallNumber(callInfo.getCallNumber());
                        newInfo.setDuration(callInfo.getDuration());
                        newInfo.setFrequency(1);
                        if (callInfo.getNightFlag()) {
                            newInfo.setNightFrequency(1);
                        }
                        if ("被叫".equals(callInfo.getInOrOut())) {
                            newInfo.setDurationIn(callInfo.getDuration());
                            newInfo.setFrequencyIn(1);
                        } else if ("主叫".equals(callInfo.getInOrOut())) {
                            newInfo.setDurationOut(callInfo.getDuration());
                            newInfo.setFrequencyOut(1);
                        }
                        map.put(kk, newInfo);
                    }
                }
            });
        });

        PhoneUtil phoneUtil = new PhoneUtil();

        map.forEach(((k, v) -> {
            //运营商
            v.setNetMark(PhoneUtil.phoneNetMark(v.getCallNumber()));
            //归属地
            try {
                v.setLocation(PhoneUtil.phoneLocation(v.getCallNumber()));
            } catch (Exception e) {
                v.setLocation("无效的手机号码");
            }
        }));
        return map;
    }

    /**
     * 按月获取实际使用话费
     *
     * @param billInfoArray
     * @return
     */
    private Map<String, String> getBillInfoMap(JsonArray billInfoArray, String channel) {
        Map<String, String> map = new HashMap<>();
        for (JsonElement jsonElement : billInfoArray) {
            JsonObject item = jsonElement.getAsJsonObject();
            if ("中国联通".equals(channel)) {
                if (!String.valueOf(item.get("bill_fee")).equals("null")) {
                    map.put(item.get("bill_cycle").getAsString(), (item.get("bill_fee").getAsInt() / 100) + "");
                } else {
                    map.put(item.get("bill_cycle").getAsString(), "无数据");
                }
            } else {
                if (!String.valueOf(item.get("bill_total")).equals("null")) {
                    map.put(item.get("bill_cycle").getAsString(), (item.get("bill_total").getAsInt() / 100) + "");
                } else {
                    map.put(item.get("bill_cycle").getAsString(), "无数据");
                }
            }
        }
        return map;
    }
}
