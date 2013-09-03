<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<jsp:include page='_search-form.jsp'></jsp:include>

<br />

<div class="panel panel-primary">
	<div class="panel-heading">
		<strong>
			<s:message code="user.list.title" />
		</strong>
	</div>
	<center>
		<jsp:include page='_pagination.jsp'></jsp:include>
	</center>
	<c:choose>
		<c:when test="${page.content != null && not empty page.content}">
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
							<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_EDIT, ROLE_USER_REMOVE">
								<th>
									<s:message code="common.actions" />
								</th>
							</security:authorize>
						</tr>
					</thead>
					
					<tbody>
						<c:forEach items="${page.content}" var="user">
							<tr id="user_${user.id}">
								<td>
									<center>
										<c:choose>
											<c:when test="${user.pictureGeneratedName != null}">
												<img class="img-thumbnail" src="<s:url value="/uploadedfiles/User"></s:url>/${user.id}/${user.pictureGeneratedName}" style="width: 40px;"/>
											</c:when>
											<c:otherwise>
												<span class="glyphicon glyphicon-user"> </span>
											</c:otherwise>
										</c:choose>								
									</center>
								</td>
								<td>
									<a href='<s:url value="/user/view"></s:url>/${user.id}' class="btn btn-link">
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
													<a href='<s:url value="/user-group/view"></s:url>/${group.id}' class="btn btn-link">
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
								<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_EDIT, ROLE_USER_REMOVE">
									<td>
										<div class="btn-group btn-group-sm">
											<a href='<s:url value="/user/view"></s:url>/${user.id}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="common.show" />">
												<i class="glyphicon glyphicon-zoom-in"></i>
											</a>
											<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_EDIT">
												<a href='<s:url value="/user/edit"></s:url>/${user.id}' class="btn btn-default btn-small" data-toggle="tooltip" title="<s:message code="common.edit" />">
													<i class="glyphicon glyphicon-edit"></i>
												</a>
											</security:authorize>
											<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_REMOVE">
												<security:authentication property="principal.systemUser.id" var="userId"/>
												<c:choose>
													<c:when test="${userId != user.id}">
														<a href='#' class="btn btn-default btn-small" onclick="removeConfirmationDialogOpen('<s:url value="/user/remove"></s:url>/${user.id}', 'user_${user.id}');" data-toggle="tooltip" title="<s:message code="common.remove" />">
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
			<jsp:include page='_pagination.jsp'></jsp:include>
		</center>
</div>
