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
    <title>申请续期</title>
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
    <%-- <div class="header">
         <a class="back_arr" data-ajax="false" href="${path}/repayment/detail.do?id=${bo.id}"></a>
         <h1>申请续期</h1>
     </div>--%>
    <!-- main start -->
    <div class="wrapper">
        <div class="apply">
            <h2>申请还款续期服务需要支付费用，请确认并支付！</h2>
            <ul class="zl_info tk_new sqxf">
                <li>
                    <a rel="external" href="javascript:;">
                        <span>待还本金<strong><fmt:formatNumber pattern='###,###,##0.00' value="${waitAmount / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>续期天数<strong>${bo.loanTerm}</strong><em>天</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>服务费<strong><fmt:formatNumber pattern='###,###,##0.00' value="${loanApr / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span>续期费<strong><fmt:formatNumber pattern='###,###,##0.00' value="${renewalFee / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li class="nobd">
                    <a rel="external" href="javascript:;">
                        <span>逾期费<strong><fmt:formatNumber pattern='###,###,##0.00' value="${waitLate / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li class="nobd">
                    <a rel="external" href="javascript:;">
                        <span style="width:100%;">支付方式<strong>${info.bankName}（${info.card_no}）</strong></span>
                    </a>
                </li>
            </ul>
            <div class="sum-box clearfix">
                <p>总服务费：<strong><fmt:formatNumber pattern='###,###,##0.00' value="${allCount / 100.00}"/></strong>元</p>
                <a href="${path}/credit-loan/description" data-ajax="false">关于续期&gt;</a>
            </div>
            <div class="tip">
                <span>完成支付续期总服务费后，即可成功续期</span>
            </div>
        </div>
        <%--<a href="javascript:;" id="mima-btn" class="js-btn">马上支付</a>--%>
        <a href="javascript:;" id="mima-btn-1" class="js-btn">马上支付</a>
    </div>
    <!-- main end -->

</div>

<div class="popup" id="defray_withhold" style="display:none">
    <div class="overlay"></div>
    <div class="dialog">
        <span class="close"></span>
        <h2>总费用</h2><h1><fmt:formatNumber pattern='###,###,##0.00' value="${allCount / 100.00}"/>元</h1>
        <p class="clearfix">
            <i></i>
            <i></i>
            <i></i>
            <i></i>
            <i></i>
            <i></i>
        </p>
        <p class="error-tips"></p>
        <input name="" type="number" value="" pattern="\d*"/>
    </div>
</div>

<form action="${path}/repayment/detail?id=${bo.id}" method="post"  id="payPath">
    <input type="text" name="VERSION" id="VERSION">
    <input type="text" name="MCHNTCD" id="MCHNTCD">
    <input type="text" name="FM" id="FM">
    <input type="text" name="ENCTP" id="ENCTP">
</form>
<script>
    function goBack(){
        location="${path}/repayment/detail?id=${bo.id}";
    }
</script>

<script type="text/javascript">

    var timer = null;
    var deal_flag = true;
    jQuery(document).ready(function($) {

        //支付密码弹框
        $('#mima-btn-1').click(function(event) {
            $('#defray_withhold').show();
            $('#defray_withhold i').removeClass('point');
            $('#defray_withhold input').val('').focus();
            $('#defray_withhold .error-tips').html('');
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
                $('#defray_withhold .error-tips').html("正在支付中，请稍等");
                show_loading("正在支付中，请稍等");
                if(!deal_flag){return;}
                deal_flag = false;
                $.post('${path}/ucfpay/renewal-withhold', {id:'${bo.id}',payPwd:input.val(), money:'${allCount}'} , function(data){
                    deal_flag = true;
                    input.val("");
                    var num = data.code;
                    if(data.code == "-103"){
                        hide_loading();
                        $('#defray_withhold .error-tips').html(data.msg);
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
    var flag = true;
    //查询订单
    function query_result(id,no){
        //每2秒钟查询一次
        timer = setInterval(function(){
            if(!flag) return;//防止频繁请求
            flag = false;
            $.post('${path}/ucfpay/query-renewalWithhold',{id:id,orderNo:no},function(data){
                if(data.code == "0"){
                    if(timer != null) clearInterval(timer);
                    hide_loading();
                    showLoader("支付成功");
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
</script>
</body>
</html>