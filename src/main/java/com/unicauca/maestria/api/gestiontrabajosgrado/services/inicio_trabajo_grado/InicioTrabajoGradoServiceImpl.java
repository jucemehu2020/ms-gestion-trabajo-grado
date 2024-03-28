package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante.Estudiante;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.InformacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.estudiante.EstudianteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InicioTrabajoGradoServiceImpl implements InicioTrabajoGradoService {

	private final EstudianteRepository estudianteRepository;
	private final TrabajoGradoRepository trabajoGradoRepository;
	private final TrabajoGradoMapper trabajoGradoMapper;
	private final TrabajoGradoResponseMapper trabajoGradoResponseMapper;
	private final ArchivoClient archivoClient;

	@Override
	@Transactional(readOnly = true)
	public List<EstudianteInfoDto> obtenerEstudiantes() {
		List<EstudianteResponseDtoAll> informacionEstudiantes = archivoClient.obtenerEstudiantes();
		List<EstudianteInfoDto> estudiantesReducidos = informacionEstudiantes.stream()
				.map(estudiante -> new EstudianteInfoDto(
						estudiante.getPersona().getNombre(),
						estudiante.getPersona().getApellido(),
						estudiante.getPersona().getTipoIdentificacion(),
						estudiante.getPersona().getIdentificacion()))
				.collect(Collectors.toList());
		return estudiantesReducidos;
	}

	@Override
	@Transactional(readOnly = true)
	public EstudianteResponseDto buscarEstadoEstudiantePor(Long idEstudiante) {
		EstudianteResponseDto estadoTmp = archivoClient.obtenerPorIdEstudiante(idEstudiante);
		System.out.println(estadoTmp);

		// Obtener la lista de trabajos de grado del estudiante
		List<TrabajoGradoDto> trabajosGrado = estadoTmp.getTrabajoGrado();

		// Iterar sobre los trabajos de grado para modificar el estado
		for (TrabajoGradoDto trabajo : trabajosGrado) {
			// Obtener el estado actual del trabajo de grado
			Integer estadoActual = trabajo.getNumeroEstado();

			// // Convertir el estado actual a entero
			// int estadoActualInt = estadoActual;

			// Obtener el enum correspondiente a partir del entero
			EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActual];

			// Asignar el estado del trabajo de grado utilizando el enum
			trabajo.setEstado(estadoEnum.getMensaje());
		}

		return estadoTmp;
	}

	@Override
	@Transactional
	public TrabajoGradoResponseDto crearTrabajoGrado(Long idEstudiante) {

		// Obtener el estudiante
		Estudiante estudianteBD = estudianteRepository.findById(idEstudiante)
				.orElseThrow(
						() -> new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));

		// Crear el objeto TrabajoGrado
		TrabajoGrado trabajoGradoConvert = new TrabajoGrado();
		trabajoGradoConvert.setEstudiante(estudianteBD);
		trabajoGradoConvert.setFechaCreacion(LocalDate.now());
		trabajoGradoConvert.setNumeroEstado(0);

		// Guardar el TrabajoGrado en la base de datos
		TrabajoGrado trabajoGradoGuardado = trabajoGradoRepository.save(trabajoGradoConvert);

		// Mapear el TrabajoGrado guardado a un DTO de respuesta
		TrabajoGradoResponseDto trabajoGradoConvertDto = trabajoGradoResponseMapper.toDto(trabajoGradoGuardado);

		// Convertir el estado actual a entero
		int estadoActualInt = trabajoGradoConvertDto.getNumeroEstado();

		// Obtener el enum correspondiente a partir del entero
		EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActualInt];

		// Asignar el estado del trabajo de grado utilizando el enum
		trabajoGradoConvertDto.setEstado(estadoEnum.getMensaje());

		return trabajoGradoConvertDto;
	}

}
