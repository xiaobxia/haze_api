package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.JsStepRecord;

import java.util.List;

public interface IJsStepRecordDao {

    List<JsStepRecord> findPageList(JsStepRecord jsStepRecord);

    void insert(JsStepRecord jsStepRecord);

    void update(JsStepRecord jsStepRecord);

}
