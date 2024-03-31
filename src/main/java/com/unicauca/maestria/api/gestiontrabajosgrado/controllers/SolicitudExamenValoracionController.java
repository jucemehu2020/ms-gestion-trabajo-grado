package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionService;

import java.util.List;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/solicitud_examen_valoracion")
public class SolicitudExamenValoracionController {
    private final SolicitudExamenValoracionService serviceSolicitudExamenValoracion;

    @GetMapping("/listarDocentes")
    public ResponseEntity<List<DocenteInfoDto>> listarDocentes() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarDocentes());
    }

    @GetMapping("/listarExpertos")
    public ResponseEntity<List<ExpertoInfoDto>> listarExpertos() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarExpertos());
    }

    @PostMapping
    public ResponseEntity<SolicitudExamenValoracionResponseDto> crear(@Valid @RequestBody SolicitudExamenValoracionDto examenValoracion, BindingResult result){
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceSolicitudExamenValoracion.crear(examenValoracion, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudExamenValoracionDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.buscarPorId(id));
    }

    @PutMapping("/{id}")
	public ResponseEntity<SolicitudExamenValoracionResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudExamenValoracionDto examenValoracion,BindingResult result){
		return ResponseEntity.status(HttpStatus.CREATED).body(serviceSolicitudExamenValoracion.actualizar(id, examenValoracion, result));
	}

    @GetMapping("/descargarDocumento")
    public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo, BindingResult resulto) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.descargarArchivo(rutaArchivo));
    }
}
