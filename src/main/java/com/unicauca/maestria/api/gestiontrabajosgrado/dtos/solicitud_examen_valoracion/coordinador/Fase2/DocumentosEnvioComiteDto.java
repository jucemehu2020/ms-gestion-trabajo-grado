package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentosEnvioComiteDto {

    @NotBlank
    private String b64FormatoA;

    @NotBlank
    private String b64FormatoD;
    
    @NotBlank
    private String b64FormatoE;

    @NotBlank
    private List<String> b64Anexos;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("formatoA", b64FormatoA);
        documentos.put("formatoD", b64FormatoD);
        documentos.put("formatoE", b64FormatoE);
        documentos.put("anexos", b64Anexos);
        return documentos;
    }
}
