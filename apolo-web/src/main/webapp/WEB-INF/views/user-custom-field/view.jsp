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
							<s:message code="user.custom.field.view.title" />
						</strong>
					</div>
					<div class="panel-body">
						<jsp:include page='_field-form.jsp'></jsp:include>
						
						<div class="form-actions">
							<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_EDIT">
								<a href='<s:url value="/${currentUser.tenant.url}/user-custom-field/edit"></s:url>/${userCustomField.id}' class="btn btn-default">
									<i class="glyphicon glyphicon-edit"></i>
									<s:message code="common.edit" />
								</a>
							</security:authorize>
					
							<a href='#' class="btn back btn-default" > 
								<i class="glyphicon glyphicon-backward"></i>
								<s:message code="common.back" />
							</a>
						</div>
					</div>
				</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>