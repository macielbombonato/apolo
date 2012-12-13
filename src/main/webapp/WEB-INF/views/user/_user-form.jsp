<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<input type="hidden" id="id" name="id" value="${user.id}" />

<div class="row">
	<div class="span12">
		<label for="name">
			<s:message code="user.username" />
		</label>
		<input type="text" id="name" name="name" class="input-block-level" value="${user.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
	</div>
</div>
<div class="row">
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
				<div class="row">
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
			<div class="row">
				<div class="span12">
					<label for="password">
						<s:message code="user.password" />
					</label>
					<input type="password" id="password" name="password" />
				</div>
			</div>
		</div>
	</c:when>
	<c:when test="${changePassword}">
		<input type="checkbox" id="changePassword" name="changePassword" value="true" checked="checked" style="display:none;"/>
		
		<div class="row">
			<div class="span12">
				<label for="password">
					<s:message code="user.password" />
				</label>
				<input type="password" id="password" name="password" />
			</div>
		</div>
	</c:when>
</c:choose>

<div class="row">

</div>

<div class="row">
	<c:choose>
		<c:when test="${not readOnly}">
			<div class="span5">
				<label for="name">
					<s:message code="user.group.permissions.available" />
				</label>
				<select id="listFrom" size="4" multiple="multiple" class="input-block-level">
					<c:forEach items="${groupList}" var="group">
						<option value="${group.id}">
							${group.name}
						</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span2">
				<button id="btnAdd" type="button" class="btn btn-secondary btn-block" >
					<i class="icon-step-forward"></i>
				</button>
				<button id="btnRemove" type="button" class="btn btn-secondary btn-block">
					<i class="icon-step-backward"></i> 
				</button>
				<button id="btnAddAll" type="button" class="btn btn-secondary btn-block">
					<i class="icon-fast-forward"></i>
				</button>
				<button id="btnRemoveAll" type="button" class="btn btn-secondary btn-block">
					<i class="icon-fast-backward"></i>
				</button>
			</div>
			
			<div class="span5">
				<label for="name">
					<s:message code="user.group.permissions.selected" />
				</label>
				<select name="groups" id="listTo" size="4" multiple="multiple" class="input-block-level" >
					<c:forEach items="${user.groups}" var="group">
						<option value="${group.id}" selected="selected">
							${group.name}
						</option>
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