package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion.SolicitudExamenValoracionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto.SolicitudExamenValoracionDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto.SolicitudExamenValoracionCoordinadorDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto.SolicitudExamenValoracionDocenteDtoBuilder;
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
public class SolicitudExamenValoracionMapperImpl implements SolicitudExamenValoracionMapper {

    @Override
    public SolicitudExamenValoracion toEntity(SolicitudExamenValoracionDto dto) {
        if ( dto == null ) {
            return null;
        }

        SolicitudExamenValoracionBuilder solicitudExamenValoracion = SolicitudExamenValoracion.builder();

        solicitudExamenValoracion.actaAprobacionExamen( dto.getActaAprobacionExamen() );
        solicitudExamenValoracion.evaluadorExterno( dto.getEvaluadorExterno() );
        solicitudExamenValoracion.evaluadorInterno( dto.getEvaluadorInterno() );
        solicitudExamenValoracion.fechaActa( dto.getFechaActa() );
        solicitudExamenValoracion.fechaMaximaEvaluacion( dto.getFechaMaximaEvaluacion() );
        solicitudExamenValoracion.linkFormatoA( dto.getLinkFormatoA() );
        solicitudExamenValoracion.linkFormatoD( dto.getLinkFormatoD() );
        solicitudExamenValoracion.linkFormatoE( dto.getLinkFormatoE() );
        solicitudExamenValoracion.linkOficioDirigidoEvaluadores( dto.getLinkOficioDirigidoEvaluadores() );
        solicitudExamenValoracion.titulo( dto.getTitulo() );

        return solicitudExamenValoracion.build();
    }

    @Override
    public SolicitudExamenValoracionDto toDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionDtoBuilder solicitudExamenValoracionDto = SolicitudExamenValoracionDto.builder();

        solicitudExamenValoracionDto.actaAprobacionExamen( entity.getActaAprobacionExamen() );
        solicitudExamenValoracionDto.evaluadorExterno( entity.getEvaluadorExterno() );
        solicitudExamenValoracionDto.evaluadorInterno( entity.getEvaluadorInterno() );
        solicitudExamenValoracionDto.fechaActa( entity.getFechaActa() );
        solicitudExamenValoracionDto.fechaMaximaEvaluacion( entity.getFechaMaximaEvaluacion() );
        solicitudExamenValoracionDto.linkFormatoA( entity.getLinkFormatoA() );
        solicitudExamenValoracionDto.linkFormatoD( entity.getLinkFormatoD() );
        solicitudExamenValoracionDto.linkFormatoE( entity.getLinkFormatoE() );
        solicitudExamenValoracionDto.linkOficioDirigidoEvaluadores( entity.getLinkOficioDirigidoEvaluadores() );
        solicitudExamenValoracionDto.titulo( entity.getTitulo() );

        return solicitudExamenValoracionDto.build();
    }

    @Override
    public List<SolicitudExamenValoracion> toEntityList(List<SolicitudExamenValoracionDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<SolicitudExamenValoracion> list = new ArrayList<SolicitudExamenValoracion>( dtos.size() );
        for ( SolicitudExamenValoracionDto solicitudExamenValoracionDto : dtos ) {
            list.add( toEntity( solicitudExamenValoracionDto ) );
        }

        return list;
    }

    @Override
    public List<SolicitudExamenValoracionDto> toDtoList(List<SolicitudExamenValoracion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SolicitudExamenValoracionDto> list = new ArrayList<SolicitudExamenValoracionDto>( entities.size() );
        for ( SolicitudExamenValoracion solicitudExamenValoracion : entities ) {
            list.add( toDto( solicitudExamenValoracion ) );
        }

        return list;
    }

    @Override
    public SolicitudExamenValoracion toEntity(SolicitudExamenValoracionDocenteDto docenteDto) {
        if ( docenteDto == null ) {
            return null;
        }

        SolicitudExamenValoracionBuilder solicitudExamenValoracion = SolicitudExamenValoracion.builder();

        solicitudExamenValoracion.evaluadorExterno( docenteDto.getEvaluadorExterno() );
        solicitudExamenValoracion.evaluadorInterno( docenteDto.getEvaluadorInterno() );
        solicitudExamenValoracion.linkFormatoA( docenteDto.getLinkFormatoA() );
        solicitudExamenValoracion.linkFormatoD( docenteDto.getLinkFormatoD() );
        solicitudExamenValoracion.linkFormatoE( docenteDto.getLinkFormatoE() );
        solicitudExamenValoracion.titulo( docenteDto.getTitulo() );

        return solicitudExamenValoracion.build();
    }

    @Override
    public SolicitudExamenValoracionDocenteDto toDocenteDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionDocenteDtoBuilder solicitudExamenValoracionDocenteDto = SolicitudExamenValoracionDocenteDto.builder();

        solicitudExamenValoracionDocenteDto.evaluadorExterno( entity.getEvaluadorExterno() );
        solicitudExamenValoracionDocenteDto.evaluadorInterno( entity.getEvaluadorInterno() );
        solicitudExamenValoracionDocenteDto.linkFormatoA( entity.getLinkFormatoA() );
        solicitudExamenValoracionDocenteDto.linkFormatoD( entity.getLinkFormatoD() );
        solicitudExamenValoracionDocenteDto.linkFormatoE( entity.getLinkFormatoE() );
        solicitudExamenValoracionDocenteDto.titulo( entity.getTitulo() );

        return solicitudExamenValoracionDocenteDto.build();
    }

    @Override
    public SolicitudExamenValoracion toEntity(SolicitudExamenValoracionCoordinadorDto coordinadorDto) {
        if ( coordinadorDto == null ) {
            return null;
        }

        SolicitudExamenValoracionBuilder solicitudExamenValoracion = SolicitudExamenValoracion.builder();

        solicitudExamenValoracion.actaAprobacionExamen( coordinadorDto.getActaAprobacionExamen() );
        solicitudExamenValoracion.fechaActa( coordinadorDto.getFechaActa() );
        solicitudExamenValoracion.fechaMaximaEvaluacion( coordinadorDto.getFechaMaximaEvaluacion() );
        solicitudExamenValoracion.linkOficioDirigidoEvaluadores( coordinadorDto.getLinkOficioDirigidoEvaluadores() );

        return solicitudExamenValoracion.build();
    }

    @Override
    public SolicitudExamenValoracionCoordinadorDto toCoordinadorDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        SolicitudExamenValoracionCoordinadorDtoBuilder solicitudExamenValoracionCoordinadorDto = SolicitudExamenValoracionCoordinadorDto.builder();

        solicitudExamenValoracionCoordinadorDto.actaAprobacionExamen( entity.getActaAprobacionExamen() );
        solicitudExamenValoracionCoordinadorDto.fechaActa( entity.getFechaActa() );
        solicitudExamenValoracionCoordinadorDto.fechaMaximaEvaluacion( entity.getFechaMaximaEvaluacion() );
        solicitudExamenValoracionCoordinadorDto.linkOficioDirigidoEvaluadores( entity.getLinkOficioDirigidoEvaluadores() );

        return solicitudExamenValoracionCoordinadorDto.build();
    }
}
