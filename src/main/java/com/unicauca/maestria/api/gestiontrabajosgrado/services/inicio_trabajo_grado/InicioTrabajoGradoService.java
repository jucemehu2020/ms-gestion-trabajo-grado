package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.InformacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoDto;

public interface InicioTrabajoGradoService {

    EstudianteResponseDtoAll obtenerEstudiantes();
    EstudianteResponseDto buscarEstadoEstudiantePor(Long id);
    TrabajoGradoDto crearTrabajoGrado(TrabajoGradoDto trabajoGrado,BindingResult result);

}
