package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class SolicitudExamenValoracionDocenteResponseListDto{

	private Long idExamenValoracion;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private Map<String, String> evaluadorInterno;
    private Map<String, String> evaluadorExterno;
}
