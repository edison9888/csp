<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div
	style="padding-left: 50px; width: 200px; height: 300px; position: absolute; overflow: scroll;">

	<form action="${base }/dbconf/rel_app_add.do" method="post" id="form1">
		<input type="hidden" name="dbId" value="${param.dbId}" />
		<ul class="inputs-list">
			<c:forEach var="item" items="${list}">
				<li>
					<label>
						<input type="checkbox" name="appId" value="${item.appId}" />
						<span>${item.appName }</span>
					</label>
				</li>
			</c:forEach>
		</ul>

		<input class="btn  primary" type="button" value="Ìí¼Ó" id="submitBtn" />
		<input class="btn " type="button" value="¹Ø±Õ" id="closeBtn" />



	</form>
</div>
