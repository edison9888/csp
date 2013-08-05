$(function(){
	returnBtnFun();
	
	
	
});

function returnBtnFun(){
	$("#returnBtn").click(function(){
		window.location.href=base+"/config/hostconf/list.jsp";	
	});
}