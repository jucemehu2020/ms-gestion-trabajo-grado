package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioEmailComiteDto {

    @NotNull
    private Long idTrabajoGrados;

    @NonNull
    private String asunto;

    @NonNull
    private String mensaje;
}
