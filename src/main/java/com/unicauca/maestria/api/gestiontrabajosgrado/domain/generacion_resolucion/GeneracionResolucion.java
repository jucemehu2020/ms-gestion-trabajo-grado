package com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "generaciones_resolucion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneracionResolucion {

    @Id
    // @Column(name = "id_generacion_resolucion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Docente
    private Long director;

    private Long codirector;

    private String linkAnteproyectoFinal;

    private String linkSolicitudComite;

    // Coordinador - Fase 1
    @Enumerated(EnumType.STRING)
    private ConceptoVerificacion conceptoDocumentosCoordinador;

    // Coordinador - Fase 2
    @OneToMany(mappedBy = "generacionResolucion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RespuestaComiteGeneracionResolucion> actaFechaRespuestaComite;

    private String linkSolicitudConsejoFacultad;

    // Coordinador - Fase 3
    private String numeroActaConsejoFacultad;

    private LocalDate fechaActaConsejoFacultad;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;
}
