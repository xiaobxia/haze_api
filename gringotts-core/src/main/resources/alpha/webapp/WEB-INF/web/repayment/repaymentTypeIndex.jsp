<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<script>
     $(document).ready(function(e) {
            $('.yh_in h2').click(function(){
				$(this).next().slideToggle("slow");		
				 $(this).toggleClass("jt_s");
		 	$('.jt-xs i').show();
			});
            setTitles();
            
        });
     function setTitles(){
    	 var type='${type}';
    	 var id = '';
    	 var sId='';
    	 var hId='';
  
    	 if(type && (type == 1 || type == 2)){
    		 id = "#jf_1";
    		 sId = '#jf1';
    		 hId = '#jf2';
    	 }else if(type && type == 3){
    		 id = "#jf_2";
    		 sId = '#jf2';
    		 hId = '#jf1';
    	 }
    	 $(id).toggleClass("jf_z");
    	 $(sId).show();
		 $(hId).hide();
		 $('#h_'+type).toggleClass("jt_s");
		 $('#d_'+type).show();
     }
          </script>
          </head>
          <body>
<!-- <div class="header"> <a class="back_arr" href="javascript:void(0);"></a>
            <h1>还款方式</h1>
          </div> -->
<!-- main start -->
<div class="wrapper">
    <div class="jf">
        <div id="jf">
            <ul>
                <li class="" id="jf_1"><a><img src="${basePath}/images/yhk_icon.png">银行卡</a></li>
       <%--  <li class=""  id="jf_2"><a><img src="${basePath}/images/zfb_icon.png">支付宝</a></li> --%>
            </ul>
        </div>
        <div id="jf1" style="display:none;">
            <div class="yh_in">
                <h2 id="h_1"><i></i>1.一键还款（推荐）</h2>
                <div id="d_1" class="yh_in1" style="display:none;">
                    <ul>
                        <li><h3>第一步：在还款页点击本次需要还款的订单</h3></li>
                        <li><img src="${basePath}/images/dh41.png"></li>
                        <li><h3>第二步：点击立即还款</h3></li>
                        <li><img src="${basePath}/images/dh42.png"></li>
                        <li><h3>第三步：选择银行卡支付</h3></li>
                        <li><img src="${basePath}/images/dh43.png"></li>
                        <li><h3>第四步：输入交易密码即可还款</h3></li>
                        <li><img src="${basePath}/images/dh44.png"></li>
                    </ul>
                </div>
                <h2  id="h_2"><i></i>2.到期自动代扣（推荐）</h2>
                <div  id="d_2" class="yh_in2" style="display:none;">
                    <li>
                    <p>在还款日之前（含当日），把本次所需还款的金额存入绑定的银行卡内，我司会在还款日的05点，17点进行扣款，如成功，将会以短信形式通知您。</p>
                    </li>
                </div>
      <!--  <h2><i></i>4.转账（不推荐）</h2>
        <div class="yh_in2" style="display:none;">
        <li>
                    <p>需您打款至我司对公账户进行还款，需提供包含转账金额及转入银行卡信息的截图，并告知客服注册手机号。</p>
                    <p style="text-align:center;">入款银行账号：中国工商银行股份有限公司上海市四平支行  </p>
                    <p style="text-align:center;">1001171609200156829</p>
                    </li>
                  </div>  -->
            </div>
            <div class="ts">
                <ul>
          <!--           <li>
            <h3><em>!</em>邮政储蓄银行卡用户</h3>
          </li>
                    <li>
            <p>由于邮政储蓄银行不支持充值还款和自动代扣，所以可通过以下1种方式进行还款：</p>
          </li> -->
         <!--            <li>
            <p>1、支付宝转账还款</p>
          </li> -->
             <!--        <li>
            <p>2、银行卡转账至对公账户</p>
          </li> -->
                    <li><p>1、更改绑定的银行卡后进行还款</p></li>
                </ul>
            </div>
        </div>
  <%--   <div id="jf2" style="display:none;">
                <div class="yh_in">
        <h2 id="h_3"><i></i>1.支付宝转账</h2>
        <div id="d_3" class="yh_in1" style="display:none;">
                    <ul>
            <li>
                        <h3>第一步：在支付宝首页点击转账</h3>
                      </li>
            <li><img src="${basePath}/images/zf1_icon.png"></li>
            <li>
                        <h3>第二步：选择转到支付宝账户</h3>
                      </li>
            <li><img style="height:6.25rem;" src="${basePath}/images/zf2_icon.png"></li>
            <li>
                        <h3>第三步：输入972802937@qq.com（推荐）或输入xjx@xianjinxia.com</h3>
                      </li>
            <li style=" height:13.5rem;"><img style=" float:left; width:46%;" src="${basePath}/images/zf4_icon1.png"><img style=" float:right; width:46%;" src="${basePath}/images/zf4_icon.png"></li>
            <li>
                        <h3>第四步：确认公司名称，输入需还金额，并备注现金侠注册的姓名和手机号</h3>
                      </li>
            <li style=" height:13.5rem;"><img style=" float:left; width:46%;" src="${basePath}/images/zf3_icon1.png"><img style=" float:right; width:46%;" src="${basePath}/images/zf3_icon.png"></li>
            <li>
                        <h3>第五步：确认转账之后，大约半小时左右，系统即会自动更新还款状态。</h3>
                      </li>
          </ul>
                  </div> --%>
    </div>
</div>
  </div>
            <!-- main end --> 
            <!-- footer start --> 
            
            <!-- footer end --> 
          </div>
<script >
window.onload=init;
function init(){
var jf_1=document.getElementById("jf_1");
var jf_2=document.getElementById("jf_2");


var jf1=document.getElementById("jf1");
var jf2=document.getElementById("jf2");


jf_2.onclick=function(){
	jf2.style.display="block";
	jf_2.className="jf_z";
	jf1.style.display="none";
	jf_1.className="";
	
	}

jf_1.onclick=function(){
	jf2.style.display="none";
	jf_2.className="";
	jf1.style.display="block";
	jf_1.className="jf_z";
	
	}
	

}
</script>
</body>
</html>
