$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    window.fetchDataAndRenderChart(header, token);

    // 부서 색 표시
    console.log($('#department').val())
    if($('#department').val() == '관리부') {
        $('.department').css('color','#ff8900');
    } else if(($('#department').val() == '인사부')) {
        $('.department').css('color','#ff6161');
    } else if(($('#department').val() == '영업부')) {
        $('.department').css('color','#4f6aaf');
    } else if(($('#department').val() == '홍보부')) {
        $('.department').css('color','#caca4c');
    } else if(($('#department').val() == '지원부')) {
        $('.department').css('color','#93cab3');
    }
    // 메뉴 열기
    $(".table-menu").click(function (event) {
        var modal = $("#table-view-M");
        var iconPosition = $(this).offset();
        var iconHeight = $(this).outerHeight();

        // 모달의 위치를 클릭한 아이콘의 바로 아래로 설정
        modal.css({
            top: iconPosition.top + iconHeight + "px",
            left: (iconPosition.left) - 110 + "px"
        });

        modal.css("display", "block");
    });

    // 메뉴 닫기 (모달 밖 클릭 시)
    $(document).mouseup(function (event) {
        var modal = $("#table-view-M");
        var modal2 = $("#table-view-delete-M")
        if (!modal.is(event.target) && modal.has(event.target).length === 0) {
            modal.css("display", "none");
        }

        if (!modal2.is(event.target) && modal.has(event.target).length === 0) {
            modal2.css("display", "none");
        }
    });

    $.ajax({
        url: '../annboard/ViewAction',
        type: 'post',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        data: {
            loginNum: $('#loginNum').val() ,
            annboardNum: $('#boardnum').val()
        },
        success: function(response) {
            // 서버로부터 받은 데이터를 처리합니다.
            console.log(response);
            console.log("OX:"+ response.OX);
            if(response.OX==1){
                $('#checkBtn').append(' <i class="mdi mdi-check"></i>');
            }
        },
        error: function(xhr, status, error) {
            // 요청이 실패했을 때의 처리를 정의합니다.
            console.error(error);
        }
    });

    $('#checkBtn').click(function() {
        var $icon = $(this).find('i');

        if ($icon.length) {
            // 아이콘이 있으면 제거
            $icon.remove();
            $.ajax({
                url: '../annboard/checkDelete',
                type: 'post',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data: {
                    loginNum: $('#loginNum').val() ,
                    annboardNum: $('#boardnum').val()
                },
                success: function(response) {
                    // 서버로부터 받은 데이터를 처리합니다.
                    console.log(response);
                    // 필요한 경우, 응답 데이터를 사용하여 화면을 업데이트합니다.
                    window.fetchDataAndRenderChart(header, token);
                },
                error: function(xhr, status, error) {
                    // 요청이 실패했을 때의 처리를 정의합니다.
                    console.error(error);
                }
            });

        } else {
            // 아이콘이 없으면 추가
            $(this).append(' <i class="mdi mdi-check"></i>');
            $.ajax({
                url: '../annboard/checkAction',
                type: 'post',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data: {
                    loginNum: $('#loginNum').val() ,
                    annboardNum: $('#boardnum').val()
                },
                success: function(response) {
                    console.log(response);
                    window.fetchDataAndRenderChart(header, token);
                },
                error: function(xhr, status, error) {
                    console.error(error);
                }
            });
        }


    });

    $('#checkBtn').hover(
        function(e) {
            var $icon = $(this).find('i');
            if ($icon.length === 0) {
                var modal = $("#toolTip");
                var iconPosition = $(this).offset();
                var iconHeight = $(this).outerHeight();

                // 모달의 위치를 클릭한 아이콘의 바로 아래로 설정
                modal.css({
                    top: iconPosition.top + iconHeight + 20 + "px",
                    left: (iconPosition.left) - 50 + "px"
                });

                modal.css("display", "block");
            }
        },
        function() {
            $('#toolTip').css('display', 'none');
        }
    );

    $('#checkBtn').mousemove(function(e) {
        $('#tooltip').css({
            left: e.pageX - 10*10 + 'px',
            top: e.pageY - 10*10 + 'px'
        });
    });

    $('body').on('click', '#not-fix-icon', function() {
        var annboardNum = $('#boardnum').val()
        var dis = $(this)
        console.log("boardnum = " + annboardNum);
        if(confirm("상단고정 하시겠어요?")) {
            $.ajax({
                url: '../annboard/topfix',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data: {
                    loginNum: $('#loginNum').val(),
                    annboardNum: annboardNum
                },
                type: "post",
                dataType: "json",
                success: function (rdata) {
                    alert(rdata.result);

                        location.reload();

                }
            })
        }
    })

    $('body').on('click', '#fix-icon', function() {
        var annboardNum = $('#boardnum').val()
        var dis = $(this)
        console.log("boardnum = " + annboardNum);

        if(confirm("상단고정을 해제하시겠어요?")) {
            $.ajax({
                url: '../annboard/topfixclear',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data: {
                    loginNum: $('#loginNum').val(),
                    annboardNum: annboardNum
                },
                type: "post",
                dataType: "json",
                success: function (rdata) {
                    alert(rdata.result);
                    if (rdata.update == 1) {
                        location.reload();  // 페이지 새로고침
                    }
                }
            })
        }
    })

    $(".standby").hover(
        function() {
            var $icon = $(this).find("#standby");
            var $tooltip = $(this).find(".tooltip-message");

            // 아이콘의 위치와 크기를 가져옴
            var iconOffset = $icon.offset();
            var iconHeight = $icon.outerHeight();

            // 툴팁의 위치를 아이콘 바로 아래로 설정
            $tooltip.css({
                top: iconOffset.top + iconHeight + -170 + 'px', // 아이콘 바로 아래 + 약간의 간격
                left: iconOffset.left + -350 + 'px'
            });

            // 툴팁 표시
            $tooltip.show();
        },
        function() {
            // 마우스를 뗐을 때
            $(this).find(".tooltip-message").hide();
        }
    );

    $(".requestFix").hover(
        function() {
            var $icon = $(this).find(".icon-item");
            var $tooltip = $(this).find(".tooltip-message");


            // 아이콘의 위치와 크기를 가져옴
            var iconOffset = $icon.offset();
            var iconHeight = $icon.outerHeight();

            // 툴팁의 위치를 아이콘 바로 아래로 설정
            $tooltip.css({
                top: iconOffset.top + iconHeight -170 + 'px', // 아이콘 바로 아래 + 약간의 간격
                left: iconOffset.left -410 + 'px'
            });

            // 툴팁 표시
            $tooltip.show();
        },
        function() {
            // 마우스를 뗐을 때
            $(this).find(".tooltip-message").hide();
        }
    );

    $('body').on('click', '.request-btn', function() {
        var annboardNum = $('#boardnum').val();
        var dis = $(this)
        console.log("boardnum = " + annboardNum);

        if(confirm("상단고정을 승인시겠어요?")) {
            $.ajax({
                url : '../annboard/topfix',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data : {
                    loginNum : $('#loginNum').val(),
                    annboardNum : annboardNum
                },
                type : "post",
                dataType : "json",
                success : function(rdata) {
                    alert(rdata.result);
                    if(rdata.update == 1){
                        location.href = "announceList" ;  // 페이지 새로고침
                    }

                }
            })
        } else {
            $.ajax({
                url : '../annboard/topfixRefuse',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                data : {
                    loginNum : $('#loginNum').val(),
                    annboardNum : annboardNum
                },
                type : "post",
                dataType : "json",
                success : function(rdata) {

                    if(rdata.result == 1){
                        alert("거절하셨습니다.")
                        location.reload();
                    }

                }
            })
        }


    })

});

// 삭제 확인
function confirmDelete() {
    // 확인 메시지를 표시
    return confirm("정말 삭제하시겠어요?");
}



// 수정 확인
function confirmModify() {
    if($("#loginNum").val() != $("#writerNum").val()){
        alert("수정은 본인만 가능합니다.");
        return false;
    }
    // 확인 메시지를 표시
    return confirm("글을 수정하시겠어요?");
}

// 권한 확인 메서드
function getUserInfo() {
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            url: "/scan/auth",
            dataType: "json",
            success: function (data) {
                resolve(data);
            },
            error: function (error) {
                console.error("삭제 권한 없음", error);
                reject(error);
            }
        });
    });
}