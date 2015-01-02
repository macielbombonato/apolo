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
							<s:message code="user.view.title" />
						</strong>
					</div>
					<div class="panel-body">
						<div class="col-sm-2">
							<center>
								<c:choose>
									<c:when test="${user.pictureGeneratedName != null && not empty user.pictureGeneratedName}">
										<img class="img-thumbnail" src="<s:url value="/${currentUser.tenant.url}/uploadedfiles/User"></s:url>/${user.id}/${user.pictureGeneratedName}" />
									</c:when>
									<c:otherwise>
										<h1>
											<span class="glyphicon glyphicon-user"> </span>
										</h1>
									</c:otherwise>
								</c:choose>
							</center>
						</div>
						
						<div class="col-sm-10">
							<jsp:include page='_user-form.jsp'></jsp:include>
							
							<div class="form-actions">
								<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_EDIT">
									<a href='<s:url value="/${currentUser.tenant.url}/user/edit"></s:url>/${user.id}' class="btn btn-default">
										<i class="glyphicon glyphicon-edit"></i>
										<s:message code="common.edit" />
									</a>
								</security:authorize>
								<security:authorize ifNotGranted="ROLE_ADMIN, ROLE_USER_EDIT">
	
									<security:authentication property="systemUser.id" var="authenticatedUserId" />
	
									<c:if test="${authenticatedUserId == user.id}">
										<a href='<s:url value="/${currentUser.tenant.url}/user/edit"></s:url>/${user.id}' class="btn btn-default">
											<i class="glyphicon glyphicon-edit"></i>
											<s:message code="common.edit" />
										</a>
									</c:if>
								</security:authorize>
								
								<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER_MANAGER">
									<c:if test="${user.status.changeable}">
										<c:choose>
											<c:when test="${user.status == 'ACTIVE'}">
												<a href='<s:url value="/${currentUser.tenant.url}/user/lock"></s:url>/${user.id}' class="btn btn-danger">
													<i class="glyphicon glyphicon-eye-close"></i>
													<s:message code="common.lock" />
												</a>
											</c:when>
											<c:otherwise>
												<a href='<s:url value="/${currentUser.tenant.url}/user/unlock"></s:url>/${user.id}' class="btn btn-success">
													<i class="glyphicon glyphicon-eye-open"></i>
													<s:message code="common.unlock" />
												</a>
											</c:otherwise>
										</c:choose>					
									</c:if>
								</security:authorize>
								
								<a href='#' class="btn btn-default back" > 
									<i class="glyphicon glyphicon-backward"></i>
									<s:message code="common.back" />
								</a>
							</div>		
						</div>
					</div>
				</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>