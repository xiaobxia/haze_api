package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.web.utils.SpringUtils;
import com.vxianjin.gringotts.web.utils.UploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ltq 批量上传图片和附件
 */
@Controller
@RequestMapping("uploadF/")
public class FileUploadController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
    private static long maxSize = 1000000;
    private static HashMap<String, String> extMap = new HashMap<>();

    static {
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file",
                "doc,docx,xls,xlsx,ppt,pptx,pdf,htm,html,txt,zip,rar,gz,bz2");
    }

    @RequestMapping(value = "/uploadFiles")
    public void uploadFiles(HttpServletRequest request,
                            HttpServletResponse response) {
        // 定义允许上传的文件扩展名
        Map<String, Object> param = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
                .getFile("imgFile");
        String realFileName = file.getOriginalFilename();
        if (file.getSize() > maxSize) {
            param.put("message", "上传文件大小超过限制");
            param.put("error", 1);
            SpringUtils.renderJson(response, param);
            return;
        }
        /** 构建图片保存的目录 **/
        String filePathDir = UploadUtils.getRelatedPath();
        /** 得到图片保存目录的真实路径 **/
        String fileRealPathDir = UploadUtils.getRealPath();

        /** 获取文件的后缀 **/
        String suffix = realFileName.substring(realFileName.lastIndexOf("."));
        if (Arrays.asList(extMap.get("image").split(",")).contains(
                realFileName.substring(realFileName.lastIndexOf(".") + 1)
                        .toLowerCase())) {
            // /**使用UUID生成文件名称**/
            String fileImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
            // String fileImageName = multipartFile.getOriginalFilename();
            /** 拼成完整的文件保存路径加文件 **/
            String fileName = fileRealPathDir + File.separator + fileImageName;

            String resultFilePath = filePathDir + "/" + fileImageName;
            File newFile = new File(fileName);
            try {
                FileCopyUtils.copy(file.getBytes(), newFile);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(resultFilePath)) {
                resultFilePath = resultFilePath.replaceAll("\\\\", "/");
            }
            String reqPath = resultFilePath;
            // 返回路径给页面上传
            param.put("url", reqPath);
            param.put("error", 0);
        } else {
            param.put("url", "");
            param.put("message", "上传文件扩展名是不允许的扩展名");
            param.put("error", 1);
        }
        // String ret = "{\"err\":0,\"url\":\""+reqPath+"\"}";
        SpringUtils.renderJson(response, param);

    }

    /**
     * 删除物理文件 2013-12-12 cjx
     */
    @RequestMapping(value = "/removeImg", method = RequestMethod.POST)
    public void removeImg(HttpServletRequest request,
                          HttpServletResponse response) {
        String msg = "0";
        try {
            String imgUrlString = request.getParameter("imgUrl");
            if (StringUtils.isNotBlank(imgUrlString)) {
                String fileRealPathDir = request.getSession()
                        .getServletContext().getRealPath(imgUrlString);
                File file = new File(fileRealPathDir);
                if (file.exists()) {
                    file.delete();
                }
                msg = "1";
            }
        } catch (Exception e) {
            log.error("removeImg error ", e);
        }
        SpringUtils.renderJson(response, msg);
    }

}
