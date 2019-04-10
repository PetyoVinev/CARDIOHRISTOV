package org.vinevweb.cardiohristov.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void index_returnCorrectMvc() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(view().name("fragments/base-layout"));
    }
    @Test
    @WithMockUser
    public void contactUs_returnCorrectMvc() throws Exception {
        this.mvc.perform(get("/contactUs"))
                .andExpect(view().name("fragments/base-layout"));
    }
}