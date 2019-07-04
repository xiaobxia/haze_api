<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = path + "/common/web";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html>
<head>

<title>拒就送现金</title>

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
<script type="text/javascript" src="${basePath}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${basePath}/js/common.js"></script>
<script type="text/javascript" src="${basePath}/js/base.js"></script>
<script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>

 
 
 
 



</head>
<style>
.j_jp img{ width:100%; height:auto;}
.j_jp li a{ display:block;}
</style>
<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page" >
        <!-- main start -->
        <div class="wrapper" style="padding-bottom:0;">
         <div class="j_jp">
         <ul>
         <li><img src="${basePath}/images/j1_icon.png"></li>
         <li><img src="${basePath}/images/j2_icon.png"></li>
<%--          <li><a href="javascript:;" rel="external"><img src="${basePath}/images/j3_icon.png"></a></li> --%>
         <li><img src="${basePath}/images/j4_icon.png"></li>
         </ul>
         </div>
        </div>
        <!-- main end -->
    </div>
</body>
</html>