package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.ObtenerDocumentosParaEnvioCorreoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Fase2.ExamenValoracionCanceladoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/respuesta_examen_valoracion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class RespuestaExamenValoracionController {

        private final RespuestaExamenValoracionService respuestaExamenValoracion;

        @PostMapping("/{idTrabajoGrado}")
        public ResponseEntity<RespuestaExamenValoracionResponseDto> crear(@PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody RespuestaExamenValoracionDto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(respuestaExamenValoracion.crear(idTrabajoGrado, examenValoracion, result));
        }

        @GetMapping("/{idTrabajoGrado}")
        public ResponseEntity<Map<String, List<RespuestaExamenValoracionResponseDto>>> buscarPorId(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion.buscarPorId(idTrabajoGrado));
        }

        @PutMapping("/{idRespuestaExamen}")
        public ResponseEntity<RespuestaExamenValoracionResponseDto> actualizar(@PathVariable Long idRespuestaExamen,
                        @Valid @RequestBody RespuestaExamenValoracionDto examenValoracion, BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(respuestaExamenValoracion.actualizar(idRespuestaExamen, examenValoracion,
                                                result));
        }

        @PostMapping("/insertarInformacionCancelado/{idTrabajoGrado}")
        public ResponseEntity<ExamenValoracionCanceladoDto> insertarInformacionCancelado(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion
                                                .insertarInformacionCancelado(idTrabajoGrado,
                                                                examenValoracionCanceladoDto, result));
        }

        @GetMapping("/validarNumeroNoAprobado/{idTrabajoGrado}")
        public ResponseEntity<?> validarNumeroNoAprobado(@PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion.validarNumeroNoAprobado(idTrabajoGrado));
        }

        @GetMapping("/obtenerDocumentosParaEnviarCorreo/{idRespuestaExamenValoracion}")
        public ResponseEntity<ObtenerDocumentosParaEnvioCorreoDto> obtenerDocumentosParaEnviarCorreo(
                        @PathVariable Long idRespuestaExamenValoracion) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion
                                                .obtenerDocumentosParaEnviarCorreo(idRespuestaExamenValoracion));
        }
}
