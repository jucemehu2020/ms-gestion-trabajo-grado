package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioEmailCorrecionesDto {

    @NotNull
    private Long idTrabajoGrados;

    @NonNull
    private String asunto;

    @NonNull
    private String mensaje;
}
