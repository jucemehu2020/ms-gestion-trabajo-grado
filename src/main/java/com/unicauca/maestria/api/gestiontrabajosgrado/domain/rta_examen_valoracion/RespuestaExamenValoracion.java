package com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rta_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaExamenValoracion {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRtaExamenValoracion;

    private String linkFormatoB;

    private String linkFormatoC;

    private String observaciones;

    private String respuestaExamenValoracion;

    private LocalDate fechaMaxmiaEntrega;

    private Boolean estadoFinalizado;

    private String observacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado idTrabajoGrado;

}
