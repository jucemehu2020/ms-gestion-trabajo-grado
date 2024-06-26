package com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.FetchType;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "trabajo_grado")
public class TrabajoGrado {

    @Id
    @Column(name = "id_trabajo_grado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_estudiante")
    private Long idEstudiante;

    private LocalDate fechaCreacion;

    private Integer numeroEstado;

    private String titulo;

    private String correoElectronicoTutor;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private SolicitudExamenValoracion examenValoracion;

    @OneToMany(mappedBy = "idTrabajoGrado", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaExamenValoracion> respuestaExamenValoracion;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private GeneracionResolucion idGeneracionResolucion;

    @OneToOne(mappedBy = "idTrabajoGrado", cascade = CascadeType.ALL)
    private SustentacionTrabajoInvestigacion idSustentacionProyectoInvestigacion;

}
