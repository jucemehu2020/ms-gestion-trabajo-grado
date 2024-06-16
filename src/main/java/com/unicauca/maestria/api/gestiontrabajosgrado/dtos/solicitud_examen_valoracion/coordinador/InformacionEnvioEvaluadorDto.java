package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEnvioEvaluadorDto {

    private String formatoD;
    private String formatoE;
    private List<String> anexos;
    private String formatoB;
    private String formatoC;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("formatoD", formatoD);
        documentos.put("formatoE", formatoE);
        documentos.put("anexos", anexos);
        documentos.put("formatoB", formatoB);
        documentos.put("formatoC", formatoC);
        return documentos;
    }
}
