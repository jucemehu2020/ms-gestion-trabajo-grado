package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaComiteExamenValoracionDto {

    private Long idAnexoExamenValoracion;
    @NotNull
    private Boolean conceptoComite;
    @NotNull
    private String numeroActa;
    @NotNull
    private LocalDate fechaActa;
}
