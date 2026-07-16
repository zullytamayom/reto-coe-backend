package reto_aprobaciones.service;

import lombok.RequiredArgsConstructor;
import reto_aprobaciones.dto.request.AprobacionRequest;
import reto_aprobaciones.dto.request.SolicitudRequest;
import reto_aprobaciones.dto.response.SolicitudResponse;
import reto_aprobaciones.exception.SolicitudNotFoundException;
import reto_aprobaciones.exception.TransicionEstadoInvalidaException;
import reto_aprobaciones.model.EventoOutbox;
import reto_aprobaciones.model.HistoricoEstado;
import reto_aprobaciones.model.Solicitud;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reto_aprobaciones.repository.EventoOutboxRepository;
import reto_aprobaciones.repository.HistoricoEstadoRepository;
import reto_aprobaciones.repository.SolicitudRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final HistoricoEstadoRepository historicoEstadoRepository;
    private final EventoOutboxRepository eventoOutboxRepository;

    public SolicitudResponse crearSolicitud(SolicitudRequest request) {
        Solicitud solicitud = new Solicitud();
        solicitud.setTitulo(request.titulo());
        solicitud.setDescripcion(request.descripcion());
        solicitud.setSolicitante(request.solicitante());
        solicitud.setResponsable(request.responsable());
        solicitud.setTipoSolicitud(request.tipoSolicitud());
        
        solicitud = solicitudRepository.save(solicitud);
        
        registrarHistorico(solicitud, "PENDIENTE", request.solicitante(), "Creación de solicitud");
        
        return mapToResponse(solicitud);
    }

    public SolicitudResponse obtenerSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new SolicitudNotFoundException("Solicitud no encontrada con ID: " + id));
        return mapToResponse(solicitud);
    }

    public List<SolicitudResponse> listarSolicitudes() {
        return solicitudRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SolicitudResponse> listarSolicitudesPorEstado(String estado) {
        return solicitudRepository.findByEstado(estado).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SolicitudResponse aprobarSolicitud(Long id, AprobacionRequest request) {
        return cambiarEstado(id, "APROBADO", request);
    }

    public SolicitudResponse rechazarSolicitud(Long id, AprobacionRequest request) {
        return cambiarEstado(id, "RECHAZADO", request);
    }

    private SolicitudResponse cambiarEstado(Long id, String nuevoEstado, AprobacionRequest request) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new SolicitudNotFoundException("Solicitud no encontrada con ID: " + id));
        
        String estadoActual = solicitud.getEstado();
        
        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new TransicionEstadoInvalidaException(
                    String.format("No se puede transicionar de %s a %s", estadoActual, nuevoEstado));
        }
        
        solicitud.setEstado(nuevoEstado);
        solicitud = solicitudRepository.save(solicitud);
        
        registrarHistorico(solicitud, nuevoEstado, request.usuarioAccion(), request.comentarios());
        
        return mapToResponse(solicitud);
    }

    private boolean esTransicionValida(String estadoActual, String nuevoEstado) {
        return switch (estadoActual) {
            case "PENDIENTE" -> "APROBADO".equals(nuevoEstado) || "RECHAZADO".equals(nuevoEstado);
            default -> false;
        };
    }

    private void registrarHistorico(Solicitud solicitud, String estado, String usuario, String comentarios) {
        HistoricoEstado historico = new HistoricoEstado();
        historico.setSolicitud(solicitud);
        historico.setEstado(estado);
        historico.setUsuarioAccion(usuario);
        historico.setComentarios(comentarios);
        historicoEstadoRepository.save(historico);

        EventoOutbox outbox = new EventoOutbox();
        outbox.setAgregado("solicitud." + estado.toLowerCase());
        outbox.setContenido(String.format(
                "{\"id\": %d, \"estado\": \"%s\", \"usuario\": \"%s\", \"titulo\": \"%s\"}",
                solicitud.getId(), estado, usuario, solicitud.getTitulo()
        ));
        eventoOutboxRepository.save(outbox);
    }

    private SolicitudResponse mapToResponse(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getTitulo(),
                solicitud.getDescripcion(),
                solicitud.getSolicitante(),
                solicitud.getResponsable(),
                solicitud.getTipoSolicitud(),
                solicitud.getEstado(),
                solicitud.getFechaCreacion()
        );
    }
}
