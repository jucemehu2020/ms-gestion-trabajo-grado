package com.unicauca.maestria.api.gestiontrabajosgrado.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;

@FeignClient(name = "ms-estudiante-docente-experto", url = "http://ms-estudiante-docente-experto:8082", configuration = FeignConfig.class)
public interface ArchivoClient {

    @GetMapping("/api/estudiantes/")
    public List<EstudianteResponseDtoAll> obtenerEstudiantes();

    @GetMapping("/api/estudiantes/{id}")
    public EstudianteResponseDto obtenerPorIdEstudiante(@PathVariable Long id);
    
    @GetMapping("/api/estudiantes/{id}")
    public EstudianteResponseDtoAll obtenerInformacionEstudiante(@PathVariable Long id);

    @GetMapping("/api/docentes")
    public List<DocenteResponseDto> listarDocentesRes();

    @GetMapping("/api/docentes/{id}")
    public DocenteResponseDto obtenerDocentePorId(@PathVariable Long id);

    @GetMapping("/api/expertos")
    public List<ExpertoResponseDto> listarExpertos();

    @GetMapping("/api/expertos/{id}")
    public ExpertoResponseDto obtenerExpertoPorId(@PathVariable Long id);

}