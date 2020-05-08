<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <meta name="viewport" content="width=1370">
    <meta name="theme-color" content="#fff">
    <meta name="format-detection" content="telephone=no">
    <meta name="robots" content="noindex, nofollow">
    <script type="text/javascript" src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.js' />"></script><script type="text/javascript" src="<c:url value='/resources/core/js/charts-for-flows.js' />"></script>
</head>
<body>
<canvas id="barChart" width="400" height="150" aria-label="Your browser does not support the canvas element." role="img">
    <p>Данные загружаются</p>
</canvas>
<canvas id="doughnutChart" width="400" height="150" aria-label="Your browser does not support the canvas element." role="img">
    <p>Данные загружаются</p>
</canvas>
</body>
</html>
