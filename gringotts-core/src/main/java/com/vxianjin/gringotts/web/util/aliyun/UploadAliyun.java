package com.vxianjin.gringotts.web.util.aliyun;

import java.io.File;

/**
 * 图片上传接口_阿里云oss服务器
 */
public interface UploadAliyun {
    /**
     * 图片上传
     */
    String uploadImage(File file, String key);

    String createKey();
}
