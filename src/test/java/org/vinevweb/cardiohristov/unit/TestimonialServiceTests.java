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
import org.vinevweb.cardiohristov.domain.entities.Testimonial;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.TestimonialServiceModel;
import org.vinevweb.cardiohristov.repositories.TestimonialRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.services.LogServiceImpl;
import org.vinevweb.cardiohristov.services.TestimonialServiceImpl;
import static org.vinevweb.cardiohristov.Constants.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class TestimonialServiceTests {

    private static final String USERNAME = "boko@abv.bg";
    private static final String TESTIMONIAL_ID = "1234";
    private static final String CONTENT = "Content";


    @InjectMocks
    private TestimonialServiceImpl testimonialService;

    @Mock
    private LogServiceImpl logService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TestimonialRepository testimonialRepository;

    @Mock
    private ModelMapper modelMapper;


    @Test
    public void createTestimonialCreatesItInDbAndCreateLog() {

        User user = new User();
        user.setUsername(USERNAME);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);


        TestimonialServiceModel testimonialServiceModel = new TestimonialServiceModel();

        Testimonial testimonial = new Testimonial();

        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(testimonial);

       boolean result =  this.testimonialService.createTestimonial(testimonialServiceModel);
        testimonial.setUser(user);
        testimonial.setWrittenOn(LocalDateTime.now());

        verify(testimonialRepository)
                .save(testimonial);
        Assert.assertTrue(result);
    }

    @Test
    public void findAllByOrderByWrittenOnDescInvokeRepoWithCorrectMethod() {
        this.testimonialService.findAllByOrderByWrittenOnDesc();
        verify(testimonialRepository)
                .findAllByOrderByWrittenOnDesc();

    }

    @Test
    public void findAllByOrderByWrittenOnAscInvokeRepoWithCorrectMethod() {
        this.testimonialService.findAllByOrderByWrittenOnAsc();
        verify(testimonialRepository)
                .findAllByOrderByWrittenOnAsc();

    }

    @Test
    public void findByIdReturnsCorrectEntity() {
        TestimonialServiceModel testimonialServiceModel = new TestimonialServiceModel();
        testimonialServiceModel.setId(TESTIMONIAL_ID);
        Testimonial testimonial = new Testimonial();
        testimonial.setId(TESTIMONIAL_ID);

        Mockito.when(this.modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(testimonialServiceModel);
        Mockito.when(this.testimonialRepository.findById(Mockito.any())).thenReturn(Optional.of(testimonial));
        String result = testimonialService.findById(TESTIMONIAL_ID).getId();
        Assert.assertEquals(TESTIMONIAL_ID, result);

    }

    @Test(expected = NullPointerException.class)
    public void findByInvalidIdThrowsException() {
            testimonialService.findById(TESTIMONIAL_ID);
            verify(testimonialRepository)
                    .findById(TESTIMONIAL_ID);
        }


    @Test
    public void deleteTestimonialRemovesItFromDBAndCreatesLog() {

        Testimonial testimonial = new Testimonial();
        testimonial.setId(TESTIMONIAL_ID);
        testimonial.setContent(CONTENT);



        User user = new User();
        user.setUsername(USERNAME);
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(secCont);
        Mockito.when(auth.getPrincipal()).thenReturn(user);

        testimonial.setUser(user);

        Mockito.when(this.testimonialRepository.findById(Mockito.any())).thenReturn(Optional.of(testimonial));
        Mockito.when(this.userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        this.testimonialService.deleteTestimonial(TESTIMONIAL_ID);

        verify(testimonialRepository)
                .delete(testimonial);
        verify(userRepository)
                .saveAndFlush(user);

        verify(logService)
                .addEvent(any());



    }

}
