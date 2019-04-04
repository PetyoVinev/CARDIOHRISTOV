package org.vinevweb.cardiohristov.services;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;

import java.util.List;

public interface AppointmentService {
    String createAppointment(AppointmentServiceModel appointmentServiceModel);

    List<AppointmentServiceModel> getAllByDateTimeAsc();

    AppointmentServiceModel findById(String id);

    boolean findByDateTime(String date, String time);


    String[][] getBuzyHoursForDate(String date);


    void deleteAppointment(AppointmentServiceModel appointmentServiceModel);

    void annulAppointment(String id);


}
