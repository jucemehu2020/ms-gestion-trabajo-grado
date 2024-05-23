package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExpertoResponseDto {

    private Long id;
    
    private PersonaDto persona;

    private String universidad;
}
