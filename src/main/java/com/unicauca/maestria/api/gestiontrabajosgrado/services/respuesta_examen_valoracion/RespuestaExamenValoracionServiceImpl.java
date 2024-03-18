package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaExamenValoracionServiceImpl implements RespuestaExamenValoracionService {

    private final RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
    private final RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
    private final TrabajoGradoRepository trabajoGradoRepository;

    @Override
    @Transactional
    public RespuestaExamenValoracionDto crear(RespuestaExamenValoracionDto respuestaExamenValoracionDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldErrorException(result);
        }

        TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(respuestaExamenValoracionDto.getIdTrabajoGrados())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TrabajoGrado con id: " + respuestaExamenValoracionDto.getIdTrabajoGrados() + " No encontrado"));

        // Mapear DTO a entidad
        RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionMapper.toEntity(respuestaExamenValoracionDto);

        // Establecer la relación uno a uno
        rtaExamenValoracion.setIdTrabajoGrado(trabajoGrado);
        trabajoGrado.setIdRtaExamenValoracion(rtaExamenValoracion);

        // Guardar la entidad ExamenValoracion
        rtaExamenValoracion.setLinkFormatoB(FilesUtilities.guardarArchivo(rtaExamenValoracion.getLinkFormatoB()));
        rtaExamenValoracion.setLinkFormatoC(FilesUtilities.guardarArchivo(rtaExamenValoracion.getLinkFormatoC()));
        rtaExamenValoracion.setObservaciones(FilesUtilities.guardarArchivo(rtaExamenValoracion.getObservaciones()));

        RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository.save(rtaExamenValoracion);

        return respuestaExamenValoracionMapper.toDto(examenValoracionRes);
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaExamenValoracionDto buscarPorId(Long idTrabajoGrado) {
        return respuestaExamenValoracionRepository.findById(idTrabajoGrado).map(respuestaExamenValoracionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Respuesta examen de valoracion con id: " + idTrabajoGrado + " no encontrado"));
    }

    @Override
    public RespuestaExamenValoracionDto actualizar(Long id, RespuestaExamenValoracionDto respuestaExamenValoracionDto, BindingResult result) {
        RespuestaExamenValoracion respuestaExamenValoracionTmp = respuestaExamenValoracionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Respuesta examen de valoracion con id: " + id + " no encontrado"));
                RespuestaExamenValoracion responseExamenValoracion = null;
        if (respuestaExamenValoracionTmp != null) {
            if (respuestaExamenValoracionDto.getLinkFormatoB().compareTo(respuestaExamenValoracionTmp.getLinkFormatoB()) != 0) {
                respuestaExamenValoracionDto
                        .setLinkFormatoB(FilesUtilities.guardarArchivo(respuestaExamenValoracionDto.getLinkFormatoB()));
                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoB());
            }
            //Repetir esto
            updateRtaExamenValoracionValues(respuestaExamenValoracionTmp, respuestaExamenValoracionDto);
            responseExamenValoracion = respuestaExamenValoracionRepository.save(respuestaExamenValoracionTmp);
        }
        return respuestaExamenValoracionMapper.toDto(responseExamenValoracion);
    }

    // Funciones privadas
    private void updateRtaExamenValoracionValues(RespuestaExamenValoracion respuestaExamenValoracion,
    RespuestaExamenValoracionDto respuestaExamenValoracionDto) {
        respuestaExamenValoracion.setRespuestaExamenValoracion(respuestaExamenValoracionDto.getRespuestaExamenValoracion());
        respuestaExamenValoracion.setFechaMaxmiaEntrega(respuestaExamenValoracionDto.getFechaMaxmiaEntrega());
        respuestaExamenValoracion.setObservacion(respuestaExamenValoracionDto.getObservacion());
    }
}
