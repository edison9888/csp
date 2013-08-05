<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测进度监控"); %>
<jsp:include page="./header.jsp" />
<script type="text/javascript">
    var xmlhttp; //全局使用 
    var data;
    /**
	       *  创建一个XmlHttp对象，并且返回
	       *  作者：wb-tangjinge
	       *  时间：2011-11-29
	       */
    function createXmlHttp() {
        //其他浏览器创建XMLHttpRequest对象的代码  
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
            //避免Mozilla早些版本会有的bug  
            if (xmlhttp.overrideMimeType) {
                xmlhttp.overrideMimeType("text/xml");
            }
        }
        //IE6 IE5 IE5.5创建XMLHttpRequest对象的代码  
        else if (window.ActiveXObject) {
            var activexName = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];
            for (var i = 0; i < activexName.length; i++) {
                try {
                    xmlhttp = new ActiveXObject(activexName[i]);
                    break;
                } catch(ex) {}
            }
        }
        return xmlhttp;
    }
    /**  
	      *  功能：监控压测
	      *  作者：wb-tangjinge
	      *  时间：2011-11-29
	      */

    function getAppPressMessage() {
        var url = "${baseUrl}/trackingPressInfo.do";
        var partxmlhttp = createXmlHttp();
        partxmlhttp.open("POST", url, false);
        partxmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        partxmlhttp.send(null);
        var result;
        if (partxmlhttp.readyState == 4) {
            if (partxmlhttp.status == 200) {
                var returnStr = partxmlhttp.responseText;
                if (returnStr != null && returnStr != '') {
                    if (returnStr == "1") {
                        alert("终止指令发送成功！");
                        window.close();
                    } else if (returnStr == "0") {
                        document.getElementById("resultinfo").innerHTML = "正在压测,请等待...";
                    } else if (returnStr == "2") {
                        document.getElementById("resultinfo").innerHTML = "正在获取压测机器负载信息,请等待...";
                    } else if (returnStr == "3") {
                        document.getElementById("resultinfo").innerHTML = "负载率大于10%，压测终止...";
                    } else if (returnStr == "4") {
                        document.getElementById("resultinfo").innerHTML = "正在线上进行压测,请等待...";
                    } else if (returnStr == "5") {
                        document.getElementById("resultinfo").innerHTML = "正在获取准备压测的日志文件,请等待...";
                    } else if (returnStr == "6") {
                        document.getElementById("resultinfo").innerHTML = "正在获取压测日志信息,请等待...";
                    } else if (returnStr == "7") {
                        document.getElementById("resultinfo").innerHTML = "正在分析压测日志信息,请等待...";
                    } else if (returnStr == "8") {
                        document.getElementById("resultinfo").innerHTML = "正在保存压测结果信息,请等待...";
                    } else if (returnStr == "9") {
                        document.getElementById("resultinfo").innerHTML = "压测完成！";
                        clearInterval(data);
                        alert("压测完成");
                        window.close();
                    }
                }
            }
        }
    }

    /**
		  *  功能：执行压测
	      *  作者：wb-tangjinge
	      *  时间：2011-11-29
	      */

    function getOneResutl() {
        var kinds = document.getElementById("kinds").value;
        var appId = document.getElementById("appId").value;
        var exeTime = document.getElementById("exeTime").value;
        var reqTotle = document.getElementById("reqTotle").value;
        var url = "${baseUrl}/getPressureInformation.do?appId="+appId+"&pretime="+exeTime+"&other="+reqTotle+"&flag="+kinds;
        var partxmlhttp = createXmlHttp();
        partxmlhttp.open("POST", url, true);
        partxmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        partxmlhttp.send();
        var result;
        if (partxmlhttp.readyState == 4) {
            if (partxmlhttp.status == 200) {
                var returnStr = partxmlhttp.responseText;
                if (returnStr != null && returnStr != '') {}
            }
        }
    }
    /**
		  *  功能：停止压测
	      *  作者：wb-tangjinge
	      *  时间：2011-11-29
	      */

    function getStopPressure() {
        var url = "${baseUrl}/stopPressInfo.do";
        var partxmlhttp = createXmlHttp();
        partxmlhttp.open("POST", url, true);
        partxmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        partxmlhttp.send();
        var result;
        if (partxmlhttp.readyState == 4) {
            if (partxmlhttp.status == 200) {
                var returnStr = partxmlhttp.responseText;
                if (returnStr != null && returnStr != '') {}
            }
        }
    }

    function closeCurWindows() {
        if (confirm('关闭窗口,你确定吗？')) {
            getStopPressure();
            window.close();
        }
    }
</script>

<div class="container">
	<div id="bd" class="resource">
		<div style="width: 990px; height: auto; margin: 0 auto">
			<table align="center" width="200">
				<tr>
					<td align="center">
					    <br />
						<br />
						压测应用：${appname}
						<input id="kinds" type="hidden" value="${flag}">
						<input id="reqTotle" type="hidden" value="${other}">
						<input id="exeTime" type="hidden" value="${pretime}">
						<input id="appId" type="hidden" value="${appId}">
						<br />
						<br />
						<br />
						<br />
						<marquee scrollamount =7 behavior=scroll width=300  direction=left height=60 onmouseover="this.stop();"  onmouseout="this.start();"  behavior = alternate style="overflow: hidden;">
						<span id="resultinfo">正在压测,请等待...</span>
						</marquee>
						<br />
						<br />
						<br />
						<br />
						<br />
						<button type="button" class="btn primary"
							onclick="closeCurWindows();">
							关闭窗口
						</button>
					</td>
				</tr>
			</table>
			<script type="text/javascript">
              setTimeout("getOneResutl()",2000);
            </script>
            <script type="text/javascript">  
              data = window.setInterval("getAppPressMessage()",2000) ;  
           </script>
		</div>
	</div>
</div>
<jsp:include page="./footer.jsp"></jsp:include>

