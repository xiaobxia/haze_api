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

    <title>注册成功</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <script type="text/javascript" src="${basePath }/js/jquery-1.9.1.min.js"></script>
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
        _czc.push(["_setAccount", "1271438488"]);
    </script>
    <script type="text/javascript">
        function gotoxjxapp(){
            //location = "http://www.xianjinxia.com:8086/xjx-platform/a/jsaward/awardCenter/drawAwardIndex";
        }
        function downApp(){
            location="http://download.jx-money.com/";
        }
    </script>
    <style>
        body,h4,html,i,li,p,ul,input{list-style:none;font-family:Arial,Helvetica,sans-serif;font-size:1rem;border:none;}
        *{margin:0;padding:0;-webkit-user-select:auto}
        input,textarea,button,a {outline: none;-webkit-appearance: none;-webkit-tap-highlight-color: rgba(0, 0, 0, 0);}
        .regist-bg{background:url(${basePath}/zmxy/images/bg_register.png) top center;background-repeat: no-repeat;background-size: cover;min-height: 100vh;}
        .regist-bg .wrapper-bg{padding:0 0 33%}
        .regist-bg .banner-txt{margin:0 0 0;padding-top:10%}
        .regist-bg .banner-txt p{font-size:26px;text-align:center;color:#fff;letter-spacing:.8px}
        .regist-bg .banner-txt p span{font-size:45px;color:#ffcf00}
        .regist-bg .banner-txt p font{color:#ffcf00}
        .regist-bg .banner-desc{margin:35px 0 50px}
        .regist-bg .banner-desc p{font-size:18px;color:#fff;text-align:center;line-height:36px}
        .regist-bg .banner-desc p span {font-size: 21px;margin: 0 2px;}
        .success-main1{background-color:#fff;width:90%;margin:20px auto 0;border-radius:10px;padding:6% 5%;box-sizing:border-box}
        .success-main1 .success-info{background:url("${basePath }/images/e_01.png") 0 0 no-repeat;background-size:100%}
        .success-main1 .success-box p{color:#333;font-size:16px;text-align:center;margin-bottom:30px}
        .success-main1 .success-box .success-info{width:180px;height:140px;margin:30px auto;}
        .success-main1 .success-box .success-info .value{padding:52px 0 5px;text-align:center;color:#ff4f3e;font-weight:400;font-size:48px}
        .success-main1 .success-box .success-info em{display:block;font-size:14px;font-style:normal;color:#666;text-align:center}
        .success-main1 .com-btn{background:#ff7700;width:90%;text-align:center;color:#fff;font-size:16px;margin:0 auto;line-height:45px;height:auto;border-radius:50px;display:block;text-decoration:none}
        .banner-pic .banner-logo {width: 80%;margin: 0.3rem auto 0;}
        .banner-pic img {display: block;}
        .banner-pic .text-center{font-size: 0;margin-top: 25px;}
        .banner-pic .text-center .img{margin: 0 auto;}
        .banner-pic .text-center .wangyuan {width: 306px;}
        .banner-pic .text-center .fast-time {width: 197px;margin: 6px auto 25px;}
        .banner-pic .text-center .banner-detail {width: 251px;}

    </style>
</head>
<body class="regist-bg">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page" tabindex="0">
    <!-- <div class="banner-txt">
        <p>最高<span class="money-num">10000</span>元<font class="small">现金</font>到账</p>
        <p>最快<span class="money-num">3</span>分钟<font class="small">放款</font></p>
    </div>
    <div class="banner-desc">
        <p>门槛低、审核快</p>
        <p>凭<span>身份证</span>就能完成借款</p>
    </div> -->
    <div class="banner-pic">
        <img class="banner-logo" src="${basePath}/zmxy/images/jx-banner.png" alt="">
    </div>
    <!-- main start -->
    <div class="wrapper wrapper-bg">
        <div class="register-wrapper">
            <!-- 无活动版 -->
            <div class="register success-main success-main1">
                <div class="success-box">
                    <p >恭喜你,注册成功!</p>
                    <!-- <div class="success-info" onclick="gotoxjxapp();"> -->
                    <div class="success-info" >
                        <h2 class="value">2000</h2>
                        <em>最高可借</em>
                    </div>
                </div>
                <a id="download_btn" onclick="downApp()" href="javascript:;" class="com-btn">立即下载App</a>
            </div>
        </div>
        <!-- 无活动版 -->
        <!--<div class="more-info1" style="display: none;">
             <h3>0元夺宝奖池金额</h3>
            <div class="bonus">
              <div class="bonus-up clearfix">
                <img src="images/d_05.png" alt="">
                <ul class="jr-list">
                  <li>¥</li>
                  <li>1</li>
                  <li>0</li>
                  <li>0</li>
                  <li>0</li>
                  <li>0</li>
                  <li>0</li>
                </ul>
              </div>
              <p>注册并完善资料，借款，还款，推荐好友</p>
              <p>都可增加奖池金额</p>
            </div>
            <h3>简单的借款流程</h3>
            <img src="${basePath }/images/c_01.png" alt="">
            <h3>小鱼儿优势</h3>
            <p>1、无需抵押，无需担保，纯信用线上借贷。</p>
            <p>2、五分钟资料填写，最快三分钟到账。</p>
            <p>3、银行级数据保护，为您保驾护航。</p>
            <p>小提示：真实的资料信息有助于您快速成功借款。</p>
        </div>
        <div class="copyright" style="display: none;">
            <p align="center">投资有风险  请谨慎</p>
            <p align="center"></p>
            <p>鸾鸟网络科技（上海）有限公司</p>
            <p>沪ICP备17044849号-1</p>
        </div>
    </div>-->
        <!-- main end -->
    </div>
</div>
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
<!-- 数字滚动效果 -->
<script type="text/javascript">
    function trackEvenDownload_btn(){
        var nowTime = new Date();
        _czc.push(['_trackEvent', '下载页面', '点击', '下载APP按钮', (nowTime.getTime() - initTime.getTime())/1000, 'download_btn']);
    }

    $.fn.numberRock=function(options){
        var defaults={
            speed:24,
            count:100
        };
        var opts=$.extend({}, defaults, options);
        var div_by = 100,
            count=opts["count"],
            speed = Math.floor(count / div_by),
            sum=0,
            $display = this,
            run_count = 1,
            int_speed = opts["speed"];
        var int = setInterval(function () {
            if (run_count <= div_by&&speed!=0) {
                $display.text(sum=speed * run_count);
                run_count++;
            } else if (sum < count) {
                $display.text(++sum);
            } else {
                clearInterval(int);
            }
        }, int_speed);
    };
    $(".value").numberRock({
        speed:10,
        count:10000
    });
</script>
</body>
</html>