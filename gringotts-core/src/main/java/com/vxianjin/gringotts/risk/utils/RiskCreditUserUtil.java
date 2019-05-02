package com.vxianjin.gringotts.risk.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vxianjin.gringotts.risk.dao.IRiskRuleCalDao;
import com.vxianjin.gringotts.risk.pojo.RiskCreditUser;
import com.vxianjin.gringotts.risk.pojo.RiskManageRule;
import com.vxianjin.gringotts.risk.pojo.RiskRuleCal;
import com.vxianjin.gringotts.risk.utils.jyzx.LoanInfo;
import com.vxianjin.gringotts.risk.utils.jyzx.Pkg2003;
import com.vxianjin.gringotts.util.ThreadPool;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JsonSerializer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 类描述：组装征信相关对象 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-12-17 上午10:49:08 <br>
 */
public class RiskCreditUserUtil {
    public static RiskCreditUserUtil riskCreditUserUtil;
    Logger logger = LoggerFactory.getLogger(getClass());

    public static RiskCreditUserUtil getInstance() {
        if (riskCreditUserUtil == null) {
            riskCreditUserUtil = new RiskCreditUserUtil();
        }
        return riskCreditUserUtil;
    }

    /**
     * 构造芝麻关注度对象
     *
     * @param userId
     * @param creditWatch
     * @return
     */
    public RiskCreditUser createZm(Integer userId, String creditWatch) {
        RiskCreditUser riskCreditUser = new RiskCreditUser();
        try {
            JSONObject re = JSONObject.fromObject(creditWatch);
            Integer zmIndustyBlack = 2;
            Integer zmOver = 0;
            Integer zmNoPayOver = 0;
            if (re.getBoolean("isMatched")) {
                JSONArray jsonArray = re.getJSONArray("details");
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject temp = jsonArray.getJSONObject(j);
                    String type = temp.getString("type");
                    if (type != null && ConstantRisk.ZM_OVER.contains(type)) {
                        zmOver++;
                    }
                    if (ConstantRisk.ZM_OVER_NO_PAY.contains(type)) {
                        zmNoPayOver++;
                    }
                }
                zmIndustyBlack = 1;
            }
            riskCreditUser = new RiskCreditUser(userId, zmIndustyBlack, zmOver,
                    zmNoPayOver);
        } catch (Exception e) {
            logger.error("createZm error ", e);
        }
        return riskCreditUser;
    }


    /**
     * 组装聚信立详情
     *
     * @param params
     * @return
     */
    public RiskCreditUser createJxl(HashMap<String, Object> params) {
        RiskCreditUser riskCreditUser = null;
        try {
            Object userId = params.get("userId");
            Object detail = params.get("detail");
            Object firstContactPhone = params.get("firstContactPhone");
            Object secondContactPhone = params.get("secondContactPhone");
            if (userId != null && detail != null && firstContactPhone != null
                    && secondContactPhone != null) {
                int jxlZjDkNum = 0;
                int jxlBjDkNum = 0;
                BigDecimal yjHf = BigDecimal.ZERO;
                int yjHfHelp = 0;
                int jxlAmthNum = 0;
                int jxlHtPhoneNum = 0;
                int jxlGjTs = 0;
                int link1Num = 0;
                int link2Num = 0;
                int jxlLink2Days = 0;
                int jxlLink1Days = 0;
                int jxlLink2Order = 0;
                int jxlLink1Order = 0;
                int jxlPhoneRegDays = -1;
                Date lastHf = null;
                int jxlTotal = 0;
                int jxlLxGjTs = 0;
                JSONObject jsonObject = JSONObject.fromObject(detail);
                if (jsonObject.containsKey("report_data")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = sdf.parse(sdf.format(new Date()));
                    JSONObject jsonObject2 = jsonObject
                            .getJSONObject("report_data");
                    if (jsonObject2.containsKey("behavior_check")) {
                        JSONArray jsonArray = jsonObject2
                                .getJSONArray("behavior_check");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject3 = jsonArray
                                        .getJSONObject(i);
                                String checkPoint = jsonObject3
                                        .getString("check_point");
                                String evidence = jsonObject3
                                        .getString("evidence");
                                if ("contact_loan".equals(checkPoint)) {
                                    String flag = "次";
                                    String flag2 = "主叫";
                                    String flag3 = "被叫";
                                    String flag4 = "联系列表";
                                    if (evidence.indexOf(flag4) >= 0) {
                                        String[] tmp = evidence.split(";");
                                        if (tmp != null && tmp.length > 0) {
                                            for (String detail2 : tmp) {
                                                while (detail2.indexOf(flag2) >= 0) {
                                                    int index2 = detail2
                                                            .indexOf(flag2);
                                                    int index1 = detail2
                                                            .indexOf(flag);
                                                    jxlZjDkNum += Integer
                                                            .valueOf(detail2
                                                                    .substring(
                                                                            index2
                                                                                    + flag2
                                                                                    .length(),
                                                                            index1));
                                                    detail2 = detail2
                                                            .substring(index1 + 1);
                                                    int index3 = detail2
                                                            .indexOf(flag3);
                                                    int index4 = detail2
                                                            .indexOf(flag);
                                                    jxlBjDkNum += Integer
                                                            .valueOf(detail2
                                                                    .substring(
                                                                            index3
                                                                                    + flag3
                                                                                    .length(),
                                                                            index4));
                                                    detail2 = detail2
                                                            .substring(index4 + 1);
                                                }
                                            }
                                        }
                                    }
                                } else if ("conatct_macao".equals(checkPoint)) {
                                    String flag = "澳门地区号码通话";
                                    String flag2 = "次";
                                    int index = evidence.indexOf(flag);
                                    if (index >= 0) {
                                        int index2 = evidence.indexOf(flag2);
                                        jxlAmthNum += Integer
                                                .valueOf(evidence.substring(
                                                        index + flag.length(),
                                                        index2));
                                    }
                                } else if ("contact_each_other"
                                        .equals(checkPoint)) {
                                    String flag = "互通过电话的号码有";
                                    String flag2 = "个";
                                    int index = evidence.indexOf(flag);
                                    if (index >= 0) {
                                        int index2 = evidence.indexOf(flag2);
                                        jxlHtPhoneNum += Integer
                                                .valueOf(evidence.substring(
                                                        index + flag.length(),
                                                        index2));
                                    }

                                } else if ("phone_silent".equals(checkPoint)) {

                                    String result = jsonObject3
                                            .getString("result");
                                    String flag0 = "天内有";
                                    if (result.indexOf(flag0) >= 0) {
                                        String[] array = result.split(flag0);
                                        if (array != null && array.length > 0) {
                                            String flag = "天";
                                            for (int j = 0; j < array.length; j++) {
                                                String tmp = array[j];
                                                if (j == 0) {
                                                    jxlTotal += Integer
                                                            .valueOf(tmp);
                                                } else {
                                                    int index = tmp
                                                            .indexOf(flag);
                                                    if (index >= 0) {
                                                        jxlGjTs += Integer
                                                                .valueOf(tmp
                                                                        .substring(
                                                                                0,
                                                                                index));
                                                    }
                                                }
                                            }
                                            String flag2 = "次: ";
                                            int index2 = evidence
                                                    .indexOf(flag2);
                                            if (index2 >= 0) {
                                                evidence = evidence
                                                        .substring(index2
                                                                + flag2
                                                                .length());
                                                String[] array2 = evidence
                                                        .split("/");
                                                for (int j = 0; j < array2.length; j++) {
                                                    String tmp = array2[j];
                                                    String flag3 = ", ";
                                                    String flag4 = "天";
                                                    int gj = 0;
                                                    int index3 = tmp
                                                            .indexOf(flag3);
                                                    int index4 = tmp
                                                            .indexOf(flag4);
                                                    if (index3 >= 0
                                                            && index4 >= 0) {
                                                        gj = Integer
                                                                .valueOf(tmp
                                                                        .substring(
                                                                                flag3
                                                                                        .length()
                                                                                        + index3,
                                                                                index4));
                                                        if (gj > jxlLxGjTs) {
                                                            jxlLxGjTs = gj;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                    if (jsonObject2.containsKey("cell_behavior")) {
                        JSONArray jsonArray = jsonObject2
                                .getJSONArray("cell_behavior");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject3 = jsonArray
                                        .getJSONObject(i);
                                JSONArray jsonArray2 = jsonObject3
                                        .getJSONArray("behavior");
                                for (int j = 0; j < jsonArray2.size(); j++) {
                                    JSONObject jsonObject4 = jsonArray2
                                            .getJSONObject(j);
                                    String amount = jsonObject4
                                            .getString("total_amount");
                                    if (!"-1".equals(amount)) {
                                        yjHfHelp += 1;
                                        yjHf = yjHf.add(new BigDecimal(amount));
                                    }
                                    Date tmp = null;
                                    if (jsonObject4.containsKey("cell_mth")) {
                                        tmp = sdf.parse(jsonObject4
                                                .getString("cell_mth")
                                                + "-01");
                                    }
                                    if (i == 0) {
                                        lastHf = tmp;
                                    } else {
                                        int num2 = DateUtil.daysBetween(lastHf,
                                                tmp);
                                        if (num2 > 0) {
                                            lastHf = tmp;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (jsonObject2.containsKey("collection_contact")) {
                        JSONArray jsonArray = jsonObject2
                                .getJSONArray("collection_contact");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject3 = jsonArray
                                        .getJSONObject(i);
                                JSONArray jsonArray2 = jsonObject3
                                        .getJSONArray("contact_details");
                                if (jsonArray2 != null && jsonArray2.size() > 0) {
                                    for (int j = 0; j < jsonArray2.size(); j++) {
                                        JSONObject jsonObject4 = jsonArray2
                                                .getJSONObject(j);
                                        int num = jsonObject4
                                                .getInt("call_cnt");
                                        String phone = jsonObject4
                                                .getString("phone_num");
                                        String transEnd = jsonObject4
                                                .getString("trans_end");
                                        int num2 = 0;
                                        if (!"".equals(transEnd)
                                                && !"null".equals(transEnd)) {
                                            Date end = sdf.parse(transEnd
                                                    .substring(0, 10));
                                            num2 = DateUtil.daysBetween(end,
                                                    now);
                                        }
                                        if (firstContactPhone.equals(phone)) {
                                            link1Num += num;
                                            jxlLink1Days += num2;
                                        } else if (secondContactPhone
                                                .equals(phone)) {
                                            link2Num += num;
                                            jxlLink2Days += num2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (jsonObject2.containsKey("application_check")) {
                        JSONArray jsonArray = jsonObject2
                                .getJSONArray("application_check");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            JSONObject jsonObject4 = jsonObject3
                                    .getJSONObject("check_points");
                            String appPoint = jsonObject3
                                    .getString("app_point");
                            if ("contact".equals(appPoint)) {
                                String phone = jsonObject4
                                        .getString("key_value");
                                String value = jsonObject4
                                        .getString("check_mobile");
                                if (!"没有该联系人电话的通话记录".equals(value)) {
                                    String flag = "名第[";
                                    String flag2 = "]位";
                                    int index = value.indexOf(flag);
                                    int index2 = value.indexOf(flag2);
                                    int num = Integer.valueOf(value.substring(
                                            index + flag.length(), index2));
                                    if (firstContactPhone.equals(phone)) {
                                        jxlLink1Order += num;
                                    } else if (secondContactPhone.equals(phone)) {
                                        jxlLink2Order += num;
                                    }
                                }
                            } else if ("cell_phone".equals(appPoint)) {
                                if (jsonObject4.containsKey("reg_time")) {
                                    String regTime = jsonObject4
                                            .getString("reg_time");
                                    if (StringUtils.isNotBlank(regTime)) {
                                        Date end = sdf.parse(regTime.substring(
                                                0, 10));
                                        jxlPhoneRegDays = DateUtil.daysBetween(
                                                end, now);
                                    }
                                }
                            }
                        }
                    }
                }
                if (yjHfHelp != 0) {
                    yjHf = yjHf.divide(new BigDecimal(yjHfHelp), 2,
                            BigDecimal.ROUND_HALF_UP);
                }
                // 如果没有获取到入网时间
                if (jxlPhoneRegDays == -1) {
                    // 如果有话费账单,以账单月1号作为入网时间
                    if (lastHf != null) {
                        jxlPhoneRegDays = DateUtil.daysBetween(lastHf,
                                new Date());
                    } else {
                        // 没有账单，则入网时间为0
                        jxlPhoneRegDays = 0;
                    }
                }
                BigDecimal jxlGjBl = BigDecimal.ZERO;
                if (jxlTotal != 0) {
                    jxlGjBl = new BigDecimal(jxlGjTs).divide(new BigDecimal(
                            jxlTotal), 2, BigDecimal.ROUND_UP);
                }
                riskCreditUser = new RiskCreditUser(Integer
                        .valueOf(userId + ""), jxlZjDkNum, jxlBjDkNum, yjHf,
                        jxlLink2Days, jxlLink1Days, link2Num, link1Num,
                        jxlLink2Order, jxlLink1Order, jxlGjTs, jxlHtPhoneNum,
                        jxlAmthNum, jxlPhoneRegDays, detail + "", jxlTotal,
                        jxlGjBl, jxlLxGjTs);
            }
        } catch (Exception e) {
            logger.error("createJxl error params=" + params, e);
        }

        return riskCreditUser;
    }

    /**
     * 组装同盾详情
     *
     * @param id
     * @param msg
     * @return
     */
    public RiskCreditUser createTd(Integer id, String msg, String reportId) {
        RiskCreditUser riskCreditUser = null;
        try {
            JSONObject jsonObject = JSONObject.fromObject(msg);
            if (jsonObject.getBoolean("success")) {
                BigDecimal tdScore = new BigDecimal(jsonObject
                        .getString("final_score"));
                JSONArray jsonArray = jsonObject.getJSONArray("risk_items");
                Integer tdPhoneBlack = 0;
                Integer tdCardNumBlack = 0;
                Integer tdMonth1Borrow = 0;
                Integer tdDay7Borrow = 0;
                Integer tdMonth3CardApply = 0;
                Integer tdMonth3ApplyCard = 0;
                Integer tdMonth1CardNumDeviceBorrow = 0;
                Integer tdDay7DeviceCardOrPhoneBorrow = 0;
                Integer tdDay7CardDevice = 0;
                if (jsonArray != null && jsonArray.size() >= 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String itemName = jsonObject2.getString("item_name");
                        JSONObject jsonObject3 = jsonObject2
                                .getJSONObject("item_detail");
                        if (ConstantRisk.TD_PHONE_BLACK.equals(itemName)) {
                            tdPhoneBlack = jsonObject3.getJSONArray(
                                    "namelist_hit_details").size();
                        } else if (ConstantRisk.TD_CARD_BLACK.equals(itemName)) {
                            tdCardNumBlack = jsonObject3.getJSONArray(
                                    "namelist_hit_details").size();
                        } else if (ConstantRisk.TD_MONTH1_BLACK
                                .equals(itemName)) {
                            tdMonth1Borrow = jsonObject3
                                    .getInt("platform_count");
                        } else if (ConstantRisk.TD_DAY7_BLACK.equals(itemName)) {
                            tdDay7Borrow = jsonObject3.getInt("platform_count");
                        } else {
                            if (jsonObject3
                                    .containsKey("frequency_detail_list")) {
                                JSONArray jsonArray2 = jsonObject3
                                        .getJSONArray("frequency_detail_list");
                                if (jsonArray2 != null && jsonArray2.size() > 0) {
                                    for (int j = 0; j < jsonArray2.size(); j++) {
                                        JSONObject jsonObject4 = jsonArray2
                                                .getJSONObject(j);
                                        String detail = jsonObject4
                                                .getString("detail");
                                        int index = detail.indexOf("：") + 1;
                                        if (index >= 0) {
                                            int num = Integer.valueOf(detail
                                                    .substring(index));
                                            if (ConstantRisk.TD_MONTH3_CARD_APPLY
                                                    .equals(itemName)) {
                                                tdMonth3CardApply += num;
                                            } else if (ConstantRisk.TD_MONTH3_APPLY_CARD
                                                    .equals(itemName)) {
                                                tdMonth3ApplyCard += num;
                                            } else if (ConstantRisk.TD_MONTH1_CARD_NUM_DEVICE_BORROW
                                                    .equals(itemName)) {
                                                tdMonth1CardNumDeviceBorrow += num;
                                            } else if (ConstantRisk.TD_DAY7_DEVICE_CARD_OR_PHONE_BORROW
                                                    .equals(itemName)) {
                                                tdDay7DeviceCardOrPhoneBorrow += num;
                                            } else if (ConstantRisk.TD_DAY7_CARD_DEVICE
                                                    .equals(itemName)) {
                                                tdDay7CardDevice += num;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                riskCreditUser = new RiskCreditUser(id, tdScore, tdPhoneBlack,
                        tdCardNumBlack, tdMonth1Borrow, tdDay7Borrow,
                        tdMonth1CardNumDeviceBorrow,
                        tdDay7DeviceCardOrPhoneBorrow, tdDay7CardDevice,
                        tdMonth3ApplyCard, tdMonth3CardApply, reportId);
            }
        } catch (Exception e) {
            logger.error("createTd error id=" + id + ",msg=" + msg, e);
        }
        return riskCreditUser;
    }

    /**
     * 组装白骑士相关信息
     *
     * @param id  征信表主键ID
     * @param msg 白骑士返回的审核意见
     * @return
     */
    public RiskCreditUser createBqs(Integer id, String msg) {
        RiskCreditUser riskCreditUser = null;
        try {
            Integer bqsBlack = 3;
            JSONObject jsonObject = JSONObject.fromObject(msg);
            String result = jsonObject.getString("finalDecision");
            if ("Accept".equals(result)) {
                bqsBlack = 1;
            } else if ("Reject".equals(result)) {
                bqsBlack = 2;
            } else {
                // 建议人工审核
                bqsBlack = 3;
            }
            riskCreditUser = new RiskCreditUser(id, bqsBlack);
        } catch (Exception e) {
            logger.error("createBqs error id=" + id + ",msg=" + msg, e);
        }
        return riskCreditUser;
    }

    /**
     * 组装91征信相关信息
     *
     * @param id  征信表主键
     * @param msg 91返回的信息
     * @return
     */
    public RiskCreditUser createJyzx(Integer id, String msg) {
        RiskCreditUser riskCreditUser = null;
        try {
            Pkg2003 pkg2003 = JsonSerializer.deserializer(msg,
                    new TypeReference<Pkg2003>() {
                    });
            int jyLoanNum = 0;
            int jyJdNum = 0;
            int jyFkNum = 0;
            int jyHkNum = 0;
            BigDecimal jyJdBl = BigDecimal.ZERO;
            int jyOverNum = 0;
            BigDecimal jyOverBl = BigDecimal.ZERO;
            BigDecimal jyHkBl = BigDecimal.ZERO;
            List<LoanInfo> list = pkg2003.getLoanInfos();
            if (list != null && list.size() > 0) {
                jyLoanNum = list.size();
                for (LoanInfo loanInfo : list) {
                    if (loanInfo.getBorrowState() == 1) {
                        jyJdNum++;
                    } else if (loanInfo.getBorrowState() == 2) {
                        jyFkNum++;
                    }
                    short status = loanInfo.getRepayState();
                    if ((status != 0 && status != 1 && status != 9)
                            || loanInfo.getArrearsAmount() > 0) {
                        jyOverNum++;
                    }
                    if (status == 1 || status == 9) {
                        jyHkNum++;
                    }
                }
                jyJdBl = new BigDecimal(jyJdNum).divide(new BigDecimal(
                        jyLoanNum), 2, BigDecimal.ROUND_UP);
                jyOverBl = new BigDecimal(jyOverNum).divide(new BigDecimal(
                        jyLoanNum), 2, BigDecimal.ROUND_UP);
                if (jyFkNum != 0) {
                    jyHkBl = new BigDecimal(jyHkNum).divide(new BigDecimal(
                            jyFkNum), 2, BigDecimal.ROUND_UP);
                }
            }
            riskCreditUser = new RiskCreditUser(id, jyLoanNum, jyJdNum, jyJdBl,
                    jyOverNum, jyOverBl, jyFkNum, jyHkNum, jyHkBl);
        } catch (Exception e) {
            logger.error("createJyzx error id=" + id + ",msg=" + msg, e);
        }
        return riskCreditUser;
    }

    /**
     * 组装密罐相关参数
     *
     * @param id  征信主键ID
     * @param msg 密罐返回信息
     * @return
     */
    public RiskCreditUser createMg(Integer id, String msg) {
        RiskCreditUser riskCreditUser = null;
        /*try {
            Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("user_searched_history_by_orgs",
					UserSearchedHistoryByOrgs.class);
			classMap.put("user_gray", UserGray.class);

			UserGridResponse userGridResponse = (UserGridResponse) JSONObject
					.toBean(JSONObject.fromObject(msg), UserGridResponse.class,
							classMap);
			UserGridInfo userGridInfo = userGridResponse.getGrid_info();
			UserBlacklist userBlacklist = userGridInfo.getUser_blacklist();
			Integer mgBlack = 2;
			if (userBlacklist.isBlacklist_name_with_idcard()
					|| userBlacklist.isBlacklist_name_with_phone()) {
				mgBlack = 1;
			}
			Integer mgDay7Num = 0;
			Integer mgMonth1Num = 0;
			List<UserSearchedHistoryByOrgs> list = userGridInfo
					.getUser_searched_history_by_orgs();
			if (list != null && list.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date now = DateUtil.getDate(sdf.format(new Date()),
						"yyyy-MM-dd");
				for (UserSearchedHistoryByOrgs userSearchedHistoryByOrgs : list) {
					Date date = DateUtil.getDate(userSearchedHistoryByOrgs
							.getSearched_date(), "yyyy-MM-dd");
					int between = DateUtil.daysBetween(date, now);
					if (between <= 30) {
						mgMonth1Num++;
						if (between <= 7) {
							mgDay7Num++;
						}
					}

				}
			}
			UserGray userGray = userGridInfo.getUser_gray();
			riskCreditUser = new RiskCreditUser(id, new BigDecimal(userGray
					.getPhone_gray_score()), mgDay7Num, mgMonth1Num, mgBlack);
		} catch (Exception e) {
			logger.error("createBqs error id=" + id + ",msg=" + msg, e);
		}*/
        return riskCreditUser;
    }

    /**
     * 根据父节点找到所有引用的子节点
     *
     * @param list 要查询子节点的树
     * @param map  所有规则map集合
     */
    public void findSon(List<RiskManageRule> list,
                        Map<String, RiskManageRule> map, String parentId) {
        for (RiskManageRule rule : list) {
            String id = ConstantRisk.RULE_PREFIX + rule.getId();
            if (StringUtils.isBlank(parentId)) {
                parentId = id;
            }
            if (rule.getRuleType().intValue() == RiskManageRule.RULE_BASE
                    .intValue()) {
                rule.setParent(parentId);
            } else if (rule.getRuleType().intValue() == RiskManageRule.RULE_EXTEND
                    .intValue()) {
                String[] array = rule.getFormula().replaceAll(" ", "").split(
                        ConstantRisk.REGEX);
                if (array != null && array.length > 0) {
                    Set<String> set = new HashSet<String>(Arrays.asList(array));
                    List<RiskManageRule> child = new ArrayList<RiskManageRule>();
                    List<String> childIds = new ArrayList<String>();
                    for (String key : set) {
                        if (!"".equals(key)) {
                            key = key.trim();
                            RiskManageRule rule2 = map.get(key);
                            if (rule2 != null) {
                                String id2 = ConstantRisk.RULE_PREFIX
                                        + rule2.getId();
                                if (childIds.contains(id2)) {
                                    continue;
                                }
                                childIds.add(id2);
                                child.add(rule2);
                            }
                        }

                    }
                    if (child != null && child.size() > 0) {
                        rule.setParent(parentId);
                        rule.setChild(child);
                        findSon(child, map, id);
                    }
                }
            }
        }
    }

    public void calSon2Parent(List<RiskManageRule> list,
                              RiskManageRule parentRule, Map<String, String> map,
                              final Integer creditId, final Integer assetId,
                              final Integer userId, final IRiskRuleCalDao riskRuleCalDao) {
        for (int i = 0; i < list.size(); i++) {
            RiskManageRule riskManageRule = list.get(i);
            String id = ConstantRisk.RULE_PREFIX + riskManageRule.getId();
            String parentId = ConstantRisk.RULE_PREFIX + parentRule.getId();
            String parentFormula = parentRule.getFormula();
            String value = "";
            String regex = "\\b" + id + "\\b";
            if (riskManageRule.getRuleType().intValue() == RiskManageRule.RULE_BASE
                    .intValue()) {
                value = map.get(id);
                parentRule.setFormula(parentFormula.replaceAll(regex, value));
                final String fValue = new String(value);
                final String fDetail = new String("");
                final Integer fId = riskManageRule.getId();
                final String fShow = riskManageRule.getFormulaShow();
                final String fName = riskManageRule.getRuleName();
                final Integer fType = riskManageRule.getAttentionType();
                // 多线程入库
                ThreadPool.getInstance().run(new Runnable() {
                    @Override
                    public void run() {
                        riskRuleCalDao.insert(new RiskRuleCal(userId, fId,
                                creditId, assetId, fShow, fValue, fDetail,
                                fName, fType));
                    }
                });
            } else {
                List<RiskManageRule> child = riskManageRule.getChild();
                if (child != null && child.size() > 0) {
                    calSon2Parent(child, riskManageRule, map, creditId,
                            assetId, userId, riskRuleCalDao);
                }
                value = riskManageRule.getFormula();
                if (id.equals(parentId)) {
                    parentFormula = value;
                } else {
                    parentFormula = parentFormula.replaceAll(regex, value);
                }
                parentRule.setFormula(parentFormula);
            }
            parentFormula = parentRule.getFormula();
            if (i == (list.size() - 1) && !id.equals(parentId)) {
                JexlContext ctxt = new MapContext();
                JexlEngine jexl = new JexlEngine();
                if (parentFormula.indexOf(ConstantRisk.RULE_PREFIX) >= 0) {
                    throw new RuntimeException("变量未计算完毕parentFormula="
                            + parentFormula);
                }
                Expression expr = jexl.createExpression(parentFormula);
                value = expr.evaluate(ctxt) + "";
                Matcher matcher = ConstantRisk.PATTERN_CN.matcher(value);
                if (matcher.find()) {
                    value = "'" + value + "'";
                }
                // 计算用户额度的时候出现'81.5>0'这样的信息报错
                // parentRule.setFormula("'" + value + "'");
                parentRule.setFormula(value);
                final String fValue = new String(value);
                final String fDetail = new String("");
                final Integer fId = parentRule.getId();
                final String fShow = parentRule.getFormulaShow();
                final String fName = parentRule.getRuleName();
                final Integer fType = parentRule.getAttentionType();
                // 多线程入库
                ThreadPool.getInstance().run(new Runnable() {
                    @Override
                    public void run() {
                        riskRuleCalDao.insert(new RiskRuleCal(userId, fId,
                                creditId, assetId, fShow, fValue, fDetail,
                                fName, fType));
                    }
                });
            }
        }
    }

    /**
     * 构造宜信对象
     *
     * @param userId
     * @return
     */
    public RiskCreditUser createYx(Integer userId, String msg) {
        RiskCreditUser riskCreditUser = new RiskCreditUser();
        try {
            Integer yxFxNum = 0;
            Integer yxMonth3Over = 0;
            Integer yxLessMonth3Over = 0;
            Integer total = 0;
            JSONObject jsonObject = JSONObject.fromObject(msg);
            JSONObject jsonObject2 = jsonObject.getJSONObject("data");
            if (jsonObject2.containsKey("riskResults")) {
                JSONArray jsonArray = jsonObject2.getJSONArray("riskResults");
                if (jsonArray != null && jsonArray.size() > 0) {
                    yxFxNum = jsonArray.size();
                }
            }
            if (jsonObject2.containsKey("loanRecords")) {
                JSONArray jsonArray = jsonObject2.getJSONArray("loanRecords");
                if (jsonArray != null && jsonArray.size() > 0) {
                    total = jsonArray.size();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        String tmpTotal = jsonObject3.getString("overdueTotal");
                        if (StringUtils.isNotBlank(tmpTotal)) {
                            total += Integer.valueOf(tmpTotal);
                            String tmpM3 = jsonObject3.getString("overdueM3");
                            if (StringUtils.isNotBlank(tmpM3)) {
                                yxMonth3Over += Integer.valueOf(tmpM3);
                            }

                        }
                    }
                }
            }
            yxLessMonth3Over = total - yxMonth3Over;
            riskCreditUser = new RiskCreditUser(userId, yxFxNum, yxMonth3Over,
                    yxLessMonth3Over, 2);
        } catch (Exception e) {
            logger.error("createZm error ", e);
        }
        return riskCreditUser;
    }

}
