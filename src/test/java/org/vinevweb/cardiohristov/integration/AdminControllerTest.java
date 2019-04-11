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
import org.vinevweb.cardiohristov.domain.models.binding.ProfileDeleteBindingModel;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.UserRoleServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.UserViewModel;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.services.LogServiceImpl;
import org.vinevweb.cardiohristov.services.ProcedureServiceImpl;
import org.vinevweb.cardiohristov.services.user.UserServiceImpl;

import java.nio.charset.Charset;
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
import static org.vinevweb.cardiohristov.Constants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminControllerTest {

    private static final String FAKE_HASH = "fakeHash";
    private static final String PROFILES_ATTRIBUTE_NAME = "profiles";
    private static final String PROCEDURES_ATTRIBUTE_NAME = "procedures";
    private static final String EDIT_ROLE_REQUEST_BODY = "email=pesho%40abv.bg&role=Moderator";
    private static final String SUCCESS_MSG = "Success";
    private static final String EDIT_ROLE_MAIL = "pesho@abv.bg";
    private static final String MODERATOR_ROLE = "Moderator";
    private static final String REDIRECT_PROFILES_ROUTE = "redirect:/profiles";
    private static final String LOG_VIEW_MODEL_ATTRIBUTE_NAME = "logViewModel";
    private static final String PROFILES = "/profiles";
    private static final String API_PROFILE_ID = "/api/profile{id}";
    private static final String BINDING_RESULT = "bindingResult";
    private static final String PROFILE_ROLE_EDIT = "/profile/roleEdit";
    private static final String PROFILE_DELETE_BINDING_MODEL = "profileDeleteBindingModel";
    private static final String DELETE_URL = "/delete";
    private static final String LOGS_URL = "/logs";

    private User fakeUser;



    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName(UTF_8)
    );

    private static final MediaType APPLICATION_TEXT_UTF8 = new MediaType(
            MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName(UTF_8)
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

        when(this.bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn(FAKE_HASH);
        this.fakeUser = new User();
        fakeUser.setId(USER_ID);
        fakeUser.setFirstName(USER_FIRST_NAME);
        fakeUser.setLastName(USER_LAST_NAME);
        fakeUser.setUsername(USER_USERNAME);
        fakeUser.setPassword(this.bCryptPasswordEncoder.encode(PASSWORD));
        fakeUser.setAuthorities(new HashSet<>());
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void allProfiles_returnsCorrectView() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName(USER_FIRST_NAME);
        userRegisterBindingModel.setLastName(USER_LAST_NAME);
        userRegisterBindingModel.setEmailRegister(USER_USERNAME);
        userRegisterBindingModel.setPasswordRegister(PASSWORD);
        userRegisterBindingModel.setConfirmPassword(PASSWORD);

        this.mvc
                .perform(get(PROFILES)
                        .flashAttr(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel)
                        .with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void allProfiles_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get(PROFILES).with(csrf()))
                .andExpect(model().attributeExists(PROFILES_ATTRIBUTE_NAME, PROCEDURES_ATTRIBUTE_NAME));
        verify(procedureService).getAllByDateAsc();
        verify(userService).extractAllUsersOrderedAlphabetically();
    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void getProfile_ReturnsCorrectViewModel() throws Exception {

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setId(USER_ID);
        userServiceModel.setFirstName(USER_FIRST_NAME);
        userServiceModel.setLastName(USER_LAST_NAME);
        userServiceModel.setEmail(USER_USERNAME);
        userServiceModel.setPasswordRegister(this.bCryptPasswordEncoder.encode(PASSWORD));
        UserRoleServiceModel userRoleServiceModel = new UserRoleServiceModel();
        userRoleServiceModel.setAuthority("ADMIN");
        List<UserRoleServiceModel> roles = new ArrayList< >();
        roles.add(userRoleServiceModel);
        userServiceModel.setAuthorities(roles);


        UserViewModel userViewModel = this.modelMapper.map(userServiceModel, UserViewModel.class);
        userViewModel.setRoles(userServiceModel.getAuthorities().stream().map(UserRoleServiceModel::getAuthority).collect(Collectors.toList()));


        when(userService.extractUserById(USER_ID)).thenReturn(userServiceModel);


        String result = this.mvc
                .perform(get(API_PROFILE_ID,"id", USER_ID)
                .param("id", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        assertTrue(result.contains(userViewModel.getEmail()));
        assertTrue(result.contains(userViewModel.getFirstName()));
        assertTrue(result.contains(userViewModel.getId()));
        assertTrue(result.contains(userViewModel.getLastName()));
        assertTrue(result.contains(userViewModel.getRoles().get(0)));


        verify(userService).extractUserById(USER_ID);

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnEdit_CallEditOnServiceAndRedirectsToAll() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setFirstName(USER_FIRST_NAME);
        userRegisterBindingModel.setLastName(USER_LAST_NAME);
        userRegisterBindingModel.setEmailRegister(USER_USERNAME);
        userRegisterBindingModel.setPasswordRegister(PASSWORD);
        userRegisterBindingModel.setConfirmPassword(PASSWORD);


        when(userService.extractUserByEmail(USER_USERNAME)).thenReturn(new UserServiceModel());
        when(userService.editUser(any())).thenReturn(true);

        when(bindingResult.hasErrors()).thenReturn(true);

        this.mvc
                .perform(post("/edit")
                        .flashAttr(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel)
                        .flashAttr(BINDING_RESULT, this.bindingResult)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_PROFILES_ROUTE));

        verify(userService).extractUserByEmail(USER_USERNAME);
        verify(userService).editUser(any());

    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void editRole_CallServiceWithCorrectDataAndReturnSuccces() throws Exception {

        String body = EDIT_ROLE_REQUEST_BODY;

        when(userService.editUserRole(EDIT_ROLE_MAIL, MODERATOR_ROLE)).thenReturn(true);


        String result = this.mvc
                .perform(post(PROFILE_ROLE_EDIT)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(body.getBytes())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_TEXT_UTF8))
                .andReturn().getResponse().getContentAsString();

        assertEquals(SUCCESS_MSG, result);

        verify(userService).editUserRole(EDIT_ROLE_MAIL, MODERATOR_ROLE);

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void editRoleWithInvalidUser_ThrowsException() throws Exception {

        String body = EDIT_ROLE_REQUEST_BODY;

        when(userService.editUserRole(EDIT_ROLE_MAIL, MODERATOR_ROLE)).thenReturn(false);


        this.mvc
                .perform(post(PROFILE_ROLE_EDIT)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(body.getBytes())
                        .with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));


        verify(userService).editUserRole(EDIT_ROLE_MAIL, MODERATOR_ROLE);

    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void validPostOnDelete_CallDeleteOnServiceAndRedirectsToAll() throws Exception {
        ProfileDeleteBindingModel profileDeleteBindingModel = new ProfileDeleteBindingModel();
        profileDeleteBindingModel.setId(USER_ID);


        this.mvc
                .perform(post(DELETE_URL)
                        .flashAttr(PROFILE_DELETE_BINDING_MODEL, profileDeleteBindingModel)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_PROFILES_ROUTE));

        verify(userService).deleteProfile(any());

    }


    @Test
    @WithMockUser(roles={"ADMIN"})
    public void logs_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get(LOGS_URL).with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void logs_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get(LOGS_URL).with(csrf()))
                .andExpect(model().attributeExists(LOG_VIEW_MODEL_ATTRIBUTE_NAME));

        verify(logService).getLogsOrderedByDate();
    }






}
