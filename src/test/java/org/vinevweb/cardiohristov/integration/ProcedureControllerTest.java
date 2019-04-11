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
import static org.vinevweb.cardiohristov.Constants.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProcedureControllerTest {
    private static final String PROCEDURES_ALL = "/procedures/all";
    private static final String PROCEDURES_CREATE = "/procedures/create";
    private static final String REDIRECT_PROCEDURES_ALL = "redirect:/procedures/all";
    private static final String PROCEDURES_DELETE = "/procedures/delete";
    private static final String PROCEDURE_ID = "1234";
    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String CONTENT = "content";
    private static final String PROCEDURES_DETAIL_NAME = "/procedures/detail/{name}";
    private static final String PROCEDURE_ATTRIBUTE_NAME = "procedure";

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
                .perform(get(PROCEDURES_ALL).with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
    }

    @Test
    @WithMockUser
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get(PROCEDURES_ALL).with(csrf()))
                .andExpect(model().attributeExists(PROCEDURES_ATTRIBUTE_NAME));
    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validGetOnCreate_RedirectsToCreateArticlePage() throws Exception {

        this.mvc
                .perform(get(PROCEDURES_CREATE)
                        .with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));

    }
    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnCreate_InsertsProcedureInDB() throws Exception {

        when(procedureService.createProcedure(any())).thenReturn(true);
        this.mvc
                .perform(post(PROCEDURES_CREATE)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_PROCEDURES_ALL));
    }

    @Test
    @WithMockUser
    public void validPostOnDeleteWithUser_RedirectsToUnauthorized() throws Exception {
        this.mvc
                .perform(post(PROCEDURES_DELETE)
                        .with(csrf())
                        .param("id", PROCEDURE_ID))
                .andExpect(view().name(ERROR_UNAUTHORIZED));

    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnDeleteWithModerator_RedirectsToAll() throws Exception {
        this.mvc
                .perform(post(PROCEDURES_DELETE)
                        .with(csrf())
                        .param("id", PROCEDURE_ID))
                .andExpect(view().name(REDIRECT_PROCEDURES_ALL));



    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getDetail_ReturnsCorrectViewModel() throws Exception {
        ProcedureServiceModel procedureServiceModel = new ProcedureServiceModel();
        procedureServiceModel.setName(NAME);
        procedureServiceModel.setPictureUrl(URL);
        procedureServiceModel.setContent(CONTENT);
        procedureServiceModel.setDate(LocalDateTime.now());

        when(procedureService.findByName("name")).thenReturn(procedureServiceModel);

        this.mvc
                .perform(get(PROCEDURES_DETAIL_NAME, NAME, NAME)
                        .param(NAME, NAME))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE))
                .andExpect(model().attributeExists(PROCEDURES_ATTRIBUTE_NAME, PROCEDURE_ATTRIBUTE_NAME));

        verify(procedureService).getAllByDateAsc();
        verify(procedureService).findByName(NAME);

    }


}
