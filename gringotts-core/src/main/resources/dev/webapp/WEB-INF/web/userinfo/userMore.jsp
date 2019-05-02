<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>更多信息</title>
<meta http-equiv="Expires" content="-1">               
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache"> 
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" href="${basePath }/css/jquery.mobile-1.4.2.min.css">
<link rel="stylesheet" type="text/css" href="${basePath }/css/basic.css" />
<link rel="stylesheet" type="text/css" href="${basePath }/css/common.css" />
<script type="text/javascript" src="${basePath }/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
<script src="${basePath }/js/global-1.1.0.min.js"></script>
</head>
<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
        <!-- main start -->
        <div class="wrapper">
          <div class="apply">
              <h2>为保证借款申请顺利通过，请务必填写真实信息！</h2>
              <ul class="zl_info tk_new more-info">
                <li class="nobd gain-yzm">
                  <a rel="external" href="javascript:;">
                  <span>淘宝账号</span>
                  <input type="text" placeholder="请输入个人淘宝账号" data-role="none" name="tobaoaccount" value="${tobaoaccount}" id="tobaoaccount">
                  </a>
                </li>
                <li class="nobd gain-yzm">
                  <a rel="external" href="javascript:;">
                  <span>常用邮箱</span>
                  <input type="text" placeholder="请输入邮箱" data-role="none" name="email" value="${email}" id="email">
                  </a>
                </li>
                <li class="nobd gain-yzm">
                  <a rel="external" href="javascript:;">
                  <span>QQ账号</span>
                  <input type="text" placeholder="请输入QQ账号" data-role="none" name="qq" value="${qq}" id="qq">
                  </a>
                </li>
                <li class="nobd gain-yzm">
                  <a rel="external" href="javascript:;">
                  <span>微信账号</span>
                  <input type="text" placeholder="请输入微信账号" data-role="none"  name="wechatAccount" value="${wechatAccount}" id="wechatAccount">
                  </a>
                </li>
              </ul> 
        </div>
          <a href="javascript:saveMore();" class="js-btn mt60" id="bc-btn" style="background-color: #31c27c">保存</a>
          <span class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
          <div class="sure-tc">
             <p id="result_code">保存信息成功</p>
            <a href="javascript:void 0;" class="btn sure-btn" style="background-color: #31c27c">确认</a>
          </div>
          <!-- 遮罩层 -->
          <div class="cover"></div>
        </div>
        <!-- main end -->
        
    </div>
    <form action="/www.moreInfo.com?msg=保存信息成功" id="tempForm" method="get" data-ajax="false"></form>
</body>
</html>
<script type="text/javascript">
	var msg='${msg}';
    jQuery(document).ready(function($) {
   	 if(msg!=null&&msg!=''){
       	 $("#result_code").text("登录已失效请重新登录");
       	 $('.sure-tc,.cover').show();
     }
      // 确认弹窗
      $('.sure-btn,.cover').click(function(event) {
        $('.sure-tc,.cover').hide();
      });
    });
    function saveMore(){
    	if($("#tobaoaccount").val()==null || $("#tobaoaccount").val()==""){
    		showLoader("请输入淘宝账号");
    		return;
    	}
    	if($("#email").val()==null || $("#email").val()==""){
    		showLoader("请输入邮箱");
    		return;
    	}else if(!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#email").val())){
    		showLoader("邮箱格式不正确");
    		return;
    	}
    	if($("#qq").val()==null || $("#qq").val()==""){
    		showLoader("请输入qq号");
    		return;
    	}
    	if($("#wechatAccount").val()==null || $("#wechatAccount").val()==""){
    		showLoader("请输入微信账号");
    		return;
    	}
    	var data = {};
    	data.qq=$("#qq").val();
    	data.tobaoaccount=$("#tobaoaccount").val();
    	data.email=$("#email").val();
    	data.wechatAccount=$("#wechatAccount").val();
		openAjax('${path}/credit-card/saveMore?deviceId=${deviceId}&mobilePhone=${mobilePhone}', data,resultSave);
    }
    function resultSave(data){
    	if(data.code=="0"){
    		$("#result_code").text("保存信息成功");
            if(null != data.isNew && 'true' == data.isNew){
                var u = navigator.userAgent, app = navigator.appVersion;
                var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
                var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
                if (isAndroid) {
                    nativeMethod.authenticationResult("保存信息成功");
                } else if(isIOS) {
                    $("#tempForm").submit();
                }
            }
    	}else if(data.code=="-2"){
    		$("#result_code").text("登录已失效,请重新登录");
    	}else{
    		$("#result_code").text("保存信息失败");
    	}
        $('.sure-tc,.cover').show();
    }
</script>