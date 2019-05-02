package com.vxianjin.gringotts.util;

import com.alibaba.fastjson.util.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Random;

/**
 * 文件帮助类
 *
 * @author zzc
 */
public class FileUtil {
    /**
     * 日志打印
     */
    private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 读取文件内容
     *
     * @param
     * @return void
     * @throws Exception
     */
    public static String readFileContent(File file) throws Exception {
        //读取合同模板内容
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] temp = new byte[500];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// 写内容到baos中
            if (bis.available() < 500) {
                temp = new byte[bis.available()];
            }
            while (bis.available() > 0 && (bis.read(temp)) != -1) {
                baos.write(temp);
                if (bis.available() < 500) {
                    temp = new byte[bis.available()];
                } else {
                    temp = new byte[500];
                }
            }
            fis.close();
            bis.close();
            byte[] bb = baos.toByteArray();
            return new String(bb, "utf-8");
        } catch (FileNotFoundException e) {
            throw new Exception("找不到文件模板！");
        } catch (IOException e) {
            throw new Exception("找不到文件模板！");
        }
    }

    /***
     * 根据图片路径将图片转成Base64数据
     * @return Base64数据
     */
    public static String GetImageStr(String imgFilePath) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //String imgFile = "E://0010001//2014-04-23//jk602745.jpg";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            //System.out.println(in.available());
            in.read(data);
            in.close();
        } catch (IOException e) {
            System.out.println("上传的印章图片转sealData错误：" + e.getMessage());
            e.printStackTrace();
        }
        //对字节数组Base64编码
        byte[] en = Base64.encodeBase64(data);
        return new String(en);//返回Base64编码过的字节数组字符串
    }

    /***
     * 根据图片Base64数据生成图片文件
     * @param imgStr
     * @param imgFilePath
     * @return
     */
    public static boolean GenerateImage(String imgStr, String imgFilePath) {//对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
        {
            return false;
        }
        try {
            //Base64解码

            byte[] b = Base64.decodeBase64(new String(imgStr).getBytes());

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            //String imgFilePath = "d://221.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            System.out.println("sealData转印章图片错误：" + e.getMessage());
            //e.printStackTrace();
            return false;
        }
    }

    /***
     * PDF转图片
     */
    public static boolean pdf2Image(String PdfFilePath, String imgFilePath) {

        File file = new File(PdfFilePath);
        File dstFile = new File(imgFilePath);
        PDDocument pdDocument;
        try {
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* 0表示第一页，300表示转换dpi，dpi越大转换后越清晰，相对转换速度越慢 */
            BufferedImage image = renderer.renderImageWithDPI(0, 300);
            ImageIO.write(image, "png", dstFile);
            return true;
        } catch (IOException e) {
            System.out.println("PDF转图片错误：" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过MD5获取文件处理字符串
     *
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file) {

        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            LOGGER.error("File GET MD5 ERROE！", e);
        } finally {
            IOUtils.close(in);
        }
        return value;
    }


    /**
     * 创建单个文件
     *
     * @param destFileName 目标文件名
     * @return 创建成功，返回true，否则返回false
     */
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);

        //文件已存在
        if (file.exists()) {
            LOGGER.warn("target file:" + destFileName + " exist already!");
            return false;
        }
        //所传为目录 非文件
        if (destFileName.endsWith(File.separator)) {
            LOGGER.warn("target file:" + destFileName + " should not be a directory!");
            return false;
        }
        // 判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            LOGGER.info("target file parent directory not exist,try create.");
            if (!file.getParentFile().mkdirs()) {
                LOGGER.error("create parent directory failed!");
                return false;
            }
            LOGGER.info("create parent directory success!");
        }
        // 创建目标文件
        try {
            if (file.createNewFile()) {
                LOGGER.info("create " + file.getName() + " success");
                return true;
            } else {
                LOGGER.error("create " + file.getName() + " failed");
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("create file happened error!");
            return false;
        }
    }

    /**
     * 创建图片存储路径
     * @return
     */
    public static String createKey(){
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


    /**
     * 创建目录   dir.mkdirs
     *
     * @param destDirName 目标目录名
     * @return 目录创建成功放回true，否则返回false
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        //如果存在直接返回ture，不需要创建
        if (dir.exists()) {
            LOGGER.error("create" + destDirName + " exist already!");
            return true;
        }
        //如果不是文件分割号结束则添加，因为只创建目录
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // 创建目标目录
        if (dir.mkdirs()) {
            LOGGER.info("create " + destDirName + " success");
            return true;
        } else {
            LOGGER.info("create " + destDirName + " failed");
            return false;
        }
    }

    /**
     * 创建临时文件
     * 生成的临时文件会有19位的Long字符串
     *
     * @param prefix  临时文件名的前缀
     * @param suffix  临时文件名的后缀
     * @param dirName 临时文件所在的目录，如果输入null，则在用户的文档目录下创建临时文件
     * @return 临时文件创建成功返回true，否则返回false
     */
    public static String createTempFile(String prefix, String suffix,
                                        String dirName) {
        File tempFile = null;
        //没有设定零时文件夹目录
        if (dirName == null) {
            try {
                // 在默认文件夹下创建临时文件
                tempFile = File.createTempFile(prefix, suffix);
                // 返回临时文件的路径
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                LOGGER.error("temp file:" + dirName + " create error!", e);
                return null;
            }
        } else {
            File dir = new File(dirName);
            // 如果临时文件所在目录不存在，首先创建
            if (!dir.exists()) {
                //创建临时文件目录失败
                if (!createDir(dirName)) {
                    return null;
                }
            }
            try {
                // 在指定目录下创建临时文件
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                LOGGER.error("create temp file happened error!", e);
                return null;
            }
        }

    }

    public static void delFileOnExist(String filepath) {
        File file = new File(filepath);
        //file.deleteOnExit();//此方法只能删除临时文件此处无效
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
}
