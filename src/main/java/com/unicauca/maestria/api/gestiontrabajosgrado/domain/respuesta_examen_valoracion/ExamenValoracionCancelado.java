package com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

@Entity
@Table(name = "examenes_valoracion_cancelado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamenValoracionCancelado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String observacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado trabajoGrado;

}
