package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion.SolicitudExamenValoracionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto.SolicitudExamenValoracionResponseDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto.SolicitudExamenValoracionCoordinadorResponseDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto.SolicitudExamenValoracionDocenteResponseDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-19T19:10:34-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class SolicitudExamenValoracionResponseMapperImpl implements SolicitudExamenValoracionResponseMapper {

    @Override
    public SolicitudExamenValoracion toEntity(SolicitudExamenValoracionResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        SolicitudExamenValoracionBuilder solicitudExamenValoracion = SolicitudExamenValoracion.builder();

        solicitudExamenValoracion.actaAprobacionExamen( dto.getActaAprobacionExamen() );
        solicitudExamenValoracion.evaluadorExterno( dto.getEvaluadorExterno() );
        solicitudExamenValoracion.evaluadorInterno( dto.getEvaluadorInterno() );
        solicitudExamenValoracion.fechaActa( dto.getFechaActa() );
        solicitudExamenValoracion.fechaMaximaEvaluacion( dto.getFechaMaximaEvaluacion() );
        solicitudExamenValoracion.idExamenValoracion( dto.getIdExamenValoracion() );
        solicitudExamenValoracion.linkFormatoA( dto.getLinkFormatoA() );
        solicitudExamenValoracion.linkFormatoD( dto.getLinkFormatoD() );
        solicitudExamenValoracion.linkFormatoE( dto.getLinkFormatoE() );
        solicitudExamenValoracion.linkOficioDirigidoEvaluadores( dto.getLinkOficioDirigidoEvaluadores() );
        solicitudExamenValoracion.titulo( dto.getTitulo() );

        return solicitudExamenValoracion.build();
    }

    @Override
    public SolicitudExamenValoracionResponseDto toDto(SolicitudExamenValoracion entity) {
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

    @Override
    public List<SolicitudExamenValoracion> toEntityList(List<SolicitudExamenValoracionResponseDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<SolicitudExamenValoracion> list = new ArrayList<SolicitudExamenValoracion>( dtos.size() );
        for ( SolicitudExamenValoracionResponseDto solicitudExamenValoracionResponseDto : dtos ) {
            list.add( toEntity( solicitudExamenValoracionResponseDto ) );
        }

        return list;
    }

    @Override
    public List<SolicitudExamenValoracionResponseDto> toDtoList(List<SolicitudExamenValoracion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SolicitudExamenValoracionResponseDto> list = new ArrayList<SolicitudExamenValoracionResponseDto>( entities.size() );
        for ( SolicitudExamenValoracion solicitudExamenValoracion : entities ) {
            list.add( toDto( solicitudExamenValoracion ) );
        }

        return list;
    }

    @Override
    public SolicitudExamenValoracionDocenteResponseDto toDocenteDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionDocenteResponseDtoBuilder solicitudExamenValoracionDocenteResponseDto = SolicitudExamenValoracionDocenteResponseDto.builder();

        solicitudExamenValoracionDocenteResponseDto.evaluadorExterno( entity.getEvaluadorExterno() );
        solicitudExamenValoracionDocenteResponseDto.evaluadorInterno( entity.getEvaluadorInterno() );
        solicitudExamenValoracionDocenteResponseDto.idExamenValoracion( entity.getIdExamenValoracion() );
        solicitudExamenValoracionDocenteResponseDto.linkFormatoA( entity.getLinkFormatoA() );
        solicitudExamenValoracionDocenteResponseDto.linkFormatoD( entity.getLinkFormatoD() );
        solicitudExamenValoracionDocenteResponseDto.linkFormatoE( entity.getLinkFormatoE() );
        solicitudExamenValoracionDocenteResponseDto.titulo( entity.getTitulo() );

        return solicitudExamenValoracionDocenteResponseDto.build();
    }

    @Override
    public SolicitudExamenValoracionDocenteResponseDto toDocenteResponseDto(SolicitudExamenValoracionResponseDto entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionDocenteResponseDtoBuilder solicitudExamenValoracionDocenteResponseDto = SolicitudExamenValoracionDocenteResponseDto.builder();

        solicitudExamenValoracionDocenteResponseDto.evaluadorExterno( entity.getEvaluadorExterno() );
        solicitudExamenValoracionDocenteResponseDto.evaluadorInterno( entity.getEvaluadorInterno() );
        solicitudExamenValoracionDocenteResponseDto.idExamenValoracion( entity.getIdExamenValoracion() );
        solicitudExamenValoracionDocenteResponseDto.linkFormatoA( entity.getLinkFormatoA() );
        solicitudExamenValoracionDocenteResponseDto.linkFormatoD( entity.getLinkFormatoD() );
        solicitudExamenValoracionDocenteResponseDto.linkFormatoE( entity.getLinkFormatoE() );
        solicitudExamenValoracionDocenteResponseDto.titulo( entity.getTitulo() );

        return solicitudExamenValoracionDocenteResponseDto.build();
    }

    @Override
    public SolicitudExamenValoracionCoordinadorResponseDto toCoordinadorDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionCoordinadorResponseDtoBuilder solicitudExamenValoracionCoordinadorResponseDto = SolicitudExamenValoracionCoordinadorResponseDto.builder();

        solicitudExamenValoracionCoordinadorResponseDto.actaAprobacionExamen( entity.getActaAprobacionExamen() );
        solicitudExamenValoracionCoordinadorResponseDto.fechaActa( entity.getFechaActa() );
        solicitudExamenValoracionCoordinadorResponseDto.fechaMaximaEvaluacion( entity.getFechaMaximaEvaluacion() );
        solicitudExamenValoracionCoordinadorResponseDto.idExamenValoracion( entity.getIdExamenValoracion() );
        solicitudExamenValoracionCoordinadorResponseDto.linkOficioDirigidoEvaluadores( entity.getLinkOficioDirigidoEvaluadores() );

        return solicitudExamenValoracionCoordinadorResponseDto.build();
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
