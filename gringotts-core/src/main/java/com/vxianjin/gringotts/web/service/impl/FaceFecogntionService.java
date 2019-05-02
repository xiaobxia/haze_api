package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IFaceRecognitionDao;
import com.vxianjin.gringotts.web.pojo.FaceRecognition;
import com.vxianjin.gringotts.web.service.IFaceFecogntionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceFecogntionService implements IFaceFecogntionService {
    @Autowired
    public IFaceRecognitionDao faceRecognitionDao;

    @Override
    public boolean saveFaceRecognitionDao(FaceRecognition face) {
        return faceRecognitionDao.saveFaceRecognitionDao(face);
    }

    @Override
    public FaceRecognition selectByUserId(Integer userId) {
        return faceRecognitionDao.selectByUserId(userId);
    }

    @Override
    public boolean updateFaceRecognition(FaceRecognition face) {
        return faceRecognitionDao.updateFaceRecognition(face);
    }
}
