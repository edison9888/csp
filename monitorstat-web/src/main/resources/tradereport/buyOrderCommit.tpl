<table width="100%">
<tr>
	<td valign="top" width="50%">
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td colspan="3" align="center"> ��ԴURLͳ�� </td>
			</tr>
			<tr class="ui-widget-header ">
				<td width="70%" align="center"> ��Դ URL</td>
				<td width="20%" align="center"> PV��</td >
				<td width="10%" align="center">	����</td>
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
				<td colspan="3" align="center">Ŀ��URLͳ��</td>	
			</tr>
			<tr class="ui-widget-header ">
				<td width="70%" align="center">Ŀ�� URL</td>
				<td width="20%" align="center">PV��</td >
				<td width="10%" align="center">����</td>
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
