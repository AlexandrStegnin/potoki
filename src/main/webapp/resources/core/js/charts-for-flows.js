let KindOnProject = function () {
}

let KindProjectOnMonies = function () {
}

let CompanyProfit = function () {
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
    dateSale: '',
    underFacility: '',
    profit: 0.0,
    build: function (dateSale, underFacility, profit) {
        this.dateSale = dateSale;
        this.underFacility = underFacility;
        this.profit = profit;
    }
}

jQuery(document).ready(function ($) {
    getKindOnProject('investor007')
    getKindProjectOnAllMonies("investor007");
    getCompanyProfit();
});

/**
 * Вложения по проектам
 *
 * @param kinds
 */
function prepareBarChart(kinds) {
    let ctx = document.getElementById('barChart').getContext('2d');
    let barOptions_stacked = {
        title: {
            display: true,
            text: 'Вложения по проектам',
            fontSize: "24"
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
                ticks: {
                    fontFamily: "'Open Sans Bold', sans-serif",
                    fontSize: 11
                },
                stacked: true
            }]
        },
        legend: {
            display: true
        },

        animation: {
            onComplete: function () {
                let chartInstance = this.chart;
                let ctx = chartInstance.ctx;
                ctx.textAlign = "left";
                ctx.font = "9px Open Sans";
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
        },
        pointLabelFontFamily: "Open Sans Extra Bold",
        scaleFontFamily: "Open Sans Extra Bold",
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

    let myChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            labels: labels,

            datasets: [{
                label: 'Мои вложения',
                data: myCash,
                backgroundColor: "rgb(18,125,29)",
                hoverBackgroundColor: "rgb(53,100,50)"
            }, {
                label: 'Стоимость проекта',
                data: projectCoasts,
                backgroundColor: "rgb(186,232,121)",
                hoverBackgroundColor: "rgb(107,182,91)"
            }]
        },

        options: barOptions_stacked,
    });
}

/**
 * Доля средств в каждом проекте
 *
 * @param kinds
 */
function prepareDoughnutChart(kinds) {
    let labels = [];
    let myCash = [];
    let colors = [];
    $.each(kinds, function (ind, el) {
        let kind = new KindProjectOnMonies();
        kind.build(el.facility, el.login, el.percent);
        labels.push(kind.facility);
        myCash.push(el.percent);
        colors.push(getRandomColor());
    });

    let ctx = document.getElementById('doughnutChart').getContext('2d');
    let myDoughnutChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: myCash,
                backgroundColor: colors
            }],

        },
        options: {
            title: {
                display: true,
                text: 'Доля средств в каждом проекте',
                fontSize: "24"
            },
            animation: {
                onComplete: function () {
                    let chartInstance = this.chart;
                    let ctx = chartInstance.ctx;
                    ctx.textAlign = "center";
                    ctx.font = "9px Open Sans";
                    ctx.fillStyle = "#000000";

                    Chart.helpers.each(this.data.datasets.forEach(function (dataset, i) {
                        let meta = chartInstance.controller.getDatasetMeta(i);
                        Chart.helpers.each(meta.data.forEach(function (bar, index) {
                            let data = dataset.data[index];
                            ctx.fillText(data, 50, 50);
                        }), this)
                    }), this);
                }
            },
            labels: {},
            tooltips: {
                callbacks: {
                    title: function (tooltipItem, data) {
                        return data['labels'][tooltipItem[0]['index']];
                    },
                    label: function (tooltipItem, data) {
                        return ' ' + data['datasets'][0]['data'][tooltipItem['index']] + '%';
                    }
                }
            }
        }
    });
}

/**
 * Мои вложения
 *
 * @param kinds
 */
function prepareInvestedBarChart(kinds) {
    let options = {
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
                    display: false,
                    color: "#fff",
                    zeroLineColor: "#fff",
                    zeroLineWidth: 0
                },
                ticks: {
                    fontFamily: "'Open Sans Bold', sans-serif",
                    fontSize: 11
                }
            }]
        },
        legend: {
            display: false
        },
        title: {
            display: true,
            text: 'Мои вложения',
            fontSize: "24"
        },
        animation: {
            onComplete: function () {
                let chartInstance = this.chart;
                let ctx = chartInstance.ctx;
                ctx.textAlign = "left";
                ctx.font = "9px Open Sans";
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
        },
        pointLabelFontFamily: "Open Sans Extra Bold",
        scaleFontFamily: "Open Sans Extra Bold",
    };

    let labels = [];
    let myCash = [];
    let colors = [];
    $.each(kinds, function (ind, el) {
        let kind = new KindOnProject();
        kind.build(el.facility, el.login, el.givenCash, el.projectCoast, el.percent);
        labels.push(kind.facility);
        myCash.push(el.givenCash);
        colors.push(getRandomColor());
    });

    let ctx = document.getElementById('investedBarChart').getContext('2d');
    let investedBarChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Мои вложения',
                data: myCash,
                backgroundColor: colors
            }]
        },
        options: options
    });
}

/**
 * Доля средств в каждом проекте (TreeMapChart)
 * @see https://codepen.io/kurkle/pen/JqbzgQ
 * @param kinds
 */
function prepareTreeMapChart(kinds) {
    let options = {
        responsive: true,
        hover: {
            animationDuration: 0
        },
        scales: {
            xAxes: [{
                ticks: {
                    beginAtZero: true,
                    fontFamily: "'Open Sans Bold', sans-serif",
                    fontSize: 11
                },
                scaleLabel: {
                    display: false
                },
                gridLines: {}
            }],
            yAxes: [{
                gridLines: {
                    display: false,
                    color: "#fff",
                    zeroLineColor: "#fff",
                    zeroLineWidth: 0
                },
                ticks: {
                    fontFamily: "'Open Sans Bold', sans-serif",
                    fontSize: 11
                }
            }]
        },
        legend: {
            display: false
        },
        title: {
            display: true,
            text: 'Доля средств в каждом проекте',
            fontSize: "24"
        },
        tooltips: {
            callbacks: {
                title: function(item, data) {
                    return '';
                },
                label: function(item, data) {
                    let dataset = data.datasets[item.datasetIndex];
                    let dataItem = dataset.data[item.index];
                    let obj = dataItem._data;
                    let label = obj.facility;
                    return ' ' + label + ': ' + (dataItem.v).toLocaleString();
                }
            }
        },
        pointLabelFontFamily: "Open Sans Extra Bold",
        scaleFontFamily: "Open Sans Extra Bold",
    };

    let colors = [];
    let treeKinds = [];
    $.each(kinds, function (ind, el) {
        let kind = new KindOnProject();
        kind.build(el.facility, el.login, el.givenCash, el.projectCoast, el.percent);
        colors.push(getRandomColor());
        treeKinds.push(kind);
    });
    let ctx = document.getElementById('investedTreeMapChart').getContext('2d');
    let treeMapChart = new Chart(ctx, {
        type: 'treemap',
        data: {
            datasets: [{
                tree: treeKinds,
                key: 'givenCash',
                groups: ['facility'],
                fontColor: '#000',
                fontFamily: 'serif',
                fontSize: 12,
                fontStyle: 'normal',
                backgroundColor: colors
            }]
        },
        options: options
    });
}

/**
 * Заработок компании
 */
function prepareCompanyProfit(profits) {
    let labels = [];
    let profitSums = [];
    $.each(profits, function (ind, el) {
        let profit = new CompanyProfit();
        let label = [];
        profit.build(el.dateSale, el.underFacility, el.profit);
        let underFacilitiesSeparated = profit.underFacility.split('_');
        let cnt = underFacilitiesSeparated.length;
        label.push(new Date(profit.dateSale).toLocaleDateString());
        for (let i = 0; i < cnt; i++) {
            label.push(underFacilitiesSeparated[i]);
        }
        labels.push(label);
        profitSums.push(profit.profit);
    });

    let options = {
        tooltips: {
            enabled: true,
            callbacks: {
                title: function (tooltipItem, data) {
                    return data['labels'][tooltipItem[0]['index']];
                },
                label: function (tooltipItem, data) {
                    return data['datasets'][0]['data'][tooltipItem['index']].toLocaleString() + ' руб.';
                }
            }
        },
        hover: {
            animationDuration: 0
        },
        scales: {
            xAxes: [{
                ticks: {
                    beginAtZero: true,
                    display: true,
                    stepSize: 0,
                    min: 0,
                    autoSkip: false,
                    fontSize: 11,
                    maxRotation: 0,
                    minRotation: 0
                },
                scaleLabel: {
                    display: false
                },
                gridLines: {
                    display: false,
                    lineWidth: 1,
                    zeroLineWidth: 1,
                    zeroLineColor: '#666666',
                    drawTicks: false
                }
            }],
            yAxes: [{
                ticks: {
                    display: false
                },
                gridLines: {
                    display: false
                }
            }]
        },
        legend: {
            display: false
        },
        title: {
            display: true,
            text: 'Заработок компании',
            fontSize: "24"
        },
        animation: {
            onComplete: function () {
                let chartInstance = this.chart;
                let ctx = chartInstance.ctx;
                ctx.textAlign = "left";
                ctx.font = "9px Open Sans";
                ctx.fillStyle = "#000000";
                ctx.textBaseline = 'bottom';

                Chart.helpers.each(this.data.datasets.forEach(function (dataset, i) {
                    let meta = chartInstance.controller.getDatasetMeta(i);
                    Chart.helpers.each(meta.data.forEach(function (bar, index) {
                        let data = dataset.data[index];
                        ctx.fillText(data.toLocaleString(), bar._model.x + 10, bar._model.y - 10);
                    }), this)
                }), this);
            }
        },
        pointLabelFontFamily: "Open Sans Extra Bold",
        scaleFontFamily: "Open Sans Extra Bold",
        spanGaps: true,
        responsive: true,
        maintainAspectRatio: true
    };

    let ctx = document.getElementById('companyProfitChart').getContext('2d');
    let companyProfitChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                data: profitSums,
                fill: false,
                lineTension: 0,
                borderColor: 'blue',
                backgroundColor: 'transparent',
                pointBorderColor: 'blue',
                pointBackgroundColor: 'rgb(0,63,229)',
                pointRadius: 5,
                pointHoverRadius: 10,
                pointHitRadius: 30,
                pointBorderWidth: 2,
                pointStyle: 'rectRounded'
            }]
        },
        options: options
    });
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
            prepareTreeMapChart(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Данные загружены!');
        });
}

function getKindProjectOnAllMonies(login) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "GET",
        contentType: "application/json;charset=utf-8",
        url: "kind-project-on-monies",
        // data: JSON.stringify(search),
        // dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareDoughnutChart(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Данные загружены!');
        });
}

function getCompanyProfit() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "GET",
        contentType: "application/json;charset=utf-8",
        url: "company-profit",
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareCompanyProfit(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Данные загружены!');
        });
}

function getRandomColor() {
    let colorR = Math.floor((Math.random() * 256));
    let colorG = Math.floor((Math.random() * 256));
    let colorB = Math.floor((Math.random() * 256));
    return "rgb(" + colorR + "," + colorG + "," + colorB + ")";
}

Chart.plugins.register({
    afterDatasetsDraw: function (chartInstance, easing) {
        if (chartInstance.config.type === 'doughnut') {
            // To only draw at the end of animation, check for easing === 1
            let ctx = chartInstance.chart.ctx;
            chartInstance.data.datasets.forEach(function (dataset, i) {
                let meta = chartInstance.getDatasetMeta(i);
                if (!meta.hidden) {
                    meta.data.forEach(function (element, index) {
                        // Draw the text in black, with the specified font
                        ctx.fillStyle = 'black';
                        let fontSize = 12;
                        let fontStyle = 'normal';
                        let fontFamily = 'Open Sans';
                        ctx.font = Chart.helpers.fontString(fontSize, fontStyle, fontFamily);
                        // Just naively convert to string for now
                        let dataString = dataset.data[index].toString();
                        // Make sure alignment settings are correct
                        ctx.textAlign = 'center';
                        ctx.textBaseline = 'middle';
                        let position = element.tooltipPosition();
                        ctx.fillText(dataString + '%', position.x, position.y - (fontSize / 2));
                    });
                }
            });
        }
    }
});
