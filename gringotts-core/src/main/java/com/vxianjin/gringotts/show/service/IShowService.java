package com.vxianjin.gringotts.show.service;

import com.vxianjin.gringotts.show.pojo.Province;
import com.vxianjin.gringotts.show.pojo.UserShow;

import java.util.HashMap;
import java.util.List;

public interface IShowService {

    List<HashMap<String, Object>> findIndexNum();

    List<UserShow> findUserList();

    List<Province> findProvinceList();
}
