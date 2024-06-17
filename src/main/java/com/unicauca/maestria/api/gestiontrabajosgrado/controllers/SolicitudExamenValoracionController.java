package com.unicauca.maestria.api.gestiontrabajosgrado.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.DatosFormatoBResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.EnvioEmailCorrecionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.ObtenerDocumentosParaEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionService;

import java.util.List;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/solicitud_examen_valoracion")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
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

    @GetMapping("/docente/{id}")
    public ResponseEntity<DocenteInfoDto> obtenerDocente(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.obtenerDocente(id));
    }

    @GetMapping("/experto/{id}")
    public ResponseEntity<ExpertoInfoDto> obtenerExperto(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.obtenerExperto(id));
    }

    @PostMapping("/insertarInformacionDocente")
    public ResponseEntity<SolicitudExamenValoracionDocenteResponseDto> insertarInformacionDocente(
            @Valid @RequestBody SolicitudExamenValoracionDocenteDto informacionDocente, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceSolicitudExamenValoracion
                        .insertarInformacionDocente(informacionDocente, result));
    }

    @PostMapping("/insertarInformacionCoordinadorFase1")
    public ResponseEntity<SolicitudExamenValoracionCoordinadorFase1Dto> insertarInformacionCoordinadorFase1(
            @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase1Dto informacionCoordinador,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceSolicitudExamenValoracion.insertarInformacionCoordinadorFase1(informacionCoordinador,
                        result));
    }

    @PostMapping("/insertarInformacionCoordinadorFase2")
    public ResponseEntity<SolicitudExamenValoracionCoordinadorResponseDto> insertarInformacionCoordinadorFase2(
            @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase2Dto informacionCoordinador,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceSolicitudExamenValoracion.insertarInformacionCoordinadorFase2(informacionCoordinador,
                        result));
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<SolicitudExamenValoracionDto> buscarPorId(@PathVariable
    // Long id) {
    // return
    // ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.buscarPorId(id));
    // }

    @GetMapping("/listarInformacionDocente/{id}")
    public ResponseEntity<SolicitudExamenValoracionDocenteResponseListDto> listarInformacionDocente(
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceSolicitudExamenValoracion.listarInformacionDocente(id));
    }

    @GetMapping("/listarInformacionCoordinador/{id}")
    public ResponseEntity<SolicitudExamenValoracionResponseDto> listarInformacionCoordinador(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceSolicitudExamenValoracion.listarInformacionCoordinador(id));
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<SolicitudExamenValoracionResponseDto>
    // actualizar(@PathVariable Long id, @Valid @RequestBody
    // SolicitudExamenValoracionDto examenValoracion,BindingResult result){
    // return
    // ResponseEntity.status(HttpStatus.CREATED).body(serviceSolicitudExamenValoracion.actualizar(id,
    // examenValoracion, result));
    // }

    @PutMapping("/actualizarInformacionDocente/{id}")
    public ResponseEntity<SolicitudExamenValoracionDocenteResponseDto> actualizarInformacionDocente(
            @PathVariable Long id, @Valid @RequestBody SolicitudExamenValoracionDocenteDto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceSolicitudExamenValoracion.actualizarInformacionDocente(id, examenValoracion, result));
    }

    @PutMapping("/actualizarInformacionCoordinador/{id}")
    public ResponseEntity<SolicitudExamenValoracionCoordinadorResponseDto> actualizarInformacionCoordinador(
            @PathVariable Long id, @Valid @RequestBody SolicitudExamenValoracionCoordinadorFase2Dto examenValoracion,
            BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceSolicitudExamenValoracion.actualizarInformacionCoordinador(id, examenValoracion, result));
    }

    @GetMapping("/descargarDocumento")
    public ResponseEntity<?> descargarArchivo(@Valid @RequestBody RutaArchivoDto rutaArchivo, BindingResult resulto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceSolicitudExamenValoracion.descargarArchivo(rutaArchivo));
    }

    @PostMapping("/enviarEmailParaCorrecion")
    public ResponseEntity<?> enviarEmailParaCorrecion(
            @Valid @RequestBody EnvioEmailCorrecionDto envioEmailCorrecionDto, BindingResult result) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceSolicitudExamenValoracion.enviarCorreoElectronicoCorrecion(envioEmailCorrecionDto,
                        result));
    }

    @GetMapping("/obtenerInformacionFormatoB/{id}")
    public ResponseEntity<DatosFormatoBResponseDto> obtenerInformacionFormatoB(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceSolicitudExamenValoracion.obtenerInformacionFormatoB(id));
    }

    @GetMapping("/obtenerDocumentosParaEvaluador/{idExamenValoracion}")
    public ResponseEntity<ObtenerDocumentosParaEvaluadorDto> obtenerDocumentosParaEvaluador(
            @PathVariable Long idExamenValoracion) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceSolicitudExamenValoracion.obtenerDocumentosParaEvaluador(idExamenValoracion));
    }

    @GetMapping("/listarEstadosExamenValoracion/{numeroEstado}")
    public ResponseEntity<List<TrabajoGradoResponseDto>> listarEstadosExamenValoracion(
            @PathVariable Integer numeroEstado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceSolicitudExamenValoracion.listarEstadosExamenValoracion(numeroEstado));
    }

}
