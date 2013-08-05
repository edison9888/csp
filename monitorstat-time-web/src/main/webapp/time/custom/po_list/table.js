function poListTable(config){
	var config = {
		method : config.method,
		appName : config.appName,
		keyName : config.keyName,
		poType : config.poType,
		property : config.property,
		properties : config.properties
	};
	$.ajax({
		url : base+"/app/detail/custom/show.do?method="+config.method+"&appName="+config.appName+"&keyName="+config.keyName+"&property="+config.property,	
		success : function (data){
			var list = data.list;
			var tbody="";
			var title="<tr>";
			for(var i=0;i<config.properties.length;i++){
				title+="<td>"+config.properties[i]+"</td>";	
			}
			title+="</tr>";
			tbody+=title;
			for(var i=0;i<list.length;i++){
				var tr="<tr>";
				for(var j=0;j<config.properties.length;j++){	
					var td = "<td>"+list[i][config.properties[j]]+"</td>";
					tr += td;
				}
				tr+="</tr>";
				tbody+=tr;
			}
			$("#poListTableBody").html(tbody);
		}
	});
	window.setTimeout(function(){poListTable(config)},60000);
}