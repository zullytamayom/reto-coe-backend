package reto_aprobaciones.repository;

import reto_aprobaciones.model.HistoricoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoEstadoRepository extends JpaRepository<HistoricoEstado, Long> {
    List<HistoricoEstado> findBySolicitudIdOrderByFechaCambioDesc(Long solicitudId);
}
