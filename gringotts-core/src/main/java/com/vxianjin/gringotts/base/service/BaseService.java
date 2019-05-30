package com.vxianjin.gringotts.base.service;

import java.io.Serializable;

/**
 * 基类接口定义
 * 
 * @author fully
 * @version 1.0
 */
public interface BaseService<T, ID extends Serializable> {

	int insert(T record);

	int insertRecord(T record);

	int updateById(T record);

	int updateRecord(T record);

	T getById(ID id);
}