package reto_aprobaciones.repository;

import reto_aprobaciones.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByEstado(String estado);
    List<Solicitud> findBySolicitante(String solicitante);
    List<Solicitud> findByResponsable(String responsable);
}
