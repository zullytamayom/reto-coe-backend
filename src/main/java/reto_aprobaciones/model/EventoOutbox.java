package reto_aprobaciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tabla_outbox")
@Getter
@Setter
public class EventoOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String agregado;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
