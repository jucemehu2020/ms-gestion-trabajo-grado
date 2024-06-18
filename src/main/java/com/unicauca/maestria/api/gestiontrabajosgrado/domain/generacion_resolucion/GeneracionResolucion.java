package com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "generacion_resolucion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneracionResolucion {

    @Id
    @Column(name = "id_generacion_resolucion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGeneracionResolucion;

    // Docente
    private String director;

    private String codirector;

    private String linkAnteproyectoFinal;

    private String linkSolicitudComite;

    // Coordinador - Fase 1
    private Boolean conceptoDocumentosCoordinador;

    // Coordinador - Fase 2
    private String numeroActaSolicitudComite;

    private LocalDate fechaActaSolicitudComite;

    private String linkSolicitudConsejoFacultad;

    // Coordinador - Fase 3
    private String numeroActaConsejoFacultad;

    private LocalDate fechaActaConsejoFacultad;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;
}
