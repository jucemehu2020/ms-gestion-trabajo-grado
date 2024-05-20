package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;

@FeignClient(name = "msv-estudiante-docente", url = "http://localhost:8082")
public interface ArchivoClient {

    @GetMapping("/api/estudiantes/")
    public List<EstudianteResponseDtoAll> obtenerEstudiantes();

    @GetMapping("/api/estudiantes/{id}")
    public EstudianteResponseDto obtenerPorIdEstudiante(@PathVariable Long id);

    @GetMapping("/api/docentes")
    public List<DocenteResponseDto> listarDocentesRes();

}