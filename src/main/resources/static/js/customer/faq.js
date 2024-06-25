$(document).ready(function () {
    let questionButton = document.querySelectorAll('.question button');

    questionButton.forEach((elem) => {
        elem.addEventListener("click", () => {
            elem.classList.toggle("show");
        })
    })

    $(".inquery-move").click(function () {
        location.href = '/inquery/list'
    });

    $(".chatbot-move").click(function () {
        location.href = ''
    });

    $(".pill").click(function () {
        $(".pill").removeClass("active");
        $(this).addClass("active");
    });
    /*var imagel = $(".album-art").attr("data-url");
    console.log(imagel);
    $(".album-art").css({
      "background-image":"url("+imagel+")"
    })*/
});