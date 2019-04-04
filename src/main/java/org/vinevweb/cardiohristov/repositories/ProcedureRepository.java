package org.vinevweb.cardiohristov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinevweb.cardiohristov.domain.entities.Procedure;

import java.util.List;


@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, String> {

    List<Procedure> findAllByOrderByDateAsc();
    Procedure findByName(String name);

}
