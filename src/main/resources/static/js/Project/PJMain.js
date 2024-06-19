

$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $('.department-name').on('click', function () {
        $(this).parent().toggleClass('show');
    });

    $('.typeBtn').on('click', function () {
        $('.typeBtn').removeClass('active');
        $(this).addClass('active')
        if($(this).text() == '요청') {
            $('#peedtype').val(1)
        } else if($(this).text() == '진행') {
            $('#peedtype').val(2)
        } else if($(this).text() == '피드백') {
            $('#peedtype').val(3)
        } else if($(this).text() == '완료') {
            $('#peedtype').val(4)
        } else if($(this).text() == '보류') {
            $('#peedtype').val(5)
        }
    })

    $('.chargeIn').focus(function() {
        const inputOffset = $('.chargeIn').offset();
        const inputHeight = $('.chargeIn').outerHeight();

        $('.chargeList').css({
            top: inputOffset.top + inputHeight,
            left: inputOffset.left + -130 +"px"
        }).fadeIn();
    })

    $('.chargeIn').blur(function() {
        // 입력 필드에서 포커스가 벗어나면 숨기기
        $('.chargeList').fadeOut();
    });

    $('.chargeIn').keyup(function() {
        var dis = $(this).val()
        $.ajax({
            type: 'POST',
            url: '../project/searchCharge',
            data: {
                projectNum : $('.projectNum').val(),
                searchWord : dis,
                loginNum : $('#loginNum').val()
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(response) {

                $(".chargeitem").remove();
                if(response.members.length > 0) {
                    $(response.members).each(function () {
                        console.log(this.projectMemberNum)
                        output = "<div class='chargeitem'>" +
                            "        <input type='hidden' class='choiceChargeNum' value='"+ this.projectMemberNum+"'>"+
                            "        <div class='chargerName'>"+ this.projectMemberName+"</div>\n" +
                        "            <span style=\"color:#878787; margin-left: 15px;\">\n" +
                                        this.projectMemberDepartment +
                        "            </span>\n" +
                        "            <code>\n" +
                                        this.projectMemberPosition +
                        "            </code>\n" +
                            "    </div>"

                        $('.chargeList').append(output)
                    })
                } else{
                    console.log("검색 결과 없음")
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
        // off('click', '.chargeitem').

    })

    $('.chargeList').on('click', '.chargeitem', function(event) {
        event.stopPropagation()
        console.log("click")
        var output = "<div class='choice-charger'>" + $(this).children('.chargerName').text() +"<i class=\"mdi mdi-close-circle\"></i></div>"
        $('.putcharge').html(output);
        $('.chargeIn').css('display', 'none');
    })

});

function openModal() {
    document.querySelector('.createContent').classList.add('active');
    document.getElementById("myModal").style.display = "block";
}

function closeModal() {
    document.querySelector('.createContent').classList.remove('active');
    document.getElementById("myModal").style.display = "none";
}

// Click anywhere outside of the modal to close it
window.onclick = function(event) {
    var modal = document.getElementById("myModal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}