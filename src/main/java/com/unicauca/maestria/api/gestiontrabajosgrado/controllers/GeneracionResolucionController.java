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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorResponseDto;
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

    @PostMapping("/insertarInformacionCoordinador")
    public ResponseEntity<GeneracionResolucionCoordinadorResponseDto> insertarInformacionCoordinador(
            @Valid @RequestBody GeneracionResolucionCoordinadorDto generacionResolucionDto,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generacionResolucion.insertarInformacionCoordinador(generacionResolucionDto, result));
    }

    @PostMapping("/insertarInformacionComite")
    public ResponseEntity<GeneracionResolucionComiteResponseDto> insertarInformacionComite(
            @Valid @RequestBody GeneracionResolucionComiteDto generacionResolucionDto,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generacionResolucion.insertarInformacionComite(generacionResolucionDto, result));
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<GeneracionResolucionDto> buscarPorId(@PathVariable Long
    // id) {
    // return
    // ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.buscarPorId(id));
    // }

    @GetMapping("/listarInformacionCoordinador/{id}")
    public ResponseEntity<GeneracionResolucionCoordinadorResponseDto> listarInformacionCoordinador(
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.listarInformacionCoordinador(id));
    }

    @GetMapping("/listarInformacionComite/{id}")
    public ResponseEntity<GeneracionResolucionDto> listarInformacionComite(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(generacionResolucion.listarInformacionComite(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneracionResolucionDto> actualizar(@PathVariable Long id,
            @Valid @RequestBody GeneracionResolucionDto generacionResolucionDto, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generacionResolucion.actualizar(id, generacionResolucionDto, result));
    }

    @PostMapping("/descargarDocumento")
    public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo, BindingResult resulto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(generacionResolucion.descargarArchivo(rutaArchivo));
    }
}
