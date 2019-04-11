package org.vinevweb.cardiohristov.integration;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.binding.AppointmentCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.ProfileDeleteBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.AppointmentServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserRoleServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.EditDeleteAppointmentViewModel;
import org.vinevweb.cardiohristov.domain.models.view.UserViewModel;
import org.vinevweb.cardiohristov.repositories.AppointmentRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.services.AppointmentServiceImpl;
import org.vinevweb.cardiohristov.services.LogServiceImpl;
import org.vinevweb.cardiohristov.services.ProcedureServiceImpl;
import org.vinevweb.cardiohristov.services.user.UserService;
import org.vinevweb.cardiohristov.services.user.UserServiceImpl;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
public class AdminControllerTest {

    private User fakeUser;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    private static final MediaType APPLICATION_TEXT_UTF8 = new MediaType(
            MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8")
    );

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private LogServiceImpl logService;

    @MockBean
    private ProcedureServiceImpl procedureService;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private UserRegisterBindingModel userRegisterBindingModel;

    @Autowired
    private Gson gson;

    @Autowired
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Before
    public void emptyDB() throws Exception {
        this.userRepository.deleteAll();

        when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("fakeHash");
        this.fakeUser = new User();
        fakeUser.setId("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
        fakeUser.setFirstName("Fake");
        fakeUser.setLastName("Fakes");
        fakeUser.setUsername("fake@fake.bg");
        fakeUser.setPassword(this.bCryptPasswordEncoder.encode("1111"));
        fakeUser.setAuthorities(new HashSet<>());
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void allProfiles_returnsCorrectView() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName("Fake");
        userRegisterBindingModel.setLastName("Fakes");
        userRegisterBindingModel.setEmailRegister("fake@fake.bg");
        userRegisterBindingModel.setPasswordRegister("1111");
        userRegisterBindingModel.setConfirmPassword("1111");

        this.mvc
                .perform(get("/profiles")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel)
                        .with(csrf()))
                .andExpect(view().name("fragments/base-layout"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void allProfiles_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/profiles").with(csrf()))
                .andExpect(model().attributeExists("profiles", "procedures"));
        verify(procedureService).getAllByDateAsc();
        verify(userService).extractAllUsersOrderedAlphabetically();
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getProfile_ReturnsCorrectViewModel() throws Exception {

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setId("0c39a7b4-b039-4d78-a0f8-333be7b0e718");
        userServiceModel.setFirstName("Fake");
        userServiceModel.setLastName("Fakes");
        userServiceModel.setEmail("fake@fake.bg");
        userServiceModel.setPasswordRegister(this.bCryptPasswordEncoder.encode("1111"));
        UserRoleServiceModel userRoleServiceModel = new UserRoleServiceModel();
        userRoleServiceModel.setAuthority("ADMIN");
        List<UserRoleServiceModel> roles = new ArrayList< >();
        roles.add(userRoleServiceModel);
        userServiceModel.setAuthorities(roles);


        UserViewModel userViewModel = this.modelMapper.map(userServiceModel, UserViewModel.class);
        userViewModel.setRoles(userServiceModel.getAuthorities().stream().map(UserRoleServiceModel::getAuthority).collect(Collectors.toList()));


        when(userService.extractUserById("0c39a7b4-b039-4d78-a0f8-333be7b0e718")).thenReturn(userServiceModel);


        String result = this.mvc
                .perform(get("/api/profile{id}","id", "0c39a7b4-b039-4d78-a0f8-333be7b0e718")
                .param("id", "0c39a7b4-b039-4d78-a0f8-333be7b0e718"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        assertTrue(result.contains(userViewModel.getEmail()));
        assertTrue(result.contains(userViewModel.getFirstName()));
        assertTrue(result.contains(userViewModel.getId()));
        assertTrue(result.contains(userViewModel.getLastName()));
        assertTrue(result.contains(userViewModel.getRoles().get(0)));


        verify(userService).extractUserById("0c39a7b4-b039-4d78-a0f8-333be7b0e718");

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnEdit_CallEditOnServiceAndRedirectsToAll() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName("Fake");
        userRegisterBindingModel.setLastName("Fakes");
        userRegisterBindingModel.setEmailRegister("fake@fake.bg");
        userRegisterBindingModel.setPasswordRegister("1111");
        userRegisterBindingModel.setConfirmPassword("1111");


        when(userService.extractUserByEmail("fake@fake.bg")).thenReturn(new UserServiceModel());
        when(userService.editUser(any())).thenReturn(true);

        when(bindingResult.hasErrors()).thenReturn(true);

        this.mvc
                .perform(post("/edit")
                        .flashAttr("userRegisterBindingModel", userRegisterBindingModel)
                        .flashAttr("bindingResult", this.bindingResult)
                        .with(csrf()))
                .andExpect(view().name("redirect:/profiles"));

        verify(userService).extractUserByEmail("fake@fake.bg");
        verify(userService).editUser(any());

    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void editRole_CallServiceWithCorrectDataAndReturnSuccces() throws Exception {

        String body = "email=pesho%40abv.bg&role=Moderator";

        when(userService.editUserRole("pesho@abv.bg", "Moderator")).thenReturn(true);


        String result = this.mvc
                .perform(post("/profile/roleEdit")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(body.getBytes())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_TEXT_UTF8))
                .andReturn().getResponse().getContentAsString();

        assertEquals("Success", result);

        verify(userService).editUserRole("pesho@abv.bg", "Moderator");

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void editRoleWithInvalidUser_ThrowsException() throws Exception {

        String body = "email=pesho%40abv.bg&role=Moderator";

        when(userService.editUserRole("pesho@abv.bg", "Moderator")).thenReturn(false);


        this.mvc
                .perform(post("/profile/roleEdit")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(body.getBytes())
                        .with(csrf()))
                .andExpect(view().name("fragments/base-layout"));


        verify(userService).editUserRole("pesho@abv.bg", "Moderator");

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnDelete_CallDeleteOnServiceAndRedirectsToAll() throws Exception {
        ProfileDeleteBindingModel profileDeleteBindingModel = new ProfileDeleteBindingModel();
        profileDeleteBindingModel.setId("0c39a7b4-b039-4d78-a0f8-333be7b0e718");


        this.mvc
                .perform(post("/delete")
                        .flashAttr("profileDeleteBindingModel", profileDeleteBindingModel)
                        .with(csrf()))
                .andExpect(view().name("redirect:/profiles"));

        verify(userService).deleteProfile(any());

    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void logs_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/logs").with(csrf()))
                .andExpect(view().name("fragments/base-layout"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void logs_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/logs").with(csrf()))
                .andExpect(model().attributeExists("logViewModel"));

        verify(logService).getLogsOrderedByDate();
    }






}
