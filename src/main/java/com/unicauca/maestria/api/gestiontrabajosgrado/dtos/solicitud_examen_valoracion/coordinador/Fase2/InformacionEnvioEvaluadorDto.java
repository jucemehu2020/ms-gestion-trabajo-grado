package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

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

    private String b64FormatoD;
    private String b64FormatoE;
    private List<String> b64Anexos;
    private String b64Oficio;
    private String b64FormatoB;
    private String b64FormatoCEv1;
    private String b64FormatoCEv2;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("formatoD", b64FormatoD);
        documentos.put("formatoE", b64FormatoE);
        documentos.put("anexos", b64Anexos);
        documentos.put("oficio", b64Oficio);
        documentos.put("formatoB", b64FormatoB);
        documentos.put("formatoCEv1", b64FormatoCEv1);
        documentos.put("formatoCEv2", b64FormatoCEv2);
        return documentos;
    }
}
