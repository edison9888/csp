/**
 *	author:	zhongting.zy
 *  ʹ��Dracular������ʾǿ��������js 
 */

window.onload = function() {

	//�趨�ó���
	var width = $(document).width();	//ȥ��������Ŀ��
	var height = $(document).height();
	document.getElementById('canvas').style.width = width + 'px';
	document.getElementById('canvas').style.height = height + 'px';

	//�������̶���ɫ,��������չ
	var colorArray = new Array("#D76565", "#15ACCB", "#feb");	

	resizeOfBody = function() {
		//alert('resizeOfBody:' + width);
		width = $(document).width();	
		height = $(document).height();
		document.getElementById('canvas').style.width = width + 'px';
		document.getElementById('canvas').style.height = height + 'px';
	}

	/**
	 * ��ʾ�����ҵ�Ӧ��
	 * @param value			ѡ���Ӧ�õ�����
	 * @param selectDate	ѡ�������
	 */
	showDependOnMe = function(value, selectDate, optType) {
		var actionUrl = contextPath + "/checkupdepend.do?method=showMeDependGraph";
		//var value = $('#appNameSelect').val();
		//var selectDate = $('#selectDate').val();
		$.getJSON(
				actionUrl,
				{ 
					opsName: value,
					selectDate: selectDate,
					optType: optType
				},
				function(json){
					if(json.message == "failure") {
						clearGraph();
						parent.alert("û������");
						return;
					}
					showGraph(json.meDepNodeList);
				} 
		); 
	}
	
	/**
	 * ��ʾ��������Ӧ��
	 * @param path	��ǰ�����·��
	 */
	clearGraph = function() {
		try {
			$('#canvas').empty();	
			width = $(document).width();
			height = $(document).height();
			document.getElementById('canvas').style.width = width + 'px';
			document.getElementById('canvas').style.height = height + 'px';
		}catch(err) {
			//alert(err.description);
		}
	}
	
	/**
	 * 
	 */
	seperateString = function(str) {
		var numberOfLine = 10;
		//����ַ������ȴ���numberOfLine�����С�
		var times = Math.floor(str.length / numberOfLine);
		for(i=0; i<times; i++) {
			str = str.substring(0, (i+1)*numberOfLine + i) + "\n" + str.substring((i+1)*numberOfLine + i);
		}
		return str;
	}

	/**
	 * ��ʾͼ��
	 * @param jsonObj
	 */
	showGraph = function(jsonArray) {
		
		//��ͼǰ�����DIV�����ݣ���ֹ�ظ���
		clearGraph();

		var g = new Graph();
		g.edgeFactory.template.style.directed = true;	//ʹ�ü�ͷ������

		var render = function(r, n) {
			var color = n.appType == "center" ? colorArray[0] : colorArray[1];	//��ͬӦ��ʹ����ɫ����
			
			var appName = seperateString(n.lable);
			var set = r.set().push(r.rect(n.point[0]-30, n.point[1]-13, 60, 44).attr({"fill": color, r : "12px", "stroke-width" : n.distance == 0 ? "3px" : "1px" })).push(
			r.text(n.point[0], n.point[1] + 10, appName));	
			
			//set.mouseover(function(){alert(n.lable) });		//��ȡӦ�õ�ʵ�����ƣ�Ȼ�����һ�����������̨
			return set;
		};

		var groupMap ={};
		
		//����������ӽڵ�
		for(i=0; i < jsonArray.length; i++) {
			groupMap[jsonArray[i].id] = jsonArray[i];
			
			g.addNode(jsonArray[i].id,{
				lable: jsonArray[i].name,
				appType: jsonArray[i].appType,		//����һ��appType��render��
				//parentId: jsonArray[i].parentId,	//����һ��parentId��render��
				render : render
			});
		}
		
		for(i=0; i<jsonArray.length; i++) {
			var tempArray = jsonArray[i].childIds; 
			if(tempArray.length != 0) {
				for(j=0; j<tempArray.length; j++) {
					
					//������ϵ�����ӽڵ��������ȷ��
					var tempNode = groupMap[tempArray[j]];
					if(tempNode.state == "strong") {
						g.addEdge(jsonArray[i].id, tempArray[j],{
							stroke : "#bfa" , fill : "#56f"
						});	
					} else {	
						//, {label : ''}	//��ӱ�ǩ����ͨ��label
						g.addEdge(jsonArray[i].id, tempArray[j]);	
					}
				}
			}
		}

		//����
		//var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
		var layouter = new Graph.Layout.Spring(g);
		var renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
		renderer.draw();
	};

	
	//����ҳ�����ʾ
	if(opsName != "null" && selectDate != "null") {	//ֻ�д��������ʱ�򣬲Ž��в�ѯ
		showDependOnMe(opsName, selectDate, optType);
	}
	
//	var paper = Raphael('canvas', 320, 200);

//	// Creates circle at x = 50, y = 40, with radius 10
//	var circle = paper.circle(50, 40, 10);
//	// Sets the fill attribute of the circle to red (#f00)
//	circle.attr("fill", "#f00");

//	// Sets the stroke attribute of the circle to white
//	circle.attr("stroke", "#fff");

//	circle.mouseover(function () {  
//	alert('where amazing happens!');  
//	});
}