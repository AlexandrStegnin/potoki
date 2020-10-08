let AccountTxDTO = function () {}

AccountTxDTO.prototype = {
    id: null,
    build: function (id) {
        this.id = id
    }
}

let freeCash;

jQuery(document).ready(function ($) {
    freeCash = $('#free-cash');
    freeCash.find('#reinvest').on('click', function (e) {
        e.preventDefault()
        let accountTxDTO = new AccountTxDTO()
        accountTxDTO.build($(this).attr('data-object-id'))
        console.log(accountTxDTO)
    })
})
