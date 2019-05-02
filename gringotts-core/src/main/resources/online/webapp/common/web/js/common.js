
/**
 * 刷新验证码
 */
function initCaptcha(imgId){
	$("input[name='captcha']").val("");
	if(imgId == undefined || imgId == null){
		imgId = "captchaImg";
	}
	$("#"+imgId).click();
}

/**
 * 自定义alert框
 * @param msg
 */
function alertc(msg,callBack){
	button = "确定";
	title = "提示";
	$.alerts.okButton=button;
	jAlert(msg,title,callBack);
}
/**
 * 自定义confirm框
 * @param msg
 * @param callBackYes
 * @param callBackNo
 */
function confirmc(msg,callBackYes,callBackNo){
	jConfirm(msg, '请确定',function(ret){
					if(ret){ //点击确定
						if(callBackYes)
							callBackYes();
					}else{  //点击取消
						if(callBackNo)
							callBackNo();
					}
					
	});
}
/**
 * 当前页面转换为top页面
 */
function breakout(){   
	if (window.top!=window.self){   
 		window.top.location=window.location;
  	}   
}
Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()  
        // millisecond  
    };  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
};
/**
 * contains
 * @param elem
 * @returns {Boolean}
 */
Array.prototype.contains = function (elem) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == elem) {
			return true;
		}
	}
	return false;	
};
$.fn.serializeObject = function()    
{    
   var o = {};    
   var a = this.serializeArray();    
   $.each(a, function() {    
       if (o[this.name]) {    
           if (!o[this.name].push) {    
               o[this.name] = [o[this.name]];    
           }    
           o[this.name].push(this.value || '');    
       } else {    
           o[this.name] = this.value || '';    
       }    
   });    
   return o;    
};  

this.REGX_HTML_ENCODE = /"|&|'|<|>|[\x00-\x20]|[\x7F-\xFF]|[\u0100-\u2700]/g; 
function encodeHtml (s){
    return (typeof s != "string") ? s :
        s.replace(this.REGX_HTML_ENCODE,
                  function($0){
                      var c = $0.charCodeAt(0), r = ["&#"];
                      c = (c == 0x20) ? 0xA0 : c;
                      r.push(c); r.push(";");
                      return r.join("");
                  });
};

$.ajaxSetup({
	contentType:"application/x-www-form-urlencoded;charset=utf-8",   
    dataType: "json",
    complete:function(XMLHttpRequest,textStatus){
            var sessionstatus=XMLHttpRequest.getResponseHeader("statusCode");//通过XMLHttpRequest取得响应头，sessionstatus，  
            if(sessionstatus != null && sessionstatus=="301"){ 
                        //如果超时就处理 ，指定要跳转的页面  
                                window.location.reload();   
                        }   
             }   
    }); 