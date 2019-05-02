<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
    String staticBasePath = "http://static.jx-money.com/common/web";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
    <c:set var="path" value="<%=path%>"></c:set>
    <c:set var="basePath" value="<%=basePath%>"></c:set>
    <c:set var="staticBasePath" value="<%=staticBasePath%>"></c:set>
    <title>银行卡列表</title>
    <meta charset="UTF-8">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/validate.css" />
    <script type="text/javascript" src="${basePath}/js/jquery-1.9.1.min.js"></script>
    <script src="${basePath }/js/jquery-mvalidate.js"></script>
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
        body,h4,html,i,li,p,ul{list-style:none;font-family:Arial,Helvetica,sans-serif;font-size:1rem;font-weight: normal !important;text-shadow: none;}
        *{margin:0;padding:0;-webkit-user-select:auto;font-weight: normal !important;text-shadow: none;}
        .f-cb:after{display:block;content:'.';height:0;visibility:hidden;overflow:hidden;clear:both}
        .fl{float:left}
        .card-list-box {
            padding: 20px;
        }
        /* 19张银行卡背景色*/
        .jiaotong {
            background: #1B64A3;
        }
        .zhaoshang {
            background: #DB5555;
        }
        .nongye {
            background: #209772;
        }
        .gongshang {
            background: #DB5555;
        }
        .zhongxin {
            background: #DB5555;
        }
        .zhongguo {
            background: #C02525;
        }
        .beijing {
            background: #DB5555;
        }
        .jianshe {
            background: #1B64A3;
        }
        .youzheng {
            background: #329457;
        }
        .xingye {
            background: #3173B8;
        }
        .pudongfazhan {
            background: #0E3868;
        }
        .hengfeng {
            background: #A88249;
        }
        .minsheng {
            background: #5AA572;
        }
        .huaxia {
            background: #DB5555;
        }
        .jiangsu {
            background: #D4AB44;
        }
        .guangda {
            background: #835192;
        }
        .guangdongfazhan {
            background: #DB5555;
        }
        .pingan {
            background: #EC681B;
        }
        .huaqi {
            background: #01458E;
        }
        .card-list-box ul li {
            position: relative;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 10px;
            cursor: pointer;
        }
        .card-list-box ul li a {
            display: block;
            text-decoration: none;
        }
        .card-list-box ul li .card-logo {
            width: 40px;
            float: left;
        }
        .card-list-box ul li .bg-img {
            position: absolute;
            right: 28px;
            top: 50%;
            z-index: 1;
            width: 108px;
            transform: translateY(-50%);
        }
        .card-list-box ul li .text {
            margin-left: 50px;
            position: relative;
            z-index: 10;
        }
        .card-list-box ul li .text p {
            font-size: 17px;
            color: #fff;
        }
        .card-list-box ul li .text .moren_card {
            font-size: 12px;
            color: #fff;
            float: right;
        }
        .card-list-box ul li .text .name {
            opacity: 0.7;
            font-size: 13px;
            color: #fff;
            margin: 2px 0 5px;
        }
        .card-list-box ul li .text h5{
            font-size: 22px;
            color: #FFFFFF;
            font-weight: normal;
        }
        .undefalted {
            background: #aaa !important;
        }
        .undefalted .grey {
            -webkit-filter: grayscale(100%);
            -moz-filter: grayscale(100%);
            -ms-filter: grayscale(100%);
            -o-filter: grayscale(100%);
            filter: grayscale(100%);
        }
        .card-list-box .warn {
            font-size: 14px;
            color: #f98620;
            margin-top: 18px;
        }
        .card-list-box .button {
            display: block;
            box-sizing: border-box;
            padding: 10px;
            text-align: center;
            width: 100%;
            margin: 8px 0 0;
            background: #31c27c;
            border: 1px solid #31c27c;
            color: #fff;
            font-size: 15px;
            letter-spacing: 1px;
            text-decoration: none;
        }
        .sure-tc {
            display: none;
            height: auto;
            width: 305px;
            top: 50%;
            left: 50%;
            margin: 0;
            padding: 20px 10px 20px;
            margin-top: -150px;
            margin-left: -163px;
            box-shadow: 0 0 5px rgba(0,0,0,.4);
            border-radius: 6px;
        }
        .sure-tc p {
            font-size: 18px;
            padding: 10px 0 25px;
            line-height: 25px;
        }
        .sure-tc .sure-btn.btn {
            font-size: 16px;
            height: 42px;
            border-radius: 4px;
            line-height: 42px;
            text-decoration: none;
            padding: 0 10px;
            width: 38%;
        }
        .show_msg{
            width:100%;
            height:35px;
            text-align:center;
            position:fixed;
            left: 0;
            z-index: 999;
        }
        .show_span{
            display: inline-block;
            height: 35px;
            padding: 0 15px;
            line-height: 35px;
            background:rgba(0,0,0,0.8);
            border-radius: 50px;
            color: #fff;
            font-size: 14px;
        }
        .ui-corner-all {
            -webkit-border-radius: .3125em;
            border-radius: .3125em;
        }
        .ui-btn-corner-all, .ui-corner-all {
            -webkit-background-clip: padding;
            background-clip: padding-box;
        }
        .ui-body-b {
            background-color: #2a2a2a;
            border-color: #1d1d1d;
            color: #fff;
            text-shadow: 0 1px 0 #111;
        }
        .ui-body-b {
            border-width: 1px;
            border-style: solid;
        }
        .ui-loader {
            display: none;
            z-index: 9999999;
            position: fixed;
            top: 50%;
            left: 50%;
            border: 0;
        }
        .ui-loader-verbose {
            width: 12.5em;
            filter: Alpha(Opacity=88);
            opacity: .88;
            box-shadow: 0 1px 1px -1px #fff;
            height: auto;
            margin-left: -6.875em;
            margin-top: -2.6875em;
            padding: .625em;
        }
        .ui-corner-all {
            border-radius: 0;
        }
        .ui-loader-verbose {
            width: 25%;
            margin: 0;
            padding: 10px;
        }
        .ui-loading .ui-loader {
            display: block;
        }
        .ui-icon-loading {
            background: url(${path}/common/web/css/images/ajax-loader.gif);
            background-size: 2.875em 2.875em;
        }
        .ui-loader .ui-icon-loading {
            display: block;
            margin: 0;
            width: 2.75em;
            height: 2.75em;
            padding: .0625em;
            -webkit-border-radius: 2.25em;
            border-radius: 2.25em;
        }
        .ui-loader-verbose .ui-icon-loading {
            margin: 0 auto .625em;
            filter: Alpha(Opacity=75);
            opacity: .75;
        }
        .ui-loader-verbose h1 {
            font-size: 1em;
            margin: 0;
            text-align: center;
        }
        .ui-loader-verbose {
            width: 25%;
            margin: 0;
            padding: 10px;
        }
        .ui-loader-verbose .ui-icon-loading {
            width: 55px;
            height: 55px;
            background-size: 100%;
            margin-bottom: 10px;
        }
        .ui-loader-verbose h1 {
            font-size: 14px;
            font-weight: normal;
        }
        .ui-loading .ui-loader {
            position: fixed;
            top: 50%;
            left: 50%;
            margin-top: -55px;
            margin-left: -55px;
        }
    </style>
</head>
<body>
<form action="">
    <div class="card-list-box">
        <input type="hidden" name="dataCardNum" value="${userBankCardcount}" />
        <ul id="cardList">
            <c:forEach items="${userBankCardList}" var="bankcard">
                <li class="f-cb undefalted">
                    <a href="javascript:;" data-defaulted="${bankcard.cardDefault}" data-bank-id="${bankcard.id}" data-open-bank="${bankcard.open_bank}" >
                        <img class="bg-img" src="${path}/${bankcard.bankLogoImg2}" alt="">
                        <img class="fl card-logo grey" src="${path}/${bankcard.bankLogoImg1}">
                        <div class="text">
                            <p class="card_name_mark">${bankcard.bankName}</p >
                            <p class="name">${bankcard.openName}</p >
                            <h5>${bankcard.bankCardNo}</h5>
                        </div>
                    </a>
                </li>
            </c:forEach>
        </ul>
        <p class="warn">温馨提示：最多只能绑3张卡</p>

        <c:if test="${userBankCardcount<3||userBankCardcount==null}">
            <a id="add-card" data-card-num="${userBankCardcount}" class="button" href="javascript:;">添加银行卡</a>
        </c:if>

        <div id="sure-tc" class="sure-tc">
            <p>确认修改默认卡吗？</p>
            <div class="btn-both clearfix" style="padding:0 0.25rem;">
                <a onclick="querySureTc();" id="cancel-button" data-ajax="false" href="javascript:;" class="btn sure-btn" style="float: left;">取消</a>
                <a onclick="closeSureTc();" id="sure-button" href="javascript:;" class="btn sure-btn" style="float: right">确定</a>
            </div>
        </div>
        <div class="cover"></div>
        <h3 id="center"></h3>
        <%--
            deviceId= ${userBankCardList.deviceId}
            mobilePhone= ${userBankCardList.mobilePhone}--%>
    </div>
</form>
<form action="/www.bindcardinfo.com?msg=操作完成" id="tempForm" method="get" data-ajax="false"></form>

<script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
<script src="${basePath }/js/jquery-mvalidate.js"></script>
<script type="text/javascript">

</script>
<script type="text/javascript">
    var randomTime =  setInterval(function () {
        var toggleBindCardTrue = localStorage.getItem("toggleBindCard");
        if(toggleBindCardTrue) {
            clearInterval(randomTime);
            localStorage.removeItem("toggleBindCard");
            window.location.reload();
        }
    },100);

    var flag=true;
    var gloabelbank_id;
    // var cardNum = $("input[ name='dataCardNum' ]").val();
    // if(cardNum >= 3) {
    //     $('#add-card').hide()
    // }

    var bank_config = [];
    bank_config.ECITIC = "zhongxin";
    bank_config.CEB = "guangda";
    bank_config.ABC = "nongye";
    bank_config.ICBC = "gongshang";
    bank_config.CCB = "jianshe";
    bank_config.CMBC = "minsheng";
    bank_config.PSBC = "youzheng";
    bank_config.BOC = "zhongguo";
    bank_config.BOCO = "jiaotong";
    bank_config.CIB = "xingye";
    bank_config.BCCB = "beijing";
    bank_config.HX = "huaxia";
    bank_config.SZPA = "pingan";
    bank_config.GDB = "guangdongfazhan";
    bank_config.HFB = "hengfeng";
    bank_config.CMBCHINA = "zhaoshang";
    bank_config.JSBC = "jiangsu";
    bank_config.SPDB = "pudongfazhan";
    bank_config.CTSH = "huaqi";
    // 默认卡显示对应背景色
    function addDefaulted(){
        $('#cardList').find('li').each(function() {
            var is_defaulted = $(this).find('a').attr('data-defaulted');
            console.log(is_defaulted);
            var cardName = $(this).find('.text').children('p:first-child').text();
            var openBank = $(this).find('a').attr('data-open-bank');
            var cardListLi = $(this);
            var morenMark_html = '<span class="moren_card">默认本卡收款、代扣</span>';
            console.log(cardName);
            if( is_defaulted == 1) {
                cardListLi.addClass(bank_config[openBank]).removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                $('#cardList').prepend(cardListLi);
            }
        })
    }

    // 弹窗-取消
    function querySureTc() {
        flag=true;
        $('#sure-tc,.cover').hide()
    }
    function showMsg(text,position){
        var show = $('.show_msg').length;
        if(show>0){
        }else{
            var	div	= $('<div></div>');
            div.addClass('show_msg');
            var span= $('<span></span>');
            span.addClass('show_span');
            span.appendTo(div);
            span.text(text);
            $('body').append(div);
        }
        $(".show_span").text(text);
        if(position=='bottom'){
            $(".show_msg").css('bottom','5%');
        }else if(position=='center'){
            $(".show_msg").css('top','');
            $(".show_msg").css('bottom','50%');
        }else{
            $(".show_msg").css('bottom','95%');
        }
        $('.show_msg').hide();
        $('.show_msg').fadeIn(100);
        $('.show_msg').fadeOut(2000);
    }

    addDefaulted();

    $(function () {
        var cardNum;
        // 点击卡列表
        $('#cardList li a').on('click',function () {
            if(!flag){return;}
            var is_defaulted = $(this).attr('data-defaulted');
            var bank_id=gloabelbank_id= parseInt($(this).attr('data-bank-id'));
            if(is_defaulted == 1) {
                showMsg("此卡已是默认卡",'center');
                return;
            }else{
                flag=false;
                // 确定切换默认卡
                $('#sure-tc,.cover').fadeIn();
            }
        });
        $('.cover').on('click',function () {
            flag=true;
            $('#sure-tc,.cover').fadeOut();
        })
    });
    function show_loading(msg){
        $.mobile.loading('show', {
            text: msg, // 加载器中显示的文字
            textVisible: true, // 是否显示文字
            theme: 'b',        // 加载器主题样式a-e
            textonly: false,   // 是否只显示文字
            html: ""           // 要显示的html内容，如图片等
        });
    }
    function hideLoader()
    {
        // 隐藏加载器
        $.mobile.loading('hide');
    }
    function closeSureTc(bankId) {
        $('#sure-tc,.cover').hide();
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        show_loading('切换中');
        $.post('${path}/yeepayBindCard/credit-card/switchDefaultCard?deviceId=${deviceId}&mobilePhone=${mobilePhone}',{bankId: gloabelbank_id},function (data) {
            $("#tempForm").attr("action","/www.bindcardinfo.com?msg="+msg);
            var msg = data.msg;
            if(data.code == "0"){
                if (isAndroid) {
                    window.nativeMethod.authenticationResult(msg);
                }else if(isIOS){
                    $("#tempForm").submit();
                }
            }else{
                $(".ui-loader.ui-corner-all").hide()
                showMsg(msg,'center');
                flag = true;
            }
        } ,'json')
    }
    // 添加银行卡，超过3张提示
    $('#add-card').on('click',function () {
        show_loading("");
        $.post('${path}/yeepayBindCard/credit-card/addCardOrNot?deviceId=${deviceId}&mobilePhone=${mobilePhone}',function (data) {
            var msg = data.msg;
            hideLoader();
            //可以绑卡
            if(data.code == "0"){
                window.location.href = "${path}/yeepayBindCard/credit-card/bindNewCard?deviceId=${deviceId}&mobilePhone=${mobilePhone}";
            }
            //超过绑卡数量
            else if(data.code == "1"){
                showMsg(msg,'center');
                setTimeout(function(){
                    window.location.reload();
                },1000);
            }else{
                showMsg(msg,'center');
            }
        } ,'json')
    });
</script>
</body>
</html>