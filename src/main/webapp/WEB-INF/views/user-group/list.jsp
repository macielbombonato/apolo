<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<fieldset>
	<legend>
		<s:message code="user.group.list.title" />
	</legend>
	
	<c:choose>
		<c:when test="${error}">
			<div class="alert alert-error">
				${message}
			</div>		
		</c:when>
		<c:when test="${msg}">
			<div class="alert alert-info">
				${message}
			</div>		
		</c:when>
	</c:choose>
	
	<table class="table table-striped table-hover table-bordered">
		<thead>
			<tr>
				<th>
					<s:message code="user.group.name" />
				</th>
				<th>
					<s:message code="user.group.permissions" />
				</th>
				<th>
					<s:message code="common.actions" />
				</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${userGroupList}" var="group">
				<tr>
					<td>
						${group.name}
					</td>
					<td>
						<table class="table table-condensed table-bordered">
							<tbody>
								<c:forEach items="${group.permissions}" var="permission">
									<tr>
										<td>
											<s:message code="user.permission.${permission}" />
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</td>
					<td>
						<div class="btn-group">
							<a href='<s:url value="/user-group/view"></s:url>/${group.id}' class="btn" tabindex="-1">
								<s:message code="common.show" />
							</a>
							<button class="btn dropdown-toggle" data-toggle="dropdown" tabindex="-1">
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<li>
									<a href='<s:url value="/user-group/edit"></s:url>/${group.id}'>
										<s:message code="common.edit" />
									</a>
								</li>
								<li>
									<a href='<s:url value="/user-group/remove"></s:url>/${group.id}'>
										<s:message code="common.remove" />
									</a>
								</li>
							</ul>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>
