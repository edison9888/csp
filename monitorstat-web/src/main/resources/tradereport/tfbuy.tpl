<html>
<head><meta http-equiv="Content-Type" content="text/html; charset=GBK">
<#include "cssStyle.tpl">
</head>
<body>
<table>

<tr><td><h2>系统参数统计</h2></tr><tr>
<tr><td><#include "performance.tpl" ></td></tr>

<tr><td><h2>外部调用接口统计</h2></tr>
<tr><td><#include "hsfout.tpl"></td></tr>

<tr><td><h2>PV统计</h2></td></tr>
<tr><td><#include "pv.tpl"></td></tr>

<tr><td><h2>下单来源统计</h2></td></tr>
<tr><td><#include "buyOrderCommit.tpl"></td></tr>
</table>
</body>
</html>
