<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Доходы инвесторов</title>
    <sec:csrfMetaTags />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta http-equiv='cache-control' content='no-cache'>
    <meta http-equiv='expires' content='0'>
    <meta http-equiv='pragma' content='no-cache'>
    <meta name="viewport" content="width=1370">
    <meta name="theme-color" content="#fff">
    <meta name="format-detection" content="telephone=no">
    <meta name="robots" content="noindex, nofollow">
    <script type="text/javascript" src="<c:url value='/resources/core/js/app.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/Chart.js' />" ></script>

    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/app.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery.selectric.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-ui.min.js' />" ></script>

    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>

    <script type="text/javascript" src="<c:url value='/resources/core/js/flowsVisualization.js' />" ></script>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <style type="text/css">
        /* Chart.js */
        @-webkit-keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}@keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}.chartjs-render-monitor{-webkit-animation:chartjs-render-animation 0.001s;animation:chartjs-render-animation 0.001s;}</style>
</head>

<body>

<div id="readAnnex" class="bg">
    Ознакомьтесь с новыми приложениями, после этого панель отчётов инвестора станет доступна.
    <br>
    <br>
    <button id="look" type="button" data-target="#read" class="btn btn-primary">Посмотреть</button>
    <button id="ext" type="button" class="btn btn-suc">Выход</button>
</div>

<div id="read" class="popup" style="z-index: 1000002">
    <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
        <div class="popup__body">
            <div class="head">
                <div class="title">Приложения для ознакомления</div>
            </div>
            <div class="table">
                <table id="tblAnnex">
                    <thead>
                    <tr>
                        <th>Приложение</th>
                        <th>Ссылка</th>
                        <th>Ознакомление</th>
                        <th>Дата ознакомления</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!--BEGIN out-->
<div class="out">
    <!--BEGIN header-->
    <%@include file="authheader.jsp" %>
    <!--END header-->
    <div class="inner">
        <div class="inner__row inner__row_finance flex-vhcenter">
            <div class="box">
                <div class="box__header">
                    <div class="title-sm">Ваши доходы</div>
                </div>
                <div id="chart-finance-legend" class="chart-legend"></div>
                <div class="wrapper">
                    <canvas id="chart-finance"></canvas>
                    <div id="chart-axis">
                        <ul></ul>
                    </div>
                </div>
                    <script type="text/javascript">
                        window.chartColors = {
                            red: 'rgb(255, 99, 132)',
                            orange: '#f86b4f',
                            sea: '#8b94ea',
                            yellow: 'rgb(255, 205, 86)',
                            green: '#80c966',
                            blue: '#8bc4ea',
                            purple: '#d520b5',
                            grey: '#cacaca'
                        };
                    </script>
                </div>


            <div class="circle">
                <div class="title-sm" id="incomeTitle"></div>
                <div class="wrapper flex-vcenter">
                    <div class="circle-canvas"><canvas id="doughnut-chart"></canvas>
                        <div class="circle-canvas__text" id="totalIncome"></div>
                    </div>
                    <div class="circle-canvas__list" id="incomesList">

                    </div>
                </div><button type="button" data-target="#detailsProfit" class="btn btn-primary">Подробности дохода</button>

            </div>
        </div>

        <div class="inner__row inner__row_costs flex-vhcenter">
            <div class="box">
                <div class="box__header">
                    <div class="title-sm">Общие доходы и расходы объекта</div>
                    <div class="box__select">
                        <select title="Объекты" id="incomesAndExpenses">
                            <option value="-1">Все объекты</option>
                        </select>
                    </div>
                </div>
                <div id="chart-costs-legend" class="chart-legend"></div>
                <div class="wrapper">
                    <canvas id="chart-costs"></canvas>
                    <div id="chart-axis2">
                        <ul></ul>
                    </div>
                </div>
                <!--
                <div class="box__footer flex-vhcenter"><button data-target="#allincomeProperty" type="button" class="btn btn-primary">По подобъектам</button></div>
                -->
            </div>
        </div>

        <!--

        <div class="inner__row inner__row_load flex-vhcenter">
            <div class="box">
                <div class="box__header">
                    <div class="title-sm">Текущая загрузка объекта</div>
                    <div class="box__select"><select><option>Электриков</option><option>Энергетиков</option></select>
                        <div class="sort"><button type="button" class="sort__button"><img src="img/sort.png"></button>
                            <div class="sort__list">
                                <div class="sort__item active">в %</div>
                                <div class="sort__item">в тыс. руб.</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div data-simplebar class="loadObject flex">
                    <div class="loadObject__inner">
                        <div class="loadObject__item">
                            <div class="loadObject__title">Сентябрь 2017</div>
                            <div class="loadObject__body flex-vhcenter">
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">35%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Фитнес</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">80%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Квест</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">100%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Салон красоты</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">0%</div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Пустое</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">65%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm"><strong>Объект целиком</strong></div>
                                </div>
                            </div>
                        </div>
                        <div class="loadObject__item">
                            <div class="loadObject__title">Сентябрь 2017</div>
                            <div class="loadObject__body flex-vhcenter">
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">35%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Фитнес</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">80%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Квест</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">100%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Салон красоты</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">0%</div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Пустое</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">65%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm"><strong>Объект целиком</strong></div>
                                </div>
                            </div>
                        </div>
                        <div class="loadObject__item">
                            <div class="loadObject__title">Сентябрь 2017</div>
                            <div class="loadObject__body flex-vhcenter">
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">35%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Фитнес</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">80%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Квест</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">100%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Салон красоты</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">0%</div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Пустое</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">65%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm"><strong>Объект целиком</strong></div>
                                </div>
                            </div>
                        </div>
                        <div class="loadObject__item">
                            <div class="loadObject__title">Сентябрь 2017</div>
                            <div class="loadObject__body flex-vhcenter">
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">35%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Фитнес</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">80%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Квест</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">100%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Салон красоты</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">0%</div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Пустое</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">65%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm"><strong>Объект целиком</strong></div>
                                </div>
                            </div>
                        </div>
                        <div class="loadObject__item">
                            <div class="loadObject__title">Сентябрь 2017</div>
                            <div class="loadObject__body flex-vhcenter">
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">35%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Фитнес</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">80%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Квест</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">100%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Салон красоты</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">0%</div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm">Пустое</div>
                                </div>
                                <div class="column">
                                    <div class="column__body">
                                        <div class="column__title">65%</div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                        <div class="column__item"></div>
                                    </div>
                                    <div class="column__title-sm"><strong>Объект целиком</strong></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box__footer"><button type="button" class="btn btn-primary">Прогноз арендной платы</button></div>
            </div>
        </div>

        -->
        <!--
        <div class="inner__row inner__row_capital flex-vhcenter">
            <div class="box">
                <div class="box__header">
                    <div class="title-sm">Ваши вложения</div>
                </div>
                <div class="attachments">
                    <div class="attachments__left"><span>руб.</span><span>10 000 000</span><span>1 000 000</span><span>500 000</span><span>100 000</span><span>50 000</span><span>0</span></div>
                    <div class="attachments__inner flex">
                        <div class="left"><span><strong>Общие вложения</strong></span><span>Ваши вложения</span></div>
                        <div class="columns__wrapper flex">
                            <div class="column">
                                <div class="column__price-first">99 788 989</div>
                                <div style="height: 50%" class="column__body">
                                    <div style="height: 30%; background-color: #8bc4ea;" class="column__bg"></div>
                                </div>
                                <div class="column__price-second">23 877 664</div>
                                <div class="column__title">Энергетиков</div>
                            </div>
                            <div class="column">
                                <div class="column__price-first">99 788 989</div>
                                <div style="height: 65%" class="column__body">
                                    <div style="height: 70%; background-color: #f86b4f;" class="column__bg"></div>
                                </div>
                                <div class="column__price-second">23 877 664</div>
                                <div class="column__title">Объект 4</div>
                            </div>
                            <div class="column">
                                <div class="column__price-first">99 788 989</div>
                                <div style="height: 100%" class="column__body">
                                    <div style="height: 0%; background-color: #f86b4f;" class="column__bg"></div>
                                </div>
                                <div class="column__price-second">0</div>
                                <div class="column__title">Объект 4</div>
                            </div>
                            <div class="column">
                                <div class="column__price-first">Доходность<br>34,90%</div>
                                <div style="height: 100%" class="column__body">
                                    <div class="complete">Реализовано</div>
                                    <div style="height: 0%; background-color: #f86b4f;" class="column__bg"></div>
                                </div>
                                <div class="column__price-second"></div>
                                <div class="column__title">Объект 4</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box__footer flex-vhcenter"><button type="button" class="btn btn-primary">По датам</button></div>
            </div>
            <div class="circle-custom">
                <div class="title-sm">Общая сумма вложений</div>
                <div class="circle-drow">
                    <div class="text"><span>21 456 899,89</span>рублей</div>
                </div>
                <div class="circle__footer"><button type="button" class="btn btn-primary">Расчет вложений</button></div>
            </div>
        </div>
        -->
        <!--
        <div class="inner__row inner__row_yield flex-vhcenter">
            <div class="yield">
                <div class="box__header">
                    <div class="title-sm">Доходность от продажи</div>
                    <div class="box__select"><select><option>Электриков</option><option>Энергетиков</option></select></div>
                </div>
                <div class="yield__inner flex-vhcenter">
                    <div class="left">
                        <div class="wrapper">
                            <div class="canvas"><canvas id="year-capital"></canvas>
                                <script>
                                    var dataYearCapital = {
                                        labels: ['Аренда', 'Продажа'],
                                        datasets: [
                                            {
                                                data: [121678.00, 221344.45],
                                                backgroundColor: [
                                                    window.chartColors.blue,
                                                    window.chartColors.orange
                                                ]
                                            }]
                                    };
                                    var ctxYearCapital = document.getElementById("year-capital");
                                    // And for a doughnut chart
                                    var myDoughnutChart = new Chart(ctxYearCapital,
                                        {
                                            type: 'doughnut',
                                            data: dataYearCapital,
                                            options:
                                                {
                                                    legend:
                                                        {
                                                            display: false
                                                        },
                                                    cutoutPercentage: 89,
                                                    rotation: 1 * Math.PI,
                                                    circumference: 1 * Math.PI
                                                }
                                        });

                                </script>
                            </div>
                            <div class="tooltip-left">Арена<span>12 678,00 р.</span></div>
                            <div class="tooltip-right">Продажа<span>221 344,45 р.</span></div>
                            <div class="tooltip-center">Годовая доходность<span>34,56%</span></div>
                        </div><button type="button" class="btn btn-primary">Детали дохода</button></div>
                    <div class="right">
                        <div class="yield-slider">
                            <div>
                                <div class="title-15">Фитнес</div>
                                <div class="wrapper">
                                    <div class="canvas"><canvas id="fitness"></canvas>
                                        <script>
                                            var dataFitness = {
                                                labels: ['Аренда', 'Продажа'],
                                                datasets: [
                                                    {
                                                        data: [121678.00, 221344.45],
                                                        backgroundColor: [
                                                            window.chartColors.blue,
                                                            window.chartColors.orange
                                                        ]
                                                    }]
                                            };
                                            var ctxFitness = document.getElementById("fitness");
                                            // And for a doughnut chart
                                            var myDoughnutChartFitness = new Chart(ctxFitness,
                                                {
                                                    type: 'doughnut',
                                                    data: dataFitness,
                                                    options:
                                                        {
                                                            tooltips:
                                                                {
                                                                    enabled: false
                                                                },
                                                            legend:
                                                                {
                                                                    display: false
                                                                },
                                                            cutoutPercentage: 77,
                                                            rotation: 1 * Math.PI,
                                                            circumference: 1 * Math.PI
                                                        }
                                                });

                                        </script>
                                    </div>
                                    <div class="tooltip-center"><span>22,45%</span><a href="#" class="btn-link">Детали</a></div>
                                </div>
                            </div>
                            <div>
                                <div class="title-15">Салон красоты</div>
                                <div class="wrapper">
                                    <div class="canvas"><canvas id="salon"></canvas>
                                        <script>
                                            var dataSalon = {
                                                labels: ['Аренда', 'Продажа'],
                                                datasets: [
                                                    {
                                                        data: [121678.00, 221344.45],
                                                        backgroundColor: [
                                                            window.chartColors.blue,
                                                            window.chartColors.orange
                                                        ]
                                                    }]
                                            };
                                            var ctxSalon = document.getElementById("salon");
                                            // And for a doughnut chart
                                            var myDoughnutChartSalon = new Chart(ctxSalon,
                                                {
                                                    type: 'doughnut',
                                                    data: dataSalon,
                                                    options:
                                                        {
                                                            tooltips:
                                                                {
                                                                    enabled: false
                                                                },
                                                            legend:
                                                                {
                                                                    display: false
                                                                },
                                                            cutoutPercentage: 77,
                                                            rotation: 1 * Math.PI,
                                                            circumference: 1 * Math.PI
                                                        }
                                                });

                                        </script>
                                    </div>
                                    <div class="tooltip-center"><span>12,45%</span><a href="#" class="btn-link">Детали</a></div>
                                </div>
                            </div>
                            <div>
                                <div class="title-15">Квест</div>
                                <div class="wrapper">
                                    <div class="canvas"><canvas id="quest"></canvas>
                                        <script>
                                            var dataQuest = {
                                                labels: ['Аренда', 'Продажа'],
                                                datasets: [
                                                    {
                                                        data: [121678.00, 221344.45],
                                                        backgroundColor: [
                                                            window.chartColors.blue,
                                                            window.chartColors.orange
                                                        ]
                                                    }]
                                            };
                                            var ctxQuest = document.getElementById("quest");
                                            // And for a doughnut chart
                                            var myDoughnutChartQuest = new Chart(ctxQuest,
                                                {
                                                    type: 'doughnut',
                                                    data: dataQuest,
                                                    options:
                                                        {
                                                            tooltips:
                                                                {
                                                                    enabled: false
                                                                },
                                                            legend:
                                                                {
                                                                    display: false
                                                                },
                                                            cutoutPercentage: 77,
                                                            rotation: 1 * Math.PI,
                                                            circumference: 1 * Math.PI
                                                        }
                                                });

                                        </script>
                                    </div>
                                    <div class="tooltip-center"><span>12,45%</span><a href="#" class="btn-link">Детали</a></div>
                                </div>
                            </div>
                            <div>
                                <div class="title-15">Тест</div>
                                <div class="wrapper">
                                    <div class="canvas"><canvas id="test"></canvas>
                                        <script>
                                            var dataTest = {
                                                labels: ['Аренда', 'Продажа'],
                                                datasets: [
                                                    {
                                                        data: [121678.00, 221344.45],
                                                        backgroundColor: [
                                                            window.chartColors.blue,
                                                            window.chartColors.orange
                                                        ]
                                                    }]
                                            };
                                            var ctxTest = document.getElementById("test");
                                            // And for a doughnut chart
                                            var myDoughnutChartTest = new Chart(ctxTest,
                                                {
                                                    type: 'doughnut',
                                                    data: dataTest,
                                                    options:
                                                        {
                                                            tooltips:
                                                                {
                                                                    enabled: false
                                                                },
                                                            legend:
                                                                {
                                                                    display: false
                                                                },
                                                            cutoutPercentage: 77,
                                                            rotation: 1 * Math.PI,
                                                            circumference: 1 * Math.PI
                                                        }
                                                });

                                        </script>
                                    </div>
                                    <div class="tooltip-center"><span>12,45%</span><a href="#" class="btn-link">Детали</a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
        -->


    <!--

    <div class="container demo">Демонстрация модальных окон
        <div class="wrapper"><button type="button" data-target="#salesRevenue" class="btn btn-primary">Подробности дохода с продажи</button><button type="button" data-target="#investments" class="btn btn-primary">Ваши вложения</button><button type="button" data-target="#underobject"
                                                                                                                                                                                                                                                   class="btn btn-primary">Доход по подобъектам</button><button type="button" data-target="#incomeProperty" class="btn btn-primary">Ваш доход по каждому объекту</button><button type="button" data-target="#allincomeProperty" class="btn btn-primary">Общие доходы и расходы по каждому объекту</button>
            <button
                    type="button" data-target="#forecast" class="btn btn-primary">Прогноз арендной платы по месяцам</button><button type="button" data-target="#detailsProfit" class="btn btn-primary">Подробности расчета прибыли и доли</button></div>
    </div>

    -->
    </div>


    <div id="salesRevenue" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Подробности дохода с продажи</div>
                    <div class="tabs">
                        <div class="tabs__item">Энергетиков</div>
                        <div class="tabs__item active">Фитнес</div>
                    </div>
                </div>
                <div class="table">
                    <table>
                        <thead>
                        <tr>
                            <th>Инвестор</th>
                            <th>Вложено</th>
                            <th>Доля</th>
                            <th>Доход с аренды</th>
                            <th>Доход с продажи</th>
                            <th>Выплата при выводе</th>
                            <th>Общая доходность</th>
                            <th>Выплата до вывода</th>
                            <th>Прирост после вывода</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Инвестор 1</td>
                            <td>1 600 000,00</td>
                            <td>14,61%</td>
                            <td>20 788,46</td>
                            <td>31 899,45</td>
                            <td>147 587,33</td>
                            <td>34,90%</td>
                            <td>554 681,29</td>
                            <td>41,42%</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div id="investments" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Ваши вложения</div>
                    <div class="head__bottom">
                        <div class="select"><select><option>Чаплина</option><option>Чаплина 2</option></select></div>
                        <div class="date-select">
                            <div class="date-select-wrapper flex-vhcenter">
                                <div class="field"><svg class="icon-calendar"><use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use></svg><input type="text" id="from" name="from"></div>
                                <div class="field"><svg class="icon-calendar"><use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use></svg><input type="text" id="to" name="to"></div>
                            </div>
                        </div><button type="button" class="btn btn-primary btn-icon"><svg class="icon-right"><use xlink:href="resources/core/img/sprite.svg#icon-right"></use></svg></button></div>
                </div>
                <div class="investments">
                    <div class="investments__head flex">
                        <div class="col">Дата</div>
                        <div class="col">Сумма</div>
                        <div class="col">Источник</div>
                    </div>
                    <div data-simplebar class="investments__body">
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/sber.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/money.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/invoice.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/reinv.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/sber.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/money.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/invoice.png' />"></div>
                        </div>
                        <div class="row flex-vcenter">
                            <div class="col">01.11.17</div>
                            <div class="col">6 895,46 руб.</div>
                            <div class="col"><img src="<c:url value='/resources/core/img/reinv.png' />"></div>
                        </div>
                    </div>
                    <div class="investments__footer flex"><span>Итого за выбранный период:</span><span><strong>41 287,56 руб.</strong></span></div>
                </div>
            </div>
        </div>
    </div>

    <div id="underobject" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Доход по подобъектам</div>
                    <div class="head__bottom">
                        <div class="select"><select><option>Чаплина</option><option>Чаплина 2</option></select></div>
                    </div>
                </div>
                <div class="canvas"><canvas id="chart-underobject"></canvas>
                    <script>
                        var configunderobject = {
                            type: 'line',
                            data:
                                {
                                    labels: ["Июль", "Авг", "Сент", "Окт", "Нояб", "Дек", "Янв", "Фев", "Март", "Апрель"],
                                    datasets: [
                                        {
                                            label: "Фитнес",
                                            backgroundColor: window.chartColors.sea,
                                            borderColor: window.chartColors.sea,
                                            data: [1000, 2000, 5000, 9000, 3000, 10000, 25000, 15000, 9000, 10000],
                                            fill: false
                                        },
                                        {
                                            label: "Квест",
                                            fill: false,
                                            backgroundColor: window.chartColors.green,
                                            borderColor: window.chartColors.green,
                                            data: [3000, 2000, 10000, 15000, 8000, 30000, 20000, 5000, 3000, 20000],
                                        },
                                        {
                                            label: "Салон красоты",
                                            fill: false,
                                            backgroundColor: window.chartColors.purple,
                                            borderColor: window.chartColors.purple,
                                            data: [10000, 20000, 22000, 25000, 30000, 25000, 25000, 15000, 9000, 10000],
                                        },
                                        {
                                            label: "Пустое",
                                            fill: false,
                                            backgroundColor: window.chartColors.blue,
                                            borderColor: window.chartColors.blue,
                                            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                                        }]
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
                                                            max: 30000,
                                                            stepSize: 5000,
                                                            fontColor: '#11325b',
                                                            fontSize: 15,
                                                            fontFamily: 'OpenSans'
                                                        }
                                                }]
                                        }
                                }
                        };
                        var ctxunderobject = document.getElementById("chart-underobject").getContext("2d");
                        window.myDoughnut3 = new Chart(ctxunderobject, configunderobject);

                    </script>
                </div>
            </div>
        </div>
    </div>
    <div id="forecast" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Прогноз арендной платы по месяцам</div>
                    <div class="head__bottom">
                        <div class="select"><select><option>Чаплина</option><option>Чаплина 2</option></select></div>
                    </div>
                </div>
                <div class="canvas"><canvas id="chart-forecast"></canvas>
                    <script>
                        var configforecast = {
                            type: 'line',
                            data:
                                {
                                    labels: ["Июль", "Авг", "Сент", "Окт", "Нояб", "Дек", "Янв", "Фев", "Март", "Апрель"],
                                    datasets: [
                                        {
                                            label: "Фитнес",
                                            backgroundColor: window.chartColors.sea,
                                            borderColor: window.chartColors.sea,
                                            data: [1000, 2000, 5000, 9000, 3000, 10000, 25000, 15000, 9000, 10000],
                                            fill: false
                                        },
                                        {
                                            label: "Квест",
                                            fill: false,
                                            backgroundColor: window.chartColors.green,
                                            borderColor: window.chartColors.green,
                                            data: [3000, 2000, 10000, 15000, 8000, 30000, 20000, 5000, 3000, 20000],
                                        },
                                        {
                                            label: "Салон красоты",
                                            fill: false,
                                            backgroundColor: window.chartColors.purple,
                                            borderColor: window.chartColors.purple,
                                            data: [10000, 20000, 22000, 25000, 30000, 25000, 25000, 15000, 9000, 10000],
                                        },
                                        {
                                            label: "Пустое",
                                            fill: false,
                                            backgroundColor: window.chartColors.blue,
                                            borderColor: window.chartColors.blue,
                                            data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                                        }]
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
                                                            max: 30000,
                                                            stepSize: 5000,
                                                            fontColor: '#11325b',
                                                            fontSize: 15,
                                                            fontFamily: 'OpenSans'
                                                        }
                                                }]
                                        }
                                }
                        };
                        var ctxforecast = document.getElementById("chart-forecast").getContext("2d");
                        window.myDoughnut3 = new Chart(ctxforecast, configforecast);

                    </script>
                </div>
            </div>
        </div>
    </div>
    <div id="incomeProperty" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Ваш доход по каждому объекту</div>
                    <div class="head__bottom">
                        <div class="date-select">
                            <div class="date-select-wrapper flex-vhcenter">
                                <div class="field"><svg class="icon-calendar"><use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use></svg><input type="text" name="to" class="date-month"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="accordion">
                    <div data-simplebar class="accordion__inner">
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box__footer flex-vhcenter"><button type="button" data-target="#" class="btn btn-primary">Подробности расчета</button></div>
            </div>
        </div>
    </div>

    <div id="allincomeProperty" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Общие доходы и расходы по каждому объекту</div>
                    <div class="head__bottom">
                        <div class="date-select">
                            <div class="date-select-wrapper flex-vhcenter">
                                <div class="field"><svg class="icon-calendar"><use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use></svg><input type="text" name="to" class="date-month"></div>
                            </div>
                        </div>
                        <div class="group-buttons"><button type="button" class="btn btn-primary active">Доходы</button><button type="button" class="btn btn-primary">Расходы</button></div>
                    </div>
                </div>
                <div class="accordion">
                    <div data-simplebar class="accordion__inner">
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="accordion__item">
                            <div class="accordion__head">
                                <div class="col col_action"><span>Чаплина</span></div>
                                <div class="col"><span>6 895,46 руб.</span></div>
                            </div>
                            <div class="accordion__body">
                                <div class="accordionChild__list">
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="accordionChild__item">
                                        <div class="row row_action">
                                            <div class="row__inner">
                                                <div class="col"><span>Пустое_Чаплина</span></div>
                                                <div class="col"><span>34 425,00</span></div>
                                            </div>
                                            <div class="row_child-wrapper">
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                                <div class="row_child-item">
                                                    <div class="col"><span>Пустое_Чаплина</span></div>
                                                    <div class="col"><span>34 425,00</span></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="detailsProfit" class="popup">
        <div class="popup__inner"><a href="#" class="popup__close"><svg class="icon-close"><use xlink:href="resources/core/img/sprite.svg#icon-close"></use></svg></a>
            <div class="popup__body">
                <div class="head">
                    <div class="title">Подробности дохода</div>
                    <div class="head__bottom">
                        <div class="date-select">
                            <div class="date-select-wrapper flex-vhcenter">
                                <div class="field"><svg class="icon-calendar"><use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use></svg><input type="text" name="to" class="date-month" id="dateMonth"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="accordion">
                    <div class="accordion__head">
                        <div class="col">Объект</div>
                        <div class="col">Общий доход</div>
                        <div class="col">Доля</div>
                        <div class="col">Ваш баланс после расходов</div>
                        <div class="col">Налог</div>
                        <div class="col">После налогообложения</div>
                        <div class="col">Комиссия</div>
                        <div class="col">После комиссии</div>
                    </div>
                    <div data-simplebar class="accordion__inner" id="accHead">

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<!--END out-->
<!--LOAD SCRIPTS-->
<%@include file="loader.jsp"%>
</body>

</html>
