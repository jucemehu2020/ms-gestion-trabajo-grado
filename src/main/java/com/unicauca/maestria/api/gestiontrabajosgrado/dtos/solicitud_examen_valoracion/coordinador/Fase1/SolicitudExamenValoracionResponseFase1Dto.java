package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudExamenValoracionResponseFase1Dto {

    private Long idExamenValoracion;
    private ConceptoVerificacion conceptoCoordinadorDocumentos;
}
