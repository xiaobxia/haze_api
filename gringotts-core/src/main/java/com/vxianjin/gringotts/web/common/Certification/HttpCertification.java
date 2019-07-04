package com.vxianjin.gringotts.web.common.Certification;

import com.alibaba.fastjson.JSON;
import com.vxianjin.gringotts.common.ResponseContent;
import com.vxianjin.gringotts.constant.FaceConfig;
import com.vxianjin.gringotts.util.HttpUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.util.json.JSONUtil;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.common.ud.UdRequestUtils;
import com.vxianjin.gringotts.web.dao.IUserUdcreditInfoDao;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import com.vxianjin.gringotts.web.pojo.FaceRecognition;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.pojo.UserUdcreditInfo;
import com.vxianjin.gringotts.web.service.IFaceFecogntionService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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
    @Resource
    private IUserUdcreditInfoDao userUdcreditInfoDao;

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
        //params.put("legality", "1");
        Map<String, String> fileMap = new HashMap<>();
        /*图片地址*/
        fileMap.put("image", map.get("filePath"));
        String contentType = "";//image/png
//		System.out.println("请求参数"+params.toString());
        logger.info("idcardScanning 请求参数" + fileMap.toString());
        String ret = HttpUtil.formUploadImage(FaceConfig.FACE_API_BASE_URL + "/faceid/v3/ocridcard", params, fileMap, contentType);
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
        boolean isIdCardImageFront = 0 == Integer.parseInt(checkResult.get("side") + "");
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
            String idCard = getResult(checkResult.get("idcard_number") + "");
            String userName = getResult(checkResult.get("name") + "");
            String gender = getResult(checkResult.get("gender") + "");
            String race = getResult(checkResult.get("nationality") + "");

            resultMap.put("id_card_number", idCard);
            resultMap.put("name", userName);
            resultMap.put("gender", gender);
            resultMap.put("race", race);
            resultMap.put("address", getResult(checkResult.get("address") + ""));
            resultCode.setCode("0");
            resultCode.setMsg("扫描成功");
        } else {//背面
            String validDate = getResult(checkResult.get("valid_date_start") + "") + "-" + getResult(checkResult.get("valid_date_end") + "");
            String issuedBy = getResult(checkResult.get("issued_by") + "");
            resultMap.put("valid_date", validDate);
            resultMap.put("issued_by", issuedBy);
            resultCode.setCode("0");
            resultCode.setMsg("扫描成功");
        }
        resultCode.setParamsMap(resultMap);
        return resultCode;
    }

    private ResponseContent udIdCardFace(User user, Map<String, String> params, UserUdcreditInfo udcreditInfo) throws IOException {
        ResponseContent resultCode = new ResponseContent("-1", "失败");
        String resp_front = "";
        if (udcreditInfo != null && StringUtils.isNoneBlank(udcreditInfo.getHeaderSession(), udcreditInfo.getLivingSession())) {

            JSONObject reqJson = new JSONObject();
            reqJson.put("header", UdRequestUtils.getRequestHeader(udcreditInfo.getHeaderSession()));

            JSONObject body = new JSONObject();
            body.put("id_name", params.get("idcard_name"));
            body.put("id_number", params.get("idcard_number"));
            body.put("photo", new HashMap<String, String>(){{
                put("img_file", udcreditInfo.getLivingSession());
                put("img_file_source", "0");
                put("img_file_type", "1");
            }});
            reqJson.put("body", body);
            logger.info("udIdCardFace reqJson = 【{}】", reqJson.toString());

            resp_front = UdRequestUtils.doHttpRequest(PropertiesConfigUtil.get("UD_FACE_GRID_COMPARE") +
                    PropertiesConfigUtil.get("UD_PUB_KEY"), reqJson);
        }


        logger.info("interface udIdCardFace return info :" + resp_front);
        if (StringUtils.isNotBlank(resp_front)) {

            String jsonResult = JSONObject.parseObject(resp_front).getString("result");
            String jsonData = JSONObject.parseObject(resp_front).getString("data");

            Map<String, Object> result = JSONUtil.parseJSON2Map(jsonResult);
            if (!result.containsKey("errorcode")) {
                // 有源比对时，数据源人脸照片与待验证人脸照的比对结果
                Map<String, Object> resultFaceid = JSONUtil.parseJSON2Map(jsonData);

                if ("1".equals(resultFaceid.get("verify_status").toString()) && "T".equals(resultFaceid.get("suggest_result").toString())) {
                    resultCode.setCode("0");
                    resultCode.setMsg("成功");
                } else {
                    switch (resultFaceid.get("verify_status").toString()) {
                        case "2":
                            resultCode.setMsg("认证未通过，姓名与号码不一致 ");
                            break;
                        case "3":
                            resultCode.setMsg("认证未通过，查询无结果");
                            break;
                    }
                    if (resultFaceid.get("result_status") != null) {
                        switch (resultFaceid.get("result_status").toString()) {
                            case "02":
                                resultCode.setMsg("认证未通过，系统判断为不同人 ");
                                break;
                            case "03":
                                resultCode.setMsg("认证未通过，不能确定是否为同一人");
                                break;
                            case "04":
                                resultCode.setMsg("认证未通过，公安网系统无法比对");
                                break;
                            case "05":
                                resultCode.setMsg("认证未通过，公安库中没有网格照");
                                break;
                        }
                    }
                }

                user.setRealCount(user.getRealCount() + 1);
                user.setLastFullTime(new Date());
                userService.updateRealCount(user);
            } else {
                logger.info("interface udIdCardFace error error_message=" + result.get("message").toString());
                resultCode.setCode("-1");
                String msg;
                msg = User.FACEID_MSG_TYPE.get(result.get("message").toString());
                if (msg != null) {
                    resultCode.setMsg(msg);
                } else {
                    resultCode.setMsg(User.FACEID_MSG_TYPE.get("OTHER_UD"));
                }
                logger.info("interface udIdCardFace error userPhone=" + user.getUserPhone() + " userId=" + user.getId() + " errorMsg return info :" + jsonResult);
            }
        }
        return resultCode;
    }

    @Override
    public ResponseContent udFace(User user, Map<String, String> params) throws IOException {
        ResponseContent resultCode = new ResponseContent("-1", "失败");
        if (StringUtils.isNotBlank(params.get("userId"))) {
            UserUdcreditInfo udcreditInfo = userUdcreditInfoDao.findSelective(new HashMap() {{
                put("userId", user.getId());
            }});
            ResponseContent responseContent = udIdCardFace(user, params, udcreditInfo);//人脸比对前，先进行实名人像比对
            if (!responseContent.isSuccessed()) {//没通过的直接返回
                return responseContent;
            }
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
                String resp_front = "";
                if (udcreditInfo != null && StringUtils.isNoneBlank(udcreditInfo.getHeaderSession(), udcreditInfo.getLivingSession())) {

                    JSONObject reqJson = new JSONObject();
                    reqJson.put("header", UdRequestUtils.getRequestHeader(null));

                    JSONObject body = new JSONObject();
                    body.put("photo1", new HashMap<String, String>(){{
                        put("img_file", udcreditInfo.getHeaderSession());
                        put("img_file_source", "0");
                        put("img_file_type", "0");
                    }});
                    body.put("photo2", new HashMap<String, String>(){{
                        put("img_file", udcreditInfo.getLivingSession());
                        put("img_file_source", "0");
                        put("img_file_type", "1");
                    }});
                    reqJson.put("body", body);
                    logger.info("udFace reqJson = 【{}】", reqJson.toString());

                    resp_front = UdRequestUtils.doHttpRequest(PropertiesConfigUtil.get("UD_NEW_FACE_COMPARE") +
                            PropertiesConfigUtil.get("UD_PUB_KEY"), reqJson);
                }


                logger.info("interface udface return info :" + resp_front);
                if (StringUtils.isNotBlank(resp_front)) {

                    String jsonResult = JSONObject.parseObject(resp_front).getString("result");
                    String jsonData = JSONObject.parseObject(resp_front).getString("data");

                    Map<String, Object> result = JSONUtil.parseJSON2Map(jsonResult);
                    if (!result.containsKey("errorcode")) {
                        // 有源比对时，数据源人脸照片与待验证人脸照的比对结果
                        Map<String, Object> resultFaceid = JSONUtil.parseJSON2Map(jsonData);
                        // 	人脸比对接口的返回的误识几率参考值
                        //	“1e-3”：误识率为千分之一的置信度阈值；
                        //	“1e-4”：误识率为万分之一的置信度阈值；
                        //	“1e-5”：误识率为十万分之一的置信度阈值;
                        //	“1e-6”：误识率为百万分之一的置信度阈值。
                        Map<String, Object> thresholds = (Map<String, Object>) resultFaceid.get("thresholds");
                        // 比对率达到十万分之一的才被认为人脸认证通过
                        resultCode.setMsg("人脸识别实名认证失败！请检查身份证和头像");
                        if (Float.valueOf(resultFaceid.get("similarity") + "") >= Float.valueOf(thresholds.get("1e-4") + "")) {
                            resultCode.setCode("0");
                            resultCode.setMsg("成功");
                        }
                        HashMap<String, String> resultMap = new HashMap<>();
                        resultMap.put("similarity", resultFaceid.get("similarity").toString());
                        resultMap.put("le3", thresholds.get("1e-3").toString());
                        resultMap.put("le4", thresholds.get("1e-4").toString());
                        resultMap.put("le5", thresholds.get("1e-5").toString());
                        //resultMap.put("le6", thresholds.get("1e-6").toString());
                        resultMap.put("userId", params.get("userId"));
                        resultMap.put("session_id", resultFaceid.get("session_id").toString());
                        resultMap.put("partner_order_id", resultFaceid.get("partner_order_id").toString());
                        resultMap.put("confidence", resultFaceid.get("similarity").toString());
                        resultCode.setParamsMap(resultMap);
                        //保存人脸识别报告
                        saveUdFace(resultMap);
                        /*user.setRealCount(user.getRealCount() + 1);
                        user.setLastFullTime(new Date());
                        userService.updateRealCount(user);*/
                    } else {
                        logger.info("interface error error_message=" + result.get("message").toString());
                        resultCode.setCode("-1");
                        String msg;
                        msg = User.FACEID_MSG_TYPE.get(result.get("message").toString());
                        if (msg != null) {
                            resultCode.setMsg(msg);
                        } else {
                            resultCode.setMsg(User.FACEID_MSG_TYPE.get("OTHER"));
                        }
                        logger.info("interface error userPhone=" + user.getUserPhone() + " userId=" + user.getId() + " errorMsg return info :" + jsonResult);
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

    private String getResult(String arg) {
        Map<String, Object> map = JSONUtil.parseJSON2Map(arg);
        return map.get("result").toString();
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
            if (!"online".equals(PropertiesConfigUtil.get("profile"))) {
                return 1;//测试环境下直接返回为正式身份证
            }
            if ("1001".equals(checkResult.get("result")+"") && legalityJson.getDouble("ID_Photo") >= legalityJson.getDouble("Edited")
                    && legalityJson.getDouble("ID_Photo") >= legalityJson.getDouble("Photocopy")
                    && legalityJson.getDouble("ID_Photo") >= legalityJson.getDouble("Screen")
                    && legalityJson.getDouble("ID_Photo") >= legalityJson.getDouble("Temporary_ID_Photo")) {
                return 1;
            } else if (legalityJson.getDouble("Screen") > legalityJson.getDouble("Edited")
                    && legalityJson.getDouble("Screen") > legalityJson.getDouble("Photocopy")
                    && legalityJson.getDouble("Screen") > legalityJson.getDouble("ID_Photo")
                    && legalityJson.getDouble("Screen") > legalityJson.getDouble("Temporary_ID_Photo")) {
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

    /**
     * 保存人脸识别信息
     *
     * @param parasmMap mao
     */
    private void saveUdFace(HashMap<String, String> parasmMap) {
        if (parasmMap != null) {
            FaceRecognition face = faceFecogntionService.selectByUserId(Integer.parseInt(parasmMap.get("userId")));
            if (face == null) {
                face = new FaceRecognition();
                face.setConfidence(parasmMap.get("confidence"));
                face.setUserId(Integer.parseInt(parasmMap.get("userId")));
                face.setLe3(parasmMap.get("le3"));
                face.setLe4(parasmMap.get("le4"));
                face.setLe5(parasmMap.get("le5"));
                if (Float.valueOf(parasmMap.get("similarity") + "") >= Float.valueOf(parasmMap.get("le4") + "")) {
                    face.setStatus("1");
                } else {
                    face.setStatus("0");
                }
                face.setSessionId(parasmMap.get("session_id"));
                face.setPartnerOrderId(parasmMap.get("partner_order_id"));
                faceFecogntionService.saveFaceRecognitionDao(face);
            } else {
                face.setConfidence(parasmMap.get("confidence"));
                face.setUserId(Integer.parseInt(parasmMap.get("userId")));
                face.setLe3(parasmMap.get("le3"));
                face.setLe4(parasmMap.get("le4"));
                face.setLe5(parasmMap.get("le5"));
                face.setId(face.getId());
                if (Float.valueOf(parasmMap.get("similarity") + "") >= Float.valueOf(parasmMap.get("le4") + "")) {
                    face.setStatus("1");
                } else {
                    face.setStatus("0");
                }
                face.setSessionId(parasmMap.get("session_id"));
                face.setPartnerOrderId(parasmMap.get("partner_order_id"));
                faceFecogntionService.updateFaceRecognition(face);
            }
        }
    }
}
