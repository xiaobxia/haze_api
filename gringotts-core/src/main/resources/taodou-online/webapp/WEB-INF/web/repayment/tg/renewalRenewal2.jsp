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
    <title>申请续期</title>
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

    <script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/base.js"></script>
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
</head>
<body style="background:#fff;">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
   <%-- <div class="header">
        <a class="back_arr" data-ajax="false" href="${path}/repayment/detail.do?id=${bo.id}"></a>
        <h1>申请续期</h1>
    </div>--%>
    <!-- main start -->
    <div class="wrapper">
        <div class="apply">
            <h2>申请还款续期服务需要支付费用，请确认并支付！</h2>
            <ul class="zl_info tk_new sqxf">
                <li>
                    <a rel="external" href="javascript:;">
                        <span>待还本金<strong><fmt:formatNumber pattern='###,###,##0.00' value="${waitAmount / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>续期天数<strong>${bo.loanTerm}</strong><em>天</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>服务费<strong><fmt:formatNumber pattern='###,###,##0.00' value="${loanApr / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>续期费<strong><fmt:formatNumber pattern='###,###,##0.00' value="${renewalFee / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li class="nobd">
                    <a rel="external" href="javascript:;">
                        <span>逾期费<strong><fmt:formatNumber pattern='###,###,##0.00' value="${waitLate / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
            </ul>
            <div class="sum-box clearfix">
                <p>总服务费：<strong><fmt:formatNumber pattern='###,###,##0.00' value="${allCount / 100.00}"/></strong>元</p>
                <a href="${path}/credit-loan/description" data-ajax="false">关于续期&gt;</a>
            </div>
            <div class="tip">
                <span>完成支付续期总服务费后，即可成功续期</span>
            </div>
        </div>
        <a href="javascript:submit();" id="mima-btn" class="js-btn">马上支付</a>
    </div>
    <!-- main end -->

</div>
<div class="popup" id="loading" style="display: none;">
    <div class="overlay">
        <p class="tips-msg">正在提交，请稍后…</p>
    </div>
    <div class="spin" id="preview">
    </div>
</div>
<form action="" method="post"  id="payPath">
    <input type="text" name="VERSION" id="VERSION">
    <input type="text" name="MCHNTCD" id="MCHNTCD">
    <input type="text" name="FM" id="FM">
    <input type="text" name="ENCTP" id="ENCTP">
</form>
<script type="text/javascript">
   function submit(){
	    $('#loading').show();
        var url = '${path}/repayment/repay-renewal';
        $.post(url, {id:'${bo.id}',errorReturnUrl:'${errorReturnUrl}',successReturnUrl:'${successReturnUrl}',rtl:'${rtl}',sgd:'${sgd}', money:'${allCount}'} , function(data){
            $('#loading').hide();
            if(data.successed){
                $('#defray').hide();
                $("#payPath").attr("action", data.paramsMap.payPath);
                $("#VERSION").val(data.paramsMap.VERSION);
                $("#MCHNTCD").val(data.paramsMap.MCHNTCD);
                $("#FM").val(data.paramsMap.FM);
                $("#ENCTP").val(data.paramsMap.ENCTP);
                $("#payPath").submit();
            }
            else if(data.code == "-100"){
                $('#error_tip').html(data.msg);
            }
            else{
                showLoader(data.msg);
            }
        });
   }
</script>
</body>
</html>