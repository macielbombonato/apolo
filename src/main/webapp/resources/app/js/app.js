$(document).ready(function() {
	$('button[type=submit]').click(function(e) {
		$('#loadingDialog').modal('toggle');
	});
	
	$('#loadingDialog').modal({
		keyboard: false,
		show: false
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
	
	$(".applyChosen").chosen({
		allow_single_deselect:true
	});
	
	$(".applyCleditor").cleditor({
		width: '99%',
		height: '80%',
		disabled: App.readOnly
	});
});

$('#header').height($('#menu').height() + 10);

if (!App.readOnly) {
	$('.focus').trigger('focus');
}
