$(function() {

			renderSubmitBtn();
			returnBtnFun();
			submit();

		});
var listUrl = base + "/app/conf/url/show.do?method=showIndex&appId="+ page.appId;
		
function returnBtnFun() {
	$("#returnBtn").click(function() {
				window.location.href =listUrl ;
			});
}

function renderSubmitBtn() {

	if ($("#hidden1").val() == "false")
		$("#submitBtn").val("添加");
}
function sendReq() {

	var url = base + "/app/conf/url/show.do?method=add_or_update";
	var data = $("#form1").serialize();
	
	//alert(data);
	var successFun = function(data) {
		popInfo(data);

	};

	$.post(url, data, successFun, "json");
}

function popInfo(data) {
	// alert("popInfo");
	var res = data.result;
	var operate = data.operate;
	var resDesc, operateDesc;
	if (res == "1")
		resDesc = "成功";
	else
		resDesc = "失败";

	if (operate == "update")
		operateDesc = "更新";
	else
		operateDesc = "保存";

	// alert(content);
	// alert(TINY.box.show);
	var content = operateDesc + resDesc + "!";
	//指定秒数
	var n = 2;
	TINY.box.show(content, 0, 100, 35, 0, n);
	setTimeout(function(){
		window.location.href = listUrl;
	
	},1000 * n);	
}

function submit() {
	$("#submitBtn").click(function() {
				sendReq();

			});
}
