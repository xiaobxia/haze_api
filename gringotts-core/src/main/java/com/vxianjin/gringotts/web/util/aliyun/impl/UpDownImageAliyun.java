package com.vxianjin.gringotts.web.util.aliyun.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.vxianjin.gringotts.web.util.aliyun.UploadAliyun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Administrator on 2018/1/13.
 */
@Component("upDownImageAliyun")
public class UpDownImageAliyun implements UploadAliyun {
    private static final Logger logger = LoggerFactory.getLogger(UpDownImageAliyun.class);

    //阿里云ACCESS_ID
    @Value("#{aliyunSettings['accessKeyId']}")
    private String accessKeyId;
    //阿里云ACCESS_KEY
    @Value("#{aliyunSettings['accessKeySecret']}")
    private String accessKeySecret;
    //阿里云OSS_ENDPOINT
    @Value("#{aliyunSettings['endPoint']}")
    private String endPoint;
    //阿里云BUCKET_NAME  OSS
    @Value("#{aliyunSettings['bucketName']}")
    private String bucketName;

    @Value("#{aliyunSettings['path']}")
    private String path;

//    private OSSClient ossClient;

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.info("获取文件类型=" + fileExtension);
        if ("bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension) || "png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if ("html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        return "text/html";
    }

    /**
     * 获取阿里云OSS客户端对象
     */
    public OSSClient getOSSClient() {
        return new OSSClient(endPoint, accessKeyId, accessKeySecret);
    }

    /**
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代
     *
     * @param file file
     * @param key key
     * @return str
     */

    @Override
    public String uploadImage(File file, String key) {
        String resultStr = "";
        logger.info("endPoint = " + endPoint);
        logger.info("uploadImage start");
        OSSClient ossClient = null;
        try {

            ossClient = getOSSClient();

            InputStream is = new FileInputStream(file);
            String fileName = file.getName();
            long fileSize = file.length();
            logger.info("uploadImage key=" + key);
            logger.info("uploadImage fileName = " + fileName);
            logger.info("uploadImage fileSize = " + fileSize);
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(key));
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte");
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, path + key, is, metadata);
            logger.info("putResult :" + JSON.toJSONString(putResult));
            //解析结果
            resultStr = "success";
        } catch (Exception e) {
            resultStr = "error";
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }finally {
            if(null != ossClient){
                ossClient.shutdown();
            }
        }
        return resultStr;
    }

    @Override
    public String createKey() {
        Random random = new Random();

        String firstFolder = "0" + random.nextInt(10); // 一级目录
        int temp = random.nextInt(100); // 二级目录

        String seceondFolder;
        if (temp < 10) {
            seceondFolder = firstFolder + "_0" + temp;
        } else {
            seceondFolder = firstFolder + "_" + temp;
        }

        String thirdFolder; // 三级目录
        int temp1 = random.nextInt(1000);
        if (temp1 < 10) {
            thirdFolder = seceondFolder + "_00" + temp1;
        } else if (temp1 > 9 && temp1 < 100) {
            thirdFolder = seceondFolder + "_0" + temp1;
        } else {
            thirdFolder = seceondFolder + "_" + temp1;
        }

        String url = File.separator + "qbm_" + firstFolder + File.separator + seceondFolder + File.separator + thirdFolder
                + File.separator;
        return url;
    }
}
