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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.vinevweb.cardiohristov.domain.entities.Article;
import org.vinevweb.cardiohristov.domain.entities.Comment;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.CommentServiceModel;
import org.vinevweb.cardiohristov.repositories.ArticleRepository;
import org.vinevweb.cardiohristov.repositories.CommentRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.repositories.UserRoleRepository;
import org.vinevweb.cardiohristov.services.CommentServiceImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentServiceTests {

    private Comment fakeComment;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;


    @Mock
    private ModelMapper modelMapper;

    @Before
    public void createFakeUser() {
        this.fakeComment = new Comment();
        this.fakeComment.setWrittenOn(LocalDateTime.now());
        this.fakeComment.setArticle(null);
        this.fakeComment.setUser(null);
        this.fakeComment.setContent("la la la ");
    }

    @Test
    public void createCommentShouldReturnTrue() {

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(new User());

        ModelMapper modelMapper = new ModelMapper();
        CommentServiceModel commentServiceModel = modelMapper.map(this.fakeComment, CommentServiceModel.class);
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(this.fakeComment);

        Mockito.when(this.userRepository.findById( Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(this.articleRepository.findById( Mockito.any())).thenReturn(Optional.of(new Article()));
        Mockito.when(this.commentRepository.save( this.fakeComment)).thenReturn(this.fakeComment);


        boolean result = this.commentService.createComment(commentServiceModel);

        Assert.assertTrue(result);
    }

    @Test
    public void removeCommentShouldDeleteCommentFromUserAndDb() {
        User user = new User();
        user.setComments(new HashSet<>());
        user.getComments().add(this.fakeComment);
        this.fakeComment.setUser(user);

        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        this.commentService.removeCommentFromUserAndDelete(fakeComment);

        Assert.assertEquals( 0,  user.getComments().size());

        verify(userRepository)
                .saveAndFlush(any());
        verify(commentRepository)
                .delete(any());

    }

    @Test
    public void removeCommentShouldDeleteCommentFromArticleAndDb() {
        Article article = new Article();
        article.setComments(new HashSet<>());
        article.getComments().add(this.fakeComment);
        this.fakeComment.setArticle(article);

        Mockito.when(this.articleRepository.findById(Mockito.any())).thenReturn(Optional.of(article));

        this.commentService.removeCommentFromArticleAndDelete(fakeComment);

        Assert.assertEquals( 0,  article.getComments().size());

        verify(articleRepository)
                .saveAndFlush(any());
        verify(commentRepository)
                .delete(any());

    }

}
