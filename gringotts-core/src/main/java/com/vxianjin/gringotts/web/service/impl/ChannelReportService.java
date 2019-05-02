package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.dao.IChannelReportDao;
import com.vxianjin.gringotts.web.dao.IPaginationDao;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.ChannelReport;
import com.vxianjin.gringotts.web.service.IChannelReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vxianjin.gringotts.web.pojo.BorrowOrder.*;

@Service
public class ChannelReportService implements IChannelReportService {


    @Autowired
    IChannelReportDao channelReportDao;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IPaginationDao paginationDao;

    @Override
    public ChannelReport getChannelReportById(Integer id) {
        return null;
    }

    @Override
    public PageConfig<ChannelReport> findPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "ChannelReport");
        PageConfig<ChannelReport> pageConfig = new PageConfig<ChannelReport>();
        pageConfig = paginationDao.findPage("findAll", "findAllCount", params, "web");
        return pageConfig;
    }

    @Override
    public PageConfig<ChannelReport> findPrPage(HashMap<String, Object> params) {
        params.put(Constant.NAME_SPACE, "ChannelReport");
        PageConfig<ChannelReport> pageConfig = new PageConfig<ChannelReport>();
        pageConfig = paginationDao.findPage("findPrAll", "findPrAllCount", params, "web");
        return pageConfig;
    }

    @Override
    public List<ChannelReport> getAllPrChannelReports(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return channelReportDao.findPrAll(params);
    }

    @Override
    public List<ChannelReport> getAllChannelReports(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return channelReportDao.findAll(params);
    }

    @Override
    public boolean saveChannelReport() {
        boolean bool = false;
        Map<String, Object> param = new HashMap<String, Object>();
        ChannelReport channelReport = new ChannelReport();
        try {
            /*设置查询时间*/

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String nowDate = DateUtil.getDateFormat(cal.getTime(), "yyyy-MM-dd");

            param.put("startDate", nowDate + " 00:00:00");
            param.put("endDate", nowDate + " 23:59:59");
            param.put("nowDate", cal.getTime());
            List<ChannelReport> channelIdList = channelReportDao.findChannelId();

            for (ChannelReport cid : channelIdList) {
                Integer channelid = cid.getChannelid();
                if (channelid != null) {
                    param.put("channelid", channelid);
                    //注册量
                    Integer registerCount = channelReportDao.findRegisterCount(param);
                    channelReport.setRegisterCount(registerCount);
                    //实名认证
                    Integer realNameCount = channelReportDao.findRealNameCount(param);
                    channelReport.setAttestationRealnameCount(realNameCount);
                    //运营商认证
                    Integer jxlCount = channelReportDao.findJXLCount(param);
                    channelReport.setJxlCount(jxlCount);

                    //芝麻认证
                    Integer zhimaCount = channelReportDao.findZMCount(param);
                    channelReport.setZhimaCount(zhimaCount);
                    //紧急联系人
                    Integer contactCount = channelReportDao.findContactCount(param);
                    channelReport.setContactCount(contactCount);
                    //工作信息
                    Integer companyCount = channelReportDao.findCompanyCount(param);
                    channelReport.setCompanyCount(companyCount);

                    //银行卡绑定
                    Integer bankCount = channelReportDao.findBankCount(param);
                    channelReport.setAttestationBankCount(bankCount);
                    //支付宝
                    Integer alipayCount = channelReportDao.findAlipayCount(param);
                    channelReport.setAlipayCount(alipayCount);

                    //授信失败人数
                    Integer approveErrorCount = channelReportDao.findApproveErrorCount(param);
                    channelReport.setApproveErrorCount(approveErrorCount);

                    //申请借款
                    Integer borrowApplyCount = channelReportDao.findBorrowApplyCount(param);
                    channelReport.setBorrowApplyCount(borrowApplyCount);

                    //申请借款成功
                    Integer[] inStatus = new Integer[]{STATUS_HKZ, STATUS_BFHK, BorrowOrder.STATUS_YHK, BorrowOrder.STATUS_YQYHK, STATUS_YYQ, STATUS_YHZ};
                    param.put("inStatus", inStatus);
                    Integer borrowSucCount = channelReportDao.findBorrowSucCount(param);
                    channelReport.setBorrowSucCount(borrowSucCount);

                    //计算百分比
                    BigDecimal a = new BigDecimal(borrowApplyCount);
                    BigDecimal b = new BigDecimal(borrowSucCount);
                    BigDecimal c = BigDecimal.ZERO;
                    if (borrowApplyCount > 0 && borrowSucCount > 0) {
                        c = b.divide(a, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    }
                    channelReport.setPassRate(c);
                    //放款金额
                    Integer intoMoney = channelReportDao.findIntoMoney(param);
                    channelReport.setIntoMoney(new BigDecimal(intoMoney));

                    //Integer findRegisterCount = channelReportDao.findRegisterCount(param);
                    channelReport.setChannelid(channelid);
                    channelReport.setReportDate(cal.getTime());
                    channelReportDao.insert(channelReport);
                }

            }

        } catch (Exception e) {
            logger.error("channek saveReport error", e);
        }
        return false;
    }


}
