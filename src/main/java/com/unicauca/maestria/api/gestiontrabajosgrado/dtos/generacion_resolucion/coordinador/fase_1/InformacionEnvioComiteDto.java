package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import java.util.HashMap;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEnvioComiteDto {

    @NonNull
    private String asunto;

    @NonNull
    private String mensaje;

    @NonNull
    private String anteproyectoFinal;

    @NonNull
    private String solicitudComite;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("anteproyectoFinal", anteproyectoFinal);
        documentos.put("solicitudComite", solicitudComite);
        return documentos;
    }
}
