package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioEmailCorrecionDto {

    @NotNull
    private Long idTrabajoGrado;

    @NotNull
    private String tituloAsunto; 

    @NotNull
    private String mensaje;
}

