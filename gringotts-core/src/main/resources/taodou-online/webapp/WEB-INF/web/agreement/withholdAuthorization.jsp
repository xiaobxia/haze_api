<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<title>授权扣款委托书</title>
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
</head>
<body style="background:#fff;">
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
        <%--<div class="header">
            <a class="back_arr" href="javascript:void(0);"></a>
            <h1>授权扣款委托书</h1>
        </div>--%>
        <!-- main start -->
        <div class="wrapper">
          <div class="agreement">
            <h2>授权扣款委托书</h2>
            <p><em>授权人：</em><span>${user.realname}</span></p>
            <p><em>被授权人：</em><span><!-- 上海利全信息科技服务有限公司 -->${companyTitle}</span></p>
            <br>
            <div class="jy-txt">
            <p>授权人同意并签署本委托书，在此通过本委托书作出如下授权与承诺：</p>
            </div>
            <br>
            <div class="em-txt">
           	<p>一、授权人同意并授权被授权人针对授权人通过使用被授权人或被授权人合作方运营的网络平台（以下简称“平台”）发起的所有借款项目（包括本委托书生效之前及其之后的借款项目，具体以授权人签订的各《借款协议》、《平台服务协议》为准），协同与被授权人或被授权人合作方合作的指定第三方银行或第三方支付机构（以下简称“被授权人指定第三方”）从授权人通过本委托书指定银行卡（银行卡账户信息见第十五条）进行资金的代扣、代还、代付及划转各《借款协议》、《平台服务协议》项下的应付款项。</p>
            <br>
            <p>二、授权人同意，在各《借款协议》及《平台服务协议》生效后，被授权人及被授权人指定第三方有权依照各《借款协议》及《平台服务协议》约定的期限和金额代扣授权人指定银行卡账户中的相应资金。</p>
            <br>
            <p>三、本委托书项下的应付款项包括但不限于授权人通过使用平台发起的全部借款相关款项，支付顺序依次：</p>
            <p>1、没有逾期的，还款顺序依次为利息、本金；</p>
            <p>2、选择提前结清的，需一次性偿还利息和本金；</p>
            <p>3、发生逾期时还款顺序依次为逾期管理费、利息、本金</p>
            <br>
            <p>四、授权人承诺，本委托书第十五条记载的授权银行卡账户是以本人真实姓名开立的合法、有效的银行卡账户，授权人同意本委托书第一条、第二条约定的资金代扣及转账优先于该账户其他任何用途的支付。</p>
            <br>
            <p>五、授权人同意，在借款成功后，如因银行、第三方支付等任何原因导致被授权人及被授权人指定第三方无法将款项代还至授权指定的银行卡账户中，由此引起的任何后果，由授权人承担。</p>
            <br>
            <p>六、授权人知悉被授权人及被授权人指定第三方对此项资金代扣、代还、代付及划转服务不收费，被授权人及被授权人指定第三方会尽其最大努力及时完成此项资金划转服务，但被授权人不对此项服务作任何永久性承诺。与本委托书项下的资金代扣、代还、代付及划转服务相关的任何责任，与被授权人及被授权人指定第三方无关，被授权人及被授权人指定第三方亦无义务承担。</p>
            <br>
            <p>七、授权人在指定银行卡账户中必须留有足够余额，否则因账户余额不足或不可归责于被授权人的任何事由，导致无法及时扣款或扣款错误、失败，责任由授权人自行承担。</p>
            <p>余额不足的，授权人知悉并同意被授权人会就授权人全部应还款项持续进行代扣，直至授权人还清全部应还款项。</p>
            <br>
            <p>八、各《借款协议》的出借人按照相关协议约定和平台规则转让各《借款协议》项下的债权的，不影响本委托书的有效性。</p>
            <br>
            <p>九、授权人针对授权人指定的银行卡账户向被授权人进行授权后，即视为授权人就该银行卡在授权人通过平台发起的所有借款项目范围内进行了授权，不因授权人后续更换绑定银行卡，或对其他银行卡进行授权而无效或产生任何影响。</p>
            <br>
            <p>十、本委托书为授权人对被授权人从其授权的指定账户中扣款和/或向该账户转账的授权证明，不作为收付现金的直接凭据。</p>
            <br>
            <p>十一、凡本委托书中未约定的事项，适用《借款协议》与《平台服务协议》的约定，凡本委托书中出现的与各《借款协议》、《平台服务协议》相同的词语或术语，如果在本委托书中无特别定义，适用各《借款协议》、《平台服务协议》中相同词语和术语的定义、涵义或解释，本委托书的规定与各《借款协议》、《平台服务协议》不一致的，以本委托书的规定为准。</p>
            <br>
            <p>十二、授权人发起终止授权或变更账户、通讯地址时，在当期款项支付日2个工作日前通知被授权人并完成信息更新，否则自行承担所造成的风险损失。</p>
            <br>
            <p>十三、授权人保证本委托书的真实性、合法性以及有效性等，被授权人依据本委托书进行的操作引起的一切法律纠纷或风险，由授权人独立承担或解决。</p>
        	<br>
            <p>十四、本委托书自授权人确认同意起生效，至授权人通过平台签订的全部《借款协议》《平台服务协议》履行完毕，所有款项全部还清时终止。</p>
        	<br>
            <p>十五、授权人资料：</p>
            <br>
            <table class="bg" width="100%" border="1">
            <tr>
                <td style="border-right:none;"></td>
                <td style="border-left:none;">授权人资料</td>
              </tr>
              <tr>
                <td>姓名</td>
                <td>${user.realname}</td>
              </tr>
              <tr>
                <td>身份证号码</td>
                <td>${user.idNumber}</td>
              </tr>
              <tr>
                <td>联系电话</td>
                <td>${user.userPhone}</td>
              </tr>
              <tr>
                <td>借记卡户主</td>
                <td>${user.realname}</td>
              </tr>
              <tr>
                <td>借记卡开户银行</td>
                <td>${cardInfo.bankName}</td>
              </tr>
              <tr>
                <td>借记卡账号</td>
                <td>${cardInfo.card_no}</td>
              </tr>
            </table>

            <p>（以下无正文）</p>
        	<br>
            </div>
            <p><em>授权人：</em><span>${user.realname}</span></p>
            <p><em>被授权人：</em><!-- 上海利全信息科技服务有限公司 -->${companyTitle}</p>
            <br>
            <c:if test="${bo != null}">
            <p class="rig">
            <c:choose>
      			<c:when test="${bo.status>=21}">通过</c:when>
      			<c:otherwise>失败</c:otherwise>
      		</c:choose></p>
            <p class="rig"><fmt:formatDate value="${bo.loanTime}" pattern="yyyy年MM月dd日"/></p>
            </c:if>
          </div>
        </div>
        <!-- main end -->
    </div>    
</body>
</html>