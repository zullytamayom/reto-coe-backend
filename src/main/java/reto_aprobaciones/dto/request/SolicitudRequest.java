package reto_aprobaciones.dto.request;

public record SolicitudRequest(
        String titulo,
        String descripcion,
        String solicitante,
        String responsable,
        String tipoSolicitud
) {}
