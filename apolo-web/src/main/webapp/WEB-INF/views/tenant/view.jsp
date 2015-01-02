<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

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
							<s:message code="tenant.view.title" />
						</strong>
					</div>
					<div class="panel-body">
					
						<c:choose>
							<c:when test="${readOnly}">
								<div class="col-sm-2">
									<center>
										<c:choose>
											<c:when test="${tenant.logo != null && not empty tenant.logo}">
												<img class="img-thumbnail" src="<s:url value="/${tenant.url}/uploadedfiles/Tenant"></s:url>/${tenant.id}/${tenant.logo}" />
											</c:when>
											<c:otherwise>
												<h1>
													<span class="fa fa-globe"> </span>
												</h1>
											</c:otherwise>
										</c:choose>
									</center>
								</div>
								
								<div class="col-sm-10">
									<jsp:include page='_tenant-form.jsp'></jsp:include>
								</div>
							</c:when>
							<c:otherwise>
								<jsp:include page='_tenant-form.jsp'></jsp:include>
							</c:otherwise>
						</c:choose>
						
						<div class="form-actions">
							<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_EDIT">
								<a href='<s:url value="/tenant/edit"></s:url>/${tenant.id}' class="btn btn-default">
									<i class="glyphicon glyphicon-edit"></i>
									<s:message code="common.edit" />
								</a>
							</security:authorize>
					
							<a href='#' class="btn back btn-default" > 
								<i class="glyphicon glyphicon-backward"></i>
								<s:message code="common.back" />
							</a>
							
							<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_MANAGER">
								<c:choose>
									<c:when test="${tenant.status == 'ACTIVE'}">
										<a href='<s:url value="/tenant/lock"></s:url>/${tenant.id}' class="btn btn-danger">
											<i class="glyphicon glyphicon-eye-close"></i>
											<s:message code="common.lock" />
										</a>
									</c:when>
									<c:otherwise>
										<a href='<s:url value="/tenant/unlock"></s:url>/${tenant.id}' class="btn btn-success">
											<i class="glyphicon glyphicon-eye-open"></i>
											<s:message code="common.unlock" />
										</a>
									</c:otherwise>
								</c:choose>					
							</security:authorize>
							
							<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_MANAGER">
								<a href='<s:url value="/tenant/change"></s:url>/${tenant.url}' class="btn btn-default btn-small">
									<i class="fa fa-exchange"></i>
									<s:message code="tenant.change" />
								</a>
							</security:authorize>
						</div>
					</div>
				</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>			
		</div>
	</body>
</html>