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
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.get().getIdEstudiante());
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
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstudianteInfoDto> listarEstudiantes() {
		List<EstudianteResponseDtoAll> informacionEstudiantes = archivoClient.obtenerEstudiantes();

		if (informacionEstudiantes.isEmpty()) {
			throw new InformationException("No hay estudiantes registrados");
		}

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
	public EstudianteResponseDto listarTrabajosGradoEstudiante(Long idEstudiante) {

		List<TrabajoGrado> trabajosGrado = trabajoGradoRepository.findByEstudianteId(idEstudiante);

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

		TrabajoGrado resTrabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		TrabajoGradoResponseDto trabajoGradoConvertDto = trabajoGradoResponseMapper.toDto(resTrabajoGrado);

		int estadoActualInt = trabajoGradoConvertDto.getNumeroEstado();

		EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActualInt];

		trabajoGradoConvertDto.setEstado(estadoEnum.getMensaje());

		return trabajoGradoConvertDto;
	}

	@Override
	@Transactional
	public TrabajoGradoResponseDto crearTrabajoGrado(Long idEstudiante, String token) {

		System.out.println("entra aqui");
		EstudianteResponseDtoAll informacionEstudiante = archivoClient.obtenerInformacionEstudiante(idEstudiante);
		System.out.println("paso 2");
		if (!informacionEstudiante.getInformacionMaestria().getEstadoMaestria().equals(EstadoMaestriaActual.ACTIVO)) {
			throw new InformationException("El estudiante no esta actualmente ACTIVO");
		}

		List<TrabajoGrado> trabajosGrado = trabajoGradoRepository.findByEstudianteId(idEstudiante);

		for (int i = 0; i < trabajosGrado.size(); i++) {
			if (trabajosGrado.get(i).getNumeroEstado() == 31) {
				throw new InformationException("Ya existe un trabajo de grado aprobado");
			} else if (trabajosGrado.get(i).getNumeroEstado() != 15 && trabajosGrado.get(i).getNumeroEstado() != 34) {
				throw new InformationException("Ya existe un trabajo de grado en proceso");
			}
		}

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient.obtenerInformacionEstudiante(idEstudiante);

		String usuario = jwtTokenProvider.getUserNameFromJwtToken(token);
		String respuestaLogin = archivoClientLogin.obtenerCorreo(usuario);
		JSONObject jsonObject = new JSONObject(respuestaLogin);
		String correoElectronico = jsonObject.getString("message");

		TrabajoGrado trabajoGradoConvert = new TrabajoGrado();
		trabajoGradoConvert.setIdEstudiante(informacionEstudiantes.getId());
		trabajoGradoConvert.setFechaCreacion(LocalDate.now());
		trabajoGradoConvert.setNumeroEstado(0);
		trabajoGradoConvert.setTitulo("");
		trabajoGradoConvert.setCorreoElectronicoTutor(correoElectronico);

		TrabajoGrado trabajoGradoGuardado = trabajoGradoRepository.save(trabajoGradoConvert);

		TrabajoGradoResponseDto trabajoGradoConvertDto = trabajoGradoResponseMapper.toDto(trabajoGradoGuardado);

		int estadoActualInt = trabajoGradoConvertDto.getNumeroEstado();

		EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[estadoActualInt];

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
	public List<InformacionTrabajoGradoResponseDto> listarInformacionEstados(
			ArrayList<Integer> capturaEstadosDto) {

		boolean isValid = capturaEstadosDto.stream().allMatch(estado -> estado >= 0 && estado <= 35);
		if (!isValid) {
			throw new InformationException("El rango de estados es del 0 a 35");
		}

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
								"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		trabajoGrado.setNumeroEstado(34);

		trabajoGradoRepository.save(trabajoGrado);

		return true;
	}

}
