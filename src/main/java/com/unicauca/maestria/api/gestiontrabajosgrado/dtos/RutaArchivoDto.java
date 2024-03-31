package com.unicauca.maestria.api.gestiontrabajosgrado.dtos;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaArchivoDto {

    @NotBlank
    private String rutaArchivo;

}
