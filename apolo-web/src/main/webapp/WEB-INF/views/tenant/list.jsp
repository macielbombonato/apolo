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
							<s:message code="tenant.list.title" />
						</strong>
					</div>
					
					<div class="panel-body">
						
						<jsp:include page='_search-form.jsp'></jsp:include>
						
						<br />
						<center>
							<jsp:include page='/WEB-INF/views/fragment/_pagination.jsp'></jsp:include>
						</center>
						<c:choose>
							<c:when test="${tenantList != null && not empty tenantList}">
								<div class="table-responsive">
									<table class="table table-striped table-hover table-bordered">
										<thead>
											<tr>
												<th>
													<s:message code="tenant.logo" />
												</th>
												<th>
													<s:message code="tenant.name" />
												</th>
												<th>
													<s:message code="tenant.url" />
												</th>
												<th>
													<s:message code="user.status" />
												</th>
												<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_LIST, ROLE_TENANT_EDIT, ROLE_TENANT_REMOVE, ROLE_TENANT_MANAGER">
													<th>
														<s:message code="common.actions" />
													</th>
												</security:authorize>
											</tr>
										</thead>
										
										<tbody>
											<c:forEach items="${tenantList}" var="tenant">
												<tr id="tenant_${tenant.id}">
													<td>
														<center>
															<c:choose>
																<c:when test="${tenant.logo != null && not empty tenant.logo}">
																	<img class="img-thumbnail" src="<s:url value="/${tenant.url}/uploadedfiles/Tenant"></s:url>/${tenant.id}/${tenant.logo}" style="width: 40px;"/>
																</c:when>
																<c:otherwise>
																	<span class="fa fa-globe"> </span>
																</c:otherwise>
															</c:choose>								
														</center>
													</td>
													<td>
														<a href='<s:url value="/tenant/view"></s:url>/${tenant.id}' class="btn btn-link">
															${tenant.name}
														</a>
													</td>
													<td>
														${tenant.url}
													</td>
													<td>
														<s:message code="common.status.${tenant.status}" />
													</td>
													<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_LIST, ROLE_TENANT_EDIT, ROLE_TENANT_REMOVE, ROLE_TENANT_MANAGER">
														<td>
															<div class="btn-group btn-group-sm">
																<a href='<s:url value="/tenant/view"></s:url>/${tenant.id}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="common.show" />">
																	<i class="glyphicon glyphicon-zoom-in"></i>
																</a>
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_EDIT">
																	<a href='<s:url value="/tenant/edit"></s:url>/${tenant.id}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="common.edit" />">
																		<i class="glyphicon glyphicon-edit"></i>
																	</a>
																</security:authorize>
																
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_MANAGER">
																	<a href='<s:url value="/tenant/change"></s:url>/${tenant.url}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="tenant.change" />">
																		<i class="fa fa-exchange"></i>
																	</a>
																</security:authorize>
															</div>
														</td>
													</security:authorize>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:when>
							<c:otherwise>
								<div class="panel-body">
									<p>
										<s:message code="common.nodatafound" htmlEscape="false"/>
									</p>
								</div>
							</c:otherwise>
						</c:choose>
						<center>
							<jsp:include page='/WEB-INF/views/fragment/_pagination.jsp'></jsp:include>
						</center>
					</div>
				</div>
			</div>
			
			<jsp:include page='/WEB-INF/views/fragment/_contentfooter.jsp'></jsp:include>
			<jsp:include page='/WEB-INF/views/fragment/_pagefooter.jsp'></jsp:include>
		</div>
	</body>
</html>