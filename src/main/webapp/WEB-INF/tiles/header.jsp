<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="panel">
	<div class="row">
		<div class="six centered columns">
			<h1>
				<s:message code="app.title" />
			</h1>
		</div>	
	</div>
	
	<security:authorize access="isAuthenticated()">
		<div class="twelve columns">
			<p class="right">
				<s:message code="user.hello" /> 
				<security:authentication property="principal.systemUser.name" />
				[
				<a href='<s:url value="/auth/logout"></s:url>' >
					<s:message code="user.logout" />
				</a>
				]
			</p>
		</div>	
	</security:authorize>
	
	<security:authorize access="!isAuthenticated()">
		<div class="twelve columns">
			<p class="right">
				<a href='<s:url value="/auth/login"></s:url>' >
					<s:message code="user.restricted.area.access" />
				</a>
			</p>
		</div>	
	</security:authorize>
</div>