
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="19" width="100%"><font color="#000000" size="2">[ HSF ]</font></td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td width="120" rowspan="2" align="center">类名</td>
	  <td width="80" rowspan="2" align="center">方法名</td>
	  <td colspan="4" align="center">执行次数</td>
	  <td colspan="4" align="center">平均执行时间(ms)</td>
	  <td colspan="4" align="center">Exception总数</td>
	  <td colspan="4" align="center">BizException总数</td>
  	</tr>
	<tr class="ui-widget-header ">
	  	<td width="220" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>
		<td width="100" align="center">基线</td>
		<td width="100" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>
		<td width="100" align="center">基线</td>
		<td width="100" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>
		<td width="100" align="center">基线</td>
		<td width="100" align="center">当前</td>
	  	<td width="100" align="center">同比</td>
		<td width="100" align="center">环比</td>	
		<td width="100" align="center">基线</td>		
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