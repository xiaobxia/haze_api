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
<link rel="stylesheet" type="text/css" href="${basePath}/css/jquery.mobile-1.4.2.min.css">
<link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />
 
<script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>
<script type="text/javascript" src="${basePath}/js/base.js"></script>
<script src="${basePath }/js/global-1.1.0.min.js"></script>
</head>
<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
<!--         <div class="header"> -->
<!--             <a class="back_arr" href="javascript:void(0);"></a> -->
<!--             <h1>重新绑定银行卡</h1> -->
<!--         </div> -->
        <!-- main start -->
        <div class="wrapper">
          <div class="apply" style="margin-top:0.4rem;">
              <ul class="zl_info2 tk_new2 more-info2">
                <li>
                  <a rel="external" href="javascript:;">
                  <span>持卡人</span>
                  <input type="text" value="${realName}" data-role="none" disabled="disabled" id="realName">
                  <input type="hidden" value="${cardId}" data-role="none" disabled="disabled" id="cardId">
                  </a>
                </li>
                <li id="xz-bank">
                  <a rel="external" href="javascript:;">
                  <span>选择银行</span>
                  <input type="text" value="${bankName}" data-role="none" readonly="true" bankId="${bankId}" id="bankId" >
                  <div class="bank-select">
                    <ul class="bank-list">
                      <c:forEach  items="${bankList}"  var="bank">
                      	  <li id="${bank.id}">${bank.bankName}</li>
                      </c:forEach>
                    </ul>
                  </div>
                  <div class="cover"></div> 
                  </a>
                </li>
                <li>
                  <a rel="external" href="javascript:;">
                  <span>银行卡号</span>
                  <input type="text" placeholder="请输入银行卡号" data-role="none" readonly="true" id="bankCard" value="${bankCard}">
                  </a>
                </li>
                <li>
                  <a rel="external" href="javascript:;">
                  <span>预留手机</span>
                  <input type="text" placeholder="请输入银行预留手机号" data-role="none" readonly="true" id="userPhone"  value="${userPhone}" >
                  </a>
                </li>
              </ul>
        </div>
          <a href="javascript:againBankCard();" class="js-btn2 mt60" id="again-btn">重新绑卡</a>
          <span class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
          <div class="sure-tc">
             <p id="result_code">保存信息成功</p>
            <a href="javascript:void 0;" class="btn sure-btn" id="display_none">确认</a>
          </div>
          <!-- 遮罩层 -->
          <div class="cover"></div>         
        </div>
        <!-- main end -->       
    </div>
    <form action="/www.bindcardinfo.com?msg=操作完成" id="tempForm" method="get" data-ajax="false"></form>
</body>
</html>
<!-- 点击银行卡选择效果 -->
<script type="text/javascript">
var msg='${msg}';
$(document).ready(function() {
	 if(msg!=null&&msg!=''){
    	 $("#result_code").text("登录已失效请重新登录");
    	 $('.sure-tc,.cover').show();
     }
      $('#xz-bank input').click(function(event) {
        	$('.bank-select,.cover').show();
      });
      $('.sure-btn,.cover').click(function(event) {
      	$('.sure-tc,.cover').hide();
    	});
      $('.bank-list li').click(function(event) {
         $(this).addClass('active').siblings().removeClass('active');
         var con=$(this).html();
         $('#xz-bank input').attr("bankId",$(this).attr("id"));
         $('#xz-bank input').val(con);
         $('.bank-select,.cover').hide();
      });
      $('.cover').click(function(event) {
         	$('.bank-select,.cover').hide();
      });
});
//发送连连验证
function getLianLianTokenMsg() {
	if($("#bankId").attr("bankId")==null|| $("#bankId").attr("bankId")==''){
		showLoader('请选择银行');
		return;
	}
	if(checkBank()&&checkUserPhone()){
		$("#sendCodeBtn").removeAttr('onclick');
    	var data = {};
    	data.bank_id=$("#bankId").attr("bankId");
    	data.card_no=$("#bankCard").val();
    	data.phone=$("#userPhone").val();
    	data.id=$("#cardId").val();
		openAjax('${path}/lianlianBindCard/getLianLianToken?deviceId=${deviceId}&mobilePhone=${mobilePhone}', data,LianLianTokenMsgCallback);
	}
}
function LianLianTokenMsgCallback(data) {
	showLoader(data.message);
	if(data.code=="0000"){
		$("#bankId,#bankCard,#userPhone").attr("disabled","disabled" );
	}
	$('#sendCodeBtn').attr("onclick","getLianLianTokenMsg()");
	$("#flash").click();
}

 //手机号码
function checkUserPhone() {
	var phonePattern = /(^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^(([0\+]\d{2,3})?(0\d{2,3}))(\d{7,8})((\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
	if ($("#userPhone").val() == "") {
		showLoader('手机号不能为空');
		return false;
	} else if (phonePattern.test($("#userPhone").val()) == false) {
		showLoader('手机号格式不对');
		return false;
	} else {
		return true;
	}
}     
//验证银行卡号
function checkBank() {
	var phonePattern =  /^(\d{16}|\d{19})$/;
	if ($("#bankCard").val() == "") {
		showLoader('卡号不能为空');
		return false;
	} else if (phonePattern.test($("#bankCard").val()) == false) {
		showLoader('卡号格式不对');
		return false;
	} else {
		return true;
	}
}
function againBankCard(){
	    	var data = {};
	    	data.verify_code=$("#smsCode").val();
	    	data.bank_id=$("#bankId").attr("bankId");
	    	data.card_no=$("#bankCard").val();
	    	data.phone=$("#userPhone").val();
	    	data.id=$("#cardId").val();
			openAjax('${path}/lianlianBindCard/validBorrowOrder?deviceId=${deviceId}&mobilePhone=${mobilePhone}', data,resultSave);
}
function resultSave(data){
	if(data.code=="0"){
		window.location.href ="${path}/lianlianBindCard/credit-card/updateUserBank?deviceId=${deviceId}&mobilePhone=${mobilePhone}";
   	}
	else if(data.code=="-2"){
   		$("#result_code").text("登录已失效,请重新登录");
   		$('.sure-tc,.cover').show();
   	}else{
   		$("#result_code").text(data.message);
	    $('.sure-tc,.cover').show();
   	}
   	 
 	/* if(data.code=="0000"){
   		var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); 
        if (isAndroid) {
            nativeMethod.authenticationResult("操作完成");
        }else if(isIOS){
        	$("#tempForm").submit();
        }
   	} */
}     
</script>