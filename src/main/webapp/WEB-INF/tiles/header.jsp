<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div class="navbar">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".navbar-responsive-collapse"> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>
			</a> 
			
			<a class="brand" href='<s:url value="/"></s:url>'>
				<s:message code="app.title" />
			</a>
			
			<div class="nav-collapse collapse navbar-responsive-collapse">
				<ul class="nav">
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#">Link</a></li>
					<li><a href="#">Link</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">Dropdown <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="#">Action</a></li>
							<li><a href="#">Another action</a></li>
							<li><a href="#">Something else here</a></li>
							<li class="divider"></li>
							<li class="nav-header">Nav header</li>
							<li><a href="#">Separated link</a></li>
							<li><a href="#">One more separated link</a></li>
						</ul></li>
				</ul>
				<form class="navbar-search pull-left" action="">
					<input type="text" class="search-query span2" placeholder="Search">
				</form>
				
				<ul class="nav pull-right">
					<security:authorize access="isAuthenticated()">
						<li>
							<a href="#">
								<s:message code="user.hello" /> <security:authentication property="principal.systemUser.name" />
							</a>
						</li>
						
						<li class="divider-vertical" />
					
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<s:message code="user" /> <b class="caret"></b>
							</a>
							
							<ul class="dropdown-menu">
								<li>
									<a href='<s:url value="/user/new"></s:url>'>
										<s:message code="user.new" />
									</a>
								</li>
								
								<li class="divider" />
								
								<li>
									<a href='<s:url value="/user"></s:url>'>
										<security:authentication property="principal.systemUser.name" />
									</a>
								</li>
								<li class="divider" />
								<li>
									<a href='<s:url value="/auth/logout"></s:url>'>
										<s:message code="user.logout" />
									</a>
								</li>
							</ul>
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
			</div>
			<!-- /.nav-collapse -->
		</div>
	</div>
	<!-- /navbar-inner -->
</div>

<br />




