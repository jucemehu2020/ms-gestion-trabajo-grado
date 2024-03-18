package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionDto {

    @NotNull
    private Long idTrabajoGrados;

    @NotBlank
    private String linkFormatoB;

    @NotBlank
    private String linkFormatoC;

    @NotBlank
    private String observaciones;

    @NotBlank
    private String respuestaExamenValoracion;

    @NotNull
    private LocalDate fechaMaxmiaEntrega;

    @NotNull
    private Boolean estadoFinalizado;
    
    @NotBlank
    private String observacion;

}
