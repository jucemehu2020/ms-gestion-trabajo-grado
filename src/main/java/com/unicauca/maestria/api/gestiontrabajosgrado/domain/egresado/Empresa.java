package com.unicauca.maestria.api.gestiontrabajosgrado.domain.egresado;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante.Estudiante;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private String ubicacion;

	private String cargo;

	private String jefeDirecto;

	private String telefono;

	private String correo;

	private String estado;

	@ManyToOne
	@JoinColumn(name = "id_estudiante")
	private Estudiante estudiante;

}
