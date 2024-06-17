package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEnvioDto {
    private String asunto;
    private String mensaje;
}
