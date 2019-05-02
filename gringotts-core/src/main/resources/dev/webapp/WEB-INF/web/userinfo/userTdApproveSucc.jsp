<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>手机运营商认证</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${basePath }/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/css/basic.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }/css/common.css" />

    <script type="text/javascript" src="${basePath }/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/base.js"></script>
    <style>
        .js-btn {
            width: 9.5rem;
            height: 2.25rem;
            line-height: 2.25rem;
            text-align: center;
            background: transparent !important;
            color: #31c27c !important;
            border-radius: 2.25rem;
            border: 1px solid #31c27c;
        }
        .rz-result p,
        .pannel-btn a {
            font-size: 0.85rem;
            font-weight: normal;
        }
        .rz-result p.detail {
            color: #777;
            font-size: 0.7rem;
            width: 14.5rem;
            margin: 0 auto;
            font-weight: normal;
            margin-top: 1.25rem;
        }
    </style>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
    <div class="wrapper">
        <div class="rz-result" style="margin:0 0 -2rem;">
            <c:if test="${code == 0}">
                <p>运营商认证</p>
            </c:if>
            <c:if test="${code == 200}">
                <p>运营商认证已完成</p>
            </c:if>
            <c:if test="${code == 100 || code == 300 || code == 500}">
                <p>运营商认证失败,重新发起认证</p>
            </c:if>
            <c:if test="${code == 400}">
                <p>运营商认证失败,请重新登录</p>
            </c:if>
            <!--认证中-->
            <c:if test="${code == 700}">
                <div class="certificate">
                    <img src="${basePath }/images/mobile_checking.png" style="width: 120px;margin: 0 auto 15px;">
                    <p style="text-align: center;font-size: 0.875rem;">&nbsp;&nbsp;请您耐心等待！</p>
                    <p style="margin-top: 2.6rem;font-weight: normal;color: #999;">运营商认证可能需要几分钟的时间<br>我们将会在认证完成后的第一时间通知您！</p>
                </div>
            </c:if>
            <!--认证失败状态-->
            <c:if test="${code == -1}">
                <div class="certified-fail">
                    <img src="${basePath }/images/ic_fail.png" style="width: 4.5rem;margin: 0 auto 1.2rem;">
                    <p>啊哦～认证失败了…</p>
                </div>
            </c:if>
            <!--认证失败后重新认证状态-->
            <c:if test="${code == -2}">
                <div class="fail-certify">
                    <img src="${basePath }/images/ic_fail.png" style="width: 4.5rem;margin: 0 auto 1.2rem;">
                    <p>啊哦～认证失败了…</p>
                    <p class="detail">由于运营商数据维护，造成认证未能成功，建议您在1～2天后重新尝试发起认证！</p>
                </div>
            </c:if>
        </div>
        <div class="pannel-btn">

            <c:if test="${code == 0 || code == 100 || code == 300 || code == 500}">
                <a href="${mobileUrl}" class="js-btn mt60 yrz" id="bc-btn">立即认证</a>
            </c:if>

            <c:if test="${code == 200}">
                <a href="javascript:;" class="js-btn mt60 yrz" id="bc-btn">已认证</a>
            </c:if>
            <c:if test="${code == 400}">
                <a href="javascript:;" class="js-btn mt60 yrz" id="bc-btn">请重新登录</a>
            </c:if>
            <!--认证失败-->
            <c:if test="${code == -1}">
                <a href="${mobileUrl}" class="js-btn mt60 yrz" id="bc-btn">重新认证</a>
            </c:if>
        </div>
        <span class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
    </div>
    <!-- main end -->
</div>

<script type="text/javascript">
    var recode = '${recode}';
    if(recode==2){
    }else{

        var  tipMsg="运营商认证失败,重新发起认证";
        var tipType="NO";
        if('${code}' == 200){
            tipMsg="运营商认证已完成";
            tipType="YES";
        }else if('${code}' == 700){
            tipMsg="运营商认证中,请耐心等待";
            tipType="YES";
        }else if('${code}' == 100){
            tipMsg='运营商认证失败,重新发起认证';
        }else if('${code}}' == 300){
            tipMsg='运营商认证失败,重新发起认证流程';
        }else if('${code}' == 500){
            tipMsg="运营商认证失败,重新发起认证";
        }else if('${code}' == 400){
            tipMsg="运营商认证失败,请重新登录";
        }
        if('${clientType}' =='ios'){
            //ios
            window.location.href="www.mobileApprove.com&result="+tipType;
        }else{
            //安卓
            nativeMethod.authenticationResult(tipMsg);
        }
    }
</script>
</body>
</html>