package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventosIdsDto {

	private Long idSolicitudExamenValoracion;
	private List<Long> idRespuestaExamenValoracion;
	private Long idGeneracionResolucion;
	private Long idSustentacion;

}
