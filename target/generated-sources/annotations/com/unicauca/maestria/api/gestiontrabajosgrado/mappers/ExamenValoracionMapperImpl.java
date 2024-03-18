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
    date = "2024-03-17T22:06:24-0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class ExamenValoracionMapperImpl implements ExamenValoracionMapper {

    @Override
    public SolicitudExamenValoracion toEntity(ExamenValoracionDto dto) {
        if ( dto == null ) {
            return null;
        }

        SolicitudExamenValoracionBuilder solicitudExamenValoracion = SolicitudExamenValoracion.builder();

        solicitudExamenValoracion.titulo( dto.getTitulo() );
        solicitudExamenValoracion.linkFormatoA( dto.getLinkFormatoA() );
        solicitudExamenValoracion.linkFormatoD( dto.getLinkFormatoD() );
        solicitudExamenValoracion.linkFormatoE( dto.getLinkFormatoE() );
        solicitudExamenValoracion.evaluadorExterno( dto.getEvaluadorExterno() );
        solicitudExamenValoracion.evaluadorInterno( dto.getEvaluadorInterno() );
        solicitudExamenValoracion.fechaActa( dto.getFechaActa() );
        solicitudExamenValoracion.linkOficioDirigidoEvaluadores( dto.getLinkOficioDirigidoEvaluadores() );
        solicitudExamenValoracion.fechaMaximaEvaluacion( dto.getFechaMaximaEvaluacion() );

        return solicitudExamenValoracion.build();
    }

    @Override
    public ExamenValoracionDto toDto(SolicitudExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        ExamenValoracionDtoBuilder examenValoracionDto = ExamenValoracionDto.builder();

        examenValoracionDto.titulo( entity.getTitulo() );
        examenValoracionDto.linkFormatoA( entity.getLinkFormatoA() );
        examenValoracionDto.linkFormatoD( entity.getLinkFormatoD() );
        examenValoracionDto.linkFormatoE( entity.getLinkFormatoE() );
        examenValoracionDto.evaluadorExterno( entity.getEvaluadorExterno() );
        examenValoracionDto.evaluadorInterno( entity.getEvaluadorInterno() );
        examenValoracionDto.fechaActa( entity.getFechaActa() );
        examenValoracionDto.linkOficioDirigidoEvaluadores( entity.getLinkOficioDirigidoEvaluadores() );
        examenValoracionDto.fechaMaximaEvaluacion( entity.getFechaMaximaEvaluacion() );

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
