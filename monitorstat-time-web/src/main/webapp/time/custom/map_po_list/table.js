function mapPoListTable(config ){
		var config = {
		method : config.method,
		appName : config.appName,
		keyName : config.keyName,
		property : config.property,
		properties : config.properties
	};
		var d  = new Date();
		var time = {};
		var tarray = [];
		for(var i=0;i<10;i++){
			var h = d.getHours()>9?d.getHours():"0"+d.getHours();
			var m = d.getMinutes()>9?d.getMinutes():"0"+d.getMinutes();
			var t = h+":"+m;
			time[t] = (10-i);
			tarray[tarray.length] = t;
			var f = d.getTime()-60*1000
			d = new Date(f);
			$("#time"+(10-i)).text(h+":"+m);
		}
		$.ajax({
			url : base+"/app/detail/custom/show.do?method="+config.method+"&appName="+config.appName+"&keyName="+config.keyName+"&property="+config.property,
			async :false,
			success : function(data) {
				var tbody = "";
				var size = getJsonObjectSize(data);
				var countLine =0;
				for(key in data){
					var flag;
					if(data[key].length==0){
						continue;
					}
					countLine++;
					var tmp = {};
					for(var i=0;i<data[key].length;i++){
						var ft = data[key][i].ftime;
						tmp[ft] = {};
						for(var j=0;j<config.properties.length;j++){
							tmp[ft][config.properties[j]] = data[key][i][config.properties[j]];
						}
					}
					var tr1="<tr id='tr_"+key+"1'><td width='200'>"+key+"</td>";
					for(var i=10;i>=1;i--){
						var k = tarray[(i-1)]
						flag="down";
						if(i<3)flag="left";
						if(i>8)flag="right";
						if(tmp[k]){
							var contentView = "<table class=\"table table-striped table-bordered table-condensed \">";
							for(var count =0;count<config.properties.length;count++){
								var tr="<tr><td>"+config.properties[count]+"</td><td>"+tmp[k][config.properties[count]]+"</td></tr>";
								contentView+=tr;
							}
							contentView+="</table>";
							tr1+="<td style='text-align:center;'  id='"+key+"1_time"+i+"' title='"+k+"'><a class='"+flag+"' data-original-title='"+k+"' data-content='"+contentView+"'>"+tmp[k][config.property]+"</a></td>";
						}else{
							tr1+="<td style='text-align:center;' id='"+key+"1_time"+i+"' title='"+k+"'>0</td>";
						}
					}
					tr1+="</tr>";		
					tbody+=tr1;
				}
				$("#mapPoListTableBody").html(tbody);
				$(".top").popover({placement : "top"});
				$(".down").popover({placement : "bottom"});
				$(".left").popover({placement : "left"});
				$(".right").popover({placement : "right"});
				$(".top").popover("hide");
				$(".down").popover("hide");
				$(".left").popover("hide");
				$(".right").popover("hide");
			}
		});
		window.setTimeout(function(){mapPoListTable(config)},60000);
}
function isJsonEmpty(object) {
		for(var i in object) 
		{ return false; }
	return true;
}
function getJsonObjectSize(object){
	var i =0;
	for(var o in object){
		if(object[o].length!=0)i++;
	}
	return i;
}