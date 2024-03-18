package com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion;

import java.time.LocalDate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSustentacionTI;

    private String linkRemisionDocumentoFinal;

    private String urlDocumentacion;

    // CF: Concejo de facultad
    private String linkRemisionDocumentoFinalCF;

    private String linkConstanciaDocumentoFinal;

    private String linkActaSustentacion;

    private String linkActaSustentacionPublica;

    private Boolean respuestaSustentacion;

    private String linkEstudioHojaVidaAcademica;

    private String numeroActaTrabajoFinal;

    private LocalDate fechaActa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;
}
