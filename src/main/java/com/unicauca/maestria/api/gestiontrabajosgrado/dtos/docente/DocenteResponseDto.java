package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente;

import java.util.ArrayList;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.EscalafonDocente;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.EstadoPersona;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.TipoVinculacion;
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
	private PersonaDto persona;
}
