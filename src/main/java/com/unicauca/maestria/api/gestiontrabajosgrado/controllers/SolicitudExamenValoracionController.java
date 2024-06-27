package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.DatosFormatoBResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.ObtenerDocumentosParaEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorResponseDto;
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
        public ResponseEntity<List<DocenteInfoDto>> listarDocentes() {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarDocentes());
        }

        @GetMapping("/listarExpertos")
        public ResponseEntity<List<ExpertoInfoDto>> listarExpertos() {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarExpertos());
        }

        @GetMapping("/docente/{id}")
        public ResponseEntity<DocenteInfoDto> obtenerDocente(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.obtenerDocente(id));
        }

        @GetMapping("/experto/{id}")
        public ResponseEntity<ExpertoInfoDto> obtenerExperto(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.obtenerExperto(id));
        }

        @PostMapping("/insertarInformacionDocente/{idTrabajoGrado}")
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
        public ResponseEntity<SolicitudExamenValoracionCoordinadorResponseDto> insertarInformacionCoordinadorFase2(
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
        public ResponseEntity<SolicitudExamenValoracionDocenteResponseListDto> listarInformacionDocente(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion.listarInformacionDocente(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase1/{idTrabajoGrado}")
        public ResponseEntity<SolicitudExamenValoracionResponseFase1Dto> listarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion
                                                .listarInformacionCoordinadorFase1(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{idTrabajoGrado}")
        public ResponseEntity<SolicitudExamenValoracionCoordinadorResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion.listarInformacionCoordinadorFase2(idTrabajoGrado));
        }

        @PutMapping("/actualizarInformacionDocente/{idTrabajoGrado}")
        public ResponseEntity<SolicitudExamenValoracionDocenteResponseDto> actualizarInformacionDocente(
                        @PathVariable Long idTrabajoGrado, @Valid @RequestBody SolicitudExamenValoracionDocenteDto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.actualizarInformacionDocente(idTrabajoGrado,
                                                examenValoracion, result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase1/{idTrabajoGrado}")
        public ResponseEntity<SolicitudExamenValoracionResponseFase1Dto> actualizarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase1Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                examenValoracion, result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase2/{idTrabajoGrado}")
        public ResponseEntity<SolicitudExamenValoracionCoordinadorResponseDto> actualizarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase2Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(serviceSolicitudExamenValoracion.actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                                examenValoracion, result));
        }

        @GetMapping("/descargarDocumento")
        public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo,
                        BindingResult resulto) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion.descargarArchivo(rutaArchivo));
        }

        @GetMapping("/obtenerInformacionFormatoB/{id}")
        public ResponseEntity<DatosFormatoBResponseDto> obtenerInformacionFormatoB(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion.obtenerInformacionFormatoB(id));
        }

        @GetMapping("/obtenerDocumentosParaEvaluador/{idExamenValoracion}")
        public ResponseEntity<ObtenerDocumentosParaEvaluadorDto> obtenerDocumentosParaEvaluador(
                        @PathVariable Long idExamenValoracion) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion
                                                .obtenerDocumentosParaEvaluador(idExamenValoracion));
        }

        @GetMapping("/listarEstadosExamenValoracion/{numeroEstado}")
        public ResponseEntity<List<TrabajoGradoResponseDto>> listarEstadosExamenValoracion(
                        @PathVariable Integer numeroEstado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(serviceSolicitudExamenValoracion.listarEstadosExamenValoracion(numeroEstado));
        }

}
