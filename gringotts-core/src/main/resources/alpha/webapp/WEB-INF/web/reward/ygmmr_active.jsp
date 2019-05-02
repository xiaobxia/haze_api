<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
<meta charset="utf-8">
<title>借钱不用还</title>
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
<style>
.nw_top{ background:url(${basePath}/ygmmr/images/new/nw_icon1.png) no-repeat center;  -webkit-background-size:100% auto; background-size:100% auto; height:16rem; position:relative;}
.nw_top h2{ position:absolute; font-size:0.6rem; color:#d8f2fe; width:100%; text-align:center; bottom:1rem;}
.nw_amo img{ width:100%;}
.nw_foot{ margin:1rem 0.75rem 0 0.75rem; background-color:#217ffd;border-radius: 0 0 0.5rem 0.5rem;}



.tab_b{ padding-bottom:0.5rem; min-height:5.6rem;}
.tab_b h2{ color:#f74f50; font-size:0.9rem; text-align:center; height:2.1rem; line-height:2.1rem; background-color:#fde000; border-radius: 0.5rem 0.5rem 0 0;}
.tab_b table{ margin:0 4%; width:92%;}
.tab_b .bott_b{margin:0 0.8rem; border-bottom:1px dashed #bdd9fe;}
.tab_b tr th{ width:33.3%; text-align:center; font-size:0.7rem; color:#fff; height:1.8rem; line-height:1.8rem;}
.tab_b tr td{ width:33.3%; text-align:center; font-size:0.65rem; color:#fff; line-height:1.4rem;}
.tab_b p{ text-align:center; color:#fff; font-size:0.65rem;}
.tab_b .zw_xx{ margin-top:3rem;}


.side{ width:1.5rem; background-color:#000;filter:alpha(opacity:30); opacity:0.5;  -moz-opacity:0.5;-khtml-opacity: 0.5; border-radius:0.3rem 0 0 0.3rem; position:fixed;right: 50%; margin-right:-9.4rem; height: 4rem;top: 50%; margin-top: 4rem;}
.side a{ width:1rem; color:#fff; font-size:0.65rem; text-align:center; padding:0.5rem 0.25rem; line-height:0.8rem;position: absolute;right: 0;top: 50%; margin-top: -2rem; z-index:33;}

#ttc{ width:100%; height:100%; background-color:#000; opacity:0.3; filter:alpha(opacity=30); position:fixed; left:0; top:0; z-index:3; }
.wind{ width:15rem; height:auto;  border-radius:0.5rem; top:50%; left:50%; margin-top:-50%; margin-left:-7.5rem; position:fixed; background:#3b8dfb; z-index:4;}
.wind h2{ height:2.25rem; line-height:2.25rem; color:#fff; font-size:0.9rem; text-align:center; position:relative; background-color:#509afd; border-radus:0.3rem;border-radius:0.5rem 0.5rem 0 0;}
.wind h2 i{ position:absolute; background:url(${basePath}/ygmmr/images/new/close.png) no-repeat center;-webkit-background-size:1.3rem auto; background-size:1.3rem auto; height:1.3rem; width:1.3rem; right:0.75rem; top:0.475rem;}
.wind ul{ margin:0 0.8rem; padding:0.3rem 0;}
.wind ul li{ padding-bottom:0.5rem;}
.wind ul li p{ font-size:0.65rem; color:#fff; line-height:1rem;}
.wind i{ display:block; font-size:0.55rem; color:#c5d6ec; margin-left:0.8rem; margin-bottom:0.5rem;}


#ttc1{ width:100%; height:100%; background-color:#000; opacity:0.6; filter:alpha(opacity=60); position:fixed; left:0; top:0; z-index:3; }
.hd_j{ width:15rem; height:auto;  border-radius:0.5rem; top:50%; left:50%; margin-top:-20%; margin-left:-7.5rem; position:fixed; background:#3b8dfb; z-index:4;}
.hd_j h2{ height:2.25rem; line-height:2.25rem; color:#fff; font-size:0.9rem; text-align:center; position:relative; background-color:#3b8dfb; border-radus:0.3rem;border-radius:0.5rem 0.5rem 0 0;}
.hd_j h2 i{ position:absolute; background:url(${basePath}images/new/close.png) no-repeat center;-webkit-background-size:1.3rem auto; background-size:1.3rem auto; height:1.3rem; width:1.3rem; right:0.75rem; top:0.475rem;}
.hd_j  p{ color:#fff; margin:0 5%; font-size:0.6rem; text-align:center;}
.hd_j a{ display:block; color:#fff; font-size:0.75rem; background-color:#f39801; width:5rem; text-align:center; height:1.6rem; line-height:1.6rem; margin:1rem auto; border-radius:0.3rem;}
</style>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
  <!-- main start -->
  <div class="wrapper" style="background-color:#3b8dfb; padding-bottom:2rem;">
    <div class="nw_top">
      <h2>活动时间：2017.03.18-2017.03.24</h2>
    </div>
    <div class="nw_amo"><img src="${basePath}/ygmmr/images/new/nw_icon2.png"></div>
    <div class="nw_foot" id="nw_foot_id">
      <div class="tab_b">
        <h2>${nowdate}免还名单</h2>
       <c:if test="${not empty nowlist}">
          <table>
            <tr class="bott_b">
              <th>用户名</th>
              <th>手机号</th>
              <th>借款金额</th>
            </tr>
          <c:forEach items="${nowlist}" var="item" >
             <tr>
              <td>${item.userName}</td>
              <td>${item.userTelephone}</td>
              <td>${item.loanAmount}元</td>
            </tr>
          </c:forEach>
          </table>
       </c:if>
       <c:if test="${empty nowlist}">
             <p class="zw_xx">暂无中奖信息</p>
             <p>每日12点前公布昨日中奖用户名单</p>
      </c:if>
     </div>
    </div>
    <div class="side"><a rel="external" href="javascript:;">查看规则</a></div>
    <div id="ttc" style="display:none;"></div>
    <div class="wind" style="display:none;">
      <h2>活动规则<i id="close"></i></h2>
      <ul>
        <li>
          <p>1.活动对象：所有人</p>
        </li>
        <li>
		<p>2.活动时间：2017.03.18-2017.03.24</p>
        </li>
        <li>
          <p>3.规则详情：</p>
          <p>①每日系统会记录成功借款用户的先后顺序，其中后四位相同数字的用户会成为本日的幸运用户，次日中午12:00前在活动区公布幸运名单，同时会以短信的方式通知幸运用户 </p>
          <p>②幸运用户的该笔借款平台会自动帮您还款</p>
        </li>
        <li><p>4.用户通过欺骗、造假等方式参与活动的，将取消活动资格，并追究法律责任</p></li>
      </ul>
      <i>*活动解释权归小鱼儿平台所有</i>
    </div>
  </div>
  <!-- main end -->
  

<div id="ttc1" style="display:;"></div>
  <div class="hd_j" style="display:;">
  <h2><i id="close1"></i></h2>
  <p>本活动已结束，请关注平台其它活动！</p>
  <a rel="external" href="javascript:;">朕知道了</a>
  </div>
</div>
<script>
$(document).ready(function(e) {
var x="";
var day=new Date().getDate();


if (day>24)

  {
  $('.hd_j').show();
  $('#ttc1').show();
  }
else
  {
   $('.hd_j').hide();
   $('#ttc1').hide();
  }
  	});
</script>
<script>
$(document).ready(function(e) {
    $('.side a').click(function(e) {
		$('.wind').show();
		$('#ttc').show();
		$('.side').hide();
	});	
	 $('#close').click(function(e) {
		$('.wind').hide();
		$('#ttc').hide();
		$('.side').show();
		
	});
	 $('#close1').click(function(e) {
			$('.hd_j').hide();
			$('#ttc1').hide();

		});	
	 $('.hd_j a').click(function(e) {
			$('.hd_j').hide();
			$('#ttc1').hide();

		});
	 
		var now = new Date();
		var beginTime=new Date("2017/03/19 00:00:00");
		if(Date.parse(now)>=Date.parse(beginTime)){
			$("#nw_foot_id").css('display','block');
		}else{
			$("#nw_foot_id").css('display','none');
		}
				
});


</script>
<script >
window.onload=init;
function init(){
var kh_1=document.getElementById("kh_1");
var kh_2=document.getElementById("kh_2");
var kh_3=document.getElementById("kh_3");


var kh1=document.getElementById("kh1");
var kh2=document.getElementById("kh2");
var kh3=document.getElementById("kh3");


kh_2.onclick=function(){
	kh2.style.display="block";
	kh_2.className="kh_z";
	kh1.style.display="none";
	kh_1.className="";
	kh3.style.display="none";
	kh_3.className="";
	}

kh_1.onclick=function(){
	kh2.style.display="none";
	kh_2.className="";
	kh1.style.display="block";
	kh_1.className="kh_z";
	kh3.style.display="none";
	kh_3.className="";
	}

kh_3.onclick=function(){
	kh2.style.display="none";
	kh_2.className="";
	kh1.style.display="none";
	kh_1.className="";
	kh3.style.display="block";
	kh_3.className="kh_z";
	}
	


}
</script>
</body>
</html>