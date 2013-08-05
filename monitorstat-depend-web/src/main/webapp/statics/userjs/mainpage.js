$(document).ready(function(){

	/** 小赌自己实现的级联*/
	var groupMap ={}
	addAppGroup = function(groupName,appName,appId) {
		if(!groupMap[groupName]){
			groupMap[groupName]={};
		}
		if(!groupMap[groupName][appName]){
			groupMap[groupName][appName]=appId;
		}			
	}

	groupChange = function(selectObj){
		var groupName = selectObj.options[selectObj.selectedIndex].value;
		var group = groupMap[groupName];
		if(group){
			clearSubSelect();
			fillSubSelect(groupName);
		}
	}

	clearSubSelect = function(){
		document.getElementById("appSelect").options.length=0;		

	}
	fillSubSelect = function(groupName,value){
		var group = groupMap[groupName];
		var ops = document.getElementById("appSelect").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appSelect").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appSelect").options[len-1].selected=true;
			}
		}
	}
	
	
	findGroupNameByAppName = function(opsName){
		for(groupName in groupMap){
			for(app in groupMap[groupName]){
				 if(app == opsName){
					 return groupName;
				 }
			}
		}
		return "";
	}

	initParentSelect = function(gvalue){
		var gname = findGroupNameByAppName(gvalue);
		clearSubSelect();
		var len = document.getElementById("parentGroupSelect").options.length;
		for (name in groupMap){
			document.getElementById("parentGroupSelect").options[len++]=new Option(name,name);
			if(name == gname){
				document.getElementById("parentGroupSelect").options[len-1].selected=true;
			}
		}
		if(gname&&gvalue){
			fillSubSelect(gname,gvalue);
		}else{
			groupChange(document.getElementById("parentGroupSelect"));
		}
	}
	
	function getYestoday(date){  
	    var yesterday_milliseconds = date.getTime() - 1000*60*60*24;       
	    var yesterday = new Date();       
	    yesterday.setTime(yesterday_milliseconds);       
	    var strYear = yesterday.getFullYear();    
	    var strDay = yesterday.getDate();    
	    var strMonth = yesterday.getMonth() + 1;  
	    if(strMonth < 10){    
	        strMonth="0" + strMonth;    
	    }    
	    datastr = strYear + "-" + strMonth + "-" + strDay;  
	    return datastr;  
	  }	
	
	/******************调整JS*****************************************************************/		
	
	
	initGroupSelect = function(path,opsName){
		$.getJSON(path+"/checkupdepend.do?method=getAllAppInfo",
				function(jsonArray){
					if(jsonArray == null || jsonArray.length == 0) {
						return;
					}
					for(i=0; i<jsonArray.length; i++) {
						addAppGroup(jsonArray[i].groupName, jsonArray[i].opsName, jsonArray[i].opsName);	
					}
					if(opsName != "") {
						initParentSelect( opsName);
					} else {
						initParentSelect(jsonArray[0].opsName);	
					}
				} 
		); 	
	}
	gotoConfigList = function(path){
		var opsName = $('#appSelect').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		window.location.href =path+"/config.do?method=showconfiglist&opsName="+opsName;
	}
	
	gotoMeDepend = function(path,type){
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/show.do?method=showMeDependTable&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
	}
	
	gotoDependMe = function(path,type){
		var opsName = $('#appSelect').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/show.do?method=showDependMeTable&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
	}
	
	gotoAppCenterHsfInfo = function(path,type){
		var opsName = $('#queryHiddenField').val();
		var selectDate = setSelectDate($('#selectDate').val());
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		if(type == 'provide') {
			window.location.href =path+"/show/hsfprovide.do?method=showAppCenterHsfInfo&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;	
		} else if(type == 'consume') {
			window.location.href =path+"/show/hsfconsume.do?method=showAppCenterConsumeHsfInfo&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
		}
	}
	
	gotoAppCenterTairInfo = function(path,type) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		if(type == 'consume') {	//调用tair信息
			window.location.href =path+"/show/tairconsume.do?method=showTairConsumeMain&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;	
		} else if(type == 'provide') {
			window.location.href =path+"/show/tairconsume.do?method=showAppCenterConsumeHsfInfo&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
		}		
	}
	
	gotoAppCenterNotifyInfo = function(path,type) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		if(type == 'consume') {	//调用tair信息
			window.location.href =path+"/show/notify.do?method=showIndex&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;	
		} else if(type == 'provide') {
			window.location.href =path+"/show/notify.do?method=showIndex&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
		}		
	}
	
	gotoURLPage = function(path,type) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		if(type == 'origin') {
			window.location.href =path+"/show/urlorigin.do?method=showUrlOriginMain&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;	
		} else if(type == 'response') {
			window.location.href =path+"/show/urlresponse.do?method=showAppCenterConsumeHsfInfo&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
		}		
	}	
	
	gotoTddlPage = function(path,type) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		if(type == 'consumeTddl') {
			window.open(path+ "/tddl/show.do?method=list");
			//window.location.href =path+"/show/tddlconsume.do?method=showTddlConsumeMain&opsName="+opsName+"&selectDate="+selectDate;	
		} else if(type == 'privodeTddl') {
			window.location.href =path+"/show/urlresponse.do?method=showAppCenterConsumeHsfInfo&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;
		}		
	}
	//TFS部分
	gotoTfsPage = function(path,type) {
		var selectDate = setSelectDate($('#selectDate').val());
		if(type == 'provideTfs') {
			window.location.href =path+"/show/tfsprovide.do?method=showTfsMain&selectDate="+selectDate;	
		} else if(type == 'privodeTddl') {
			//
		}		
	}	
	
	//PVUV
	gotoPVUVPage = function(path,type) {
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/show/urlorigin.do?method=queryDomainAndUrl&startDate="+selectDate;	
	}	
	
	//应用主页
	gotoAppIndexPage = function(path,type) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/main/appmain.do?method=showAppIndexMain&opsName="+opsName+"&showType="+type+"&selectDate="+selectDate;	
	}		

	//Tair汇总分布
	gotoTairSummaryPage = function(path,type) {	//tairsummary
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/show/tairconsume.do?method=showTopTair&collectTime="+selectDate;	
	}	
	
	//依赖地图主页
	gotoDependMapPage = function(path,type) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/show/dependmap.do?method=index&opsName="+opsName+"&selectDate="+selectDate;	
	}
	//依赖路径诊断
	gotoDependPathPage = function(path) {
		var opsName = $('#queryHiddenField').val();
		if(opsName == undefined || opsName == "")
			opsName = 'itemcenter';
		var selectDate = setSelectDate($('#selectDate').val());
		window.location.href =path+"/rate/callrate.do?method=gotoTopoPage&opsName="+opsName+ "&appName=" + opsName + "&selectDate="+selectDate;	
	}
	
	//show/tairconsume.do?method=showTopTair
	 openWin = function(path,h,w){
		 //窗口名称为''，是为了可以重复弹出窗口。
		 if(screen.height < (h + 130))
			 h = screen.height - 130 ;	//130包括底部工具栏的大小
		 if(screen.width < (w + 80))
			 w = screen.width - 80;
		 
		 h = screen.height - 130;
		 var topTmp = 20;//(screen.height-h)/2;
		 var leftTmp = (screen.width-w)/2;
		 window.open (path, '', 'height='+h+', width='+w+', top='+topTmp+',	left='+leftTmp+', toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no');
	 }
 
	 selectMenuColor = function(){
	 }
 
 	setSelectDate = function(date) {
 		if(date == null || date == undefined) {
 			return getYestoday(new Date());
 			//return dateStr.getFullYear() + "-" + (dateStr.getMonth() + 1) + "-" + dateStr.getDate();
 		}
 		return date;
 	}
 
 	changeColor = function(id) {
 		try {
 			//document.getElementById(id).style.backgroundColor = '#18ADCD';
 			//document.getElementById(id).className = 'selectLeftMenu';
 		}catch(exception){
 			//alert('id error');
 		} 
 		
 	}
	
/******************Button的公共查询操作*****************************************************************/	
	btnSearch = function() {
//		var object = $(".selectLeftMenu");
//		alert(object);
		
		var opsName = $('#queryHiddenField').val();
		var selectDate = $('#selectDate').val();
		var parentGroupName = $('#parentGroupSelect').val();
		
		var paramUrl = "parentGroupName=" + parentGroupName + "&opsName=" + opsName + "&selectDate=" + selectDate; 
		//	document.getElementById(id).className = 'selectLeftMenu';
		if(currentAccordion == 0) {
			searchMeDepend(paramUrl);
		} else if(currentAccordion == 1) {
			searchDependMe(paramUrl);
		}
	}
	
	/*
	 * 选择不同的菜单，不同的操作
	 */
	configSearchBar = function(value){
		currentAccordion = value;
		if(currentAccordion == 0) {						//我依赖		
			$("#searchDiv").show();
			$("#showType").attr("disabled", false);			
		} else if(currentAccordion == 1) {				//依赖我
			$("#searchDiv").show();
			$("#showType").val("list");
			$("#showType").attr("disabled", true);
		} else if(currentAccordion == 2) {				//进入配置检测
			$("#searchDiv").hide();
		} else {
			//alert("还未定义值");
		}
	}
/***********************************************************************************/		
	/**
	 * 我依赖的应用部分,点击类别超链接出发的查询操作
	 * @param dependAppType	查询的类别
	 */	
	meDependLinkSearch = function(dependAppType) {
		configSearchBar(0);
		
		var opsName = $('#queryHiddenField').val();
		var selectDate = $('#selectDate').val();
		var parentGroupName = $('#parentGroupSelect').val();
		var paramUrl = "parentGroupName=" + parentGroupName + "&opsName=" + opsName + "&selectDate=" + selectDate + "&dependAppType=" + dependAppType;
		searchMeDepend(paramUrl);
	}
	
	/**
	 * 我依赖应用查询汇总
	 * @param paramUrl	查询的拼接好的URL的后缀
	 */
	searchMeDepend = function(paramUrl) {
		//图形还是列表
		var showType = $('#showType').val();
		var middleUrl;
//		middleUrl = "/checkupdepend.do?method=changeParam&showType=" + showType + "&";
		middleUrl = "/depend/mainpage.jsp?currentAccordion=0&showType=" + showType + "&";
		var optType = $('#optType').val();
		var actionUrl = contextPath + middleUrl + paramUrl + "&optType=" + optType;
		var changeURL = encodeURI(actionUrl, "UTF-8");
		window.location.href = changeURL; 
	}
	
/***********************************************************************************/	
	/*
	 * 超链接查询（依赖我的应用）
	 */
	dependMeLinkSearch = function(dependAppType) {
		configSearchBar(1);
		
		var opsName = $('#queryHiddenField').val();
		var selectDate = $('#selectDate').val();
		var parentGroupName = $('#parentGroupSelect').val();
		var paramUrl = "parentGroupName=" + parentGroupName + "&opsName=" + opsName + "&selectDate=" + selectDate 
					+ "&dependAppType=" + dependAppType;
		searchDependMe(paramUrl);
	}	
	
	searchDependMe = function(paramUrl) {
		var showType = $('#showType').val();
		
		middleUrl = "/depend/mainpage.jsp?currentAccordion=1&showType=" + showType + "&";
		var optType = $('#optType').val();
		var actionUrl = contextPath + middleUrl + paramUrl + "&optType=" + optType;
		var changeURL = encodeURI(actionUrl, "UTF-8");
		window.location.href = changeURL; 
	}	
/***********************************************************************************/
	/*
	 * 现在配置信息较少，不排除以后增加搜索的可能
	 */
	configLink = function() {
		configSearchBar(2);
		var showType = $('#showType').val();
		var opsName = $('#appSelect').val();
		var selectDate = $('#selectDate').val();
		var parentGroupName = $('#parentGroupSelect').val();
		
		var paramUrl = "parentGroupName=" + parentGroupName + "&opsName=" + opsName + "&selectDate=" + selectDate;
		var middleUrl = "/depend/mainpage.jsp?currentAccordion=2&";
		var actionUrl = contextPath + middleUrl + paramUrl;
		var changeURL = encodeURI(actionUrl, "UTF-8");
		window.location.href = changeURL; 		
	}
})