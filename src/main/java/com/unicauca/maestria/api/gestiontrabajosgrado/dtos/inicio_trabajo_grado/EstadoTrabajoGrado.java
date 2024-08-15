package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoTrabajoGrado {
    private Long id;

    @NotNull
    private Boolean estadoTrabajoGrado;

    private List<TrabajoGradoResponseDto> trabajoGrado;
}
