var monthNames = [
    'Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сент', 'Окт', 'Ноя', 'Дек'
];
var monthLongNames = [
    'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'
];

jQuery(document).ready(function ($) {
    blurElement($('.out'), 4);
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
        var output = day + '.' + month + '.' + year;
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
    var el = $('<button type="button" class="btn btn-primary btn-sm" id="getDate">Фильтр</button>')
    el.appendTo($('#detailsProfit').find('.head__bottom'));

    $(document).on('click', '#getDate', function () {
        var investorsFlowsList;
        if(checkLocalStorage()){
            investorsFlowsList = JSON.parse(localStorage.getItem('investorsFlowsList'));
            prepareDetailsProfit(investorsFlowsList, $('#dateMonth').val());
        }else{
            getInvestorsFlowsList($('#dateMonth').val());
        }
        prepareToggle();
    });

    $(document).on('change', 'input[type="checkbox"]:checked', function () {
        var userId = $(this).data('user-id');
        var annex = {
            id: $(this).data('annex-id'),
            annexName: $(this).data('annex-name')
        };
        //var annexesCnt = $('#annexCnt').text();
        $(this).attr('disabled', 'disabled');
        var d = new Date();
        var month = ('' + d.getMonth() + 1).length < 2 ? '0' + (d.getMonth() + 1) + '' : '' + (d.getMonth() + 1) + '';
        var day = ('' + d.getDate()).length < 2 ? '0' + d.getDate() + '' : '' + d.getDate() + '';
        var year = d.getFullYear();
        var output = day + '.' + month + '.' + year;
        $('#annexDate-' + annex.id).html(output);
        var trId = $(this).closest('tr').attr('id');
        setAnnexRead(trId, userId, annex, 1/*, annexesCnt*/);
    });

    $(document).on('click', '#ext', function () {
        window.location.href = '/logout'
    });

    //$('#incomesAndExpenses').selectric();

    //$(function(){$('select').selectric();$('select#incomesAndExpenses').selectric({onChange: function (element) {var mainFlows;var facility = $('.inner__row.inner__row_costs.flex-vhcenter').find('.selectric').find('span.label').text();if(checkLocalStorage()){mainFlows = JSON.parse(localStorage.getItem('mainFlowsList'));}else{getMainFlowsList(facility);}prepareIncomesAndExpenses(mainFlows, facility)}});})
});

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
    switch (i){
        case 0: return window.chartColors.red;
        case 1: return window.chartColors.sea;
        case 2: return window.chartColors.yellow;
        case 3: return window.chartColors.orange;
        case 4: return window.chartColors.green;
        case 5: return window.chartColors.blue;
        case 6: return window.chartColors.purple;
        case 7: return window.chartColors.grey;
    }
}

function prepareIncomeDiagram(investorsFlowsList) {
    var invFlows = $.grep(investorsFlowsList, function (el) {
        return el.afterCashing >= 0;
    });
    var reportDates = [];
    var tmpDate = [];

    var cash;
    var cashes =[];
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

    $.each(reportDates, function (ind, el) {
        tmpDate.push(monthNames[el.getMonth()] + ' ' + el.getFullYear())
    });

    reportDates = $.unique(tmpDate);

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
        if(res[value.facility].facility === value.facility && res[value.facility].reportDate === value.reportDate){
            res[value.facility].sum += value.sum;
        }else{
            res[value.facility] = {
                sum: value.sum,
                facility: value.facility,
                reportDate: value.reportDate
            };
            groupedCashes.push(res[value.facility])
        }
        return res;
    }, {});

    //console.log("До reduce");
    //console.log(groupedCashes);
    var reducedCash = JSON.parse(JSON.stringify(groupedCashes));

    $.each(reducedCash, function (ind, el) {
        el.sum = Math.round(el.sum*100)/100;
    });

    var facilities = [];
    $.each(reducedCash, function (i, el) {
        facilities.push(el.facility);
    });
    facilities = $.unique(facilities);

    var incomesAndExpenses = $('#incomesAndExpenses');

    var oldOptions = document.getElementById('incomesAndExpenses').options;
    console.log(oldOptions);

    incomesAndExpenses
        .find('option:not(:first)')
        .remove()
        .end();

    $.each(facilities, function (key, value) {
        incomesAndExpenses
            .append($("<option></option>")
                .attr("value", key)
                .text(value))
        ;
    });

    $(function(){
        //$('select').selectric();
        $('select#incomesAndExpenses').selectric({
            onChange: function (element) {
                var mainFlows;
                var facility = $('.inner__row.inner__row_costs.flex-vhcenter').find('.selectric:last').find('span.label').text();
                if(checkLocalStorage()){
                    mainFlows = JSON.parse(localStorage.getItem('mainFlowsList'));
                }else{
                    getMainFlowsList(facility);
                }
                prepareIncomesAndExpenses(mainFlows, facility)
            }
        });});

    var totals = [];
    var total;
    var sums = [];
    var totalSums = [];
    var i = -1;
    var facilitiesDates =[];
    var facilityCnt = facilities.length;
    var usedFacilities = [];

    var res = groupedCashes.reduce(function (prev, cur, i) {
        var cash = prev[cur.reportDate];
        if(cash){
            cash.sum +=cur.sum;
        }else {
            prev[cur.reportDate] = cur;
            //delete cur.reportDate;
        }
        return prev;

    }, []);

    var eachObject = function(obj){
        for (let key in obj) {
            totalSums.push(obj[key].sum);
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
                    if(!facilitiesDates[i]){
                        facilitiesDates[i] = elGr.reportDate;
                    }
                    if(facilitiesDates[i].length === 0){
                        sums.splice(i, 0, 0);
                    }else{
                        sums.splice(i, 0, elGr.sum);
                    }
                }
                /*
                if (elGr.facility !== el && elGr.reportDate === elDates) {
                    if(!totalSums[i]){
                        totalSums[i] = elGr.sum;
                    }else{
                        totalSums[i] += elGr.sum;

                        if(elGr.facility !== el && elGr.reportDate === elDates
                            && $.inArray(elGr.facility, usedFacilities) === -1){
                            totalSums[i] += elGr.sum;
                        }

                    }
                }
                */
            });
        });

        $.each(facilitiesDates, function (ind, el) {
            if(typeof el === 'undefined'){
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

    if(reportDates.length < 6){
        reportDates = appendMonths(reportDates);
    }

    $.each(totals, function (i, el) {
        var oldEl = JSON.parse(JSON.stringify(el));
        if(el.reportDates.length < reportDates.length){
            el.reportDates = reportDates;
        }

        if(el.sums.length < reportDates.length){
            $.each(reportDates, function (ind, elem) {
                if($.inArray(elem, oldEl.reportDates) === -1 && oldEl.reportDates[ind] !== null){
                    el.sums.splice(ind, 0, 0);
                }
            });
        }

    });

    if(totalSums.length < reportDates.length){
        for(var j = totalSums.length; j < reportDates.length; j++){
            totalSums.splice(0, 0, 0);
        }
    }

    console.log(totals);

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

    ds = {
        label: "Все объекты",
        backgroundColor: getColor(7),
        borderColor: getColor(7),
        data: totalSums,
        fill: false
    };

    dSet.push(ds);

    var max = Math.max.apply(Math, totalSums);

    var step = Math.floor(max/parseInt('5' + zeroDigits(max.toString().indexOf('.') - 2)))
                * parseInt('1' + zeroDigits(max.toString().indexOf('.') - 2));
    max = step * 7;

    Chart.plugins.register({
        id: 'scroll-plugin',
        rendered: false,
        afterDraw: function(chart, options) {
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
            $(chartOptions.legendSelector + " > ul > li").on("click",function(e){
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

            ticks.forEach(function(el) {
                $chartList.append('<li>' + el + '</li>')
            })
        }
    });

    var config = {
        type: 'line',
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
                                        min: 0,
                                        max: max,
                                        stepSize: step,
                                        fontSize: 0
                                    }
                            }]
                    }
            }
    };
    /*
    var ctx1 = document.getElementById("chart-finance").getContext("2d");
    window.myDoughnut1 = new Chart(ctx1, config);
    */
    var oldChart = window.myDoughnut1;
    var ctx1 = document.getElementById("chart-finance").getContext("2d");
    if(typeof oldChart === 'undefined'){
        window.myDoughnut1 = new Chart(ctx1, config);
    }else {
        window.myDoughnut1.config = config;
        window.myDoughnut1.update();
    }

    $('#chart-finance-legend').html(myDoughnut1.generateLegend());
    $("#chart-finance-legend > ul > li").on("click", function(e)
    {
        var index = $(this).index(),
            chart = e.view.myDoughnut1,
            dataset = chart.data.datasets[index]._meta[2];
        $(this).toggleClass("hidden");
        dataset.hidden = !dataset.hidden;
        chart.update();
    })

}

function zeroDigits(numb) {
    var str = '';
    for(var i = 0; i < numb; i++){
        str += "0";
    }
    return str;
}

function prepareIncomeCircle(investorsFlowsList) {
    var invFlows = $.grep(investorsFlowsList, function (el) {
        return el.afterCashing > 0;
    });
    var dates = [];
    $.each(invFlows, function (ind, el) {
        dates.push(new Date(el.reportDate));
    });
    var maxDate = new Date(Math.max.apply(null, dates));
    var ttl = 'Ваш доход за ' + monthLongNames[maxDate.getMonth()].toLowerCase() + ' ' + maxDate.getFullYear();

    invFlows = $.grep(invFlows, function (el) {
        var elDate = new Date(el.reportDate);
        var curDate = new Date(maxDate);
        return elDate.getMonth() === curDate.getMonth() && elDate.getFullYear() === curDate.getFullYear();
    });

    $('#incomeTitle').html(ttl);

    var cash;
    var cashes =[];
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
        if(res[value.facility].facility === value.facility){
            res[value.facility].sum += value.sum;
        }else{
            res[value.facility] = {
                sum: value.sum,
                facility: value.facility
            };
            groupedCashes.push(res[value.facility])
        }

        return res;
    }, {});
    var totalIncome;
    if(groupedCashes.length > 1){
        totalIncome = $.map(groupedCashes, function (el) {
            return el.sum
        }).reduce(function (a, b) {
            return a + b;
        });
    }else{
        totalIncome = groupedCashes[0].sum;
    }

    $('#totalIncome').html('<span>' +  new Intl.NumberFormat('ru-RU').format(totalIncome) + '</span>' + 'рублей');
    $('#incomesList').html('');
    $.each(groupedCashes, function (ind, el) {
        $('#incomesList').append('<div class="circle-canvas__item" style="color: ' + getColor(ind) + '"><span>' + new Intl.NumberFormat('ru-RU').format(el.sum) + '</span>' + el.facility + '</div>');
    });

    var facilities = [];
    var sums = [];
    var colors = [];
    $.each(groupedCashes, function (i, el) {
        facilities.push(el.facility);
        sums.push(el.sum);
        colors.push(getColor(i));
    });
    facilities = $.unique(facilities);

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
    if(typeof oldChart === 'undefined'){
        window.myDoughnut2 = new Chart(ctx2, configDoughnut);
    }else {
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
    switch (iMonth){
        case 'max':
            dates = investorsFlowsList.map(function(x) { return new Date(x.reportDate); });
            dateMonth = new Date(Math.max.apply(null, dates));
            break;
        default:
            dateMonth = new Date(getDateFromString(iMonth));
    }

    console.log(investorsFlowsList);

    var invFlows = $.grep(investorsFlowsList, function(el){
        return new Date(el.reportDate).getMonth() === dateMonth.getMonth() && new Date(el.reportDate).getFullYear() === dateMonth.getFullYear();
    });

    var facilitiesFlows = [];
    var facilityFlows;
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
        if(res[value.facility].facility === value.facility){
            res[value.facility].sum += value.sum;
            res[value.facility].afterCosts += value.afterCosts;
            res[value.facility].afterTax += value.afterTax;
            res[value.facility].afterComission += value.afterComission;
            res[value.facility].shareKind = value.shareKind;
            res[value.facility].share += value.share;
            res[value.facility].tax += value.tax;
            res[value.facility].comission += value.comission;
            res[value.facility].cnt += 1;
            if(res[value.facility].shareKind !== 'Сверхдоля'){
                res[value.facility].cntKind += 1;
            }
        }else{
            res[value.facility] = {
                facility: value.facility,
                sum: value.sum,
                share: value.share,
                afterCosts: value.afterCosts,
                tax: value.tax,
                afterTax: value.afterTax,
                comission: value.comission,
                afterComission: value.afterComission,

            };
            gFacilitiesFlows.push(res[value.facility])
        }

        return res;
    }, {});
    $.each(gFacilitiesFlows, function (ind, el) {
        if(el.cnt !== el.cntKind){
            el.share = el.share / el.cntKind;
        }else {
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
            if(el.facility === elSec.facility.facility){
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
    //prepareToggle();
}

function prepareIncomesAndExpenses(mainFlowsList, facility) {

    var settlementDates = [];
    var tmpDate = [];
    var mainFlows;
    switch (facility){
        case 'Все объекты':
            mainFlows = mainFlowsList;
            break;
        default:
            mainFlows = $.grep(mainFlowsList, function(el){
                return el.underFacilities.facility.facility === facility;
            });
    }

    var flow;
    var flows =[];
    $.each(mainFlows, function (ind, el) {
        settlementDates.push(new Date(el.settlementDate));

        if(el.summa > 0){
            flow = {
                settlementDate: new Date(el.settlementDate),
                incomeOrExpense: 'Доходы',
                sum: el.summa
            };
        }else {
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

    settlementDates = $.unique(tmpDate);

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
        if(res[value.incomeOrExpense].incomeOrExpense === value.incomeOrExpense &&
            res[value.incomeOrExpense].settlementDate === value.settlementDate){
            res[value.incomeOrExpense].sum += value.sum;
        }else{
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
        if(el.incomeOrExpense === 'Доходы'){
            incomes.push(el.sum);
            sums.push(el.sum);
        }else{
            expenses.push(el.sum * -1);
            sums.push(el.sum * -1);
        }
    });

    var max = Math.max.apply(Math, sums);

    var step = Math.floor(max/parseInt('5' + zeroDigits(max.toString().indexOf('.') - 2)))
        * parseInt('1' + zeroDigits(max.toString().indexOf('.') - 2));
    max = step * 7;

    if(settlementDates.length < 6){
        settlementDates = appendMonths(settlementDates);
    }

    if(incomes.length < settlementDates.length){
        for(var j = incomes.length; j < settlementDates.length; j++){
            incomes.splice(0, 0, 0);
        }
    }
    if(expenses.length < settlementDates.length){
        for(var j = expenses.length; j < settlementDates.length; j++){
            expenses.splice(0, 0, 0);
        }
    }
    //console.log(incomes);
    //console.log(expenses);
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
                                        min: 0,
                                        max: max,
                                        stepSize: step,
                                        fontSize: 0
                                    }
                            }]
                    }
            }
    };

    var oldChart = window.myDoughnut;
    var ctxCosts = document.getElementById("chart-costs").getContext("2d");
    if(typeof oldChart === 'undefined'){
        window.myDoughnut = new Chart(ctxCosts, configCosts);
    }else {
        window.myDoughnut.config = configCosts;
        window.myDoughnut.update();
    }
    var chartLegend = $('#chart-costs-legend');
    chartLegend.html(myDoughnut.generateLegend());
    chartLegend.find("> ul > li").on("click", function(e)
    {
        var index = $(this).index(),
            chart = e.view.myDoughnut,
            dataset = chart.data.datasets[index]._meta[4];
        $(this).toggleClass("hidden");
        dataset.hidden = !dataset.hidden;
        chart.update();
    });

}

function prepareInvestedMoney(investorsCashes) {
    //console.log(investorsCashes);

}

function prepareIncomeChart(mainFlowsList) {

    var invFlows = $.grep(mainFlowsList, function(el){
        return el.summa > 0
    });
    var settlementDates = [];
    var tmpDate = [];

    var cash;
    var cashes =[];
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

    settlementDates = $.unique(tmpDate);
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
        if(res[value.facility].facility === value.facility && res[value.facility].settlementDate === value.settlementDate){
            res[value.facility].sum += value.sum;
        }else{
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
    facilities = $.unique(facilities);

    var totals = [];
    var total;
    var sums = [];
    var totalSums = [];
    var i = -1;
    var facilitiesDates =[];
    $.each(facilities, function (ind, el) {
        sums = [];
        facilitiesDates = [];
        i = -1;
        $.each(settlementDates, function (indDates, elDates) {
            i++;

            $.each(groupedCashes, function (indGr, elGr) {
                if (elGr.facility === el && elGr.settlementDate === elDates) {

                    if(!totalSums[i]){
                        totalSums[i] = 0;
                    }
                    if(!facilitiesDates[i]){
                        facilitiesDates[i] = elGr.settlementDate;
                    }
                    if(facilitiesDates[i].length === 0){
                        sums.push(0);
                    }else{
                        sums.push(elGr.sum);
                    }
                }
                if (elGr.facility !== el && elGr.settlementDate === elDates) {
                    if(!totalSums[i]){
                        totalSums[i] = elGr.sum;
                    }else{
                        if(elGr.facility !== el && elGr.settlementDate === elDates){
                            totalSums[i] =  totalSums[i] + elGr.sum;
                        }
                    }
                }
            });
        });

        $.each(facilitiesDates, function (ind, el) {
            if(typeof el === 'undefined'){
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
    var step = Math.floor(max/60000)*10000;
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

    if(checkLocalStorage()){
        if(localStorage.getItem('investorsFlowsList') && localStorage.getItem('mainFlowsList') &&
            localStorage.getItem('investorsCashList')){
            var tmp = JSON.parse(localStorage.getItem('investorsFlowsList'));
            var mainFlows = JSON.parse(localStorage.getItem('mainFlowsList'));
            var investorCashes = JSON.parse(localStorage.getItem('investorsCashList'));
            if(tmp.length > 0 && mainFlows.length > 0 && investorCashes.length > 0){
                prepareDetailsProfit(tmp, 'max');
                prepareIncomeDiagram(tmp);
                prepareIncomeCircle(tmp);
                prepareIncomesAndExpenses(mainFlows, 'Все объекты');
                prepareInvestedMoney(investorCashes);
            }else {
                getIncomeData();
            }

        }else{
            getIncomeData();
        }
        populateStorage();
    }else {
        getIncomeData();
    }

}

function populateStorage() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getIncomes",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            var tmp;
            var mainFlows;
            var investorCashes;
            if(checkLocalStorage()){
                localStorage.setItem('investorsFlowsList', JSON.stringify(data.investorsFlowsList));
                localStorage.setItem('mainFlowsList', JSON.stringify(data.mainFlowsList));
                localStorage.setItem('investorCashList', JSON.stringify(data.investorsCashList));
                tmp = JSON.parse(localStorage.getItem('investorsFlowsList'));
                mainFlows = JSON.parse(localStorage.getItem('mainFlowsList'));
                investorCashes = JSON.parse(localStorage.getItem('investorCashList'));
            }
            if(typeof tmp === 'undefined'){
                tmp = data.investorsFlowsList;
            }
            if(typeof mainFlows === 'undefined'){
                mainFlows = data.mainFlowsList;
            }
            if(typeof investorCashes === 'undefined'){
                investorCashes = data.investorCashList;
            }
            createAnnexesTbl(data.annexes);
            prepareIncomeDiagram(tmp);
            prepareIncomeCircle(tmp);
            prepareDetailsProfit(tmp, 'max');
            prepareIncomesAndExpenses(mainFlows, 'Все объекты');
            prepareInvestedMoney(investorCashes);
            prepareToggle();
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Закончили!');
        });
}

function getIncomeData() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/getIncomes",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            var tmp;
            var mainFlows;
            var investorCashes;
            if(checkLocalStorage()){
                localStorage.setItem('investorsFlowsList', JSON.stringify(data.investorsFlowsList));
                localStorage.setItem('mainFlowsList', JSON.stringify(data.mainFlowsList));
                localStorage.setItem('investorCashList', JSON.stringify(data.investorsCashList));
                tmp = JSON.parse(localStorage.getItem('investorsFlowsList'));
                mainFlows = JSON.parse(localStorage.getItem('mainFlowsList'));
                investorCashes = JSON.parse(localStorage.getItem('investorCashList'));
            }
            if(typeof tmp === 'undefined'){
                tmp = data.investorsFlowsList;
            }
            if(typeof mainFlows === 'undefined'){
                mainFlows = data.mainFlowsList;
            }
            if(typeof investorCashes === 'undefined'){
                investorCashes = data.investorCashList;
            }
            prepareIncomeDiagram(tmp);
            prepareIncomeCircle(tmp);
            prepareDetailsProfit(tmp, 'max');
            prepareIncomesAndExpenses(mainFlows, 'Все объекты');
            prepareInvestedMoney(investorCashes);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Закончили!');
        });

}

function getInvestorsFlowsList(iMonth) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "getInvestorsFlowsList",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareDetailsProfit(data.investorsFlowsList, iMonth);
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
        url: "getInvestorsFlowsList",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            prepareIncomesAndExpenses(data.mainFlowsList, facility);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Потоки инвесторов загружены!');
        });

}

function checkLocalStorage() {
    return window.localStorage;
}

function getDateFromString(strDate) {
    var parts = strDate.split(' ');
    var iMonth = 0;
    var reportDate;
    switch (parts[0]){
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
    switch (month){
        case 'Янв': monthNumb = 0;
            break;
        case 'Фев': monthNumb = 1;
            break;
        case 'Мар': monthNumb = 2;
            break;
        case 'Апр': monthNumb = 3;
            break;
        case 'Май': monthNumb = 4;
            break;
        case 'Июн': monthNumb = 5;
            break;
        case 'Июл': monthNumb = 6;
            break;
        case 'Авг': monthNumb = 7;
            break;
        case 'Сент': monthNumb = 8;
            break;
        case 'Окт': monthNumb = 9;
            break;
        case 'Ноя': monthNumb = 10;
            break;
        case 'Дек': monthNumb = 11;
            break;
    }
    for(var i = months.length; i < 6; i++){
        monthNumb--;
        if(monthNumb < 0){
            monthNumb = 11;
            year = parseInt(year) - 1;
        }
        months.splice(0, 0, monthNames[monthNumb] + ' ' + year);

    }

    return months;
}

function createAnnexesTbl(annexes) {
    var body = $('table#tblAnnex').find('tbody');
    var tr, td, a, label, input, strDate;
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
        td.attr('id', 'annexDate-' + el.annex.id);
        td.appendTo(tr);

    });

    var unread = checkUnreadAnnex();
    if(unread > 0){
        blurElement($('.out'), 4);
        //$('.out').addClass('blur-bg');
        $('#readAnnex').css('z-index', '1000001');
        $('#look').attr('disabled', false);
    }else{
        blurElement($('.out'), 0);
        //$('.out').removeClass('blur-bg');
        $('#readAnnex').css('z-index', '-1000001');
    }

}

function setAnnexRead(id, userId, annex, read) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var usersAnnexToContracts = {
        id : id,
        userId: userId,
        annex: annex,
        annexRead: read,
        dateRead: new Date().getTime()
    };

    var search = ({
        "usersAnnexToContracts" : usersAnnexToContracts
    });
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "setReadToAnnex",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            updateUnreadAnnexes();
            console.log(data.message);
        },
        error : function(e) {
            console.log('Произошла ошибка - ' + e.toString());
        }
    });
}

function updateUnreadAnnexes() {
    var unchecked = checkUnreadAnnex();
    var read = $('#read');
    if(unchecked === 0){
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
        'filter':filterVal,
        'webkitFilter':filterVal,
        'mozFilter':filterVal,
        'oFilter':filterVal,
        'msFilter':filterVal,
        'transition':'all 0.5s ease-out',
        '-webkit-transition':'all 0.5s ease-out',
        '-moz-transition':'all 0.5s ease-out',
        '-o-transition':'all 0.5s ease-out'
    });

}

var localStorageSpace = function(){
    var allStrings = '';
    for(var key in window.localStorage){
        if(window.localStorage.hasOwnProperty(key)){
            allStrings += window.localStorage[key];
        }
    }
    return allStrings ? 3 + ((allStrings.length*16)/(8*1024)) + ' KB' : 'Empty (0 KB)';
};

