package com.vxianjin.gringotts.web.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;

import javax.annotation.Resource;

public class BaseDao {

    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionTemplate getSqlSessionTemplate() {
        return this.sqlSessionTemplate;
    }

    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

}
