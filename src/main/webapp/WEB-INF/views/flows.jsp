<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>Инвестиции</title>
    <sec:csrfMetaTags/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css' />"
          rel="stylesheet"/>
    <script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
    <style type="text/css">
        table, td, th {
            text-align: center;
        }
    </style>
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
    <link href="<c:url value='/resources/core/css/annex.css' />" rel="stylesheet"/>
</head>
<body>
<%@include file="annex_popup.jsp" %>
<input type="hidden" id="investorLogin" value="${investorLogin}">
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
<%@include file="ddk_loader.jsp" %>
<%@include file="popup_modal.jsp" %>
<%@include file="table-popup.jsp" %>
</body>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js' />"></script>
<script type="text/javascript" src="<c:url value='https://www.gstatic.com/charts/loader.js' />"></script>
<%--    <script src="<c:url value='https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@0.7.0' />"></script>--%>
<script type="text/javascript" src="<c:url value='/resources/core/js/charts-for-flows.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/annex.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
</html>
