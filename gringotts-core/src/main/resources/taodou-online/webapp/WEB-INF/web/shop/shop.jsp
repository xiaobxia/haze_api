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
  <title>贷款超市</title>
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
  <style>
    html, body {
      background-color: rgb(246,246,246);
    }
    .wrapper {
      padding: 0.695rem;
    }
    .card {
      display: block;
      text-decoration: none;
      padding: 0.65rem 0.695rem 0.6rem 0.695rem;
      background-color: #fff;
      border-radius: 0.2rem;
      box-shadow: 0 0.1rem 0.4rem #eee;
    }
    .card-header {
      position: relative;
    }
    .card-header .img-wrap{
      position: absolute;
      left: 0;
      top: 0;
      display: block;
      height: 2.083rem;
      width: 2.083rem;
      overflow: hidden;
    }
    .card-header .img-wrap img {
      width: 100%;
      height: auto;
    }
    .header-detail {
      position: relative;
      margin-left: 2.5rem;
    }
    .header-detail h4 {
      margin: 0;
      line-height: 1.1;
      font-size: 0.764rem;
      color: #000;
    }
    .header-detail .sub {
      font-size: 0.684rem;
      color: #999;
    }
    .header-detail .sub .number{
      color: #000;
    }
    .header-detail .sub .right{
      color: #000;
      font-size: 0.868rem;
      font-weight: 400;
      float: right;
    }
    .tag-wrap {
      margin: 0.347rem 0 0 0;
    }
    .tag-wrap .tag {
      padding: 0.1rem 0.2rem;
      font-size: 0.684rem;
      color: rgb(70,160,230);
      border-radius: 0.984rem;
      border: 0.05rem solid rgb(70,160,230);
    }
    .number-info {
      text-align: right;
      font-size: 0.684rem;
      color: #999;
    }
    .card-bottom {
      border-top: 0.05rem dashed #eee;
      padding: 0.5rem 0 0 0;
      font-size: 0.684rem;
      color: #999;
    }
    .card-bottom .number{
      color: #000;
    }
    .card-bottom .right{
      float: right;
    }
  </style>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
  <!-- main start -->
  <div class="wrapper">
    <a href="http://t.cn/EVUQ5s3" class="card">
      <div class="card-header">
        <div class="img-wrap">
          <img src="./common/web/images/2019xinhebao.png" alt="">
        </div>
        <div class="header-detail">
          <h4>信合宝</h4>
          <div class="sub"><span class="left">成功借款<span class="number">235432</span>人</span><span class="right">¥50000</span></div>
          <div class="tag-wrap">
            <span class="tag">芝麻分600分</span>
            <span class="tag">只要身份证</span>
            <span class="tag">就能借到钱</span>
          </div>
          <div class="number-info">最高额度</div>
        </div>
      </div>
      <div class="card-bottom">
        <span class="left">日利率：<span class="number">1%</span></span>
        <span class="right">期限：<span class="number">7-7天</span></span>
      </div>
    </a>
  </div>
  <!-- main end -->

</div>
</body>
</html>
