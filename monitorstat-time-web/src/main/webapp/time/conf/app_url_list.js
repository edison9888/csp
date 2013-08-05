window.onload = function() {
	showlist();
};

function handleValue(colName, po, item, dbId) {
	var v;
	if (colName == "operate") {
		v = createOperateLinks(dbId);
	} else {
		v = po[colName];
		if (colName == "modifyDate") {
			v = longToFormatStr(v['time'],"yyyy-MM-dd HH:mm:ss");
		}else if (colName == "dynamic") {
			v = v == true ? "��" : "��";
		}
	}
	return v;
}

// ����еġ���������
page.operate = {};

page.operate.links2 = {
	/* ��ʽ��operate:[desc,css class] */
	edit : ["�޸�", "editAction"],
	delete_1 : ["ɾ��", "deleteAction"]
};


function addEvents(td, id, tr) {
	if (td.className == "so_operate") {
		var links1 = {
			edit : base +"/app/conf/url/show.do?method=edit&appId="+ page.appId+"&id=" + id,
			delete_1 : base + "/app/conf/url/show.do?method=delete&appId="+ page.appId+"&id=" + id,
			view : base + "/app/conf/url/show.do?method=view&appId="+ page.appId+"&id=" + id
		}
		var hrefs = td.getElementsByTagName("a");
		// alert(hrefs.length);

		for (var j in hrefs) {
			var href = hrefs[j];
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
					viewAction(id, this, links1.view, tr);
				}
			} 
		}

	}

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
	 * 1����������divƬ�� 1.1������divƬ�β�����id���ԣ������Ҫ�����ƺ��Ƭ��Ҫ�����µ�id
	 * 
	 */

	var c1_htm = $("#confirm1").html();

	TINY.box.show(c1_htm, 0, 200, 100, 0);
	// ���������Ҫ�Ǹ���show()��������c1_htm�����ģ���Ԫ������¼�
	model_1_part2();

}

function confirm1Hide() {
	TINY.box.hide();
}

function model_1_part2() {
	$(" .confirm1_yes").click(function() {
				// ִ��ɾ������
				doDelete(page.confirm1.currLink, page.confirm1.currTr);
				confirm1Hide();
			});
	$(" .confirm1_no").click(function() {
				confirm1Hide();
			});
	$(" .confirm1_close").click(function() {
				confirm1Hide();
			});

	// ע������Ĳ���������ֱ�Ӵ�deleteAction�����ȡ������ͨ��c.callBackParams��ȡ
	function doDelete($link, $tr) {
		$.ajax({
					url : $link,
					success : function(data) {
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
				url : base + "/app/conf/url/show.do?method=list&appId="
						+ page.appId,
				success : function(data) {
					renderlist(data);
				}
			});
}

function createOperateLinks(id) {

	var str = "";
	for (var i in page.operate.links2) {
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
/** model_1_part1(link, tr); model_1_part2();ʹ��bootstrap�����˵��Լ�д�Ĵ��� */
/** model_2()ʹ��window.confirm()ʵ�� */
function model_2(link, tr) {
	if (!confirm("�����Ҫɾ����û�а�"))
		return;

	$.ajax({
				url : link,
				success : function(data) {
					// alert("��ɾ��!");
					deleteUI(tr);
				}
			});

	function deleteUI(tr) {
		tr.parentNode.removeChild(tr);
	}

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
	// ����col��ָtable��col����ӦPO���������
	var colNames = [];
	var cols = $("table thead tr th");
	for (var i = 0; i < cols.length; i++) {
		var cc = cols[i].className;

		// һ��className�п��ܰ������class���ҳ�����������class
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

	for (var i = 0; i < data.length; i++) {

		var item = data[i];
		var tr = document.createElement("tr");

		var po = item.po;
		var dbId = po.id;
		/* ����colNames��Ӧҳ������ʾ���У�������ֱ�Ӻͺ�̨��ĳ����¼��Ӧ�� */
		for (var j = 0; j < colNames.length; j++) {
			var colName = colNames[j];

			var v = handleValue(colName, po, item, dbId);
			var td = addTd(tr, v, colName);
			addEvents(td, dbId, tr);
		}
		t1_tb.appendChild(tr);
	}
}

function addTd(tr, value, colName) {
	var td = newTd(value, colName);
	tr.appendChild(td);
	return td;
}


function newTd(value, colName) {
	var td = document.createElement("td");
	td.innerHTML = value;
	td.className = "so_" + colName;
	return td;
}
//��Ҫdate.js
function longToFormatStr(time,format){
	var d = new Date();
	d.setTime(time);
	return d.toString(format);
}