package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IReportDao;
import com.vxianjin.gringotts.web.pojo.InfoReport;
import com.vxianjin.gringotts.web.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportService")
public class ReportService implements IReportService {

    @Autowired
    IReportDao reportDao;

    @Override
    public void saveReport(InfoReport infoReport) {
        reportDao.saveReport(infoReport);
    }

}
