package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.pojo.Advertisement;

import java.util.List;
import java.util.Map;

/**
 * 从core中提取出来
 */
public interface IAdvertisementService {

    void save(Advertisement ad);

    void delete(int id);

    void update(Advertisement ad);

    Advertisement findById(int id);

    PageConfig<Advertisement> findPage(Map<String, Object> params);

    List<Advertisement> findAll();

    List<Advertisement> findOne(Map<String, Object> params);
}
