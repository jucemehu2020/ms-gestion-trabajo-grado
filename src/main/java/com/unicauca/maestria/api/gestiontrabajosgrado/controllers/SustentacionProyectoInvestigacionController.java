package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionListDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sustentacion_proyecto_investigacion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class SustentacionProyectoInvestigacionController {
        private final SustentacionProyectoInvestigacionService sustentacionProyectoInvestigacion;

        @GetMapping("/listarDocentes")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<List<DocenteInfoDto>> listarDocentes() {
                return ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.listarDocentes());
        }

        @GetMapping("/listarExpertos")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<List<ExpertoInfoDto>> listarExpertos() {
                return ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.listarExpertos());
        }

        @GetMapping("/docente/{id}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<DocenteInfoDto> obtenerDocente(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.obtenerDocente(id));
        }

        @GetMapping("/experto/{id}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<ExpertoInfoDto> obtenerExperto(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.obtenerExperto(id));
        }

        @PostMapping("/insertarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
        @PreAuthorize("hasRole('ESTUDIANTE')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<SustentacionTrabajoInvestigacionListDocenteDto> listarInformacionDocente(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionDocente(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<STICoordinadorFase1ResponseDto> listarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase1(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<STICoordinadorFase2ResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase2(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase3/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<STICoordinadorFase3ResponseDto> listarInformacionCoordinadorFase3(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase3(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionEstudiante/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> listarInformacionEstudiante(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionEstudiante(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase4/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<STICoordinadorFase4ResponseDto> listarInformacionCoordinadorFase4(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion
                                                .listarInformacionCoordinadorFase4(idTrabajoGrado));
        }

        @GetMapping("/verificarEgresado/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<?> verificarEgresado(@PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.verificarEgresado(idTrabajoGrado));
        }

        @PutMapping("/actualizarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<STICoordinadorFase3ResponseDto> actualizarInformacionCoordinadoFase3(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.actualizarInformacionCoordinadoFase3(
                                                idTrabajoGrado,
                                                sustentacionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionEstudiante/{idTrabajoGrado}")
        @PreAuthorize("hasRole('ESTUDIANTE')")
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
        @PreAuthorize("hasRole('COORDINADOR')")
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
