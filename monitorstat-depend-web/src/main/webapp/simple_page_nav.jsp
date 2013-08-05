<%@ page contentType="text/html; charset=GB18030"%>
<script>
var base="<%=request.getContextPath()%>";
</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css" rel="stylesheet" />
<div width="100%">

<select id="companySelect"  style="width:140px;"></select>
<select id="groupSelect"  style="width:140px;" ></select>
<select id="appSelect" name="opsName"  style="width:120px;"></select>

&nbsp;&nbsp;<input type="submit" value="查询"  style="width:60px;" onClick="goto1();" 
class="btn btn-success">

&nbsp;&nbsp;<input type="text" id="directGo"  style="width:100px;height:25px;">&nbsp;&nbsp;
<input type="submit" value="应用直达"  style="width:80px;" onClick="goto1();"  class="btn btn-success">

</div>        
<script>
var nw = new NavigateWidget2({appName:QueryString("appName")});

function goto1(){
 var redirect_url=QueryString('redirecturl');
 var getPar=QueryString('getPar');
 var getParValue="";
 if(document.getElementById("directGo").value.trim()!=""){
    getParValue=document.getElementById("directGo").value.trim();

 }else{
        getParValue=document.getElementsByName(getPar)[0].value;

 }
 window.location.href=redirect_url+"?getPar="+getPar+"&"+getPar+"="+getParValue;

}

//获取参数
function QueryString(fieldName)
{ 
  var urlString = document.location.search;
  if(urlString != null)
  {
       var typeQu = fieldName+"=";
       var urlEnd = urlString.indexOf(typeQu);
       if(urlEnd != -1)
       {
            var paramsUrl = urlString.substring(urlEnd+typeQu.length);
            var isEnd =  paramsUrl.indexOf('&');
            if(isEnd != -1)
            {
                 return paramsUrl.substring(0, isEnd);
            }
            else
            {
             return paramsUrl;
            }
       }
       else
       {
            return null;
       }
  }
 else
 {
    return null;
 }
}
</script>