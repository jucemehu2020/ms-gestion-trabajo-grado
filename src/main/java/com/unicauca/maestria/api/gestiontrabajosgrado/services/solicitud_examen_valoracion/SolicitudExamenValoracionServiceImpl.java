package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.*;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolicitudExamenValoracionServiceImpl implements SolicitudExamenValoracionService {

	private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
	private final ExamenValoracionMapper examenValoracionMapper;
	private final TrabajoGradoRepository trabajoGradoRepository;
	private final InformacionUnicaSolicitudExamenValoracion informacionUnicaSolicitudExamenValoracion;
	private final ArchivoClient archivoClient;

	@Override
	@Transactional(readOnly = true)
	public DocenteResponseDto listarDocentes() {
		DocenteResponseDto estadoTmp = archivoClient.listarDocentesRes();
		return estadoTmp;
	}

	@Override
	@Transactional
	public ExamenValoracionDto crear(ExamenValoracionDto examenValoracionDto, BindingResult result) {
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

		// Mapear DTO a entidad
		SolicitudExamenValoracion examenValoracion = examenValoracionMapper.toEntity(examenValoracionDto);

		// Establecer la relación uno a uno
		examenValoracion.setIdTrabajoGrado(trabajoGrado);
		trabajoGrado.setExamenValoracion(examenValoracion);

		// Guardar la entidad ExamenValoracion
		examenValoracion.setLinkFormatoA(FilesUtilities.guardarArchivo(examenValoracion.getLinkFormatoA()));
		examenValoracion.setLinkFormatoD(FilesUtilities.guardarArchivo(examenValoracion.getLinkFormatoD()));
		examenValoracion.setLinkFormatoE(FilesUtilities.guardarArchivo(examenValoracion.getLinkFormatoE()));
		examenValoracion.setLinkOficioDirigidoEvaluadores(
				FilesUtilities.guardarArchivo(examenValoracion.getLinkOficioDirigidoEvaluadores()));

		SolicitudExamenValoracion examenValoracionRes = solicitudExamenValoracionRepository.save(examenValoracion);

		return examenValoracionMapper.toDto(examenValoracionRes);
	}

	private CamposUnicosSolicitudExamenValoracionDto obtenerCamposUnicos(ExamenValoracionDto docenteSaveDto) {
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
		return "Campo único, ya existe un docente con la información: " + nombreCampo + ": " + valorCampo;
	}

	@Override
	@Transactional(readOnly = true)
	public ExamenValoracionDto buscarPorId(Long idTrabajoGrado) {
		return solicitudExamenValoracionRepository.findById(idTrabajoGrado).map(examenValoracionMapper::toDto)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Examen de valoracion con id: " + idTrabajoGrado + " no encontrado"));
	}

	@Override
	public ExamenValoracionDto actualizar(Long id, ExamenValoracionDto examenValoracionDto, BindingResult result) {
		SolicitudExamenValoracion examenValoracionTmp = solicitudExamenValoracionRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Examen de valoracion con id: " + id + " no encontrado"));
		SolicitudExamenValoracion responseExamenValoracion = null;
		if (examenValoracionTmp != null) {
			if (examenValoracionDto.getLinkFormatoA().compareTo(examenValoracionTmp.getLinkFormatoA()) != 0) {
				examenValoracionDto
						.setLinkFormatoA(FilesUtilities.guardarArchivo(examenValoracionDto.getLinkFormatoA()));
				FilesUtilities.deleteFileExample(examenValoracionTmp.getLinkFormatoA());
			}
			updateExamenValoracionValues(examenValoracionTmp, examenValoracionDto);
			responseExamenValoracion = solicitudExamenValoracionRepository.save(examenValoracionTmp);
		}
		return examenValoracionMapper.toDto(responseExamenValoracion);
	}

	// Funciones privadas
	private void updateExamenValoracionValues(SolicitudExamenValoracion examenValoracion,
			ExamenValoracionDto examenValoracionDto) {
		examenValoracion.setTitulo(examenValoracionDto.getTitulo());
		examenValoracion.setEvaluadorExterno(examenValoracionDto.getEvaluadorExterno());
		examenValoracion.setEvaluadorInterno(examenValoracion.getEvaluadorInterno());
		examenValoracion.setActaAprobacionExamen(examenValoracionDto.getActaNombramientoEvaluadores());
		examenValoracion.setFechaActa(examenValoracionDto.getFechaActa());
		examenValoracion.setFechaMaximaEvaluacion(examenValoracionDto.getFechaMaximaEvaluacion());
	}

}
