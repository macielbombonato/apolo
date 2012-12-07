<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authorize access="isAuthenticated()">
	<ul class="nav">
		<li class="dropdown">
			<a href="#" class="dropdown-toggle" data-toggle="dropdown">
				<s:message code="user" /> <b class="caret"></b>
			</a>
			
			<ul class="dropdown-menu">
				<li class="nav-header">
					<s:message code="user.header.admin" />
				</li>
				<li>
					<a href='<s:url value="/user/new"></s:url>'>
						<s:message code="user.new" />
					</a>
				</li>
				
				<li class="divider" />
				
				<li class="nav-header">
					<s:message code="user.header.user" />
				</li>
				<li>
					<a href='<s:url value="/user"></s:url>'>
						<s:message code="user.change.password" />
					</a>
				</li>
			</ul>
		</li>		
	</ul>
</security:authorize>

<ul class="nav pull-right">
	<security:authorize access="isAuthenticated()">
		<li>
			<a href='<s:url value="/user"></s:url>'>
				<security:authentication property="principal.systemUser.name" />
			</a>
		</li>
		
		<li class="divider-vertical" />
		
		<li>
			<a href='<s:url value="/auth/logout"></s:url>'>
				<s:message code="user.logout" />
			</a>
		</li>
	
	</security:authorize>
	
	<security:authorize access="!isAuthenticated()">
		<li>
			<a href='<s:url value="/auth/login"></s:url>'> 
				<s:message code="user.restricted.area.access" />
			</a>
		</li>
	</security:authorize>
</ul>