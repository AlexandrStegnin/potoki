<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="send-email-modal-form">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="title">ОТПРАВКА ПРИГЛАСИТЕЛЬНОГО EMAIL</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="container">
          <form:form method="POST" modelAttribute="emailDTO" class="form-horizontal"
                     style="margin: 10px 0 10px 0" id="send-email-form">
            <div class="form-group row">
              <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="user">ИНВЕСТОР:</label>
              <div class="col-sm-6">
                <form:select path="user" id="user" items="${investors}" multiple="false"
                             itemValue="id" itemLabel="login"
                             class="form-control form-control-sm selectpicker"
                             data-live-search="true" data-size="10"/>
                <div class="has-error d-none" id="investorError">
                  НЕОБХОДИМО ВЫБРАТЬ ИНВЕСТОРА
                </div>
              </div>
            </div>
          </form:form>
        </div>
      </div>
      <div class="modal-footer" data-action="" id="action">
        <button type="button" class="btn btn-primary" id="send">ОТПРАВИТЬ</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">ОТМЕНА</button>
      </div>
    </div>
  </div>
</div>
