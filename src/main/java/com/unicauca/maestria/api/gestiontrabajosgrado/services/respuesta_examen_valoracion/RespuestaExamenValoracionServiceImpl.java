package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.AnexoRespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.ObtenerDocumentosParaEnvioCorreoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Fase2.ExamenValoracionCanceladoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoRespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosRespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaExamenValoracionServiceImpl implements RespuestaExamenValoracionService {

        private final RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
        private final ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
        private final AnexosRespuestaExamenValoracionRepository anexosRespuestaExamenValoracionRepository;
        private final RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
        private final RespuestaExamenValoracionResponseMapper respuestaExamenValoracionResponseMapper;
        private final AnexoRespuestaExamenValoracionMapper anexoRespuestaExamenValoracionMapper;
        private final ExamenValoracionCanceladoMapper examenValoracionCanceladoMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;

        // Extras
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;

        @Autowired
        private EnvioCorreos envioCorreos;

        @Override
        @Transactional
        public RespuestaExamenValoracionResponseDto crear(Long idTrabajoGrado,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (respuestaExamenValoracionDto.getRespuestaExamenValoracion().equals(ConceptosVarios.APROBADO)
                                && respuestaExamenValoracionDto.getFechaMaximaEntrega() != null) {
                        throw new InformationException("Atributo FECHA MAXIMA no permitido");
                }

                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                if (numeroNoAprobado >= 4) {
                        throw new InformationException("Ya no es permitido registrar mas respuestas");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 5 && trabajoGrado.getNumeroEstado() != 6
                                && trabajoGrado.getNumeroEstado() != 8 && trabajoGrado.getNumeroEstado() != 9
                                && trabajoGrado.getNumeroEstado() != 10 && trabajoGrado.getNumeroEstado() != 11) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO)) {
                        archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.EXTERNO)) {
                        archivoClientExpertos
                                        .obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                List<RespuestaExamenValoracion> listaExamenes = respuestaExamenValoracionRepository
                                .findByIdTrabajoGrado(idTrabajoGrado);

                for (int i = 0; i < listaExamenes.size(); i++) {
                        if (respuestaExamenValoracionDto.getIdEvaluador() == listaExamenes.get(i).getIdEvaluador() &&
                                        respuestaExamenValoracionDto.getTipoEvaluador()
                                                        .equals(listaExamenes.get(i).getTipoEvaluador())
                                        && listaExamenes.get(i).getRespuestaExamenValoracion()
                                                        .equals(ConceptosVarios.APROBADO)) {
                                throw new InformationException(
                                                "El evaluador previamente dio su concepto como APROBADO, no es permitido que realice nuevos registros");
                        }
                }

                RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionMapper
                                .toEntity(respuestaExamenValoracionDto);

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                int numEstado = validarEstado(idTrabajoGrado,
                                rtaExamenValoracion.getRespuestaExamenValoracion());
                trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad ExamenValoracion
                rtaExamenValoracion
                                .setLinkFormatoB(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                rtaExamenValoracion.getLinkFormatoB()));
                rtaExamenValoracion
                                .setLinkFormatoC(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                rtaExamenValoracion.getLinkFormatoC()));
                rtaExamenValoracion.setLinkObservaciones(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                rtaExamenValoracion.getLinkObservaciones()));

                List<AnexoRespuestaExamenValoracion> updatedLinkAnexos = new ArrayList<>();
                for (AnexoRespuestaExamenValoracionDto linkAnexoDto : respuestaExamenValoracionDto
                                .getAnexos()) {
                        String updatedAnexo = FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                        linkAnexoDto.getLinkAnexo());
                        AnexoRespuestaExamenValoracion anexo = new AnexoRespuestaExamenValoracion();
                        anexo.setLinkAnexo(updatedAnexo);
                        anexo.setRespuestaExamenValoracion(rtaExamenValoracion);
                        updatedLinkAnexos.add(anexo);
                }
                rtaExamenValoracion.setAnexos(updatedLinkAnexos);

                // Se asigna al trabajo de grado
                rtaExamenValoracion.setIdTrabajoGrado(trabajoGrado);

                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                rtaExamenValoracion.setPermitidoExamen(true);

                RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository
                                .save(rtaExamenValoracion);

                if (numeroNoAprobado == 3) {
                        rtaExamenValoracion.setPermitidoExamen(false);
                }

                return respuestaExamenValoracionResponseMapper.toDto(examenValoracionRes);
        }

        @Override
        @Transactional
        public ExamenValoracionCanceladoDto insertarInformacionCancelado(
                        Long idTrabajoGrado,
                        ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result) {

                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                if (numeroNoAprobado == 4) {

                        if (result.hasErrors()) {
                                throw new FieldErrorException(result);
                        }

                        TrabajoGrado trabajoGrado = trabajoGradoRepository
                                        .findById(idTrabajoGrado)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "TrabajoGrado con id: "
                                                                        + idTrabajoGrado
                                                                        + " No encontrado"));

                        trabajoGrado.setNumeroEstado(12);

                        ExamenValoracionCancelado examenValoracionCancelado = examenValoracionCanceladoRepository
                                        .save(examenValoracionCanceladoMapper.toEntity(examenValoracionCanceladoDto));

                        return examenValoracionCanceladoMapper.toDto(examenValoracionCancelado);
                } else {
                        throw new InformationException("No es permitido registrar esta informacion");
                }
        }

        @Override
        @Transactional
        public Long validarNumeroNoAprobado(Long idTrabajoGrado) {

                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                return numeroNoAprobado;
        }

        private boolean enviarCorreoTutorEstudiante(RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        TrabajoGrado trabajoGrado) {

                ArrayList<String> correos = new ArrayList<>();

                correos.add(trabajoGrado.getCorreoElectronicoTutor());

                EstudianteResponseDtoAll estudiante = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                correos.add(estudiante.getPersona().getCorreoElectronico());

                Map<String, Object> documentosParaEvaluador = new HashMap<>();

                String[] formatoB = respuestaExamenValoracionDto.getLinkFormatoB().split("-");
                documentosParaEvaluador.put("formatoB", formatoB[1]);
                String[] formatoC = respuestaExamenValoracionDto.getLinkFormatoC().split("-");
                documentosParaEvaluador.put("formatoC", formatoC[1]);
                String[] obervaciones = respuestaExamenValoracionDto.getLinkObservaciones().split("-");
                documentosParaEvaluador.put("observaciones", obervaciones[1]);
                for (int i = 0; i < respuestaExamenValoracionDto.getAnexos().size(); i++) {
                        String[] anexo = respuestaExamenValoracionDto.getAnexos().get(i).getLinkAnexo()
                                        .split("-");
                        documentosParaEvaluador.put("anexo-" + (i + 1), anexo[1]);
                }

                envioCorreos.enviarCorreoConAnexos(correos,
                                respuestaExamenValoracionDto.getEnvioEmail().getAsunto(),
                                respuestaExamenValoracionDto.getEnvioEmail().getMensaje(),
                                documentosParaEvaluador);

                return true;
        }

        @Override
        @Transactional(readOnly = true)
        public Map<String, List<RespuestaExamenValoracionResponseDto>> buscarPorId(Long idTrabajoGrado) {

                respuestaExamenValoracionRepository.findByIdTrabajoGradoId(idTrabajoGrado).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                List<RespuestaExamenValoracion> respuestas = respuestaExamenValoracionRepository
                                .findByIdTrabajoGrado(idTrabajoGrado);

                if (respuestas.isEmpty()) {
                        throw new InformationException("No se han registrado datos");
                }

                return respuestas.stream()
                                .map(respuestaExamenValoracionResponseMapper::toDto)
                                .collect(Collectors.groupingBy(
                                                respuesta -> respuesta.getTipoEvaluador() == TipoEvaluador.INTERNO
                                                                ? "evaluador_interno"
                                                                : "evaluador_externo"));
        }

        @Override
        public RespuestaExamenValoracionResponseDto actualizar(Long idRespuestaExamen,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                if (respuestaExamenValoracionDto.getRespuestaExamenValoracion().equals(ConceptosVarios.APROBADO)
                                && respuestaExamenValoracionDto.getFechaMaximaEntrega() != null) {
                        throw new InformationException("Atributo FECHA MAXIMA no permitido");
                }
                Boolean cambioDocumento = false;

                RespuestaExamenValoracion respuestaExamenValoracionTmp = respuestaExamenValoracionRepository
                                .findById(idRespuestaExamen).orElseThrow(() -> new ResourceNotFoundException(
                                                "Respuesta examen de valoracion con id " + idRespuestaExamen
                                                                + " no encontrado"));

                if (respuestaExamenValoracionTmp.getIdEvaluador() != respuestaExamenValoracionDto.getIdEvaluador()
                                && respuestaExamenValoracionTmp.getTipoEvaluador() != respuestaExamenValoracionDto
                                                .getTipoEvaluador()) {
                        throw new InformationException("Los datos del evaluador no corresponden");
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(respuestaExamenValoracionTmp.getIdTrabajoGrado().getId()).orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Trabajo de grado con id: "
                                                                                + respuestaExamenValoracionTmp
                                                                                                .getIdTrabajoGrado()
                                                                                                .getId()
                                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 6 && trabajoGrado.getNumeroEstado() != 7
                                && trabajoGrado.getNumeroEstado() != 8 && trabajoGrado.getNumeroEstado() != 9
                                && trabajoGrado.getNumeroEstado() != 10 && trabajoGrado.getNumeroEstado() != 11) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO)) {
                        archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.EXTERNO)) {
                        archivoClientExpertos
                                        .obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                if (respuestaExamenValoracionDto.getLinkFormatoB()
                                .compareTo(respuestaExamenValoracionTmp.getLinkFormatoB()) != 0) {
                        respuestaExamenValoracionDto.setLinkFormatoB(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        respuestaExamenValoracionDto.getLinkFormatoB()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoB());
                        cambioDocumento = true;
                }
                if (respuestaExamenValoracionDto.getLinkFormatoC()
                                .compareTo(respuestaExamenValoracionTmp.getLinkFormatoC()) != 0) {
                        respuestaExamenValoracionDto.setLinkFormatoC(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        respuestaExamenValoracionDto.getLinkFormatoC()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoC());
                        cambioDocumento = true;
                }
                if (respuestaExamenValoracionDto.getLinkObservaciones()
                                .compareTo(respuestaExamenValoracionTmp.getLinkObservaciones()) != 0) {
                        respuestaExamenValoracionDto.setLinkObservaciones(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        respuestaExamenValoracionDto.getLinkObservaciones()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkObservaciones());
                        cambioDocumento = true;
                }

                // Convierte DTOs a Entidades
                List<AnexoRespuestaExamenValoracion> anexosEntidades = respuestaExamenValoracionDto.getAnexos()
                                .stream()
                                .map(anexoDto -> anexoRespuestaExamenValoracionMapper.toEntity(anexoDto))
                                .collect(Collectors.toList());

                actualizarAnexos(respuestaExamenValoracionTmp, anexosEntidades,
                                rutaArchivo, cambioDocumento, trabajoGrado, respuestaExamenValoracionDto);

                updateRtaExamenValoracionValues(respuestaExamenValoracionTmp, respuestaExamenValoracionDto);

                RespuestaExamenValoracion responseExamenValoracion = respuestaExamenValoracionRepository
                                .save(respuestaExamenValoracionTmp);
                return respuestaExamenValoracionResponseMapper.toDto(responseExamenValoracion);
        }

        private void actualizarAnexos(RespuestaExamenValoracion examenValoracionTmp,
                        List<AnexoRespuestaExamenValoracion> anexosDto, String rutaArchivo, Boolean cambioDocumento,
                        TrabajoGrado trabajoGrado, RespuestaExamenValoracionDto respuestaExamenValoracionDto) {

                List<AnexoRespuestaExamenValoracion> anexosExistentes = examenValoracionTmp.getAnexos();
                Map<Long, AnexoRespuestaExamenValoracion> anexosExistentesMap = anexosExistentes.stream()
                                .collect(Collectors.toMap(AnexoRespuestaExamenValoracion::getId, Function.identity()));

                // Eliminar los anexos que ya no están en el DTO
                for (Iterator<AnexoRespuestaExamenValoracion> it = anexosExistentes.iterator(); it.hasNext();) {
                        AnexoRespuestaExamenValoracion anexoExistente = it.next();
                        if (anexosDto.stream().noneMatch(
                                        anexoDto -> anexoDto.getId() != null
                                                        && anexoDto.getId().equals(anexoExistente.getId()))) {
                                FilesUtilities.deleteFileExample(anexoExistente.getLinkAnexo());
                                it.remove();
                        }
                }

                // Agregar o actualizar los anexos del DTO
                for (AnexoRespuestaExamenValoracion anexoDto : anexosDto) {
                        if (anexoDto.getId() == null) {
                                anexoDto.setLinkAnexo(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                anexoDto.getLinkAnexo()));
                                anexoDto.setRespuestaExamenValoracion(examenValoracionTmp);
                                anexosExistentes.add(anexoDto);
                        } else {
                                // Anexo existente, actualizar si es necesario
                                AnexoRespuestaExamenValoracion anexoExistente = anexosExistentesMap
                                                .get(anexoDto.getId());
                                if (anexoExistente != null
                                                && !anexoExistente.getLinkAnexo().equals(anexoDto.getLinkAnexo())) {
                                        FilesUtilities.deleteFileExample(anexoExistente.getLinkAnexo());
                                        anexoExistente.setLinkAnexo(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        anexoDto.getLinkAnexo()));
                                }
                        }
                        cambioDocumento = true;
                }
        }

        // Funciones privadas
        private void updateRtaExamenValoracionValues(RespuestaExamenValoracion respuestaExamenValoracion,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto) {

                respuestaExamenValoracion.setRespuestaExamenValoracion(
                                respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                respuestaExamenValoracion.setFechaMaximaEntrega(respuestaExamenValoracionDto.getFechaMaximaEntrega());
                // Update archivos
                respuestaExamenValoracion.setLinkFormatoB(respuestaExamenValoracionDto.getLinkFormatoB());
                respuestaExamenValoracion.setLinkFormatoC(respuestaExamenValoracionDto.getLinkFormatoC());
                respuestaExamenValoracion.setLinkObservaciones(respuestaExamenValoracionDto.getLinkObservaciones());
        }

        @Override
        @Transactional(readOnly = true)
        public ObtenerDocumentosParaEnvioCorreoDto obtenerDocumentosParaEnviarCorreo(Long idRtaExamenValoracion) {

                RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionRepository
                                .findById(idRtaExamenValoracion)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Respuesa del examen de valoracion con id: " + idRtaExamenValoracion
                                                                + " no encontrado"));

                List<AnexoRespuestaExamenValoracion> anexosRtaExamenValoracion = anexosRespuestaExamenValoracionRepository
                                .obtenerAnexosPorId(rtaExamenValoracion.getIdRespuestaExamenValoracion());

                ArrayList<String> listaAnexos = new ArrayList<>();
                for (int documento = 0; documento < anexosRtaExamenValoracion.size(); documento++) {
                        listaAnexos.add(
                                        FilesUtilities.recuperarArchivo(
                                                        anexosRtaExamenValoracion.get(documento).getLinkAnexo()));
                }

                ObtenerDocumentosParaEnvioCorreoDto obtenerDocumentosParaEnvioCorreoDto = new ObtenerDocumentosParaEnvioCorreoDto(
                                FilesUtilities.recuperarArchivo(rtaExamenValoracion.getLinkFormatoB()),
                                FilesUtilities.recuperarArchivo(rtaExamenValoracion.getLinkFormatoC()),
                                FilesUtilities.recuperarArchivo(rtaExamenValoracion.getLinkObservaciones()),
                                listaAnexos);

                return obtenerDocumentosParaEnvioCorreoDto;
        }

        private int validarEstado(Long idTrabajoGrado, ConceptosVarios conceptoEvaluador) {
                Integer numeroEstadoActual = trabajoGradoRepository.obtenerEstadoTrabajoGrado(idTrabajoGrado);
                int numEstado = 0;
                switch (conceptoEvaluador) {
                        case NO_APROBADO:
                                if (numeroEstadoActual == 5 || numeroEstadoActual == 6 || numeroEstadoActual == 9) {
                                        numEstado = 8;
                                } else if (numeroEstadoActual == 8) {
                                        numEstado = 9;
                                }
                                break;
                        case APLAZADO:
                                if (numeroEstadoActual == 5 || numeroEstadoActual == 6) {
                                        numEstado = 10;
                                } else if (numeroEstadoActual == 10) {
                                        numEstado = 11;
                                }
                                break;
                        case APROBADO:
                        default:
                                if (numeroEstadoActual == 5) {
                                        numEstado = 6;
                                } else if (numeroEstadoActual == 6) {
                                        numEstado = 7;
                                }
                                break;
                }
                return numEstado;
        }

        private String identificacionArchivo(TrabajoGrado trabajoGrado) {
                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                String procesoVa = "Respuesta_Examen_Valoracion";

                LocalDate fechaActual = LocalDate.now();
                int anio = fechaActual.getYear();
                int mes = fechaActual.getMonthValue();

                Long identificacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String informacionEstudiante = identificacionEstudiante + "-" + nombreEstudiante + "_"
                                + apellidoEstudiante;
                String rutaCarpeta = anio + "/" + mes + "/" + informacionEstudiante + "/" + procesoVa;

                return rutaCarpeta;
        }

}
