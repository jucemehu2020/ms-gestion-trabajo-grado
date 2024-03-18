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
    date = "2024-03-17T22:06:24-0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class SustentacionProyectoInvestigacionMapperImpl implements SustentacionProyectoInvestigacionMapper {

    @Override
    public SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionDto dto) {
        if ( dto == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionBuilder sustentacionTrabajoInvestigacion = SustentacionTrabajoInvestigacion.builder();

        sustentacionTrabajoInvestigacion.linkRemisionDocumentoFinal( dto.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacion.urlDocumentacion( dto.getUrlDocumentacion() );
        sustentacionTrabajoInvestigacion.linkRemisionDocumentoFinalCF( dto.getLinkRemisionDocumentoFinalCF() );
        sustentacionTrabajoInvestigacion.linkConstanciaDocumentoFinal( dto.getLinkConstanciaDocumentoFinal() );
        sustentacionTrabajoInvestigacion.linkActaSustentacion( dto.getLinkActaSustentacion() );
        sustentacionTrabajoInvestigacion.linkActaSustentacionPublica( dto.getLinkActaSustentacionPublica() );
        sustentacionTrabajoInvestigacion.respuestaSustentacion( dto.getRespuestaSustentacion() );
        sustentacionTrabajoInvestigacion.linkEstudioHojaVidaAcademica( dto.getLinkEstudioHojaVidaAcademica() );
        sustentacionTrabajoInvestigacion.numeroActaTrabajoFinal( dto.getNumeroActaTrabajoFinal() );
        sustentacionTrabajoInvestigacion.fechaActa( dto.getFechaActa() );

        return sustentacionTrabajoInvestigacion.build();
    }

    @Override
    public SustentacionTrabajoInvestigacionDto toDto(SustentacionTrabajoInvestigacion entity) {
        if ( entity == null ) {
            return null;
        }

        SustentacionTrabajoInvestigacionDtoBuilder sustentacionTrabajoInvestigacionDto = SustentacionTrabajoInvestigacionDto.builder();

        sustentacionTrabajoInvestigacionDto.linkRemisionDocumentoFinal( entity.getLinkRemisionDocumentoFinal() );
        sustentacionTrabajoInvestigacionDto.urlDocumentacion( entity.getUrlDocumentacion() );
        sustentacionTrabajoInvestigacionDto.linkRemisionDocumentoFinalCF( entity.getLinkRemisionDocumentoFinalCF() );
        sustentacionTrabajoInvestigacionDto.linkConstanciaDocumentoFinal( entity.getLinkConstanciaDocumentoFinal() );
        sustentacionTrabajoInvestigacionDto.linkActaSustentacion( entity.getLinkActaSustentacion() );
        sustentacionTrabajoInvestigacionDto.linkActaSustentacionPublica( entity.getLinkActaSustentacionPublica() );
        sustentacionTrabajoInvestigacionDto.respuestaSustentacion( entity.getRespuestaSustentacion() );
        sustentacionTrabajoInvestigacionDto.linkEstudioHojaVidaAcademica( entity.getLinkEstudioHojaVidaAcademica() );
        sustentacionTrabajoInvestigacionDto.numeroActaTrabajoFinal( entity.getNumeroActaTrabajoFinal() );
        sustentacionTrabajoInvestigacionDto.fechaActa( entity.getFechaActa() );

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
