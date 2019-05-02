<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web";
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
    <link rel="stylesheet" type="text/css" href="${basePath}/zmxy/css/validate.css" />
    <script type="text/javascript" src="${basePath}/zmxy/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script src="${basePath}/zmxy/js/jquery-mvalidate.js"></script>
    <%--<script type='text/javascript' src='https://webchat.7moor.com/javascripts/7moorInit.js?accessId=e48aabd0-5755-11e8-9ced-e7fc175a7ab5&autoShow=false&language=ZHCN' async='async'>--%>
    <%--</script>--%>


    <style>
        body,h4,html,i,li,p,ul{list-style:none;font-family:Arial,Helvetica,sans-serif;font-size:1rem}
        *{margin:0;padding:0;-webkit-user-select:auto}
        .f-cb:after{display:block;content:'.';height:0;visibility:hidden;overflow:hidden;clear:both}
        .fl{float:left}
        .fr{float:right}
        .g-repayment h5{font-size:13px;color:#777;padding:20px;font-weight:400}
        .g-repayment ul li{border-bottom:1px solid #efefef}
        .g-repayment ul li a{display:block;text-decoration:none;background:#fff;padding:12px 15px;-webkit-tap-highlight-color:transparent;-moz-tap-highlight-color:transparent;-ms-tap-highlight-color:transparent;-o-tap-highlight-color:transparent;tap-highlight-color:transparent}
        .g-repayment ul li .content .ewm p button:active{borer:none}
        .g-repayment ul li a span{font-size:16px;color:#333;line-height:37px}
        .g-repayment ul li a .direction{width:17px;margin-top:14px;transform:rotate(-90deg)}
        .g-repayment ul li a .active{transform:rotate(0)}
        .g-repayment ul li a .sign{margin-right:15px}
        .g-repayment ul li .content{display:none}
        .g-repayment ul li .content .ewm{padding:20px 0;text-align:center}
        .g-repayment ul li .content .ewm.bank_desc p{margin: 5px 0;}
        .g-repayment ul li .content .ewm img{width:170px}
        .g-repayment ul li .content .ewm p{font-size:15px;color:#595959;line-height:1.5}
        .g-repayment ul li .content .ewm .company_txt{margin-top: 8px;font-size: 14px;color: #777;}
        .g-repayment ul li .content .ewm p .content_num {font-weight: bold}
        .g-repayment ul li .content .ewm p button,.g-repayment ul li .warn li .copy_btn{color:#EE6839;font-size:16px;border:none;background:0 0;margin-left:10px}
        .g-repayment ul li .warn{padding:5px 15px 20px}
        .g-repayment ul li .warn li{font-size:14px;color:#777;line-height:1.8}
        .g-repayment ul li .warn li i{color:#db3900;font-style:normal;font-size:16px;font-weight:normal;}
        .g-repayment ul li .content .ewm p .copy_btn,.g-repayment ul li .warn li .copy_btn{background: #FF6464; border-radius: 4px;color: #fff;width: 56px; height: 27px;    border: none;}
        #card_num {font-size: 16px; color: #151515; font-weight: 500;}
        .g-repayment ul li .content .method-type>p{font-size:14px;padding:0 15px;color:#595959;line-height:23px}
        .g-repayment ul li .content .method-type .gzh{text-align:center;margin-top:15px}
        .g-repayment ul li .content .method-type .gzh img{width:80%}
        .g-repayment ul li a.call-tel{display:inline-block;font-size:0;padding:0;background:0 0;vertical-align:-10px;margin:10px 1px 10px 6px}
        .g-repayment ul li a.call-tel img{width:35px}
        /*  2018/4/8 */
        .g-repayment ul li .qq_spred{margin-right:3px;display:inline-block;background:0 0;padding:0 3px;color:#db3900;font-size:16px;font-weight:normal;}
        .g-repayment ul li .qq_spred img{width:35px;vertical-align:-10px;margin-left:-6px;margin-right:-4px}
        .g-repayment > ul > li > .content > .warn > .onlineChat{
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            align-items: center;
        }
        .g-repayment > ul > li > .content > .warn > .onlineChat > .iconForChat{
            margin: 0px 8px;
            padding: 0;
            background: 0 0;
        }
        .g-repayment > ul > li > .content > .warn > .onlineChat > .iconForChat img{
            width: 35px;
            height: 35px;
        }

        .ui-btn{
            margin:0;
            padding:0;
            display:inline-block;
            font-weight: normal; !important;
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
        .error-popop{font-size:16px;display:none;text-align:center;position: fixed;top: 50%;left: 50%;transform: translate(-50%,-50%);z-index:100;color:#fff;background:rgba(0,0,0,.6);width:65%;padding:0 20px;height:45px;line-height:45px;border-radius:55px}



    </style>
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
</head>
<body style="background-color: #eeeeee;">

<div data-role="page">
    <section class="g-repayment">
        <h5 style="display: none;">更多优惠活动、还款方式请关注“小鱼儿”微信公众号</h5>
        <ul>
            <!--<li>
                <a class="f-cb nav" href="javascript:;">
                    <img style="margin-top: 5px;" class="sign fl" src="${basePath}/images/icon-bank-card.png" alt="">
                    <span class="fl">银行卡转账</span>
                    <img class="fr direction" src="${basePath}/images/icon-down.png" alt="">
                </a>
                <div class="content">
                    <div class="ewm bank_desc">
                        <p>户名：鸾鸟网络科技（上海）有限公司</p>
                        <p>账号：<span class="content_num" id="card_num">31050180363700000448</span><button id="copy_card_num" class="copy_btn" href="javascript:;">复制</button></p>
                        <p>开户行：中国建设银行上海市分行松江新城支行</p>
                    </div>
                    <ul class="warn">
                        <li>1、转账时请务必备注“<i>姓名+手机号</i>”，方便客服妹妹及时帮您核实处理。</li>
                        <li>2、客服热线:&nbsp;&nbsp;<i class="tel-service" onclick="service_tel()">0571-28079313</i><a class="call-tel tel-service" onclick="service_tel()" href="javascript:;"><img src="${basePath}/images/ic_phone.png" alt=""></a>（9:30～22:00）</li>
                        <%--<li>3、QQ客服：<span style="margin-right: 3px;">2583310643</span><button id="copy_qq2_num" class="copy_btn" href="javascript:;">复制</button>（9:00～24:00）</li>--%>
                        <li class="qqChat">3、QQ客服:&nbsp;&nbsp;<a class="qq_spred" onclick="service_qq()" href="javascript:;">2583310643</a>
                            <a class="qq_spred" onclick="service_qq()" href="javascript:;"><img src="${basePath}/images/ic_qq.png" alt=""></a>（9:00～24:00）
                        </li>
                        <li class="onlineChat">
                            <span>3、在线客服:</span>
                            <a class="iconForChat" onclick="qimoChatClick();">
                                <img src="${basePath}/images/ic_chat.png" alt="">
                            </a>
                            <span>工作时间（9:00～24:00）</span>
                        </li>
                    </ul>
                </div>
            </li>-->
            <li>
                <a class="f-cb nav z-active" href="javascript:;">
                    <img class="sign fl" src="${basePath}/images/icon-alipay.png" alt="">
                    <span class="fl">支付宝还款</span>
                    <img class="fr direction active" src="${basePath}/images/icon-down.png" alt="">
                </a>
                <div class="content" style="display: block;">
                    <div class="ewm">
                        <img src="${qr_code}" alt="">
                        <p>支付宝账号<br/></p>
                        <p><span class="content_num" id="content">${pay_account}</span><button class="copy_btn" id="copy" href="javascript:;">复制</button></p>
                        <%--<p class="company_txt"><p class="company_txt"><span>鸾鸟网络科技(上海)有限公司</span><button class="copy_btn" id="copy_company_txt" href="javascript:;">复制</button></p ></p>--%>
                        <p class="company_txt">收款人姓名:<span>${account_name}</span><button class="copy_btn" id="copy_company_txt" href="javascript:;">复制</button></p>
                    </div>
                    <ul class="warn">
                        <li>1、转账时请务必备注“<i>姓名+手机号</i>”，方便客服妹妹及时帮您核实处理。</li>
                        <li>2、客服热线:&nbsp;&nbsp;<i  class="tel-service" onclick="service_tel()">${service_phone}</i><a class="call-tel tel-service" onclick="service_tel()" href="javascript:;"><img src="${basePath}/images/ic_phone.png" alt=""></a>（${tel_time}）</li>
                        <%--<li>3、QQ客服：<span style="margin-right: 3px;">2583310643</span><button id="copy_qq1_num" class="copy_btn" href="javascript:;">复制</button>（9:00～24:00）</li>--%>
                        <li class="qqChat">3、QQ客服:&nbsp;&nbsp;<a class="qq_spred" onclick="service_qq()" href="javascript:;">${services_qq}</a>
                            <a class="qq_spred" onclick="service_qq()" href="javascript:;"><img src="${basePath}/images/ic_qq.png" alt=""></a>（${online_time}）
                        </li>
                        <li class="onlineChat">
                            <span>3、在线客服:</span>
                            <a class="iconForChat" onclick="qimoChatClick();">
                                <img src="${basePath}/images/ic_chat.png" alt="">
                            </a>
                            <span>工作时间（${online_time}）</span>
                        </li>
                    </ul>
                </div>
            </li>
        </ul>
    </section>
    <div class="error-popop"></div>
</div>


<script type="text/javascript">
    // slideToggle
    var _nav_click = $('.g-repayment ul li .nav');
    _nav_click.on("click",function () {
        if($(this).hasClass('z-active')){
            $(this).removeClass('z-active');
            $(this).parent().find('.content').slideUp();
            $(this).find('.direction').removeClass('active');
        }else {
            $(this).addClass('z-active');
            $(this).siblings('.content').slideDown();
            $(this).find('.direction').addClass('active');
        }
    });
    $(document).ready(function () {
        jQuery.mobile.loading('show', {
            text: '加载中', // 加载器中显示的文字
            textVisible: true, // 是否显示文字
            theme: 'b',        // 加载器主题样式a-e
            textonly: false,   // 是否只显示文字
            html: ""           // 要显示的html内容，如图片等
        });
        // 如下部分是安卓上线在线聊天注释
        // var u = navigator.userAgent;
        // var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        // if(isIOS){
            $(".qqChat").css("display","none");
        // }else {
        //     $(".onlineChat").css("display","none");
        // }
    });

    window.onload=function(){
        $.mobile.loading('hide');
    }

    // 唤起客服QQ
    function service_qq() {
        //window.nativeMethod.callQQ(qq_num);
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        if (isAndroid) {
            window.nativeMethod.callQQ('${services_qq}');
        }else if(isIOS){
            window.location.href = "http://wpa.qq.com/msgrd?v=3&uin=${services_qq}&site=qq&menu=yes";
        }
    }
    function brower_info() {
        var ua = navigator.userAgent;
        var brower = {
            is_weixin: function () {//是否是微信
                return ua.toLowerCase().match(/MicroMessenger/i) == "micromessenger" ? true : false;
            },
            is_android: function () {//是否是安卓终端
                return /android/.test(ua.toLowerCase()) ? true : false;
            },
            is_iPhone: function () {//是否是苹果终端
                return /iphone|ipad|ipod/.test(ua.toLowerCase()) ? true : false;
            }
        };
        return brower;
    };
    // 唤起客服聊天
    function qimoChatClick(){
        var browers = brower_info();
        if (browers.is_android()) {
            var mobile = getUrlParams('mobilePhone');
            if(mobile){
                window.nativeMethod.onlineService();
            }else{
                doRedirect();
            }
        } else if (browers.is_iPhone()) {
            doRedirect();
        } else {
            doRedirect();
        }
    }

    function showLoader(obj,msg,second) {
        var popup_box = obj;
        popup_box.text(msg);
        popup_box.fadeIn();
        setTimeout(hideLoader(obj,second),second)
    }
    function hideLoader(hideObj,second) {
        hideObj.fadeOut(second);
    }

    function doRedirect(){
        var url = 'https://webchat.7moor.com/wapchat.html?accessId=e48aabd0-5755-11e8-9ced-e7fc175a7ab5&fromUrl=api.jx-money&urlTitle=%E6%9D%A5%E8%87%AA%E5%80%9F%E4%BA%AB%E9%92%B1%E5%8C%85';
        var mobile = getUrlParams('mobilePhone');
        mobile = mobile ? mobile : Math.floor(Math.random() * 10000000);
        var params = '&clientId=' + mobile + '';
        window.location.href = url + params;
    }

    function getUrlParams(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
    // 唤起电话
    function service_tel(){
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
        if (isAndroid) {
            nativeMethod.callPhoneMethod("${service_phone}");
        }else if(isIOS){
            window.location.href = "tel:${service_phone}";
        }
    }

    // 点击复制文字
    function copyArticle(event) {
        const range = document.createRange();
        const target = event.target;
        range.selectNode(target.previousSibling);
        const selection = window.getSelection();
        if(selection.rangeCount > 0) selection.removeAllRanges();
        selection.addRange(range);
        if(document.execCommand('copy', false, null)){
            $.mvalidateTip("复制成功！")
        } else{
            $.mvalidateTip("复制失败！")
        }
    }
    // 点击复制支付宝账号
    var _copy = document.getElementById('copy');
    _copy.addEventListener('click',copyArticle);

    // 复制银行卡号
//    var _copy_card_num= document.getElementById('copy_card_num');
//    _copy_card_num.addEventListener('click',copyArticle);
    // 复制QQ号
//    var _copy_qq1_num= document.getElementById('copy_qq1_num');
//    _copy_qq1_num.addEventListener('click',copyArticle);
//    var _copy_qq2_num= document.getElementById('copy_qq2_num');
//    _copy_qq2_num.addEventListener('click',copyArticle);
    // 复制公司户名
    var _copy = document.getElementById('copy_company_txt');
    _copy.addEventListener('click',copyArticle);
</script>
</body>
</html>