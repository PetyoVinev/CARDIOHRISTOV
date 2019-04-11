package org.vinevweb.cardiohristov.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.vinevweb.cardiohristov.domain.entities.Appointment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;
import org.vinevweb.cardiohristov.repositories.AppointmentRepository;
import org.vinevweb.cardiohristov.services.AppointmentServiceImpl;
import org.vinevweb.cardiohristov.services.LogServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class AppointmentServiceTests {

    private static final String APPOINTMENT_ID = "1234";
    private static final String APPOINTMENT_FIRST_NAME = "Boko";
    private static final String APPOINTMENT_FAKE_DATETIME = "2019-04-16T08:00";
    private static final String FAKE_DATE_2 = "16/04/2019 вторник";
    private static final String APPOINTMENT_FAKE_DATETIME_2 = "2019-04-16T08:30";
    private static final String FAKE_HOUR_8 = "08:00";
    private static final String FAKE_HOUR_8_30 = "08:30";
    private static final String FAKE_HOUR_9 = "09:00";
    private static final String FAKE_DATIME_3 = "2019-04-23T08:30";
    private static final String FAKE_DATE_3 = "23/04/2019 вторник";

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LogServiceImpl logService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    public void findByIdReturnsCorrectEntity() {
        AppointmentServiceModel appointmentServiceModel = new AppointmentServiceModel();
        appointmentServiceModel.setId(APPOINTMENT_ID);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(appointmentServiceModel);
        String result = appointmentService.findById(APPOINTMENT_ID).getId();
        Assert.assertEquals(APPOINTMENT_ID, result);

    }

    @Test
    public void getAllByDateTimeReturnsCorrect() {
        this.appointmentService.getAllByDateTimeAsc();
        verify(appointmentRepository)
                .findAllByOrderByDatetimeAsc();

    }


    @Test
    public void createAppointmentWorksCorrect() {
        AppointmentServiceModel appointmentServiceModel = new AppointmentServiceModel();
        appointmentServiceModel.setId(APPOINTMENT_ID);

        Appointment appointment = new Appointment();
        appointment.setId(APPOINTMENT_ID);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(appointment);
        Mockito.when(this.appointmentRepository.saveAndFlush(Mockito.any())).thenReturn(appointment);
        String result = appointmentService.createAppointment(appointmentServiceModel);
        Assert.assertEquals(APPOINTMENT_ID, result);

    }

    @Test
    public void annulAppointmentRemovesItFromDb() {
        this.appointmentService.annulAppointment(APPOINTMENT_ID);
        verify(appointmentRepository)
                .deleteById(APPOINTMENT_ID);

    }

    @Test
    public void deleteAppointmentRemovesItFromDbAndCreateLog() {
        AppointmentServiceModel appointmentServiceModel = new AppointmentServiceModel();
        appointmentServiceModel.setId(APPOINTMENT_ID);

        Appointment appointment = new Appointment();
        appointment.setId(APPOINTMENT_ID);
        appointment.setAppointmentName(APPOINTMENT_FIRST_NAME);
        appointment.setDatetime(LocalDateTime.now());

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(new User());

        Mockito.when(this.appointmentRepository.getOne(Mockito.any())).thenReturn(appointment);
        this.appointmentService.deleteAppointment(appointmentServiceModel);

        verify(appointmentRepository)
                .deleteById(APPOINTMENT_ID);
        verify(logService)
                .addEvent(any());

    }
    @Test
    public void updateAppointmentUpdateItInDbAndCreateLog() {
        AppointmentServiceModel appointmentServiceModel = new AppointmentServiceModel();
        appointmentServiceModel.setId(APPOINTMENT_ID);
        appointmentServiceModel.setAppointmentName(APPOINTMENT_FIRST_NAME);
        appointmentServiceModel.setDatetime(LocalDateTime.now());

        Appointment appointment = new Appointment();
        appointment.setId(APPOINTMENT_ID);


        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(new User());

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(appointment);
        Mockito.when(this.appointmentRepository.saveAndFlush(Mockito.any())).thenReturn(appointment);


       String result =  this.appointmentService.updateAppointment(appointmentServiceModel);
       Assert.assertEquals(APPOINTMENT_ID, result);
        verify(appointmentRepository)
                .saveAndFlush(appointment);
        verify(logService)
                .addEvent(any());

    }

    @Test
    public void getBuzyHoursForDateReturnsOccupiedHours() {

        String date = FAKE_DATE_2;
        Appointment appointment1 = new Appointment();
        appointment1.setDatetime( LocalDateTime.parse(APPOINTMENT_FAKE_DATETIME));
        Appointment appointment2 = new Appointment();
        appointment2.setDatetime( LocalDateTime.parse(APPOINTMENT_FAKE_DATETIME_2));
        List<Appointment> appointments  = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        Mockito.when(this.appointmentRepository.findAppointmentsByDatetimeBetween(Mockito.any(), Mockito.any())).thenReturn(appointments);


         String[][] result  =   this.appointmentService.getBuzyHoursForDate(date);

        Assert.assertEquals(FAKE_HOUR_8, result[0][0]);
        Assert.assertEquals(FAKE_HOUR_8_30, result[0][1]);
        Assert.assertEquals(FAKE_HOUR_8_30, result[1][0]);
        Assert.assertEquals(FAKE_HOUR_9, result[1][1]);


    }


    @Test
    public void cleanScheduleFromOldAppointmentsWorksCorrect() {

        Appointment appointment1 = new Appointment();
        appointment1.setDatetime( LocalDateTime.parse(APPOINTMENT_FAKE_DATETIME));
        Appointment appointment2 = new Appointment();
        appointment2.setDatetime( LocalDateTime.parse(APPOINTMENT_FAKE_DATETIME_2));
        List<Appointment> appointments  = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        Mockito.when(this.appointmentRepository.findAppointmentsByDatetimeBefore(Mockito.any())).thenReturn(appointments);

        this.appointmentService.cleanScheduleFromOldAppointments();

        verify(appointmentRepository)
                .delete(appointment1);
        verify(appointmentRepository)
                .delete(appointment2);


    }

    @Test
    public void findByDateTimeWorksCorrect() {

        this.appointmentService.findByDateTime(FAKE_DATE_3, FAKE_HOUR_8_30);

        verify(appointmentRepository)
                .findAppointmentByDatetime(LocalDateTime.parse(FAKE_DATIME_3));

    }

}
