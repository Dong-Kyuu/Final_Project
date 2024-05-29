let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(function () {
    $("#viewcount").change(function () {
        go(1); // 보여줄 페이지를 1페이지로 설정합니다.
    }); // change end

    $("button").click(function () {
        location.href = "write";
    })
});


function go(page) {
    const limit = $('#viewcount').val();
    /* const data = `limit=${limit}&state=ajax&page=${page}`; */
    const data = {limit: limit, state: "ajax", page: page}
    ajax(data);
}

function ajax(sdata) {
    console.log(sdata)

    $.ajax({
        type: "POST",
        data: sdata,
        url: "list_ajax",
        dataType: "json",
        cache: false,
        beforeSend: function (xhr) {
            //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $("#viewcount").val(data.limit);
            $("thead").find("span").text("글 개수 : " + data.listcount);
            console.log(data.listcount + '입니다.')

            if (data.listcount > 0) { // 총 갯수가 0보다 큰 경우
                $("tbody").remove();
                let num = data.listcount - (data.page - 1) * data.limit;
                console.log(num)
                let output = "<tbody>";
                $(data.boardlist).each(
                    function (index, item) {
                        output += '<tr><td>' + (num--) + '</td>'
                        output += '<td><div>'
                        console.log(typeof item.cnt + '입니다.')
                        if (Number(item.cnt) > 0) {
                            console.log(typeof item.cnt + '답변처리입니다.')
                            output += '<span style="font-weight: bold;">[답변완료]</span>'
                        } else if (Number(item.cnt) == 0) {
                            console.log(typeof item.cnt + '미처리입니다.')
                            output += '<span style="color: red; font-weight: bold;">[미처리 문의]</span>'
                        }

                        if (item.inq_pass_exist) {
                            output += '<a href="detail?num=' + item.inq_num + '">'
                            output += '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-lock-fill" viewBox="0 0 16 16"><path d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2m3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2" /></svg>'
                            output += '비밀글입니다.'
                                + '</a></div></td>'
                        } else {
                            let subject = item.inq_subject;
                            if (subject.length >= 20) {
                                subject = subject.substr(0, 20) + "..."; //0부터 20개의 부분 문자열 추출
                            }
                            output += '<a href="detail?num=' + item.inq_num + '">'
                            output += subject.replace(/</g, '&lt;').replace(/>/g, '&gt;')
                                + '</a></div></td>'
                        }
                        output += '<td><div>' + item.inq_name + '</div></td>'
                        output += '<td><div>' + item.inq_date + '</div></td>'
                        output += '</tr>'
                    }
                )

                output += "</tbody>"
                $('table').append(output) //table 완성

                $(".pagination").empty(); // 페이징 처리 영역 내용 제거
                output = "";

                let digit = '<i class="fa-solid fa-angle-left"></i>'
                let href = "";
                let style = "";
                if (data.page > 1) {
                    href = 'href=javascript:go(' + (data.page - 1) + ')'
                }

                output += setPaging(style, href, digit);

                for (let i = data.startpage; i <= data.endpage; i++) {
                    digit = i;
                    href = "";
                    style = "page-num";
                    if (i != data.page) {
                        href = 'href=javascript:go(' + i + ')';
                    }
                    output += setPaging(style, href, digit);
                }

                style = "";
                digit = '<i class="fa-solid fa-angle-right"></i>';
                href = "";
                if (data.page < data.maxpage) {
                    href = 'href=javascript:go(' + (data.page + 1) + ')';
                }

                output += setPaging(style, href, digit);

                $('.pagination').append(output)
            } // if(data.listcount) > 0 end
        }, // success end
        error: function () {
            console.log('에러')
        }
    }) // ajax end
} // function ajax end

function setPaging(style, href, digit) {
    let style_active = "";
    let active = "";
    let gray = "";
    if (href == "") { // href가 빈 문자열인 경우
        if (isNaN(digit)) { // 이전 &nbsp; 또는 다음 &nbsp;
            gray = "gray"; // 11, 20번 라인처럼 href 속성이 없고 <a> 요소의 textnode가 숫자가 아닌 경우
        } else {
            active = "active"; // 12, 19번 라인처럼 href 속성이 없고 <a>요소의 textnode가 숫자인 경우
        }
    }

    if (style != "") {
        style_active = style;
    }
    let output = `<li class="page-item ${active}">`;
    // let anchor = "<a class='page-link" + gray + "'" + href + ">" + digit + "</a></li>";
    let anchor = `<a class="page-link ${style_active} ${gray}" ${href}>${digit}</a></li>`;
    output += anchor;
    return output;
}