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
    let login = $('#investorLogin').val();
    if (login === '') login = null;
    getUnionProfit(login);
    getKindOnProject(login);
});

/**
 * Вложения по проектам
 *
 * @param kinds
 */
function prepareBarChart(kinds) {
    if (kinds.length === 0) {
        $('#barChartContainer').css('display', 'none');
        return;
    } else {
        $('#barChartContainer').css('display', 'block');
    }
    let ctx = document.getElementById('barChart').getContext('2d');
    let myChart;

    let labels = [];
    let projectCoasts = [];
    let myCash = [];
    let maxProjectCoast = 0;
    $.each(kinds, function (ind, el) {
        let kind = new KindOnProject();
        kind.build(el.facility, el.login, el.givenCash, el.projectCoast, el.percent);
        labels.push(kind.facility);
        projectCoasts.push(kind.projectCoast);
        myCash.push(kind.givenCash);
        if (maxProjectCoast < kind.projectCoast) {
            maxProjectCoast = kind.projectCoast;
        }
    });
    maxProjectCoast = maxProjectCoast + (maxProjectCoast * 0.15);
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
                    display: false,
                    min: 0,
                    max: maxProjectCoast
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
                    if (kinds.length === 1) {
                        scale.width = chartWidth * 0.1 / kinds.length;
                    } else {
                        scale.width = chartWidth * 0.2;
                    }
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
    if (kinds.length === 0) {
        $('#investedBarChartContainer').css('display', 'none');
        return;
    } else {
        $('#investedBarChartContainer').css('display', 'block');
    }
    let labels = [];
    let myCash = [];
    let maxCash = 0;
    $.each(kinds, function (ind, el) {
        let kind = new KindOnProject();
        kind.build(el.facility, el.login, el.givenCash, el.projectCoast, el.percent);
        labels.push(kind.facility);
        myCash.push(kind.givenCash);
        if (maxCash < kind.givenCash) {
            maxCash = kind.givenCash;
        }
    });
    $('#balanceText').text(myCash.toLocaleString());
    maxCash = maxCash + (maxCash * 0.15);

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
                    display: false,
                    max: maxCash
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
                    if (kinds.length === 1) {
                        scale.width = chartWidth * 0.1 / kinds.length;
                    } else {
                        scale.width = chartWidth * 0.2;
                    }
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

    let labels = [];
    let companySums = [];
    let myCash = [];
    let maxCompanySum = 0;
    $.each(profits, function (ind, el) {
        let profit = new Profit();
        profit.build(el.yearSale, el.profit, el.login, el.investorProfit);
        labels.push(profit.yearSale);
        companySums.push(profit.profit);
        myCash.push(profit.investorProfit);
        if (maxCompanySum < profit.profit) {
            maxCompanySum = profit.profit;
        }
    });

    maxCompanySum = maxCompanySum + (maxCompanySum * 0.1);

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
                    display: false,
                    max: maxCompanySum
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
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "kind-on-project",
        data: JSON.stringify(login),
        dataType: 'json',
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

function getUnionProfit(login) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "union-profit",
        data: JSON.stringify(login),
        dataType: 'json',
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
