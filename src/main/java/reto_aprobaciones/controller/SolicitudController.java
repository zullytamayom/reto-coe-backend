package reto_aprobaciones.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reto_aprobaciones.dto.request.AprobacionRequest;
import reto_aprobaciones.dto.request.SolicitudRequest;
import reto_aprobaciones.dto.response.SolicitudResponse;
import reto_aprobaciones.service.SolicitudService;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;
    @PostMapping
    public ResponseEntity<SolicitudResponse> crearSolicitud(@RequestBody SolicitudRequest request) {
        SolicitudResponse response = solicitudService.crearSolicitud(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtenerSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerSolicitud(id));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudService.listarSolicitudes());
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudResponse> aprobarSolicitud(@PathVariable Long id, @RequestBody AprobacionRequest request) {
        return ResponseEntity.ok(solicitudService.aprobarSolicitud(id, request));
    }

    @PutMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudResponse> rechazarSolicitud(@PathVariable Long id, @RequestBody AprobacionRequest request) {
        return ResponseEntity.ok(solicitudService.rechazarSolicitud(id, request));
    }
}
