<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="row">
	<div class="col-sm-4">
		<div class="panel panel-primary">
			<div class="panel-body">
				<center>
					<c:choose>
						<c:when test="${user.pictureGeneratedName != null}">
							<img class="img-thumbnail" src="<s:url value="/uploadedfiles/User"></s:url>/${user.id}/${user.pictureGeneratedName}" />
						</c:when>
						<c:otherwise>
							<h1>
								<span class="glyphicon glyphicon-user"> </span>
							</h1>
						</c:otherwise>
					</c:choose>					
				</center>
				<br />
				<div class="form-group">
					<label for="name" class="control-label">
						<s:message code="user.name" />:
					</label>
					<span id="name">
						${user.name}
					</span>
				</div>
				<div class="form-group">
					<label for="email" class="control-label">
						<s:message code="user.email" />:
					</label>
					<span id="email">
						${user.email}
					</span>
				</div>
				
				<div class="form-group">
					<div class="panel panel-default">
						<div class="panel-heading">
							<strong>
								<s:message code="user.groups" />
							</strong>
						</div>
						<ul class="list-group">
							<c:forEach items="${user.groups}" var="group">
								<li class="list-group-item">
									<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_VIEW">
										<a href='<s:url value="/user-group/view"></s:url>/${group.id}'>
											${group.name}
										</a>
									</security:authorize>
									
									<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_VIEW">
										${group.name}
									</security:authorize>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-8">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<center>
					<strong>
						<s:message code="user.index.title" />
					</strong>
				</center>
			</div>
			<div class="panel-body">
				<s:message code="user.index.body" />
			</div>
		</div>
	</div>
</div>