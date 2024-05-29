$(document).ready(function () {
    let questionButton = document.querySelectorAll('.question button');

    questionButton.forEach((elem) => {
        elem.addEventListener("click", () => {
            elem.classList.toggle("show");
        })
    })

    $(".inquery-move").click(function () {
        location.href = 'InqueryList.inq'
    });

    $(".chatbot-move").click(function () {
        location.href = 'InqueryChatBot.inq'
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