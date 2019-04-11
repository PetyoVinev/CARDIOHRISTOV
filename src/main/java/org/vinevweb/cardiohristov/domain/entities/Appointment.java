package org.vinevweb.cardiohristov.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    private String appointmentName;

    private String appointmentPhone;

    private String appointmentEmail;

    private LocalDateTime datetime;

    private String appointmentMessage;

    public Appointment() {
    }

    @Column(name = "appointmentName", nullable = false)
    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    @Column(name = "appointmentPhone", nullable = false)
    public String getAppointmentPhone() {
        return appointmentPhone;
    }

    public void setAppointmentPhone(String appointmentPhone) {
        this.appointmentPhone = appointmentPhone;
    }

    @Column(name = "email", nullable = false)
    public String getAppointmentEmail() {
        return appointmentEmail;
    }

    public void setAppointmentEmail(String appointmentEmail) {
        this.appointmentEmail = appointmentEmail;
    }

    @Column(name = "datetime", nullable = false)
    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    @Column(name = "appointmentMessage", columnDefinition = "TEXT")
    public String getAppointmentMessage() {
        return appointmentMessage;
    }

    public void setAppointmentMessage(String appointmentMessage) {
        this.appointmentMessage = appointmentMessage;
    }
}