package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.InformacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;

@FeignClient(name = "msv-estudiante-docente", url = "http://localhost:8082")
public interface ArchivoClient {

    @GetMapping("/api/estudiantes/")
    public EstudianteResponseDtoAll obtenerEstudiantes();

    @GetMapping("/api/estudiantes/{id}")
    public EstudianteResponseDto obtenerPorIdEstudiante(@PathVariable Long id);

    @GetMapping("/api/docentes")
    public DocenteResponseDto listarDocentesRes();

}
