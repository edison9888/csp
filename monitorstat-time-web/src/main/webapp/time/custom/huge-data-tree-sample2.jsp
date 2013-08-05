<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
    <title>Big Tree</title>
  
    <link href="css/tree.css" rel="stylesheet" type="text/css" />
    <link href="sample-css/page.css" rel="stylesheet" type="text/css" />
    
    <style type="text/css">
    body {
		color:#000; /* MAIN BODY TEXT COLOR */
		font-family:"Lucida Grande","Lucida Sans Unicode",Arial,Verdana,sans-serif; /* MAIN BODY FONTS */
		}
		.demo{
      float:left;
      width:260px;
    }
    .docs{
      margin-left: 265px;
    }
    a.button{
      font-size: 0.8em;
      margin-right: 4px;
    }
    </style>
    <meta charset="UTF-8" />
    <%@ include file="/time/common/base.jsp"%>
    <%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
   	<script src="src/jquery.js" type="text/javascript"></script>
    <script src="src/Plugins/jquery.tree.js" type="text/javascript"></script>
    <script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
</head>
<body>
  <div style="padding:10px;"> 
  <div >
		<select id="groupSelect"></select>
		<select id="appSelect"></select>
		<button id='getKey' type="button">²é¿´Key</button>		
		<script>
		function getKey(){
			$("#getKey").click(function(e){
				load();	
			});
		}
		$(document).ready(getKey);
		</script>
  </div>
  <div class="demo">
    <div style="margin-bottom:5px;">
        <a class="button" href="javascript:void(0);" id="showchecked">Get Selected Nodes</a>
    </div>
    <div style="border-bottom: #c3daf9 1px solid; border-left: #c3daf9 1px solid; width: 250px; height: 500px; overflow: auto; border-top: #c3daf9 1px solid; border-right: #c3daf9 1px solid;">
        <div id="tree">
        </div> 
    </div>
  </div>
  <div id="keyForm">
  	
  </div>
    <script type="text/javascript">
         var userAgent = window.navigator.userAgent.toLowerCase();
        $.browser.msie8 = $.browser.msie && /msie 8\.0/i.test(userAgent);
        $.browser.msie7 = $.browser.msie && /msie 7\.0/i.test(userAgent);
        $.browser.msie6 = !$.browser.msie8 && !$.browser.msie7 && $.browser.msie && /msie 6\.0/i.test(userAgent);
        function load() {        
            var o = { showcheck: true
            };    
            var appId = getSelectAppId();
  			var treedata;
  			$.ajax({
            	url : base+"/app/detail/custom/show.do?method=getKey&appId="+appId,
            	async : false,
            	success : function(data){
            		treedata = data.keyData;
            	}
            });
            o.data = treedata;
            $("#tree").treeview(o);            
            $("#showchecked").click(function(e){
                var keys=$("#tree").getCheckedNodes();
                if(keys !=null){
                	createForm(keys);	
                }
            });
        } 
        var url;
        var viewMod;
        var naviName;
        function createForm(keys){
        	var appId = getSelectAppId();
        	var content="<form name='form' action='"+base+"/app/detail/custom/show.do'>";
        	var keyString = keys.join(",");
        	for(var i=0;i<keys.length;i++){
        		content+=keys[i];
        		content+=":<input type='radio' checked='checked' name="+i+"_1 value='single'>single";
        		content+="<input type='radio'  name="+i+"_1 value='child'>child";
        		content+="<input type='radio' checked='checked' name="+i+"_2 value='recently'>recently";
        		content+="<input type='radio'  name="+i+"_2 value='cache'>cache";
        		content+="<input type='radio' checked='checked' name="+i+"_3 value='host'>host";
        		content+="<input type='radio'  name="+i+"_3 value='app'>app";
        		content+="<input type='radio' checked='checked' name="+i+"_4 value='table'>table";
        		content+="<input type='radio'  name="+i+"_4 value='chart'>chart<br>";
        		content+="<br>";
        	}
        	content+="naviName:<input type='text' name='naviName'>";
        	content+="property:<input type='text' name='mainProperty'>";
        	content+="potype:<input type='text' name='poType'>";
        	content+="<input type='hidden' name='keyString' value="+keyString+">";
        	content+="<input type='hidden' name='appId' value="+appId+">";
        	content+="<input type='hidden' name='method' value='addNavi'>";
        	content+="<input type='submit' value='submit'/>";
        	content+="</form>";
        	$("#keyForm").html(content);
        }
        function generateParams(){
        	
        }
        $(function() {
			var nw = new NavigateWidget({appId:"${appInfo.appId}"});
		});
    </script>
</body>
</html>