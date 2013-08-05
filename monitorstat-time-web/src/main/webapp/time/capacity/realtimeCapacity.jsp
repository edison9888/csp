<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>CSP实时容量</title>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
</head>
<body>

<table class="table table-striped table-bordered">
	<thead id="head_part">
      
    </thead>
    
	<tbody id="body_part" >
		
	</tbody>
</table>


<script type="text/javascript">

function queryJob(){
	$.getJSON("../../app/capacity.do?method=showRealTimeCapacity", {}, function(json){
		$("#head_part").empty();
		var head_content = "<tr><td>应用名</td>";
		for(time in json){
			head_content = head_content +"<td>"+time+"</td>";
		}
		head_content += "</tr>"
		$("#head_part").append(head_content);
		var values = new Array();
		var filled_appname=false;
		
		$("#body_part").empty();
		var col=0;
        for(time in json){
             col++;
             var app_level = json[time];
             var i = 0;
             for (app in app_level) {
                   if (!filled_appname) {
                         values[i] = "<tr><td>"+app+"</td>"
                    }

                    var level = app_level[app];
                          if (col==3) {
                                values[i] += "<td style='color:red'>"+level+"</td>";
                          } else {
                               values[i] += "<td>"+level+"</td>";
                           }
                           i++;
             	}
                filled_appname = true;
         }
		for(j=0;j<values.length;j++) {
			values[j] += "</tr>";
		}
		
		for(j=0;j<values.length;j++) {
			$("#body_part").append(values[j]);
		}
		

	}); 
	window.setTimeout("queryJob()",60000); 	
}

queryJob();

</script>
</body>
</html>