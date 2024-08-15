package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.AnexoSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.*;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSolicitudRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitudExamenValoracionServiceImpl implements SolicitudExamenValoracionService {

	private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
	private final RespuestaComiteSolicitudRepository respuestaComiteSolicitudRepository;
	private final AnexosSolicitudExamenValoracionRepository anexosSolicitudExamenValoracionRepository;
	private final TrabajoGradoRepository trabajoGradoRepository;
	private final TiemposPendientesRepository tiemposPendientesRepository;
	private final SolicitudExamenValoracionMapper examenValoracionMapper;
	private final SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
	private final AnexoSolicitudExamenValoracionMapper anexoSolicitudExamenValoracionMapper;
	private final ArchivoClient archivoClient;

	@Autowired
	private EnvioCorreos envioCorreos;

	/**
	 * Método para obtener la lista de docentes registrados en el sistema.
	 * 
	 * @return una lista de {@link DocenteInfoDto} que contiene la información de
	 *         los docentes registrados.
	 * @throws InformationException si no hay docentes registrados.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DocenteInfoDto> listarDocentes() {
		// Obtener la lista de docentes desde el servicio remoto
		List<DocenteResponseDto> listadoDocentes = archivoClient.listarDocentesRes();

		// Verificar si la lista está vacía y lanzar una excepción si no hay docentes
		// registrados
		if (listadoDocentes.isEmpty()) {
			throw new InformationException("No hay docentes registrados");
		}

		// Convertir la lista de DocenteResponseDto a DocenteInfoDto y retornarla
		return listadoDocentes.stream()
				.map(docente -> new DocenteInfoDto(
						docente.getId(),
						docente.getPersona().getNombre(),
						docente.getPersona().getApellido(),
						docente.getPersona().getCorreoElectronico(),
						"Universidad del Cauca"))
				.collect(Collectors.toList());
	}

	/**
	 * Método para obtener la lista de expertos registrados en el sistema.
	 * 
	 * @return una lista de {@link ExpertoInfoDto} que contiene la información de
	 *         los expertos registrados.
	 * @throws InformationException si no hay expertos registrados.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ExpertoInfoDto> listarExpertos() {
		// Obtener la lista de expertos desde el servicio remoto
		List<ExpertoResponseDto> listadoExpertos = archivoClient.listarExpertos();

		// Verificar si la lista está vacía y lanzar una excepción si no hay expertos
		// registrados
		if (listadoExpertos.isEmpty()) {
			throw new InformationException("No hay expertos registrados");
		}

		// Convertir la lista de ExpertoResponseDto a ExpertoInfoDto y retornarla
		return listadoExpertos.stream()
				.map(experto -> new ExpertoInfoDto(
						experto.getId(),
						experto.getPersona().getNombre(),
						experto.getPersona().getApellido(),
						experto.getPersona().getCorreoElectronico(),
						experto.getUniversidadtitexp()))
				.collect(Collectors.toList());
	}

	/**
	 * Método para obtener la información de un docente por su ID.
	 * 
	 * @param id el identificador del docente.
	 * @return un objeto {@link DocenteInfoDto} que contiene la información del
	 *         docente.
	 * @throws ResourceNotFoundException si no se encuentra el docente con el ID
	 *                                   proporcionado.
	 */
	@Override
	@Transactional(readOnly = true)
	public DocenteInfoDto obtenerDocente(Long id) {
		// Obtener el docente por su ID desde el servicio remoto
		DocenteResponseDto docente = archivoClient.obtenerDocentePorId(id);

		// Retornar la información del docente en un objeto DocenteInfoDto
		return new DocenteInfoDto(
				docente.getId(),
				docente.getPersona().getNombre(),
				docente.getPersona().getApellido(),
				docente.getPersona().getCorreoElectronico(),
				"Universidad del Cauca");
	}

	/**
	 * Método para obtener la información de un experto por su ID.
	 * 
	 * @param id el identificador del experto.
	 * @return un objeto {@link ExpertoInfoDto} que contiene la información del
	 *         experto.
	 * @throws ResourceNotFoundException si no se encuentra el experto con el ID
	 *                                   proporcionado.
	 */
	@Override
	@Transactional(readOnly = true)
	public ExpertoInfoDto obtenerExperto(Long id) {
		// Obtener el experto por su ID desde el servicio remoto
		ExpertoResponseDto experto = archivoClient.obtenerExpertoPorId(id);

		// Retornar la información del experto en un objeto ExpertoInfoDto
		return new ExpertoInfoDto(
				experto.getId(),
				experto.getPersona().getNombre(),
				experto.getPersona().getApellido(),
				experto.getPersona().getCorreoElectronico(),
				experto.getUniversidadtitexp());
	}

	/**
	 * Inserta la información de la solicitud de examen de valoración para un
	 * docente.
	 * 
	 * @param idTrabajoGrado        el ID del trabajo de grado asociado.
	 * @param datosExamenValoracion los datos de la solicitud de examen de
	 *                              valoración para el docente.
	 * @param validacion            el resultado de la validación de los datos.
	 * @return un objeto {@link SolicitudExamenValoracionDocenteResponseDto} con la
	 *         información de la solicitud creada.
	 * @throws FieldErrorException       si hay errores de validación en los datos.
	 * @throws InformationException      si ya existe una solicitud asociada o si el
	 *                                   estado del trabajo no permite el registro.
	 * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
	 */
	@Override
	@Transactional
	public SolicitudExamenValoracionDocenteResponseDto insertarInformacionDocente(Long idTrabajoGrado,
			SolicitudExamenValoracionDocenteDto datosExamenValoracion, BindingResult validacion) {

		// Validar los datos de entrada
		if (validacion.hasErrors()) {
			throw new FieldErrorException(validacion);
		}

		// Validar los enlaces proporcionados en la solicitud
		validarLink(datosExamenValoracion.getLinkFormatoA());
		validarLink(datosExamenValoracion.getLinkFormatoD());
		validarLink(datosExamenValoracion.getLinkFormatoE());

		for (AnexoSolicitudExamenValoracionDto anexo : datosExamenValoracion.getAnexos()) {
			validarLink(anexo.getLinkAnexo());
		}

		// Verificar si ya existe una solicitud asociada al trabajo de grado
		if (solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)) {
			throw new InformationException("Ya existe un examen de valoración asociado al trabajo de grado");
		}

		// Obtener el trabajo de grado y validar su estado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (trabajoGrado.getNumeroEstado() != 0) {
			throw new InformationException("No es permitido registrar la información");
		}

		// Validar la existencia de los evaluadores
		archivoClient.obtenerDocentePorId(datosExamenValoracion.getIdEvaluadorInterno());
		archivoClient.obtenerExpertoPorId(datosExamenValoracion.getIdEvaluadorExterno());

		// Guardar los archivos asociados y crear la solicitud
		String directorioArchivos = identificacionArchivo(trabajoGrado);
		SolicitudExamenValoracion nuevaSolicitud = examenValoracionMapper.toEntity(datosExamenValoracion);
		nuevaSolicitud.setTrabajoGrado(trabajoGrado);
		trabajoGrado.setSolicitudExamenValoracion(nuevaSolicitud);
		trabajoGrado.setNumeroEstado(1);
		trabajoGrado.setTitulo(datosExamenValoracion.getTitulo());

		nuevaSolicitud.setLinkFormatoA(
				FilesUtilities.guardarArchivoNew2(directorioArchivos, nuevaSolicitud.getLinkFormatoA()));
		nuevaSolicitud.setLinkFormatoD(
				FilesUtilities.guardarArchivoNew2(directorioArchivos, nuevaSolicitud.getLinkFormatoD()));
		nuevaSolicitud.setLinkFormatoE(
				FilesUtilities.guardarArchivoNew2(directorioArchivos, nuevaSolicitud.getLinkFormatoE()));

		// Procesar los anexos
		if (datosExamenValoracion.getAnexos() != null) {
			List<AnexoSolicitudExamenValoracion> anexosActualizados = new ArrayList<>();
			for (AnexoSolicitudExamenValoracionDto anexoDto : datosExamenValoracion.getAnexos()) {
				String rutaAnexo = FilesUtilities.guardarArchivoNew2(directorioArchivos, anexoDto.getLinkAnexo());
				AnexoSolicitudExamenValoracion anexo = new AnexoSolicitudExamenValoracion();
				anexo.setLinkAnexo(rutaAnexo);
				anexo.setSolicitudExamenValoracion(nuevaSolicitud);
				anexosActualizados.add(anexo);
			}
			nuevaSolicitud.setAnexos(anexosActualizados);
		}

		// Guardar la solicitud en la base de datos
		SolicitudExamenValoracion solicitudGuardada = solicitudExamenValoracionRepository.save(nuevaSolicitud);

		return examenValoracionResponseMapper.toDocenteDto(solicitudGuardada);
	}

	/**
	 * Inserta la información del coordinador en la fase 1 del examen de valoración.
	 * 
	 * @param idTrabajoGrado   el ID del trabajo de grado asociado.
	 * @param datosExamenFase1 los datos de la fase 1 del examen de valoración para
	 *                         el coordinador.
	 * @param validacion       el resultado de la validación de los datos.
	 * @return un objeto {@link SolicitudExamenValoracionResponseFase1Dto} con la
	 *         información de la solicitud actualizada.
	 * @throws FieldErrorException       si hay errores de validación en los datos.
	 * @throws InformationException      si faltan atributos para el registro o si
	 *                                   el estado del trabajo no permite el
	 *                                   registro.
	 * @throws ResourceNotFoundException si el trabajo de grado o la solicitud de
	 *                                   examen de valoración no se encuentran.
	 */
	@Override
	@Transactional
	public SolicitudExamenValoracionResponseFase1Dto insertarInformacionCoordinadorFase1(Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase1Dto datosExamenFase1, BindingResult validacion) {

		// Validar los datos de entrada
		if (validacion.hasErrors()) {
			throw new FieldErrorException(validacion);
		}

		// Validar la consistencia de los atributos de la solicitud
		if (datosExamenFase1.getEnvioEmail() == null
				&& datosExamenFase1.getConceptoCoordinadorDocumentos().equals(ConceptoVerificacion.RECHAZADO)) {
			throw new InformationException("Faltan atributos para el registro");
		}

		if (datosExamenFase1.getEnvioEmail() != null
				&& datosExamenFase1.getConceptoCoordinadorDocumentos().equals(ConceptoVerificacion.ACEPTADO)) {
			throw new InformationException("Envio de atributos no permitido");
		}

		// Obtener el trabajo de grado y validar su estado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (trabajoGrado.getNumeroEstado() != 1) {
			throw new InformationException("No es permitido registrar la información en este estado");
		}

		// Obtener la solicitud de examen de valoración actual
		SolicitudExamenValoracion solicitudActual = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Examen de valoración no encontrado para id: "
						+ trabajoGrado.getSolicitudExamenValoracion().getId()));

		// Actualizar el estado y enviar correos según el concepto del coordinador
		if (datosExamenFase1.getConceptoCoordinadorDocumentos().equals(ConceptoVerificacion.ACEPTADO)) {
			trabajoGrado.setNumeroEstado(3);
		} else {
			ArrayList<String> correos = new ArrayList<>();
			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
			correos.add(estudiante.getCorreoUniversidad());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());
			envioCorreos.enviarCorreosCorrecion(correos, datosExamenFase1.getEnvioEmail().getAsunto(),
					datosExamenFase1.getEnvioEmail().getMensaje());
			trabajoGrado.setNumeroEstado(2);
		}

		// Guardar la actualización en la base de datos
		solicitudActual.setConceptoCoordinadorDocumentos(datosExamenFase1.getConceptoCoordinadorDocumentos());
		SolicitudExamenValoracion solicitudGuardada = solicitudExamenValoracionRepository.save(solicitudActual);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(solicitudGuardada);
	}

	/**
	 * Inserta la información del coordinador en la fase 2 del examen de valoración.
	 * 
	 * @param idTrabajoGrado      el ID del trabajo de grado asociado.
	 * @param examenValoracionDto los datos de la fase 2 del examen de valoración
	 *                            para el coordinador.
	 * @param result              el resultado de la validación de los datos.
	 * @return un objeto
	 *         {@link SolicitudExamenValoracionCoordinadorFase2ResponseDto} con la
	 *         información de la solicitud actualizada.
	 * @throws FieldErrorException       si hay errores de validación en los datos.
	 * @throws InformationException      si faltan atributos para el registro, si el
	 *                                   estado del trabajo no permite el registro,
	 *                                   o si los datos son incorrectos.
	 * @throws ResourceNotFoundException si el trabajo de grado o la solicitud de
	 *                                   examen de valoración no se encuentran.
	 */
	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, BindingResult result) {

		// Validar los datos de entrada
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		// Validar la consistencia de los atributos en función del concepto del comité
		Concepto conceptoComite = examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite();
		boolean tieneAtributosEnvio = examenValoracionDto.getLinkOficioDirigidoEvaluadores() != null ||
				examenValoracionDto.getFechaMaximaEvaluacion() != null ||
				examenValoracionDto.getInformacionEnvioEvaluador() != null;

		if ((conceptoComite.equals(Concepto.NO_APROBADO) && tieneAtributosEnvio) ||
				(conceptoComite.equals(Concepto.APROBADO) && !tieneAtributosEnvio)) {
			throw new InformationException(conceptoComite.equals(Concepto.NO_APROBADO)
					? "Envio de atributos no permitido"
					: "Atributos incorrectos");
		}

		// Validar fechas
		if (examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa() != null
				&& examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa().isAfter(LocalDate.now())) {
			throw new InformationException("La fecha de registro del comité no puede ser mayor a la fecha actual.");
		}

		if (examenValoracionDto.getFechaMaximaEvaluacion() != null
				&& examenValoracionDto.getFechaMaximaEvaluacion().isBefore(LocalDate.now())) {
			throw new InformationException("La fecha máxima de evaluación no puede ser menor a la fecha actual.");
		}

		// Validar los enlaces y documentos si el concepto del comité es APROBADO
		if (conceptoComite.equals(Concepto.APROBADO)) {
			validarLink(examenValoracionDto.getLinkOficioDirigidoEvaluadores());

			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoD());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoE());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64Oficio());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoB());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv1());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv2());

			for (String anexo : examenValoracionDto.getInformacionEnvioEvaluador().getB64Anexos()) {
				ValidationUtils.validarBase64(anexo);
			}
		}

		// Obtener el trabajo de grado y validar su estado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (trabajoGrado.getNumeroEstado() != 3) {
			throw new InformationException("No es permitido registrar la información");
		}

		// Obtener la solicitud de examen de valoración actual
		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Examen de valoración con id " + trabajoGrado.getSolicitudExamenValoracion().getId()
								+ " no encontrado"));

		// Verificar si el concepto ya es APROBADO
		for (RespuestaComiteExamenValoracion respuesta : examenValoracionTmp.getActaFechaRespuestaComite()) {
			if (respuesta.getConceptoComite().equals(Concepto.APROBADO)) {
				throw new InformationException("El concepto ya es APROBADO");
			}
		}

		// Procesar correos y documentos en función del concepto del comité
		ArrayList<String> correos = new ArrayList<>();
		EstudianteResponseDtoAll estudiante = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
		correos.add(estudiante.getCorreoUniversidad());
		correos.add(trabajoGrado.getCorreoElectronicoTutor());

		if (conceptoComite.equals(Concepto.APROBADO)) {
			Map<String, Object> documentosParaEvaluador = examenValoracionDto
					.getInformacionEnvioEvaluador().getDocumentos();

			// Separar documentos para docentes y expertos
			Map<String, Object> documentosParaDocente = documentosParaEvaluador.entrySet().stream()
					.filter(entry -> !entry.getKey().equals("formatoCEv2"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			Map<String, Object> documentosParaExperto = documentosParaEvaluador.entrySet().stream()
					.filter(entry -> !entry.getKey().equals("formatoCEv1"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			// Enviar correos a los evaluadores
			DocenteResponseDto docente = archivoClient.obtenerDocentePorId(examenValoracionTmp.getIdEvaluadorInterno());
			envioCorreos.enviarCorreoEvaluadores(docente.getPersona().getCorreoElectronico(),
					examenValoracionDto.getEnvioEmailDto().getAsunto(),
					examenValoracionDto.getEnvioEmailDto().getMensaje(), documentosParaDocente);

			ExpertoResponseDto experto = archivoClient.obtenerExpertoPorId(examenValoracionTmp.getIdEvaluadorExterno());
			envioCorreos.enviarCorreoEvaluadores(experto.getPersona().getCorreoElectronico(),
					examenValoracionDto.getEnvioEmailDto().getAsunto(),
					examenValoracionDto.getEnvioEmailDto().getMensaje(), documentosParaExperto);

			// Enviar copia de los documentos a los correos asociados al trabajo de grado
			String asunto = "Copia envio documentos a evaluadores";
			String mensaje = "Se adjunta copia de los documentos enviados a los evaluadores "
					+ docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
			envioCorreos.enviarCorreoConAnexos(correos, asunto, mensaje, documentosParaEvaluador);

			// Guardar el oficio dirigido a los evaluadores y actualizar el estado del
			// trabajo
			String rutaArchivo = identificacionArchivo(trabajoGrado);
			examenValoracionDto.setLinkOficioDirigidoEvaluadores(FilesUtilities
					.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkOficioDirigidoEvaluadores()));
			trabajoGrado.setNumeroEstado(5);

			// Registrar los tiempos de evaluación
			insertarInformacionTiempos(examenValoracionDto.getFechaMaximaEvaluacion(), trabajoGrado);
		} else {
			// Enviar correos de corrección si el concepto no es aprobado
			envioCorreos.enviarCorreosCorrecion(correos, examenValoracionDto.getEnvioEmailDto().getAsunto(),
					examenValoracionDto.getEnvioEmailDto().getMensaje());
			trabajoGrado.setNumeroEstado(4);
		}

		// Agregar la información del coordinador a la solicitud
		agregarInformacionCoordinador(examenValoracionTmp, examenValoracionDto);
		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionTmp);

		return examenValoracionResponseMapper.toCoordinadorFase2Dto(examenValoracionRes);
	}

	/**
	 * Inserta o actualiza la información de tiempos pendientes para un trabajo de
	 * grado.
	 * 
	 * @param fechaLimite  La fecha límite para el tiempo pendiente.
	 * @param trabajoGrado El trabajo de grado asociado al tiempo pendiente.
	 */
	private void insertarInformacionTiempos(LocalDate fechaLimite, TrabajoGrado trabajoGrado) {
		// Buscar si ya existen tiempos pendientes para el trabajo de grado
		Optional<TiemposPendientes> optionalTiemposPendientes = tiemposPendientesRepository
				.findByTrabajoGradoId(trabajoGrado.getId());

		// Si existen, se actualizan; si no, se crean nuevos
		TiemposPendientes tiemposPendientes = optionalTiemposPendientes.orElseGet(TiemposPendientes::new);

		// Establecer los valores para el registro
		tiemposPendientes.setTrabajoGrado(trabajoGrado);
		tiemposPendientes.setFechaRegistro(LocalDate.now());
		tiemposPendientes.setEstado(trabajoGrado.getNumeroEstado());
		tiemposPendientes.setFechaLimite(fechaLimite);

		// Guardar o actualizar el registro en el repositorio
		tiemposPendientesRepository.save(tiemposPendientes);
	}

	/**
	 * Agrega la información del coordinador al examen de valoración.
	 * 
	 * @param examenValoracion    El examen de valoración al que se le agregará la
	 *                            información.
	 * @param examenValoracionDto Los datos del examen de valoración proporcionados
	 *                            por el coordinador.
	 */
	private void agregarInformacionCoordinador(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto) {

		// Crear una nueva instancia de RespuestaComiteExamenValoracion con los datos
		// proporcionados
		RespuestaComiteExamenValoracion respuestaComite = RespuestaComiteExamenValoracion.builder()
				.conceptoComite(examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite())
				.numeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa())
				.fechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa())
				.solicitudExamenValoracion(examenValoracion)
				.build();

		// Inicializar la lista si está vacía
		if (examenValoracion.getActaFechaRespuestaComite() == null) {
			examenValoracion.setActaFechaRespuestaComite(new ArrayList<>());
		}

		// Agregar la nueva respuesta del comité a la lista existente
		examenValoracion.getActaFechaRespuestaComite().add(respuestaComite);

		// Actualizar los otros campos del examen de valoración
		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
	}

	/**
	 * Lista la información del docente evaluador en una solicitud de examen de
	 * valoración.
	 * 
	 * @param idTrabajoGrado El ID del trabajo de grado para el cual se listará la
	 *                       información.
	 * @return Un objeto {@link SolicitudExamenValoracionDocenteResponseListDto} con
	 *         los datos del docente evaluador.
	 * @throws ResourceNotFoundException Si no se encuentra la solicitud de examen
	 *                                   de valoración para el trabajo de grado
	 *                                   dado.
	 * @throws InformationException      Si faltan datos registrados en la
	 *                                   solicitud.
	 */
	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionDocenteResponseListDto listarInformacionDocente(Long idTrabajoGrado) {
		// Obtener la solicitud de examen de valoración asociada al trabajo de grado
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Solicitud examen de valoracion con id trabajo de grado " + idTrabajoGrado + " no encontrado"));

		// Verificar que los datos necesarios estén presentes
		if (solicitudExamenValoracion.getLinkFormatoA() == null || solicitudExamenValoracion.getLinkFormatoD() == null
				|| solicitudExamenValoracion.getLinkFormatoE() == null
				|| solicitudExamenValoracion.getActaFechaRespuestaComite() == null
				|| solicitudExamenValoracion.getIdEvaluadorInterno() == null
				|| solicitudExamenValoracion.getIdEvaluadorExterno() == null) {
			throw new InformationException("No se han registrado datos");
		}

		// Obtener la información del evaluador interno (docente)
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(solicitudExamenValoracion.getIdEvaluadorInterno());
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("id", docente.getId().toString());
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener la información del evaluador externo (experto)
		ExpertoResponseDto experto = archivoClient
				.obtenerExpertoPorId(solicitudExamenValoracion.getIdEvaluadorExterno());
		String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
		Map<String, String> evaluadorExternoMap = new HashMap<>();
		evaluadorExternoMap.put("id", experto.getId().toString());
		evaluadorExternoMap.put("nombres", nombre_experto);
		evaluadorExternoMap.put("universidad", experto.getUniversidadtitexp());
		evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

		// Crear el objeto de respuesta con los datos obtenidos
		SolicitudExamenValoracionDocenteResponseListDto docenteResponseDto = new SolicitudExamenValoracionDocenteResponseListDto();
		docenteResponseDto.setId(solicitudExamenValoracion.getId());
		docenteResponseDto.setTitulo(solicitudExamenValoracion.getTitulo());
		docenteResponseDto.setLinkFormatoA(solicitudExamenValoracion.getLinkFormatoA());
		docenteResponseDto.setLinkFormatoD(solicitudExamenValoracion.getLinkFormatoD());
		docenteResponseDto.setLinkFormatoE(solicitudExamenValoracion.getLinkFormatoE());

		// Obtener los anexos de la solicitud
		List<AnexoSolicitudExamenValoracion> anexosSolicitudExamenValoracion = anexosSolicitudExamenValoracionRepository
				.obtenerAnexosPorId(solicitudExamenValoracion.getId());

		ArrayList<String> listaAnexos = new ArrayList<>();
		for (AnexoSolicitudExamenValoracion anexo : anexosSolicitudExamenValoracion) {
			listaAnexos.add(anexo.getLinkAnexo());
		}

		// Configurar la respuesta con los anexos y evaluadores
		docenteResponseDto.setAnexos(listaAnexos);
		docenteResponseDto.setEvaluadorInterno(evaluadorInternoMap);
		docenteResponseDto.setEvaluadorExterno(evaluadorExternoMap);

		return docenteResponseDto;
	}

	/**
	 * Lista la información del coordinador en la fase 1 de una solicitud de examen
	 * de valoración.
	 * 
	 * @param idTrabajoGrado El ID del trabajo de grado para el cual se listará la
	 *                       información del coordinador.
	 * @return Un objeto {@link SolicitudExamenValoracionResponseFase1Dto} con los
	 *         datos de la fase 1 del coordinador.
	 * @throws ResourceNotFoundException Si no se encuentra la solicitud de examen
	 *                                   de valoración para el trabajo de grado
	 *                                   dado.
	 * @throws InformationException      Si no se han registrado los datos del
	 *                                   coordinador.
	 */
	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionResponseFase1Dto listarInformacionCoordinadorFase1(Long idTrabajoGrado) {
		// Obtener la solicitud de examen de valoración asociada al trabajo de grado
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Verificar que los datos del coordinador estén presentes
		if (solicitudExamenValoracion.getConceptoCoordinadorDocumentos() == null) {
			throw new InformationException("No se han registrado datos");
		}

		// Mapear la solicitud a un DTO de respuesta para la fase 1 del coordinador
		SolicitudExamenValoracionResponseFase1Dto solicitudExamenValoracionResponseFase1Dto = examenValoracionResponseMapper
				.toCoordinadorFase1Dto(solicitudExamenValoracion);

		return solicitudExamenValoracionResponseFase1Dto;
	}

	/**
	 * Lista la información de la fase 2 del coordinador en una solicitud de examen
	 * de valoración.
	 * 
	 * @param idTrabajoGrado El ID del trabajo de grado para el cual se listará la
	 *                       información de la fase 2 del coordinador.
	 * @return Un objeto
	 *         {@link SolicitudExamenValoracionCoordinadorFase2ResponseDto} con los
	 *         datos de la fase 2 del coordinador.
	 * @throws ResourceNotFoundException Si no se encuentra la solicitud de examen
	 *                                   de valoración para el trabajo de grado
	 *                                   dado.
	 * @throws InformationException      Si no se han registrado los datos de la
	 *                                   fase 2.
	 */
	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {
		// Obtener la solicitud de examen de valoración asociada al trabajo de grado
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Verificar si los datos de la fase 2 están completos
		boolean actaFechaRespuestaComiteEmpty = solicitudExamenValoracion.getActaFechaRespuestaComite() == null
				|| solicitudExamenValoracion.getActaFechaRespuestaComite().isEmpty();

		if (actaFechaRespuestaComiteEmpty
				&& solicitudExamenValoracion.getLinkOficioDirigidoEvaluadores() == null
				&& solicitudExamenValoracion.getFechaMaximaEvaluacion() == null) {
			throw new InformationException("No se han registrado datos");
		}

		// Mapear la solicitud a un DTO de respuesta para la fase 2 del coordinador
		return examenValoracionResponseMapper.toCoordinadorFase2Dto(solicitudExamenValoracion);
	}

	/**
	 * Actualiza la información del examen de valoración de un docente asociado a un
	 * trabajo de grado.
	 *
	 * @param idTrabajoGrado      El ID del trabajo de grado para el cual se
	 *                            actualizará la información.
	 * @param examenValoracionDto DTO que contiene la nueva información del examen
	 *                            de valoración.
	 * @param result              Resultado de la validación de los datos
	 *                            proporcionados.
	 * @return Un objeto {@link SolicitudExamenValoracionDocenteResponseDto} con los
	 *         datos actualizados del examen de valoración.
	 * @throws FieldErrorException       Si hay errores de validación en los datos
	 *                                   proporcionados.
	 * @throws ResourceNotFoundException Si no se encuentra el trabajo de grado o el
	 *                                   examen de valoración correspondiente.
	 * @throws InformationException      Si no es permitido registrar la información
	 *                                   debido al estado del trabajo de grado.
	 */
	@Override
	@Transactional
	public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long idTrabajoGrado,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, BindingResult result) {

		// Validar errores en los datos proporcionados
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		// Buscar el trabajo de grado en la base de datos
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Validar el estado del trabajo de grado para permitir la actualización
		int numeroEstado = trabajoGrado.getNumeroEstado();
		if (numeroEstado != 1 && numeroEstado != 2 && numeroEstado != 4 && numeroEstado != 16 && numeroEstado != 35) {
			throw new InformationException("No es permitido registrar la información");
		}

		// Validar la existencia de los evaluadores
		archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno());
		archivoClient.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno());

		// Buscar la solicitud de examen de valoración asociada al trabajo de grado
		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Examen de valoracion con id: " + trabajoGrado.getSolicitudExamenValoracion().getId()
								+ " no encontrado"));

		// Identificar la ruta del archivo para almacenar los nuevos documentos
		String rutaArchivo = identificacionArchivo(trabajoGrado);

		// Actualizar y validar los links de los formatos A, D y E
		if (!examenValoracionDto.getLinkFormatoA().equals(examenValoracionTmp.getLinkFormatoA())) {
			validarLink(examenValoracionDto.getLinkFormatoA());
			examenValoracionDto.setLinkFormatoA(
					FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkFormatoA()));
			FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoA());
		}
		if (!examenValoracionDto.getLinkFormatoD().equals(examenValoracionTmp.getLinkFormatoD())) {
			validarLink(examenValoracionDto.getLinkFormatoD());
			examenValoracionDto.setLinkFormatoD(
					FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkFormatoD()));
			FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoD());
		}
		if (!examenValoracionDto.getLinkFormatoE().equals(examenValoracionTmp.getLinkFormatoE())) {
			validarLink(examenValoracionDto.getLinkFormatoE());
			examenValoracionDto.setLinkFormatoE(
					FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkFormatoE()));
			FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoE());
		}

		// Actualizar los anexos si se proporcionaron nuevos
		if (examenValoracionDto.getAnexos() != null) {
			List<AnexoSolicitudExamenValoracion> anexosEntidades = examenValoracionDto.getAnexos().stream()
					.map(anexoSolicitudExamenValoracionMapper::toEntity)
					.collect(Collectors.toList());

			actualizarAnexos(examenValoracionTmp, anexosEntidades, rutaArchivo);
		}

		// Actualizar los valores del examen de valoración docente
		updateExamenValoracionDocenteValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
		SolicitudExamenValoracion responseExamenValoracion = solicitudExamenValoracionRepository
				.save(examenValoracionTmp);

		// Actualizar el estado del trabajo de grado
		trabajoGrado.setNumeroEstado(numeroEstado == 16 ? 17 : 1);

		return examenValoracionResponseMapper.toDocenteDto(responseExamenValoracion);
	}

	/**
	 * Actualiza los valores del examen de valoración con la nueva información
	 * proporcionada.
	 * 
	 * @param examenValoracion    La entidad de examen de valoración que se
	 *                            actualizará.
	 * @param examenValoracionDto El DTO que contiene la nueva información.
	 * @param trabajoGrado        El trabajo de grado asociado al examen de
	 *                            valoración.
	 */
	private void updateExamenValoracionDocenteValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, TrabajoGrado trabajoGrado) {

		// Si el título del trabajo de grado ha cambiado, se eliminan las carpetas
		// asociadas al título anterior
		if (!examenValoracion.getTitulo().equals(examenValoracionDto.getTitulo())) {
			String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracion.getTitulo());
			FilesUtilities.deleteFolderAndItsContents(tituloTrabajoGrado);
		}

		// Restablecer el concepto del coordinador a nulo
		examenValoracion.setConceptoCoordinadorDocumentos(null);

		// Actualizar el título y los evaluadores en el examen de valoración y el
		// trabajo de grado
		trabajoGrado.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setIdEvaluadorExterno(examenValoracionDto.getIdEvaluadorExterno());
		examenValoracion.setIdEvaluadorInterno(examenValoracionDto.getIdEvaluadorInterno());

		// Actualizar los enlaces de los formatos A, D y E
		examenValoracion.setLinkFormatoA(examenValoracionDto.getLinkFormatoA());
		examenValoracion.setLinkFormatoD(examenValoracionDto.getLinkFormatoD());
		examenValoracion.setLinkFormatoE(examenValoracionDto.getLinkFormatoE());
	}

	/**
	 * Actualiza los anexos asociados a un examen de valoración, eliminando los
	 * antiguos y añadiendo los nuevos.
	 * 
	 * @param examenValoracionTmp La entidad del examen de valoración actual.
	 * @param anexosNuevos        La lista de nuevos anexos a asociar.
	 * @param rutaArchivo         La ruta donde se guardarán los nuevos anexos.
	 */
	private void actualizarAnexos(SolicitudExamenValoracion examenValoracionTmp,
			List<AnexoSolicitudExamenValoracion> anexosNuevos, String rutaArchivo) {

		// Obtener la lista de anexos actuales del examen de valoración
		List<AnexoSolicitudExamenValoracion> anexosActuales = examenValoracionTmp.getAnexos();

		// Crear un mapa para facilitar la comparación entre los anexos actuales y los
		// nuevos
		Map<String, AnexoSolicitudExamenValoracion> mapaAnexosActuales = anexosActuales.stream()
				.collect(Collectors.toMap(AnexoSolicitudExamenValoracion::getLinkAnexo, Function.identity()));

		// Lista para almacenar los anexos actualizados
		List<AnexoSolicitudExamenValoracion> anexosActualizados = new ArrayList<>();

		// Validar y actualizar los anexos nuevos
		for (AnexoSolicitudExamenValoracion anexoNuevo : anexosNuevos) {
			AnexoSolicitudExamenValoracion anexoActual = mapaAnexosActuales.get(anexoNuevo.getLinkAnexo());

			if (anexoActual != null) {
				anexosActualizados.add(anexoActual);
			} else {
				validarLink(anexoNuevo.getLinkAnexo());
				String rutaAnexoNueva = FilesUtilities.guardarArchivoNew2(rutaArchivo, anexoNuevo.getLinkAnexo());
				anexoNuevo.setLinkAnexo(rutaAnexoNueva);
				anexoNuevo.setSolicitudExamenValoracion(examenValoracionTmp);
				anexosActualizados.add(anexoNuevo);
			}
		}

		// Eliminar los anexos antiguos que ya no están presentes en la lista de anexos
		// actualizados
		Iterator<AnexoSolicitudExamenValoracion> iterator = anexosActuales.iterator();
		while (iterator.hasNext()) {
			AnexoSolicitudExamenValoracion anexoActual = iterator.next();
			if (!anexosActualizados.contains(anexoActual)) {
				FilesUtilities.deleteFileExample(anexoActual.getLinkAnexo());
				iterator.remove();
			}
		}

		// Actualizar la lista de anexos en la entidad de examen de valoración
		anexosActuales.clear();
		anexosActuales.addAll(anexosActualizados);
	}

	/**
	 * Actualiza la información del coordinador en la Fase 1 del examen de
	 * valoración asociado a un trabajo de grado.
	 *
	 * @param idTrabajoGrado                      El ID del trabajo de grado para el
	 *                                            cual se actualizará la
	 *                                            información.
	 * @param examenValoracionFase1CoordinadorDto DTO que contiene la nueva
	 *                                            información del examen de
	 *                                            valoración Fase 1 del coordinador.
	 * @param result                              Resultado de la validación de los
	 *                                            datos proporcionados.
	 * @return Un objeto {@link SolicitudExamenValoracionResponseFase1Dto} con los
	 *         datos actualizados del examen de valoración.
	 * @throws FieldErrorException       Si hay errores de validación en los datos
	 *                                   proporcionados.
	 * @throws ResourceNotFoundException Si no se encuentra el trabajo de grado o el
	 *                                   examen de valoración correspondiente.
	 * @throws InformationException      Si no es permitido registrar la información
	 *                                   debido al estado del trabajo de grado o si
	 *                                   faltan atributos.
	 */
	@Override
	@Transactional
	public SolicitudExamenValoracionResponseFase1Dto actualizarInformacionCoordinadorFase1(
			Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionFase1CoordinadorDto,
			BindingResult result) {

		// Validar errores en los datos proporcionados
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		// Validar que los atributos requeridos estén presentes dependiendo del concepto
		// del coordinador
		if (examenValoracionFase1CoordinadorDto.getEnvioEmail() == null
				&& examenValoracionFase1CoordinadorDto.getConceptoCoordinadorDocumentos()
						.equals(ConceptoVerificacion.RECHAZADO)) {
			throw new InformationException("Faltan atributos para el registro");
		}

		if (examenValoracionFase1CoordinadorDto.getEnvioEmail() != null
				&& examenValoracionFase1CoordinadorDto.getConceptoCoordinadorDocumentos()
						.equals(ConceptoVerificacion.ACEPTADO)) {
			throw new InformationException("Envio de atributos no permitido");
		}

		// Obtener el concepto del coordinador
		ConceptoVerificacion conceptoCoordinador = examenValoracionFase1CoordinadorDto
				.getConceptoCoordinadorDocumentos();

		// Buscar el trabajo de grado en la base de datos
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Validar el estado del trabajo de grado para permitir la actualización
		int numeroEstado = trabajoGrado.getNumeroEstado();
		if (numeroEstado != 1 && numeroEstado != 2 && numeroEstado != 3) {
			throw new InformationException("No es permitido registrar la información");
		}

		// Buscar la solicitud de examen de valoración asociada al trabajo de grado
		SolicitudExamenValoracion examenValoracionOld = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id: "
						+ trabajoGrado.getSolicitudExamenValoracion().getId() + " no encontrado"));

		// Validar si existen datos previos registrados
		if (examenValoracionOld.getConceptoCoordinadorDocumentos() == null) {
			throw new InformationException("No se han registrado datos");
		}

		// Actualizar el concepto del coordinador si es diferente al anterior
		if (!conceptoCoordinador.equals(examenValoracionOld.getConceptoCoordinadorDocumentos())) {
			ArrayList<String> correos = new ArrayList<>();
			if (conceptoCoordinador.equals(ConceptoVerificacion.RECHAZADO)) {
				// Enviar correos de corrección si el concepto es RECHAZADO
				EstudianteResponseDtoAll estudiante = archivoClient
						.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
				correos.add(estudiante.getCorreoUniversidad());
				correos.add(trabajoGrado.getCorreoElectronicoTutor());
				envioCorreos.enviarCorreosCorrecion(correos,
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getAsunto(),
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getMensaje());
				trabajoGrado.setNumeroEstado(2);
			} else {
				// Actualizar el estado a 3 si el concepto es ACEPTADO
				trabajoGrado.setNumeroEstado(3);
			}
		}

		// Guardar el nuevo concepto del coordinador
		examenValoracionOld.setConceptoCoordinadorDocumentos(conceptoCoordinador);

		// Guardar los cambios en la base de datos
		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionOld);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(examenValoracionRes);
	}

	/**
	 * Actualiza la información del coordinador en la Fase 2 del examen de
	 * valoración asociado a un trabajo de grado.
	 *
	 * @param idTrabajoGrado      El ID del trabajo de grado para el cual se
	 *                            actualizará la información.
	 * @param examenValoracionDto DTO que contiene la nueva información del examen
	 *                            de valoración Fase 2 del coordinador.
	 * @param result              Resultado de la validación de los datos
	 *                            proporcionados.
	 * @return Un objeto
	 *         {@link SolicitudExamenValoracionCoordinadorFase2ResponseDto} con los
	 *         datos actualizados del examen de valoración.
	 * @throws FieldErrorException       Si hay errores de validación en los datos
	 *                                   proporcionados.
	 * @throws ResourceNotFoundException Si no se encuentra el trabajo de grado o el
	 *                                   examen de valoración correspondiente.
	 * @throws InformationException      Si no es permitido registrar la información
	 *                                   debido al estado del trabajo de grado o si
	 *                                   faltan atributos.
	 */
	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorFase2ResponseDto actualizarInformacionCoordinadorFase2(
			Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, BindingResult result) {

		// Validar errores en los datos proporcionados
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		// Obtener el concepto del comité y validar atributos de envío
		Concepto conceptoComite = examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite();
		String linkOficio = examenValoracionDto.getLinkOficioDirigidoEvaluadores();
		LocalDate fechaMaximaEvaluacion = examenValoracionDto.getFechaMaximaEvaluacion();
		Map<String, Object> informacionEnvioEvaluador = examenValoracionDto.getInformacionEnvioEvaluador() != null
				? examenValoracionDto.getInformacionEnvioEvaluador().getDocumentos()
				: null;

		// Validar que no se envíen atributos adicionales si el concepto del comité es
		// NO_APROBADO
		if (conceptoComite.equals(Concepto.NO_APROBADO) && (linkOficio != null || fechaMaximaEvaluacion != null
				|| informacionEnvioEvaluador != null)) {
			throw new InformationException("Envio de atributos no permitido");
		}

		// Validar que todos los atributos requeridos estén presentes si el concepto del
		// comité es APROBADO
		if (conceptoComite.equals(Concepto.APROBADO) && (linkOficio == null || fechaMaximaEvaluacion == null
				|| informacionEnvioEvaluador == null)) {
			throw new InformationException("Atributos incorrectos");
		}

		// Validar que la fecha máxima de evaluación no sea anterior a la fecha actual
		if (examenValoracionDto.getFechaMaximaEvaluacion() != null
				&& examenValoracionDto.getFechaMaximaEvaluacion().isBefore(LocalDate.now())) {
			throw new InformationException("La fecha máxima de evaluación no puede ser menor a la fecha actual.");
		}

		// Validar los archivos en formato Base64 si el concepto del comité es APROBADO
		if (conceptoComite.equals(Concepto.APROBADO)) {
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoD());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoE());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64Oficio());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoB());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv1());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv2());
			for (String anexo : examenValoracionDto.getInformacionEnvioEvaluador().getB64Anexos()) {
				ValidationUtils.validarBase64(anexo);
			}
		}

		// Buscar el trabajo de grado en la base de datos
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		// Validar el estado del trabajo de grado para permitir la actualización
		int numeroEstado = trabajoGrado.getNumeroEstado();
		if (numeroEstado != 3 && numeroEstado != 4 && numeroEstado != 5) {
			throw new InformationException("No es permitido registrar la información");
		}

		// Buscar la solicitud de examen de valoración asociada al trabajo de grado
		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id "
						+ trabajoGrado.getSolicitudExamenValoracion().getId() + " no encontrado"));

		// Validar que existan datos previos registrados en la solicitud de examen de
		// valoración
		boolean actaFechaRespuestaComiteEmpty = examenValoracionTmp.getActaFechaRespuestaComite() == null ||
				examenValoracionTmp.getActaFechaRespuestaComite().isEmpty();
		if (actaFechaRespuestaComiteEmpty && examenValoracionTmp.getLinkOficioDirigidoEvaluadores() == null
				&& examenValoracionTmp.getFechaMaximaEvaluacion() == null) {
			throw new InformationException("No se han registrado datos");
		}

		String rutaArchivo = identificacionArchivo(trabajoGrado);

		// Si el concepto del comité es APROBADO, validar y actualizar el link del
		// oficio dirigido a los evaluadores
		if (conceptoComite.equals(Concepto.APROBADO) && examenValoracionTmp.getLinkOficioDirigidoEvaluadores() != null
				&& !examenValoracionDto.getLinkOficioDirigidoEvaluadores()
						.equals(examenValoracionTmp.getLinkOficioDirigidoEvaluadores())) {
			examenValoracionDto.setLinkOficioDirigidoEvaluadores(
					FilesUtilities.guardarArchivoNew2(rutaArchivo,
							examenValoracionDto.getLinkOficioDirigidoEvaluadores()));
			FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
		} else if (conceptoComite.equals(Concepto.APROBADO)
				&& examenValoracionTmp.getLinkOficioDirigidoEvaluadores() == null) {
			validarLink(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
			examenValoracionDto.setLinkOficioDirigidoEvaluadores(
					FilesUtilities.guardarArchivoNew2(rutaArchivo,
							examenValoracionDto.getLinkOficioDirigidoEvaluadores()));
		}

		// Obtener la última respuesta del comité
		SolicitudExamenValoracion responseExamenValoracion = null;
		List<RespuestaComiteExamenValoracion> respuestaComiteList = solicitudExamenValoracionRepository
				.findRespuestaComiteBySolicitudExamenValoracionId(examenValoracionTmp.getId());
		RespuestaComiteExamenValoracion ultimoRegistro = respuestaComiteList.isEmpty() ? null
				: respuestaComiteList.get(0);

		// Verificar si se ha cambiado el concepto del comité y proceder según sea
		// aprobado o no aprobado
		if (ultimoRegistro != null && ultimoRegistro.getConceptoComite() != conceptoComite) {
			ArrayList<String> correos = new ArrayList<>();
			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
			correos.add(estudiante.getCorreoUniversidad());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());
			if (conceptoComite.equals(Concepto.NO_APROBADO)) {
				if (examenValoracionTmp.getLinkOficioDirigidoEvaluadores() != null) {
					FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
				}
				envioCorreos.enviarCorreosCorrecion(correos,
						examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje());
				trabajoGrado.setNumeroEstado(4);
			} else {
				// Enviar correos y actualizar el estado si el concepto del comité es APROBADO
				Map<String, Object> documentosParaEvaluador = examenValoracionDto
						.getInformacionEnvioEvaluador().getDocumentos();

				Map<String, Object> documentosParaDocente = documentosParaEvaluador.entrySet().stream()
						.filter(entry -> !entry.getKey().equals("formatoCEv2"))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				Map<String, Object> documentosParaExperto = documentosParaEvaluador.entrySet().stream()
						.filter(entry -> !entry.getKey().equals("formatoCEv1"))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				DocenteResponseDto docente = archivoClient
						.obtenerDocentePorId(examenValoracionTmp.getIdEvaluadorInterno());

				envioCorreos.enviarCorreoEvaluadores(docente.getPersona().getCorreoElectronico(),
						examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje(), documentosParaDocente);
				ExpertoResponseDto experto = archivoClient
						.obtenerExpertoPorId(examenValoracionTmp.getIdEvaluadorExterno());

				envioCorreos.enviarCorreoEvaluadores(experto.getPersona().getCorreoElectronico(),
						examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje(), documentosParaExperto);

				String asunto = "Copia envio documentos a evaluador";
				String mensaje = "Se adjunta copia de los documentos enviados a los evaluadores "
						+ docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
				envioCorreos.enviarCorreoConAnexos(correos, asunto, mensaje, documentosParaEvaluador);

				trabajoGrado.setNumeroEstado(5);
				insertarInformacionTiempos(examenValoracionDto.getFechaMaximaEvaluacion(), trabajoGrado);
				tiemposPendientesRepository.findByTrabajoGradoId(idTrabajoGrado)
						.ifPresent(tiemposPendientes -> tiemposPendientes.setFechaLimite(fechaMaximaEvaluacion));
			}
		}

		// Actualizar los valores del examen de valoración coordinador
		updateExamenValoracionCoordinadorValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
		responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		return examenValoracionResponseMapper.toCoordinadorFase2Dto(responseExamenValoracion);
	}

	/**
	 * Actualiza los valores del examen de valoración para el coordinador en la Fase
	 * 2.
	 *
	 * @param examenValoracion    La entidad {@link SolicitudExamenValoracion} que
	 *                            se está actualizando.
	 * @param examenValoracionDto El DTO que contiene la nueva información para la
	 *                            Fase 2 del examen de valoración.
	 * @param trabajoGrado        La entidad {@link TrabajoGrado} asociada al examen
	 *                            de valoración.
	 */
	private void updateExamenValoracionCoordinadorValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, TrabajoGrado trabajoGrado) {

		// Obtener el último registro de la respuesta del comité
		RespuestaComiteExamenValoracion ultimoRegistro = respuestaComiteSolicitudRepository
				.findFirstBySolicitudExamenValoracionIdOrderByIdDesc(examenValoracion.getId());

		// Si existe un registro, actualizarlo con la nueva información del DTO
		if (ultimoRegistro != null) {
			ultimoRegistro.setNumeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa());
			ultimoRegistro.setFechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa());
			ultimoRegistro
					.setConceptoComite(examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite());

			// Guardar los cambios en la base de datos
			respuestaComiteSolicitudRepository.save(ultimoRegistro);
		}

		// Actualizar la fecha máxima de evaluación y el link del oficio dirigido a los
		// evaluadores
		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
	}

	/**
	 * Genera la ruta del archivo basado en la información del estudiante y el
	 * trabajo de grado.
	 *
	 * @param trabajoGrado La entidad {@link TrabajoGrado} asociada al archivo.
	 * @return La ruta del archivo donde se guardarán los documentos asociados al
	 *         trabajo de grado.
	 */
	private String identificacionArchivo(TrabajoGrado trabajoGrado) {
		// Obtener la información del estudiante a partir del cliente de archivos
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		// Definir el proceso asociado a la solicitud de examen de valoración
		String procesoVa = "Solicitud_Examen_Valoracion";

		// Obtener la fecha actual para incluir en la ruta del archivo
		LocalDate fechaActual = LocalDate.now();
		int anio = fechaActual.getYear();
		int mes = fechaActual.getMonthValue();

		// Construir la información del estudiante con su identificación, nombre y
		// apellido
		Long identificacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
		String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
		String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
		String informacionEstudiante = identificacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

		// Generar la ruta completa de la carpeta donde se almacenarán los documentos
		String rutaCarpeta = anio + "/" + mes + "/" + informacionEstudiante + "/" + procesoVa;

		return rutaCarpeta;
	}

	/**
	 * Valida el formato y el contenido en Base64 del link proporcionado.
	 *
	 * @param link El link que se va a validar.
	 * @throws ValidationException Si el formato del link o el contenido Base64 no
	 *                             son válidos.
	 */
	private void validarLink(String link) {
		// Validar el formato del link
		ValidationUtils.validarFormatoLink(link);

		// Extraer y validar el contenido en Base64 del link
		String base64 = link.substring(link.indexOf('-') + 1);
		ValidationUtils.validarBase64(base64);
	}

}
