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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vinevweb.cardiohristov.repositories.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void emptyDB() {
        this.userRepository.deleteAll();
    }


    @Test
    public void register_returnCorrectView() throws Exception {
        this.mvc
                .perform(get("/register").with(csrf()))
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void register_confirmRegister() throws Exception {
        this.mvc
                .perform(post("/register")
                        .with(csrf())
                        .param("emailRegister", "pesho@abv.bg")
                        .param("passwordRegister", "1234")
                        .param("confirmPassword", "1234")
                        .param("firstName", "Pesho")
                        .param("lastName", "Peshov"));

        Assert.assertEquals(1, this.userRepository.count());
    }

    @Test
    public void register_returnRedirectCorrect() throws Exception {
        this.mvc
                .perform(post("/register")
                        .with(csrf())
                        .param("emailRegister", "pesho@abv.bg")
                        .param("passwordRegister", "1234")
                        .param("confirmPassword", "1234")
                        .param("firstName", "Pesho")
                        .param("lastName", "Peshov"))
                .andExpect(view().name("fragments/base-layout"));
    }
}
