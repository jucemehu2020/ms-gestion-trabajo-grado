package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente;

import lombok.*;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionDocenteDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String titulo;

    @NotNull
    private String linkFormatoA;

    @NotNull
    private String linkFormatoD;

    @NotBlank
    private String linkFormatoE;

    @NotNull
    private String evaluadorExterno;

    @NotNull
    private String evaluadorInterno;
}
