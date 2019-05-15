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
    <title>运营商认证</title>
<meta http-equiv="Expires" content="-1" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/jquery.mobile-1.4.2.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />
<script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
<script type="text/javascript" src="${basePath}/js/base.js"></script>
<script src="${basePath }/js/global-1.1.0.min.js"></script>
    <style type="text/css">
        .op-wrapper {
            text-align: center;
            padding-top: 100px;
        }
        .op-title {
            text-align: center;
            margin: 30px 0 50px 0;
            font-size: 20px;
            color: #aaa;
        }
        .back-btn {
            color: #fff;
            text-align: center;
            background-color: #FF8240;
            border-radius:24px;
            line-height: 48px;
            font-size: 20px;
        }
    </style>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
    <!-- main start -->
    <div class="op-wrapper">
        <img src=".${basePath}/images/b_06.png" alt="">
        <div class="op-title">运营商认证成功</div>
        <div class="back-btn">返回</div>
    </div>
</div>
<script type="text/javascript">
    $('.back-btn').click(function () {
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        $("#tempForm").attr("action","/www.close.com");
        if (isAndroid) {
            nativeMethod.close();
        }else if(isIOS){
            $("#tempForm").submit();
        }
    })

</script>
</body>
</html>