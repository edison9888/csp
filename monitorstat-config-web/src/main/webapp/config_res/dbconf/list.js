window.onload = function() {
	showlist();
	// temp commented
	// model_1_part2();

	// confirm1Show();
};

var page = {};
// 表格中的“操作”列
page.operate = {};

page.operate.links2 = {
	/* 格式：operate:[desc,css class] */
	edit : ["修改", "editAction"],
	delete_1 : ["删除", "deleteAction"],
	view : ["查看", "viewAction"],
	realtime : ["设置实时应用", "setRTAppAction"]
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
				url : base + "/dbconf/list.do",

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

// alert(createOperateLinks(10)); -- test function
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
function viewAction(id, $this, link, tr) {
	window.location.href = link;
}

function editAction(id, $this, link, tr) {
	window.location.href = link;
}

function deleteAction(id, $this, link, tr) {
	// alert("deleteAction");

	model_1_part1(link, tr);

	// model_2(link, tr);
}

function setRTAppAction(link) {
	window.location.href = link;
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
		
		//一个className中可能包含多个class，找出符合条件的class
		var ccSplits = cc.split(/\s+/);
		
		var target;
		
		for(var j=0;j< ccSplits.length; j++){
			var item = ccSplits[j];

			if(item.indexOf("so_")!= -1)
				target = item;
		}
		//alert(target);
		target = target.substring(3);
		colNames[colNames.length] = target;

	}
	/**
	 * 
	 * 
	 */
	for (var i = 0; i < data.length; i++) {

		var item = data[i];
		var tr = document.createElement("tr");

		var po = item.po;
		var dbId = po.dbId;
		/* 这里colNames对应页面上显示的列，而不是直接和后台的某条记录对应的 */
		for (var j = 0; j < colNames.length; j++) {
			var colName = colNames[j];

			var v = handleValue(colName, po, item, dbId);
			var td = addTd(tr, v, colName);
			addEvents(td, dbId, tr);
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
			edit : base+"/dbconf/edit.do?id=" + id,
			delete_1 : base+"/dbconf/delete.do?id=" + id,
			view : base+"/dbconf/view.do?id=" + id,
			setRTApp : base+"/config/dbconf/rel_apps.jsp?id=" + id
		}
		var hrefs = td.getElementsByTagName("a");
		// alert(hrefs.length);

		for (var j in hrefs) {
			var href = hrefs[j];
			// for(var i in href){
			// alert(i);
			//		
			// }
			if (href.className == "deleteAction") {

				href.onclick = function() {
					deleteAction(id, this, links1.delete_1, tr);
				}

			} else if (href.className == "editAction") {
				href.onclick = function() {
					editAction(id, this, links1.edit, tr);
				}

			} else if (href.className == "viewAction") {
				href.onclick = function() {
					// alert(links1.view);
					viewAction(id, this, links1.view, tr);
				}
				// alert(href.onclick);
			} else if (href.className == "setRTAppAction") {
				href.onclick = function() {
					// alert(links1.view);
					setRTAppAction(links1.setRTApp);
				}
				// alert(href.onclick);
			}
		}

	}

}
function handleValue(colName, po, item, dbId) {
	var v;
	if (colName == "type1num") {
		v = item[colName];
		v = "(" + v + ")";
	} else if (colName == "operate") {
		v = createOperateLinks(dbId);
	} else {
		v = po[colName];
		if (colName == "dbType") {
			v = (v == 0) ? "核心库" : ((v == 1) ? "业务库" : "外部库");
		}
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
