package reto_aprobaciones;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListTopicsResponse;

@SpringBootApplication
@EnableScheduling
public class RetoAprobacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetoAprobacionesApplication.class, args);
	}
}
