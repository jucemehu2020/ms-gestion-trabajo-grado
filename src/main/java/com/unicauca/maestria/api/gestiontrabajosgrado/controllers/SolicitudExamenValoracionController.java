package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/solicitud_examen_valoracion")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class SolicitudExamenValoracionController {
    private final SolicitudExamenValoracionService serviceSolicitudExamenValoracion;

    @GetMapping
    public ResponseEntity<DocenteResponseDto> buscaarPorId() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarDocentes());
    }

    @PostMapping
    public ResponseEntity<ExamenValoracionDto> crear(@Valid @RequestBody ExamenValoracionDto examenValoracion, BindingResult result){
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceSolicitudExamenValoracion.crear(examenValoracion, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamenValoracionDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.buscarPorId(id));
    }

    @PutMapping("/{id}")
	public ResponseEntity<ExamenValoracionDto> actualizar(@PathVariable Long id, @Valid @RequestBody ExamenValoracionDto examenValoracion,BindingResult result){
		return ResponseEntity.status(HttpStatus.CREATED).body(serviceSolicitudExamenValoracion.actualizar(id, examenValoracion, result));
	}
}
