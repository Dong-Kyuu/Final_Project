$(function () {
    // 메뉴 열기
    $(".table-menu").click(function(event){
        var modal = $("#table-view-M");
        var iconPosition = $(this).offset();
        var iconHeight = $(this).outerHeight();

        // 모달의 위치를 클릭한 아이콘의 바로 아래로 설정
        modal.css({
            top: iconPosition.top + iconHeight + "px",
            left: (iconPosition.left)-110 + "px"
        });

        modal.css("display", "block");
    });
    // 메뉴 닫기 (x버튼)
    $(".close").click(function(){
        $("#table-view-M").css("display", "none");
    });
    // 메뉴 닫기 (모달 밖 클릭 시)
    $(document).mouseup(function(event){
        var modal = $("#table-view-M");
        if (!modal.is(event.target) && modal.has(event.target).length === 0) {
            modal.css("display", "none");
        }
    });
});