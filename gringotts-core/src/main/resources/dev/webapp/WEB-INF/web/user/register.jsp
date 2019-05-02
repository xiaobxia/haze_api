<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web";
    String staticBasePath = "https://static.jx-money.com/common/web";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:set var="staticBasePath" value="<%=staticBasePath%>"></c:set>
    <c:set var="basePath" value="<%=basePath%>"></c:set>
    <c:set var="path" value="<%=path%>"></c:set>
    <title>注册-小鱼儿</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <script type="text/javascript" src="${staticBasePath}/js/jquery-1.9.1.min.js"></script>
    <script src="//captcha.luosimao.com/static/js/api.js"></script>
    <style>
        body,h4,html,i,li,p,ul,input{list-style:none;font-family:Arial,Helvetica,sans-serif;font-size:1rem;border:none;}
        *{margin:0;padding:0;-webkit-user-select:auto;outline:none;}
        input,textarea,button,a {outline: none;-webkit-appearance: none;-webkit-tap-highlight-color: rgba(0, 0, 0, 0);}
        .regist-bg{background:url(${basePath}/zmxy/images/bg_register.png) top center;background-repeat: no-repeat;background-size: cover;min-height: 100vh;}
        .regist-bg .register{background-color:#fff;border-radius:10px;padding:20px 30px;}
        .regist-bg .banner-txt{margin:0 0 0;padding-top:7%}
        .regist-bg .banner-txt p{font-size:26px;text-align:center;color:#fff;letter-spacing:.8px}
        .regist-bg .banner-txt p span{font-size:45px;color:#ffcf00}
        .regist-bg .banner-txt p font{color:#ffcf00}
        .regist-bg .banner-desc{margin: 35px 0 15px;}
        .regist-bg .banner-desc p{font-size:18px;color:#fff;text-align:center;line-height:36px}
        .regist-bg .banner-desc p span {font-size: 21px;margin: 0 2px;}
        .regist-bg .register li{position:relative;border:1px solid #c4c4c4;box-shadow:none;border-radius:2px;padding:0 15px;font-size: 16px;}
        .regist-bg .register li:not(:last-child) {margin-bottom:16px;}
        .regist-bg .register li input::-webkit-input-placeholder {color: #bbb;}
        .regist-bg .register .com-btn{background:#ff7700;display:block;width:200px;height:44px;color:#fff;font-size:16px;line-height:44px;text-align:center;text-decoration:none;border-radius:35px;margin: 0 auto 15px;}
        .regist-bg .register li input{font-size:14px;font-weight: 300;padding:0;height:45px;line-height:45px;display:block;width:100%}
        .regist-bg .register li .pst00{position:absolute;top:0;right:0}
        .regist-bg .register li .gain-yzm{font-size:16px;color:#FF1400;line-height:45px;right:10px;cursor: pointer;}
        .regist-bg .register li .captcha-pic{width:128px;height:auto;position:absolute;top:0;right:0}
        .regist-bg .register .odds{font-size:0;text-align:center;display:table;width: 100%;}
        .regist-bg .register .odds>li{display:table-cell;border:none;width:24%;padding:0}
        .regist-bg .register .odds>li:not(:last-child){margin-right:2rem}
        .regist-bg .register .odds>li img{width:85px;margin:0 auto}
        p.beizhu{text-align:left;color:#BBB;font-size:12px;color: #b1b1b1;}
        .regist-bg .wrapper-bg{padding: 8px 15px 25px;}
        /*.error-popop{font-size:16px;display:none;text-align:center;position:absolute;top:0;left:0;bottom:0;right:0;margin:auto;z-index:100;color:#fff;background:rgba(0,0,0,.6);max-width:65%;padding:0 20px;height:45px;line-height:45px;border-radius:55px}*/
        .error-popop{font-size:16px;display:none;text-align:center;position: fixed;top: 50%;left: 50%;transform: translate(-50%,-50%);z-index:100;color:#fff;background:rgba(0,0,0,.6);width:65%;padding:0 20px;height:45px;line-height:45px;border-radius:55px}
        .loading-popop{font-size:16px;display:none;text-align:center;position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);padding:0 10px;z-index:100;color:#fff;background:rgba(0,0,0,.6);width:30%;padding:0 10px;height:60px;line-height:60px;border-radius:8px}
        .registered-popup{display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,.4);z-index:10}
        .registered-popup .be-registered{position:fixed;top:50%;left:50%;transform:translate(-50%,-50%);padding:10% 10px 7%;z-index:100;background:rgba(0,0,0,.8);text-align:center;color:#fff;width:60%;border-radius:12px}
        .registered-popup .be-registered .data-msg{margin-bottom:40px}
        .registered-popup .be-registered a{display:block;height:30px;line-height:29px;margin:15px auto;color:#fff;text-decoration:none;padding:5px;background-image:linear-gradient(-180deg,#FE9938 0,#FD4349 100%);border-radius:25px;width:55%;width:150px}
        .registered-popup .be-registered .data-msg,.registered-popup .be-registered a{font-size:16px}
        .registered-popup .be-registered button{background:0 0;border:none;position:absolute;top:0;right:0;cursor: pointer;}
        .registered-popup .be-registered button img{width:36px;border-radius:50%}
        .registered-popup .be-registered .type{font-size:14px;color:#fff;opacity:.6;text-align:center}
        .none_code{padding: 15px 0;font-size: 12px;color: #FF6400;text-align: center;cursor: pointer;}
        .voice-popup-shadow {display: none;position: fixed;top: 0;left: 0;width: 100%;height: 100%;background: rgba(0,0,0,.5)}
        .voice-popup{display:none;position:fixed;top:50%;left:50%;width:275px;height:150px;background:#fff;padding:10px;border-radius:10px;margin:-75px 0 0 -138px;text-align:center;z-index:10}
        .voice-popup p{padding:43px 0;font-size:17px;color:#333}
        .voice-popup .btn-box{font-size:0}
        .voice-popup .btn-box a{display:inline-block;width:120px;line-height:39px;font-size:17px;color:#fff;text-decoration:none}
        .voice-popup .btn-box a:first-child{margin-right:15px}
        .voice-popup .btn-box .btn-cancle{background:#aaa;border-radius:50px}
        .voice-popup .btn-box .btn-sure{background:#fe8a3b;border-radius:50px}
        .banner-pic .banner-logo {width: 80%;margin: 0.3rem auto 0;}
        .banner-pic img {display: block;}
        .banner-pic .text-center{font-size: 0;margin-top: 25px;}
        .banner-pic .text-center .img{margin: 0 auto;}
        .banner-pic .text-center .wangyuan {width: 306px;}
        .banner-pic .text-center .fast-time {width: 197px;margin: 6px auto 25px;}
        .banner-pic .text-center .banner-detail {width: 251px;}
        .insure-box p, .insure-box .moren{font-size: 12px;color: #666; line-height: 21px;}
        .insure-box .icon-select {width: 18px;height: 18px;display: inline-block;background: #666;cursor:pointer;border-radius: 6px;vertical-align: middle;margin-right: 3px;}
        .insure-box .icon-select.selected {background: transparent url(${basePath}/zmxy/images/ic_selected.png) no-repeat;background-size: 100%;}
        .insure-box p a {color:#E93326;text-decoration: none;}
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
    <script>
        var initTime = new Date();
        var _czc = _czc || [];
        _czc.push(["_setAccount", "1271438590"]);
    </script>
</head>
<body class="regist-bg" style="height: 100%;">
<form method="POST" id="registerForm" class="ui-mobile-viewport ui-overlay-a">
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page" tabindex="0">
        <!--<div class="banner-txt">
            <p>最高<span class="money-num">10000</span>元<font class="small">现金</font>到账</p>
            <p>最快<span class="money-num">3</span>分钟<font class="small">放款</font></p>
        </div>
        <div class="banner-desc">
            <p>门槛低、审核快</p>
            <p>凭<span>身份证</span>就能完成借款</p>
        </div>-->
        <div class="banner-pic">
            <img class="banner-logo" src="${basePath}/zmxy/images/jx-banner.png" alt="">
            <!--<div class="text-center">
                <img class="img wangyuan" src="${basePath}/zmxy/images/register1.png" alt="">
                <img class="img fast-time" src="${basePath}/zmxy/images/register2.png" alt="">
                <img class="img banner-detail" src="${basePath}/zmxy/images/register3.png" alt="">
            </div>-->
        </div>
        <div class="wrapper wrapper-bg" style="background-color: transparent">
            <div class="register-wrapper">
                <div class="register">
                    <ul class="info-list">
                        <li>
                            <input type="text" placeholder="注册手机号" onfocus="trackEvenUserPhone()" data-role="none" name="userPhone" id="userPhone">
                        </li>
                        <!--<li style="padding: 0;">
                            <%--<input type="hidden" name="RCaptchaKey" id="RCaptchaKey" value="${RCaptchaKey}">--%>
                            <%--<input type="text" placeholder="请输入图形验证码" onclick="trackEvenCaptcha()" data-role="none" name="captcha" id="captcha" style="width: 50%">--%>
                            <%--<img id="imgCap" class="pst00 captcha-pic" src="<%=path %>/captcha.svl?RCaptchaKey=${RCaptchaKey}" onclick="this.src='<%=request.getContextPath() %>/captcha.svl?RCaptchaKey=${RCaptchaKey}&d='+new Date()*1" valign="bottom" alt="点击更新" title="点击更新">&ndash;%&gt;--%>
                            <div class="l-captcha" data-site-key="909f06dfef67bd8309dc1b5bdca5ff7f" data-width="100%" data-callback="getResponse"></div>
                        </li>-->
                        <li>
                            <input type="text" placeholder="收到的验证码" data-role="none" name="smsCode" id="smsCode">
                            <div class="gain-yzm pst00" id="sendcode">获取验证码</div>
                        </li>
                        <li>
                            <input type="password" placeholder="设置密码" data-role="none" name="passWord" id="passWord">
                        </li>
                    </ul>
                    <input type="hidden" placeholder="邀请码" data-role="none" name="invite_code" id="invite_code" value="${invite_code}">

                    <input type="hidden" placeholder="来源" data-role="none" name="user_from" id="user_from" value="${user_from}">
                    <input type="hidden" data-role="none" name="token" id="token" value="${token}">
                    <p id="voice-code" class="none_code">获取不到验证码？</p>
                    <p style="display:none;" >注册即同意<a rel="external" href="${path}/act/light-loan-xjx/agreement.do?appName=${appKey}" class="ui-link">《小鱼儿注册协议》</a>
                        <a rel="external" href="${path}/agreement/creditExtension.do?appName=${appKey}" class="ui-link">《信用授权协议》</a></p>
                    <a rel="external" id="registerClick" onclick="nextStep()" href="javascript:;" class="com-btn ui-link">注册即可领取</a>
                    <p class="beizhu">*注：注册下载小鱼儿app后即可申请领取</p>
                    <!-- <ul class="odds">
                        <li><img src="${basePath}/images/pic_01.png" alt=""></li>
                        <li><img src="${basePath}/images/pic_02.png" alt=""></li>
                        <li><img src="${basePath}/images/pic_03.png" alt=""></li>
                    </ul> -->
                    <!-- 注册送保险 -->
                    <!-- <div class="insure-box">
                        <div class="moren"><span class="icon-select selected"></span>注册审核通过有礼：送最高100万意外保保障，平安出行！</div>
                        <p>本人同意领取免费险，本人同意中国平安后续致电联系确认保险产品相关事宜，查看<a href="http://www.jx-money.com/ins/insureAgreement.html">投保须知</a>及<a href="http://www.jx-money.com/ins/insureInfoSafe.html">信息安全条款</a></p>
                        <p>备注：如需赠送保险，请用本人名下的手机号码注册。</p>
                        <p><a href="http://www.jx-money.com/ins/insureArea.html">（符合赠送保险区域查看）</a></p>
                    </div> -->
                </div>
            </div>
            <!-- <a onclick="downxjxApp();" class="download-app"><u>已有帐号 去下载APP</u></a> -->
            <!-- <div class="more-info1" style="display:none;" >
                <h3>简单的借款流程</h3>
                <img src="${basePath}/images/c_01.png" alt="">
                <h3>小鱼儿优势</h3>
                <p>1、无需抵押，无需担保，纯信用线上借贷。</p>
                <p>2、五分钟资料填写，最快三分钟到账。</p>
                <p>3、银行级数据保护，为您保驾护航。</p>
                <p>小提示：真实的资料信息有助于您快速成功借款。</p>
            </div> -->
        </div>
        <!-- main end -->
    </div>
    <%--<div style="display: none" class="ui-loader ui-corner-all ui-body-a ui-loader-default"><div style="display: none"><span class="ui-icon-loading"></span><h1>loading</h1></div></div>--%>
</form>

<!--  用户输入错误提示弹层 -->
<div class="error-popop"></div>
<!--  加载中 -->
<div class="loading-popop"></div>
<!--  手机号已被注册，引导用户下载app -->
<div class="registered-popup">
    <div class="be-registered">
        <p class="data-msg">该手机号已被注册</p>
        <a id="download_btn" href="javascript:;">立即下载App</a>
        <p class="type" id="android_msg" style="display: none">适用于Android设备</p>
        <p class="type" id="ios_msg" style="display: none">适用于Ios设备</p>
        <p class="type" id="other_msg" style="display: none">请在安卓或苹果手机下载</p>
        <button type="button" value="x" id="registed-close"><img src="${basePath}/images/a_17.png" alt=""></button>
    </div>
</div>
<!-- 是否获取语音验证码 -->
<div class="voice-popup">
    <p>是否获取语音验证码？</p>
    <div class="btn-box">
        <a href="javascript:;" onclick="hideVoicePopup();" class="btn-cancle">取消</a>
        <a id="sendVoicecode" href="javascript:;" class="btn-sure">确定</a>
    </div>
</div>
<div class="voice-popup-shadow"></div>

<!--统计代码-->
<style type="text/css">
    #cnzz_stat_icon_1271438488 {
        display: none;
    }
    #cnzz_stat_icon_1271438590 {
        display: none;
    }
</style>
<!--<script type="text/javascript">
    var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
    document.write(unescape("%3Cspan id='cnzz_stat_icon_1271438488'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s13.cnzz.com/z_stat.php%3Fid%3D1271438488' type='text/javascript'%3E%3C/script%3E"));
</script>-->
<!--小鱼儿新域名对应cnzz-->
<script type="text/javascript">
    var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
    document.write(unescape("%3Cspan id='cnzz_stat_icon_1271438590'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s19.cnzz.com/z_stat.php%3Fid%3D1271438590' type='text/javascript'%3E%3C/script%3E"));
</script>

<%--<script type="text/javascript">--%>
<%--var _hmt = _hmt || [];--%>
<%--(function() {--%>
<%--var hm1 = document.createElement("script");--%>
<%--hm1.src = "https://hm.baidu.com/hm.js?5c79fcb203ee23ebb7cb81acde97ee08";--%>
<%--var s1 = document.getElementsByTagName("script")[0];--%>
<%--s1.parentNode.insertBefore(hm1, s1);--%>
<%--})();--%>
<%--</script>--%>
<%--<script type="text/javascript">--%>
<%--var _hmt = _hmt || [];--%>
<%--(function() {--%>
<%--var hm2 = document.createElement("script");--%>
<%--hm2.src = "https://hm.baidu.com/hm.js?ea3785a12b2e420ec891dd0c2075467a";--%>
<%--var s2 = document.getElementsByTagName("script")[0];--%>
<%--s2.parentNode.insertBefore(hm2, s2);--%>
<%--})();--%>
<%--</script>--%>
<script type="text/javascript">
    var apply_ins = 1;
    function trackEvenUserPhone(){
        var nowTime = new Date();
        _czc.push(['_trackEvent', '注册页面', '点击', '输入手机号', (nowTime.getTime() - initTime.getTime())/1000, 'userPhone']);
    }

    function trackEvenCaptcha(){
        var nowTime = new Date();
        _czc.push(['_trackEvent', '注册页面', '点击', '选择图片验证码', (nowTime.getTime() - initTime.getTime())/1000, 'captcha']);
    }

    //手机号码正则表达式
    var phonePattern = /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^(([0\+]\d{2,3})?(0\d{2,3}))(\d{7,8})((\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;

    //判断变量是否为空
    function checkVar(param){
        if(param=='' || param==undefined || param==null){
            return false;
        }
        return true;
    }
    //判断浏览器类型：1、Android 2、ios 3、pc
    function borrowInfo() {
        var ua = navigator.userAgent;
        if(/android/.test(ua.toLowerCase())){
            return "1";
        }
        if(/iphone|ipad|ipod/.test(ua.toLowerCase())){
            return "2";
        }
        return "3"
    }

    function baseAjax(urlStr,dataParm,callback,asyncFlag){
        if(asyncFlag == undefined || asyncFlag == null){
            asyncFlag = true;
        }
        $.ajax({
            type: "POST",
            dataType: 'json',
            timeout: 30000,
            data:dataParm,
            url: urlStr,
            async : asyncFlag,
            /*beforeSend: function () {
                showLoader($(".error-popop"),"加载中……")
                hideLoader($(".error-popop"));
            },
//             complete:function(){
//                 hideLoader();
//             },*/
            success: function(data) {
                hideLoader($(".error-popop"));
                callback(data);
            },
            error: function() {

                showLoader($(".error-popop"),"加载失败！",800);
                hideLoader($(".error-popop"),800);
            }
        });
    }
    //显示加载器
    function showLoader(obj,msg,second) {
        var popup_box = obj;
        popup_box.text(msg);
        popup_box.fadeIn();
        setTimeout(hideLoader(obj,second),second)
    }
    function hideLoader(hideObj,second) {
        hideObj.fadeOut(second);
    }

    function nextStep(){//点击注册的时候
        var nowTime = new Date();
        _czc.push(['_trackEvent', '注册页面', '点击', '注册', (nowTime.getTime() - initTime.getTime())/1000, 'registerClick']);
        var userPhone =	$("#userPhone").val();
        var smsCode =	$("#smsCode").val();
        var passWord = $("#passWord").val();
        var user_from = $("#user_from").val();
        var invite_code=$("#invite_code").val();
       /* if(!$('.icon-select').hasClass('selected')) {
            apply_ins =0;   // 如果用户没有选择保险赋值为0
        }*/
        apply_ins =0;
        var token=$("#token").val();
        var brower_type = borrowInfo();//手机浏览器类型（1Android、2ios、4pc）
        // var validateCode = $("input[name='luotest_response']").val();
        var validateCode = 0;
        if(!checkVar(userPhone)){
            showLoader($(".error-popop"),'请输入手机号码',800);
            LUOCAPTCHA.reset();
            return;
        }else if(phonePattern.test(userPhone) == false){
            showLoader($(".error-popop"),'手机号格式不对',800);
            LUOCAPTCHA.reset();
            return;
        }else if(!checkVar(passWord)){
            showLoader($(".error-popop"),'请输入密码',800);
            LUOCAPTCHA.reset();
            return;
        }else if(!checkVar(smsCode)){
            showLoader($(".error-popop"),'请输入手机验证码',800);
            LUOCAPTCHA.reset();
            return;
        }else if(passWord.length<6){
            showLoader($(".error-popop"),'密码必须大于等于6位!',800);
            LUOCAPTCHA.reset();
            return;
        }
        var url = "${path}/act/light-loan-xjx/register";
        var param = {
            phone:userPhone,
            code:smsCode,
            password:passWord,
            invite_code:invite_code,
            user_from:user_from,
            token:token,
            brower_type:brower_type,
            apply_ins: apply_ins
        };
        $.ajax({
            type : "POST",
            async:false,
            url : url,
            data : param,
            dataType : "json",
            success : function(ret) {
                if (ret.code == '0') {
                    showLoader($(".error-popop"),"注册成功",800);
                    setTimeout(function(){window.location.href='${path}/act/light-loan-xjx/succ';}, 1 * 1000 );
                } else {
                    $("#password").val("");//密码置为空
                    showLoader($(".error-popop"),ret.message,800);
                    LUOCAPTCHA.reset();
                }
            }
        });
    }
    // 人机验证回调方法
    function getResponse(resp){}

    function hideVoicePopup() {
        $('.voice-popup-shadow').hide();
        $('.voice-popup').hide();
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
    $(document).ready(function() {
        // 该手机号已被注册，引导用户下载APP
        $('body').on('click','#registed-close', function () {
            $(this).parents('.registered-popup').fadeOut();
        });
        var browers = brower_info();
        var downBtn = $('#download_btn');
        var wechatPop = $('.wechat-pop');
        // 安卓用户微信打开提示到浏览器下载
        if (browers.is_android()) {
            if (browers.is_weixin()) {
                $("#android_msg").show();
                downBtn.on('click', function () {
                    window.location.href = 'http://download.jx-money.com/apps/jxmoney.apk';
                });
            } else {
                $("#android_msg").show();
                downBtn.on('click', function () {
                    window.location.href = 'http://download.jx-money.com/apps/jxmoney.apk';
                });
            }
        } else if (browers.is_iPhone()) {
            $("#ios_msg").show();
            downBtn.on('click', function () {
                window.location.href = "http://download.jx-money.com/";
            });

        } else {
            $('#download_btn').addClass('jqqd').text('温馨提示');
            $('#other_msg').show();
            downBtn.on('click', function () {
                window.location.href = "http://download.jx-money.com/";
            })
        }
        
        $('.icon-select').on('click',function () {
            if($(this).hasClass('selected')) {
                $(this).removeClass('selected');
                apply_ins = 0;
            }else {
                $(this).addClass('selected');
                apply_ins = 1;
            }
        })

        //倒计时
        var wait=60;
        var voiceWait = 60;
        $('#sendcode').click(function(event) {
            var nowTime = new Date();
            _czc.push(["_trackEvent", "注册页面", "点击", "发送短信验证码", (nowTime.getTime() - initTime.getTime())/1000, "sendcode"]);
            var phone =	$("#userPhone").val();
            var captcha =	$("#captcha").val();
            var RCaptchaKey =	$("#RCaptchaKey").val();
            // var validateCode = $("input[name='luotest_response']").val();
            var validateCode = 0;
            if (wait != 60) {return;}
            if(!checkVar(phone)){
                showLoader($(".error-popop"),'请输入手机号码',800);
                LUOCAPTCHA.reset();
                return;
            }else if(!phonePattern.test(phone)){
                showLoader($(".error-popop"),'手机号格式不对',800);
                LUOCAPTCHA.reset();
                return;
            }
            /*else if(!checkVar(validateCode)) {
                showLoader($(".error-popop"),'请先进行人机验证！',800);
            }*/
            else {
                var data = {};
                data.phone = phone;
                baseAjax('${path}/act/light-loan-xjx/registerCode?phone='+phone+'&RCaptchaKey='+RCaptchaKey+'&captcha='+captcha+'&validateCode='+validateCode, data, checksendSmsCallBack);
            }
        });

        function checksendSmsCallBack(data){
            if (data.code == '0') {
                showLoader($(".error-popop"),"短信已发送",800);
                time();
            } else{
                if( data.message == '手机号码已被注册' ) {
                    LUOCAPTCHA.reset();
//                    $("#imgCap").trigger("click");
                    $('.registered-popup').fadeIn();
                    $('.registered-popup').find('.data-msg').text(data.message)
                }else{
                    showLoader($(".error-popop"),data.message,800);
                }
                LUOCAPTCHA.reset();
            }
        }
        function time(){
            if (wait==0) {
                $('#sendcode').text('获取验证码');
                wait=60;
            }else{
                $('#sendcode').text(wait+'秒后重试');
                wait--;
                setTimeout(function(){time();}, 1000);
            }
        }

        /* 获取不到验证码，语音验证 -start*/
        $('.voice-popup-shadow').on('click',function () {
            $(this).hide();
            $('.voice-popup').hide();
        });
        function showVoicePopup() {
            $('.voice-popup-shadow').fadeIn();
            $('.voice-popup').fadeIn();
        }
        $('#voice-code').click(function(event) {
            var nowTime = new Date();
            _czc.push(["_trackEvent", "注册页面", "点击", "发送语音验证码", (nowTime.getTime() - initTime.getTime())/1000, "sendcode"]);
            var phone =	$("#userPhone").val();
            // var validateCode = $("input[name='luotest_response']").val();
            var validateCode = 0;
            if (voiceWait != 60) {return;}
            if(!checkVar(phone)){
                showLoader($(".error-popop"),'请输入手机号码',800);
                LUOCAPTCHA.reset();
                return;
            }else if(!phonePattern.test(phone)){
                showLoader($(".error-popop"),'手机号格式不对',800);
                LUOCAPTCHA.reset();
                return;
            }
            /*else if(!checkVar(validateCode)) {
                showLoader($(".error-popop"),'请先进行人机验证！',800);
            }*/
            else {
                showVoicePopup();
            }
        });
        $('#sendVoicecode').click(function () {
            hideVoicePopup();
            var phone =	$("#userPhone").val();
            var captcha =	$("#captcha").val();
            var RCaptchaKey =	$("#RCaptchaKey").val();
            // var validateCode = $("input[name='luotest_response']").val();
            var validateCode = 0;
            if (voiceWait != 60) {return;}
            var data = {};
            data.phone = phone;
            baseAjax('${path}/act/light-loan-xjx/getsmsvoicecode?phone='+phone+'&RCaptchaKey='+RCaptchaKey+'&captcha='+captcha+'&validateCode='+validateCode, data, checksendSmsVoiceCallBack);
        });
        function checksendSmsVoiceCallBack(data) {
            if (data.code == '0') {
                showLoader($(".error-popop"), data.message, 2000);
            } else {
                if (data.message == '手机号码已被注册') {
                    LUOCAPTCHA.reset();
                    $('.registered-popup').fadeIn();
                    $('.registered-popup').find('.data-msg').text(data.message)
                } else {
                    LUOCAPTCHA.reset();
                    showLoader($(".error-popop"), data.message, 2000);
                }
            }
        }
            /*function voiceTime(){
                if (wait==0) {
                    $('#sendVoicecode').text('获取验证码');
                    wait=60;
                }else{
                    showLoader($(".error-popop"),wait+'秒后重试',1200);
                    wait--;
                    setTimeout(function(){voiceTime();}, 1000);
                }
            }*/
            /* 获取不到验证码，语音验证 -end*/

    });
</script>

</body>

</html>
