package com.vxianjin.gringotts.base.service.impl;


import com.vxianjin.gringotts.base.dao.BaseMapper;
import com.vxianjin.gringotts.base.service.BaseService;

import javax.annotation.Resource;
import java.io.Serializable;

public abstract class BaseServiceImpl<T, ID extends Serializable> implements
		BaseService<T, ID> {
	@Resource
	private BaseMapper<T, ID> baseMapper;
	
	public int insert(T record) {
		return getMapper().save(record);
	}

	@Override
	public int insertRecord(T record) {
		return getMapper().saveRecord(record);
	}
	
	public T getById(ID id) {
		return getMapper().findByPrimary(id);
	}

	public int updateById(T record) {
		return getMapper().update(record);
	}

	@Override
	public int updateRecord(T record) {
		return getMapper().updateSelective(record);
	}

	public abstract BaseMapper<T, ID> getMapper();

}
