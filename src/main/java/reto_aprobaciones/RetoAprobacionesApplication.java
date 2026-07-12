package reto_aprobaciones;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListTopicsResponse;

@SpringBootApplication
public class RetoAprobacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetoAprobacionesApplication.class, args);
	}
    // Bean de prueba que se ejecuta automáticamente al iniciar la app
    @Bean
    public CommandLineRunner pruebaConexionAws(SnsClient snsClient) {
        return args -> {
            System.out.println("==================================================");
            System.out.println("🚀 CASO DE PRUEBA: Validando conexión con AWS LocalStack...");
            try {
                // Intenta listar los temas creados por nuestro script init-aws.sh
                ListTopicsResponse response = snsClient.listTopics();

                System.out.println("✅ CONEXIÓN EXITOSA: ¡Spring Boot se conectó a LocalStack!");
                System.out.println("📋 Temas de SNS encontrados en tu Mac:");
                response.topics().forEach(topic ->
                        System.out.println("   - ARN del Tema: " + topic.topicArn())
                );
            } catch (Exception e) {
                System.out.println("❌ ERROR DE CONEXIÓN: No se pudo hablar con LocalStack.");
                System.out.println("💡 Detalle del fallo: " + e.getMessage());
            }
            System.out.println("==================================================");
        };
    }

}
