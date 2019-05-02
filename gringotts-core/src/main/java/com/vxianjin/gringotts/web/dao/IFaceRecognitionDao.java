package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.FaceRecognition;

public interface IFaceRecognitionDao {
    /**
     * 添加人脸识别参数
     *
     * @param face
     * @return
     */
    boolean saveFaceRecognitionDao(FaceRecognition face);

    /**
     * 根据用户编号查询人脸参数
     */
    FaceRecognition selectByUserId(Integer userId);

    /**
     * 修改人脸参数
     *
     * @param face
     * @return
     */
    boolean updateFaceRecognition(FaceRecognition face);
}
