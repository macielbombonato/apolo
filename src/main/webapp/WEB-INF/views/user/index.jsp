<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<div class="row">
	<div class="col-sm-4">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>
					<s:message code="user.index.title" />
				</strong>
			</div>
			<div class="panel-body">
				<center>
					<c:choose>
						<c:when test="${user.pictureGeneratedName != null}">
							<img class="img-thumbnail" src="<s:url value="/uploadedfiles/User"></s:url>/${user.id}/${user.pictureGeneratedName}" />
						</c:when>
						<c:otherwise>
							<h1>
								<span class="glyphicon glyphicon-user"> </span>
							</h1>
						</c:otherwise>
					</c:choose>					
				</center>
				<br />
				<br />
				<jsp:include page='_user-form.jsp'></jsp:include>
			</div>
		</div>
	</div>
</div>