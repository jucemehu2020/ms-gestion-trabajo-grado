package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEnvioConsejoDto {

    @NotNull
    private String b64Monografia;

    @NotNull
    private List<String> b64Anexos;

    @NotNull
    private String b64FormatoG;

    @NotNull
    private String b64HistoriaAcademica;

    @NotNull
    private String b64AnteproyectoFinal;

    public Map<String, Object> getDocumentos() {
        Map<String, Object> documentos = new HashMap<>();
        documentos.put("monografia", b64Monografia);
        documentos.put("anexos", b64Anexos);
        documentos.put("formatoG", b64FormatoG);
        documentos.put("historiaAcademica", b64HistoriaAcademica);
        documentos.put("anteproyectoFinal", b64AnteproyectoFinal);
        return documentos;
    }
}
