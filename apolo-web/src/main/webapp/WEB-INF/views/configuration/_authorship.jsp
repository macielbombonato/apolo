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
		<label for="configuration.createdBy.name" class="control-label">
			<s:message code="common.author" />
		</label>
		
		<form:input path="configuration.createdBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			${configuration.createdBy.name}
		</p>
	</div>
	
	<div class="form-group col-sm-6 ">
		<label for="configuration.creationDate" class="control-label">
			<s:message code="common.creationDate" />
		</label>
		
		<form:hidden path="configuration.creationDate" cssClass="input-block-level" cssStyle="display:none;" />
		
		<p>
			<fmt:formatDate value="${configuration.creationDate}" pattern="${datePattern}" />
		</p>
	</div>
	
	<c:if test="${configuration.lastUpdatedBy.name != null}">
		<div class="form-group col-sm-6 " <c:if test="${!readOnly}">style="display:none;"</c:if>>
			<label for="configuration.lastUpdatedBy.name" class="control-label">
				<s:message code="common.lastUpdatedBy" />
			</label>
			
			<form:input path="configuration.lastUpdatedBy.id" cssClass="input-block-level" cssStyle="display:none;" />
			
			<p>
				${configuration.lastUpdatedBy.name}
			</p>
		</div>
	</c:if>
		
	<c:if test="${configuration.lastUpdateDate != null}">
		<div class="form-group col-sm-6 " <c:if test="${!readOnly}">style="display:none;"</c:if>>
			<label for="configuration.lastUpdateDate" class="control-label">
				<s:message code="common.lastUpdateDate" />
			</label>
			
			<form:hidden path="configuration.lastUpdateDate" cssClass="input-block-level" cssStyle="display:none;" />
			
			<p>
				<fmt:formatDate value="${configuration.lastUpdateDate}" pattern="${datePattern}" />
			</p>
		</div>
	</c:if>
</div>