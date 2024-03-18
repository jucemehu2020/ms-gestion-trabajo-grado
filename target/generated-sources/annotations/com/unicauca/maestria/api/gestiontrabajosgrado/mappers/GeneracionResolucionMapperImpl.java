package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion.GeneracionResolucionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto.GeneracionResolucionDtoBuilder;
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
public class GeneracionResolucionMapperImpl implements GeneracionResolucionMapper {

    @Override
    public GeneracionResolucion toEntity(GeneracionResolucionDto dto) {
        if ( dto == null ) {
            return null;
        }

        GeneracionResolucionBuilder generacionResolucion = GeneracionResolucion.builder();

        generacionResolucion.titulo( dto.getTitulo() );
        generacionResolucion.director( dto.getDirector() );
        generacionResolucion.codirector( dto.getCodirector() );
        generacionResolucion.numeroActaRevision( dto.getNumeroActaRevision() );
        generacionResolucion.fechaActa( dto.getFechaActa() );
        generacionResolucion.linkAnteproyectoAprobado( dto.getLinkAnteproyectoAprobado() );
        generacionResolucion.linkSolicitudComite( dto.getLinkSolicitudComite() );
        generacionResolucion.linkSolicitudConcejoFacultad( dto.getLinkSolicitudConcejoFacultad() );
        generacionResolucion.numeroResolucionGeneradaCF( dto.getNumeroResolucionGeneradaCF() );
        generacionResolucion.fechaResolucion( dto.getFechaResolucion() );
        generacionResolucion.linkResolucionGeneradaCF( dto.getLinkResolucionGeneradaCF() );

        return generacionResolucion.build();
    }

    @Override
    public GeneracionResolucionDto toDto(GeneracionResolucion entity) {
        if ( entity == null ) {
            return null;
        }

        GeneracionResolucionDtoBuilder generacionResolucionDto = GeneracionResolucionDto.builder();

        generacionResolucionDto.titulo( entity.getTitulo() );
        generacionResolucionDto.director( entity.getDirector() );
        generacionResolucionDto.codirector( entity.getCodirector() );
        generacionResolucionDto.numeroActaRevision( entity.getNumeroActaRevision() );
        generacionResolucionDto.fechaActa( entity.getFechaActa() );
        generacionResolucionDto.linkAnteproyectoAprobado( entity.getLinkAnteproyectoAprobado() );
        generacionResolucionDto.linkSolicitudComite( entity.getLinkSolicitudComite() );
        generacionResolucionDto.linkSolicitudConcejoFacultad( entity.getLinkSolicitudConcejoFacultad() );
        generacionResolucionDto.numeroResolucionGeneradaCF( entity.getNumeroResolucionGeneradaCF() );
        generacionResolucionDto.fechaResolucion( entity.getFechaResolucion() );
        generacionResolucionDto.linkResolucionGeneradaCF( entity.getLinkResolucionGeneradaCF() );

        return generacionResolucionDto.build();
    }

    @Override
    public List<GeneracionResolucion> toEntityList(List<GeneracionResolucionDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<GeneracionResolucion> list = new ArrayList<GeneracionResolucion>( dtos.size() );
        for ( GeneracionResolucionDto generacionResolucionDto : dtos ) {
            list.add( toEntity( generacionResolucionDto ) );
        }

        return list;
    }

    @Override
    public List<GeneracionResolucionDto> toDtoList(List<GeneracionResolucion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GeneracionResolucionDto> list = new ArrayList<GeneracionResolucionDto>( entities.size() );
        for ( GeneracionResolucion generacionResolucion : entities ) {
            list.add( toDto( generacionResolucion ) );
        }

        return list;
    }
}
