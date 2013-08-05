$(function() {
			fillDbInfo();
			showlist();
			rel_app_add();

		});

var page = {};

function rel_app_add() {

	// add click event
	$("#a_relAppAdd").click(function() {

				relAppAddUnit();
			});

	function relAppAddUnit() {
		// url
		var $url = "../../dbconf/no_rel_apps.do?dbId=" + page.dbId;

		TINY.box.show($url, 1, 260, 320, 1);

		var fun_in = setInterval(fun, 200);

		function fun() {
			if (TINY.done == true) {
				clearInterval(fun_in);

				relAppAddProcess();
			}
		}

	}
	function relAppAddProcess() {
		handleCloseBtn();
		submit();

		function closeCurr() {
			TINY.box.hide();
		}
		function handleCloseBtn() {
			$("#closeBtn").click(function() {
						// alert(1);
						closeCurr();
					});
		}
		function popInfo(data) {
			// alert("popInfo");
			var res = data.result;
			var resDesc, operateDesc;
			if (res == "1")
				resDesc = "成功";
			else
				resDesc = "失败";

			operateDesc = "添加";

			// alert(content);
			// alert(TINY.box.show);
			var content = operateDesc + resDesc + "!";
			// 指定秒数
			var n = 2;
			TINY.box.getB().innerHTML = content;

			setTimeout(function() {

						TINY.box.hide();
						// 刷新页面
						//alert("刷新页面");
						window.location.href = "../../config/dbconf/rel_apps.jsp?id="+ page.dbId;
					}, 2000);

			// TINY.box.show(content, 0, 100, 35, 0, n);

		}

		function submit() {
			$("#submitBtn").click(function() {
						sendReq();

					});

		}
		function sendReq() {

			var url = "../../dbconf/rel_app_add.do";
			var data = $("#form1").serialize();

			// alert(data);
			// var successFun = ;

			$.post(url, data, function(data) {
						var res = data.result;
						// closeCurr();
						// 指定时间段之后执行，这样防止两个hide、show这两个操作间隔过短，引起的视觉的不舒服。
						setTimeout(function() {

									popInfo(data);

									// 如果失败,重新发起请求
									if (res == 0) {
										relAppAddUnit();
									}
								}, 800);

					}, "json");
		}

	}
}

// 表格中的“操作”列
page.operate = {};

page.operate.links2 = {
	/* 格式：operate:[desc,css class] */
	delete_1 : ["删除", "deleteAction"]
};

page.confirm1 = {};
page.confirm1.currTr;
page.confirm1.currLink;

function model_1_part1(link, tr) {
	page.confirm1.currLink = link;
	page.confirm1.currTr = tr;
	confirm1Show();

};

function confirm1Show() {
	/**
	 * 1、复制现有div片段 1.1、现有div片段不能有id属性，如果需要，复制后的片段要设置新的id
	 * 
	 */

	var c1_htm = $("#confirm1").html();
	var e1 = document.createElement("div");
	var e1_htm = $(e1).html(c1_htm);

	TINY.box.show(c1_htm, 0, 200, 100, 0);
	// 放在这里，主要是给（show()方法利用c1_htm创建的）新元素添加事件
	model_1_part2();

	// $("#confirm1").css("display", "block");
}

function confirm1Hide() {
	TINY.box.hide();
	// $("#confirm1").css("display", "none");
}

function model_1_part2() {

	// alert($("#confirm1 .yes").click);
	$(" .confirm1_yes").click(function() {
				// alert("hello");
				// 执行删除操作
				doDelete(page.confirm1.currLink, page.confirm1.currTr);
				confirm1Hide();

			});
	$(" .confirm1_no").click(function() {
				// alert("no");
				confirm1Hide();
			});

	$(" .confirm1_close").click(function() {
				// alert("close");
				confirm1Hide();
			});

	// 注意这里的参数，不是直接从deleteAction那里获取，而是通过c.callBackParams获取
	function doDelete($link, $tr) {
		$.ajax({
					url : $link,
					success : function(data) {
						// alert("已删除!");
						deleteUI($tr);
					}
				});

		function deleteUI(node) {
			// alert(node.innerHTML);
			// alert(node.parentNode.innerHTML);
			node.parentNode.removeChild(node);
		}
	}
}

function fillDbInfo() {

	var ids = ["dbName", "dbUser", "dbPwd", "dbType", "dbUrl","dbDesc"];
	$.ajax({
				url : "../../dbconf/get.do?id=" + page.dbId,

				success : function(data) {
					// alert(data);
					for (var i = 0; i < ids.length; i++) {
						document.getElementById(ids[i]).innerHTML = data[ids[i]];
					}
				}

			});

}

function renderlist(data) {
	/*
	 * 格式：operate:url
	 * 
	 */

	var t1 = document.getElementById("table1");
	var t1_tb = t1.getElementsByTagName("tbody")[0];
	// alert(data.length);
	// data.length 行数据

	/**
	 * 策略：显示哪列，取那列。好处1： 这样解决了td间排序问题，td间的顺序在cols中定义好了。 好处2：减少循环的次数，对性能有所提升。
	 * 
	 */

	// 从table tr th中获取。
	// 示例： ["dbName", "type1num", "dbUrl", "dbType", "operate"]
	var colNames = [];
	var cols = $("table thead tr th");
	for (var i = 0; i < cols.length; i++) {
		var cc = cols[i].className;
		cc = cc.substring(3);
		colNames[colNames.length] = cc;

	}
	/**
	 * 
	 * 
	 */
	for (var i = 0; i < data.length; i++) {

		var item = data[i];
		var tr = document.createElement("tr");

		var po = item.po;
		var appId = po.appId;
		/* 这里colNames对应页面上显示的列，而不是直接和后台的某条记录对应的 */
		for (var j = 0; j < colNames.length; j++) {
			var colName = colNames[j];

			var v = handleValue(colName, po, item, appId);
			var td = addTd(tr, v, colName);
			addEvents(td, appId, tr);
		}

		// 给操作列添加事件
		// temp comment
		// addEventsToTd(td5, dbId, tr);

		t1_tb.appendChild(tr);
	}
}
function addEvents(td, id, tr) {
	if (td.className == "so_operate") {
		var links1 = {
			delete_1 : "../../dbconf/rel_app_delete.do?id=" + id + "&dbId="
					+ page.dbId
		}
		var hrefs = td.getElementsByTagName("a");
		// alert(hrefs.length);

		for (var j in hrefs) {
			var href = hrefs[j];
			if (href.className == "deleteAction") {

				href.onclick = function() {
					deleteAction(id, this, links1.delete_1, tr);
				}

			}

		}

	}

}

function deleteAction(id, $this, link, tr) {
	// alert("deleteAction");

	model_1_part1(link, tr);

	// model_2(link, tr);
}

function createOperateLinks(id) {

	var str = "";
	for (var i in page.operate.links2) {
		// "<a href=\""+ url +"\">" + desc + "</a>"
		var desc = page.operate.links2[i][0];
		var cssClass = page.operate.links2[i][1];
		var split = "&nbsp;";

		str += "<a href=\"javascript:void(0)\" class=\"" + cssClass + "\">"
				+ desc + "</a>" + split;

	}

	return str;

}
function handleValue(colName, po, item, id) {
	var v;
	if (colName == "appName") {
		v = po[colName];
	} else if (colName == "operate") {
		v = createOperateLinks(id);
	} else {
		v = item[colName];

	}
	return v;
}

function addTd(tr, value, colName) {
	var td = newTd(value, colName);
	tr.appendChild(td);
	return td;
}

// function addTdInsertBefore(tr, value, special_td) {
// var td = newTd(value);
// tr.insertBefore(td, special_td);
// }
function newTd(value, colName) {
	var td = document.createElement("td");
	td.innerHTML = value;
	td.className = "so_" + colName;
	return td;
}

function showlist() {
	$.ajax({
				url : "../../dbconf/rel_apps.do?id=" + page.dbId,

				success : function(data) {
					// alert(data);
					// document.write(data);
					// data = eval(data);
					renderlist(data);
					// 表格全部画出来之后，再排序
					// 表格排序
					$("#table1").tablesorter({
								sortList : [[0, 0]]
							});

				}

			});
}