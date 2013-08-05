function analyseOneLine(line) {
    var lineToLowerCase = line.toLowerCase(); 
    line = line.replace(/(^\s*)|(\s*$)/g, "");
	line=line.replace(/\s{2,}/g," ");
	var tmp = line.split(" ");    	
	if(tmp.length==6){
		 var date = new Date();                          
	     var year = date.getFullYear();
	     var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+ 1) : (date.getMonth() + 1);
	     var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	     var hours = date.getHours();
	     var minutes = date.getMinutes();
	     var time = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
	     var d = tmp[4].replace("%","");
		 putAverageData(time, "System_磁盘空间使用率", tmp[5], d);
	}else{
		log(line)
	}
}


function doAnalyse(){
	
	
     
}