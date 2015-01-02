<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_CREATE, ROLE_USER_CUSTOM_FIELD_LIST, ROLE_USER_CUSTOM_FIELD_CREATE, ROLE_CONFIGURATION_CREATE, ROLE_CONFIGURATION_EDIT, ROLE_CONFIGURATION_LIST,, ROLE_CONFIGURATION_REMOVE">
	
	<security:authentication property="systemUser" var="currentUser" />
	
	<li class="openable">
		<a href="#">
			<span class="menu-icon">
				<i class="glyphicon glyphicon-cog "></i>
			</span>
			<span class="text">
				<s:message code="sysadmin.menu.header" />
			</span>
			<span class="menu-hover"></span>
		</a>
		
		<ul class="submenu">
			<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_LIST, ROLE_USER_MANAGER">
				<li class="dropdown-header">
					<s:message code="users" />
				</li>
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/user/list"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.list" />
							</span>
						</a>
					</li>
				</security:authorize>
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_MANAGER">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/user/list-locked"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-eye-close"></i>
								<s:message code="user.list.locked" />
							</span>
						</a>
					</li>
				</security:authorize>
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/user/new"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="user.new" />
							</span>
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
						<a href='<s:url value="/${currentUser.tenant.url}/user-group/list"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.group.list" />
							</span>
						</a>
					</li>
				</security:authorize>
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_PERMISSION_CREATE">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/user-group/new"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="user.group.new" />
							</span>
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
						<a href='<s:url value="/${currentUser.tenant.url}/auditlog/list"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="auditlog.list" />
							</span>
						</a>
					</li>
				</security:authorize>
				
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_LIST">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/user-custom-field/list"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.custom.field.list" />
							</span>
						</a>
					</li>
				</security:authorize>
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CUSTOM_FIELD_CREATE">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/user-custom-field/new"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="user.custom.field.new" />
							</span>
						</a>
					</li>
				</security:authorize>
				
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_LIST">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/configuration/list"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="configuration.list" />
							</span>
						</a>
					</li>
				</security:authorize>
				
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_CONFIGURATION_CREATE">
					<li>
						<a href='<s:url value="/${currentUser.tenant.url}/configuration/new"></s:url>' class="submenu-label">
							<span class="submenu-label">
								<i class="glyphicon glyphicon-plus"></i>
								<s:message code="configuration.new" />
							</span>
						</a>
					</li>
				</security:authorize>
			</security:authorize>
		</ul>
	</li>
</security:authorize>