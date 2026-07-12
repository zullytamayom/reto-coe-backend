package reto_aprobaciones.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reto_aprobaciones.model.EventoOutbox;
import reto_aprobaciones.repository.EventoOutboxRepository;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxProcessor {

    private final EventoOutboxRepository outboxRepository;
    private final SnsClient snsClient;

    // ARN del canal de AWS LocalStack configurado en Docker
    private static final String TOPIC_ARN = "arn:aws:sns:us-east-1:000000000000:tema-solicitudes-aprobacion";

    // Se ejecuta de fondo de forma ininterrumpida cada 5 segundos
    @Scheduled(fixedDelay = 5000)
    public void procesarEventosPendientes() {
        List<EventoOutbox> eventosPendientes = outboxRepository.findByEstadoOrderByFechaCreacionAsc("PENDIENTE");

        if (eventosPendientes.isEmpty()) {
            return;
        }

        log.info("Outbox: Detectados {} eventos bancarios listos para eyectar a la nube...", eventosPendientes.size());

        for (EventoOutbox evento : eventosPendientes) {
            try {
                // 1. Construir la publicación hacia AWS SNS
                PublishRequest request = PublishRequest.builder()
                        .topicArn(TOPIC_ARN)
                        .message(evento.getContenido())
                        .build();

                // 2. Eyectar el mensaje JSON directo al puerto 4566 de LocalStack
                snsClient.publish(request);

                // 3. Confirmación exitosa: Cambiamos el estado a PROCESADO
                evento.setEstado("PROCESADO");
                outboxRepository.save(evento);

                log.info("✅ Evento [{}] enviado a AWS SNS y marcado como PROCESADO con éxito.", evento.getAgregado());

            } catch (Exception e) {
                log.error("❌ Fallo crítico al enviar el evento [{}] a AWS: {}", evento.getAgregado(), e.getMessage());
                evento.setEstado("ERROR");
                outboxRepository.save(evento);
            }
        }
    }
}

