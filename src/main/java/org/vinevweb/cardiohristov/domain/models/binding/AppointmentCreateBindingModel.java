package org.vinevweb.cardiohristov.domain.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.vinevweb.cardiohristov.common.Constants.*;

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


    @NotNull(message = APPOINTMENT_NAME_COULD_NOT_BE_NULL)
    @NotEmpty(message = APPOINTMENT_NAME_COULD_NOT_BE_EMPTY)
    @Pattern(regexp = APPOINTMENT_NAME_PATTERN, message = APPOINTMENT_NAME_REQUIREMENTS)
    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    @NotNull(message = APPOINTMENT_NUMBER_COULD_NOT_BE_NULL)
    @NotEmpty(message = APPOINTMENT_NUMBER_COULD_NOT_BE_EMPTY)
    @Pattern(regexp = APPOINTMENT_NUMBER_PATTERN, message = APPOINTMENT_NUMBER_INVALID_MSG)
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

    @NotNull(message = APPOINTMENT_DATE_COULD_NOT_BE_NULL)
    @NotEmpty(message = APPOINTMENT_DATE_COULD_NOT_BE_EMPTY)
    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @NotNull(message = APPOINTMENT_TIME_COULD_NOT_BE_NULL)
    @NotEmpty(message = APPOINTMENT_TIME_COULD_NOT_BE_EMPTY)
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(APPOINTMENT_DATETIME_FORMAT);
        this.datetime   = LocalDateTime.parse(dateAndTimeString, formatter);

    }
}