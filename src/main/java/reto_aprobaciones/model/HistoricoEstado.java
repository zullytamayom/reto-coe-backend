package reto_aprobaciones.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historico_estados")
public class HistoricoEstado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "usuario_accion", nullable = false)
    private String usuarioAccion;

    @Column(length = 500)
    private String comentarios;

    @PrePersist
    protected void onCreate() {
        this.fechaCambio = LocalDateTime.now();
    }
}
