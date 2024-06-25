package com.unicauca.maestria.api.gestiontrabajosgrado.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioEmailDto {

    @NonNull
    private String asunto;

    @NonNull
    private String mensaje;
}
