$(function () {

    $('#role').change(function () {
        var userEmail = document.getElementById("emailRegister").value;
        var role =  $("#role option:selected").text();
        let _csrf  = $('input[name=_csrf]').val();

        var data = {
            email: userEmail,
            role: role,
            _csrf:_csrf
        };


        $.ajax({
            type : 'POST',
            contentType: 'application/json',
            url: 'http://localhost:8000/profile/roleEdit',
            headers: {
                'X-CSRF-Token': _csrf
            },
            data: data,
            success: function (success) {
                console.log("success");
            },
            error: function (error) {
                console.log(error);
            },
            done: function () {
                console.log("Done.");
            }
        });
    });
});