<table width="100%" >
	<tr class="headcon " border="1"> 
		<td colspan="10" align="left"><font color="#000000" size="2">性能数据</font></td>
	</tr>
	<tr><td>
			<table width="50%" border="1" class="ui-widget ui-widget-content">
			
			  <tr class="ui-widget-header "> 	    
			    <td width="100" align="center">系统数据</td> 	    
			    <td width="90" align="center">当前</td> 
			    <td width="70" align="center">同比</td> 
			    <td width="70" align="center">环比</td> 
			    <td width="70" align="center">基线</td> 
			  </tr> 
			  <tr> 
			    <td>CPU</td> 
			    <td> ${(performance.cpu)!"-"}</td> 
			    <td> ${(performance.tongbiCpu)!"-"}</td> 
			    <td> ${(performance.huanbiCpu)!"-"}</td> 
			    <td> ${(performance.baselineCpu)!"-"}</td> 
			  </tr> 
			  <tr> 
			    <td>IOWAIT</td> 
			    <td> ${(performance.iowait)!"-"}</td> 
			    <td> ${(performance.tongbiIowait)!"-"}</td> 
			    <td> ${(performance.huanbiIowait)!"-"}</td> 
			    <td> ${(performance.baselineIowait)!"-"}</td> 
			  </tr> 
			  <tr> 
			    <td>Load</td> 
			    <td> ${(performance.load)!"-"}</td> 
			    <td> ${(performance.tongbiLoad)!"-"}</td> 
			    <td> ${(performance.huanbiLoad)!"-"}</td> 
			    <td> ${(performance.baselineLoad)!"-"}</td> 
			  </tr> 
			  <tr> 
			    <td>Mem</td> 
			    <td> ${(performance.mem)!"-"}</td> 
			    <td> ${(performance.tongbiMem)!"-"}</td> 
			    <td> ${(performance.huanbiMem)!"-"}</td> 
			    <td> ${(performance.baselineMem)!"-"}</td> 
			  </tr> 
			  <tr> 
			    <td>Swap</td> 
			    <td> ${(performance.swap)!"-"}</td> 
			    <td> ${(performance.tongbiSwap)!"-"}</td> 
			    <td> ${(performance.huanbiSwap)!"-"}</td> 
			    <td> ${(performance.baselineSwap)!"-"}</td> 
			  </tr> 
			</table> 
		</td> 
		
		<td valign="top"> 
			<table width="50%" border="1" class="ui-widget ui-widget-content"> 
				<tr class="ui-widget-header "> 
				  <td width="100" align="center">访问信息</td> 
				  <td width="90" align="center">当前</td> 
				  <td width="70" align="center">同比</td> 
				  <td width="70" align="center">环比</td> 
				  <td width="70" align="center">基线</td> 
				</tr> 
				
			    <tr> 
			      <td>PV(哈勃)</td> 
			      <td> ${(performance.pv)!"-"}</td> 
			      <td> ${(performance.tongbiPv)!"-"}</td> 
			      <td> ${(performance.huanbiPv)!"-"}</td> 
			      <td> ${(performance.baselinePv)!"-"}</td> 
			    </tr> 
			    <tr> 
			      <td>Qps(哈勃)</td> 
			      <td> ${(performance.qps)!"-"}</td> 
			      <td> ${(performance.tongbiQps)!"-"}</td> 
			      <td> ${(performance.huanbiQps)!"-"}</td> 
			      <td> ${(performance.baselineQps)!"-"}</td> 
			    </tr> 
			    <tr> 
			      <td>Rt(哈勃)</td> 
			      <td> ${(performance.rt)!"-"}</td> 
			      <td> ${(performance.tongbiRt)!"-"}</td> 
			      <td> ${(performance.huanbiRt)!"-"}</td> 
			      <td> ${(performance.baselineRt)!"-"}</td> 
			    </tr> 
			    <tr> 
			      <td>PV(CSP)</td> 
			      <td> ${(performance.cspPv)!"-"}</td> 
			      <td> ${(performance.tongbiCspPv)!"-"}</td> 
			      <td> ${(performance.huanbiCspPv)!"-"}</td> 
			      <td> ${(performance.baselineCspPv)!"-"}</td> 
			    </tr> 
			    <tr> 
			      <td>Qps(CSP)</td> 
			      <td> ${(performance.cspQps)!"-"}</td> 
			      <td> ${(performance.tongbiCspQps)!"-"}</td> 
			      <td> ${(performance.huanbiCspQps)!"-"}</td> 
			      <td> ${(performance.baselineCspQps)!"-"}</td> 
			    </tr> 
			    <tr> 
			      <td>Rt(CSP)</td> 
			      <td> ${(performance.cspRt)!"-"}</td> 
			      <td> ${(performance.tongbiCspRt)!"-"}</td> 
			      <td> ${(performance.huanbiCspRt)!"-"}</td> 
			      <td> ${(performance.baselineCspRt)!"-"}</td> 
			    </tr> 
		  	</table>
	  	</td> 
	</tr>
</table>