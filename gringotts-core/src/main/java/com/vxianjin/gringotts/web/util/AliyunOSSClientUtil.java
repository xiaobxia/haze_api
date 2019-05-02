package com.vxianjin.gringotts.web.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * /**
 * Author :${cqry_2017}
 * Date   :2017-10-14 16:43.
 */
public class AliyunOSSClientUtil {

    //阿里云API的内或外网域名
//    public static final String ENDPOINT = "uploads.jx-money.com";
    public static final String ENDPOINT = "fully-online.oss-cn-hangzhou-internal.aliyuncs.com";
    //阿里云API的密钥Access Key ID
    public static final String ACCESS_KEY_ID = "LTAIU2TLHViOwb9v";
    //阿里云API的密钥Access Key Secret
    public static final String ACCESS_KEY_SECRET = "z4vKbsW3o9k2Bolt9RrmR5PDvgXSQv";
    //阿里云API的bucket名称
    public static final String BACKET_NAME = "fully-online";
    //阿里云API的文件夹名称
    public static final String FOLDER = "uploads";
    private static Logger logger = LoggerFactory.getLogger(AliyunOSSClientUtil.class);

//    private static OSSClient ossClient = new OSSClient(ENDPOINT,ACCESS_KEY_ID,ACCESS_KEY_SECRET);

    /**
     * 获取阿里云客户端对象
     *
     * @return osseClient
     */
    public static OSSClient getOSSClient() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 创建存储空间
     *
     * @param ossClient
     * @param bucketName
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间
        final String targetName = bucketName;
        if (!ossClient.doesBucketExist(bucketName)) {
            //不存在则创建
            Bucket bucket = ossClient.createBucket(bucketName);
//            logger.info("目标不存在，创建存储空间成功!");
            return bucket.getName();
        }
        return targetName;
    }

    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
//        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient, String bucketName, String folder) {
        //文件夹名
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
//            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     *
     * @param ossClient  oss连接
     * @param bucketName 存储空间
     * @param folder     模拟文件夹名 如"qj_nanjing/"
     * @param key        Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
        ossClient.deleteObject(bucketName, folder + key);
//        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }


    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public static String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            logger.info("uploadFile2OSS start");
            OSSClient client = getOSSClient();
            logger.info("client" + client);
            PutObjectResult putResult = client.putObject(BACKET_NAME, FOLDER + fileName, instream, objectMetadata);
            logger.info("putResult" + putResult);
            ret = putResult.getETag();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        //默认返回格式
        return "image/jpeg";
    }

    //测试
    public static void main(String[] args) throws Exception {
        //初始化OSSClient
        OSSClient ossClient = AliyunOSSClientUtil.getOSSClient();
        //上传文件
        String files = "D:\\test.png";
        String[] file = files.split(",");
        for (String filename : file) {
            //System.out.println("filename:"+filename);
            File filess = new File(filename);

            InputStream inputStream = new FileInputStream(files);
            MultipartFile multipartFile = new MockMultipartFile("file", filess.getName(), "text/plain", IOUtils.toByteArray(inputStream));


            AliyunOSSClientUtil util = new AliyunOSSClientUtil();
            String name = util.uploadImg2Oss(multipartFile);
            System.out.println("文件名: " + name);
            System.out.println("文件路径：" + util.getImgUrl(name));
//            String md5key = AliyunOSSClientUtil.uploadFile2OSS(inputStream,filess.getName());
//            logger.info("上传后的文件MD5数字唯一签名:" + md5key);
        }


    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(FOLDER + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传图片并返回url
     *
     * @param file
     * @return
     */
    public String uploadAndGetUrl(MultipartFile file) {
        return getImgUrl(uploadImg2Oss(file));
    }

    /**
     * File转换为MultipartFile类型
     *
     * @param path
     * @return
     * @throws IOException
     */
    public MultipartFile file2MultipartFile(String path) throws IOException {
        File appImage = new File(path);
        InputStream inputStream = new FileInputStream(appImage);
        MultipartFile multipartFile = new MockMultipartFile("file", appImage.getName(), "text/plain", IOUtils.toByteArray(inputStream));
        return multipartFile;
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Oss(String url) {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("图片上传失败");
        }
    }

    public String uploadImg2Oss(MultipartFile file) {
        logger.info("上传文件开始!+uploadImg2Oss");
        if (file.getSize() > 1024 * 1024) {
            throw new RuntimeException("上传图片大小不能超过1M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            logger.info("获得inputStream：" + inputStream);
            uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new RuntimeException("图片上传失败");
        }
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = getOSSClient().generatePresignedUrl(BACKET_NAME, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
}