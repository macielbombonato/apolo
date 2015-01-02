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
							<s:message code="configuration.list" />
						</strong>
					</div>
					
					<div class="panel-body">
						
						<center>
							<jsp:include page='/WEB-INF/views/fragment/_pagination.jsp'></jsp:include>
						</center>
						<c:choose>
							<c:when test="${configurationList != null && not empty configurationList}">
								<div class="table-responsive">
									<table class="table table-striped table-hover table-bordered">
										<thead>
											<tr>
												<th>
													<s:message code="configuration.field" />
												</th>
												<th>
													<s:message code="configuration.value" />
												</th>

												<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_VIEW, ROLE_CONFIGURATION_EDIT, ROLE_CONFIGURATION_REMOVE">
													<th>
														<s:message code="common.actions" />
													</th>
												</security:authorize>
											</tr>
										</thead>
										
										<tbody>
											<c:forEach items="${configurationList}" var="conf">
												<tr id="configuration_${conf.id}">
													<td>
														<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_VIEW">
															<a href='<s:url value="/${currentUser.tenant.url}/configuration/view"></s:url>/${conf.id}' class="btn btn-link">
																<s:message code="configuration.field.${conf.field}" />
															</a>
														</security:authorize>
														<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_CONFIGURATION_VIEW">
															<s:message code="configuration.field.${conf.field}" />
														</security:authorize>
													</td>
													<td>
														<c:choose>
															<c:when test="${conf.field.cryptedValue}">
																xxx
															</c:when>
															<c:otherwise>
																${conf.value}
															</c:otherwise>
														</c:choose>
													</td>
													<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_VIEW, ROLE_CONFIGURATION_EDIT, ROLE_CONFIGURATION_REMOVE">
														<td>
															<div class="btn-group btn-group-sm">
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_VIEW">
																	<a href='<s:url value="/${currentUser.tenant.url}/configuration/view"></s:url>/${conf.id}' class="btn btn-small btn-default" data-toggle="tooltip" title="<s:message code="common.show" />">
																		<i class="glyphicon glyphicon-zoom-in"></i>
																	</a>
																</security:authorize>
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_EDIT">
																	<a href='<s:url value="/${currentUser.tenant.url}/configuration/edit"></s:url>/${conf.id}' class="btn btn-small btn-default" data-toggle="tooltip" title="<s:message code="common.edit" />">
																		<i class="glyphicon glyphicon-edit"></i>
																	</a>
																</security:authorize>
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_REMOVE">
																	<a href='#' class="btn btn-small btn-default" onclick="removeConfirmationDialogOpen('<s:url value="/${currentUser.tenant.url}/configuration/remove"></s:url>/${conf.id}', 'configuration_${conf.id}');" data-toggle="tooltip" title="<s:message code="common.remove" />">
																		<i class="glyphicon glyphicon-remove"></i>
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