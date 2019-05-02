package com.vxianjin.gringotts.constant;

import java.util.ResourceBundle;


//@Deprecated
public class FaceConfig {

    private static ResourceBundle propertiesLoader = ResourceBundle.getBundle("face");
    public static final String FACE_API_FACE_URL = propertiesLoader.getString("faceApiFaceUrl");//人脸识别
    public static final String FACE_API_BASE_URL = propertiesLoader.getString("faceApiBaseUrl");//身证扫描
    public static final String FACE_API_KEY = propertiesLoader.getString("faceApiKey");//风控接口加密密码
    public static final String FACE_API_SECRET = propertiesLoader.getString("faceApiSecret");//风控接口地址
}
