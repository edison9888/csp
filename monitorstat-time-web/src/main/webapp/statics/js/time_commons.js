/**
 * 
 * 导航相关 // 使用ajax从后台获取；因为数据量也不大，所以一次性获取完。 // option value（appId）, option
 * text（appName）
 */

/**
 * 增加公司信息、应用名直达;
 */
function NavigateWidget2(config,path) {
	var companyNameList;
	var groupListMap;
	var appListMap;
	
	var contextPath = path;

	var appIdMap;
	var appNameIdMap;
	var appNameList;
	var getCompanyNameByAppId = function(appId) {
		return appIdMap[appId].companyName;
	};

	var getGroupNameByAppId = function(appId) {
		return appIdMap[appId].groupName;
	};

	var config = {
		appId : (typeof config == 'undefined') ? (typeof config) : config.appId
	};

	var companySelect = $("#companySelect").get(0);
	var groupSelect = $("#groupSelect").get(0);
	var appSelect = $("#appSelect").get(0);

	var init = function() {
		addChangeEvent();
		grabAndRender();

	}

	var grabAndRender = function() {
		$.ajax({
					url : contextPath + "/app_info.do?method=allEffectiveAppInfo3",
					success : function(data) {
						companyNameList = data.companyNameList;
						appIdMap = data.appIdMap;
						appNameIdMap = data.appNameIdMap;
						groupListMap = data.groupListMap;
						appListMap = data.appListMap;
						appNameList = data.appNameList;
						window.appNameIdMap = appNameIdMap;
							
						if (typeof config.appId != "undefined" && config.appId !='') {
							renderCompanyLevel(getCompanyNameByAppId(config.appId));
							renderGroupLevel(
									getCompanyNameByAppId(config.appId),
									getGroupNameByAppId(config.appId));
							renderAppLevel(getCompanyNameByAppId(config.appId),
									getGroupNameByAppId(config.appId),
									config.appId);
						} else {
							renderCompanyLevel();
							renderGroupLevel($("#companySelect").val());
							renderAppLevel($("#companySelect").val(),
									$("#groupSelect").val());
						}

						auto();
					}
				});
	};
	/** companyName:指定选中项 */
	var renderCompanyLevel = function(companyName) {
		// console.log("renderCompanyLevel()");
		var data = companyNameList;
		clearSelect(companySelect);
		for (var i = 0; i < data.length; i++) {
			var gn = data[i];
			var len = companySelect.options.length;
			companySelect.options[len] = new Option(gn, gn);
			if (companyName == gn) {
				companySelect.options[len].selected = true;
			}

		}

	}
	/**
	 * groupName:指定选中项 companyName: 获取option list
	 */
	var renderGroupLevel = function(companyName, groupName) {
		// console.log('renderGroupLevel-----------------');
		var data = groupListMap[companyName];
		clearSelect(groupSelect);
		for (var i = 0; i < data.length; i++) {
			var gn = data[i];
			// console.log('renderGroupLevel-----------------data[i]: '+ gn);
			var len = groupSelect.options.length;
			groupSelect.options[len] = new Option(gn, gn);
			if (groupName == gn) {
				groupSelect.options[len].selected = true;
			}

		}
	}

	/**
	 * appId :指定选中项 companyName、groupName: 获取option list
	 */
	var renderAppLevel = function(companyName, groupName, appId) {
		var data = appListMap[companyName + '`' + groupName];
		clearSelect(appSelect);
		for (var i = 0; i < data.length; i++) {
			var appName = data[i];

			var currLoopAppId = appNameIdMap[appName];
			var len = appSelect.options.length;
			appSelect.options[len] = new Option(appName, currLoopAppId);
			// console.log("appSelect----------------:appName: "+appName+"\t
			// appId: "+ currLoopAppId);
			if (appId == currLoopAppId) {
				appSelect.options[len].selected = true;
			}
		}
	}

	var addChangeEvent = function() {
		companySelect.onchange = function() {
			// console.log('companySelect.onchange-------------');
			var c = companySelect.options[companySelect.selectedIndex].value;
			// console.log('companySelect.onchange-------------c: '+c);
			renderGroupLevel(c);
			// 不仅处理第二级，还要处理第三级——应用级别
			var g = groupSelect.options[groupSelect.selectedIndex].value;
			renderAppLevel(c, g);
		}

		groupSelect.onchange = function() {
			// console.log('groupSelect.onchange-------------');
			var c = companySelect.options[companySelect.selectedIndex].value;
			var g = groupSelect.options[groupSelect.selectedIndex].value;
			renderAppLevel(c, g);
		}
	}
	var clearSelect = function(sel) {
		sel.options.length = 0;
	}
	try{
	/** 文本框自动填充 */
	var auto = function() {
		$("#appname_text").autocomplete({
					source : appNameList,
					select : function(event, ui) {
						 // var id = appNameIdMap[ui.item.value];
					}
				});
		var appId2 = getSelectAppId();
		$("#keyName").autocomplete(
				{
					source: base+"/app/detail/custom/show.do?method=getKeyName&appId="+appId2,
					minLength: 2,
					select: function( event, ui ) {
					}
				});
		
	}
	}catch(exception){
	}

	init();

}// Nav2

/**
 * 取得当前选择的应用的ID
 */
function getSelectAppId() {

	return $("#appSelect").val();
}

function getSelectAppName() {
	return $("#appSelect").text();
}

function getInputAppId(){
	var inputText = $("#appname_text").val();
	var id = window.appNameIdMap[inputText];
	if(typeof id == 'undefined')
		return 1;
	return id; 
	
}

function loadNav(base, appId, urlPrefix){
	$("#page_nav").load(base + '/page_nav.jsp', {urlPrefix:urlPrefix,appId:appId});
}