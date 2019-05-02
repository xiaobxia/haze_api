<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>芝麻信用认证</title>
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
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page" >
<!--         <div class="header"> -->
<!--             <a class="back_arr" href="javascript:void(0);"></a> -->
<!--             <h1>芝麻信用</h1> -->
<!--         </div> -->
        <!-- main start -->
        <div class="wrapper">
          <div class="sq-wrap">
              <img src="${basePath }/images/logo.png" height="539" width="393" alt="" class="logo">
              <!-- <p>您的芝麻信用已授权</p> -->
              <p>我们需要您授权芝麻信用</p>
              <p>良好的信用记录会加速审核和提高额度</p>
              <c:if test="${code == 200}">
              <a href="" class="btn h-btn">已授权</a>
              </c:if>
               <c:if test="${code == 100}">
              <a href="${msg}" class="btn">授权</a>
              </c:if>
              <c:if test="${code == 300}">
              <a href="" class="btn h-btn">${msg}</a>
              </c:if>
              
              <span><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
          </div>
          
        </div>
        <!-- main end -->
        
    </div>
    <script type="text/javascript">
        var recode = '${record}';
        if("2" == recode){
            var tipMsg = "芝麻授信认证失败,重新发起认证";
            var tipType="NO";
            if('${code}' == 200){
                tipMsg="芝麻授信认证已完成";
                tipType="YES";
            }else if('${code}' == 100){
                tipMsg='芝麻授信认证失败,重新发起认证';
            }else if('${code}' == 300){
                tipMsg='芝麻授信认证失败,请重新登录';
            }else{
                tipMsg="${msg}";
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