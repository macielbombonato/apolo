<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div id="removeConfirmationDialog" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4>
					<s:message code="common.remove" />
				</h4>
			</div>
			
			<div class="modal-body">
				<p>
					<s:message code="common.remove.msg" htmlEscape="false"/>
				</p>
				<input type="hidden" id="removeConfirmationUrl" />
				<input type="hidden" id="removeConfirmationObjectId" />
			</div>
			
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">
					<i class="glyphicon glyphicon-remove-circle"></i>
					<s:message code="common.cancel" />
				</a>
				<a href="#" class="btn btn-danger" onclick="callRemove();">
					<i class="glyphicon glyphicon-remove"></i>
					<s:message code="common.remove" />
				</a>
			</div>
		</div>
	</div>
</div>

<div id="removeMsgDialog" class="modal fade" tabindex="-1" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4>
					<s:message code="common.remove" />
				</h4>
			</div>
			
			<div class="modal-body">
				<p id="removeMsg"></p>
			</div>
			
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">
					<i class="glyphicon glyphicon-check"></i>
					<s:message code="common.ok" />
				</a>
			</div>
		</div>
	</div>
</div>


<div id="removeRedirectionConfirmationDialog" class="modal hide fade" tabindex="-1" data-role="dialog">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>
			<s:message code="common.remove" />
		</h4>
	</div>
	
	<div class="modal-body">
		<p>
			<s:message code="common.remove.msg" htmlEscape="false"/>
		</p>
		<input type="hidden" id="removeRedirectionConfirmationUrl" />
		<input type="hidden" id="removeRedirectionConfirmationObjectId" />
	</div>
	
	<div class="modal-footer">
		<a href="#" class="btn" data-dismiss="modal">
			<s:message code="common.cancel" />
		</a>
		<a id="btnCallRemove" href="#" class="btn btn-danger">
			<s:message code="common.remove" />
		</a>
	</div>
</div>