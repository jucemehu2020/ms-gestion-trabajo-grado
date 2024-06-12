package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import java.util.HashMap;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEnvioDto {
    
    private String asunto;
    private String mensaje;
    private String formatoB;
    private String formatoC;

    public Map<String, String> getDocumentos() {
        Map<String, String> documentos = new HashMap<>();
        documentos.put("formatoB", formatoB);
        documentos.put("formatoC", formatoC);
        return documentos;
    }
}
