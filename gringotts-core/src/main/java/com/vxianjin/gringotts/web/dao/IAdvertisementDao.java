package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.Advertisement;

import java.util.List;
import java.util.Map;

public interface IAdvertisementDao {
    void save(Advertisement ad);

    void delete(int id);

    void update(Advertisement ad);

    Integer countByCondition(Map<String, Object> params);

    Advertisement findById(int id);

    List<Advertisement> findByCondition(Map<String, Object> params);

    List<Advertisement> findAll();

    List<Advertisement> findOne(Map<String, Object> params);
}
