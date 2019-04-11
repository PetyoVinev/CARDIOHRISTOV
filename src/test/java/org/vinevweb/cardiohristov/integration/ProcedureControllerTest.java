package org.vinevweb.cardiohristov.integration;

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
import org.vinevweb.cardiohristov.domain.models.service.ProcedureServiceModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.ProcedureRepository;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.ProcedureService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProcedureControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProcedureRepository procedureRepository;

    @MockBean
    private ProcedureService procedureService;

    @Before
    public void emptyDB() throws Exception {
        this.procedureRepository.deleteAll();
    }


    @Test
    @WithMockUser
    public void getAll_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/procedures/all").with(csrf()))
                .andExpect(view().name("fragments/base-layout"));
    }

    @Test
    @WithMockUser
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/procedures/all").with(csrf()))
                .andExpect(model().attributeExists("procedures"));
    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validGetOnCreate_RedirectsToCreateArticlePage() throws Exception {

        this.mvc
                .perform(get("/procedures/create")
                        .with(csrf()))
                .andExpect(view().name("fragments/base-layout"));

    }
    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnCreate_InsertsProcedureInDB() throws Exception {

        when(procedureService.createProcedure(any())).thenReturn(true);
        this.mvc
                .perform(post("/procedures/create")
                        .with(csrf()))
                .andExpect(view().name("redirect:/procedures/all"));
    }

    @Test
    @WithMockUser
    public void validPostOnDeleteWithUser_RedirectsToUnauthorized() throws Exception {
        this.mvc
                .perform(post("/procedures/delete")
                        .with(csrf())
                        .param("id", "1234"))
                .andExpect(view().name("/error/unauthorized"));

    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnDeleteWithModerator_RedirectsToAll() throws Exception {
        this.mvc
                .perform(post("/procedures/delete")
                        .with(csrf())
                        .param("id", "1234"))
                .andExpect(view().name("redirect:/procedures/all"));



    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getDetail_ReturnsCorrectViewModel() throws Exception {
        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();
        procedureServiceModel.setName("name");
        procedureServiceModel.setPictureUrl("url");
        procedureServiceModel.setContent("content");
        procedureServiceModel.setDate(LocalDateTime.now());

        when(procedureService.findByName("name")).thenReturn(procedureServiceModel);

        this.mvc
                .perform(get("/procedures/detail/{name}", "name", "name")
                        .param("name", "name"))
                .andExpect(view().name("fragments/base-layout"))
                .andExpect(model().attributeExists("procedures",  "procedure"));

        verify(procedureService).getAllByDateAsc();
        verify(procedureService).findByName("name");

    }


}
