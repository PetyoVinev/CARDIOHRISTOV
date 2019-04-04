package org.vinevweb.cardiohristov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vinevweb.cardiohristov.domain.entities.Appointment;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findAllByOrderByDatetimeAsc();

    Appointment findAppointmentByDatetime(LocalDateTime dateTime);

    List<Appointment> findAppointmentsByDatetimeBefore(LocalDateTime dateTime);

    List<Appointment> findAppointmentsByDatetimeBetween(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);

}
