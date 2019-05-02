package com.vxianjin.gringotts.constant;

import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;

import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User : zhangsh
 * Date : 2017年1月5日
 */
public class CollectionConstant {


    //    #催收系统数据交互地址
    public static String getCollectionPath() {
        Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.COLLECTION);
        return keys.get("COLLECTION_PATH");
    }

    //    #催收扣款加密密钥
    public static String getCollectionSign() {
        Map<String, String> keys = SysCacheUtils.getConfigParams(BackConfigParams.COLLECTION);
        return keys.get("COLLECTION_SIGN");
    }

}
