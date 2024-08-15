package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common;

import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.Genero;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class PersonaDto {
    private Long id;
	
	@NotNull
	private Long identificacion;
	
	@NotBlank
	private String nombre;
	
	@NotBlank
	private String apellido;
	
	@Email
	private String correoElectronico;
	
	private String telefono;
	
	@NotNull
	private Genero genero;
	
	@NotNull
	private TipoIdentificacion tipoIdentificacion;
}
