package com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "respuesta_comite_solicitud_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaComiteExamenValoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnexoExamenValoracion;

    private Boolean conceptoComite;

    private String numeroActa;

    private String fechaActa;

    @ManyToOne
    @JoinColumn(name = "id_examen_valoracion")
    @JsonBackReference
    private SolicitudExamenValoracion solicitudExamenValoracion;
}
