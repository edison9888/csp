
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="19" width="100%"><font color="#000000" size="2">[ HSF ]</font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td width="120" rowspan="2" align="center">����</td>
	  <td width="80" rowspan="2" align="center">������</td>
	  <td colspan="4" align="center">ִ�д���</td>
	  <td colspan="4" align="center">ƽ��ִ��ʱ��(ms)</td>
	  <td colspan="4" align="center">Exception����</td>
	  <td colspan="4" align="center">BizException����</td>
  	</tr>
	<tr class="ui-widget-header ">
	  	<td width="220" align="center">��ǰ</td>
	  	<td width="100" align="center">ͬ��</td>
		<td width="100" align="center">����</td>
		<td width="100" align="center">����</td>
		<td width="100" align="center">��ǰ</td>
	  	<td width="100" align="center">ͬ��</td>
		<td width="100" align="center">����</td>
		<td width="100" align="center">����</td>
		<td width="100" align="center">��ǰ</td>
	  	<td width="100" align="center">ͬ��</td>
		<td width="100" align="center">����</td>
		<td width="100" align="center">����</td>
		<td width="100" align="center">��ǰ</td>
	  	<td width="100" align="center">ͬ��</td>
		<td width="100" align="center">����</td>	
		<td width="100" align="center">����</td>		
	</tr>
	
		<#if (hsfout.records)??>
		<#list hsfout.records as hsf >
			<tr>
				<td >${(hsf.clz)!"-"}</td>
				<td >${(hsf.method)!"-"}</td>
				
				<td >${(hsf.callCount)!"-"}</td>
				<td >${(hsf.callCountTongbi)!"-"}</td>
				<td >${(hsf.callCountHuanbi)!"-"}</td>
				<td >${(hsf.callCountBaseline)!"-"}</td>
				
				<td >${(hsf.avgTime)!"-"}</td>
				<td >${(hsf.avgTimeTongbi)!"-"}</td>
				<td >${(hsf.avgTimeHuanbi)!"-"}</td>
				<td >${(hsf.avgTimeBaseline)!"-"}</td>
				
				<td >${(hsf.exCount)!"-"}</td>
				<td >${(hsf.exCountTongbi)!"-"}</td>
				<td >${(hsf.exCountHuanbi)!"-"}</td>
				<td >${(hsf.exCountBaseline)!"-"}</td>
				
				<td >${(hsf.bizExCount)!"-"}</td>
				<td >${(hsf.bizExCountTongbi)!"-"}</td>
				<td >${(hsf.bizExCountHuanbi)!"-"}</td>
				<td >${(hsf.bizExCountBaseline)!"-"}</td>
			</tr>
		</#list>
		</#if>				
</table>