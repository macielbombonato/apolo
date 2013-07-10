<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="br.apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${user.id}" />

<div class="row-fluid">
	<div class="span12">
		<label for="name">
			<s:message code="user.name" />
		</label>
		<input type="text" id="name" name="name" class="input-block-level" value="${user.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
		<label for="email">
			<s:message code="user.email" />
		</label>
		<input type="text" id="email" name="email" class="input-block-level" value="${user.email}" <c:if test="${readOnly}">readonly="true"</c:if>/>
	</div>
</div>

<c:choose>
	<c:when test="${not readOnly}">
		<c:choose>
			<c:when test="${editing}">
				<div class="row-fluid">
					<div class="span12 inline">
						<label for="changePassword">
							<s:message code="user.change-password.title" />
						</label>
						<input type="checkbox" id="changePassword" name="changePassword" value="true" onchange="$('#passwordFields').toggle();"/>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<input type="checkbox" id="changePassword" name="changePassword" value="true" checked="checked" style="display:none;"/>
			</c:otherwise>
		</c:choose>
		
		<div id="passwordFields" <c:if test="${editing}"> style="display:none;" </c:if>>
			<div class="row-fluid">
				<div class="span12">
					<label for="password">
						<s:message code="user.password" />
					</label>
					<div class="input-append">
						<input type="password" id="password" name="password" />
						<div class="switch switch-small" 
								data-on="danger" 
								data-off="success"
								data-on-label="<i class='icon-eye-open'></i>" 
								data-off-label="<i class='icon-eye-close'></i>"
							>
							<input type="checkbox" id="showPassword"/>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row-fluid">
				<div class="span12">
					<label for="passwordConfirmation">
						<s:message code="user.password.confirmation" />
					</label>
					<input type="password" id="passwordConfirmation" name="passwordConfirmation" />
				</div>
			</div>
		</div>
	</c:when>
	<c:when test="${changePassword}">
		<input type="checkbox" id="changePassword" name="changePassword" value="true" checked="checked" style="display:none;"/>
		
		<div class="row-fluid">
			<div class="span12">
				<label for="password">
					<s:message code="user.password" />
				</label>
				<div class="input-append">
					<input type="password" id="password" name="password" />
					<div class="switch switch-small" 
							data-on="danger" 
							data-off="success"
							data-on-label="<i class='icon-eye-open'></i>" 
							data-off-label="<i class='icon-eye-close'></i>"
						>
						<input type="checkbox" id="showPassword"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row-fluid">
			<div class="span12">
				<label for="passwordConfirmation">
					<s:message code="user.password.confirmation" />
				</label>
				<input type="password" id="passwordConfirmation" name="passwordConfirmation" />
			</div>
		</div>
	</c:when>
</c:choose>

<div class="row-fluid">

</div>

<div class="row-fluid">
	<c:choose>
		<c:when test="${not readOnly}">
			<div class="span12">
				<label for="name">
					<s:message code="user.groups" />
				</label>
				<select name="groups" id="listTo" size="5" multiple="multiple" class="input-block-level applyChosen" <c:if test="${readOnly}">disabled="disabled"</c:if> data-placeholder='<s:message code="common.select" />' >
					<c:forEach items="${groupList}" var="group">
						<option value="${group.id}" 
							<c:forEach items="${user.groups}" var="userGroup">
								<c:if test="${group == userGroup}">
									selected="selected"
								</c:if>
							</c:forEach>						
						>${group.name}</option>
					</c:forEach>
				</select>
			</div>
		</c:when>
		<c:otherwise>
			<div class="span5">
				<table class="table table-striped table-hover table-bordered">
					<caption>
						<strong>
							<s:message code="user.groups" />
						</strong>
					</caption>
					<tbody>
						<c:forEach items="${user.groups}" var="group">
							<tr>
								<td>
									<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_VIEW">
										<a href='<s:url value="/user-group/view"></s:url>/${group.id}'>
											${group.name}
										</a>
									</security:authorize>
									
									<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_VIEW">
										${group.name}
									</security:authorize>
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
		<label for="user.createdBy.name">
			<s:message code="common.author" />
		</label>
		<form:input path="user.createdBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		<form:input path="user.createdBy.name" cssClass="input-block-level" readonly="true" />
	</div>
	<div class="span6">
		<label for="user.creationDate">
			<s:message code="common.creationDate" />
		</label>
		<input 
				type="text" 
				id="creationDate" 
				name="creationDate" 
				class="input-block-level" 
				value="<fmt:formatDate value="${user.creationDate}" 
				pattern="${datePattern}" />" 
				readonly="readonly"
			/>
	</div>
</div>

<div class="row-fluid" <c:if test="${!readOnly}">style="display:none;"</c:if>>
	<div class="span6">
		<label for="user.lastUpdatedBy.name">
			<s:message code="common.lastUpdatedBy" />
		</label>
		<form:input path="user.lastUpdatedBy.id" cssClass="input-block-level" cssStyle="display:none;" />
		<form:input path="user.lastUpdatedBy.name" cssClass="input-block-level" readonly="true"/>
	</div>
	<div class="span6">
		<label for="user.lastUpdateDate">
			<s:message code="common.lastUpdateDate" />
		</label>
		<input 
				type="text" 
				id="lastUpdateDate" 
				name="lastUpdateDate" 
				class="input-block-level" 
				value="<fmt:formatDate value="${user.lastUpdateDate}" 
				pattern="${datePattern}" />" 
				readonly="readonly"
			/>
	</div>
</div>