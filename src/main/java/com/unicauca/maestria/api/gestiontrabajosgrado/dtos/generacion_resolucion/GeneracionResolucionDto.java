package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDto {

    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String director;

    @NotNull
    private String codirector;

    @NotNull
    private String linkAnteproyectoFinal;

    @NotNull
    private String linkSolicitudComite;

    @NotNull
    private ConceptoVerificacion conceptoDocumentosCoordinador;

    @NotNull
    private String numeroActaSolicitudComite;

    @NotNull
    private LocalDate fechaActaSolicitudComite;

    @NotNull
    private String linkSolicitudConsejoFacultad;

    @NotNull
    private String numeroActaConsejoFacultad;

    @NotNull
    private LocalDate fechaActaConsejoFacultad;

    @NotNull
    private String linkOficioConsejo;
}
