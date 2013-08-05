/**
 * @author zhanfei.tm
 * @date 2011-05-10
 * @description 获取父级页面中的iframe、实现iframe的自动调整。
 * 获取父级页面中的iframe，自动调整即窗体一加载便调整iframe的大小
 */
function iframeAuto(){
	try{
		if(window!=parent){
			//定位需要调整的frame框架（在父级窗口中查找）
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
					//调整框架的大小
					a[i].style.height = a[i].parentNode.style.height = h +"px";
				} 
		   } 
	    }
			
	}catch (ex){}
}
//事件绑定的方法，支持IE5以上版本
if(window.attachEvent){
	window.attachEvent("onload", iframeAuto);
}else if(window.addEventListener){
	window.addEventListener('load', iframeAuto, false);
}
