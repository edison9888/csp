function analyseOneLine(line) {
	if(line){
		regjump = new RegExp("GET\\s+/jump\\?target\\=","g");
		regadd = new RegExp("GET\\s+/add","g");
		exp = new RegExp("\"-\" 400 0 \"-\" \"-\"");
		 var date = new Date();                          
	     var year = date.getFullYear();
	     var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+ 1) : (date.getMonth() + 1);
	     var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	     var hours = date.getHours();
	     var minutes = date.getMinutes();
	     var time = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
	     
		if(regjump.test(line)){
			putCountData(time, "OTHER_tbpass", "jump", 1);
			
		}
		if(regadd.test(line)){
			putCountData(time, "OTHER_tbpass", "add", 1);
		}
		if(exp.test(line)){
			putCountData(time, "OTHER_tbpass", "exception", 1);
		}
	}
}