<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>

<div class="modal fade" id="readAnnexTable" tabindex="-1" role="dialog" aria-labelledby="readAnnex" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document" style="min-width: 790px">
        <div class="modal-content popup_inner" style="border-radius: 0; max-height: 90%; overflow: auto">
            <div class="modal-header">
                <h5 class="modal-title" style="font-weight: 600; color: #11325b;">Приложения для ознакомления</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
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
</div>

<div id="readAnnex" class="bg">
    Ознакомьтесь с новыми приложениями, после этого панель отчётов инвестора станет доступна.
    <br>
    <br>
    <div class="row">
        <div class="col-md-6">
            <button id="look" type="button" class="btn btn-primary annex-look" data-toggle="modal" data-target="#readAnnexTable">Посмотреть</button>
        </div>
        <div class="col-md-6">
                <a href="<c:url value='/logout' />" style="color: white; text-decoration: none" >
                    <button type="button" class="btn btn-success annex-exit">Выход</button>
                </a>
        </div>
    </div>
</div>

<%--<div id="read" class="popup" style="z-index: 1000002">--%>
<%--    <div class="popup__inner"><a href="#" class="popup__close">--%>
<%--        <svg class="icon-close">--%>
<%--            <use xlink:href="resources/core/img/sprite.svg#icon-close"></use>--%>
<%--        </svg>--%>
<%--    </a>--%>
<%--        <div class="popup__body">--%>
<%--            <div class="head">--%>
<%--                <div class="title">Приложения для ознакомления</div>--%>
<%--            </div>--%>
<%--            <div class="table">--%>
<%--                <table id="tblAnnex">--%>
<%--                    <thead>--%>
<%--                    <tr>--%>
<%--                        <th>Приложение</th>--%>
<%--                        <th>Ссылка</th>--%>
<%--                        <th>Ознакомление</th>--%>
<%--                        <th>Дата ознакомления</th>--%>
<%--                    </tr>--%>
<%--                    </thead>--%>
<%--                    <tbody>--%>

<%--                    </tbody>--%>
<%--                </table>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
