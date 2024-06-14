$(function () {
    var eventSource = new EventSource("../sse"); // SSE 엔드포인트 설정

    let notifyForm = "<a class='dropdown-item preview-item'>" +
        "   <div class='preview-item-content d-flex align-items-start flex-column justify-content-center'>" +
        "      <h6 class='preview-subject font-weight-normal mb-1'>도착한 알림이 없습니다.</h6>" +
        "      <p class='text-gray ellipsis mb-0'></p>" +
        "   </div>" +
        "</a>";
    $(".notify-tr").nextAll('.dropdown-item').remove();
    let output = '';
    let count = 0;
    $(".alarm2").hide();
    eventSource.addEventListener('notification', function (event) {
        console.log(event.data);
        if (event.data != '') {

            $('.reddot').css('display', 'block');
            let data = JSON.parse(event.data); // JSON 데이터를 JavaScript 객체로 변환
            console.log("data" + data);

            count++;
            let notifyForm = "<a class='dropdown-item preview-item'>" +
                "                       <div class='preview-item-content d-flex align-items-start flex-column justify-content-center'>" +
                "                           <h6 class='preview-subject font-weight-normal mb-1'>" + data.notificationMessage + "</h6>" +
                "                           <p class='text-gray ellipsis mb-0'></p>" +
                "                       </div>" +
                "                   </a><div class=\"dropdown-divider\"></div>";

            $(".notify-tr").after(notifyForm);
        } else {
            $(".notify-tr").nextAll('.dropdown-item').remove();
            $('.reddot').css('display', 'none');
            console.log("알림없음");
            $(".notify-tr").after(notifyForm);
        }
    });

    $(".alarm2 .dropdown-menu").click(function () {
        $.ajax({
            url: "../alarm_isRead",
            success: function () {
                $("#alarmCount").text('0');
                $(".alarm0 img").attr('src', "../image/alarm0.png");
                $(".alarm0").show();
                $(".alarm2").hide();
            }
        });
    });
});
