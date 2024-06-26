package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.GeneracionResolucionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/generacion_resolucion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class GeneracionResolucionController {
        private final GeneracionResolucionService generacionResolucion;

        @GetMapping("/listarDirectorAndCodirector")
        public ResponseEntity<List<DirectorAndCodirectorResponseDto>> listarDirectorAndCodirector() {
                return ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.listarDirectorAndCodirector());
        }

        @PostMapping("/insertarInformacionDocente/{idTrabajoGrado}")
        public ResponseEntity<GeneracionResolucionDocenteResponseDto> insertarInformacionDocente(
                        @PathVariable Long idTrabajoGrado,
                        @Valid @RequestBody GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionDocente(idTrabajoGrado,
                                                generacionResolucionDto, result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase1")
        public ResponseEntity<GeneracionResolucionCoordinadorFase1ResponseDto> insertarInformacionCoordinadorFase1(
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion
                                                .insertarInformacionCoordinadorFase1(
                                                                generacionResolucionCoordinadorFase1Dto, result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase2/{idGeneracionResolucion}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> insertarInformacionCoordinadorFase2(
                        @PathVariable Long idGeneracionResolucion,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionCoordinadorFase2(idGeneracionResolucion,
                                                generacionResolucionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase3/{idGeneracionResolucion}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> insertarInformacionCoordinadorFase3(
                        @PathVariable Long idGeneracionResolucion,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionCoordinadorFase3(idGeneracionResolucion,
                                                generacionResolucionDto,
                                                result));
        }

        @GetMapping("/listarInformacionDocente/{id}")
        public ResponseEntity<GeneracionResolucionDocenteResponseDto> listarInformacionDocente(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.listarInformacionDocente(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase2/{id}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> listarInformacionCoordinadorFase2(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarInformacionCoordinadorFase2(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase3/{id}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> listarInformacionCoordinadorFase3(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarInformacionCoordinadorFase3(id));
        }

        @PostMapping("/actualizarInformacionDocente/{idGeneracionResolucion}")
        public ResponseEntity<GeneracionResolucionDocenteResponseDto> actualizarInformacionDocente(
                        @PathVariable Long idGeneracionResolucion,
                        @Valid @RequestBody GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionDocente(idGeneracionResolucion,
                                                generacionResolucionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadorFase1/{idGeneracionResolucion}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase1ResponseDto> actualizarInformacionCoordinadorFase1(
                        @PathVariable Long idGeneracionResolucion,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionCoordinadorFase1(idGeneracionResolucion,
                                                generacionResolucionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadorFase2/{idGeneracionResolucion}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> actualizarInformacionCoordinadorFase2(
                        @PathVariable Long idGeneracionResolucion,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionCoordinadorFase2(idGeneracionResolucion,
                                                generacionResolucionDto,
                                                result));
        }

        @PostMapping("/actualizarInformacionCoordinadorFase3/{idGeneracionResolucion}")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> actualizarInformacionCoordinadorFase3(
                        @PathVariable Long idGeneracionResolucion,
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.actualizarInformacionCoordinadorFase3(idGeneracionResolucion,
                                                generacionResolucionDto,
                                                result));
        }

        @PostMapping("/descargarDocumento")
        public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.descargarArchivo(rutaArchivo));
        }

        @GetMapping("/obtenerDocumentosParaEnviarAlComite/{idGeneracionResolucion}")
        public ResponseEntity<ObtenerDocumentosParaEnvioDto> obtenerDocumentosParaEnviarAlComite(
                        @PathVariable Long idGeneracionResolucion) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.obtenerDocumentosParaEnviarAlComite(idGeneracionResolucion));
        }

        @GetMapping("/listarEstadosExamenValoracion/{numeroEstado}")
        public ResponseEntity<List<TrabajoGradoResponseDto>> listarEstadosExamenValoracion(
                        @PathVariable Integer numeroEstado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarEstadosExamenValoracion(numeroEstado));
        }

}
