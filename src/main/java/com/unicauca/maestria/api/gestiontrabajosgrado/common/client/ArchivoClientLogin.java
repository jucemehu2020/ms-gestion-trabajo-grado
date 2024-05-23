package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;

@FeignClient(name = "ms-gestion-login", url = "http://localhost:8080")
public interface ArchivoClientLogin {

    @GetMapping("/api/auth/obtenerCorreo/{user}")
    public String obtenerCorreo(@PathVariable String user);

}
