let monthNames = [
    'Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сент', 'Окт', 'Ноя', 'Дек'
];
let monthLongNames = [
    'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'
];

let loop = 0;
let maxFacility = '';

jQuery(document).ready(function ($) {

    $(function () {
        function onHover(evt/**$.Event*/) {
            if (evt.type === 'mouseenter') {
                $(this).animate({
                    height: '100px',
                    'max-width': '120px',
                    'font-size': '14px',
                    left: '-30px',
                    top: '-30px',
                    'background-color': 'white',
                    opacity: '0.9',
                    'text-align': 'center',
                    'z-index': 500,
                    'border': '1px solid white',
                    'border-radius': '5px'
                }, 150);
                $('.profit').css('max-width', '120px');
                $('.splitter').css('max-width', '120px');
                $('.gains').css('max-width', '120px');

            } else {
                $(this).animate({
                    height: '40px',
                    'max-width': '60px',
                    'font-size': '6px',
                    left: '0px',
                    top: '0px',
                    'background-color': 'transparent',
                    opacity: '1',
                    'text-align': 'center',
                    'font-color': 'black'
                }, 150);
                $('.profit').css('max-width', '120px');
                $('.splitter').css('max-width', '120px');
                $('.gains').css('max-width', '120px');
            }
        }

        $('.profitBox').hover(onHover);
        $(document).on('mouseenter mouseleave', '.profitBox', onHover);

    });

    $(document).on('click', '#showInvestsDetails', function (e) {
        e.preventDefault();

        blurElement($('#investments').find('.popup__inner'), 4);

        $('#investmentsAll').removeClass('js-active');
        $('#investments').addClass('js-active');
        let facility = $(this).parent().parent().children(':first').text();

        $('#investmentsDetailsTitle').html('Ваши вложения в ' + facility);

        let ind = $('div#investments')
            .find('div.box__select')
            .find('div.selectric-hide-select')
            .find('#in_out option:contains("Вложено")').val();
        $('#in_out').prop('selectedIndex', ind).selectric('refresh');

        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
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
                createIncomeTable(data.investorsCashList, facility);
            })
            .fail(function (e) {
                console.log(e);
            })
            .always(function () {
                console.log('Деньги инвесторов загружены!');
            });
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
        let userId = $(this).data('user-id');
        let annex = {
            id: $(this).data('annex-id'),
            annexName: $(this).data('annex-name')
        };
        $(this).attr('disabled', 'disabled');
        let d = new Date();
        let output = d.toLocaleDateString();//day + '.' + month + '.' + year;
        $('#annexDate-' + annex.id).html(output);
        let trId = $(this).closest('tr').attr('id');
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
        grey: '#cacaca',
        black: '#000000'
    };
    onStartUp();
    prepareToggle();

    $(document).on('click', '#searchIncomesByDate', function (e) {
        e.preventDefault();
        let month = parseInt($('#months').find('option:selected').val());
        let year = $('#years').find('option:selected').text();
        let fullDate = new Date(parseInt(year), month, 1);

        getInvestorsFlowsList('max', fullDate);
    });

    $(document).on('click', '#ext', function () {
        window.location.href = '/logout'
    });

    $('#searchInvestmentsByDate').on('click', function (e) {
        e.preventDefault();
        let facility = $('#investmentsDetailsTitle').text().split(' ')[3];
        let investments = $('#investments');

        let tmp;
        let dateFrom = $(this).parent().find('.date-select').find('.date-select-wrapper.flex-vhcenter').find('.field:first').find('input[name=from]').val();
        let dateTo = $(this).parent().find('.date-select').find('.date-select-wrapper.flex-vhcenter').find('.field:last').find('input[name=to]').val();
        if (typeof dateFrom === 'undefined' || dateFrom === '') {
            let d = new Date();
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

        let totalSum = 0;
        let dataCash;
        investments.find('[data-date]').each(function () {
            let cur = $(this).data('date');
            let curFacility = $(this).parent().find('[data-facility]').text();
            tmp = cur.split('.');
            cur = new Date(parseInt(tmp[2]), parseInt(tmp[1]) - 1, parseInt(tmp[0]));
            if (cur >= dateFrom && cur <= dateTo && curFacility === facility) {
                $(this).parent().show();
                dataCash = $(this).parent().find('[data-cash]');
                totalSum += parseFloat(dataCash.data('cash'));
            } else {
                $(this).parent().hide();
            }
        });
        $('#totalSumFooter').find('span').empty().html('Итого за выбранный период: ' +
            new Intl.NumberFormat('ru-RU', {
                minimumFractionDigits: 0
            }).format(Math.round(totalSum)) + ' руб.');
    });

});

function prepareToggle() {
    $('.accordion__item').each(function () {
        let self = $(this);
        let $body = self.find('.accordion__body');
        let $btnAction = self.find('.col_action span');

        let $item = self.find('.accordionChild__item');

        $item.each(function () {
            let $itm = $(this);
            let $rowAction = $itm.find('.row_action');
            let $child = $rowAction.find('.row_child-wrapper');
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
        case 8:
            return window.chartColors.black;
    }
}

function prepareIncomeDiagram(investorsFlowsList) {
    let invFlows = $.grep(investorsFlowsList, function (el) {
        return el.afterCashing >= 0;
    });
    let reportDates = [];
    let tmpDate = [];

    let cash;
    let cashes = [];
    $.each(invFlows, function (ind, el) {
        reportDates.push(new Date(el.reportDate));
        if (el.facility !== null) {
            cash = {
                reportDate: new Date(el.reportDate),
                facility: el.facility.facility,
                sum: el.afterCashing
            };
            cashes.push(cash);
        }
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

    let tmpYears = [];
    let years;
    $.each(reportDates, function (ind, el) {
        tmpDate.push(monthNames[el.getMonth()] + ' ' + el.getFullYear());
        tmpYears.push(el.getFullYear());
    });

    reportDates = unique(tmpDate);
    years = unique(tmpYears);

    let groupedCashes = [];

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

    let reducedCash = JSON.parse(JSON.stringify(groupedCashes));

    $.each(reducedCash, function (ind, el) {
        el.sum = Math.round(el.sum * 100) / 100;
    });

    let facilities = [];
    $.each(reducedCash, function (i, el) {
        facilities.push(el.facility);
    });
    facilities = unique(facilities);

    let months = $('#months');
    months
        .find('option')
        .remove()
        .end();

    $.each(monthLongNames, function (key, value) {
        let option = $('<option></option>');
        option.attr('value', key);
        option.text(value);

        if (key === new Date().getMonth()) {
            option.attr('selected', true)
        }

        option.appendTo(months);
    });

    let optYears = $('#years');
    optYears
        .find('option')
        .remove()
        .end();

    $.each(years, function (key, value) {
        let option = $('<option></option>');
        option.attr('value', value);
        option.text(value);

        if (value === new Date().getFullYear()) {
            option.attr('selected', true)
        }

        option.appendTo(optYears);
    });


    let incomesAndExpenses = $('#incomesAndExpenses');

    incomesAndExpenses
        .find('option')
        .remove()
        .end();

    $.each(facilities, function (key, value) {
        let option = $('<option></option>');
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
            disableOnMobile: false,
            nativeOnMobile: false,
            onChange: function () {
                let facility = $('.inner__row.inner__row_costs.flex-vhcenter').find('.selectric:last').find('span.label').text();
                getMainFlowsList(facility);
            }
        });

        $('select#months').selectric({
            disableOnMobile: false,
            nativeOnMobile: false,
            onChange: function () {
                let month = $('.inner__row.inner__row_finance.flex-vhcenter')
                    .find('.circle')
                    .find('.box__select:first')
                    .find('.selectric:last').find('span.label').text();
            }
        });

        $('select#years').selectric({
            disableOnMobile: false,
            nativeOnMobile: false,
            onChange: function () {
                let year = $('.inner__row.inner__row_finance.flex-vhcenter')
                    .find('.circle')
                    .find('.box__select:last')
                    .find('.selectric:last').find('span.label').text();
            }
        });
    });

    let totals = [];
    let total;
    let sums = [];
    let totalSums = [];
    let i = -1;
    let facilitiesDates = [];

    let res = groupedCashes.reduce(function (prev, cur, i) {
        let cash = prev[cur.reportDate];
        if (cash) {
            cash.sum += cur.sum;
        } else {
            prev[cur.reportDate] = cur;
        }
        return prev;

    }, []);

    let eachObject = function (obj) {
        for (let key in obj) {
            totalSums.push(Number(obj[key].sum).toFixed(2));
        }
    };

    eachObject(res);

    $.each(facilities, function (ind, el) {
        sums = [];
        facilitiesDates = [];
        i = -1;
        $.each(reportDates, function (indDates, elDates) {
            i++;
            $.each(reducedCash, function (indGr, elGr) {
                if (elGr.facility === el && elGr.reportDate === elDates) {
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
        let oldEl = JSON.parse(JSON.stringify(el));
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
        for (let j = totalSums.length; j < reportDates.length; j++) {
            totalSums.splice(0, 0, 0);
        }
    }

    let ds;
    let dSet = [];
    let color = 0;
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

    let max = Math.max.apply(Math, totalSums);

    let step = Math.floor(max / parseInt('5' + zeroDigits(max.toString().indexOf('.') - 2)))
        * parseInt('1' + zeroDigits(max.toString().indexOf('.') - 2));
    max = step * 7;

    Chart.plugins.register({
        id: 'scroll-plugin',
        rendered: false,
        afterDraw: function (chart, options) {
            let chartOptions = chart.options;

            if (!chartOptions.scroll) {
                return;
            }
            nummberColumns = chartOptions.nummberColumns > 1 ? chartOptions.nummberColumns : chart.config.data.labels.length;
            chartOptions.scroll = false;

            this.rendered = true;

            let chartWrapper = $('<div class="chart-wrapper"></div>');
            let chartAreaWrapper = $('<div class="chart-area-wrapper"></div>');
            chartAreaWrapper.width((chart.width / nummberColumns) * chart.config.data.labels.length);
            $(chart.ctx.canvas).wrap(chartWrapper).wrap(chartAreaWrapper);
            chartAreaWrapper.wrap(chartWrapper);

            $(chartOptions.legendSelector).html(chart.generateLegend());
            $(chartOptions.legendSelector + " > ul > li").on("click", function (e) {
                let index = $(this).index(),
                    dataset = chart.data.datasets[index];

                $(this).toggleClass("hidden");
                dataset.hidden = !dataset.hidden;

                chart.update();
            });

            let copyWidth = chart.scales['y-axis-0'].width - 10;
            let copyHeight = chart.scales['y-axis-0'].height + chart.scales['y-axis-0'].top + 10;

            let chartAxis = document.querySelector(chartOptions.axisSelector);
            chartAxis.style.height = copyHeight + 'px';
            chartAxis.style.width = copyWidth + 'px';

            let ticks = chart.scales['y-axis-0'].ticksAsNumbers;

            let $chartList = $(chartOptions.axisSelector + ' > ul');
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

    let config = {
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
                                        fontSize: 14,
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
                    bodyFontSize: 14,
                    titleFontSize: 14,
                    footerFontSize: 14,
                    mode: 'label',
                    callbacks: {
                        afterTitle: function () {
                            window.total = 0;
                        },
                        label: function (tooltipItem, data) {
                            let facility = data.datasets[tooltipItem.datasetIndex].label;
                            let totalSum = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                            totalSum = parseFloat(totalSum);

                            window.total += totalSum;
                            if (totalSum !== 0) {
                                return facility + ": " + totalSum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");
                            }
                        },
                        footer: function () {
                            return "Итого: " + window.total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ");

                        }
                    },
                    itemSort: function (a, b) {
                        return b.datasetIndex - a.datasetIndex
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
    let ctx1 = document.getElementById("chart-finance").getContext("2d");
    window.myDoughnut1 = new Chart(ctx1, config);
    */
    let oldChart = window.myDoughnut1;
    let ctx1 = document.getElementById("chart-finance").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnut1 = new Chart(ctx1, config);
    } else {
        window.myDoughnut1.config = config;
        window.myDoughnut1.update();
    }

    $('#chart-finance-legend').html(myDoughnut1.generateLegend());
    $('#chart-finance-legend > ul > li').on('click', function (e) {
        let index = $(this).index(),
            chart = e.view.myDoughnut1,
            dataset = chart.data.datasets[index]._meta[2];
        $(this).toggleClass("hidden");
        dataset.hidden = !dataset.hidden;
        chart.update();
    });
}

function zeroDigits(numb) {
    let str = '';
    for (let i = 0; i < numb; i++) {
        str += "0";
    }
    return str;
}

function prepareIncomeCircle(investorsFlowsList, fullDate) {
    let invFlows;
    let month = new Date(fullDate).getMonth();
    let year = new Date(fullDate).getFullYear();
    let elDate;
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

    let dates = [];
    $.each(invFlows, function (ind, el) {
        dates.push(new Date(el.reportDate));
    });

    let maxDate;
    if (dates.length === 0 && fullDate !== '') maxDate = fullDate;
    else if (dates.length > 0) maxDate = new Date(Math.max.apply(null, dates));
    else maxDate = new Date();


    let ttl = 'Ваш доход за ' + monthLongNames[maxDate.getMonth()].toLowerCase() + ' ' + maxDate.getFullYear();

    $('#months').val(maxDate.getMonth());
    $('#years').val(maxDate.getFullYear());

    invFlows = $.grep(invFlows, function (el) {
        let elDate = new Date(el.reportDate);
        let curDate = new Date(maxDate);
        return elDate.getMonth() === curDate.getMonth() && elDate.getFullYear() === curDate.getFullYear();
    });

    let facilitiesColors = [];
    let facilityColor = {};
    let chartJS = window.myDoughnut1.config.data.datasets;
    $.each(chartJS, function (ind, el) {
        facilityColor = {
            facility: el.label,
            color: el.backgroundColor
        };
        facilitiesColors.push(facilityColor);
    });

    let cash;
    let cashes = [];
    $.each(invFlows, function (ind, el) {
        cash = {
            facility: el.facility.facility,
            sum: el.afterCashing
        };
        cashes.push(cash);
    });

    let groupedCashes = [];

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
    let totalIncome;
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
    let clr;
    $('#totalIncome').html('<span>' + new Intl.NumberFormat('ru-RU').format(totalIncome) + '</span>' + 'рублей');
    $('#incomesList').html('');
    if (groupedCashes.length > 0) {
        $.each(groupedCashes, function (ind, el) {
            clr = facilitiesColors.filter(function (indCol) {
                return indCol.facility === el.facility;
            });

            $('#incomesList').append('<div class="circle-canvas__item" style="color: ' +
                clr[0].color
                + '"><span>' + new Intl.NumberFormat('ru-RU').format(el.sum) + '</span>' + el.facility + '</div>');
        });
    } else {
        for (let i = 0; i < 2; i++) {
            $('#incomesList').append('<div class="circle-canvas__item" style="color: transparent"><span>' + new Intl.NumberFormat('ru-RU').format(0.0001) + '</span>' + 'Объекты' + '</div>');
        }
    }

    let facilities = [];
    let sums = [];
    let colors = [];
    if (groupedCashes.length > 0) {
        $.each(groupedCashes, function (i, el) {
            facilities.push(el.facility);
            sums.push(el.sum);

            $.each(facilitiesColors, function (indCol, elCol) {
                if (elCol.facility === el.facility) {
                    colors.push(elCol.color)
                }
            });
        });
        facilities = unique(facilities);
    } else {
        facilities.push('');
        sums.push(0.001);
        colors.push(getColor(0));
    }


    let configDoughnut = {
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

    let oldChart = window.myDoughnut2;
    let ctx2 = document.getElementById("doughnut-chart").getContext("2d");
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
    let dates;
    let dateMonth;
    switch (iMonth) {
        case 'max':
            dates = investorsFlowsList.map(function (x) {
                return new Date(x.reportDate);
            });
            dateMonth = new Date(Math.max.apply(null, dates));
            break;
        default:
            dateMonth = new Date(iMonth);
    }

    let invFlows = $.grep(investorsFlowsList, function (el) {
        return (new Date(el.reportDate).getMonth() === dateMonth.getMonth() && new Date(el.reportDate).getFullYear() === dateMonth.getFullYear())
            && el.underFacilities != null && el.underFacilities.facility != null;
    });

    let facilitiesFlows = [];
    let facilityFlows;

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

    let gFacilitiesFlows = [];
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

    $.each(gFacilitiesFlows, function (ind, el) {
        if (el.cnt !== el.cntKind) {
            el.share = el.share / el.cntKind;
        } else {
            el.share = el.share / el.cnt;
        }
        el.tax = el.tax / el.cnt;
        el.comission = el.comission / el.cnt;
    });

    let elemChild;
    let elemParent;
    let elHead = $('#detailsProfit').find('.simplebar-content');
    elHead.html('');
    let constParent;

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

    let settlementDates = [];
    let tmpDate = [];
    let mainFlows;
    switch (facility) {
        case 'Все объекты':
            mainFlows = mainFlowsList;
            break;
        default:
            mainFlows = $.grep(mainFlowsList, function (el) {
                return el.underFacilities.facility.facility === facility;
            });
    }

    let dates = [];
    $.each(mainFlows, function (ind, el) {
        dates.push(new Date(el.settlementDate));
    });
    if (dates.length === 0) {
        dates.push(new Date())
    }
    if (facility === null) facility = '';
    let maxDate = new Date(Math.max.apply(null, dates));
    let ttl = 'Баланс ' + facility + ' за ' + monthLongNames[maxDate.getMonth()].toLowerCase() + ' ' + maxDate.getFullYear();
    $('#totalExpenses').html(ttl);

    let flow;
    let flows = [];
    if (mainFlows.length === 0) {
        settlementDates.push(new Date());
    }
    $.each(mainFlows, function (ind, el) {
        settlementDates.push(new Date(el.settlementDate));

        if (el.summa >= 0) {
            flow = {
                underFacility: el.underFacilities.underFacility,
                settlementDate: new Date(el.settlementDate),
                incomeOrExpense: 'Доходы',
                sum: el.summa,
                payment: el.payment.replace('%', '')
            };
        } else {
            flow = {
                underFacility: el.underFacilities.underFacility,
                settlementDate: new Date(el.settlementDate),
                incomeOrExpense: 'Расходы',
                sum: el.summa,
                payment: el.payment
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

    let items = {}, key;
    $.each(flows, function (index, val) {
        key = val.underFacility + val.settlementDate + val.payment + val.incomeOrExpense;
        if (!items[key]) {
            items[key] = {
                //facility: val.facility,
                underFacility: val.underFacility,
                payment: val.payment,
                sum: 0,
                incomeOrExpense: val.incomeOrExpense,
                settlementDate: val.settlementDate
            };
        }
        if (items[key].underFacility === val.underFacility &&
            items[key].incomeOrExpense === val.incomeOrExpense &&
            items[key].settlementDate === val.settlementDate &&
            items[key].payment === val.payment) {
            items[key].sum += val.sum;
        } else {
            items[key] = {
                //facility: val.facility,
                underFacility: val.underFacility,
                payment: val.payment,
                sum: val.sum,
                incomeOrExpense: val.incomeOrExpense,
                settlementDate: val.settlementDate
            };
        }
    });

    let outputArr = [];
    $.each(items, function (key, val) {
        outputArr.push(val);
    });

    let sums = [];
    let incomes = {};
    let expenses = {};

    settlementDates.map(function (value) {
        return incomes[value] = 0;
    });

    settlementDates.map(function (value) {
        return expenses[value] = 0;
    });

    $.each(outputArr, function (ind, el) {
        if (el.incomeOrExpense === 'Доходы') {
            incomes[el.settlementDate] += parseFloat(el.sum);
            sums.push(incomes[el.settlementDate]);
        } else {
            expenses[el.settlementDate] += Math.abs(parseFloat(el.sum));
            sums.push(expenses[el.settlementDate]);
        }
    });

    $.each(settlementDates, function (ind, el) {
        incomes[el] = incomes[el].toFixed(2);
        expenses[el] = expenses[el].toFixed(2);
    });

    let max = Math.max.apply(Math, sums);

    let zeroCount = parseInt('1' + zeroDigits(max.toFixed(2).toString().indexOf('.') - 1));
    let step = (max / 7 / zeroCount).toFixed(2) * zeroCount;
    let min = max === 0 ? -100 : 0;
    if (max === 0) {
        step = 100;
    }

    max = step * 8;

    if (settlementDates.length < 6) {
        settlementDates = appendMonths(settlementDates);
    }

    if (Object.keys(incomes).length < settlementDates.length) {
        for (let j = incomes.length; j < settlementDates.length; j++) {
            incomes.splice(0, 0, 0);
        }
    }
    if (Object.keys(expenses).length < settlementDates.length) {
        for (let j = expenses.length; j < settlementDates.length; j++) {
            expenses.splice(0, 0, 0);
        }
    }

    let dataSets = [];
    let dataSet = {};
    let incomesArr = [];
    let expensesArr = [];
    Array.prototype.groupBy = function (prop) {
        return this.reduce(function (groups, item) {
            const val = item[prop];
            groups[val] = groups[val] || [];
            groups[val].push(item);
            return groups
        }, {})
    };

    const groupedByUnderFacility = outputArr.groupBy('underFacility');
    const groupedByPayment = outputArr.groupBy('payment');
    let counter = 0;
    $.each(groupedByUnderFacility, function (indSd, elSd) {
        counter = 0;
        incomesArr = [];
        key = indSd;
        $.each(elSd, function (indEl, elEl) {
            if (elEl.sum >= 0) {

                if (!dataSet[key]) {
                    //incomesArr.push(elEl.sum);
                    dataSet[key] = {
                        underFacility: key,
                        settlementDate: elEl.settlementDate,
                        sums: incomesArr
                    };

                    while (settlementDates[counter] !== elEl.settlementDate) {
                        dataSet[key].sums.push(0);
                        counter++;
                    }
                    dataSet[key].sums.push(elEl.sum);
                } else {
                    if (dataSet[key].settlementDate === elEl.settlementDate) {
                        dataSet[key].sums[dataSet[key].sums.length - 1] += elEl.sum;
                    } else {
                        if (settlementDates[dataSet[key].sums.length] !== elEl.settlementDate) {
                            incomesArr.push(0);
                        } else {
                            incomesArr.push(elEl.sum);
                            dataSet[key] = {
                                underFacility: key,
                                settlementDate: elEl.settlementDate,
                                sums: incomesArr
                            };
                        }
                    }
                }
            }
        });
        if (dataSet[key]) {
            if (settlementDates.length > dataSet[key].sums.length) {
                for (let i = dataSet[key].sums.length - 1; i < settlementDates.length - 1; i++) {
                    dataSet[key].sums.push(0);
                }
            }
        }
    });

    $.each(dataSet, function (key, val) {
        dataSets.push(val);
    });

    dataSet = {};
    key = '';

    $.each(groupedByPayment, function (indSd, elSd) {
        counter = 0;
        expensesArr = [];
        key = indSd;
        $.each(elSd, function (indEl, elEl) {
            if (elEl.sum < 0) {
                let sum = parseFloat((-1 * elEl.sum).toFixed(2));
                if (!dataSet[key]) {
                    dataSet[key] = {
                        payment: key,
                        settlementDate: elEl.settlementDate,
                        sums: expensesArr
                    };

                    while (settlementDates[counter] !== elEl.settlementDate) {
                        dataSet[key].sums.push(0);
                        counter++;
                    }
                    dataSet[key].sums.push(sum);
                } else {
                    if (dataSet[key].settlementDate === elEl.settlementDate) {
                        dataSet[key].sums[dataSet[key].sums.length - 1] += sum;
                    } else {
                        while (settlementDates[dataSet[key].sums.length] !== elEl.settlementDate) {
                            expensesArr.push(0);
                        }

                        expensesArr.push(sum);
                        dataSet[key] = {
                            payment: key,
                            settlementDate: elEl.settlementDate,
                            sums: expensesArr
                        };

                    }
                }
            }
        });
        if (dataSet[key]) {
            if (settlementDates.length > dataSet[key].sums.length) {
                for (let i = dataSet[key].sums.length - 1; i < settlementDates.length - 1; i++) {
                    dataSet[key].sums.push(0);
                }
            }
        }
    });
    let exDataSets = [];
    $.each(dataSet, function (key, val) {
        exDataSets.push(val);
    });

    let underFacilitiesIncomesColors = [
        '#1f7a1f',
        '#29a329',
        '#33cc33',
        '#5cd65c',
        '#85e085',
        '#adebad',
        '#d6f5d6'
    ];
    let underFacilitiesExpensesColors = [
        '#995c00',
        '#cc7a00',
        '#ff9900',
        '#ffad33',
        '#ffc266',
        '#ffd699',
        '#ffebcc'
    ];


    let finDs = [];
    $.each(dataSets, function (ind, el) {

        finDs.push({
            label: el.underFacility,
            backgroundColor: underFacilitiesIncomesColors[ind],
            //borderColor: getColor(8),
            borderWidth: 0,
            data: el.sums,
            fill: false,
            stack: 'Stack 1'
        })
    });

    let clr = 0;
    $.each(exDataSets, function (ind, el) {
        clr = ind >= 8 ? 0 : ind;
        clr = clr === 4 ? ++clr : clr;
        finDs.push({
            label: el.payment,
            backgroundColor: underFacilitiesExpensesColors[clr],
            //borderColor: getColor(8),
            borderWidth: 0,
            data: el.sums,
            fill: false,
            stack: 'Stack 2'
        })
    });

    let configCosts = {
        type: 'bar',
        data:
            {
                labels: settlementDates,
                datasets: finDs
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
                                        fontSize: 14,
                                        fontStyle: 600,
                                        fontFamily: 'OpenSans'
                                    },
                                gridLines:
                                    {
                                        display: true
                                    },
                                stacked: true
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
                                    },
                                stacked: true
                            }]
                    },
                tooltips: {
                    titleFontSize: 14,
                    bodyFontSize: 14,
                    mode: 'x',
                    itemSort: function (a, b) {
                        return b.datasetIndex - a.datasetIndex
                    },
                    callbacks: {
                        label: function (tooltipItem, chart) {
                            if (tooltipItem.yLabel > 0) {
                                return chart.datasets[tooltipItem.datasetIndex].label + ': ' + new Intl.NumberFormat('ru-RU', {
                                    maximumFractionDigits: 2,
                                    minimumFractionDigits: 2
                                })
                                    .format(tooltipItem.yLabel) + ' руб.';
                            }
                        }
                    }
                },
                animation: {
                    onComplete: function (e) {
                        if (e.numSteps === 0) {
                            $('.chart-wrapper').stop(true, true).animate({scrollLeft: $('.chart-area-wrapper').outerWidth()}, 500);
                            $('#chart-axis2').css('height', '390px');
                            $('#chart-costs-legend').find('ul').css({
                                'width': '650px',
                                'margin-left': '10%',
                                'margin-top': '1%'
                            });
                        }
                    }
                }
            }
    };

    let chartOptions;

    let oldChart = window.myDoughnut;
    let ctxCosts = document.getElementById("chart-costs").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnut = new Chart(ctxCosts, configCosts);
    } else {
        chartOptions = window.myDoughnut.options;
        let nummberColumns = chartOptions.nummberColumns;
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

        let $chartList = $(chartOptions.axisSelector);
        $chartList.append('<ul></ul>');
        $chartList.find('ul').html('');

        for (let i = max; i >= min; i -= step) {
            $chartList.find('ul').append('<li>' + new Intl.NumberFormat('ru-RU', {
                maximumFractionDigits: 2,
                minimumFractionDigits: 0
            }).format(i) + '</li>')
        }
        //window.myDoughnut.update();
    }

    let chartLegend = $('#chart-costs-legend');
    chartLegend.html(myDoughnut.generateLegend());
    chartLegend.find("> ul > li").on("click", function (e) {
        let index = $(this).index(),
            chart = e.view.myDoughnut,
            dataset = chart.data.datasets[index]._meta[4];
        $(this).toggleClass("hidden");
        dataset.hidden = !dataset.hidden;
        chart.update();
    });

    let totalIncomes = 0;
    for (let k in incomes) {
        if (incomes.hasOwnProperty(k)) {
            totalIncomes = parseFloat(incomes[k]);
        }
    }
    let totalExpenses = 0;
    for (let k in expenses) {
        if (expenses.hasOwnProperty(k)) {
            totalExpenses = parseFloat(expenses[k]);
        }
    }

    if (totalIncomes === 0) totalIncomes = 0.0001;
    if (totalExpenses === 0) totalExpenses = 0.0001;

    let totalIn = totalIncomes - totalExpenses;
    $('#totalExpensesText').html('<span>' + new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2
    }).format(totalIn) + '</span>' + 'рублей');
    let configDoughnutExpenses = {
        type: 'doughnut',
        data:
            {
                labels: ['Доходы', 'Расходы'],
                datasets: [
                    {
                        label: "",
                        backgroundColor: [underFacilitiesIncomesColors[0], underFacilitiesExpensesColors[0]],
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
                                fontSize: 14
                            }
                    },
                title:
                    {
                        display: false,
                        fontStyle: 'bold',
                        fontSize: '14',
                        fontColor: '#11325b',
                        text: ''
                    }
            }
    };

    let oldChartExpenses = window.myDoughnutExpenses;
    let ctxExpenses = document.getElementById("doughnut-chart-expenses").getContext("2d");
    if (typeof oldChartExpenses === 'undefined') {
        window.myDoughnutExpenses = new Chart(ctxExpenses, configDoughnutExpenses);
    } else {
        window.myDoughnutExpenses.config = configDoughnutExpenses;
        window.myDoughnutExpenses.update();
    }

    let expensesList = $('#expensesList');
    expensesList.html('');
    expensesList.append('<div class="circle-canvas__item" style="color: ' + underFacilitiesIncomesColors[0] + '"><span>' + new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2
    }).format(totalIncomes) + '</span>' + "Доходы" + '</div>');
    expensesList.append('<div class="circle-canvas__item" style="color: ' + underFacilitiesExpensesColors[0] + '"><span>' + new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
        minimumFractionDigits: 2
    }).format(totalExpenses) + '</span>' + "Расходы" + '</div>');

    $('div.inner__row.inner__row_costs.flex-vhcenter').find('div.box__select').find('div.selectric').find('span.label').text(maxFacility);
    let ind = $('div.inner__row.inner__row_costs.flex-vhcenter')
        .find('div.box__select')
        .find('div.selectric-hide-select')
        .find('#incomesAndExpenses option:contains("' + maxFacility + '")').val();
    $('#incomesAndExpenses').prop('selectedIndex', ind).selectric('refresh');
}

function prepareInvestedMoney(investedMoneyDb) {
    maxFacility = investedMoneyDb.facilityWithMaxSum;

    let investor = investedMoneyDb.investor;

    let totalMoney = investedMoneyDb.totalMoney || 0;

    let investedBalance = investedMoneyDb.invested;

    let finalArr = investedMoneyDb.invested;

    let investorsCashes = investedMoneyDb.investorsCashList;

    let colors = [];

    $('#balance').find('span:last').html('<b>' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(totalMoney) + '</b>' + ' рублей');

    $('#totalInvested').html('<span>' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(totalMoney) + '</span>' + 'рублей');
    $('#investedList').html('');
    $.each(investedBalance, function (ind, el) {
        if (el.openCash > 0) {
            colors.push(getColor(ind));
            $('#investedList').append('<div class="circle-canvas__item" style="color: ' + getColor(ind) + '"><span>' + new Intl.NumberFormat('ru-RU', {
                maximumFractionDigits: 2,
                minimumFractionDigits: 2
            }).format(el.openCash) + '</span>' + el.facility + '</div>');
        }
    });

    let maxPriceFacility = 0;
    if (finalArr !== null) {
        maxPriceFacility = Math.max.apply(Math, finalArr.map(function (value) {
            return value.myCash
        }));
    }

    let investedMoney = $('#investedMoney');
    investedMoney.empty();

    $.each(finalArr, function (ind, el) {
        if ((el.facility !== 'Вайнера' && el.facility !== 'Суворовский' && el.facility !== 'Энергетиков') || el.myCash > 0) {
            let percentLen = Math.floor((el.myCash / maxPriceFacility) * 100) | 0;
            let percentBg = Math.floor((el.openCash / el.myCash) * 100) | 0;
            if (percentLen !== 0) {
                let colDiv = $('<div class="column"></div>');
                colDiv.appendTo(investedMoney);
                let priceFirst = $('<div class="column__price-first">' + new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(el.coast) + '</div>');
                priceFirst.appendTo(colDiv);
                let colBody = $('<div style="height:' + percentLen + '%" class="column__body"></div>');
                colBody.appendTo(colDiv);

                let percentBgStr = Math.floor((el.myCash / el.coast) * 100) | 0;//percentBg;

                let leftPerc = 0;
                if (percentBgStr >= 100) {
                    leftPerc = 30;
                } else if (percentBgStr >= 10 && percentBgStr <= 99) {
                    leftPerc = 35;
                } else {
                    leftPerc = 40;
                }

                percentBg = percentBg < 5 ? 0 : percentBg;
                let colBg = $('<div style="height:' + percentBg + '%; background-color:' + getColor(ind) + ';" class="column__bg"></div>');
                if (percentBg === 0 && percentLen !== 0) {
                    colBody.parent().addClass('toRemove');
                } else if (percentLen === 0) {
                    let colNotParticipate = $('<div class="notParticipate">Не участвовали</div>');
                    colNotParticipate.appendTo(colDiv);
                }
                colBg.appendTo(colBody);

                let colPercent = $('<div style="background-color:transparent; position:absolute; bottom: ' + (colBody.height() + 5) + 'px; left:' + leftPerc + '%; font-size:14px; font-weight:600;color:#11325b">' + percentBgStr + '%</div>');
                colPercent.appendTo(colBody);

                let priceSecond = $('<div class="column__price-second">' +
                    new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.openCash)) + '</div>');
                priceSecond.appendTo(colDiv);
                let colTitle = $('<div class="column__title">' + el.facility + '</div>');
                colTitle.appendTo(colDiv);
            }
        }
    });

    $('.toRemove').remove();

    let facilities = investedMoneyDb.facilitiesList;
    let sums = investedMoneyDb.sums;

    let configDoughnutInvested = {
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

    let oldChart = window.myDoughnutInvested;
    let ctxInvested = document.getElementById("doughnut-chart-invested").getContext("2d");
    if (typeof oldChart === 'undefined') {
        window.myDoughnutInvested = new Chart(ctxInvested, configDoughnutInvested);
    } else {
        window.myDoughnutInvested.config = configDoughnutInvested;
        window.myDoughnutInvested.update();
    }

    $(function () {
        $('select#in_out').selectric({
            disableOnMobile: false,
            nativeOnMobile: false,
            onChange: function () {
                let in_out = $('#investments').find('.popup__inner').find('.popup__body').find('.head').find('.head__bottom')
                    .find('.box__select')
                    .find('.selectric').find('span.label').text();

                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");

                switch (in_out) {
                    case 'Вложено':

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
                                createIncomeTable(data.investorsCashList, '');
                            })
                            .fail(function (e) {
                                console.log(e);
                            })
                            .always(function () {
                                console.log('Деньги инвесторов загружены!');
                            });

                        break;
                    case 'Выведено':
                        blurElement($('#investments').find('.popup__inner'), 4);

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
                                createCashingTable(data.investorsCashList, investor);
                            })
                            .fail(function (e) {
                                console.log(e);
                            })
                            .always(function () {
                                console.log('Деньги инвесторов загружены!');
                            });

                        break;
                }
            }
        });
    });

    createInvestedAllTable(investorsCashes, investedMoneyDb.invested);
    createInvestedTable(investorsCashes, investedMoneyDb.invested);
}

function getToFacility(investor, facility, summ, invCash, dateGivedCash) {
    let investorsCash = $.grep(invCash, function (el) {
        return el.investor.login === investor &&
            el.sourceFacility !== null &&
            el.sourceFacility.facility === facility &&
            el.dateGivedCash !== null && el.dateGivedCash === dateGivedCash &&
            Math.round(el.givedCash) === Math.round(summ);
    });
    if (investorsCash.length > 0) {
        return investorsCash[0].facility.facility;
    } else {
        return '';
    }
}

function createCashingTable(invCash, investor) {
    let cashing = getMyCash(invCash, investor);
    let investments = $('#investments').find('.investments');
    let investmentsHead = investments.find('.investments__head.flex');
    investmentsHead.empty();
    let sourceFacilityCol = $('<div class="col">Объект (откуда пришли)</div>');
    sourceFacilityCol.appendTo(investmentsHead);
    let sourceUnderFacilityCol = $('<div class="col">Подобъект (откуда пришли)</div>');
    sourceUnderFacilityCol.appendTo(investmentsHead);

    let invColDate = $('<div class="col">Дата вывода</div>');
    invColDate.appendTo(investmentsHead);
    let invColCashing = $('<div class="col">Сумма вывода</div>');
    invColCashing.appendTo(investmentsHead);
    let invColCommission = $('<div class="col">Сумма комиссии</div>');
    invColCommission.appendTo(investmentsHead);
    let invColTypeCashing = $('<div class="col">Вид вывода</div>');
    invColTypeCashing.appendTo(investmentsHead);
    let invColFacility = $('<div class="col" hidden>Объект</div>');
    invColFacility.appendTo(investmentsHead);
    let colToFacility = $('<div class="col">Объект (куда реинвестировали)</div>');
    colToFacility.appendTo(investmentsHead);

    let investedDetailsTableContent = $('#investedDetailsTable').find('.simplebar-scroll-content').find('.simplebar-content');
    let facility = investedDetailsTableContent.find('.row.flex-vcenter:visible:first').find('.col:last').text();
    if (facility === '') {
        let tmp = $('#investmentsDetailsTitle').text().split(' ');
        $.each(tmp, function (ind, el) {
            if (ind > 2) {
                facility += el;
            }
        })
    }

    investedDetailsTableContent.empty();
    let totalSum = 0;

    $.each(cashing, function (ind, el) {
        if (el.facility === facility) {

            let toFacility = getToFacility(investor, facility, el.cashing, invCash, el.dateClosingInvest);

            let row = $('<div class="row flex-vcenter"></div>');

            let rowSourceFacility = $('<div class="col" style="margin-left: 15px" data-source-facility="' + el.sourceFacility + '">' +
                el.sourceFacility +
                '</div>');
            rowSourceFacility.appendTo(row);

            let rowSourceUnderFacility = $('<div class="col" data-source-under-facility="' + el.sourceUnderFacility + '">' +
                el.sourceUnderFacility +
                '</div>');
            rowSourceUnderFacility.appendTo(row);

            let dateClose = new Intl.DateTimeFormat('ru-RU').format(el.dateClosingInvest);
            let rowDate = $('<div class="col" data-date="' + dateClose + '">' +
                dateClose +
                '</div>');
            rowDate.appendTo(row);

            let rowCashing = $('<div class="col" data-cash="' + el.cashing + '">' +
                new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.cashing)) + ' руб.' + '</div>');
            rowCashing.appendTo(row);
            let rowCommission;
            rowCommission = $('<div class="col" data-cash="' + el.commission + '">' +
                new Intl.NumberFormat('ru-RU', {
                    maximumFractionDigits: 2,
                    minimumFractionDigits: 2
                }).format(el.commission) + ' руб.' + '</div>');
            rowCommission.appendTo(row);

            let rowTypeCashing = $('<div class="col" data-typecashing="' + el.typeClosingInvest + '">' + el.typeClosingInvest + '</div>');
            rowTypeCashing.appendTo(row);

            let rowFacility = $('<div class="col" data-facility="' + el.facility + '" hidden>' + el.facility + '</div>');
            rowFacility.appendTo(row);

            let rowToFacility = $('<div class="col" data-toFacility="' + toFacility + '">' + toFacility + '</div>');
            rowToFacility.appendTo(row);

            row.appendTo(investedDetailsTableContent);

            totalSum += el.cashing + el.commission;
        }

    });

    $('#totalSumFooter').find('span').empty().html('Итого за выбранный период: ' +
        new Intl.NumberFormat('ru-RU', {
            minimumFractionDigits: 2
        }).format(totalSum) + ' руб.');

    blurElement($('#investments').find('.popup__inner'), 0);
}

function createIncomeTable(investorsCash, gettingFacility) {

    let invCash;
    let investor = $('#profile').find($('b')).text();

    let investmentsHead = $('#investments').find('.investments__head.flex');
    investmentsHead.empty();
    let invColDate = $('<div class="col">Дата</div>');
    invColDate.appendTo(investmentsHead);
    let invColCashing = $('<div class="col">Сумма</div>');
    invColCashing.appendTo(investmentsHead);
    let invColCommission = $('<div class="col">Источник</div>');
    invColCommission.appendTo(investmentsHead);
    let invColFacility = $('<div class="col" hidden>Объект</div>');
    invColFacility.appendTo(investmentsHead);
    let curFacility = '';
    let investedDetailsTableContent = $('#investedDetailsTable').find('.simplebar-scroll-content').find('.simplebar-content');
    if (gettingFacility === '') {
        curFacility = investedDetailsTableContent.find('.row.flex-vcenter:visible:first').find('.col:last').text();
        if (curFacility === '') {
            let tmp = $('#investmentsDetailsTitle').text().split(' ');
            $.each(tmp, function (ind, el) {
                if (ind > 2) {
                    curFacility += el;
                }
            })
        }
    } else {
        curFacility = gettingFacility;
    }

    investedDetailsTableContent.empty();

    invCash = $.grep(investorsCash, function (el) {
        return el.givedCash > 0 && el.facility.facility === curFacility &&
            el.investor != null && el.investor.login === investor
    });

    invCash.sort(function (a, b) {
        return new Date(a.dateGivedCash) - new Date(b.dateGivedCash);
    });

    $.each(invCash, function (ind, el) {
        let row = $('<div class="row flex-vcenter"></div>');
        row.appendTo(investedDetailsTableContent);
        let dateGiv = new Intl.DateTimeFormat('ru-RU').format(el.dateGivedCash);
        let colDate = $('<div class="col" style="margin-left: 15px" data-date="' + dateGiv + '">' + dateGiv + '</div>');
        colDate.appendTo(row);

        let colSum = $('<div class="col" data-cash="' + el.givedCash + '">' +
            new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(el.givedCash) +
            ' руб.' + '</div>');
        colSum.appendTo(row);
        let cashSource = el.cashSource === null ? '' : el.cashSource.cashSource;
        let cashImg = '';
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
        let colImg = $('<div class="col"><img src="/resources/core/img/' + cashImg + '" title="' + cashSource + '"></div>');
        colImg.appendTo(row);

        let colFacility = $('<div class="col" hidden data-facility="' + el.facility.facility + '">' + el.facility.facility + '</div>');
        colFacility.appendTo(row);
    });
    let investments = $('#investments');
    let invested = [];
    invCash.reduce(function (res, value) {
        let facility = value.facility.facility;
        if (!res[facility]) {
            res[facility] = {
                givedCash: 0,
                facility: facility,
                myCash: 0
            };
            invested.push(res[facility])
        }
        if (res[facility].facility === facility) {
            res[facility].givedCash += value.givedCash;
            if (value.investor.login === investor) {
                res[facility].myCash += value.givedCash;
            }
        } else {
            res[facility] = {
                givedCash: 0,
                facility: facility,
                myCash: 0
            };
            invested.push(res[facility])
        }
        return res;
    }, {});

    $.each(invested, function (ind, el) {
        if (el.facility === curFacility) {
            $('#totalSumFooter').find('span').empty().html('Итого за выбранный период: ' +
                new Intl.NumberFormat('ru-RU', {
                    minimumFractionDigits: 2
                }).format(Math.round(el.givedCash)) + ' руб.');
        }
    });
    blurElement($('#investments').find('.popup__inner'), 0);

}

function createInvestedAllTable(investorsCash, investedDb) {

    let investedAllDetailsTable = $('#investedAllDetailsTable').find('.simplebar-content');
    investedAllDetailsTable.empty();

    $.each(investedDb, function (ind, el) {
        if (el.myCash > 0) {
            let row = $('<div class="row flex-vcenter"></div>');
            row.appendTo(investedAllDetailsTable);

            let colFacility = $('<div class="col" style="margin-left: 15px">' + el.facility + '</div>');
            colFacility.appendTo(row);

            let colIncomeTotal = $('<div class="col" data-income="' + el.incomeCash + '">' +
                new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.incomeCash)) + ' руб.' + '</div>');
            colIncomeTotal.appendTo(row);

            let colCashingTotal = $('<div class="col" data-cashing="' + el.cashing + '">' +
                new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.cashing)) + ' руб.' + '</div>');
            colCashingTotal.appendTo(row);

            let colSum = $('<div class="col" data-cash="' + el.myCash + '">' +
                new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(Math.round(el.incomeCash - el.cashing)) + ' руб.' + '</div>');
            colSum.appendTo(row);
            let colDetails = $('<div class="col"><button type="button" class="btn btn-primary" id="showInvestsDetails">Подробности</button></div>');
            colDetails.appendTo(row);
        }
    });

}

function createInvestedTable(investorsCash, investedDb) {
    let invCash;
    let investor = $('#profile').find($('b')).text();

    if (investorsCash !== null) {
        invCash = $.grep(investorsCash, function (el) {
            return el.givedCash > 0 && el.investor != null;
        });

        invCash.sort(function (a, b) {
            return new Date(a.dateGivedCash) - new Date(b.dateGivedCash);
        });
    }

    let investedDetailsTable = $('#investedDetailsTable').find('.simplebar-content');
    investedDetailsTable.empty();

    let investmentsHead = $('#investments').find('.investments__head.flex');
    investmentsHead.empty();
    let invColDate = $('<div class="col">Дата</div>');
    invColDate.appendTo(investmentsHead);
    let invColCashing = $('<div class="col">Сумма</div>');
    invColCashing.appendTo(investmentsHead);
    let invColCommission = $('<div class="col">Источник</div>');
    invColCommission.appendTo(investmentsHead);
    let invColFacility = $('<div class="col" hidden>Объект</div>');
    invColFacility.appendTo(investmentsHead);

    $.each(invCash, function (ind, el) {
        if (el.investor.login === investor) {
            let row = $('<div class="row flex-vcenter"></div>');
            row.appendTo(investedDetailsTable);
            let dateGiv = new Intl.DateTimeFormat('ru-RU').format(el.dateGivedCash);
            let colDate = $('<div class="col" style="margin-left: 15px" data-date="' + dateGiv + '">' + dateGiv + '</div>');
            colDate.appendTo(row);

            let colSum = $('<div class="col" data-cash="' + el.givedCash + '">' +
                new Intl.NumberFormat('ru-RU', {maximumFractionDigits: 2}).format(el.givedCash) +
                ' руб.' + '</div>');
            colSum.appendTo(row);
            let cashSource = el.cashSource === null ? '' : el.cashSource.cashSource;
            let cashImg = '';
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
            let colImg = $('<div class="col"><img src="/resources/core/img/' + cashImg + '" title="' + cashSource + '"></div>');
            colImg.appendTo(row);

            let colFacility = $('<div class="col" hidden data-facility="' + el.facility.facility + '">' + el.facility.facility + '</div>');
            colFacility.appendTo(row);
        }
    });
    let investments = $('#investments');

    let facility = investments
        .find('.popup__inner')
        .find('.popup__body')
        .find('.head')
        .find('.head__bottom')
        .find('.select')
        .find('option:first').text();

    $.each(investedDb, function (ind, el) {
        if (el.facility === facility) {
            let totalSum = $('#totalSumFooter');
            totalSum.find('strong').empty();
            $('<span><strong>' + Intl.NumberFormat('ru-RU', {maximumFractionDigits: 0}).format(Math.round(el.givenCash)) + ' руб.' + '</strong></span>')
                .appendTo(totalSum);
        }
    });
}

function getMyCash(investorsCash, investor) {

    let invCash = $.grep(investorsCash, function (el) {
        return el.investor.login === investor && el.typeClosingInvest !== null &&
            (el.typeClosingInvest.typeClosingInvest === 'Вывод' || el.typeClosingInvest.typeClosingInvest === 'Вывод_комиссия'
                || el.typeClosingInvest.typeClosingInvest === 'Реинвестирование') && el.givedCash > 0;
    });

    let groupedCashes = [];

    let result = invCash.reduce(function (res, value) {
        if (res.typeClosingInvest !== null) {

            let key = value.facility.facility + '' + value.dateClosingInvest;

            if (!res[key]) {
                res[key] = {
                    cashing: 0,
                    facility: value.facility.facility,
                    sourceFacility: value.sourceFacility === null ? '' : value.sourceFacility.facility,
                    sourceUnderFacility: value.sourceUnderFacility === null ? '' : value.sourceUnderFacility.underFacility,
                    dateClosingInvest: value.dateClosingInvest,
                    typeClosingInvest: value.typeClosingInvest.typeClosingInvest,
                    commission: 0
                };
                groupedCashes.push(res[key])
            }
            if (res[key].facility === value.facility.facility &&
                res[key].dateClosingInvest === value.dateClosingInvest) {

                if (value.typeClosingInvest.typeClosingInvest !== 'Вывод_комиссия') {
                    res[key].cashing += value.givedCash;
                } else {
                    res[key].commission += value.givedCash;
                }

            } else {
                res[key] = {
                    cashing: value.givedCash,
                    facility: value.facility.facility,
                    dateClosingInvest: value.dateClosingInvest,
                    typeClosingInvest: value.typeClosingInvest.typeClosingInvest
                };
                groupedCashes.push(res[key])
            }
            return res;
        }

    }, {});
    // console.log(result);
    return result;
}

function populateInvestedStorage() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

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
            // fillInvestedStorage(data);
            prepareInvestedMoney(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Локальное хранилище обновлено!');
        });
}

function fillInvestedStorage(investedMoneyDb) {
    localStorage.setItem('investedMoneyDb', JSON.stringify(investedMoneyDb));
}

function getInvestorsCashFromLocalStorage() {
    let investedMoneyDb;

    if (checkLocalStorage() && localStorage.getItem('investedMoneyDb')) {
        investedMoneyDb = JSON.parse(localStorage.getItem('investedMoneyDb'));
        prepareInvestedMoney(investedMoneyDb);
        populateInvestedStorage();
    } else {
        getInvestorsCash();
    }

}

function onStartUp() {
    getInvestorsCashFromLocalStorage();
    // getInvestorsCash();
    getInvestorsFlowsList('max', '');
    getMainFlowsList('Все объекты');
    getAnnexes();
}

function getInvestorsFlowsList(iMonth, fullDate) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let invId = $('#invId').val();
    let invLogin = $('#invLogin').val();

    let search = ({
        investor: invId,
        login: invLogin
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getInvestorsFlows",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            let invFlows = data.investorsFlowsList;
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
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let invId = $('#invId').val();
    let invLogin = $('#invLogin').val();

    let search = ({
        investor: invId,
        login: invLogin
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getMainFlows",
        data: JSON.stringify(search),
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
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let invId = $('#invId').val();
    let invLogin = $('#invLogin').val();

    let search = ({
        investor: invId,
        login: invLogin
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getInvestorsCashList",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareInvestedMoney(data);
            // fillInvestedStorage(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Деньги инвесторов загружены!');
        });

}

function getAnnexes() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let invId = $('#invId').val();
    let invLogin = $('#invLogin').val();

    let search = ({
        investor: invId,
        login: invLogin
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getAnnexesList",
        data: JSON.stringify(search),
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

function appendMonths(months) {
    let month;
    let monthNumb;
    let year;
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
    for (let i = months.length; i < 6; i++) {
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

    let body = $('table#tblAnnex').find('tbody');
    let tr, td, a, label, input, strDate;
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

    let unread = checkUnreadAnnex();
    if (unread > 0) {
        blurElement($('.out'), 4);
        $('#readAnnex').css('z-index', '1000001');
        $('#look').attr('disabled', false);
    } else {
        blurElement($('.out'), 0);
        $('div.out').css('filter', '');
        $('#readAnnex').css('z-index', '-1000001');
    }

}

function setAnnexRead(id) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let usersAnnexToContracts = {
        id: id
    };

    let search = ({
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
    let unchecked = checkUnreadAnnex();
    let read = $('#read');
    if (unchecked === 0) {
        setTimeout(function () {
            read.removeClass('js-active')
        }, 1500);
        $('#readAnnex').css('z-index', '-1000001');
        blurElement($('.out'), 0);
        $('div.out').css('filter', '');
    }

}

function checkUnreadAnnex() {
    return $('#tblAnnex').find('tr').find('input[type="checkbox"]').not(':checked').length;
}

function blurElement(element, size) {
    let filterVal = 'blur(' + size + 'px)';
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

function unique(array) {
    return $.grep(array, function (el, index) {
        return index === $.inArray(el, array);
    });
}
