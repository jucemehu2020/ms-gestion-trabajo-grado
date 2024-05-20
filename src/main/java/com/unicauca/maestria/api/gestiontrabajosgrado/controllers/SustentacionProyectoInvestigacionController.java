package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
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
                .body(sustentacionProyectoInvestigacion.insertarInformacionDocente(examenValoracion, result));
    }

    @PostMapping("/insertarInformacionComite")
    public ResponseEntity<SustentacionTrabajoInvestigacionComiteResponseDto> insertarInformacionComite(
            @Valid @RequestBody SustentacionTrabajoInvestigacionComiteDto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sustentacionProyectoInvestigacion.insertarInformacionComite(examenValoracion, result));
    }

    @PostMapping("/insertarInformacionCoordinador")
    public ResponseEntity<SustentacionTrabajoInvestigacionCoordinadorResponseDto> insertarInformacionCoordinador(
            @Valid @RequestBody SustentacionTrabajoInvestigacionCoordinadorDto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sustentacionProyectoInvestigacion.insertarInformacionCoordinador(examenValoracion, result));
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

    @GetMapping("/listarInformacionComite/{id}")
    public ResponseEntity<SustentacionTrabajoInvestigacionComiteResponseDto> listarInformacionComite(
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.listarInformacionComite(id));
    }

    @GetMapping("/listarInformacionCoordinador/{id}")
    public ResponseEntity<SustentacionTrabajoInvestigacionDto> listarInformacionCoordinador(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(sustentacionProyectoInvestigacion.listarInformacionCoordinador(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SustentacionTrabajoInvestigacionDto> actualizar(@PathVariable Long id,
            @Valid @RequestBody SustentacionTrabajoInvestigacionDto examenValoracion, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sustentacionProyectoInvestigacion.actualizar(id, examenValoracion, result));
    }

    @PostMapping("/descargarDocumento")
    public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo, BindingResult resulto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(sustentacionProyectoInvestigacion.descargarArchivo(rutaArchivo));
    }
}
