package com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "sustentacion_proyecto_investigacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacion {

    @Id
    @Column(name = "id_sustentacion_trabajo_investigacion")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSustentacionTrabajoInvestigacion;

    private String linkFormatoF;

    private String urlDocumentacion;

    private Long idJuradoInterno;

    private Long idJuradoExterno;

    @Enumerated(EnumType.STRING)
    private ConceptoVerificacion conceptoCoordinador;

    @OneToMany(mappedBy = "sustentacionTrabajoInvestigacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RespuestaComiteSustentacion> actaFechaRespuestaComite;

    private String linkFormatoG;

    private String linkEstudioHojaVidaAcademica;

    @Enumerated(EnumType.STRING)
    private ConceptoVerificacion juradosAceptados;

    private String numeroActaConsejo;

    private LocalDate fechaActaConsejo;

    private String linkFormatoH;

    private String linkFormatoI;

    private String linkActaSustentacionPublica;

    @Enumerated(EnumType.STRING)
    private ConceptosVarios respuestaSustentacion;

    private String linkEstudioHojaVidaAcademicaGrado;

    private String numeroActaFinal;

    private LocalDate fechaActaFinal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;

}
