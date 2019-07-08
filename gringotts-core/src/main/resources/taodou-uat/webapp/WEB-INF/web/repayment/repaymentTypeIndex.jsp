<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>还款方式</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/css/common.css"/>
    <style>
        .wrapper {
            padding-bottom: 2rem;
        }
    </style>
    <script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
    <script>
        $(document).ready(function (e) {
            $('.yh_in h2').click(function () {
                $(this).next().slideToggle("slow");
                $(this).toggleClass("jt_s");
                $('.jt-xs i').show();
            });
            setTitles();

        });
        function setTitles() {
            var type = '${type}';
            var id = '';
            var sId = '';
            var hId = '';

            if (type && (type == 1 || type == 2)) {
                id = "#jf_1";
                sId = '#jf1';
                hId = '#jf2';
            } else if (type && type == 3) {
                id = "#jf_2";
                sId = '#jf2';
                hId = '#jf1';
            }
            $(id).toggleClass("jf_z");
            $(sId).show();
            $(hId).hide();
            $('#h_' + type).toggleClass("jt_s");
            $('#d_' + type).show();
        }
    </script>
</head>
<body>
<div class="wrapper">
    <div class="jf no-top">
        <div id="jf">
            <ul>
                <li class="" id="jf_1"><a><img src="${basePath}/images/yhk_icon.png">银行卡</a></li>
            </ul>
        </div>
        <div id="jf1" style="display:none;">
            <div class="yh_in">
                <h2 id="h_1"><i></i>1.一键还款（推荐）</h2>
                <div id="d_1" class="yh_in1" style="display:none;">
                    <ul>
                        <li><h3>第一步：在还款页点击本次需要还款的订单</h3></li>
                        <li><img src="${basePath}/images/dh41.png"></li>
                        <li><h3>第二步：点击立即还款</h3></li>
                        <li><img src="${basePath}/images/dh42.png"></li>
                        <li><h3>第三步：点击马上支付</h3></li>
                        <li><img src="${basePath}/images/dh43.png"></li>
                    </ul>
                </div>
                <h2 id="h_2" style="display:none;"><i></i>2.到期自动代扣（推荐）</h2>
                <div id="d_2" class="yh_in2" style="display:none;">
                    <li>
                        <p>在还款日之前（含当日），把本次所需还款的金额存入绑定的银行卡内，我司会在还款日的05点，17点进行扣款，如成功，将会以短信形式通知您。</p>
                    </li>
                </div>
            </div>
            <div class="ts" style="display:none;">
                <ul>
                    <li><p>1、更改绑定的银行卡后进行还款</p></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<script>
    window.onload = init;
    function init() {
        var jf_1 = document.getElementById("jf_1");
        var jf_2 = document.getElementById("jf_2");
        var jf1 = document.getElementById("jf1");
        var jf2 = document.getElementById("jf2");
        jf_2.onclick = function () {
            jf2.style.display = "block";
            jf_2.className = "jf_z";
            jf1.style.display = "none";
            jf_1.className = "";

        }
        jf_1.onclick = function () {
            jf2.style.display = "none";
            jf_2.className = "";
            jf1.style.display = "block";
            jf_1.className = "jf_z";

        }
    }
</script>
</body>
</html>
