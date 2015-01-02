<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.datePattern" var="datePattern" />

<jsp:useBean id="inputlength" class="apolo.common.util.InputLength"/>

<input type="hidden" id="id" name="id" value="${tenant.id}" />
<input type="hidden" id="status" name="status" value="${tenant.status}" />
<input type="hidden" id="logo" name="logo" value="${tenant.logo}" />

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
				<s:message code="tenant.skin" />
			</label>
			
			<div class="row">
				<c:forEach items="${skinList}" var="skin">
					<div class="col-sm-3">
						<label class="control-label">
							<input type="radio" 
									id="skin" 
									name="skin" 
									value="${skin}" 
									<c:if test="${skin eq tenant.skin}">checked="checked"</c:if> 
									<c:if test="${readOnly}">disabled="true"</c:if> 
								/>
							<span class="theme-color" style="background:${skin.color}"></span>
						</label>
					</div>
				</c:forEach>			
			</div>
		</div>
		
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
									<c:if test="${lang eq tenant.language}">checked="checked"</c:if> 
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
				<s:message code="tenant.name" />
			</label>
			<input class="form-control" type="text" id="name" name="name" value="${tenant.name}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
		<div class="form-group">
			<label for="url" class="control-label">
				<s:message code="tenant.url" />
			</label>
			<input class="form-control" type="text" id="url" name="url" value="${tenant.url}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
		<div class="form-group">
			<label for="logoWidth" class="control-label">
				<s:message code="tenant.logoWidth" />
			</label>
			<input class="form-control" type="number" id="logoWidth" name="logoWidth" value="${tenant.logoWidth}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
		<div class="form-group">
			<label for="logoHeight" class="control-label">
				<s:message code="tenant.logoHeight" />
			</label>
			<input class="form-control" type="number" id="logoHeight" name="logoHeight" value="${tenant.logoHeight}" <c:if test="${readOnly}">readonly="true"</c:if> />
		</div>
		
		<c:if test="${!readOnly}">
			<div class="form-group">
				<label for="logoFile" class="control-label">
					<s:message code="tenant.logo" />
				</label>
				<input type="file" class="form-control" name="logoFile[0]" />
			</div>		
		</c:if>
	</div>
	
	<div class="tab-pane" id="authorShipTab">
		<jsp:include page='_authorship.jsp'></jsp:include>
	</div>	
</div>
		