<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
<meta charset="utf-8">
<title>关于我们</title>
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
 
<script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
</head>
<body style="background:#fff;">
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page" style="background:#fff;"><!--
        <div class="header">
            <a class="back_arr" href="javascript:history.back();"></a>
            <h1>关于我们</h1>
        </div>
        --><!-- main start -->
       <div class="wrapper">
	    <div class="gy_ban"><img src="${basePath}/images/gy_banner.png"></div>
	    <div class="gy1"><img src="${basePath}/images/gy_b3.png"></div>
	    <div class="gy2"><img src="${basePath}/images/gy_b2.png"></div>
	        <!-- main end -->
	        
	    </div>
    </div>
</body>
</html>
