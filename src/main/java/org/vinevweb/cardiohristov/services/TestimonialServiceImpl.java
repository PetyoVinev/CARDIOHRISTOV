package org.vinevweb.cardiohristov.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.vinevweb.cardiohristov.domain.entities.Testimonial;
import org.vinevweb.cardiohristov.domain.entities.User;
import org.vinevweb.cardiohristov.domain.models.service.TestimonialServiceModel;
import org.vinevweb.cardiohristov.repositories.TestimonialRepository;
import org.vinevweb.cardiohristov.repositories.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class TestimonialServiceImpl implements TestimonialService {


    private final TestimonialRepository testimonialRepository;
    private final UserRepository userRepository;
    private final LogService logService;
    private final ModelMapper modelMapper;

    @Autowired
    public TestimonialServiceImpl(TestimonialRepository testimonialRepository, UserRepository userRepository, LogService logService, ModelMapper modelMapper) {
        this.testimonialRepository = testimonialRepository;
        this.userRepository = userRepository;
        this.logService = logService;
        this.modelMapper = modelMapper;
    }



    @Override
    public boolean createTestimonial(TestimonialServiceModel testimonialServiceModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();
        Testimonial testimonial = this.modelMapper.map(testimonialServiceModel, Testimonial.class);
        testimonial.setUser(userRepository.findById(currentUser.getId()).orElse(null));
        testimonial.setWrittenOn(LocalDateTime.now());
        this.testimonialRepository.save(testimonial);
        return true;
    }

    @Override
    public List<TestimonialServiceModel> findAllByOrderByWrittenOnDesc() {
        return this.testimonialRepository
                .findAllByOrderByWrittenOnDesc()
                .stream()
                .map(x -> this.modelMapper.map(x, TestimonialServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<TestimonialServiceModel> findAllByOrderByWrittenOnAsc() {
        return this.testimonialRepository
                .findAllByOrderByWrittenOnAsc()
                .stream()
                .map(x -> this.modelMapper.map(x, TestimonialServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
    @Override
    public TestimonialServiceModel findById(String id) {
        return modelMapper.map(Objects.requireNonNull(this.testimonialRepository.findById(id).orElse(null)), TestimonialServiceModel.class);
    }

    @Override
    public void deleteTestimonial(String id) {
        Testimonial testimonial = this.testimonialRepository.findById(id).orElse(null);
        User user = this.userRepository.findById(testimonial.getUser().getId()).orElse(null);
        user.getTestimonials().remove(testimonial);
        userRepository.saveAndFlush(user);
        this.testimonialRepository.delete(testimonial);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User)authentication.getPrincipal();

        this.logService.addEvent(new String[]{ LocalDateTime.now().toString(),
                currentUser.getUsername(),
                "Изтрит е отзив с текст: " + testimonial.getContent()});
    }

}
