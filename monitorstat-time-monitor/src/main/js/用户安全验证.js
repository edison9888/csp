function analyseOneLine(line) {
   if(line.indexOf("�û���ȫǩ����ƥ��")>-1){
	  var time = line.substring(0,16);
	  putCountData(time, "OTHER_��־�ļ�","�û���ȫǩ����ƥ��",1);
   }
}
function doAnalyse(){	     
}