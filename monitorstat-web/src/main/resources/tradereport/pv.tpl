<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td  colspan="9" class="headcon"><font color="#000000" size="2"> ${(pvs.appName)!"-"}</font></td>
	</tr>
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center">类型</td>
	  <td colspan="4" align="center">执行数次</td>
	  <td colspan="4" align="center">平均执行时间</td>
	</tr>
	<tr class="ui-widget-header ">
		<td align="center" >当前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
		<td align="center">基线</td>

		<td align="center">当前</td>
		<td align="center">同比</td>
		<td align="center">环比</td>
		<td align="center">基线</td>
	</tr>
	<#if (pvs.records)??>
	<#list pvs.records as pv >
		<tr>
			<td >${(pv.type)!"-"}</td>
	
			<td >${(pv.count)!"-"}</td>
			<td >${(pv.tongbi)!"-"}</td>
			<td >${(pv.huanbi)!"-"}</td>
			<td >${(pv.baseline)!"-"}</td>
	
			<td >${(pv.time)!"-"}</td>
			<td >${(pv.timeTongbi)!"-"}</td>
			<td >${(pv.timeHuanbi)!"-"}</td>
			<td >${(pv.timeBaseline)!"-"}</td>
		</tr>
	</#list>
	</#if>
</table>