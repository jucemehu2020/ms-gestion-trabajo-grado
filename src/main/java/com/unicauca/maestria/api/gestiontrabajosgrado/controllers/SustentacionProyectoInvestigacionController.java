package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
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

        // @PostMapping
        // public ResponseEntity<SustentacionTrabajoInvestigacionDto> crear(
        // @Valid @RequestBody SustentacionTrabajoInvestigacionDto examenValoracion,
        // BindingResult result) {
        // return ResponseEntity.status(HttpStatus.CREATED)
        // .body(sustentacionProyectoInvestigacion.crear(examenValoracion, result));
        // }

        @PostMapping("/insertarInformacionDocente")
        public ResponseEntity<SustentacionTrabajoInvestigacionDocenteResponseDto> insertarInformacionDocente(
                        @Valid @RequestBody SustentacionTrabajoInvestigacionDocenteDto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionDocente(examenValoracion,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase1")
        public ResponseEntity<STICoordinadorFase1ResponseDto> insertarInformacionCoordinadoFase1(
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase1Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase1(
                                                examenValoracion,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase2")
        public ResponseEntity<STICoordinadorFase2ResponseDto> insertarInformacionCoordinadoFase2(
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase2Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase2(
                                                examenValoracion,
                                                result));
        }

        @PostMapping("/insertarInformacionEstudiante")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> insertarInformacionEstudiante(
                        @Valid @RequestBody SustentacionTrabajoInvestigacionEstudianteDto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionEstudiante(examenValoracion,
                                                result));
        }

        @PostMapping("/insertarInformacionCoordinadoFase3")
        public ResponseEntity<STICoordinadorFase3ResponseDto> insertarInformacionCoordinadoFase3(
                        @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorFase3Dto examenValoracion,
                        BindingResult result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinadoFase3(
                                                examenValoracion,
                                                result));
        }

        // @GetMapping("/{id}")
        // public ResponseEntity<SustentacionTrabajoInvestigacionDto>
        // buscarPorId(@PathVariable Long id) {
        // return
        // ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.buscarPorId(id));
        // }

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

        @GetMapping("/listarInformacionEstudiante/{id}")
        public ResponseEntity<SustentacionTrabajoInvestigacionEstudianteResponseDto> listarInformacionEstudiante(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionEstudiante(id));
        }

        @GetMapping("/listarInformacionCoordinadorFase3/{id}")
        public ResponseEntity<STICoordinadorFase3ResponseDto> listarInformacionCoordinadorFase3(
                        @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.listarInformacionCoordinadorFase3(id));
        }

        // @PutMapping("/{id}")
        // public ResponseEntity<SustentacionTrabajoInvestigacionDto>
        // actualizar(@PathVariable Long id,
        // @Valid @RequestBody SustentacionTrabajoInvestigacionDto examenValoracion,
        // BindingResult result) {
        // return ResponseEntity.status(HttpStatus.CREATED)
        // .body(sustentacionProyectoInvestigacion.actualizar(id, examenValoracion,
        // result));
        // }

        @PostMapping("/descargarDocumento")
        public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo,
                        BindingResult resulto) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(sustentacionProyectoInvestigacion.descargarArchivo(rutaArchivo));
        }
}
