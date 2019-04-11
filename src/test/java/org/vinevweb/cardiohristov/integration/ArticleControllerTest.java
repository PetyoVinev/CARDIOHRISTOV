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
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.binding.ArticleCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.ArticleServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserRoleServiceModel;
import org.vinevweb.cardiohristov.domain.models.service.UserServiceModel;
import org.vinevweb.cardiohristov.domain.models.view.UserViewModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.TestimonialRepository;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.CommentService;
import org.vinevweb.cardiohristov.services.ProcedureServiceImpl;
import org.vinevweb.cardiohristov.services.TestimonialServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ProcedureServiceImpl procedureService;

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
        verify(procedureService).getAllByDateAsc();
        verify(articleService).findAllByOrderByWrittenOnDesc();
    }

    @Test
    @WithMockUser
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/articles/all").with(csrf()))
                .andExpect(model().attributeExists("procedures", "articles"));
        verify(procedureService).getAllByDateAsc();
        verify(articleService).findAllByOrderByWrittenOnDesc();
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void validGetOnCreate_RedirectsToCreateArticlePage() throws Exception {

        this.mvc
                .perform(get("/articles/create")
                        .with(csrf()))
                .andExpect(view().name("fragments/base-layout"))
                .andExpect(model().attributeExists("procedures"));
        verify(procedureService).getAllByDateAsc();

    }


    @Test
    @WithMockUser(roles = {"MODERATOR"})
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
    @WithMockUser(roles = {"MODERATOR"})
    public void validPostOnDeleteWithModerator_RedirectsToAll() throws Exception {
        this.mvc
                .perform(post("/articles/delete")
                        .with(csrf())
                        .param("id", "1234"))
                .andExpect(view().name("redirect:/articles/all"));

        verify(articleService).deleteArticle("1234");

    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getDetail_ReturnsCorrectViewModel() throws Exception {
        ArticleServiceModel articleServiceModel = new ArticleServiceModel();
        Comment comment = new Comment();
        Set<Comment> comments = new HashSet<>();
        User user = new User();
        user.setFirstName("Boko");
        user.setLastName("Bokov");
        comment.setUser(user);
        comment.setWrittenOn(LocalDateTime.now());
        comments.add(comment);
        articleServiceModel.setComments(comments);

        when(articleService.findByTitle("title")).thenReturn(articleServiceModel);

        this.mvc
                .perform(get("/articles/detail/{title}", "title", "title")
                        .param("title", "title"))
                .andExpect(view().name("fragments/base-layout"))
                .andExpect(model().attributeExists("procedures", "articles", "article"));

        verify(procedureService).getAllByDateAsc();
        verify(articleService).findByTitle("title");
        verify(articleService).findAllByOrderByWrittenOnDesc();

    }


}
