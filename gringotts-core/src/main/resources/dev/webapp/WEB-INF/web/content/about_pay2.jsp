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
        img {
            display: block;
            width: 100%;
        }
        .repayment .top-banner {
            position: relative;
        }
        .content-wrapper .pannel {
            position: relative;
        }
        .content-wrapper {
            position: relative;
            z-index: -10;
        }
        .content-wrapper .pannel .method-bg {
            position: absolute;
            top: 0;
            left: 50%;
            z-index: -5;
            width: 5.8rem;
            transform: translate(-50%,0);
        }
        .content-wrapper .pannel ul {
            padding: 1.6rem 0 1rem;
            margin: 0 1.1rem;
        }
        .content-wrapper .pannel ul li {
            list-style: none;
            color: #333;
            line-height: 20px;
        }

        .top-title {
            width: 4.26rem;
            margin: .9rem auto 0.6rem;
        }
        .top-txt {
            width: 2.3rem;
            margin: 0 auto 0.55rem;
        }
    </style>
</head>
<body style="background: url(${basePath}/images/content/repay2/group_2.png);background-size: 100%;background-repeat: no-repeat;">
<div class="repayment">
    <div class="top-banner">
        <img class="top-title" src="${basePath}/images/content/repay2/top-banner1.png" alt="">
        <img class="top-txt" src="${basePath}/images/content/repay2/repay_custom.png" alt="">
        <!--<img src="../static/img/method-1.png" alt="" class="method1-title">-->
    </div>
    <div class="content-wrapper">
        <div class="pannel">
            <img class="method-bg" src="${basePath}/images/content/repay2/method1-bg.png" alt="">
            <ul>
                <li>关注小鱼儿官方微信公众号即可在线还款</li>
                <li><img style="margin: 0.2rem auto;width: 4rem;" src="${basePath}/images/content/repay2/wx-pay-1.png" alt=""></li>
                <li style="font-weight: bold;color: #E8403E;">*注：请认准官方唯一微信公众号“小鱼儿”</li>
            </ul>
        </div>
        <div class="pannel">
            <img class="method-bg" style="top: 0.3rem;" src="${basePath}/images/content/repay2/method2-bg.png" alt="">
            <ul style="padding-top: 2rem;padding-bottom: 1.45rem;">
                <li>还款详情请点击app中的联系客服，即可迅速联系到客服小妹妹哦～</li>
                <li><img style="margin: 0.2rem auto;width: 4rem;" src="${basePath}/images/content/repay2/wx-pay-2.png" alt=""></li>
            </ul>
        </div>
    </div>
</div>

</body>
</html>
