<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:url var="firstUrl" value="/user-group/list/1" />
<c:url var="lastUrl" value="/user-group/list/${page.totalPages}" />
<c:url var="prevUrl" value="/user-group/list/${currentIndex - 1}" />
<c:url var="nextUrl" value="/user-group/list/${currentIndex + 1}" />

<ul class="pagination pagination-sm">
	<c:choose>
		<c:when test="${currentIndex == 1}">
			<li class="disabled"><a href="#">&lt;&lt;</a></li>
			<li class="disabled"><a href="#">&lt;</a></li>
		</c:when>
		<c:otherwise>
			<li><a href="${firstUrl}">&lt;&lt;</a></li>
			<li><a href="${prevUrl}">&lt;</a></li>
		</c:otherwise>
	</c:choose>
	<c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
		<c:url var="pageUrl" value="/user-group/list/${i}" />
		<c:choose>
			<c:when test="${i == currentIndex}">
				<li class="active"><a href="${pageUrl}"><c:out value="${i}" /></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<c:choose>
		<c:when test="${currentIndex == page.totalPages}">
			<li class="disabled"><a href="#">&gt;</a></li>
			<li class="disabled"><a href="#">&gt;&gt;</a></li>
		</c:when>
		<c:otherwise>
			<li><a href="${nextUrl}">&gt;</a></li>
			<li><a href="${lastUrl}">&gt;&gt;</a></li>
		</c:otherwise>
	</c:choose>
</ul>