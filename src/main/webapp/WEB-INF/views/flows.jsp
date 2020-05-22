<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Доходы инвесторов</title>
    <sec:csrfMetaTags/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">

    <meta http-equiv='cache-control' content='no-cache'>
    <meta http-equiv='expires' content='0'>
    <meta http-equiv='pragma' content='no-cache'>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.js' />"></script>
    <script type="text/javascript"
            src="<c:url value='https://cdn.jsdelivr.net/npm/chartjs-chart-treemap@0.2.3' />"></script>
    <script type="text/javascript" src="<c:url value='https://www.gstatic.com/charts/loader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/charts-for-flows.js' />"></script>
    <link href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"
          rel="stylesheet"/>
    <script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
    <!-- Yandex.Metrika counter -->
    <script type="text/javascript">
        (function (m, e, t, r, i, k, a) {
            m[i] = m[i] || function () {
                (m[i].a = m[i].a || []).push(arguments)
            };
            m[i].l = 1 * new Date();
            k = e.createElement(t), a = e.getElementsByTagName(t)[0], k.async = 1, k.src = r, a.parentNode.insertBefore(k, a)
        })
        (window, document, "script", "https://mc.yandex.ru/metrika/tag.js", "ym");

        ym(53797528, "init", {
            clickmap: true,
            trackLinks: true,
            accurateTrackBounce: true,
            webvisor: true
        });
    </script>
    <noscript>
        <div><img src="https://mc.yandex.ru/watch/53797528" style="position:absolute; left:-9999px;" alt=""/></div>
    </noscript>
    <!-- /Yandex.Metrika counter -->
    <script type="text/javascript" src="<c:url value='/resources/core/js/annex.js' />"></script>
    <link href="<c:url value='/resources/core/css/annex.css' />" rel="stylesheet"/>
    <script type="text/javascript"
            src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
</head>
<body>
<%@include file="annex_popup.jsp" %>
<div class="out">
    <%@include file="header.jsp" %>
    <div class="container-fluid" id="barChartContainer">
        <div class="row">
            <canvas id="barChart" aria-label="Your browser does not support the canvas element.">
                <p>Данные загружаются</p>
            </canvas>
        </div>
    </div>
    <div class="container-fluid" id="investedBarChartContainer">
        <div class="row">
            <canvas id="investedBarChart" aria-label="Your browser does not support the canvas element.">
                <p>Данные загружаются</p>
            </canvas>
        </div>
    </div>
    <div class="container-fluid" id="profitBarChartContainer">
        <div class="row">
            <canvas id="profitBarChart" aria-label="Your browser does not support the canvas element.">
                <p>Данные загружаются</p>
            </canvas>
        </div>
    </div>
</div>
<%@include file="loader.jsp" %>
</body>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>

</html>
