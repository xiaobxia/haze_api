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
<style type="text/css">
	.bank_no{
		width: 9.2rem !important;
	}
</style>
</head>
<style>

</style>

<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
<!--         <div class="header"> -->
<!--             <a class="back_arr" href="javascript:void(0);"></a> -->
<!--             <h1>重新绑定银行卡</h1> -->
<!--         </div> -->
        <!-- main start -->
        <div class="wrapper">
          <div class="apply" style="margin-top:0.4rem;">
              <h2>请填写银行卡信息</h2>
              <ul class="zl_info2 tk_new2 more-info2">
                <li>
                  <a rel="external" href="javascript:;">
                  <span>持卡人</span>
                  <input type="text" value="${realName}" data-role="none" disabled="disabled" >
                  <input type="hidden" value="${cardId}" data-role="none" disabled="disabled" id="cardId">
                  <i id="ckr"></i>
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
                  <i id="xl"></i>
                  </a>
                </li>
                <li>
                  <a rel="external" href="javascript:;">
                  <span>银行卡号</span>
                  <input type="number" pattern="\d*" class="bank_no" placeholder="请输入银行卡号" data-role="none" id="bankCard" value="${bankCard}">
                </li>
                <li>
                  <a rel="external" href="javascript:;">
                  <span>手机号</span>
                  <input type="number" pattern="\d*" placeholder="请输入银行预留手机号" data-role="none" id="userPhone" value="${userPhone}" >
                  </a>
                </li>
                <!-- <li class="nobd gain-yzm1 gain-yzm2">
                  <a rel="external" href="javascript:;">
                  <span>验证码</span>
                  <input type="text" placeholder="请输入验证码" data-role="none" id="smsCode">
                  <div class="yzm2" onclick="getLianLianTokenMsg();" id="sendCodeBtn">
					  点击获取
				  </div>
                  </a>
                </li> -->
                <li class="nobd gain-yzm gain-yzm1"> 
                	<a rel="external" href="javascript:;"> 
                	<span>验证码</span>
         	 		<input style="width:8rem;" type="text" placeholder="请输入验证码" data-role="none" id="smsCode">
          			<div class="yzm clickToken" onclick="getLianLianTokenMsg();" id="yzm">点击获取</div>
          			</a> 
          		</li>
              </ul>
        </div>
          <a href="javascript:saveBankCard();" class="js-btn2 mt60" id="bc-btn">确定绑卡</a>
          <span class="safe"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
          <div class="sure-tc">
             <p id="result_code">保存信息成功</p>
            <a href="javascript:void 0;" class="btn sure-btn" id="display_none">确认</a>
          </div>
          <!-- 遮罩层 -->
          <div class="cover"></div>         
        </div>
        <!-- main end -->   
        <div id="ttc" style="display:none;"></div>
 		 <div class="wind4" style="display:none;">
   		 	<h2>持卡人说明</h2>
   		 	<p>为保障账户资金安全，只能绑定认证用户本人的银行卡！</p>
   		 	<a rel="external" href="javascript:;" id="close4">我知道了</a> </div>
		 </div>
		 <div class="wind2" style="display:none;">
   			 <h2><img src="${basePath}/images/th.png"><span>验证码请求频繁</span><i id="close1"></i></h2>
   		 <div class="qq2">
  			 <p>倒计时</p>
   		 	<div id="sendcode"><i>0</i><em>s</em></div> 
   		 </div>
    </div>
    <form action="/www.bindcardinfo.com?msg=操作完成" id="tempForm" method="get" data-ajax="false"></form>
</body>
</html>
<!-- 点击银行卡选择效果 -->
<script type="text/javascript">
var msg='${msg}';
var timer1;
$(document).ready(function() {
	 if(msg!=null&&msg!=''){
    	 $("#result_code").text("登录已失效请重新登录");
    	 $('.sure-tc,.cover').show();
     }
      $('#xz-bank input,#xl').click(function(event) {
        	$('.bank-select,.cover').show();
      });
      $('.sure-btn,.cover').click(function(event) {
      	$('.sure-tc,.cover').hide();
    	});
      $('.bank-list li').click(function(event) {
         $(this).addClass('active').siblings().removeClass('active');
         var con=$(this).html();
         $('#xz-bank input,#xl').attr("bankId",$(this).attr("id"));
         $('#xz-bank input').val(con);
         $('.bank-select,.cover').hide();
      });
      $('.cover').click(function(event) {
         	$('.bank-select,.cover').hide();
      });
  	
		//-------------------------------------------
		$('#ckr').click(function(e) {
			$('.wind4').show();
			$('#ttc').show();
		});		
		$('#close4').click(function(e) {
		    $('.wind4').hide();
			$('#ttc').hide();
		});	
		/*  $('.yzm').click(function(e) {
		    $('.wind2').show();
			$('#ttc').show();
		});	  */
		$('#close1').click(function(e) {
			clearInterval(timer1);
		    $('.wind2').hide();
			 $('#ttc').hide();
		});	
 		$('#ttc').click(function(e) {
 			clearInterval(timer1);
 		    $('.wind2').hide();
 			$('#ttc').hide();
 		});	
		
});
//发送连连验证
function getLianLianTokenMsg() {
	if($("#bankId").attr("bankId")==null|| $("#bankId").attr("bankId")==''){
		showLoader('请选择银行');
		return;
	}
	if(checkBank()&&checkUserPhone()){
	//	$("#sendCodeBtn").removeAttr('onclick');
    	var data = {};
    	data.bank_id=$("#bankId").attr("bankId");
    	data.card_no=$("#bankCard").val();
    	data.phone=$("#userPhone").val();
    	data.id=$("#cardId").val();
		openAjax('${path}/lianlianBindCard/getLianLianToken?deviceId=${deviceId}&mobilePhone=${mobilePhone}', data,LianLianTokenMsgCallback);
	}
}
function LianLianTokenMsgCallback(data) {
	var wait=data.time;
	if (data.code == "0"){
		$("#bankId,#bankCard,#userPhone").attr("disabled","disabled" );
		showLoader("验证码发送成功");
	}
	else if (data.code == "-3") {
		//倒计时 
		time(wait,wait);
        $('.wind2').show();
	}else{
		showLoader(data.message);
	} 
	$('.clickToken').attr("onclick","getLianLianTokenMsg()");
	$("#flash").click();
}
function time(wait,nowWait){
    if (nowWait==0) {
    	clearInterval(timer1);
        $('#sendcode').text('0s');
		$('.wind2').hide();
     }else{
		$('#sendcode').text(nowWait+'s');
		$('#sendcode').css({"font-size": "1.95rem","color":"#525256","line-height":"2rem"});
		if(wait==nowWait){
			timer1 = setInterval(function(){time(wait,--nowWait);}, 1000);
		}
     }
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
 
function saveBankCard(){
	if($("#bankId").attr("bankId")==null|| $("#bankId").attr("bankId")==''){
		showLoader('请选择银行');
		return;
	}
	if(checkBank()&&checkUserPhone()){
		if($("#smsCode").val()==null || $("#smsCode").val()==''){
			showLoader('请输入验证码');
			return;
		}
    	var data = {};
    	data.verify_code=$("#smsCode").val();
    	data.bank_id=$("#bankId").attr("bankId");
    	data.card_no=$("#bankCard").val();
    	data.phone=$("#userPhone").val();
    	data.id=$("#cardId").val();
		openAjax('${path}/lianlianBindCard/tokenValidate?deviceId=${deviceId}&mobilePhone=${mobilePhone}', data,resultSave);
	}
}
function resultSave(data){
	
   	 if(data.code=="-2"){
   		$("#result_code").text("登录已失效,请重新登录");
   		$('.sure-tc,.cover').show();
   	}else{
   		$("#result_code").text(data.message);
	    $('.sure-tc,.cover').show();
   	}
   	 
 	if(data.code=="0000"){
   		var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); 
        if (isAndroid) {
            nativeMethod.authenticationResult("操作完成");
        }else if(isIOS){
        	$("#tempForm").submit();
        }
   	}

}     
</script>