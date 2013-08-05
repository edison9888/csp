<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测机器"); %>
<jsp:include page="./header.jsp" />
<div class="container">
	<div id="bd" class="resource">
		<div class="alert-message block-message success">
			<form action="${baseUrl}/getAllAppMachineByAppId.do" name="appForm"
				method="GET">
				<select id="selectedapp" style="width: 500px;">
				<c:forEach items="${listMac}" var="exp" varStatus="index">
				  <option value="${exp.id}"
							<c:if test="${exp.id == appId}"> selected="selected"  </c:if>>
							${exp.appName}
						</option>
				</c:forEach>
				</select>
				</select>
				<input type="hidden" name="appId" value="${appId}" />
				<button type="submit" class="btn primary"
					onClick="document.appForm.appId.value=document.appForm.selectedapp.value;">
					确定
				</button>
			</form>
		</div>
		<div style="width: 990px; height: auto; margin: 0 auto;">
			<table align="center">
				<thead>
					<tr>
						<td colspan="3">
							<button data-controls-modal="modal-from-dom" data-backdrop="true"
								data-keyboard="true" class="btn info" onclick="SelectAll()">
								全选/反选
							</button>
							<button data-controls-modal="modal-from-dom" data-backdrop="true"
								data-keyboard="true" class="btn info" onclick="batchMachine()">
								批量删除
							</button>
						</td>
						<td colspan="3" align="right" style="padding-top: 20px;">
							&nbsp;&nbsp;共
							<span id="blink"
								style="font-weight: bolder; font-size: 14px; color: red;">${other}
							</span>台状态为working_online，os为64位机器。
						</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td align="center" width="50">
							序号
						</td>
						<td align="center" width="200">
							应用名称
						</td>
						<td align="center" width="200">
							NODE_NAME
						</td>
						<td align="center" width="200">
							DNS_IP
						</td>
						<td align="center" width="200">
							入库时间
						</td>
						<td align="center" width="100">
							操作
						</td>
					</tr>

					<c:forEach items="${listPressure}" var="exp" varStatus="index">

						<tr onMouseOver="this.bgColor='#BCE774'"
							onMouseOut="this.bgColor='#FFFFFF'">
							<td align="center" >
								<input type="checkbox" name="checkboxid"
									id="checkboxid_${index.index + 1}" value="${exp.id}" />
							</td>
							<td align="center" >
								<span title="${exp.appName}">${exp.appName}</span>
							</td>
							<td align="center" >
								&nbsp;&nbsp;
								<span title="${exp.macName}">${exp.macName}</span>
							</td>
							<td align="center" >
								<span title="${exp.macIp}">${exp.macIp}</span>
							</td>
							<td align="center" >
								<span title="${exp.createTime}">${exp.createTime}</span>
							</td>
							<td align="center" >
								<button data-controls-modal="modal-from-dom"
									data-backdrop="true" data-keyboard="true" class="btn info"
									onClick="delOneMachine('${exp.id}')">
									删除
								</button>
							</td>
						</tr>

					</c:forEach>
					 
				</tbody>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
function SelectAll() {
 var checkboxs=document.getElementsByName("checkboxid");
 for (var i=0;i<checkboxs.length;i++) {
  var e=checkboxs[i];
  e.checked=!e.checked;
 }
}
 function getDeleteStr(){  
     var select=false; 
     var dataStr = ""; 
     var checkboxs=document.getElementsByName("checkboxid");
     for(var i=0;i<checkboxs.length;i++){  
        if(checkboxs[i].checked){
         var str = checkboxs[i].value;
         dataStr =   str+","+ dataStr;
         select=true;  
        }  
     }  
     if(!select){  
      alert("你还没选择要删除的记录！");  
     }  
     return dataStr;  
    }
function batchMachine() {
  var val  = getDeleteStr();
 if (confirm('确认删除选择的机器吗?')) {
      var url = "${baseUrl}/delAppMachineById.do?appId=${appId}&flag=" + val;
      window.location.href = url;
    }
}

function delOneMachine(val) {
 if (confirm('确认删除选择的机器吗?')) {
      var url = "${baseUrl}/delAppMachineById.do?appId=${appId}&flag=" + val;
      window.location.href = url;
    }
}
</script>
<jsp:include page="./footer.jsp" />