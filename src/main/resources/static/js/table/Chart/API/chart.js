$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    window.fetchDataAndRenderChart = fetchDataAndRenderChart;
    window.createDoughnutPieData = createDoughnutPieData;
    window.createDoughnutPieOptions = createDoughnutPieOptions;
    window.handleChartClick = handleChartClick;
    window.getUserInfo = getUserInfo;

    function fetchDataAndRenderChart(header, token) {
        $.ajax({
            url: '../annboard/getUsersdata',
            type: 'post',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            data: {
                loginNum: $('#loginNum').val(),
                annboardNum: $('#boardnum').val()
            },
            success: function(response) {
                var doughnutPieData = createDoughnutPieData(response);
                var doughnutPieOptions = createDoughnutPieOptions();

                if ($("#doughnutChart").length) {
                    var doughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
                    var doughnutChart = new Chart(doughnutChartCanvas, {
                        type: 'doughnut',
                        data: doughnutPieData,
                        options: doughnutPieOptions
                    });

                    $("#doughnutChart").click(function(evt) {
                        handleChartClick(evt, doughnutChart, response);
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });
    }

    function createDoughnutPieData(response) {
        return {
            datasets: [{
                data: [
                    response.checkedHR && response.checkedHR[1] ? response.checkedHR[1].length : 0,
                    response.humanResource ? response.humanResource.length - (response.checkedHR && response.checkedHR[1] ? response.checkedHR[1].length : 0) : 0,
                    response.checkedMG && response.checkedMG[1] ? response.checkedMG[1].length : 0,
                    response.management ? response.management.length - (response.checkedMG && response.checkedMG[1] ? response.checkedMG[1].length : 0) : 0,
                    response.checkedRT && response.checkedRT[1] ? response.checkedRT[1].length : 0,
                    response.relations ? response.relations.length - (response.checkedRT && response.checkedRT[1] ? response.checkedRT[1].length : 0) : 0,
                    response.checkedSP && response.checkedSP[1] ? response.checkedSP[1].length : 0,
                    response.supportDepartment ? response.supportDepartment.length - (response.checkedSP && response.checkedSP[1] ? response.checkedSP[1].length : 0) : 0,
                    response.checkedSL && response.checkedSL[1] ? response.checkedSL[1].length : 0,
                    response.sales ? response.sales.length - (response.checkedSL && response.checkedSL[1] ? response.checkedSL[1].length : 0) : 0,
                ],
                backgroundColor: [
                    'rgba(255,0,0,0.5)',
                    'rgba(236,236,236,0.5)',
                    'rgba(255,129,0,0.5)',
                    'rgba(236,236,236,0.5)',
                    'rgba(240,255,0,0.5)',
                    'rgba(236,236,236,0.5)',
                    'rgba(41,255,46,0.5)',
                    'rgba(236,236,236,0.5)',
                    'rgba(31,77,255,0.5)',
                    'rgba(236,236,236,0.5)',
                ],
                borderColor: [
                    'rgb(255,0,0)',
                    'rgb(255,0,0)',
                    'rgb(255,129,0)',
                    'rgb(255,129,0)',
                    'rgb(240,255,0)',
                    'rgb(240,255,0)',
                    'rgb(41,255,46)',
                    'rgb(41,255,46)',
                    'rgb(31,77,255)',
                    'rgb(31,77,255)',
                ],
            }],
            labels: [
                '인사부(확인)',
                '인사부(미확인)',
                '관리부(확인)',
                '관리부(미확인)',
                '홍보부(확인)',
                '홍보부(미확인)',
                '지원부(확인)',
                '지원부(미확인)',
                '영업부(확인)',
                '영업부(미확인)',
            ]
        };
    }

    function createDoughnutPieOptions() {
        return {
            responsive: true,
            animation: {
                animateScale: true,
                animateRotate: true
            }
        };
    }

    function handleChartClick(evt, doughnutChart, response) {
        var activePoints = doughnutChart.getElementsAtEventForMode(evt, 'nearest', { intersect: true }, false);
        if (activePoints.length > 0) {
            var chartElement = activePoints[0];
            var datasetIndex = chartElement._datasetIndex;
            var index = chartElement._index;

            if (doughnutChart.data.labels && doughnutChart.data.datasets[datasetIndex]) {

                var label = response.department[Math.floor(index / 2)];
                $('#chartInfoLabel').text(label);

                var users;
                switch (index) {
                    case 0: users = response.checkedHR[1]; break;
                    case 1: users = response.checkedHR[0]; break;
                    case 2: users = response.checkedMG[1]; break;
                    case 3: users = response.checkedMG[0]; break;
                    case 4: users = response.checkedRT[1]; break;
                    case 5: users = response.checkedRT[0]; break;
                    case 6: users = response.checkedSP[1]; break;
                    case 7: users = response.checkedSP[0]; break;
                    case 8: users = response.checkedSL[1]; break;
                    case 9: users = response.checkedSL[0]; break;
                }

                $('#User').empty();
                if (users && users.length > 0) {
                    users.forEach(function(user) {
                        var listItem = '<li style="margin-left:20px; margin-top:10px">'+user.userName+'<span style="color:#fe7c96; font-size:12px; font-weight: bold; margin-left:2em">'+ user.positionName +'</span></li>'
                        $('#User').append(listItem);
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

    // 권한 확인
    function getUserInfo() {
        return new Promise((resolve, reject) => {
            $.ajax({
                type: "GET",
                url: "/scan/auth",
                dataType: "json",
                success: function (data) {
                    resolve(data);
                },
                error: function (error) {
                    console.error("삭제 권한 없음", error);
                    reject(error);
                }
            });
        });
    }
});

