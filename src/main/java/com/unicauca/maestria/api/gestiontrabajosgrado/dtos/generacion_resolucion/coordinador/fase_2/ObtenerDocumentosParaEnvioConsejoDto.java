package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObtenerDocumentosParaEnvioConsejoDto {

    @NotNull
    private String b64FormatoBEv1;

    @NotNull
    private String b64FormatoBEv2;

    @NotNull
    private String b64SolicitudConsejoFacultad;

    @NotNull
    private String b64AnteproyectoFinal;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("formatoBEv1", b64FormatoBEv1);
        documentos.put("formatoBEv2", b64FormatoBEv2);
        documentos.put("solicitudConsejoFacultad", b64SolicitudConsejoFacultad);
        documentos.put("anteproyectoFinal", b64AnteproyectoFinal);
        return documentos;
    }
}
