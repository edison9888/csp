<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<script type="text/javascript"	src="<%=request.getContextPath()%>/time/custom/map_po_list/table.js"></script>

<script>
	$(document).ready(function(){
		var method = "${method}";
		var appName = "${appName}";
		var keyName = "${keyName}";
		var property = "${property}";
		var propertiesRaw = "${properties}";
		var properties = propertiesRaw.split(",");
		mapPoListTable({
			method : method,
			appName : appName,
			keyName : keyName,
			property : property,
			properties : properties
		});
	});	
</script>
	<table class="table table-striped table-bordered table-condensed"
		id="mapPoListTable">
		<caption>
			<strong>${appName } ${keyName }</strong>
		</caption>
		<thead>
			<tr>
				<td width="200" style="text-align: center;">KEY</td>
				<td style="text-align: center;" id="time1">10:21</td>
				<td style="text-align: center;" id="time2">10:22</td>
				<td style="text-align: center;" id="time3">10:23</td>
				<td style="text-align: center;" id="time4">10:24</td>
				<td style="text-align: center;" id="time5">10:25</td>
				<td style="text-align: center;" id="time6">10:26</td>
				<td style="text-align: center;" id="time7">10:27</td>
				<td style="text-align: center;" id="time8">10:28</td>
				<td style="text-align: center;" id="time9">10:29</td>
				<td style="text-align: center;" id="time10">10:30</td>
			</tr>
		</thead>
		<tbody id="mapPoListTableBody">
		</tbody>
	</table>