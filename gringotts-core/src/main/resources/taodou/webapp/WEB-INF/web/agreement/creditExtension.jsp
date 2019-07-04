<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<title>信用查询授权协议</title>
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
  <!-- main start -->
  <div class="wrapper">
    <div class="agreement">
      <h2>信用查询授权协议</h2>
      <div class="jy-txt">
        <p><!-- 极速现金侠 -->${appName}（以下简称“本平台”）由<!-- 上海利全信息科技服务有限公司 -->${companyTitle}（以下简称“本公司”）负责运营，《信用查询授权协议》（以下简称“本协议”）签约方为<!-- 上海利全信息科技服务有限公司 -->${companyTitle}与<!-- 极速现金侠 -->${appName}平台用户（以下简称用户或“您”）。 用户在使用本公司提供服务前请务必仔细阅读以下条款，一经勾选“同意《信用查询授权协议》”，即表示用户同意并签署了本协议，本协议即构成对双方有约束力的法律文件。</p>
        <p>在接受本协议之前，请您仔细阅读本协议的全部内容。如果您不同意本协议的任意内容，或者无法准确理解该条款的含义，请不要进行后续操作。</p>
      </div>
      <br>
      <div class="em-txt">
        <h4>一、声明与承诺</h4>
        <p>1、用户确认在接受本平台提供的服务前，已充分阅读、理解并接受本协议的全部内容，用户在使用本平台服务，即表示同意并遵循本协议之所有约定。</p>
        <p>2、本平台根据需要不时地制定、修改本协议或各种类型规则，如本协议及规则有任何变更，一切变更皆以本平台最新公布为准。经修订的协议、规则一经在本网站公布，即自动生效或在该等协议、规则指定的时间生效。您应不时地注意本协议及附属规则地变更，若您不同意相关变更，本网站有权不经任何告知终止、中止本协议或者限制您进入本网站的全部或者部分板块且不承担任何法律责任。但该终止、中止或限制行为并不能豁免您在本网站已经进行的交易下所应承担的义务。</p>
        <br>
        <h4>二、授权内容</h4>
        <p>1、用户使用本平台申请服务即表明用户授权本平台或由本平台及公司委托的第三方（以下简称“授权主体”）通过合法渠道了解、咨询、审查用户的资信状况、财务状况和其他与评定用户信用付款额度及付款能力有关的信息，包括但不限于：用户征信报告、学历信息、电商信息、银联流水信息等；</p>
        <p>2、用户同意拥有用户本人不良信息的机构，通过合法设立的征信公司向本公司提供本人的不良信息。用户已被事先明确告知并已同意拥有本人不良信息的机构提供本人的不良信息。</p>
        <p>3、用户同意本公司委托合法设立的征信公司，采集本人收入、存款、有价证券、商业保险、不动产的信息和纳税数额信息。用户已被明确告知提供上述信息可能会给本人带来财产或信用损失，以及其他可能的不利后果，但本人仍然同意本公司委托合法设立的征信公司采集这些信息。</p>
        <p>4、授权主体有权使用用户通过本平台提供的信息以及通过本条上述约定自行收集的用户资料和信息用于以下用途（包括但不限于）：</p>
        <p><span>（1）为用户提供服务的整个过程中；</span></p>
        <p><span>（2）根据需要向其他为用户申请提供服务的第三方等披露与用户相关的任何信息；</span></p>
        <p><span>（3）追偿用户违约欠款、解决争议及对有关纠纷进行调停等其他合法用途。</span></p>
        <p>5、用户承诺，授权主体根据本协议所采取的全部行动和措施的法律后果均归属于用户本人承担。</p>
        <br>
        <h4>三、用户须知</h4>
        <p>1、用户在使用本平台时须按操作提示填写相关信息（如：填写本人姓名、手机号码、vvv身份证号、单位名称、上传本人身份证正反面等），且用户须确保所填写资料的真实性、准确性，不得提供虚假信息或隐瞒重要事实。</p>
        <p>2、用户在填写上述信息时，应遵守中国相关法律法规，不得将本服务用于任何非法目的（例如用于骗取贷款、合同诈骗），也不以任何非法方式使用本服务。</p>
        <p> 3、本平台有权根据用户提供的各项信息及授权主体独立获得的信息评定用户在本平台处所拥有的信用等级，并根据对用户个人信息的评审结果，决定是否审核通过并将乙方的借款需求向第三方进行推荐。 即用户通过本平台申请服务并不意味着用户的申请一定审核通过，用户审核结果以平台后续综合评审结果为准。</p>
        <br>
        <h4>四、责任限制</h4>
        <p>1、基于互联网的特殊性，本公司无法保证本平台的服务不会中断，如果由于本公司、本平台及相关第三方的设备、系统故障或缺陷、遭到病毒、黑客攻击、网络故障、网络中断或者发生地震、海啸、流行病、战争、恐怖主义等不可抗力或者因电力中断、经济形势恶化、政府管制等其他类似事件而造成本平台未能履行本协议或履行本协议不符合约定，不构成本平台的违约，对于因此导致的损失，本平台不承担任何责任。</p>
        <p>2、在法律允许的情况下，本公司对于与本协议有关或由本协议引起的任何间接的、惩罚性的、特殊的、派生的损失（包括业务损失、收益损失、利润损失、商誉损失或其他经济利益的损失）均不负有任何责任，即使事先已被告知此等损失的可能。</p>
        <p>3、 用户明确同意其使用本平台服务所存在的风险及一切后果将完全由用户本人承担，本平台对此不承担任何责任。</p>
        <p>4、无论如何，在任何情况下，本平台方对用户使用本平台服务而产生的任何形式的直接或间接损失均不承担法律责任。</p>
        <br>
        <h4>五、 法律适用及管辖</h4>
        <p>1、本协议的订立、执行、终止、解释和终端解决均适用中华人民共和国的法律。</p>
        <br>
        <h4>六、 其他条款</h4>
        <p>1、除本协议约定外，本平台对用户信息及资料进行保密；如任何一方违约，或因相关权力部门要求（包括但是不限于法院、仲裁机构、金融监管机构等），本平台有权披露。</p>
        <p>2、若本协议的部分条款被认定为无效或者无法实施时，不影响本协议其他条款的效力。</p>
        <p>3、本协议采用电子文本形式制成，自用户勾选“《信用查询授权协议》”或类似确认键时成立。</p>
        <br>
      </div>
    </div>
  </div>
  <!-- main end --> 
  
</div>
</body>
</html>