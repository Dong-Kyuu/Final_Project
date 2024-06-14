$(function () {

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let loginNum = $("#loginNum").val()
    $(".write-btn").click(function () {
        getUserInfo().then(data => {
            console.log(data);
            if (data.authorities.includes('ROLE_HEAD')) {
                // 권한이 있는 경우 페이지 이동
                location.href = "announceWrite";
            } else {
                // 권한이 없는 경우 경고 메시지 표시
                alert("글 작성 권한이 없습니다.");
            }
        }).catch(error => {
            console.error("권한 확인 실패:", error);
            alert("권한 확인 중 오류가 발생했습니다.");
        });
    });

    $('tbody tr').each(function() {
        var department= $(this).data('department');
        var importance= $(this).data('importance');
        var annboardNum = $(this).data('annboardnum');

        console.log("loginNum : " + loginNum + ", annboardNum : " + annboardNum)

        var listcolor = ""
        if(department == '관리부') {
            listcolor = 'orange';
        } else if (department == 'ALL') {
            listcolor = 'black';
        } else if (department == '인사부') {
            listcolor = 'red';
        } else if (department == '영업부') {
            listcolor = 'blue';
        } else if (department == '지원부') {
            listcolor = 'green';
        } else if (department == '홍보부') {
            listcolor = 'yellow';
        }

        var importanceLev = ""
        if(importance == '3') {
            importanceLev = '-lev3'
        } else if (importance == '2') {
            importanceLev = '-lev2'
        }

        $(this).addClass('dept-' + listcolor);
        $(this).children('td:first').addClass('dept-' + listcolor + importanceLev);
        $(this).find('.department').addClass(listcolor);
        var dis = $(this)
        $.ajax({
            url : '../annboard/ViewCheck',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            data : {
                loginNum : loginNum,
                annboardNum : annboardNum
            },
            type : "post",
            dataType : "json",
            success : function(rdata) {
                if(rdata.OX == 1){
                    console.log("OX : " + rdata.OX)
                    dis.find('#check-icon').attr('class', 'mdi mdi-checkbox-marked-outline');
                }
            }
        })
    })

    $('#importancelev').each(function() {
        // 해당 요소의 값이 3인지 확인
        if ($(this).val() == '3') {
            // 가장 가까운 부모 tr 요소에 클래스 lev3 추가
            $(this).closest('tr').addClass('lev3');
        } else if ($(this).val() =='2') {
            $(this).closest('tr').addClass('lev2');
        }
    });

    // 부서별 보기 모달
    $(document).on('click', '#target', function() {
        var iconPosition = $(this).offset();
        var iconHeight = $(this).outerHeight();
        $('.main-chevron-icon').attr('class','mdi mdi-chevron-down main-chevron-icon')
        $('#DepartmentModal').css({
            top: (iconPosition.top + iconHeight) - 210 + "px",
            left: (iconPosition.left) -260 + "px"
        }).slideDown('fast');
    });

    // 모달 창 외부 클릭 시 모달 창 숨기기
    $(window).on('click', function(event) {
        if (!$(event.target).closest('#target, #DepartmentModal').length) {
            $('#DepartmentModal').slideUp('fast');
            $('.main-chevron-icon').attr('class','mdi mdi-chevron-right main-chevron-icon')
        }
    });

    // 모달 창의 버튼 클릭 시 search-target 내용 변경 및 모달 창 숨기기
    $(document).on('click', '.menu-M2', function() {
        var selectedText = $(this).text();
        $('#listTitle').html(selectedText + '<i class="mdi mdi-chevron-right main-chevron-icon"></i>');
        $('#DepartmentModal').slideUp('fast');
    });
    // 부서별 보기 end

    // 아이콘 클릭 시 폼 서브밋
    $('#submit-icon').on('click', function() {
        $(this).closest('form').submit();
    });

    $('tbody').on('click', '#not-fix-icon', function() {
        var annboardNum = $(this).closest('tr').data('annboardnum');
        var dis = $(this)
        console.log("boardnum = " + annboardNum);

        $.ajax({
            url : '../annboard/topfix',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            data : {
                loginNum : loginNum,
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
    })

    $('tbody').on('click', '#fix-icon', function() {
        var annboardNum = $(this).closest('tr').data('annboardnum');
        var dis = $(this)
        console.log("boardnum = " + annboardNum);

        $.ajax({
            url : '../annboard/topfixclear',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            data : {
                loginNum : loginNum,
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
    })

})