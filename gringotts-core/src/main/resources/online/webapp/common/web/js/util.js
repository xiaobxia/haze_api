var Util = Util || {};
Util.config = {
	JSfile : "/common/web/js/",
	CSSfile : "/common/web/css/",
	imgfile : "/common/web/img/",
	CITYSPOTLIGHT : [],
	HOTCITYSPOTLIGHT : [],
	BANKSSPOTLIGHT : []
};
function getFormJson(form) {
	var o = {};
	var a = $(form).serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
}
/**
 * 数字加逗号
 * 
 * @param nStr
 *            zeroFull 没有小数点是否以00填充,默认部填充
 * @returns
 */
//function addCommas(nStr, zeroFull) {
//	nStr += '';
//	x = nStr.split('.');
//	x1 = x[0];
//	var tmp = (x.length > 1 ? x[1] == "00" ? '' : x[1] : '');
//	if (tmp != '') {
//		while (tmp.substring(tmp.length - 1, tmp.length) == '0') {
//			tmp = tmp.substring(0, tmp.length - 1);
//		}
//	}
//	x2 = (tmp != '') ? '.' + tmp : zeroFull ? '.00' : '';
//	var rgx = /(\d+)(\d{3})/;
//	while (rgx.test(x1)) {
//		x1 = x1.replace(rgx, '$1' + ',' + '$2');
//	}
//	return x1 + x2;
//}
/**
 * 数字加逗号
 * 
 * @param nStr
 *            zeroFull 没有小数点是否以00填充,默认不填充
 * @returns
 */
function addCommas(nStr, zeroFull) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	var tmp = (x.length > 1 ? x[1] == "00" ? '' : x[1] : '');
	x2 = (tmp != '') ? '.' + tmp : zeroFull ? '.00' : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}
/**
 * 日期格式化
 * 
 * @param obj
 *            要格式化的日期
 * @param format
 *            以某种格式输出，为空则按照yyyy-MM-dd HH:mm:ss
 * @return
 */
function toDate(obj, format) {
	var date = new Date();
	date.setTime(obj.time);
	date.setHours(obj.hours);
	date.setMinutes(obj.minutes);
	date.setSeconds(obj.seconds);
	if (format == undefined || format == null) {
		format = "yyyy-MM-dd HH:mm:ss";
	}
	return date.format(format);
}
$(document).ajaxComplete(function(event, xhr, options) {
	var status = xhr.getResponseHeader("statusCode");
	var path = xhr.getResponseHeader("path");
	var msg = xhr.getResponseHeader("msg");
	if (status == '301') {
		if (msg != null && msg != '') {
			alert(decodeURI(msg));
		}
		window.location.href = xhr.getResponseHeader("Referer");
	}
});
var wait = 60;
/**
 * 倒计时1分钟，剩余0秒时绑定click事件，否则移除click事件
 * 
 * @param htmlId
 *            倒计时控件
 * @param click
 *            要绑定的时间
 * @return
 */
function time(htmlId, click) {
	if (wait == 0) {
		$("#" + htmlId).attr("onclick", click);
		$("#" + htmlId).text("获取验证码");
		wait = 60;
	} else {
		$("#" + htmlId).text(wait + "秒后重试");
		$("#" + htmlId).removeAttr("onclick");
		wait--;
		setTimeout(function() {
			time(htmlId, click);
		}, 1000);
	}
}
/**
 * ajax打开页面
 * 
 * @param url
 *            请求地址
 * @param data
 *            请求参数
 * @param async
 *            同步（异步）
 * @param domId
 *            dom节点
 */
Util.ajaxOpenHtml = function(url, data, domId, async) {
	if (data == undefined || data == null) {
		data = {};
	}
	if (async == undefined || async == null) {
		async = true;
	}
	if (domId == undefined || domId == null) {
		domId = "user_content";
	}
	$.ajax( {
		type : "POST",
		url : url,
		data : data,
		async : async,
		dataType : "html",
		success : function(data) {
			$("#" + domId).html(data);
			dealPwdAndPlaceHolder();
		},
		error : function(request) {
			$("#" + domId).html("位置异常，请稍后重试");
		}
	});
}
String.prototype.replaceAll = function(find, rep) {
	var exp = new RegExp(find, "g");
	return this.replace(exp, rep);
};