$(document).ready(function() {
	$('button[type=submit]').click(function(e) {
		$('#loadingDialog').modal('toggle');
	});
});

$('#header').height($('#menu').height());