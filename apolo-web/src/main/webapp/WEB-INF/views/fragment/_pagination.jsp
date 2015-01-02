<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:url var="firstUrl" value="${url}/1" />
<c:url var="lastUrl" value="${url}/${page.totalPages}" />
<c:url var="prevUrl" value="${url}/${currentIndex - 1}" />
<c:url var="nextUrl" value="${url}/${currentIndex + 1}" />

<c:if test="${page != null && page.content != null}">
	<ul class="pagination pagination-sm">
		<c:choose>
			<c:when test="${currentIndex == 1}">
				<li class="disabled">
					<a href="#">
						<i class="glyphicon glyphicon-fast-backward"></i>
					</a>
				</li>
				<li class="disabled">
					<a href="#">
						<i class="glyphicon glyphicon-step-backward"></i>
					</a>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<a href="${firstUrl}">
						<i class="glyphicon glyphicon-fast-backward"></i>
					</a>
				</li>
				<li>
					<a href="${prevUrl}">
						<i class="glyphicon glyphicon-step-backward"></i>
					</a>
				</li>
			</c:otherwise>
		</c:choose>
		<c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
			<c:url var="pageUrl" value="${url}/${i}" />
			<c:choose>
				<c:when test="${i == currentIndex}">
					<li class="active">
						<a href="${pageUrl}">
							<c:out value="${i}" />
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<a href="${pageUrl}">
							<c:out value="${i}" />
						</a>
					</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${currentIndex == page.totalPages}">
				<li class="disabled">
					<a href="#">
						<i class="glyphicon glyphicon-step-forward"></i>
					</a>
				</li>
				<li class="disabled">
					<a href="#">
						<i class="glyphicon glyphicon-fast-forward"></i>
					</a>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<a href="${nextUrl}">
						<i class="glyphicon glyphicon-step-forward"></i>
					</a>
				</li>
				<li>
					<a href="${lastUrl}">
						<i class="glyphicon glyphicon-fast-forward"></i>
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</c:if>