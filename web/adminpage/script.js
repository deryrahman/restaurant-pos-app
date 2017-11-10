$(document).ready(function () {

    // User Section //
    var userUri = "http://localhost:8080/peregrine/api/v1/users";

    function printUserTableData() {
        var html = '' +
            '<tr>\n' +
            '    <th>#ID</th>\n' +
            '    <th>Username</th>\n' +
            '    <th>Role</th>\n' +
            '    <th>Email</th>\n' +
            '    <th>Restaurant</th>\n' +
            '</tr>';

        $.getJSON(userUri, function (userJSON) {
            console.log(userJSON);
            userJSON.forEach(function (userItem) {
                html += '' +
                    '<tr>\n' +
                    '    <td>'+ userItem.id +'</td>\n' +
                    '    <td>'+ userItem.name + '</td>\n' +
                    '    <td>'+ userItem.role + '</td>\n' +
                    '    <td>'+ userItem.email + '</td>\n' +
                    '    <td>' +userItem.restaurant_id + '</td>\n' +
                    '</tr>';
            });
            console.log(html);
            $('#user-table').html(html);
        });
    }

    printUserTableData();

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