<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_CREATE">
	<ul class="nav navbar-nav">
		<li class="dropdown">
			<a href="#" class="dropdown-toggle" data-toggle="dropdown">
				<i class="glyphicon glyphicon-cog "></i>
				<span class="hidden-phone">
					<s:message code="user" />
				</span>
				<span class="caret"></span>
			</a>
                        
			<ul class="dropdown-menu">
				<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_LIST">
					<li class="dropdown-header">
						<s:message code="users" />
					</li>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST">
						<li>
							<a href='<s:url value="/user/search-form"></s:url>'>
								<i class="glyphicon glyphicon-search"></i>
								<s:message code="common.search" />
							</a>
						</li>
					</security:authorize>
					<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST">
						<li>
							<a href='<s:url value="/user/list"></s:url>'>
								<i class="glyphicon glyphicon-th-list"></i>
								<s:message code="user.list" />
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
							<a href='<s:url value="/user-group/search-form"></s:url>'>
								<i class="glyphicon glyphicon-search"></i>
								<s:message code="common.search" />
							</a>
						</li>
					</security:authorize>
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
			</ul>
		</li>           
	</ul>
</security:authorize>

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