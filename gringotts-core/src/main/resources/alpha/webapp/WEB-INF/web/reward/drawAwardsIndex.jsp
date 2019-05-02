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
    <title>老客户福利</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${basePath}/zmxy/css/validate.css" />
    <script type="text/javascript" src="${basePath}/zmxy/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/clipboard.min.js"></script>
    <script src="${basePath}/zmxy/js/jquery-mvalidate.js"></script>
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
        .f-cb:after {
            display: block;
            clear: both;
            content: '.';
            height: 0;
            overflow: hidden;
            visibility: hidden;
        }
        img {
            width: 100%;
        }
        .tc {
            text-align: center;
        }
        .g-yqhy {
            background: url(${basePath}/images/content/yqhy-bg.jpg) top center no-repeat;
            background-size: cover;
            background-color: #5e51df;
        }
        .g-yqhy .head-logo img {
            width: 5.1rem;
            margin: 0.625rem 0 0;
        }
        .g-yqhy .m-welfare {
            margin-top: 0.3rem;
        }
        .g-yqhy .m-welfare img {
            width: 4.5rem;
        }
        .m-pannel .title-bg {
            position: relative;
            background: url("${basePath}/images/content/yqhy-title-bg.png") no-repeat;
            background-size: contain;
            z-index: 10;
        }
        .m-pannel,
        .m-call{
            padding: 0 0.4rem;
        }
        .title-bg {
            padding: 0.25rem 1.5rem 0.3rem 0.4rem;
            display: table;
            margin: 0 auto;
        }
        .title-bg img {
            width: 2.3rem;
        }
        .m-pannel .content {
            text-align: center;
            margin-top: -0.53rem;
            box-shadow: 0 1px 8px 2px rgba(35, 116, 225, 0.57);
        }
        .m-pannel .content p {
            font-size: 0.22rem;
            color: #6c46ed;
            letter-spacing: -0.006rem;
        }
        .m-pannel .content ul li {
            font-size: 0.24rem;
            color: #ed464e;
            margin-top: 0.15rem;
            list-style: none;
        }
        .m-pannel .text {
            padding: 0.65rem 0.06rem 0.3rem;
            background: url("${basePath}/images/content/reward-bg.png") no-repeat;
            background-size: 100%;
        }
        .m-pannel .way {
            background: url("${basePath}/images/content/reward-bg2.png") no-repeat;
            background-size: 100%;
            padding: 0.65rem 0.5rem 0.3rem;
            position: relative;
        }
        .m-pannel .way p {
        }
        .m-pannel .content .detail p {
            font-size: 0.18rem;
            letter-spacing: normal;
        }
        .way .way-to {
            width: 1rem;
            margin: 0.77rem 0.12rem 0;
        }
        .way .detail,
        .way .way-to {
            float: left;
        }
        .detail img {
            box-shadow: 0 5px 15px -3px rgba(113, 136, 240, 0.57);
            border-radius: 50%;
            width: 1.6rem;
            margin-bottom: 0.16rem;
        }
        .tel-phone {
            text-align: center;
            margin: 0.36rem 0;
            color: #6c46ed;
            font-size: 0.24rem;
            line-height: 1.8;
        }
        .m-call img {
            box-shadow: 0 3px 5px 0px rgba(35, 116, 225, 0.57);
        }
        .m-exposition {
            margin: 0.3rem 0 0.2rem;
            text-align: center;
        }
        .m-exposition p {
            color: #7574f4;
            font-size: 12px;
            letter-spacing: -0.006rem;
        }
        .m-exposition .time {
            margin-bottom: 0.2rem;
            color: #6c46ed;
            font-size: 0.22rem;
            letter-spacing: 0;
            text-indent: -0.08rem;
        }
        .share {
            position: absolute;
            bottom: 10px;
            right: 15px;
            max-width: 3rem;
        }
    </style>
</head>
<body class="g-yqhy">
<div class="head-logo tc"><img src="${basePath}/images/content/jxqb.png" alt=""></div>
<div class="m-welfare tc"><img src="${basePath}/images/content/yqhy-welfare.png" alt=""></div>
<div class="m-pannel" style="margin-top: 0.35rem">
    <h3 class="title-bg"><img src="${basePath}/images/content/yq-reward.png" alt=""></h3>
    <div class="content text">
        <p>信用良好的平台用户推荐新用户，有机会获得以下福利:</p>
        <ul>
            <li>更高借款额度、</li>
            <li>更长借款期限、</li>
            <li>更低借款利息、</li>
            <li>更快审批放款！</li>
        </ul>
    </div>
</div>
<div class="m-pannel" style="margin-top: 0.4rem;">
    <h3 class="title-bg"><img src="${basePath}/images/content/join-way.png" alt=""></h3>
    <div class="content way f-cb">
        <div class="detail">
            <img src="${basePath}/images/content/join-detail1.png" alt="">
            <p style="padding-top: 0.13rem;">打开小鱼儿app</p>
        </div>
        <img class="way-to" src="${basePath}/images/content/join-dt1dt2.png" alt="">
        <div class="detail">
            <img src="${basePath}/images/content/join-detail2.png" alt="">
            <p class="share">点击推荐给好友<br/>即可分享自己的推广二维码</p>
        </div>
    </div>
    <div class="tel-phone">详情请咨询客服，即可领取福利，<br/>客服电话：<i id="phone-msg">400-007-2140</i></div>
</div>
<div class="m-call">
    <a id="copy-btn" href="javascript:;" data-clipboard-action="copy" data-clipboard-text="i"><img src="${basePath}/images/content/call-welfare.png" alt=""></a>
</div>
<div class="m-exposition">
    <p class="time">工作时间：工作日9:00～18:00</p>
    <p>本活动最终解释权归小鱼儿所有！</p>
</div>
<script>
    var _copy = document.getElementById('copy-btn');
    function copyArticle(event) {
        const range = document.createRange();
        range.selectNode(document.getElementById('phone-msg'));

        const selection = window.getSelection();
        if(selection.rangeCount > 0) {
            selection.removeAllRanges();
        }
        selection.addRange(range);
        if(document.execCommand('copy', false, null)){
            $.mvalidateTip("客服电话已复制到粘贴板")
        } else{
            $.mvalidateTip("暂不支持该功能")
        }

    }
    _copy.addEventListener('click',copyArticle);

    //var clipboard = new Clipboard('#copy');
    ////可以自己加些处理
    //clipboard.on('success', function(e) {
    //    $.mvalidateTip("客服电话已复制到粘贴板")
    //});
    //clipboard.on('error', function(e) {
    //    $.mvalidateTip("暂不支持该功能")
    //});

</script>
</body>
</html>
