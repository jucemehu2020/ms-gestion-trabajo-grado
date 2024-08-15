package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-gestion-login", url = "http://ms-gestion-login:8080")
public interface ArchivoClientLogin {

    @GetMapping("/api/auth/obtenerCorreo/{user}")
    public String obtenerCorreo(@PathVariable String user);

    @GetMapping("/api/auth/obtenerPersonaId/{user}")
    public String obtenerPersonaId(@PathVariable String user);

}
