<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>还款支付页面</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/theme-orange.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/validate.css" />

    <script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/base.js"></script>
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script src="${basePath }/js/jquery-mvalidate.js"></script>

</head>
<body style="background:#fff;">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
    <!-- main start -->
    <div class="wrapper">
        <div class="apply">
            <h2>申请还款支付费用，请确认并支付！</h2>
            <input type="hidden" id="borrowId" value="${bo.id}">
            <input type="hidden" id="requestNo" value="">
            <ul class="zl_info tk_new sqxf">
                <li>
                    <a rel="external" href="javascript:;">
                        <span>支付金额<strong><fmt:formatNumber pattern='###,###,##0.00' value="${(repayment.repaymentAmount - repayment.repaymentedAmount) / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span style="width:100%;">支付方式<strong>${info.bankName}（${info.card_no}）</strong></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>预留手机<strong>${info.phone}</strong></span>
                    </a>
                </li>
                <li class="nobd gain-yzm gain-yzm1">
                    <a rel="external" href="javascript:;">
                        <span style="width: 4rem;">验证码</span>
                        <input style="width:45%;" type="text" placeholder="请输入验证码" data-role="none" id="smsCode">
                        <div class="yzm clickToken" onclick="getWithholdRequest();" id="yzm" style="text-indent: 16px;float: left;width:25%;">点击获取</div>
                    </a>
                </li>
            </ul>
        </div>
        <a href="javascript:;" id="mima-btn-1" class="js-btn">马上支付</a>
    </div>
    <!-- main end -->

</div>

<div class="popup" id="defray_withhold" style="display:none">
    <div class="overlay"></div>
    <div class="dialog">
        <span class="close"></span>
        <h2>还款总额</h2><h1><fmt:formatNumber pattern='###,###,##0.00' value="${(repayment.repaymentAmount - repayment.repaymentedAmount) / 100.00}"/>元</h1>
        <p class="clearfix">
            <i></i>
            <i></i>
            <i></i>
            <i></i>
            <i></i>
            <i></i>
        </p>
        <p class="error-tips" id="error_tip_withhold"></p>
        <input name="" type="number" value="" pattern="\d*"/>
    </div>
</div>

<form action="" method="post"  id="payPath" style="display: none;">
    <input type="text" name="VERSION" id="VERSION">
    <input type="text" name="MCHNTCD" id="MCHNTCD">
    <input type="text" name="FM" id="FM">
    <input type="text" name="ENCTP" id="ENCTP">
</form>
<script type="text/javascript">
    function goBack(){
        window.location.href = "${path}/repayment/detail?id=${bo.id}";
    }
</script>
<script type="text/javascript">


    function getWithholdRequest(){
        var data = {};
        data.borrowId=$("#borrowId").val();
        openAjax('${path}/pay/repayWithholdRequest?userId=${bo.userId}',data,WithholdRequestCallback);
    }

    function WithholdRequestCallback(data){
        var wait=60;
        if (data.code == "0"){
            $("#requestNo").val(data.msg);
            $('.clickToken').attr("onclick","getWithholdSmsCode()");
            $.mvalidateTip("验证码发送成功");
            time(wait);
        }else{
            $.mvalidateTip(data.msg)
        }
    }

    function getWithholdSmsCode(){
        if(checkRequestNo()){
            var data = {};
            data.request_no = $("#requestNo").val();
            openAjax('${path}/pay/repayWithholdSmscode?userId=${bo.userId}', data,WithholdTokenMsgCallback);
        }
    }
    //易宝支付短信验证码回调
    function WithholdTokenMsgCallback(data) {
        var wait=data.time;
        if (data.code == "0"){
            $.mvalidateTip("验证码发送成功");
            time(wait);
        }else if (data.code == "-3") {
            $.mvalidateTip("请求过于频繁，请"+wait+"s后再重新操作");
        }else{
            $.mvalidateTip(data.message);
        }
    }



    var timer = null;
    jQuery(document).ready(function($) {

        //密码弹窗
        $('#mima-btn-1').click(function(event) {
            if(checkSmsCode() && checkRequestNo()){
                $('#defray_withhold').show();
                $('#defray_withhold i').removeClass('point');
                $('#defray_withhold input').val('').focus();
                $('#error_tip_withhold').html('');
            }
        });

        $('#defray_withhold .close').click(function(event){
            $('#defray_withhold').hide();
        });

        $('#defray_withhold p').click(function(event){
            $('#defray_withhold input').focus();
        });

        $('#defray_withhold input').focus(function(){
            var interval = setInterval(function(){
                if(document.activeElement.nodeName == 'INPUT'){
                    $('#defray_withhold .dialog').css({top:0,marginTop:0});
                }else{
                    $('#defray_withhold .dialog').attr('style','');
                    if (interval) {
                        clearInterval(interval);
                        interval = null;
                    }
                }
            },500);
        });

        $('#defray_withhold input').bind('input',function(event){
            var input = $(this);
            var val = input.val();
            $('#defray_withhold i').removeClass('point');
            for(var i = 0; i < val.length; i++){
                $('#defray_withhold i').eq(i).addClass('point');
            }
            if (val.length >= 6){
                input.val(val.slice(0,6));
                $('#error_tip_withhold').html("正在支付中，请稍等");
                show_loading("正在支付中，请稍等")
                $.post('${path}/pay/repayWithholdConfirm', {id:'${bo.id}',smsCode:$("#smsCode").val(),requestNo:$("#requestNo").val(),payPwd:input.val()} , function(data){
                    input.val("");
                    if(data.code == "-103"){
                        hide_loading();
                        $('#error_tip_withhold').html(data.msg);
                        $.mvalidateTip(data.msg);
                    }else if(data.code == "0"){
                        $('#defray_withhold').hide();
                        //启动轮询
                        query_result("${bo.id}",data.msg);
                    }else{
                        $('#defray_withhold').hide();
                        hide_loading();
                        $.mvalidateTip(data.msg);
                    }
                    $('#defray_withhold i').removeClass('point');
                });
            }
        })
    });
    //判断短信验证码
    function checkSmsCode() {
        if($("#smsCode").val() == null || $("#smsCode").val() == ""){
            $.mvalidateTip("请输入验证码")
            return false;
        }
        return true;
    }

    //验证支付请求编号
    function checkRequestNo(){
        if($("#requestNo").val()==null || $("#requestNo").val()==''){
            $.mvalidateTip("请求完善信息")
            return false;
        }else{
            return true;
        }
    }

    var flag = true;
    //查询订单
    function query_result(id,no){
        //每2秒钟查询一次
        timer = setInterval(function(){
            if(!flag) return;//防止频繁请求
            flag = false;
            $.post('${path}/pay/query-withhold',{id:id,orderNo:no},function(data){
                if(data.code == "0"){
                    if(timer != null) clearInterval(timer);
                    hide_loading();
                    showLoader("还款成功");
                    goBack();
                }else if(data.code == "-101"){
                    flag = true;
                }else{
                    hide_loading();
                    showLoader(data.msg);
                }
            });
        },2000);
    }

    function show_loading(msg){
        $.mobile.loading('show', {
            text: msg, // 加载器中显示的文字
            textVisible: true, // 是否显示文字
            theme: 'b',        // 加载器主题样式a-e
            textonly: false,   // 是否只显示文字
            html: ""           // 要显示的html内容，如图片等
        });
    }

    function hide_loading(){
        // 隐藏加载器
        $.mobile.loading('hide');
    }

    function time(wait){

        for (var i = 1; i <= wait; i++) {
            window.setTimeout("update_p("+ i + "," + wait + ")", i * 1000);
        }
    }

    var update_p = function(num,t){
        if (num == t) {
            $('#yzm').text('重新获取');
            $('#yzm').css({"font-size": "0.8rem","color":"#1283fe"});
        } else {
            var printnr = t - num;
            $('#yzm').text(printnr+'s');
            $('#yzm').css({"font-size": "0.875rem","color":"#666"});
        }
    }
</script>
</body>
</html>