<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page='/WEB-INF/views/menu/menu.jsp'></jsp:include>

<!-- Subhead ================================================== -->
<header id="header">
  <div class="container-fluid">
    <p class="lead">&nbsp;</p>
  </div>
</header>

<div class="row-fluid">
	<div class="span12">
		<div id="breadcrumb">
			<ul class="breadcrumb">
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
									<li id="current">
										<span class="divider">/</span>
										${bc.name}
									</li>
								</c:when>
								<c:otherwise>
									<li>
										<span class="divider">/</span>
										<a href="${bc.value}">
											${bc.name}
										</a>
									</li> 
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>	
	</div>
</div>
