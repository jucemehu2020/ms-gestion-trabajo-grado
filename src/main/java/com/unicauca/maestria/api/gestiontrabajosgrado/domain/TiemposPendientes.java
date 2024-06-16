package com.unicauca.maestria.api.gestiontrabajosgrado.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "tiempos_pendientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TiemposPendientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fechaRegistro;

    private String fechaLimite;

    private String estado;
}
