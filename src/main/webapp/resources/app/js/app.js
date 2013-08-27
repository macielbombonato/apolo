$(document).ready(function() {
	$('#showPassword').change(function(){
		var passwordText = $('#password');
		if($(this).attr('checked') == 'checked'){
			passwordText.get(0).type = 'text';
		} else {
			passwordText.get(0).type = 'password';
		}
		
		var passwordConfirmationText = $('#passwordConfirmation');
		if($(this).attr('checked') == 'checked'){
			passwordConfirmationText.get(0).type = 'text';
		} else {
			passwordConfirmationText.get(0).type = 'password';
		}
	});
	
	$('button[type=submit]').click(function(e) {
		$('#loadingDialog').modal('toggle');
	});
	
	$('#loadingDialog').modal({
		backdrop: 'static',
		keyboard: false,
		show: false
	});
	
    $('a.back').click(function(){
        parent.history.back();
        return false;
    });
	
	$('#editData').modal({
		backdrop: 'static',
		keyboard: false,
		show: false
	});
	
	$( ".btn" ).tooltip({
		'selector': '',
		'placement': 'top'
	});
	
	// Tooltip
	$( ".tooltipLeft" ).tooltip({
		'selector': '',
		'placement': 'left'
	});
	
	$( ".tooltipRight" ).tooltip({
		'selector': '',
		'placement': 'right'
	});
	
	$( ".tooltipBottom" ).tooltip({
		'selector': '',
		'placement': 'bottom'
	});
	
	$( ".tooltipTop" ).tooltip({
		'selector': '',
		'placement': 'top'
	});
	
	// set the defaults for the datepicker
	jQuery(function($){
		$.datepicker.regional['pt-BR'] = {
		closeText: 'Fechar',
		prevText: '&#x3c;Anterior',
		nextText: 'Pr&oacute;ximo&#x3e;',
		currentText: 'Hoje',
		monthNames: ['Janeiro','Fevereiro','Mar&ccedil;o','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
		dayNames: ['Domingo','Segunda-feira','Ter&ccedil;a-feira','Quarta-feira','Quinta-feira','Sexta-feira','S&aacute;bado'],
		dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','S&aacute;b'],
		dayNamesMin: ['Dom','Seg','Ter','Qua','Qui','Sex','S&aacute;b'],
		weekHeader: 'Sm',
		dateFormat: 'dd/mm/yy',
		firstDay: 0,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
		$.datepicker.setDefaults($.datepicker.regional['pt-BR']);
	});
	
	$('.applyDatePicker').each(function(){
		$(this).datepicker({
			changeMonth: true,
			changeYear: true
		});
	});
	
	$(".applyTagit").tagit({
		allowSpaces : true, 
		readOnly : App.readOnly
	});
	
	$('.tagit').each(function(){
		$(this).css('margin-left', '0px');
	});
	
	$(".applyChosen").chosen({
		allow_single_deselect:true
	});
	
	$(".applyCleditor").cleditor({
		width: '99%',
		height: '80%',
		disabled: App.readOnly
	});
	
	$(".ellipsis").ellipsis({
		row: 10, 
		char: '...'
	});
	
	$('#formTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});
});

$('#header').height($('#menu').height() + 5);

$('video').mediaelementplayer({
	success: function(media, node, player) {
		$('#' + node.id + '-mode').html('mode: ' + media.pluginType);
	}
});

if (!App.readOnly) {
	$('.focus').trigger('focus');
}

function editDataOpen(fieldName) {
	$('#dataInput').val($('#'+fieldName).val());
	$('#editingFieldName').val(fieldName);
	$('#editData').modal('toggle');
} 

function editData() {
	$('#'+$('#editingFieldName').val()).val($('#dataInput').val());
	$('#label_'+$('#editingFieldName').val()).text($('#dataInput').val());
	$('#dataInput').val('');
	$('#editingFieldName').val('');
	$('#editData').modal('toggle');
}

function removeConfirmationDialogOpen(url, objectId) {
	$('#removeConfirmationObjectId').val(objectId);
	$('#removeConfirmationUrl').val(url);
	$('#removeConfirmationDialog').modal('toggle');
} 

function callRemove() {
	$.ajax({
		type : "GET",
		url : $('#removeConfirmationUrl').val(),
		beforeSend : function(xhr) {
			$('#loadingDialog').modal('toggle');
		},
		success : function(response) {
			var json = $.parseJSON(response);
			
			$('#removeMsg').text(json.result.message);
			
			if (json.result.success) {
				$('#'+$('#removeConfirmationObjectId').val()).remove();		
			}
			
			$('#loadingDialog').modal('toggle');
			$('#removeConfirmationDialog').modal('toggle');
			$('#removeMsgDialog').modal('toggle');
		},
		complete : function() {
			$('#removeConfirmationObjectId').val('');
			$('#removeConfirmationUrl').val('');
		}
	});
}