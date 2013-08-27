$('#loginDialog').modal({
	backdrop: 'static',
	keyboard: false,
	show: false
});

$('#loginDialog').click(function(){
	$('#loginDialog').modal('show');
});