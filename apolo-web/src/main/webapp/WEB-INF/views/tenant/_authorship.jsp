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
		<label for="tenant.createdBy.name" class="control-label">
			<s:message code="common.author" />
		</label>
		
		<form:input path="tenant.createdBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			${tenant.createdBy.name}
		</p>
	</div>
	
	<div class="form-group col-sm-6 ">
		<label for="tenant.creationDate" class="control-label">
			<s:message code="common.creationDate" />
		</label>
		
		<form:hidden path="tenant.creationDate" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			<fmt:formatDate value="${tenant.creationDate}" pattern="${datePattern}" />
		</p>
	</div>
	
	<c:if test="${tenant.lastUpdatedBy.name != null}">
		<div class="form-group col-sm-6 " <c:if test="${!readOnly}">style="display:none;"</c:if>>
			<label for="tenant.lastUpdatedBy.name" class="control-label">
				<s:message code="common.lastUpdatedBy" />
			</label>
			
			<form:input path="tenant.lastUpdatedBy.id" cssClass="input-block-level" cssStyle="display:none;" />
			
			<p>
				${tenant.lastUpdatedBy.name}
			</p>
		</div>
	</c:if>
		
	<c:if test="${tenant.lastUpdateDate != null}">
		<div class="form-group col-sm-6 " <c:if test="${!readOnly}">style="display:none;"</c:if>>
			<label for="tenant.lastUpdateDate" class="control-label">
				<s:message code="common.lastUpdateDate" />
			</label>
			
			<form:hidden path="tenant.lastUpdateDate" cssClass="input-block-level" cssStyle="display:none;" />
			
			<p>
				<fmt:formatDate value="${tenant.lastUpdateDate}" pattern="${datePattern}" />
			</p>
		</div>
	</c:if>
</div>