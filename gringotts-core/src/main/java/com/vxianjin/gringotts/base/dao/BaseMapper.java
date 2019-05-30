package com.vxianjin.gringotts.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T, ID extends Serializable> {

	int save(T e);
	
	int saveRecord(T e);

	int update(T e);

	int updateSelective(T e);

	T findSelective(Map<String, Object> paramMap);

	T findByPrimary(ID primary);
	
	List<T> listSelective(Map<String, Object> paramMap);
	
}