package com.vxianjin.gringotts.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.vxianjin.gringotts.api.CloseableOkHttp;
import com.vxianjin.gringotts.api.service.IYunFaTongApiService;
import com.vxianjin.gringotts.common.JsonResult;
import com.vxianjin.gringotts.constant.Commons;
import com.vxianjin.gringotts.constant.FileTypeEnum;
import com.vxianjin.gringotts.util.FileUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.component.IContractComponent;
import com.vxianjin.gringotts.web.component.ICustomerComponent;
import com.vxianjin.gringotts.web.component.IFileUploadComponent;
import com.vxianjin.gringotts.web.dao.ICfcaContractInfoDao;
import com.vxianjin.gringotts.web.dao.ICfcaHttpInfoDao;
import com.vxianjin.gringotts.web.dao.ICfcaUserInfoDao;
import com.vxianjin.gringotts.web.dao.IUserDao;
import com.vxianjin.gringotts.web.pojo.CfcaContractInfo;
import com.vxianjin.gringotts.web.pojo.CfcaUserInfo;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.ICfcaSignAndViewService;
import com.vxianjin.gringotts.web.util.ConfigLoader;
import com.vxianjin.gringotts.web.util.cfcautil.CfcaCommonUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付令生成服务
 *
 * @author tgy
 * @version [版本号, 2018年2月2日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service(value = "cfcaSignAndViewService")
public class CfcaSignAndViewServiceImpl implements ICfcaSignAndViewService {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CfcaSignAndViewServiceImpl.class);
    @Autowired
    IFileUploadComponent fileUploadCnt;
    @Autowired
    IContractComponent contractCnt;
    @Autowired
    ICustomerComponent customerCnt;
    @Autowired
    ICfcaUserInfoDao cfcaUsrDao;
    @Autowired
    ICfcaContractInfoDao cfcaContractDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private ICfcaHttpInfoDao cfcaHttpDao;

	private static PropertiesConfiguration configuration = null;
	private static String appCode = "";

	static {
		appCode = PropertiesConfigUtil.get("RISK_BUSINESS");
        configuration = ConfigLoader.getInstance().getAliConfigurations();

    }/**
	 * 生成支付令访问url给前端
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public void generatePayToken(String userId, CfcaContractInfo contractInfo) throws Exception {

        String requestUrl = "";
        String viewUrl = "";
        String fileMark = "";
        Map<String, String> urlMap = new HashMap<String, String>();
        //身份证临时文件路径
        Map<String, String> pathMap = new HashMap<String, String>();

        //用户校验
        if (StringUtils.isEmpty(userId)) {
            throw new Exception("userId should not null!");
        }
        User user = userDao.searchByUserid(Integer.valueOf(userId));
        if (user == null) {
            throw new Exception("user " + userId + " not exist!");
        }
        //首先判断此用户是否已经在云法通存在
        CfcaUserInfo cfcaUser = cfcaUsrDao.selectCfcaByUserId(userId);
        try {
            if (cfcaUser == null || StringUtils.isEmpty(cfcaUser.getCfcaUserId())) {

                cfcaUser = new CfcaUserInfo();
                //若存在直接生成合同，否则进行主体信息的进件
                cfcaUser.setUser(user);
                //以下为新用户生成支付令主流程，返回1代表成功,否则中断
                //上传身份证正反面
                if (1 == handleIdCardImg(cfcaUser, pathMap)) {
                    fileMark = "1";
                    //处理用户主体信息
                    if (1 == handleCfcaUserInfo(cfcaUser)) {
                        //保存云法通用户
                        cfcaUsrDao.insertSelective(cfcaUser);
                        //处理合同信息生成合同ID
                        if (1 == handleCfcaContractInfo(contractInfo, cfcaUser)) {
                            cfcaContractDao.insertSelective(contractInfo);
//							requestUrl = contractInfo.getUrl();
//							viewUrl =  CfcaCommonUtil.SIGN_AND_VIEW_REQ_URL.replace("(1)", contractInfo.getCfcaContractId()).replace("(2)", cfcaUser.getCfcaUserId());
//							urlMap.put("viewUrl",viewUrl);
//							urlMap.put("signUrl",requestUrl);
                        }
                    }
                }

            } else {
                //若存在直接生成合同，否则进行主体信息的进件
                cfcaUser.setUser(user);
                //处理合同信息生成合同ID
                if (1 == handleCfcaContractInfo(contractInfo, cfcaUser)) {
                    cfcaContractDao.insertSelective(contractInfo);
//					requestUrl = contractInfo.getUrl();
//					viewUrl =  CfcaCommonUtil.SIGN_AND_VIEW_REQ_URL.replace("(1)", contractInfo.getCfcaContractId()).replace("(2)", cfcaUser.getCfcaUserId());
//					urlMap.put("viewUrl",viewUrl);
//					urlMap.put("signUrl",requestUrl);
                }
            }
        } catch (Exception e) {
            LOGGER.error("generate pay token failed!");
            //return urlMap;
        } finally {
            //产生过临时文件需删除身份证临时文件
            if ("1".equals(fileMark)) {
                for (String key : pathMap.keySet()) {
                    FileUtil.delFileOnExist(pathMap.get(key));
                }
            }
        }

        //提供签署合同的页面url
        //if (StringUtils.isNotEmpty(requestUrl)) {
        //	return requestUrl;
        //}
        //return urlMap;
    }

    /**
     * 生成云法通用户 1成功 0失败
     *
     * @param userId
     */
    @Override
    public int feachCfcaUserModule(String userId) throws Exception {

        User user = userDao.searchByUserid(Integer.valueOf(userId));
        //身份证临时文件路径
        Map<String, String> pathMap = new HashMap<String, String>();
        String fileMark = "";
        if (user == null) {
            throw new Exception("user " + userId + " not exist!");
        }
        CfcaUserInfo cfcaUser = cfcaUsrDao.selectCfcaByUserId(userId);
        try {
            //此前未生成过
            if (cfcaUser == null || StringUtils.isEmpty(cfcaUser.getCfcaUserId())) {

                cfcaUser = new CfcaUserInfo();
                //若存在直接生成合同，否则进行主体信息的进件
                cfcaUser.setUser(user);
                //上传身份证正反面
                if (1 == handleIdCardImg(cfcaUser, pathMap)) {
                    fileMark = "1";
                    //处理用户主体信息
                    if (1 == handleCfcaUserInfo(cfcaUser)) {
                        //保存云法通用户
                        cfcaUsrDao.insertSelective(cfcaUser);
                        return 1;
                    }
                }
            } else {
                return 1;
            }
        } catch (Exception e) {
            LOGGER.error("generate cfca user failed!");
        } finally {
            //产生过临时文件需删除身份证临时文件
            if ("1".equals(fileMark)) {
                for (String key : pathMap.keySet()) {
                    FileUtil.delFileOnExist(pathMap.get(key));
                }
            }
        }
        return 0;
    }

    /**
     * 云法通合同状态查询，并更新合同状态
     * <p>
     * -1为合同不存在
     * 0为未签署
     * 1为正在签署
     * 2为签署成功
     * 4为签署失败
     *
     * @param contractId
     * @return
     * @throws Exception
     */
    @Override
    public int cfcaQueryStatus(String contractId) throws Exception {

        CfcaContractInfo contractInfo = cfcaContractDao.selectByContractId(contractId);

        if (4 == contractInfo.getSignStatus()) {
            return 2;
        }

        IYunFaTongApiService service = CloseableOkHttp.obtainRemoteService(CfcaCommonUtil.API, IYunFaTongApiService.class);
        JSONObject json = service.contractStatus(contractId).execute().body();

        String code = null;
        if (json != null) {
            code = json.getString("code");
        }
        if (StringUtils.isEmpty(code)) {
            return 0;
        }
        Integer status = Integer.valueOf(code);
        contractInfo.setSignStatus(status);
        cfcaContractDao.updateByPrimaryKeySelective(contractInfo);
        return status;
    }

    /**
     * 处理两张身份证正反面 1成功 0异常
     *
     * @autho tgy
     */
    public int handleIdCardImg(CfcaUserInfo cfcaUser, Map<String, String> pathMap) throws Exception {
        Map<String, String> typeMap = new HashMap<String, String>();
        doTmpFile(cfcaUser, pathMap, typeMap);
        final String pathZM = pathMap.get("pathZM");
        final String pathFM = pathMap.get("pathFM");
        int zmCunt = 0;
        int fmCunt = 0;

        if (StringUtils.isNotEmpty(pathZM)) {
            JsonResult jreZ = fileUploadCnt.fileUpload(cfcaUser, pathZM, typeMap.get("imgIdCardZM"), "1");
            //如果不成功,则中断
            if (CfcaCommonUtil.CODE_SUCCESS.equals(jreZ.getCode())) {
                zmCunt = 1;
            }
        }
        if (StringUtils.isNotEmpty(pathFM)) {
            JsonResult jreF = fileUploadCnt.fileUpload(cfcaUser, pathFM, typeMap.get("imgIdCardFM"), "2");
            //如果不成功,则中断
            if (CfcaCommonUtil.CODE_SUCCESS.equals(jreF.getCode())) {
                fmCunt = 1;
            }
        }
        return zmCunt * fmCunt;
    }

    /**
     * 处理用户信息获取云法通用户ID 1成功 0异常
     *
     * @param cfcaUser
     * @return
     */
    public int handleCfcaUserInfo(CfcaUserInfo cfcaUser) throws IOException {

        JsonResult jrs = customerCnt.registUserCustomer(cfcaUser);

        if (CfcaCommonUtil.CODE_SUCCESS.equals(jrs.getCode()) || CfcaCommonUtil.CODE_EXIST.equals(jrs.getCode())) {
            return 1;
        }
        return 0;
    }

    /**
     * 处理合同信息 获取云法通合同ID 1成功 0异常
     *
     * @param cfcaUser
     * @param contractInfo
     * @return
     */
    private int handleCfcaContractInfo(CfcaContractInfo contractInfo, CfcaUserInfo cfcaUser) throws IOException {
        JsonResult jrs = contractCnt.createContract(contractInfo, cfcaUser);
        if (CfcaCommonUtil.CODE_SUCCESS.equals(jrs.getCode())) {
            return 1;
        }
        return 0;
    }

    /**
     * 处理临时文件
     *
     * @param cfcaUser
     * @return
     * @throws Exception
     */
    private Map<String, String> doTmpFile(CfcaUserInfo cfcaUser, Map<String, String> pathMap, Map<String, String> typeMap) throws Exception {
        LOGGER.info("deal cfca idcard img start!");
        User user = cfcaUser.getUser();

		String userId = user.getId();
		String domainOfBucket = configuration.getString("domainOfBucket");
		//根据用户ID获取用户信息,正反面url链接
		String imgIdCardZM = "http://" + domainOfBucket + user.getIdcardImgZ();
		String imgIdCardFM = "http://" + domainOfBucket + user.getIdcardImgF();
		LOGGER.info("imgIdCardZM:" + imgIdCardZM);
		LOGGER.info("imgIdCardFM:" + imgIdCardFM);
		Map<String, String> imgMap = new HashMap<String, String>();
		imgMap.put("imgIdCardZM", imgIdCardZM);
		imgMap.put("imgIdCardFM", imgIdCardFM);

        //文件类型校验
        for (String key : imgMap.keySet()) {
            String fileName = imgMap.get(key);
            //身份證正反面不能為空
            if (StringUtils.isEmpty(fileName)) {
                throw new Exception("idCard should not empty!");
            }
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            typeMap.put(key, fileType);
            //文件類型是否支持
            if (FileTypeEnum.getEnumFromCode(fileType) == null) {
                throw new Exception("idCard fileType not support!");
            }
        }

        //从url上获取两个文件流
        //身份證正面輸入流
        InputStream insZm = getBalanceIOStream(imgMap.get("imgIdCardZM"));
        //身份證正面輸入流
        InputStream insFm = getBalanceIOStream(imgMap.get("imgIdCardFM"));

        String imgNameZm = userId.concat("img_zm.").concat(typeMap.get("imgIdCardZM"));
        String imgNameFm = userId.concat("img_fm.").concat(typeMap.get("imgIdCardFM"));

        //所要创建的临时文件的目录路径
        String pathZM = CfcaCommonUtil.TMP_FILE_PATH.concat(File.separator).concat(imgNameZm);
        String pathFM = CfcaCommonUtil.TMP_FILE_PATH.concat(File.separator).concat(imgNameFm);

        //在临时目录创建临时文件
        try {
            createFile(pathZM, insZm);
            createFile(pathFM, insFm);
        } catch (IOException e) {
            LOGGER.error("create tmp file error");
            return pathMap;
        }

        cfcaUser.setUserId(userId);
        pathMap.put("pathZM", pathZM);
        pathMap.put("pathFM", pathFM);
        LOGGER.info("deal cfca idcard img start!" + pathMap);
        return pathMap;

    }

    /**
     * 在临时目录创建文件
     *
     * @param path
     * @param ins
     * @throws Exception
     */
    private void createFile(String path, InputStream ins) throws IOException {
        //临时文件创建完成之后开始写流
        FileUtil.createFile(path);
        File fileTmp = new File(path);
        OutputStream out = new FileOutputStream(fileTmp);

        byte[] temp = new byte[1024];
        int length = 0;
        // 源文件读取一部分内容
        while ((length = ins.read(temp)) != -1) {
            // 目标文件写入一部分内容
            out.write(temp, 0, length);
        }
        out.flush();
        ins.close();
        out.close();
    }

    /**
     * 根据URL获取文件输入流
     *
     * @param urlStr
     * @return
     * @throws Exception
     */
    private InputStream getBalanceIOStream(String urlStr)
            throws Exception {
        URL url = null;
        HttpURLConnection httpUrlConnection = null;
        InputStream fis = null;
        try {
            url = new URL(urlStr);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(Commons.DEFAULTTIMEOUT);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("CHARSET", "UTF-8");
            httpUrlConnection.connect();
            fis = httpUrlConnection.getInputStream();

            ByteArrayOutputStream bosStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = fis.read(buff, 0, 1024)) > 0) {
                bosStream.write(buff, 0, rc);
            }
            byte[] bytes = bosStream.toByteArray();

            return new ByteArrayInputStream(bytes);
        } finally {
            IOUtils.close(fis);
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
    }
}
