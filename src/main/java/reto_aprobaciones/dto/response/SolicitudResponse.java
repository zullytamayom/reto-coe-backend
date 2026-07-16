package reto_aprobaciones.dto.response;

import java.time.LocalDateTime;

public record SolicitudResponse(
        Long id,
        String titulo,
        String descripcion,
        String solicitante,
        String responsable,
        String tipoSolicitud,
        String estado,
        LocalDateTime fechaCreacion
) {
}
