function mapPoTop10Table(config){
	var config = {
		method : config.method,
		appName : config.appName,
		keyName : config.keyName,
		poType : config.poType,
		property : config.property,
		properties : config.properties
	};
	$.ajax({
		url : base+"/app/detail/custom/show.do?method="+config.method+"&appName="+config.appName+"&keyName="+config.keyName+"&poType="+config.poType+"&property="+config.property,	
		success : function (data){
			var tbody="";
			var tr="<tr>";
			tr+="<td>KEY</td>";
			for(var j=0;j<config.properties.length;j++){	
					var td = "<td>"+config.properties[j]+"</td>";
					tr += td;
				}
			tr+="</tr>";
			tbody+=tr;
			var topCount = 0;
			for(k in data){
				if(isJsonEmpty(data[k]))continue;
				topCount++;
				if(topCount>10)break;
				var tr="<tr>";
				tr+="<td>"+k+"</td>";
				for(var j=0;j<config.properties.length;j++){
					var td = "<td>"+data[k][config.properties[j]]+"</td>";
					tr += td;
				}
				tr+="</tr>";
				tbody+=tr;
			}
			$("#mapPoTableBody").html(tbody);
		}
	});
	window.setTimeout(function(){mapPoTop10Table(config)},60000);
}
function isJsonEmpty(object) {
	for(var i in object) 
		{ return false; }
	return true;
}