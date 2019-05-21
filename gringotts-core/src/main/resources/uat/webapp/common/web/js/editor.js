(function ($) {
window.onload = initAll;
window.isIE = !!window.ActiveXObject;
function initAll() {
	//隐藏textarea
	var textArea = $("content");
	textArea.style.display="none";
	//设置为可编辑
	var EditorFrame = $("EditorFrame");
	var FrameWin = EditorFrame.contentWindow;
	var FrameDoc = FrameWin.document;
	FrameDoc.designMode = "on";
	FrameDoc.contentEditable=true;
	FrameDoc.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Content</title><style type='text/css'>* {font-size:12px;} body {margin:0;padding:0;}</style></head><body> </body></html>");
	FrameDoc.close();
	//查看HTML源代码按钮
	var viewHTML = $("viewHTML");
	viewHTML.checked = false;
	viewHTML.onclick = function () {
		if (this.checked) {
			textArea.value = FrameDoc.body.innerHTML;
			textArea.style.display="block";
			EditorFrame.style.display="none";
		} else {
			textArea.style.display="none";
			EditorFrame.style.display="block";
			FrameDoc.body.innerHTML = textArea.value;
		}
	};
	//关闭所有的层,IE
	FrameDoc.onclick = function () {
		portraitTable.style.display="none";
	};
	//QQ表情
	var portraitTable = getPortraitTable(11,"QQ 表情","portraitTable");
	document.body.appendChild(portraitTable);
	var insertQQ = $("insertQQ");
	portraitTable.onclick = function (evt) {
		evt = evt || window.event;
		var target = evt.target || evt.srcElement;
		if (target.tagName =="IMG") {
			cmd("InsertImage",target.src);
			this.style.display="none";
		}
	};
	insertQQ.onclick = function () {
		toggle(portraitTable,"display","block","none");
	};
	function toggle(obj,css,val1,val2) {
		obj.style[css]= obj.style[css]==val1?val2:val1;
	}
	//颜色表
	var colorTable = getColorTable("colorTable");
	document.body.appendChild(colorTable);
	var setFontColor = $("setFontColor");
	setFontColor.onclick =function () {
		toggle(colorTable,"display","block","none");
		colorTable.command ="ForeColor";
		colorTable.onclick = setColor;
	};
	var setBgColor = $("setBgColor");
	setBgColor.onclick = function () {
		toggle(colorTable,"display","block","none");
		colorTable.command ="BackColor";
		colorTable.onclick =setColor;
	};
	function setColor(evt) {
		evt =evt || window.event;
		var obj = evt.target || evt.srcElement;
		if (obj.tagName=="TD" && obj.title) {
			cmd(this.command,obj.title);
		}
		this.style.display="none";
	}
	//插入图片
	var insertImage = $("insertImage");
	insertImage.onclick = function () {
		var url = prompt("请输入图像地址!!!","http://");
		if (url && url!= "http://") {
			var img = document.createElement("img");
			img.onload = function () {
				cmd("InsertImage",url);
			};
			img.onerror =function () {
				alert("图像载入出错,请检查您输入的URL是否正确!\n"+url);
			};
			img.src=url;
		}
	};
	//插入超链接
	var insertLink = $("insertLink");
	insertLink.onclick = function () {
		if (isIE) {
			cmd("CreateLink");
		} else {
			if (!FrameWin.getSelection().toString()) {
				return alert("请先选择要添加链接的文本!");
			}
			var url = prompt("请输入URL:","http://");
			if (url && url != "http://") {
				cmd("CreateLink",url);
			}
		}
	};
	//加粗
	var boldButton = $("boldButton");
	boldButton.onclick = function () {
		cmd("Bold");
	};
	//倾斜
	var italicButton = $("italicButton");
	italicButton.onclick = function () {
		cmd("Italic");
	};
	//下划线
	var underLineButton=$("underLineButton");
	underLineButton.onclick = function () {
		cmd("UnderLine");
	};
	//左对齐
	var leftAlign =$("leftAlign");
	leftAlign.onclick = function () {
		cmd("JustifyLeft");
	};
	//右对齐
	var rightAlign =$("rightAlign");
	rightAlign.onclick = function () {
		cmd("JustifyRight");
	};
	//居中对齐
	var centerAlign =$("centerAlign");
	centerAlign.onclick = function () {
		cmd("JustifyCenter");
	};
	//插入对象
	function insertNode() {
	}
	
	//执行命令
	function cmd(type,param) {
		FrameWin.focus();
		if (param) {
			FrameDoc.execCommand(type,false,param);
		} else if (isIE) {
			FrameDoc.execCommand(type,true);
		} else {
				FrameDoc.execCommand(type,false,false);
		}
	}
};
})(function (id) {return document.getElementById(id);});
