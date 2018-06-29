jQuery(function($) {
	$('table th input:checkbox').on(
			'click',
			function() {
				var that = this;
				$(this).closest('table').find(
						'tr > td:first-child input:checkbox').each(function() {
					this.checked = that.checked;
					$(this).closest('tr').toggleClass('selected');
				});

			});

	$("#config-alarm-direct-by-mail").click(function() {
		$(window).attr('location', '/alarm/alarmForward/0/0');
	})

	$("#config-alarm-direct-by-sms").click(function() {
		$(window).attr('location', '/alarm/alarmForward/0/0');
	})
})