package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObtenerDocumentosParaEnvioDto {

    @NotNull
    private String base64AnteproyectoFinal;

    @NotNull
    private String base64SolicitudComite;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("anteproyectoFinal", base64AnteproyectoFinal);
        documentos.put("solicitudComite", base64SolicitudComite);
        return documentos;
    }

}
