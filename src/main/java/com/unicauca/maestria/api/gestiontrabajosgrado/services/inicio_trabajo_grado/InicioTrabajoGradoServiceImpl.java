package com.unicauca.maestria.api.gestiontrabajosgrado.services.inicio_trabajo_grado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientLogin;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.EstadoMaestriaActual;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.security.JwtUtil;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EventosIdsDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.InformacionTrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InicioTrabajoGradoServiceImpl implements InicioTrabajoGradoService {

	private final TrabajoGradoRepository trabajoGradoRepository;
	private final TiemposPendientesRepository tiemposPendientesRepository;
	private final TrabajoGradoResponseMapper trabajoGradoResponseMapper;
	private final ArchivoClient archivoClient;
	private final ArchivoClientLogin archivoClientLogin;

	// Eventos
	private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
	private final RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
	private final GeneracionResolucionRepository generacionResolucionRepository;
	private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;

	@Autowired
	private EnvioCorreos envioCorreos;

	@Autowired
	private JwtUtil jwtTokenProvider;

	@Scheduled(fixedRate = 300000) // Ejecuta cada 300 segundos (5 minutos)
	public void checkDates() {
		List<TiemposPendientes> tiemposPendientesList = tiemposPendientesRepository.findAll();
		LocalDate hoy = LocalDate.now();

		for (TiemposPendientes tiemposPendientes : tiemposPendientesList) {
			LocalDate fechaLimite = tiemposPendientes.getFechaLimite();

			if (fechaLimite.isBefore(hoy)) {
				List<TiemposPendientes> listaTiempos = tiemposPendientesRepository.findAll();
				for (int i = 0; i < listaTiempos.size(); i++) {
					Optional<TrabajoGrado> resTrabajoGrado = trabajoGradoRepository
							.findById(listaTiempos.get(i).getTrabajoGrado().getId());
					verificarEstado(resTrabajoGrado, listaTiempos.get(i));
				}
			}
		}
	}

	private void verificarEstado(Optional<TrabajoGrado> trabajoGrado, TiemposPendientes tiemposPendientes) {
		// Caso para solicitud
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.get().getIdEstudiante());
		// if (trabajoGrado.get().getNumeroEstado() == 7 ||
		// trabajoGrado.get().getNumeroEstado() == 34 ||
		// trabajoGrado.get().getNumeroEstado() == 4) {
		// tiemposPendientesRepository.deleteById(tiemposPendientes.getId());
		// } else if (trabajoGrado.get().getNumeroEstado() >= 8 &&
		// trabajoGrado.get().getNumeroEstado() <= 14
		// || trabajoGrado.get().getNumeroEstado() == 34 ||
		// trabajoGrado.get().getNumeroEstado() == 35) {
		// tiemposPendientesRepository.deleteById(tiemposPendientes.getId());
		// } else if (trabajoGrado.get().getNumeroEstado() == 31 ||
		// trabajoGrado.get().getNumeroEstado() == 32
		// || trabajoGrado.get().getNumeroEstado() == 34) {
		// tiemposPendientesRepository.deleteById(tiemposPendientes.getId());
		// } else {
		String complementoMensaje = trabajoGrado.get().getId() + " titulado " + trabajoGrado.get().getTitulo()
				+ " realizado por el estudiante " + informacionEstudiantes.getPersona().getNombre() + " "
				+ informacionEstudiantes.getPersona().getApellido() + " registrada en la fecha "
				+ tiemposPendientes.getFechaRegistro();

		if (trabajoGrado.get().getNumeroEstado() == 5 || trabajoGrado.get().getNumeroEstado() == 6
				|| trabajoGrado.get().getNumeroEstado() == 8 && trabajoGrado.get().getNumeroEstado() == 10) {
			String mensaje = "Este es un correo de recuerdo informando que la subida de la informacion del trabajo de grado numero "
					+ complementoMensaje + " no ha sido completada.";
			envioCorreos.enviarCorreosUnico(Constants.correoCoordinacionMaestria,
					"Notificacion de recuerdo respuesta evaluadores", mensaje);
		} else if (trabajoGrado.get().getNumeroEstado() >= 8 && trabajoGrado.get().getNumeroEstado() <= 14) {
			String mensaje = "Este es un correo de recuerdo informando que el docente y estudiante del trabajo de grado numero "
					+ complementoMensaje
					+ " no han cargado la informacion despues de la respuesta de los evaluadores.";
			envioCorreos.enviarCorreosUnico(Constants.correoCoordinacionMaestria,
					"Notificacion de recuerdo correciones examen de valoracion", mensaje);
		} else if (trabajoGrado.get().getNumeroEstado() == 33) {
			String mensaje = "Este es un correo de recuerdo informando que el estudiante del trabajo de grado numero "
					+ complementoMensaje
					+ " no han dado respuesta despues de la sustentacion aplazada.";
			envioCorreos.enviarCorreosUnico(Constants.correoCoordinacionMaestria,
					"Notificacion de recuerdo sustentacion aplazada", mensaje);
		}
		// }

	}

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

		EstudianteResponseDtoAll informacionEstudiante = archivoClient.obtenerInformacionEstudiante(idEstudiante);
		if (!informacionEstudiante.getInformacionMaestria().getEstadoMaestria().equals(EstadoMaestriaActual.ACTIVO)) {
			throw new InformationException("El estudiante no esta actualmente ACTIVO");
		}
		// hacer aqui si no existe estudiante

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

		List<TrabajoGrado> trabajosGrado = trabajoGradoRepository.findByEstudianteId(idEstudiante);

		for (int i = 0; i < trabajosGrado.size(); i++) {
			if (trabajosGrado.get(i).getNumeroEstado() == 31) {
				throw new InformationException("Ya existe un trabajo de grado aprobado");
			} else if (trabajosGrado.get(i).getNumeroEstado() != 15 || trabajosGrado.get(i).getNumeroEstado() != 34) {
				throw new InformationException("Ya existe un trabajo de grado en proceso");
			}
		}

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(idEstudiante);

		String usuario = jwtTokenProvider.getUserNameFromJwtToken(token);
		String respuestaLogin = archivoClientLogin.obtenerCorreo(usuario);
		JSONObject jsonObject = new JSONObject(respuestaLogin);
		String correoElectronico = jsonObject.getString("message");

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
								"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));
		trabajoGradoRepository.deleteById(idTrabajoGrado);

	}

	@Override
	@Transactional(readOnly = true)
	public EventosIdsDto obtenerIdsEventos(Long idTrabajoGrado) {

		trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Tarabajo de grado con id: " + idTrabajoGrado + " no encontrado"));

		Long idSolicitudExamenvaloracion = solicitudExamenValoracionRepository
				.findIdExamenValoracionByTrabajoGradoId(idTrabajoGrado);
		List<Long> idRespuestaExamenvaloracion = respuestaExamenValoracionRepository
				.findIdRespuestaExamenValoracionByTrabajoGradoId(idTrabajoGrado);
		Long idGeneracionResolucion = generacionResolucionRepository
				.findIdGeneracionResolucionByTrabajoGradoId(idTrabajoGrado);
		Long idSustentacion = sustentacionProyectoInvestigacionRepository
				.findIdSustentacionTrabajoInvestigacionByTrabajoGradoId(idTrabajoGrado);

		EventosIdsDto eventosIdsDto = new EventosIdsDto();
		eventosIdsDto.setIdSolicitudExamenValoracion(idSolicitudExamenvaloracion);
		eventosIdsDto.setIdRespuestaExamenValoracion(idRespuestaExamenvaloracion);
		eventosIdsDto.setIdGeneracionResolucion(idGeneracionResolucion);
		eventosIdsDto.setIdSustentacion(idSustentacion);

		return eventosIdsDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<InformacionTrabajoGradoResponseDto> listarEstadosExamenValoracion(
			ArrayList<Integer> capturaEstadosDto) {
		List<TrabajoGrado> listaTrabajoGrado = trabajoGradoRepository.findAll();

		List<InformacionTrabajoGradoResponseDto> trabajosGradoDto = listaTrabajoGrado.stream()
				.filter(trabajo -> capturaEstadosDto.contains(trabajo.getNumeroEstado()))
				.map(trabajo -> {
					EstudianteResponseDtoAll estudianteResponseDtoAll = archivoClient
							.obtenerInformacionEstudiante(trabajo.getIdEstudiante());
					EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[trabajo.getNumeroEstado()];
					return InformacionTrabajoGradoResponseDto.builder()
							.id(trabajo.getId())
							.estudianteId(estudianteResponseDtoAll.getId())
							.identificacion(estudianteResponseDtoAll.getPersona().getIdentificacion())
							.nombreCompleto(estudianteResponseDtoAll.getPersona().getNombre() + " "
									+ estudianteResponseDtoAll.getPersona().getApellido())
							.correoElectronico(estudianteResponseDtoAll.getPersona().getCorreoElectronico())
							.numeroEstado(trabajo.getNumeroEstado())
							.estado(estadoEnum.getMensaje())
							.build();
				}).collect(Collectors.toList());

		return trabajosGradoDto;
	}

	@Override
	@Transactional(readOnly = true)
	public String descargarArchivo(String rutaArchivo) {
		return FilesUtilities.recuperarArchivo(rutaArchivo);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean cancelarTrabajoGrado(Long idTrabajoGrado) {

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Tarabajo de grado con id: " + idTrabajoGrado + " no encontrado"));

		trabajoGrado.setNumeroEstado(34);

		trabajoGradoRepository.save(trabajoGrado);

		return true;
	}

}
