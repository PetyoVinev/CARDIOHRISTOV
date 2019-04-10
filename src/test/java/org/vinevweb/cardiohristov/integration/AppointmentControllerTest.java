package org.vinevweb.cardiohristov.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.vinevweb.cardiohristov.domain.models.binding.AppointmentCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.EditDeleteAppointmentViewModel;
import org.vinevweb.cardiohristov.repositories.AppointmentRepository;
import org.vinevweb.cardiohristov.repositories.ProcedureRepository;
import org.vinevweb.cardiohristov.services.AppointmentService;
import org.vinevweb.cardiohristov.services.AppointmentServiceImpl;
import org.vinevweb.cardiohristov.services.ProcedureService;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
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

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

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

    @Before
    public void emptyDB() throws Exception {
        this.appointmentRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getAll_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/appointments/all").with(csrf()))
                .andExpect(view().name("fragments/base-layout"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/appointments/all").with(csrf()))
                .andExpect(model().attributeExists("appointments", "procedures"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getOnCreate_RedirectsToIndex() throws Exception {

        this.mvc
                .perform(get("/appointments/create")
                        .with(csrf()))
                .andExpect(view().name("redirect:/"));

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getOnAnnul_CallAnnulOnServiceAndRedirectsToIndex() throws Exception {

        this.mvc
                .perform(get("/appointments/annul")
                        .param("id", "id")
                        .with(csrf()))
                .andExpect(view().name("redirect:/"));

        verify(appointmentService).annulAppointment(any());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnDelete_CallDeleteOnServiceAndRedirectsToAll() throws Exception {
        this.mvc
                .perform(post("/appointments/delete")
                        .with(csrf()))
                .andExpect(view().name("redirect:/appointments/all"));
        verify(appointmentService).deleteAppointment(any());

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnUpdate_CallUpdateOnServiceAndRedirectsToAll() throws Exception {
        AppointmentCreateBindingModel appointmentCreateBindingModel = new AppointmentCreateBindingModel();
        appointmentCreateBindingModel.setAppointmentDate("23/04/2019 вторник");
        appointmentCreateBindingModel.setAppointmentTime("08:30");
        this.mvc
                .perform(post("/appointments/update")
                        .flashAttr("appointmentCreateBindingModel", appointmentCreateBindingModel)
                        .with(csrf()))
                .andExpect(view().name("redirect:/appointments/all"));
        verify(appointmentService).updateAppointment(any());

    }

    @Test
    public void checkAvailableDateTime_CallService() throws Exception {

        this.mvc
                .perform(get("/appointments/api/checkSpecificHour")
                        .param("date", "23/04/2019 вторник")
                        .param("time", "08:30")
                        .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        verify(appointmentService).findByDateTime("23/04/2019 вторник","08:30" );

    }

    @Test
    public void getBusyHoursForDate_CallServiceWithDateAndReturnResult() throws Exception {

        String[][] expected = new String[1][1];
        expected[0][0]= "8:30";


        when(appointmentService.getBuzyHoursForDate("23/04/2019 вторник")).thenReturn(expected);


       String result = this.mvc
                .perform(get("/appointments/api/getBuzyHoursForDate")
                        .param("date", "23/04/2019 вторник")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andReturn().getResponse().getContentAsString();

        String[][] actual = this.gson.fromJson(result, String[][] .class);

        assertEquals(expected[0][0], actual[0][0]);
        verify(appointmentService).getBuzyHoursForDate("23/04/2019 вторник" );

    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void editDeleteAppointment_ReturnsCorrectViewModel() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        this.gson  =
        new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();

      AppointmentServiceModel appointmentServiceModel = new AppointmentServiceModel();
        appointmentServiceModel.setAppointmentName("Boko Boko");
        appointmentServiceModel.setAppointmentPhone("1212121212");
        appointmentServiceModel.setAppointmentEmail("bokos@abv.bg");
        appointmentServiceModel.setDatetime(LocalDateTime.parse("2019-04-16T09:00", formatter));
        appointmentServiceModel.setAppointmentMessage("asd");


        EditDeleteAppointmentViewModel expected = new EditDeleteAppointmentViewModel();
        expected.setAppointmentName("Boko Boko");
        expected.setAppointmentPhone("1212121212");
        expected.setAppointmentEmail("bokos@abv.bg");
        expected.setDatetime(LocalDateTime.parse("2019-04-16T09:00", formatter));
        expected.setAppointmentMessage("asd");
        expected.setAppointmentDate();
        expected.setAppointmentTime();



        when(appointmentService.findById("1234")).thenReturn(appointmentServiceModel);


        String result = this.mvc
                .perform(get("/appointments/api/getAppointmentById")
                        .param("id", "1234")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        EditDeleteAppointmentViewModel actual = this.gson.fromJson(result, EditDeleteAppointmentViewModel.class);

        assertEquals(expected.getAppointmentMessage(), actual.getAppointmentMessage());
        assertEquals(expected.getDatetime(), actual.getDatetime());
        assertEquals(expected.getAppointmentDate(), actual.getAppointmentDate());
        assertEquals(expected.getAppointmentTime(), actual.getAppointmentTime());
        assertEquals(expected.getAppointmentEmail(), actual.getAppointmentEmail());
        assertEquals(expected.getAppointmentPhone(), actual.getAppointmentPhone());
        assertEquals(expected.getAppointmentName(), actual.getAppointmentName());

        verify(appointmentService).findById("1234" );

    }

    /*@Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnCreate_CallCreateOnServiceAndRedirectsToAll() throws Exception {
        AppointmentCreateBindingModel appointmentCreateBindingModel = new AppointmentCreateBindingModel();
        appointmentCreateBindingModel.setAppointmentDate("23/04/2019 вторник");
        appointmentCreateBindingModel.setAppointmentTime("08:30");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(appointmentService.createAppointment(any())).thenReturn("1234");

        this.mvc
                .perform(post("/appointments/create")
                        .flashAttr("appointmentCreateBindingModel", appointmentCreateBindingModel)
                        .flashAttr("bindingResult" , bindingResult)
                        .flashAttr("userRegisterBindingModel" , userRegisterBindingModel)
                        .with(csrf()));

        verify(appointmentService).createAppointment(any());

    }
*/




}
