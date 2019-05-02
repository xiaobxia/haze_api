package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IReportDao;
import com.vxianjin.gringotts.web.pojo.InfoReport;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao extends BaseDao implements IReportDao {

    @Override
    public void saveReport(InfoReport infoReport) {
        getSqlSessionTemplate().insert("report.saveReport", infoReport);
    }

}
