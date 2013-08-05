<%@ page language="java" contentType="text/html; charset=GBK"
        pageEncoding="GBK"%>

<!-- iframe的中间结果页面，用于跨域的交互 -->

<script>
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

   if(QueryString('getPar')!=null){

     if(QueryString(QueryString('getPar'))!=null){
         parent.onResponse(QueryString(QueryString('getPar')));
      }else{
        alert("要获取的参数为空");
       }
     
   }else{
        alert("请指定获取的参数");
   }


</script>