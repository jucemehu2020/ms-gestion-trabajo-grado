package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionService;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/sustentacion_proyecto_investigacion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class SustentacionProyectoInvestigacionController {
    private final SustentacionProyectoInvestigacionService sustentacionProyectoInvestigacion;

    @PostMapping
    public ResponseEntity<SustentacionTrabajoInvestigacionDto> crear(@Valid @RequestBody SustentacionTrabajoInvestigacionDto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sustentacionProyectoInvestigacion.crear(examenValoracion, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SustentacionTrabajoInvestigacionDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(sustentacionProyectoInvestigacion.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SustentacionTrabajoInvestigacionDto> actualizar(@PathVariable Long id,
            @Valid @RequestBody SustentacionTrabajoInvestigacionDto examenValoracion, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sustentacionProyectoInvestigacion.actualizar(id, examenValoracion, result));
    }
}
