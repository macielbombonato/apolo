$(document).ready(function() {
	$('body').data('spy', 'scroll');
	$('body').data('target', '#scrollspy');

	$('.select2').each(function() {
		var $this = $(this);
		var width = $this.attr('data-select-width') || '100%';
		//, _showSearchInput = $this.attr('data-select-search') === 'true';
		$this.select2({
			//showSearchInput : _showSearchInput,
			allowClear : true,
			width : width
		});
	});

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

	$('#formTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});

	if (!App.readOnly) {
		$('.focus').trigger('focus');
	}

	if (!$('.ad-desktop').is(':visible')) {
		$('.ad-desktop').remove();
	}

	if (!$('.ad-mobile').is(':visible')) {
		$('.ad-mobile').remove();
	}

	$('.google-ad#ad-two').html(
		'<script async="async" src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script> ' +
		'<ins class="adsbygoogle" ' +
		'style="display:inline-block;width:' + $('#ad-two').data('google-ad-width') + 'px;height:' + $('#ad-two').data('google-ad-height') + 'px" ' +
		'data-ad-client="' + $('#googleAdClient').val() + '" ' +
		'data-ad-slot="' + $('#googleAdSlotTwo').val() + '"></ins> ' +
		'<script> ' +
		'(adsbygoogle = window.adsbygoogle || []).push({}); ' +
		'</script> '
	);

	$('.google-ad#ad-three').html(
		'<script async="async" src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script> ' +
		'<ins class="adsbygoogle" ' +
		'style="display:inline-block;width:' + $('#ad-three').data('google-ad-width') + 'px;height:' + $('#ad-three').data('google-ad-height') + 'px" ' +
		'data-ad-client="' + $('#googleAdClient').val() + '" ' +
		'data-ad-slot="' + $('#googleAdSlotThree').val() + '"></ins> ' +
		'<script> ' +
		'(adsbygoogle = window.adsbygoogle || []).push({}); ' +
		'</script> '
	);

	$('.google-ad-sync#ad-three').html(
		'<script type="text/javascript"> ' +
		'google_ad_client = "' + $('#googleAdClient').val() + '"; ' +
		'google_ad_slot = "' + $('#googleAdSlotThree').val() + '"; ' +
		'google_ad_width = ' + $('#ad-three').data('google-ad-width') + '; ' +
		'google_ad_height = ' + $('#ad-three').data('google-ad-height') + '; ' +
		'</script> ' +
		'<script type="text/javascript" ' +
		'src="//pagead2.googlesyndication.com/pagead/show_ads.js"> ' +
		'</script> '
	);
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
			$('#removeConfirmationDialog').modal('toggle');
		},
		success : function(response) {
			var json = $.parseJSON(response);

			$('#removeMsg').text(json.result.message);

			if (json.result.success) {
				$('#'+$('#removeConfirmationObjectId').val()).remove();
			}

			$('#loadingDialog').modal('toggle');
			$('#removeMsgDialog').modal('toggle');
		},
		complete : function() {
			$('#removeConfirmationObjectId').val('');
			$('#removeConfirmationUrl').val('');
		}
	});
}