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
<!--         <div class="header"> -->
<!--             <a class="back_arr" href="javascript:void(0);"></a> -->
<!--             <h1>忘记密码</h1> -->
<!--         </div> -->
        <!-- main start -->
        <div class="wrapper">
          <ul class="tab-list clearfix">
            <li class="current"><a href="javascript:;" rel="external">移动</a></li>
            <li><a href="javascript:;" rel="external">联通</a></li>
            <li><a href="javascript:;" rel="external">电信</a></li>
          </ul>
          <div class="item-box">
            <div class="item" style="display:block;">
              <ul class="item-inner">
                <li>
                  <div class="plan-title clearfix">
                    <h2>方案一<span>（推荐）</span></h2>
                    <a href="javascript:;">拨打电话</a>
                  </div>
                  <div class="plan-info">
                    <p>使用本机拨打10086客服电话热线，按照语音提示进行密码重置；</p>
                  </div>
                </li>
                <li>
                  <div class="plan-title">
                    <h2>方案二</h2>
                    <a href="http://www.10086.cn">进入官网</a>
                  </div>
                  <div class="plan-info">
                    <p>进入移动官网找回密码：<span><a href="http://www.10086.cn">http://www.10086.cn</a></span></p>
                    <p>1、选择手机号归属地省份</p>
                    <p>2、点击【登录】，选择【登录网上营业厅】</p>
                    <p>3、点击【忘记密码】进入相应页面找回密码</p>
                  </div>
                </li>
              </ul>
            </div>
            <div class="item">
                <ul class="item-inner">
                <li>
                  <div class="plan-title clearfix">
                    <h2>方案一<span>（推荐）</span></h2>
                    <a href="javascript:;">拨打电话</a>
                  </div>
                  <div class="plan-info">
                    <p>使用本机拨打10010客服电话热线，按照语音提示进行密码重置；</p>
                  </div>
                </li>
                <li>
                  <div class="plan-title">
                    <h2>方案二</h2>
                    <a href="javascript:;">进入官网</a>
                  </div>
                  <div class="plan-info">
                    <p>进入联通官网找回密码：<span><a href="http://uac.10010.com">http://uac.10010.com</a></span></p>
                    <p>1、选择手机号归属地省份</p>
                    <p>2、点击【登录】，选择【登录网上营业厅】</p>
                    <p>3、点击【忘记密码】进入相应页面找回密码</p>
                  </div>
                </li>
              </ul>
            </div>
            <div class="item">
              <ul class="item-inner">
                <li>
                  <div class="plan-title clearfix">
                    <h2>方案一<span>（推荐）</span></h2>
                    <a href="javascript:;">拨打电话</a>
                  </div>
                  <div class="plan-info">
                    <p>使用本机拨打10000客服电话热线，按照语音提示进行密码重置；</p>
                  </div>
                </li>
                <li>
                  <div class="plan-title">
                    <h2>方案二</h2>
                    <a href="javascript:;">进入官网</a>
                  </div>
                  <div class="plan-info">
                    <p>进入电信官网找回密码：<span><a href="http://www.189.cn">http://www.189.cn</a></span></p>
                    <p>1、选择手机号归属地省份</p>
                    <p>2、点击【登录】，选择【登录网上营业厅】</p>
                    <p>3、点击【忘记密码】进入相应页面找回密码</p>
                  </div>
                </li>
              </ul>
            </div>
          </div>
          
        </div>
        <!-- main end -->
        
    </div>
    <script type="text/javascript">
        jQuery(document).ready(function($) {
          $('.tab-list li').click(function(event) {
            $(this).addClass('current').siblings().removeClass('current');
            var s=$(this).index();
            $('.item').eq(s).show().siblings('.item').hide();
          });
        });
    </script>
</body>
</html>