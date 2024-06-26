package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.util.HashMap;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObtenerDocumentosParaEnvioDto {

    @NonNull
    private String b64FormatoG;

    @NonNull
    private String b64HistoriaAcademica;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("formatoG", b64FormatoG);
        documentos.put("historiaAcademica", b64HistoriaAcademica);
        return documentos;
    }
}
