package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.pay.dao.IRepaymentDao;
import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.AbstractTest;
import com.vxianjin.gringotts.web.pojo.Repayment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Author: chenkai
 * @Date: 2018/7/25 15:03
 * @Description: 还款测试类
 */
public class RepaymentServiceTest extends AbstractTest {
    @Resource
    private IRepaymentDao repaymentDao;

    @Autowired
    RepaymentService repaymentService;

    @Test
    public void repaymentInsertTest() {
        Repayment repayment = new Repayment();

        try {
            repayment.setUserId(250103);
            repayment.setAssetOrderId(768897);
            repayment.setRepaymentAmount(103000l);
            repayment.setLateFeeApr(300);
            repayment.setRepaymentedAmount(206000l);
            repayment.setRepaymentPrincipal(80500l);
            repayment.setRepaymentInterest(19500l);

            repayment.setFirstRepaymentTime(DateUtil.addDay(new Date(), 2 - 1));// 放款时间加上借款期限
            repayment.setRepaymentTime(DateUtil.addDay(new Date(), 2 - 1));// 放款时间加上借款期限
            repayment.setCreditRepaymentTime(new Date());

            repayment.setCreatedAt(new Date());
            repayment.setStatus(34);
            repaymentDao.insertSelective(repayment);
        } catch (DuplicateKeyException e) {
            System.out.println("唯一索引插入异常");
        } catch (Exception e) {
            System.out.println(e);
        }
        //        int count = repaymentDao.queryCountByAssetOrderId("2501031");
        //        System.out.println(count);

    }
    @Test
    public void renewalWhether() {
        Repayment repayment = repaymentService.selectByPrimaryKey(95);
        assert repayment != null;
        assert repayment.getAssetOrderId() != null;

        Map<String, Object> map = repaymentService.findBorrowLoanTerm(repayment.getAssetOrderId());
        assert map != null;
    }
}
