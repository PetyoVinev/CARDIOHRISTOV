package org.vinevweb.cardiohristov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinevweb.cardiohristov.domain.entities.Testimonial;

import java.util.List;


@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, String> {

    List<Testimonial> findAllByOrderByWrittenOnDesc();
    List<Testimonial> findAllByOrderByWrittenOnAsc();

}
