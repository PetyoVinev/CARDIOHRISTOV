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

import static org.vinevweb.cardiohristov.Constants.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ArticleControllerTest {
    private static final String ARTICLES_ALL = "/articles/all";
    private static final String ARTICLES_ATTRIBUTE_NAME = "articles";
    private static final String ARTICLES_CREATE = "/articles/create";
    private static final String REDIRECT_ARTICLES_ALL = "redirect:/articles/all";
    private static final String ARTICLES_DELETE = "/articles/delete";
    private static final String ARTICLE_ID = "1234";
    private static final String FIRST_NAME = "Boko";
    private static final String LAST_NAME = "Bokov";
    private static final String ARTICLES_DETAIL_TITLE = "/articles/detail/{title}";
    private static final String ARTICLE_ATTRIBUTE_NAME = "article";

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
                .perform(get(ARTICLES_ALL).with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
        verify(procedureService).getAllByDateAsc();
        verify(articleService).findAllByOrderByWrittenOnDesc();
    }

    @Test
    @WithMockUser
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get(ARTICLES_ALL).with(csrf()))
                .andExpect(model().attributeExists(PROCEDURES_ATTRIBUTE_NAME, ARTICLES_ATTRIBUTE_NAME));
        verify(procedureService).getAllByDateAsc();
        verify(articleService).findAllByOrderByWrittenOnDesc();
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void validGetOnCreate_RedirectsToCreateArticlePage() throws Exception {

        this.mvc
                .perform(get(ARTICLES_CREATE)
                        .with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE))
                .andExpect(model().attributeExists(PROCEDURES_ATTRIBUTE_NAME));
        verify(procedureService).getAllByDateAsc();

    }


    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void validPostOnCreate_InsertsArticleInDB() throws Exception {

        when(articleService.createArticle(any())).thenReturn(true);
        this.mvc
                .perform(post(ARTICLES_CREATE)
                        .with(csrf()))
                .andExpect(view().name(REDIRECT_ARTICLES_ALL));
    }

    @Test
    @WithMockUser
    public void validPostOnDeleteWithUser_RedirectsToUnauthorized() throws Exception {
        this.mvc
                .perform(post(ARTICLES_DELETE)
                        .with(csrf())
                        .param("id", ARTICLE_ID))
                .andExpect(view().name(ERROR_UNAUTHORIZED));

    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void validPostOnDeleteWithModerator_RedirectsToAll() throws Exception {
        this.mvc
                .perform(post(ARTICLES_DELETE)
                        .with(csrf())
                        .param("id", ARTICLE_ID))
                .andExpect(view().name(REDIRECT_ARTICLES_ALL));

        verify(articleService).deleteArticle(ARTICLE_ID);

    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getDetail_ReturnsCorrectViewModel() throws Exception {
        ArticleServiceModel articleServiceModel = new ArticleServiceModel();
        Comment comment = new Comment();
        Set<Comment> comments = new HashSet<>();
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        comment.setUser(user);
        comment.setWrittenOn(LocalDateTime.now());
        comments.add(comment);
        articleServiceModel.setComments(comments);

        when(articleService.findByTitle(TITLE)).thenReturn(articleServiceModel);

        this.mvc
                .perform(get(ARTICLES_DETAIL_TITLE, TITLE, TITLE)
                        .param(TITLE, TITLE))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE))
                .andExpect(model().attributeExists(PROCEDURES_ATTRIBUTE_NAME, ARTICLES_ATTRIBUTE_NAME, ARTICLE_ATTRIBUTE_NAME));

        verify(procedureService).getAllByDateAsc();
        verify(articleService).findByTitle("title");
        verify(articleService).findAllByOrderByWrittenOnDesc();

    }


}
