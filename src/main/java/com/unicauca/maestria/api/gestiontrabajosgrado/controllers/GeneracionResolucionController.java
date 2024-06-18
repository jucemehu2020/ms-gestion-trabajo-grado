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
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.EnvioEmailCorrecionesDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.InformacionEnvioComiteDto;
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

        // @PostMapping
        // public ResponseEntity<GeneracionResolucionDto> crear(
        // @Valid @RequestBody GeneracionResolucionDto generacionResolucionDto,
        // BindingResult result) {
        // return ResponseEntity.status(HttpStatus.CREATED)
        // .body(generacionResolucion.crear(generacionResolucionDto, result));
        // }

        @PostMapping("/insertarInformacionDocente")
        public ResponseEntity<GeneracionResolucionDocenteResponseDto> insertarInformacionDocente(
                        @Valid @RequestBody GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionDocente(generacionResolucionDto, result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase2")
        public ResponseEntity<GeneracionResolucionCoordinadorFase2ResponseDto> insertarInformacionCoordinadorFase2(
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionCoordinadorFase2(generacionResolucionDto,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadorFase3")
        public ResponseEntity<GeneracionResolucionCoordinadorFase3ResponseDto> insertarInformacionCoordinadorFase3(
                        @Valid @RequestBody GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(generacionResolucion.insertarInformacionCoordinadorFase3(generacionResolucionDto,
                                                result));
        }

        // @GetMapping("/{id}")
        // public ResponseEntity<GeneracionResolucionDto> buscarPorId(@PathVariable Long
        // id) {
        // return
        // ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.buscarPorId(id));
        // }

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

        // @PutMapping("/{id}")
        // public ResponseEntity<GeneracionResolucionDto> actualizar(@PathVariable Long
        // id,
        // @Valid @RequestBody GeneracionResolucionDto generacionResolucionDto,
        // BindingResult result) {
        // return ResponseEntity.status(HttpStatus.CREATED)
        // .body(generacionResolucion.actualizar(id, generacionResolucionDto, result));
        // }

        @PostMapping("/descargarDocumento")
        public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.descargarArchivo(rutaArchivo));
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

        @GetMapping("/obtenerDocumentosParaEnviarAlComite/{idGeneracionResolucion}")
        public ResponseEntity<ObtenerDocumentosParaEnvioDto> obtenerDocumentosParaEnviarAlComite(
                        @PathVariable Long idGeneracionResolucion) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.obtenerDocumentosParaEnviarAlComite(idGeneracionResolucion));
        }

        @PostMapping("/enviarCorreoComite")
        public ResponseEntity<InformacionEnvioComiteDto> enviarCorreoComite(
                        @Valid @RequestBody InformacionEnvioComiteDto informacionEnvioComiteDto) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.enviarCorreoComite(informacionEnvioComiteDto));
        }

        @PostMapping("/enviarCorreoCorrecion")
        public ResponseEntity<EnvioEmailCorrecionesDto> enviarCorreoCorrecion(
                        @Valid @RequestBody EnvioEmailCorrecionesDto envioEmailCorrecionesDto) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.enviarCorreoCorrecion(envioEmailCorrecionesDto));
        }

        @GetMapping("/listarEstadosExamenValoracion/{numeroEstado}")
        public ResponseEntity<List<TrabajoGradoResponseDto>> listarEstadosExamenValoracion(
                        @PathVariable Integer numeroEstado) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(generacionResolucion.listarEstadosExamenValoracion(numeroEstado));
        }

}
