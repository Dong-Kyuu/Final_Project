$(function () {
    $(".write-btn").click(function () {
        location.href = "f_write";
    });

    $('tbody tr').each(function() {
        var department= $(this).data('department');
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
        console.log('타겟 부서 : ' + department)

        $(this).addClass('dept-' + listcolor);
        $(this).find('.department').addClass(listcolor);
    })

    // // 검색 모달관련
    // $(document).on('click', '.chevron-icon', function() {
    //     var iconPosition = $(this).offset();
    //     var iconHeight = $(this).outerHeight();
    //     $('.chevron-icon').attr('class','mdi mdi-chevron-down chevron-icon')
    //     $('#searchModal').css({
    //         top: (iconPosition.top + iconHeight) - 160 + "px",
    //         left: (iconPosition.left) -390 + "px"
    //     }).slideDown('fast');
    // });
    //
    // // 모달 창 외부 클릭 시 모달 창 숨기기
    // $(window).on('click', function(event) {
    //     if (!$(event.target).closest('.search-space, #searchModal').length) {
    //         $('#searchModal').slideUp('fast');
    //         $('.chevron-icon').attr('class','mdi mdi-chevron-right chevron-icon')
    //     }
    // });
    //
    // // 모달 창의 버튼 클릭 시 search-target 내용 변경 및 모달 창 숨기기
    // $(document).on('click', '.menu-M', function() {
    //     var selectedText = $(this).text();
    //     $('.search-target').html(selectedText + '<i class="mdi mdi-chevron-right chevron-icon"></i>');
    //     if(selectedText=='작성자'){
    //         $('.search-target').css('font-size','17px')
    //     } else {
    //         $('.search-target').css('font-size','18px')
    //     }
    //     $('#searchModal').slideUp('fast');
    // });
    // // 검색 모달관련 end

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



})