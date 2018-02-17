var monthNames = [
    'Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сент', 'Окт', 'Ноя', 'Дек'
];
var monthLongNames = [
    'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'
];

var loop = 0;
var maxFacility = '';

jQuery(document).ready(function ($) {

    $(document).on('click', '#showInvestsDetails', function (e) {
        e.preventDefault();

        $('#investmentsAll').removeClass('js-active');
        $('#investments').addClass('js-active');
        var facility = $(this).parent().parent().children(':first').text();

        $('#investmentsDetailsTitle').html('Ваши вложения в ' + facility);
        var totalSum = 0;
        var dataCash;
        $.each($('#investedDetailsTable').find('.simplebar-scroll-content').find('.simplebar-content').find('.row.flex-vcenter'), function (ind, el) {
            if ($(this).children(':last').data('facility') === facility) {
                $(this).show();
                dataCash = $(this).find('[data-cash]');
                totalSum += dataCash.data('cash');
            } else {
                $(this).hide();
            }
        });

        $('#totalSumFooter').find('span').empty().html('Итого за выбранный период: ' +
            new Intl.NumberFormat('ru-RU', {
                minimumFractionDigits: 0
            }).format(Math.round(totalSum)) + ' руб.');

    });

    $(document).on('click', '#closeInvestmentsDetails', function (e) {
        e.preventDefault();
        $('#investments').removeClass('js-active');
        $('#investmentsAll').addClass('js-active');
    });

    blurElement($('.out'), 4);
    if (checkLocalStorage()) {
        if (localStorage.getItem('firstLoad')) {
            localStorage.setItem('firstLoad', 'true')
        } else {
            localStorage.clear();
            localStorage.setItem('firstLoad', 'false')
        }
    }

    $('#readAnnex').css('z-index', '-1000001');
    $('#look').attr('disabled', true);
    $(document).on('change', 'input[type="checkbox"]:checked', function () {
        var userId = $(this).data('user-id');
        var annex = {
            id: $(this).data('annex-id'),
            annexName: $(this).data('annex-name')
        };
        $(this).attr('disabled', 'disabled');
        var d = new Date();
        var month = ('' + d.getMonth() + 1).length < 2 ? '0' + (d.getMonth() + 1) + '' : '' + (d.getMonth() + 1) + '';
        var day = ('' + d.getDate()).length < 2 ? '0' + d.getDate() + '' : '' + d.getDate() + '';
        var year = d.getFullYear();
        var output = d.toLocaleDateString();//day + '.' + month + '.' + year;
        $('#annexDate-' + annex.id).html(output);
        var trId = $(this).closest('tr').attr('id');
        setAnnexRead(trId, userId, annex, 1);
    });

    window.chartColors = {
        orange: '#f86b4f',
        sea: '#8b94ea',
        yellow: '#ffcd56',
        red: '#ff6384',
        green: '#80c966',
        blue: '#8bc4ea',
        purple: '#d520b5',
        grey: '#cacaca'
    };
    onStartUp();
    prepareToggle();

    $(document).on('click', '#searchIncomesByDate', function (e) {
        e.preventDefault();
        var month = parseInt($('#months').find('option:selected').val());
        var year = $('#years').find('option:selected').text();
        var fullDate = new Date(parseInt(year), month, 1);

        getInvestorsFlowsList('max', fullDate);
    });

    $(document).on('click', '#ext', function () {
        window.location.href = '/logout'
    });

    //$('#incomesAndExpenses').selectric();

    //$(function(){$('select').selectric();$('select#incomesAndExpenses').selectric({onChange: function (element) {var mainFlows;var facility = $('.inner__row.inner__row_costs.flex-vhcenter').find('.selectric').find('span.label').text();if(checkLocalStorage()){mainFlows = JSON.parse(localStorage.getItem('mainFlowsList'));}else{getMainFlowsList(facility);}prepareIncomesAndExpenses(mainFlows, facility)}});})
    $('#searchInvestmentsByDate').on('click', function (e) {
        e.preventDefault();
        var facility = $('#investmentsDetailsTitle').text().split(' ')[3];
        var investments = $('#investments');

        /*
        var facility = $(this).parent().find('.select')
            .find('.selectric-wrapper').find('.selectric:last').find('span.label').text();
        */
        var tmp;
        var dateFrom = $(this).parent().find('.date-select').find('.date-select-wrapper.flex-vhcenter').find('.field:first').find('input[name=from]').val();
        var dateTo = $(this).parent().find('.date-select').find('.date-select-wrapper.flex-vhcenter').find('.field:last').find('input[name=to]').val();
        if (typeof dateFrom === 'undefined' || dateFrom === '') {
            var d = new Date();
            dateFrom = new Date(d.getFullYear() - 100, d.getMonth(), d.getDate());
        } else {
            tmp = dateFrom.split('.');
            dateFrom = new Date(parseInt(tmp[2]), parseInt(tmp[1]) - 1, parseInt(tmp[0]));
        }
        if (typeof dateTo === 'undefined' || dateTo === '') {
            dateTo = new Date();
        } else {
            tmp = dateTo.split('.');
            dateTo = new Date(parseInt(tmp[2]), parseInt(tmp[1]) - 1, parseInt(tmp[0]));
        }

        var totalSum = 0;
        var dataCash;
        investments.find('[data-date]').each(function () {
            var cur = $(this).data('date');
            var curFacility = $(this).parent().find('[data-facility]').text();
            tmp = cur.split('.');
            cur = new Date(parseInt(tmp[2]), parseInt(tmp[1]) - 1, parseInt(tmp[0]));
            if (cur >= dateFrom && cur <= dateTo && curFacility === facility) {
                //$(this).parent().children().show();
                $(this).parent().show();
                dataCash = $(this).parent().find('[data-cash]');
                totalSum += parseFloat(dataCash.data('cash'));
            } else {
                //$(this).parent().children().hide();
                $(this).parent().hide();
            }
        });
        $('#totalSumFooter').find('span').empty().html('Итого за выбранный период: ' +
            new Intl.NumberFormat('ru-RU', {
                minimumFractionDigits: 0
            }).format(Math.round(totalSum)) + ' руб.');
        //console.log(totalSum);
        //console.log(dateFrom);
        //console.log(dateTo);
    })
});

function getPlugin(labelLength) {
    var plugin = {
        id: 'scroll-plugin',
        rendered: false,
        afterDraw: function (chart, options) {
            var chartOptions = chart.options;

            if (!chartOptions.scroll) {
                return;
            }
            nummberColumns = chartOptions.nummberColumns > 1 ? chartOptions.nummberColumns : chart.config.data.labels.length;
            chartOptions.scroll = false;

            this.rendered = true;

            var chartWrapper = $('<div class="chart-wrapper"></div>');
            var chartAreaWrapper = $('<div class="chart-area-wrapper"></div>');
            chartAreaWrapper.width((800 / nummberColumns) * labelLength);
            chartAreaWrapper.height('400px');
            $(chart.ctx.canvas).wrap(chartWrapper).wrap(chartAreaWrapper);
            chartAreaWrapper.wrap(chartWrapper);

            $(chartOptions.legendSelector).html(chart.generateLegend());
            $(chartOptions.legendSelector + " > ul > li").on("click", function (e) {
                var index = $(this).index(),
                    dataset = chart.data.datasets[index];

                $(this).toggleClass("hidden");
                dataset.hidden = !dataset.hidden;

                chart.update();
            });

            var copyWidth = chart.scales['y-axis-0'].width - 10;
            var copyHeight = chart.scales['y-axis-0'].height + chart.scales['y-axis-0'].top + 10;

            var chartAxis = document.querySelector(chartOptions.axisSelector);
            chartAxis.style.height = copyHeight + 'px';
            chartAxis.style.width = copyWidth + 'px';

            var ticks = chart.scales['y-axis-0'].ticksAsNumbers;

            var $chartList = $(chartOptions.axisSelector + ' > ul');
            $chartList.html('');

            ticks.forEach(function (el) {
                $chartList.append('<li>' + new Intl.NumberFormat('ru-RU', {
                    maximumFractionDigits: 2,
                    minimumFractionDigits: 0
                }).format(el) + '</li>')
            });

            $('#chart-axis').css('height', '390px');
        }
    };
    return plugin;
}

function prepareToggle() {
    $('.accordion__item').each(function () {
        var self = $(this);
        var $body = self.find('.accordion__body');
        var $btnAction = self.find('.col_action span');

        var $item = self.find('.accordionChild__item');

        $item.each(function () {
            var $itm = $(this);
            var $rowAction = $itm.find('.row_action');
            var $child = $rowAction.find('.row_child-wrapper');
            $rowAction.on('click', function () {

                $rowAction.toggleClass('active');
                $child.slideToggle(300);
            });
        });

        $btnAction.on('click', function () {

            self.toggleClass('active');
            $body.slideToggle(300);
        });
    });
}

function getColor(i) {
    switch (i) {
        case 0:
            return window.chartColors.red;
        case 1:
            return window.chartColors.sea;
        case 2:
            return window.chartColors.yellow;
        case 3:
            return window.chartColors.orange;
        case 4:
            return window.chartColors.green;
        case 5:
            return window.chartColors.blue;
        case 6:
            return window.chartColors.purple;
        case 7:
            return window.chartColors.grey;
    }
}

function prepareIncomeDiagram(investorsFlowsList) {
    var invFlows = $.grep(investorsFlowsList, function (el) {
        return el.afterCashing >= 0;
    });
    var reportDates = [];
    var tmpDate = [];

    var cash;
    var cashes = [];
    $.each(invFlows, function (ind, el) {
        reportDates.push(new Date(el.reportDate));
        cash = {
            reportDate: new Date(el.reportDate),
            facility: el.facility.facility,
            sum: el.afterCashing
        };
        cashes.push(cash);
    });

    reportDates.sort(function (a, b) {
        return new Date(a) - new Date(b);
    });

    cashes.sort(function (a, b) {
        return a.reportDate - b.reportDate;
    });

    $.each(cashes, function (ind, el) {
        el.reportDate = monthNames[el.reportDate.getMonth()] + ' ' + el.reportDate.getFullYear();
    });

    var tmpYears = [];
    var years;
    $.each(reportDates, function (ind, el) {
        tmpDate.push(monthNames[el.getMonth()] + ' ' + el.getFullYear());
        tmpYears.push(el.getFullYear());
    });

    reportDates = unique(tmpDate);
    years = unique(tmpYears);

    var groupedCashes = [];

    cashes.reduce(function (res, value) {
        if (!res[value.facility]) {
            res[value.facility] = {
                sum: 0.00,
                facility: value.facility,
                reportDate: value.reportDate
            };
            groupedCashes.push(res[value.facility])
        }
        if (res[value.facility].facility === value.facility && res[value.facility].reportDate === value.reportDate) {
            res[value.facility].sum += value.sum;
        } else {
            res[value.facility] = {
                sum: value.sum,
                facility: value.facility,
                reportDate: value.reportDate
            };
            groupedCashes.push(res[value.facility])
        }
        return res;
    }, {});

    var reducedCash = JSON.parse(JSON.stringify(groupedCashes));

    $.each(reducedCash, function (ind, el) {
        el.sum = Math.round(el.sum * 100) / 100;
    });

    var facilities = [];
    $.each(reducedCash, function (i, el) {
        facilities.push(el.facility);
    });
    facilities = unique(facilities);

    var months = $('#months');
    months
        .find('option')
        .remove()
        .end();

    $.each(monthLongNames, function (key, value) {
        var option = $('<option></option>');
        option.attr('value', key);
        option.text(value);

        if (key === new Date().getMonth()) {
            option.attr('selected', true)
        }

        option.appendTo(months);
    });

    var optYears = $('#years');
    optYears
        .find('option')
        .remove()
        .end();

    $.each(years, function (key, value) {
        var option = $('<option></option>');
        option.attr('value', value);
        option.text(value);

        if (value === new Date().getFullYear()) {
            option.attr('selected', true)
        }

        option.appendTo(optYears);
    });


    var incomesAndExpenses = $('#incomesAndExpenses');

    incomesAndExpenses
        .find('option')
        .remove()
        .end();

    $.each(facilities, function (key, value) {
        var option = $('<option></option>');
        option.attr('value', key);
        option.text(value);

        if (value === maxFacility) {
            option.attr('selected', true)
        }

        option.appendTo(incomesAndExpenses);
    });

    $(function () {
        //$('select').selectric();
        $('select#incomesAndExpenses').selectric({
            onChange: function () {
                var facility = $('.inner__row.inner__row_costs.flex-vhcenter').find('.selectric:last').find('span.label').text();
                getMainFlowsList(facility);
            }
        });

        $('select#months').selectric({
            onChange: function () {
                var month = $('.inner__row.inner__row_finance.flex-vhcenter')
                    .find('.circle')
                    .find('.box__select:first')
                    .find('.selectric:last').find('span.label').text();
            }
        });

        $('select#years').selectric({
            onChange: function () {
                var year = $('.inner__row.inner__row_finance.flex-vhcenter')
                    .find('.circle')
                    .find('.box__select:last')
                    .find('.selectric:last').find('span.label').text();
            }
        });
    });

    var totals = [];
    var total;
    var sums = [];
    var totalSums = [];
    var i = -1;
    var facilitiesDates = [];

    var res = groupedCashes.reduce(function (prev, cur, i) {
        var cash = prev[cur.reportDate];
        if (cash) {
            cash.sum += cur.sum;
        } else {
            prev[cur.reportDate] = cur;
            //delete cur.reportDate;
        }
        return prev;

    }, []);

    var eachObject = function (obj) {
        for (let key in obj) {
            totalSums.push(Number(obj[key].sum).toFixed(2));
        }
    };

    eachObject(res);

    //console.log("После reduce");
    //console.log(groupedCashes);
    //console.log(reducedCash);

    $.each(facilities, function (ind, el) {
        sums = [];
        facilitiesDates = [];
        i = -1;
        $.each(reportDates, function (indDates, elDates) {
            i++;
            $.each(reducedCash, function (indGr, elGr) {
                if (elGr.facility === el && elGr.reportDate === elDates) {
                    /*
                    if(!totalSums[i]){
                        totalSums[i] = 0;
                    }

                    if(facilityCnt < 2){
                        totalSums[i] = elGr.sum;
                    }
                    */
                    if (!facilitiesDates[i]) {
                        facilitiesDates[i] = elGr.reportDate;
                    }
                    if (facilitiesDates[i].length === 0) {
                        sums.splice(i, 0, 0);
                    } else {
                        sums.splice(i, 0, elGr.sum);
                    }
                }
            });
        });

        $.each(facilitiesDates, function (ind, el) {
            if (typeof el === 'undefined') {
                sums.splice(ind, 0, 0);
            }
        });

        total =
            {
                facility: el,
                reportDates: facilitiesDates,
                sums: sums
            };
        totals.push(total);
    });

    if (reportDates.length < 6) {
        reportDates = appendMonths(reportDates);
    }

    $.each(totals, function (i, el) {
        var oldEl = JSON.parse(JSON.stringify(el));
        if (el.reportDates.length < reportDates.length) {
            el.reportDates = reportDates;
        }

        if (el.sums.length < reportDates.length) {
            $.each(reportDates, function (ind, elem) {
                if ($.inArray(elem, oldEl.reportDates) === -1 && oldEl.reportDates[ind] !== null) {
                    el.sums.splice(ind, 0, 0);
                }
            });
        }

    });

    if (totalSums.length < reportDates.length) {
        for (var j = totalSums.length; j < reportDates.length; j++) {
            totalSums.splice(0, 0, 0);
        }
    }

    //console.log(totals);

    var ds;
    var dSet = [];
    var color = 0;
    $.each(totals, function (ind, el) {
        color++;
        ds = {
            label: el.facility,
            backgroundColor: getColor(color),
            borderColor: getColor(color),
            data: el.sums,
            fill: false
        };
        dSet.push(ds);
    });

    /*
    ds = {
        label: "Все объекты",
        backgroundColor: getColor(7),
        borderColor: getColor(7),
        data: totalSums,
        fill: false
    };

    dSet.push(ds);
    */
    var max = Math.max.apply(Math, totalSums);

    var step = Math.floor(max / parseInt('5' + zeroDigits(max.toString().indexOf('.') - 2)))
        * parseInt('1' + zeroDigits(max.toString().indexOf('.') - 2));
    max = step * 7;

    Chart.plugins.register({
        id: 'scroll-plugin',
        rendered: false,
        afterDraw: function (chart, options) {
            var chartOptions = chart.options;

            if (!chartOptions.scroll) {
                return;
            }
            nummberColumns = chartOptions.nummberColumns > 1 ? chartOptions.nummberColumns : chart.config.data.labels.length;
            chartOptions.scroll = false;

            this.rendered = true;

            var chartWrapper = $('<div class="chart-wrapper"></div>');
            var chartAreaWrapper = $('<div class="chart-area-wrapper"></div>');
            chartAreaWrapper.width((chart.width / nummberColumns) * chart.config.data.labels.length);
            $(chart.ctx.canvas).wrap(chartWrapper).wrap(chartAreaWrapper);
            chartAreaWrapper.wrap(chartWrapper);

            $(chartOptions.legendSelector).html(chart.generateLegend());
            $(chartOptions.legendSelector + " > ul > li").on("click", function (e) {
                var index = $(this).index(),
                    dataset = chart.data.datasets[index];

                $(this).toggleClass("hidden");
                dataset.hidden = !dataset.hidden;

                chart.update();
            });

            var copyWidth = chart.scales['y-axis-0'].width - 10;
            var copyHeight = chart.scales['y-axis-0'].height + chart.scales['y-axis-0'].top + 10;

            var chartAxis = document.querySelector(chartOptions.axisSelector);
            chartAxis.style.height = copyHeight + 'px';
            chartAxis.style.width = copyWidth + 'px';

            var ticks = chart.scales['y-axis-0'].ticksAsNumbers;

            var $chartList = $(chartOptions.axisSelector + ' > ul');
            $chartList.html('');

            ticks.forEach(function (el) {
                $chartList.append('<li>' + new Intl.NumberFormat('ru-RU', {
                    maximumFractionDigits: 2,
                    minimumFractionDigits: 0
                }).format(el) + '</li>')
            });

            $('#chart-axis').css('height', '390px');
        }
    });

    var config = {
        type: 'bar',
        data:
            {
                labels: reportDates,
                datasets: dSet
            },
        options:
            {
                scroll: true,
                nummberColumns: 6,
                axisSelector: '#chart-axis',
                legendSelector: '#chart-finance-legend',
                maintainAspectRatio: false,
                legend:
                    {
                        display: false
                    },
                responsive: true,
                title:
                    {
                        display: false,
                        text: ''
                    },
                scales:
                    {
                        xAxes: [
                            {
                                barThickness: 60,
                                stacked: true,
                                ticks:
                                    {
                                        fontColor: '#11325b',
                                        fontSize: 15,
                                        fontStyle: 600,
                                        fontFamily: 'OpenSans'
                                    },
                                gridLines:
                                    {
                                        display: true
                                    }
                            }],
                        yAxes: [
                            {
                                stacked: true,
                                offset: true,
                                gridLines:
                                    {
                                        display: true,
                                        offsetGridLines: true
                                    },
                                ticks:
                                    {
                                        min: 0,
                                        max: max,
                                        stepSize: step,
                                        fontSize: 0
                                    }
                            }]
                    },
                tooltips: {
                    mode: 'label',
                    callbacks: {
                        afterTitle: function () {
                            window.total = 0;
                        },
                        label: function (tooltipItem, data) {
                            var facility = data.datasets[tooltipItem.datasetIndex].label;
                            var totalSum = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                            totalSum = parseFloat(totalSum);

                            window.total += totalSum;
                            return facility + ": " + totalSum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");
                        },
                        footer: function () {
                            return "Итого: " + window.total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");

                        }
                    }
                },
                animation: {
                    onComplete: function (e) {
                        if (e.numSteps === 0) {
                            $('.chart-wrapper').stop(true, true).animate({scrollLeft: $('.chart-area-wrapper').outerWidth()}, 500);
                            $('#chart-axis2').css('height', '390px');
                        }
                    }
                }

            }
    };
    /*
    var ctx1 = document.getElementById("chart-finance").getContext("2d");
    window.myDoughnut1 = new Chart(ctx1, config);
    */
    var oldChart = window.myDoughnut1;
    var ctx1 = document.getElementById("chart-finance").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnut1 = new Chart(ctx1, config);
    } else {
        window.myDoughnut1.config = config;
        window.myDoughnut1.update();
    }

    $('#chart-finance-legend').html(myDoughnut1.generateLegend());
    $("#chart-finance-legend > ul > li").on("click", function (e) {
        var index = $(this).index(),
            chart = e.view.myDoughnut1,
            dataset = chart.data.datasets[index]._meta[2];
        $(this).toggleClass("hidden");
        dataset.hidden = !dataset.hidden;
        chart.update();
    });

    //$('.chart-wrapper:first').stop(true, true).animate({scrollLeft: $('.chart-area-wrapper:first').outerWidth()}, 500);
    //$('#chart-axis').css('height', '390px');

}

function zeroDigits(numb) {
    var str = '';
    for (var i = 0; i < numb; i++) {
        str += "0";
    }
    return str;
}

function prepareIncomeCircle(investorsFlowsList, fullDate) {
    var invFlows;
    var month = new Date(fullDate).getMonth();
    var year = new Date(fullDate).getFullYear();
    var elDate;
    if (fullDate === '') {
        invFlows = $.grep(investorsFlowsList, function (el) {
            return el.afterCashing > 0;
        });
    } else {
        invFlows = $.grep(investorsFlowsList, function (el) {
            elDate = new Date(el.reportDate);
            return el.afterCashing > 0 && elDate.getFullYear() === year && elDate.getMonth() === month;
        });
    }

    var dates = [];
    $.each(invFlows, function (ind, el) {
        dates.push(new Date(el.reportDate));
    });

    var maxDate;
    if (dates.length === 0 && fullDate !== '') maxDate = fullDate;
    else maxDate = new Date(Math.max.apply(null, dates));

    var ttl = 'Ваш доход за ' + monthLongNames[maxDate.getMonth()].toLowerCase() + ' ' + maxDate.getFullYear();

    $('#months').val(maxDate.getMonth());
    $('#years').val(maxDate.getFullYear());

    invFlows = $.grep(invFlows, function (el) {
        var elDate = new Date(el.reportDate);
        var curDate = new Date(maxDate);
        return elDate.getMonth() === curDate.getMonth() && elDate.getFullYear() === curDate.getFullYear();
    });

    //$('#incomeTitle').html(ttl);

    var cash;
    var cashes = [];
    $.each(invFlows, function (ind, el) {
        cash = {
            facility: el.facility.facility,
            sum: el.afterCashing
        };
        cashes.push(cash);
    });

    var groupedCashes = [];

    cashes.reduce(function (res, value) {
        if (!res[value.facility]) {
            res[value.facility] = {
                sum: 0,
                facility: value.facility
            };
            groupedCashes.push(res[value.facility])
        }
        if (res[value.facility].facility === value.facility) {
            res[value.facility].sum += value.sum;
        } else {
            res[value.facility] = {
                sum: value.sum,
                facility: value.facility
            };
            groupedCashes.push(res[value.facility])
        }

        return res;
    }, {});
    var totalIncome;
    if (groupedCashes.length > 1) {
        totalIncome = $.map(groupedCashes, function (el) {
            return el.sum
        }).reduce(function (a, b) {
            return a + b;
        });
    } else if (groupedCashes.length > 0) {
        totalIncome = groupedCashes[0].sum;
    } else {
        totalIncome = 0;
    }

    $('#totalIncome').html('<span>' + new Intl.NumberFormat('ru-RU').format(totalIncome) + '</span>' + 'рублей');
    $('#incomesList').html('');
    if (groupedCashes.length > 0) {
        $.each(groupedCashes, function (ind, el) {
            $('#incomesList').append('<div class="circle-canvas__item" style="color: ' + getColor(ind) + '"><span>' + new Intl.NumberFormat('ru-RU').format(el.sum) + '</span>' + el.facility + '</div>');
        });
    } else {
        for (var i = 0; i < 2; i++) {
            $('#incomesList').append('<div class="circle-canvas__item" style="color: transparent"><span>' + new Intl.NumberFormat('ru-RU').format(0.0001) + '</span>' + 'Объекты' + '</div>');
        }
    }

    var facilities = [];
    var sums = [];
    var colors = [];
    if(groupedCashes.length > 0){
        $.each(groupedCashes, function (i, el) {
            facilities.push(el.facility);
            sums.push(el.sum);
            colors.push(getColor(i));
        });
        facilities = unique(facilities);
    } else {
        facilities.push('');
        sums.push(0.001);
        colors.push(getColor(0));
    }


    var configDoughnut = {
        type: 'doughnut',
        data:
            {
                labels: facilities,
                datasets: [
                    {
                        label: "",
                        backgroundColor: colors,
                        borderWidth: 0,
                        data: sums
                    }
                ]
            },
        options:
            {
                maintainAspectRatio: false,
                cutoutPercentage: 89,
                legend:
                    {
                        position: 'right',
                        display: false,
                        labels:
                            {
                                padding: 15,
                                fontFamily: 'OpenSans',
                                fontSize: 15
                            }
                    },
                title:
                    {
                        display: false,
                        fontStyle: 'bold',
                        fontSize: '15',
                        fontColor: '#11325b',
                        text: ttl
                    }
            }
    };
    /*
    var ctx2 = document.getElementById("doughnut-chart").getContext("2d");
    window.myDoughnut2 = new Chart(ctx2, configDoughnut);
    */

    var oldChart = window.myDoughnut2;
    var ctx2 = document.getElementById("doughnut-chart").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnut2 = new Chart(ctx2, configDoughnut);
    } else {
        window.myDoughnut2.config = configDoughnut;
        window.myDoughnut2.update();
    }
}

function prepareDetailsProfit(investorsFlowsList, iMonth) {
    const percentFormat = new Intl.NumberFormat('ru-RU', {
        style: 'percent',
        maximumFractionDigits: 2
    });
    const currencyFormat = new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'RUB'
    });
    var dates;
    var dateMonth;
    switch (iMonth) {
        case 'max':
            dates = investorsFlowsList.map(function (x) {
                return new Date(x.reportDate);
            });
            dateMonth = new Date(Math.max.apply(null, dates));
            break;
        default:
            dateMonth = new Date(iMonth/*getDateFromString(iMonth*/);
    }

    //console.log(investorsFlowsList);

    var invFlows = $.grep(investorsFlowsList, function (el) {
        return new Date(el.reportDate).getMonth() === dateMonth.getMonth() && new Date(el.reportDate).getFullYear() === dateMonth.getFullYear();
    });

    var facilitiesFlows = [];
    var facilityFlows;

    //console.log(invFlows);

    $.each(invFlows, function (ind, el) {
        facilityFlows = {
            facility: el.underFacilities.facility.facility,
            sum: el.summa,
            share: el.share,
            afterCosts: el.onInvestors,
            tax: el.taxation,
            afterTax: el.afterTax,
            comission: el.cashing,
            afterComission: el.afterCashing,
            shareKind: el.shareKind
        };
        facilitiesFlows.push(facilityFlows);
    });

    var gFacilitiesFlows = [];
    //console.log(facilitiesFlows);
    facilitiesFlows.reduce(function (res, value) {
        if (!res[value.facility]) {
            res[value.facility] = {
                facility: value.facility,
                sum: 0,
                share: 0,
                afterCosts: 0,
                tax: 0,
                afterTax: 0,
                comission: 0,
                afterComission: 0,
                cnt: 0,
                cntKind: 0,
                shareKind: value.shareKind
            };
            gFacilitiesFlows.push(res[value.facility])
        }
        if (res[value.facility].facility === value.facility) {
            res[value.facility].sum += value.sum;
            res[value.facility].afterCosts += value.afterCosts;
            res[value.facility].afterTax += value.afterTax;
            res[value.facility].afterComission += value.afterComission;
            res[value.facility].shareKind = value.shareKind;
            res[value.facility].share += value.share;
            res[value.facility].tax += value.tax;
            res[value.facility].comission += value.comission;
            res[value.facility].cnt += 1;
            if (res[value.facility].shareKind !== 'Сверхдоля') {
                res[value.facility].cntKind += 1;
            }
        } else {
            res[value.facility] = {
                facility: value.facility,
                sum: value.sum,
                share: value.share,
                afterCosts: value.afterCosts,
                tax: value.tax,
                afterTax: value.afterTax,
                comission: value.comission,
                afterComission: value.afterComission,
                cnt: 0,
                cntKind: 0,
                shareKind: value.shareKind
            };
            gFacilitiesFlows.push(res[value.facility])
        }

        return res;
    }, {});

    //console.log(gFacilitiesFlows);

    $.each(gFacilitiesFlows, function (ind, el) {
        if (el.cnt !== el.cntKind) {
            el.share = el.share / el.cntKind;
        } else {
            el.share = el.share / el.cnt;
        }
        el.tax = el.tax / el.cnt;
        el.comission = el.comission / el.cnt;
    });

    var elemChild;
    var elemParent;
    var elHead = $('#detailsProfit').find('.simplebar-content');
    elHead.html('');
    var constParent;

    $.each(gFacilitiesFlows, function (ind, el) {
        elemChild = $('<div class="accordion__item" id="' + ind + '"></div>');
        elemChild.appendTo(elHead);
        elemParent = elemChild;
        elemChild = $('<div class="accordion__head"></div>');
        elemChild.appendTo(elemParent);
        elemParent = elemChild;
        elemChild = $('<div class="col col_action"><span>' + el.facility + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + currencyFormat.format(el.sum) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + percentFormat.format(el.share) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + currencyFormat.format(el.afterCosts) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + percentFormat.format(el.tax) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + currencyFormat.format(el.afterTax) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + percentFormat.format(el.comission) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemChild = $('<div class="col"><span>' + currencyFormat.format(el.afterComission) + '</span></div>');
        elemChild.appendTo(elemParent);
        elemParent = $('#detailsProfit').find('.simplebar-content').find('#' + ind);
        elemChild = $('<div class="accordion__body"></div>');
        elemChild.appendTo(elemParent);
        elemParent = elemChild;
        elemChild = $('<div class="accordionChild__list" id="accBody"></div>');
        elemChild.appendTo(elemParent);
        elemParent = elemChild;
        constParent = elemParent;
        $.each(invFlows, function (indSec, elSec) {
            if (el.facility === elSec.facility.facility) {
                elemChild = $('<div class="accordionChild__item"></div>');
                elemChild.appendTo(constParent);
                elemParent = elemChild;
                elemChild = $('<div class="row" style="margin: 0"></div>');
                elemChild.appendTo(elemParent);
                elemParent = elemChild;
                elemChild = $('<div class="row__inner"></div>');
                elemChild.appendTo(elemParent);
                elemParent = elemChild;
                elemChild = $('<div class="col" style="padding-left: 15px"><span>' + elSec.underFacilities.underFacility + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + currencyFormat.format(elSec.summa) + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + percentFormat.format(elSec.share) + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + currencyFormat.format(elSec.onInvestors) + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + percentFormat.format(elSec.taxation) + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + currencyFormat.format(elSec.afterTax) + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + percentFormat.format(elSec.cashing) + '</span></div>');
                elemChild.appendTo(elemParent);
                elemChild = $('<div class="col"><span>' + currencyFormat.format(elSec.afterCashing) + '</span></div>');
                elemChild.appendTo(elemParent);
            }
        });

        /*
        $('#accHead').append('</div>');
        */
    });
    prepareToggle();
}

function prepareIncomesAndExpenses(mainFlowsList, facility) {

    var settlementDates = [];
    var tmpDate = [];
    var mainFlows;
    switch (facility) {
        case 'Все объекты':
            mainFlows = mainFlowsList;
            break;
        default:
            mainFlows = $.grep(mainFlowsList, function (el) {
                return el.underFacilities.facility.facility === facility;
            });
    }

    var dates = [];
    $.each(mainFlows, function (ind, el) {
        dates.push(new Date(el.settlementDate));
    });
    var maxDate = new Date(Math.max.apply(null, dates));
    var ttl = 'Баланс ' + facility + ' за ' + monthLongNames[maxDate.getMonth()].toLowerCase() + ' ' + maxDate.getFullYear();
    $('#totalExpenses').html(ttl);

    var flow;
    var flows = [];
    $.each(mainFlows, function (ind, el) {
        settlementDates.push(new Date(el.settlementDate));

        if (el.summa >= 0) {
            flow = {
                settlementDate: new Date(el.settlementDate),
                incomeOrExpense: 'Доходы',
                sum: el.summa
            };
        } else {
            flow = {
                settlementDate: new Date(el.settlementDate),
                incomeOrExpense: 'Расходы',
                sum: el.summa
            };
        }
        flows.push(flow);
    });

    settlementDates.sort(function (a, b) {
        return new Date(a) - new Date(b);
    });
    $.each(settlementDates, function (ind, el) {
        tmpDate.push(monthNames[el.getMonth()] + ' ' + el.getFullYear())
    });

    settlementDates = unique(tmpDate);

    flows.sort(function (a, b) {
        return a.settlementDate - b.settlementDate;
    });
    $.each(flows, function (ind, el) {
        el.settlementDate = monthNames[el.settlementDate.getMonth()] + ' ' + el.settlementDate.getFullYear();
    });

    var groupedFlows = [];
    flows.reduce(function (res, value) {
        if (!res[value.incomeOrExpense]) {
            res[value.incomeOrExpense] = {
                sum: 0,
                incomeOrExpense: value.incomeOrExpense,
                settlementDate: value.settlementDate
            };
            groupedFlows.push(res[value.incomeOrExpense])
        }
        if (res[value.incomeOrExpense].incomeOrExpense === value.incomeOrExpense &&
            res[value.incomeOrExpense].settlementDate === value.settlementDate) {
            res[value.incomeOrExpense].sum += value.sum;
        } else {
            res[value.incomeOrExpense] = {
                sum: value.sum,
                incomeOrExpense: value.incomeOrExpense,
                settlementDate: value.settlementDate
            };
            groupedFlows.push(res[value.incomeOrExpense])
        }

        return res;
    }, {});
    var sums = [];
    var incomes = [];
    var expenses = [];
    //console.log(groupedFlows);
    $.each(groupedFlows, function (ind, el) {
        if (el.incomeOrExpense === 'Доходы') {
            incomes.push(Number(el.sum).toFixed(2));
            sums.push(Number(el.sum).toFixed(2));
        } else {
            expenses.push(Number(el.sum * -1).toFixed(2));
            sums.push(Number(el.sum * -1).toFixed(2));
        }
    });

    var max = Math.max.apply(Math, sums);

    var step = Math.floor(max / parseInt('5' + zeroDigits(max.toString().indexOf('.') - 2)))
        * parseInt('1' + zeroDigits(max.toString().indexOf('.') - 2));

    var min = max === 0 ? -100 : 0;
    if (max === 0) {
        step = 100;
    }

    max = step * 7;

    if (settlementDates.length < 6) {
        settlementDates = appendMonths(settlementDates);
    }

    if (incomes.length < settlementDates.length) {
        for (var j = incomes.length; j < settlementDates.length; j++) {
            incomes.splice(0, 0, 0);
        }
    }
    if (expenses.length < settlementDates.length) {
        for (var j = expenses.length; j < settlementDates.length; j++) {
            expenses.splice(0, 0, 0);
        }
    }

    var configCosts = {
        type: 'line',
        data:
            {
                labels: settlementDates,
                datasets: [
                    {
                        label: "Доходы",
                        backgroundColor: window.chartColors.green,
                        borderColor: window.chartColors.green,
                        data: incomes,
                        fill: false
                    },
                    {
                        label: "Расходы",
                        fill: false,
                        backgroundColor: window.chartColors.orange,
                        borderColor: window.chartColors.orange,
                        data: expenses
                    }]
            },
        options:
            {
                scroll: true,
                nummberColumns: 6,
                axisSelector: '#chart-axis2',
                legendSelector: '#chart-costs-legend',
                maintainAspectRatio: false,
                legend:
                    {
                        display: false
                    },
                responsive: true,
                title:
                    {
                        display: false,
                        text: ''
                    },
                scales:
                    {
                        xAxes: [
                            {
                                ticks:
                                    {
                                        fontColor: '#11325b',
                                        fontSize: 15,
                                        fontStyle: 600,
                                        fontFamily: 'OpenSans'
                                    },
                                gridLines:
                                    {
                                        display: true
                                    }
                            }],
                        yAxes: [
                            {
                                offset: true,
                                gridLines:
                                    {
                                        display: true,
                                        offsetGridLines: true
                                    },
                                ticks:
                                    {
                                        min: min,
                                        max: max,
                                        stepSize: step,
                                        fontSize: 0
                                    }
                            }]
                    },
                animation: {
                    onComplete: function (e) {
                        if (e.numSteps === 0) {
                            $('.chart-wrapper').stop(true, true).animate({scrollLeft: $('.chart-area-wrapper').outerWidth()}, 500);
                            $('#chart-axis2').css('height', '390px');
                        }
                    }
                }
            }
    };

    var chartOptions;

    var oldChart = window.myDoughnut;
    var ctxCosts = document.getElementById("chart-costs").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnut = new Chart(ctxCosts, configCosts);
    } else {
        chartOptions = window.myDoughnut.options;
        var nummberColumns = chartOptions.nummberColumns;
        ctxCosts.canvas.parentNode.style.width = (oldChart.width / nummberColumns) * settlementDates.length + 'px';
        oldChart.clear();
        oldChart.destroy();

        $('#chart-costs').remove(); // this is my <canvas> element
        $('.inner__row.inner__row_costs.flex-vhcenter')
            .find('.box')
            .find('.wrapper').empty().css('height', '400px')
            .append('<canvas id="chart-costs"></canvas>')
            .append('<div id="chart-axis2" style="height: 390px; width: 0px;"></div>');
        ctxCosts = document.getElementById("chart-costs").getContext("2d");

        window.myDoughnut = new Chart(ctxCosts, configCosts);
        window.myDoughnut.options.scales.yAxes[0].ticks.min = min;
        window.myDoughnut.options.scales.yAxes[0].ticks.max = max;
        window.myDoughnut.options.scales.yAxes[0].ticks.stepSize = step;


        var $chartList = $(chartOptions.axisSelector);
        $chartList.append('<ul></ul>');
        $chartList.find('ul').html('');

        for (var i = max; i >= min; i -= step) {
            $chartList.find('ul').append('<li>' + new Intl.NumberFormat('ru-RU', {
                maximumFractionDigits: 2,
                minimumFractionDigits: 0
            }).format(i) + '</li>')
        }
        //window.myDoughnut.update();
    }

    var chartLegend = $('#chart-costs-legend');
    chartLegend.html(myDoughnut.generateLegend());
    chartLegend.find("> ul > li").on("click", function (e) {
        var index = $(this).index(),
            chart = e.view.myDoughnut,
            dataset = chart.data.datasets[index]._meta[4];
        $(this).toggleClass("hidden");
        dataset.hidden = !dataset.hidden;
        chart.update();
    });

    var totalIncomes = incomes[incomes.length - 1];
    var totalExpenses = expenses[expenses.length - 1];

    if (totalIncomes === 0) totalIncomes += 0.0001;
    if (totalExpenses === 0) totalExpenses += 0.0001;

    var totalIn = totalIncomes - totalExpenses;
    $('#totalExpensesText').html('<span>' + new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2
    }).format(totalIn) + '</span>' + 'рублей');
    var configDoughnutExpenses = {
        type: 'doughnut',
        data:
            {
                labels: ['Доходы', 'Расходы'],
                datasets: [
                    {
                        label: "",
                        backgroundColor: [window.chartColors.green, window.chartColors.orange],
                        borderWidth: 0,
                        data: [totalIncomes, totalExpenses]
                    }
                ]
            },
        options:
            {
                maintainAspectRatio: false,
                responsive: true,
                cutoutPercentage: 89,
                legend:
                    {
                        position: 'right',
                        display: false,
                        labels:
                            {
                                padding: 15,
                                fontFamily: 'OpenSans',
                                fontSize: 15
                            }
                    },
                title:
                    {
                        display: false,
                        fontStyle: 'bold',
                        fontSize: '15',
                        fontColor: '#11325b',
                        text: ''
                    }
            }
    };

    var oldChartExpenses = window.myDoughnutExpenses;
    var ctxExpenses = document.getElementById("doughnut-chart-expenses").getContext("2d");
    if (typeof oldChartExpenses === 'undefined') {
        window.myDoughnutExpenses = new Chart(ctxExpenses, configDoughnutExpenses);
    } else {
        window.myDoughnutExpenses.config = configDoughnutExpenses;
        window.myDoughnutExpenses.update();
    }

    var expensesList = $('#expensesList');
    expensesList.html('');
    expensesList.append('<div class="circle-canvas__item" style="color: ' + window.chartColors.green + '"><span>' + new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2
    }).format(totalIncomes) + '</span>' + "Доходы" + '</div>');
    expensesList.append('<div class="circle-canvas__item" style="color: ' + window.chartColors.orange + '"><span>' + new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2
    }).format(totalExpenses) + '</span>' + "Расходы" + '</div>');
    //blurElement($('.out'), 0);

}

function prepareInvestedMoney(investorsCashes, rooms) {

    maxFacility = getMaxInFacility(investorsCashes);

    var cashes = $.grep(investorsCashes, function (el) {
        return el.typeClosingInvest === null || el.typeClosingInvest.typeClosingInvest !== 'Перепродажа доли';
    });

    var balanceCash = $.grep(investorsCashes, function (el) {
        return el.typeClosingInvest === null;
    });

    var totalMoney = 0;

    $.each(balanceCash, function (ind, el) {
        totalMoney += el.givedCash;
    });

    createInvestedAllTable(cashes);
    createInvestedTable(cashes);
    var facilitiesCoasts = [];

    rooms.reduce(function (res, value) {
        var facility = value.underFacility.facility.facility;
        if (!res[facility]) {
            res[facility] = {
                coast: 0,
                facility: facility
            };
            facilitiesCoasts.push(res[facility])
        }
        if (res[facility].facility === facility) {
            res[facility].coast += value.coast;
        } else {
            res[facility] = {
                coast: 0,
                facility: facility
            };
            facilitiesCoasts.push(res[facility])
        }

        return res;
    }, {});

    var invested = [];
    cashes.reduce(function (res, value) {
        var facility = value.facility.facility;
        if (!res[facility]) {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        if (res[facility].facility === facility) {
            res[facility].givedCash += value.givedCash;
        } else {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        return res;
    }, {});

    /*
    $.each(invested, function (ind, el) {
        totalMoney += el.givedCash;
    });
    */
    var finalArr = [];
    $.each(facilitiesCoasts, function (ind, el) {
        var fin;
        var obj = $.grep(invested, function (grEl) {
            return grEl.facility === el.facility
        });
        if (typeof obj[0] !== 'undefined') {
            fin = {
                facility: el.facility,
                coast: el.coast,
                givedCash: obj[0].givedCash
            };
            finalArr.push(fin);
        }
    });
    $('#balance').find('span:last').html('<b>' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(totalMoney) + '</b>' + ' рублей');

    /*
    totalMoney = 0;
    $.each(invested, function (ind, el) {
        totalMoney += el.givedCash;
    });
    */

    $('#totalInvested').html('<span>' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(totalMoney) + '</span>' + 'рублей');
    $('#investedList').html('');
    $.each(finalArr, function (ind, el) {
        $('#investedList').append('<div class="circle-canvas__item" style="color: ' + getColor(ind) + '"><span>' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.givedCash)) + '</span>' + el.facility + '</div>');
    });

    var facilities = [];
    var sums = [];
    var colors = [];
    $.each(finalArr, function (i, el) {
        facilities.push(el.facility);
        sums.push(el.givedCash);
        colors.push(getColor(i));
    });
    facilities = unique(facilities);

    var maxPriceFacility = Math.max.apply(Math, finalArr.map(function (value) {
        return value.coast
    }));
    //console.log(maxPriceFacility);

    //$('#totalSum').find('span').html(new Intl.NumberFormat('ru-RU', { maximumFractionDigits: 2 }).format(totalMoney));
    var investedMoney = $('#investedMoney');
    investedMoney.empty();
    $.each(finalArr, function (ind, el) {
        var percentLen = Math.floor((el.coast / maxPriceFacility) * 100);
        var percentBg = Math.floor((el.givedCash / el.coast) * 100);
        var colDiv = $('<div class="column"></div>');
        colDiv.appendTo(investedMoney);
        var priceFirst = $('<div class="column__price-first">' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(el.coast) + '</div>');
        priceFirst.appendTo(colDiv);
        var colBody = $('<div style="height:' + percentLen + '%" class="column__body"></div>');
        colBody.appendTo(colDiv);

        var leftPerc = 0;
        if (percentBg === 100) {
            leftPerc = 30;
        } else if (percentBg >= 10 && percentBg <= 99) {
            leftPerc = 35;
        } else {
            leftPerc = 40;
        }

        var colPercent = $('<div style="background-color:transparent; position:absolute; bottom:10%; left:' + leftPerc + '%; font-size:12px; font-weight:600;color:#11325b">' + percentBg + '%</div>');
        colPercent.appendTo(colBody);
        percentBg = percentBg < 5 ? 5 : percentBg;
        var colBg = $('<div style="height:' + percentBg + '%; background-color:' + getColor(ind) + ';" class="column__bg"></div>');
        colBg.appendTo(colBody);
        var priceSecond = $('<div class="column__price-second">' +
            new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.givedCash)) + '</div>');
        priceSecond.appendTo(colDiv);
        var colTitle = $('<div class="column__title">' + el.facility + '</div>');
        colTitle.appendTo(colDiv);

    });


    var configDoughnutInvested = {
        type: 'doughnut',
        data:
            {
                labels: facilities,
                datasets: [
                    {
                        label: "",
                        backgroundColor: colors,
                        borderWidth: 0,
                        data: sums
                    }
                ]
            },
        options:
            {
                maintainAspectRatio: false,
                responsive: true,
                cutoutPercentage: 89,
                legend:
                    {
                        position: 'right',
                        display: false,
                        labels:
                            {
                                padding: 15,
                                fontFamily: 'OpenSans',
                                fontSize: 15
                            }
                    },
                title:
                    {
                        display: false,
                        fontStyle: 'bold',
                        fontSize: '15',
                        fontColor: '#11325b',
                        text: ''
                    }
            }
    };

    var oldChart = window.myDoughnutInvested;
    var ctxInvested = document.getElementById("doughnut-chart-invested").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnutInvested = new Chart(ctxInvested, configDoughnutInvested);
    } else {
        window.myDoughnutInvested.config = configDoughnutInvested;
        window.myDoughnutInvested.update();
    }
}

function createInvestedAllTable(investorsCash) {
    var invCash;

    invCash = $.grep(investorsCash, function (el) {
        return el.givedCash > 0;
    });
    var facilities = unique(invCash.map(function (value) {
        return value.facility.facility
    }));
    //console.log(facilities);

    var investedAllDetailsTable = $('#investedAllDetailsTable').find('.simplebar-content');
    investedAllDetailsTable.empty();

    var invested = [];
    invCash.reduce(function (res, value) {
        var facility = value.facility.facility;
        if (!res[facility]) {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        if (res[facility].facility === facility) {
            res[facility].givedCash += value.givedCash;
        } else {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        return res;
    }, {});

    $.each(invested, function (ind, el) {
        var row = $('<div class="row flex-vcenter"></div>');
        row.appendTo(investedAllDetailsTable);

        var colFacility = $('<div class="col" style="margin-left: 15px">' + el.facility + '</div>');
        colFacility.appendTo(row);
        var colSum = $('<div class="col" data-cash="' + el.givedCash + '">' +
            new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.givedCash)) + ' руб.' + '</div>');
        colSum.appendTo(row);
        var colDetails = $('<div class="col"><button type="button" class="btn btn-primary" id="showInvestsDetails">Подробности</button></div>');
        colDetails.appendTo(row);
    });

}

function createInvestedTable(investorsCash) {
    var invCash;

    invCash = $.grep(investorsCash, function (el) {
        return el.givedCash > 0;
    });
    //console.log(invCash);
    var facilities = unique(invCash.map(function (value) {
        return value.facility.facility
    }));
    //console.log(facilities);

    var investedDetailsTable = $('#investedDetailsTable').find('.simplebar-content');
    investedDetailsTable.empty();

    $.each(invCash, function (ind, el) {
        var row = $('<div class="row flex-vcenter"></div>');
        row.appendTo(investedDetailsTable);
        var dateGiv = new Intl.DateTimeFormat('ru-RU').format(el.dateGivedCash);
        var colDate = $('<div class="col" style="margin-left: 15px" data-date="' + dateGiv + '">' + dateGiv + '</div>');
        colDate.appendTo(row);
        var colSum = $('<div class="col" data-cash="' + el.givedCash + '">' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(el.givedCash) + ' руб.' + '</div>');
        colSum.appendTo(row);
        var cashSource = el.cashSource === null ? '' : el.cashSource.cashSource;
        var cashImg = '';
        switch (cashSource) {
            case 'Альфа':
            case 'Карта Альфа-банка':
                cashImg = 'alfabank.png';
                break;
            case 'Сбер':
                cashImg = 'sber.png';
                break;
            case 'Наличка':
                cashImg = 'money.png';
                break;
            default:
                cashImg = 'invoice.png';
        }
        var colImg = $('<div class="col"><img src="/resources/core/img/' + cashImg + '" title="' + cashSource + '"></div>');
        colImg.appendTo(row);
        var colFacility = $('<div class="col" hidden data-facility="' + el.facility.facility + '">' + el.facility.facility + '</div>');
        colFacility.appendTo(row);
    });

    /*
    $('select#invested').find('option')
        .remove()
        .end();

    $.each(facilities, function (ind, el) {
        var option = $('<option>' + el + '</option>');
        option.appendTo($('select#invested'));
    });
    */
    var investments = $('#investments');

    var invested = [];
    invCash.reduce(function (res, value) {
        var facility = value.facility.facility;
        if (!res[facility]) {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        if (res[facility].facility === facility) {
            res[facility].givedCash += value.givedCash;
        } else {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        return res;
    }, {});

    //console.log(invCash);

    /*
    $(function () {

        //$('select').selectric();
        $('select#invested').selectric({
            /*
            onChange: function (element) {
                var facility = investments
                    .find('.popup__inner')
                    .find('.popup__body')
                    .find('.head')
                    .find('.head__bottom')
                    .find('.select')
                    .find('.selectric-wrapper.selectric-below').find('.selectric:last').find('span.label').text();

                investments.find('[data-facility]').each(function () {
                    var cur = $(this).data('facility');

                    if (cur === facility) {
                        $(this).parent().children().show();
                        $(this).css('color', 'transparent');
                        $(this).parent().show();
                    } else {
                        $(this).parent().children().hide();
                        $(this).parent().hide();
                    }

                });

                $.each(invested, function (ind, el) {
                    if (el.facility === facility) {
                        var totalSum = $('#totalSumFooter');
                        totalSum.find('strong').empty();
                        $('<span><strong>' + Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(el.givedCash) + ' руб.' + '</strong></span>')
                            .appendTo(totalSum);
                    }
                });
            }

        });

    });
    */

    var facility = investments
        .find('.popup__inner')
        .find('.popup__body')
        .find('.head')
        .find('.head__bottom')
        .find('.select')
        .find('option:first').text();
    /*
    investments.find('[data-facility]').each(function () {
        var cur = $(this).data('facility');

        if (cur === facility) {
            $(this).parent().children().show();
            $(this).css('color', 'transparent');
            $(this).parent().show();
        } else {
            $(this).parent().children().hide();
            $(this).parent().hide();
        }

    });
    */
    $.each(invested, function (ind, el) {
        if (el.facility === facility) {
            var totalSum = $('#totalSumFooter');
            totalSum.find('strong').empty();
            $('<span><strong>' + Intl.NumberFormat('ru-RU', {maximumFractionDigits: 0}).format(Math.round(el.givedCash)) + ' руб.' + '</strong></span>')
                .appendTo(totalSum);
        }
    });
}

function prepareIncomeChart(mainFlowsList) {

    var invFlows = $.grep(mainFlowsList, function (el) {
        return el.summa > 0
    });
    var settlementDates = [];
    var tmpDate = [];

    var cash;
    var cashes = [];
    $.each(invFlows, function (ind, el) {
        settlementDates.push(new Date(el.settlementDate));
        cash = {
            settlementDate: new Date(el.settlementDate),
            facility: el.underFacilities.facility.facility,
            sum: el.summa
        };
        cashes.push(cash);
    });
    settlementDates.sort(function (a, b) {
        return new Date(a) - new Date(b);
    });

    cashes.sort(function (a, b) {
        return a.settlementDate - b.settlementDate;
    });

    $.each(cashes, function (ind, el) {
        el.settlementDate = monthNames[el.settlementDate.getMonth()] + ' ' + el.settlementDate.getFullYear();
    });

    $.each(settlementDates, function (ind, el) {
        tmpDate.push(monthNames[el.getMonth()] + ' ' + el.getFullYear())
    });

    settlementDates = unique(tmpDate);
    var ds;
    var dSet = [];

    var groupedCashes = [];

    cashes.reduce(function (res, value) {
        if (!res[value.facility]) {
            res[value.facility] = {
                sum: 0,
                facility: value.facility,
                settlementDate: value.settlementDate
            };
            groupedCashes.push(res[value.facility])
        }
        if (res[value.facility].facility === value.facility && res[value.facility].settlementDate === value.settlementDate) {
            res[value.facility].sum += value.sum;
        } else {
            res[value.facility] = {
                sum: value.sum,
                facility: value.facility,
                settlementDate: value.settlementDate
            };
            groupedCashes.push(res[value.facility])
        }

        return res;
    }, {});

    var facilities = [];
    $.each(groupedCashes, function (i, el) {
        facilities.push(el.facility);
    });
    facilities = unique(facilities);

    var totals = [];
    var total;
    var sums = [];
    var totalSums = [];
    var i = -1;
    var facilitiesDates = [];
    $.each(facilities, function (ind, el) {
        sums = [];
        facilitiesDates = [];
        i = -1;
        $.each(settlementDates, function (indDates, elDates) {
            i++;

            $.each(groupedCashes, function (indGr, elGr) {
                if (elGr.facility === el && elGr.settlementDate === elDates) {

                    if (!totalSums[i]) {
                        totalSums[i] = 0;
                    }
                    if (!facilitiesDates[i]) {
                        facilitiesDates[i] = elGr.settlementDate;
                    }
                    if (facilitiesDates[i].length === 0) {
                        sums.push(0);
                    } else {
                        sums.push(elGr.sum);
                    }
                }
                if (elGr.facility !== el && elGr.settlementDate === elDates) {
                    if (!totalSums[i]) {
                        totalSums[i] = elGr.sum;
                    } else {
                        if (elGr.facility !== el && elGr.settlementDate === elDates) {
                            totalSums[i] = totalSums[i] + elGr.sum;
                        }
                    }
                }
            });
        });

        $.each(facilitiesDates, function (ind, el) {
            if (typeof el === 'undefined') {
                sums.splice(ind, 0, 0);
            }
        });

        total =
            {
                facility: el,
                settlementDates: facilitiesDates,
                sums: sums
            };
        totals.push(total);
    });
    var color = 0;
    $.each(totals, function (ind, el) {
        color++;
        ds = {
            label: el.facility,
            backgroundColor: getColor(color),
            borderColor: getColor(color),
            data: el.sums,
            fill: false
        };
        dSet.push(ds);
    });

    ds = {
        label: "Все объекты",
        backgroundColor: getColor(8),
        borderColor: getColor(8),
        data: totalSums,
        fill: false
    };

    dSet.push(ds);

    var max = Math.max.apply(Math, totalSums);
    var step = Math.floor(max / 60000) * 10000;
    max = step * 7;

    var config = {
        type: 'line',
        data:
            {
                labels: settlementDates,
                datasets: dSet
            },
        options:
            {
                legend:
                    {
                        position: 'top',
                        labels:
                            {
                                padding: 15,
                                fontFamily: 'OpenSans',
                                fontSize: 15
                            }
                    },
                responsive: true,
                title:
                    {
                        display: false,
                        text: ''
                    },
                scales:
                    {
                        xAxes: [
                            {
                                ticks:
                                    {
                                        fontColor: '#11325b',
                                        fontSize: 15,
                                        fontStyle: 600,
                                        fontFamily: 'OpenSans'
                                    },
                                gridLines:
                                    {
                                        display: true
                                    }
                            }],
                        yAxes: [
                            {
                                gridLines:
                                    {
                                        display: true
                                    },
                                ticks:
                                    {
                                        min: 0,
                                        max: max,
                                        stepSize: step,
                                        fontColor: '#11325b',
                                        fontSize: 15,
                                        fontFamily: 'OpenSans'
                                    }
                            }]
                    }
            }
    };

    var ctx1 = document.getElementById("chart-finance").getContext("2d");
    window.myDoughnut = new Chart(ctx1, config);
}

function onStartUp() {
    getInvestorsCash();
    getInvestorsFlowsList('max', '');
    getMainFlowsList('Все объекты');
    getAnnexes();
}

function getInvestorsFlowsList(iMonth, fullDate) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getInvestorsFlows",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            var invFlows = data.investorsFlowsList;
            if (fullDate === '') {
                prepareDetailsProfit(invFlows, iMonth);
                prepareIncomeDiagram(invFlows);
                prepareIncomeCircle(invFlows, fullDate);
            } else {
                prepareIncomeCircle(invFlows, fullDate);
                prepareDetailsProfit(invFlows, fullDate);
            }

        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Потоки инвесторов загружены!');
        });

}

function getMainFlowsList(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/getMainFlows",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            if (facility === 'Все объекты') facility = data.facility.facility;
            maxFacility = facility;
            prepareIncomesAndExpenses(data.mainFlowsList, facility);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Ручные потоки инвесторов загружены!');
        });
}

function getInvestorsCash() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getInvestorsCashList",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareInvestedMoney(data.investorsCashList, data.rooms);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Деньги инвесторов загружены!');
        });

}

function getAnnexes() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getAnnexesList",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            createAnnexesTbl(data.annexes);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Деньги инвесторов загружены!');
        });
}


function checkLocalStorage() {
    return window.localStorage;
}

function getDateFromString(strDate) {
    var parts = strDate.split(' ');
    var iMonth = 0;
    var reportDate;
    switch (parts[0]) {
        case 'January':
        case 'Январь':
            iMonth = 0;
            break;
        case 'February':
        case 'Февраль':
            iMonth = 1;
            break;
        case 'March':
        case 'Март':
            iMonth = 2;
            break;
        case 'April':
        case 'Апрель':
            iMonth = 3;
            break;
        case 'May':
        case 'Май':
            iMonth = 4;
            break;
        case 'June':
        case 'Июнь':
            iMonth = 5;
            break;
        case 'July':
        case 'Июль':
            iMonth = 6;
            break;
        case 'August':
        case 'Август':
            iMonth = 7;
            break;
        case 'September':
        case 'Сентябрь':
            iMonth = 8;
            break;
        case 'October':
        case 'Октябрь':
            iMonth = 9;
            break;
        case 'November':
        case 'Ноябрь':
            iMonth = 10;
            break;
        case 'December':
        case 'Декабрь':
            iMonth = 11;
            break;
    }

    reportDate = new Date(parseInt(parts[1]), iMonth, 1);
    return Date.parse(reportDate.toDateString());

}

function appendMonths(months) {
    var month;
    var monthNumb;
    var year;
    month = months[0].split(' ')[0];
    year = months[0].split(' ')[1];
    switch (month) {
        case 'Янв':
            monthNumb = 0;
            break;
        case 'Фев':
            monthNumb = 1;
            break;
        case 'Мар':
            monthNumb = 2;
            break;
        case 'Апр':
            monthNumb = 3;
            break;
        case 'Май':
            monthNumb = 4;
            break;
        case 'Июн':
            monthNumb = 5;
            break;
        case 'Июл':
            monthNumb = 6;
            break;
        case 'Авг':
            monthNumb = 7;
            break;
        case 'Сент':
            monthNumb = 8;
            break;
        case 'Окт':
            monthNumb = 9;
            break;
        case 'Ноя':
            monthNumb = 10;
            break;
        case 'Дек':
            monthNumb = 11;
            break;
    }
    for (var i = months.length; i < 6; i++) {
        monthNumb--;
        if (monthNumb < 0) {
            monthNumb = 11;
            year = parseInt(year) - 1;
        }
        months.splice(0, 0, monthNames[monthNumb] + ' ' + year);

    }

    return months;
}

function createAnnexesTbl(annexes) {

    /*
    var annexList = [];

    $.each(annexes, function(index, event) {
        var events = $.grep(annexList, function (e) {
            return event.id === e.id &&
                event.annexName === e.annexName;
        });
        if (events.length === 0) {
            annexList.push(event);
        }
    });
    */
    //console.log(annexList);

    //annexes.annex = annexList;
    //console.log(annexes);

    var body = $('table#tblAnnex').find('tbody');
    var tr, td, a, label, input, strDate;
    body.empty();

    $.each(annexes, function (i, el) {
        tr = $('<tr></tr>');
        tr.attr('id', el.id);
        tr.appendTo(body);
        td = $('<td>' + el.annex.annexName + '</td>');
        td.appendTo(tr);

        td = $('<td></td>');
        td.data('annexName', el.annex.annexName);
        a = $('<a>Посмотреть</a>');
        a.attr('href', '/annexToContract/' + el.annex.annexName)
            .attr('target', '_blank');
        a.appendTo(td);
        td.appendTo(tr);

        label = $('<label style="margin-right: 5px">Ознакомлен</label>');
        label.attr('htmlFor', 'annexId' + el.id);
        input = $('<input type="checkbox">');
        input.attr('checked', el.annexRead === 1);
        input.attr('disabled', el.annexRead === 1);
        input.data('userId', el.userId);
        input.data('annexId', el.id);
        input.data('annexName', el.annex.annexName);
        td = $('<td></td>');
        label.appendTo(td);
        input.appendTo(td);
        td.appendTo(tr);

        strDate = el.dateRead === null ? '' : new Date(el.dateRead).toLocaleDateString();

        td = $('<td>' + strDate + '</td>');
        td.attr('id', 'annexDate-' + el.id);
        td.appendTo(tr);

    });

    var unread = checkUnreadAnnex();
    if (unread > 0) {
        blurElement($('.out'), 4);
        $('#readAnnex').css('z-index', '1000001');
        $('#look').attr('disabled', false);
    } else {
        blurElement($('.out'), 0);
        $('#readAnnex').css('z-index', '-1000001');
    }

}

function setAnnexRead(id, userId, annex, read) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var usersAnnexToContracts = {
        id: id
        /*
        userId: userId,
        annex: annex,
        annexRead: read,
        dateRead: new Date().getTime()
        */
    };

    var search = ({
        "usersAnnexToContracts": usersAnnexToContracts
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "setReadToAnnex",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            updateUnreadAnnexes();
        },
        error: function (e) {
            console.log('Произошла ошибка - ' + e.toString());
        }
    });
}

function updateUnreadAnnexes() {
    var unchecked = checkUnreadAnnex();
    var read = $('#read');
    if (unchecked === 0) {
        setTimeout(function () {
            read.removeClass('js-active')
        }, 1500);
        $('#readAnnex').css('z-index', '-1000001');
        blurElement($('.out'), 0);
    }

}

function checkUnreadAnnex() {
    return $('#tblAnnex').find('tr').find('input[type="checkbox"]').not(':checked').length;
}

function blurElement(element, size) {
    var filterVal = 'blur(' + size + 'px)';
    $(element).css({
        'filter': filterVal,
        'webkitFilter': filterVal,
        'mozFilter': filterVal,
        'oFilter': filterVal,
        'msFilter': filterVal,
        'transition': 'all 0.5s ease-out',
        '-webkit-transition': 'all 0.5s ease-out',
        '-moz-transition': 'all 0.5s ease-out',
        '-o-transition': 'all 0.5s ease-out'
    });

}

var localStorageSpace = function () {
    var allStrings = '';
    for (var key in window.localStorage) {
        if (window.localStorage.hasOwnProperty(key)) {
            allStrings += window.localStorage[key];
        }
    }
    return allStrings ? 3 + ((allStrings.length * 16) / (8 * 1024)) + ' KB' : 'Empty (0 KB)';
};

function getMaxInFacility(cashes) {
    var invested = [];
    cashes.reduce(function (res, value) {
        var facility = value.facility.facility;
        if (!res[facility]) {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        if (res[facility].facility === facility) {
            res[facility].givedCash += value.givedCash;
        } else {
            res[facility] = {
                givedCash: 0,
                facility: facility
            };
            invested.push(res[facility])
        }
        return res;
    }, {});

    var max = 0;
    var indMax = -1;
    var facility;
    $.each(invested, function (ind, el) {
        if (max < el.givedCash) {
            max = el.givedCash;
            indMax = ind;
        }
    });
    facility = invested[indMax].facility;
    return facility;

}

function unique(array) {
    return $.grep(array, function (el, index) {
        return index === $.inArray(el, array);
    });
}