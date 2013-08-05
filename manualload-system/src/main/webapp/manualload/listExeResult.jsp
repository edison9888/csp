<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测结果"); %>
<jsp:include page="./header.jsp" />
<div class="container">
	<div id="bd" class="resource">
		 
		<div class="alert-message block-message success">
			<form action="${baseUrl}/getAllPresureResultInfo.do" name="appForm"
				method="GET">
				<select id="selectedapp" style="width: 300px;">
				<c:forEach items="${listMac}" var="exp" varStatus="index">
				  <option value="${exp.id}"
							<c:if test='${exp.id == appId}'> selected="selected"  </c:if>>
							${exp.appName}
						</option>
				</c:forEach>
				</select>
				<span class="inline-inputs"> 
				<input class="Wdate" type="text" name="querydate" value="${querydate}" onClick="WdatePicker()" 
				    style="width:150px;height:25px;"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"> 
				</span>
				<input type="hidden" name="appId" value="${appId}"/>
				<button type="submit" class="btn primary"
					onClick="document.appForm.appId.value=document.appForm.selectedapp.value;">
					确定
				</button>
			</form>
		</div>
   </div>
    <div style="width: 990px; height: auto; margin: 0 auto">
		<table  align="center" >
			<tr>
				<td align="center"  width="80">
					序号
				</td>
				<td align="center"  width="100">
					压测时间
				</td>
				<td align="center"  width="100">
					压测机器
				</td>
				<td align="center"  width="80">
					request_totle
				</td>
				<td align="center"  width="80">
					bad_count
				</td>
				<td align="center"  width="80">
					response_avg
				</td>
				<td align="center"  width="80">
					fetches_sec
				</td>
				<td align="center"  width="80">
					bytes_connection
				</td>
				<td align="center"  width="160">
					state_count
				</td>
			</tr>
			<c:forEach items="${listResult}" var="exp" varStatus="i">
                       <tr onMouseOver="this.bgColor='#BCE774'"
						onMouseOut="this.bgColor='#FFFFFF'">
						
						<td align="center" width="20" >
							<span> ${i.index+1}</span>
						</td>
						<td align="center" width="380" >
							<span title="${exp.createTime}"> ${exp.insertTime} </span>
						</td>
						<td width="120" >
							 <span title="${exp.macName}">${exp.macName}</span>
						</td>
						<td align="center" width="20" >
							<span title="${exp.requestTotle}">${exp.requestTotle}</span>
						</td>
						<td align="center" width="20" >
							<span title="${exp.badCount}"> 
							 ${exp.badCount} 
						    </span>
						</td>
						<td align="center" width="20" >
							<span title="${exp.responseAvg}">
							 ${exp.responseAvg} 
						    </span>
						</td>
						<td align="center" width="20" >
							<span title="${exp.fetchesSec}">
							 ${exp.fetchesSec} 
						    </span>
						</td>
						<td align="center" width="20" >
							<span title="${exp.bytesConnection}"> 
						     ${exp.bytesConnection}
						    </span>
						</td>
						<td align="center" width="270" >
							<span title="${exp.httpState_stateCount}">${exp.httpState_stateCount}</span>
						</td>
					</tr>
					</c:forEach>
		</table>
	</div>
</div>
<jsp:include page="./footer.jsp" />

