function analyseOneLine(line) {
	
	
	 var date = new Date();                          
     var year = date.getFullYear();
     var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+ 1) : (date.getMonth() + 1);
     var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
     var hours = date.getHours();
     var minutes = date.getMinutes();
     var time = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
	
   if(line.indexOf("memory")>-1){
	   var v = line.split(" ");
	   if(v.length == 3){
		   var value = v[2];
		   putAverageData(time, "Session-tair", "memory", value/1048576);
	   }
   }else if(line.indexOf("itemCount")>-1){
	   var v = line.split(" ");
	   if(v.length == 3){
		   var value = v[2];
		   putAverageData(time, "Session-tair", "itemCount", value);
	   }
   }else if(line.indexOf("getCount")>-1){
	   var v = line.split(" ");
	   if(v.length == 3){
		   var value = v[2];
		   putAverageData(time, "Session-tair", "getCount", value);
	   }
   }else if(line.indexOf("hitCount")>-1){
	   var v = line.split(" ");
	   if(v.length == 3){
		   var value = v[2];
		   putAverageData(time, "Session-tair", "hitCount", value);
	   }
   }else if(line.indexOf("putCount")>-1){
	   var v = line.split(" ");
	   if(v.length == 3){
		   var value = v[2];
		   putAverageData(time, "Session-tair", "putCount", value);
	   }
   }else if(line.indexOf("removeCount")>-1){
	   var v = line.split(" ");
	   if(v.length == 3){
		   var value = v[2];
		   putAverageData(time, "Session-tair", "removeCount", value);
	   }
   }
   
}
function doAnalyse(){	     
}