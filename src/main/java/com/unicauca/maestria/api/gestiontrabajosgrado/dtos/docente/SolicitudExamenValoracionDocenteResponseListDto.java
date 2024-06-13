package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente;

import java.util.Map;

import lombok.*;

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
    private String linkAnexos;
    private Map<String, String> evaluadorInterno;
    private Map<String, String> evaluadorExterno;
}
