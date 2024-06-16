package com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "solicitud_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudExamenValoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExamenValoracion;

    private String titulo;

    private String linkFormatoA;

    private String linkFormatoD;

    private String linkFormatoE;
    
    @OneToMany(mappedBy = "solicitudExamenValoracion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnexoSolicitudExamenValoracion> anexos;

    private String idEvaluadorInterno;

    private String idEvaluadorExterno;

    private String conceptoCoordinadorDocumentos;

    @OneToMany(mappedBy = "solicitudExamenValoracion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RespuestaComite> actaFechaRespuestaComite;

    private String linkOficioDirigidoEvaluadores;

    private LocalDate fechaMaximaEvaluacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;
}
