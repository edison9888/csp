<%@ page language="java" contentType="text/html; charset=GBK"
        pageEncoding="GBK"%>

<!-- iframe���м���ҳ�棬���ڿ���Ľ��� -->

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
        alert("Ҫ��ȡ�Ĳ���Ϊ��");
       }
     
   }else{
        alert("��ָ����ȡ�Ĳ���");
   }


</script>