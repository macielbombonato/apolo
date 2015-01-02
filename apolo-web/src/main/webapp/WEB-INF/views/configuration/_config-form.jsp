<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${configuration.id}" />
<input type="hidden" id="tenant.id" name="tenant.id" value="${configuration.tenant.id}" />
<input type="hidden" id="tenant.name" name="tenant.name" value="${configuration.tenant.name}" />
<input type="hidden" id="tenant.url" name="tenant.url" value="${configuration.tenant.url}" />

<ul class="nav nav-tabs" id="formTab">
	<li class="active">
		<a href="#home">
			<span class="glyphicon glyphicon-tasks"></span>
		</a>
	</li>
	<c:if test="${readOnly}">
		<li>
			<a href="#authorShipTab">
				<span class="glyphicon glyphicon-user"></span>
			</a>
		</li>
	</c:if>
</ul>

<div class="tab-content">
	<div class="tab-pane active" id="home">

		<div class="form-group">
			<label for="name" class="control-label">
				<s:message code="configuration.field" />
			</label>
			
			<c:set var="useTLS" value="<%=apolo.data.enums.ConfigField.EMAIL_USE_TLS%>"/>
			
			<select id="field" 
						name="field" 
						class="form-control chzn-select" 
						<c:if test="${readOnly}">disabled="disabled"</c:if> 
						data-placeholder='<s:message code="common.select" />' 
						onchange="if ($('#field').val() == 'EMAIL_USE_TLS') {$('#value').val('true');}"
					>
				<c:forEach items="${configFieldList}" var="field">
					<option value="${field}"
						<c:if test="${field == configuration.field}">
							selected="selected"
						</c:if>
					><s:message code="configuration.field.${field}" /></option>
				</c:forEach>
			</select>
		</div>

		<div class="form-group">
			<label for="value" class="control-label">
				<s:message code="configuration.value" />
			</label>
			<input class="form-control" 
					<c:if test="${configuration.field != null}"> 
						<c:choose>
							<c:when test="${configuration.field.cryptedValue}">
								type="password"
								
								value="${configuration.value}" 
							</c:when>
							<c:otherwise>
								type="text"
								
								value="${configuration.value}" 
							</c:otherwise>
						</c:choose>					
					</c:if>
					
					id="value" 
					name="value" 
					
					<c:if test="${readOnly}">readonly="true"</c:if> 
				/>
		</div>
	</div>
	
	<div class="tab-pane" id="authorShipTab">
		<jsp:include page='_authorship.jsp'></jsp:include>
	</div>	
</div>
		