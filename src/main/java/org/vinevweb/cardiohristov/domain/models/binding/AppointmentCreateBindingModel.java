package org.vinevweb.cardiohristov.domain.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentCreateBindingModel {

    private String id;

    private String appointmentName;

    private String appointmentPhone;

    private String appointmentEmail;

    private String appointmentDate;

    private String appointmentTime;

    private LocalDateTime datetime;

    private String appointmentMessage;


    public AppointmentCreateBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @NotNull(message = "Полето за име и фамилия не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за име и фамилия не може да е празно.")
    @Pattern(regexp = "^[A-Z][a-zA-Z]+ [A-Z][a-zA-Z]+$", message = "Името и фамилията трябва да започват с главна буква, да са изписани с латински букви и да са разделени с интервал")
    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    @NotNull(message = "Телефонният номер не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за телефонен номер не може да е празно.")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Невалиден телефонен номер.")
    public String getAppointmentPhone() {
        return appointmentPhone;
    }

    public void setAppointmentPhone(String appointmentPhone) {
        this.appointmentPhone = appointmentPhone;
    }

    public String getAppointmentEmail() {
        return appointmentEmail;
    }

    public void setAppointmentEmail(String appointmentEmail) {
        this.appointmentEmail = appointmentEmail;
    }

    @NotNull(message = "Полето за дата не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за дата не може да е празно.")
    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @NotNull(message = "Полето за време не може да е с нулева стойност.")
    @NotEmpty(message = "Полето за време не може да е празно.")
    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getAppointmentMessage() {
        return appointmentMessage;
    }

    public void setAppointmentMessage(String appointmentMessage) {
        this.appointmentMessage = appointmentMessage;
    }

    public LocalDateTime getDatetime() {

        return this.datetime;
    }

    public void setDatetime() {

        String day  = this.appointmentDate.substring(0, appointmentDate.indexOf("/"));
        String month  = appointmentDate.substring(3,5);
        String year;
        if (appointmentDate.length() > 10){
            year  = appointmentDate.substring(6, appointmentDate.indexOf(" ") );
        }else {
            year  = appointmentDate.substring(6, appointmentDate.length() );
        }

        String dateAndTimeString =  year + "-" + month + "-" + day + " " + this.appointmentTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.datetime   = LocalDateTime.parse(dateAndTimeString, formatter);

    }
}