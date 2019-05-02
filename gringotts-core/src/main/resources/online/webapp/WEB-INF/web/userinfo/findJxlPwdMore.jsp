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
<title>忘记密码</title>
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
<script type="text/javascript" src="${basePath }/js/jquery-1.9.1.min.js"></script>
</head>
<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
        <div class="wrapper">
          <div class="reset-guide">
            <div class="guide-title">
              <img src="${basePath }/images/icon_06.png" alt="">
              <c:choose>
                 <c:when test="${type eq 1}">
                                                       移动运营商服务密码重置指南
                 </c:when>
                 <c:when test="${type eq 2}">
                                                       联通运营商服务密码重置指南
                 </c:when>
                 <c:when test="${type eq 3}">
                                                       电信运营商服务密码重置指南
                 </c:when>
              </c:choose>
                                          
            </div>
            <div class="guide-content" id="jxl_1" style="display: none;">
              <img src="${basePath }/images/p_06.png" alt="">
              <img src="${basePath }/images/p_07.png" alt="">
              <img src="${basePath }/images/p_03.png" alt="">
            </div>
            <div class="guide-content" id="jxl_2" style="display: none;">
              <img src="${basePath }/images/p_04.png" alt="">
              <img src="${basePath }/images/p_05.png" alt="">
              <img src="${basePath }/images/p_03.png" alt="">
            </div>
            <div class="guide-content" id="jxl_3" style="display: none;">
              <img src="${basePath }/images/p_01.png" alt="">
              <img src="${basePath }/images/p_02.png" alt="">
              <img src="${basePath }/images/p_03.png" alt="">
            </div>
            <div class="copyright">
                <p>上海皖湘网络信息科技有限公司</p>
                <p>沪ICP备16044119号</p>
            </div>
         </div>  
        </div>
        <script type="text/javascript">
        $(document).ready(function() {
             var type='${type}';
             $("#jxl_"+type).show();
             if(type == '1'){
            	 
             }
           //  $('.guide-title').
             
        });
        </script>
    </div>
</body>
</html>