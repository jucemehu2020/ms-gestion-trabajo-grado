package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.util.ArrayList;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.InformacionTrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

public interface InicioTrabajoGradoService {

    public List<EstudianteInfoDto> listarEstudiantes();

    public EstudianteResponseDto listarTrabajosGradoEstudiante(Long id);

    public EstudianteInfoDto obtenerInformacionEstudiante(Long id);

    public TrabajoGradoResponseDto buscarTrabajoGrado(Long id);

    public TrabajoGradoResponseDto crearTrabajoGrado(Long idEstudiante, String token);

    public void eliminarTrabajoGrado(Long idTrabajoGrado);

    public List<InformacionTrabajoGradoResponseDto> listarInformacionEstados(ArrayList<Integer> capturaEstadosDto);

    public String descargarArchivo(String rutaArchivo);

    public Boolean cancelarTrabajoGrado(Long idTrabajoGrado);

}
