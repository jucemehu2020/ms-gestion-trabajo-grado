package com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;

@Entity
@Table(name = "respuestas_comite_solicitud_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaComiteExamenValoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Concepto conceptoComite;

    private String numeroActa;

    private LocalDate fechaActa;

    @ManyToOne
    @JoinColumn(name = "id_examen_valoracion")
    @JsonBackReference
    private SolicitudExamenValoracion solicitudExamenValoracion;
}
