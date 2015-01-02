<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_CREATE, ROLE_TENANT_EDIT, ROLE_TENANT_LIST, ROLE_TENANT_MANAGER">

	<security:authentication property="systemUser" var="currentUser" />

	<li class="openable">
		<a href="#">
			<span class="menu-icon">
				<i class="fa fa-globe"></i>
			</span>
			<span class="text">
				<s:message code="tenant.menu.header" />
			</span>
			<span class="menu-hover"></span>
		</a>
                       
		<ul class="submenu">
			<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_LIST">
				<li>
					<a href='<s:url value="/tenant/list"></s:url>' class="submenu-label">
						<span class="submenu-label">
							<i class="glyphicon glyphicon-th-list"></i>
							<s:message code="tenant.list" />
						</span>
					</a>
				</li>
			</security:authorize>
			<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_TENANT_CREATE">
				<li>
					<a href='<s:url value="/tenant/new"></s:url>' class="submenu-label">
						<span class="submenu-label">
							<i class="glyphicon glyphicon-plus"></i>
							<s:message code="tenant.create" />
						</span>
					</a>
				</li>
			</security:authorize>
		</ul>
	</li>
</security:authorize>