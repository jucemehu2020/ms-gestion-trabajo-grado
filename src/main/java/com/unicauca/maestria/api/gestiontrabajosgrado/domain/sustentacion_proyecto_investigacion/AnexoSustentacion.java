package com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "anexos_sustentacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnexoSustentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkAnexo;

    @ManyToOne
    @JoinColumn(name = "id_sustentacion_proyecto_investigacion")
    @JsonBackReference
    private SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion;
}
