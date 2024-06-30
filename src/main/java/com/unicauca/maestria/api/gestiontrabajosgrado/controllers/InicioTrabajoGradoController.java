package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.CapturaEstadosDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EventosIdsDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.InformacionTrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoService;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/inicio_trabajo_grado")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
public class InicioTrabajoGradoController {

    private final InicioTrabajoGradoService inicioTrabajoGradoService;

    @GetMapping("/")
    public ResponseEntity<List<EstudianteInfoDto>> obtenerEstudiantes() {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.obtenerEstudiantes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDto> buscarEstadoEstudiante(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.buscarEstadoEstudiantePor(id));
    }

    @GetMapping("/obtenerInformacionEstudiante/{id}")
    public ResponseEntity<EstudianteInfoDto> obtenerInformacionEstudiante(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.obtenerInformacionEstudiante(id));
    }

    @GetMapping("/buscarTrabajoGrado/{id}")
    public ResponseEntity<TrabajoGradoResponseDto> buscarTrabajoGrado(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.buscarTrabajoGrado(id));
    }

    @PostMapping("/{idEstudiante}")
    @PreAuthorize("hasRole('DOCENTE')")
    public ResponseEntity<TrabajoGradoResponseDto> crearTrabajoGrado(@PathVariable Long idEstudiante,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inicioTrabajoGradoService.crearTrabajoGrado(idEstudiante, token));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminarTrabajoGrado(@PathVariable Long id) {
        inicioTrabajoGradoService.eliminarTrabajoGrado(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/obtenerIdsEventos/{idTrabajoGrado}")
    public ResponseEntity<EventosIdsDto> obtenerIdsEventos(@PathVariable Long idTrabajoGrado) {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.obtenerIdsEventos(idTrabajoGrado));
    }

    @GetMapping("/listarInformacionEstados")
    public ResponseEntity<List<InformacionTrabajoGradoResponseDto>> listarEstadosExamenValoracion(
            @Valid @RequestBody CapturaEstadosDto capturaEstadosDto,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inicioTrabajoGradoService.listarEstadosExamenValoracion(capturaEstadosDto,
                        result));
    }

}
