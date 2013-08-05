function analyseOneLine(line) {
   if(line.indexOf("Login_ss_open")>-1){
	     var date = new Date();                          
	     var year = date.getFullYear();
	     var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+ 1) : (date.getMonth() + 1);
	     var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	     var hours = date.getHours();
	     var minutes = date.getMinutes();
	     var time = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
	   putAverageData(time, "Login_ss", "open", 1);
   }
}
function doAnalyse(){	     
}