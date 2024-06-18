package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObtenerDocumentosParaEnvioDto {
    
    private String base64AnteproyectoFinal;
    private String base64SolicitudComite;

}
