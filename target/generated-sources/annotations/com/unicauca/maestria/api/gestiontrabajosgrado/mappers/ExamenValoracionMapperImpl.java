package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion.SolicitudExamenValoracionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto.ExamenValoracionDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-17T20:02:31-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240206-1609, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class ExamenValoracionMapperImpl implements ExamenValoracionMapper {

    @Override
    public SolicitudExamenValoracion toEntity(ExamenValoracionDto dto) {
        if ( dto == null ) {
            return null;
        }

        SolicitudExamenValoracionBuilder solicitudExamenValoracion = SolicitudExamenValoracion.builder();

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
    public ExamenValoracionDto toDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        ExamenValoracionDtoBuilder examenValoracionDto = ExamenValoracionDto.builder();

        examenValoracionDto.evaluadorExterno( entity.getEvaluadorExterno() );
        examenValoracionDto.evaluadorInterno( entity.getEvaluadorInterno() );
        examenValoracionDto.fechaActa( entity.getFechaActa() );
        examenValoracionDto.fechaMaximaEvaluacion( entity.getFechaMaximaEvaluacion() );
        examenValoracionDto.linkFormatoA( entity.getLinkFormatoA() );
        examenValoracionDto.linkFormatoD( entity.getLinkFormatoD() );
        examenValoracionDto.linkFormatoE( entity.getLinkFormatoE() );
        examenValoracionDto.linkOficioDirigidoEvaluadores( entity.getLinkOficioDirigidoEvaluadores() );
        examenValoracionDto.titulo( entity.getTitulo() );

        return examenValoracionDto.build();
    }

    @Override
    public List<SolicitudExamenValoracion> toEntityList(List<ExamenValoracionDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<SolicitudExamenValoracion> list = new ArrayList<SolicitudExamenValoracion>( dtos.size() );
        for ( ExamenValoracionDto examenValoracionDto : dtos ) {
            list.add( toEntity( examenValoracionDto ) );
        }

        return list;
    }

    @Override
    public List<ExamenValoracionDto> toDtoList(List<SolicitudExamenValoracion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ExamenValoracionDto> list = new ArrayList<ExamenValoracionDto>( entities.size() );
        for ( SolicitudExamenValoracion solicitudExamenValoracion : entities ) {
            list.add( toDto( solicitudExamenValoracion ) );
        }

        return list;
    }
}
