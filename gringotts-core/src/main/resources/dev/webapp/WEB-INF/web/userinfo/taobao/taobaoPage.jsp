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
<title>淘宝授权</title>
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
<link rel="stylesheet" type="text/css" href="${basePath}/css/validate.css" />

<script type="text/javascript" src="${basePath }/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
<script type="text/javascript" src="${basePath }/js/base.js"></script>
<script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
<script type="text/javascript" src="${basePath }/js/jquery-mvalidate.js" ></script>
    <style>
        html, body,ul,li,p,i,h4 {
            list-style: none;
            font-family: '微软雅黑', Arial, "\5FAE\8F6F\96C5\9ED1";
            /*font-size: 1rem;*/
        }
        * {
            margin: 0;padding: 0;
            -webkit-user-select: auto;
        }
        .fixed-encrypt-protect p {
            font-size: 18px;
            color: #31C27C;
        }
        .fixed-encrypt-protect p img {
            width: 45px;
            vertical-align: -8px;
            margin-right: 20px;
        }

        .g-uncredit .icon-box {
            text-align: center;
            background: url("${basePath}/images/bg_taobao_nocredit.png") no-repeat;
            background-size: 100%;
        }
        .g-pannel .icon-box {
            padding:57px 0 70px;
        }
        .g-pannel .icon-box img {
            margin:0 auto;
        }
        .g-pannel .go-credit {
            padding: 0 30px;
        }
        .g-pannel h5 {
            color: #333;
            font-size: 18px;
            text-align: center;
            margin:20px 0 50px;
            font-weight: 500;
        }
        .g-pannel .go-credit a {
            display: block;
            width: 100%;
            background: #31c27c;
            color: #fff;
            padding: 8px 0;
            text-align: center;
            border-radius: 60px;
            font-size: 20px;
            text-decoration: none;
            letter-spacing: 1px;
        }
        /*  授信中  */
        .g-credit .icon-box {
            text-align: center;
            background: url("${basePath}/images/bg_taobao_conductx.png") no-repeat;
            background-size: 100%;
        }
        .g-credit .icon-box img, .g-credited .icon-box img {
            width:114px;
        }
        .g-credit .warm-tips {
            margin-top:-20px;
        }
        .g-credit .warm-tips p {
            font-size: 16px;
            padding: 0 40px;
            color: #FF8B10;
            text-align: center;
            line-height: 30px;
        }
        .g-credited .icon-box {
            text-align: center;
            background: url("${basePath}/images/bg_taobao_complete.png") no-repeat;
            background-size: 100%;
        }
    </style>
</head>


<body>
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page" style="background: #fff;">
        <!-- main start -->
        <div class="wrapper">
          <div class="sq-wrap">
              <c:if test="${code == 200}">
                  <!--  已授信  -->
                  <div class="g-pannel g-credited">
                      <div class="icon-box"><img src="${basePath}/images/ic_taobao_complete.png" alt=""></div>
                      <h5>恭喜！您已完成淘宝授信！</h5>
                  </div>
              </c:if>
              <c:if test="${code == 100}">
                   <!--  未授信  -->
                   <div class="g-pannel g-uncredit">
                       <div class="icon-box"><img src="${basePath}/images/ic_taobao_nocredit.png" alt="" style="width:85px;"></div>
                       <h5><c:choose><c:when test="${msg} eq ''">您暂时还没有完成淘宝授信</c:when><c:otherwise>${msg}</c:otherwise></c:choose></h5>
                       <div class="go-credit"><a href="javascript:;" class="start-btn">授信</a></div>
                   </div>
              </c:if>
              <c:if test="${code == 300}">
                  <!--  授信中  -->
                  <div class="g-pannel g-credit">
                      <div class="icon-box"><img src="${basePath }/images/ic_taobao_conduct.png" alt=""></div>
                      <h5>淘宝授信中……</h5>
                      <div class="warm-tips">
                          <p>温馨小提示：<br/>感谢您对小鱼儿的信任，授信操作可能需要一些时间，请您耐心等候……<br/>等候期间您可以先进行下一步认证!</p>
                      </div>
                  </div>
              </c:if>

              <span style="font-size: 16px;"><img src="${basePath}/images/dp.png" style="vertical-align: -6px;width:25px;margin: -0.2rem 0.15rem 0 0;" alt="">银行级数据加密防护</span>
          </div>
          
        </div>
        <!-- main end -->
        
    </div>
    <script type="text/javascript">
        var flag = true;
        $(function(){
            $(".start-btn").on("click",function(){
                if(!flag){
                    $.mvalidateTip("请勿频繁操作");
                    return;
                }else{
                    flag = false;
                }
                show_loading("授权中，请稍等");
                $.post('${path}/creditreport/taobao-request', {userId:'${userId}',clientType:'${clientType}',mobilePhone:"${mobilePhone}",deviceId:'${deviceId}'} , function(data){
                    hide_loading();
                   if(data.code == "0"){
                        window.location.href = data.msg;
                    }else{
                        $.mvalidateTip(data.msg);
                    }
                });

            });
        });
        function show_loading(msg){
            $.mobile.loading('show', {
                text: msg, // 加载器中显示的文字
                textVisible: true, // 是否显示文字
                theme: 'b',        // 加载器主题样式a-e
                textonly: false,   // 是否只显示文字
                html: ""           // 要显示的html内容，如图片等
            });
        }

        function hide_loading(){
            // 隐藏加载器
            $.mobile.loading('hide');
        }
    </script>
</body>
</html>