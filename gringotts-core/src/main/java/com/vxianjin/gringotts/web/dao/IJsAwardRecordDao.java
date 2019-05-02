package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.JsAwardRecord;

import java.util.HashMap;
import java.util.List;


public interface IJsAwardRecordDao {

    List<JsAwardRecord> findList(HashMap<String, Object> jsAwardRecord);

    JsAwardRecord findOne(JsAwardRecord jsAwardRecord);

    void insert(JsAwardRecord jsAwardRecord);

    void update(JsAwardRecord jsAwardRecord);

}
