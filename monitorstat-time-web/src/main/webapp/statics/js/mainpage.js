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
		document.getElementById("appNameSelect").options.length=0;		

	}
	fillSubSelect = function(groupName,value){
		var group = groupMap[groupName];
		var ops = document.getElementById("appNameSelect").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appNameSelect").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appNameSelect").options[len-1].selected=true;
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
	
	gotoCommonIpSummary= function(path,keyName){
		var opsName = $('#appNameId').val();
		window.location.href =path+"/common.do?method=queryAppIpsSummary&opsName="+opsName+"&keyName="+keyName;
	}

	gotoHSFProviderSummary = function(path){
		var opsName = $('#appNameId').val();
		window.location.href =path+"/hsf.do?method=appProviderSummary&providerApp="+opsName;
	}
	gotoHSFConsumerSummary = function(path){
		var opsName = $('#appNameId').val();
		window.location.href =path+"/hsf.do?method=appConsumerSummary&consumerApp="+opsName;
	}
	gotoAllHsfIpSummary = function(path){
		var opsName = $('#appNameId').val();
		window.location.href =path+"/hsf.do?method=queryAppIpsSummary&appName="+opsName;
	}
	
	gotoAllTairIpSummary = function(path){
		var opsName = $('#appNameId').val();
		window.location.href =path+"/tair.do?method=doQueryConsumerIpSummary&appName="+opsName;
	}
 
 	setSelectDate = function(date) {
 		if(date == null || date == undefined) {
 			var dateStr = new Date(); 
 			return dateStr.getFullYear() + "-" + (dateStr.getMonth() + 1) + "-" + dateStr.getDate();
 		}
 		return date;
 	}
 
 	changeColor = function(id) {
 		try {
 			//document.getElementById(id).style.backgroundColor = '#18ADCD';
 			document.getElementById(id).className = 'selectLeftMenu';
 		}catch(exception){
 			alert('id error');
 		} 
 	}
 	
	
})