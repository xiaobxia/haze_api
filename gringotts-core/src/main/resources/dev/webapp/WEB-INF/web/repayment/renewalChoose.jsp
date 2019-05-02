<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>支付方式</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/theme-orange.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/validate.css" />

    <script type="text/javascript" src="${basePath}/js/base.js"></script>
    <script type="text/javascript" src="${basePath}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script src="${basePath }/js/jquery-mvalidate.js"></script>
    <style type="text/css">
        .nongye-point {
            color: #FC5438;
            font-size: 12px;
            text-align: left;
        }
    </style>
</head>
<body style="background:#fff;">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page" style="background:#fff;" id="page1">
    <%-- <div class="header">
         <a class="back_arr" href="${path}/repayment/detail.do?id=${bo.id}" data-ajax="false"></a>
         <h1>请选择还款方式</h1>
     </div>--%>
    <!-- main start -->
    <div class="wrapper">
        <div class="apply">
            <h2>请选择支付方式</h2>
            <ul class="zl_info tk_new">
                <li>
                    <a rel="external" href="${path}/repayment/renewal-pay-yeepay?id=${bo.id}">
                        <span><img style="vertical-align: middle" src="${basePath}/images/b_01.png" alt="">一键支付</span>
                        <p><em>${info.bankName}（${info.card_no}）</em><img src="${basePath}/images/el_06.png" alt=""></p>
                    </a>
                </li>
                <!--支付宝维护，临时隐藏支付宝还款页面-->
                <!--<li class="nobd"  id="alipay-choose">
                    <a rel="external" href="${path}/repayment/renewal-pay-alipay?id=${bo.id}&userId=${bo.userId}">
                        <span><img style="vertical-align: middle" src="${basePath}/images/b_02.png" alt="">支付宝支付</span>
                        <p><img src="${basePath}/images/el_06.png" alt=""></p>
                    </a>
                </li>-->
            </ul>
            <div class="tip">
                <p>备注：若在借款期间内未主动发起还款，则默认于还款日当天从绑定银行卡${info.bankName}（${info.card_no}）自动扣除所借款项，请保证在扣款之前账户资金充足。</p>
            </div>
            <!-- 无法支付信息提示 -->
            <div class="tip">
                <p class="nongye-point">提示：如果无法支付，请重新绑卡！</p>
            </div>
        </div>
    </div>
    <!-- main end -->
</div>
<script type="text/javascript">
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    if (isIOS) {
        $("#alipay-choose").css("display","none");
    }
</script>
</body>
</html>