

(function($) {


    'use strict';


    /*Modal management*/
  /*  $(document.body).on('hide.bs.modal,hidden.bs.modal', function () {
        $('body').css('padding-right','0');
    });*/

    $(document).ready(function(){

        var path = window.location.pathname;
        var page = path.split("/").pop();


        var emailErrorText = document.getElementById("smallEmailErrorText");
        var passErrorText = document.getElementById("smallPasswordErrorText");
        var confPassErrorText = document.getElementById("smallConfirmPasswordErrorText");
        var firstNameErrorText = document.getElementById("smallFirstNameErrorText");
        var lastNameErrorText = document.getElementById("smallLastNameErrorText");
        var emailRegisterField = document.getElementById("emailRegister");
        var url = window.location.href;

            if (emailErrorText != null || passErrorText != null || confPassErrorText != null || firstNameErrorText != null || lastNameErrorText != null ){
                $('#registerModal').modal('toggle');
            } else if (emailRegisterField != null && emailRegisterField.value != "") {
                $('#loginModal').modal('toggle');
                document.getElementById("loginModalLabel").innerHTML  = 'Успешно се регистрирахте, може да влезете с профила си в сайта:'

            }

            if(url.indexOf('/?incorrectCredentials=true') != -1 ) {
                $('#loginModal').modal('show');
                document.getElementById("loginModalLabel").style.color = 'red';
                document.getElementById("loginModalLabel").innerHTML  = 'Невалиден имейл адрес или парола!'  + '<br />' + 'Опитайте отново:';
            }




    });
    /* !---Modal management--! */



    $(function() {
        $.validator.addMethod(
            "regex",
            function(value, element, regexp) {
                var re = new RegExp(regexp);
                return this.optional(element) || re.test(value);
            },
            "Телефонният Ви номер трябва да съдържа 10 цифри или кода на страната последван от 8-10 цифри!"
        );
        $.validator.addMethod(
            "regex_name",
            function(value, element, regexp) {
                var re = new RegExp(regexp);
                return this.optional(element) || re.test(value);
            },
            "Името и фамилията трябва да започват с главна буква, да са изписани с латински букви и да са разделени с интервал!"
        );

        $("#newModalForm").validate({
            rules: {
                appointmentName: {
                    required: true,
                    regex_name: /^[A-Z][a-zA-Z]+ [A-Z][a-zA-Z]+$/
                },
                appointmentPhone: {
                    required: true,
                    regex: /^(\+\d{1,3}[- ]?)?\d{10}$/
                },
                appointmentDate: {
                    required: true
                },
                appointmentTime: {
                    required: true
                },
                action: "required"
            },
            messages: {
                appointmentName: {
                    required: "Попълването на полето е задължително!",
                    minlength: "Името и фамилията Ви трябва да съдържат поне 8 символа!"
                },
                appointmentPhone: {
                    required: "Попълването на полето е задължително!",
                },
                appointmentDate: {
                    required: "Попълването на полето е задължително!"
                },
                appointmentTime: {
                    required: "Попълването на полето е задължително!"
                },
                action: "Попълването на полето е задължително!"
            }
        });
    });

    $('#appointmentDate').datetimepicker({
       /* language: 'bg',
        'format': 'dd/mm/yyyy DD',
        'autoclose': true,
        weekStart: 1,
        daysOfWeekDisabled: [0, 1, 3, 5, 6],
        startDate: '+0d',
        disableTouchKeyboard: true,
        toggleActive: true,
        allowInputToggle: true  ,
        ignoreReadonly: true*/

       locale: 'bg',
        useCurrent: false,
         minDate: moment().add(+0, 'day'),
        format: 'DD/MM/YYYY dddd',
        daysOfWeekDisabled: [0, 1, 3, 5, 6],
        ignoreReadonly: true,
        allowInputToggle: true
     /*   disableTouchKeyboard: true,
        toggleActive: true,*/

    }).on('dp.change', function(e) {

        getBuzyHoursForDate();



    }) ;

    $.fn.menumaker = function(options) {

        var cssmenu = $(this), settings = $.extend({
            title: "Menu",
            format: "dropdown",
            sticky: false
        }, options);

        return this.each(function() {
            cssmenu.prepend('<div id="menu-button">' + settings.title + '</div>');
            $(this).find("#menu-button").on('click', function(){
                $(this).toggleClass('menu-opened');
                var mainmenu = $(this).next('ul');
                if (mainmenu.hasClass('open')) {
                    mainmenu.hide().removeClass('open');
                }
                else {
                    mainmenu.show().addClass('open');
                    if (settings.format === "dropdown") {
                        mainmenu.find('ul').show();
                    }
                }
            });

            cssmenu.find('li ul').parent().addClass('has-sub');

            multiTg = function() {
                cssmenu.find(".has-sub").prepend('<span class="submenu-button"></span>');
                cssmenu.find('.submenu-button').on('click', function() {
                    $(this).toggleClass('submenu-opened');
                    if ($(this).siblings('ul').hasClass('open')) {
                        $(this).siblings('ul').removeClass('open').hide();
                    }
                    else {
                        $(this).siblings('ul').addClass('open').show();
                    }
                });
            };

            if (settings.format === 'multitoggle') multiTg();
            else cssmenu.addClass('dropdown');

            if (settings.sticky === true) cssmenu.css('position', 'fixed');

            resizeFix = function() {
                if ($( window ).width() > 768) {
                    cssmenu.find('ul').show();
                }

                if ($(window).width() <= 768) {
                    cssmenu.find('ul').hide().removeClass('open');
                }
            };
            resizeFix();
            return $(window).on('resize', resizeFix);

        });
    };

    $(document).ready(function(){
        $("#mytable #checkall").click(function () {
            if ($("#mytable #checkall").is(':checked')) {
                $("#mytable input[type=checkbox]").each(function () {
                    $(this).prop("checked", true);
                });

            } else {
                $("#mytable input[type=checkbox]").each(function () {
                    $(this).prop("checked", false);
                });
            }
        });

        $("[data-toggle=tooltip]").tooltip();
    });



})(jQuery);
