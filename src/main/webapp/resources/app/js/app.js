$(document).ready(function() {
	$('button[type=submit]').click(function(e) {
		$('#loadingDialog').modal('toggle');
	});
	
	$('#loadingDialog').modal({
		keyboard: false,
		show: false
	});
});

$('#header').height($('#menu').height() + 10);

if (!App.readOnly) {
	$('.focus').trigger('focus');
}
