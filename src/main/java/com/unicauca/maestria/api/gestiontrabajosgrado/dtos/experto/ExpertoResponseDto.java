package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertoResponseDto {

    private Long id;
    
    private PersonaDto persona;

    private String universidadtitexp;
}
