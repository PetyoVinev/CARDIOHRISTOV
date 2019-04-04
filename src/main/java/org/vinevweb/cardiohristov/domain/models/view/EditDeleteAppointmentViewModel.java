package org.vinevweb.cardiohristov.domain.models.view;


import java.time.LocalDateTime;

public class EditDeleteAppointmentViewModel {


    private String id;

    private String appointmentName;

    private String appointmentPhone;

    private String appointmentEmail;

    private String appointmentDate;

    private String appointmentTime;

    private LocalDateTime datetime;

    private String appointmentMessage;

    public EditDeleteAppointmentViewModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate() {

        String[] appointmentDateTockens  = this.getDatetime().toLocalDate().toString().split("-");
        String appointmentDate = appointmentDateTockens[2] + "/" + appointmentDateTockens[1] + "/" + appointmentDateTockens[0];

                this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime() {
        this.appointmentTime = this.getDatetime().toLocalTime().toString();;
    }

    public LocalDateTime getDatetime() {
        return datetime;
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
}