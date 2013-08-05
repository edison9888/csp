/*
 */

/**

 * ������� // ʹ��ajax�Ӻ�̨��ȡ����Ϊ������Ҳ��������һ���Ի�ȡ�ꡣ // option value��appId��, option
 * text��appName��
 */


/**
 * ���ӹ�˾��Ϣ��Ӧ����ֱ��;
 */
function NavigateWidget2(config) {
	var companyNameList;
	var groupListMap;
	var appListMap;

	var appNameMap;
	var appNameList;
	var getCompanyNameByAppName = function(appName) {
			var m = appNameMap[appName];
			if(typeof m=='undefined'){
				//���δ���壬����һ�����ڵ�ֵ
				return companyNameList[0];
			}
			return appNameMap[appName].companyName;
	};

	var getGroupNameByAppName = function(appName) {
		var m = appNameMap[appName];
			if(typeof m=='undefined'){
				//���δ���壬����һ�����ڵ�ֵ
				return groupListMap[companyNameList[0]][0];
			}
		return appNameMap[appName].groupName;
	};

	var config = {
		appName : (typeof config == 'undefined') ? (typeof config) : config.appName
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
					url : base + "/app_info.do?method=allEffectiveAppInfo3",
					success : function(data) {
						companyNameList = data.companyNameList;
						appNameMap = data.appNameMap;
						//appNameIdMap = data.appNameIdMap;
						groupListMap = data.groupListMap;
						appListMap = data.appListMap;
						appNameList = data.appNameList;
						//window.appNameIdMap = appNameIdMap;
							
						if (typeof config.appName != "undefined" && config.appName !='' && config.appName != 'undefined') {
							
							renderCompanyLevel(getCompanyNameByAppName(config.appName));
							
							renderGroupLevel(
									getCompanyNameByAppName(config.appName),
									getGroupNameByAppName(config.appName));
							renderAppLevel(getCompanyNameByAppName(config.appName),
									getGroupNameByAppName(config.appName),
									config.appName);
						} else {
							renderCompanyLevel();
							renderGroupLevel($("#companySelect").val());
							renderAppLevel($("#companySelect").val(),
									$("#groupSelect").val());
						}

						//auto();
					}
				});
	};
	/** companyName:ָ��ѡ���� */
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
	 * groupName:ָ��ѡ���� companyName: ��ȡoption list
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
	 * appId :ָ��ѡ���� companyName��groupName: ��ȡoption list
	 */
	var renderAppLevel = function(companyName, groupName, appName) {
		var data = appListMap[companyName + '`' + groupName];
		clearSelect(appSelect);
		for (var i = 0; i < data.length; i++) {
			var an = data[i];
			//var currLoopAppId = appNameIdMap[appName];
			var len = appSelect.options.length;
			appSelect.options[len] = new Option(an, an);
			// console.log("appSelect----------------:appName: "+appName+"\t
			// appId: "+ currLoopAppId);
			if (appName == an) {
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
			// ��������ڶ�������Ҫ�������������Ӧ�ü���
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

	
	init();

}// Nav2

