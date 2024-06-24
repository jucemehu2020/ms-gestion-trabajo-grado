package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosFormatoBResponseDto{ 

    private String titulo;
    private String nombreEstudiante;
    private Map<String, String> evaluadorInterno;
    private Map<String, String> evaluadorExterno;
}
