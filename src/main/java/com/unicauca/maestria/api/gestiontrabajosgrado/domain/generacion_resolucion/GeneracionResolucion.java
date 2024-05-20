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

    private String titulo;

    private String director;

    private String codirector;

    private String numeroActaRevision;

    private LocalDate fechaActa;

    private String linkAnteproyectoAprobado;

    private String linkSolicitudComite;

    private String linkSolicitudConcejoFacultad;

    //CF: Concejo de facultad
    private String numeroResolucionGeneradaCF;

    private LocalDate fechaResolucion;

    private String linkResolucionGeneradaCF;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;
}
