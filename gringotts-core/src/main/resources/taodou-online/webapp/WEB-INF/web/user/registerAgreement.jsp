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
<title>淘豆钱包用户借款协议</title>
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
      <h2>用户注册协议</h2>
      <div class="jy-txt">
        <p>伟福科技有限公司在此郑重提示您，本文系您与“淘豆钱包”（以下简称平台）之间的法律协议，请您认真阅读并理解本协议。您通过平台点击确认本协议的，即表示您同意遵循本协议的所有约定，本协议在您和平台之间具有法律约束力。如发生法律纠纷，您不得以未仔细阅读或理解本协议为由进行抗辩。</p>
      </div>
      <br>
      <%--<input type="button">--%>
      <div class="em-txt">
        <h4>第一条：用户确认及服务接纳</h4>
        <p>1.1 注册用户需满足下列条件：</p>
        <p><span>（1）中华人民共和国大陆地区居民（不包含台、港、澳地区）；</span></p>
        <p><span>（2）具备接受平台各项服务，履行相应义务的完全民事权利能力及行为能力。</span></p>
        <p>1.2 用户需接受平台全部协议条款及各项平台规则，方可成为平台注册用户，接受平台提供的各项服务。</p>
        <p>1.3 用户确认本协议全部条款并完成注册的，视为用户符合注册条件，能够独立承担因接受平台服务所产生权利义务。</p>
        <p>1.4 平台保留在中华人民共和国现行有效之法律、法规范围内接受、拒绝、终止/中止用户接受平台服务之资格。</p>
        <br>
        <h4>第二条 ：用户注册信息</h4>
        <p>2.1 用户首次通过平台提交个人信息并确认本协议的，即成为平台注册用户。</p>
        <p>2.2 用户应自行如实向平台提供注册信息。用户应当确保其提供的注册信息真实、准确、完整、合法有效。如用户提供的注册信息不合法、不真实、不准确、不详尽，用户需承担由此产生的相应责任及后果，平台保留终止注册用户资格的权利。</p>
        <p>2.3 用户认可平台收集及储存用户的资料及信息，包含但不限于用户本人提交的资料及信息以及平台自行收集的用户资料及信息。平台收集、储存用户资料及信息的目的在于更加效率及便利地为用户提供平台服务，平台不得将用户资料及信息用于其他目的。</p>
        <p>2.4 平台应当采取不低于一般行业惯例对于用户的资料及信息进行保护，但因不可抗力所导致的用户资料及信息泄露（包含但不限于黑客攻击、第三方导致的系统缺陷等），平台不承担相应的责任。</p>
        <p>2.5 平台有义务根据行政、司法机关的要求向该等机关提供用户的资料及信息。</p>
        <p>2.6 用户应当谨慎地保存、使用其平台账号、密码、手机验证码等信息。用户不得将平台账号向他人透露、借用，否则用户应当承担由此产生的全部后果及责任。</p>
        <br>
        <h4>第三条 平台服务</h4>
        <p>平台依靠互联网依法向平台注册用户提供互联网信息服务等服务内容。</p>
        <br>
        <h4>第四条 用户承诺</h4>
        <p>4.1 用户应当妥善保管本人的平台账号、密码、绑定的手机号码、手机验证码等信息。对于非因平台过错产生的上述信息泄露所导致的用户损失平台不承担任何责任。</p>
        <p>4.2 用户承诺在接受平台服务过程中应当诚实、守信地履行相关义务，否则将承担包含但不限于下列后果：</p>
        <p><span>（1）用户的不良信用信息将被上传至经中国人民银行批准并依法设立的各征信数据机构；</span></p>
        <p><span>（2）用户将因违约行为承担相应的违约责任；</span></p>
        <p><span>（3）用户将因违约行为承担相应的诉讼后果。</span></p>
        <br>
        <h4>第五条 征信授权</h4>
        <p>5.1 用户在此不可撤销地授权平台通过依法设立的征信机构了解、咨询、审查用户的个人信息、信用状况、履约能力及其他评定用户资信状况的信息，包含可能存在的用户不良信用信息。</p>
        <p>5.2 用户在此不可撤销地授权平台向依法设立的征信机构提供用户接受平台服务所对应的个人信息、借贷信息及后续还款记录等信息。</p>
        <p>5.3 用户在此不可撤销地授权平台向依法设立的征信机构提供用户可能产生的不良信用信息。</p>
        <br>
        <h4>第六条 关于电子合同</h4>
        <p>6.1本协议采用电子文本形式制成，并在平台系统上保留存档。其签订方式符合《中华人民共和国电子签名法》的要求。用户通过平台系统点击确认或以其他方式选择接受本协议，即表示已同意接受本协议的全部内容以及与本协议有关的各项平台规则。</p>
        <p>6.2用户应当妥善保管自己的账号、密码等账户信息，不得以账户信息被盗用或其他理由否认已订立的协议的效力或不履行相关义务。</p>
        <br>
        <h4>第七条 责任限制</h4>
        <p>除非另有明确的书面说明,平台及其所包含的或以其它方式通过平台提供给用户的全部信息、内容、材料、产品（包括软件）和服务，均是在"按现状"和"按现有"的基础上提供的。</p>
        <p>如因不可抗力或其它平台无法控制的原因平台系统崩溃或无法正常使用导致无法向用户提供平台服务的，平台不承担任何责任。</p>
        <br>
        <h4>第八条 联系信息更新</h4>
        <p>用户接受平台服务期间，用户本人姓名、身份证号码、手机号码、银行账户等信息如果发生变更，应当在相关信息发生变更之日起三日内将更新后的信息提供给平台。因用户未能及时提供上述变更信息而带来的损失或额外费用应由用户自行承担。</p>
        <br>
        <h4>第九条 适用法律及争议解决</h4>
        <p>9.1本协议的签订、履行、终止、解释均适用中华人民共和国法律。</p>
        <p>9.2因履行本协议所产生的争议应当通过友好协商解决；如协商不成，则本协议任意一方均可向本协议签订地上海市杨浦区有管辖权的人民法院起诉。</p>
        <br>
        <h4>第十条 其他</h4>
        <p>10.1本协议未尽事项按照平台现有及不时发布的各项规则执行。</p>
        <p>10.2如本协议中的任何一条或多条被确认为无效，该无效条款并不影响本协议其他条款的效力。</p>
        <br>
      </div>
    </div>
  </div>
  <!-- main end --> 
</div>
</body>
</html>