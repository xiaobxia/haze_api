<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css"
          href="${basePath }/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/css/basic.css"/>
    <link rel="stylesheet" type="text/css"
          href="${basePath }/css/common.css"/>

    <script type="text/javascript" src="${basePath }/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript"
            src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/base.js"></script>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
    <!-- 		<div class="header"> -->
    <!-- 			<a class="back_arr" href="javascript:void(0);"></a> -->
    <!-- 			<h1>手机运营商认证</h1> -->
    <!-- 		</div> -->
    <!-- main start -->
    <div class="wrapper">
        <div class="apply" style="margin-top: 0.4rem;">
            <form id="form" name="form" action="${path}/credit-web/mobileApproveSub" method="post" data-ajax="false">

                <input type="hidden" name="userId" value="${userId}">
                <input type="hidden" name="twoOrFail" value="${needQueryPwd}">
                <input type="hidden" name="token" value="${token}">
                <input type="hidden" name="website" value="${website}">
                <input type="hidden" name="password" value="${password}">
                <input type="hidden" name="queryPwd" value="${queryPwd}">
                <input type="hidden" name="deviceId" value="${deviceId}">
                <input type="hidden" name="mobilePhone" value="${mobilePhone}">
                <input type="hidden" name="clientType" value="${clientType}">
                <ul class="zl_info tk_new more-info">
                    <li><a rel="external" href="javascript:;"> <input
                            type="text" value="${userPhone} " name="phone" data-role="none" readonly="true">
                    </a></li>
                    <li class="nobd"><a rel="external" href="javascript:;"> <input
                            type="text" name="SUBMIT_CAPTCHA" id="SUBMIT_CAPTCHA" placeholder="请输入收到的验证码" value=""
                            data-role="none">

                    </a></li>

                </ul>
            </form>
            <div class="tip">
                <p>温馨提示</p>
                <p>1、请输入正确的运营商（移动、联通、电信）服务密码，如若忘记可通过拨打运营商服务电话或者登陆网上营业厅找回密码；</p>
                <p>2、运营商认证需要2~3分钟，请耐心等待；</p>
            </div>
        </div>
        <a href="javascript:;" class="js-btn" id="bc-btn" onclick="submit1();">确认</a> <span
            class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
    </div>
    <!-- main end -->
</div>
</body>
</html>
<script type="text/javascript">
    $(function () {
        var msg = '${msg}';
        if (msg != null && msg.length > 0) {
            showLoader(msg);
        }
    })
    function submit1() {

        if (form.SUBMIT_CAPTCHA.value != "") {
            $('#bc-btn').removeAttr("onclick");
            $('#bc-btn').removeClass("js-btn");
            $('#bc-btn').addClass("js-btn yrz");
            showLoader("请等待……")
            $('#bc-btn').text("认证中");
            $('#form').submit();

        } else {
            showLoader("验证码码不能为空！");

        }

    }
    function bind() {
        //if ($("#form").valid()) {
        $.ajax({
            type: "post",
            data: $("#form").serialize(),
            timeout: 120 * 1000,
            url: "${path}/credit-web/mobileApproveSub",
            success: function (ret) {
                alert(ret.msg);
                alert(ret.code);
                if (ret.code == '200') {
                    //window.location.href = "info";
                } else if (ret.code == '100') {
                    $("#li_SUBMIT_CAPTCHA").show();
                    //clearInput();
                } else if (ret.code == '300') {
                    window.location.href = "toMobile";
                } else {
                }
            },
            error: function (ret) {
                alert("未知异常，请稍后重试！");
            },
            beforeSend: function () {
                $("#saveA").attr('onclick', "");
                $("#saveA").html("请耐心等待");
            },
            complete: function () {
                $("#saveA").html("确定");
                $("#saveA").attr('onclick', "bind();");
            }
        });
        //}
    }


</script>