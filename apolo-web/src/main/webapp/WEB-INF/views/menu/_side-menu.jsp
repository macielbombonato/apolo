<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>


<security:authorize ifAnyGranted="ROLE_AFTER_AUTH_USER">
	<security:authentication property="systemUser" var="currentUser" />
	<c:set var="skin" value="skin-5" />
	
	<c:if test="${currentUser != null && currentUser.tenant != null}">
		<c:if test="${currentUser.tenant.skin != null}">
			<c:set var="skin" value="${currentUser.tenant.skin.code}" />
		</c:if>
	</c:if>

	<aside class="${skin}">
		<div class="slimScrollDiv" style="position: relative; overflow: hidden; width: auto; height: 100%;">
			<div class="sidebar-inner scrollable-sidebar" style="overflow: hidden; width: auto; height: 100%;">
				<div class="size-toggle">
					<a class="btn btn-sm" id="sizeToggle">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</a>
					
					<a class="btn btn-sm pull-right" href='<s:url value="/auth/logout"></s:url>' >
						<i class="fa fa-power-off"></i>
					</a>
				</div><!-- /size-toggle -->
				<div class="user-block clearfix">
					<div class="row">
						<div class="col-sm-12">
							<c:choose>
								<c:when test="${currentUser.pictureGeneratedName != null && not empty currentUser.pictureGeneratedName}">
									<span>
										<i>
											<img src="<s:url value="/${currentUser.dbTenant.url}/uploadedfiles/User/${currentUser.id}/${currentUser.pictureGeneratedName}"></s:url>" />
										</i>
									</span>
								</c:when>
								<c:otherwise>
									<span><i class="glyphicon glyphicon-user"> </i></span>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				
					<div class="detail">
						<strong>
							<security:authentication property="systemUser.name" />
						</strong>
						<ul class="list-inline">
							<li>
								<a href='<s:url value="/${currentUser.tenant.url}/user"></s:url>'>
									<i class="fa fa-desktop fa-lg"></i>
									<s:message code="user.dashboard" />
								</a>
							</li>
							<li>
								<a href='<s:url value="/${currentUser.tenant.url}/user/view"></s:url>/<security:authentication property="systemUser.id" />'>
									<i class="glyphicon glyphicon-user"></i>
									<s:message code="user.profile" />
								</a>
							</li>
						</ul>
					</div>
				</div><!-- /user-block -->
		        <div class="main-menu">
					<ul>
						<jsp:include page='_side-menu-system-admin.jsp'></jsp:include>
						<jsp:include page='_side-menu-tenant.jsp'></jsp:include>
					</ul>
		        </div>
			</div>
		</div>
	</aside>
	
</security:authorize>