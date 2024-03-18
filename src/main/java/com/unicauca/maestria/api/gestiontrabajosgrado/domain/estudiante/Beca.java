package com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.DedicacionBeca;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoBeca;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.BooleanConverter;


import lombok.Data;

@Data 
@Entity @Table(name = "becas")
public class Beca {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	private String entidadAsociada;
	
	@OneToOne
	@JoinColumn(name = "id_estudiante")
	private Estudiante estudiante;
	
	@Enumerated(EnumType.STRING)
	private TipoBeca tipo;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean esOfrecidaPorUnicauca;
	
	@Enumerated(EnumType.STRING)
	private DedicacionBeca dedicacion;
}
