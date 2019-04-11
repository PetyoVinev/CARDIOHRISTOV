package org.vinevweb.cardiohristov.integration;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.vinevweb.cardiohristov.domain.models.binding.AppointmentCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.EditDeleteAppointmentViewModel;
import org.vinevweb.cardiohristov.repositories.AppointmentRepository;
import org.vinevweb.cardiohristov.services.AppointmentServiceImpl;
import static org.vinevweb.cardiohristov.Constants.*;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AppointmentControllerTest {

    private static final String APPOINTMENTS_CREATE = "/appointments/create";
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName(UTF_8)
    );
    private static final String APPOINTMENTS_ALL_ROUTE = "/appointments/all";
    private static final String APPOINTMENTS_ATTRIBUTE_NAME = "appointments";
    private static final String APPOINTMENTS_ANNUL_ROUTE = "/appointments/annul";
    private static final String APPOINTMENTS_DELETE_ROUTE = "/appointments/delete";
    private static final String REDIRECT_APPOINTMENTS_ALL_ROUTE = "redirect:/appointments/all";
    private static final String FAKE_DATE = "23/04/2019 вторник";
    private static final String FAKE_HOUR = "08:30";
    private static final String APPOINTMENTS_UPDATE_ROUTE = "/appointments/update";
    private static final String APPOINTMENTS_API_CHECK_SPECIFIC_HOUR = "/appointments/api/checkSpecificHour";
    private static final String APPOINTMENTS_API_GET_BUZY_HOURS_FOR_DATE = "/appointments/api/getBuzyHoursForDate";
    private static final String APPOINTMENT_NAME = "Boko Boko";
    private static final String APPOINTMENT_PHONE = "1212121212";
    private static final String APPOINTMENT_EMAIL = "bokos@abv.bg";
    private static final String APPOINTMENT_DATETIME = "2019-04-16T09:00";
    private static final String APPOINTMENT_MESSAGE = "asd";
    private static final String APPOINTMENT_ID = "1234";
    private static final String APPOINTMENTS_API_GET_APPOINTMENT_BY_ID = "/appointments/api/getAppointmentById";
    private static final String APPOINTMENT_CREATE_BINDING_MODEL = "appointmentCreateBindingModel";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AppointmentServiceImpl appointmentService;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private UserRegisterBindingModel userRegisterBindingModel;

    @Autowired
    private Gson gson;

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void emptyDB() throws Exception {
        this.appointmentRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getAll_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get(APPOINTMENTS_ALL_ROUTE).with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get(APPOINTMENTS_ALL_ROUTE).with(csrf()))
                .andExpect(model().attributeExists(APPOINTMENTS_ATTRIBUTE_NAME, PROCEDURES_ATTRIBUTE_NAME));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getOnCreate_RedirectsToIndex() throws Exception {

        this.mvc
                .perform(get(APPOINTMENTS_CREATE)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_INDEX_ROUTE));

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getOnAnnul_CallAnnulOnServiceAndRedirectsToIndex() throws Exception {

        this.mvc
                .perform(get(APPOINTMENTS_ANNUL_ROUTE)
                        .param("id", "id")
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_INDEX_ROUTE));

        verify(appointmentService).annulAppointment(any());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnDelete_CallDeleteOnServiceAndRedirectsToAll() throws Exception {
        this.mvc
                .perform(post(APPOINTMENTS_DELETE_ROUTE)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_APPOINTMENTS_ALL_ROUTE));
        verify(appointmentService).deleteAppointment(any());

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnUpdate_CallUpdateOnServiceAndRedirectsToAll() throws Exception {
        AppointmentCreateBindingModel appointmentCreateBindingModel = new AppointmentCreateBindingModel();
        appointmentCreateBindingModel.setAppointmentDate(FAKE_DATE);
        appointmentCreateBindingModel.setAppointmentTime(FAKE_HOUR);
        this.mvc
                .perform(post(APPOINTMENTS_UPDATE_ROUTE)
                        .flashAttr(APPOINTMENT_CREATE_BINDING_MODEL, appointmentCreateBindingModel)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_APPOINTMENTS_ALL_ROUTE));
        verify(appointmentService).updateAppointment(any());

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void checkAvailableDateTime_CallService() throws Exception {

        this.mvc
                .perform(get(APPOINTMENTS_API_CHECK_SPECIFIC_HOUR)
                        .param("date", FAKE_DATE)
                        .param("time", FAKE_HOUR)
                        .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        verify(appointmentService).findByDateTime(FAKE_DATE,FAKE_HOUR );

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getBusyHoursForDate_CallServiceWithDateAndReturnResult() throws Exception {

        String[][] expected = new String[1][1];
        expected[0][0]= FAKE_HOUR;


        when(appointmentService.getBuzyHoursForDate(FAKE_DATE)).thenReturn(expected);


       String result = this.mvc
                .perform(get(APPOINTMENTS_API_GET_BUZY_HOURS_FOR_DATE)
                        .param("date", FAKE_DATE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andReturn().getResponse().getContentAsString();

        String[][] actual = this.gson.fromJson(result, String[][] .class);

        assertEquals(expected[0][0], actual[0][0]);
        verify(appointmentService).getBuzyHoursForDate(FAKE_DATE );

    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void editDeleteAppointment_ReturnsCorrectViewModel() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

      AppointmentServiceModel appointmentServiceModel = new AppointmentServiceModel();
      appointmentServiceModel.setId(null);
        appointmentServiceModel.setAppointmentName(APPOINTMENT_NAME);
        appointmentServiceModel.setAppointmentPhone(APPOINTMENT_PHONE);
        appointmentServiceModel.setAppointmentEmail(APPOINTMENT_EMAIL);
        appointmentServiceModel.setDatetime(LocalDateTime.parse(APPOINTMENT_DATETIME, formatter));
        appointmentServiceModel.setAppointmentMessage(APPOINTMENT_MESSAGE);


        EditDeleteAppointmentViewModel expectedModel =  this.modelMapper.map(appointmentServiceModel,
                EditDeleteAppointmentViewModel.class);
        expectedModel.setAppointmentTime();
        expectedModel.setAppointmentDate();


        when(appointmentService.findById(APPOINTMENT_ID)).thenReturn(appointmentServiceModel);


        String result = this.mvc
                .perform(get(APPOINTMENTS_API_GET_APPOINTMENT_BY_ID)
                        .param("id", APPOINTMENT_ID)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        assertTrue(result.contains(expectedModel.getAppointmentName()));
        assertTrue(result.contains(expectedModel.getAppointmentEmail()));
        assertTrue(result.contains(expectedModel.getAppointmentMessage()));
        assertTrue(result.contains(expectedModel.getAppointmentPhone()));
        assertTrue(result.contains(expectedModel.getAppointmentDate()));
        assertTrue(result.contains(expectedModel.getAppointmentTime()));
        assertTrue(result.contains(expectedModel.getDatetime().format(formatter)));


        verify(appointmentService).findById(APPOINTMENT_ID );

    }


}
