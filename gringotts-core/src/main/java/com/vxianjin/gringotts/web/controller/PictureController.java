package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.common.ResponseStatus;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.FileUtil;
import com.vxianjin.gringotts.util.JpgThumbnail;
import com.vxianjin.gringotts.util.Uploader;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.common.Certification.IHttpCertification;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IUserInfoImageService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.util.ConfigLoader;
import com.vxianjin.gringotts.web.util.aliyun.UploadAliyun;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import org.apache.log4j.Logger;
/**
 * 上传用户图片信息
 *
 * @author user
 */
@Controller
public class PictureController extends BaseController {
    private static final HashMap<String, String> TypeMap = new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(PictureController.class);
    private static String domainOfBucket = "";
    private static PropertiesConfiguration configuration = null;
    private static String appCode = "";
    static {
        TypeMap.put("image", "gif,jpg,jpeg,png,bmp");
        TypeMap.put("flash", "swf,flv");
        TypeMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        TypeMap.put("file", "doc,docx,xls,xlsx,ppt,pptx,htm,html,txt,dwg,pdf");

        appCode = PropertiesConfigUtil.get("RISK_BUSINESS");

        configuration = ConfigLoader.getInstance().getAliConfigurations();

        if (null != configuration) {
            domainOfBucket = configuration.getString("domainOfBucket");
        }
    }

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserInfoImageService userInfoImageService;
    @Autowired
    private IHttpCertification httpCertification;
    @Autowired
    private UploadAliyun uploadAliyun;

    /**
     * 个人信息-身份证信息识别-身份证图片信息保存至数据库 身份证图片保存至服务器磁盘      ""10""
     *
     * @param file
     * @return
     */
    @RequestMapping("picture/upload-image")
    public void imageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "attach", required = true) CommonsMultipartFile file, String type) {
        Map<String, Object> json = new HashMap<String, Object>();
        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
        try {
            // 判断是否为空 文件大小是否合适
            if (!file.isEmpty()) {
                int type1 = request.getParameter("type") == null ? 0 : Integer.parseInt(request.getParameter("type"));
                switch (type1) {
                    case 1:// 身份证
                        break;
                    case 2://学历证明
                        break;
                    case 3://工作证明
//                        result = workImageUpload(request, response, file);
                        break;
                    case 4://收入证明
                        break;
                    case 5://财产证明
                        break;
                    case 6://工牌照片
//                        result = workcardImg(request, response, file);
                        break;
                    case 7://个人名片
                        break;
                    case 8://银行卡
                        break;
                    case 9://好房贷房产证
                        break;
                    case 10://人脸识别
                        result = recognitionImageUpload(request, response, file);
                        break;
                    case 11://身份证正面
                        result = idCardImageUploadZ(request, response, file);
                        break;
                    case 12://身份证反面
                        result = idCardImageUploadF(request, response, file);
                        break;
                    case 100://其它证明
                        break;
                    default:
                        break;
                }
            } else {
                result.setMsg("不能上传空文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result.isSuccessed()) {
                Map<String, String> map = result.getParamsMap();
                if (map != null) {
                    String idCard = map.get("id_card_number");
                    String userName = map.get("name");
                    String gender = map.get("gender");
                    json.put("data", map);
                }
            }
            json.put("code", result.getCode());
            json.put("message", result.getMsg());
            JSONUtil.toObjectJson(response, JSONUtil.beanToJson(json));
        }
    }

    /**
     * 身份证
     *
     * @param request
     * @param response
     * @param file
     * @return
     */
    public ResponseContent idCardImageUpload(HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile file) {
        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
        // 当前登录用户
        User logUser = this.loginFrontUserByDeiceId(request);
        try {
            if (logUser != null) {
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                long fileSize = 2 * 1024 * 1024;
                // 如果文件大小大于限制
                if (file.getSize() > fileSize) {
                    result.setMsg("图片过大,请选择小于2M的图片");
                    return result;
                }
                // 获取文件名字
                String originalFilename = file.getOriginalFilename();
                // 获取文件格式 jpg
                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
                    result.setMsg("上传图片格式不符合规范");
                    return result;
                }
                if (!ServletFileUpload.isMultipartContent(request)) {
                    result.setMsg("上传图片失败");
                    return result;
                }
                // 获取项目的真实路径
                //String realPath = request.getSession().getServletContext().getRealPath("/" + Constant.FILEPATH);
                String realPath = Constant.FILEPATH_CORE;
                // 获取文件类型 jpg
                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
                // 获取 文件位置
                String filePath = Uploader.getQuickPathname(ext);
                // 拼接全路径
                File uploadedFile = new File(realPath + "/" + filePath);//
                org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
                String path = uploadedFile.getPath();
                String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
                JpgThumbnail.getThumbnail(path, newPath, 150, 110);// 生成APP端的手机缩略图
                user.setIdcardImgZ(newPath.substring(0, newPath.length()).replaceAll("\\\\", "\\/"));
                userService.updateByPrimaryKeyUser(user);
                result.setCode("0");
                result.setMsg("上传成功");
                File f = new File(path);//删除压缩前的图片
                if (f.exists()) {
                    f.delete();
                }
            } else {
                result = new ResponseContent(ResponseStatus.LOGIN.getName(), ResponseStatus.LOGIN.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMsg("上传图片处理异常");
        }
        return result;
    }

    /**
     * 身份证正面
     *
     * @param request
     * @param response
     * @param file
     * @return
     */
    public ResponseContent idCardImageUploadZ(HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile file) {
        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
        // 当前登录用户
        User logUser = this.loginFrontUserByDeiceId(request);
        try {
            if (logUser != null) {
                logger.info("idCardImageUploadZ appCode=" + appCode);
                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                long fileSize = 2 * 1024 * 1024;
                // 如果文件大小大于限制
                if (file.getSize() > fileSize) {
                    result.setMsg("图片过大,请选择小于2M的图片");
                    return result;
                }
                // 获取文件名字
                String originalFilename = file.getOriginalFilename();
                // 获取文件格式 jpg
                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
                    result.setMsg("上传图片格式不符合规范");
                    return result;
                }
                if (!ServletFileUpload.isMultipartContent(request)) {
                    result.setMsg("上传图片失败");
                    return result;
                }
                // 获取项目的真实路径
                String realPath = File.separator + Constant.FILEPATH_CORE;
                // 获取文件类型 jpg
                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
                // 获取 文件位置
                String filePath = Uploader.getQuickPathname(ext);
                // 拼接全路径
                File uploadedFile = new File(realPath + FileUtil.createKey() + filePath);//
                org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
                String path = uploadedFile.getPath().replaceAll("\\\\", "\\/");
                HashMap<String, String> params = new HashMap<>();

                //将CommonsMultipartFile转为File
                DiskFileItem fi = (DiskFileItem) file.getFileItem();
                String uploadResult = "";
                uploadResult = uploadAliyun.uploadImage(fi.getStoreLocation(), path);

                if (!path.startsWith("/")) {
                    path = File.separator + path;
                }
                logger.info("idCardImageUploadZ user_id=" + user.getId() + " uploadResult=" + (uploadResult != null ? JSONObject.toJSONString(uploadResult) : "null"));
                //判断上传结果
                if ("success".equals(uploadResult)) {
                    params.put("filePath", domainOfBucket + path);
                    String api_key = getAppConfig(request.getParameter("appName"), "XJX_API_KEY");
                    String api_secret = getAppConfig(request.getParameter("appName"), "XJX_API_SECRET");
                    ResponseContent jsonResult = httpCertification.idcardScanning(params, api_key, api_secret);
                    if (jsonResult.isSuccessed()) {

                        String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
                        Map<String, String> map = jsonResult.getParamsMap();
                        logger.info("map result:{}",JSON.toJSONString(map));
                        JpgThumbnail.getThumbnail(path, newPath, 250, 210);// 生成APP端的手机缩略图
                        String idCard = map.get("id_card_number");
                        String userName = map.get("name");
                        String gender = map.get("gender");
                        String race = map.get("race");
                        logger.info("race = " + race);
                        User userCard = userService.searchByUserIDCard(idCard);
                        if (userCard == null) {
                            if (StringUtils.isNotEmpty(idCard) && StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(gender)) {
                                user.setIdNumber(idCard);
                                user.setRealname(userName);
                                user.setUserSex(gender);
                                user.setUserRace(race);
                                logger.info("user info :{}", JSON.toJSONString(user));
                            }
                            String url = newPath.substring(0, newPath.length()).replaceAll("\\\\", "\\/");
                            //将CommonsMultipartFile转为File
                            DiskFileItem dfi = (DiskFileItem) file.getFileItem();
                            uploadAliyun.uploadImage(dfi.getStoreLocation(), url);

                            user.setIdcardImgZ(url);
                            logger.info("userRace = " + user.getUserRace());
                            logger.info("user end :{}",JSON.toJSONString(user));
                            userService.updateByPrimaryKeyUser(user);
                            result = jsonResult;
                            result.setMsg("扫描成功");
                            logger.info("scan success");
                        } else {
                            result.setMsg("该身份证已扫描绑定过");
                        }
                    } else {
                        result.setMsg(jsonResult.getMsg());
                    }
                } else {
                    result.setMsg("图片上传失败");
                }
                logger.info("删除图片路径1：" + path);
                logger.info("删除图片路径2：" + uploadedFile);
                File f = new File(path);//删除压缩前的图片
                if (f.exists()) {
                    f.delete();
                }
            } else {
                result = new ResponseContent(ResponseStatus.LOGIN.getName(), ResponseStatus.LOGIN.getValue());
            }
        } catch (Exception e) {
            logger.error("idCardImageUploadZ user_id="+(logUser != null ? logUser.getId() : "null")+" error:",e);
            result.setMsg("图片处理失败，请稍后重试");
        }
        logger.info("end result:{}",JSON.toJSONString(result));
        return result;

    }

    //身份证正反面
    public ResponseContent idCardImageUploadF(HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile file) {
        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
        // 当前登录用户
        User logUser = this.loginFrontUserByDeiceId(request);
        try {
            if (logUser != null) {

                logger.info("idCardImageUploadF appCode=" + appCode);

                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                long fileSize = 2 * 1024 * 1024;
                // 如果文件大小大于限制
                if (file.getSize() > fileSize) {
                    result.setMsg("图片过大,请选择小于2M的图片");
                    return result;
                }
                // 获取文件名字
                String originalFilename = file.getOriginalFilename();

                // 获取文件格式 jpg
                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
                    result.setMsg("上传图片格式不符合规范");
                    return result;
                }
                if (!ServletFileUpload.isMultipartContent(request)) {
                    result.setMsg("上传图片失败");
                    return result;
                }
                // 获取项目的真实路径
                //String realPath = request.getSession().getServletContext().getRealPath("/" + Constant.FILEPATH);
                String realPath = File.separator + Constant.FILEPATH_CORE;
                // 获取文件类型 jpg
                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
                // 获取 文件位置
                String filePath = Uploader.getQuickPathname(ext);
                // 拼接全路径
                File uploadedFile = new File(realPath + FileUtil.createKey() + filePath);//
                org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
                String path = uploadedFile.getPath().replaceAll("\\\\", "\\/");
                HashMap<String, String> params = new HashMap<String, String>();

                //将CommonsMultipartFile转为File
                DiskFileItem fi = (DiskFileItem) file.getFileItem();
                String uploadResult = "";

                uploadResult = uploadAliyun.uploadImage(fi.getStoreLocation(), path);


                if (!path.startsWith("/")) {
                    path = File.separator + path;
                }
                logger.info("idCardImageUploadF user_id=" + user.getId() + " uploadResult=" + (uploadResult != null ? JSONObject.toJSONString(uploadResult) : "null"));
                //判断上传结果
                if ("success".equals(uploadResult)) {
                    params.put("filePath", domainOfBucket + path);
                    logger.info("idCardImageUploadF path2=" + path);
                    String api_key = getAppConfig(request.getParameter("appName"), "XJX_API_KEY");
                    String api_secret = getAppConfig(request.getParameter("appName"), "XJX_API_SECRET");
                    ResponseContent jsonResult = httpCertification.idcardScanning(params, api_key, api_secret);

                    if (jsonResult.isSuccessed()) {
                        String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
                        Map<String, String> map = jsonResult.getParamsMap();
                        JpgThumbnail.getThumbnail(path, newPath, 250, 210);// 生成APP端的手机缩略图
                        String issuedBy = map.get("issued_by");
                        String validDate = map.get("valid_date");
                        logger.info("validDate = " + validDate);
                        if (user != null) {
                            user.setRace(validDate);

                            String url = newPath.substring(0, newPath.length()).replaceAll("\\\\", "\\/");
                            //将CommonsMultipartFile转为File
                            DiskFileItem dfi = (DiskFileItem) file.getFileItem();
                            uploadAliyun.uploadImage(dfi.getStoreLocation(), url);


                            user.setIdcardImgF(url);
                            logger.info("validDate = " + user.getRace());
                            userService.updateByPrimaryKeyUser(user);
                            result = jsonResult;
                            result.setMsg("扫描成功");
                            result.setCode("0");
                        } else {
                            result.setMsg("身份证扫描失败");
                        }
                    } else {
                        result.setMsg(jsonResult.getMsg());
                    }
                }else{
                    result.setMsg("图片上传失败");
                }
                logger.info("删除图片路径1：" + path);
                logger.info("删除图片路径2：" + uploadedFile);
                /*********************/
                File f = new File(path);//删除压缩前的图片
                if (f.exists()) f.delete();
            } else {
                result = new ResponseContent(ResponseStatus.LOGIN.getName(), ResponseStatus.LOGIN.getValue());
            }
        } catch (Exception e) {
            logger.error("idCardImageUploadF user_id="+(logUser != null ? logUser.getId() : "null")+" error:",e);
            result.setMsg("图片处理失败，请稍后重试");
        }
        return result;

    }

    /**
     * 人脸识别
     *
     * @return
     */
    public ResponseContent recognitionImageUpload(HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile file) {
        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
        // 当前登录用户
        User logUser = this.loginFrontUserByDeiceId(request);
        try {
            if (logUser != null) {

                logger.info("recognitionImageUpload appCode=" + appCode);

                User user = userService.searchByUserid(Integer.parseInt(logUser.getId()));
                long fileSize = 2 * 1024 * 1024;
                // 如果文件大小大于限制
                if (file.getSize() > fileSize) {
                    result.setMsg("图片过大,请选择小于2M的图片");
                    return result;
                }
                // 获取文件名字 QQ图片20160224093916.jpg
                String originalFilename = file.getOriginalFilename();

                // 获取文件格式 jpg
                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
                    result.setMsg("上传图片格式不符合规范");
                    return result;
                }
                if (!ServletFileUpload.isMultipartContent(request)) {
                    result.setMsg("上传图片失败");
                    return result;
                }
                // 获取项目的真实路径
                String realPath = File.separator + Constant.FILEPATH_CORE;
                // 获取文件类型 jpg
                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
                logger.info("获取文件类型：" + ext);
                // 获取 文件位置
                String filePath = Uploader.getQuickPathname(ext);
                // 拼接全路径 2017-05-19
                File uploadedFile = new File(realPath + FileUtil.createKey() + filePath);//
                org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());

                String path = uploadedFile.getPath().replaceAll("\\\\", "\\/");
//                logger.info("recognitionImageUpload path=" + path);
                if (!path.startsWith("/")) {
                    path = File.separator + path;
                }

                String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
//                logger.info("recognitionImageUpload newPath=" + newPath);
                JpgThumbnail.getThumbnail(path, newPath, 150, 110);// 生成APP端的手机缩略图
                //2017-05-19
                String url = newPath.substring(0, newPath.length()).replaceAll("\\\\", "\\/");
                //将CommonsMultipartFile转为File
                DiskFileItem fi = (DiskFileItem) file.getFileItem();
                uploadAliyun.uploadImage(fi.getStoreLocation(), url);


                user.setHeadPortrait(url);
                userService.updateByPrimaryKeyUser(user);
                result.setCode("0");
                result.setMsg("上传成功");
                File f = new File(path);//删除压缩前的图片
                logger.info("上传成功:" + path);
                if(f.exists())f.delete();
            } else {
                result = new ResponseContent(ResponseStatus.LOGIN.getName(), ResponseStatus.LOGIN.getValue());
            }
        } catch (Exception e) {
            logger.error("recognitionImageUpload user_id="+(logUser != null ? logUser.getId() : "null")+" error:", e);
            result.setMsg("图片处理失败，请稍后重试");
        }
        return result;
    }

//    /**
//     * 工作照片
//     *
//     * @param request
//     * @param response
//     * @param file
//     * @return
//     */
//    public ResponseContent workImageUpload(HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile file) {
//        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
//        // 当前登录用户
//        User logUser = this.loginFrontUserByDeiceId(request);
//        try {
//            if (logUser != null) {
//                long fileSize = 2 * 1024 * 1024;
//                // 如果文件大小大于限制
//                if (file.getSize() > fileSize) {
//                    result.setMsg("图片过大,请选择小于2M的图片");
//                    return result;
//                }
//                // 获取文件名字 QQ图片20160224093916.jpg
//                String originalFilename = file.getOriginalFilename();
//
//                // 获取文件格式 jpg
//                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
//
//                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
//                    result.setMsg("上传图片格式不符合规范");
//                    return result;
//                }
//                if (!ServletFileUpload.isMultipartContent(request)) {
//                    result.setMsg("上传图片失败");
//                    return result;
//                }
//                // 获取项目的真实路径
//                //String realPath = request.getSession().getServletContext().getRealPath("/" + Constant.FILEPATH);
//                String realPath = File.separator + Constant.FILEPATH_CORE;
//                // 获取文件类型 jpg
//                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
//                // 获取 文件位置
//                String filePath = Uploader.getQuickPathname(ext);
//                // 拼接全路径
//                File uploadedFile = new File(realPath + uploadQn.createKey() + filePath);//
//                org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
//
//                String path = uploadedFile.getPath();
//                if (!path.startsWith("/")) {
//                    path = File.separator + path;
//                }
//                String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
//                JpgThumbnail.getThumbnail(path, newPath, 400, 400);// 生成APP端的手机缩略图
//
//                String url = newPath.substring(0, newPath.length()).replaceAll("\\\\", "\\/");
//                //将CommonsMultipartFile转为File
//                DiskFileItem fi = (DiskFileItem) file.getFileItem();
//                uploadQn.uploadImage(fi.getStoreLocation(), url);
//
//                UserInfoImage image = new UserInfoImage();
//                image.setPicName(UserInfoImage.typeMap.get(3));
//                image.setStatus("0");
//                image.setType("3");
//                image.setUrl(url);
//                image.setCreatedDate(new Date());
//                image.setUserId(logUser.getId());
//                userInfoImageService.saveUserImage(image);
//                result.setCode("0");
//                result.setMsg("上传成功");
//                File f = new File(path);//删除压缩前的图片
//                if (f.exists()) {
//                    f.delete();
//                }
//            } else {
//                result = new ResponseContent(ResponseStatus.LOGIN.getName(), ResponseStatus.LOGIN.getValue());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setMsg("上传图片处理异常");
//        }
//        return result;
//    }

//    /**
//     * 工牌照
//     *
//     * @param request
//     * @param response
//     * @param file
//     * @return
//     */
//    public ResponseContent workcardImg(HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile file) {
//        ResponseContent result = new ResponseContent(ResponseStatus.FAILD.getName(), "上传失败");
//        // 当前登录用户
//        User logUser = this.loginFrontUserByDeiceId(request);
//        try {
//            if (logUser != null) {
//                long fileSize = 2 * 1024 * 1024;
//                // 如果文件大小大于限制
//                if (file.getSize() > fileSize) {
//                    result.setMsg("图片过大,请选择小于2M的图片");
//                    return result;
//                }
//                // 获取文件名字 QQ图片20160224093916.jpg
//                String originalFilename = file.getOriginalFilename();
//
//                // 获取文件格式 jpg
//                String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
//
//                if (!Arrays.asList(TypeMap.get("image").split(",")).contains(fileSuffix)) {
//                    result.setMsg("上传图片格式不符合规范");
//                    return result;
//                }
//                if (!ServletFileUpload.isMultipartContent(request)) {
//                    result.setMsg("上传图片失败");
//                    return result;
//                }
//                // 获取项目的真实路径
//                //String realPath = request.getSession().getServletContext().getRealPath("/" + Constant.FILEPATH);
//                String realPath = File.separator + Constant.FILEPATH_CORE;
//                // 获取文件类型 jpg
//                String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
//                // 获取 文件位置
//                String filePath = Uploader.getQuickPathname(ext);
//                // 拼接全路径
//                File uploadedFile = new File(realPath + uploadQn.createKey() + filePath);//
//                org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
//                String path = uploadedFile.getPath();
//                if (!path.startsWith("/")) {
//                    path = File.separator + path;
//                }
//                String newPath = path.substring(0, path.lastIndexOf(".")) + "_appTh.png";
//                JpgThumbnail.getThumbnail(path, newPath, 400, 400);// 生成APP端的手机缩略图
//
//                String url = newPath.substring(0, newPath.length()).replaceAll("\\\\", "\\/");
//                //将CommonsMultipartFile转为File
//                DiskFileItem fi = (DiskFileItem) file.getFileItem();
//                uploadQn.uploadImage(fi.getStoreLocation(), url);
//
//                UserInfoImage image = new UserInfoImage();
//                image.setPicName(UserInfoImage.typeMap.get(3));
//                image.setStatus("0");
//                image.setType("6");
//                image.setUrl(url);
//                image.setCreatedDate(new Date());
//                image.setUserId(logUser.getId());
//                userInfoImageService.saveUserImage(image);
//                result.setCode("0");
//                result.setMsg("上传成功");
//                File f = new File(path);//删除压缩前的图片
//                if (f.exists()) {
//                    f.delete();
//                }
//            } else {
//                result = new ResponseContent(ResponseStatus.LOGIN.getName(), ResponseStatus.LOGIN.getValue());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setMsg("上传图片处理异常");
//        }
//        return result;
//    }
}
