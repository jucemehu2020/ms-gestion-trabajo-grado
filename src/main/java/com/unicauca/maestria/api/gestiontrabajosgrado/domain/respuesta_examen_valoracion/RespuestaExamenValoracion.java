package com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

@Entity
@Table(name = "respuesta_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaExamenValoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuestaExamenValoracion;

    private String linkFormatoB;

    private String linkFormatoC;

    private String linkObservaciones;

    @OneToMany(mappedBy = "respuestaExamenValoracion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnexoRespuestaExamenValoracion> anexos;

    private String respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    private Long idEvaluador;

    private String tipoEvaluador;

    private Boolean permitidoExamen;

    @ManyToOne
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;

}
