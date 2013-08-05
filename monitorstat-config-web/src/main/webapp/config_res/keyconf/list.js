window.onload = function() {
	showlist();
	// temp commented
	// model_1_part2();

	// confirm1Show();
};

var page = {};

function setVarOnLinksForEveryRecord(id, operate, so_aliasName, po) {

	/* 格式：operate:[url] */
	operate.links1 = {
		setAlarm : base + "/config/keyconf/rel_apps.jsp?id=" + id,
		view : base + "/keyconf/view.do?id=" + id

	};
	/* 格式：operate:[desc(链接文本),css class] */
	operate.links2 = {

		setAlarm : ["加入告警", "setAlarmAction"],
		view : ["查看", "viewAction"]

	};

	so_aliasName.links1 = {
		edit : base + "/keyconf/edit.do?id=" + id
	};

	var editV;
	if (po.aliasName != null && $.trim(po.aliasName) != "")
		editV = po.aliasName;
	else
		editV = "<span style=\"color:red\">点击填写</span>";
	so_aliasName.links2 = {
		edit : [editV, "editAction"]
	};

}

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
			node.parentNode.removeChild(node);
		}
	}
}

function showlist() {

	$.ajax({
				url : base + "/keyconf/list.do?appId=" + page.appId
						+ "&keyName=" + page.keyName,

				success : function(data) {
					renderlist(data);
					// 表格全部画出来之后，再排序
					// 表格排序
					$("#table1").tablesorter({
								sortList : [[0, 0]]
							});

				}

			});
}

/** model_1_part1(link, tr); model_1_part2();使用bootstrap，加了点自己写的代码 */
/** model_2()使用window.confirm()实现 */
function model_2(link, tr) {
	if (!confirm("想清楚要删除了没有啊"))
		return;

	$.ajax({
				url : link,
				success : function(data) {
					// alert("已删除!");
					deleteUI(tr);
				}
			});

	function deleteUI(tr) {
		tr.parentNode.removeChild(tr);
	}

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

	var colNames = getColNames();

	data = data.list;

	for (var i = 0; i < data.length; i++) {

		var item = data[i];
		var tr = document.createElement("tr");

		var po = item.po;
		var id = po.keyId;
		/* 这里colNames对应页面上显示的列，而不是直接和后台的某条记录对应的 */
		for (var j = 0; j < colNames.length; j++) {

			// alert(1);
			var colName = colNames[j];
			var operate = {}, so_aliasName = {};

			setVarOnLinksForEveryRecord(id, operate, so_aliasName, po);
			var v = handleValue(colName, po, item, id, operate, so_aliasName);
			// alert(v);
			var td = addTd(tr, v, colName);
			addEvents(td, id, tr, operate, so_aliasName);
		}

		// 给操作列添加事件
		// temp comment
		// addEventsToTd(td5, dbId, tr);

		t1_tb.appendChild(tr);
	}

}
/**
 * 策略：显示哪列，取那列。好处1： 这样解决了td间排序问题，td间的顺序在cols中定义好了。 好处2：减少循环的次数，对性能有所提升。
 * 
 */
// 从table tr th中获取。
// 示例： ["dbName", "type1num", "dbUrl", "dbType", "operate"]
function getColNames() {
	var colNames = [];
	var cols = $("table thead tr th");
	for (var i = 0; i < cols.length; i++) {
		var cc = cols[i].className;

		// 一个className中可能包含多个class，找出符合条件的class
		var ccSplits = cc.split(/\s+/);

		var target;

		for (var j = 0; j < ccSplits.length; j++) {
			var item = ccSplits[j];

			if (item.indexOf("so_") != -1)
				target = item;
		}
		// alert(target);
		target = target.substring(3);
		colNames[colNames.length] = target;

	}
	return colNames;
}
function addEvents(td, id, tr, operate, so_aliasName) {
	// alert(tr.innerHTML);
	if (td.className == "so_operate") {
		// alert(td.innerHTML);
		var hrefs = td.getElementsByTagName("a");
		// alert(hrefs.length);

		for (var j = 0; j < hrefs.length; j++) {
			// alert(j);
			var href = hrefs[j];
			if (href.className == "viewAction") {
				href.onclick = function() {
					// alert(viewUrl);
					viewAction(operate.links1.view);
				}

			} else if (href.className == "setAlarmAction") {
				href.onclick = function() {
					setAlarmAction(operate.links1.setAlarm);
				}
			}
		}
	} else if (td.className == "so_aliasName") {
		var hrefs = td.getElementsByTagName("a");
		for (var j in hrefs) {
			var href = hrefs[j];
			if (href.className == "editAction") {
				href.onclick = function() {
					editAction(so_aliasName.links1.edit);
				}
			}
		}
	}

	function viewAction(link) {
		window.location.href = link;
	}

	function editAction(link) {
		window.location.href = link;
	}

	function deleteAction(link, tr) {
		model_1_part1(link, tr);
	}

	function setAlarmAction(link) {
		window.location.href = link;
	}

}
function handleValue(colName, po, item, id, operate, so_aliasName) {
	// alert(colName);
	var v;
	if (colName == "operate") {
		v = createOperateLinks(operate.links2);
	} else if (colName == "aliasName") {
		v = createOperateLinks(so_aliasName.links2);
	} else {
		v = po[colName];

	}
	// alert(colName+': \t'+v);
	return v;

	// alert(createOperateLinks(10)); -- test function
	function createOperateLinks(links) {

		var str = "";

		for (var i in links) {
			// "<a href=\""+ url +"\">" + desc + "</a>"
			var desc = links[i][0];
			var cssClass = links[i][1];
			var split = "&nbsp;";

			str += "<a href=\"javascript:void(0)\" class=\"" + cssClass + "\">"
					+ desc + "</a>" + split;

		}

		return str;

	}

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
