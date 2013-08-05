$(document).ready(function(){
	var dataString ='<chart manageResize="1" origW="450" origH="250" caption="Taobao用户pv和购买链路转化监控图" baseFontSize="14" showLegend="1" legendPosition="Right" showLabels="1" showBorder="0" decmials="2" numberPrefix="%" showPercentageValues="1" formatNumberScale="0">\n\
		<set value="102494" label="Pay" color="ec7c10" />\n\
		<set value="168982" label="Tradeplatform" color="f8ca3f" />\n\
		<set value="182731" label="Buy" color="f5eeb7"/>\n\
		<set value="223461" label="Cart/Buy" color="FFFF00" />\n\
		<set value="534532" label="Detail" color="79c833" isSliced="1"/>\n\
		<set value="84532" label="Login" color="007555" />\n\
	</chart>';
	var chart = new FusionCharts("flash/Pyramid.swf", "ChartId", "600", "468", "0", "1" );
	chart.setXMLData( dataString );
	chart.render("chartdiv");
	var tmallDataString ='<chart manageResize="1" origW="450" origH="250" caption="Tmall用户pv和购买链路转化监控图" showLegend="1" legendPosition="Right" showLabels="1" showBorder="0" decmials="2" numberPrefix="%" showPercentageValues="1" formatNumberScale="0">\n\
	   <set value="60494" label="Pay" color="980101" />\n\
	   <set value="88982" label="Tradeplatform" color="ff8f45" />\n\
	   <set value="82731" label="Tmall-Buy" color="ffb384" />\n\
	   <set value="63461" label="Tmall-Cart" color="f8ca3f" />\n\
	   <set value="234532" label="Tmall-Detail" color="e5e519" isSliced="1"/>\n\
	   <set value="84532" label="Login" color="5ebcff" />\n\
	</chart>';
	var tmall = new FusionCharts("flash/Pyramid.swf", "tmallChartId", "600", "468", "0", "1" );
	tmall.setXMLData( tmallDataString );
	tmall.render("tmallchartdiv");
	
	var taobaoDataString ='<chart manageResize="1" caption="Taobao用户pv和购买链路转化监控图" baseFontSize="14" decimals="1" showPercentageValues="1" isHollow="0" \n\
				formatNumberScale="1" numberPrefix="%">\n\
	   <set value="102494" label="Pay" color="ec7c10" />\n\
	   <set value="168982" label="Tradeplatform" color="f8ca3f" isSliced="1"/>\n\
	   <set value="223461" label="Buy" color="5ebcff"/>\n\
	   <set value="182731" label="Cart" color="FFFF00" />\n\
	   <set value="1296732" label="Detail" color="79c833" />\n\
	   <set value="84532" label="Login" color="007555" />\n\
	   <set value="2058932" label="PV" color="005233" />\n\
	</chart>';
	var taobaoFunnelChart = new FusionCharts("flash/Funnel.swf", "taobaoChartId", "594", "464", "0", "1" );
	taobaoFunnelChart.setXMLData( taobaoDataString );
	taobaoFunnelChart.render("taobaoFunnelChartDiv");
	
	var tmallDataString ='<chart manageResize="1" caption="Tmall用户pv和购买链路转化监控图" color="000000" baseFontSize="14" decimals="1" showPercentageValues="1" isSliced="1" isHollow="0" \n\
			formatNumberScale="1" numberPrefix="%">\n\
	   <set value="60494" label="Pay" color="980101" />\n\
	   <set value="88982" label="Tradeplatform" color="ff8f45" />\n\
	   <set value="82731" label="天猫Tmall-Buy" color="ffb384" />\n\
	   <set value="63461" label="Tmall-Cart" color="f8ca3f" />\n\
	   <set value="234532" label="Tmall-Detail" color="e5e519"/>\n\
	   <set value="84532" label="Login" color="5ebcff" />\n\
	</chart>';
	var tmallFunnelChart = new FusionCharts("flash/Funnel.swf", "tmallFunnelChartId", "594", "464", "0", "1" );
	tmallFunnelChart.setXMLData( tmallDataString );
	tmallFunnelChart.render("tmallFunnelChartDiv");
});