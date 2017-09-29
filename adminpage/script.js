$(document).ready(function () {
    $('#show-users').click(function () {
        $('#panel-overview').hide();
        $('#panel-items').hide();
        $('#panel-users').show();

        $('#show-overview').removeClass('active main-bg-color');
        $('#show-items').removeClass('active main-bg-color');
        $('#show-users').addClass('active main-bg-color');

    });

    $('#show-overview').click(function () {
        $('#panel-users').hide();
        $('#panel-items').hide();
        $('#panel-overview').show();

        $('#show-users').removeClass('active main-bg-color');
        $('#show-items').removeClass('active main-bg-color');
        $('#show-overview').addClass('active main-bg-color');
    });

    $('#show-items').click(function () {
        $('#panel-users').hide();
        $('#panel-overview').hide();
        $('#panel-items').show();

        $('#show-users').removeClass('active main-bg-color');
        $('#show-overview').removeClass('active main-bg-color');
        $('#show-items').addClass('active main-bg-color');
    });
});