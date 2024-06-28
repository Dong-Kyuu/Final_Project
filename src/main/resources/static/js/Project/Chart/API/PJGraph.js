$(document).ready(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    window.fetchDataAndRenderChartPJ = fetchDataAndRenderChartPJ;
    window.createDoughnutPieDataPJ = createDoughnutPieDataPJ;
    window.createDoughnutPieOptionsPJ = createDoughnutPieOptionsPJ;

    function fetchDataAndRenderChartPJ(header, token, peedNum, projectNum) {
        $.ajax({
            url: '../project/getPeedCheckUser',
            type: 'post',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: {
                peedNum : peedNum,
                projectNum : projectNum
            },
            success: function(response) {
                console.log('AJAX 성공:', response);
                var doughnutPieData = createDoughnutPieDataPJ(response);
                var doughnutPieOptions = createDoughnutPieOptionsPJ();

                if ($("#doughnutChart").length) {
                    var doughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
                    var doughnutChart = new Chart(doughnutChartCanvas, {
                        type: 'doughnut',
                        data: doughnutPieData,
                        options: doughnutPieOptions
                    });

                    $("#doughnutChart").click(function(evt) {
                        handleChartClickPJ(evt, doughnutChart, response);
                    });
                } else {
                    console.log("#doughnutChartPJ 요소가 존재하지 않습니다.");
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX 실패:', error);
            }
        });
    }

    function createDoughnutPieDataPJ(response) {
        console.log("createDoughnut")
        return {
            datasets: [{
                data: [response.checkMembers.length, response.unCheckMembers.length],
                backgroundColor: [
                    'rgba(255, 206, 86, 0.5)',
                    'rgba(75, 192, 192, 0.5)',
                    'rgba(153, 102, 255, 0.5)',
                    'rgba(255, 159, 64, 0.5)'
                ],
                borderColor: [
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
            }],

            // These labels appear in the legend and in the tooltips when hovering different arcs
            labels: [
                '확인',
                '미확인',
            ]
        };
    }

    function createDoughnutPieOptionsPJ() {
        console.log("Option")
        return {
            responsive: true,
            animation: {
                animateScale: true,
                animateRotate: true
            }
        };
    }
    let use=1;
    function handleChartClickPJ(evt, doughnutChart, response) {
        var activePoints = doughnutChart.getElementsAtEventForMode(evt, 'nearest', { intersect: true }, false);
        if (activePoints.length > 0) {
            var chartElement = activePoints[0];
            var datasetIndex = chartElement._datasetIndex;
            var index = chartElement._index;

            if (doughnutChart.data.labels && doughnutChart.data.datasets[datasetIndex]) {
                $('.checkGraph').css('width', '650px')
                if(use == 1 || $('#checkGraph').find('.partList').length === 0) {
                    var partList = "<div style='padding:20px; display: none' class='partList'><div> <ul class='partListItem'></ul></div></div>"
                    $('#checkGraph').append(partList);
                    use=1;
                }
                if(use%2===0) {
                    $('.partList').slideUp();
                } else {
                    $('.partList').slideDown();
                }
                use ++


                var users;
                switch (index) {
                    case 0: users = response.checkMembers; break;
                    case 1: users = response.unCheckMembers; break;
                }

                $('.partListItem').empty();
                if (users && users.length > 0) {
                    users.forEach(function(user) {
                        var listItem = '<li>'+user.projectMemberName+'<span style="color:#fe7c96; font-size:12px; font-weight: bold; margin-left:1em">'+ user.projectMemberPosition +'</span></li>'
                        $('.partListItem').append(listItem);
                    });
                } else {
                    $('#User').append('<li> 해당하는 사원이 없습니다. </li>')
                }

                $('#chartInfoModal').modal('show');
            } else {
                console.error("Data is undefined");
            }
        }
    }
});