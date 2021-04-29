let Action = {
    CREATE: 'CREATE',
    UPDATE: 'UPDATE',
    FIND: 'FIND',
    DELETE: 'DELETE',
    properties: {
        CREATE: {
            url: 'create'
        },
        UPDATE: {
            url: 'update'
        },
        FIND: {
            url: 'find'
        },
        DELETE: {
            url: 'delete'
        }
    }
}

Object.freeze(Action)

let UnderFacilityDTO = function () {}

UnderFacilityDTO.prototype = {
    id: null,
    name: null,
    facility: null,
    build: function (id, name, facility) {
        this.id = id;
        this.name = name;
        this.facility = facility;
    }
}

let confirmForm;
let ufForm;

jQuery(document).ready(function ($) {
    confirmForm = $('#confirm-form');
    ufForm = $('#uf-modal-form');
    onCreateUnderFacilityEvent()
    onUpdateUnderFacilityEvent()
    onDeleteFacilityEvent()
    onAcceptFormEvent()
    $('#create').click(function (event) {
        showLoader();
        let action = $(this).data('action')
        if (action === 'create') {
            event.preventDefault();
            create();
        }
        closeLoader();
    })
});

/**
 * Событие при создании подобъекта
 */
function onCreateUnderFacilityEvent() {
    $(document).on('click', '#create-uf',function (e) {
        e.preventDefault()
        showForm(Action.CREATE)
    })
}

/**
 * Событие при изменении подобъекта
 */
function onUpdateUnderFacilityEvent() {
    $(document).on('click', '#edit-uf',function (e) {
        e.preventDefault()
        showLoader()
        let ufId = $(this).attr('data-uf-id')
        getUnderFacility(ufId)
    })
}

/**
 * Показать форму для создания/редактирования подобъекта
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание подобъекта'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление подобъекта'
            button = 'Обновить'
            break
    }
    ufForm.find('#title').html(title)
    ufForm.find('#accept').html(button)
    ufForm.find('#accept').attr('data-action', action)
    ufForm.modal('show')
}

/**
 * Получить подобъект по id
 *
 * @param ufId {number}
 */
function getUnderFacility(ufId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let ufDTO = new UnderFacilityDTO()
    ufDTO.build(ufId, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(ufDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        })
        .done(function (data) {
            showUpdateUnderFacilityForm(data)
        })
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            console.log('Закончили!');
        });
}

/**
 * Показать форму для изменения подобъекта
 *
 * @param data
 */
function showUpdateUnderFacilityForm(data) {
    let ufDTO = new UnderFacilityDTO()
    ufDTO.build(data.id, data.name, data.facility)
    ufForm.find('#uf-id').val(ufDTO.id)
    ufForm.find('#edit').val(true)
    ufForm.find('#name').val(ufDTO.name)
    bindFacility(ufDTO.facility)
    ufForm.find('#action').attr("data-action", Action.UPDATE)
    closeLoader()
    showForm(Action.UPDATE)
}

/**
 * Преобразовать объект в выделенный элемент выпадающего списка
 *
 * @param facility
 */
function bindFacility(facility) {
    $.each(ufForm.find('#facility option'), function (ind, el) {
        if (el.value === (facility.id + '')) {
            $(el).attr('selected', 'selected')
        }
    })
    ufForm.find('#facility').selectpicker('refresh')
}
