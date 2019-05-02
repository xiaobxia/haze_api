package com.vxianjin.gringotts.show.dao;

import com.vxianjin.gringotts.show.pojo.Province;
import com.vxianjin.gringotts.show.pojo.UserShow;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface IShowDao {
    List<HashMap<String, Object>> findIndexNum();

    List<HashMap<String, Object>> findXq();

    UserShow findUserList(Map<String, String> params);

    List<Province> findProvinceList();

    List<String> findAssetIds();

    String findSuf(String assetId);

    String findRuleValue(Map<String, String> params);
}
