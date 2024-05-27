
function deleteClick() {
	setTimeout(function() {
	 	const inq_board_num = $('.inq_board_num');
		const inq_board_pass = $('input[name=inq_pass]');
		console.log(inq_board_num.val());
		console.log(inq_board_pass.val());
		location.href = 'delete?num=' + inq_board_num.val() + '&inq_pass=' + inq_board_pass.val();
	}, 2000);
}


window.addEventListener("DOMContentLoaded", () => {	
	const d = new DeleteButton("#delete");
});

class DeleteButton {
	isRunning = false;

	constructor(el) {
		this.el = document.querySelector(el);
		this.init()
	};
	init() {
		this.el?.addEventListener("click",this.delete.bind(this));
		
		const resetTrigger = this.el?.querySelector("[data-anim]");
		resetTrigger?.addEventListener("animationend",this.reset.bind(this));
	}
	delete() {
		this.isRunning = true;
		this.displayState();
	}
	displayState() {
		this.el.disabled = this.isRunning;
		this.el.setAttribute("data-running",this.isRunning);
	}
	reset() {
		this.isRunning = false;
		this.displayState();
	}
}

