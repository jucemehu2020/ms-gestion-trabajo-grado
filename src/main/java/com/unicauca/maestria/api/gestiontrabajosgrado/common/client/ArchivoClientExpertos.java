package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;

@FeignClient(name = "ms-expertos", url = "http://localhost:8085")
public interface ArchivoClientExpertos {

    @GetMapping("/api/expertos")
    public List<ExpertoResponseDto> listar();

    @GetMapping("/api/expertos/{id}")
    public ExpertoResponseDto obtenerExpertoPorId(@PathVariable Long id);

}