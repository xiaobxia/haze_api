package com.vxianjin.gringotts.web.util;

import com.vxianjin.gringotts.constant.UserPushUntil;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IPushUserService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 地推线程
 *
 * @author zjb
 */
public class DtThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(DtThread.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Integer assetId;// 订单id
    private Integer userId;// 用户id
    private String pushType;// 推送类型
    private String dateTime;// 数据时间
    private IBorrowOrderService borrowOrderService;
    private IUserService userService;
    private IPushUserService pushUserService;
    private HashMap<String, Object> params;

    /**
     * @param pushType           推送类型
     * @param userId             用户ID
     * @param assetId            订单id
     * @param dateTime           日期
     * @param userService
     * @param pushUserService
     * @param borrowOrderService
     * @param params
     */
    public DtThread(String pushType, Integer userId, Integer assetId, Date dateTime, IUserService userService, IPushUserService pushUserService,
                    IBorrowOrderService borrowOrderService, HashMap<String, Object> params) {

        this.assetId = assetId;
        this.userId = userId;
        if (dateTime == null) {
            dateTime = new Date();
        }
        this.dateTime = dateFormat.format(dateTime);
        this.userService = userService;

        this.borrowOrderService = borrowOrderService;
        this.pushType = pushType;
        this.pushUserService = pushUserService;
        this.params = params;
    }

    public void run() {
        try {
            if (pushType == null) {
                return;
            }
            HashMap<String, Object> pushMap = new HashMap<String, Object>();
            // 注册 判断pushId在我库是否存在，不存在则添加
            if (pushType.equals(UserPushUntil.REGISTER)) {
                pushMap.put("pushId", params.get("pushId"));
                Integer pushIdCount = userService.selectChanelUserPushCount(params);
                if (pushIdCount == 0) {
                    userService.insertChanelUserPush(params);
                }
            } else {
                // 判断是否为地推用户
                pushMap = userService.selectPushId(Integer.valueOf(userId.toString()));
                if (pushMap == null || pushMap.get("pushId") == null) {
                    return;
                }
                // 各项认证
                if (pushType.equals(UserPushUntil.BANKAPPROVE) || pushType.equals(UserPushUntil.CONTACTAPPROVE)
                        || pushType.equals(UserPushUntil.ZHIMAAPPROVE) || pushType.equals(UserPushUntil.REALNAME)
                        || pushType.equals(UserPushUntil.COMPANYAPPROVE) || pushType.equals(UserPushUntil.JXLAPPROVE)) {

                }
                // 还款
                else if (pushType.equals(UserPushUntil.PUSH_REPAYMENTSUCC)) {
                    BorrowOrder borrow = borrowOrderService.findOneBorrow(assetId);
                    // 只判断用户第一次借款订单
                    if (borrow == null || borrow.getCustomerType().intValue() != 0) {
                        return;
                    }
                }
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("pushId", pushMap.get("pushId"));
            map.put("approveTime", dateTime);
            map.put("pushType", pushType);
            if (params != null && params.containsKey("userPhone")) {
                map.put("userPhone", params.get("userPhone"));
            }
            if (params != null && params.containsKey("realName")) {
                map.put("realName", params.get("realName"));
            }
            pushUserService.addPushUserApproves(map);

        } catch (Exception e) {
            logger.error(" Dt info error pushType:" + pushType + ", userId:" + userId + ",assetId:" + assetId + ",dateTime:" + dateTime, e);
        }
    }
}
