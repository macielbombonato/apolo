<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="br.apolo.common.util.InputLength"/>

<div class="form-group " <c:if test="${!readOnly}">style="display:none;"</c:if>>
	<label for="user.createdBy.name" class="control-label">
		<s:message code="common.author" />
	</label>
	<form:input path="user.createdBy.id" cssClass="form-control" cssStyle="display:none;" />
	<p>
		${user.createdBy.name}
	</p>
</div>

<div class="form-group " <c:if test="${!readOnly}">style="display:none;"</c:if>>
	<label for="user.creationDate" class="control-label">
		<s:message code="common.creationDate" />
	</label>
	<p>
		<fmt:formatDate value="${user.creationDate}" pattern="${datePattern}" />
	</p>
</div>

<c:if test="${user.lastUpdatedBy.name != null}">
	<div class="form-group " <c:if test="${!readOnly}">style="display:none;"</c:if>>
		<label for="user.lastUpdatedBy.name" class="control-label">
			<s:message code="common.lastUpdatedBy" />
		</label>
		<form:input path="user.lastUpdatedBy.id" cssClass="form-control" cssStyle="display:none;" />
		<p>
			${user.lastUpdatedBy.name}
		</p>
	</div>
</c:if>

<c:if test="${user.lastUpdateDate != null}">
	<div class="form-group " <c:if test="${!readOnly}">style="display:none;"</c:if>>
		<label for="user.lastUpdateDate" class="control-label">
			<s:message code="common.lastUpdateDate" />
		</label>
		<p>
			<fmt:formatDate value="${user.lastUpdateDate}" pattern="${datePattern}" />
		</p>
	</div>
</c:if>