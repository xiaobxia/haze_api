package com.vxianjin.gringotts.attach.controller;

import com.lowagie.text.pdf.BaseFont;
import com.vxianjin.gringotts.attach.util.SignTestByChing;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.util.FileUtil;
import com.vxianjin.gringotts.util.StringUtils;
import com.vxianjin.gringotts.util.date.DateUtil;
import com.vxianjin.gringotts.web.controller.AgreementController;
import com.vxianjin.gringotts.web.controller.BaseController;
import com.vxianjin.gringotts.web.pojo.BorrowContractInfo;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IBorrowContractInfoService;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 相关协议h5页面导出
 *
 * @author zzc
 */
@Controller
public class AgreementExportController extends BaseController {
    @Autowired
    private IBorrowOrderService borrowOrderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBorrowContractInfoService borrowContractInfoService;

    private SignTestByChing signTestByChing;

    public AgreementExportController() {
//		if (signTestByChing == null) {
//			signTestByChing = new SignTestByChing();
//			signTestByChing.initProject();
//		}
    }

    /**
     * @param filePath 目录路径
     * @return
     * @功能描述：判断文件目录是否存在如果不存在则创建目录
     * @创建时间
     * @author zzc
     */
    public static void mkDirs(String filePath) {
        // 判断路径是否存在
        File directory = new File(filePath.toString());
        // 如果不存在则创建目录
        if (!directory.exists() || directory.isDirectory()) {
            directory.mkdirs();
        }
    }

    /**
     * H5 借款协议下载签章  如果已签章直接下载
     * 借款的合同生成pdf下载
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("credit-loan/agreement_export")
    public void borrowAgreement(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String borrowId = request.getParameter("borrowId");
            String userId = request.getParameter("userId");
            String realPath = request.getSession().getServletContext().getRealPath("/" + Constant.FILEPATH_CORE); //存放路径
            String filePath = request.getSession().getServletContext().getRealPath("") + "/WEB-INF/web/agreement"; //模板存放路径
            String template = "agreementTemplate"; //协议存放的文件夹
            //如果协议文件已签章则直接下载
            File file = new File(realPath + "/" + template + "/" + borrowId + ".pdf");
            if (!file.exists()) {
                //获取模板内容替换
                String contentStr = FileUtil.readFileContent(new File(filePath + "/borrowSuccessAgreement.html"));
                BorrowOrder borrowOrderR = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
                User user = userService.searchByUserid(Integer.parseInt(userId));
//				String appName = getAppConfig(request.getParameter("appName"), "APP_TITLE");
                String companyTitle = getAppConfig(request.getParameter("appName"), "COMPANY_TITLE");
                String companyShortTitle = getAppConfig(request.getParameter("appName"), "COMPANY_TITLE_SHORT");


                contentStr = contentStr.replaceAll("###A1###", borrowOrderR.getId().toString());
                contentStr = contentStr.replaceAll("###A2###", user.getRealname());
                String indCardNumber = user.getIdNumber().substring(0, 5) + "****" + user.getIdNumber().substring(user.getIdNumber().length() - 4, user.getIdNumber().length());
                user.setIdNumber(indCardNumber);
                contentStr = contentStr.replaceAll("###A3###", user.getIdNumber());
                contentStr = contentStr.replaceAll("###A4###", companyTitle);

                String capitalName = "";
                String capitalCity = "";
                String realName = user.getRealname();
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-25");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (borrowOrderR.getLoanTime() != null && !"".equals(borrowOrderR.getLoanTime())
                        && borrowOrderR.getLoanTime().before(date)) { //2017-3-25(含)号以后才采取查询方式查出资人表
                    capitalName = "上海鱼耀金融信息服务有限公司"; //丁方公司名称
                    capitalCity = "上海市"; //丁方城市
                    model.addAttribute("realName", "丁方的用户"); //乙方
                } else {
                    //添加乙方和丁方
                    BorrowContractInfo params = new BorrowContractInfo();
                    params.setAssetOrderId(borrowOrderR.getId());
                    List<BorrowContractInfo> borrowContractlist = borrowContractInfoService.findBorrowContractInfo(params);
                    if (borrowContractlist != null && borrowContractlist.size() > 0) {
                        BorrowContractInfo borrowContractInfo = borrowContractlist.get(0);
                        if (StringUtils.isNotBlank(borrowContractInfo.getCapitalName())) {
                            String[] str = borrowContractInfo.getCapitalName().split(";;");
                            capitalName = StringUtils.isNotBlank(str[0]) ? str[0] : "**订单生成后可见**"; //丁方公司名称
                            capitalCity = StringUtils.isNotBlank(str[1]) ? str[1] : "**订单生成后可见**"; //丁方城市
                        } else {
                            capitalName = "**订单生成后可见**"; //丁方公司名称
                            capitalCity = "**订单生成后可见**"; //丁方城市
                        }
                        if (StringUtils.isNotBlank(borrowContractInfo.getRealName())) {
                            realName = borrowContractInfo.getRealName(); //乙方
                        } else {
                            realName = "**订单生成后可见**"; //乙方
                        }
                    } else {
                        capitalName = "**订单生成后可见**"; //丁方公司名称
                        capitalCity = "**订单生成后可见**"; //丁方城市
                        realName = "**订单生成后可见**"; //乙方
                    }
                }
                contentStr = contentStr.replaceAll("###A5###", realName);
                contentStr = contentStr.replaceAll("###A6###", companyShortTitle);
                contentStr = contentStr.replaceAll("###A7###", capitalName);
                contentStr = contentStr.replaceAll("###A8###", capitalCity);

                contentStr = contentStr.replaceAll("###A9###", (borrowOrderR.getMoneyAmount() / 100) + "");
                String borrowMonery = AgreementController.change(borrowOrderR.getMoneyAmount() / 100);
                contentStr = contentStr.replaceAll("###A10###", borrowMonery);
                String loanTime = "";
                if (borrowOrderR.getLoanTime() != null) {
                    loanTime = DateUtil.getDateFormat(borrowOrderR.getLoanTime(), "yyyy年MM月dd日");
                }
                contentStr = contentStr.replaceAll("###A11###", loanTime);
                String loanEndTime = "";
                if (borrowOrderR.getLoanEndTime() != null) {
                    loanEndTime = DateUtil.getDateFormat(borrowOrderR.getLoanEndTime(), "yyyy年MM月dd日");
                }
                contentStr = contentStr.replaceAll("###A12###", loanEndTime);

                String status = "";
                if (borrowOrderR.getStatus() >= 21) {
                    status = "通过";
                } else {
                    status = "失败";
                }
                contentStr = contentStr.replaceAll("###A13###", status);

                String loanTerm = "";
                if (borrowOrderR.getLoanTerm() == 7) {
                    loanTerm = "21.6%";
                } else if (borrowOrderR.getLoanTerm() == 14) {
                    loanTerm = "18%";
                }
                contentStr = contentStr.replaceAll("###A14###", loanTerm);
                String orderTime = "";
                if (borrowOrderR.getOrderTime() != null) {
                    orderTime = DateUtil.getDateFormat(borrowOrderR.getOrderTime(), "yyyy年MM月dd日");
                }
                contentStr = contentStr.replaceAll("###A15###", orderTime);
                getAttachmentsMap(realPath, borrowId, contentStr, template, filePath);  //创建pdf
                signTestByChing.selfLocalSignPdfByPDFStream(0, realPath + "/" + template + "/" + borrowId, realPath + "/" + template + "/", borrowId, "7", 200, 135, 80);  //对pdf进行签章
            }
            downLoadFile(realPath + "/" + template, borrowId, response);  //下载文件
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * H5 平台服务协议下载签章  如果已签章直接下载
     *
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("credit-loan/platformServiceNew_export")
    public void platformServiceNew(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String borrowId = request.getParameter("borrowId");
            String userId = request.getParameter("userId");
            String realPath = request.getSession().getServletContext().getRealPath("/" + Constant.FILEPATH_CORE);  //存放路径
            String filePath = request.getSession().getServletContext().getRealPath("") + "/WEB-INF/web/agreement"; //模板存放路径
            String template = "platformServiceTemplate"; //协议存放的文件夹
            //如果协议文件已签章则直接下载
            File file = new File(realPath + "/" + template + "/" + borrowId + ".pdf");
            if (!file.exists()) {
                //获取模板内容替换
                String contentStr = FileUtil.readFileContent(new File(filePath + "/borrowPlatformServiceNew.html"));
                BorrowOrder borrowOrderR = borrowOrderService.findOneBorrow(Integer.parseInt(borrowId));
                User user = userService.searchByUserid(Integer.parseInt(userId));
                String appName = getAppConfig(request.getParameter("appName"), "APP_TITLE");
                String companyTitle = getAppConfig(request.getParameter("appName"), "COMPANY_TITLE");
//				String companyShortTitle = getAppConfig(request.getParameter("appName"), "COMPANY_TITLE_SHORT");

                contentStr = contentStr.replaceAll("###A1###", borrowOrderR.getId().toString());
                contentStr = contentStr.replaceAll("###A2###", companyTitle);
                String indCardNumber = user.getIdNumber().substring(0, 5) + "****" + user.getIdNumber().substring(user.getIdNumber().length() - 4, user.getIdNumber().length());
                user.setIdNumber(indCardNumber);
                contentStr = contentStr.replaceAll("###A3###", user.getRealname());
                contentStr = contentStr.replaceAll("###A4###", user.getIdNumber());
                String loanEndTime = "";
                if (borrowOrderR.getLoanEndTime() != null) {
                    loanEndTime = DateUtil.getDateFormat(borrowOrderR.getLoanEndTime(), "yyyy年MM月dd日");
                }
                contentStr = contentStr.replaceAll("###A5###", loanEndTime);
                contentStr = contentStr.replaceAll("###A6###", (borrowOrderR.getMoneyAmount() / 100) + "");
                contentStr = contentStr.replaceAll("###A7###", (borrowOrderR.getLoanInterests() / 100) + "");
                String status = "";
                if (borrowOrderR.getStatus() >= 21) {
                    status = "通过";
                } else {
                    status = "失败";
                }
                contentStr = contentStr.replaceAll("###A8###", status);
                String loanTime = "";
                if (borrowOrderR.getLoanTime() != null) {
                    loanTime = DateUtil.getDateFormat(borrowOrderR.getLoanTime(), "yyyy年MM月dd日");
                }
                contentStr = contentStr.replaceAll("###A9###", loanTime);
                contentStr = contentStr.replaceAll("###A10###", appName);
                getAttachmentsMap(realPath, borrowId, contentStr, template, filePath);  //创建pdf
                signTestByChing.selfLocalSignPdfByPDFStream(0, realPath + "/" + template + "/" + borrowId, realPath + "/" + template + "/", borrowId, "5", 146, 365, 80);  //对pdf进行签章
            }
            downLoadFile(realPath + "/" + template, borrowId, response);  //下载文件
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 替换后的内容重新生成html 再转换为pdf
     *
     * @param filePath
     * @param fileName
     * @param contentStr
     * @param template
     * @throws Exception
     */
    public void getAttachmentsMap(String realPath, String fileName, String contentStr, String template, String filePath) {
        try {
            //替换内容后从新生成html文件
            mkDirs(realPath + "/" + template);
            File f = new File(realPath + "/" + template + "/" + fileName + ".html");
            BufferedWriter o = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
            o.write(contentStr);
            o.flush();
            o.close();
            //创建pdf文件
            String url = f.toURI().toURL().toString();
            String outputFile = realPath + "/" + template + "/" + fileName + ".pdf";
            OutputStream os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(url);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            //解决中文问题
            fontResolver.addFont(filePath + "/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.layout();
            renderer.createPDF(os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  TODO 放到工具类或者一个通用的fileservice 处理类中更好一点
     * 下载文件
     */
    public void downLoadFile(String path, String fileName, HttpServletResponse response) throws Exception {
        if (path != null && path != null) {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".pdf");
                bis = new BufferedInputStream(new FileInputStream(path + "/" + fileName + ".pdf"));
                bos = new BufferedOutputStream(response.getOutputStream());
                int word = 0;
                // 写内容到baos中
                while ((word = bis.read()) != -1) {
                    bos.write(word);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            }
        }
    }
}
