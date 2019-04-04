package org.vinevweb.cardiohristov.services;


import org.vinevweb.cardiohristov.domain.models.service.TestimonialServiceModel;

import java.util.List;

public interface TestimonialService {

    boolean createTestimonial(TestimonialServiceModel testimonialServiceModel);

    List<TestimonialServiceModel> findAllByOrderByWrittenOnDesc();
    List<TestimonialServiceModel> findAllByOrderByWrittenOnAsc();

    TestimonialServiceModel findById(String id);

    void deleteTestimonial(String id);
}
