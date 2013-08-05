
function insertDataToDb(context){
	
}

function putCountData(time,title,key,value){	
	scriptContain.putCountData(time,title,key,value);
}
	
function putAverageData(time,title,key,value){
	scriptContain.putAverageData(time,title,key,value);
}
	
function putTextData(time,title,key,value){
	scriptContain.putTextData(time,title,key,value);
}


function getCountKeyValue(){
	return scriptContain.getCountKeyValue();
}

function getAverageKeyValue(){
	return scriptContain.getAverageKeyValue();
}

function getTextKeyValue(){
	return scriptContain.getTextKeyValue();
}

function log(message){
	scriptContain.log(message);
}
