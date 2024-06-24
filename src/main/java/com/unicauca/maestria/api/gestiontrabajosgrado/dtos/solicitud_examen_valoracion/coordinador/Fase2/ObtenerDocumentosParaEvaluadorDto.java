package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import java.util.ArrayList;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObtenerDocumentosParaEvaluadorDto {
    
    private String base64FormatoD;
    private String base64FormatoE;
    private ArrayList<String> base64Anexos;

}
