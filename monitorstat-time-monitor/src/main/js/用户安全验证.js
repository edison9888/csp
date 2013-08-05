function analyseOneLine(line) {
   if(line.indexOf("用户安全签名不匹配")>-1){
	  var time = line.substring(0,16);
	  putCountData(time, "OTHER_日志文件","用户安全签名不匹配",1);
   }
}
function doAnalyse(){	     
}