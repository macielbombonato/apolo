<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="br.apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${userCustomField.id}" />

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
				<s:message code="user.custom.field.type" />
			</label>
			<select id="type" name="type" class="input-block-level applyChosen form-control" <c:if test="${readOnly}">disabled="disabled"</c:if> data-placeholder='<s:message code="common.select" />'>
				<c:forEach items="${typeList}" var="type">
					<option value="${type}"
						<c:if test="${type == userCustomField.type}">
							selected="selected"
						</c:if>
					><s:message code="user.custom.field.type.${type}" /></option>
				</c:forEach>
			</select>
		</div>

		<div class="form-group">
			<label for="name" class="control-label">
				<s:message code="user.custom.field.name" />
			</label>
			<input class="form-control" type="text" id="name" name="name" value="${userCustomField.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
		<div class="form-group">
			<label for="type" class="control-label">
				<s:message code="user.custom.field.label" />
			</label>
			<input class="form-control" type="text" id="label" name="label" value="${userCustomField.label}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
	</div>
	
	<div class="tab-pane" id="authorShipTab">
		<jsp:include page='_authorship.jsp'></jsp:include>
	</div>	
</div>
		