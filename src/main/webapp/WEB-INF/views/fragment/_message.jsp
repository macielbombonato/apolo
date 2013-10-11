<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${error || warn || msg}">
	<div class="row ">
		<div class="span12">
			<c:choose>
				<c:when test="${error}">
					<div class="alert alert-danger">
						${message}
					</div>		
				</c:when>
				<c:when test="${warn}">
					<div class="alert alert-warn">
						${message}
					</div>		
				</c:when>
				<c:when test="${msg}">
					<div class="alert alert-info">
						${message}
					</div>		
				</c:when>
			</c:choose>
		</div>
	</div>
</c:if>