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
<title>手机运营商认证</title>
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
<script type="text/javascript" src="${basePath }/js/base.js"></script>
</head>
<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
<!--         <div class="header"> -->
<!--             <a class="back_arr" href="javascript:void(0);"></a> -->
<!--             <h1>手机运营商认证</h1> -->
<!--         </div> -->
        <!-- main start -->
        <div class="wrapper">
          <div class="rz-result" style="margin-top:0.4rem;">
          <c:if test="${code == 200}">
              <p>运营商认证已完成</p> 
          </c:if>
          <c:if test="${code == 100}">
              <p>运营商认证失败,重新发起认证</p> 
          </c:if>
          <c:if test="${code == 300}">
              <p>运营商认证失败,重新发起认证流程</p> 
          </c:if>
          <c:if test="${code == 500}">
              <p>运营商认证失败,重新发起认证</p> 
          </c:if>
          <c:if test="${code == 400}">
              <p>运营商认证失败,请重新登录</p> 
          </c:if>
         
        </div>
         <c:if test="${code == 200}">
             <a href="javascript:;" class="js-btn mt60 yrz" id="bc-btn">已认证</a>
          </c:if>
          <c:if test="${code == 100}">
             <a href="javascript:;" class="js-btn mt60 yrz" id="bc-btn">认证失败</a>
          </c:if>
          <c:if test="${code == 300}">
             <a href="javascript:;" class="js-btn mt60 yrz" id="bc-btn">认证失败</a>
          </c:if>
           <c:if test="${code == 400}">
             <a href="javascript:;" class="js-btn mt60 yrz" id="bc-btn">请重新登录</a>
          </c:if>
          
          <span class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>         
        </div>
        <!-- main end -->       
    </div>
    
    <script type="text/javascript">
    var recode = '${recode}';
    
    if(recode==2){
    	
    	
    }else{
    	    	
    	var  tipMsg="运营商认证失败,重新发起认证";
    	var tipType="NO";
    	if('${code}' == 200){
    		tipMsg="运营商认证已完成"; 
    		tipType="YES";
    	}else if('${code}' == 100){
    		tipMsg='运营商认证失败,重新发起认证';
    	}else if('${code}}' == 300){
    		tipMsg='运营商认证失败,重新发起认证流程';
    	}else if('${code}' == 500){
    		tipMsg="运营商认证失败,重新发起认证";
    	}else if('${code}' == 400){
    		tipMsg="运营商认证失败,请重新登录";
    	} else if('${code}' == 600){
    		tipMsg="请添加个人信息";
    	}
    	

    	if('${clientType}' =='ios'){
    		
    		//ios
    		window.location.href="www.mobileApprove.com&result="+tipType;
    	}else{
    		//安卓
        	nativeMethod.authenticationResult(tipMsg);
    	}
    }
    
    
    
    </script>
</body>
</html>