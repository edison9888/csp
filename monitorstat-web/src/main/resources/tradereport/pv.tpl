<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td  colspan="9" class="headcon"><font color="#000000" size="2"> ${(pvs.appName)!"-"}</font></td>
	</tr>
	<tr class="ui-widget-header ">
	  <td rowspan="2" align="center">����</td>
	  <td colspan="4" align="center">ִ������</td>
	  <td colspan="4" align="center">ƽ��ִ��ʱ��</td>
	</tr>
	<tr class="ui-widget-header ">
		<td align="center" >��ǰ</td>
		<td align="center">ͬ��</td>
		<td align="center">����</td>
		<td align="center">����</td>

		<td align="center">��ǰ</td>
		<td align="center">ͬ��</td>
		<td align="center">����</td>
		<td align="center">����</td>
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