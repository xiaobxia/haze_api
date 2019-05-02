package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.JsDrawrollsRecord;

import java.util.List;

public interface IJsDrawrollsRecordDao {

    List<JsDrawrollsRecord> findList(JsDrawrollsRecord jsDrawrollsRecord);

    List<JsDrawrollsRecord> findListTop(JsDrawrollsRecord jsDrawrollsRecord);

    List<JsDrawrollsRecord> findListUser(JsDrawrollsRecord jsDrawrollsRecord);

    List<JsDrawrollsRecord> findListByPhone(JsDrawrollsRecord jsDrawrollsRecord);

    JsDrawrollsRecord findMaxOne(JsDrawrollsRecord jsDrawrollsRecord);

    void insert(JsDrawrollsRecord jsDrawrollsRecord);

    void update(JsDrawrollsRecord jsDrawrollsRecord);

}
