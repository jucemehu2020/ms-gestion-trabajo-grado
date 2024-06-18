package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.EnvioEmailCorrecionesDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase2Dto {
    
    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String numeroActaSolicitudComite;

    @NotNull
    private LocalDate fechaActaSolicitudComite;

    @NotNull
    private String linkSolicitudConsejoFacultad;

    @NotNull
    private EnvioEmailCorrecionesDto envioEmailCorrecionesDto;

}
