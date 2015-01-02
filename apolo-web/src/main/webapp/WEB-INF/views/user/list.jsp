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
							<s:message code="user.list.title" />
						</strong>
					</div>
					
					<div class="panel-body">
						
						<jsp:include page='_search-form.jsp'></jsp:include>
						
						<br />
						<center>
							<jsp:include page='/WEB-INF/views/fragment/_pagination.jsp'></jsp:include>
						</center>
						<c:choose>
							<c:when test="${userList != null && not empty userList}">
								<div class="table-responsive">
									<table class="table table-striped table-hover table-bordered">
										<thead>
											<tr>
												<th>
													<s:message code="user.picturefiles" />
												</th>
												<th>
													<s:message code="user.name" />
												</th>
												<th>
													<s:message code="user.email" />
												</th>
												<th>
													<s:message code="user.groups" />
												</th>
												<th>
													<s:message code="user.status" />
												</th>
												<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_EDIT, ROLE_USER_REMOVE">
													<th>
														<s:message code="common.actions" />
													</th>
												</security:authorize>
											</tr>
										</thead>
										
										<tbody>
											<c:forEach items="${userList}" var="user">
												<tr id="user_${user.id}">
													<td>
														<center>
															<c:choose>
																<c:when test="${user.pictureGeneratedName != null && not empty user.pictureGeneratedName}">
																	<img class="img-thumbnail" src="<s:url value="/${currentUser.tenant.url}/uploadedfiles/User"></s:url>/${user.id}/${user.pictureGeneratedName}" style="width: 40px;"/>
																</c:when>
																<c:otherwise>
																	<span class="glyphicon glyphicon-user"> </span>
																</c:otherwise>
															</c:choose>								
														</center>
													</td>
													<td>
														<a href='<s:url value="/${currentUser.tenant.url}/user/view"></s:url>/${user.id}' class="btn btn-link">
															${user.name}
														</a>
													</td>
													<td>
														${user.email}
													</td>
													<td>
														<ul class="list-group">
															<c:forEach items="${user.groups}" var="group">
																<li class="list-group-item">
																	<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_VIEW">
																		<a href='<s:url value="/${currentUser.tenant.url}/user-group/view"></s:url>/${group.id}' class="btn btn-link">
																			${group.name}
																		</a>
																	</security:authorize>
																	<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_VIEW">
																		${group.name}
																	</security:authorize>
																</li>
															</c:forEach>
														</ul>
													</td>
													<td>
														<s:message code="user.status.${user.status}" />
													</td>
													<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_EDIT, ROLE_USER_REMOVE">
														<td>
															<div class="btn-group btn-group-sm">
																<a href='<s:url value="/${currentUser.tenant.url}/user/view"></s:url>/${user.id}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="common.show" />">
																	<i class="glyphicon glyphicon-zoom-in"></i>
																</a>
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_EDIT">
																	<a href='<s:url value="/${currentUser.tenant.url}/user/edit"></s:url>/${user.id}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="common.edit" />">
																		<i class="glyphicon glyphicon-edit"></i>
																	</a>
																</security:authorize>
																<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_REMOVE">
																	<security:authentication property="systemUser.id" var="userId"/>
																	<c:choose>
																		<c:when test="${userId != user.id}">
																			<a href='#' class="btn btn-default btn-small" onclick="removeConfirmationDialogOpen('<s:url value="/${currentUser.tenant.url}/user/remove"></s:url>/${user.id}', 'user_${user.id}');" data-toggle="tooltip" title="<s:message code="common.remove" />">
																				<i class="glyphicon glyphicon-remove"></i>
																			</a>
																		</c:when>
																	</c:choose>
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