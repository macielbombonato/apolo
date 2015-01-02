<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<security:authentication property="systemUser" var="currentUser" />

<div id="breadcrumb">
	<ul id="breadcrumb" class="breadcrumb">
		<c:choose>
			<c:when test="${breadcrumb == null || breadcrumb.tree == null || empty breadcrumb.tree}">
				<li>
					<i class="glyphicon glyphicon-home"></i>
				</li>
			</c:when>
			<c:otherwise>
				<c:forEach var="bc" items="${breadcrumb.tree}" varStatus="status">
					<c:choose>
						<c:when test="${status.index==0}">
							<li>
								<i class="glyphicon glyphicon-home"></i>
							</li>
						</c:when>
						<c:when test="${status.index == fn:length(breadcrumb.tree)-1 && status.index!=0}">
							<li id="current" class="active">
								<s:message code="${bc.name}" />
							</li>
						</c:when>
						<c:otherwise>
							<li class="active">
								<s:message code="${bc.name}" />
							</li> 
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		
		<li class="pull-right">
			<s:message code="version" var="version"/>
			<small>
				<security:authorize ifAnyGranted="ROLE_ADMIN">
					<a href='<s:url value="/version"></s:url>'>
						<s:message code="system.version" />:&nbsp; ${fn:substring(version, 0, 15)}
					</a>
				</security:authorize>
				
				<security:authorize ifNotGranted="ROLE_ADMIN">
					<s:message code="system.version" />:&nbsp; ${fn:substring(version, 0, 15)}
				</security:authorize>			
			</small>
		</li>
		
		<!-- Language selection -->
		<c:set var="langs" value="<%=apolo.data.enums.Language.values()%>"/>

		<li class="pull-right">
			<c:forEach items="${langs}" var="lang">
				<a href='<s:url value="/change-locale/${currentUser.tenant.url}/${lang.code}"></s:url>'>
					<img src='<c:url value="${lang.icon}" />' 
							width="20" 
							height="20" 
							style="width: 20px; height: 20px;" 
						/>
				</a>
			</c:forEach>
		</li>
		<!-- END Language selection -->
	</ul>
</div>