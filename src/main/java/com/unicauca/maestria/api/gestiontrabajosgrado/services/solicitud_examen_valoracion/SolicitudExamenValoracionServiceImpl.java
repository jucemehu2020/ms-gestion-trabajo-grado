package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.DatosFormatoBResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.EnvioEmailCorrecionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.*;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitudExamenValoracionServiceImpl implements SolicitudExamenValoracionService {

	// Repository
	private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
	private final TrabajoGradoRepository trabajoGradoRepository;
	// Mapper
	private final SolicitudExamenValoracionMapper examenValoracionMapper;
	private final SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
	// Other
	private final ArchivoClient archivoClient;
	private final ArchivoClientExpertos archivoClientExpertos;
	private final InformacionUnicaSolicitudExamenValoracion informacionUnicaSolicitudExamenValoracion;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	@Transactional(readOnly = true)
	public List<DocenteInfoDto> listarDocentes() {
		List<DocenteResponseDto> estadoTmp = archivoClient.listarDocentesRes();
		List<DocenteInfoDto> docentes = estadoTmp.stream()
				.map(docente -> new DocenteInfoDto(
						docente.getId(),
						docente.getPersona().getNombre(),
						docente.getPersona().getApellido(),
						docente.getPersona().getCorreoElectronico(),
						docente.getUltimaUniversidad()))
				.collect(Collectors.toList());
		return docentes;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ExpertoInfoDto> listarExpertos() {
		List<ExpertoResponseDto> estadoTmp = archivoClientExpertos.listar();
		List<ExpertoInfoDto> expertos = estadoTmp.stream()
				.map(experto -> new ExpertoInfoDto(
						experto.getId(),
						experto.getPersona().getNombre(),
						experto.getPersona().getApellido(),
						experto.getPersona().getCorreoElectronico(),
						experto.getUniversidad()))
				.collect(Collectors.toList());
		return expertos;
	}

	@Override
	@Transactional(readOnly = true)
	public DocenteInfoDto obtenerDocente(Long id) {
		DocenteResponseDto docente = archivoClient.obtenerDocentePorId(id);
		return new DocenteInfoDto(
				docente.getId(),
				docente.getPersona().getNombre(),
				docente.getPersona().getApellido(),
				docente.getPersona().getCorreoElectronico(),
				docente.getUltimaUniversidad());
	}

	@Override
	@Transactional(readOnly = true)
	public ExpertoInfoDto obtenerExperto(Long id) {
		ExpertoResponseDto experto = archivoClientExpertos.obtenerExpertoPorId(id);
		return new ExpertoInfoDto(
				experto.getId(),
				experto.getPersona().getNombre(),
				experto.getPersona().getApellido(),
				experto.getPersona().getCorreoElectronico(),
				experto.getUniversidad());
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionDocenteResponseDto insertarInformacionDocente(
			SolicitudExamenValoracionDocenteDto examenValoracionDto,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionDto.getIdTrabajoGrados())
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + examenValoracionDto.getIdTrabajoGrados() + " No encontrado"));

		Map<String, String> validacionCamposUnicos = validacionCampoUnicos(obtenerCamposUnicos(examenValoracionDto),
				null);
		if (!validacionCamposUnicos.isEmpty()) {
			throw new FieldUniqueException(validacionCamposUnicos);
		}

		// Obtener iniciales del trabajo de grado
		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());

		Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
		String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
		String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
		String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

		// Mapear DTO a entidad
		SolicitudExamenValoracion examenValoracion = examenValoracionMapper.toEntity(examenValoracionDto);

		// Establecer la relación uno a uno
		examenValoracion.setIdTrabajoGrado(trabajoGrado);
		trabajoGrado.setExamenValoracion(examenValoracion);

		// Se cambia el numero de estado
		trabajoGrado.setNumeroEstado(1);
		trabajoGrado.setTitulo(examenValoracionDto.getTitulo());

		// Guardar la entidad ExamenValoracion
		examenValoracion
				.setLinkFormatoA(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
						examenValoracion.getLinkFormatoA(), nombreCarpeta));
		examenValoracion
				.setLinkFormatoD(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
						examenValoracion.getLinkFormatoD(), nombreCarpeta));
		examenValoracion
				.setLinkFormatoE(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
						examenValoracion.getLinkFormatoE(), nombreCarpeta));

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracion);

		return examenValoracionResponseMapper.toDocenteDto(examenValoracionRes);
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorResponseDto insertarInformacionCoordinador(
			SolicitudExamenValoracionCoordinadorDto examenValoracionDto,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionDto.getIdTrabajoGrados())
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + examenValoracionDto.getIdTrabajoGrados() + " No encontrado"));

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(examenValoracionDto.getIdTrabajoGrados());

		// Map<String, String> validacionCamposUnicos =
		// validacionCampoUnicos(obtenerCamposUnicos(examenValoracionDto),
		// null);
		//
		// if (!validacionCamposUnicos.isEmpty()) {
		// throw new FieldUniqueException(validacionCamposUnicos);
		// }
		//

		// Mapear DTO a entidad
		// SolicitudExamenValoracion examenValoracion =
		// examenValoracionMapper.toEntity(examenValoracionDto);

		// Establecer la relación uno a uno
		// examenValoracion.setIdTrabajoGrado(trabajoGrado);
		// trabajoGrado.setExamenValoracion(examenValoracion);

		// Se cambia el numero de estado
		trabajoGrado.setNumeroEstado(3);

		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionTmp.getTitulo());
		Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
		String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
		String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
		String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

		examenValoracionDto
				.setLinkOficioDirigidoEvaluadores(
						FilesUtilities
								.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
										examenValoracionDto.getLinkOficioDirigidoEvaluadores(), nombreCarpeta));

		agregarInformacionCoordinador(examenValoracionTmp, examenValoracionDto);

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionTmp);

		enviarCorreoEvaluadores(examenValoracionTmp, examenValoracionDto);

		return examenValoracionResponseMapper.toCoordinadorDto(examenValoracionRes);
	}

	// Funciones privadas
	private void agregarInformacionCoordinador(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorDto examenValoracionDto) {

		examenValoracion.setActaAprobacionExamen(examenValoracionDto.getActaAprobacionExamen());
		examenValoracion.setFechaActa(examenValoracionDto.getFechaActa());
		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		// Update archivos
		examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
	}

	private boolean enviarCorreoEvaluadores(SolicitudExamenValoracion examenValoracionTmp,
			SolicitudExamenValoracionCoordinadorDto solicitudExamenValoracionCoordinadorDto) {
		try {
			ArrayList<String> correos = new ArrayList<>();
			Map<String, Object> templateModel = new HashMap<>();

			// Obtener correos de los evaluadores
			DocenteResponseDto docente = archivoClient
					.obtenerDocentePorId(Long.parseLong(examenValoracionTmp.getEvaluadorInterno()));
			correos.add(docente.getPersona().getCorreoElectronico());
			ExpertoResponseDto experto = archivoClientExpertos
					.obtenerExpertoPorId(Long.parseLong(examenValoracionTmp.getEvaluadorExterno()));
			correos.add(experto.getPersona().getCorreoElectronico());

			// Iterar sobre cada correo electrónico para enviar los mensajes individualmente
			for (String correo : correos) {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true); // habilitar modo multipart

				// Configurar variables del contexto para la plantilla
				// templateModel.put("nombreEvaluador", "Nombre del Evaluador");
				templateModel.put("mensaje", solicitudExamenValoracionCoordinadorDto.getInformacionEnvioEvaluador().getMensaje());

				// Crear el contexto para el motor de plantillas
				Context context = new Context();
				context.setVariables(templateModel);

				// Procesar la plantilla de correo electrónico
				String html = templateEngine.process("emailTemplate", context);

				helper.setTo(correo);
				helper.setSubject(solicitudExamenValoracionCoordinadorDto.getInformacionEnvioEvaluador().getAsunto());
				helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

				// Obtener documentos y adjuntarlos
				Map<String, String> documentosParaEvaluador = solicitudExamenValoracionCoordinadorDto
						.getInformacionEnvioEvaluador().getDocumentos();
				for (Map.Entry<String, String> entry : documentosParaEvaluador.entrySet()) {
					String nombreDocumento = entry.getKey();
					String base64Documento = entry.getValue();
					byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
					ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes, "application/pdf");
					helper.addAttachment(nombreDocumento + ".pdf", dataSource); // Cambiar a .pdf si es necesario
				}

				// Enviar el mensaje
				mailSender.send(message);
			}

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	// @Override
	// @Transactional
	// public SolicitudExamenValoracionResponseDto
	// crear(SolicitudExamenValoracionDto examenValoracionDto,
	// BindingResult result) {
	// if (result.hasErrors()) {
	// throw new FieldErrorException(result);
	// }

	// TrabajoGrado trabajoGrado =
	// trabajoGradoRepository.findById(examenValoracionDto.getIdTrabajoGrados())
	// .orElseThrow(() -> new ResourceNotFoundException(
	// "TrabajoGrado con id: " + examenValoracionDto.getIdTrabajoGrados() + " No
	// encontrado"));

	// Map<String, String> validacionCamposUnicos =
	// validacionCampoUnicos(obtenerCamposUnicos(examenValoracionDto),
	// null);
	// if (!validacionCamposUnicos.isEmpty()) {
	// throw new FieldUniqueException(validacionCamposUnicos);
	// }

	// // Obtener iniciales del trabajo de grado
	// String procesoVa = "Solicitud_Examen_Valoracion";
	// String tituloTrabajoGrado =
	// ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());

	// Long idenficiacionEstudiante =
	// trabajoGrado.getEstudiante().getPersona().getIdentificacion();
	// String nombreEstudiante =
	// trabajoGrado.getEstudiante().getPersona().getNombre();
	// String apellidoEstudiante =
	// trabajoGrado.getEstudiante().getPersona().getApellido();
	// String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_"
	// + apellidoEstudiante;

	// // Mapear DTO a entidad
	// SolicitudExamenValoracion examenValoracion =
	// examenValoracionMapper.toEntity(examenValoracionDto);

	// // Establecer la relación uno a uno
	// examenValoracion.setIdTrabajoGrado(trabajoGrado);
	// trabajoGrado.setExamenValoracion(examenValoracion);

	// // Se cambia el numero de estado
	// trabajoGrado.setNumeroEstado(1);
	// trabajoGrado.setTitulo(examenValoracionDto.getTitulo());

	// // Guardar la entidad ExamenValoracion
	// examenValoracion
	// .setLinkFormatoA(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
	// procesoVa,
	// examenValoracion.getLinkFormatoA(), nombreCarpeta));
	// examenValoracion
	// .setLinkFormatoD(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
	// procesoVa,
	// examenValoracion.getLinkFormatoD(), nombreCarpeta));
	// examenValoracion
	// .setLinkFormatoE(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
	// procesoVa,
	// examenValoracion.getLinkFormatoE(), nombreCarpeta));
	// examenValoracion.setLinkOficioDirigidoEvaluadores(
	// FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
	// examenValoracion.getLinkOficioDirigidoEvaluadores(), nombreCarpeta));

	// SolicitudExamenValoracion examenValoracionRes =
	// solicitudExamenValoracionRepository.save(examenValoracion);

	// return examenValoracionResponseMapper.toDto(examenValoracionRes);
	// }

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionDocenteResponseListDto listarInformacionDocente(Long idTrabajoGrado) {
		Optional<SolicitudExamenValoracionResponseDto> responseDtoOptional = solicitudExamenValoracionRepository
				.findByIdTrabajoGradoId(idTrabajoGrado);
		System.out.println("tiene::: " + responseDtoOptional);

		if (!responseDtoOptional.isPresent()) {
			return null;
		}

		SolicitudExamenValoracionResponseDto responseDto = responseDtoOptional.get();

		// Obtener y construir información del evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(Long.parseLong(responseDto.getEvaluadorInterno()));
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener y construir información del evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(Long.parseLong(responseDto.getEvaluadorExterno()));
		String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
		Map<String, String> evaluadorExternoMap = new HashMap<>();
		evaluadorExternoMap.put("nombres", nombre_experto);
		evaluadorExternoMap.put("universidad", experto.getUniversidad());
		evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

		// Crear y poblar el nuevo DTO
		SolicitudExamenValoracionDocenteResponseListDto docenteResponseDto = new SolicitudExamenValoracionDocenteResponseListDto();
		docenteResponseDto.setIdExamenValoracion(responseDto.getIdExamenValoracion());
		docenteResponseDto.setTitulo(responseDto.getTitulo());
		docenteResponseDto.setLinkFormatoA(responseDto.getLinkFormatoA());
		docenteResponseDto.setLinkFormatoD(responseDto.getLinkFormatoD());
		docenteResponseDto.setLinkFormatoE(responseDto.getLinkFormatoE());
		docenteResponseDto.setEvaluadorInterno(evaluadorInternoMap);
		docenteResponseDto.setEvaluadorExterno(evaluadorExternoMap);

		return docenteResponseDto;
	}

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionResponseDto listarInformacionCoordinador(Long idTrabajoGrado) {
		return solicitudExamenValoracionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
				.stream()
				.map(examenValoracionResponseMapper::toDto)
				.findFirst()
				.orElse(null);
	}

	// @Override
	// public SolicitudExamenValoracionResponseDto actualizar(Long id,
	// SolicitudExamenValoracionDto examenValoracionDto,
	// BindingResult result) {

	// SolicitudExamenValoracion examenValoracionTmp =
	// solicitudExamenValoracionRepository.findById(id).orElseThrow(
	// () -> new ResourceNotFoundException("Examen de valoracion con id: " + id + "
	// no encontrado"));

	// // Busca el trabajo de grado
	// TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
	// () -> new ResourceNotFoundException("Trabajo de grado con id: " + id + " no
	// encontrado"));

	// String procesoVa = "Solicitud_Examen_Valoracion";
	// String tituloTrabajoGrado =
	// ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());
	// Long idenficiacionEstudiante =
	// trabajoGrado.getEstudiante().getPersona().getIdentificacion();
	// String nombreEstudiante =
	// trabajoGrado.getEstudiante().getPersona().getNombre();
	// String apellidoEstudiante =
	// trabajoGrado.getEstudiante().getPersona().getApellido();
	// String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_"
	// + apellidoEstudiante;

	// SolicitudExamenValoracion responseExamenValoracion = null;
	// if (examenValoracionTmp != null) {
	// if
	// (examenValoracionDto.getLinkFormatoA().compareTo(examenValoracionTmp.getLinkFormatoA())
	// != 0) {
	// examenValoracionDto
	// .setLinkFormatoA(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
	// procesoVa,
	// examenValoracionDto.getLinkFormatoA(), nombreCarpeta));
	// FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoA());
	// }
	// if
	// (examenValoracionDto.getLinkFormatoD().compareTo(examenValoracionTmp.getLinkFormatoD())
	// != 0) {
	// examenValoracionDto
	// .setLinkFormatoD(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
	// procesoVa,
	// examenValoracionDto.getLinkFormatoD(), nombreCarpeta));
	// FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoD());
	// }
	// if
	// (examenValoracionDto.getLinkFormatoE().compareTo(examenValoracionTmp.getLinkFormatoE())
	// != 0) {
	// examenValoracionDto
	// .setLinkFormatoE(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
	// procesoVa,
	// examenValoracionDto.getLinkFormatoE(), nombreCarpeta));
	// FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoE());
	// }
	// if (examenValoracionDto.getLinkOficioDirigidoEvaluadores()
	// .compareTo(examenValoracionTmp.getLinkOficioDirigidoEvaluadores()) != 0) {
	// examenValoracionDto
	// .setLinkOficioDirigidoEvaluadores(
	// FilesUtilities
	// .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
	// examenValoracionDto.getLinkOficioDirigidoEvaluadores(), nombreCarpeta));
	// FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
	// }
	// updateExamenValoracionValues(examenValoracionTmp, examenValoracionDto,
	// trabajoGrado);
	// responseExamenValoracion =
	// solicitudExamenValoracionRepository.save(examenValoracionTmp);
	// }
	// return examenValoracionResponseMapper.toDto(responseExamenValoracion);
	// }

	@Override
	public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long id,
			SolicitudExamenValoracionDocenteDto examenValoracionDto,
			BindingResult result) {

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));

		// Busca el trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Trabajo de grado con id: " + id + " no encontrado"));

		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());
		Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
		String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
		String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
		String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

		SolicitudExamenValoracion responseExamenValoracion = null;
		if (examenValoracionTmp != null) {
			if (examenValoracionDto.getLinkFormatoA().compareTo(examenValoracionTmp.getLinkFormatoA()) != 0) {
				examenValoracionDto
						.setLinkFormatoA(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
								examenValoracionDto.getLinkFormatoA(), nombreCarpeta));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoA());
			}
			if (examenValoracionDto.getLinkFormatoD().compareTo(examenValoracionTmp.getLinkFormatoD()) != 0) {
				examenValoracionDto
						.setLinkFormatoD(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
								examenValoracionDto.getLinkFormatoD(), nombreCarpeta));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoD());
			}
			if (examenValoracionDto.getLinkFormatoE().compareTo(examenValoracionTmp.getLinkFormatoE()) != 0) {
				examenValoracionDto
						.setLinkFormatoE(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
								examenValoracionDto.getLinkFormatoE(), nombreCarpeta));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoE());
			}
			updateExamenValoracionDocenteValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
			responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		}
		return examenValoracionResponseMapper.toDocenteDto(responseExamenValoracion);
	}

	@Override
	public SolicitudExamenValoracionCoordinadorResponseDto actualizarInformacionCoordinador(Long id,
			SolicitudExamenValoracionCoordinadorDto examenValoracionDto,
			BindingResult result) {

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));

		// Busca el trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Trabajo de grado con id: " + id + " no encontrado"));

		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionTmp.getTitulo());
		Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
		String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
		String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
		String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

		SolicitudExamenValoracion responseExamenValoracion = null;
		if (examenValoracionTmp != null) {
			if (examenValoracionDto.getLinkOficioDirigidoEvaluadores()
					.compareTo(examenValoracionTmp.getLinkOficioDirigidoEvaluadores()) != 0) {
				examenValoracionDto
						.setLinkOficioDirigidoEvaluadores(
								FilesUtilities
										.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
												examenValoracionDto.getLinkOficioDirigidoEvaluadores(), nombreCarpeta));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
			}
			updateExamenValoracionCoordinadorValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
			responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		}
		return examenValoracionResponseMapper.toCoordinadorDto(responseExamenValoracion);
	}

	@Override
	@Transactional(readOnly = true)
	public String descargarArchivo(RutaArchivoDto rutaArchivo) {
		return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean enviarCorreoElectronicoCorrecion(EnvioEmailCorrecionDto envioEmailCorrecionDto,
			BindingResult result) {
		try {
			TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(envioEmailCorrecionDto.getIdTrabajoGrado())
					.orElseThrow(
							() -> new ResourceNotFoundException("Trabajo de grado con id: "
									+ envioEmailCorrecionDto.getIdTrabajoGrado() + " no encontrado"));

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			Map<String, Object> templateModel = new HashMap<>();
			templateModel.put("title", envioEmailCorrecionDto.getTituloAsunto());
			templateModel.put("message", envioEmailCorrecionDto.getMensaje());

			Context context = new Context();
			context.setVariables(templateModel);

			String html = templateEngine.process("emailTemplate", context);

			helper.setTo(trabajoGrado.getCorreoElectronicoTutor());
			helper.setSubject(envioEmailCorrecionDto.getTituloAsunto());
			helper.setText(html, true);

			mailSender.send(message);
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public DatosFormatoBResponseDto obtenerInformacionFormatoB(Long idTrabajoGrado) {

		// Obtener informacion trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + idTrabajoGrado + " No encontrado"));

		// Obtener informacion estudiante
		EstudianteResponseDtoAll estudiante = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getEstudiante().getId());

		// Obtener informacion examen de valoracion
		Optional<SolicitudExamenValoracionResponseDto> examenValoracion = solicitudExamenValoracionRepository
				.findByIdTrabajoGradoId(idTrabajoGrado);
		SolicitudExamenValoracionResponseDto responseDto = examenValoracion.get();

		// Obtener informacion evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(Long.parseLong(responseDto.getEvaluadorInterno()));
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener informacion evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(Long.parseLong(responseDto.getEvaluadorExterno()));
		String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
		Map<String, String> evaluadorExternoMap = new HashMap<>();
		evaluadorExternoMap.put("nombres", nombre_experto);
		evaluadorExternoMap.put("universidad", experto.getUniversidad());
		evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

		return new DatosFormatoBResponseDto(trabajoGrado.getTitulo(),
				estudiante.getPersona().getNombre() + " " + estudiante.getPersona().getApellido(),
				evaluadorInternoMap, evaluadorExternoMap);
	}

	// Funciones privadas
	private void updateExamenValoracionDocenteValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, TrabajoGrado trabajoGrado) {

		if (!examenValoracionDto.getTitulo().equals(examenValoracionDto.getTitulo())) {
			String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());
			FilesUtilities.deleteFolderAndItsContents(tituloTrabajoGrado);
		}

		trabajoGrado.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setEvaluadorExterno(examenValoracionDto.getEvaluadorExterno());
		examenValoracion.setEvaluadorInterno(examenValoracionDto.getEvaluadorInterno());
		// Update archivos
		examenValoracion.setLinkFormatoA(examenValoracionDto.getLinkFormatoA());
		examenValoracion.setLinkFormatoD(examenValoracionDto.getLinkFormatoD());
		examenValoracion.setLinkFormatoE(examenValoracionDto.getLinkFormatoE());
	}

	private void updateExamenValoracionCoordinadorValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorDto examenValoracionDto, TrabajoGrado trabajoGrado) {

		examenValoracion.setActaAprobacionExamen(examenValoracionDto.getActaAprobacionExamen());
		examenValoracion.setFechaActa(examenValoracionDto.getFechaActa());
		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		// Update archivos
		examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
	}

	private CamposUnicosSolicitudExamenValoracionDto obtenerCamposUnicos(
			SolicitudExamenValoracionDocenteDto docenteSaveDto) {
		return informacionUnicaSolicitudExamenValoracion.apply(docenteSaveDto);
	}

	private Map<String, String> validacionCampoUnicos(CamposUnicosSolicitudExamenValoracionDto camposUnicos,
			CamposUnicosSolicitudExamenValoracionDto camposUnicosBD) {

		Map<String, Function<CamposUnicosSolicitudExamenValoracionDto, Boolean>> mapCamposUnicos = new HashMap<>();

		mapCamposUnicos.put("idTrabajoGrados",
				dto -> (camposUnicosBD == null || !dto.getIdTrabajoGrados().equals(camposUnicosBD.getIdTrabajoGrados()))
						&& solicitudExamenValoracionRepository.countByTrabajoGradoId(dto.getIdTrabajoGrados()) > 0);

		Predicate<Field> existeCampoUnico = campo -> mapCamposUnicos.containsKey(campo.getName());
		Predicate<Field> existeCampoBD = campoBD -> mapCamposUnicos.get(campoBD.getName()).apply(camposUnicos);
		Predicate<Field> campoInvalido = existeCampoUnico.and(existeCampoBD);

		return Arrays.stream(camposUnicos.getClass().getDeclaredFields())
				.filter(campoInvalido)
				.peek(field -> field.setAccessible(true))
				.collect(Collectors.toMap(Field::getName, field -> {
					Object valorCampo = null;
					try {
						valorCampo = field.get(camposUnicos);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					return mensajeException(field.getName(), valorCampo);
				}));

	}

	private <T> String mensajeException(String nombreCampo, T valorCampo) {
		return "Campo único, ya existe un registrado una SOLICITUD DE EXAMEN DE VALORACION al trabajo de grado: "
				+ valorCampo;
	}

}
