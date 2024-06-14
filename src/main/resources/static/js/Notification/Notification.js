$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    var eventSource = new EventSource("../sse"); // SSE 엔드포인트 설정

    let notifyForm = "<a class='dropdown-item preview-item'>" +
        "   <div class='preview-item-content d-flex align-items-start flex-column justify-content-center'>" +
        "      <h6 class='preview-subject font-weight-normal mb-1'>도착한 알림이 없습니다.</h6>" +
        "      <p class='text-gray ellipsis mb-0'></p>" +
        "   </div>" +
        "</a>";
    $(".notify-tr").nextAll('.dropdown-item').hide()
    let output = '';
    let count = 0;
    $(".alarm2").hide();

    eventSource.addEventListener('notification', function (event) {

        console.log(event.data);
        if (event.data != '') {


            let data = JSON.parse(event.data); // JSON 데이터를 JavaScript 객체로 변환
            console.log("읽음? " + data.notificationIsread);

            count++;
            let notifyForm = "<div class=\"dropdown-divider\"></div>"
            if(data.notificationIsread == 1){
                notifyForm +=    "<a class='dropdown-item preview-item readNotify' id='requestLink' href='"+ data.notificationUrl + "'>"
            } else {
                notifyForm +=    "<a class='dropdown-item preview-item unreadNotify' id='requestLink' href='"+ data.notificationUrl + "'>"
            }
            notifyForm +=    "      <input type='hidden' id='notifiNum' value='"+data.notificationNum+"'>" +
                "                       <div class='preview-item-content d-flex align-items-start flex-column justify-content-center'>" +
                "                           <p class='text-gray ellipsis mb-0'>" + data.notificationFromUserName + "님께서 </p>" +
                "                           <h6 class='preview-subject font-weight-normal mb-1'>" + data.notificationMessage + "</h6>" +
                "                           <span style='font-size:10px; color:lightslategrey'>" + data.notificationRegdate + "</span>"
                "                       </div>" +
                "                   </a>";

            $(".notify-tr").after(notifyForm);
            console.log("랭스" + $("body").find(".unreadNotify").length)
            if($("body").find(".unreadNotify").length >= 1) {
                $('.reddot').css('display', 'block');
            }

        } else {
            $(".notify-tr").nextAll('.dropdown-item').remove();
            $('.reddot').css('display', 'none');
            console.log("알림없음");
            $(".notify-tr").after(notifyForm);
        }
    });

    eventSource.addEventListener('notifyBefore', function (event) {
        console.log(event.data)
        $(".notify-tr").nextAll('.dropdown-item').remove()

    });

    $(document).on('click', '#requestLink', function(event) {
        event.preventDefault(); // 링크 기본 동작 막기
        var dis = $(this);
        console.log("알림넘버 = " + dis.find($('#notifiNum')).val())
        $.ajax({
            type: 'POST',
            url: '../notification/readAction',
            data: {notifiNum : dis.find($('#notifiNum')).val()},
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(response) {
                console.log("?")
                if(response == 1) {
                    console.log("알림 읽음처리")
                    window.location.href = $('#requestLink').attr('href');
                } else{
                    console.log("실패")
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    });
});
