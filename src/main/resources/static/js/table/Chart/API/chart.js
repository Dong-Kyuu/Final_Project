$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

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
            // 서버로부터 받은 데이터를 처리합니다.
            var doughnutPieData = {
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
                        'rgb(255,144,144)',
                        'rgb(255,129,0)',
                        'rgb(255,181,126)',
                        'rgb(240,255,0)',
                        'rgb(228,232,146)',
                        'rgb(41,255,46)',
                        'rgb(192,255,187)',
                        'rgb(31,77,255)',
                        'rgb(93,162,255)'
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

            var doughnutPieOptions = {
                responsive: true,
                animation: {
                    animateScale: true,
                    animateRotate: true
                }
            };

            if ($("#doughnutChart").length) {
                var doughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
                var doughnutChart = new Chart(doughnutChartCanvas, {
                    type: 'doughnut',
                    data: doughnutPieData,
                    options: doughnutPieOptions
                });

                $("#doughnutChart").click(function(evt) {
                    var activePoints = doughnutChart.getElementsAtEventForMode(evt, 'nearest', { intersect: true }, false);
                    console.log("Active Points: ", activePoints); // 추가된 로그

                    if (activePoints.length > 0) {
                        var chartElement = activePoints[0];
                        console.log("Chart Element: ", chartElement); // 추가된 로그

                        var datasetIndex = chartElement._datasetIndex; // _datasetIndex를 사용
                        var index = chartElement._index; // _index를 사용

                        console.log("Dataset Index: ", datasetIndex); // 추가된 로그
                        console.log("Index: ", index); // 추가된 로그
                        console.log("doughnutChart.data: ", doughnutChart.data); // 추가된 로그

                        // 데이터 접근 전에 데이터가 정의되어 있는지 확인
                        if (doughnutChart.data.labels && doughnutChart.data.datasets[datasetIndex]) {
                            var label = doughnutChart.data.labels[index];
                            var value = doughnutChart.data.datasets[datasetIndex].data[index];

                            console.log(response.department[1])
                            label = response.department[Math.floor(index / 2)]
                            $('#chartInfoLabel').text(label + " 목록");

                            var maxIndex = doughnutChart.data.labels.length - 1;
                            var users

                            if(index==0) {
                                users = response.checkedHR[1]
                            } else if(index==1) {
                                users = response.checkedHR[0]
                            } else if(index==2) {
                                users = response.checkedMG[1]
                            } else if(index==3) {
                                users = response.checkedMG[0]
                            } else if(index==4) {
                                users = response.checkedRT[1]
                            } else if(index==5) {
                                users = response.checkedRT[0]
                            } else if(index==6) {
                                users = response.checkedSP[1]
                            } else if(index==7) {
                                users = response.checkedSP[0]
                            } else if(index==8) {
                                users = response.checkedSL[1]
                            } else if(index==9) {
                                users = response.checkedSL[0]
                            }
                            $('#User').empty();
                            if (users && users.length > 0) {
                                users.forEach(function(user) {
                                    var listItem = '<li style="margin-left:20px; margin-top:20px">'+user.username+'<span style="color:#fe7c96; font-size:12px; font-weight: bold; margin-left:30px">'+ user.positionName +'</span></li>'
                                    $('#User').append(listItem);
                                });
                            } else {
                                $('#User').append('<li> 해당하는 사원이 없습니다. </li>')
                            }


                            // 모달 창 표시
                            $('#chartInfoModal').modal('show');
                        } else {
                            console.error("Data is undefined");
                        }
                    }
                });
            }
        },
        error: function(xhr, status, error) {
            // 요청이 실패했을 때의 처리를 정의합니다.
            console.error(error);
        }
    });




});