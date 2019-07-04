<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
    String imgPath = path + "/common/web";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<c:set var="imgPath" value="<%=imgPath%>"></c:set>
<html lang="en">
<head>
<meta charset="utf-8">
<title>紧急通知</title>
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
<link rel="stylesheet" type="text/css" href="${basePath}/css/validate.css">


<script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
<script type="text/javascript" src="${basePath}/js/base.js"></script>
<script type="text/javascript" src="${basePath}/js/global-1.1.0.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jquery-mvalidate.js"></script>

    <style>
        body.ui-mobile-viewport {
            background: #ec5e66 url("${imgPath}/images/content/notice/bg@2x.png") top center no-repeat;
            background-size: 100%;
        }
        .ui-page.ui-page-theme-a {
            background: none;
        }
        .m-banner img {
            width: 6.8rem;
            margin: 3.2rem auto 2.8rem;
        }
        .m-pannel {
            margin: 0 0.15rem 0.15rem 0.6rem;
            background: url("${imgPath}/images/content/notice/bg_02@2x.png") top center no-repeat;
            background-size: 100%;
            padding: 1.275rem 1.125rem 2rem;
        }
        .m-pannel img {
            margin: 0 auto;
        }
        .m-pannel .word_01,
        .m-pannel .word_02{
            margin-left: .3rem;
        }
        .m-pannel .word_pic_01,
        .m-pannel .word_pic_02
        {
            position: relative;
            left: -0.3rem;
        }
        .m-pannel .word_01 {
            width: 14.625rem;
        }
        .m-pannel .word_pic_01 {
            width: 6.425rem;
        }
        .m-pannel .word_02 {
            width: 14.5rem;
        }
        .m-pannel .word_pic_02 {
            width: 8.6rem;
            margin-top: 1rem;
        }
    </style>

</head>
<body class="ui-mobile-viewport ui-overlay-a">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page" style="min-height: 736px;">
    <!-- main start -->
    <div class="wrapper">
        <div class="sq-wrap">
            <div class="m-banner"><img src="${imgPath}/images/content/notice/word@2x.png" alt=""></div>
            <div class="m-pannel">
                <img class="word_01" src="${imgPath}/images/content/notice/word_01@2x.png" alt="">
                <img class="word_pic_01" src="${imgPath}/images/content/notice/pic_011@2x.png" alt="">
                <img class="word_02" src="${imgPath}/images/content/notice/word_02@2x.png" alt="">
                <img class="word_02" style="margin-top:10px;" src="${imgPath}/images/content/notice/pic_02@2x.png" alt="">
            </div>
        </div>
    </div>
    <!-- main end -->
</div>
</body>
</html>
