jQuery(document).ready(function ($) {
    var saveFrm = $('#save');
    var submit = saveFrm.find(':submit');

    var jVal = {
        'login' : function() {

            var nameInfo = $('#loginErr');
            var ele = $('#login');
            var rus = new RegExp(".*?[А-Яа-я $\/].*?");
            if(ele.val().length < 4 || ele.val() === '' || ele.val().length > 16){
                jVal.errors = true;
                nameInfo.html('Имя пользователя должно быть от 4 до 16 символов').show();
            }else if(rus.test(ele.val())) {
                jVal.errors = true;
                nameInfo.html('Имя пользователя может содержать только буквы латинского алфавита, ' +
                    'цифры, знак подчёркивания (_) и точку (.)').show();
            } else {
                nameInfo.html('').hide();
            }
        },

        'readAnnexes' : function() {
            var errUnread = $('#errUnread');
            jVal.errors = !!errUnread.css('display', 'block');
        },

        'email' : function() {
            var emailInfo = $('#emailErr');
            var ele = $('#email');
            var emailValid = new RegExp("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$");
            if(!emailValid.test(ele.val())) {
                jVal.errors = true;
                emailInfo.html('Введите Email в формате mymail@example.ru').show();
            } else {
                emailInfo.html('').hide();
            }
        },
        'inn' : function() {
            var innInfo = $('#innErr');
            var rus = new RegExp(".*?^\\d.*?");
            var ele = $('#inn');
            if(ele.val().length < 6 || ele.val().trim() === '' || ele.val().length > 20 || !rus.test(ele.val())) {
                jVal.errors = true;
                innInfo.html('ИНН должен содержать от 6 до 20 цифр').show();
            }else {
                innInfo.html('').hide();
            }
        },
        'account' : function() {
            var accountInfo = $('#accountErr');
            var ele = $('#account');
            var accValid = new RegExp(".*?^\\d.*?");
            if(ele.val().length < 16 || ele.val().trim() === '' || ele.val().length > 30 || !accValid.test(ele.val())) {
                jVal.errors = true;
                accountInfo.html('№ счёта может содержать от 16 до 30 цифр').show();
            } else {
                accountInfo.html('').hide();
            }
        },
        'orgname' : function() {
            var orgnameInfo = $('#orgnameErr');
            var ele = $('#orgname');
            if(ele.val().trim() === '') {
                jVal.errors = true;
                orgnameInfo.html('Название организации не может быть пустым').show();
            } else {
                orgnameInfo.html('').hide();
            }
        },
        'sendIt' : function (){
            if(!jVal.errors) {
                prepareUserSave();
            }
        }
    };

    $('#send').click(function (event){
        event.preventDefault();
        var modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === true){
            jVal.errors = false;
            jVal.login();
            jVal.readAnnexes();
            /* jVal.email(); */
            if($('#stuffs').find(':selected').text() === 'Арендатор') {
                jVal.inn();
                jVal.account();
                jVal.orgname();
            }

        }
        jVal.sendIt();
        return false;
    });

    // bind jVal.fullName function to "Full name" form field
    $('#login').blur(jVal.login);
    /*$('#password').blur(jVal.password);*/
    $('#email').blur(jVal.email);
    $('#inn').blur(jVal.inn);
    $('#account').blur(jVal.account);
    $('#orgname').blur(jVal.orgname);


    $('a#delete').click(function (event) {
        event.preventDefault();
        var userId = $(this).attr('name');
        deleteUser(userId);
        $('#tblUsers').find('tr#' + userId).remove();
    });

    $('#stuffs').change(function () {
        if ($(this).find(':selected').text() === 'Арендатор'){
            $('#pInn').css('display', 'block');
            $('#pAccount').css('display', 'block');
            $('#pOrgName').css('display', 'block');
        }else{
            $('#pInn').css('display', 'none');
            $('#pAccount').css('display', 'none');
            $('#pOrgName').css('display', 'none');
        }

    });

    $('#bth-search').click(function (event) {
        event.preventDefault();
        //searchUsers();
        prepareUsersFilter();
    });
});

function prepareUserSave() {
    var roles = [];
    $('#roles').find(':selected').each(function(i, selected){
        roles.push({
            id: $(selected).val(),
            role: $(selected).text()
        });
    });

    var facilities = [];
    $('#facility').find(':selected').each(function(i, selected){
        facilities.push({id: $(selected).val()});
    });

    var stuffs = $('#stuffs');
    var stuff = {id : stuffs.find(':selected').val(), stuff : stuffs.find(':selected').text()};

    var inn = $('#inn').val();
    var account = $('#account').val();
    var orgName = $('#orgname').val();
    var sChanel = $('#sChanel');
    var partner = {
        id: sChanel.find(':selected').val(),
        login: sChanel.find(':selected').text()
    };

    var kin = $('#kins').val();

    var salesChanel = {
        "partnerId": partner.id,
        "kin": kin
    };

    var user = {
        id : $('#id').val(),
        lastName: $('#last_name').val(),
        first_name: $('#first_name').val(),
        middle_name: $('#middle_name').val(),
        login: $('#login').val(),
        password: /*$('#password').val()*/ null,
        email: $('#email').val(),
        state: $('#state').val(),
        userStuff: stuff,
        roles: roles,
        kin: kin,
        partnerId: salesChanel.partnerId
    };
    saveUser(user, facilities, inn, account, orgName);
}

function saveUser(user, facilities, inn, account, orgName) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"user" : user, "facilityList" : facilities, "inn" : inn, "account" : account, "organization" : orgName});

    showLoader();

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "saveuser",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            closeLoader();
            $('#popup_modal_form').find('#message').append(data.message);
            $('#last_name').val('');
            $('#first_name').val('');
            $('#middle_name').val('');
            $('#login').val('');
            /*$('#password').val('');*/
            $('#email').val('');
            $('#inn').val('');
            $('#account').val('');
            $('#orgname').val('');
            $('#stuffs').prop('selectedIndex', 0);
            $('#facility').prop('selectedIndex', -1);
            $('#mGroups').prop('selectedIndex', 0);
            $('#roles').prop('selectedIndex', -1);

            showPopup();
            closePopup();
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
        }
    });
}

function deleteUser(userId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"rentor" : userId});

    showLoader();

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deleteuser",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            closeLoader();
            $('#popup_modal_form').find('#message').append(data.message);
            closeLoader();
            showPopup();
            closePopup();
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
        }
    });
}

function searchUsers() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"searchStuff" : $("#srchStuff").find("option:selected").val()});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "usersByStuff",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            display(data);
        },
        error : function(e) {
            display(e);
        },
        done : function(e) {
            enableSearchButton(true);
        }
    });

}

function prepareUsersFilter() {
    var stuff = $('#srchStuff').find(':selected').text();

    if(stuff !== 'Все'){
        filters = [];
        apply_filter('#tblUsers tbody', 4, stuff);
    }else{
        filters = [];
        apply_filter('#tblUsers tbody', 4, 'any');
    }
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data) {
    var json = JSON.stringify(data, null, 4);
    $('#tblUsers').html(json);
}