<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<fieldset>
	<legend>
		<s:message code="user.list.title" />
	</legend>
	
	<c:if test="${error}">
		<div class="alert alert-error">
			<s:message code="user.login.failure" />
		</div>
		
	</c:if>
	
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
					<s:message code="common.actions" />
				</th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${userList}" var="user">
				<tr>
					<td>
						${user.name}
					</td>
					<td>
						${user.email}
					</td>
					<td>
						<div class="btn-group">
							<a href='<s:url value="/user/view"></s:url>/${user.id}' class="btn" tabindex="-1">
								<s:message code="common.show" />
							</a>
							<button class="btn dropdown-toggle" data-toggle="dropdown" tabindex="-1">
								<span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<li>
									<a href='<s:url value="/user/edit"></s:url>/${user.id}'>
										<s:message code="common.edit" />
									</a>
								</li>
								<li>
									<a href='<s:url value="/user/remove"></s:url>/${user.id}'>
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
