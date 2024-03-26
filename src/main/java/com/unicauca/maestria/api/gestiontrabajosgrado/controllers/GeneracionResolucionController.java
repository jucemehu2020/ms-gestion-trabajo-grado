package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.GeneracionResolucionService;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/generacion_resolucion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class GeneracionResolucionController {
    private final GeneracionResolucionService generacionResolucion;

    @PostMapping
    public ResponseEntity<GeneracionResolucionDto> crear(
            @Valid @RequestBody GeneracionResolucionDto generacionResolucionDto,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generacionResolucion.crear(generacionResolucionDto, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneracionResolucionDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneracionResolucionDto> actualizar(@PathVariable Long id,
            @Valid @RequestBody GeneracionResolucionDto generacionResolucionDto, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generacionResolucion.actualizar(id, generacionResolucionDto, result));
    }
}
