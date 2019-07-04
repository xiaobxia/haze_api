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
    <title>借款详情</title>
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
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
</head>
<body>
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page">
    <%--<div class="header">
        <a class="back_arr" href="javascript:void(0);"></a>
        <h1>借款详情</h1>
    </div>--%>
    <!-- main start -->
    <div class="wrapper">
        <div class="jk-box">
            <h2>借款详情</h2>
            <ul class="jk-list">
                <li>
                    <div class="line-bg" style="height: ${(fn:length(list) - 1) * 3.4}rem;">
                        <div class="line-percent" style="height:1.5rem;"></div>
                    </div>
                    <dl class="jk-step">
                        <c:forEach items="${list}" varStatus="items">
                            <dd <c:if test="${list[fn:length(list) - items.index - 1].tag == 1}"> class="success"</c:if>>
                                <em></em>
                                <div class="jk-step-info">
                                    <h3>${list[fn:length(list) - items.index - 1].title}</h3>
                                    <p>${list[fn:length(list) - items.index - 1].body}</p>
                                </div>
                            </dd>
                        </c:forEach>
                    </dl>
                </li>
                <li>
                    <p>借款金额<span><fmt:formatNumber pattern='###,###,##0.00' value="${bo.moneyAmount / 100.00}"/></span>元</p>
                    <p>实际到账<span><fmt:formatNumber pattern='###,###,##0.00' value="${bo.intoMoney / 100.00}"/></span>元</p>
                    <p>服务费用<span><fmt:formatNumber pattern='###,###,##0.00' value="${bo.loanInterests / 100.00}"/></span>元</p>
                    <p>借款期限<span>${bo.loanTerm}</span>${LOAN_METHED[bo.loanMethod]}</p>
                    <p>申请日期<span><fmt:formatDate value="${bo.createdAt }" pattern="yyyy-MM-dd"/></span></p>
                    <p>收款银行<span>${info.bankName}（${info.card_no}）</span></p>
                    <p class="xy">
                        <em>借款合同</em>
                        <span>
                            <a href="${path}/credit-loan/agreement?borrowId=${bo.id}&userId=${bo.userId}" rel="external">《借款协议》</a>
                            <a href="${path}/agreement/platformServiceNew?borrowId=${bo.id}&userId=${bo.userId}" rel="external">《平台服务协议》</a>
                        </span>
                    </p>
                    <p>
                        <span>
                            <!-- <a href="${path}/agreement/withholdAuthorization?borrowId=${bo.id}&userId=${bo.userId}" rel="external" style="margin-left: 3.4rem;">《授权扣款协议》</a> -->
                        </span>
                    </p>
                </li>
                <c:if test="${repayment.status == 21 || repayment.status == 23 || repayment.status == -11 || repayment.status == 30 || repayment.status == 34}">
                    <li>
                        <p>待还金额<span><fmt:formatNumber pattern='###,###,##0.00' value="${(repayment.repaymentAmount - repayment.repaymentedAmount) / 100.00}"/></span>元</p>
                        <p>已还金额<span><fmt:formatNumber pattern='###,###,##0.00' value="${repayment.repaymentedAmount / 100.0}"/></span>元</p>
                        <p>最迟还款日期<span><fmt:formatDate value="${repayment.repaymentTime }" pattern="yyyy-MM-dd"/></span></p>
                        <p>实际还款日期
                            <span>
                                <c:if test="${empty repayment.repaymentRealTime}">-</c:if>
                                <c:if test="${not empty repayment.repaymentRealTime}"><fmt:formatDate value="${repayment.repaymentRealTime }" pattern="yyyy-MM-dd"/></c:if>
                            </span>
                        </p>
                    </li>
                </c:if>
            </ul>
        </div>
        <c:if test="${repayment.status == 21 || repayment.status == 23 || repayment.status == -11}">
            <c:if test="${not applying eq 'true'}">
                <div class="btn-box clearfix">
                    <a data-ajax="false" onclick="toRenewal()" href="javascript:;">申请续期</a>
                    <a data-ajax="false" href="javascript:;" onclick="toPay()">立即还款</a>
                </div>
            </c:if>
        </c:if>
        <div class="sure-tc">
            <p>您已逾期，不能申请续期，请先去还款</p>
            <div class="btn-both clearfix" style="padding:0 0.5rem;">
                <a data-ajax="false"  href="javascript:;" onclick="toPay()" class="btn sure-btn" style="width:7rem;float:left;border:1px solid #eba220;height:2rem;line-height:2rem;">去还款</a>
                <a href="javascript:;" onclick="closeSureTc();" class="btn sure-btn" style="width:7rem;float:right;background:none;border:1px solid #eba220; color:#eba220 !important;height:2rem;line-height:2rem;">取消</a>
            </div>
        </div>
        <!-- 遮罩层 -->
        <div class="cover"></div>
    </div>
    <div class="popup" id="loading" style="display: none;">
    <div class="overlay">
        <p class="tips-msg">正在提交，请稍后…</p>
    </div>
    <div class="spin" id="preview">
    </div>
</div>
    <!-- main end -->
<form action="" method="post"  id="payPath" style="display: none;">
    <input type="text" name="VERSION" id="VERSION">
    <input type="text" name="MCHNTCD" id="MCHNTCD">
    <input type="text" name="FM" id="FM">
    <input type="text" name="ENCTP" id="ENCTP">
</form>
</div>
<script type="text/javascript">
	function toPay(){
		 $('#loading').show();
        var url = '${path}/repayment/repay-bank-card';
        $.post(url, {id:'${bo.id}',sgd:'${sgd}','errorReturnUrl':'${errorReturnUrl }','successReturnUrl':'${successReturnUrl}',rtl:'${rtl}'} , function(data){
            $('#loading').hide();
            if(data.successed){
                $("#payPath").attr("action", data.paramsMap.payPath);
                $("#VERSION").val(data.paramsMap.VERSION);
                $("#MCHNTCD").val(data.paramsMap.MCHNTCD);
                $("#FM").val(data.paramsMap.FM);
                $("#ENCTP").val(data.paramsMap.ENCTP);
                $("#payPath").submit();
            }
            else{
                showLoader(data.msg);
            }
        });
	}
    function toRenewal(){
        $.post("${path}/repayment/renewal-whether", {id:'${repayment.id}',sgd:'${sgd}',errorReturnUrl:'${errorReturnUrl}',successReturnUrl:'${successReturnUrl}',rtl:'${rtl}'} , function(data){
            if(data.successed){
               window.location.href="${path}/repayment/renewal?id=${bo.id}&sgd=${sgd}&errorReturnUrl=${errorReturnUrl }&successReturnUrl=${successReturnUrl}&rtl=${rtl}";
            }
            else if('-101' == data.code){
                $('.sure-tc,.cover').show();
            }
            else{
                showLoader(data.msg);
            }
        });
    }
    function closeSureTc(){
        $('.sure-tc,.cover').hide();
    }
</script>
</body>
</html>