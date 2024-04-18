package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante.Estudiante;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapperImpl;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.estudiante.EstudianteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InicioTrabajoGradoServiceImpl implements InicioTrabajoGradoService {

	private final EstudianteRepository estudianteRepository;
	private final TrabajoGradoRepository trabajoGradoRepository;
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

		// Consultar si existe estudiante
		estudianteRepository.findById(idEstudiante)
				.orElseThrow(
						() -> new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));

		// Si existe consulta los datos del otro ms
		EstudianteResponseDto estadoTmp = archivoClient.obtenerPorIdEstudiante(idEstudiante);

		// Obtener la lista de trabajos de grado del estudiante
		List<TrabajoGradoResponseDto> trabajosGrado = estadoTmp.getTrabajoGrado();

		// Iterar sobre los trabajos de grado para modificar el estado
		for (TrabajoGradoResponseDto trabajo : trabajosGrado) {
			// Obtener el estado actual del trabajo de grado
			Integer estadoActual = trabajo.getNumeroEstado();

			// Obtener el enum correspondiente a partir del entero
			EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActual];

			// Asignar el estado del trabajo de grado utilizando el enum
			trabajo.setEstado(estadoEnum.getMensaje());
		}

		estadoTmp.setIdEstudiante(idEstudiante);

		return estadoTmp;
	}

	@Override
	@Transactional(readOnly = true)
	public TrabajoGradoResponseDto buscarTrabajoGrado(Long idTrabajoGrado) {

		// Consultar si existe estudiante
		TrabajoGrado resTrabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Trabajo de grado con id: " + idTrabajoGrado + " No encontrado"));

		return trabajoGradoResponseMapper.toDto(resTrabajoGrado);
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
		trabajoGradoConvert.setTitulo("");

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

	@Override
	@Transactional
	public void eliminarTrabajoGrado(Long idTrabajoGrado) {
		trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Tarabajo de grado con id: " + idTrabajoGrado + " no encontrado"));
		trabajoGradoRepository.deleteById(idTrabajoGrado);

	}

}
