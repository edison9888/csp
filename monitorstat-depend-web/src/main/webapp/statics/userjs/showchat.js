/**
 *	author:	zhongting.zy
 *  使用Dracular库来显示强弱依赖的js 
 */

window.onload = function() {

	//设定好长宽
	var width = $(document).width();	//去掉侧边栏的宽度
	var height = $(document).height();
	document.getElementById('canvas').style.width = width + 'px';
	document.getElementById('canvas').style.height = height + 'px';

	//按层数固定颜色,层数可扩展
	var colorArray = new Array("#D76565", "#15ACCB", "#feb");	

	resizeOfBody = function() {
		//alert('resizeOfBody:' + width);
		width = $(document).width();	
		height = $(document).height();
		document.getElementById('canvas').style.width = width + 'px';
		document.getElementById('canvas').style.height = height + 'px';
	}

	/**
	 * 显示依赖我的应用
	 * @param value			选择的应用的名称
	 * @param selectDate	选择的日期
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
						parent.alert("没有依赖");
						return;
					}
					showGraph(json.meDepNodeList);
				} 
		); 
	}
	
	/**
	 * 显示我依赖的应用
	 * @param path	当前请求的路径
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
		//如果字符串长度大于numberOfLine，则换行。
		var times = Math.floor(str.length / numberOfLine);
		for(i=0; i<times; i++) {
			str = str.substring(0, (i+1)*numberOfLine + i) + "\n" + str.substring((i+1)*numberOfLine + i);
		}
		return str;
	}

	/**
	 * 显示图表
	 * @param jsonObj
	 */
	showGraph = function(jsonArray) {
		
		//画图前，清空DIV的内容，防止重复画
		clearGraph();

		var g = new Graph();
		g.edgeFactory.template.style.directed = true;	//使用箭头连接线

		var render = function(r, n) {
			var color = n.appType == "center" ? colorArray[0] : colorArray[1];	//不同应用使用颜色区分
			
			var appName = seperateString(n.lable);
			var set = r.set().push(r.rect(n.point[0]-30, n.point[1]-13, 60, 44).attr({"fill": color, r : "12px", "stroke-width" : n.distance == 0 ? "3px" : "1px" })).push(
			r.text(n.point[0], n.point[1] + 10, appName));	
			
			//set.mouseover(function(){alert(n.lable) });		//获取应用的实际名称，然后调用一个函数传入后台
			return set;
		};

		var groupMap ={};
		
		//遍历对象，添加节点
		for(i=0; i < jsonArray.length; i++) {
			groupMap[jsonArray[i].id] = jsonArray[i];
			
			g.addNode(jsonArray[i].id,{
				lable: jsonArray[i].name,
				appType: jsonArray[i].appType,		//传入一个appType到render中
				//parentId: jsonArray[i].parentId,	//传入一个parentId到render中
				render : render
			});
		}
		
		for(i=0; i<jsonArray.length; i++) {
			var tempArray = jsonArray[i].childIds; 
			if(tempArray.length != 0) {
				for(j=0; j<tempArray.length; j++) {
					
					//依赖关系根据子节点的属性来确定
					var tempNode = groupMap[tempArray[j]];
					if(tempNode.state == "strong") {
						g.addEdge(jsonArray[i].id, tempArray[j],{
							stroke : "#bfa" , fill : "#56f"
						});	
					} else {	
						//, {label : ''}	//添加标签可以通过label
						g.addEdge(jsonArray[i].id, tempArray[j]);	
					}
				}
			}
		}

		//排序
		//var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
		var layouter = new Graph.Layout.Spring(g);
		var renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
		renderer.draw();
	};

	
	//进入页面就显示
	if(opsName != "null" && selectDate != "null") {	//只有传入参数的时候，才进行查询
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