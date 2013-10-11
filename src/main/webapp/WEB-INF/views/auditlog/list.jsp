<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<s:message code="common.fullDatePattern" var="datePattern" />

<jsp:include page='_search-form.jsp'></jsp:include>

<br />

<div class="panel panel-primary">
	<div class="panel-heading">
		<strong>
			<s:message code="auditlog.list" />
		</strong>
	</div>
	<center>
		<jsp:include page='../_pagination.jsp'></jsp:include>
	</center>
	<c:choose>
		<c:when test="${auditList != null && not empty auditList}">
			<div class="table-responsive">
				<table class="table table-striped table-hover table-bordered">
					<thead>
						<tr>
							<th>
								#
							</th>
							<th>
								<s:message code="auditlog.transactionType" />
							</th>
							<th>
								<s:message code="auditlog.entityName" />
							</th>
							<th>
								<s:message code="auditlog.registryId" />
							</th>
							<th>
								<s:message code="auditlog.operationDate" />
							</th>
							<th>
								<s:message code="auditlog.executedBy" />
							</th>
						</tr>
					</thead>
					
					<tbody>
						<c:forEach items="${auditList}" var="audit" varStatus="line">
							<tr id="audit_${audit.id}">
								<td>
									${line.count}
								</td>
								<td>
									<s:message code="auditlog.databaseTransactionType.${audit.transactionType}" />
								</td>
								<td>
									${audit.entityName}
								</td>
								<td>
									${audit.registryId}
								</td>
								<td>
									<fmt:formatDate value="${audit.operationDate}" pattern="${datePattern}" />
								</td>
								<td>
									${audit.executedBy.name}
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:when>
		<c:otherwise>
			<div class="panel-body">
				<p>
					<s:message code="common.nodatafound" htmlEscape="false"/>
				</p>
			</div>
		</c:otherwise>
	</c:choose>
	<center>
		<jsp:include page='../_pagination.jsp'></jsp:include>
	</center>
</div>