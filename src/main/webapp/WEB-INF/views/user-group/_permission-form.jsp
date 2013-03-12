<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="br.apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${userGroup.id}" />

<div class="row-fluid">
	<div class="span12">
		<label for="name">
			<s:message code="user.group.name" />
		</label>
		<input class="input-block-level" type="text" id="name" name="name" value="${userGroup.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
	</div>
</div>

<br />

<div class="row-fluid">
	<c:choose>
		<c:when test="${not readOnly}">
			<div class="span12">
				<label for="name">
					<s:message code="user.group.permissions.selected" />
				</label>
				<select name="permissions" id="listTo" size="5" multiple="multiple" class="input-block-level" <c:if test="${readOnly}">disabled="disabled"</c:if> data-rel="chosen" data-placeholder='<s:message code="common.select" />'>
					<c:forEach items="${permissionList}" var="permission">
						<option value="${permission}"
							<c:forEach items="${userGroup.permissions}" var="groupPermission">
								<c:if test="${permission == groupPermission}">
									selected="selected"
								</c:if>
							</c:forEach>						
						><s:message code="user.permission.${permission}" /></option>
					</c:forEach>
				</select>
			</div>
		</c:when>
		<c:otherwise>
			<div class="span5">
				<table class="table table-striped table-hover table-bordered">
					<caption>
						<strong>
							<s:message code="user.group.permissions.selected" />
						</strong>
					</caption>
					<tbody>
						<c:forEach items="${userGroup.permissions}" var="permission">
							<tr>
								<td>
									<s:message code="user.permission.${permission}" />
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<div class="row-fluid" <c:if test="${!readOnly}">style="display:none;"</c:if>>
	<div class="span6">
		<label for="userGroup.createdBy.name">
			<s:message code="common.author" />
		</label>
		<form:input path="userGroup.createdBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		<form:input path="userGroup.createdBy.name" cssClass="input-block-level" readonly="true" />
	</div>
	<div class="span6">
		<label for="userGroup.creationDate">
			<s:message code="common.creationDate" />
		</label>
		<input 
				type="text" 
				id="creationDate" 
				name="creationDate" 
				class="input-block-level" 
				value="<fmt:formatDate value="${userGroup.creationDate}" 
				pattern="${datePattern}" />" 
				readonly="readonly"
			/>
	</div>
</div>

<div class="row-fluid" <c:if test="${!readOnly}">style="display:none;"</c:if>>
	<div class="span6">
		<label for="userGroup.lastUpdatedBy.name">
			<s:message code="common.lastUpdatedBy" />
		</label>
		<form:input path="userGroup.lastUpdatedBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		<form:input path="userGroup.lastUpdatedBy.name" cssClass="input-block-level" readonly="true"/>
	</div>
	<div class="span6">
		<label for="userGroup.lastUpdateDate">
			<s:message code="common.lastUpdateDate" />
		</label>
		<input 
				type="text" 
				id="lastUpdateDate" 
				name="lastUpdateDate" 
				class="input-block-level" 
				value="<fmt:formatDate value="${userGroup.lastUpdateDate}" 
				pattern="${datePattern}" />" 
				readonly="readonly"
			/>
	</div>
</div>