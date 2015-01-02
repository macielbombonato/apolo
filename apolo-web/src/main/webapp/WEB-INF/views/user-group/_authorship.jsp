<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="apolo.common.util.InputLength"/>

<div class="row">
	<div class="form-group col-sm-6 ">
		<label for="userGroup.createdBy.name" class="control-label">
			<s:message code="common.author" />
		</label>
		
		<form:input path="userGroup.createdBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			${userGroup.createdBy.name}
		</p>
	</div>
	
	<div class="form-group col-sm-6 ">
		<label for="userGroup.creationDate" class="control-label">
			<s:message code="common.creationDate" />
		</label>
		
		<form:hidden path="userGroup.creationDate" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			<fmt:formatDate value="${userGroup.creationDate}" pattern="${datePattern}" />
		</p>
	</div>
	
	<c:if test="${userGroup.lastUpdatedBy.name != null}">
		<div class="form-group col-sm-6 " <c:if test="${!readOnly}">style="display:none;"</c:if>>
			<label for="userGroup.lastUpdatedBy.name" class="control-label">
				<s:message code="common.lastUpdatedBy" />
			</label>
			
			<form:input path="userGroup.lastUpdatedBy.id" cssClass="input-block-level" cssStyle="display:none;" />
			
			<p>
				${userGroup.lastUpdatedBy.name}
			</p>
		</div>
	</c:if>
		
	<c:if test="${userGroup.lastUpdateDate != null}">
		<div class="form-group col-sm-6 " <c:if test="${!readOnly}">style="display:none;"</c:if>>
			<label for="userGroup.lastUpdateDate" class="control-label">
				<s:message code="common.lastUpdateDate" />
			</label>
			
			<form:hidden path="userGroup.lastUpdateDate" cssClass="input-block-level" cssStyle="display:none;" />
			
			<p>
				<fmt:formatDate value="${userGroup.lastUpdateDate}" pattern="${datePattern}" />
			</p>
		</div>
	</c:if>
</div>