package com.unicauca.maestria.api.gestiontrabajosgrado.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import javax.persistence.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

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

    private LocalDate fechaRegistro;

    private LocalDate fechaLimite;

    private Integer estado;

    @ManyToOne
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado trabajoGrado;
}
