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
        body,h4,html,i,li,p,ul{list-style:none;font-family:Arial,Helvetica,sans-serif;font-size:1rem}
        *{margin:0;padding:0;-webkit-user-select:auto}
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
            height: 110px;
            width: 305px;
            top: 50%;
            left: 50%;
            margin: 0;
            padding: 10px;
            margin-top: -150px;
            margin-left: -163px;
            box-shadow: 0 0 5px rgba(0,0,0,.4);
        }
        .sure-tc p {
            font-size: 16px;
            padding: 8px 0 30px;
            line-height: 25px;
        }
        .sure-tc .sure-btn.btn {
            font-size: 14px;
            height: 30px;
            border-radius: 4px;
            line-height: 30px;
            text-decoration: none;
            padding: 0 10px;
            width: 50px;
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
    </style>
</head>
<body>
<form action="">
<div class="card-list-box">
    <input type="hidden" name="dataCardNum" value="${userBankCardcount}" />
    <ul id="cardList">
        <c:forEach items="${userBankCardList}" var="bankcard">
            <li class="f-cb undefalted">
                <a href="javascript:;" data-defaulted="${bankcard.cardDefault}" data-bank-id="${bankcard.id}" >
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

    <a id="add-card" data-card-num="${userBankCardcount}" class="button" href="javascript:;">添加银行卡</a>

    <div id="sure-tc" class="sure-tc">
        <p>确认修改默认卡吗？</p>
        <div class="btn-both clearfix" style="padding:0 0.5rem;">
            <a onclick="querySureTc();" id="cancel-button" data-ajax="false" href="javascript:;" class="btn sure-btn" style="float: left;">取消</a>
            <a onclick="closeSureTc();" id="sure-button" href="javascript:;" class="btn sure-btn" style="float: right">确定</a>
        </div>
    </div>
    <h3 id="center"></h3>
<%--
    deviceId= ${userBankCardList.deviceId}
    mobilePhone= ${userBankCardList.mobilePhone}--%>
</div>
</form>

<script src="${basePath }/js/jquery-mvalidate.js"></script>
<script type="text/javascript">
    var flag=true;
    var gloabelbank_id;
    var cardNum = $("input[ name='dataCardNum' ]").val();
    if(cardNum >= 3) {
        $('#add-card').hide()
    }
    // 默认卡显示对应背景色
    function addDefaulted(){
        $('#cardList').find('li').each(function() {
            var is_defaulted = $(this).find('a').attr('data-defaulted');
            console.log(is_defaulted);
            var cardName = $(this).find('.text').children('p:first-child').text();
            var cardListLi = $(this);
            var morenMark_html = '<span class="moren_card">默认本卡收款、带扣</span>';
            console.log(cardName);
            if( is_defaulted == 1) {
                switch (cardName)
                {
                    case '中国工商银行':
                        cardListLi.addClass('gongshang').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '招商银行':
                        cardListLi.addClass('zhaoshang').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '中国银行':
                        cardListLi.addClass('zhongguo').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '中国农业银行':
                        cardListLi.addClass('nongye').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '中国建设银行':
                        cardListLi.addClass('jianshe').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '交通银行':
                        cardListLi.addClass('jiaotong').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '中国邮政':
                        cardListLi.addClass('youzheng').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '兴业银行':
                        cardListLi.addClass('xingye').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '浦东发展银行':
                        cardListLi.addClass('pudongfazhan').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        break;
                    case '中国光大银行':
                        cardListLi.addClass('guangda').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '广东发展银行':
                        cardListLi.addClass('guangdongfazhan').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '平安银行':
                        cardListLi.addClass('pingan').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '花旗银行':
                        cardListLi.addClass('huaqi').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '恒丰银行':
                        cardListLi.addClass('hengfeng').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '中国民生银行':
                        cardListLi.addClass('minsheng').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '华夏银行':
                        cardListLi.addClass('huaxia').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '中信银行':
                        cardListLi.addClass('zhongxin').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '北京银行':
                        cardListLi.addClass('beijing').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                    case '江苏银行':
                        cardListLi.addClass('jiangsu').removeClass('undefalted').find('.card-logo').removeClass('grey').end().find('.card_name_mark').append(morenMark_html);
                        $('#cardList').prepend(cardListLi);
                        break;
                }
            }
        })
    }

    // 弹窗-取消
    function querySureTc() {
        flag=true;
        $('#sure-tc').hide()
    }
    function showMsg(text,position){
        var show = $('.show_msg').length;
        if(show>0){
        }else{
            var	div		=	 $('<div></div>');
            div.addClass('show_msg');
            var span	=	$('<span></span>');
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
    $(function () {
        var cardNum;
        addDefaulted();
        // 点击卡列表
        $('#cardList li a').on('click',function () {
//            debugger
            if(!flag)
                return;
//            var cardName = $(this).find('.text').children('p:first-child').text();
//            var cardListLi = $(this).parent();
            var is_defaulted = $(this).attr('data-defaulted');
            var bank_id=gloabelbank_id= parseInt($(this).attr('data-bank-id'));
            if(is_defaulted == 1) {
                window.location.href = '${path}/yeepayBindCard/credit-card/showUserBankCardInfo?deviceId=${deviceId}&mobilePhone=${mobilePhone}&bankId='+ bank_id;
            }else{
                flag=false;
                // 确定切换默认卡
                $('#sure-tc').fadeIn();
            }
        });
    });

    function closeSureTc(bankId) {
        $('#sure-tc').hide();
        $.post('${path}/yeepayBindCard/credit-card/switchDefaultCard?deviceId=${deviceId}&mobilePhone=${mobilePhone}',{bankId: gloabelbank_id},function (data) {
            console.log(data.msg)
            showMsg(data.msg,'center');
            setTimeout(function () {
                flag=true
                window.location.reload();
                addDefaulted();
            },500)

        } ,'json')
    }
    // 添加银行卡，超过3张提示
    $('#add-card').on('click',function () {
//        var cardNum = $(this).attr('data-card-num');
        console.log(cardNum);
        if(cardNum >= 3) {
            showMsg('最多只能绑3张卡','center');
        }else {
            window.location.href = "${path}/yeepayBindCard/credit-card/bindNewCard?deviceId=${deviceId}&mobilePhone=${mobilePhone}";
        }
    })
    /*function  formPost(url,params) {
        var form= document.createElement("form");
        form.action=url;
        form.method="post";
        for(var i in params){
            var hidden = document.createElement('input');
            hidden.setAttribute('type', 'hidden');
            hidden.name=i;
            hidden.value=params[i];
            form.appendChild(hidden);
        }
        document.body.appendChild(form);
        form.submit();
    }*/
    

</script>
</body>
</html>