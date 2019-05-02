<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web";
%>
<!DOCTYPE html>
<html lang="en">
<head>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />

    <script type="text/javascript" src="${basePath}/js/base.js"></script>
    <script type="text/javascript" src="${basePath}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/zmxy/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/common.js"></script>
    <script type="text/javascript">
        function downxjxApp(){
            location="http://a.app.qq.com/o/simple.jsp?pkgname=com.innext.xjx";
        }
    </script>
</head>
<body>
<form  method="POST" id="registerForm">
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page" >
        <!-- main start -->
        <div class="wrapper wrapper-bg">
            <div class="register">
                <ul class="info-list">
                    <li>
                        <input type="text" placeholder="注册手机号"  data-role="none" name="userPhone" id="userPhone">
                    </li>
                    <li>
                        <input type="hidden" name="RCaptchaKey" id="RCaptchaKey" value="${RCaptchaKey}"/>
                        <input type="text" placeholder="请输入图形验证码" data-role="none" name="captcha" id="captcha" style="width: 50%">
                        <img id="imgCap" style="  width: 4.5rem; height: 2rem;float: right;margin-right: -0.9rem;" src="<%=path %>/captcha.svl?RCaptchaKey=${RCaptchaKey}" onclick="this.src='<%=request.getContextPath() %>/captcha.svl?RCaptchaKey=${RCaptchaKey}&d='+new Date()*1" valign="bottom" alt="点击更新" title="点击更新" />

                    </li>
                    <li>
                        <input type="password" placeholder="设置密码" data-role="none" name="passWord" id="passWord">
                    </li>
                    <li>
                        <input type="text" placeholder="收到的验证码" data-role="none" style="width:8.25rem;" name="smsCode" id="smsCode">
                        <div class="gain-yzm" id="sendcode">获取验证码</div>
                    </li>
                </ul>
                <input type="hidden" placeholder="邀请码" data-role="none" name="invite_code" id="invite_code" value="${pushId}">
                <input type="hidden" placeholder="来源" data-role="none" name="user_from"  id="user_from" value="${user_from}">
                <input type="hidden" placeholder="pushId" data-role="none" name="pushId"  id="pushId" value="${pushId}">
                <a rel="external" href="#" onclick="nextStep();"  class="com-btn">立即注册 极速借款</a>
                <p>注册即同意<a rel="external" href="${path}/act/light-loan-xjx/agreement.do" >《小鱼儿注册协议》</a></p>
            </div>
            <!-- <a onclick="downxjxApp();" class="download-app"><u>已有帐号 去下载APP</u></a> -->
            <div class="more-info1">
                <!-- 2016-12-22添加 -->
                <%--  <h3>本期进行中活动</h3>
                 <img src="${basePath}/images/d_07.png" height="300" width="680" alt="" class="d-07"> --%>
                <!-- 2016-12-22添加 -->
                <h3>简单的借款流程</h3>
                <img src="${basePath}/images/c_01.png" alt="">
                <h3>小鱼儿优势</h3>
                <p>1、无需抵押，无需担保，纯信用线上借贷。</p>
                <p>2、五分钟资料填写，最快三分钟到账。</p>
                <p>3、银行级数据保护，为您保驾护航。</p>
                <p>小提示：真实的资料信息有助于您快速成功借款。</p>
            </div>
            <div class="copyright">
                <p align="center">投资有风险  请谨慎</p>
                <p align="center"></p>
                <p>鸾鸟网络科技（上海）有限公司</p>
                <p>沪ICP备17021248号-1</p>
            </div>
        </div>
        <!-- main end -->
    </div>
</form>
<!--统计代码-->
<style type="text/css">
    #cnzz_stat_icon_1271438488 {
        display: none;
    }
</style>
<script type="text/javascript">
    var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
    document.write(unescape("%3Cspan id='cnzz_stat_icon_1271438488'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s13.cnzz.com/z_stat.php%3Fid%3D1271438488' type='text/javascript'%3E%3C/script%3E"));
</script>
</body>
<script type="text/javascript">
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
    function nextStep(){//点击注册的时候

        var userPhone =	$("#userPhone").val();
        var smsCode =	$("#smsCode").val();
        var passWord = $("#passWord").val();
        var user_from = $("#user_from").val();
        var invite_code=$("#invite_code").val();
        var token=$("#token").val();
        var brower_type = borrowInfo();//手机浏览器类型（1Android、2ios、4pc）
        if(!checkVar(userPhone)){
            showLoader('请输入手机号码');
            return;
        }else if(phonePattern.test(userPhone) == false){
            showLoader('手机号格式不对');
            return;
        }else if(!checkVar(passWord)){
            showLoader('请输入密码');
            return;
        }else if(!checkVar(smsCode)){
            showLoader('请输入手机验证码');
            return;
        }else if(passWord.length<6){
            showLoader('密码必须大于等于6位!');
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
            brower_type:brower_type
        };
        $.ajax({
            type : "POST",
            async:false,
            url : url,
            data : param,
            dataType : "json",
            success : function(ret) {
                if (ret.code == '0') {
                    showLoader("注册成功");
                    setTimeout(function(){window.location.href='${path}/act/light-loan-xjx/succ';}, 1 * 1000 );
                } else {
                    $("#password").val("");//密码置为空
                    showLoader(ret.message);
                }
            }
        });
    }


    jQuery(document).ready(function($) {
        //倒计时
        var wait=60;
        $('#sendcode').click(function(event) {
            var phone =	$("#userPhone").val();
            var captcha =	$("#captcha").val();
            var RCaptchaKey =	$("#RCaptchaKey").val();
            if (wait != 60) {
                return;
            }
            var data = {};
            data.phone = phone;
            openAjax('${path}/act/light-loan-xjx/registerCode?phone='+phone+'&RCaptchaKey='+RCaptchaKey+'&captcha='+captcha, data, checksendSmsCallBack);
        });
        function checksendSmsCallBack(data){
            var userPhone =	$("#userPhone").val();
            var captcha =	$("#captcha").val();
            if(!checkVar(userPhone)){
                showLoader('请输入手机号码');
                return;
            }else if(phonePattern.test(userPhone) == false){
                showLoader('手机号格式不对');
                return;
            }else if (!checkVar(captcha)){
                showLoader("请输入正确的图形验证码！");
                return false;
            }else{
                if (data.code == '0') {
                    showLoader("短信已发送");
                    time();
                } else{
                    showLoader(data.message);
                }
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
    });
</script>
</html>