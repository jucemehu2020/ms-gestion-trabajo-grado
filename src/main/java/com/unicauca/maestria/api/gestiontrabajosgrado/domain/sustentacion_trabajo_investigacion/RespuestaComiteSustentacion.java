package com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "respuesta_comite_sustentacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaComiteSustentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuestaComiteSustentacion;

    private Boolean conceptoComite;

    private String numeroActa;

    private String fechaActa;

    @ManyToOne
    @JoinColumn(name = "id_sustentacion_trabajo_investigacion")
    @JsonBackReference
    private SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacion;
}
