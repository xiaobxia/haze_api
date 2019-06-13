package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.pojo.UserOrder;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.web.service.IUserBankService;
import com.vxianjin.gringotts.web.service.IUserOrderService;
import com.vxianjin.gringotts.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.*;

/**
 * 用户借款记录
 *
 * @author dxp
 * @date 2016年12月9日 16:51:09
 */
@Controller
public class UserloanDetailController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserloanDetailController.class);

    @Resource
    private IUserOrderService userOrderService;
    @Resource
    private IBorrowOrderService borrowOrderService;
    @Resource
    private RepaymentService repaymentService;

    @Resource
    private IUserBankService userBankService;

    /**
     * 获取用户个人中心借款列表
     *
     */
    @RequestMapping("credit-loan/get-my-orders")
    public void getMyOrders(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> json = new HashMap<>();
        HashMap<String, Object> params = this.getParametersO(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        try {
            User user = this.loginFrontUserByDeiceId(request);
            if (user != null) {
                List<Map<String, Object>> dataList = new ArrayList<>();
                if (request.getParameter("page") != null) {
                    Integer pageNo = Integer.valueOf(request.getParameter("page"));
                    params.put("pageNum", pageNo);
                }
                if (request.getParameter("page") != null) {
                    Integer pageSize = Integer.valueOf(request.getParameter("pagsize"));
                    params.put("numPerPage", pageSize);
                }

//				params.put("statusList", Arrays.asList(BorrowOrder.STATUS_HKZ,BorrowOrder.STATUS_DCS,BorrowOrder.STATUS_CSTG,BorrowOrder.STATUS_FSBH,BorrowOrder.STATUS_FKBH,
//						  BorrowOrder.STATUS_YYQ,BorrowOrder.STATUS_YHZ,
//						BorrowOrder.STATUS_YHK, BorrowOrder.STATUS_FKSB,BorrowOrder.STATUS_FKZ,BorrowOrder.STATUS_CSBH));
                params.put("userId", user.getId());
                PageConfig<BorrowOrder> page = borrowOrderService.findPage(params);

                List<BorrowOrder> pageList = page.getItems();
                if (pageList != null && pageList.size() > 0) {
                    Map<String, Object> dataMap = null;
                    for (BorrowOrder useO : pageList) {
                        String statusName = borrowStatusMap_front.get(useO.getStatus());
//						if (borrowStatusMap_fangkanSucee.containsKey(useO
//								.getStatus().intValue())) {
//							statusName = "等待还款";
//						} else if (borrowStatusMap_shenhezhong.containsKey(useO
//								.getStatus().intValue())) {// 机审拒绝、待初审、待复审、待放款审核
//							statusName = "审核中";
//						} else if (borrowStatusMap_shenheFail.containsKey(useO
//								.getStatus().intValue())) {// 初审驳回、复审驳回,放款驳回
//							statusName = "审核失败";
//						} else if (borrowStatusMap_yiyuqi.containsKey(useO
//								.getStatus().intValue())) {// 逾期、坏账
//							statusName = "已逾期";
//						} else if (borrowStatusMap_finish.containsKey(useO
//								.getStatus().intValue())) {// 逾期、坏账
//							statusName = "已还款";
//						} else {
//							statusName = useO.getStatusName();
//						}

                        dataMap = new HashMap<>();
                        dataMap.put("text",
                                "<font color='#ff8003' size='3'>"
                                        + statusName + "</font>");
                        dataMap.put("time", sdf.format(useO.getCreatedAt()));
                        dataMap.put("title", "借款" + new DecimalFormat("0.00").format(useO.getMoneyAmount() / 100) + "元");
                        dataMap.put("url", PropertiesConfigUtil.get("APP_HOST_API") + "/repayment/detail.do?id="
                                + useO.getId());
                        dataList.add(dataMap);
                    }
                }
                Map<String, Object> map = new HashMap<>();
                map.put("item", dataList);
                map.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/repayment/repay-type");
                map.put("page", page.getCurrentPage());
                map.put("pageSize", page.getPageSize());
                map.put("pageTotal", page.getTotalResultSize());
                json.put("data", map);
                code = ResponseStatus.SUCCESS.getName();
                msg = "获取成功";
            } else {
                msg = "请先登录";
            }
        } catch (Exception e) {
            logger.error("get-my-orders error:{}",e);
            code = ResponseStatus.ERROR.getName();
            msg = "系统异常";

        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }

    }


    /**
     * 获取用户个人中心借款列表
     *
     */

    @RequestMapping("credit-loan/get-my-loan")
    public void getMyLoans(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> json = new HashMap<>();
        HashMap<String, Object> params = this.getParametersO(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        try {
            User user = this.loginFrontUserByDeiceId(request);
            if (user != null) {
                params.put("userId", user.getId());
                Integer[] statuses = new Integer[]{STATUS_DCS, STATUS_CSTG, STATUS_FSTG, STATUS_FKZ, STATUS_FKSB, STATUS_HKZ, STATUS_BFHK, STATUS_YYQ, STATUS_YHZ};
                params.put("statuses", statuses);
                List<Map<String, Object>> repayments = repaymentService.findMyLoan(params);
                for (Map<String, Object> tMap : repayments) {
                    String textTip = borrowStatusMap_front.get(Integer.parseInt(tMap.get("status").toString()));
                    tMap.put("text_tip", "<font color='#ee4957' size='3'>" + textTip + "</font>");
                    tMap.put("interests", 0);
                    tMap.put("url", PropertiesConfigUtil.get("APP_HOST_API") + "/repayment/detail.do?id=" + tMap.get("asset_order_id"));
                    if (null != tMap.get("plan_fee_time")) {
                        tMap.put("plan_fee_time", sdf.format((Date) tMap.get("plan_fee_time")));
                    }
                    tMap.put("debt", new DecimalFormat("0.00").format(Integer.parseInt(tMap.get("debt").toString()) / 100.00));
                    tMap.put("principal", new DecimalFormat("0.00").format(Integer.parseInt(tMap.get("principal").toString()) / 100.00));
                    tMap.put("counter_fee", new DecimalFormat("0.00").format(Integer.parseInt(tMap.get("counter_fee").toString()) / 100.00));
                    tMap.put("receipts", new DecimalFormat("0.00").format(Integer.parseInt(tMap.get("receipts").toString()) / 100.00));
                    if (null != tMap.get("late_fee")) {
                        tMap.put("late_fee", new DecimalFormat("0.00").format(Integer.parseInt(tMap.get("late_fee").toString()) / 100.00));
                    } else {
                        tMap.put("late_fee", "0.00");
                    }
                }

                Map<String, Object> data = new HashMap<String, Object>();

                List<Map<String, Object>> payList = new ArrayList<Map<String, Object>>();
                Map<String, Object> mapPay = new HashMap<String, Object>();
                mapPay.put("type", 1);
                mapPay.put("title", "银行卡还款(推荐)");
                mapPay.put("img_url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/union_pay_new.png");
                mapPay.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/gotoRepaymentType?type=1");
                payList.add(mapPay);

//				mapPay = new HashMap<String, Object>();
//				mapPay.put("type", 2);
//				mapPay.put("title", "自动扣款(银行卡)");


//				mapPay.put("img_url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/self_motion.png");
//				mapPay.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/gotoRepaymentType?type=2");
//				payList.add(mapPay);

                /*mapPay = new HashMap<String, Object>();
                mapPay.put("type", 2);
                mapPay.put("title", "更多还款方式");
                mapPay.put("img_url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/more_pay.png");
                mapPay.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/gotoAlipayPayType");
                payList.add(mapPay);*/

				/*Map<String, Object> mapPay = new HashMap<String, Object>();
                mapPay.put("type", 1);
				mapPay.put("title", "银行卡还款");
				mapPay.put("img_url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/union_pay.png");
				mapPay.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/repayment/repay-type");
				payList.add(mapPay);*/
                /*mapPay = new HashMap<String, Object>();
                mapPay.put("type", 2);
				mapPay.put("title", "支付宝转账");
				mapPay.put("img_url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/alipay_card_info.png");
				mapPay.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/repayment/repay-type");
				payList.add(mapPay);*/
//				mapPay = new HashMap<String, Object>();
//				mapPay.put("type", 3);
//				mapPay.put("title", "微信转账");
//				mapPay.put("img_url", PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/wechat.png");
//				mapPay.put("link_url", PropertiesConfigUtil.get("APP_HOST_API") + "/repayment/repay-type");
//				payList.add(mapPay);

                Map<String, Object> item = new HashMap<>();
                item.put("list", repayments);
                item.put("pay_title", "支持多种还款方式，方便快捷");
                item.put("count", repayments.size());
                item.put("old_path", PropertiesConfigUtil.get("APP_HOST_API") + "/content/Olduser");
                item.put("pay_type", payList);

                data.put("item", item);
                json.put("data", data);
                code = ResponseStatus.SUCCESS.getName();
                msg = "获取成功";
            } else {
                code = ResponseStatus.LOGIN.getName();
                msg = "请先登录";
            }
        } catch (Exception e) {
            logger.error("get-my-loan error:{}",e);
            code = ResponseStatus.ERROR.getName();
            msg = "系统异常";

        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }

    }

    /**
     * 获取用户个人中心借款列表详情
     *
     */

    @RequestMapping("mobile/web/loan/loan-detail")
    public void loanDetail(HttpServletRequest request,
                           HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> json = new HashMap<>();
        String code = ResponseStatus.FAILD.getName();
        String msg = "获取失败";
        Map<String, Object> dataMap = null;
        try {
            if (request.getParameter("id") == null
                    || "".equals(request.getParameter("id")) ) {
                code = ResponseStatus.FAILD.getName();
                msg = "请求参数错误！";
                logger.error("get loan-detail error:id  is null");
                return;
            }
            Integer id = Integer.valueOf(request.getParameter("id"));
            UserOrder userOrder = userOrderService.findById(id);

            if (userOrder != null) {
                HashMap<String, Object> userparams = new HashMap<>();
                // 查询用户的借款入账账户信息
                userparams.put("userId", userOrder.getUserId());
                // 借记卡
                userparams.put("type", "2");
                List<UserCardInfo> cardInfo = userBankService
                        .findUserCardByUserId(userparams);
                UserCardInfo info = new UserCardInfo();
                if (cardInfo != null && cardInfo.size() > 0) {
                    info = cardInfo.get(0);
                }

                dataMap = new HashMap<>();
                // 状态
                dataMap.put("status", userOrder.getStatus());
                dataMap.put("2", "公司还款成功，又积攒了一份信用");
                dataMap.put("打款成功", "打款成功");
                dataMap.put("审核通过", "审核通过");
                dataMap.put("申请提交成功", "申请提交成功");
                dataMap.put("moneyAmount", userOrder.getMoneyAmount());
                dataMap.put("intoMoney", userOrder.getIntoMoney());
                dataMap.put("loanInterests", userOrder.getLoanInterests());
                dataMap.put("借款期限", "借款期限");
                dataMap.put("orderTime", userOrder.getOrderTime() == null ? "" : sdf.format(userOrder.getOrderTime()));
                dataMap.put("receiveCardId", info.getCard_no() == null ? info.getBankName() : info.getBankName()
                                + "("
                                + info.getBankName().substring(
                                info.getBankName().length() - 4,
                                info.getBankName().length()) + ")");
            }

            json.put("data", dataMap);
            code = ResponseStatus.SUCCESS.getName();
            msg = "获取成功";
            logger.info("get loan-detail success");
        } catch (Exception e) {
            logger.error("get loan-detail error", e);
            code = ResponseStatus.ERROR.getName();
            msg = "系统异常";

        } finally {
            json.put("code", code);
            json.put("message", msg);
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }

    }

}
