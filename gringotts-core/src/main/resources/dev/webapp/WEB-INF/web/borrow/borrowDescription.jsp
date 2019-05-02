<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = path + "/common/web";
%>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<c:set var="path" value="<%=path%>"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>用户借贷说明</title>
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
 
<script type="text/javascript" src="${basePath }/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
<script type="text/javascript" src="${basePath }/js/base.js"></script>
</head>
<body style="background:#fff;">
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
       <%-- <div class="header">
            <a class="back_arr" href="javascript:void(0);"></a>
            <h1>小鱼儿-纯信用小额借钱极速放贷</h1>
        </div>--%>
        <!-- main start -->
        <div class="wrapper">
          <div class="explain">
           <h3>1、适用对象</h3>
           <p>适用于在到期还款日，因为资金不足而无法正常还款的用户。</p>
           <p>比如，小明的还款日是2016年11月11日，还款金额是1000.00元，此时小明可以申请续期服务，将还款期限廷后几天，等资金充实再完成还款。</p>
            <h3>2、相关费用</h3>
            <p>目前续期服务所收费用由三部分组成：服务费、续期费、逾期费。</p>
            <p>服务费：即一笔借款延期一个借款周期的费用，即一个借款周期所需的费用；</p> 
            <p>续期费：申请续期服务所需的服务费用；</p>
            <p>逾期费：即申请续期服务时该笔借款当前产生的逾期费用，申请续期服务的时候需要优先结算掉。</p>
            <h3> 3、续期次数</h3>
            <p>目前借款所允许的续期服务次数为3次，以平台最新官方公告为准。</p>

          </div>
          
        </div>
        <!-- main end -->
        
    </div>
</body>
</html>