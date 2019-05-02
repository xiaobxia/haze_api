package com.vxianjin.gringotts.web.common.Certification;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.FaceConfig;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.FaceRecognition;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IFaceFecogntionService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 相关认证服务类
 *
 * @author user
 */
@Service
public class HttpCertification implements IHttpCertification {
    private static final Logger logger = LoggerFactory.getLogger(HttpCertification.class);
    @Resource
    private IFaceFecogntionService faceFecogntionService;
    @Resource
    private IUserService userService;

    @Override
    public ResponseContent bankCard(Map<String, String> params) {
        return new ResponseContent("0", "成功");
    }

    /**
     * 人脸识别
     *
     * @param map map
     * @return res
     */
    @Override
    public ResponseContent face(User user, Map<String, String> map, String apiKey, String apiSecret) {
        ResponseContent resultCode = new ResponseContent("-1", "失败");
        if (StringUtils.isNotBlank(map.get("userId"))) {
            Map<String, String> interval = SysCacheUtils.getConfigParams(BackConfigParams.INTERVAL);
            int maxCount = 3;
            if (interval != null) {
                maxCount = interval.get("INTERVAL_REAL_COUNT") == null ? 3 : Integer.parseInt(interval.get("INTERVAL_REAL_COUNT"));
            }
            boolean toface = false;
            if (!"1".equals(user.getRealnameStatus())) {
                if (user.getLastFullTime() != null) {
                    int day = 0;
                    day = DateUtil.daysBetween(user.getLastFullTime(), new Date());
                    if (day >= 1) {
                        toface = true;
                    } else if (day == 0) {
                        if (user.getRealCount() % maxCount != 0) {
                            toface = true;
                        }
                    }
                } else {
                    toface = true;
                }
            }
            if (toface) {
                Map<String, String> textMap = new HashMap<>();
                //可以设置多个input的name，value
                textMap.put("api_key", apiKey);
                textMap.put("api_secret", apiSecret);
                // 确定本次比对为“有源比对”或“无源比对”。取值只为“1”或“0”
                textMap.put("comparison_type", "1");
                // 确定待比对图片的类型。取值只为“meglive”、“facetoken”、“raw_image”三者之一
                textMap.put("face_image_type", "raw_image");
                textMap.put("idcard_name", map.get("idcard_name"));
                textMap.put("idcard_number", map.get("idcard_number"));
                Map<String, String> fileMap = new HashMap<>();
                //人脸图片地址
                String filePath = map.get("faceImageAttach");
                fileMap.put("image", filePath.replaceAll("\\\\", "\\/"));
                logger.info("人脸识别参数params" + textMap.toString() + "图片地址：" + fileMap.toString());
                String ret = HttpUtil.formUploadImage(FaceConfig.FACE_API_FACE_URL + "/faceid/v2/verify", textMap, fileMap, "");
                logger.info("interface" + FaceConfig.FACE_API_FACE_URL + "/faceid/v2/verify return info :" + ret);
                if (StringUtils.isNotBlank(ret)) {
                    Map<String, Object> result = JSONUtil.parseJSON2Map(ret);
                    if (!result.containsKey("error_message")) {
                        // 有源比对时，数据源人脸照片与待验证人脸照的比对结果
                        Map<String, Object> resultFaceid = (Map<String, Object>) result.get("result_faceid");
                        // 	人脸比对接口的返回的误识几率参考值
                        //	“1e-3”：误识率为千分之一的置信度阈值；
                        //	“1e-4”：误识率为万分之一的置信度阈值；
                        //	“1e-5”：误识率为十万分之一的置信度阈值;
                        //	“1e-6”：误识率为百万分之一的置信度阈值。
                        Map<String, Object> thresholds = (Map<String, Object>) resultFaceid.get("thresholds");
                        // 比对率达到十万分之一的才被认为人脸认证通过
                        resultCode.setMsg("人脸识别实名认证失败！请检查身份证和头像");
                        if (Float.valueOf(resultFaceid.get("confidence") + "") >= Float.valueOf(thresholds.get("1e-4") + "")) {
                            resultCode.setCode("0");
                            resultCode.setMsg("成功");
                        }
                        HashMap<String, String> resultMap = new HashMap<>();
                        resultMap.put("confidence", resultFaceid.get("confidence").toString());
                        resultMap.put("le3", thresholds.get("1e-3").toString());
                        resultMap.put("le4", thresholds.get("1e-4").toString());
                        resultMap.put("le5", thresholds.get("1e-5").toString());
                        resultMap.put("le6", thresholds.get("1e-6").toString());
                        resultMap.put("userId", map.get("userId"));
                        resultCode.setParamsMap(resultMap);
                        //保存人脸识别报告
                        saveFace(resultMap);
                        user.setRealCount(user.getRealCount() + 1);
                        user.setLastFullTime(new Date());
                        userService.updateRealCount(user);
                    } else {
                        logger.info("interface error error_message=" + result.get("error_message").toString());
                        resultCode.setCode("-1");
                        String msg = null;
                        msg = User.FACEID_MSG_TYPE.get(result.get("error_message").toString());
                        if (msg != null) {
                            resultCode.setMsg(msg);
                        } else {
                            resultCode.setMsg(User.FACEID_MSG_TYPE.get("OTHER"));
                        }
                        logger.info("interface error userPhone=" + user.getUserPhone() + " userId=" + user.getId() + " errorMsg=" + FaceConfig.FACE_API_FACE_URL + "/faceid/v2/verify return info :" + ret);
                    }
                }

            } else {
                resultCode.setMsg("您当天识别实名失败次数已超过上线");
            }
        } else {
            logger.info("人脸识别的时候需要传入userid编号已便保存人脸识别信息");
            resultCode.setMsg("请传入用户唯一标识编号");
        }
        return resultCode;
    }
    @Override
    public ResponseContent idcardScanning(Map<String, String> map, String apiKey, String apiSecret) {
        ResponseContent resultCode = new ResponseContent("-1", "失败");
        HashMap<String, String> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("api_secret", apiSecret);
        // 返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。
        params.put("legality", "1");
        Map<String, String> fileMap = new HashMap<>();
        /*图片地址*/
        fileMap.put("image", map.get("filePath"));
        String contentType = "";//image/png
//		System.out.println("请求参数"+params.toString());
        logger.info("idcardScanning 请求参数" + fileMap.toString());
        String ret = HttpUtil.formUploadImage(FaceConfig.FACE_API_BASE_URL + "/faceid/v1/ocridcard", params, fileMap, contentType);
        Map<String, Object> checkResult;
        logger.info("idcardScanning 返回结果" + ret);
        try {
            checkResult = JSONUtil.parseJSON2Map(ret);
        } catch (Exception e) {
            logger.error("check result error:{}",e);
            resultCode.setMsg("识别失败,请重新尝试");
            return resultCode;
        }
        if (checkResult.containsKey("error")) {
            resultCode.setMsg("识别失败,请重新尝试");
            return resultCode;
        }
        boolean isIdCardImageFront = "front".equals(checkResult.get("side"));
        // 保存附件信息至数据库
        // 保存一次身份证信息至数据库  但是不更新该用户的人脸识别状态
        // 判断该图片是否为身份证件正面 如果是正面那么将识别出的姓名和身份证号信息保存至
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("isIdCardImageFront", isIdCardImageFront + "");
        logger.info("idcardScanning checkResult=" + (checkResult != null ? JSON.toJSONString(checkResult) : "null"));
        // 判断是否可能是正式身份证照片，当ID Photo对应可能性值是最大的即可(有一样大的也认为是最大的)

        int isIdPhoto = checkIdPhoto(checkResult);
        logger.info("isIdPhoto :" + isIdPhoto);
        if (isIdPhoto < 0) {
            resultCode.setCode("-1");
            switch (isIdPhoto) {
                case -1:
                    resultCode.setMsg("系统异常");
                    break;
                case -2:
                    resultCode.setMsg("不支持照片扫描");
                    break;
                case -3:
                    resultCode.setMsg("请使用身份证原件");
                    break;
                default:
                    resultCode.setMsg("系统异常");
            }
            //正面
        } else if (isIdCardImageFront) {
            String idCard = checkResult.get("id_card_number") + "";
            String userName = checkResult.get("name") + "";
            String gender = checkResult.get("gender") + "";
            String race = checkResult.get("race") + "";

            resultMap.put("id_card_number", idCard);
            resultMap.put("name", userName);
            resultMap.put("gender", gender);
            resultMap.put("race", race);
            resultMap.put("address", checkResult.get("address") + "");
            resultCode.setCode("0");
            resultCode.setMsg("扫描成功");
        } else {//背面
            String validDate = checkResult.get("valid_date") + "";
            String issuedBy = checkResult.get("issued_by") + "";
            resultMap.put("valid_date", validDate);
            resultMap.put("issued_by", issuedBy);
            resultCode.setCode("0");
            resultCode.setMsg("扫描成功");
        }
        resultCode.setParamsMap(resultMap);
        return resultCode;
    }

    /**
     * 检查是否是正式身份证照片
     *
     * @param checkResult result
     * @return -1 异常，-2 照片扫描，-3 非身份证原件 ，1 正式身份证
     */
    private int checkIdPhoto(Map<String, Object> checkResult) {
        try {
            JSONObject legalityJson = (JSONObject) checkResult.get("legality");
            if (legalityJson.getDouble("ID Photo") >= legalityJson.getDouble("Edited")
                    && legalityJson.getDouble("ID Photo") >= legalityJson.getDouble("Photocopy")
                    && legalityJson.getDouble("ID Photo") >= legalityJson.getDouble("Screen")
                    && legalityJson.getDouble("ID Photo") >= legalityJson.getDouble("Temporary ID Photo")) {
                return 1;
            } else if (legalityJson.getDouble("Screen") > legalityJson.getDouble("Edited")
                    && legalityJson.getDouble("Screen") > legalityJson.getDouble("Photocopy")
                    && legalityJson.getDouble("Screen") > legalityJson.getDouble("ID Photo")
                    && legalityJson.getDouble("Screen") > legalityJson.getDouble("Temporary ID Photo")) {
                return -2;
            } else {
                return -3;
            }
        } catch (Exception e) {
            logger.error("legalityJson has error:", e);
            return -1;
        }
    }


    /**
     * 保存人脸识别信息
     *
     * @param parasmMap mao
     */
    private void saveFace(HashMap<String, String> parasmMap) {
        if (parasmMap != null) {
            FaceRecognition face = faceFecogntionService.selectByUserId(Integer.parseInt(parasmMap.get("userId")));
            if (face == null) {
                face = new FaceRecognition();
                face.setConfidence(parasmMap.get("confidence"));
                face.setUserId(Integer.parseInt(parasmMap.get("userId")));
                face.setLe3(parasmMap.get("le3"));
                face.setLe4(parasmMap.get("le4"));
                face.setLe5(parasmMap.get("le5"));
                face.setLe6(parasmMap.get("le6"));
                if (Float.valueOf(parasmMap.get("confidence") + "") >= Float.valueOf(parasmMap.get("le4") + "")) {
                    face.setStatus("1");
                } else {
                    face.setStatus("0");
                }
                faceFecogntionService.saveFaceRecognitionDao(face);
            } else {
                face.setConfidence(parasmMap.get("confidence"));
                face.setUserId(Integer.parseInt(parasmMap.get("userId")));
                face.setLe3(parasmMap.get("le3"));
                face.setLe4(parasmMap.get("le4"));
                face.setLe5(parasmMap.get("le5"));
                face.setLe6(parasmMap.get("le6"));
                face.setId(face.getId());
                if (Float.valueOf(parasmMap.get("confidence") + "") >= Float.valueOf(parasmMap.get("le4") + "")) {
                    face.setStatus("1");
                } else {
                    face.setStatus("0");
                }
                faceFecogntionService.updateFaceRecognition(face);
            }
        }
    }
}
