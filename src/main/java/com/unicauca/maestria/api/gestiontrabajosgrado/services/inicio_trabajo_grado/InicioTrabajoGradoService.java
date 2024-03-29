package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

public interface InicioTrabajoGradoService {

    List<EstudianteInfoDto> obtenerEstudiantes();

    EstudianteResponseDto buscarEstadoEstudiantePor(Long id);

    TrabajoGradoResponseDto crearTrabajoGrado(Long idEstudiante);

    void eliminarTrabajoGrado(Long idTrabajoGrado);

}
