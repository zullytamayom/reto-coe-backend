package reto_aprobaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reto_aprobaciones.model.EventoOutbox;

import java.util.List;

@Repository
public interface EventoOutboxRepository extends JpaRepository<EventoOutbox, Long> {
    List<EventoOutbox> findByEstadoOrderByFechaCreacionAsc(String estado);
}

