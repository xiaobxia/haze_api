package com.vxianjin.gringotts.web.dao.impl;

import com.vxianjin.gringotts.web.dao.IFaceRecognitionDao;
import com.vxianjin.gringotts.web.pojo.FaceRecognition;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class FaceRecognitionDao extends BaseDao implements IFaceRecognitionDao {

    @Override
    public boolean saveFaceRecognitionDao(FaceRecognition face) {
        return getSqlSessionTemplate().insert("com.vxianjin.gringotts.web.dao.IFaceRecognitionDao.saveFaceRecognition", face) > 0;
    }

    @Override
    public FaceRecognition selectByUserId(Integer userId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return getSqlSessionTemplate().selectOne("com.vxianjin.gringotts.web.dao.IFaceRecognitionDao.selectByFaceRecognitionUserId", params);
    }

    @Override
    public boolean updateFaceRecognition(FaceRecognition face) {
        return getSqlSessionTemplate().update("com.vxianjin.gringotts.web.dao.IFaceRecognitionDao.updateByUserId", face) > 0;
    }

}
