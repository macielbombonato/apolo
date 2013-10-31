<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_CREATE, ROLE_USER_CUSTOM_FIELD_LIST, ROLE_USER_CUSTOM_FIELD_CREATE">
	<ul class="nav navbar-nav">
		<li class="dropdown">
			<a href="#" class="dropdown-toggle" data-toggle="dropdown">
				<i class="glyphicon glyphicon-cog "></i>
				<span class="hidden-phone">
					<s:message code="sysadmin.menu.header" />
				</span>
				<span class="caret"></span>
			</a>
                        
			<ul class="dropdown-menu">
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_LIST, ROLE_USER_MANAGER">
					<li class="dropdown-header">
						<s:message code="users" />
					</li>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST">
						<li>
							<a href='<s:url value="/user/list"></s:url>'>
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.list" />
							</a>
						</li>
					</security:authorize>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_MANAGER">
						<li>
							<a href='<s:url value="/user/list-locked"></s:url>'>
								<i class="glyphicon glyphicon-eye-close"></i>
								<s:message code="user.list.locked" />
							</a>
						</li>
					</security:authorize>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE">
						<li>
							<a href='<s:url value="/user/new"></s:url>'>
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="user.new" />
							</a>
						</li>
					</security:authorize>
				</security:authorize>
                                
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_CREATE">
					<li class="dropdown-header">
						<s:message code="user.groups.short" />
					</li>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_LIST">
						<li>
							<a href='<s:url value="/user-group/list"></s:url>'>
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.group.list" />
							</a>
						</li>
					</security:authorize>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_CREATE">
						<li>
							<a href='<s:url value="/user-group/new"></s:url>'>
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="user.group.new" />
							</a>
						</li>
					</security:authorize>
				</security:authorize>
				
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_LIST, ROLE_USER_CUSTOM_FIELD_CREATE">
					<li class="dropdown-header">
						<s:message code="sysadmin.menu.config" />
					</li>
					
					<security:authorize  ifAnyGranted="ROLE_ADMIN">
						<li>
							<a href='<s:url value="/auditlog/list"></s:url>'>
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="auditlog.list" />
							</a>
						</li>
					</security:authorize>
					
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_LIST">
						<li>
							<a href='<s:url value="/user-custom-field/list"></s:url>'>
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.custom.field.list" />
							</a>
						</li>
					</security:authorize>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_CREATE">
						<li>
							<a href='<s:url value="/user-custom-field/new"></s:url>'>
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="user.custom.field.new" />
							</a>
						</li>
					</security:authorize>
				</security:authorize>
			</ul>
		</li>           
	</ul>
</security:authorize>