/*
 */

/**
 * 导航相关 // 使用ajax从后台获取；因为数据量也不大，所以一次性获取完。 // option value（appId）, option
 * text（appName）
 */
function NavigateWidget2(config) {
	// 用于一级select
	var groupList;
	// 用于二级select
	var groupMap;
	// appId指定当前应用

	var config = {
		appId : (typeof config=='undefined')? (typeof config) : config.appId
	};

	var groupSelect = $("#groupSelect2").get(0);
	var appSelect = $("#appSelect2").get(0);

	var init = function() {
		// alert("init()");
		addChangeEvent();
		getGroupList();
	}

	var getGroupList = function() {
		$.ajax({
					url : base + "/app_info.do?method=allEffectiveAppInfo",
					async : false,
					success : function(data) {
						groupList = data.groupList;
						groupMap = data.groupMap;
						// alert(data.groupList);
						renderFirstLevel();
						renderSecondLevel($("#groupSelect2").val());
					}
				});
	};

	var renderFirstLevel = function() {
		var data = groupList;
		clearSelect(groupSelect);
		for (var i = 0; i < data.length; i++) {
			var gn = data[i];
			var len = groupSelect.options.length;
			// alert(len);
			groupSelect.options[len] = new Option(gn, gn);
		}
	}

	var renderSecondLevel = function(groupName) {
		var data = groupMap[groupName];
		clearSelect(appSelect);
		for (var i = 0; i < data.length; i++) {
			var appId = data[i].appId;
			var appName = data[i].appName;
			var len = appSelect.options.length;
			appSelect.options[len] = new Option(appName, appId);
			
			if(typeof config.appId !="undefined" && config.appId == appId){
				appSelect.options[len].selected=true;
			}
		}
	}

	var addChangeEvent = function() {
		groupSelect.onchange = function() {
			// alert("change");
			var g = groupSelect.options[groupSelect.selectedIndex].value;
			// alert(g);
			if (g == -1) {
				clearSelect(appSelect);
				appSelect.options[0] = new Option("**请选择**", "-1");
				return;
			}
			renderSecondLevel(g);
		}

		appSelect.onchange = function() {
			var sel = appSelect;
			var v = sel.options[sel.selectedIndex].value;
			if (v == -1)
				return;
		}

	}
	var clearSelect = function(sel) {
		sel.options.length = 0;
	}
	init();

}
// 导航相关 end
/**
 * 取得当前选择的应用的ID
 */
function getSelectAppId2(){
	return $("#appSelect2").val();
}
function getSelectAppName2(){
	return $("#appSelect2 option:selected").text();
}