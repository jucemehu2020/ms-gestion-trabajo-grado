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
import org.json.JSONArray;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.EstudianteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.InformacionTrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.TrabajoGradoResponseMapper;
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

	@Autowired
	private EnvioCorreos envioCorreos;

	@Autowired
	private JwtUtil jwtTokenProvider;

	/**
	 * Método programado para ejecutarse cada 24 horas y verificar fechas límite de
	 * tiempos pendientes.
	 * Envía notificaciones en caso de que se haya superado la fecha límite y no se
	 * haya completado la tarea.
	 * El método busca todos los registros en la base de datos y revisa su estado.
	 */
	@Scheduled(fixedRate = 86400000) // Ejecuta cada 24 horas (Tiempo en milisegundos)
	public void checkDates() {
		// Obtener todos los registros de tiempos pendientes
		List<TiemposPendientes> tiemposPendientesList = tiemposPendientesRepository.findAll();
		LocalDate hoy = LocalDate.now(); // Obtener la fecha actual

		// Recorrer la lista de tiempos pendientes para verificar si la fecha límite ha
		// pasado
		for (TiemposPendientes tiemposPendientes : tiemposPendientesList) {
			LocalDate fechaLimite = tiemposPendientes.getFechaLimite();

			// Si la fecha límite ha pasado, proceder a verificar el estado del trabajo de
			// grado
			if (fechaLimite != null && fechaLimite.isBefore(hoy)) {
				List<TiemposPendientes> listaTiempos = tiemposPendientesRepository.findAll();
				for (int i = 0; i < listaTiempos.size(); i++) {
					Optional<TrabajoGrado> resTrabajoGrado = trabajoGradoRepository
							.findById(listaTiempos.get(i).getTrabajoGrado().getId());
					verificarEstado(resTrabajoGrado, listaTiempos.get(i)); // Verificar el estado y enviar
																			// notificaciones
				}
			}
		}
	}

	/**
	 * Verifica el estado de un trabajo de grado y envía notificaciones por correo
	 * electrónico
	 * basadas en su estado y la información del estudiante.
	 *
	 * @param trabajoGrado      El trabajo de grado a verificar.
	 * @param tiemposPendientes El registro de tiempos pendientes asociado.
	 */
	private void verificarEstado(Optional<TrabajoGrado> trabajoGrado, TiemposPendientes tiemposPendientes) {
		// Obtener información del estudiante asociado al trabajo de grado
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.get().getIdEstudiante());

		// Construir el mensaje base con la información del trabajo de grado y el
		// estudiante
		String complementoMensaje = trabajoGrado.get().getId() + " titulado " + trabajoGrado.get().getTitulo()
				+ " realizado por el estudiante " + informacionEstudiantes.getPersona().getNombre() + " "
				+ informacionEstudiantes.getPersona().getApellido() + " registrada en la fecha "
				+ tiemposPendientes.getFechaRegistro();

		// Verificar el estado del trabajo de grado y enviar notificaciones en
		// consecuencia
		if (trabajoGrado.get().getNumeroEstado() == 5 || trabajoGrado.get().getNumeroEstado() == 6
				|| trabajoGrado.get().getNumeroEstado() == 8 && trabajoGrado.get().getNumeroEstado() == 10) {
			// Notificación para estados 5, 6, 8 y 10
			String mensaje = "Este es un correo de recuerdo informando que la subida de la información del trabajo de grado numero "
					+ complementoMensaje + " no ha sido completada.";
			envioCorreos.enviarCorreosUnico(Constants.correoCoordinacionMaestria,
					"Notificacion de recuerdo respuesta evaluadores", mensaje);
		} else if (trabajoGrado.get().getNumeroEstado() >= 8 && trabajoGrado.get().getNumeroEstado() <= 14) {
			// Notificación para estados entre 8 y 14
			String mensaje = "Este es un correo de recuerdo informando que el docente y estudiante del trabajo de grado numero "
					+ complementoMensaje
					+ " no han cargado la información despues de la respuesta de los evaluadores.";
			envioCorreos.enviarCorreosUnico(Constants.correoCoordinacionMaestria,
					"Notificacion de recuerdo correciones examen de valoracion", mensaje);
		} else if (trabajoGrado.get().getNumeroEstado() == 33) {
			// Notificación para estado 33
			String mensaje = "Este es un correo de recuerdo informando que el estudiante del trabajo de grado numero "
					+ complementoMensaje
					+ " no han dado respuesta despues de la sustentacion aplazada.";
			envioCorreos.enviarCorreosUnico(Constants.correoCoordinacionMaestria,
					"Notificacion de recuerdo sustentacion aplazada", mensaje);
		}
	}

	/**
	 * Lista todos los estudiantes registrados, reduciendo la información obtenida a
	 * datos básicos
	 * como nombre, apellido, identificación y código.
	 *
	 * @return Lista de estudiantes en formato reducido.
	 * @throws InformationException si no hay estudiantes registrados.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<EstudianteInfoDto> listarEstudiantes() {
		// Obtener la lista de estudiantes del cliente externo
		List<EstudianteResponseDtoAll> informacionEstudiantes = archivoClient.obtenerEstudiantes();

		// Verificar si la lista está vacía y lanzar una excepción si es el caso
		if (informacionEstudiantes.isEmpty()) {
			throw new InformationException("No hay estudiantes registrados");
		}

		// Reducir la información de los estudiantes a los datos relevantes
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

	/**
	 * Lista todos los trabajos de grado asociados a un estudiante en particular.
	 *
	 * @param idEstudiante ID del estudiante para obtener los trabajos de grado.
	 * @return DTO con la información del estudiante y sus trabajos de grado.
	 * @throws ResourceNotFoundException si el estudiante no existe.
	 */
	@Override
	@Transactional(readOnly = true)
	public EstudianteResponseDto listarTrabajosGradoEstudiante(Long idEstudiante) {
		// Obtener la información del estudiante a través del cliente externo
		archivoClient.obtenerInformacionEstudiante(idEstudiante);

		// Obtener los trabajos de grado asociados al estudiante
		List<TrabajoGrado> trabajosGrado = trabajoGradoRepository.findByEstudianteId(idEstudiante);

		// Convertir la lista de trabajos de grado a DTOs con información relevante
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

		// Retornar la información del estudiante junto con sus trabajos de grado
		return EstudianteResponseDto.builder()
				.idEstudiante(idEstudiante)
				.trabajoGrado(trabajosGradoDto)
				.build();
	}

	/**
	 * Obtiene la información básica de un estudiante en base a su ID.
	 *
	 * @param id ID del estudiante.
	 * @return DTO con la información básica del estudiante.
	 * @throws ResourceNotFoundException si el estudiante no existe.
	 */
	@Override
	@Transactional(readOnly = true)
	public EstudianteInfoDto obtenerInformacionEstudiante(Long id) {
		// Obtener la información del estudiante a través del cliente externo
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(id);

		// Retornar la información básica del estudiante en formato DTO
		return new EstudianteInfoDto(
				informacionEstudiantes.getId(),
				informacionEstudiantes.getPersona().getNombre(),
				informacionEstudiantes.getPersona().getApellido(),
				informacionEstudiantes.getPersona().getTipoIdentificacion(),
				informacionEstudiantes.getPersona().getIdentificacion(),
				informacionEstudiantes.getCodigo());
	}

	/**
	 * Busca un trabajo de grado por su ID y devuelve su información en un DTO.
	 *
	 * @param idTrabajoGrado ID del trabajo de grado a buscar.
	 * @return DTO con la información del trabajo de grado.
	 * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
	 */
	@Override
	@Transactional(readOnly = true)
	public TrabajoGradoResponseDto buscarTrabajoGrado(Long idTrabajoGrado) {
		// Buscar el trabajo de grado en el repositorio
		TrabajoGrado resTrabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Convertir el trabajo de grado a DTO
		TrabajoGradoResponseDto trabajoGradoConvertDto = trabajoGradoResponseMapper.toDto(resTrabajoGrado);

		// Obtener el estado actual como entero y convertirlo al enum correspondiente
		int estadoActualInt = trabajoGradoConvertDto.getNumeroEstado();
		EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActualInt];

		// Establecer el mensaje del estado en el DTO
		trabajoGradoConvertDto.setEstado(estadoEnum.getMensaje());

		return trabajoGradoConvertDto;
	}

	/**
	 * Crea un nuevo trabajo de grado para un estudiante específico.
	 *
	 * @param idEstudiante ID del estudiante para el cual se creará el trabajo de
	 *                     grado.
	 * @param token        Token JWT del usuario autenticado.
	 * @return DTO con la información del trabajo de grado creado.
	 * @throws InformationException si el estudiante no está activo o si ya existe
	 *                              un trabajo de grado en proceso o aprobado.
	 */
	@Override
	@Transactional
	public TrabajoGradoResponseDto crearTrabajoGrado(Long idEstudiante, String token) {
		// Obtener la información del estudiante y verificar su estado en la maestría
		EstudianteResponseDtoAll informacionEstudiante = archivoClient.obtenerInformacionEstudiante(idEstudiante);
		if (!informacionEstudiante.getInformacionMaestria().getEstadoMaestria().equals(EstadoMaestriaActual.ACTIVO)) {
			throw new InformationException("El estudiante no esta actualmente ACTIVO");
		}

		// Verificar si ya existe un trabajo de grado en proceso o aprobado para el
		// estudiante
		List<TrabajoGrado> trabajosGrado = trabajoGradoRepository.findByEstudianteId(idEstudiante);
		for (TrabajoGrado trabajo : trabajosGrado) {
			if (trabajo.getNumeroEstado() == 31) {
				throw new InformationException("Ya existe un trabajo de grado aprobado");
			} else if (trabajo.getNumeroEstado() != 15 && trabajo.getNumeroEstado() != 34) {
				throw new InformationException("Ya existe un trabajo de grado en proceso");
			}
		}

		// Obtener la información adicional del estudiante y del tutor
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(idEstudiante);
		String usuario = jwtTokenProvider.getUserNameFromJwtToken(token);
		String respuestaLogin = archivoClientLogin.obtenerPersonaId(usuario);
		JSONObject jsonObject = new JSONObject(respuestaLogin);
		Long personaId = jsonObject.getLong("personaId");
		DocenteResponseDto docente = archivoClient.obtenerDocentePorId(personaId);

		// Crear y guardar el nuevo trabajo de grado
		TrabajoGrado trabajoGradoConvert = new TrabajoGrado();
		trabajoGradoConvert.setIdEstudiante(informacionEstudiantes.getId());
		trabajoGradoConvert.setFechaCreacion(LocalDate.now());
		trabajoGradoConvert.setNumeroEstado(0);
		trabajoGradoConvert.setTitulo("");
		trabajoGradoConvert.setCorreoElectronicoTutor(docente.getPersona().getCorreoElectronico());

		TrabajoGrado trabajoGradoGuardado = trabajoGradoRepository.save(trabajoGradoConvert);

		// Convertir el trabajo de grado guardado a DTO y establecer el mensaje del
		// estado
		TrabajoGradoResponseDto trabajoGradoConvertDto = trabajoGradoResponseMapper.toDto(trabajoGradoGuardado);
		int estadoActualInt = trabajoGradoConvertDto.getNumeroEstado();
		EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActualInt];
		trabajoGradoConvertDto.setEstado(estadoEnum.getMensaje());

		return trabajoGradoConvertDto;
	}

	/**
	 * Elimina un trabajo de grado por su ID.
	 *
	 * @param idTrabajoGrado ID del trabajo de grado a eliminar.
	 * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
	 */
	@Override
	@Transactional
	public void eliminarTrabajoGrado(Long idTrabajoGrado) {
		// Verificar si el trabajo de grado existe y lanzar excepción si no se encuentra
		trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Eliminar el trabajo de grado por su ID
		trabajoGradoRepository.deleteById(idTrabajoGrado);
	}

	/**
	 * Lista la información de los trabajos de grado que se encuentran en los
	 * estados especificados.
	 *
	 * @param capturaEstadosDto Lista de estados de los trabajos de grado a filtrar.
	 * @return Lista de DTOs con la información de los trabajos de grado filtrados.
	 * @throws InformationException si algún estado en la lista está fuera del rango
	 *                              permitido (0 a 37).
	 */
	@Override
	@Transactional(readOnly = true)
	public List<InformacionTrabajoGradoResponseDto> listarInformacionEstados(ArrayList<Integer> capturaEstadosDto) {
		// Verificar que todos los estados estén en el rango permitido (0 a 37)
		boolean isValid = capturaEstadosDto.stream().allMatch(estado -> estado >= 0 && estado <= 37);
		if (!isValid) {
			throw new InformationException("El rango de estados es del 0 a 37");
		}

		// Obtener todos los trabajos de grado del repositorio
		List<TrabajoGrado> listaTrabajoGrado = trabajoGradoRepository.findAll();

		// Filtrar los trabajos de grado por los estados especificados y mapear a DTOs
		List<InformacionTrabajoGradoResponseDto> trabajosGradoDto = listaTrabajoGrado.stream()
				.filter(trabajo -> capturaEstadosDto.contains(trabajo.getNumeroEstado()))
				.map(trabajo -> {
					// Obtener información del estudiante asociado al trabajo de grado
					EstudianteResponseDtoAll estudianteResponseDtoAll = archivoClient
							.obtenerInformacionEstudiante(trabajo.getIdEstudiante());

					// Convertir el estado del trabajo de grado al mensaje correspondiente
					EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[trabajo.getNumeroEstado()];

					// Crear y devolver el DTO con la información del trabajo de grado
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

	/**
	 * Descarga un archivo desde la ubicación especificada.
	 *
	 * @param rutaArchivo Ruta del archivo a descargar.
	 * @return El contenido del archivo como una cadena de texto.
	 */
	@Override
	@Transactional(readOnly = true)
	public String descargarArchivo(String rutaArchivo) {
		return FilesUtilities.recuperarArchivo(rutaArchivo);
	}

	/**
	 * Cancela un trabajo de grado cambiando su estado a "cancelado" (estado 34).
	 *
	 * @param idTrabajoGrado ID del trabajo de grado a cancelar.
	 * @return true si el trabajo de grado fue cancelado con éxito.
	 * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
	 * @throws InformationException      si el trabajo de grado ya está cancelado.
	 */
	@Override
	@Transactional
	public Boolean cancelarTrabajoGrado(Long idTrabajoGrado) {
		// Buscar el trabajo de grado en el repositorio
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Verificar si el trabajo de grado ya ha sido cancelado
		if (trabajoGrado.getNumeroEstado() == 34) {
			throw new InformationException("El trabajo de grado ya ha sido cancelado");
		}

		// Cambiar el estado del trabajo de grado a "cancelado" (estado 34)
		trabajoGrado.setNumeroEstado(34);

		// Guardar los cambios en el repositorio
		trabajoGradoRepository.save(trabajoGrado);

		return true;
	}

	/**
	 * Verifica si el usuario identificado por el token es el docente asignado al
	 * trabajo de grado.
	 *
	 * @param idTrabajoGrado ID del trabajo de grado a verificar.
	 * @param token          Token JWT del usuario que realiza la solicitud.
	 * @return true si el usuario es el docente asignado al trabajo de grado o si no
	 *         es un docente, false en caso contrario.
	 * @throws ResourceNotFoundException si no se encuentra el trabajo de grado con
	 *                                   el ID proporcionado.
	 */
	@Override
	@Transactional
	public Boolean verificarDocente(String idTrabajoGrado, String token) {

		// Obtener el nombre de usuario a partir del token JWT
		String usuario = jwtTokenProvider.getUserNameFromJwtToken(token);

		// Buscar el trabajo de grado por ID en el repositorio
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(Long.parseLong(idTrabajoGrado))
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Obtener la información de la persona a partir del usuario
		String respuestaLogin = archivoClientLogin.obtenerPersonaId(usuario);
		JSONObject jsonObject = new JSONObject(respuestaLogin);
		Long personaId = jsonObject.getLong("personaId");

		// Obtener los roles del usuario
		JSONArray rolesArray = jsonObject.getJSONArray("roles");
		String nombreRol = rolesArray.getJSONObject(0).getString("nombreRol");

		// Si el rol del usuario es "ROLE_DOCENTE", verificar si es el tutor del trabajo
		// de grado
		if (nombreRol.equals("ROLE_DOCENTE")) {
			DocenteResponseDto docente = archivoClient.obtenerDocentePorId(personaId);
			// Comparar el correo electrónico del docente con el correo del tutor del
			// trabajo de grado
			return docente.getPersona().getCorreoElectronico().equals(trabajoGrado.getCorreoElectronicoTutor());
		}

		// Si el usuario no es un docente, devolver true
		return true;
	}

}
