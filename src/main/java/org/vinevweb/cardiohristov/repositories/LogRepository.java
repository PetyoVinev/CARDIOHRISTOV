package org.vinevweb.cardiohristov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vinevweb.cardiohristov.domain.entities.Log;


import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    List<Log> findAllByOrderByDateTimeDesc();
}
