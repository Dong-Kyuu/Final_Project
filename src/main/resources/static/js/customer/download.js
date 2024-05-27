$(document).ready(function() {
	$("#download").click(function() {
		$("#download").attr("disabled", true);
		$("#download .loader").css("visibility", "visible");
		setTimeout(() => {
			$("#download .loaded").css("visibility", "visible");
			$("#download .loader").css("visibility", "hidden");
			setTimeout(() => {
				$("#download .loaded").css("visibility", "hidden");
				$("#download").attr("disabled", false);
			}, 1000);
		}, 1000);
	});
});