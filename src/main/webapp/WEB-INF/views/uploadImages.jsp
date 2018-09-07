<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
<head>
    <title>Upload File Request Page</title>
    <sec:csrfMetaTags />
</head>
<body>
<sec:authorize access="isFullyAuthenticated()">
    <sec:authorize access="hasRole('ADMIN')">
        <div class="well">
            <form:form method="POST" action="uploadImage" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-horizontal">

                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-3 control-lable" for="file">Загрузить файл</label>
                        <div class="col-md-7">
                            <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                            <div class="has-error">
                                <form:errors path="file" class="help-inline"/>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Загрузить</button>
                    </div>
                </div>
                <a href="/annexToContract/${urlToAnnex}" target="_blank">PDF report</a>

            </form:form>
        </div>

    </sec:authorize>
</sec:authorize>
<%@include file="slideDiv.jsp" %>
</body>
</html>