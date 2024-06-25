package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionResponseDto {

    private Long idExamenValoracion;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private List<AnexoSolicitudExamenValoracion> anexos = new ArrayList<>();;
    private Map<String, String> evaluadorInterno;
    private Map<String, String> evaluadorExterno;
    private Boolean conceptoCoordinadorDocumentos;
    private List<RespuestaComiteExamenValoracion> actaFechaRespuestaComite = new ArrayList<>();;
    private String linkOficioDirigidoEvaluadores;
    private LocalDate fechaMaximaEvaluacion;
}
