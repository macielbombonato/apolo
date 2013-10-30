<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<ul class="nav navbar-nav navbar-right">
	<security:authorize access="isAuthenticated()">
		<li class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="glyphicon glyphicon-user "></i>
				<span class="hidden-phone">
					<security:authentication property="principal.systemUser.name" />
				</span>
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li>
					<a href='<s:url value="/user"></s:url>'>
						<i class="glyphicon glyphicon-dashboard"></i>
						<s:message code="user.dashboard" />
					</a>
				</li>
				<li>
					<a href='<s:url value="/user/view"></s:url>/<security:authentication property="principal.systemUser.id" />'>
						<i class="glyphicon glyphicon-user"></i>
						<s:message code="user.profile" />
					</a>
				</li>
				<li>
					<a href='<s:url value="/user/change-password"></s:url>'>
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
		</li>
	</security:authorize>
        
	<security:authorize access="!isAuthenticated()">
		<li>
			<a href='<s:url value="/auth/login"></s:url>'>
				<i class="glyphicon glyphicon-lock "></i>
				<span class="hidden-phone">
					<s:message code="user.restricted.area.access" />
				</span> 
			</a>
		</li>
	</security:authorize>
</ul>