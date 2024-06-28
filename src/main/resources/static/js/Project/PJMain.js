

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

    function adjustScrollPosition() {
        // 현재 해시값을 가져옴
        var hash = window.location.hash;
        if (hash) {
            // 해당 해시값을 가진 요소를 찾음
            console.log("scroll")
            var $targetElement = $(hash);
            if ($targetElement.length) {
                // 브라우저의 기본 스크롤 동작을 약간 지연시킴
                setTimeout(function() {
                    // 원하는 만큼 추가 스크롤 조정
                    $('html, body').animate({
                        scrollTop: $targetElement.offset().top - 200 // 50px 만큼 더 위로 이동 (원하는 값으로 변경 가능)
                    }, 0); // 기본 동작 이후에 실행되도록 지연 시간 설정
                }, 0);
            }
        }
    }

    $(window).on('load', adjustScrollPosition);
    $(window).on('hashchange', adjustScrollPosition);

    $('.btn-flex').on('click', '.PeedBtn', function() {
        var dis = $(this)
        var type = 0
        var peedNum = $(this).closest('.card-body').find('.peedNum').val();
        var projectNum = $('.projectNum').val();
        var loginNum = $('#loginNum').val()
        console.log(type + "peedNum = " + peedNum + ", projectNum = " +projectNum + ", loginNum = " + loginNum)

        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $('.PeedBtn').removeClass('active');
            $(this).addClass('active');
            if ($(this).text() == '요청') {
                type = 1
            } else if ($(this).text() == '진행') {
                type = 2
            } else if ($(this).text() == '피드백') {
                type = 3
            } else if ($(this).text() == '완료') {
                type = 4
            } else if ($(this).text() == '보류') {
                type = 5
            }
        }
            $.ajax({
                type: 'POST',
                url: '../project/changeType',
                data: {
                    type : type,
                    peedNum : peedNum,
                    projectNum : projectNum,
                    loginNum : loginNum
                },
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    if(response.result == 1) {
                        console.log('타입 수정.')
                        var output = optionChangeComment(response.peedNum, response.projectNum, response.option, response.type)

                        dis.closest('.card-body').find('.comment-area').prepend(output);


                    } else {
                        alert("피드 수정 권한이 없습니다.")
                    }
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });

    })

    function optionChangeComment(peedNum, projectNum, option, type) {
        var loginNum = $('#loginNum').val()

        $.ajax({
            type: 'POST',
            url: '../project/optionComment',
            data: {
                loginNum : loginNum,
                peedNum : peedNum,
                projectNum : projectNum,
                option : option,
                type : type
            },
            async : false,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(response) {
                if(response.result == 1){
                    output = "<div class=\"comment-box\" style=\"margin-bottom:20px;\">\n" +
                        "        <div style=\"height: 25px;\">\n" +
                        "           <img src=" + response.comment.commWriterProfile+ " alt=\"image\" style=\"width:25px!important; height:25px; border-radius: 12.5px; float: left; margin: 5px 15px 0px 0px;\">\n" +
                        "           <i class=\"mdi mdi-dots-vertical\" style=\"float: right; font-size: 25px; margin-top:3px;\"></i>\n" +
                        "           <div style=\"height:40px; padding-top:0px;\">\n" +
                        "              <span class=\"card-title\" style=\"font-width: bold; font-size:12px\">\n" +
                                          response.comment.commWriter +
                        "              </span>\n" +
                        "              <p class=\"card-description\" style=\"font-size:10px\">\n" +
                                          response.comment.commWriterDepartment + "<code style=\"font-size:10px\"> " + response.comment.commWriterPosition + "</code> <code style=\"font-size:10px; color:dimgray\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + response.comment.projectCommentRegdate + "</code>\n" +
                        "              </p>\n" +
                        "           </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"comm-contentbox\" style=\"margin-left:45px; margin-top:8px; font-size:15px\">\n" +
                        "           <br>\n" +
                                    response.comment.projectCommentContent +
                        "           <br>\n" +
                        "        </div>\n" +
                        "     </div>"

                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        })

        return output;
    }

    $('.content-icon').on('click', '.checkBtn', function() {
        var peedNum = $(this).closest('.card-body').find('.peedNum').val();
        var projectNum = $('.projectNum').val();
        var loginNum = $('#loginNum').val()
        console.log("peedNum= " + peedNum + ", projectNum = " + projectNum + ", loginNum = " + loginNum);

        if($(this).hasClass('uncheck')) {
            $(this).removeClass();
            $(this).addClass('mdi mdi-checkbox-blank-outline checkBtn')
            $.ajax({
                type: 'POST',
                url: '../project/deleteCheck',
                data: {
                    peedNum : peedNum,
                    projectNum : projectNum,
                    loginNum : loginNum
                },
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                    console.log(response.result);
                    fetchDataAndRenderChartPJ(header, token, peedNum, projectNum);
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });

        } else {
            $(this).removeClass();
            $(this).addClass('mdi mdi-checkbox-marked-outline checkBtn uncheck')

            $.ajax({
                type: 'POST',
                url: '../project/addCheck',
                data: {
                    peedNum : peedNum,
                    projectNum : projectNum,
                    loginNum : loginNum
                },
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(response) {
                        console.log(response.result);
                    fetchDataAndRenderChartPJ(header, token, peedNum, projectNum);
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });
        }
    })
let re=0;
    $('.content-icon').on('click', '.checkcheck', function () {
        var peedNum = $(this).closest('.card-body').find('.peedNum').val();
        var projectNum = $('.projectNum').val();
        var dis = $(this)
        $('.partList').remove();
        $('.chart-board').empty();
        $('.chart-board').append('<h4 class="card-title chart-title">확인 현황</h4>\n' +
            '                                <canvas id="doughnutChart" style="height:300px"></canvas>').promise().done(function() {
            // fetchDataAndRenderChartPJ 함수 호출
            fetchDataAndRenderChartPJ(header, token, peedNum, projectNum);
        }).promise().done(function(){
            if(re==0) {
                $("#checkGraph").hide();
            }
            re++
            $('.partListItem').empty();

            var offset = dis.offset();
            $("#checkGraph").css({
                top: offset.top + dis.outerHeight() + 10 + 'px',
                left: offset.left
            }).slideToggle('fast', function(){
                // Check if the div is visible after toggling
                if ($(this).is(':visible')) {
                    $(this).css('display', 'flex'); // Show with flex display
                } else {
                    $(this).css('display', 'none'); // Hide
                }
            });
        })
        $('.checkGraph').css('width', '500px')
        // $('#doughnutChart').css('width', '400px')
    })

    // 모달 닫기
    $(".close").click(function(){
        $("#myModal").css("display", "none");
    });

    // 모달을 클릭했을 때 닫기
    $(window).click(function(event){
        if ($(event.target).is("#myModal")) {
            $("#myModal").css("display", "none");
        }
    });
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

