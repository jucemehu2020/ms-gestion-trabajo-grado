package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

    private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
    private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
    private final TrabajoGradoRepository trabajoGradoRepository;

    @Override
    @Transactional
    public SustentacionTrabajoInvestigacionDto crear(SustentacionTrabajoInvestigacionDto examenValoracionDto,
            BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldErrorException(result);
        }

        TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionDto.getIdTrabajoGrados())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TrabajoGrado con id: " + examenValoracionDto.getIdTrabajoGrados()
                                + " No encontrado"));

        // Mapear DTO a entidad
        SustentacionTrabajoInvestigacion examenValoracion = sustentacionProyectoIngestigacionMapper.toEntity(examenValoracionDto);

        // Establecer la relación uno a uno
        examenValoracion.setIdTrabajoGrado(trabajoGrado);
        trabajoGrado.setIdSustentacionProyectoInvestigacion(examenValoracion);

        // Guardar la entidad ExamenValoracion
        examenValoracion.setLinkRemisionDocumentoFinal(
                FilesUtilities.guardarArchivo(examenValoracion.getLinkRemisionDocumentoFinal()));
        examenValoracion.setLinkRemisionDocumentoFinalCF(
                FilesUtilities.guardarArchivo(examenValoracion.getLinkRemisionDocumentoFinalCF()));
        examenValoracion.setLinkConstanciaDocumentoFinal(
                FilesUtilities.guardarArchivo(examenValoracion.getLinkConstanciaDocumentoFinal()));
        examenValoracion
                .setLinkActaSustentacion(FilesUtilities
                        .guardarArchivo(examenValoracion.getLinkActaSustentacion()));
        examenValoracion.setLinkActaSustentacionPublica(
                FilesUtilities.guardarArchivo(examenValoracion.getLinkActaSustentacionPublica()));
        examenValoracion.setLinkEstudioHojaVidaAcademica(
                FilesUtilities.guardarArchivo(examenValoracion.getLinkEstudioHojaVidaAcademica()));

        SustentacionTrabajoInvestigacion examenValoracionRes = sustentacionProyectoInvestigacionRepository.save(examenValoracion);

        return sustentacionProyectoIngestigacionMapper.toDto(examenValoracionRes);
    }

    @Override
    @Transactional(readOnly = true)
    public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado) {
        return sustentacionProyectoInvestigacionRepository.findById(idTrabajoGrado)
                .map(sustentacionProyectoIngestigacionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Examen de valoracion con id: " + idTrabajoGrado + " no encontrado"));
    }

    @Override
    public SustentacionTrabajoInvestigacionDto actualizar(Long id,
            SustentacionTrabajoInvestigacionDto examenValoracionDto, BindingResult result) {
        SustentacionTrabajoInvestigacion examenValoracionTmp = sustentacionProyectoInvestigacionRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Examen de valoracion con id: " + id
                                + " no encontrado"));
        SustentacionTrabajoInvestigacion responseExamenValoracion = null;
        if (examenValoracionTmp != null) {
            if (examenValoracionDto.getLinkRemisionDocumentoFinal()
                    .compareTo(examenValoracionTmp.getLinkRemisionDocumentoFinal()) != 0) {
                examenValoracionDto
                        .setLinkRemisionDocumentoFinal(FilesUtilities.guardarArchivo(
                                examenValoracionDto.getLinkRemisionDocumentoFinal()));
                FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkRemisionDocumentoFinal());
            }
            updateExamenValoracionValues(examenValoracionTmp, examenValoracionDto);
            responseExamenValoracion = sustentacionProyectoInvestigacionRepository
                    .save(examenValoracionTmp);
        }
        return sustentacionProyectoIngestigacionMapper.toDto(responseExamenValoracion);
    }

    // Funciones privadas
    private void updateExamenValoracionValues(SustentacionTrabajoInvestigacion examenValoracion,
            SustentacionTrabajoInvestigacionDto examenValoracionDto) {
        examenValoracion.setUrlDocumentacion(examenValoracionDto.getUrlDocumentacion());
        examenValoracion.setRespuestaSustentacion(examenValoracionDto.getRespuestaSustentacion());
        examenValoracion.setNumeroActaTrabajoFinal(examenValoracion.getNumeroActaTrabajoFinal());
        examenValoracion.setFechaActa(examenValoracionDto.getFechaActa());
    }
}
