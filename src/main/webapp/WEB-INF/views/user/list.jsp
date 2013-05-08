<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<jsp:include page='_search-form.jsp'></jsp:include>

<fieldset>
	<legend>
		<s:message code="user.list.title" />
	</legend>
	
	<c:choose>
		<c:when test="${userList != null && not empty userList}">
			<table class="table table-striped table-hover table-bordered">
				<thead>
					<tr>
						<th>
							<s:message code="user.username" />
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
					<c:forEach items="${userList}" var="user">
						<tr id="user_${user.id}">
							<td>
								<a href='<s:url value="/user/view"></s:url>/${user.id}' class="btn btn-link">
									${user.name}
								</a>
							</td>
							<td>
								${user.email}
							</td>
							<td>
								<table class="table table-condensed table-bordered">
									<tbody>
										<c:forEach items="${user.groups}" var="group">
											<tr>
												<td>
													<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_VIEW">
														<a href='<s:url value="/user-group/view"></s:url>/${group.id}' class="btn btn-link">
															${group.name}
														</a>
													</security:authorize>
													<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_VIEW">
														${group.name}
													</security:authorize>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</td>
							<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_EDIT, ROLE_USER_REMOVE">
								<td>
									<div class="btn-group">
										<a href='<s:url value="/user/view"></s:url>/${user.id}' class="btn btn-small" data-toggle="tooltip" title="<s:message code="common.show" />">
											<i class="icon-zoom-in"></i>
										</a>
										<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_EDIT">
											<a href='<s:url value="/user/edit"></s:url>/${user.id}' class="btn btn-small" data-toggle="tooltip" title="<s:message code="common.edit" />">
												<i class="icon-edit"></i>
											</a>
										</security:authorize>
										<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_REMOVE">
											<a href='#' class="btn btn-small" onclick="removeConfirmationDialogOpen('<s:url value="/user/remove"></s:url>/${user.id}', 'user_${user.id}');" data-toggle="tooltip" title="<s:message code="common.remove" />">
												<i class="icon-remove"></i>
											</a>
										</security:authorize>
									</div>
								</td>
							</security:authorize>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<s:message code="common.nodatafound" htmlEscape="false"/>
		</c:otherwise>
	</c:choose>
</fieldset>
