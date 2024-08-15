package com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "anexos_solicitud_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnexoSolicitudExamenValoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkAnexo;

    @ManyToOne
    @JoinColumn(name = "id_examen_valoracion")
    @JsonBackReference
    private SolicitudExamenValoracion solicitudExamenValoracion;
}
