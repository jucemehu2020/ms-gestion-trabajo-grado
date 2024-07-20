package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionService;

import java.util.List;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/solicitud_examen_valoracion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class SolicitudExamenValoracionController {
        private final SolicitudExamenValoracionService serviceSolicitudExamenValoracion;

        @GetMapping("/listarDocentes")
        @PreAuthorize("hasRole('DOCENTE')")
        public ResponseEntity<List<DocenteInfoDto>> listarDocentes() {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarDocentes());
        }

        @GetMapping("/listarExpertos")
        @PreAuthorize("hasRole('DOCENTE')")
        public ResponseEntity<List<ExpertoInfoDto>> listarExpertos() {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarExpertos());
        }

        @GetMapping("/docente/{id}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<DocenteInfoDto> obtenerDocente(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.obtenerDocente(id));
        }

        @GetMapping("/experto/{id}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<ExpertoInfoDto> obtenerExperto(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.obtenerExperto(id));
        }

        @PostMapping("/insertarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE')")
        public ResponseEntity<SolicitudExamenValoracionDocenteResponseDto> insertarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionDocenteDto informacionDocente,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion
                                                .insertarInformacionDocente(idTrabajoGrado, informacionDocente,
                                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionResponseFase1Dto> insertarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase1Dto informacionCoordinador,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.insertarInformacionCoordinadorFase1(
                                                idTrabajoGrado,
                                                informacionCoordinador,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionCoordinadorFase2ResponseDto> insertarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase2Dto informacionCoordinador,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.insertarInformacionCoordinadorFase2(
                                                idTrabajoGrado,
                                                informacionCoordinador,
                                                result));
        }

        @GetMapping("/listarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionDocenteResponseListDto> listarInformacionDocente(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion.listarInformacionDocente(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionResponseFase1Dto> listarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion
                                                .listarInformacionCoordinadorFase1(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionCoordinadorFase2ResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion
                                                .listarInformacionCoordinadorFase2(idTrabajoGrado));
        }

        @PutMapping("/actualizarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionDocenteResponseDto> actualizarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionDocenteDto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.actualizarInformacionDocente(idTrabajoGrado,
                                                examenValoracion, result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionResponseFase1Dto> actualizarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase1Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.actualizarInformacionCoordinadorFase1(
                                                idTrabajoGrado,
                                                examenValoracion, result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<SolicitudExamenValoracionCoordinadorFase2ResponseDto> actualizarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase2Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.actualizarInformacionCoordinadorFase2(
                                                idTrabajoGrado,
                                                examenValoracion, result));
        }

}
