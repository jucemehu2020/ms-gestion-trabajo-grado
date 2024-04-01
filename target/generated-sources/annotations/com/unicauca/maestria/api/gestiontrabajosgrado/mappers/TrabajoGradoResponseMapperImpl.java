package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado.TrabajoGradoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto.TrabajoGradoResponseDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-01T09:31:29-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240206-1609, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class TrabajoGradoResponseMapperImpl implements TrabajoGradoResponseMapper {

    @Override
    public TrabajoGrado toEntity(TrabajoGradoResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        TrabajoGradoBuilder trabajoGrado = TrabajoGrado.builder();

        trabajoGrado.fechaCreacion( dto.getFechaCreacion() );
        trabajoGrado.id( dto.getId() );
        trabajoGrado.numeroEstado( dto.getNumeroEstado() );
        trabajoGrado.titulo( dto.getTitulo() );

        return trabajoGrado.build();
    }

    @Override
    public TrabajoGradoResponseDto toDto(TrabajoGrado entity) {
        if ( entity == null ) {
            return null;
        }

        TrabajoGradoResponseDtoBuilder trabajoGradoResponseDto = TrabajoGradoResponseDto.builder();

        trabajoGradoResponseDto.fechaCreacion( entity.getFechaCreacion() );
        trabajoGradoResponseDto.id( entity.getId() );
        trabajoGradoResponseDto.numeroEstado( entity.getNumeroEstado() );
        trabajoGradoResponseDto.titulo( entity.getTitulo() );

        return trabajoGradoResponseDto.build();
    }

    @Override
    public List<TrabajoGrado> toEntityList(List<TrabajoGradoResponseDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<TrabajoGrado> list = new ArrayList<TrabajoGrado>( dtos.size() );
        for ( TrabajoGradoResponseDto trabajoGradoResponseDto : dtos ) {
            list.add( toEntity( trabajoGradoResponseDto ) );
        }

        return list;
    }

    @Override
    public List<TrabajoGradoResponseDto> toDtoList(List<TrabajoGrado> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TrabajoGradoResponseDto> list = new ArrayList<TrabajoGradoResponseDto>( entities.size() );
        for ( TrabajoGrado trabajoGrado : entities ) {
            list.add( toDto( trabajoGrado ) );
        }

        return list;
    }
}
