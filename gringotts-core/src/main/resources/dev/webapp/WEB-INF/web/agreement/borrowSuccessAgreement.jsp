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
<!--${appName}用户借款协议 -->
<title>个人借款协议</title>
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
<style type="text/css">
.butt{ width:1.5rem; background-color:#000;opacity:0.5;filter:alpha(opacity:50);border-radius: 0.3rem 0 0 0.3rem; position: fixed;right: 50%;margin-right: -9.4rem; height: 2.5rem;  top:15%;  margin-top:-1.25rem;}
.butt a{width: 1rem;color: #fff;font-size: 0.65rem;text-align: center; padding: 0.5rem 0.25rem;line-height: 0.8rem; position: absolute; right: 0; top: 0%; margin-top:0rem; z-index: 33;}
</style>
<script type="text/javascript">
	function agreementExport(){
		document.getElementById("frm").submit();
	}
</script>
<body style="background:#fff;">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
<!--   <div class="header"> <a class="back_arr" href="javascript:void(0);"></a> -->
<!--     <h1>借款协议</h1> -->
<!--   </div> -->
  <!-- main start -->
  <div class="wrapper">
    <div class="agreement">
      <h2>个人借款协议</h2>
      <!--
      <div class="butt"><c:if test="${borrow.status>=21}"><a href="javascript:agreementExport()">下载</a></c:if></div> -->
      <form id="frm" method="post" action="/xjx/credit-loan/agreement_export" onsubmit="return navTabSearch(this);">
      	<input type="hidden" name="borrowId" id="borrowId" value="${borrow.id}" />
      	<input type="hidden" name="userId" id="userId" value="${user.id}"/>
      </form>
      <br>
      <p><em>编号：</em><span>${borrow.id}</span></p>
      <p><em>甲方（借款人）：</em><span>${user.realname}</span></p>
      <p><em>身份证号码：</em><span>${user.idNumber}</span></p>
      <p><em>乙方（出借人）：</em><span>蔡坤燚</span></p>
      <p><em>身份证号码：</em><span>31010****0414</span></p>
      <br>
      <p>借款标的明细</p>
      <br>
      <table class="bgg" width="100%" border="1">
        <tr>
          <th>本金金额人民币</th>
          <th>大写人民币</th>
          <th>借款用途</th>
          <th>年化利率</th>
          <th>借款起息日</th>
          <th>借款到期日</th>
          <th>还款方式</th>
        </tr>
        <tr>
           <td>${borrow.moneyAmount/100}元</td>
          <td>${borrowMonery}</td>
          <td>个人消费</td>
          <td><c:if test="${borrow.loanTerm == '7'}">18%</c:if><c:if test="${borrow.loanTerm == '14'}">18%</c:if></td>
          <td><fmt:formatDate value="${borrow.loanTime}" pattern="yyyy年MM月dd日"/></td>
          <td><fmt:formatDate value="${borrow.loanEndTime}" pattern="yyyy年MM月dd日"/></td>
          <td>一次性还款</td>
        </tr>
      </table>
      <br>
      <div class="em-txt">
        <h4>第一条 借款</h4>
        <br>
        <p>1、借款标的各项相关内容以《借款标的明细》中列明的内容为准。</p>
        <p>2、借款期限是指《借款标的明细》中列明的借款起息日起至借款到期日。</p>
        <p>3、借款起息日是指乙方或乙方委托的第三方打款至甲方账户的日期。</p>
        <p>4、还款日是指根据本协议约定甲方将还款转至指定还款账户之日，为借款到期日。</p>
        <br>
        <h4>第二条 正常还款</h4>
        <br>
        <p>1、还款方式以《借款标的明细》中列明的方式为准，甲方须在还款日当日24:00前支付相当于《还款计划》中列明的包括本金、利息。</p>
        <p>2、甲方应通过app平台操作点击“还款”的方式进行还款，按时足额将还款金额支付至还款账户，由于余额不足、账户问题、甲方操作失误等原因导致划款失败的由甲方承担相应责任。</p>
        <br>
        <h4>第三条 逾期还款</h4>
        <br>
        <p>1、甲方未能在任何一个还款日24：00前足额支付还款金额至还款账户的，视作逾期还款，逾期还款情况下，甲方需支付逾期违约金，至甲方全额还款为止。逾期违约金为以逾期未还金额为基数按日收取，以当日0:00至24:00为一日，甲方应当每日向乙方支付未还金额的  3    %作为逾期违约金。</p>
        <p>2、甲方发生逾期超过3日，甲方同意将其信息纳入信用征信系统，由此产生的后果由甲方承担。</p>
        <p>3、甲方发生逾期情形的，甲方允许乙方将其信息提供给乙方授权的第三方，并接受乙方或授权第三方进行催收，催收方式包括但不限于：发送网站、APP推送通知，发送短信，发送函件、电话催收，提起诉讼等。</p>
        <br>
        <h4>第四条 提前还款</h4>
        <br>
        <p>1、甲方可以提前还款。但因受银行结算等原因影响，以APP平台操作界面展示为准。</p>
        <p>2、甲方选择提前还款的：甲方应一次性全额偿还按本协议应还的款项。</p>
        <br>
        <h4>第五条 债权、债务转让</h4>
        <br>
        <p>1、未经乙方事先书面（包括但不限于电子邮件等方式）同意，甲方不得将本协议项下的任何权利义务转让给任何合同外第三方。</p>
        <p>2、乙方可通过签订《债权转让协议》向第三方转让本协议项下权利义务，转让后甲方即向新的债权人履行本协议项下全部义务。</p>
        <br>
        <h4>第六条 资金通道服务 </h4>
        <br>
        <p>甲方已充分知晓并愿意在获得借款后第一时间支付乙方因借款所支出的资金通道费。</p>
        <br>
        <h4>第七条 承诺与保证</h4>
        <br>
        <p>1、甲方及乙方各自在此确认其为具有完全民事权利能力和完全民事行为能力的主体，有权签订并履行本协议，并充分知晓民间借贷行为可能存在的各类风险。</p>
        <p>2、甲方使用以本人名义在APP平台上注册的经过实名认证并设置个人账户密码后所有行为均视为甲方本人行为，包括但不限于订立本合同、申请贷款和归还贷款等，该等行为的法律后果均由甲方本人承担。</p>
        <p>3、乙方保证其所用于出借的资金来源合法，且乙方是该资金的合法支配权人。</p>
        <p>4、甲方应如实向乙方或乙方授权的第三方（银行、第三方支付机构、资产管理公司等）提供本人信息（包括但不限于姓名、身份证号、企业或组织名称、统一社会信用代码、联系方式、联系地址、职业信息、联系人信息等）以及借款用途等相关信息。</p>
        <p>5、甲方、乙方承诺并保证向其他各方提供的所有本人信息均真实、完整、有效、及时，不存在虚假记载、重大遗漏或误导性陈述。因上述任何本人信息及资料的变更、修改、停用等，至少提前2个工作日通知其他各方。</p>
        <p>6、如甲方或乙方变更账户信息（账户名称、账号等）、通讯地址的，应当至少在款项交付日前2个工作日通知对方。如因未能遵守上述承诺而导致的损失，由其自行承担。</p>
        <p>7、如发现：（1）甲方的行为数据或其他信息发生异常变化；或（2）甲方账户出现异常现象；或（3）甲方发生本合同约定的违约情形；或（4）甲方不履行或违反与乙方授权个人或机构间任何约定（如有），已经或可能影响甲方履行本合同项下义务，乙方或乙方授权方有权基于合理怀疑停止向甲方发放贷款或宣布贷款提前到期或对甲方的账户进行权限限制或扣划相应款项用于偿还本合同项下欠款。</p>
        <p>8、甲方承诺根据本协议列明的借款用途使用借款资金，并保证不挪用借款资金或将借款资金用于以下目的和用途：</p>
        <p><span>（1）以任何形式进入证券市场，或用于股本权益性投资；</span></p>
        <p><span>（2）用于吸毒、赌博；</span></p>
        <p><span>（3）用于洗钱、信用卡套现等不正当交易行为；</span></p>
        <p><span>（4）用于国家法律法规明令禁止或限制的各项其他活动。</span></p>
        <br>
        <h4>第八条 违约</h4>
        <br>
        <p>1、发生下列任何一项或几项情形的，视为甲方违约：</p>
        <p><span>（1）甲方违反其在本协议所做的任何承诺和保证的；</span></p>
        <p><span>（2）甲方未按协议约定用途使用借款的；</span></p>
        <p><span>（3）甲方提供虚假的信息；</span></p>
        <p><span>（4）甲方的任何财产遭受没收、征用、查封、扣押、冻结等可能影响其履约能力的不利事件，且不能及时提供有效补救措施或担保的；</span></p>
        <p><span>（5）甲方的财务状况出现影响其履约能力的不利变化，且不能及时提供有效补救措施或担保的；</span></p>
        <p><span>（6）借款人被宣告失踪、处于限制民事行为能力或丧失民事行为能力状态、被刑事监禁、或发生重大疾病、重大事故等可能危及本合同项下贷款安全的情况；</span></p>
        <p><span>（7）借款人卷入或即将卷入重大的诉讼或仲裁程序及其他法律纠纷，足以影响借款人的偿债能力；</span></p>
        <p><span>（8）借款人转移资产，以逃避债务的；</span></p>
        <p>2、若甲方违约或可能发生违约事件的，乙方或债权受让人有权采取下列任何一项或几项救济措施，包括但不限于：</p>
        <p><span>（1）立即暂缓、取消发放全部或部分借款；</span></p>
        <p><span>（2）宣告已发放借款全部提前到期，甲方应立即偿还所有应付款项；</span></p>
        <p><span>（3）解除本协议；</span></p>
        <p><span>（4）采取法律、法规以及本协议约定的其他救济措施。</span></p>
        <p>3、任何一方违约造成另一方损失的，违约方应当赔偿给守约方造成的全部损失（包括但不限于（1）因代偿债务发生的税费；（2）诉讼、仲裁过程中支出的费用，包括法院、仲裁机构收取的费用等；（3）审计费、拍卖费、律师费等中介费用；（4）交通费、餐费等必要差旅费用等）。</p>
        <br>
        <h4>第九条 通知与送达</h4>
        <br/>
        <p>1、本协议任何一方根据本协议约定做出的通知和/或文件均应以书面形式做出，可由专人送达、挂号邮递、特快专递或通过网络平台发布等方式传送，具体送达信息以甲方在平台预留信息为准。</p>
        <p>2、通知在下列日期视为送达：</p>
        <p><span>（1）短信、邮件发出即视为送达，专人递送的通知，在专人递送之交付日为有效送达；      </span></p>
        <p><span>（2）以挂号信（付清邮资）发出的通知，在寄出（以邮戳为凭）后的五（5）个工作日内为有效送达；</span></p>
        <p><span>（3）以特快专递（付清邮资）发出的通知，在寄出（以邮戳为凭）后的三个（3）工作日内为有效送达；</span></p>
        <p><span>（4）通过网络平台发布的方式通知的，在网络平台发布之日为有效送达。</span></p>
        <h4>第十条 法律适用和管辖</h4>
        <br>
        <p>本合同适用中华人民共和国法律。如果各方在本协议履行过程中发生争议，由乙方住所地人民法院管辖。诉讼期间，本合同不涉及争议部分的条款仍须继续履行。</p>
        <h4>第十一条 其他</h4>
        <p>1、本协议自借款人实际收到借款之日起生效。</p>
        <p>2、本协议中部分条款根据相关法律法规等的规定成为无效，或部分无效时，该等无效不影响本协议项下其他条款的效力，各方仍应履行其在本协议项下的义务。</p>
        <p>3、本协议以书面形式（电子平台）签署。</p>
        <br>
        <p>（以下无正文）</p>
      </div>
      <br>
      <p><em>甲方（借款人）：</em><span>${user.realname}</span></p>
      <p><em>乙方（出借人）：</em><span>蔡坤燚</span></p
      <br>
      <c:if test="${borrow != null}">
            <p class="rig">
            <c:choose>
      			<c:when test="${borrow.status>=21}">通过</c:when>
      			<c:otherwise>失败</c:otherwise>
      		</c:choose></p>
      <p class="rig"><fmt:formatDate value="${borrow.orderTime}" pattern="yyyy年MM月dd日 "/></p>
      </c:if>
      <br>
    </div>
  </div>
  <!-- main end --> 
</div>
</body>
</html>