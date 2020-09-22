<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Доходы инвесторов</title>
    <sec:csrfMetaTags/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">

    <meta http-equiv='cache-control' content='no-cache'>
    <meta http-equiv='expires' content='0'>
    <meta http-equiv='pragma' content='no-cache'>

    <meta name="viewport" content="width=1370">
    <meta name="theme-color" content="#fff">
    <meta name="format-detection" content="telephone=no">
    <meta name="robots" content="noindex, nofollow">
    <script type="text/javascript" src="<c:url value='/resources/core/js/app.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/Chart.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/app.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery.selectric.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-ui.min.js' />"></script>

    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />"></script>

    <script type="text/javascript" src="<c:url value='/resources/core/js/flowsVisualization.js' />"></script>

    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <style type="text/css">
        /* Chart.js */
        @-webkit-keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        @keyframes chartjs-render-animation {
            from {
                opacity: 0.99
            }
            to {
                opacity: 1
            }
        }

        .chartjs-render-monitor {
            -webkit-animation: chartjs-render-animation 0.001s;
            animation: chartjs-render-animation 0.001s;
        }

        .profitBox {
            position: absolute;
            font-size: 6px;
            font-weight: 600;
            text-align: center;
            max-width: 60px;
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            align-items: center;
            top: 5%;
        }

        .profitBox div {
            margin: 0 15px;
            max-width: 60px;
        }

        .profit .splitter .gains {
            max-width: 50px;
            margin: 0 auto;
        }

        .notParticipate {
            color: #70696c;
            border: 2px solid #70696c;
            font-weight: 600;
            font-size: 14px;
            padding: 3px 14px;
            position: absolute;
            -ms-transform: rotate(-90deg) translateY(-110%) translateX(5%);
            transform: rotate(-90deg) translateY(-110%) translateX(5%);
            bottom: 35%;
            left: -20%;
            width: 150px;
        }

    </style>
    <!-- Yandex.Metrika counter -->
    <script type="text/javascript" >
        (function(m,e,t,r,i,k,a){m[i]=m[i]||function(){(m[i].a=m[i].a||[]).push(arguments)};
            m[i].l=1*new Date();k=e.createElement(t),a=e.getElementsByTagName(t)[0],k.async=1,k.src=r,a.parentNode.insertBefore(k,a)})
        (window, document, "script", "https://mc.yandex.ru/metrika/tag.js", "ym");

        ym(53797528, "init", {
            clickmap:true,
            trackLinks:true,
            accurateTrackBounce:true,
            webvisor:true
        });
    </script>
    <noscript><div><img src="https://mc.yandex.ru/watch/53797528" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
    <!-- /Yandex.Metrika counter -->
</head>

<body>
<input type="hidden" id="invId" value="${invId}">
<input type="hidden" id="invLogin" value="${invLogin}">
<script>var amo_social_button = {
    id: 4009,
    hash: "f8f8284b4bfb0a4600a985dc4d76d76e2ab7097a0dd0c3061b62634e7139bde6",
    locale: "ru"
};</script>
<script id="amo_social_button_script" async="async" src="https://gso.amocrm.ru/js/button.js"></script>
<div id="readAnnex" class="bg">
    Ознакомьтесь с новыми приложениями, после этого панель отчётов инвестора станет доступна.
    <br>
    <br>
    <button id="look" type="button" data-target="#read" class="btn btn-primary">Посмотреть</button>
    <button id="ext" type="button" class="btn btn-suc">Выход</button>
</div>

<div id="read" class="popup" style="z-index: 1000002">
    <div class="popup__inner"><a href="#" class="popup__close">
        <svg class="icon-close">
            <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
        </svg>
    </a>
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
    <%@include file="old_authheader.jsp" %>
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

                <div class="title-sm" id="incomeTitle" style="padding-left: 0; margin-left: 30%; margin-bottom: 10px;">
                    Ваш доход за
                </div>
                <div class="box__select" style="width: 165px; margin-left: 50px;">
                    <select title="Месяцы" id="months" tabindex="-1">

                    </select>
                </div>
                <div class="box__select" style="width: 110px; position: relative; left: 220px; top: -40px">
                    <select title="Года" id="years" tabindex="-1">

                    </select>
                </div>
                <button type="button" class="btn btn-primary btn-icon" id="searchIncomesByDate"
                        style="position: relative; left: 395px; top: -130px;">
                    <svg class="icon-right">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink"
                             xlink:href="resources/core/img/sprite.svg#icon-right"></use>
                    </svg>
                </button>
                <div class="wrapper flex-vcenter">
                    <div class="circle-canvas">
                        <canvas id="doughnut-chart"></canvas>
                        <div class="circle-canvas__text" id="totalIncome"></div>
                    </div>
                    <div class="circle-canvas__list" id="incomesList">

                    </div>
                </div>
                <button type="button" data-target="#detailsProfit" class="btn btn-primary">Подробности дохода</button>

            </div>
        </div>

        <div class="inner__row inner__row_costs flex-vhcenter">
            <div class="box">
                <div class="box__header">
                    <div class="title-sm">Общие доходы и расходы объекта</div>
                    <div class="box__select">
                        <select title="Объекты" id="incomesAndExpenses">

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
            </div>

            <div class="circle">
                <div class="title-sm" id="totalExpenses"></div>
                <div class="wrapper flex-vcenter">
                    <div class="circle-canvas">
                        <canvas id="doughnut-chart-expenses"></canvas>
                        <div class="circle-canvas__text" id="totalExpensesText"></div>
                    </div>
                    <div class="circle-canvas__list" id="expensesList">

                    </div>

                </div>
            </div>
        </div>

        <div class="inner__row inner__row_capital flex-vhcenter">
            <div class="box">
                <div class="box__header">
                    <div class="title-sm">История Ваших вложений</div>
                </div>
                <div class="attachments">
                    <div class="attachments__left"></div>
                    <div class="attachments__inner flex" style="padding-right: 15px">
                        <div class="left"><span><strong>Стоимость объектов</strong></span><span>Ваши вложения</span>
                        </div>
                        <div class="columns__wrapper flex" id="investedMoney">

                        </div>
                    </div>
                </div>
                <div class="box__footer flex-vhcenter">
                    <button type="button" class="btn btn-primary" data-target="#investmentsAll" >По датам</button>
                </div>
            </div>

            <div class="circle">
                <div class="title-sm" style="text-align: center; padding-right: 55px; padding-left: 0">Ваш баланс</div>
                <div class="title-sm" id="totalSum"></div>
                <div class="wrapper flex-vcenter">
                    <div class="circle-canvas">
                        <canvas id="doughnut-chart-invested"></canvas>
                        <div class="circle-canvas__text" id="totalInvested"></div>
                    </div>
                    <div class="circle-canvas__list" id="investedList">

                    </div>
                </div>
            </div>

        </div>

        <div id="salesRevenue" class="popup">
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
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
            <div class="popup__inner"><a href="#" class="popup__close" id="closeInvestmentsDetails">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title" id="investmentsDetailsTitle">Ваши вложения</div>
                        <div class="head__bottom">

                            <div class="date-select">
                                <div class="date-select-wrapper flex-vhcenter">
                                    <div class="field">
                                        <svg class="icon-calendar">
                                            <use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use>
                                        </svg>
                                        <input type="text" id="from" name="from"></div>
                                    <div class="field">
                                        <svg class="icon-calendar">
                                            <use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use>
                                        </svg>
                                        <input type="text" id="to" name="to"></div>
                                </div>
                            </div>
                            <button type="button" class="btn btn-primary btn-icon" id="searchInvestmentsByDate">
                                <svg class="icon-right">
                                    <use xlink:href="resources/core/img/sprite.svg#icon-right"></use>
                                </svg>
                            </button>
                            <div class="box__select" style="width: 150px; margin-left: 20px">
                                <select title="ввод_вывод" id="in_out" tabindex="-1">
                                    <option value="Ввод" selected>Вложено</option>
                                    <option value="Вывод">Выведено</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="investments">
                        <div class="investments__head flex">
                            <div class="col">Дата</div>
                            <div class="col">Сумма</div>
                            <div class="col">Источник</div>
                            <div class="col" hidden>Объект</div>
                        </div>
                        <div data-simplebar class="investments__body" id="investedDetailsTable">

                        </div>
                        <div class="investments__footer flex" id="totalSumFooter">
                            <span>Итого за выбранный период:</span>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="investmentsAll" class="popup">
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title">Ваши вложения</div>
                        <div class="head__bottom">
                        </div>
                    </div>
                    <div class="investments">
                        <div class="investments__head flex">
                            <div class="col">Объект</div>
                            <div class="col">Вложено всего</div>
                            <div class="col">Выведено всего</div>
                            <div class="col">Сумма</div>
                            <div class="col">Детали</div>
                        </div>
                        <div data-simplebar class="investments__body" id="investedAllDetailsTable">

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="underobject" class="popup">
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title">Доход по подобъектам</div>
                        <div class="head__bottom">
                            <div class="select"><select>
                                <option>Чаплина</option>
                                <option>Чаплина 2</option>
                            </select></div>
                        </div>
                    </div>
                    <div class="canvas">
                        <canvas id="chart-underobject"></canvas>
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
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title">Прогноз арендной платы по месяцам</div>
                        <div class="head__bottom">
                            <div class="select"><select>
                                <option>Чаплина</option>
                                <option>Чаплина 2</option>
                            </select></div>
                        </div>
                    </div>
                    <div class="canvas">
                        <canvas id="chart-forecast"></canvas>
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
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title">Ваш доход по каждому объекту</div>
                        <div class="head__bottom">
                            <div class="date-select">
                                <div class="date-select-wrapper flex-vhcenter">
                                    <div class="field">
                                        <svg class="icon-calendar">
                                            <use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use>
                                        </svg>
                                        <input type="text" name="to" class="date-month"></div>
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
                    <div class="box__footer flex-vhcenter">
                        <button type="button" data-target="#" class="btn btn-primary">Подробности расчета</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="allincomeProperty" class="popup">
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title">Общие доходы и расходы по каждому объекту</div>
                        <div class="head__bottom">
                            <div class="date-select">
                                <div class="date-select-wrapper flex-vhcenter">
                                    <div class="field">
                                        <svg class="icon-calendar">
                                            <use xlink:href="resources/core/img/sprite.svg#icon-calendar"></use>
                                        </svg>
                                        <input type="text" name="to" class="date-month"></div>
                                </div>
                            </div>
                            <div class="group-buttons">
                                <button type="button" class="btn btn-primary active">Доходы</button>
                                <button type="button" class="btn btn-primary">Расходы</button>
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
            <div class="popup__inner"><a href="#" class="popup__close">
                <svg class="icon-close">
                    <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>
                </svg>
            </a>
                <div class="popup__body">
                    <div class="head">
                        <div class="title">Подробности дохода</div>

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
    <%@include file="loader.jsp" %>
    <%@include file="slideDiv.jsp" %>
</body>

</html>
