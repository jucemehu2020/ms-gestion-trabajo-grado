package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import org.json.JSONObject;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientLogin;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.security.JwtUtil;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapperImpl;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InicioTrabajoGradoServiceImpl implements InicioTrabajoGradoService {

	private final TrabajoGradoRepository trabajoGradoRepository;
	private final TrabajoGradoResponseMapper trabajoGradoResponseMapper;
	private final ArchivoClient archivoClient;
	private final ArchivoClientLogin archivoClientLogin;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private JwtUtil jwtTokenProvider;

	@Override
	@Transactional(readOnly = true)
	public List<EstudianteInfoDto> obtenerEstudiantes() {
		List<EstudianteResponseDtoAll> informacionEstudiantes = archivoClient.obtenerEstudiantes();
		List<EstudianteInfoDto> estudiantesReducidos = informacionEstudiantes.stream()
				.map(estudiante -> new EstudianteInfoDto(
						estudiante.getId(),
						estudiante.getPersona().getNombre(),
						estudiante.getPersona().getApellido(),
						estudiante.getPersona().getTipoIdentificacion(),
						estudiante.getPersona().getIdentificacion(),
						estudiante.getCodigo()))
				.collect(Collectors.toList());
		return estudiantesReducidos;
	}

	@Override
	@Transactional(readOnly = true)
	public EstudianteResponseDto buscarEstadoEstudiantePor(Long idEstudiante) {

		// // Consultar si existe estudiante
		// estudianteRepository.findById(idEstudiante)
		// .orElseThrow(
		// () -> new ResourceNotFoundException("Estudiante con id: " + idEstudiante + "
		// No encontrado"));
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(idEstudiante);
		//hacer aqui si no existe estudiante

		// Obtener la lista de trabajos de grado del estudiante desde el repositorio
		List<TrabajoGrado> trabajosGrado = trabajoGradoRepository.findByEstudianteId(idEstudiante);

		// Transformar la lista de trabajos de grado a DTOs
		List<TrabajoGradoResponseDto> trabajosGradoDto = trabajosGrado.stream().map(trabajo -> {
			EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[trabajo.getNumeroEstado()];
			return TrabajoGradoResponseDto.builder()
					.id(trabajo.getId())
					.estado(estadoEnum.getMensaje())
					.fechaCreacion(trabajo.getFechaCreacion())
					.titulo(trabajo.getTitulo() != null ? trabajo.getTitulo() : "Título no disponible")
					.numeroEstado(trabajo.getNumeroEstado())
					.build();
		}).collect(Collectors.toList());

		// Crear y retornar el DTO de respuesta del estudiante
		return EstudianteResponseDto.builder()
				.idEstudiante(idEstudiante)
				.trabajoGrado(trabajosGradoDto)
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public EstudianteInfoDto obtenerInformacionEstudiante(Long id) {
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(id);
		return new EstudianteInfoDto(
				informacionEstudiantes.getId(),
				informacionEstudiantes.getPersona().getNombre(),
				informacionEstudiantes.getPersona().getApellido(),
				informacionEstudiantes.getPersona().getTipoIdentificacion(),
				informacionEstudiantes.getPersona().getIdentificacion(),
				informacionEstudiantes.getCodigo());
	}

	@Override
	@Transactional(readOnly = true)
	public TrabajoGradoResponseDto buscarTrabajoGrado(Long idTrabajoGrado) {

		// Consultar si existe estudiante
		TrabajoGrado resTrabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Trabajo de grado con id: " + idTrabajoGrado + " No encontrado"));

		TrabajoGradoResponseDto trabajoGradoConvertDto = trabajoGradoResponseMapper.toDto(resTrabajoGrado);

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
	public TrabajoGradoResponseDto crearTrabajoGrado(Long idEstudiante, String token) {

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(idEstudiante);
		// Obtener el estudiante
		// Estudiante estudianteBD = estudianteRepository.findById(idEstudiante)
		// .orElseThrow(
		// () -> new ResourceNotFoundException("Estudiante con id: " + idEstudiante + "
		// No encontrado"));

		String usuario = jwtTokenProvider.getUserNameFromJwtToken(token);
		System.out.println("Usuario::: " + usuario);
		String respuestaLogin = archivoClientLogin.obtenerCorreo(usuario);
		JSONObject jsonObject = new JSONObject(respuestaLogin);
		String correoElectronico = jsonObject.getString("message");
		System.out.println("correoElectronico::: " + correoElectronico);

		// Crear el objeto TrabajoGrado
		TrabajoGrado trabajoGradoConvert = new TrabajoGrado();
		trabajoGradoConvert.setIdEstudiante(informacionEstudiantes.getId());
		trabajoGradoConvert.setFechaCreacion(LocalDate.now());
		trabajoGradoConvert.setNumeroEstado(0);
		trabajoGradoConvert.setTitulo("");
		trabajoGradoConvert.setCorreoElectronicoTutor(correoElectronico);

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
