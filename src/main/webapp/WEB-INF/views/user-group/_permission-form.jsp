<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<input type="hidden" id="id" name="id" value="${userGroup.id}" />

<div class="row">
	<div class="span12">
		<label for="name">
			<s:message code="user.group.name" />
		</label>
		<input class="input-block-level" type="text" id="name" name="name" value="${userGroup.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
	</div>
</div>

<br />

<div class="row">
	<c:choose>
		<c:when test="${not readOnly}">
			<div class="span5">
				<label for="name">
					<s:message code="user.group.permissions.available" />
				</label>
				<select id="listFrom" size="4" multiple="multiple" class="input-block-level">
					<c:forEach items="${permissionList}" var="permission">
						<option value="${permission}">
							<s:message code="user.permission.${permission}" />
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
				<select name="permissions" id="listTo" size="4" multiple="multiple" class="input-block-level" >
					<c:forEach items="${userGroup.permissions}" var="permission">
						<option value="${permission}" selected="selected">
							<s:message code="user.permission.${permission}" />
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