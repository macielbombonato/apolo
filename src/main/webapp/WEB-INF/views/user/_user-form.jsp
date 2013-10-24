<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="apolo" uri="/WEB-INF/taglib/apolo.tld" %>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="br.apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${user.id}" />
<input type="hidden" id="status" name="status" value="${user.status}" />
<input type="hidden" id="pictureOriginalName" name="pictureOriginalName" value="${user.pictureOriginalName}" />
<input type="hidden" id="pictureGeneratedName" name="pictureGeneratedName" value="${user.pictureGeneratedName}" />

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
				<s:message code="user.name" />
			</label>
			<input type="text" id="name" name="name" class="form-control" value="${user.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		<div class="form-group">
			<label for="email" class="control-label">
				<s:message code="user.email" />
			</label>
			<input type="text" id="email" name="email" class="form-control" value="${user.email}" <c:if test="${readOnly}">readonly="true"</c:if>/>
		</div>
		
		<c:choose>
			<c:when test="${not readOnly}">
				<c:choose>
					<c:when test="${editing}">
						<div class="form-group">
							<label for="changePassword" class="control-label">
								<s:message code="user.change-password.title" />
							</label>
							<input type="checkbox" id="changePassword" name="changePassword" value="true" onchange="$('#passwordFields').toggle();"/>
						</div>
					</c:when>
					<c:otherwise>
						<input type="checkbox" id="changePassword" name="changePassword" value="true" checked="checked" style="display:none;"/>
					</c:otherwise>
				</c:choose>
				
				<div id="passwordFields" <c:if test="${editing}"> style="display:none;" </c:if>>
					<div class="form-group">
						<label for="password" class="control-label">
							<s:message code="user.password" />
						</label>
						<div class="input-append">
							<input type="password" id="password" name="password" class="form-control"/>
						</div>
					</div>
					
					<div class="form-group">
						<label for="passwordConfirmation" class="control-label">
							<s:message code="user.password.confirmation" />
						</label>
						<div class="input-append">
							<input type="password" id="passwordConfirmation" name="passwordConfirmation" class="form-control"/>
						</div>
					</div>
				</div>
			</c:when>
			<c:when test="${changePassword}">
				<input type="checkbox" id="changePassword" name="changePassword" value="true" checked="checked" style="display:none;"/>
				
				<div class="form-group">
					<label for="password" class="control-label">
						<s:message code="user.password" />
					</label>
					<div class="input-append">
						<input type="password" id="password" name="password" class="form-control"/>
					</div>
				</div>
				
				<div class="form-group">
					<label for="passwordConfirmation" class="control-label">
						<s:message code="user.password.confirmation" />
					</label>
					<input type="password" id="passwordConfirmation" name="passwordConfirmation" class="form-control"/>
				</div>
			</c:when>
		</c:choose>
		
		<c:choose>
			<c:when test="${not readOnly}">
				<div class="form-group">
					<label for="name" class="control-label">
						<s:message code="user.groups" />
					</label>
					<select name="groups" id="listTo" size="5" multiple="multiple" class="input-block-level applyChosen form-control" <c:if test="${readOnly}">disabled="disabled"</c:if> data-placeholder='<s:message code="common.select" />' >
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
				<div class="form-group">
					<div class="panel panel-default">
						<div class="panel-heading">
							<strong>
								<s:message code="user.groups" />
							</strong>
						</div>
						<ul class="list-group">
							<c:forEach items="${user.groups}" var="group">
								<li class="list-group-item">
									<security:authorize  ifAnyGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_VIEW">
										<a href='<s:url value="/user-group/view"></s:url>/${group.id}'>
											${group.name}
										</a>
									</security:authorize>
									
									<security:authorize  ifNotGranted="ROLE_ADMIN, ROLE_USER_LIST, ROLE_USER_PERMISSION_LIST, ROLE_USER_PERMISSION_VIEW">
										${group.name}
									</security:authorize>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${!readOnly}">
			<div class="form-group">
				<label for="picturefiles" class="control-label">
					<s:message code="user.picturefiles" />
				</label>
				<input type="file" class="form-control" name="picturefiles[0]" />
			</div>		
		</c:if>
		
		<apolo:customField user="${user}" fieldList="${customFieldList}" readOnly="${readOnly}" />
		
	</div>
	
	<div class="tab-pane" id="authorShipTab">
		<jsp:include page='_authorship.jsp'></jsp:include>
	</div>
</div>
