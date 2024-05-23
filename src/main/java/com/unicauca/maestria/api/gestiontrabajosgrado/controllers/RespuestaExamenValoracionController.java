package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionInformacionGeneralDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/respuesta_examen_valoracion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
public class RespuestaExamenValoracionController {

    private final RespuestaExamenValoracionService respuestaExamenValoracion;

    @PostMapping
    public ResponseEntity<RespuestaExamenValoracionDto> crear(
            @Valid @RequestBody RespuestaExamenValoracionDto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respuestaExamenValoracion.crear(examenValoracion, result));
    }

    @GetMapping("/listarInformacionGeneral/{id}")
    public ResponseEntity<RespuestaExamenValoracionInformacionGeneralDto> listarInformacionGeneral(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(respuestaExamenValoracion.listarInformacionGeneral(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, List<RespuestaExamenValoracionDto>>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(respuestaExamenValoracion.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaExamenValoracionDto> actualizar(@PathVariable Long id,
            @Valid @RequestBody RespuestaExamenValoracionDto examenValoracion, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respuestaExamenValoracion.actualizar(id, examenValoracion, result));
    }

    @PostMapping("/descargarDocumento")
    public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo, BindingResult resulto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(respuestaExamenValoracion.descargarArchivo(rutaArchivo));
    }
}
