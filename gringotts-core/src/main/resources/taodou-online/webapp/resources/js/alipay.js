var timer = null;
var second = 0;
var hbAmount = null;
var currentUrl = "my.alipay.com";
function afterLogin() {
	accountObj = document.getElementById("J-userInfo-account-userEmail");
	imgObj = document.getElementById("J-portrait-user");
}
function toDo() {
	if (window.location.hostname == currentUrl) {
		afterLogin();
		// 显示遮罩层
		nativeMethod.goneLayout(0);
		// 进度为百分之0
		nativeMethod.setProgress(0);
		var div = document.getElementsByClassName("amount-des")[0];
		var tmpTxt = null;
		if (div != undefined) {
			var hbDiv = div.childNodes;
			if (hbDiv != undefined) {
				for ( var i = 0; i < hbDiv.length; i++) {
					var tmp = hbDiv[i].innerText;
					if (tmp != undefined) {
						if (tmpTxt == null) {
							if (tmp.indexOf("本期未还金额") >= 0) {
								tmpTxt = tmp;
							}
						}
						if (tmpTxt != null) {
							var flag2 = " 元";
							var index = tmp.indexOf(flag2);
							if (index >= 0) {
								hbAmount = tmp.substring(0,index);
							}
						} else {
							var flag = "消费额度:";
							var flag2 = " 元";
							var index = tmp.indexOf(flag);
							if (index >= 0) {
								hbAmount = tmp.substring(flag.length);
								var index2 = hbAmount.indexOf(flag2);
								if (index2 >= 0) {
									hbAmount = hbAmount.substring(0, index2);
								}
							}
						}
						if (hbAmount == null) {
							if (!isNaN(tmp)) {
								hbAmount = tmp;
							}
						}
						if (hbAmount != null) {
							// alert(hbAmount);
							// 调用app函数 ，通过app传递到后台
							clearInterval(timer);
							break;
						}
					}
				}
			}
			if (hbAmount != null) {
				// 传递给app保存到app中
				nativeMethod.saveText("money", hbAmount);
				// 进度为百分之50
				nativeMethod.setProgress(50);
				// 调用app提交方法
				nativeMethod.submitText("zhifubao");
				// 进度为百分之百
				nativeMethod.setProgress(100);
			}
			second = second + 1;
			if (second >= 3) {
				if (hbAmount == null) {
					hbAmount = 0;
					// 传递给app保存到app中
					nativeMethod.saveText("money", hbAmount);
					// 进度为百分之50
					nativeMethod.setProgress(50);
					// 调用app提交方法
					nativeMethod.submitText("zhifubao");
					nativeMethod.setProgress(100);
				}
				// 三秒后没有拿到花呗信息也返回
				clearInterval(timer);
			}
		} else {
			hbAmount = 0;
			// 传递给app保存到app中
			nativeMethod.saveText("money", hbAmount);
			// 进度为百分之50
			nativeMethod.setProgress(50);
			// 调用app提交方法
			nativeMethod.submitText("zhifubao");
			nativeMethod.setProgress(100);
			clearInterval(timer);
		}
	}
}
timer = setInterval(toDo, 1000);
