package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion.SustentacionTrabajoInvestigacionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto.SolicitudExamenValoracionResponseDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto.SustentacionTrabajoInvestigacionDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite.SustentacionTrabajoInvestigacionComiteResponseDto.SustentacionTrabajoInvestigacionComiteResponseDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.SustentacionTrabajoInvestigacionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto.SustentacionTrabajoInvestigacionDocenteDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-19T23:43:13-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class SustentacionProyectoInvestigacionMapperImpl implements SustentacionProyectoInvestigacionMapper {

    @Override
    public SustentacionTrabajoInvestigacionDto toDto(SustentacionTrabajoInvestigacion entity) {
        if ( entity == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionDtoBuilder sustentacionTrabajoInvestigacionDto = SustentacionTrabajoInvestigacionDto.builder();

        sustentacionTrabajoInvestigacionDto.fechaActa( entity.getFechaActa() );
        sustentacionTrabajoInvestigacionDto.linkActaSustentacion( entity.getLinkActaSustentacion() );
        sustentacionTrabajoInvestigacionDto.linkActaSustentacionPublica( entity.getLinkActaSustentacionPublica() );
        sustentacionTrabajoInvestigacionDto.linkConstanciaDocumentoFinal( entity.getLinkConstanciaDocumentoFinal() );
        sustentacionTrabajoInvestigacionDto.linkEstudioHojaVidaAcademica( entity.getLinkEstudioHojaVidaAcademica() );
        sustentacionTrabajoInvestigacionDto.linkRemisionDocumentoFinal( entity.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacionDto.linkRemisionDocumentoFinalCF( entity.getLinkRemisionDocumentoFinalCF() );
        sustentacionTrabajoInvestigacionDto.numeroActaTrabajoFinal( entity.getNumeroActaTrabajoFinal() );
        sustentacionTrabajoInvestigacionDto.respuestaSustentacion( entity.getRespuestaSustentacion() );
        sustentacionTrabajoInvestigacionDto.urlDocumentacion( entity.getUrlDocumentacion() );

        return sustentacionTrabajoInvestigacionDto.build();
    }

    @Override
    public List<SustentacionTrabajoInvestigacionDto> toDtoList(List<SustentacionTrabajoInvestigacion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SustentacionTrabajoInvestigacionDto> list = new ArrayList<SustentacionTrabajoInvestigacionDto>( entities.size() );
        for ( SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion : entities ) {
            list.add( toDto( sustentacionTrabajoInvestigacion ) );
        }

        return list;
    }

    @Override
    public SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionDto dto) {
        if ( dto == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionBuilder sustentacionTrabajoInvestigacion = SustentacionTrabajoInvestigacion.builder();

        sustentacionTrabajoInvestigacion.fechaActa( dto.getFechaActa() );
        sustentacionTrabajoInvestigacion.linkActaSustentacion( dto.getLinkActaSustentacion() );
        sustentacionTrabajoInvestigacion.linkActaSustentacionPublica( dto.getLinkActaSustentacionPublica() );
        sustentacionTrabajoInvestigacion.linkConstanciaDocumentoFinal( dto.getLinkConstanciaDocumentoFinal() );
        sustentacionTrabajoInvestigacion.linkEstudioHojaVidaAcademica( dto.getLinkEstudioHojaVidaAcademica() );
        sustentacionTrabajoInvestigacion.linkRemisionDocumentoFinal( dto.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacion.linkRemisionDocumentoFinalCF( dto.getLinkRemisionDocumentoFinalCF() );
        sustentacionTrabajoInvestigacion.numeroActaTrabajoFinal( dto.getNumeroActaTrabajoFinal() );
        sustentacionTrabajoInvestigacion.respuestaSustentacion( dto.getRespuestaSustentacion() );
        sustentacionTrabajoInvestigacion.urlDocumentacion( dto.getUrlDocumentacion() );

        return sustentacionTrabajoInvestigacion.build();
    }

    @Override
    public List<SustentacionTrabajoInvestigacion> toEntityList(List<SustentacionTrabajoInvestigacionDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<SustentacionTrabajoInvestigacion> list = new ArrayList<SustentacionTrabajoInvestigacion>( dtos.size() );
        for ( SustentacionTrabajoInvestigacionDto sustentacionTrabajoInvestigacionDto : dtos ) {
            list.add( toEntity( sustentacionTrabajoInvestigacionDto ) );
        }

        return list;
    }

    @Override
    public SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionDocenteDto docenteDto) {
        if ( docenteDto == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionBuilder sustentacionTrabajoInvestigacion = SustentacionTrabajoInvestigacion.builder();

        sustentacionTrabajoInvestigacion.linkRemisionDocumentoFinal( docenteDto.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacion.urlDocumentacion( docenteDto.getUrlDocumentacion() );

        return sustentacionTrabajoInvestigacion.build();
    }

    @Override
    public SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionComiteDto comiteDto) {
        if ( comiteDto == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionBuilder sustentacionTrabajoInvestigacion = SustentacionTrabajoInvestigacion.builder();

        sustentacionTrabajoInvestigacion.linkRemisionDocumentoFinalCF( comiteDto.getLinkRemisionDocumentoFinalCF() );

        return sustentacionTrabajoInvestigacion.build();
    }

    @Override
    public SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorDto coordinadorDto) {
        if ( coordinadorDto == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionBuilder sustentacionTrabajoInvestigacion = SustentacionTrabajoInvestigacion.builder();

        sustentacionTrabajoInvestigacion.fechaActa( coordinadorDto.getFechaActa() );
        sustentacionTrabajoInvestigacion.linkActaSustentacion( coordinadorDto.getLinkActaSustentacion() );
        sustentacionTrabajoInvestigacion.linkActaSustentacionPublica( coordinadorDto.getLinkActaSustentacionPublica() );
        sustentacionTrabajoInvestigacion.linkConstanciaDocumentoFinal( coordinadorDto.getLinkConstanciaDocumentoFinal() );
        sustentacionTrabajoInvestigacion.linkEstudioHojaVidaAcademica( coordinadorDto.getLinkEstudioHojaVidaAcademica() );
        sustentacionTrabajoInvestigacion.numeroActaTrabajoFinal( coordinadorDto.getNumeroActaTrabajoFinal() );
        sustentacionTrabajoInvestigacion.respuestaSustentacion( coordinadorDto.getRespuestaSustentacion() );

        return sustentacionTrabajoInvestigacion.build();
    }

    @Override
    public SustentacionTrabajoInvestigacionDocenteDto toDocenteDto(SustentacionTrabajoInvestigacion entity) {
        if ( entity == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionDocenteDtoBuilder sustentacionTrabajoInvestigacionDocenteDto = SustentacionTrabajoInvestigacionDocenteDto.builder();

        sustentacionTrabajoInvestigacionDocenteDto.linkRemisionDocumentoFinal( entity.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacionDocenteDto.urlDocumentacion( entity.getUrlDocumentacion() );

        return sustentacionTrabajoInvestigacionDocenteDto.build();
    }

    @Override
    public SustentacionTrabajoInvestigacionDocenteDto toDocenteResponseDto(SustentacionTrabajoInvestigacionDto entity) {
        if ( entity == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionDocenteDtoBuilder sustentacionTrabajoInvestigacionDocenteDto = SustentacionTrabajoInvestigacionDocenteDto.builder();

        sustentacionTrabajoInvestigacionDocenteDto.idTrabajoGrados( entity.getIdTrabajoGrados() );
        sustentacionTrabajoInvestigacionDocenteDto.linkRemisionDocumentoFinal( entity.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacionDocenteDto.urlDocumentacion( entity.getUrlDocumentacion() );

        return sustentacionTrabajoInvestigacionDocenteDto.build();
    }

    @Override
    public SustentacionTrabajoInvestigacionComiteResponseDto toCoordinadorDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionComiteResponseDtoBuilder sustentacionTrabajoInvestigacionComiteResponseDto = SustentacionTrabajoInvestigacionComiteResponseDto.builder();

        return sustentacionTrabajoInvestigacionComiteResponseDto.build();
    }

    @Override
    public SolicitudExamenValoracionResponseDto toDto(SolicitudExamenValoracionResponseDto entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionResponseDtoBuilder solicitudExamenValoracionResponseDto = SolicitudExamenValoracionResponseDto.builder();

        solicitudExamenValoracionResponseDto.actaAprobacionExamen( entity.getActaAprobacionExamen() );
        solicitudExamenValoracionResponseDto.evaluadorExterno( entity.getEvaluadorExterno() );
        solicitudExamenValoracionResponseDto.evaluadorInterno( entity.getEvaluadorInterno() );
        solicitudExamenValoracionResponseDto.fechaActa( entity.getFechaActa() );
        solicitudExamenValoracionResponseDto.fechaMaximaEvaluacion( entity.getFechaMaximaEvaluacion() );
        solicitudExamenValoracionResponseDto.idExamenValoracion( entity.getIdExamenValoracion() );
        solicitudExamenValoracionResponseDto.linkFormatoA( entity.getLinkFormatoA() );
        solicitudExamenValoracionResponseDto.linkFormatoD( entity.getLinkFormatoD() );
        solicitudExamenValoracionResponseDto.linkFormatoE( entity.getLinkFormatoE() );
        solicitudExamenValoracionResponseDto.linkOficioDirigidoEvaluadores( entity.getLinkOficioDirigidoEvaluadores() );
        solicitudExamenValoracionResponseDto.titulo( entity.getTitulo() );

        return solicitudExamenValoracionResponseDto.build();
    }
}
