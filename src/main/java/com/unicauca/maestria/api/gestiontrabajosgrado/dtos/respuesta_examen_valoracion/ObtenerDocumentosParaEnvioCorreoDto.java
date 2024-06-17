package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import java.util.ArrayList;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObtenerDocumentosParaEnvioCorreoDto {

    private String base64FormatoB;
    private String base64FormatoC;
    private String base64Observaciones;
    private ArrayList<String> base64Anexos;

}
