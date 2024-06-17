$(function () {
    /* ChartJS
     * -------
     * Data and config for chartjs
     */
    'use strict';
    var data = {
        labels: ["아시아", "유럽", "아프리카", "아메리카", "오세아니아", "남극"],
        datasets: [{
            label: '판매량 ',
            data: [10, 19, 3, 5, 2, 3],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1,
            fill: false
        }]
    };
    var dataDark = {
        labels: ["아시아", "유럽", "아프리카", "아메리카", "오세아니아", "남극"],
        datasets: [{
            label: '판매량 ', //# of Votes
            data: [10, 19, 3, 5, 2, 3],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1,
            fill: false
        }]
    };
    var multiLineData = {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: [{
            label: 'Dataset 1',
            data: [12, 19, 3, 5, 2, 3],
            borderColor: [
                '#587ce4'
            ],
            borderWidth: 2,
            fill: false
        },
            {
                label: 'Dataset 2',
                data: [5, 23, 7, 12, 42, 23],
                borderColor: [
                    '#ede190'
                ],
                borderWidth: 2,
                fill: false
            },
            {
                label: 'Dataset 3',
                data: [15, 10, 21, 32, 12, 33],
                borderColor: [
                    '#f44252'
                ],
                borderWidth: 2,
                fill: false
            }
        ]
    };
    var options = {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        },
        legend: {
            display: false
        },
        elements: {
            point: {
                radius: 0
            }
        }

    };
    var optionsDark = {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                },
                gridLines: {
                    color: '#322f2f',
                    zeroLineColor: '#322f2f'
                }
            }],
            xAxes: [{
                ticks: {
                    beginAtZero: true
                },
                gridLines: {
                    color: '#322f2f',
                }
            }],
        },
        legend: {
            display: false
        },
        elements: {
            point: {
                radius: 0
            }
        }

    };
    var doughnutPieData = {
        datasets: [{
            data: [30, 40, 30],
            backgroundColor: [
                'rgba(255, 99, 132, 0.5)',
                'rgba(54, 162, 235, 0.5)',
                'rgba(255, 206, 86, 0.5)',
                'rgba(75, 192, 192, 0.5)',
                'rgba(153, 102, 255, 0.5)',
                'rgba(255, 159, 64, 0.5)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
        }],

        // These labels appear in the legend and in the tooltips when hovering different arcs
        labels: [
            'Pink',
            'Blue',
            'Yellow',
        ]
    };
    var doughnutPieOptions = {
        responsive: true,
        animation: {
            animateScale: true,
            animateRotate: true
        }
    };
    var areaData = {
        labels: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
        datasets: [{
            data: [2, 1, 3, 6, 5, 7, 3, 5, 8, 5, 7, 8],
            label: '월별 매출:'+ data +' 천만',
            backgroundColor: [
                'rgba(54, 162, 235, 0.2)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)',
                'rgba(0,68,115,0.2)',
                'rgb(211,0,43)',
                'rgba(229,166,0,0.2)',
                'rgba(0,255,255,0.2)',
                'rgba(61,0,183,0.2)',
                'rgba(255,130,3,0.2)'
            ],
            borderColor: [
                'rgba(54, 162, 235, 1)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)',
                'rgb(166,219,255)',
                'rgb(250,174,189)',
                'rgb(252,229,182)',
                'rgb(173,253,253)',
                'rgb(197,175,245)',
                'rgb(246,200,154)'
            ],
            borderWidth: 1,
            fill: true, // 3: no fill
        }]
    };

    var areaDataDark = {
        labels: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
        datasets: [{
            data: [2, 1, 3, 6, 5, 7, 3, 5, 8, 5, 7, 8],
            label: '월별 매출 :' + data +'천만',
            backgroundColor: [
                'rgba(54, 162, 235, 0.2)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)',
                'rgba(0,68,115,0.2)',
                'rgb(211,0,43)',
                'rgba(229,166,0,0.2)',
                'rgba(0,255,255,0.2)',
                'rgba(61,0,183,0.2)',
                'rgba(255,130,3,0.2)'
            ],
            borderColor: [
                'rgba(54, 162, 235, 1)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)',
                'rgb(166,219,255)',
                'rgb(250,174,189)',
                'rgb(252,229,182)',
                'rgb(173,253,253)',
                'rgb(197,175,245)',
                'rgb(246,200,154)'
            ],
            borderWidth: 1,
            fill: true, // 3: no fill
        }]
    };

    var areaOptions = {
        plugins: {
            filler: {
                propagate: true
            }
        }
    }

    var areaOptionsDark = {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                },
                gridLines: {
                    color: '#2f3032',
                    zeroLineColor: '#2f3032'
                }
            }],
            xAxes: [{
                ticks: {
                    beginAtZero: true
                },
                gridLines: {
                    color: '#2f3032',
                }
            }],
        },
        plugins: {
            filler: {
                propagate: true
            }
        }
    }

    var multiAreaData = {
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        datasets: [{
            label: 'Facebook',
            data: [8, 11, 13, 15, 12, 13, 16, 15, 13, 19, 11, 14],
            borderColor: ['rgba(255, 99, 132, 0.5)'],
            backgroundColor: ['rgba(255, 99, 132, 0.5)'],
            borderWidth: 1,
            fill: true
        },
            {
                label: 'Twitter',
                data: [7, 17, 12, 16, 14, 18, 16, 12, 15, 11, 13, 9],
                borderColor: ['rgba(54, 162, 235, 0.5)'],
                backgroundColor: ['rgba(54, 162, 235, 0.5)'],
                borderWidth: 1,
                fill: true
            },
            {
                label: 'Linkedin',
                data: [6, 14, 16, 20, 12, 18, 15, 12, 17, 19, 15, 11],
                borderColor: ['rgba(255, 206, 86, 0.5)'],
                backgroundColor: ['rgba(255, 206, 86, 0.5)'],
                borderWidth: 1,
                fill: true
            }
        ]
    };

    var multiAreaOptions = {
        plugins: {
            filler: {
                propagate: true
            }
        },
        elements: {
            point: {
                radius: 0
            }
        },
        scales: {
            xAxes: [{
                gridLines: {
                    display: false
                }
            }],
            yAxes: [{
                gridLines: {
                    display: false
                }
            }]
        }
    }

    var scatterChartData = {
        datasets: [{
            label: 'First Dataset',
            data: [{
                x: -10,
                y: 0
            },
                {
                    x: 0,
                    y: 3
                },
                {
                    x: -25,
                    y: 5
                },
                {
                    x: 40,
                    y: 5
                }
            ],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)'
            ],
            borderWidth: 1
        },
            {
                label: 'Second Dataset',
                data: [{
                    x: 10,
                    y: 5
                },
                    {
                        x: 20,
                        y: -30
                    },
                    {
                        x: -25,
                        y: 15
                    },
                    {
                        x: -10,
                        y: 5
                    }
                ],
                backgroundColor: [
                    'rgba(54, 162, 235, 0.2)',
                ],
                borderColor: [
                    'rgba(54, 162, 235, 1)',
                ],
                borderWidth: 1
            }
        ]
    }

    var scatterChartDataDark = {
        datasets: [{
            label: 'First Dataset',
            data: [{
                x: -10,
                y: 0
            },
                {
                    x: 0,
                    y: 3
                },
                {
                    x: -25,
                    y: 5
                },
                {
                    x: 40,
                    y: 5
                }
            ],
            backgroundColor: [
                'rgba(31,40,250,0.2)'
            ],
            borderColor: [
                'rgba(31,40,250,0.2)'
            ],
            borderWidth: 1
        },
            {
                label: 'Second Dataset',
                data: [{
                    x: 10,
                    y: 5
                },
                    {
                        x: 20,
                        y: -30
                    },
                    {
                        x: -25,
                        y: 15
                    },
                    {
                        x: -10,
                        y: 5
                    }
                ],
                backgroundColor: [
                    'rgba(2,81,133,0.2)',
                ],
                borderColor: [
                    'rgb(3,87,136)',
                ],
                borderWidth: 1
            }
        ]
    }

    var scatterChartOptions = {
        scales: {
            xAxes: [{
                type: 'linear',
                position: 'bottom'
            }]
        }
    }

    var scatterChartOptionsDark = {
        scales: {
            xAxes: [{
                type: 'linear',
                position: 'bottom',
                gridLines: {
                    color: '#322f2f',
                    zeroLineColor: '#322f2f'
                }
            }],
            yAxes: [{
                gridLines: {
                    color: '#322f2f',
                    zeroLineColor: '#322f2f'
                }
            }]
        }
    }
    // Get context with jQuery - using jQuery's .get() method.
    if ($("#barChart").length) {
        var barChartCanvas = $("#barChart").get(0).getContext("2d");
        // This will get the first returned node in the jQuery collection.
        var barChart = new Chart(barChartCanvas, {
            type: 'bar',
            data: data,
            options: options
        });
    }

    if ($("#barChartDark").length) {
        var barChartCanvasDark = $("#barChartDark").get(0).getContext("2d");
        // This will get the first returned node in the jQuery collection.
        var barChartDark = new Chart(barChartCanvasDark, {
            type: 'bar',
            data: dataDark,
            options: optionsDark
        });
    }

    if ($("#lineChart").length) {
        var lineChartCanvas = $("#lineChart").get(0).getContext("2d");
        var lineChart = new Chart(lineChartCanvas, {
            type: 'line',
            data: data,
            options: options
        });
    }

    if ($("#lineChartDark").length) {
        var lineChartCanvasDark = $("#lineChartDark").get(0).getContext("2d");
        var lineChartDark = new Chart(lineChartCanvasDark, {
            type: 'line',
            data: dataDark,
            options: optionsDark
        });
    }

    if ($("#linechart-multi").length) {
        var multiLineCanvas = $("#linechart-multi").get(0).getContext("2d");
        var lineChart = new Chart(multiLineCanvas, {
            type: 'line',
            data: multiLineData,
            options: options
        });
    }

    if ($("#areachart-multi").length) {
        var multiAreaCanvas = $("#areachart-multi").get(0).getContext("2d");
        var multiAreaChart = new Chart(multiAreaCanvas, {
            type: 'line',
            data: multiAreaData,
            options: multiAreaOptions
        });
    }

    if ($("#doughnutChart").length) {
        var doughnutChartCanvas = $("#doughnutChart").get(0).getContext("2d");
        var doughnutChart = new Chart(doughnutChartCanvas, {
            type: 'doughnut',
            data: doughnutPieData,
            options: doughnutPieOptions
        });
    }

    if ($("#pieChart").length) {
        var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
        var pieChart = new Chart(pieChartCanvas, {
            type: 'pie',
            data: doughnutPieData,
            options: doughnutPieOptions
        });
    }

    if ($("#areaChart").length) {
        var areaChartCanvas = $("#areaChart").get(0).getContext("2d");
        var areaChart = new Chart(areaChartCanvas, {
            type: 'line',
            data: areaData,
            options: areaOptions
        });
    }

    if ($("#areaChartDark").length) {
        var areaChartCanvas = $("#areaChartDark").get(0).getContext("2d");
        var areaChart = new Chart(areaChartCanvas, {
            type: 'line',
            data: areaDataDark,
            options: areaOptionsDark
        });
    }

    if ($("#scatterChart").length) {
        var scatterChartCanvas = $("#scatterChart").get(0).getContext("2d");
        var scatterChart = new Chart(scatterChartCanvas, {
            type: 'scatter',
            data: scatterChartData,
            options: scatterChartOptions
        });
    }

    if ($("#scatterChartDark").length) {
        var scatterChartCanvas = $("#scatterChartDark").get(0).getContext("2d");
        var scatterChart = new Chart(scatterChartCanvas, {
            type: 'scatter',
            data: scatterChartDataDark,
            options: scatterChartOptionsDark
        });
    }

    if ($("#browserTrafficChart").length) {
        var doughnutChartCanvas = $("#browserTrafficChart").get(0).getContext("2d");
        var doughnutChart = new Chart(doughnutChartCanvas, {
            type: 'doughnut',
            data: browserTrafficData,
            options: doughnutPieOptions
        });
    }
});