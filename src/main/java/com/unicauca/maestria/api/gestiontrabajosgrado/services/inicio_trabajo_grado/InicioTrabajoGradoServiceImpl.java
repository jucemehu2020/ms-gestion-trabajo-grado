package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.estudiante.Estudiante;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.InformacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.estudiante.EstudianteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InicioTrabajoGradoServiceImpl implements InicioTrabajoGradoService {

	private final EstudianteRepository estudianteRepository;
	private final TrabajoGradoRepository trabajoGradoRepository;
	private final TrabajoGradoMapper trabajoGradoMapper;
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
		return estadoTmp;
	}

	@Override
	@Transactional
	public TrabajoGradoDto crearTrabajoGrado(TrabajoGradoDto trabajoGrado, BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		// Estudiante estudiante = estudianteSaveMapper.toEntity(estudianteSaveDto);
		Estudiante EstudianteBD = estudianteRepository.findById(trabajoGrado.getIdEstudiante())
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Estudiante con id: " + trabajoGrado.getIdEstudiante() + " No encontrado"));

		// CamposUnicosEstudianteDto camposUnicosEstudiante =
		// informacionUnicaEstudiante.apply(trabajoGrado);
		// CamposUnicosEstudianteDto camposUnicosEstudianteBD =
		// informacionUnicaEstudiante.apply(estudianteSaveMapper.toDto(EstudianteBD));

		// Map<String, String> validacionCamposUnicos =
		// validacionCampoUnicos(camposUnicosEstudiante,camposUnicosEstudianteBD);
		// if(!validacionCamposUnicos.isEmpty()) {
		// throw new FieldUniqueException(validacionCamposUnicos);
		// }
		// actualizarDirectorCodirector(idEstudiante,
		// estudianteSaveDto.getIdDirector(),estudianteSaveDto.getIdCodirector());
		// actualizarTrabajoGrado(estudiante, EstudianteBD);

		TrabajoGrado trabajoGradoConvert = trabajoGradoMapper.toEntity(trabajoGrado);
		trabajoGradoConvert.setEstudiante(EstudianteBD);
		TrabajoGrado estudianteActualizado = trabajoGradoRepository.save(trabajoGradoConvert);
		TrabajoGradoDto trabajoGradoConvertDto = trabajoGradoMapper.toDto(estudianteActualizado);

		return trabajoGradoConvertDto;
	}

}
