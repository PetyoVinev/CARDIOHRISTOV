package org.vinevweb.cardiohristov.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Appointment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;
import org.vinevweb.cardiohristov.repositories.AppointmentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.vinevweb.cardiohristov.common.Constants.*;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final ModelMapper modelMapper;

    private final LogService logService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ModelMapper modelMapper, LogService logService) {
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
        this.logService = logService;
    }



    @Override
    public boolean findByDateTime(String date, String time) {

        String day  = date.substring(0, date.indexOf("/"));
        String month  = date.substring(3,5);
        String year  = date.substring(6, date.indexOf(" ") );
        String dateAndTimeString =  year + "-" + month + "-" + day + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(APPOINTMENT_DATETIME_FORMAT);
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTimeString, formatter);

        Appointment appointment = this.appointmentRepository.findAppointmentByDatetime(dateTime);
        return appointment == null;
    }

    @Override
    public String[][] getBuzyHoursForDate(String date) {
        String day  = date.substring(0, date.indexOf("/"));
        String month  = date.substring(3,5);
        String year;
        if (date.length() > 10){
             year  = date.substring(6, date.indexOf(" ") );
        }else {
            year  = date.substring(6, date.length() );
        }
        String dateAndTimeStartString =  year + "-" + month + "-" + day + " " + START_TIME;
        String dateAndTimeEndString =  year + "-" + month + "-" + day + " " + END_TIME;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(APPOINTMENT_DATETIME_FORMAT);
        LocalDateTime dateTimeStart = LocalDateTime.parse(dateAndTimeStartString, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(dateAndTimeEndString, formatter);

        List<Appointment>  appointments = this.appointmentRepository.findAppointmentsByDatetimeBetween(dateTimeStart,dateTimeEnd);
        String[][] appointmentPairs = new String[appointments.size()][];

        for (int i = 0; i < appointments.size(); i++) {
            LocalDateTime appointmentStartTime = appointments.get(i).getDatetime();
            LocalDateTime appointmentEndTime = appointments.get(i).getDatetime().plusMinutes(30);

            String appointmentStartTimeString = String.valueOf((appointmentStartTime.getHour()<10?"0":"") + appointmentStartTime.getHour()) +
                    ":" + String.valueOf((appointmentStartTime.getMinute()<10?"0":"") + appointmentStartTime.getMinute());
            String appointmentEndTimeString= String.valueOf((appointmentEndTime.getHour()<10?"0":"") + appointmentEndTime.getHour()) +
                    ":" + String.valueOf((appointmentEndTime.getMinute()<10?"0":"") + appointmentEndTime.getMinute());


            String[] timePair = new String[2];
            timePair[0] = appointmentStartTimeString;
            timePair[1] = appointmentEndTimeString;
            appointmentPairs[i] = timePair;
        }


        return appointmentPairs;
    }

    @Override
    public void deleteAppointment(AppointmentServiceModel appointmentServiceModel) {
        String appName = appointmentRepository.getOne(appointmentServiceModel.getId()).getAppointmentName();
        String appDateTime = this.formatDate(appointmentRepository.getOne(appointmentServiceModel.getId()).getDatetime());
        this.appointmentRepository.deleteById(appointmentServiceModel.getId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                String.format(APPOINTMENT_HAS_BEEN_DELETED,
                        appName,
                        appDateTime)
                 });
    }

    @Override
    public void annulAppointment(String id) {
        this.appointmentRepository.deleteById(id);
    }

    @Override
    public String createAppointment(AppointmentServiceModel appointmentServiceModel) {
        Appointment appointment = this.modelMapper.map(appointmentServiceModel, Appointment.class);
            Appointment createdAppointment = this.appointmentRepository.saveAndFlush(appointment);
            return createdAppointment.getId();

    }

    @Override
    public String updateAppointment(AppointmentServiceModel appointmentServiceModel) {
        Appointment appointment = this.modelMapper.map(appointmentServiceModel, Appointment.class);
        Appointment createdAppointment = this.appointmentRepository.saveAndFlush(appointment);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                String.format(APPOINTMENT_HAS_BEEN_EDITED,
                        appointmentServiceModel.getAppointmentName(),
                        this.formatDate(appointmentServiceModel.getDatetime()))
        });


        return createdAppointment.getId();

    }

    @Override
    public List<AppointmentServiceModel> getAllByDateTimeAsc() {
        return this.appointmentRepository
                .findAllByOrderByDatetimeAsc()
                .stream()
                .map(x -> this.modelMapper.map(x, AppointmentServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }



    @Override
    public AppointmentServiceModel findById(String id) {
        return modelMapper.map(appointmentRepository.findById(id).orElse(null), AppointmentServiceModel.class);
    }

    @Scheduled(cron = CRON_VALUE)
    public void cleanScheduleFromOldAppointments() {
        for (Appointment appointment : this.appointmentRepository.findAppointmentsByDatetimeBefore(LocalDateTime.now())) {
            appointmentRepository.delete(appointment);
        }
    }


    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(APPOINTMENT_DATETIME_FORMAT);

        return dateTime.format(formatter);
    }


}
