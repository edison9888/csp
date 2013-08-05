def doAnalyse
end
def insertDataToDb(context)
end

def putCountData(time,title,key,value)
	print(time+":"+title+":"+key+":"+value);
	scriptContain.putCountData(time,title,key,value);
end
	
def putAverageData(time,title,key,value)
	print(time+":"+title+":"+key+":"+value);
	scriptContain.putCountData(time,title,key,value);
end
	
def putTextData(time,title,key,value)
	print(time+":"+title+":"+key+":"+value);
	scriptContain.putCountData(time,title,key,value);
end


def getCountKeyValue
	return scriptContain.getCountKeyValue();
end

def getAverageKeyValue
	return scriptContain.getAverageKeyValue();
end

def getTextKeyValue
	return scriptContain.getTextKeyValue();
end
