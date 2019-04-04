$(function () {

    $('#role').change(function () {
        var userEmail = document.getElementById("emailRegister").value;
        var role =  $("#role option:selected").text();

        var data = {
            email: userEmail,
            role: role
        };

        $.ajax({
            type : 'POST',
            contentType: 'application/json',
            url: 'http://localhost:8000/profile/roleEdit',
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