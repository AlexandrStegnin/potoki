let KindOnProject = function () {
}

let KindProjectOnMonies = function () {
}

let CompanyProfit = function () {
}

let InvestorProfit = function () {
}

let Profit = function () {
}

KindOnProject.prototype = {
    facility: '',
    login: '',
    givenCash: 0.0,
    projectCoast: 0.0,
    percent: 0.0,
    build: function (facility, login, givenCash, projectCoast, percent) {
        this.facility = facility;
        this.login = login;
        this.givenCash = givenCash;
        this.projectCoast = projectCoast;
        this.percent = percent;
    }
}

KindProjectOnMonies.prototype = {
    facility: '',
    login: '',
    percent: 0.0,
    build: function (facility, login, percent) {
        this.facility = facility;
        this.login = login;
        this.percent = percent;
    }
}

CompanyProfit.prototype = {
    yearSale: '',
    profit: 0.0,
    build: function (dateSale, profit) {
        this.yearSale = dateSale;
        this.profit = profit;
    }
}

InvestorProfit.prototype = {
    yearSale: '',
    profit: 0.0,
    build: function (yearSale, profit) {
        this.yearSale = yearSale;
        this.profit = profit;
    }
}

Profit.prototype = {
    yearSale: '',
    profit: 0.0,
    login: '',
    investorProfit: 0.0,
    build: function (yearSale, profit, login, investorProfit) {
        this.yearSale = yearSale;
        this.profit = profit;
        this.login = login;
        this.investorProfit = investorProfit;
    }
}

jQuery(document).ready(function ($) {
    getUnionProfit();
    getKindOnProject('investor007');
});

/**
 * Вложения по проектам
 *
 * @param kinds
 */
function prepareBarChart(kinds) {
    let ctx = document.getElementById('barChart').getContext('2d');
    let myChart;
    let options = {
        responsive: true,
        maintainAspectRatio: true,
        title: {
            display: true,
            text: 'ВЛОЖЕНИЯ ПО ПРОЕКТАМ',
            fontSize: 12
        },
        tooltips: {
            enabled: false
        },
        hover: {
            animationDuration: 0
        },
        scales: {
            xAxes: [{
                ticks: {
                    beginAtZero: true,
                    display: false
                },
                scaleLabel: {
                    display: false
                },
                gridLines: {
                    display: false
                },
                stacked: true
            }],
            yAxes: [{
                gridLines: {
                    display: false,
                    color: "#fff",
                    zeroLineColor: "#fff",
                    zeroLineWidth: 0
                },
                stacked: true,
                /* Keep y-axis width proportional to overall chart width */
                afterFit: function(scale) {
                    let chartWidth = scale.chart.width;
                    scale.width = chartWidth * 0.20;
                }
            }]
        },
        legend: {
            display: true,
            labels: {
                fontSize: 12,
                fontStyle: 'bold'
            }
        },
        animation: {
            onComplete: function () {
                let chartInstance = this.chart;
                let ctx = chartInstance.ctx;
                let width = chartInstance.width;
                let size = Math.round(width / 96);
                ctx.font = "" + size + "px 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"

                ctx.textAlign = "left";
                ctx.fillStyle = "#000000";
                ctx.textBaseline = 'middle';

                Chart.helpers.each(this.data.datasets.forEach(function (dataset, i) {
                    let meta = chartInstance.controller.getDatasetMeta(i);
                    Chart.helpers.each(meta.data.forEach(function (bar, index) {
                        let data = dataset.data[index];
                        ctx.fillText(data.toLocaleString(), bar._model.x + 5, bar._model.y);
                    }), this)
                }), this);
            }
        }
    };

    let labels = [];
    let projectCoasts = [];
    let myCash = [];
    $.each(kinds, function (ind, el) {
        let kind = new KindOnProject();
        kind.build(el.facility, el.login, el.givenCash, el.projectCoast, el.percent);
        labels.push(kind.facility);
        projectCoasts.push(el.projectCoast);
        myCash.push(el.givenCash);
    });

    myChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            labels: labels,

            datasets: [{
                label: 'МОИ ВЛОЖЕНИЯ',
                data: myCash,
                backgroundColor: "#0d345d"
            }, {
                label: 'СТОИМОСТЬ ПРОЕКТА',
                data: projectCoasts,
                backgroundColor: "#95B3D7"
            }]
        },

        options: options,
        plugins: [{
            beforeDraw: function(c) {
                let chartHeight = c.chart.height;
                c.scales['y-axis-0'].options.ticks.minor.fontSize = chartHeight * (3 / 100); //fontSize: 3% of canvas height
            }
        }]
    });
    myChart.options.title.fontSize = Math.round(myChart.width / 48);
    myChart.options.legend.labels.fontSize = myChart.options.title.fontSize / 2 * 1.5;
    myChart.update();
}

/**
 * Мои вложения
 *
 * @param kinds
 */
function prepareInvestedBarChart(kinds) {
    let myChart;
    let options = {
        responsive: true,
        tooltips: {
            enabled: false
        },
        hover: {
            animationDuration: 0
        },
        scales: {
            xAxes: [{
                ticks: {
                    beginAtZero: true,
                    display: false
                },
                scaleLabel: {
                    display: false
                },
                gridLines: {
                    display: false
                }
            }],
            yAxes: [{
                gridLines: {
                    display: false
                },
                /* Keep y-axis width proportional to overall chart width */
                afterFit: function(scale) {
                    let chartWidth = scale.chart.width;
                    scale.width = chartWidth * 0.20;
                }
            }]
        },
        legend: {
            display: false
        },
        title: {
            display: true,
            text: 'МОИ ВЛОЖЕНИЯ',
            fontSize: 24
        },
        animation: {
            onComplete: function () {
                let chartInstance = this.chart;
                let ctx = chartInstance.ctx;
                let width = chartInstance.width;
                let size = Math.round(width / 96);
                ctx.font = "" + size + "px 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                ctx.textAlign = "left";
                ctx.fillStyle = "#000000";
                ctx.textBaseline = 'middle';

                Chart.helpers.each(this.data.datasets.forEach(function (dataset, i) {
                    let meta = chartInstance.controller.getDatasetMeta(i);
                    Chart.helpers.each(meta.data.forEach(function (bar, index) {
                        let data = dataset.data[index];
                        ctx.fillText(data.toLocaleString(), bar._model.x + 5, bar._model.y);
                    }), this)
                }), this);
            }
        }
    };

    let labels = [];
    let myCash = [];
    $.each(kinds, function (ind, el) {
        let kind = new KindOnProject();
        kind.build(el.facility, el.login, el.givenCash, el.projectCoast, el.percent);
        labels.push(kind.facility);
        myCash.push(el.givenCash);
    });

    let ctx = document.getElementById('investedBarChart').getContext('2d');
    myChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            labels: labels,
            datasets: [{
                data: myCash,
                backgroundColor: "#0D345D"
            }]
        },
        options: options,
        plugins: [{
            beforeDraw: function(c) {
                let chartHeight = c.chart.height;
                c.scales['y-axis-0'].options.ticks.minor.fontSize = chartHeight * (3 / 100); //fontSize: 3% of canvas height
            }
        }]
    });
    myChart.options.title.fontSize = Math.round(myChart.width / 48);
    myChart.update();
}

/**
 * Заработок компании
 *
 * @param profits
 */
function prepareCompanyProfitBarChart(profits) {
    let myChart;
    let ctx = document.getElementById('profitBarChart').getContext('2d');
    let options = {
        responsive: true,
        title: {
            display: true,
            text: 'ЗАРАБОТОК КОМПАНИИ',
            fontSize: 24
        },
        tooltips: {
            enabled: false
        },
        hover: {
            animationDuration: 0
        },
        scales: {
            xAxes: [{
                ticks: {
                    beginAtZero: true,
                    display: true
                },
                scaleLabel: {
                    display: false
                },
                gridLines: {
                    display: false
                },
                stacked: true
            }],
            yAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    display: false
                },
                stacked: true
            }]
        },
        legend: {
            display: true,
            labels: {
                fontSize: 12,
                fontStyle: 'bold'
            }
        },

        animation: {
            onComplete: function () {
                let chartInstance = this.chart;
                let ctx = chartInstance.ctx;
                let width = chartInstance.width;
                let size = Math.round(width / 96);
                ctx.font = "" + size + "px 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif"
                ctx.textAlign = "center";
                ctx.fillStyle = "#000000";
                ctx.textBaseline = 'bottom';

                Chart.helpers.each(this.data.datasets.forEach(function (dataset, i) {
                    let meta = chartInstance.controller.getDatasetMeta(i);
                    Chart.helpers.each(meta.data.forEach(function (bar, index) {
                        let data = dataset.data[index];
                        if (data === null) {
                            data = 0;
                        }
                        ctx.fillText(data.toLocaleString(), bar._model.x, bar._model.y - 5);
                    }), this)
                }), this);
            }
        }
    };

    let labels = [];
    let companySums = [];
    let myCash = [];
    $.each(profits, function (ind, el) {
        let profit = new Profit();
        profit.build(el.yearSale, el.profit, el.login, el.investorProfit);
        labels.push(profit.yearSale);
        companySums.push(profit.profit);
        myCash.push(profit.investorProfit);
    });

    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,

            datasets: [{
                label: 'ЗАРАБОТАНО ДЛЯ ВАС',
                data: myCash,
                backgroundColor: "#0d345d"
            }, {
                label: 'ЗАРАБОТАНО ДЛЯ ВСЕХ',
                data: companySums,
                backgroundColor: "#95B3D7"
            }]
        },
        options: options,
    });
    myChart.options.title.fontSize = Math.round(myChart.width / 48);
    myChart.options.legend.labels.fontSize = myChart.options.title.fontSize / 2 * 1.5;
    myChart.update();
}


function getKindOnProject(login) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "GET",
        contentType: "application/json;charset=utf-8",
        url: "kind-on-project",
        // data: JSON.stringify(search),
        // dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareBarChart(data);
            prepareInvestedBarChart(data);
        })
        .fail(function (e) {
            console.log(e);
        });
}

function getUnionProfit() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "GET",
        contentType: "application/json;charset=utf-8",
        url: "union-profit",
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareCompanyProfitBarChart(data);
        })
        .fail(function (e) {
            console.log(e);
        });
}
