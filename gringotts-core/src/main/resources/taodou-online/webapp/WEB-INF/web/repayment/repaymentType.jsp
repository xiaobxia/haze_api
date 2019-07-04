<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
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
    <link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />

    <script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/base.js"></script>
</head>
<body style="background:#fff;">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
   <%-- <div class="header">
        <a class="back_arr" href="${base}/repayment/repay-type-alipay?id=${id}"></a>
        <h1>还款方式</h1>
    </div>--%>
    <!-- main start -->
    <div class="wrapper">
        <div class="info">
            <p>①【推荐-主动发起还款】，用户可打开APP，进入借款详情点击我要还款即可；</p>
          <!--   <p>②【推荐-支付宝还款】，部分银行卡无法扣款的可以还款到支付宝账号972802937@qq.com（推荐）上海言金信息科技有限公司或xjx@xianjinxia.com上海连米网络科技有限公司，需要备注好姓名以及注册手机号。备注了正确的手机号和姓名，系统会自动更新的，您稍后关注下APP的还款状态哦，大概半小时左右</p> -->
            <p>③【到期平台代扣】，在还款日之前将所需还款金额放在绑定银行卡，还款日平台将会在四个时间段进行扣款（每天10:00，13:00，16:00，20:00，23:00），扣款成功后会给您发送短信。</p>
            <p><span>④</span>【邮政储蓄银行用户注意事项】对于绑定银行卡是邮政储蓄银行的用户，由于邮政储蓄银行不支持主动发起还款与到期平台代扣，所以可以通过以下2
            1种方式还款：1)).到<strong>我的</strong>或者<strong>认证中心</strong>更改绑定银行卡。</p>
        </div>

    </div>
    <!-- main end -->
</div>
</body>
</html>