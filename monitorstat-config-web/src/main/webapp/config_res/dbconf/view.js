$(function(){
	returnBtnFun();
	
	
	
});

function returnBtnFun(){
	$("#returnBtn").click(function(){
		window.location.href=base+"/config/dbconf/list.jsp";	
	});
}