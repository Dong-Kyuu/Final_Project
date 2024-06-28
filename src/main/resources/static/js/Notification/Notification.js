$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let linkUrl = window.location.origin
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

        // console.log(event.data);
        if (event.data != '') {


            let data = JSON.parse(event.data); // JSON 데이터를 JavaScript 객체로 변환
            // console.log("읽음? " + data.notificationIsread);

            count++;
            let notifyForm = "<div class=\"dropdown-divider\"></div>"
            if(data.notificationIsread == 1){
                notifyForm +=    "<a class='dropdown-item preview-item readNotify' id='requestLink' href='"+ linkUrl + data.notificationUrl + "'>"
            } else {
                notifyForm +=    "<a class='dropdown-item preview-item unreadNotify' id='requestLink' href='"+ linkUrl + data.notificationUrl + "'>"
            }
            notifyForm +=
                "                       <div class='preview-item-content d-flex align-items-start flex-column justify-content-center'>" +
                "                           <p class='text-gray ellipsis mb-0'>" + data.notificationFromUserName + "님께서 </p>" +
                "                           <h6 class='preview-subject font-weight-normal mb-1'>" + data.notificationMessage + "</h6>" +
                "                           <span style='font-size:10px; color:lightslategrey'>" + data.notificationRegdate + "</span>" +
                "                       </div>" +
                "                   <input type='hidden' id='notifiNum' value='"+data.notificationNum+"'>" +
                "                   </a>";

            $(".notify-tr").after(notifyForm);
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
        // console.log(event.data)
        $(".notify-tr").nextAll('.dropdown-item').remove()

    });

    $(document).on('click', '#requestLink', function(event) {
        event.preventDefault(); // 링크 기본 동작 막기
        var dis = $(this);
        var Link = $(this).attr('href');

        $.ajax({
            type: 'POST',
            url: '../notification/readAction',
            data: {notifiNum : $(this).find('#notifiNum').val()},
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(response) {
                console.log("?")
                if(response == 1) {
                    console.log("알림 읽음처리")
                    window.location.href = Link;
                } else{
                    console.log("실패")
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    });

    $('.notifiList').off('click', '.see-all-notifications').on('click', '.see-all-notifications', function() {
        if(confirm("모든 알림을 확인처리 하시겠어요?")) {
            $.ajax({
                type: 'POST',
                url: '../notification/readAll',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (response) {
                    if(response == 1) {
                        $(".unreadNotify").removeClass("unreadNotify").addClass("readNotify");
                        $('.reddot').css('display', 'none');
                        alert("모든 알림을 읽음 처리했습니다.");
                    } else {
                        alert("읽지않은 알림이 없습니다.");
                    }
                },
                error: function (error) {
                    console.error('Error:', error);
                }
            });
        }
    });

    $('.notifiList').off('click', '.deleteNotifi').on('click', '.deleteNotifi', function() {
        if(confirm("모든 알림을 지우시겠습니까?")) {
            $.ajax({
                type: 'POST',
                url: '../notification/deleteAll',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (response) {
                    if(response == 1) {
                        alert("모든 알림을 삭제했습니다.");
                        $(".notify-tr").nextAll('.dropdown-item').remove();
                        $('.reddot').css('display', 'none');
                        console.log("알림없음");
                        $(".notify-tr").after(notifyForm);
                    } else {
                        alert("알림이 존재하지 않습니다.");
                    }
                },
                error: function (error) {
                    console.error('Error:', error);
                }
            });
        }
    });
});
