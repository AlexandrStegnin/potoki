<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>

<div class="modal fade" id="readAnnexTable" tabindex="-1" role="dialog" aria-labelledby="readAnnex" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document" style="min-width: 75%">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-responsive" style="font-weight: 600; color: #11325b;">Приложения для ознакомления</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true" class="text-responsive">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table id="tblAnnex" class="table table-striped table-hover">
                        <thead style="text-align: center">
                        <tr>
                            <th scope="col" class="text-responsive">Приложение</th>
                            <th scope="col" class="text-responsive">Ознакомлен</th>
                            <th scope="col" class="text-responsive">Дата</th>
                        </tr>
                        </thead>
                        <tbody class="text-responsive-annex">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="readAnnex" class="container-fluid bg">
    Ознакомьтесь с новыми приложениями, после этого панель отчётов инвестора станет доступна.
    <br>
    <br>
    <div class="row">
        <div class="col">
            <button id="look" type="button" class="btn btn-primary annex-look" data-toggle="modal" data-target="#readAnnexTable">Посмотреть</button>
        </div>
        <div class="col">
                <a href="<c:url value='/logout' />" style="color: white; text-decoration: none" >
                    <button type="button" class="btn btn-success annex-exit">Выход</button>
                </a>
        </div>
    </div>
</div>
