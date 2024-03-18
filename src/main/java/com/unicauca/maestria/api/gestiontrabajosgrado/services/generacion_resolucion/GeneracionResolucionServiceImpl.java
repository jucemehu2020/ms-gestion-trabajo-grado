package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneracionResolucionServiceImpl implements GeneracionResolucionService {

    private final GeneracionResolucionRepository generacionResolucionRepository;
    private final GeneracionResolucionMapper generacionResolucionMapper;
    private final TrabajoGradoRepository trabajoGradoRepository;

    @Override
    @Transactional
    public GeneracionResolucionDto crear(GeneracionResolucionDto generacionResolucionDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldErrorException(result);
        }

        TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(generacionResolucionDto.getIdTrabajoGrados())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados() + " No encontrado"));

        // Mapear DTO a entidad
        GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

        // Establecer la relación uno a uno
        generarResolucion.setIdTrabajoGrado(trabajoGrado);
        trabajoGrado.setIdGeneracionResolucion(generarResolucion);

        // Guardar la entidad ExamenValoracion
        generarResolucion.setLinkAnteproyectoAprobado(FilesUtilities.guardarArchivo(generarResolucion.getLinkAnteproyectoAprobado()));
        generarResolucion.setLinkSolicitudComite(FilesUtilities.guardarArchivo(generarResolucion.getLinkSolicitudComite()));
        generarResolucion.setLinkSolicitudConcejoFacultad(FilesUtilities.guardarArchivo(generarResolucion.getLinkSolicitudConcejoFacultad()));
        generarResolucion.setLinkResolucionGeneradaCF(FilesUtilities.guardarArchivo(generarResolucion.getLinkResolucionGeneradaCF()));

        GeneracionResolucion generarResolucionRes = generacionResolucionRepository.save(generarResolucion);

        return generacionResolucionMapper.toDto(generarResolucionRes);
    }

    @Override
    @Transactional(readOnly = true)
    public GeneracionResolucionDto buscarPorId(Long idTrabajoGrado) {
        return generacionResolucionRepository.findById(idTrabajoGrado).map(generacionResolucionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Generacion de resolucion con id: " + idTrabajoGrado + " no encontrado"));
    }

    @Override
    public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto respuestaExamenValoracionDto, BindingResult result) {
        GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Generacion de resolucion con id: " + id + " no encontrado"));
                GeneracionResolucion responseExamenValoracion = null;
        if (generacionResolucionTmp != null) {
            if (respuestaExamenValoracionDto.getLinkAnteproyectoAprobado().compareTo(generacionResolucionTmp.getLinkAnteproyectoAprobado()) != 0) {
                respuestaExamenValoracionDto
                        .setLinkAnteproyectoAprobado(FilesUtilities.guardarArchivo(respuestaExamenValoracionDto.getLinkAnteproyectoAprobado()));
                FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkAnteproyectoAprobado());
            }
            //Repetir esto
            updateRtaExamenValoracionValues(generacionResolucionTmp, respuestaExamenValoracionDto);
            responseExamenValoracion = generacionResolucionRepository.save(generacionResolucionTmp);
        }
        return generacionResolucionMapper.toDto(responseExamenValoracion);
    }

    // Funciones privadas
    private void updateRtaExamenValoracionValues(GeneracionResolucion respuestaExamenValoracion,
    GeneracionResolucionDto respuestaExamenValoracionDto) {
        respuestaExamenValoracion.setDirector(respuestaExamenValoracionDto.getDirector());
        respuestaExamenValoracion.setCodirector(respuestaExamenValoracionDto.getCodirector());
        respuestaExamenValoracion.setNumeroActaRevision(respuestaExamenValoracionDto.getNumeroActaRevision());
        respuestaExamenValoracion.setFechaActa(respuestaExamenValoracionDto.getFechaActa());
        respuestaExamenValoracion.setNumeroResolucionGeneradaCF(respuestaExamenValoracionDto.getNumeroResolucionGeneradaCF());
        respuestaExamenValoracion.setFechaResolucion(respuestaExamenValoracionDto.getFechaResolucion());
    }

}
