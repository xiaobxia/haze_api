package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.JsStepConfig;

import java.util.List;


public interface IJsStepConfigDao {
    List<JsStepConfig> findList(JsStepConfig jsStepConfig);
}
