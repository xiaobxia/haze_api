package com.vxianjin.gringotts.attach.util;

import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.result.Result;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import com.timevale.esign.sdk.tech.service.EsignsdkService;
import com.timevale.esign.sdk.tech.service.SelfSignService;
import com.timevale.esign.sdk.tech.service.factory.EsignsdkServiceFactory;
import com.timevale.esign.sdk.tech.service.factory.SelfSignServiceFactory;
import com.timevale.tech.sdk.bean.HttpConnectionConfig;
import com.timevale.tech.sdk.bean.ProjectConfig;
import com.timevale.tech.sdk.bean.SignatureConfig;
import com.timevale.tech.sdk.constants.AlgorithmType;
import com.timevale.tech.sdk.constants.HttpType;
import com.vxianjin.gringotts.web.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * E签宝 对合同签章
 *
 * @author zzc
 */
public class SignTestByChing {
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获得指定文件的byte数组
     *
     * @param filename String 文件路径
     * @return byte[] 文件内容
     * @throws IOException
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组生产文件
     *
     * @param filename String 文件路径
     * @return byte[] 文件内容
     * @throws IOException
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /***
     * 项目初始化
     * 使用到的接口：sdk.init(proCfg, httpConCfg, sCfg);
     */
    public void initProject() {

        ProjectConfig proCfg = new ProjectConfig();
        Map<String, String> map = SysCacheUtils.getConfigMap("EQB");
        /* 项目ID(应用ID) */
        proCfg.setProjectId(map.get("eqb_project_id"));   //1111563517
        /* 项目Secret(应用Secret) */
        proCfg.setProjectSecret(map.get("eqb_project_secret"));   // 95439b0863c241c63a861b87d1e647b7

        /* 开放平台地址 */
        proCfg.setItsmApiUrl(map.get("eqb_api_url")); //http://121.40.164.61:8080/tgmonitor/rest/app!getAPIInfo2

        HttpConnectionConfig httpConCfg = new HttpConnectionConfig();

        /* 协议类型，默认https */
        httpConCfg.setHttpType(HttpType.HTTPS);
        /* 代理服务IP地址 */
        httpConCfg.setProxyIp(null);
        /* 代理服务端口 */
        httpConCfg.setProxyPort(new Integer(0));
        /* 请求失败重试次数，默认5次 */
        httpConCfg.setRetry(new Integer(5));

        SignatureConfig sCfg = new SignatureConfig();

        /* 算法类型，默认HMAC-SHA256 */
        sCfg.setAlgorithm(AlgorithmType.HMACSHA256); //可选RSA
        /* e签宝公钥，可以从开放平台获取。若算法类型为RSA，此项必填 */
        sCfg.setEsignPublicKey("");
        /* 平台私钥，可以从开放平台下载密钥生成工具生成。若算法类型为RSA，此项必填 */
        sCfg.setPrivateKey("");

        logger.info("----开始项目初始化...");

        EsignsdkService sdk = EsignsdkServiceFactory.instance();
        Result result = sdk.init(proCfg, httpConCfg, sCfg);

        if (0 != result.getErrCode()) {
            logger.info("项目初始化失败，错误码=" + result.getErrCode() + ",错误信息=" + result.getMsg());
        } else {
            logger.info("----项目初始化成功！");
        }
    }

    /***
     * 平台自身PDF摘要签署（文件流）； 盖章位置通过坐标定位；
     * 使用到接口：SelfSignServiceFactory.instance();
     * selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId, SignType.Single);
     */
    public FileDigestSignResult selfLocalSignPdfByPDFStream(int sealId, String setSrcPdfFile, String setDstPdfFile, String firstSign, String posPage, int posX, int posY, int width) {

        SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();
        /* 签署PDF文档信息: 待签署文档本地二进制数据*/
        signPDFStreamBean.setStream(getBytes(setSrcPdfFile + ".pdf"));
        /* 签署PDF文档信息: 文档名称，e签宝签署日志对应的文档名(可理解成签署后的PDF文件名)，若为空则取文档路径中的名称*/
        signPDFStreamBean.setFileName(firstSign + ".pdf");
        /* 签署PDF文档信息: 文档编辑密码，当目标PDF设置权限密码保护时必填*/
        signPDFStreamBean.setOwnerPassword(null);


        PosBean posBean = new PosBean();
        /* 签章位置信息: 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。*/
        posBean.setPosType(0);
        /* 签章位置信息: 签署页码，若为多页签章，支持页码格式“1-3,5,8“，若为坐标定位时，不可空*/
        posBean.setPosPage(posPage);
        /* 签章位置信息: 签署位置X坐标，若为关键字定位，相对于关键字的X坐标偏移量，默认0*/
        posBean.setPosX(posX);
        /* 签章位置信息: 签署位置Y坐标，若为关键字定位，相对于关键字的Y坐标偏移量，默认0*/
        posBean.setPosY(posY);
        /* 签章位置信息: 关键字，仅限关键字签章时有效，若为关键字定位时，不可空*/
        posBean.setKey(null);
        /* 签章位置信息: 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述*/
        posBean.setWidth(width);

        logger.info("----开始平台自身PDF摘要签署...");
        SelfSignService selfSignService = SelfSignServiceFactory.instance();
        /* SignType(签章类型):Single-单页签章、Multi-多页签章、Edges-签骑缝章、Key-关键字签章 */
        FileDigestSignResult fileDigestSignResult = selfSignService.localSignPdf(signPDFStreamBean, posBean, sealId, SignType.Single);
        //SelfSignService selfSignService = SelfSignServiceFactory.instance();
        //FileDigestSignResult fileDigestSignResult = selfSignService.localSignPdf(signPDFFileBean, posBean, 0, SignType.Single);
        if (0 != fileDigestSignResult.getErrCode()) {
            logger.info("平台自身PDF摘要签署（文件流）失败，错误码=" + fileDigestSignResult.getErrCode() + ",错误信息=" + fileDigestSignResult.getMsg());
        } else {
            getFile(fileDigestSignResult.getStream(), setDstPdfFile, signPDFStreamBean.getFileName());
            logger.info("----平台自身PDF摘要签署成功！");
        }
        return fileDigestSignResult;
    }
}
