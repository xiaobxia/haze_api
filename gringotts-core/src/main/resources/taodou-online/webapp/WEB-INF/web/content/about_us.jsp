<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>关注公众号</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <!-- WebApp全屏模式   隐藏地址栏-->
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style" content="black">
    <meta content="telephone=no,email=no" name="format-detection">
    <script type="text/javascript">
        (function (doc, win) {
            var docEl = doc.documentElement,
                resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
                recalc = function () {
                    var clientWidth = docEl.clientWidth;
                    if (!clientWidth) return;
                    if (clientWidth >= 640) {
                        docEl.style.fontSize = '100px';
                    } else {
                        docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
                    }
                };
            if (!doc.addEventListener) return;
            win.addEventListener(resizeEvt, recalc, false);
            doc.addEventListener('DOMContentLoaded', recalc, false);
        })(document, window);
    </script>
    <style>
        html,body {
            -webkit-user-select:none;
            user-select: none;
        }
        a,body,button,dd,div,dl,dt,form,h1,h2,h3,h4,h5,h6,hr,input,label,li,ol,p,select,span,table,td,textarea,th,tr,ul {
            margin: 0;
            padding: 0;
            vertical-align: baseline;
            font-size: 14px;
            font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
            color:#333;
            line-height: 1.2;
            -webkit-text-size-adjust:100%;
        }
        div {
            font-size: 0;
        }
        img {
            width: 100%;
        }
    </style>
</head>
<body>
<div class="">
    <img src="${basePath}/images/content/weixinhao_01.png" alt="">
    <img src="${basePath}/images/content/weixinhao_02.png" alt="">
    <img src="${basePath}/images/content/weixinhao_03.png" alt="">
    <img src="${basePath}/images/content/weixinhao_04.png" alt="">
    <img src="${basePath}/images/content/weixinhao_05.png" alt="">
    <div class="section2" style="background: -webkit-linear-gradient(top,#fbb17b,#F6D365);">
        <img src="${basePath}/images/content/weixinhao_06.png" style="margin: 5px 0 15px;" alt="">
        <img src="${basePath}/images/content/weixinhao_08.png" alt="">
        <img src="${basePath}/images/content/weixinhao_07.png" style="margin-top: 5px;" alt="">
        <img src="${basePath}/images/content/weixinhao_09.png" alt="">
        <img src="${basePath}/images/content/weixinhao_10.png" alt="">
        <img src="${basePath}/images/content/weixinhao_11.png" style="margin-bottom: 25px;" alt="">
    </div>
</div>
</body>
</html>
