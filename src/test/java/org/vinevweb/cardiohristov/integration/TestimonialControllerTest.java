package org.vinevweb.cardiohristov.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.vinevweb.cardiohristov.domain.models.binding.TestimonialCreateBindingModel;
import org.vinevweb.cardiohristov.domain.models.service.TestimonialServiceModel;
import org.vinevweb.cardiohristov.repositories.TestimonialRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;
import org.vinevweb.cardiohristov.services.ProcedureService;
import org.vinevweb.cardiohristov.services.TestimonialServiceImpl;

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
public class TestimonialControllerTest {

    private static final String TESTIMONIALS_CREATE = "/testimonials/create";
    private static final String CONTENT = "content";
    private static final String CONTENT_VALUE = "Very good doctor!";
    private static final String REDIRECT_TESTIMONIALS_ALL = "redirect:/testimonials/all";
    private static final String TESTIMONIAL_CREATE_BINDING_MODEL = "testimonialCreateBindingModel";
    private static final String TESTIMONIALS_DELETE = "/testimonials/delete";
    private static final String TESTIMONIAL_ID = "1234";
    private static final String TESTIMONIALS_ALL = "/testimonials/all";
    private static final String TESTIMONIALS_ATTRIBUTE_NAME = "testimonials";


    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestimonialRepository testimonialRepository;

    @MockBean
    private TestimonialServiceImpl testimonialService;

    @MockBean
    private ProcedureService procedureService;

    @MockBean
    private ModelMapper modelMapper;

    @Before
    public void emptyDB() throws Exception {
        this.testimonialRepository.deleteAll();
    }


    @Test
    @WithMockUser
    public void validPostOnCreate_RedirectsToAll() throws Exception {

        when(testimonialService.createTestimonial(any())).thenReturn(true);
        this.mvc
                .perform(post(TESTIMONIALS_CREATE)
                        .with(csrf())
                        .param(CONTENT, CONTENT_VALUE))
                .andExpect(view().name(REDIRECT_TESTIMONIALS_ALL));

    }

    @Test
    @WithMockUser
    public void validPostOnCreate_InsertsTestimonialInDB() throws Exception {

        TestimonialCreateBindingModel testimonialCreateBindingModel = new TestimonialCreateBindingModel();
        testimonialCreateBindingModel.setContent(CONTENT);

        TestimonialServiceModel testimonialServiceModel = modelMapper.map(testimonialCreateBindingModel, TestimonialServiceModel.class);

        when(testimonialService.createTestimonial(testimonialServiceModel)).thenReturn(true);
        this.mvc
                .perform(post(TESTIMONIALS_CREATE)
                        .with(csrf())
                        .flashAttr(TESTIMONIAL_CREATE_BINDING_MODEL, testimonialCreateBindingModel))
                        .andExpect(view().name(REDIRECT_TESTIMONIALS_ALL));

        verify(testimonialService)
                .createTestimonial(testimonialServiceModel);
    }

    @Test
    @WithMockUser
    public void invalidPostOnCreate_ThrowsException() throws Exception {

        TestimonialCreateBindingModel testimonialCreateBindingModel = new TestimonialCreateBindingModel();
        testimonialCreateBindingModel.setContent(CONTENT);

        TestimonialServiceModel testimonialServiceModel = modelMapper.map(testimonialCreateBindingModel, TestimonialServiceModel.class);

        when(testimonialService.createTestimonial(testimonialServiceModel)).thenReturn(false);
        this.mvc
                .perform(post(TESTIMONIALS_CREATE)
                        .with(csrf())
                        .flashAttr(TESTIMONIAL_CREATE_BINDING_MODEL, testimonialCreateBindingModel))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));

        verify(testimonialService)
                .createTestimonial(testimonialServiceModel);
    }

    @Test
    @WithMockUser
    public void validPostOnDeleteWithUser_RedirectsToUnauthorized() throws Exception {
        this.mvc
                .perform(post(TESTIMONIALS_DELETE)
                        .with(csrf())
                        .param("id", TESTIMONIAL_ID))
                .andExpect(view().name(ERROR_UNAUTHORIZED));

    }

    @Test
    @WithMockUser(roles={"MODERATOR"})
    public void validPostOnDeleteWithModerator_RedirectsToAll() throws Exception {
        this.mvc
                .perform(post(TESTIMONIALS_DELETE)
                        .with(csrf())
                        .param("id", TESTIMONIAL_ID))
                .andExpect(view().name(REDIRECT_TESTIMONIALS_ALL));
        verify(testimonialService)
                .deleteTestimonial(TESTIMONIAL_ID);
    }

    @Test
    @WithMockUser
    public void getAll_returnsCorrectView() throws Exception {
        this.mvc
                .perform(get(TESTIMONIALS_ALL).with(csrf()))
                .andExpect(view().name(FRAGMENTS_BASE_LAYOUT_ROUTE));
    }

    @Test
    @WithMockUser
    public void getAll_returnsPassCorrectAttribute() throws Exception {
        this.mvc
                .perform(get(TESTIMONIALS_ALL).with(csrf()))
                .andExpect(model().attributeExists(PROCEDURES_ATTRIBUTE_NAME, TESTIMONIALS_ATTRIBUTE_NAME));
        verify(testimonialService)
                .findAllByOrderByWrittenOnDesc();
        verify(procedureService)
                .getAllByDateAsc();
    }

}
