$(document).ready(function() {
	$('#content-location').html($('#page-content').html());

	$('#page-content').remove();

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
	
	// Popover
	$(".applyPopover").popover();
	
	$(".applyPopoverHover").popover({
		'trigger': 'hover' 
	});

	$(".chosen-select").chosen();
	
	$('#formTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});

    if ($(window).width() <= 767) {
        $('#content').removeClass('col-sm-8');
        $('#content').addClass('col-sm-10');

        $('#sidebar').hide();
    }

	if (!App.readOnly) {
		$('.focus').trigger('focus');
	}

});

$.fn.is_on_screen = function(){
	var win = $(window);
	var viewport = {
		top : win.scrollTop(),
		left : win.scrollLeft()
	};
	viewport.right = viewport.left + win.width();
	viewport.bottom = viewport.top + win.height();

	var bounds = this.offset();
	bounds.right = bounds.left + this.outerWidth();
	bounds.bottom = bounds.top + this.outerHeight();

	return (!(viewport.right < bounds.left || viewport.left > bounds.right || viewport.bottom < bounds.top || viewport.top > bounds.bottom));
};

$.fn.invertElement = function() {
	var prop = 'color';

	if (!this.css(prop)) return;

	var color = new RGBColor(this.css(prop));
	if (color.ok) {
		this.css(prop, 'rgb(' + (255 - color.r) + ',' + (255 - color.g) + ',' + (255 - color.b) + ')');
	}
};

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

function removeRedirectionConfirmationDialogOpen(url, objectId) {
	$('#removeRedirectionConfirmationObjectId').val(objectId);
	$('#removeRedirectionConfirmationUrl').val(url);
	$('#removeRedirectionConfirmationDialog').modal('toggle');
	
	$('#btnCallRemove').attr('href', $('#removeRedirectionConfirmationUrl').val());
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