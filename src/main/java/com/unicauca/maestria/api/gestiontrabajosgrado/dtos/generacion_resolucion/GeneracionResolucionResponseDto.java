package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionResponseDto {
    
    private Long id;
    private String titulo;
    private String director;
    private String codirector;
    private String numeroActaRevision;
    private LocalDate fechaActa;
    private String linkAnteproyectoAprobado;
    private String linkSolicitudComite;
    private ConceptoVerificacion conceptoDocumentosCoordinador;
    private String linkSolicitudConcejoFacultad;
    private String numeroResolucionGeneradaCF;
    private LocalDate fechaResolucion;
    private String linkResolucionGeneradaCF;
    private String linkOficioConsejo;
}
