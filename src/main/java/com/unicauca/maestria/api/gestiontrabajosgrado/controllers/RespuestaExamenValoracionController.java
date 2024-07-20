package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RetornoFormatoBDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado.ExamenValoracionCanceladoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/respuesta_examen_valoracion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class RespuestaExamenValoracionController {

        private final RespuestaExamenValoracionService respuestaExamenValoracion;

        @PostMapping("/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<RespuestaExamenValoracionResponseDto> insertarInformacion(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody RespuestaExamenValoracionDto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(respuestaExamenValoracion.insertarInformacion(idTrabajoGrado, examenValoracion,
                                                result));
        }

        @GetMapping("/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<Map<String, List<RespuestaExamenValoracionResponseDto>>> buscarPorId(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion.buscarPorId(idTrabajoGrado));
        }

        @PutMapping("/{idRespuestaExamen}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<RespuestaExamenValoracionResponseDto> actualizar(@PathVariable Long idRespuestaExamen,
                        @Valid @RequestBody RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(respuestaExamenValoracion.actualizar(idRespuestaExamen,
                                                respuestaExamenValoracionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCancelado/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<ExamenValoracionCanceladoDto> insertarInformacionCancelado(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion
                                                .insertarInformacionCancelado(idTrabajoGrado,
                                                                examenValoracionCanceladoDto, result));
        }

        @GetMapping("/evaluadorNoRespondio/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<?> evaluadorNoRespondio(@PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion.evaluadorNoRespondio(idTrabajoGrado));
        }

        @GetMapping("/obtenerFormatosB/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<?> obtenerFormatosB(@PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(respuestaExamenValoracion.obtenerFormatosB(idTrabajoGrado));
        }
}
