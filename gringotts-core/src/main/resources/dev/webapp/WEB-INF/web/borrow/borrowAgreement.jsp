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
<title>小鱼儿用户借款协议</title>
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
        <div class="header">
            <a class="back_arr" href="javascript:void(0);"></a>
            <h1>小鱼儿用户借款协议</h1>
        </div>
        <!-- main start -->
        <div class="wrapper">
          <div class="agreement">
            <h2>借款咨询服务协议</h2>
            <h3>编号：<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u></h3>
            <p>甲方（资金借入方）：</p>
            <p>身份证号码：</p>
            <br>
            <p>乙方（居间服务方）：</p>
            <br>
            <p>鉴于：</p>
            <div class="jy-txt">
              <p>甲方为具备完全民事行为能力的中华人民共和国大陆地区居民，系“小鱼儿”注册用户，具有一定资金需求。</p>
              <p>乙方系互联网金融信息服务平台“小鱼儿”运营方,能够通过其平台为甲方借款事项提供信息中介服务。</p>
              <p>甲方在申请使用本服务前应充分阅读、理解本协议的全部内容。甲方在本平台上的账户和密码是识别甲方身份及指令的唯一标志，所有使用甲方的账户和密码的操作即为甲方本人的操作行为，任何使用甲方的账户和密码在乙方平台上作出的指示均是不可撤销的，本平台对依照该指示进行的操作行为及其结果不承担任何责任。</p>
            </div>
            <h4>第一条：借款信息</h4>
            <p>1.1 借款本金金额：甲方本次借款本金总金额为人民币     元。</p>
            <p>1.2 借款用途：生活消费</p>
            <p>1.3 借款期限：自201<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>年<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>月<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>日起至201<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>年<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>月<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>日止，共计<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>天。</p>
            <h4>第二条：服务费</h4>
            <p>2.1 乙方为甲方和出借人（互联网投资理财用户）双方提供撮合、信息咨询、信用评估与管理、合同签订、促成双方交易、还款提醒、账户管理、提前还款、还款特殊情况下还款处理诉讼协助等系列相关咨询服务，甲方同意乙方可收取一定比例的服务费，包括信息服务费、咨询费、信用评审费、管理费等。</p>
            <p>2.2 乙方就向甲方提供本协议项下服务收取甲方服务费，金额为人民币100元。</p>
            <p>2.3 乙方应当收取的服务费金额自甲方收取借款金额时先行扣除。甲方在此不可撤销地授权乙方执行上述扣除操作，甲方对于服务费收取标准及支付方式事先知悉并认可。</p>
            <h4>第三条：还款方式</h4>
            <p>3.1 甲方还款采取到期一次性还本付息方式；</p>
            <p>3.2 甲方应当于201<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>年<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>月<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>日前共计偿还借款本息人民币<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>元。</p>
            <p>3.3 甲方还款由乙方通过合作的第三方支付机构采取银行卡代扣或其他乙方指定的还款方式。</p>
            <p>3.4 若甲方的还款金额不足时，清偿顺序为：若逾期两期以上，应首先清偿时间在前的借款债务，然后再清偿时间在后的借款债务；同一期借款债务的清偿顺序依次为：各种费用（不足的，按比例清偿），违约金，逾期利息，利息，本金。</p>
            <h4>第四条：甲方的陈述与保证</h4>
            <p>4.1 甲方保证并承诺借款真实、合法，并按照约定用途使用借贷资金，不会用于出借等其他目的，也不会将所借款项用于任何非法用途。</p>
            <p>4.2 甲方保证提供真实、准确、完整的个人信息及融资信息，其提供的其他资料也均为真实、完整、有效，并且甲方进一步保证其不会提供任何虚假信息或隐瞒重要事实。</p>
            <p>4.3 甲方承诺不会将本协议项下的债务以任何形式转让给任何其他方，如转让亦不对乙方及出借人发生任何法律上的效力。</p>
            <p>4.4 甲方知悉，本合同项下的债权可以进行全部、部分转让，且债权转让的次数不受限制。甲方签署本合同即视为对后续所有债权转让的同意和确认。</p>
            <h4>第五条  授权及声明</h4>
            <p>5.1 甲方在此不可撤销地授乙方向依法设立的征信机构查询甲方的个人信息及个人信用记录。</p>
            <p>5.2 甲方在此不可撤销地授权乙方向依法设立的征信机构提供甲方接受乙方服务所对应的个人信息、借贷信息及后续还款记录等信息。 </p>
            <p>5.3 甲方在此不可撤销地授权乙方向依法设立的征信机构提供甲方可能产生的不良信用信息。</p>
            <h4>第六条：违约责任</h4>
            <p>甲方承诺按时足额履行还款义务，如甲方逾期还款，将承担包含但不限于下列违约责任：</p>
            <p><span>（1）甲方的不良信用信息记录将被上传至经中国人民银行批准并依法设立的各类型征信数据机构；</span></p>
            <p><span>（2）甲方应当于还款逾期之日起每日按照借款本金金额的1%承担逾期违约金直至全部款项偿还完毕之日。</span></p>
            <p><span>（3）甲方应当于还款逾期之日起每日按照借款本金金额的0.5%支付逾期期间利息直至全部款项偿还完毕之日。</span></p>
            <p><span>（4）甲方应当于还款逾期之日起每日按照借款本金金额的0.5%支付催收费用直至全部款项偿还完毕之日。</span></p>
            <h4>第七条  关于电子合同及协议效力</h4>
            <p>7.1本合同采用电子文本形式制成，自生成之日起成立，乙方实际收到借款金额之日生效，甲方认可合同签署方式具有法律层面的强制约束力。</p>
            <p>7.2甲方应当妥善保管自己的账号、密码等账户信息，不得以账户信息被盗用或其他理由否认已订立的合同的效力或不履行相关义务。</p>
            <p></p>
            <p></p>
            <p></p>
            <h4>第八条  通知</h4>
            <p>8.1 本合同任何一方因履行本合同做出的通知和/或文件均应以书面形式做出，通过专人送达、挂号邮递、特快专递、短信及邮件等方式传送。</p>
            <p>8.2 通知在下列日期视为送达：</p>
            <p><span>（1）专人递送的通知，在专人递送之交付对方日为有效送达；</span></p>
            <p><span>（2）以挂号信（付清邮资）发出的通知，在寄出（以邮戳为凭）后的三个工作日内为有效送达；</span></p>
            <p><span>（3）以特快专递（付清邮资）发出的通知，在寄出（以邮戳为凭）后的三个工作日内为有效送达；</span></p>
            <p><span>（4）以短信方式（发出的通知，短信成功发出即为有效送达；</span></p>
            <p><span>（5）以邮件方式发出的通知，邮件发送成功时即为有效送达。</span></p>
            <p>8.3 本合同存续期间甲方本人姓名、身份证号码、手机号码、银行账户等信息如发生变更，应当在相关信息发生变更之日起三日内将更新后的信息提供给乙方。因甲方未能及时提供上述变更信息而带来的损失或额外费用应由甲方承担。</p>
            <h4>第九条  适用法律及争议解决</h4>
            <p>9.1 本合同的签订、履行、终止、解释均适用中华人民共和国法律。</p>
            <p>9.2 因履行本合同所产生的争议双方应当通过友好协商解决；如协商不成，则本合同任意一方均可将争议提交至乙方住所地上海杨浦区有管辖权的人民法院。</p>
            
          </div>
          
        </div>
        <!-- main end -->
        
    </div>
</body>
</html>