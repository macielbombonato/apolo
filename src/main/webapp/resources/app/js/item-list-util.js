$(document).ready(function() {
	$('#btnAdd').click(function(e) {
		$('#listFrom > option:selected').appendTo('#listTo');
		e.preventDefault();
	});

	$('#btnAddAll').click(function(e) {
		$('#listFrom > option').appendTo('#listTo');
		e.preventDefault();
	});

	$('#btnRemove').click(function(e) {
		$('#listTo > option:selected').appendTo('#listFrom');
		e.preventDefault();
	});

	$('#btnRemoveAll').click(function(e) {
		$('#listTo > option').appendTo('#listFrom');
		e.preventDefault();
	});
	
	$('button[type=submit]').click(function(e) {
		$('#listTo > option').attr('selected', 'selected');
	});

});