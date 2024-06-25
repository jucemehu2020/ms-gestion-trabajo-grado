package com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "respuesta_comite_generacion_resolucion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaComiteGeneracionResolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuestaComiteGeneracionResolucion;

    private Boolean conceptoComite;

    private String numeroActa;

    private String fechaActa;

    @ManyToOne
    @JoinColumn(name = "id_generacion_resolucion")
    @JsonBackReference
    private GeneracionResolucion generacionResolucion;
}
