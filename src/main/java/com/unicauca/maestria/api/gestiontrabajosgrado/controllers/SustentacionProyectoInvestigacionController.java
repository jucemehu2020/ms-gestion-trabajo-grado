package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionService;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/sustentacion_proyecto_investigacion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class SustentacionProyectoInvestigacionController {
        private final SustentacionProyectoInvestigacionService sustentacionProyectoInvestigacion;

        @PostMapping("/insertarInformacionDocente/{idTrabajoGrado}")
        public ResponseEntity<SustentacionTrabajoInvestigacionDocenteResponseDto> insertarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionDocente(idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase1/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase1ResponseDto> insertarInformacionCoordinadoFase1(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase1(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase2/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase2ResponseDto> insertarInformacionCoordinadoFase2(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase2(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase3/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> insertarInformacionCoordinadoFase3(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase3(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionEstudiante/{idSustentacion}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> insertarInformacionEstudiante(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionEstudiante(idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase4/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase4ResponseDto> insertarInformacionCoordinadoFase4(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase4(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @GetMapping("/listarInformacionDocente/{id}")
        public ResponseEntity<SustentacionTrabajoInvestigacionDocenteResponseDto> listarInformacionDocente(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionDocente(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase1/{id}")
        public ResponseEntity<STICoordinadorFase1ResponseDto> listarInformacionCoordinadorFase1(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionCoordinadorFase1(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{id}")
        public ResponseEntity<STICoordinadorFase2ResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionCoordinadorFase2(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase3/{id}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> listarInformacionCoordinadorFase3(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionCoordinadorFase3(id));
        }

        @GetMapping("/listarInformacionEstudiante/{id}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> listarInformacionEstudiante(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionEstudiante(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase4/{id}")
        public ResponseEntity<STICoordinadorFase4ResponseDto> listarInformacionCoordinadorFase4(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionCoordinadorFase4(id));
        }

        @PostMapping("/descargarDocumento")
        public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo,
                        BindingResult resulto) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.descargarArchivo(rutaArchivo));
        }

        @GetMapping("/verificarEgresado/{idEstudiante}")
        public ResponseEntity<?> verificarEgresado(@PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.verificarEgresado(idTrabajoGrado));
        }

        @GetMapping("/listarEstadosExamenValoracion/{numeroEstado}")
        public ResponseEntity<List<TrabajoGradoResponseDto>> listarEstadosExamenValoracion(
                        @PathVariable Integer numeroEstado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarEstadosExamenValoracion(numeroEstado));
        }

        @PostMapping("/actualizarInformacionDocente/{idSustentacion}")
        public ResponseEntity<SustentacionTrabajoInvestigacionDocenteResponseDto> actualizarInformacionDocente(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionDocente(idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadoFase1/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase1ResponseDto> actualizarInformacionCoordinadoFase1(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase1(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadoFase2/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase2ResponseDto> actualizarInformacionCoordinadoFase2(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase2(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionEstudiante/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> actualizarInformacionEstudiante(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase3(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadoFase3/{idSustentacion}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> actualizarInformacionCoordinadoFase3(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionEstudiante(idSustentacion,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadoFase4/{idSustentacion}")
        public ResponseEntity<STICoordinadorFase4ResponseDto> actualizarInformacionCoordinadoFase4(
                        @PathVariable Long idSustentacion,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase4(
                                                idSustentacion,
                                                sustentacionDto,
                                                result));
        }

}
