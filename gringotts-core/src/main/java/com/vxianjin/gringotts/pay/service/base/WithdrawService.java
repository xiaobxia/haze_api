package com.vxianjin.gringotts.pay.service.base;

import com.vxianjin.gringotts.pay.common.exception.BizException;
import com.vxianjin.gringotts.pay.model.NeedPayInfo;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserCardInfo;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * 代付（划款）业务处理
 * Created by jintian on 2018/7/17.
 */
@Service
public class WithdrawService {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawService.class);

    @Resource
    private JedisCluster jedisCluster;

    @Autowired
    private IBorrowOrderService borrowOrderService;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IUserService userService;

    /**
     * 代付redis锁key前缀
     */
    private static final String WITHDRAW_KEY_HEAD = "WITHDRAW_KEY_";

    /**
     * 获取代付相关信息
     *
     * @return
     */
    public NeedPayInfo getNeedPayInfo(String userId, String borrowId) throws BizException {
        logger.info("WithdrawService getNeedPayInfo,params userId:{} borrowId:{}", userId, borrowId);
        NeedPayInfo needPayInfo = new NeedPayInfo();

        //获取用户信息
        User user = userDao.searchByUserid(Integer.parseInt(userId));
        if (user == null) {
            logger.info("WithdrawService getNeedPayInfo 请求参数有误");
            throw new BizException("-101", "请求参数有误");
        }
        //获取借款订单信息
        BorrowOrder order = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
        if (order == null) {
            logger.info("WithdrawService getNeedPayInfo 未发现该笔订单信息");
            throw new BizException("-101", "未发现该笔订单信息");
        }
        UserCardInfo info = null;
        if (!user.getId().equals(String.valueOf(order.getUserId()))) {
            logger.info("WithdrawService getNeedPayInfo 非法请求参数");
            throw new BizException("-101", "非法请求参数");
        }
        //检测借款订单状态
        if ("22".equals(String.valueOf(order.getStatus())) && "0".equals(order.getPaystatus())) {
            //还款用户银行卡信息
            info = userService.findUserBankCard(Integer.parseInt(user.getId()));
            needPayInfo.setOrderSholdPay(true);
            logger.info("WithdrawService getNeedPayInfo 正常获取银行卡信息");
        } else {
            needPayInfo.setOrderSholdPay(false);
            logger.info("WithdrawService getNeedPayInfo 此状态下无法发起代付操作，请联系技术人员");
            throw new BizException("-101", "此状态下无法发起代付操作，请联系技术人员");
        }

        Long flag = addPayKey(borrowId);
        //验证该笔订单是否处于处理中状态
        if ("0".equals(String.valueOf(flag))) {
            logger.info("WithdrawService getNeedPayInfo 该笔代付操作正在被处理中，请勿重复操作");
            throw new BizException("-101", "该笔代付操作正在被处理中，请勿重复操作");
        }

        needPayInfo.setBorrowOrder(order);
        needPayInfo.setUser(user);
        needPayInfo.setUserCardInfo(info);
        return needPayInfo;
    }

    /**
     * 给代付订单加锁（加上则返回1，没有则返回0）
     *
     * @param borrowId
     * @return
     */
    public long addPayKey(String borrowId) {
        return jedisCluster.setnx(WITHDRAW_KEY_HEAD + borrowId, "true");
    }

    /**
     * 解除订单锁
     * @param borrowId
     */
    public void removePayKey(String borrowId) {
        jedisCluster.del(WITHDRAW_KEY_HEAD + borrowId);
    }

}
