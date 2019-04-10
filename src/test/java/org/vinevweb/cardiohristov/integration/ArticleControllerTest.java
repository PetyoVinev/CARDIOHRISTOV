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
import org.vinevweb.cardiohristov.domain.models.binding.ArticleCreateBindingModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.TestimonialRepository;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.TestimonialServiceImpl;

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
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @MockBean
    private ArticleService articleService;

    @Before
    public void emptyDB() throws Exception {
        this.articleRepository.deleteAll();
    }


    @Test
    @WithMockUser
    public void getAll_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/articles/all").with(csrf()))
                .andExpect(view().name("fragments/base-layout"));
    }

    @Test
    @WithMockUser
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/articles/all").with(csrf()))
                .andExpect(model().attributeExists("procedures", "articles"));
    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validGetOnCreate_RedirectsToCreateArticlePage() throws Exception {

        this.mvc
                .perform(get("/articles/create")
                        .with(csrf()))
                .andExpect(view().name("fragments/base-layout"));

    }
    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnCreate_InsertsArticleInDB() throws Exception {

        when(articleService.createArticle(any())).thenReturn(true);
        this.mvc
                .perform(post("/articles/create")
                        .with(csrf()))
                .andExpect(view().name("redirect:/articles/all"));
    }

    @Test
    @WithMockUser
    public void validPostOnDeleteWithUser_RedirectsToUnauthorized() throws Exception {
        this.mvc
                .perform(post("/articles/delete")
                        .with(csrf())
                        .param("id", "1234"))
                .andExpect(view().name("/error/unauthorized"));

    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnDeleteWithModerator_RedirectsToAll() throws Exception {
        this.mvc
                .perform(post("/articles/delete")
                        .with(csrf())
                        .param("id", "1234"))
                .andExpect(view().name("redirect:/articles/all"));



    }


}
