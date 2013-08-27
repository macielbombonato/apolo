<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page='/WEB-INF/views/menu/menu.jsp'></jsp:include>

<!-- Subhead ================================================== -->
<header id="header">
  <div class="container">
    <p class="lead">&nbsp;</p>
  </div>
</header>

<ol id="breadcrumb" class="breadcrumb">
	<c:choose>
		<c:when test="${breadcrumb == null || breadcrumb.tree == null || empty breadcrumb.tree}">
			<li>
				<a id="first" href="<s:url value="/"></s:url>">
					<s:message code="breadcrumb.home" />
				</a>
			</li>
		</c:when>
		<c:otherwise>
			<c:forEach var="bc" items="${breadcrumb.tree}" varStatus="status">
				<c:choose>
					<c:when test="${status.index==0}">
						<li>
							<a id="first" href="<s:url value="/"></s:url>">
								<s:message code="breadcrumb.home" />
							</a>
						</li>
					</c:when>
					<c:when test="${status.index == fn:length(breadcrumb.tree)-1 && status.index!=0}">
						<li id="current" class="active">
							${bc.name}
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<a href="${bc.value}">
								${bc.name}
							</a>
						</li> 
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	<li class="pull-right">
		<s:message code="version" var="version"/>
		
		<small class="text-right muted tooltipLeft" data-toggle="tooltip" title="<s:message code="version"/>">
			<s:message code="system.version" />:&nbsp; ${fn:substring(version, 0, 15)}
		</small>
	</li>
</ol>
