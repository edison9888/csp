$(function() {
	var auto = function (queryValue) {
		try{
			/** 文本框自动填充 */
			if($("#keyword").length != 0) {		//公共查询，自动填充功能
		        $(document).ready(function() {
		        	/**
		        	 * http://shell.loopj.com/tokeninput/tvshows.php
		        	 * 查询时动态生成的url
		        	 * http://shell.loopj.com/tokeninput/tvshows.php?callback=jQuery1710713294817651886_1344397385677&q=j&_=1344397692103
		        	 */
		        	//var queryUrl = "http://shell.loopj.com/tokeninput/tvshows.php";
		        	var queryUrl = base + "/tagopt.do?method=tagAutoCompleted";
		            $("#keyword").tokenInput(queryUrl, {
		                theme: "facebook"
		            });
		        });				
			}
				/*
				$("#keyword").autocomplete({
							source: base + "/tagopt.do?method=tagAutoCompleted&keyword=" + queryValue,
							minChars: 0,
							max: 5,
							autoFill: true,
							mustMatch: true,
							matchContains: true,
							scrollHeight: 220,
							formatItem: function(data, i, total) {
								return "<I>"+data[0]+"</I>";
							},
							formatMatch: function(data, i, total) {
								return data[0];
							},
							formatResult: function(data) {
								return data[0];
							},							
							select: function( event, ui ) {
							}
				});			
			}
			*/
			/**
			if($("#pkeyword")) {		//个人主页查询
				$("#pkeyword").autocomplete({
							source: base + "/tagopt.do?method=tagAutoCompleted&pkeyword="+appId2,
							minLength: 2,
							select: function( event, ui ) {
							}
					});			
			}	*/	
		}catch(exception){
			alert(exception);
		}
	};
	auto('');
})