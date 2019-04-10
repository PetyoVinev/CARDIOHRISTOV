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
import org.vinevweb.cardiohristov.domain.models.service.ArticleServiceModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.CommentRepository;
import org.vinevweb.cardiohristov.services.ArticleService;
import org.vinevweb.cardiohristov.services.ArticleServiceImpl;
import org.vinevweb.cardiohristov.services.CommentService;
import org.vinevweb.cardiohristov.services.CommentServiceImpl;

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
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentRepository commentRepository;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private ArticleServiceImpl articleService;

    @Before
    public void emptyDB() {
        this.commentRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnCreate_InsertsCommentInDBAndRedirectsToItsArticle() throws Exception {

        when(commentService.createComment(any())).thenReturn(true);

        ArticleServiceModel articleServiceModel = new ArticleServiceModel();
        articleServiceModel.setTitle("title");
        when(articleService.findById(any())).thenReturn(articleServiceModel);

        this.mvc
                .perform(post("/comments/create")
                        .with(csrf()))
                .andExpect(view().name("redirect:/articles/detail/title"));

        verify(commentService).createComment(any());
    }




}
