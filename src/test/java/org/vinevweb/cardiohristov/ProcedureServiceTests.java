package org.vinevweb.cardiohristov;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.vinevweb.cardiohristov.domain.entities.Article;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.ArticleServiceModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.services.ArticleServiceImpl;
import org.vinevweb.cardiohristov.services.CommentServiceImpl;
import org.vinevweb.cardiohristov.services.LogServiceImpl;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProcedureServiceTests {

    private Comment fakeComment;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private LogServiceImpl logService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentServiceImpl commentService;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void create() {

    }

    @Test
    public void createArticleCreatesItInDbAndCreateLog() {

        User user = new User();
        user.setUsername("boko@abv.bg");

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        ArticleServiceModel articleServiceModel = new ArticleServiceModel();

        Article article = new Article();
        article.setTitle("Article Title");

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(article);
        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        this.articleService.createArticle(articleServiceModel);

        verify(articleRepository)
                .save(article);
        verify(logService)
                .addEvent(any());

    }

    @Test
    public void findAllByOrderByWrittenOnDescInvokeRepository() {
        this.articleService.findAllByOrderByWrittenOnDesc();
        verify(articleRepository)
                .findAllByOrderByWrittenOnDesc();

    }

    @Test
    public void findByIdReturnsCorrectEntity() {
        ArticleServiceModel articleServiceModel = new ArticleServiceModel();
        articleServiceModel.setId("1234");
        Article article = new Article();
        article.setId("1234");

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(articleServiceModel);
        Mockito.when(this.articleRepository.findById(Mockito.any())).thenReturn(Optional.of(article));
        String result = articleService.findById("1234").getId();
        Assert.assertEquals("1234", result);

    }

    @Test(expected = NullPointerException.class)
    public void findByInvalidIdThrowsException() {
            articleService.findById("asd");
            verify(articleRepository)
                    .findById("asd");
        }


    @Test
    public void findByTitleReturnsCorrectEntity() {
        ArticleServiceModel articleServiceModel = new ArticleServiceModel();
        articleServiceModel.setTitle("Title");
        Article article = new Article();
        article.setTitle("Title");

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(articleServiceModel);
        Mockito.when(this.articleRepository.findByTitle(Mockito.any())).thenReturn(article);
        String result = articleService.findByTitle("1234").getTitle();
        Assert.assertEquals("Title", result);

    }

    @Test
    public void deleteArticleRemovesItFromDBAndCreatesLog() {

        Article article = new Article();
        article.setTitle("Title");
        article.setComments(new HashSet<>());
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        article.getComments().add(comment1);
        article.getComments().add(comment2);

        Mockito.when(this.articleRepository.findById(Mockito.any())).thenReturn(Optional.of(article));

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(new User());

        this.articleService.deleteArticle("1234");

        verify(articleRepository, times(2))
                .saveAndFlush(article);

        verify(commentService)
                .removeCommentFromUserAndDelete(comment1);
        verify(commentService)
                .removeCommentFromUserAndDelete(comment2);

        verify(articleRepository)
                .deleteById("1234");
        verify(logService)
                .addEvent(any());



    }

}
