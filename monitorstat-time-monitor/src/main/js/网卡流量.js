Receive_valueMap1 = {};
Transmit_valueMap1 = {};
Receive_valueMap2 = {};
Transmit_valueMap2 = {};
function analyseOneLine(line) {
    var lineToLowerCase = line.toLowerCase(); 
    if (lineToLowerCase.indexOf(":") > -1) {
    	line = line.replace(/(^\s*)|(\s*$)/g, "");
    	line=line.replace(/\s{2,}/g," ");
    	var tmp = line.split(":");    	
    	var name = tmp[0];
    	var value = tmp[1]; 
    	value = value.replace(/(^\s*)|(\s*$)/g, "");
    	var b = value.split(" ");
    	if(Receive_valueMap1[name]){
    		Receive_valueMap2[name]=b[0];
        	Transmit_valueMap2[name]=b[8];
    	}else{
    		Receive_valueMap1[name]=b[0];
        	Transmit_valueMap1[name]=b[8];
    	}
    }
}


function doAnalyse(){
	
	 var date = new Date();                          
     var year = date.getFullYear();
     var month = (date.getMonth() + 1) < 10 ? "0" + (date.getMonth()+ 1) : (date.getMonth() + 1);
     var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
     var hours = date.getHours();
     var minutes = date.getMinutes();
     var time = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
     for(name in Receive_valueMap1){
    	 var value1 = Receive_valueMap1[name];
    	 var value2 = Receive_valueMap2[name];
    	 var data = (value2-value1)/2;
    	 var num = parseInt(data);
    	 if(!isNaN(num)){
    		 putAverageData(time, "System_网卡", name, data);
    	 }else{
    		 log(name+"网卡流量获取异常-value1:"+value1+" value2:"+value2);
    	 }
    	
     }
     
}