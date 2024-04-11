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
    date = "2024-04-10T20:38:10-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240325-1403, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class GeneracionResolucionMapperImpl implements GeneracionResolucionMapper {

    @Override
    public GeneracionResolucion toEntity(GeneracionResolucionDto dto) {
        if ( dto == null ) {
            return null;
        }

        GeneracionResolucionBuilder generacionResolucion = GeneracionResolucion.builder();

        generacionResolucion.codirector( dto.getCodirector() );
        generacionResolucion.director( dto.getDirector() );
        generacionResolucion.fechaActa( dto.getFechaActa() );
        generacionResolucion.fechaResolucion( dto.getFechaResolucion() );
        generacionResolucion.linkAnteproyectoAprobado( dto.getLinkAnteproyectoAprobado() );
        generacionResolucion.linkResolucionGeneradaCF( dto.getLinkResolucionGeneradaCF() );
        generacionResolucion.linkSolicitudComite( dto.getLinkSolicitudComite() );
        generacionResolucion.linkSolicitudConcejoFacultad( dto.getLinkSolicitudConcejoFacultad() );
        generacionResolucion.numeroActaRevision( dto.getNumeroActaRevision() );
        generacionResolucion.numeroResolucionGeneradaCF( dto.getNumeroResolucionGeneradaCF() );
        generacionResolucion.titulo( dto.getTitulo() );

        return generacionResolucion.build();
    }

    @Override
    public GeneracionResolucionDto toDto(GeneracionResolucion entity) {
        if ( entity == null ) {
            return null;
        }

        GeneracionResolucionDtoBuilder generacionResolucionDto = GeneracionResolucionDto.builder();

        generacionResolucionDto.codirector( entity.getCodirector() );
        generacionResolucionDto.director( entity.getDirector() );
        generacionResolucionDto.fechaActa( entity.getFechaActa() );
        generacionResolucionDto.fechaResolucion( entity.getFechaResolucion() );
        generacionResolucionDto.linkAnteproyectoAprobado( entity.getLinkAnteproyectoAprobado() );
        generacionResolucionDto.linkResolucionGeneradaCF( entity.getLinkResolucionGeneradaCF() );
        generacionResolucionDto.linkSolicitudComite( entity.getLinkSolicitudComite() );
        generacionResolucionDto.linkSolicitudConcejoFacultad( entity.getLinkSolicitudConcejoFacultad() );
        generacionResolucionDto.numeroActaRevision( entity.getNumeroActaRevision() );
        generacionResolucionDto.numeroResolucionGeneradaCF( entity.getNumeroResolucionGeneradaCF() );
        generacionResolucionDto.titulo( entity.getTitulo() );

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
