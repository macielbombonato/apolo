<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authentication property="systemUser" var="currentUser" />

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body class="overflow-hidden">
		<div id="overlay" class="transparent"></div>

		<div id="wrapper" class="preload">
			<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
			
			<div id="main-container">
			
				<jsp:include page='/WEB-INF/views/fragment/_breadcrumb.jsp'></jsp:include>
				
				<jsp:include page='/WEB-INF/views/fragment/_message.jsp'></jsp:include>
	
				<div class="panel panel-default">
					<div class="panel-heading">
						<strong>
							<s:message code="user.custom.field.edit.title" />
						</strong>
					</div>
					<div class="panel-body">
						<form id="userGroupForm" action="<s:url value="/${currentUser.tenant.url}/user-custom-field/save"></s:url>" method="post" role="form">
							<jsp:include page='_field-form.jsp'></jsp:include>
							
							<div class="form-actions">
								<button type="submit" class="btn btn-primary">
									<i class="glyphicon glyphicon-ok"></i>
									<s:message code="common.save" /> 
								</button>
					
								<c:choose>
									<c:when test="${userCustomField.id != null}">
										<a href='<s:url value="/${currentUser.tenant.url}/user-custom-field/view"></s:url>/${userCustomField.id}' class="btn btn-default">
											<i class="glyphicon glyphicon-remove-circle"></i>
											<s:message code="common.cancel" />
										</a>				
									</c:when>
									<c:otherwise>
										<a href='<s:url value="/${currentUser.tenant.url}/user-custom-field/list"></s:url>' class="btn btn-default">
											<i class="glyphicon glyphicon-remove-circle"></i>
											<s:message code="common.cancel" />
										</a>
									</c:otherwise>
								</c:choose>
							</div>
						</form>
					</div>
				</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<script type="text/javascript" src='<c:url value="/${currentUser.tenant.url}/resources/app/js/userCustomField.js" />'></script>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>