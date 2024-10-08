package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.GeneracionResolucionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/generacion_resolucion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class GeneracionResolucionController {
        private final GeneracionResolucionService generacionResolucion;

        @GetMapping("/listarDirectorAndCodirector")
        @PreAuthorize("hasRole('DOCENTE')")
        public ResponseEntity<List<DirectorAndCodirectorResponseDto>> listarDirectorAndCodirector() {
                return ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.listarDirectorAndCodirector());
        }

        @PostMapping("/insertarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE')")
        public ResponseEntity<GeneracionResolucionDocenteResponseDto> insertarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionDocente(idTrabajoGrado,
                                                generacionResolucionDto, result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase1ResponseDto> insertarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion
                                                .insertarInformacionCoordinadorFase1(
                                                                idTrabajoGrado,
                                                                generacionResolucionCoordinadorFase1Dto, result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> insertarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionCoordinadorFase2(idTrabajoGrado,
                                                generacionResolucionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase3/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> insertarInformacionCoordinadorFase3(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionCoordinadorFase3(idTrabajoGrado,
                                                generacionResolucionDto,
                                                result));
        }

        @GetMapping("/listarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR') or hasRole('ESTUDIANTE')")
        public ResponseEntity<GeneracionResolucionDocenteListDto> listarInformacionDocente(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarInformacionDocente(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase1ResponseDto> listarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarInformacionCoordinadorFase1(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarInformacionCoordinadorFase2(idTrabajoGrado));
        }

        @GetMapping("/listarInformacionCoordinadorFase3/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE') or hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> listarInformacionCoordinadorFase3(
                        @PathVariable Long idTrabajoGrado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarInformacionCoordinadorFase3(idTrabajoGrado));
        }

        @PutMapping("/actualizarInformacionDocente/{idTrabajoGrado}")
        @PreAuthorize("hasRole('DOCENTE')")
        public ResponseEntity<GeneracionResolucionDocenteResponseDto> actualizarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionDocente(idTrabajoGrado,
                                                generacionResolucionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase1/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase1ResponseDto> actualizarInformacionCoordinadorFase1(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                                generacionResolucionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase2/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> actualizarInformacionCoordinadorFase2(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionCoordinadorFase2(idTrabajoGrado,
                                                generacionResolucionDto,
                                                result));
        }

        @PutMapping("/actualizarInformacionCoordinadorFase3/{idTrabajoGrado}")
        @PreAuthorize("hasRole('COORDINADOR')")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> actualizarInformacionCoordinadorFase3(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionCoordinadorFase3(idTrabajoGrado,
                                                generacionResolucionDto,
                                                result));
        }

}
