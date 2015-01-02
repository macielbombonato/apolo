<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="apolo" uri="/WEB-INF/taglib/apolo.tld" %>

<security:authentication property="systemUser" var="currentUser" />

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${user.id}" />
<input type="hidden" id="status" name="status" value="${user.status}" />
<input type="hidden" id="pictureOriginalName" name="pictureOriginalName" value="${user.pictureOriginalName}" />
<input type="hidden" id="pictureGeneratedName" name="pictureGeneratedName" value="${user.pictureGeneratedName}" />
<input type="hidden" id="tenant.id" name="tenant.id" value="${user.tenant.id}" />
<input type="hidden" id="tenant.name" name="tenant.name" value="${user.tenant.name}" />
<input type="hidden" id="tenant.url" name="tenant.url" value="${user.tenant.url}" />

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
			<label for="skin" class="control-label">
				<s:message code="common.language" />
			</label>
			
			<c:set var="langs" value="<%=apolo.data.enums.Language.values()%>"/>
			
			<div class="row">
				<c:forEach items="${langs}" var="lang">
					<div class="col-sm-3">
						<label class="control-label">
							<input type="radio" 
									id="language" 
									name="language" 
									value="${lang}" 
									<c:if test="${lang eq user.language}">checked="checked"</c:if> 
									<c:if test="${readOnly}">disabled="true"</c:if>
								/>
							<span class="flag-selector">
								<img src='<c:url value="${lang.icon}" />' 
										class="pull-left"
									/>							
							</span>
						</label>
					</div>
				</c:forEach>			
			</div>
		</div>
		
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
			<input type="email" id="email" name="email" class="form-control" value="${user.email}" <c:if test="${readOnly}">readonly="true"</c:if>/>
		</div>
		<div class="form-group">
			<label for="mobile" class="control-label">
				<s:message code="user.mobile" />
			</label>
			<input type="number" id="mobile" name="mobile" class="form-control" value="${user.mobile}" <c:if test="${readOnly}">readonly="true"</c:if>/>
		</div>		
		
		<c:choose>
			<c:when test="${not readOnly}">
				<c:choose>
					<c:when test="${editing}">
						<div class="form-group">
							<label class="control-label">
								<input type="checkbox" id="changePassword" name="changePassword" value="true" onchange="$('#passwordFields').toggle();"/>
								<s:message code="user.change-password.title" />
							</label>
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
					<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_EDIT">
						<label for="name" class="control-label">
							<s:message code="user.groups" />
						</label>
					</security:authorize>

					<select name="groups" id="listTo" size="5" multiple="multiple"  
							<security:authorize ifNotGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_EDIT">style="display:none"</security:authorize>
							<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER_CREATE, ROLE_USER_EDIT">class="form-control chzn-select"</security:authorize>
							<c:if test="${readOnly}">disabled="disabled"</c:if> 
							data-placeholder='<s:message code="common.select" />' >
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
										<a href='<s:url value="/${currentUser.tenant.url}/user-group/view"></s:url>/${group.id}'>
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
		
		<security:authorize ifAnyGranted="ROLE_ADMIN, ROLE_SECOND_FACTOR_MANAGER">
			<div class="form-group">
				<label class="control-label">
					<input type="checkbox" id="requiredSecondFactor" name="requiredSecondFactor" value="true" <c:if test="${user.requiredSecondFactor}">checked="checked"</c:if> <c:if test="${readOnly}">disabled="true"</c:if> />
					<s:message code="user.requiredSecondFactor" />
				</label>
			</div>
		</security:authorize>
		
		<security:authorize ifNotGranted="ROLE_ADMIN, ROLE_SECOND_FACTOR_MANAGER">
			<input type="hidden" id="requiredSecondFactor" name="requiredSecondFactor" value="${user.requiredSecondFactor}" />
		</security:authorize>
		
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
