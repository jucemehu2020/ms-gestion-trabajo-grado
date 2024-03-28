package com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante.Estudiante;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trabajo_grado")
public class TrabajoGrado {

    @Id
    @Column(name = "id_trabajo_grado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    private LocalDate fechaCreacion;

    private Integer numeroEstado;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private SolicitudExamenValoracion examenValoracion;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private RespuestaExamenValoracion idRtaExamenValoracion;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private GeneracionResolucion idGeneracionResolucion;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private SustentacionTrabajoInvestigacion idSustentacionProyectoInvestigacion;

}
