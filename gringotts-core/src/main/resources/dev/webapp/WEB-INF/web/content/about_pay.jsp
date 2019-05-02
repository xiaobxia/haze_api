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
    <title>还款方式</title>
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
            font-size: 0.21rem;
            font-family: PingFangSC-Regular,"Helvetica Neue",Helvetica,Arial,sans-serif;
            color:#333;
            line-height: 1.2;
            -webkit-text-size-adjust:100%;
        }
        .f-cb:after {
            display: block;
            clear: both;
            content: '.';
            height: 0;
            overflow: hidden;
            visibility: hidden;
        }
        img {
            display: block;
            width: 100%;
        }
        .tc {
            text-align: center;
        }
        .repayment .top-banner {
            position: relative;
        }
        .method1-title {
            position: absolute;
            bottom: -0.13rem;
            left: 50%;
            transform: translate(-50%,0);
            width: 2.8rem;
        }
        .method2-title {
            width: 2.8rem;
            margin: 0 auto;
        }
        .content-wrapper .pannel {
            position: relative;
        }
        .content-wrapper {
            position: relative;
            z-index: -10;
            background: linear-gradient(to bottom,#f9fcac,#feee5d);
        }
        .content-wrapper .pannel .method-bg {
            position: absolute;
            top: 0;
            left: 0;
            z-index: -5;
        }
        .content-wrapper .pannel ul {
            padding: 1.5rem 0 1rem;
            margin: 0 1.1rem;
        }
        .content-wrapper .pannel ul li {
            list-style: none;
            color: #fd5964;
        }
    </style>
</head>
<body>
<div class="repayment">
    <div class="top-banner">
        <img src="${basePath}/images/content/top-banner.png" alt="">
        <img src="${basePath}/images/content/method-1.png" alt="" class="method1-title">
    </div>
    <div class="content-wrapper">
        <div class="pannel">
            <img class="method-bg" src="${basePath}/images/content/method1-bg.png" alt="">
            <ul>
                <li>关注小鱼儿官方微信公众号即可在线还款</li>
                <li><img src="${basePath}/images/content/wx-pay-1.png" alt=""></li>
                <li style="font-weight: bold;">*注：请认准官方唯一微信公众号“小鱼儿”</li>
            </ul>
        </div>
        <div class="pannel">
            <img class="method-bg" style="top: 0.3rem;" src="${basePath}/images/content/method2-bg.png" alt="">
            <img class="method2-title" src="${basePath}/images/content/method-2.png" alt="">
            <ul style="padding-top: 1.25rem;padding-bottom: 1.19rem;">
                <li>还款详情请点击app中的联系客服，即可迅速联系到客服小妹妹哦～</li>
                <li><img src="${basePath}/images/content/wx-pay-2.png" alt=""></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
