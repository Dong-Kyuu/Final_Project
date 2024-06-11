$(function () {
    $('#upload-btn').on('click', function () {
        $('#upfile').click();
    });

    $('#upfile').on('change', function () {
        var fileNames = $.map(this.files, function (file) {
            return file.name;
        }).join('\n\n');  // 쉼표 없이 한 줄씩 나오게 수정
        $('.upfileview').val(fileNames);

        // 파일 이름에 따라 textarea의 높이를 조정합니다.
        var baseHeight = 43; // 기본 높이
        var extraHeightPerFile = 27; // 파일 하나당 추가될 높이
        var numFiles = this.files.length -1; // 파일 수
        var newHeight = baseHeight + (extraHeightPerFile * numFiles); // 새로운 높이 계산
        var maxHeight = 150; // 최대 높이 설정

        // padding, border 값을 고려하여 계산합니다.
        var padding = parseInt($('.upfileview').css('padding-top')) + parseInt($('.upfileview').css('padding-bottom'));
        var border = parseInt($('.upfileview').css('border-top-width')) + parseInt($('.upfileview').css('border-bottom-width'));
        var totalHeight = newHeight + padding + border;

        console.log("Number of files:", numFiles);
        console.log("Calculated new height:", newHeight);
        console.log("Padding:", padding);
        console.log("Border:", border);
        console.log("Total height:", totalHeight);

        if (totalHeight > maxHeight) {
            $('.upfileview').css('height', maxHeight + 'px');
            $('.upfileview').css('overflow', 'auto'); // 최대 높이를 초과하면 스크롤을 추가합니다.
        } else {
            $('.upfileview').css('height', totalHeight - 30 + 'px');
            $('.upfileview').css('overflow', 'hidden'); // 스크롤을 숨깁니다.
        }

        console.log("Applied height:", $('.upfileview').css('height'));
    });

    $('#writeForm').on('submit', function(event) {
        var title = $('#board_subject').val().trim();
        var content = $('#board_content').val().trim();
        if (title === '') {
            alert('제목을 입력해주세요.');
            $('#board_subject').focus();
            event.preventDefault();
        } else if (content === '') {
            alert('내용을 입력해주세요.');
            $('#board_content').focus();
            event.preventDefault();
        }
    });
});