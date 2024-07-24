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

	// Repository
	private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
	private final RespuestaComiteSolicitudRepository respuestaComiteSolicitudRepository;
	private final AnexosSolicitudExamenValoracionRepository anexosSolicitudExamenValoracionRepository;
	private final TrabajoGradoRepository trabajoGradoRepository;
	private final TiemposPendientesRepository tiemposPendientesRepository;
	// Mapper
	private final SolicitudExamenValoracionMapper examenValoracionMapper;
	private final SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
	private final AnexoSolicitudExamenValoracionMapper anexoSolicitudExamenValoracionMapper;
	// Other
	private final ArchivoClient archivoClient;

	@Autowired
	private EnvioCorreos envioCorreos;

	@Override
	@Transactional(readOnly = true)
	public List<DocenteInfoDto> listarDocentes() {
		List<DocenteResponseDto> listadoDocentes = archivoClient.listarDocentesRes();
		if (listadoDocentes.isEmpty()) {
			throw new InformationException("No hay docentes registrados");
		}

		return listadoDocentes.stream()
				.map(docente -> new DocenteInfoDto(
						docente.getId(),
						docente.getPersona().getNombre(),
						docente.getPersona().getApellido(),
						docente.getPersona().getCorreoElectronico(),
						"Universidad del Cauca"))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ExpertoInfoDto> listarExpertos() {
		List<ExpertoResponseDto> listadoExpertos = archivoClient.listarExpertos();
		if (listadoExpertos.isEmpty()) {
			throw new InformationException("No hay expertos registrados");
		}
		List<ExpertoInfoDto> expertos = listadoExpertos.stream()
				.map(experto -> new ExpertoInfoDto(
						experto.getId(),
						experto.getPersona().getNombre(),
						experto.getPersona().getApellido(),
						experto.getPersona().getCorreoElectronico(),
						experto.getUniversidadtitexp()))
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
				"Universidad del Cauca");
	}

	@Override
	@Transactional(readOnly = true)
	public ExpertoInfoDto obtenerExperto(Long id) {
		ExpertoResponseDto experto = archivoClient.obtenerExpertoPorId(id);
		return new ExpertoInfoDto(
				experto.getId(),
				experto.getPersona().getNombre(),
				experto.getPersona().getApellido(),
				experto.getPersona().getCorreoElectronico(),
				experto.getUniversidadtitexp());
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionDocenteResponseDto insertarInformacionDocente(Long idTrabajoGrado,
			SolicitudExamenValoracionDocenteDto datosExamenValoracion,
			BindingResult validacion) {

		if (validacion.hasErrors()) {
			throw new FieldErrorException(validacion);
		}

		validarLink(datosExamenValoracion.getLinkFormatoA());
		validarLink(datosExamenValoracion.getLinkFormatoD());
		validarLink(datosExamenValoracion.getLinkFormatoE());

		for (AnexoSolicitudExamenValoracionDto anexo : datosExamenValoracion.getAnexos()) {
			validarLink(anexo.getLinkAnexo());
		}

		if (solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)) {
			throw new InformationException("Ya existe un examen de valoracion asociado al trabajo de grado");
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (trabajoGrado.getNumeroEstado() != 0) {
			throw new InformationException("No es permitido registrar la informacion");
		}

		archivoClient.obtenerDocentePorId(datosExamenValoracion.getIdEvaluadorInterno());
		archivoClient.obtenerExpertoPorId(datosExamenValoracion.getIdEvaluadorExterno());

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

		List<AnexoSolicitudExamenValoracion> anexosActualizados = new ArrayList<>();
		for (AnexoSolicitudExamenValoracionDto anexoDto : datosExamenValoracion.getAnexos()) {
			String rutaAnexo = FilesUtilities.guardarArchivoNew2(directorioArchivos, anexoDto.getLinkAnexo());
			AnexoSolicitudExamenValoracion anexo = new AnexoSolicitudExamenValoracion();
			anexo.setLinkAnexo(rutaAnexo);
			anexo.setSolicitudExamenValoracion(nuevaSolicitud);
			anexosActualizados.add(anexo);
		}
		nuevaSolicitud.setAnexos(anexosActualizados);

		SolicitudExamenValoracion solicitudGuardada = solicitudExamenValoracionRepository.save(nuevaSolicitud);

		return examenValoracionResponseMapper.toDocenteDto(solicitudGuardada);
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionResponseFase1Dto insertarInformacionCoordinadorFase1(Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase1Dto datosExamenFase1,
			BindingResult validacion) {

		if (validacion.hasErrors()) {
			throw new FieldErrorException(validacion);
		}

		if (datosExamenFase1.getEnvioEmail() == null
				&& datosExamenFase1.getConceptoCoordinadorDocumentos().equals(ConceptoVerificacion.RECHAZADO)) {
			throw new InformationException("Faltan atributos para el registro");
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (trabajoGrado.getNumeroEstado() != 1) {
			throw new InformationException("No es permitido registrar la informacion en este estado");
		}

		SolicitudExamenValoracion solicitudActual = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Examen de valoracion no encontrado para id: "
								+ trabajoGrado.getSolicitudExamenValoracion().getId()));

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

		solicitudActual.setConceptoCoordinadorDocumentos(datosExamenFase1.getConceptoCoordinadorDocumentos());

		SolicitudExamenValoracion solicitudGuardada = solicitudExamenValoracionRepository.save(solicitudActual);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(solicitudGuardada);
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		Concepto conceptoComite = examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite();
		boolean tieneAtributosEnvio = examenValoracionDto.getLinkOficioDirigidoEvaluadores() != null &&
				examenValoracionDto.getFechaMaximaEvaluacion() != null &&
				examenValoracionDto.getInformacionEnvioEvaluador() != null;

		if ((conceptoComite.equals(Concepto.NO_APROBADO) && tieneAtributosEnvio) ||
				(conceptoComite.equals(Concepto.APROBADO) && !tieneAtributosEnvio)) {
			throw new InformationException(conceptoComite.equals(Concepto.NO_APROBADO)
					? "Envio de atributos no permitido"
					: "Atributos incorrectos");
		}

		if (examenValoracionDto.getFechaMaximaEvaluacion() != null
				&& examenValoracionDto.getFechaMaximaEvaluacion().isBefore(LocalDate.now())) {
			throw new InformationException("La fecha máxima de evaluación no puede ser menor a la fecha actual.");
		}

		if (examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals(Concepto.APROBADO)) {
			validarLink(examenValoracionDto.getLinkOficioDirigidoEvaluadores());

			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoD());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoE());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64Oficio());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoBEv1());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv1());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoBEv2());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv2());
			for (String anexo : examenValoracionDto.getInformacionEnvioEvaluador().getB64Anexos()) {
				ValidationUtils.validarBase64(anexo);
			}
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (trabajoGrado.getNumeroEstado() != 3) {
			throw new InformationException("No es permitido registrar la informacion");
		}

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Examen de valoracion con id " + trabajoGrado.getSolicitudExamenValoracion().getId()
								+ " no encontrado"));

		for (RespuestaComiteExamenValoracion respuesta : examenValoracionTmp.getActaFechaRespuestaComite()) {
			if (respuesta.getConceptoComite().equals(Concepto.APROBADO)) {
				throw new InformationException("El concepto ya es APROBADO");
			}
		}

		ArrayList<String> correos = new ArrayList<>();

		if (conceptoComite.equals(Concepto.APROBADO)) {
			Map<String, Object> documentosParaEvaluador = examenValoracionDto
					.getInformacionEnvioEvaluador().getDocumentos();

			// Filtrar documentos para el docente
			Map<String, Object> documentosParaDocente = documentosParaEvaluador.entrySet().stream()
					.filter(entry -> !entry.getKey().equals("formatoBEv2") && !entry.getKey().equals("formatoCEv2"))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			// Filtrar documentos para el experto
			Map<String, Object> documentosParaExperto = documentosParaEvaluador.entrySet().stream()
					.filter(entry -> !entry.getKey().equals("formatoBEv1") && !entry.getKey().equals("formatoCEv1"))
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

			// envioCorreos.enviarCorreoConAnexos(correos,
			// examenValoracionDto.getEnvioEmailDto().getAsunto(),
			// examenValoracionDto.getEnvioEmailDto().getMensaje(),
			// documentosParaEvaluador);

			String rutaArchivo = identificacionArchivo(trabajoGrado);

			examenValoracionDto.setLinkOficioDirigidoEvaluadores(FilesUtilities
					.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkOficioDirigidoEvaluadores()));

			trabajoGrado.setNumeroEstado(5);

			insertarInformacionTiempos(examenValoracionDto.getFechaMaximaEvaluacion(), trabajoGrado);
		} else {
			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
			correos.add(estudiante.getCorreoUniversidad());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());
			envioCorreos.enviarCorreosCorrecion(correos, examenValoracionDto.getEnvioEmailDto().getAsunto(),
					examenValoracionDto.getEnvioEmailDto().getMensaje());
			trabajoGrado.setNumeroEstado(4);
		}

		agregarInformacionCoordinador(examenValoracionTmp, examenValoracionDto);
		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionTmp);

		return examenValoracionResponseMapper.toCoordinadorFase2Dto(examenValoracionRes);
	}

	private void insertarInformacionTiempos(LocalDate fechaLimite, TrabajoGrado trabajoGrado) {
		Optional<TiemposPendientes> optionalTiemposPendientes = tiemposPendientesRepository
				.findByTrabajoGradoId(trabajoGrado.getId());

		TiemposPendientes tiemposPendientes = new TiemposPendientes();
		if (optionalTiemposPendientes.isPresent()) {
			// Si el registro ya existe, lo actualizamos
			tiemposPendientes = optionalTiemposPendientes.get();
		} else {
			// Si no existe, creamos uno nuevo
			tiemposPendientes = new TiemposPendientes();
			tiemposPendientes.setTrabajoGrado(trabajoGrado);
		}

		tiemposPendientes.setFechaRegistro(LocalDate.now());
		tiemposPendientes.setEstado(trabajoGrado.getNumeroEstado());
		tiemposPendientes.setFechaLimite(fechaLimite);

		tiemposPendientesRepository.save(tiemposPendientes);
	}

	private void agregarInformacionCoordinador(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto) {

		RespuestaComiteExamenValoracion respuestaComite = RespuestaComiteExamenValoracion.builder()
				.conceptoComite(examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite())
				.numeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa())
				.fechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa())
				.solicitudExamenValoracion(examenValoracion)
				.build();

		if (examenValoracion.getActaFechaRespuestaComite() == null) {
			examenValoracion.setActaFechaRespuestaComite(new ArrayList<>());
		}

		examenValoracion.getActaFechaRespuestaComite().add(respuestaComite);

		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
		examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
	}

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionDocenteResponseListDto listarInformacionDocente(Long idTrabajoGrado) {
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(idTrabajoGrado).orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (solicitudExamenValoracion.getLinkFormatoA() == null || solicitudExamenValoracion.getLinkFormatoD() == null
				|| solicitudExamenValoracion.getLinkFormatoE() == null
				|| solicitudExamenValoracion.getActaFechaRespuestaComite() == null
				|| solicitudExamenValoracion.getIdEvaluadorInterno() == null
				|| solicitudExamenValoracion.getIdEvaluadorExterno() == null) {
			throw new InformationException("No se han registrado datos");
		}

		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(solicitudExamenValoracion.getIdEvaluadorInterno());
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("id", docente.getId().toString());
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		ExpertoResponseDto experto = archivoClient
				.obtenerExpertoPorId(solicitudExamenValoracion.getIdEvaluadorExterno());
		String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
		Map<String, String> evaluadorExternoMap = new HashMap<>();
		evaluadorExternoMap.put("id", experto.getId().toString());
		evaluadorExternoMap.put("nombres", nombre_experto);
		evaluadorExternoMap.put("universidad", experto.getUniversidadtitexp());
		evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

		SolicitudExamenValoracionDocenteResponseListDto docenteResponseDto = new SolicitudExamenValoracionDocenteResponseListDto();
		docenteResponseDto.setId(solicitudExamenValoracion.getId());
		docenteResponseDto.setTitulo(solicitudExamenValoracion.getTitulo());
		docenteResponseDto.setLinkFormatoA(solicitudExamenValoracion.getLinkFormatoA());
		docenteResponseDto.setLinkFormatoD(solicitudExamenValoracion.getLinkFormatoD());
		docenteResponseDto.setLinkFormatoE(solicitudExamenValoracion.getLinkFormatoE());

		List<AnexoSolicitudExamenValoracion> anexosSolicitudExamenValoracion = anexosSolicitudExamenValoracionRepository
				.obtenerAnexosPorId(solicitudExamenValoracion.getId());

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
	public SolicitudExamenValoracionResponseFase1Dto listarInformacionCoordinadorFase1(Long idTrabajoGrado) {
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(idTrabajoGrado).orElseThrow(
						() -> new ResourceNotFoundException(
								"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		if (solicitudExamenValoracion.getConceptoCoordinadorDocumentos() == null) {
			throw new InformationException("No se han registrado datos");
		}

		SolicitudExamenValoracionResponseFase1Dto solicitudExamenValoracionResponseFase1Dto = examenValoracionResponseMapper
				.toCoordinadorFase1Dto(solicitudExamenValoracion);

		return solicitudExamenValoracionResponseFase1Dto;
	}

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findByTrabajoGradoId(idTrabajoGrado).orElseThrow(
						() -> new ResourceNotFoundException(
								"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		boolean actaFechaRespuestaComiteEmpty = solicitudExamenValoracion
				.getActaFechaRespuestaComite() == null ||
				solicitudExamenValoracion.getActaFechaRespuestaComite().isEmpty();
		if (actaFechaRespuestaComiteEmpty && solicitudExamenValoracion.getLinkOficioDirigidoEvaluadores() == null
				&& solicitudExamenValoracion.getFechaMaximaEvaluacion() == null) {
			throw new InformationException("No se han registrado datos");
		}

		return examenValoracionResponseMapper.toCoordinadorFase2Dto(solicitudExamenValoracion);
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long idTrabajoGrado,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		int numeroEstado = trabajoGrado.getNumeroEstado();
		if (numeroEstado != 1 && numeroEstado != 2 && numeroEstado != 4 && numeroEstado != 16 && numeroEstado != 35) {
			throw new InformationException("No es permitido registrar la informacion");
		}

		archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno());
		archivoClient.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno());

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Examen de valoracion con id: " + trabajoGrado.getSolicitudExamenValoracion().getId()
								+ " no encontrado"));

		String rutaArchivo = identificacionArchivo(trabajoGrado);

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

		List<AnexoSolicitudExamenValoracion> anexosEntidades = examenValoracionDto.getAnexos().stream()
				.map(anexoSolicitudExamenValoracionMapper::toEntity)
				.collect(Collectors.toList());

		actualizarAnexos(examenValoracionTmp, anexosEntidades, rutaArchivo);

		updateExamenValoracionDocenteValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
		SolicitudExamenValoracion responseExamenValoracion = solicitudExamenValoracionRepository
				.save(examenValoracionTmp);

		trabajoGrado.setNumeroEstado(numeroEstado == 16 ? 17 : 1);

		return examenValoracionResponseMapper.toDocenteDto(responseExamenValoracion);
	}

	private void updateExamenValoracionDocenteValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, TrabajoGrado trabajoGrado) {

		if (!examenValoracion.getTitulo().equals(examenValoracionDto.getTitulo())) {
			String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracion.getTitulo());
			FilesUtilities.deleteFolderAndItsContents(tituloTrabajoGrado);
		}

		examenValoracion.setConceptoCoordinadorDocumentos(null);

		trabajoGrado.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setIdEvaluadorExterno(examenValoracionDto.getIdEvaluadorExterno());
		examenValoracion.setIdEvaluadorInterno(examenValoracionDto.getIdEvaluadorInterno());

		examenValoracion.setLinkFormatoA(examenValoracionDto.getLinkFormatoA());
		examenValoracion.setLinkFormatoD(examenValoracionDto.getLinkFormatoD());
		examenValoracion.setLinkFormatoE(examenValoracionDto.getLinkFormatoE());
	}

	private void actualizarAnexos(SolicitudExamenValoracion examenValoracionTmp,
			List<AnexoSolicitudExamenValoracion> anexosNuevos, String rutaArchivo) {
		List<AnexoSolicitudExamenValoracion> anexosActuales = examenValoracionTmp.getAnexos();

		// Mapa para buscar anexos actuales por enlace
		Map<String, AnexoSolicitudExamenValoracion> mapaAnexosActuales = anexosActuales.stream()
				.collect(Collectors.toMap(AnexoSolicitudExamenValoracion::getLinkAnexo, Function.identity()));

		// Lista de anexos actualizados
		List<AnexoSolicitudExamenValoracion> anexosActualizados = new ArrayList<>();

		for (AnexoSolicitudExamenValoracion anexoNuevo : anexosNuevos) {
			AnexoSolicitudExamenValoracion anexoActual = mapaAnexosActuales.get(anexoNuevo.getLinkAnexo());

			if (anexoActual != null) {
				// El anexo no ha cambiado, mantener el actual
				anexosActualizados.add(anexoActual);
			} else {
				// El anexo ha cambiado o es nuevo, validar y agregar
				validarLink(anexoNuevo.getLinkAnexo());
				String rutaAnexoNueva = FilesUtilities.guardarArchivoNew2(rutaArchivo, anexoNuevo.getLinkAnexo());
				anexoNuevo.setLinkAnexo(rutaAnexoNueva);
				anexoNuevo.setSolicitudExamenValoracion(examenValoracionTmp);
				anexosActualizados.add(anexoNuevo);
			}
		}

		// Eliminar archivos de los anexos que ya no están en la lista nueva y eliminar
		// el anexo de la entidad
		Iterator<AnexoSolicitudExamenValoracion> iterator = anexosActuales.iterator();
		while (iterator.hasNext()) {
			AnexoSolicitudExamenValoracion anexoActual = iterator.next();
			if (!anexosActualizados.contains(anexoActual)) {
				FilesUtilities.deleteFileExample(anexoActual.getLinkAnexo());
				iterator.remove();
			}
		}

		// Agregar los nuevos anexos a la colección existente
		anexosActuales.clear();
		anexosActuales.addAll(anexosActualizados);
	}

	@Override
	public SolicitudExamenValoracionResponseFase1Dto actualizarInformacionCoordinadorFase1(
			Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionFase1CoordinadorDto,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		if (examenValoracionFase1CoordinadorDto.getEnvioEmail() == null
				&& examenValoracionFase1CoordinadorDto.getConceptoCoordinadorDocumentos()
						.equals(ConceptoVerificacion.ACEPTADO)) {
			throw new InformationException("Faltan atributos para el registro");
		}

		ConceptoVerificacion conceptoCoordinador = examenValoracionFase1CoordinadorDto
				.getConceptoCoordinadorDocumentos();

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		int numeroEstado = trabajoGrado.getNumeroEstado();
		if (numeroEstado != 1 && numeroEstado != 2 && numeroEstado != 3) {
			throw new InformationException("No es permitido registrar la informacion");
		}

		SolicitudExamenValoracion examenValoracionOld = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id: "
						+ trabajoGrado.getSolicitudExamenValoracion().getId() + " no encontrado"));

		if (examenValoracionOld.getConceptoCoordinadorDocumentos() == null) {
			throw new InformationException("No se han registrado datos");
		}

		if (!conceptoCoordinador.equals(examenValoracionOld.getConceptoCoordinadorDocumentos())) {
			ArrayList<String> correos = new ArrayList<>();
			if (conceptoCoordinador.equals(ConceptoVerificacion.RECHAZADO)) {
				EstudianteResponseDtoAll estudiante = archivoClient
						.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
				correos.add(estudiante.getCorreoUniversidad());
				correos.add(trabajoGrado.getCorreoElectronicoTutor());
				envioCorreos.enviarCorreosCorrecion(correos,
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getAsunto(),
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getMensaje());
				trabajoGrado.setNumeroEstado(2);
			} else {
				trabajoGrado.setNumeroEstado(3);
			}
		}

		examenValoracionOld.setConceptoCoordinadorDocumentos(conceptoCoordinador);

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionOld);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(examenValoracionRes);
	}

	@Override
	public SolicitudExamenValoracionCoordinadorFase2ResponseDto actualizarInformacionCoordinadorFase2(
			Long idTrabajoGrado,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		Concepto conceptoComite = examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite();
		String linkOficio = examenValoracionDto.getLinkOficioDirigidoEvaluadores();
		LocalDate fechaMaximaEvaluacion = examenValoracionDto.getFechaMaximaEvaluacion();
		Map<String, Object> informacionEnvioEvaluador = examenValoracionDto.getInformacionEnvioEvaluador() != null
				? examenValoracionDto.getInformacionEnvioEvaluador().getDocumentos()
				: null;

		if (conceptoComite.equals(Concepto.NO_APROBADO) && linkOficio != null && fechaMaximaEvaluacion != null
				&& informacionEnvioEvaluador != null) {
			throw new InformationException("Envio de atributos no permitido");
		}

		if (conceptoComite.equals(Concepto.APROBADO) && linkOficio == null && fechaMaximaEvaluacion == null
				&& informacionEnvioEvaluador == null) {
			throw new InformationException("Atributos incorrectos");
		}

		if (examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals(Concepto.APROBADO)) {
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoD());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoE());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64Oficio());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoBEv1());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv1());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoBEv2());
			ValidationUtils.validarBase64(examenValoracionDto.getInformacionEnvioEvaluador().getB64FormatoCEv2());
			for (String anexo : examenValoracionDto.getInformacionEnvioEvaluador().getB64Anexos()) {
				ValidationUtils.validarBase64(anexo);
			}
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

		int numeroEstado = trabajoGrado.getNumeroEstado();
		if (numeroEstado != 3 && numeroEstado != 4 && numeroEstado != 5) {
			throw new InformationException("No es permitido registrar la informacion");
		}

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(trabajoGrado.getSolicitudExamenValoracion().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Examen de valoracion con id "
						+ trabajoGrado.getSolicitudExamenValoracion().getId() + " no encontrado"));

		boolean actaFechaRespuestaComiteEmpty = examenValoracionTmp.getActaFechaRespuestaComite() == null ||
				examenValoracionTmp.getActaFechaRespuestaComite().isEmpty();
		if (actaFechaRespuestaComiteEmpty && examenValoracionTmp.getLinkOficioDirigidoEvaluadores() == null
				&& examenValoracionTmp.getFechaMaximaEvaluacion() == null) {
			throw new InformationException("No se han registrado datos");
		}

		String rutaArchivo = identificacionArchivo(trabajoGrado);

		SolicitudExamenValoracion responseExamenValoracion = null;
		List<RespuestaComiteExamenValoracion> respuestaComiteList = solicitudExamenValoracionRepository
				.findRespuestaComiteBySolicitudExamenValoracionId(examenValoracionTmp.getId());
		RespuestaComiteExamenValoracion ultimoRegistro = respuestaComiteList.isEmpty() ? null
				: respuestaComiteList.get(0);

		if (ultimoRegistro != null && ultimoRegistro.getConceptoComite() != conceptoComite) {
			ArrayList<String> correos = new ArrayList<>();
			if (conceptoComite.equals(Concepto.NO_APROBADO)) {
				EstudianteResponseDtoAll estudiante = archivoClient
						.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
				correos.add(estudiante.getCorreoUniversidad());
				correos.add(trabajoGrado.getCorreoElectronicoTutor());
				envioCorreos.enviarCorreosCorrecion(correos,
						examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje());
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
				trabajoGrado.setNumeroEstado(4);
			} else {
				validarLink(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
				linkOficio = FilesUtilities.guardarArchivoNew2(rutaArchivo, linkOficio);
				examenValoracionDto.setLinkOficioDirigidoEvaluadores(linkOficio);
				DocenteResponseDto docente = archivoClient
						.obtenerDocentePorId(examenValoracionTmp.getIdEvaluadorInterno());
				correos.add(docente.getPersona().getCorreoElectronico());
				ExpertoResponseDto experto = archivoClient
						.obtenerExpertoPorId(examenValoracionTmp.getIdEvaluadorExterno());
				correos.add(experto.getPersona().getCorreoElectronico());
				envioCorreos.enviarCorreoConAnexos(correos, examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje(), informacionEnvioEvaluador);
				trabajoGrado.setNumeroEstado(5);
				insertarInformacionTiempos(examenValoracionDto.getFechaMaximaEvaluacion(), trabajoGrado);
			}
		} else if (examenValoracionTmp != null
				&& !linkOficio.equals(examenValoracionTmp.getLinkOficioDirigidoEvaluadores())) {
			validarLink(examenValoracionDto.getLinkOficioDirigidoEvaluadores());
			linkOficio = FilesUtilities.guardarArchivoNew2(rutaArchivo, linkOficio);
			examenValoracionDto.setLinkOficioDirigidoEvaluadores(linkOficio);
			FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
		}

		tiemposPendientesRepository.findByTrabajoGradoId(idTrabajoGrado)
				.ifPresent(tiemposPendientes -> tiemposPendientes.setFechaLimite(fechaMaximaEvaluacion));

		updateExamenValoracionCoordinadorValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
		responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		return examenValoracionResponseMapper.toCoordinadorFase2Dto(responseExamenValoracion);
	}

	private void updateExamenValoracionCoordinadorValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, TrabajoGrado trabajoGrado) {

		List<RespuestaComiteExamenValoracion> respuestaComiteList = solicitudExamenValoracionRepository
				.findRespuestaComiteBySolicitudExamenValoracionId(examenValoracion.getId());
		RespuestaComiteExamenValoracion ultimoRegistro = respuestaComiteList.isEmpty() ? null
				: respuestaComiteList.get(0);

		if (ultimoRegistro != null) {
			ultimoRegistro.setNumeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa());
			ultimoRegistro.setFechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa());

			RespuestaComiteExamenValoracion actaFechaRespuestaComite = respuestaComiteSolicitudRepository
					.findFirstByOrderByIdDesc();

			actaFechaRespuestaComite
					.setConceptoComite(examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite());
			actaFechaRespuestaComite
					.setNumeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa());
			actaFechaRespuestaComite
					.setFechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa());

			examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
			examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());

			respuestaComiteSolicitudRepository.save(actaFechaRespuestaComite);
		}
	}

	private String identificacionArchivo(TrabajoGrado trabajoGrado) {
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		String procesoVa = "Solicitud_Examen_Valoracion";

		LocalDate fechaActual = LocalDate.now();
		int anio = fechaActual.getYear();
		int mes = fechaActual.getMonthValue();

		Long identificacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
		String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
		String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
		String informacionEstudiante = identificacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;
		String rutaCarpeta = anio + "/" + mes + "/" + informacionEstudiante + "/" + procesoVa;

		return rutaCarpeta;
	}

	private void validarLink(String link) {
		ValidationUtils.validarFormatoLink(link);
		String base64 = link.substring(link.indexOf('-') + 1);
		ValidationUtils.validarBase64(base64);
	}

}
