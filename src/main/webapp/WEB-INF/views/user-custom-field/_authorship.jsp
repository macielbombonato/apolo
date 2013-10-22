<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="br.apolo.common.util.InputLength"/>

<div class="form-group ">
	<label for="userCustomField.createdBy.name" class="control-label">
		<s:message code="common.author" />
	</label>
	
	<form:input path="userCustomField.createdBy.id" cssClass="input-block-level" cssStyle="display:none;" />
	
	<p>
		${userCustomField.createdBy.name}
	</p>
</div>

<div class="form-group ">
	<label for="userCustomField.creationDate" class="control-label">
		<s:message code="common.creationDate" />
	</label>
	
	<form:hidden path="userCustomField.creationDate" cssClass="input-block-level" cssStyle="display:none;" />
	
	<p>
		<fmt:formatDate value="${userCustomField.creationDate}" pattern="${datePattern}" />
	</p>
</div>

<c:if test="${userCustomField.lastUpdatedBy.name != null}">
	<div class="form-group " <c:if test="${!readOnly}">style="display:none;"</c:if>>
		<label for="userCustomField.lastUpdatedBy.name" class="control-label">
			<s:message code="common.lastUpdatedBy" />
		</label>
		
		<form:input path="userCustomField.lastUpdatedBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			${userCustomField.lastUpdatedBy.name}
		</p>
	</div>
</c:if>
	
<c:if test="${userCustomField.lastUpdateDate != null}">
	<div class="form-group " <c:if test="${!readOnly}">style="display:none;"</c:if>>
		<label for="userCustomField.lastUpdateDate" class="control-label">
			<s:message code="common.lastUpdateDate" />
		</label>
		
		<form:hidden path="userCustomField.lastUpdateDate" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			<fmt:formatDate value="${userCustomField.lastUpdateDate}" pattern="${datePattern}" />
		</p>
	</div>
</c:if>