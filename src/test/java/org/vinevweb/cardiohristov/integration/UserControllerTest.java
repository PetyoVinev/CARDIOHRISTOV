package org.vinevweb.cardiohristov.integration;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vinevweb.cardiohristov.domain.models.binding.UserRegisterBindingModel;
import org.vinevweb.cardiohristov.repositories.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.vinevweb.cardiohristov.Constants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {

    private static final String REGISTER = "/register";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private UserRegisterBindingModel userRegisterBindingModel;

    @Before
    public void emptyDB() throws Exception {
        this.userRepository.deleteAll();
        this.userRegisterBindingModel = new UserRegisterBindingModel();
        this.userRegisterBindingModel.setEmailRegister(USER_USERNAME);
        this.userRegisterBindingModel.setPasswordRegister(PASSWORD);
        this.userRegisterBindingModel.setConfirmPassword(PASSWORD);
        this.userRegisterBindingModel.setFirstName(USER_FIRST_NAME);
        this.userRegisterBindingModel.setLastName(USER_LAST_NAME);
    }


    @Test
    public void validPostOnRegister_InsertsUserInDB() throws Exception {

        this.mvc
                .perform(post(REGISTER)
                        .with(csrf())
                        .flashAttr(USER_REGISTER_BINDING_MODEL, this.userRegisterBindingModel));

        Assert.assertEquals(1, this.userRepository.count());
    }


    @Test
    @WithMockUser
    public void getRegister_returnRedirectCorrect() throws Exception {
        this.mvc
                .perform(get(REGISTER).with(csrf()))
                .andExpect(view().name(REDIRECT_INDEX_ROUTE));
    }

    @Test
    public void validPostOnRegister_returnViewWithAttribute() throws Exception {
        this.mvc
                .perform(post(REGISTER)
                        .with(csrf())
                        .flashAttr(USER_REGISTER_BINDING_MODEL, this.userRegisterBindingModel))
                        .andExpect(model().attributeExists(USER_REGISTER_BINDING_MODEL))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
    }

}
