package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.AnexoSolicitudExamenValoracionDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionResponseDto {

    private Long id;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private List<AnexoSolicitudExamenValoracionDto> anexos = new ArrayList<>();;
    private Long idEvaluadorInterno;
    private Long idEvaluadorExterno;
    private ConceptoVerificacion conceptoCoordinadorDocumentos;
    private List<RespuestaComiteExamenValoracion> actaFechaRespuestaComite = new ArrayList<>();;
    private String linkOficioDirigidoEvaluadores;
    private LocalDate fechaMaximaEvaluacion;
}
