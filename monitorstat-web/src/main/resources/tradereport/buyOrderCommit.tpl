<table width="100%">
<tr>
	<td valign="top" width="50%">
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td colspan="3" align="center"> 来源URL统计 </td>
			</tr>
			<tr class="ui-widget-header ">
				<td width="70%" align="center"> 来源 URL</td>
				<td width="20%" align="center"> PV量</td >
				<td width="10%" align="center">	比例</td>
			</tr>
			<tr >
				<td align="left">${(orderCommit.fromUrl)!"-"}</td>
				<td align="left">${(orderCommit.fromPv)!"-"}</td>
				<td align="left">${(orderCommit.fromPercent)!"-"}</td>
			</tr>
		</table>
	</td>
	<td valign="top" width="50%">
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td colspan="3" align="center">目的URL统计</td>	
			</tr>
			<tr class="ui-widget-header ">
				<td width="70%" align="center">目的 URL</td>
				<td width="20%" align="center">PV量</td >
				<td width="10%" align="center">比例</td>
			</tr> 
			<tr>
				<td align="left">${(orderCommit.destUrl)!"-"}</td>
				<td align="left">${(orderCommit.destPv)!"-"}</td>
				<td align="left">${(orderCommit.destPercent)!"-"}</td>
			</tr>
		</table>
	</td>
</tr>
</table>
