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
<title>小鱼儿</title>
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

<script type="text/javascript">

/**
  出来的链接大概是长这样的
  http://xxxx.cn/243423.html?c=Q23DR32
*/

// c=Q23DR32是注册时扫描获取的邀请码。
// 这样加参数，后面的参数会被自动忽略，不会影响加载此网页

  goDownload();

  // 去下载
  function goDownload() {
    var u = navigator.userAgent, app = navigator.appVersion;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1;
    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    // 是安卓浏览器
    if (isAndroid) {
      window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.innext.xjx'; // 跳安卓端下载地址
    }
    // 是iOS浏览器
    if (isIOS) {
      window.location.href = 'https://itunes.apple.com/cn/app/ji-su-xian-jin-xia/id1156341826?mt=8'; // 跳AppStore下载地址
    }

    // 是微信内部webView
    if (is_weixn()) {
      alert("请点击右上角按钮, 点击使用浏览器打开");
    }

    // 是PC端
    if (IsPC()) {
      window.location.href = 'http://www.xianjinxia.com/'; // 公司主页
    }
  }

  // 是微信浏览器
  function is_weixn(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
      return true;
    } else {
      return false;
    }
  }


  function IsPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
      "SymbianOS", "Windows Phone",
      "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
      if (userAgentInfo.indexOf(Agents[v]) > 0) {
        flag = false;
        break;
      }
    }
    return flag;
  }

</script>
<body>
</body>
</html>