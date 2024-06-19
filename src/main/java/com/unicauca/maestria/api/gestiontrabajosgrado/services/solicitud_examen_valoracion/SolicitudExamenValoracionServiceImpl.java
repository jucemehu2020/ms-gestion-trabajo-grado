package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComite;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.DatosFormatoBResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.EnvioEmailCorrecionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.ObtenerDocumentosParaEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.*;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitudExamenValoracionServiceImpl implements SolicitudExamenValoracionService {

	// Repository
	private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
	private final AnexosSolicitudExamenValoracionRepository anexosSolicitudExamenValoracionRepository;
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
		List<DocenteResponseDto> docenteResponse = archivoClient.listarDocentesRes();
		System.out.println("Docente response: " + docenteResponse);
		if (docenteResponse.size() == 0) {
			throw new InformationException("No hay docentes registrados");
		}
		List<DocenteInfoDto> docentes = docenteResponse.stream()
				.map(docente -> new DocenteInfoDto(
						docente.getId(),
						docente.getPersona().getNombre(),
						docente.getPersona().getApellido(),
						docente.getPersona().getCorreoElectronico(),
						"Universidad del Cauca"))
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

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		if (trabajoGrado.getNumeroEstado() == 0 || trabajoGrado.getNumeroEstado() == 2
				|| trabajoGrado.getNumeroEstado() == 4) {

			Map<String, String> validacionCamposUnicos = validacionCampoUnicos(obtenerCamposUnicos(examenValoracionDto),
					null);
			if (!validacionCamposUnicos.isEmpty()) {
				throw new FieldUniqueException(validacionCamposUnicos);
			}

			// Obtener iniciales del trabajo de grado
			String procesoVa = "Solicitud_Examen_Valoracion";
			String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());

			Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
			String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
			String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
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

			// Crear y agregar anexos a la entidad principal
			List<AnexoSolicitudExamenValoracion> updatedLinkAnexos = new ArrayList<>();
			for (AnexoSolicitudExamenValoracion linkAnexoDto : examenValoracionDto.getAnexos()) {
				String updatedAnexo = FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
						linkAnexoDto.getLinkAnexo(),
						nombreCarpeta);
				AnexoSolicitudExamenValoracion anexo = new AnexoSolicitudExamenValoracion();
				anexo.setLinkAnexo(updatedAnexo);
				anexo.setSolicitudExamenValoracion(examenValoracion);
				updatedLinkAnexos.add(anexo);
			}
			examenValoracion.setAnexos(updatedLinkAnexos);

			// examenValoracion
			// .setLinkAnexos(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
			// procesoVa,
			// examenValoracion.getLinkAnexos(), nombreCarpeta));

			SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracion);

			return examenValoracionResponseMapper.toDocenteDto(examenValoracionRes);

		} else {
			throw new InformationException("No es permitido registrar esta solicitud");
		}
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorFase1Dto insertarInformacionCoordinadorFase1(
			SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionDto,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		// CAMBIAR POR EXAMEN DE VALORACION
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionDto.getIdExamenValoracion())
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + examenValoracionDto.getIdExamenValoracion() + " No encontrado"));

		SolicitudExamenValoracion examenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(examenValoracionDto.getIdExamenValoracion());

		// SolicitudExamenValoracion examenValoracion =
		// solicitudExamenValoracionRepository
		// .findById(examenValoracionDto.getIdExamenValoracion()).orElseThrow(
		// () -> new ResourceNotFoundException("Examen de valoracion con id: "
		// + examenValoracionDto.getIdExamenValoracion() + " no encontrado"));

		// TrabajoGrado trabajoGrado =
		// trabajoGradoRepository.findById(examenValoracion.getIdTrabajoGrado().getId())
		// .orElseThrow(
		// () -> new ResourceNotFoundException("Trabajo de grado con id: "
		// + examenValoracion.getIdTrabajoGrado().getId() + " no encontrado"));

		// Map<String, String> validacionCamposUnicos =
		// validacionCampoUnicos(obtenerCamposUnicos(examenValoracionDto),
		// null);
		//
		// if (!validacionCamposUnicos.isEmpty()) {
		// throw new FieldUniqueException(validacionCamposUnicos);
		// }
		//

		// Se cambia el numero de estado
		if (examenValoracionDto.getConceptoCoordinadorDocumentos().equals("Aprobado")) {
			trabajoGrado.setNumeroEstado(3);
		} else {
			trabajoGrado.setNumeroEstado(2);
		}

		examenValoracion.setConceptoCoordinadorDocumentos(examenValoracionDto.getConceptoCoordinadorDocumentos());

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracion);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(examenValoracionRes);
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorResponseDto insertarInformacionCoordinadorFase2(
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionDto.getIdTrabajoGrados())
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + examenValoracionDto.getIdTrabajoGrados() + " No encontrado"));

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(examenValoracionDto.getIdTrabajoGrados());

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		if (examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals("Aprobado")) {
			trabajoGrado.setNumeroEstado(5);
		} else {
			trabajoGrado.setNumeroEstado(4);
		}

		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionTmp.getTitulo());
		Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
		String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
		String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
		String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

		examenValoracionDto
				.setLinkOficioDirigidoEvaluadores(
						FilesUtilities
								.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
										examenValoracionDto.getLinkOficioDirigidoEvaluadores(), nombreCarpeta));

		agregarInformacionCoordinador(examenValoracionTmp, examenValoracionDto);

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionTmp);

		if (examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals("Aprobado")) {
			enviarCorreoEvaluadores(examenValoracionTmp, examenValoracionDto);
		} else {
			enviarCorreoCorrecion(examenValoracionTmp, examenValoracionDto);
		}

		return examenValoracionResponseMapper.toCoordinadorFase2Dto(examenValoracionRes);
	}

	// Funciones privadas
	private void agregarInformacionCoordinador(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto) {

		// Crear una nueva instancia de RespuestaComite
		RespuestaComite respuestaComite = RespuestaComite.builder()
				.conceptoComite(examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite())
				.numeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa())
				.fechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa().toString())
				.solicitudExamenValoracion(examenValoracion)
				.build();

		// Si la colección está vacía, inicializarla
		if (examenValoracion.getActaFechaRespuestaComite() == null) {
			examenValoracion.setActaFechaRespuestaComite(new ArrayList<>());
		}

		// Agregar la nueva respuesta a la lista existente
		examenValoracion.getActaFechaRespuestaComite().add(respuestaComite);

		// Actualizar los campos existentes
		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
	}

	private boolean enviarCorreoEvaluadores(SolicitudExamenValoracion examenValoracionTmp,
			SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorDto) {
		try {
			ArrayList<String> correos = new ArrayList<>();
			Map<String, Object> templateModel = new HashMap<>();

			// Obtener correos de los evaluadores
			DocenteResponseDto docente = archivoClient
					.obtenerDocentePorId(Long.parseLong(examenValoracionTmp.getIdEvaluadorInterno()));
			correos.add(docente.getPersona().getCorreoElectronico());
			ExpertoResponseDto experto = archivoClientExpertos
					.obtenerExpertoPorId(Long.parseLong(examenValoracionTmp.getIdEvaluadorExterno()));
			correos.add(experto.getPersona().getCorreoElectronico());

			// Iterar sobre cada correo electrónico para enviar los mensajes individualmente
			for (String correo : correos) {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true); // habilitar modo multipart

				// Configurar variables del contexto para la plantilla
				// templateModel.put("nombreEvaluador", "Nombre del Evaluador");
				templateModel.put("mensaje",
						solicitudExamenValoracionCoordinadorDto.getEnvioEmailDto().getMensaje());

				// Crear el contexto para el motor de plantillas
				Context context = new Context();
				context.setVariables(templateModel);

				// Procesar la plantilla de correo electrónico
				String html = templateEngine.process("emailTemplate", context);

				helper.setTo(correo);
				helper.setSubject(solicitudExamenValoracionCoordinadorDto.getEnvioEmailDto().getAsunto());
				helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

				// Obtener documentos y adjuntarlos
				Map<String, Object> documentosParaEvaluador = solicitudExamenValoracionCoordinadorDto
						.getInformacionEnvioEvaluador().getDocumentos();

				for (Map.Entry<String, Object> entry : documentosParaEvaluador.entrySet()) {
					String nombreDocumento = entry.getKey();
					Object valorDocumento = entry.getValue();

					if (valorDocumento instanceof String) {
						// Manejar los documentos que son cadenas
						String base64Documento = (String) valorDocumento;
						byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
						ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes, "application/pdf");
						helper.addAttachment(nombreDocumento + ".pdf", dataSource); // Cambiar a .pdf si es necesario
					} else if (valorDocumento instanceof List) {
						// Manejar la lista de anexos
						List<String> listaAnexos = (List<String>) valorDocumento;
						for (int i = 0; i < listaAnexos.size(); i++) {
							String base64Anexo = listaAnexos.get(i);
							byte[] anexoBytes = Base64.getDecoder().decode(base64Anexo);
							ByteArrayDataSource dataSource = new ByteArrayDataSource(anexoBytes, "application/pdf");
							helper.addAttachment(nombreDocumento + "_" + (i + 1) + ".pdf", dataSource); // Cambiar a
																										// .pdf si es
																										// necesario
						}
					}
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

	private boolean enviarCorreoCorrecion(SolicitudExamenValoracion examenValoracionTmp,
			SolicitudExamenValoracionCoordinadorFase2Dto solicitudExamenValoracionCoordinadorDto) {

		try {
			ArrayList<String> correos = new ArrayList<>();

			TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionTmp.getIdTrabajoGrado().getId())
					.orElseThrow(
							() -> new ResourceNotFoundException("Trabajo de grado con id: "
									+ examenValoracionTmp.getIdTrabajoGrado().getId() + " no encontrado"));

			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

			correos.add(estudiante.getPersona().getCorreoElectronico());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());

			// Iterar sobre cada correo electrónico para enviar los mensajes individualmente
			for (String correo : correos) {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);

				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("title", solicitudExamenValoracionCoordinadorDto.getEnvioEmailDto().getAsunto());
				templateModel.put("message", solicitudExamenValoracionCoordinadorDto.getEnvioEmailDto().getMensaje());

				Context context = new Context();
				context.setVariables(templateModel);

				String html = templateEngine.process("emailTemplate", context);

				helper.setTo(correo);
				helper.setSubject(solicitudExamenValoracionCoordinadorDto.getEnvioEmailDto().getAsunto());
				helper.setText(html, true);

				mailSender.send(message);
			}

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionDocenteResponseListDto listarInformacionDocente(Long idTrabajoGrado) {
		Optional<SolicitudExamenValoracion> entityOptional = solicitudExamenValoracionRepository
				.findByIdTrabajoGradoId(idTrabajoGrado);

		// SolicitudExamenValoracion entity = entityOptional.get();
		SolicitudExamenValoracionResponseDto responseDto = examenValoracionResponseMapper.toDto(entityOptional.get());

		// Obtener y construir información del evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(Long.parseLong(entityOptional.get().getIdEvaluadorInterno()));
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener y construir información del evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(Long.parseLong(entityOptional.get().getIdEvaluadorExterno()));
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

		List<AnexoSolicitudExamenValoracion> anexosSolicitudExamenValoracion = anexosSolicitudExamenValoracionRepository
				.obtenerAnexosPorId(responseDto.getIdExamenValoracion());

		ArrayList<String> listaAnexos = new ArrayList<>();
		for (AnexoSolicitudExamenValoracion anexo : anexosSolicitudExamenValoracion) {
			listaAnexos.add(anexo.getLinkAnexo());
		}

		docenteResponseDto.setAnexos(listaAnexos);
		docenteResponseDto.setEvaluadorInterno(evaluadorInternoMap);
		docenteResponseDto.setEvaluadorExterno(evaluadorExternoMap);

		return docenteResponseDto;
	}

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionResponseDto listarInformacionCoordinador(Long idTrabajoGrado) {
		Optional<SolicitudExamenValoracion> entityOptional = solicitudExamenValoracionRepository
				.findByIdTrabajoGradoId(idTrabajoGrado);

		// if (!entityOptional.isPresent()) {
		// return null;
		// }

		// SolicitudExamenValoracion entity = entityOptional.get();
		SolicitudExamenValoracionResponseDto responseDto = examenValoracionResponseMapper.toDto(entityOptional.get());

		// Obtener y construir información del evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(Long.parseLong(entityOptional.get().getIdEvaluadorInterno()));
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener y construir información del evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(Long.parseLong(entityOptional.get().getIdEvaluadorExterno()));
		String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
		Map<String, String> evaluadorExternoMap = new HashMap<>();
		evaluadorExternoMap.put("nombres", nombre_experto);
		evaluadorExternoMap.put("universidad", experto.getUniversidad());
		evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

		responseDto.setEvaluadorInterno(evaluadorInternoMap);
		responseDto.setEvaluadorExterno(evaluadorExternoMap);

		return responseDto;
	}

	@Override
	public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long id,
			SolicitudExamenValoracionDocenteDto examenValoracionDto,
			BindingResult result) {

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));

		// Busca el trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Trabajo de grado con id: " + id + " no encontrado"));

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());
		Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
		String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
		String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
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
			// if
			// (examenValoracionDto.getLinkAnexos().compareTo(examenValoracionTmp.getLinkAnexos())
			// != 0) {
			// examenValoracionDto
			// .setLinkAnexos(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado,
			// procesoVa,
			// examenValoracionDto.getLinkAnexos(), nombreCarpeta));
			// FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkAnexos());
			// }
			updateExamenValoracionDocenteValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
			responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		}
		return examenValoracionResponseMapper.toDocenteDto(responseExamenValoracion);
	}

	@Override
	public SolicitudExamenValoracionCoordinadorResponseDto actualizarInformacionCoordinador(Long id,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto,
			BindingResult result) {

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));

		// Busca el trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Trabajo de grado con id: " + id + " no encontrado"));

		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		String procesoVa = "Solicitud_Examen_Valoracion";
		String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracionTmp.getTitulo());
		Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
		String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
		String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
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
		return examenValoracionResponseMapper.toCoordinadorFase2Dto(responseExamenValoracion);
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
			ArrayList<String> correos = new ArrayList<>();

			TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(envioEmailCorrecionDto.getIdTrabajoGrado())
					.orElseThrow(
							() -> new ResourceNotFoundException("Trabajo de grado con id: "
									+ envioEmailCorrecionDto.getIdTrabajoGrado() + " no encontrado"));

			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

			correos.add(estudiante.getPersona().getCorreoElectronico());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());

			// Iterar sobre cada correo electrónico para enviar los mensajes individualmente
			for (String correo : correos) {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);

				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("title", envioEmailCorrecionDto.getTituloAsunto());
				templateModel.put("message", envioEmailCorrecionDto.getMensaje());

				Context context = new Context();
				context.setVariables(templateModel);

				String html = templateEngine.process("emailTemplate", context);

				helper.setTo(correo);
				helper.setSubject(envioEmailCorrecionDto.getTituloAsunto());
				helper.setText(html, true);

				mailSender.send(message);
			}

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public DatosFormatoBResponseDto obtenerInformacionFormatoB(Long idTrabajoGrado) {

		// Obtener información trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + idTrabajoGrado + " No encontrado"));

		// Obtener información estudiante
		EstudianteResponseDtoAll estudiante = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		// Obtener información examen de valoración
		Optional<SolicitudExamenValoracion> examenValoracion = solicitudExamenValoracionRepository
				.findByIdTrabajoGradoId(idTrabajoGrado);

		if (!examenValoracion.isPresent()) {
			throw new ResourceNotFoundException(
					"SolicitudExamenValoracion con idTrabajoGrado: " + idTrabajoGrado + " No encontrada");
		}

		// SolicitudExamenValoracion entity = examenValoracion.get();
		// SolicitudExamenValoracionResponseDto responseDto =
		// examenValoracionResponseMapper.toDto(entity);

		// Obtener información evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(Long.parseLong(examenValoracion.get().getIdEvaluadorInterno()));
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener información evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(Long.parseLong(examenValoracion.get().getIdEvaluadorExterno()));
		String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
		Map<String, String> evaluadorExternoMap = new HashMap<>();
		evaluadorExternoMap.put("nombres", nombre_experto);
		evaluadorExternoMap.put("universidad", experto.getUniversidad());
		evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

		return new DatosFormatoBResponseDto(trabajoGrado.getTitulo(),
				estudiante.getPersona().getNombre() + " " + estudiante.getPersona().getApellido(),
				evaluadorInternoMap, evaluadorExternoMap);
	}

	@Override
	@Transactional(readOnly = true)
	public ObtenerDocumentosParaEvaluadorDto obtenerDocumentosParaEvaluador(Long idExamenValoracion) {

		SolicitudExamenValoracion examenValoracion = solicitudExamenValoracionRepository.findById(idExamenValoracion)
				.orElseThrow(
						() -> new ResourceNotFoundException(
								"Examen de valoracion con id: " + idExamenValoracion + " no encontrado"));

		List<AnexoSolicitudExamenValoracion> anexosSolicitudExamenValoracion = anexosSolicitudExamenValoracionRepository
				.obtenerAnexosPorId(examenValoracion.getIdExamenValoracion());

		ArrayList<String> listaAnexos = new ArrayList();
		for (int documento = 0; documento < anexosSolicitudExamenValoracion.size(); documento++) {
			listaAnexos.add(
					FilesUtilities.recuperarArchivo(anexosSolicitudExamenValoracion.get(documento).getLinkAnexo()));
		}

		ObtenerDocumentosParaEvaluadorDto obtenerDocumentosParaEvaluadorDto = new ObtenerDocumentosParaEvaluadorDto(
				FilesUtilities.recuperarArchivo(examenValoracion.getLinkFormatoD()),
				FilesUtilities.recuperarArchivo(examenValoracion.getLinkFormatoE()),
				listaAnexos);

		return obtenerDocumentosParaEvaluadorDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TrabajoGradoResponseDto> listarEstadosExamenValoracion(Integer numeroEstado) {

		List<TrabajoGrado> listaTrabajoGrado = trabajoGradoRepository.findByNumeroEstado(numeroEstado);
		List<TrabajoGradoResponseDto> trabajosGradoDto = listaTrabajoGrado.stream().map(trabajo -> {
			EstadoTrabajoGrado estadoEnum = EstadoTrabajoGrado.values()[trabajo.getNumeroEstado()];
			return TrabajoGradoResponseDto.builder()
					.id(trabajo.getId())
					.estado(estadoEnum.getMensaje())
					.fechaCreacion(trabajo.getFechaCreacion())
					.titulo(trabajo.getTitulo() != null ? trabajo.getTitulo() : "Título no disponible")
					.numeroEstado(trabajo.getNumeroEstado())
					.build();
		}).collect(Collectors.toList());
		return trabajosGradoDto;
	}

	// Funciones privadas
	private void updateExamenValoracionDocenteValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, TrabajoGrado trabajoGrado) {

		// if (!examenValoracionDto.getTitulo().equals(examenValoracionDto.getTitulo()))
		// {
		// String tituloTrabajoGrado =
		// ConvertString.obtenerIniciales(examenValoracionDto.getTitulo());
		// FilesUtilities.deleteFolderAndItsContents(tituloTrabajoGrado);
		// }

		// trabajoGrado.setTitulo(examenValoracionDto.getTitulo());
		// examenValoracion.setTitulo(examenValoracionDto.getTitulo());
		// examenValoracion.setEvaluadorExterno(examenValoracionDto.getEvaluadorExterno());
		// examenValoracion.setEvaluadorInterno(examenValoracionDto.getEvaluadorInterno());
		// // Update archivos
		// examenValoracion.setLinkFormatoA(examenValoracionDto.getLinkFormatoA());
		// examenValoracion.setLinkFormatoD(examenValoracionDto.getLinkFormatoD());
		// examenValoracion.setLinkFormatoE(examenValoracionDto.getLinkFormatoE());
	}

	private void updateExamenValoracionCoordinadorValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, TrabajoGrado trabajoGrado) {

		// examenValoracion.setActaAprobacionExamen(examenValoracionDto.getActaAprobacionExamen());
		// examenValoracion.setFechaActa(examenValoracionDto.getFechaActa());
		// examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		// // Update archivos
		// examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
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
