package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado.InicioTrabajoGradoService;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/inicio_trabajo_grado")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
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

    @PostMapping("/{idEstudiante}")
    public ResponseEntity<TrabajoGradoResponseDto> crearTrabajoGrado(@PathVariable Long idEstudiante) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inicioTrabajoGradoService.crearTrabajoGrado(idEstudiante));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminarTrabajoGrado(@PathVariable Long id) {
        inicioTrabajoGradoService.eliminarTrabajoGrado(id);
        return ResponseEntity.ok().build();
    }

}
