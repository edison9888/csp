/**
 * @author zhanfei.tm
 * @date 2011-05-10
 * @description ��ȡ����ҳ���е�iframe��ʵ��iframe���Զ�������
 * ��ȡ����ҳ���е�iframe���Զ�����������һ���ر����iframe�Ĵ�С
 */
function iframeAuto(){
	try{
		if(window!=parent){
			//��λ��Ҫ������frame��ܣ��ڸ��������в��ң�
			var a = parent.document.getElementsByTagName("IFRAME");
			for(var i=0; i<a.length; i++){
				if(a[i].contentWindow==window){
					var h1=0, h2=0;
					a[i].parentNode.style.height = a[i].offsetHeight +"px";
					a[i].style.height = "10px";
					if(document.documentElement&&document.documentElement.scrollHeight){
						h1=document.documentElement.scrollHeight;
					}
					if(document.body) h2=document.body.scrollHeight;
					var h=Math.max(h1, h2);              
					if(document.all) {h += 4;}
					if(window.opera) {h += 1;}
					//������ܵĴ�С
					a[i].style.height = a[i].parentNode.style.height = h +"px";
				} 
		   } 
	    }
			
	}catch (ex){}
}
//�¼��󶨵ķ�����֧��IE5���ϰ汾
if(window.attachEvent){
	window.attachEvent("onload", iframeAuto);
}else if(window.addEventListener){
	window.addEventListener('load', iframeAuto, false);
}
