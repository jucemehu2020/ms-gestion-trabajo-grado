package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import java.time.LocalDate;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionResponseDto {
    
    private Long id;
    private String titulo;
    private Long director;
    private Long codirector;
    private String linkAnteproyectoFinal;
    private String linkSolicitudComite;
    private ConceptoVerificacion conceptoDocumentosCoordinador;
    private List<RespuestaComiteGeneracionResolucion> actaFechaRespuestaComite;
    private String linkSolicitudConsejo;
    private String numeroActaConsejo;
    private LocalDate fechaActaConsejo;
    private String linkOficioConsejo;
}
