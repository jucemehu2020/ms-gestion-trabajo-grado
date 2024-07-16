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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.AnexoRespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado.ExamenValoracionCanceladoDto;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
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
        private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        private final TiemposPendientesRepository tiemposPendientesRepository;

        // Extras
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;

        @Autowired
        private EnvioCorreos envioCorreos;

        @Override
        @Transactional(noRollbackFor = InformationException.class)
        public RespuestaExamenValoracionResponseDto insertarInformacion(Long idTrabajoGrado,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                validarLink(respuestaExamenValoracionDto.getLinkFormatoB());
                validarLink(respuestaExamenValoracionDto.getLinkFormatoC());

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado + " no encontrado"));

                Long numeroNoAprobadoPorEvaluador = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                if (numeroNoAprobadoPorEvaluador == 2) {
                        throw new InformationException("Ya no es permitido registrar mas respuestas para el evaluador");
                }

                if (trabajoGrado.getNumeroEstado() == 15) {
                        throw new InformationException("Ya no es permitido registrar mas respuestas");
                }

                if (trabajoGrado.getNumeroEstado() != 5 && trabajoGrado.getNumeroEstado() != 6
                                && trabajoGrado.getNumeroEstado() != 8 && trabajoGrado.getNumeroEstado() != 9
                                && trabajoGrado.getNumeroEstado() != 10 && trabajoGrado.getNumeroEstado() != 11
                                && trabajoGrado.getNumeroEstado() != 12 && trabajoGrado.getNumeroEstado() != 13
                                && trabajoGrado.getNumeroEstado() != 14 && trabajoGrado.getNumeroEstado() != 17) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                Optional<SolicitudExamenValoracion> solicitudExamenValoracion = solicitudExamenValoracionRepository
                                .findByTrabajoGradoId(idTrabajoGrado);
                if ((solicitudExamenValoracion.get().getIdEvaluadorInterno() != respuestaExamenValoracionDto
                                .getIdEvaluador()
                                && respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO))
                                || (solicitudExamenValoracion.get()
                                                .getIdEvaluadorExterno() != respuestaExamenValoracionDto
                                                                .getIdEvaluador()
                                                && respuestaExamenValoracionDto.getTipoEvaluador()
                                                                .equals(TipoEvaluador.EXTERNO))) {
                        throw new InformationException(
                                        "La informacion del evaluador no coincide con la registrada en la solicitud");
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO)) {
                        archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.EXTERNO)) {
                        archivoClientExpertos
                                        .obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                List<RespuestaExamenValoracion> listaExamenes = respuestaExamenValoracionRepository
                                .findByTrabajoGrado(idTrabajoGrado);

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

                int numEstado = validarEstado(idTrabajoGrado, rtaExamenValoracion.getRespuestaExamenValoracion(), 1);

                LocalDate fechaActual = LocalDate.now();

                TiemposPendientes tiemposPendientes = new TiemposPendientes();

                if (respuestaExamenValoracionDto.getRespuestaExamenValoracion()
                                .equals(ConceptosVarios.NO_APROBADO)) {

                        tiemposPendientes.setEstado(numEstado);
                        tiemposPendientes.setFechaLimite(fechaActual.plusDays(15));
                        insertarInformacionTiempos(fechaActual.plusDays(15), trabajoGrado);
                        rtaExamenValoracion.setFechaMaximaEntrega(fechaActual.plusDays(15));
                } else if (respuestaExamenValoracionDto.getRespuestaExamenValoracion()
                                .equals(ConceptosVarios.APLAZADO)) {
                        insertarInformacionTiempos(fechaActual.plusDays(30), trabajoGrado);
                        tiemposPendientes.setEstado(numEstado);
                        tiemposPendientes.setFechaLimite(fechaActual.plusDays(30));
                        rtaExamenValoracion.setFechaMaximaEntrega(fechaActual.plusDays(30));
                }

                Long totalNoAprobados = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                if (totalNoAprobados > 0 && numEstado == 7 && trabajoGrado.getNumeroEstado() != 17) {
                        trabajoGrado.setNumeroEstado(16);
                        trabajoGradoRepository.save(trabajoGrado);
                        throw new InformationException(
                                        "El docente no ha actualizado los documentos con las correciones");
                } else {
                        trabajoGrado.setNumeroEstado(numEstado);
                }

                rtaExamenValoracion.setLinkFormatoB(FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                rtaExamenValoracion.getLinkFormatoB()));
                rtaExamenValoracion.setLinkFormatoC(FilesUtilities.guardarArchivoNew2(rutaArchivo,
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

                rtaExamenValoracion.setTrabajoGrado(trabajoGrado);

                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository
                                .save(rtaExamenValoracion);

                // Long numeroNoAprobado = respuestaExamenValoracionRepository
                // .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                if (totalNoAprobados == 3) {
                        trabajoGrado.setNumeroEstado(15);
                }

                return respuestaExamenValoracionResponseMapper.toDto(examenValoracionRes);
        }

        private void insertarInformacionTiempos(LocalDate fechaLimite, TrabajoGrado trabajoGrado) {
                TiemposPendientes tiemposPendientes = new TiemposPendientes();
                tiemposPendientes.setEstado(trabajoGrado.getNumeroEstado());

                LocalDate fechaActual = LocalDate.now();

                tiemposPendientes.setFechaRegistro(fechaActual);

                tiemposPendientes.setFechaLimite(fechaLimite);
                tiemposPendientes.setTrabajoGrado(trabajoGrado);
                tiemposPendientesRepository.save(tiemposPendientes);
        }

        @Override
        @Transactional
        public ExamenValoracionCanceladoDto insertarInformacionCancelado(
                        Long idTrabajoGrado,
                        ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado
                                                                + " no encontrado"));

                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);

                if (numeroNoAprobado != 4 && trabajoGrado.getNumeroEstado() != 15) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                ExamenValoracionCancelado examenValoracionCancelado = examenValoracionCanceladoRepository
                                .save(examenValoracionCanceladoMapper.toEntity(examenValoracionCanceladoDto));

                return examenValoracionCanceladoMapper.toDto(examenValoracionCancelado);
        }

        private boolean enviarCorreoTutorEstudiante(RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        TrabajoGrado trabajoGrado) {

                ArrayList<String> correos = new ArrayList<>();

                correos.add(trabajoGrado.getCorreoElectronicoTutor());

                EstudianteResponseDtoAll estudiante = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                correos.add(estudiante.getCorreoUniversidad());

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
                trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                List<RespuestaExamenValoracion> respuestas = respuestaExamenValoracionRepository
                                .findByTrabajoGrado(idTrabajoGrado);

                if (respuestas.isEmpty()) {
                        throw new InformationException("No se han registrado datos");
                }

                Map<String, List<RespuestaExamenValoracionResponseDto>> respuestasPorTipo = respuestas.stream()
                                .map(respuestaExamenValoracionResponseMapper::toDto)
                                .collect(Collectors.groupingBy(
                                                respuesta -> respuesta.getTipoEvaluador() == TipoEvaluador.INTERNO
                                                                ? "evaluador_interno"
                                                                : "evaluador_externo"));
                return respuestasPorTipo;
        }

        @Override
        public RespuestaExamenValoracionResponseDto actualizar(Long idRespuestaExamen,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
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
                                .findById(respuestaExamenValoracionTmp.getTrabajoGrado().getId()).orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                "Trabajo de grado con id: "
                                                                                + respuestaExamenValoracionTmp
                                                                                                .getTrabajoGrado()
                                                                                                .getId()
                                                                                + " no encontrado"));

                if (trabajoGrado.getNumeroEstado() != 6 && trabajoGrado.getNumeroEstado() != 7
                                && trabajoGrado.getNumeroEstado() != 8 && trabajoGrado.getNumeroEstado() != 9
                                && trabajoGrado.getNumeroEstado() != 10 && trabajoGrado.getNumeroEstado() != 11
                                && trabajoGrado.getNumeroEstado() != 12 && trabajoGrado.getNumeroEstado() != 13
                                && trabajoGrado.getNumeroEstado() != 14) {
                        throw new InformationException("No es permitido registrar la informacion");
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO)) {
                        archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.EXTERNO)) {
                        archivoClientExpertos
                                        .obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                Long ultimoRegistro = respuestaExamenValoracionRepository.findLatestIdByIdEvaluadorAndTipoEvaluador(
                                respuestaExamenValoracionDto.getIdEvaluador(),
                                respuestaExamenValoracionDto.getTipoEvaluador());

                if (idRespuestaExamen != ultimoRegistro) {
                        throw new InformationException(
                                        "No es permitido actualizar porque no es el ultimo registro realizado");
                }

                String rutaArchivo = identificacionArchivo(trabajoGrado);

                int numEstado = validarEstado(respuestaExamenValoracionTmp.getTrabajoGrado().getId(),
                                respuestaExamenValoracionDto.getRespuestaExamenValoracion(), 2);
                trabajoGrado.setNumeroEstado(numEstado);

                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                if (respuestaExamenValoracionDto.getLinkFormatoB()
                                .compareTo(respuestaExamenValoracionTmp.getLinkFormatoB()) != 0) {
                        validarLink(respuestaExamenValoracionDto.getLinkFormatoB());
                        respuestaExamenValoracionDto.setLinkFormatoB(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        respuestaExamenValoracionDto.getLinkFormatoB()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoB());
                        cambioDocumento = true;
                }
                if (respuestaExamenValoracionDto.getLinkFormatoC()
                                .compareTo(respuestaExamenValoracionTmp.getLinkFormatoC()) != 0) {
                        validarLink(respuestaExamenValoracionDto.getLinkFormatoC());
                        respuestaExamenValoracionDto.setLinkFormatoC(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        respuestaExamenValoracionDto.getLinkFormatoC()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoC());
                        cambioDocumento = true;
                }
                if (respuestaExamenValoracionDto.getLinkObservaciones()
                                .compareTo(respuestaExamenValoracionTmp.getLinkObservaciones()) != 0) {
                        validarLink(respuestaExamenValoracionDto.getLinkObservaciones());
                        respuestaExamenValoracionDto.setLinkObservaciones(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo,
                                        respuestaExamenValoracionDto.getLinkObservaciones()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkObservaciones());
                        cambioDocumento = true;
                }

                List<AnexoRespuestaExamenValoracion> anexosEntidades = respuestaExamenValoracionDto.getAnexos()
                                .stream()
                                .map(anexoDto -> anexoRespuestaExamenValoracionMapper.toEntity(anexoDto))
                                .collect(Collectors.toList());

                LocalDate fechaActual = LocalDate.now();

                actualizarAnexos(respuestaExamenValoracionTmp, anexosEntidades, rutaArchivo);

                TiemposPendientes tiemposPendientes = new TiemposPendientes();

                if (respuestaExamenValoracionDto.getRespuestaExamenValoracion()
                                .equals(ConceptosVarios.NO_APROBADO)) {

                        Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                        .findByTrabajoGradoId(respuestaExamenValoracionTmp.getTrabajoGrado().getId());
                        if (tiemposPendientesOpt.isPresent()) {
                                tiemposPendientes.setEstado(numEstado);
                                tiemposPendientes.setFechaLimite(fechaActual.plusDays(15));
                        } else {
                                insertarInformacionTiempos(fechaActual.plusDays(15), trabajoGrado);
                        }

                        respuestaExamenValoracionTmp.setFechaMaximaEntrega(fechaActual.plusDays(15));
                } else if (respuestaExamenValoracionDto.getRespuestaExamenValoracion()
                                .equals(ConceptosVarios.APLAZADO)) {

                        Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                        .findByTrabajoGradoId(respuestaExamenValoracionTmp.getTrabajoGrado().getId());
                        if (tiemposPendientesOpt.isPresent()) {
                                tiemposPendientes.setEstado(numEstado);
                                tiemposPendientes.setFechaLimite(fechaActual.plusDays(30));
                        } else {
                                insertarInformacionTiempos(fechaActual.plusDays(30), trabajoGrado);
                        }

                        respuestaExamenValoracionTmp.setFechaMaximaEntrega(fechaActual.plusDays(30));
                } else {
                        respuestaExamenValoracionTmp.setFechaMaximaEntrega(null);
                }

                updateRtaExamenValoracionValues(respuestaExamenValoracionTmp, respuestaExamenValoracionDto);

                RespuestaExamenValoracion responseExamenValoracion = respuestaExamenValoracionRepository
                                .save(respuestaExamenValoracionTmp);
                return respuestaExamenValoracionResponseMapper.toDto(responseExamenValoracion);
        }

        private void actualizarAnexos(RespuestaExamenValoracion examenValoracionTmp,
                        List<AnexoRespuestaExamenValoracion> anexosNuevos, String rutaArchivo) {
                List<AnexoRespuestaExamenValoracion> anexosActuales = examenValoracionTmp.getAnexos();

                // Mapa para buscar anexos actuales por enlace
                Map<String, AnexoRespuestaExamenValoracion> mapaAnexosActuales = anexosActuales.stream()
                                .collect(Collectors.toMap(AnexoRespuestaExamenValoracion::getLinkAnexo,
                                                Function.identity()));

                // Lista de anexos actualizados
                List<AnexoRespuestaExamenValoracion> anexosActualizados = new ArrayList<>();

                for (AnexoRespuestaExamenValoracion anexoNuevo : anexosNuevos) {
                        AnexoRespuestaExamenValoracion anexoActual = mapaAnexosActuales.get(anexoNuevo.getLinkAnexo());

                        if (anexoActual != null) {
                                // El anexo no ha cambiado, mantener el actual
                                anexosActualizados.add(anexoActual);
                        } else {
                                // El anexo ha cambiado o es nuevo, validar y agregar
                                validarLink(anexoNuevo.getLinkAnexo());
                                String rutaAnexoNueva = FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                anexoNuevo.getLinkAnexo());
                                anexoNuevo.setLinkAnexo(rutaAnexoNueva);
                                anexoNuevo.setRespuestaExamenValoracion(examenValoracionTmp);
                                anexosActualizados.add(anexoNuevo);
                        }
                }

                // Eliminar archivos de los anexos que ya no están en la lista nueva y eliminar
                // el anexo de la entidad
                Iterator<AnexoRespuestaExamenValoracion> iterator = anexosActuales.iterator();
                while (iterator.hasNext()) {
                        AnexoRespuestaExamenValoracion anexoActual = iterator.next();
                        if (!anexosActualizados.contains(anexoActual)) {
                                FilesUtilities.deleteFileExample(anexoActual.getLinkAnexo());
                                iterator.remove();
                        }
                }

                // Agregar los nuevos anexos a la colección existente
                anexosActuales.clear();
                anexosActuales.addAll(anexosActualizados);
        }

        private void updateRtaExamenValoracionValues(RespuestaExamenValoracion respuestaExamenValoracion,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto) {

                respuestaExamenValoracion.setRespuestaExamenValoracion(
                                respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                // Update archivos
                respuestaExamenValoracion.setLinkFormatoB(respuestaExamenValoracionDto.getLinkFormatoB());
                respuestaExamenValoracion.setLinkFormatoC(respuestaExamenValoracionDto.getLinkFormatoC());
                respuestaExamenValoracion.setLinkObservaciones(respuestaExamenValoracionDto.getLinkObservaciones());
        }

        private int validarEstado(Long idTrabajoGrado, ConceptosVarios conceptoEvaluador, int vieneDe) {
                Integer numeroEstadoActual = trabajoGradoRepository.obtenerEstadoTrabajoGrado(idTrabajoGrado);
                int numEstado = trabajoGradoRepository.obtenerEstadoTrabajoGrado(idTrabajoGrado);

                Boolean tieneDosEvaluadores = respuestaExamenValoracionRepository
                                .existsByEvaluadorTypes(TipoEvaluador.INTERNO, TipoEvaluador.EXTERNO);

                switch (conceptoEvaluador) {
                        case APROBADO:
                                if (numeroEstadoActual == 5 || (!tieneDosEvaluadores && vieneDe == 2)) {
                                        numEstado = 6;
                                } else if (numeroEstadoActual == 6 || numeroEstadoActual == 12
                                                || numeroEstadoActual == 13 || numeroEstadoActual == 17) {
                                        Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                                        .findByTrabajoGradoId(idTrabajoGrado);
                                        if (tiemposPendientesOpt.isPresent()) {
                                                tiemposPendientesRepository.delete(tiemposPendientesOpt.get());
                                        }
                                        numEstado = 7;
                                } else if (numeroEstadoActual == 9 || numeroEstadoActual == 8) {
                                        numEstado = 12;
                                } else if (numeroEstadoActual == 10 || numeroEstadoActual == 11
                                                || numeroEstadoActual == 14) {
                                        numEstado = 13;
                                }
                                break;
                        case NO_APROBADO:
                                if (numeroEstadoActual == 5 || (!tieneDosEvaluadores && vieneDe == 2)) {
                                        numEstado = 8;
                                } else if (numeroEstadoActual == 8 || numeroEstadoActual == 12
                                                || numeroEstadoActual == 14) {
                                        numEstado = 9;
                                } else if (numeroEstadoActual == 7 || numeroEstadoActual == 6) {
                                        numEstado = 12;
                                } else if (numeroEstadoActual == 11 || numeroEstadoActual == 10
                                                || numeroEstadoActual == 13) {
                                        numEstado = 14;
                                }
                                break;
                        case APLAZADO:
                                if (numeroEstadoActual == 5 || (!tieneDosEvaluadores && vieneDe == 2)) {
                                        numEstado = 10;
                                } else if (numeroEstadoActual == 10 || numeroEstadoActual == 13
                                                || numeroEstadoActual == 14) {
                                        numEstado = 11;
                                } else if (numeroEstadoActual == 6 || numeroEstadoActual == 7) {
                                        numEstado = 13;
                                } else if (numeroEstadoActual == 8 || numeroEstadoActual == 9
                                                || numeroEstadoActual == 12) {
                                        numEstado = 14;
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

        @Override
        @Transactional(readOnly = true)
        public Boolean evaluadorNoRespondio(Long idTrabajoGrado) {
                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + idTrabajoGrado + " no encontrado"));
                trabajoGrado.setNumeroEstado(35);
                trabajoGradoRepository.save(trabajoGrado);
                return true;
        }

        private void validarLink(String link) {
                ValidationUtils.validarFormatoLink(link);
                String base64 = link.substring(link.indexOf('-') + 1);
                ValidationUtils.validarBase64(base64);
        }

}
