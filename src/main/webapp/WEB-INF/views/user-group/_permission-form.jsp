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
				<s:message code="user.group.name" />
			</label>
			<input class="form-control" type="text" id="name" name="name" value="${userGroup.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
		<c:choose>
			<c:when test="${not readOnly}">
				<div class="form-group">
					<label for="name" class="control-label">
						<s:message code="user.group.permissions.selected" />
					</label>
					<select name="permissions" id="listTo" size="5" multiple="multiple" class="input-block-level applyChosen form-control" <c:if test="${readOnly}">disabled="disabled"</c:if> data-placeholder='<s:message code="common.select" />'>
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
				<div class="form-group">
					<div class="panel panel-default">
						<div class="panel-heading">
							<strong>
								<s:message code="user.group.permissions.selected" />
							</strong>
						</div>
						<ul class="list-group">
							<c:forEach items="${userGroup.permissions}" var="permission">
								<li class="list-group-item">
									<s:message code="user.permission.${permission}" />
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="tab-pane" id="authorShipTab">
		<jsp:include page='_authorship.jsp'></jsp:include>
	</div>	
</div>
		