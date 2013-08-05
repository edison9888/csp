function analyseOneLine(line) {
	if(line){
		 var lineToLowerCase = line.toLowerCase(); 
		 if(line == "error"){
			 var date = new Date();                          
		     var year = date.getFullYear();
		     var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+ 1) : (date.getMonth() + 1);
		     var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
		     var hours = date.getHours();
		     var minutes = date.getMinutes();
		     var time = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
		     putCountData(time, "AppStatus", "应用不可访问", 1);
		 }
	}
}


function doAnalyse(){
	
}