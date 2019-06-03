package com.vxianjin.gringotts.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.risk.dao.IRiskOrdersDao;
import com.vxianjin.gringotts.risk.pojo.*;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.IdUtil;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IBorrowOrderDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.risk.StrongRiskResult;
import com.vxianjin.gringotts.web.service.IAutoRiskService;
import com.vxianjin.gringotts.web.service.IBackConfigParamsService;
import com.vxianjin.gringotts.web.util.aliyun.RocketMqUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zed
 */
@Service
public class AutoRiskService implements IAutoRiskService {

    private static Logger logger = LoggerFactory.getLogger(AutoRiskService.class);

    @Resource
    private IUserDao userDao;
    @Resource
    private IRiskOrdersDao riskOrdersDao;
    @Resource
    private IBorrowOrderDao borrowOrderDao;

    @Resource
    private OrderLogComponent orderLogComponent;

    @Autowired
    IBackConfigParamsService backConfigParamsService;

    /**
     * 用户执行借款订单的风控信息
     *
     * @param assetBorrowId 订单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void dealRemoteCreditReport(Integer assetBorrowId) {
        logger.info("dealRemoteCreditReport start");
        logger.info("dealRemoteCreditReport assetBorrowId=" + assetBorrowId);
        BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(assetBorrowId);
        if(borrowOrder.getStatus() != 0){
            logger.error("adviceExecute failure borrowId = " + assetBorrowId);
            throw new RuntimeException("adviceExecute failure borrowId = " + assetBorrowId);
        }
        boolean advice =false;
        Integer re = 10;
        User user = userDao.searchByUserid(borrowOrder.getUserId());
        StrongRiskResult strongRiskResult = userDao.getStrongRiskResultByUserId(String.valueOf(borrowOrder.getUserId()));
        if(strongRiskResult!=null ){
            if(!"30".equals(strongRiskResult.getResult())){
                Map<String,Object> map = new HashMap<>();
                map.put("orderNo", "wr"+DateUtil.formatDateNow("yyyyMMddHHmmssSSS")+IdUtil.generateRandomStr(6));
                map.put("consumerNo",PropertiesConfigUtil.get("RISK_BUSINESS")+borrowOrder.getUserId());
                map.put("borrowNo",borrowOrder.getId());
                map.put("channel",user.getUserFrom()==null?"0":user.getUserFrom());
                map.put("borrowType",borrowOrder.getLoanTerm());
                map.put("scene",100);
//                JSONObject blackData = new JSONObject();
//                blackData.put("blackBox",tdDevice);
//                if("android".equals(clientType)){
//                    blackData.put("appName","sdhb_and");
//                }else{
//                    blackData.put("appName","sdhb_ios");
//                }
//                map.put("datas",blackData.toJSONString());
                String result = HttpUtil.postForm(PropertiesConfigUtil.get("risk_url"),map);
                logger.info("risk result:{}",result);
                try{
                    saveCreditReport(result,borrowOrder.getUserId(),assetBorrowId);

                }catch (Exception e){
                    logger.error("save credit report error:{}",e);
                }
                JSONObject jsonObject = JSON.parseObject(result);

                if(jsonObject!=null){
                    if("0000".equals(jsonObject.getString("code"))){
                        JSONObject data = jsonObject.getJSONObject("data");
                        advice = "10".equals(data.getString("result"));
                        re = Integer.parseInt(data.getString("result"));
                    }
                }
            }
        }

        adviceExecute(assetBorrowId, borrowOrder.getUserId(), advice, re);


//        orderNo:jqxRI7PG02267222080000  订单号
//        consumerNo:ad158   首字母小写加字母
//        borrowNo:jkxUI7PG02267162080000  借款订单号
//        channel:fx    来源固定
//        borrowType:7   借款天数
//        scene:100     场景固定
    }



    private void saveCreditReport(String creditReport, Integer userId, Integer assetBorrowId) {
        logger.info("saveCreditReport start");
        RiskOrders riskOrders = new RiskOrders();
        riskOrders.setUserId(String.valueOf(userId));
        riskOrders.setOrderNo(UUID.randomUUID().toString());
        riskOrders.setReturnParams(creditReport);
        riskOrders.setOrderType("AUTO_RISK");
        riskOrders.setAssetBorrowId(assetBorrowId);
        RiskOrders riskOrdersSelect = riskOrdersDao.selectCreditReportByBorrowId(assetBorrowId);
        if (riskOrdersSelect != null) {
            riskOrders.setId(riskOrdersSelect.getId());
            int flag = riskOrdersDao.update(riskOrders);
            if (flag == 1) {
                logger.info("update success assetBorrowId = " + assetBorrowId);
            } else {
                logger.info("update fail assetBorrowId = " + assetBorrowId);
            }
        } else {
            int flag = riskOrdersDao.insertCreditReport(riskOrders);
            if (flag == 1) {
                logger.info("store success assetBorrowId = " + assetBorrowId);
            } else {
                logger.info("store fail assetBorrowId = " + assetBorrowId);
            }
        }
    }
    /**
     * * 执行建议
     *
     * @param assetBorrowId     订单号
     * @param userId            用户id
     */
    public void adviceExecute(Integer assetBorrowId, Integer userId, boolean advice, Integer re) {
        logger.info("adviceExecute start assetBorrowId=" + assetBorrowId);
        BorrowOrder borrowOrderAutoRisk = new BorrowOrder();
        borrowOrderAutoRisk.setId(assetBorrowId);
        Date nowDate = new Date();
        //初审备注信息
        borrowOrderAutoRisk.setVerifyTrialUser("自动化信审，初审完成");
        borrowOrderAutoRisk.setVerifyTrialTime(nowDate);
        borrowOrderAutoRisk.setUpdatedAt(nowDate);

        //默认，初审通过/待复审
        Integer loanStatus = BorrowOrder.STATUS_DCS;
        //判断系统是机审还是人审
        String result = backConfigParamsService.findMachine();
        Integer userBrowserSource = userDao.searchBrowserSource(userId);
        if(((StringUtils.isBlank(result) && result.equals("0")) || (userBrowserSource != null && userBrowserSource == 0)) && re == 10) {
            if (advice) {
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
                int time = c.get(Calendar.HOUR_OF_DAY);


                //此时系统选择为机审
                if (time > 20 || time < 9) {
                    loanStatus = BorrowOrder.STATUS_AI;
                } else {
                    loanStatus = BorrowOrder.STATUS_AI;

                    Map<String, String> map = new HashMap<>();

                    User user = userDao.searchByUserid(userId);
                    map.put("phone", user.getUserPhone());
                    map.put("name", user.getRealname());
                    RocketMqUtil.sendAiMessage(JSON.toJSONString(map));
                }
                //22:放款中
                //复审备注信息
                borrowOrderAutoRisk.setVerifyReviewUser("自动化信审，机审放款");
                borrowOrderAutoRisk.setVerifyReviewTime(nowDate);
                //}
            } else {
                //-3:初审驳回
                loanStatus = BorrowOrder.STATUS_CSBH;
                //审核失败恢复可借额度
                changeLimitMoney(assetBorrowId);

            }
        }

        //订单修改日志记录
        OrderLogModel logModel = new OrderLogModel();
        logModel.setUserId(String.valueOf(userId));
        logModel.setBorrowId(String.valueOf(assetBorrowId));
        logModel.setOperateType(OperateType.BORROW.getCode());
        logModel.setBeforeStatus("0");
        logModel.setAfterStatus(String.valueOf(loanStatus));
        orderLogComponent.addNewOrderLog(logModel);

        //订单最终状态
        borrowOrderAutoRisk.setStatus(loanStatus);
        //borrowOrderAutoRisk.setStatus(-3==loanStatus?loanStatus:0);

        logger.info("adviceExecute end assetBorrowId=" + assetBorrowId + " status=" + loanStatus);
        //logger.info("3.16 adviceExecute end assetBorrowId=" + assetBorrowId + " status=" + loanStatus);

        //乐观锁，只允许修改订单状态原先为0的借款订单
        int flag = borrowOrderDao.updateByPrimaryKeySelectiveAndStatus(borrowOrderAutoRisk);
        if (flag == 1) {
            logger.info("adviceExecute success borrowId = " + assetBorrowId);
        } else {
            logger.error("adviceExecute failure borrowId = " + assetBorrowId);
            throw new RuntimeException("adviceExecute failure borrowId = " + assetBorrowId);
        }
    }

    /**
     * 审核不通过，恢复用户额度
     *
     * @param assetBorrowId id
     */
    private void changeLimitMoney(Integer assetBorrowId) {

        BorrowOrder borrowOrder = borrowOrderDao.selectByPrimaryKey(assetBorrowId);

        User user = userDao.searchByUserid(borrowOrder.getUserId());
        User newUser = new User();
        newUser.setId(user.getId());
        //不幂等的操作
        newUser.setAmountAvailable(String.valueOf(Integer.valueOf(user.getAmountAvailable()) + borrowOrder.getMoneyAmount()));
        newUser.setUpdateTime(new Date());
        userDao.updateAmountAvailableByUserId(newUser);
        logger.info("changeLimitMoney userId=" + user.getId() + " 审核失败，用户额度恢复成功！！");
    }
}
