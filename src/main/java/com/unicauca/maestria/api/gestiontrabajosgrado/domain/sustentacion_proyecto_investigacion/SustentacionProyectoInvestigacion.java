package com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sustentaciones_proyecto_investigacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SustentacionProyectoInvestigacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkFormatoF;

    private String linkMonografia;

    @OneToMany(mappedBy = "sustentacionProyectoInvestigacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnexoSustentacion> anexos;

    private Long idJuradoInterno;

    private Long idJuradoExterno;

    @Enumerated(EnumType.STRING)
    private ConceptoVerificacion conceptoCoordinador;

    @OneToMany(mappedBy = "sustentacionProyectoInvestigacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RespuestaComiteSustentacion> actaFechaRespuestaComite;

    private String linkFormatoG;

    private String linkEstudioHojaVidaAcademica;

    @Enumerated(EnumType.STRING)
    private ConceptoVerificacion juradosAceptados;

    private LocalDate fechaSustentacion;

    private String numeroActaConsejo;

    private LocalDate fechaActaConsejo;

    private String linkOficioConsejo;

    private String linkFormatoH;

    private String linkFormatoI;

    @Enumerated(EnumType.STRING)
    private ConceptoSustentacion respuestaSustentacion;

    private String linkEstudioHojaVidaAcademicaGrado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado trabajoGrado;

}
