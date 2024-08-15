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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.AnexoRespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RetornoFormatoBDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado.ExamenValoracionCanceladoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoRespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionResponseMapper;
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
        private final RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
        private final RespuestaExamenValoracionResponseMapper respuestaExamenValoracionResponseMapper;
        private final AnexoRespuestaExamenValoracionMapper anexoRespuestaExamenValoracionMapper;
        private final ExamenValoracionCanceladoMapper examenValoracionCanceladoMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        private final TiemposPendientesRepository tiemposPendientesRepository;
        private final ArchivoClient archivoClient;

        @Autowired
        private EnvioCorreos envioCorreos;

        /**
         * Inserta la información de la respuesta del examen de valoración para un
         * trabajo de grado.
         * 
         * @param idTrabajoGrado               El ID del trabajo de grado.
         * @param respuestaExamenValoracionDto DTO con la información de la respuesta
         *                                     del examen de valoración.
         * @param result                       Resultado de la validación de los datos
         *                                     de entrada.
         * @return RespuestaExamenValoracionResponseDto con los detalles de la respuesta
         *         del examen guardada.
         * @throws FieldErrorException  Si hay errores en la validación de los datos de
         *                              entrada.
         * @throws InformationException Si se presentan condiciones que impidan el
         *                              registro de la información.
         */
        @Override
        @Transactional(noRollbackFor = InformationException.class)
        public RespuestaExamenValoracionResponseDto insertarInformacion(Long idTrabajoGrado,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto, BindingResult result) {

                // Verificar si hay errores en la validación de los datos de entrada
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Validar los enlaces proporcionados
                validarLink(respuestaExamenValoracionDto.getLinkFormatoB());
                validarLink(respuestaExamenValoracionDto.getLinkFormatoC());

                // Obtener el trabajo de grado asociado al ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si el evaluador ya ha registrado dos respuestas "No Aprobado"
                Long numeroNoAprobadoPorEvaluador = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobadoAndEvaluador(idTrabajoGrado,
                                                respuestaExamenValoracionDto.getIdEvaluador(),
                                                respuestaExamenValoracionDto.getTipoEvaluador());

                if (numeroNoAprobadoPorEvaluador == 2) {
                        throw new InformationException("No se permiten más registros para el evaluador");
                }

                // Verificar si el estado del trabajo de grado permite registrar más respuestas
                if (trabajoGrado.getNumeroEstado() == 15) {
                        throw new InformationException("Ya no es permitido registrar más respuestas");
                }

                // Validar si el estado actual del trabajo de grado permite registrar la
                // información
                if (!Arrays.asList(5, 6, 8, 9, 10, 11, 12, 13, 14, 17).contains(trabajoGrado.getNumeroEstado())) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Verificar si la información del evaluador coincide con la registrada en la
                // solicitud
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
                                        "La información del evaluador no coincide con la registrada en la solicitud");
                }

                // Verificar la existencia del evaluador en el sistema de archivos (interno o
                // externo)
                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO)) {
                        archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador());
                } else if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.EXTERNO)) {
                        archivoClient.obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                // Verificar si el evaluador ya ha registrado una respuesta "Aprobado"
                List<RespuestaExamenValoracion> listaExamenes = respuestaExamenValoracionRepository
                                .findByTrabajoGrado(idTrabajoGrado);
                for (RespuestaExamenValoracion examen : listaExamenes) {
                        if (respuestaExamenValoracionDto.getIdEvaluador().equals(examen.getIdEvaluador()) &&
                                        respuestaExamenValoracionDto.getTipoEvaluador()
                                                        .equals(examen.getTipoEvaluador())
                                        &&
                                        examen.getRespuestaExamenValoracion().equals(ConceptosVarios.APROBADO)) {
                                throw new InformationException(
                                                "El evaluador previamente dio su concepto como APROBADO, no es permitido que realice nuevos registros");
                        }
                }

                // Convertir el DTO a la entidad correspondiente
                RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionMapper
                                .toEntity(respuestaExamenValoracionDto);

                // Obtener la ruta del archivo donde se almacenarán los documentos
                String rutaArchivo = identificacionArchivo(trabajoGrado);

                // Validar y actualizar el estado del trabajo de grado
                int numEstado = validarEstado(idTrabajoGrado, rtaExamenValoracion.getRespuestaExamenValoracion(), 1,
                                null, rtaExamenValoracion.getTipoEvaluador());
                LocalDate fechaActual = LocalDate.now();

                TiemposPendientes tiemposPendientes = new TiemposPendientes();

                // Configurar tiempos pendientes y fechas límite en función de la respuesta del
                // examen
                if (respuestaExamenValoracionDto.getRespuestaExamenValoracion().equals(ConceptosVarios.NO_APROBADO)) {
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

                // Contar las respuestas "No Aprobado" y "Aplazado" asociadas al trabajo de
                // grado
                Long totalNoAprobados = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);
                Long totalAplazados = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaAplazado(idTrabajoGrado);

                // Verificar si hay tanto "No Aprobado" como "Aplazado" y si el estado actual es
                // 7
                if (totalNoAprobados > 0 && totalAplazados > 0 && numEstado == 7
                                && trabajoGrado.getNumeroEstado() != 17) {
                        trabajoGrado.setNumeroEstado(16);
                        trabajoGradoRepository.save(trabajoGrado);
                        throw new InformationException(
                                        "El docente no ha actualizado los documentos con las correcciones");
                } else {
                        trabajoGrado.setNumeroEstado(numEstado);
                }

                // Guardar los archivos asociados al examen de valoración
                rtaExamenValoracion.setLinkFormatoB(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo, rtaExamenValoracion.getLinkFormatoB()));
                rtaExamenValoracion.setLinkFormatoC(
                                FilesUtilities.guardarArchivoNew2(rutaArchivo, rtaExamenValoracion.getLinkFormatoC()));

                // Procesar y guardar los anexos si están presentes
                List<AnexoRespuestaExamenValoracion> updatedLinkAnexos = new ArrayList<>();
                if (respuestaExamenValoracionDto.getAnexos() != null) {
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
                }

                // Asociar la respuesta del examen con el trabajo de grado
                rtaExamenValoracion.setTrabajoGrado(trabajoGrado);

                // Enviar correo al tutor y al estudiante informando sobre la nueva respuesta
                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                // Guardar la respuesta del examen de valoración en la base de datos
                RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository
                                .save(rtaExamenValoracion);

                // Si hay 3 respuestas "No Aprobado", actualizar el estado del trabajo de grado
                if (totalNoAprobados == 3) {
                        trabajoGrado.setNumeroEstado(15);
                }

                // Devolver el DTO con la información de la respuesta guardada
                return respuestaExamenValoracionResponseMapper.toDto(examenValoracionRes);
        }

        /**
         * Inserta o actualiza la información de tiempos pendientes para un trabajo de
         * grado.
         * 
         * @param fechaLimite  La fecha límite establecida para el trabajo de grado.
         * @param trabajoGrado El trabajo de grado asociado.
         */
        private void insertarInformacionTiempos(LocalDate fechaLimite, TrabajoGrado trabajoGrado) {
                // Busca si ya existe un registro de tiempos pendientes para el trabajo de grado
                Optional<TiemposPendientes> optionalTiemposPendientes = tiemposPendientesRepository
                                .findByTrabajoGradoId(trabajoGrado.getId());

                TiemposPendientes tiemposPendientes = new TiemposPendientes();
                if (optionalTiemposPendientes.isPresent()) {
                        // Si el registro ya existe, se actualiza
                        tiemposPendientes = optionalTiemposPendientes.get();
                } else {
                        // Si no existe, se crea un nuevo registro
                        tiemposPendientes = new TiemposPendientes();
                        tiemposPendientes.setTrabajoGrado(trabajoGrado);
                }

                // Se establecen la fecha de registro y el estado actual del trabajo de grado
                tiemposPendientes.setFechaRegistro(LocalDate.now());
                tiemposPendientes.setEstado(trabajoGrado.getNumeroEstado());
                tiemposPendientes.setFechaLimite(fechaLimite);

                // Guarda el registro de tiempos pendientes en el repositorio
                tiemposPendientesRepository.save(tiemposPendientes);
        }

        /**
         * Inserta la información de cancelación de un examen de valoración para un
         * trabajo de grado.
         * 
         * @param idTrabajoGrado               El ID del trabajo de grado.
         * @param examenValoracionCanceladoDto DTO con la información de la cancelación
         *                                     del examen de valoración.
         * @param result                       Resultado de la validación de los datos
         *                                     de entrada.
         * @return ExamenValoracionCanceladoDto DTO con la información del examen de
         *         valoración cancelado guardado.
         * @throws FieldErrorException  Si hay errores en la validación de los datos de
         *                              entrada.
         * @throws InformationException Si se presentan condiciones que impidan el
         *                              registro de la cancelación.
         */
        @Override
        @Transactional
        public ExamenValoracionCanceladoDto insertarInformacionCancelado(Long idTrabajoGrado,
                        ExamenValoracionCanceladoDto examenValoracionCanceladoDto, BindingResult result) {

                // Verificar si hay errores en la validación de los datos de entrada
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Obtener el trabajo de grado asociado al ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si ya existe un registro de cancelación para el trabajo de grado
                Optional<ExamenValoracionCancelado> examenExisteValoracionCancelado = examenValoracionCanceladoRepository
                                .findByTrabajoGradoId(idTrabajoGrado);
                if (examenExisteValoracionCancelado.isPresent()) {
                        throw new InformationException(
                                        "Ya existe un examen de valoración cancelado para este trabajo de grado");
                }

                // Verificar si el trabajo de grado cumple las condiciones para cancelar el
                // examen
                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado);
                if (numeroNoAprobado != 4 && trabajoGrado.getNumeroEstado() != 15) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Eliminar cualquier registro de tiempos pendientes asociado al trabajo de
                // grado
                Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                .findByTrabajoGradoId(idTrabajoGrado);
                if (tiemposPendientesOpt.isPresent()) {
                        tiemposPendientesRepository.delete(tiemposPendientesOpt.get());
                }

                // Eliminar todas las respuestas de examen de valoración asociadas al trabajo de
                // grado
                List<RespuestaExamenValoracion> respuestas = respuestaExamenValoracionRepository
                                .findByTrabajoGrado(idTrabajoGrado);
                if (!respuestas.isEmpty()) {
                        respuestaExamenValoracionRepository.deleteAll(respuestas);
                }

                // Convertir el DTO a la entidad correspondiente
                ExamenValoracionCancelado examenValoracionCancelado = examenValoracionCanceladoMapper
                                .toEntity(examenValoracionCanceladoDto);
                examenValoracionCancelado.setTrabajoGrado(trabajoGrado);

                // Guardar la cancelación del examen de valoración en la base de datos y
                // devolver el DTO
                return examenValoracionCanceladoMapper
                                .toDto(examenValoracionCanceladoRepository.save(examenValoracionCancelado));
        }

        /**
         * Envía un correo electrónico al tutor y al estudiante con los documentos
         * adjuntos del examen de valoración.
         *
         * @param respuestaExamenValoracionDto DTO que contiene la información de la
         *                                     respuesta del examen de valoración.
         * @param trabajoGrado                 El trabajo de grado asociado.
         * @return boolean Verdadero si el correo fue enviado con éxito.
         */
        private boolean enviarCorreoTutorEstudiante(RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        TrabajoGrado trabajoGrado) {

                // Lista de correos a los que se enviará el correo electrónico
                ArrayList<String> correos = new ArrayList<>();
                correos.add(trabajoGrado.getCorreoElectronicoTutor());

                // Obtener la información del estudiante
                EstudianteResponseDtoAll estudiante = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                correos.add(estudiante.getCorreoUniversidad());

                // Crear un mapa para almacenar los documentos que se enviarán como adjuntos
                Map<String, Object> documentosParaEvaluador = new HashMap<>();

                // Separar el formato B y C y agregarlos al mapa de documentos
                String[] formatoB = respuestaExamenValoracionDto.getLinkFormatoB().split("-");
                documentosParaEvaluador.put("formatoB", formatoB[1]);

                String[] formatoC = respuestaExamenValoracionDto.getLinkFormatoC().split("-");
                documentosParaEvaluador.put("formatoC", formatoC[1]);

                // Procesar los anexos y agregarlos al mapa de documentos
                List<AnexoRespuestaExamenValoracionDto> anexos = respuestaExamenValoracionDto.getAnexos();
                if (anexos != null) {
                        for (int i = 0; i < anexos.size(); i++) {
                                String[] anexo = anexos.get(i).getLinkAnexo().split("-");
                                documentosParaEvaluador.put("anexo-" + (i + 1), anexo[1]);
                        }
                }

                // Enviar el correo con los documentos adjuntos
                envioCorreos.enviarCorreoConAnexos(correos, respuestaExamenValoracionDto.getEnvioEmail().getAsunto(),
                                respuestaExamenValoracionDto.getEnvioEmail().getMensaje(), documentosParaEvaluador);

                return true;
        }

        /**
         * Busca todas las respuestas del examen de valoración asociadas a un trabajo de
         * grado y las agrupa por tipo de evaluador.
         *
         * @param idTrabajoGrado El ID del trabajo de grado.
         * @return Map<String, List<RespuestaExamenValoracionResponseDto>> Un mapa que
         *         agrupa las respuestas por tipo de evaluador (interno o externo).
         * @throws ResourceNotFoundException Si no se encuentra el trabajo de grado.
         * @throws InformationException      Si no se han registrado respuestas.
         */
        @Override
        @Transactional(readOnly = true)
        public Map<String, List<RespuestaExamenValoracionResponseDto>> buscarPorId(Long idTrabajoGrado) {
                // Verificar si el trabajo de grado existe
                trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Obtener todas las respuestas asociadas al trabajo de grado
                List<RespuestaExamenValoracion> respuestas = respuestaExamenValoracionRepository
                                .findByTrabajoGrado(idTrabajoGrado);

                // Verificar si se han registrado respuestas
                if (respuestas.isEmpty()) {
                        throw new InformationException("No se han registrado datos");
                }

                // Agrupar las respuestas por tipo de evaluador (interno o externo)
                Map<String, List<RespuestaExamenValoracionResponseDto>> respuestasPorTipo = respuestas.stream()
                                .map(respuestaExamenValoracionResponseMapper::toDto)
                                .collect(Collectors.groupingBy(
                                                respuesta -> respuesta.getTipoEvaluador() == TipoEvaluador.INTERNO
                                                                ? "evaluador_interno"
                                                                : "evaluador_externo"));

                return respuestasPorTipo;
        }

        /**
         * Actualiza la respuesta de un examen de valoración.
         *
         * @param idRespuestaExamen            El ID de la respuesta de examen de
         *                                     valoración a actualizar.
         * @param respuestaExamenValoracionDto El DTO que contiene la nueva información
         *                                     de la respuesta de examen.
         * @param result                       El resultado de la validación de la
         *                                     solicitud.
         * @return Un DTO con la respuesta de examen de valoración actualizada.
         * @throws FieldErrorException       Si el resultado de la validación tiene
         *                                   errores.
         * @throws ResourceNotFoundException Si no se encuentra la respuesta de examen
         *                                   de valoración o el trabajo de grado.
         * @throws InformationException      Si hay restricciones que impiden la
         *                                   actualización.
         */
        @Override
        public RespuestaExamenValoracionResponseDto actualizar(Long idRespuestaExamen,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                // Verificar si hay errores de validación
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Buscar la respuesta de examen de valoración por ID
                RespuestaExamenValoracion respuestaExamenValoracionTmp = respuestaExamenValoracionRepository
                                .findById(idRespuestaExamen)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Respuesta examen de valoración con id " + idRespuestaExamen
                                                                + " no encontrado"));

                // Validar que los datos del evaluador coincidan con los registrados
                if (respuestaExamenValoracionTmp.getIdEvaluador() != respuestaExamenValoracionDto.getIdEvaluador()
                                || respuestaExamenValoracionTmp.getTipoEvaluador() != respuestaExamenValoracionDto
                                                .getTipoEvaluador()) {
                        throw new InformationException("Los datos del evaluador no corresponden");
                }

                // Buscar el trabajo de grado asociado
                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(respuestaExamenValoracionTmp.getTrabajoGrado().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id "
                                                                + respuestaExamenValoracionTmp.getTrabajoGrado().getId()
                                                                + " no encontrado"));

                // Validar el estado del trabajo de grado para permitir la actualización
                if (trabajoGrado.getNumeroEstado() < 6 || trabajoGrado.getNumeroEstado() > 15) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Validar el evaluador según su tipo (interno o externo)
                if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.INTERNO)) {
                        archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador());
                } else if (respuestaExamenValoracionDto.getTipoEvaluador().equals(TipoEvaluador.EXTERNO)) {
                        archivoClient.obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador());
                }

                // Verificar que el registro actual sea el último realizado por el evaluador
                List<RespuestaExamenValoracion> ultimoRegistroList = respuestaExamenValoracionRepository
                                .findLatestByIdEvaluadorAndTipoEvaluadorAndId(
                                                respuestaExamenValoracionDto.getIdEvaluador(),
                                                respuestaExamenValoracionDto.getTipoEvaluador(),
                                                idRespuestaExamen);

                if (!ultimoRegistroList.isEmpty()) {
                        RespuestaExamenValoracion ultimoRegistro = ultimoRegistroList.get(0);
                        if (!idRespuestaExamen.equals(ultimoRegistro.getId())) {
                                throw new InformationException(
                                                "No es permitido actualizar porque no es el último registro realizado por el evaluador");
                        }
                } else {
                        throw new InformationException(
                                        "No se encontró ningún registro asociado a este evaluador, tipo de evaluador, y respuesta específica");
                }

                // Obtener la ruta de archivo para guardar los archivos actualizados
                String rutaArchivo = identificacionArchivo(trabajoGrado);

                // Actualizar el estado del trabajo de grado si ha cambiado la respuesta del
                // examen
                int numEstado = trabajoGrado.getNumeroEstado();
                if (!respuestaExamenValoracionDto.getRespuestaExamenValoracion()
                                .equals(respuestaExamenValoracionTmp.getRespuestaExamenValoracion())) {
                        numEstado = validarEstado(respuestaExamenValoracionTmp.getTrabajoGrado().getId(),
                                        respuestaExamenValoracionDto.getRespuestaExamenValoracion(), 2,
                                        respuestaExamenValoracionTmp.getRespuestaExamenValoracion(),
                                        respuestaExamenValoracionTmp.getTipoEvaluador());
                        trabajoGrado.setNumeroEstado(numEstado);
                }

                // Enviar correo al tutor y estudiante sobre la actualización
                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                // Validar y actualizar los enlaces de los archivos si han cambiado
                if (!respuestaExamenValoracionDto.getLinkFormatoB()
                                .equals(respuestaExamenValoracionTmp.getLinkFormatoB())) {
                        validarLink(respuestaExamenValoracionDto.getLinkFormatoB());
                        respuestaExamenValoracionDto.setLinkFormatoB(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo, respuestaExamenValoracionDto.getLinkFormatoB()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoB());
                }

                if (!respuestaExamenValoracionDto.getLinkFormatoC()
                                .equals(respuestaExamenValoracionTmp.getLinkFormatoC())) {
                        validarLink(respuestaExamenValoracionDto.getLinkFormatoC());
                        respuestaExamenValoracionDto.setLinkFormatoC(FilesUtilities.guardarArchivoNew2(
                                        rutaArchivo, respuestaExamenValoracionDto.getLinkFormatoC()));
                        FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoC());
                }

                // Actualizar los anexos si han cambiado
                List<AnexoRespuestaExamenValoracionDto> anexos = respuestaExamenValoracionDto.getAnexos();
                if (anexos == null) {
                        anexos = Collections.emptyList();
                }
                List<AnexoRespuestaExamenValoracion> anexosEntidades = anexos.stream()
                                .map(anexoRespuestaExamenValoracionMapper::toEntity)
                                .collect(Collectors.toList());
                actualizarAnexos(respuestaExamenValoracionTmp, anexosEntidades, rutaArchivo);

                // Manejar tiempos pendientes según la nueva respuesta del examen
                LocalDate fechaActual = LocalDate.now();
                TiemposPendientes tiemposPendientes = new TiemposPendientes();

                if (respuestaExamenValoracionDto.getRespuestaExamenValoracion().equals(ConceptosVarios.NO_APROBADO)) {
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

                // Actualizar los valores de la respuesta del examen
                updateRtaExamenValoracionValues(respuestaExamenValoracionTmp, respuestaExamenValoracionDto);

                // Guardar la respuesta de examen actualizada
                RespuestaExamenValoracion responseExamenValoracion = respuestaExamenValoracionRepository
                                .save(respuestaExamenValoracionTmp);

                // Devolver el DTO de la respuesta actualizada
                return respuestaExamenValoracionResponseMapper.toDto(responseExamenValoracion);
        }

        /**
         * Actualiza los anexos de una respuesta de examen de valoración.
         *
         * @param examenValoracionTmp La entidad de la respuesta de examen de valoración
         *                            actual.
         * @param anexosNuevos        La lista de nuevos anexos a agregar o actualizar.
         * @param rutaArchivo         La ruta donde se guardarán los archivos de los
         *                            anexos.
         */
        private void actualizarAnexos(RespuestaExamenValoracion examenValoracionTmp,
                        List<AnexoRespuestaExamenValoracion> anexosNuevos,
                        String rutaArchivo) {
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

        /**
         * Actualiza los valores de la entidad de respuesta de examen de valoración.
         *
         * @param respuestaExamenValoracion    La entidad de la respuesta de examen de
         *                                     valoración a actualizar.
         * @param respuestaExamenValoracionDto El DTO que contiene los nuevos valores
         *                                     para la respuesta de examen.
         */
        private void updateRtaExamenValoracionValues(RespuestaExamenValoracion respuestaExamenValoracion,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto) {

                // Actualizar la respuesta del examen de valoración
                respuestaExamenValoracion.setRespuestaExamenValoracion(
                                respuestaExamenValoracionDto.getRespuestaExamenValoracion());

                // Actualizar los enlaces de los archivos
                respuestaExamenValoracion.setLinkFormatoB(respuestaExamenValoracionDto.getLinkFormatoB());
                respuestaExamenValoracion.setLinkFormatoC(respuestaExamenValoracionDto.getLinkFormatoC());
        }

        /**
         * Valida y determina el estado del trabajo de grado basado en el concepto del
         * evaluador,
         * el tipo de evaluador y el estado actual del trabajo de grado.
         *
         * @param idTrabajoGrado       El ID del trabajo de grado.
         * @param conceptoEvaluador    El concepto dado por el evaluador (APROBADO,
         *                             NO_APROBADO, APLAZADO).
         * @param vieneDe              Indica el origen de la evaluación (1 para nuevo,
         *                             2 para actualización).
         * @param conceptoEvaluadorOld El concepto anterior del evaluador (si existe).
         * @param tipoEvaluador        El tipo de evaluador (INTERNO o EXTERNO).
         * @return El nuevo estado del trabajo de grado basado en la evaluación.
         */
        private int validarEstado(Long idTrabajoGrado, ConceptosVarios conceptoEvaluador, int vieneDe,
                        ConceptosVarios conceptoEvaluadorOld, TipoEvaluador tipoEvaluador) {
                // Obtener el estado actual del trabajo de grado
                Integer numeroEstadoActual = trabajoGradoRepository.obtenerEstadoTrabajoGrado(idTrabajoGrado);
                int numEstado = numeroEstadoActual;

                // Verificar si hay dos evaluadores para el trabajo de grado
                Boolean tieneDosEvaluadores = respuestaExamenValoracionRepository
                                .existsByEvaluadorTypes(idTrabajoGrado);

                // Determinar el tipo de evaluador opuesto (INTERNO o EXTERNO)
                TipoEvaluador t2 = tipoEvaluador.equals(TipoEvaluador.INTERNO) ? TipoEvaluador.EXTERNO
                                : TipoEvaluador.INTERNO;

                // Inicializar el concepto del otro evaluador (si existe)
                ConceptosVarios conceptoOther = null;

                // Si hay dos evaluadores, obtener el concepto del otro evaluador
                if (tieneDosEvaluadores) {
                        List<ConceptosVarios> conceptos = respuestaExamenValoracionRepository
                                        .findConceptoByTipoEvaluadorAndTrabajoGrado(t2, idTrabajoGrado);
                        conceptoOther = conceptos.stream().findFirst().orElse(null);
                }

                // Evaluar el nuevo estado basado en el concepto del evaluador actual
                switch (conceptoEvaluador) {
                        case APROBADO:
                                if (numeroEstadoActual == 5 || (!tieneDosEvaluadores && vieneDe == 2)) {
                                        numEstado = 6;
                                } else if (numeroEstadoActual == 6 || numeroEstadoActual == 12
                                                || numeroEstadoActual == 13
                                                || numeroEstadoActual == 17) {
                                        // Eliminar tiempos pendientes si se aprueba en estados específicos
                                        tiemposPendientesRepository.findByTrabajoGradoId(idTrabajoGrado)
                                                        .ifPresent(tiemposPendientesRepository::delete);
                                        numEstado = 7;
                                } else if (numeroEstadoActual == 9 || numeroEstadoActual == 8
                                                || numeroEstadoActual == 15
                                                || (conceptoOther != null
                                                                && conceptoOther.equals(ConceptosVarios.NO_APROBADO)
                                                                && numeroEstadoActual == 14)) {
                                        numEstado = 12;
                                } else if (numeroEstadoActual == 10 || numeroEstadoActual == 11
                                                || (conceptoOther != null
                                                                && conceptoOther.equals(ConceptosVarios.APLAZADO)
                                                                && numeroEstadoActual == 14)) {
                                        numEstado = 13;
                                }
                                break;
                        case NO_APROBADO:
                                if (numeroEstadoActual == 5 || (!tieneDosEvaluadores && vieneDe == 2)) {
                                        numEstado = 8;
                                } else if (numeroEstadoActual == 8 || numeroEstadoActual == 12
                                                || numeroEstadoActual == 14) {
                                        numEstado = 9;
                                } else if (numeroEstadoActual == 7 || numeroEstadoActual == 6
                                                || (conceptoOther != null
                                                                && conceptoOther.equals(ConceptosVarios.APROBADO)
                                                                && numeroEstadoActual == 13)) {
                                        numEstado = 12;
                                } else if (numeroEstadoActual == 11 || numeroEstadoActual == 10
                                                || (conceptoOther != null
                                                                && conceptoOther.equals(ConceptosVarios.APLAZADO)
                                                                && numeroEstadoActual == 13)) {
                                        numEstado = 14;
                                }
                                break;
                        case APLAZADO:
                                if (numeroEstadoActual == 5 || (!tieneDosEvaluadores && vieneDe == 2)) {
                                        numEstado = 10;
                                } else if (numeroEstadoActual == 10 || numeroEstadoActual == 13
                                                || numeroEstadoActual == 14) {
                                        numEstado = 11;
                                } else if (numeroEstadoActual == 6 || numeroEstadoActual == 7
                                                || (conceptoOther != null
                                                                && conceptoOther.equals(ConceptosVarios.APROBADO)
                                                                && numeroEstadoActual == 12)) {
                                        numEstado = 13;
                                } else if (numeroEstadoActual == 8 || numeroEstadoActual == 9
                                                || numeroEstadoActual == 15
                                                || (conceptoOther != null
                                                                && conceptoOther.equals(ConceptosVarios.NO_APROBADO)
                                                                && numeroEstadoActual == 12)) {
                                        numEstado = 14;
                                }
                                break;
                }

                // Devolver el nuevo estado determinado
                return numEstado;
        }

        /**
         * Genera la ruta de archivo para un trabajo de grado específico, basada en la
         * información del estudiante.
         *
         * @param trabajoGrado El trabajo de grado para el cual se genera la ruta.
         * @return La ruta generada en formato de cadena.
         */
        private String identificacionArchivo(TrabajoGrado trabajoGrado) {
                // Obtener información del estudiante asociado al trabajo de grado
                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                String procesoVa = "Respuesta_Examen_Valoracion"; // Definir el nombre del proceso

                // Obtener la fecha actual para estructurar la ruta
                LocalDate fechaActual = LocalDate.now();
                int anio = fechaActual.getYear();
                int mes = fechaActual.getMonthValue();

                // Crear una cadena con la identificación y el nombre del estudiante
                Long identificacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String informacionEstudiante = identificacionEstudiante + "-" + nombreEstudiante + "_"
                                + apellidoEstudiante;

                // Generar la ruta de la carpeta
                String rutaCarpeta = anio + "/" + mes + "/" + informacionEstudiante + "/" + procesoVa;

                return rutaCarpeta;
        }

        /**
         * Marca un trabajo de grado como no respondido por el evaluador, eliminando
         * cualquier registro de tiempo pendiente.
         *
         * @param idTrabajoGrado El ID del trabajo de grado.
         * @return True si la operación se realizó correctamente.
         */
        @Override
        public Boolean evaluadorNoRespondio(Long idTrabajoGrado) {
                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si existen tiempos pendientes y eliminarlos
                Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                .findByTrabajoGradoId(idTrabajoGrado);
                if (tiemposPendientesOpt.isPresent()) {
                        tiemposPendientesRepository.delete(tiemposPendientesOpt.get());
                }

                // Actualizar el estado del trabajo de grado y guardar los cambios
                trabajoGrado.setNumeroEstado(35);
                trabajoGradoRepository.save(trabajoGrado);
                return true;
        }

        /**
         * Obtiene los enlaces de los formatos B asociados a un trabajo de grado
         * específico.
         *
         * @param idTrabajoGrado El ID del trabajo de grado.
         * @return Un objeto RetornoFormatoBDto que contiene los formatos B.
         */
        @Override
        public RetornoFormatoBDto obtenerFormatosB(Long idTrabajoGrado) {
                // Verificar la existencia del trabajo de grado
                trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Obtener la lista de enlaces de formatos B asociados al trabajo de grado
                List<String> listaFormatosB = respuestaExamenValoracionRepository
                                .findLinkFormatoBByIdTrabajoGradoAndRespuestaExamenValoracion(idTrabajoGrado);

                // Crear un mapa para almacenar los formatos B con sus claves
                Map<String, String> formatosB = new HashMap<>();
                for (int i = 0; i < listaFormatosB.size(); i++) {
                        String clave = "formatoBEv" + (i + 1);
                        formatosB.put(clave, listaFormatosB.get(i));
                }

                // Construir y devolver el DTO que contiene los formatos B
                RetornoFormatoBDto retornoFormatoBDto = RetornoFormatoBDto.builder()
                                .formatosB(formatosB)
                                .build();

                return retornoFormatoBDto;
        }

        /**
         * Valida un enlace verificando su formato y contenido en base64.
         *
         * @param link El enlace a validar.
         */
        private void validarLink(String link) {
                // Validar el formato del enlace
                ValidationUtils.validarFormatoLink(link);

                // Extraer la parte codificada en base64 del enlace y validarla
                String base64 = link.substring(link.indexOf('-') + 1);
                ValidationUtils.validarBase64(base64);
        }

}
