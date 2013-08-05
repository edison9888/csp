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
				resDesc = "�ɹ�";
			else
				resDesc = "ʧ��";

			operateDesc = "���";

			// alert(content);
			// alert(TINY.box.show);
			var content = operateDesc + resDesc + "!";
			// ָ������
			var n = 2;
			TINY.box.getB().innerHTML = content;

			setTimeout(function() {

						TINY.box.hide();
						// ˢ��ҳ��
						//alert("ˢ��ҳ��");
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
						// ָ��ʱ���֮��ִ�У�������ֹ����hide��show����������������̣�������Ӿ��Ĳ������
						setTimeout(function() {

									popInfo(data);

									// ���ʧ��,���·�������
									if (res == 0) {
										relAppAddUnit();
									}
								}, 800);

					}, "json");
		}

	}
}

// ����еġ���������
page.operate = {};

page.operate.links2 = {
	/* ��ʽ��operate:[desc,css class] */
	delete_1 : ["ɾ��", "deleteAction"]
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
	 * 1����������divƬ�� 1.1������divƬ�β�����id���ԣ������Ҫ�����ƺ��Ƭ��Ҫ�����µ�id
	 * 
	 */

	var c1_htm = $("#confirm1").html();
	var e1 = document.createElement("div");
	var e1_htm = $(e1).html(c1_htm);

	TINY.box.show(c1_htm, 0, 200, 100, 0);
	// ���������Ҫ�Ǹ���show()��������c1_htm�����ģ���Ԫ������¼�
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
				// ִ��ɾ������
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

	// ע������Ĳ���������ֱ�Ӵ�deleteAction�����ȡ������ͨ��c.callBackParams��ȡ
	function doDelete($link, $tr) {
		$.ajax({
					url : $link,
					success : function(data) {
						// alert("��ɾ��!");
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
	 * ��ʽ��operate:url
	 * 
	 */

	var t1 = document.getElementById("table1");
	var t1_tb = t1.getElementsByTagName("tbody")[0];
	// alert(data.length);
	// data.length ������

	/**
	 * ���ԣ���ʾ���У�ȡ���С��ô�1�� ���������td���������⣬td���˳����cols�ж�����ˡ� �ô�2������ѭ���Ĵ���������������������
	 * 
	 */

	// ��table tr th�л�ȡ��
	// ʾ���� ["dbName", "type1num", "dbUrl", "dbType", "operate"]
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
		/* ����colNames��Ӧҳ������ʾ���У�������ֱ�Ӻͺ�̨��ĳ����¼��Ӧ�� */
		for (var j = 0; j < colNames.length; j++) {
			var colName = colNames[j];

			var v = handleValue(colName, po, item, appId);
			var td = addTd(tr, v, colName);
			addEvents(td, appId, tr);
		}

		// ������������¼�
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
					// ���ȫ��������֮��������
					// �������
					$("#table1").tablesorter({
								sortList : [[0, 0]]
							});

				}

			});
}