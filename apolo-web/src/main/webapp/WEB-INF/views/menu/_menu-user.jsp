<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authorize ifAnyGranted="ROLE_AFTER_AUTH_USER">
	<security:authentication property="systemUser" var="currentUser" />

	<a class="dropdown-toggle" data-toggle="dropdown" href="#">
		<span><i class="glyphicon glyphicon-user"></i></span>
		<strong>
			<security:authentication property="systemUser.name" />
		</strong>
		<span><i class="fa fa-chevron-down"></i></span>
	</a>
	<ul class="dropdown-menu">
		<li>
			<a class="clearfix" href="#">
				<div class="detail">
					<c:choose>
						<c:when test="${currentUser.pictureGeneratedName != null && not empty currentUser.pictureGeneratedName}">
							<span>
								<i>
									<img src="<s:url value="/${currentUser.dbTenant.url}/uploadedfiles/User/${currentUser.id}/${currentUser.pictureGeneratedName}"></s:url>" />
								</i>
							</span>
						</c:when>
						<c:otherwise>
							<span><i class="glyphicon glyphicon-user"></i></span>
						</c:otherwise>
					</c:choose>
					<strong>
						<security:authentication property="systemUser.name" />
					</strong>
				</div>
			</a>
		</li>
		<li>
			<a href='<s:url value="/${currentUser.tenant.url}/user"></s:url>'>
				<i class="glyphicon glyphicon-dashboard"></i>
				<s:message code="user.dashboard" />
			</a>
		</li>
		<li>
			<a href='<s:url value="/${currentUser.tenant.url}/user/view"></s:url>/<security:authentication property="systemUser.id" />'>
				<i class="glyphicon glyphicon-user"></i>
				<s:message code="user.profile" />
			</a>
		</li>
		<li>
			<a href='<s:url value="/${currentUser.tenant.url}/user/change-password"></s:url>'>
				<i class="glyphicon glyphicon-edit"></i>
				<s:message code="user.change.password" />
			</a>
		</li>
		<li class="divider"></li>
		<li>
			<a href='<s:url value="/auth/logout"></s:url>'>
				<i class="glyphicon glyphicon-off"></i>
				<s:message code="user.logout" />
			</a>
		</li>
	</ul>
</security:authorize>
<security:authorize access="!isAuthenticated()">
	<a href='<s:url value="/auth/login"></s:url>'>
		<span><i class="fa fa-lock"></i></span>
		<strong>
			<s:message code="user.restricted.area.access" />
		</strong>
		<span><i class="fa fa-chevron-down"></i></span>
	</a>
</security:authorize>

<security:authorize ifAnyGranted="ROLE_BEFORE_AUTH_USER">
	<a href='<s:url value="/auth/login"></s:url>'>
		<span><i class="fa fa-lock"></i></span>
		<strong>
			<s:message code="user.restricted.area.access" />
		</strong>
		<span><i class="fa fa-chevron-down"></i></span>
	</a>
</security:authorize>