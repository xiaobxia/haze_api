<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>手机运营商认证</title>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${basePath }/css/jquery.mobile-1.4.2.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath }/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath }/css/common.css"/>

    <script type="text/javascript" src="${basePath }/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/base.js"></script>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
    <!-- 		<div class="header"> -->
    <!-- 			<a class="back_arr" href="javascript:void(0);"></a> -->
    <!-- 			<h1>手机运营商认证</h1> -->
    <!-- 		</div> -->
    <!-- main start -->
    <div class="wrapper">
        <form id="form" name="form" rel="prefetch" rel="external" action="" data-ajax="false">
				<input type="hidden" name = "deviceId" value= "${deviceId}">
				<input type="hidden" name = "clientType" value= "${clientType}">
            <input type="hidden" name="website" value="${website}">
            <input type="hidden" name="mobilePhone" value="${mobilePhone}">
            <input type="hidden" name="userPhone" value="${userPhone}">
            <input type="hidden" name="clientType" value="${clientType}">
            <div class="find-mm-wrap">
                <dl class="find-mm">
                    <dd>
                        <p><img src="${basePath }/images/icon_01.png" alt="">手机号码</p>
                        <div class="input">
                            <input type="text" value="${userPhone}" name="phone" data-role="none" readonly="true">
                        </div>
                    </dd>
                    <dd>
                        <p><img src="${basePath }/images/icon_02.png" alt="">服务密码</p>
                        <div class="input">
                            <input type="password" name="password" placeholder="请输入手机服务密码" value="" data-role="none">
                        </div>
                    </dd>
                    <dd id="liCap" style="display: none;">
                     	<p id="hid1" ><img src="${basePath }/images/icon_02.png" alt="">验证码</p>
                        <p id ="hid2" style="display: none;"><img id="imgCap"   src="" /></p>
                        <div class="input">
                            <input style="border:none;" type="yzm" name="yzm" placeholder="请输入验证码" value="" data-role="none">
                        </div>
                        </p>
                    </dd>
                </dl>
                <div class="tc-btn">
                    <p onclick="findMobileBelong();">
                        <img src="${basePath }/images/icon_03.png" alt="">忘记密码
                    </p>
                </div>
                <a href="javascript:;" onclick="bind();" id="bc-btn" class="rz-btn mrt88">马上认证</a>
            </div>
            <span class="safe mrt8"><img src="${basePath }/images/dp.png" alt="">银行级数据加密防护</span>
        </form>
    </div>
    <!-- main end -->
    <!-- 弹窗-联通-->
    <div class="tc-box unicom">
        <div class="tc-title">

        </div>
        <div class="tc-con">
            <input type="hidden" name="jxlType"/>
            <input type="hidden" name="sendMsg"/>
            <input type="hidden" name="mobile"/>
            <dl class="find-mm">
                <dd>
                    <span>授权手机号码：<span id="tel"></span></span>

                </dd>
                <dd id="pwdLT" style="display: none;">
                    <p>输入6位新密码</p>
                    <div class="input">
                        <input type="password" data-role="none" name="pwd" id="pwd"/>
                    </div>
                </dd>
            </dl>
            <a href="javascript:sendReset();" class="reset-btn">短信重置</a>
            <a href="javascript:;" class="other-way">其他方式找回</a>
        </div>
    </div>
    <!-- 遮罩层 -->
    <div class="cover"></div>
</div>
<form action="" id="tempForm" method="get" data-ajax="false"></form>
</body>
</html>

<script type="text/javascript">
    $(function () {
    	$('.tc-btn p').click(function (event) {
            //findMobileBelong();
            $('.unicom,.cover').show();
        });
        $('.cover').click(function (event) {
            $('.unicom,.cover').hide();
        });
    });
    
    
    function findMobileBelong() {
        var pwd = $("input[name=phone]").val();
        var url = '${path}/credit-web2/findMobileBelong';
        $.post(url, {deviceId: '${deviceId}', mobilePhone: '${userPhone}', phone: pwd}, function (result) {
            if (result) {
                if (result.code == '200') {
                    if (!result.data.sendMsg) {
                        window.open('${path}/credit-web2/findJxlPwdMore?type=' + result.data.jxlType);
                    } else {
                        $('.tc-title').html(result.data.province + result.data.jxlName);
                        $('#jxl_type').val(result.data.jxlType);
                        $('#tel').html(result.data.phone);
                        $('input[name=jxlType]').val(result.data.jxlType);
                        $('input[name=sendMsg]').val(result.data.sendMsg);
                        $('input[name=mobile]').val(result.data.sendPhone);
                        if (result.data.jxlType == '2') {
                            $('#pwdLT').show();
                        }
                    }
                } else {
                    showLoader(result.data.msg);
                    $('.unicom,.cover').hide();
                }
            } else {
                window.open('${path}/credit-web2/jxlfindPwd');
                $('.unicom,.cover').hide();
            }
            $('.other-way').attr('href', '${path}/credit-web2/findJxlPwdMore?type=' + result.data.jxlType);

        });

    }
    function sendReset() {
        var jxlType = $('input[name=jxlType]').val();
        var msg = $('input[name=sendMsg]').val();
        var phone = $('input[name=mobile]').val();
        if (jxlType == '2') {
            var pwd = $("#pwd").val();
            if (!pwd) {
                showLoader("请输入服务密码！");
                return;
            }
            msg = msg.replace("6位新密码", pwd);
        }
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        //isIOS = true;
        if (isAndroid) {
            nativeMethod.sendMessage(phone, msg);
        } else if (isIOS) {
            $("#tempForm").attr("action", "/www.iosMessage.com&" + msg + "&" + phone);
            $("#tempForm").submit();
        }
        $('.unicom,.cover').hide();
    }

    

    function bind() {
    	 
    
        if (form.password.value == "") {
           showLoader("服务密码不能为空！");
        } else {
            $('#bc-btn').removeAttr("onclick");
            $('#bc-btn').removeClass("js-btn");
            $('#bc-btn').addClass("js-btn yrz");
            $('#bc-btn').text("请等待");
            showLoader("请等待……");
            sub();
        }

    }
    var url = "${path}/credit-web2/mobileApproveGetCap";
    var clientType = '${clientType}';
    var code = '${code}';
    function sub(){
   	 $.ajax({
   			type : "post",
   			data: $("#form").serialize(),	
   			url : url,
   			success : function(ret) {
   				if(ret.code == '200'){	
   					 $("#bc-btn").attr('onclick',"");
   					 $("#bc-btn").html("认证中，请耐心等待");
   					queryTodo(ret.ext);
   				}else if(ret.code == '400'){
   					window.location.href ="${path}/credit-web2/jxlSuccess?clientType="+clientType+"&code="+ret.code;
   				}else{
   					showLoader(ret.msg);
   				}
   			},
   			error:function(ret){
   				showLoader("未知异常，请稍后重试！");
   			},
   			
   		});
   }

    function queryTodo(tid){
    	$("#imgCap").attr("src","");
    	
    	 $.ajax({
    			type : "post",
    			data: {tid:tid},	
    			url :"${path}/credit-web2/progress",
    			success : function(ret) {
    				var json = ret.ext;
    				showLoader(json.description);
    				if(json.phase_status == 'DOING'){
    					setTimeout(function(){
    						queryTodo(tid);
    						},2*1000);
    				}else if(json.phase_status=='WAIT_CODE'){
    					url = "${path}/credit-web2/mobileApproveSub/"+tid;
    					$("#bc-btn").attr('onclick',"bind2();");
    					$("#bc-btn").html("马上认证");
    					$("#yzm").val('');    				
    					$("#liCap").show();
    					//跳转到输入验证码页面
    					var input = json.input;
    					//showLoader(json.description);
    					if(input.type == 'img'){
    						$("#hid2").show();
    						$("#hid1").hide();
    						$("#imgCap").attr("src", "data:image/png;base64,"+input.value); 
    						$("#imgCap").css({"width": "4.5rem", "height": "1.6rem","margin":"0 0.3rem 0 0","float": "right"});
    				    }else if(input.type == 'sms'){
    				    	$("#yzm").val('');
    						$("#hid1").show();
    						$("#hid2").hide();
    					}else{
    						
    					}
    				} else if(json.phase_status=='DONE_SUCC'){
    					//回到APP,成功提交采集请求
    					//showLoader(json.can_leave);
    					if(tid!=null){
    						$("#bc-btn").html("已认证完成！");
    						saveTid(tid);
    					}else{
    						$("#bc-btn").html("认证失败，退出重新认证！");
    					}
    					
    				}else if(json.phase_status=='DONE_FAIL'){
    					//showLoader(json.description);
    					setTimeout("window.location.reload()",2000);
    					
    					
    				}else{
    					//showLoader(json.description)
    					$("#bc-btn").html("马上认证");
    					$("#bc-btn").attr('onclick',"bind();");
    				}
    			},error:function(ret){
    				showLoader("系统异常，请重新认证！");
    				window.location.reload();
    			}
    	 });
    }
    
    function bind2() {
    	if (form.yzm.value==""){
    		showLoader("验证码不能为空！");
    		return;
    	}
    	sub();
    }

    	function saveTid(tid) {
    		var deviceId ='${deviceId}';
    		var mobilePhone = '${mobilePhone}';
    		 $.ajax({
    				type : "post",
    				data: {'deviceId':deviceId,'mobilePhone':mobilePhone},
    				url :  "${path}/credit-web2/saveTid/"+tid,
    				success : function(ret) {
    					window.location.href ="${path}/credit-web2/jxlSuccess?clientType="+clientType+"&code="+ret.code;
    				},
    				error:function(ret){
    					showLoader("未知异常，请稍后重试！");
    				},
    				beforeSend: function(){
    					
    			    },
    			    complete: function(){
    			    }
    			});
    	}
</script>