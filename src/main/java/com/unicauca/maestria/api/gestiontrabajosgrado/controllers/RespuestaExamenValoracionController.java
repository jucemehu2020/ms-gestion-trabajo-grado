package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/respuesta_examen_valoracion")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class RespuestaExamenValoracionController {

    private final RespuestaExamenValoracionService respuestaExamenValoracion;

    @PostMapping
    public ResponseEntity<RespuestaExamenValoracionDto> crear(@Valid @RequestBody RespuestaExamenValoracionDto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respuestaExamenValoracion.crear(examenValoracion, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaExamenValoracionDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(respuestaExamenValoracion.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExamenValoracionDto> actualizar(@PathVariable Long id,
            @Valid @RequestBody RespuestaExamenValoracionDto examenValoracion, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respuestaExamenValoracion.actualizar(id, examenValoracion, result));
    }
}
