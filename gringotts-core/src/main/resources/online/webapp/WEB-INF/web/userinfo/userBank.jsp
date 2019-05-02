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
<title>绑定银行卡</title>
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
<style type="text/css">
	.bank_no{
		width: 9.2rem !important;
	}
</style>
</head>
<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
        <!-- main start -->
        <div class="wrapper">
          <div class="apply" style="margin-top:0.4rem;">
              <ul class="zl_info tk_new more-info">
                <li>
                  <a rel="external" href="javascript:;">
                  <span>所属银行</span>
                  <input type="text" value="${bankName}" data-role="none" readonly="true">
                  </a>
                </li>
                <li>
                  <a rel="external" href="javascript:;">
                  <span>银行卡号</span>
                  <input type="text" class="bank_no" value="${bankCard}" data-role="none"readonly="true">
                  </a>
                </li>
                <li class="nobd">
                  <a rel="external" href="javascript:;">
                  <span>预留手机号</span>
                  <input type="text" value="${bankPhone}" data-role="none"readonly="true">
                  </a>
                </li>
              </ul>
              <div class="tip">
                <p>备注</p>
                <p>1、借款通过申请后，我们将会将您的所借款项发放到该张银行卡；</p>
                <p>2、若申请重新绑卡，则新卡将被激活为收款银行卡；</p>
                <p>3、未完成借款期间不允许更换银行卡；</p>
              </div> 
        </div>
          <a href="${path}/credit-card/againUserBank?deviceId=${deviceId}&mobilePhone=${mobilePhone}" class="js-btn mt60" id="bc-btn" data-ajax="false">重新绑卡</a>
          <span class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
          <div class="sure-tc">
             <p id="result_code">保存信息成功</p>
            <a href="javascript:void 0;" class="btn sure-btn">确认</a>
          </div>
          <!-- 遮罩层 -->
          <div class="cover"></div>         
        </div>
        <!-- main end -->       
    </div>
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
</script>