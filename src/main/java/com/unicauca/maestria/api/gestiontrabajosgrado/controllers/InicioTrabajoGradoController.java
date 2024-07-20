package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
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
    @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
    public ResponseEntity<List<EstudianteInfoDto>> listarEstudiantes() {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.listarEstudiantes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<EstudianteResponseDto> listarTrabajosGradoEstudiante(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.listarTrabajosGradoEstudiante(id));
    }

    @GetMapping("/obtenerInformacionEstudiante/{id}")
    @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<EstudianteInfoDto> obtenerInformacionEstudiante(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(inicioTrabajoGradoService.obtenerInformacionEstudiante(id));
    }

    @GetMapping("/buscarTrabajoGrado/{id}")
    @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
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
    @PreAuthorize("hasRole('DOCENTE')")
    public ResponseEntity<?> eliminarTrabajoGrado(@PathVariable Long id) {
        inicioTrabajoGradoService.eliminarTrabajoGrado(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listarInformacionEstados")
    @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<List<InformacionTrabajoGradoResponseDto>> listarInformacionEstados(
            @RequestParam ArrayList<Integer> consultarEstados) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inicioTrabajoGradoService.listarInformacionEstados(consultarEstados));
    }

    @GetMapping("/descargarDocumento")
    @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
    public ResponseEntity<?> descargarArchivo(@RequestParam String rutaArchivo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inicioTrabajoGradoService.descargarArchivo(rutaArchivo));
    }

    @GetMapping("/cancelarTrabajoGrado/{idTrabajoGrado}")
    @PreAuthorize("hasRole('COORDINADOR')")
    public ResponseEntity<?> cancelarTrabajoGrado(@PathVariable Long idTrabajoGrado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inicioTrabajoGradoService.cancelarTrabajoGrado(idTrabajoGrado));
    }

}
