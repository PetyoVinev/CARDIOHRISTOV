package org.vinevweb.cardiohristov.domain.models.view;

import java.time.LocalDateTime;


public class AllAppointmentViewModel {


    private String id;

    private String appointmentName;

    private String appointmentPhone;

    private String appointmentEmail;

    private LocalDateTime datetime;

    private String appointmentMessage;

    public AllAppointmentViewModel() {
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