package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.common.PageConfig;
import com.vxianjin.gringotts.web.dao.IAdvertisementDao;
import com.vxianjin.gringotts.web.pojo.Advertisement;
import com.vxianjin.gringotts.web.service.IAdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvertisementService implements IAdvertisementService {
    @Autowired
    private IAdvertisementDao adDao;

    @Override
    public void save(Advertisement ad) {
        adDao.save(ad);
    }

    @Override
    public void delete(int id) {
        adDao.delete(id);
    }

    @Override
    public void update(Advertisement ad) {
        adDao.update(ad);
    }

    @Override
    public Advertisement findById(int id) {
        return adDao.findById(id);
    }

    @Override
    public List<Advertisement> findAll() {
        return adDao.findAll();
    }

    @Override
    public List<Advertisement> findOne(Map<String, Object> params) {
        return adDao.findOne(params);
    }

    @Override
    public PageConfig<Advertisement> findPage(Map<String, Object> params) {
        String currentpageStr = params.get("pageNum") == null ? "1" : params.get("pageNum").toString();
        String pagesizeStr = params.get("numPerPage") == null ? "10" : params.get("numPerPage").toString();
        Integer currentpage = Integer.valueOf(currentpageStr);
        Integer pagesize = Integer.valueOf(pagesizeStr);
        params.put("beginIndex", (currentpage - 1) * pagesize);
        params.put("pagesize", pagesize);
        PageConfig<Advertisement> pageConfig = new PageConfig<Advertisement>();
        Integer count = adDao.countByCondition(params);
        List<Advertisement> list = adDao.findByCondition(params);
        pageConfig.setTotalResultSize(count);
        pageConfig.setCurrentPage(currentpage);
        pageConfig.setItems(list);
        pageConfig.setPageSize(pagesize);
        return pageConfig;
    }


}
