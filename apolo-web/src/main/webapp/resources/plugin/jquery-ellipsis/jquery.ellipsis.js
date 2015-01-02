(function($) {
    $.fn.ellipsis = function(options) {
        var defaults = {
            row : 1,
            char : '...'
        };

        options = $.extend(defaults, options);

        var maxWidth = 0;
        
        function getWidth(obj) {
        	if ($(obj).parent('div').length > 0) {
        		maxWidth = $(obj).parent('div').width() - 15;
        		return true;
        	} else {
        		getWidth($(obj).parent());
        	}
        }
        
        this.each(function() {
            var $this = $(this);
            var text = $this.text();
            text = $.trim(text);
            
            getWidth($(this));
            
            $this.text('a');
            var rowHeight = $this.height();
            $this.text('');
            var flag = false;
            var height = 0;
            charLengthPerLine = 0;
            charCount = 0;
            for (var i = 0; i < text.length; i++) {
                var s = text.substring(i, i + 1);
                $this.text($this.text() + s);
                charCount++;
                
                if ($(this).width() >= maxWidth) {
                	$this.text($this.text().substring(0, (charCount / 2) - 3) + '...' + text.substring(text.length - (charCount / 2) - 3, text.length));
               		break;
                }
            }

            if (flag) {
                text = $this.text();
                while (true) {
                    text = text.substring(0, text.length - 1);
                    $this.text(text + options.char);
                    height = $this.height();

                    if (height < rowHeight) {
                        break;
                    }
                }
            }

        });

        return this;
    };

})(jQuery);

