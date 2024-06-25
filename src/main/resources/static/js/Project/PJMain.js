

$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $('.department-name').on('click', function () {
        $(this).parent().toggleClass('show');
    });

    $('.typeBtn').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            $('#peedtype').val(0);
        } else {
            $('.typeBtn').removeClass('active');
            $(this).addClass('active');

            if ($(this).text() == '요청') {
                $('#peedtype').val(1)
            } else if ($(this).text() == '진행') {
                $('#peedtype').val(2)
            } else if ($(this).text() == '피드백') {
                $('#peedtype').val(3)
            } else if ($(this).text() == '완료') {
                $('#peedtype').val(4)
            } else if ($(this).text() == '보류') {
                $('#peedtype').val(5)
            }
        }

        console.log($('#peedtype').val())
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
        var output = "<div class='choice-charger'>" + $(this).children('.chargerName').text() +"<i class='mdi mdi-close-circle cancelBtn'></i></div>"
        $('.putcharge').html(output);
        $('.chargeIn').css('display', 'none');
        $('.chargerNum').val($(this).children('.choiceChargeNum').val());
        console.log('담당자 번호 = ' + $('.chargerNum').val())
    })

    $('#putcharge').on('click', '.cancelBtn', function() {
        console.log("test")
        $('.chargeIn').css('display', 'block');
        $('.putcharge').empty()
        $('.chargerNum').val(0)
        console.log('담당자 번호 = ' + $('.chargerNum').val())
    })

    $('.dropdown-item.scope').on('click', function(){

        var selectedText = $(this).text();
        var inDisclosure = 1
        if(selectedText == '담당자만') {
            inDisclosure = 3
        } else if(selectedText == '전체보기') {
            inDisclosure = 1
        } else if(selectedText == '나만보기') {
            inDisclosure = 2
        }

        $('.selectDisclosure').text(selectedText);
        $('#peedDisclosure').val(inDisclosure);

    });

    $('.uploadBtn').on('click', function(){
        $('#upfile').click();
    });

    var fileList = [];
    $('#upfile').on('change', function(){
        var newFiles = $(this)[0].files;
        for (var i = 0; i < newFiles.length; i++) {
            fileList.push(newFiles[i]);
        }
        displayFiles();
    });

    function displayFiles() {
        $('.upfileview').empty();
        for (var i = 0; i < fileList.length; i++) {
            $('.upfileview').append('<div data-index="' + i + '" class="upfilename"> <i class="mdi mdi-upload"></i> ' + fileList[i].name + ' <i class="mdi mdi-delete-forever deleteUpfile" style="cursor: pointer;"></i> </div>');
        }
        addDeleteEvent();
    }

    function addDeleteEvent() {
        $('.deleteUpfile').on('click', function(){
            var index = $(this).parent().data('index');
            fileList.splice(index, 1);
            displayFiles();
        });
    }

    $('.comment-input').on('click', '.commentBtn', function() {
        var peedNum = $(this).closest('.card-body').find('.peedNum').val();
        var projectNum = $('.projectNum').val();
        var comm = $(this).closest('.comment-input').find('.contentInput').val().trim();
        var loginNum = $('#loginNum').val()
        var peedWriter = $(this).closest('.card-body').find('.peedWriter').val();
        console.log("peedNum= " + peedNum + ", projectNum = " + projectNum + ", comm = " + comm + ", loginNum = " + loginNum);

        if (!comm) {
            alert('내용을 입력하세요');
            return false;
        }

        enter = false;
        if (confirm('댓글을 등록하시겠어요?')) {
            url = "../project/commentAdd";
            data = {
                "projectCommentContent": comm,
                "projectMemberNum": loginNum,
                "projectNum": projectNum,
                "projectPeedNum" : peedNum,
                "peedWriter" : peedWriter
            };
            enter = true;
            $(this).closest('.comment-input').find('.contentInput').val('')
        }

        if (enter) {
            $.ajax({
                type: "post",
                url: url,
                data: data,
                beforeSend: function (xhr) {   //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
                    xhr.setRequestHeader(header, token);
                },
                success: function (result) {
                    if (result == 1) {
                        alert("댓글을 등록했습니다.")
                        location.reload();
                    }else{
                        alert("댓글을 등록하는데 실패했습니다.");
                    }
                }
            })
        }

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