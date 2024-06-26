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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComiteExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.DatosFormatoBResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.ObtenerDocumentosParaEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.*;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSolicitudRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
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
	// Mapper
	private final SolicitudExamenValoracionMapper examenValoracionMapper;
	private final SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
	// Other
	private final ArchivoClient archivoClient;
	private final ArchivoClientExpertos archivoClientExpertos;

	@Autowired
	private EnvioCorreos envioCorreos;

	@Override
	@Transactional(readOnly = true)
	public List<DocenteInfoDto> listarDocentes() {
		List<DocenteResponseDto> docenteResponse = archivoClient.listarDocentesRes();
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
	public SolicitudExamenValoracionDocenteResponseDto insertarInformacionDocente(Long idTrabajoGrado,
			SolicitudExamenValoracionDocenteDto examenValoracionDto,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		Boolean existeExamenValoracion = solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado);

		if (existeExamenValoracion) {
			throw new InformationException("Ya existe un examen de valoracion asociado al trabajo de grado");
		}

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + idTrabajoGrado + " No encontrado"));

		if (trabajoGrado.getNumeroEstado() == 0 || trabajoGrado.getNumeroEstado() == 2
				|| trabajoGrado.getNumeroEstado() == 4) {

			String rutaArchivo = identificacionArchivo(trabajoGrado);

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
					.setLinkFormatoA(
							FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracion.getLinkFormatoA()));
			examenValoracion
					.setLinkFormatoD(
							FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracion.getLinkFormatoD()));
			examenValoracion
					.setLinkFormatoE(
							FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracion.getLinkFormatoE()));

			// Crear y agregar anexos a la entidad principal
			List<AnexoSolicitudExamenValoracion> updatedLinkAnexos = new ArrayList<>();
			for (AnexoSolicitudExamenValoracion linkAnexoDto : examenValoracionDto.getAnexos()) {
				String updatedAnexo = FilesUtilities.guardarArchivoNew2(rutaArchivo, linkAnexoDto.getLinkAnexo());
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
	public SolicitudExamenValoracionResponseFase1Dto insertarInformacionCoordinadorFase1(Long idExamenValoracion,
			SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionDto,
			BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		ArrayList<String> correos = new ArrayList<>();

		SolicitudExamenValoracion examenValoracion = solicitudExamenValoracionRepository
				.findById(idExamenValoracion).orElseThrow(
						() -> new ResourceNotFoundException(
								"Examen de valoracion con id: " + idExamenValoracion + "no encontrado"));

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracion.getIdTrabajoGrado().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + examenValoracion.getIdTrabajoGrado().getId() + " No encontrado"));

		if (examenValoracionDto.getConceptoCoordinadorDocumentos()) {
			correos.add(Constants.correoComite);
			Map<String, Object> documentosEnvioComiteDto = examenValoracionDto.getDocumentosEnvioComiteDto()
					.getDocumentos();
			envioCorreos.enviarCorreoConAnexos(correos, examenValoracionDto.getEnvioEmail().getAsunto(),
					examenValoracionDto.getEnvioEmail().getMensaje(), documentosEnvioComiteDto);
			trabajoGrado.setNumeroEstado(3);
		} else {
			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
			correos.add(estudiante.getPersona().getCorreoElectronico());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());
			envioCorreos.enviarCorreosCorrecion(correos, examenValoracionDto.getEnvioEmail().getAsunto(),
					examenValoracionDto.getEnvioEmail().getMensaje());
			trabajoGrado.setNumeroEstado(2);
		}

		examenValoracion.setConceptoCoordinadorDocumentos(examenValoracionDto.getConceptoCoordinadorDocumentos());

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository
				.save(examenValoracion);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(examenValoracionRes);
	}

	@Override
	@Transactional
	public SolicitudExamenValoracionCoordinadorResponseDto insertarInformacionCoordinadorFase2(Long idExamenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		ArrayList<String> correos = new ArrayList<>();

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository
				.findById(idExamenValoracion).orElseThrow(
						() -> new ResourceNotFoundException(
								"Examen de valoracion con id: " + idExamenValoracion + "no encontrado"));

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionTmp.getIdTrabajoGrado().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"TrabajoGrado con id: " + examenValoracionTmp.getIdTrabajoGrado().getId() + " No encontrado"));

		if (examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()) {
			DocenteResponseDto docente = archivoClient
					.obtenerDocentePorId(examenValoracionTmp.getIdEvaluadorInterno());
			correos.add(docente.getPersona().getCorreoElectronico());
			ExpertoResponseDto experto = archivoClientExpertos
					.obtenerExpertoPorId(examenValoracionTmp.getIdEvaluadorExterno());
			correos.add(experto.getPersona().getCorreoElectronico());
			Map<String, Object> documentosParaEvaluador = examenValoracionDto
					.getInformacionEnvioEvaluador().getDocumentos();
			envioCorreos.enviarCorreoConAnexos(correos, examenValoracionDto.getEnvioEmailDto().getAsunto(),
					examenValoracionDto.getEnvioEmailDto().getMensaje(), documentosParaEvaluador);

			String rutaArchivo = identificacionArchivo(trabajoGrado);

			// Falta poner el tiempo aqui

			examenValoracionDto
					.setLinkOficioDirigidoEvaluadores(
							FilesUtilities
									.guardarArchivoNew2(rutaArchivo,
											examenValoracionDto.getLinkOficioDirigidoEvaluadores()));

			trabajoGrado.setNumeroEstado(5);
		} else {
			EstudianteResponseDtoAll estudiante = archivoClient
					.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
			correos.add(estudiante.getPersona().getCorreoElectronico());
			correos.add(trabajoGrado.getCorreoElectronicoTutor());
			envioCorreos.enviarCorreosCorrecion(correos, examenValoracionDto.getEnvioEmailDto().getAsunto(),
					examenValoracionDto.getEnvioEmailDto().getMensaje());
			trabajoGrado.setNumeroEstado(4);
		}

		agregarInformacionCoordinador(examenValoracionTmp, examenValoracionDto);

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository
				.save(examenValoracionTmp);

		return examenValoracionResponseMapper.toCoordinadorFase2Dto(examenValoracionRes);
	}

	// Funciones privadas
	private void agregarInformacionCoordinador(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto) {

		// Crear una nueva instancia de RespuestaComite
		RespuestaComiteExamenValoracion respuestaComite = RespuestaComiteExamenValoracion.builder()
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

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionDocenteResponseListDto listarInformacionDocente(Long idTrabajoGrado) {
		Optional<SolicitudExamenValoracion> entityOptional = solicitudExamenValoracionRepository
				.findByIdTrabajoGradoId(idTrabajoGrado);

		// SolicitudExamenValoracion entity = entityOptional.get();
		SolicitudExamenValoracionResponseDto responseDto = examenValoracionResponseMapper.toDto(entityOptional.get());

		// Obtener y construir información del evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(entityOptional.get().getIdEvaluadorInterno());
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener y construir información del evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(entityOptional.get().getIdEvaluadorExterno());
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
	public SolicitudExamenValoracionResponseFase1Dto listarInformacionCoordinadorFase1(Long idExamenValoracion) {
		Optional<SolicitudExamenValoracion> entityOptional = solicitudExamenValoracionRepository
				.findById(idExamenValoracion);

		SolicitudExamenValoracionResponseFase1Dto responseDto = examenValoracionResponseMapper
				.toCoordinadorFase1Dto(entityOptional.get());

		return responseDto;
	}

	@Override
	@Transactional(readOnly = true)
	public SolicitudExamenValoracionCoordinadorResponseDto listarInformacionCoordinadorFase2(Long idExamenValoracion) {
		SolicitudExamenValoracion solicitudExamenValoracion = solicitudExamenValoracionRepository
				.findById(idExamenValoracion).orElseThrow(
						() -> new ResourceNotFoundException(
								"Examen de valoracion con id: " + idExamenValoracion + " no encontrado"));

		return examenValoracionResponseMapper.toCoordinadorFase2Dto(solicitudExamenValoracion);
	}

	@Override
	public SolicitudExamenValoracionDocenteResponseDto actualizarInformacionDocente(Long id,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, BindingResult result) {

		if (result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));

		// Busca el trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionTmp.getIdTrabajoGrado().getId())
				.orElseThrow(
						() -> new ResourceNotFoundException("Trabajo de grado con id: "
								+ examenValoracionTmp.getIdTrabajoGrado().getId() + " no encontrado"));

		// EstudianteResponseDtoAll informacionEstudiantes = archivoClient
		// .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		String rutaArchivo = identificacionArchivo(trabajoGrado);

		SolicitudExamenValoracion responseExamenValoracion = null;
		if (examenValoracionTmp != null) {
			if (!examenValoracionDto.getLinkFormatoA().equals(examenValoracionTmp.getLinkFormatoA())) {
				examenValoracionDto
						.setLinkFormatoA(
								FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkFormatoA()));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoA());
			}
			if (!examenValoracionDto.getLinkFormatoD().equals(examenValoracionTmp.getLinkFormatoD())) {
				examenValoracionDto
						.setLinkFormatoD(
								FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkFormatoD()));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoD());
			}
			if (!examenValoracionDto.getLinkFormatoE().equals(examenValoracionTmp.getLinkFormatoE())) {
				examenValoracionDto
						.setLinkFormatoE(
								FilesUtilities.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkFormatoE()));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoE());
			}
			// Actualizar anexos
			actualizarAnexos(examenValoracionTmp, examenValoracionDto.getAnexos(), rutaArchivo);

			updateExamenValoracionDocenteValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
			responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		}
		return examenValoracionResponseMapper.toDocenteDto(responseExamenValoracion);
	}

	// Funciones privadas
	private void updateExamenValoracionDocenteValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionDocenteDto examenValoracionDto, TrabajoGrado trabajoGrado) {

		if (!examenValoracion.getTitulo().equals(examenValoracionDto.getTitulo())) {
			String tituloTrabajoGrado = ConvertString.obtenerIniciales(examenValoracion.getTitulo());
			FilesUtilities.deleteFolderAndItsContents(tituloTrabajoGrado);
		}

		trabajoGrado.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setIdEvaluadorExterno(examenValoracionDto.getIdEvaluadorExterno());
		examenValoracion.setIdEvaluadorInterno(examenValoracionDto.getIdEvaluadorInterno());
		// Update archivos
		examenValoracion.setLinkFormatoA(examenValoracionDto.getLinkFormatoA());
		examenValoracion.setLinkFormatoD(examenValoracionDto.getLinkFormatoD());
		examenValoracion.setLinkFormatoE(examenValoracionDto.getLinkFormatoE());
	}

	private void actualizarAnexos(SolicitudExamenValoracion examenValoracionTmp,
			List<AnexoSolicitudExamenValoracion> anexosDto, String rutaArchivo) {

		List<AnexoSolicitudExamenValoracion> anexosExistentes = examenValoracionTmp.getAnexos();
		Map<Long, AnexoSolicitudExamenValoracion> anexosExistentesMap = anexosExistentes.stream()
				.collect(Collectors.toMap(AnexoSolicitudExamenValoracion::getId, Function.identity()));

		// Eliminar los anexos que ya no están en el DTO
		for (Iterator<AnexoSolicitudExamenValoracion> it = anexosExistentes.iterator(); it.hasNext();) {
			AnexoSolicitudExamenValoracion anexoExistente = it.next();
			if (anexosDto.stream().noneMatch(
					anexoDto -> anexoDto.getId() != null && anexoDto.getId().equals(anexoExistente.getId()))) {
				FilesUtilities.deleteFileExample(anexoExistente.getLinkAnexo());
				it.remove();
			}
		}

		// Agregar o actualizar los anexos del DTO
		for (AnexoSolicitudExamenValoracion anexoDto : anexosDto) {
			if (anexoDto.getId() == null) {
				// Nuevo anexo
				anexoDto.setLinkAnexo(FilesUtilities.guardarArchivoNew2(rutaArchivo, anexoDto.getLinkAnexo()));
				anexoDto.setSolicitudExamenValoracion(examenValoracionTmp);
				anexosExistentes.add(anexoDto);
			} else {
				// Anexo existente, actualizar si es necesario
				AnexoSolicitudExamenValoracion anexoExistente = anexosExistentesMap.get(anexoDto.getId());
				if (anexoExistente != null && !anexoExistente.getLinkAnexo().equals(anexoDto.getLinkAnexo())) {
					FilesUtilities.deleteFileExample(anexoExistente.getLinkAnexo());
					anexoExistente
							.setLinkAnexo(FilesUtilities.guardarArchivoNew2(rutaArchivo, anexoDto.getLinkAnexo()));
				}
			}
		}
	}

	@Override
	public SolicitudExamenValoracionResponseFase1Dto actualizarInformacionCoordinadoFase1(Long id,
			SolicitudExamenValoracionCoordinadorFase1Dto examenValoracionFase1CoordinadorDto, BindingResult result) {

		SolicitudExamenValoracion examenValoracionOld = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + "no encontrado"));

		// Busca el trabajo de grado
		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionOld.getIdTrabajoGrado().getId())
				.orElseThrow(
						() -> new ResourceNotFoundException("Trabajo de grado con id: "
								+ examenValoracionOld.getIdTrabajoGrado().getId() + " no encontrado"));

		if (examenValoracionFase1CoordinadorDto.getConceptoCoordinadorDocumentos() != examenValoracionOld
				.getConceptoCoordinadorDocumentos()) {
			ArrayList<String> correos = new ArrayList<>();
			// Si pasa de aprobado a no aprobado
			if (!examenValoracionFase1CoordinadorDto.getConceptoCoordinadorDocumentos()) {
				EstudianteResponseDtoAll estudiante = archivoClient
						.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
				correos.add(estudiante.getPersona().getCorreoElectronico());
				correos.add(trabajoGrado.getCorreoElectronicoTutor());
				envioCorreos.enviarCorreosCorrecion(correos,
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getAsunto(),
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getMensaje());
				trabajoGrado.setNumeroEstado(2);
			} else {
				// Revisar si se envia correo ofreciendo excusas
				correos.add(Constants.correoComite);
				Map<String, Object> documentosEnvioComiteDto = examenValoracionFase1CoordinadorDto
						.getDocumentosEnvioComiteDto()
						.getDocumentos();
				envioCorreos.enviarCorreoConAnexos(correos,
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getAsunto(),
						examenValoracionFase1CoordinadorDto.getEnvioEmail().getMensaje(), documentosEnvioComiteDto);
				trabajoGrado.setNumeroEstado(3);
			}
		}

		examenValoracionOld.setConceptoCoordinadorDocumentos(
				examenValoracionFase1CoordinadorDto.getConceptoCoordinadorDocumentos());

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracionOld);

		return examenValoracionResponseMapper.toCoordinadorFase1Dto(examenValoracionRes);
	}

	@Override
	public SolicitudExamenValoracionCoordinadorResponseDto actualizarInformacionCoordinadorFase2(Long id,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, BindingResult result) {

		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));

		TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(examenValoracionTmp.getIdTrabajoGrado().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id: "
						+ examenValoracionTmp.getIdTrabajoGrado().getId() + " no encontrado"));

		String rutaArchivo = identificacionArchivo(trabajoGrado);

		SolicitudExamenValoracion responseExamenValoracion = null;
		List<RespuestaComiteExamenValoracion> respuestaComiteList = solicitudExamenValoracionRepository
				.findRespuestaComiteBySolicitudExamenValoracionId(examenValoracionTmp.getIdExamenValoracion());
		RespuestaComiteExamenValoracion ultimoRegistro = respuestaComiteList.isEmpty() ? null
				: respuestaComiteList.get(0);

		if (ultimoRegistro != null
				&& ultimoRegistro.getConceptoComite() != examenValoracionDto.getActaFechaRespuestaComite().get(0)
						.getConceptoComite()) {
			ArrayList<String> correos = new ArrayList<>();
			// Si pasa de aprobado a no aprobado
			if (!examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()) {
				EstudianteResponseDtoAll estudiante = archivoClient
						.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
				correos.add(estudiante.getPersona().getCorreoElectronico());
				correos.add(trabajoGrado.getCorreoElectronicoTutor());
				envioCorreos.enviarCorreosCorrecion(correos,
						examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje());
				trabajoGrado.setNumeroEstado(4);
			} else {
				examenValoracionDto.setLinkOficioDirigidoEvaluadores(FilesUtilities.guardarArchivoNew2(rutaArchivo,
						examenValoracionDto.getLinkOficioDirigidoEvaluadores()));
				DocenteResponseDto docente = archivoClient
						.obtenerDocentePorId(examenValoracionTmp.getIdEvaluadorInterno());
				correos.add(docente.getPersona().getCorreoElectronico());
				ExpertoResponseDto experto = archivoClientExpertos
						.obtenerExpertoPorId(examenValoracionTmp.getIdEvaluadorExterno());
				correos.add(experto.getPersona().getCorreoElectronico());
				Map<String, Object> documentosParaEvaluador = examenValoracionDto
						.getInformacionEnvioEvaluador().getDocumentos();
				envioCorreos.enviarCorreoConAnexos(correos, examenValoracionDto.getEnvioEmailDto().getAsunto(),
						examenValoracionDto.getEnvioEmailDto().getMensaje(), documentosParaEvaluador);
				trabajoGrado.setNumeroEstado(5);
			}
		} else {
			if (examenValoracionTmp != null) {
				if (examenValoracionDto.getLinkOficioDirigidoEvaluadores()
						.compareTo(examenValoracionTmp.getLinkOficioDirigidoEvaluadores()) != 0) {
					examenValoracionDto.setLinkOficioDirigidoEvaluadores(FilesUtilities
							.guardarArchivoNew2(rutaArchivo, examenValoracionDto.getLinkOficioDirigidoEvaluadores()));
					FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkOficioDirigidoEvaluadores());
				}
			}
		}
		updateExamenValoracionCoordinadorValues(examenValoracionTmp, examenValoracionDto, trabajoGrado);
		responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		return examenValoracionResponseMapper.toCoordinadorFase2Dto(responseExamenValoracion);
	}

	private void updateExamenValoracionCoordinadorValues(SolicitudExamenValoracion examenValoracion,
			SolicitudExamenValoracionCoordinadorFase2Dto examenValoracionDto, TrabajoGrado trabajoGrado) {

		List<RespuestaComiteExamenValoracion> respuestaComiteList = solicitudExamenValoracionRepository
				.findRespuestaComiteBySolicitudExamenValoracionId(examenValoracion.getIdExamenValoracion());
		RespuestaComiteExamenValoracion ultimoRegistro = respuestaComiteList.isEmpty() ? null
				: respuestaComiteList.get(0);

		if (ultimoRegistro != null) {
			// Actualizar los valores de ultimoRegistro
			ultimoRegistro.setNumeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa());
			ultimoRegistro.setFechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa());

			// Actualizar la lista actaFechaRespuestaComite de examenValoracion
			RespuestaComiteExamenValoracion actaFechaRespuestaComite = respuestaComiteSolicitudRepository
					.findFirstByOrderByIdAnexoExamenValoracionDesc();

			actaFechaRespuestaComite
					.setConceptoComite(examenValoracionDto.getActaFechaRespuestaComite().get(0).getConceptoComite());
			actaFechaRespuestaComite
					.setNumeroActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getNumeroActa());
			actaFechaRespuestaComite
					.setFechaActa(examenValoracionDto.getActaFechaRespuestaComite().get(0).getFechaActa());

			// Actualizar otros campos de examenValoracion
			examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
			examenValoracion.setLinkOficioDirigidoEvaluadores(examenValoracionDto.getLinkOficioDirigidoEvaluadores());

			respuestaComiteSolicitudRepository.save(actaFechaRespuestaComite);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public String descargarArchivo(RutaArchivoDto rutaArchivo) {
		return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
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
					"Solicitud examen valoracion con idTrabajoGrado: " + idTrabajoGrado + " No encontrada");
		}

		// SolicitudExamenValoracion entity = examenValoracion.get();
		// SolicitudExamenValoracionResponseDto responseDto =
		// examenValoracionResponseMapper.toDto(entity);

		// Obtener información evaluador interno
		DocenteResponseDto docente = archivoClient
				.obtenerDocentePorId(examenValoracion.get().getIdEvaluadorInterno());
		String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
		Map<String, String> evaluadorInternoMap = new HashMap<>();
		evaluadorInternoMap.put("nombres", nombre_docente);
		evaluadorInternoMap.put("universidad", "Universidad del Cauca");
		evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

		// Obtener información evaluador externo
		ExpertoResponseDto experto = archivoClientExpertos
				.obtenerExpertoPorId(examenValoracion.get().getIdEvaluadorExterno());
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

	private String identificacionArchivo(TrabajoGrado trabajoGrado) {
		EstudianteResponseDtoAll informacionEstudiantes = archivoClient
				.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

		String procesoVa = "Solicitud_Examen_Valoracion";

		// Obtener la fecha actual
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

}
