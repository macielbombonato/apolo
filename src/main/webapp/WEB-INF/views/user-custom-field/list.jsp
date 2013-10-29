<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
	<jsp:include page='/WEB-INF/views/fragment/_pageheader.jsp'></jsp:include>
	<body>
		<jsp:include page='/WEB-INF/views/fragment/_contentheader.jsp'></jsp:include>
		
		<div class="container ">
			<div class="row ">

				<jsp:include page='_search-form.jsp'></jsp:include>
				
				<br />
				
				<div class="panel panel-primary">
					<div class="panel-heading">
						<strong>
							<s:message code="user.custom.field.list.title" />
						</strong>
					</div>
					<center>
						<jsp:include page='/WEB-INF/views/fragment/_pagination.jsp'></jsp:include>
					</center>
					<c:choose>
						<c:when test="${userCustomFieldList != null && not empty userCustomFieldList}">
							<div class="table-responsive">
								<table class="table table-striped table-hover table-bordered">
									<thead>
										<tr>
											<th>
												<s:message code="user.custom.field.name" />
											</th>
											<th>
												<s:message code="user.custom.field.type" />
											</th>
											<th>
												<s:message code="user.custom.field.label" />
											</th>
											<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_VIEW, ROLE_USER_CUSTOM_FIELD_EDIT, ROLE_USER_CUSTOM_FIELD_REMOVE">
												<th>
													<s:message code="common.actions" />
												</th>
											</security:authorize>
										</tr>
									</thead>
									
									<tbody>
										<c:forEach items="${userCustomFieldList}" var="field">
											<tr id="userCustomField_${field.id}">
												<td>
													<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_VIEW">
														<a href='<s:url value="/user-custom-field/view"></s:url>/${field.id}' class="btn btn-link">
															${field.name}
														</a>
													</security:authorize>
													<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_VIEW">
														${field.name}
													</security:authorize>
												</td>
												<td>
													<s:message code="user.custom.field.type.${field.type}" />
												</td>
												<td>
													${field.label}
												</td>
												<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_VIEW, ROLE_USER_CUSTOM_FIELD_EDIT, ROLE_USER_CUSTOM_FIELD_REMOVE">
													<td>
														<div class="btn-group btn-group-sm">
															<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_VIEW">
																<a href='<s:url value="/user-custom-field/view"></s:url>/${field.id}' class="btn btn-small btn-default" data-toggle="tooltip" title="<s:message code="common.show" />">
																	<i class="glyphicon glyphicon-zoom-in"></i>
																</a>
															</security:authorize>
															<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_EDIT">
																<a href='<s:url value="/user-custom-field/edit"></s:url>/${field.id}' class="btn btn-small btn-default" data-toggle="tooltip" title="<s:message code="common.edit" />">
																	<i class="glyphicon glyphicon-edit"></i>
																</a>
															</security:authorize>
															<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_REMOVE">
																<a href='#' class="btn btn-small btn-default" onclick="removeConfirmationDialogOpen('<s:url value="/user-custom-field/remove"></s:url>/${field.id}', 'userCustomField_${field.id}');" data-toggle="tooltip" title="<s:message code="common.remove" />">
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
	</body>
</html>