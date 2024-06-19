$(document).ready(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    loginNum = $('#sideLoginNum').val()

    $.ajax({
        url : '../project/myproject',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        data : {
            loginNum : loginNum,
        },
        type : "post",
        dataType : "json",
        success : function(rdata) {
            
            $('.project-menu').empty()
            if(rdata.projects.length > 0) {
                $(rdata.projects).each(function () {
                    projectlistform =
                        "<li class='nav-item'><a class='nav-link' href='/project/mainProject?projectNum="+this.projectNum+"'>"+this.projectTitle+"</a></li>"
                    $('.project-menu').append(projectlistform);
                })
            }

        }
    })


    $(".createProject").click(function () {
        console.log("클릭")
        getUserInfo().then(data => {
            console.log(data);
            if (data.authorities.includes('ROLE_TEAM')) {
                // 권한이 있는 경우 페이지 이동
                location.href = "/project/createproject";
            } else {
                // 권한이 없는 경우 경고 메시지 표시
                alert("권한이 없습니다.");
            }
        }).catch(error => {
            console.error("권한 확인 실패:", error);
            alert("권한 확인 중 오류가 발생했습니다.");
        });
    });
});