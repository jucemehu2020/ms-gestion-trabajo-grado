package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.EnvioEmailCorrecionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;

public interface SolicitudExamenValoracionService {

    public List<DocenteInfoDto> listarDocentes();

    public List<ExpertoInfoDto> listarExpertos();

    public SolicitudExamenValoracionDocenteResponseDto insertarInformacionDocente(
            SolicitudExamenValoracionDocenteDto informacionDocente, BindingResult result);

    public SolicitudExamenValoracionCoordinadorResponseDto insertarInformacionCoordinador(
            SolicitudExamenValoracionCoordinadorDto informacionDocente, BindingResult result);

//     public SolicitudExamenValoracionResponseDto crear(SolicitudExamenValoracionDto oficio, BindingResult result);

    public SolicitudExamenValoracionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado);

    public SolicitudExamenValoracionResponseDto listarInformacionCoordinador(Long idTrabajoGrado);

    // public SolicitudExamenValoracionResponseDto actualizar(Long id,
    // SolicitudExamenValoracionDto examenValoracionDto, BindingResult result);

    public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long id,
            SolicitudExamenValoracionDocenteDto examenValoracionDto, BindingResult result);

    public SolicitudExamenValoracionCoordinadorResponseDto actualizarInformacionCoordinador(Long id,
            SolicitudExamenValoracionCoordinadorDto examenValoracionDto, BindingResult result);

    public String descargarArchivo(RutaArchivoDto rutaArchivo);

    public Boolean enviarCorreoElectronicoCorrecion(EnvioEmailCorrecionDto envioEmailCorrecionDto, BindingResult result);
}
