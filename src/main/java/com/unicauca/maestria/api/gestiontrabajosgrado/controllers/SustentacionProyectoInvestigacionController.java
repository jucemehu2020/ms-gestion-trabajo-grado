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

        @PostMapping("/insertarInformacionCoordinadoFase1/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase1ResponseDto> insertarInformacionCoordinadoFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase1(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase2/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase2ResponseDto> insertarInformacionCoordinadoFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase2(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase3/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> insertarInformacionCoordinadoFase3(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase3(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionEstudiante/{idTrabajoGrado}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> insertarInformacionEstudiante(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionEstudiante(idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase4/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase4ResponseDto> insertarInformacionCoordinadoFase4(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase4(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @GetMapping("/listarInformacionDocente/{idTrabajoGrado}")
        public ResponseEntity<SustentacionTrabajoInvestigacionDocenteResponseDto> listarInformacionDocente(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionDocente(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase1/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase1ResponseDto> listarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase1(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase2ResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase2(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase3/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> listarInformacionCoordinadorFase3(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase3(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionEstudiante/{idTrabajoGrado}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> listarInformacionEstudiante(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionEstudiante(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase4/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase4ResponseDto> listarInformacionCoordinadorFase4(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase4(idTrabajoGrado));
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

        @PutMapping("/actualizarInformacionDocente/{idTrabajoGrado}")
        public ResponseEntity<SustentacionTrabajoInvestigacionDocenteResponseDto> actualizarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionDocente(idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadoFase1/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase1ResponseDto> actualizarInformacionCoordinadoFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase1(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadoFase2/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase2ResponseDto> actualizarInformacionCoordinadoFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase2(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadoFase3/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> actualizarInformacionCoordinadoFase3(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase3(idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionEstudiante/{idTrabajoGrado}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> actualizarInformacionEstudiante(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionEstudiante(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadoFase4/{idTrabajoGrado}")
        public ResponseEntity<STICoordinadorFase4ResponseDto> actualizarInformacionCoordinadoFase4(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase4(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

}
