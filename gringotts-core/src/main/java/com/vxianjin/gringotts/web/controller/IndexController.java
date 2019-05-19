package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.pay.model.UserQuotaSnapshot;
import com.vxianjin.gringotts.pay.service.BorrowProductConfigService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.pay.service.UserQuotaSnapshotService;
import com.vxianjin.gringotts.util.MapUtils;
import com.vxianjin.gringotts.util.WriteUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.listener.IndexInit;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.index.IndexDto;
import com.vxianjin.gringotts.web.service.IBackConfigParamsService;
import com.vxianjin.gringotts.web.service.IIndexService;
import com.vxianjin.gringotts.web.service.IInfoIndexService;
import com.vxianjin.gringotts.web.service.IReportService;
import com.vxianjin.gringotts.web.service.impl.BorrowOrderService;
import com.vxianjin.gringotts.web.util.IndexUtil;
import com.vxianjin.gringotts.web.utils.SpringUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.borrowStatusMap_shenheFail;

/**
 * 首页
 *
 * @author gaoyuhai
 * 2016-12-10 上午11:29:15
 */
@Controller
public class IndexController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
//    private static String oldUserUrl = "http://super.xianjinxia.com/common/web/images/olduser.png";
    @Resource
    private JedisCluster jedisCluster;
    @Resource
    private IIndexService indexService;
    @Resource
    private IInfoIndexService infoIndexService;
    @Resource
    @Qualifier("reportService")
    private IReportService reportService;
    @Resource
    private BorrowOrderService borrowOrderService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private RepaymentService repaymentService;

    @Resource
    private BorrowProductConfigService borrowProductConfigService;
    /**
     * 用户额度
     */
    @Resource
    private UserQuotaSnapshotService userQuotaSnapshotService;

    /**
     * 更新系统缓存
     * @param response res
     */
    @RequestMapping("updateCache_otherUpdateMe")
    public void updateCacheOtherUpdateMe(HttpServletResponse response) {
        boolean succ;
        try {
            new IndexInit().updateCache();
            //刷新首页缓存zjb
            initIndex();
            succ = true;
        } catch (Exception e) {
            log.error("updateCache error ", e);
            succ = false;
        }
        SpringUtils.renderDwzResult(response, succ, succ ? "刷新缓存成功" : "刷新缓存失败");
    }

    @RequestMapping(value = "getNewAppUpgrade")
    public String getNewAppUpgrade() {
        return "content/newAppUpgrade.jsp";
    }


    /**
     * gotoindex
     * @param request req
     * @param response res
     */
    @RequestMapping(value = "/credit-app/newIndex")
    public void gotoNewIndex(HttpServletRequest request, HttpServletResponse response) {
        log.info(getIpAddr(request) + "  goto index...");
        String deviceId = request.getParameter("deviceId");
        String telephone = request.getParameter("mobilePhone");
        String appVersion = request.getParameter("appVersion");
        String clientType = request.getParameter("clientType");
        //APP新版本升级
        String newVersionUpgradeUrl;
        newVersionUpgradeUrl = PropertiesConfigUtil.get("APP_HOST_API") + "/" + "getNewAppUpgrade";
        log.info("设备号：" + deviceId);
        log.info("手机号码：" + telephone);
        //设备号为空
        if (StringUtils.isBlank(deviceId)) {
            sendDefault3(response, appVersion, newVersionUpgradeUrl);
            log.info("设备号或者手机号不存在,默认展示");
        } else {
            User user = loginFrontUserByDeiceId(request);
            log.info("获取用户 user = " + (user != null ? JSON.toJSONString(user) : "null"));
            try {
                if (null == user) {
                    log.info("获取首页信息 start");
                    sendDefault3(response, appVersion, newVersionUpgradeUrl);
                    log.info("获取首页信息 end");
                    log.info("用户ID不存在，设备号为：" + deviceId + ",手机号为：," + telephone + "默认展示 ");
                } else {
                    sendDynamic3(response, user.getId(), appVersion, newVersionUpgradeUrl);
                }
            } catch (Exception e) {
                sendDefault(response);
            }

        }
    }

    private void sendDynamic3(HttpServletResponse response, String userId, String appVersion,
                              String newVersionUpgradeUrl) {
        log.info("sendDynamic3:userid=" + userId);
        String indexJson =null;
        try {
            //获取用户信息数据
//            indexJson = jedisCluster.get(Constant.CACHE_INDEX_INFO_KEY+userId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("USER_ID", userId);
            //表：info_index_info
            InfoIndexInfo indexInfo = this.indexService.searchInfoIndexInfo(map);
            log.info("sendDynamic3:userid=" + userId + " indexInfo="
                    + (indexInfo != null
                    ? JSON.toJSONString(indexInfo) + " authSum=" + indexInfo.getAuthSum()
                    : "null"));
            //表：user_info
            User user = this.indexService.searchUserByIndex(map);

            if (null == indexInfo) {
                log.info("info_user_info表中无" + userId + "用户记录");
                sendDefault3(response, appVersion, newVersionUpgradeUrl);
            } else {
                user = setMaxAmount(user);
                log.info("sendDynamic3:userid=" + userId + "user =" + JSON.toJSONString(user));
                //infoIndexInfo 存在 开始组装
                //					indexJson = jedisCluster.get(Constant.CACHE_INDEX_KEY);//获取默认数据-备用
                //未获取到
                if (StringUtils.isBlank(indexJson)) {
                    indexJson = initIndex();
                    if (StringUtils.isBlank(indexJson)) {
                        log.info("静态展示-2-1-");
                        //发送静态字符串
                        indexJson = JSONUtil.beanToJson(this.indexService.getDefaultJson());
                    }
                }
                String indexJsonRe = getJsonStr3(indexJson, indexInfo, user,
                        newVersionUpgradeUrl);
                //log.info("sendDynamic3 after indexJson=" + indexJsonRe);
                if (StringUtils.isBlank(indexJsonRe)) {
                    WriteUtil.writeDataToApp(response, indexJson);
                } else {
                    indexJson = indexJsonRe;
                }
                jedisCluster.set(Constant.CACHE_INDEX_INFO_KEY + userId, indexJson);
                log.info("动态返回");
                WriteUtil.writeDataToApp(response, indexJson);
            }
        } catch (Exception e) {
            sendDefault3(response, appVersion, newVersionUpgradeUrl);
            log.error("gotoIndex-exception :" + e);
        }
    }

    private void sendDefault3(HttpServletResponse response, String appVersion,
                              String newVersionUpgradeUrl) {
        log.info("sendDefault3 start");
        String indexJson;
        try {
            //获取默认数据--{"message":"访问首页成功","data":{"user_loan_log_list":["尾号2269，正常还款，成功提额至1050元","尾号6547，
            // 成功借款1000元，申请至放款耗时3分钟","尾号2265，成功借款1000元，申请至放款耗时4分钟","尾号3313，正常还款，成功提额至1050元",
            // "尾号3369，成功借款1000元，申请至放款耗时3分钟","尾号1225，正常还款，成功提额至1050元","尾号6681，成功借款1000元，申请至放款耗时3分钟",
            // "尾号5423，成功借款1000元，申请至放款耗时4分钟","尾号3212，正常还款，成功提额至1050元","尾号7634，成功借款1000元，申请至放款耗时5分钟"],
            // "index_images":[{"title":"你借我还活动","sort":"1","reurl":"http://192.168.1.161:8080/ygmmr/index",
            // "url":"http://super.xianjinxia.com/common/web/images/index_banner0000.png?typeImg=1"},
            // {"title":"首页活动一","sort":"2","reurl":"http://192.168.1.161:8080/jsaward/awardCenter/drawAwardIndex",
            // "url":"http://super.xianjinxia.com/common/web/images/index_banner111.png"},
            // {"title":"首页活动二","sort":"3","reurl":"http://192.168.1.161:8080/gotoDrawAwardsIndex",
            // "url":"http://super.xianjinxia.com/common/web/images/index_banner2.png"},
            // {"title":"首页活动三","sort":"4","reurl":"http://192.168.1.161:8080/gotoAboutIndex",
            // "url":"http://super.xianjinxia.com/common/web/images/index_banner3.png"}],
            // "amount_days_list":{"amounts":["20000","30000","40000","50000","60000","70000","80000","90000",
            // "100000","110000","120000","130000","140000","150000","160000","170000","180000","190000","200000",
            // "210000","220000","230000","240000","250000","260000","270000","280000","290000","300000","310000",
            // "320000","330000","340000","350000","360000","370000","380000","390000","400000","410000","420000",
            // "430000","440000","450000","460000","470000","480000","490000","500000","510000","520000","530000",
            // "540000","550000","560000","570000","580000","590000","600000","610000","620000","630000","640000",
            // "650000","660000","670000","680000","690000","700000","710000","720000","730000","740000","750000",
            // "760000","770000","780000","790000","800000","810000","820000","830000","840000","850000","860000",
            // "870000","880000","890000","900000","910000","920000","930000","940000","950000","960000","970000",
            // "980000","990000","1000000"],"days":["7","14"],"interests":["49000","75000"]},
            // "item":{"verify_loan_pass":0,"card_title":"现金侠","card_amount":"1000000","card_verify_step":"认证0/5","verify_loan_nums":"0"},
            // "today_last_amount":"123400"},"code":"0"}

            indexJson = jedisCluster.get(Constant.CACHE_INDEX_KEY);
            //log.info("CASH_MAN_INDEX = " + indexJson);
            //未获取到
            if (StringUtils.isBlank(indexJson)) {
                indexJson = initIndex();
                if (StringUtils.isBlank(indexJson)) {
                    log.info("静态展示-0-start");
                    //发送静态字符串
                    indexJson = JSONUtil.beanToJson(this.indexService.getDefaultJson());
                    log.info("静态展示-0-end");
                }
            }
            //log.info("indexJson = " + indexJson);
            JSONObject jsonObj = JSONObject.fromObject(indexJson);
            String code = jsonObj.getString("code");
            if ("0".equals(code)) {
                JSONObject data = JSONObject.fromObject(jsonObj.get("data"));
                JSONObject item = JSONObject.fromObject(data.get("item"));
                item.put("new_version_upgrade_url", newVersionUpgradeUrl);
                data.put("item", item);
                data.put("amount_days_list", borrowProductConfigService.queryIndexAmountDayList());
                jsonObj.put("data", data);
                indexJson = jsonObj.toString();
            } else if ("-1".equals(code)) {
                WriteUtil.writeDataToApp(response, indexJson);
            }
            WriteUtil.writeDataToApp(response, indexJson);
        } catch (Exception e) {
            log.error("gotoIndex-exception :" + e);
            log.error("静态展示-1-");
            WriteUtil.writeDataToApp(response, this.indexService.getDefaultJson());
        }
    }

    /**
     * 老版本首页添加banner
     * @param jsonObj obj
     * @param clientType clientType
     * @return json
     */
    private JSONObject addBanner(JSONObject jsonObj, String clientType) {
        String bannerStr = PropertiesConfigUtil.get("old_ios_add_banner");
        if ("ios".equals(clientType) && StringUtils.isNotBlank(bannerStr)) {
            JSONObject data = JSONObject.fromObject(jsonObj.get("data"));
            net.sf.json.JSONArray images = net.sf.json.JSONArray.fromObject(data.get("index_images"));

            JSONObject image = JSONObject.fromObject(bannerStr);
            images.add(image);
            data.put("index_images", images);
            jsonObj.put("data", data);
        }
        return jsonObj;
    }

    /**
     * 返回默认数据
     */
    private void sendDefault(HttpServletResponse response) {
        log.info("sendDefault start");
        String indexJson;
        try {
            indexJson = jedisCluster.get(Constant.CACHE_INDEX_KEY);
            log.info("indexJson1 = " + indexJson);
            //未获取到
            if (StringUtils.isBlank(indexJson)) {
                indexJson = initIndex();
                log.info("indexJson2 = " + indexJson);
                if (StringUtils.isBlank(indexJson)) {
                    log.info("静态展示-0-");
                    //发送静态字符串
                    indexJson = JSONUtil.beanToJson(this.indexService.getDefaultJson());
                }
            }
            log.info("DEFAULT-SEND");
            WriteUtil.writeDataToApp(response, indexJson);
        } catch (Exception e) {
            log.error("gotoIndex-exception :" + e.getMessage());
            log.error("静态展示-1-");
            WriteUtil.writeDataToApp(response, this.indexService.getDefaultJson());
        }
    }

    /**
     * 新版本首页
     */
    private String getJsonStr3(String indexJson, InfoIndexInfo indexInfo, User user, String newVersionUpgradeUrl) {
        log.info("getJsonStr3 start");
        try {
            log.info("getJsonStr3 indexJson=" + indexJson);
            JSONObject jsonObj = JSONObject.fromObject(indexJson);
            String code = jsonObj.getString("code");
            if ("0".equals(code)) {
                JSONObject data = JSONObject.fromObject(jsonObj.get("data"));

                JSONObject item = JSONObject.fromObject(data.get("item"));
                // 可借额度默认1000
                String cardAmount = "160000";
                if (null != user) {
                    cardAmount = user.getAmountMax();
                }
                JSONArray amountDaysList;
                List<UserQuotaSnapshot> userQuotaSnapshots = userQuotaSnapshotService.getUserQuotaSnapshotByUser(user);

                //  判断用户和用户额度是否存在
                if (null != user && userQuotaSnapshots.size() > 0) {
                    log.info("step 1");
                    log.info("getJsonStr2 userId=" + user.getId());
                    //  根据额度获取用户的可借产品线
                    amountDaysList = borrowProductConfigService.queryIndexUserAllowAmountDayList(userQuotaSnapshots);
                }else if(userQuotaSnapshots.size() == 0){
                    // 没有则插入一条初始为1000的数据
                    userQuotaSnapshotService.addUserQuota(Integer.valueOf(user.getId()),3,new BigDecimal(160000),7);
                    String amountDaysListStr = "[{\"day\":7,\"amount_free\":[{\"amount\":160000,\"totalFee\":50000,\"arrivalMoney\":110000,\"creditVet\":4000,\"accountManage\":3000,\"accrual\":35000,\"platformUse\":5000,\"collectionChannel\":3000}]}]";
                    amountDaysList = JSON.parseArray(amountDaysListStr);
                } else {
                    log.info("step 2");
                    // 获取所有额度
                    amountDaysList = borrowProductConfigService.queryIndexAmountDayList();
                }
                data.put("amount_days_list", amountDaysList);

                item.put("card_amount", cardAmount);
                item.put(
                        "card_verify_step", Constant.CARD_VERIFY_STEP
                                + (indexInfo.getAuthInfo() + indexInfo.getAuthContacts()
                                + indexInfo.getAuthMobile() + indexInfo.getAuthSesame())
                                + "/" + 4);


                item.put("verify_loan_nums", indexInfo.getAuthSum());
                item.put("verify_loan_pass",
                        IndexUtil.getVerifyLoanPass2(indexInfo.getAuthSum() + "",
                                indexInfo.getAuthCount() + "", indexInfo.getAuthBank() + ""));

                //总认证数
                item.put("all_auth_Count", indexInfo.getAuthCount());

                item.put("new_version_upgrade_url", newVersionUpgradeUrl);
                //存在借款
                if (indexInfo.getBorrowStatus().equals(Constant.STATUS_VALID)) {
                    if (!checkBankNo(indexInfo)) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("userId", indexInfo.getUserId());
                        //设置银行卡号
                        this.infoIndexService.authBank(map);

                        map = new HashMap<>();
                        map.put("USER_ID", indexInfo.getUserId());
                        //重新查询
                        indexInfo = this.indexService.searchInfoIndexInfo(map);
                        log.info("indexInfo-getBankNo:" + indexInfo.getBankNo());
                    }
                    item.put("loan_infos", this.indexService.getLoanInfos(indexInfo));
                }
                if (null != user) {
                    Map<String, String> interval = borrowOrderService
                            .findAuditFailureOrderByUserId(user.getId());
                    item.put("next_loan_day", interval.get("nextLoanDay"));
                } else {
                    item.put("next_loan_day", 0);
                }
                //获取用户最近借款状态 代替风控
                // 如果最近借款被拒绝, 那么就显示问号, 跳转去其他的app
                // 0 不显示 1显示
                item.put("risk_status", "0");
                //贷款超时链接
                item.put("shop_url", PropertiesConfigUtil.get("SHOP_URL"));
                BorrowOrder bo = borrowOrderService
                        .selectBorrowOrderNowUseId(Integer.valueOf(user.getId()));
                if (bo != null) {
                    //借款审核被拒绝 添加全局控制开关 1 打开 0 关闭
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("sysType", "SYS_NOCACHE");
                    params.put("syskey", "SYJ_SWITCH");
                    //params.put("sysName","");
                    List<BackConfigParams> list = backConfigParamsService.findParams(params);
                    String offon = "1";
                    if (list.size() == 1) {
                        offon = list.get(0).getSysValue();
                    }
                    //驳回情况下显示
                    if (borrowStatusMap_shenheFail.containsKey(bo.getStatus())
                            && "1".equals(offon)) {
                        item.put("shop_url", PropertiesConfigUtil.get("SHOP_URL_FK"));
                        item.put("risk_status", "1");
                    }
                    log.info("bo.getStatus1=" + bo.getStatus());
                    //逾期情况下显示
                    if (BorrowOrder.STATUS_YYQ.equals(bo.getStatus())) {
                        Map<String, Object> param = new HashMap<>();
                        param.put("assetOrderId", bo.getId());
                        param.put("userId", bo.getUserId());
                        log.info("param = " + JSON.toJSONString(param));
                        Repayment repayment = repaymentService.findOneRepayment(param);
                        log.info("repayment=" + JSON.toJSONString(repayment));
                        Integer overdueDays = Integer
                                .parseInt(PropertiesConfigUtil.get("SHOP_URL_OVERDUE_DAYS"));
                        log.info("overdue_days=" + overdueDays + " lateDays=" + repayment.getLateDay()
                                        + " flag=" + (repayment.getLateDay() >= overdueDays));
                        if (null != repayment && "1".equals(offon)
                                && BorrowOrder.STATUS_YYQ.equals(repayment.getStatus())
                                && repayment.getLateDay() >= overdueDays) {
                            item.put("shop_url", PropertiesConfigUtil.get("SHOP_URL_YQ"));
                            item.put("risk_status", "1");
                        }
                    }

                    //借款审核被拒绝 贷超链接显示方式
                    String alterType = "1";
                    params.put("sysType", "SYS_NOCACHE");
                    params.put("syskey", "ALTER_LINK_TYPE");
                    List<BackConfigParams> paramList = backConfigParamsService.findParams(params);
                    if (paramList.size() == 1) {
                        alterType = paramList.get(0).getSysValue();
                    }
                    item.put("alter_type", alterType);
                }
                data.put("item", item);

                jsonObj.put("data", data);
                return jsonObj.toString();
            } else if ("-1".equals(code)) {
                return indexJson;
            }
        } catch (Exception e) {
            log.error("静态展示-3-");
            return this.indexService.getDefaultJson();
        }
        log.info("静态展示-4-");
        return this.indexService.getDefaultJson();
    }

    /**
     * 设置第二版amounts为0的问题
     * @param user user
     * @return user
     */
    private User setMaxAmount(User user) {
        String min = "1";
        String max = "1";
        if (StringUtils.isNotBlank(user.getAmountMin())) {
            if (!"0".equals(user.getAmountMin())) {
                min = user.getAmountMin();
            }
        }
        user.setAmountMin(min);
        if (StringUtils.isNotBlank(user.getAmountMax())) {
            if (!"0".equals(user.getAmountMax())) {
                max = user.getAmountMax();
            }
        }
        user.setAmountMax(max);
        return user;
    }

    /**
     * 初始化index
     */
    private String initIndex() {
        log.info("indexController-initIndex start...");
        HashMap<String, Object> dtoMap = new HashMap<>();
        //表：info_index_cache
        IndexDto iDto = this.indexService.searchIndexDto(dtoMap);
        if (null != iDto && StringUtils.isNotBlank(iDto.getContent())) {
            return iDto.getContent();
        }

        HashMap<String, Object> resultMap = MapUtils.getResultMap("0", "访问首页成功");
        //存放数据
        HashMap<String, Object> dataMap = new HashMap<>();

        HashMap<String, Object> map = new HashMap<>();
        map.put("NOT_SELECT", Constant.STATUS_VALID);
        //表：info_index
        //查询首页内容
        InfoIndex index = this.indexService.searchInfoIndex(map);

        List<InfoNotice> noticeList;

        if (null != index) {
            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("card_title", index.getCard_title());
            itemMap.put("card_amount", index.getCard_amount());
            itemMap.put("card_verify_step",
                    Constant.CARD_VERIFY_STEP + index.getAuth_min() + "/" + index.getAuth_max());
            itemMap.put("verify_loan_pass",
                    IndexUtil.getVerifyLoanPass(index.getAuth_min(), index.getAuth_max()));
            itemMap.put("verify_loan_nums", index.getAuth_min());
            dataMap.put("item", itemMap);

            HashMap<String, Object> amountDaysListMap = new HashMap<>();
            //借款日期
            amountDaysListMap.put("days", IndexUtil.getDays(index));
            //到账金额
            amountDaysListMap.put("interests", IndexUtil.getInterests(index));
            //金额列表
            amountDaysListMap.put("amounts", IndexUtil.getAmounts(index));
            //信审查询费
            amountDaysListMap.put("creditVet", IndexUtil.getCreditVetDays(index));
            //账户管理费
            amountDaysListMap.put("accountManage", IndexUtil.getAccountManageDays(index));
            //利息
            amountDaysListMap.put("accrual", IndexUtil.getAccrualDays(index));
            //平台使用费
            amountDaysListMap.put("platformUse", IndexUtil.getPlatformUse(index));
            //代收通道费
            amountDaysListMap.put("collectionChannel", IndexUtil.getCollectionChannel(index));

            //多米优 可能会用到
            //			amountDaysListMap.put("creditVet", IndexUtil.getAmounts(index));//金额列表
            //			amountDaysListMap.put("accountManage", IndexUtil.getAccountManageDays(index));//金额列表
            //			amountDaysListMap.put("accrual", IndexUtil.getAccrualDays(index));//利息

            dataMap.put("amount_days_list", amountDaysListMap);

            Integer noticeSize = null;
            List<InfoImage> imageList;
            try {
                noticeSize = Integer.parseInt(index.getNotice_size());
            } catch (Exception e) {
                log.error("number format error:{}",e);
            }
            if (noticeSize == null) {
                noticeSize = Constant.INDEX_LIMIT;
            }

            map.put("COUNT_XJX", noticeSize);
            //查询公告
            noticeList = this.indexService.searchInfoNoticeByIndex(map);
            log.info("noticeList size = " + noticeList.size());
            dataMap.put("user_loan_log_list", IndexUtil.getUserLoanLogList(noticeList));

            map.put("STATUS", Constant.STATUS_VALID);
            //查询图片
            imageList = indexService.searchInfoImage(map);
            dataMap.put("index_images", IndexUtil.getInfoImageList(imageList));

            dataMap.put("today_last_amount", index.getToday_last_amount());
            resultMap.put("data", dataMap);
            String indexJson = "";
            try {
                indexJson = JSONUtil.beanToJson(resultMap);
                IndexDto indexDto = new IndexDto(indexJson, Constant.STATUS_VALID);
                this.indexService.saveIndexDto(indexDto);
                jedisCluster.set(Constant.CACHE_INDEX_KEY, indexJson);
                return indexJson;
            } catch (Exception e) {
                log.error("initIndex error:{}",e);
            }
        } else {
            log.error("-----------------INDEX-ERROR-ISNULL-----------------");
        }
        return null;
    }

    /**
     * 设置审核拒绝时button事件
     *
     * @param request req
     * @param response res
     */
    @RequestMapping(value = "/credit-loan/confirm-failed-loan")
    public void setBorrowStatus(HttpServletRequest request, HttpServletResponse response) {
        log.info("setBorrowStatus start...");
        //设备号
        String deviceId = request.getParameter("deviceId");
        log.info("设备号：" + deviceId);
        //设备号为空
        if (StringUtils.isBlank(deviceId)) {
            HashMap<String, Object> reMap = MapUtils.getResultMapByError("-1", "请您检查网络再试");
            WriteUtil.writeDataToApp(response, JSONUtil.beanToJson(reMap));
        } else {
            User user = null;
            try {
                user = loginFrontUserByDeiceId(request);
            } catch (Exception e) {
                log.error("IndexController setBorrowStatus error:", e);
            }
            try {
                if (null != user) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("USER_ID", user.getId());
                    map.put("BORROW_STATUS", "0");
                    this.indexService.updateInfoUserInfoBorrowStatus(map);
                    HashMap<String, Object> reMap = MapUtils.getResultMap("0", "初始化成功");
                    //初始化用户动态信息
                    this.infoIndexService.sendDynamic(Integer.parseInt(user.getId()));
                    WriteUtil.writeDataToApp(response, JSONUtil.beanToJson(reMap));
                } else {
                    HashMap<String, Object> reMap = MapUtils.getResultMapByError("-1", "请您检查网络再试");
                    WriteUtil.writeDataToApp(response, JSONUtil.beanToJson(reMap));
                }
            } catch (Exception e) {
                log.error("IndexController setBorrowStatus error:", e);
                HashMap<String, Object> reMap = MapUtils.getResultMapByError("-1", "请您检查网络再试");
                WriteUtil.writeDataToApp(response, JSONUtil.beanToJson(reMap));
            }
        }
    }

    /**
     * 上报
     *
     * @param request req
     * @param response res
     */
    @RequestMapping(value = "/credit-app/device-report")
    public void deviceReport(HttpServletRequest request, HttpServletResponse response){
        log.info("IndexController deviceReport start");
        String identifierId = request.getParameter("IdentifierId");
        String appMarket = request.getParameter("appMarket");
        String deviceId = request.getParameter("device_id");
        String installedTime = request.getParameter("installed_time");
        String netType = request.getParameter("net_type");
        String uid = request.getParameter("uid");
        String username = request.getParameter("username");
        log.info("parameter IdentifierId:" + identifierId + ",appMarket:" + appMarket
                + ",device_id:" + deviceId + ",installed_time:" + installedTime + ",net_type:"
                + netType + ",uid:" + uid + ",username:" + username);

        if (StringUtils.isBlank(identifierId) || StringUtils.isBlank(deviceId)) {
            log.info("device-report 上报设备号和身份信息为空，取消上报");
            return;
        }
        InfoReport infoReport = new InfoReport();
        infoReport.setIdentifierId(identifierId);
        infoReport.setAppMarket(appMarket);
        infoReport.setDeviceId(deviceId);
        infoReport.setInstalledTime(installedTime);
        infoReport.setNetType(netType);
        infoReport.setUid(uid);
        infoReport.setUserName(username);

        reportService.saveReport(infoReport);
        JSONObject json = new JSONObject();
        json.put("code", "0");
        JSONUtil.toObjectJson(response, json.toString());
        log.info("IndexController deviceReport end");
    }


    /**
     * 校验银行卡
     *
     * @return boolean
     */
    private boolean checkBankNo(InfoIndexInfo indexInfo) {
        if (null != indexInfo) {
            if (null != indexInfo.getAuthBank()) {
                //银行认证完成
                if (Constant.STATUS_INT_VALID.equals(indexInfo.getAuthBank())) {
                    return StringUtils.isNotBlank(indexInfo.getBankNo());
                }
            }
        }
        return false;
    }

    /**
     * APP首页banner-关于我们
     */
    @RequestMapping("gotoAboutUs")
    public String gotoAboutIndex() {
        return "content/about_us";
    }

    /**
     * 进入静态活动页面
     *
     * @param request req
     * @return str
     */
    @RequestMapping("gotoStaticIndex")
    public String gotoStaticIndex(HttpServletRequest request) {
        String pageName = request.getParameter("pageName");
        return "content/" + pageName;
    }

    /**
     * 进入拒就送
     */
    @RequestMapping("gotoJujiuSong")
    public String gotoJujiuSong() {
        return "content/jujiupeiIndex";
    }

    /**
     * APP首页banner-还款方式
     */
    @RequestMapping("gotoRepaymentType")
    public String gotoRepaymentType(HttpServletRequest request,
                                    Model model) {
        model.addAttribute("type", request.getParameter("type"));
        return "repayment/repaymentTypeIndex";
    }

    /**
     * 新增支付宝还款页面
     * @return str
     */
    @RequestMapping("gotoAlipayPayType")
    public String gotoAlipayPayType(Model model) {
        //获取网站配置数据
        HashMap<String, Object> params = new HashMap<>();
        params.put("sysType", "WEBSITE");
        List<BackConfigParams> list = backConfigParamsService.findParams(params);
        Map<String, String> intervalMap = getBackConfigParamsMap(list);
        //收款人姓名
        model.addAttribute("account_name", intervalMap.get("account_name"));
        //收款人账号
        model.addAttribute("pay_account", intervalMap.get("pay_account"));
        //收款人二维码
        model.addAttribute("qr_code", intervalMap.get("qr_code"));
        //客服热线
        model.addAttribute("service_phone", intervalMap.get("service_phone"));
        //客服热线时间
        model.addAttribute("tel_time", intervalMap.get("tel_time"));
        //客服qq
        model.addAttribute("services_qq", intervalMap.get("services_qq"));
        //客服qq时间
        model.addAttribute("online_time", intervalMap.get("online_time"));
        //微信二维码
        model.addAttribute("wechat_code",intervalMap.get("wechat_code"));
        //微信收款姓名
        model.addAttribute("we_account_name",intervalMap.get("we_account_name"));

        return "repayment/repaymentTypeAlipay";
    }

    /**
     * 极速放款
     */
    @RequestMapping("gotoDrawAwardsIndex")
    public String gotoDrawAwardsIndex() {
        System.out.println("gotoDrawAwardsIndex");
        return "content/drawAwardsIndex";
    }
    /**
     * 首页活动-还款方式
     */
    @RequestMapping("gotoContentPay")
    public String gotoContentPay() {
        return "content/about_pay";
    }
}