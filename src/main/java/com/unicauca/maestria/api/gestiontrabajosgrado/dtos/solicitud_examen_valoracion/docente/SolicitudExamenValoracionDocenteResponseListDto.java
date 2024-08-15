package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente;

import java.util.ArrayList;
import java.util.Map;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionDocenteResponseListDto{

	private Long id;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private ArrayList<String> anexos = new ArrayList<>();
    private Map<String, String> evaluadorInterno;
    private Map<String, String> evaluadorExterno;
}
