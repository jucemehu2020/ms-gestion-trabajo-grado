package com.unicauca.maestria.api.gestiontrabajosgrado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsGestionTrabajoGradoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGestionTrabajoGradoApplication.class, args);
	}

}
