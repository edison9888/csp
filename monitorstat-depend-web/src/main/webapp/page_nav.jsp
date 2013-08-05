<%@ page contentType="text/html; charset=GB18030"%>
<script>
var base="<%=request.getContextPath()%>";
</script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<select id="companySelect"></select>
<select id="groupSelect"></select>
<select id="appSelect" name="opsName"></select>

&nbsp;&nbsp;&nbsp;
ÈÕÆÚ: <input type="text" id="selectDate" value="${param.selectDate }" name="selectDate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="span2"/>
							&nbsp;&nbsp;<input type="submit" value="²éÑ¯" class="btn btn-success">
<input type="hidden" id="queryHiddenField" name='queryHiddenField' value='${param.appName}'/>
<script>
var nw = new NavigateWidget2({appName:'${param.appName}'});
</script>