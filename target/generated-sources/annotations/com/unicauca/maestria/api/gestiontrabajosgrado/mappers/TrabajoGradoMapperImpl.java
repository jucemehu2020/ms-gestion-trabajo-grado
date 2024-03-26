package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado.TrabajoGradoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoDto.TrabajoGradoDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-26T09:19:48-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240206-1609, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class TrabajoGradoMapperImpl implements TrabajoGradoMapper {

    @Override
    public TrabajoGrado toEntity(TrabajoGradoDto dto) {
        if ( dto == null ) {
            return null;
        }

        TrabajoGradoBuilder trabajoGrado = TrabajoGrado.builder();

        trabajoGrado.estado( dto.getEstado() );
        trabajoGrado.fechaCreacion( dto.getFechaCreacion() );
        trabajoGrado.id( dto.getId() );
        trabajoGrado.numeroEstado( dto.getNumeroEstado() );

        return trabajoGrado.build();
    }

    @Override
    public TrabajoGradoDto toDto(TrabajoGrado entity) {
        if ( entity == null ) {
            return null;
        }

        TrabajoGradoDtoBuilder trabajoGradoDto = TrabajoGradoDto.builder();

        trabajoGradoDto.estado( entity.getEstado() );
        trabajoGradoDto.fechaCreacion( entity.getFechaCreacion() );
        trabajoGradoDto.id( entity.getId() );
        trabajoGradoDto.numeroEstado( entity.getNumeroEstado() );

        return trabajoGradoDto.build();
    }

    @Override
    public List<TrabajoGrado> toEntityList(List<TrabajoGradoDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<TrabajoGrado> list = new ArrayList<TrabajoGrado>( dtos.size() );
        for ( TrabajoGradoDto trabajoGradoDto : dtos ) {
            list.add( toEntity( trabajoGradoDto ) );
        }

        return list;
    }

    @Override
    public List<TrabajoGradoDto> toDtoList(List<TrabajoGrado> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TrabajoGradoDto> list = new ArrayList<TrabajoGradoDto>( entities.size() );
        for ( TrabajoGrado trabajoGrado : entities ) {
            list.add( toDto( trabajoGrado ) );
        }

        return list;
    }
}
