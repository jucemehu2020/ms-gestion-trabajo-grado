package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente;

import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteResponseDto {

	private Long id;
	private PersonaDto persona;
	private List<TituloDto> titulos;

	public String getUltimaUniversidad() {
        if (titulos != null && !titulos.isEmpty()) {
            return titulos.get(titulos.size() - 1).getUniversidad();
        }
        return "Universidad del Cauca";
    }
}
