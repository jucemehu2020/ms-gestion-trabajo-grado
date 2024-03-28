package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion.SustentacionTrabajoInvestigacionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto.SustentacionTrabajoInvestigacionDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-28T00:13:02-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240206-1609, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class SustentacionProyectoInvestigacionMapperImpl implements SustentacionProyectoInvestigacionMapper {

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
}
