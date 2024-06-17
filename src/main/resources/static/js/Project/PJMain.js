$(function () {
    $('.department-name').on('click', function () {
        $(this).parent().toggleClass('show');
    });


});

function openModal() {
    document.querySelector('.createContent').classList.add('active');
    document.getElementById("myModal").style.display = "block";
}

function closeModal() {
    document.querySelector('.createContent').classList.remove('active');
    document.getElementById("myModal").style.display = "none";
}

// Click anywhere outside of the modal to close it
window.onclick = function(event) {
    var modal = document.getElementById("myModal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}