package org.vinevweb.cardiohristov.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
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
public class ArticleServiceTests {

    private static final String ARTICLE_ID = "1234";
    private static final String EMAIL = "boko@abv.bg";
    private static final String ARTICLE_TITLE = "Article Title";


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


    @Test
    public void createArticleCreatesItInDbAndCreateLog() {

        User user = new User();
        user.setUsername(EMAIL);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        ArticleServiceModel articleServiceModel = new ArticleServiceModel();

        Article article = new Article();
        article.setTitle(ARTICLE_TITLE);

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
        articleServiceModel.setId(ARTICLE_ID);
        Article article = new Article();
        article.setId(ARTICLE_ID);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(articleServiceModel);
        Mockito.when(this.articleRepository.findById(Mockito.any())).thenReturn(Optional.of(article));
        String result = articleService.findById(ARTICLE_ID).getId();
        Assert.assertEquals(ARTICLE_ID, result);

    }

    @Test(expected = NullPointerException.class)
    public void findByInvalidIdThrowsException() {
            articleService.findById(ARTICLE_ID);
            verify(articleRepository)
                    .findById(ARTICLE_ID);
        }


    @Test
    public void findByTitleReturnsCorrectEntity() {
        ArticleServiceModel articleServiceModel = new ArticleServiceModel();
        articleServiceModel.setTitle(ARTICLE_TITLE);
        Article article = new Article();
        article.setTitle(ARTICLE_TITLE);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(articleServiceModel);
        Mockito.when(this.articleRepository.findByTitle(Mockito.any())).thenReturn(article);
        String result = articleService.findByTitle(ARTICLE_ID).getTitle();
        Assert.assertEquals(ARTICLE_TITLE, result);

    }

    @Test
    public void deleteArticleRemovesItFromDBAndCreatesLog() {

        Article article = new Article();
        article.setTitle(ARTICLE_TITLE);
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

        this.articleService.deleteArticle(ARTICLE_ID);

        verify(articleRepository, times(2))
                .saveAndFlush(article);

        verify(commentService)
                .removeCommentFromUserAndDelete(comment1);
        verify(commentService)
                .removeCommentFromUserAndDelete(comment2);

        verify(articleRepository)
                .deleteById(ARTICLE_ID);
        verify(logService)
                .addEvent(any());



    }

}
