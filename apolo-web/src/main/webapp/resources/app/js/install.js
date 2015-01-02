function submitPage(){
	$('#systemForm').attr('action', $('#systemForm').attr('action'));
	$('#systemForm').attr('method', 'POST');
	$('#systemForm').attr('target', '_self');
	$('#systemForm').submit();
}