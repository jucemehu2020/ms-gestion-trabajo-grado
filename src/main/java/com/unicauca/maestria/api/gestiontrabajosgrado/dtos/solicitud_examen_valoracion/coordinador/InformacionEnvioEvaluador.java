package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import java.util.HashMap;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEnvioEvaluador {

    private String asunto;
    private String mensaje;
    private String formatoD;
    private String formatoE;
    private String anexos;
    private String formatoB;
    private String formatoC;

    public Map<String, String> getDocumentos() {
        Map<String, String> documentos = new HashMap<>();
        documentos.put("formatoD", formatoD);
        documentos.put("formatoE", formatoE);
        documentos.put("anexos", anexos);
        documentos.put("formatoB", formatoB);
        documentos.put("formatoC", formatoC);
        return documentos;
    }
}
