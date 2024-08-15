package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDto {

    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private Long director;

    @NotNull
    private Long codirector;

    @NotNull
    private String linkAnteproyectoFinal;

    @NotNull
    private String linkSolicitudComite;

    @NotNull
    private ConceptoVerificacion conceptoDocumentosCoordinador;

    @NotNull
    private List<RespuestaComiteGeneracionResolucion> actaFechaRespuestaComite;

    @NotNull
    private String linkSolicitudConsejo;

    @NotNull
    private String numeroActaConsejo;

    @NotNull
    private LocalDate fechaActaConsejo;

    @NotNull
    private String linkOficioConsejo;
}
