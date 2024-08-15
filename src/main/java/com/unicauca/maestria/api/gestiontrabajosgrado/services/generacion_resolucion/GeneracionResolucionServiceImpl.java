package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteListDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteGeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneracionResolucionServiceImpl implements GeneracionResolucionService {

        private final GeneracionResolucionRepository generacionResolucionRepository;
        private final GeneracionResolucionMapper generacionResolucionMapper;
        private final GeneracionResolucionResponseMapper generacionResolucionResponseMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final RespuestaComiteGeneracionResolucionRepository respuestaComiteGeneracionResolucionRepository;
        private final ArchivoClient archivoClient;

        @Autowired
        private EnvioCorreos envioCorreos;

        /**
         * Método que lista directores y codirectores disponibles.
         * Este método interactúa con un servidor externo para obtener la lista de
         * docentes registrados, y luego convierte esos datos en una lista de objetos
         * DirectorAndCodirectorResponseDto que contienen
         * la información relevante de los docentes.
         * 
         * @return una lista de objetos {@link DirectorAndCodirectorResponseDto} que
         *         contienen la información de los docentes disponibles.
         * @throws InformationException si no se encuentran docentes registrados.
         */
        @Override
        @Transactional(readOnly = true)
        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector() {
                List<DirectorAndCodirectorResponseDto> directoresYCodirectores = new ArrayList<>();

                // Obtiene la lista de docentes a través de un cliente externo
                List<DocenteResponseDto> docentes = archivoClient.listarDocentesRes();

                // Verifica si la lista de docentes está vacía y lanza una excepción si es así
                if (docentes.isEmpty()) {
                        throw new InformationException("No hay docentes registrados");
                }

                // Transforma la lista de docentes en objetos DirectorAndCodirectorResponseDto
                List<DirectorAndCodirectorResponseDto> directoresYCodirectoresConvertidos = docentes.stream()
                                .map(docente -> new DirectorAndCodirectorResponseDto(
                                                docente.getId(),
                                                docente.getPersona().getTipoIdentificacion(),
                                                docente.getPersona().getIdentificacion(),
                                                docente.getPersona().getNombre(),
                                                docente.getPersona().getApellido()))
                                .collect(Collectors.toList());

                // Añade todos los docentes transformados a la lista principal
                directoresYCodirectores.addAll(directoresYCodirectoresConvertidos);

                return directoresYCodirectores;
        }

        /**
         * Método para insertar la información de un docente en relación con la
         * generación de resolución para un trabajo de grado. Se valida la información
         * proporcionada, incluyendo los enlaces y la asignación de director y
         * codirector.
         * También se gestionan los archivos asociados.
         * 
         * @param idTrabajoGrado          el identificador del trabajo de grado.
         * @param generacionResolucionDto objeto que contiene la información de la
         *                                generación de resolución.
         * @param result                  resultado de la validación de los campos.
         * @return un objeto {@link GeneracionResolucionDocenteResponseDto} que contiene
         *         la información del docente y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
         * @throws InformationException      si no se permite registrar la información o
         *                                   si se intenta registrar
         *                                   al mismo docente como director y
         *                                   codirector.
         */
        @Override
        @Transactional
        public GeneracionResolucionDocenteResponseDto insertarInformacionDocente(
                        Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de los enlaces proporcionados
                validarLink(generacionResolucionDto.getLinkAnteproyectoFinal());
                validarLink(generacionResolucionDto.getLinkSolicitudComite());

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 7 && trabajoGrado.getNumeroEstado() != 17) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Validación para evitar que director y codirector sean la misma persona
                if (generacionResolucionDto.getIdDirector().equals(generacionResolucionDto.getIdCodirector())) {
                        throw new InformationException(
                                        "No se permite registrar al mismo docente como director y codirector");
                }

                // Validación de la existencia del director y codirector
                archivoClient.obtenerDocentePorId(generacionResolucionDto.getIdDirector());
                archivoClient.obtenerDocentePorId(generacionResolucionDto.getIdCodirector());

                // Convertir DTO a entidad y asignar director y codirector
                GeneracionResolucion generacionResolucion = generacionResolucionMapper
                                .toEntity(generacionResolucionDto);
                generacionResolucion.setDirector(generacionResolucionDto.getIdDirector());
                generacionResolucion.setCodirector(generacionResolucionDto.getIdCodirector());

                // Identificación de la ruta del archivo
                String rutaArchivo = identificarRutaArchivo(trabajoGrado);

                // Asignación del trabajo de grado y actualización de su estado
                generacionResolucion.setTrabajoGrado(trabajoGrado);
                trabajoGrado.setGeneracionResolucion(generacionResolucion);
                trabajoGrado.setNumeroEstado(18);

                // Guardar los archivos asociados y actualizar los enlaces en la entidad
                generacionResolucion.setLinkAnteproyectoFinal(FilesUtilities.guardarArchivoNew2(
                                rutaArchivo, generacionResolucion.getLinkAnteproyectoFinal()));
                generacionResolucion.setLinkSolicitudComite(FilesUtilities.guardarArchivoNew2(
                                rutaArchivo, generacionResolucion.getLinkSolicitudComite()));

                // Guardar la entidad en el repositorio
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta
                GeneracionResolucionDocenteResponseDto respuestaDto = generacionResolucionResponseMapper
                                .toDocenteDto(generacionResolucionGuardada);
                respuestaDto.setTitulo(trabajoGrado.getTitulo());

                return respuestaDto;
        }

        /**
         * Método para insertar la información del coordinador en la fase 1 de la
         * generación de resolución
         * para un trabajo de grado. Se valida la información proporcionada, incluyendo
         * el concepto de los
         * documentos del coordinador y el envío de correos electrónicos si es
         * necesario.
         * 
         * @param idTrabajoGrado          el identificador del trabajo de grado.
         * @param generacionResolucionDto objeto que contiene la información de la
         *                                generación de resolución
         *                                para la fase 1 del coordinador.
         * @param bindingResult           resultado de la validación de los campos.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase1ResponseDto} que
         *         contiene la información del
         *         coordinador en la fase 1 y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información,
         *                                   si faltan atributos o si se
         *                                   envían atributos no permitidos.
         */
        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase1ResponseDto insertarInformacionCoordinadorFase1(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos en función del concepto de verificación
                if (generacionResolucionDto.getEnvioEmail() == null
                                && generacionResolucionDto.getConceptoDocumentosCoordinador()
                                                .equals(ConceptoVerificacion.RECHAZADO)) {
                        throw new InformationException("Faltan atributos para el registro");
                }

                if (generacionResolucionDto.getEnvioEmail() != null
                                && generacionResolucionDto.getConceptoDocumentosCoordinador()
                                                .equals(ConceptoVerificacion.ACEPTADO)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 18) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                // Actualización del estado del trabajo de grado en función del concepto del
                // coordinador
                if (generacionResolucionDto.getConceptoDocumentosCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        trabajoGrado.setNumeroEstado(20);
                } else {
                        // Envío de correos electrónicos de corrección si el concepto es rechazado
                        ArrayList<String> correos = new ArrayList<>();
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(19);
                }

                // Actualizar el concepto de los documentos del coordinador en la generación de
                // resolución
                generacionResolucion.setConceptoDocumentosCoordinador(
                                generacionResolucionDto.getConceptoDocumentosCoordinador());

                // Guardar los cambios en la generación de resolución
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta
                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucionGuardada);
        }

        /**
         * Método para insertar la información del coordinador en la fase 2 de la
         * generación de resolución para un trabajo de grado. Se valida la información
         * proporcionada, incluyendo las fechas de actas, los conceptos del comité
         * y el envío de correos electrónicos si es necesario.
         * 
         * @param idTrabajoGrado          el identificador del trabajo de grado.
         * @param generacionResolucionDto objeto que contiene la información de la
         *                                generación de resolución
         *                                para la fase 2 del coordinador.
         * @param bindingResult           resultado de la validación de los campos.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase2ResponseDto} que
         *         contiene la información del
         *         coordinador en la fase 2 y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información,
         *                                   si se proporcionan atributos incorrectos
         *                                   o si el concepto ya es APROBADO.
         */
        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos en función del concepto del comité
                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && (generacionResolucionDto.getLinkSolicitudConsejo() != null ||
                                                generacionResolucionDto
                                                                .getObtenerDocumentosParaEnvioConsejo() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)
                                && (generacionResolucionDto.getLinkSolicitudConsejo() == null ||
                                                generacionResolucionDto
                                                                .getObtenerDocumentosParaEnvioConsejo() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                // Validación de la fecha del acta del comité
                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getFechaActa() != null
                                && generacionResolucionDto.getActaFechaRespuestaComite().get(0).getFechaActa()
                                                .isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha de registro del comité no puede ser mayor a la fecha actual.");
                }

                // Validación y procesamiento de documentos si el concepto es APROBADO
                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        validarLink(generacionResolucionDto.getLinkSolicitudConsejo());
                        ValidationUtils.validarBase64(generacionResolucionDto.getObtenerDocumentosParaEnvioConsejo()
                                        .getB64FormatoBEv1());
                        ValidationUtils.validarBase64(generacionResolucionDto.getObtenerDocumentosParaEnvioConsejo()
                                        .getB64FormatoBEv2());
                        ValidationUtils.validarBase64(generacionResolucionDto.getObtenerDocumentosParaEnvioConsejo()
                                        .getB64AnteproyectoFinal());
                        ValidationUtils.validarBase64(generacionResolucionDto.getObtenerDocumentosParaEnvioConsejo()
                                        .getB64SolicitudConsejo());
                }

                ArrayList<String> correos = new ArrayList<>();

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 20) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                // Verificación para evitar duplicados en el concepto de APROBADO
                for (RespuestaComiteGeneracionResolucion respuesta : generacionResolucion
                                .getActaFechaRespuestaComite()) {
                        if (respuesta.getConceptoComite().equals(Concepto.APROBADO)) {
                                throw new InformationException("El concepto ya es APROBADO");
                        }
                }

                // Procesamiento de correos y actualización del estado del trabajo de grado
                // según el concepto del comité
                if (generacionResolucionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        correos.add(Constants.correoConsejo);
                        Map<String, Object> documentosParaConsejo = generacionResolucionDto
                                        .getObtenerDocumentosParaEnvioConsejo().getDocumentos();
                        envioCorreos.enviarCorreoConAnexos(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje(),
                                        documentosParaConsejo);
                        String rutaArchivo = identificarRutaArchivo(trabajoGrado);
                        generacionResolucionDto.setLinkSolicitudConsejo(
                                        FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                        generacionResolucionDto.getLinkSolicitudConsejo()));
                        trabajoGrado.setNumeroEstado(22);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        generacionResolucionDto.getEnvioEmail().getAsunto(),
                                        generacionResolucionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(21);
                }

                // Agregar la información del coordinador de la fase 2 a la generación de
                // resolución
                agregarInformacionCoordinadorFase2(generacionResolucion, generacionResolucionDto);

                // Guardar los cambios en la generación de resolución
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta
                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucionGuardada);
        }

        /**
         * Método que agrega la información del coordinador en la fase 2 a la entidad de
         * generación de resolución.
         * Esta información incluye los detalles del acta de respuesta del comité y el
         * enlace a la solicitud del consejo.
         * 
         * @param generacionResolucion    la entidad de generación de resolución a la
         *                                que se le agregará la información.
         * @param generacionResolucionDto objeto que contiene la información de la
         *                                generación de resolución para la fase 2.
         */
        private void agregarInformacionCoordinadorFase2(
                        GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto) {

                // Crear una nueva respuesta del comité con la información proporcionada en el
                // DTO
                RespuestaComiteGeneracionResolucion respuestaComite = RespuestaComiteGeneracionResolucion.builder()
                                .conceptoComite(generacionResolucionDto.getActaFechaRespuestaComite().get(0)
                                                .getConceptoComite())
                                .numeroActa(generacionResolucionDto.getActaFechaRespuestaComite().get(0)
                                                .getNumeroActa())
                                .fechaActa(generacionResolucionDto.getActaFechaRespuestaComite().get(0).getFechaActa())
                                .generacionResolucion(generacionResolucion)
                                .build();

                // Inicializar la lista de respuestas del comité si está vacía
                if (generacionResolucion.getActaFechaRespuestaComite() == null) {
                        generacionResolucion.setActaFechaRespuestaComite(new ArrayList<>());
                }

                // Agregar la nueva respuesta del comité a la lista existente
                generacionResolucion.getActaFechaRespuestaComite().add(respuestaComite);

                // Establecer el enlace a la solicitud del consejo en la generación de
                // resolución
                generacionResolucion.setLinkSolicitudConsejo(generacionResolucionDto.getLinkSolicitudConsejo());
        }

        /**
         * Método para insertar la información del coordinador en la fase 3 de la
         * generación de resolución para un trabajo de grado. Se valida la
         * información proporcionada, incluyendo el enlace del oficio
         * del consejo, y se actualiza el estado del trabajo de grado.
         * 
         * @param idTrabajoGrado          el identificador del trabajo de grado.
         * @param generacionResolucionDto objeto que contiene la información de la
         *                                generación de resolución
         *                                para la fase 3 del coordinador.
         * @param bindingResult           resultado de la validación de los campos.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase3ResponseDto} que
         *         contiene la información del
         *         coordinador en la fase 3 y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado.
         */
        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase3ResponseDto insertarInformacionCoordinadorFase3(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación del enlace del oficio del consejo
                validarLink(generacionResolucionDto.getLinkOficioConsejo());

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 22) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                // Actualizar el estado del trabajo de grado
                trabajoGrado.setNumeroEstado(23);

                // Identificar el directorio para guardar el archivo
                String directorioArchivos = identificarRutaArchivo(trabajoGrado);

                // Guardar el archivo del oficio del consejo y actualizar el enlace en el DTO
                generacionResolucionDto.setLinkOficioConsejo(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                generacionResolucionDto.getLinkOficioConsejo()));

                // Agregar la información de la fase 3 del coordinador a la generación de
                // resolución
                agregarInformacionCoordinadorFase3(generacionResolucion, generacionResolucionDto);

                // Guardar los cambios en la generación de resolución
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta
                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucionGuardada);
        }

        /**
         * Método que agrega la información del coordinador en la fase 3 a la entidad de
         * generación de resolución.
         * Esta información incluye el número y la fecha del acta del consejo, así como
         * el enlace al oficio del consejo.
         * 
         * @param generacionResolucion    la entidad de generación de resolución a la
         *                                que se le agregará la información.
         * @param generacionResolucionDto objeto que contiene la información de la
         *                                generación de resolución para la fase 3.
         */
        private void agregarInformacionCoordinadorFase3(
                        GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto) {

                // Asignar el número y la fecha del acta del consejo
                generacionResolucion.setNumeroActaConsejo(generacionResolucionDto.getNumeroActaConsejo());
                generacionResolucion.setFechaActaConsejo(generacionResolucionDto.getFechaActaConsejo());

                // Asignar el enlace al oficio del consejo
                generacionResolucion.setLinkOficioConsejo(generacionResolucionDto.getLinkOficioConsejo());
        }

        /**
         * Método que obtiene la información de la generación de resolución para un
         * trabajo de grado de un docente, incluyendo los detalles del director y
         * codirector.
         * Esta información se devuelve en un objeto
         * {@link GeneracionResolucionDocenteListDto}.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link GeneracionResolucionDocenteListDto} que contiene la
         *         información del
         *         director, codirector y enlaces relevantes para la generación de
         *         resolución.
         * @throws ResourceNotFoundException si no se encuentra la generación de
         *                                   resolución asociada al trabajo de grado.
         */
        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionDocenteListDto listarInformacionDocente(Long idTrabajoGrado) {

                // Buscar la generación de resolución por el ID del trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generacion de resolucion con ID trabajo de grado " + idTrabajoGrado
                                                                + " no encontrado"));

                // Buscar el trabajo de grado asociado
                Optional<TrabajoGrado> trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucion.getTrabajoGrado().getId());

                // Obtener información del director y armar el mapa con los datos
                DocenteResponseDto director = archivoClient.obtenerDocentePorId(generacionResolucion.getDirector());
                String nombreDirector = director.getPersona().getNombre() + " " + director.getPersona().getApellido();
                Map<String, String> directorMap = new HashMap<>();
                directorMap.put("id", director.getId().toString());
                directorMap.put("nombres", nombreDirector);

                // Obtener información del codirector y armar el mapa con los datos
                DocenteResponseDto codirector = archivoClient.obtenerDocentePorId(generacionResolucion.getCodirector());
                String nombreCodirector = codirector.getPersona().getNombre() + " "
                                + codirector.getPersona().getApellido();
                Map<String, String> codirectorMap = new HashMap<>();
                codirectorMap.put("id", codirector.getId().toString());
                codirectorMap.put("nombres", nombreCodirector);

                // Crear el DTO de respuesta y asignar la información obtenida
                GeneracionResolucionDocenteListDto generacionResolucionDocenteListDto = new GeneracionResolucionDocenteListDto();
                generacionResolucionDocenteListDto.setId(generacionResolucion.getId());
                generacionResolucionDocenteListDto.setTitulo(trabajoGrado.get().getTitulo());
                generacionResolucionDocenteListDto.setDirector(directorMap);
                generacionResolucionDocenteListDto.setCodirector(codirectorMap);
                generacionResolucionDocenteListDto
                                .setLinkAnteproyectoFinal(generacionResolucion.getLinkAnteproyectoFinal());
                generacionResolucionDocenteListDto
                                .setLinkSolicitudComite(generacionResolucion.getLinkSolicitudComite());

                return generacionResolucionDocenteListDto;
        }

        /**
         * Método que obtiene la información del coordinador en la fase 1 para un
         * trabajo de grado, devolviendo un objeto
         * {@link GeneracionResolucionCoordinadorFase1ResponseDto}
         * con los detalles.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase1ResponseDto} que
         *         contiene la información
         *         del coordinador en la fase 1 de la generación de resolución.
         * @throws ResourceNotFoundException si no se encuentra la generación de
         *                                   resolución asociada al trabajo de grado.
         * @throws InformationException      si no se han registrado datos del
         *                                   coordinador.
         */
        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(Long idTrabajoGrado) {

                // Buscar la generación de resolución por el ID del trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si no se han registrado datos del coordinador
                if (generacionResolucion.getConceptoDocumentosCoordinador() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Convertir la entidad en el DTO de respuesta para la fase 1 del coordinador
                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucion);
        }

        /**
         * Método que obtiene la información del coordinador en la fase 2 para un
         * trabajo de grado,
         * devolviendo un objeto {@link GeneracionResolucionCoordinadorFase2ResponseDto}
         * con los detalles.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase2ResponseDto} que
         *         contiene la información
         *         del coordinador en la fase 2 de la generación de resolución.
         * @throws ResourceNotFoundException si no se encuentra la generación de
         *                                   resolución asociada al trabajo de grado.
         * @throws InformationException      si no se han registrado datos del
         *                                   coordinador en la fase 2.
         */
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {

                // Buscar la generación de resolución por el ID del trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si no se han registrado datos del comité o del consejo
                boolean actaFechaRespuestaComiteEmpty = generacionResolucion.getActaFechaRespuestaComite() == null ||
                                generacionResolucion.getActaFechaRespuestaComite().isEmpty();
                if (actaFechaRespuestaComiteEmpty && generacionResolucion.getLinkSolicitudConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Convertir la entidad en el DTO de respuesta para la fase 2 del coordinador
                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucion);
        }

        /**
         * Método que obtiene la información del coordinador en la fase 3 para un
         * trabajo de grado,
         * devolviendo un objeto {@link GeneracionResolucionCoordinadorFase3ResponseDto}
         * con los detalles.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase3ResponseDto} que
         *         contiene la información
         *         del coordinador en la fase 3 de la generación de resolución.
         * @throws ResourceNotFoundException si no se encuentra la generación de
         *                                   resolución asociada al trabajo de grado.
         * @throws InformationException      si no se han registrado datos del
         *                                   coordinador en la fase 3.
         */
        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(Long idTrabajoGrado) {

                // Buscar la generación de resolución por el ID del trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si no se han registrado datos del consejo
                if (generacionResolucion.getNumeroActaConsejo() == null &&
                                generacionResolucion.getFechaActaConsejo() == null &&
                                generacionResolucion.getLinkOficioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Convertir la entidad en el DTO de respuesta para la fase 3 del coordinador
                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucion);
        }

        /**
         * Método para actualizar la información del docente en la generación de
         * resolución para un trabajo de grado.
         * Se valida la información proporcionada, incluyendo los enlaces de los
         * documentos y la asignación de director y codirector.
         * 
         * @param idTrabajoGrado                 el identificador del trabajo de grado.
         * @param generacionResolucionDocenteDto objeto que contiene la información
         *                                       actualizada de la generación de
         *                                       resolución docente.
         * @param bindingResult                  resultado de la validación de los
         *                                       campos.
         * @return un objeto {@link GeneracionResolucionDocenteResponseDto} que contiene
         *         la información actualizada del docente
         *         y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información o
         *                                   si se intenta registrar al mismo docente
         *                                   como director y codirector.
         */
        @Override
        public GeneracionResolucionDocenteResponseDto actualizarInformacionDocente(
                        Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validar la existencia del director y codirector
                archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdDirector());
                archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdCodirector());

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 18 && trabajoGrado.getNumeroEstado() != 19
                                && trabajoGrado.getNumeroEstado() != 21) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Validación para evitar que director y codirector sean la misma persona
                if (generacionResolucionDocenteDto.getIdDirector()
                                .equals(generacionResolucionDocenteDto.getIdCodirector())) {
                        throw new InformationException(
                                        "No se permite registrar al mismo docente como director y codirector");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generación de resolución con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificarRutaArchivo(trabajoGrado);

                // Actualizar el estado del trabajo de grado
                trabajoGrado.setNumeroEstado(18);

                // Actualizar enlaces de los documentos si han cambiado
                if (!generacionResolucionDocenteDto.getLinkAnteproyectoFinal()
                                .equals(generacionResolucion.getLinkAnteproyectoFinal())) {
                        validarLink(generacionResolucionDocenteDto.getLinkAnteproyectoFinal());
                        generacionResolucionDocenteDto.setLinkAnteproyectoFinal(
                                        FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                        generacionResolucionDocenteDto.getLinkAnteproyectoFinal()));
                        FilesUtilities.deleteFileExample(generacionResolucion.getLinkAnteproyectoFinal());
                }
                if (!generacionResolucionDocenteDto.getLinkSolicitudComite()
                                .equals(generacionResolucion.getLinkSolicitudComite())) {
                        validarLink(generacionResolucionDocenteDto.getLinkSolicitudComite());
                        generacionResolucionDocenteDto.setLinkSolicitudComite(
                                        FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                        generacionResolucionDocenteDto.getLinkSolicitudComite()));
                        FilesUtilities.deleteFileExample(generacionResolucion.getLinkSolicitudComite());
                }

                // Actualizar los valores de la generación de resolución
                updateExamenValoracionDocenteValues(generacionResolucion, generacionResolucionDocenteDto, trabajoGrado);

                // Guardar los cambios en la generación de resolución
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta
                GeneracionResolucionDocenteResponseDto generacionResolucionDocenteResponseDto = generacionResolucionResponseMapper
                                .toDocenteDto(generacionResolucionGuardada);
                generacionResolucionDocenteResponseDto.setTitulo(trabajoGrado.getTitulo());

                return generacionResolucionDocenteResponseDto;
        }

        /**
         * Método auxiliar para actualizar los valores de la generación de resolución
         * docente con los datos proporcionados.
         * 
         * @param generacionResolucion           la entidad de generación de resolución
         *                                       que se actualizará.
         * @param generacionResolucionDocenteDto objeto que contiene la información
         *                                       actualizada de la generación de
         *                                       resolución docente.
         * @param trabajoGrado                   el trabajo de grado asociado.
         */
        private void updateExamenValoracionDocenteValues(
                        GeneracionResolucion generacionResolucion,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto,
                        TrabajoGrado trabajoGrado) {

                // Validar la existencia del director y codirector
                archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdDirector());
                archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdCodirector());

                // Resetear el concepto de documentos del coordinador
                generacionResolucion.setConceptoDocumentosCoordinador(null);

                // Actualizar director y codirector
                generacionResolucion.setDirector(generacionResolucionDocenteDto.getIdDirector());
                generacionResolucion.setCodirector(generacionResolucionDocenteDto.getIdCodirector());

                // Actualizar enlaces de documentos
                generacionResolucion
                                .setLinkAnteproyectoFinal(generacionResolucionDocenteDto.getLinkAnteproyectoFinal());
                generacionResolucion.setLinkSolicitudComite(generacionResolucionDocenteDto.getLinkSolicitudComite());
        }

        /**
         * Método para actualizar la información del coordinador en la fase 1 de la
         * generación de resolución para un trabajo de grado.
         * Se valida la información proporcionada, incluyendo el concepto de los
         * documentos del coordinador y el envío de correos electrónicos.
         * 
         * @param idTrabajoGrado                 el identificador del trabajo de grado.
         * @param generacionResolucionDocenteDto objeto que contiene la información
         *                                       actualizada de la fase 1 del
         *                                       coordinador.
         * @param bindingResult                  resultado de la validación de los
         *                                       campos.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase1ResponseDto} que
         *         contiene la información actualizada
         *         del coordinador en la fase 1 y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información o
         *                                   si faltan atributos necesarios.
         */
        @Override
        public GeneracionResolucionCoordinadorFase1ResponseDto actualizarInformacionCoordinadorFase1(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDocenteDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos según el concepto de verificación
                if (generacionResolucionDocenteDto.getEnvioEmail() == null &&
                                generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                                .equals(ConceptoVerificacion.RECHAZADO)) {
                        throw new InformationException("Faltan atributos para el registro");
                }

                if (generacionResolucionDocenteDto.getEnvioEmail() != null &&
                                generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                                .equals(ConceptoVerificacion.ACEPTADO)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 18 && trabajoGrado.getNumeroEstado() != 19 &&
                                trabajoGrado.getNumeroEstado() != 20) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generación de resolución con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                // Verificar si no se han registrado datos del coordinador
                if (generacionResolucion.getConceptoDocumentosCoordinador() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Lista de correos para notificaciones
                ArrayList<String> correos = new ArrayList<>();

                // Actualización del estado del trabajo de grado y envío de correos si cambia el
                // concepto de verificación
                if (!generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                .equals(generacionResolucion.getConceptoDocumentosCoordinador())) {

                        if (generacionResolucionDocenteDto.getConceptoDocumentosCoordinador()
                                        .equals(ConceptoVerificacion.RECHAZADO)) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                generacionResolucionDocenteDto.getEnvioEmail().getAsunto(),
                                                generacionResolucionDocenteDto.getEnvioEmail().getMensaje());
                                trabajoGrado.setNumeroEstado(19);
                        } else {
                                trabajoGrado.setNumeroEstado(20);
                        }
                }

                // Actualizar el concepto de los documentos del coordinador en la generación de
                // resolución
                generacionResolucion.setConceptoDocumentosCoordinador(
                                generacionResolucionDocenteDto.getConceptoDocumentosCoordinador());

                // Guardar los cambios en la generación de resolución
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta para la fase 1 del
                // coordinador
                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucionGuardada);
        }

        /**
         * Método para actualizar la información del coordinador en la fase 2 de la
         * generación de resolución para un trabajo de grado.
         * Se valida la información proporcionada, incluyendo los conceptos del comité y
         * la documentación asociada al consejo.
         * 
         * @param idTrabajoGrado                          el identificador del trabajo
         *                                                de grado.
         * @param generacionResolucionCoordinadorFase2Dto objeto que contiene la
         *                                                información actualizada de la
         *                                                fase 2 del coordinador.
         * @param bindingResult                           resultado de la validación de
         *                                                los campos.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase2ResponseDto} que
         *         contiene la información actualizada
         *         del coordinador en la fase 2 y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información o
         *                                   si se proporcionan atributos incorrectos.
         */
        @Override
        public GeneracionResolucionCoordinadorFase2ResponseDto actualizarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos en función del concepto del comité
                if (generacionResolucionCoordinadorFase2Dto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && (generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo() != null ||
                                                generacionResolucionCoordinadorFase2Dto
                                                                .getObtenerDocumentosParaEnvioConsejo() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (generacionResolucionCoordinadorFase2Dto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)
                                && (generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo() == null ||
                                                generacionResolucionCoordinadorFase2Dto
                                                                .getObtenerDocumentosParaEnvioConsejo() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 20 && trabajoGrado.getNumeroEstado() != 21 &&
                                trabajoGrado.getNumeroEstado() != 22) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generación de resolución con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                boolean actaFechaRespuestaComiteEmpty = generacionResolucion.getActaFechaRespuestaComite() == null ||
                                generacionResolucion.getActaFechaRespuestaComite().isEmpty();
                if (actaFechaRespuestaComiteEmpty && generacionResolucion.getLinkSolicitudConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificarRutaArchivo(trabajoGrado);

                GeneracionResolucion generacionResolucionActualizada = null;
                List<RespuestaComiteGeneracionResolucion> respuestaComiteList = generacionResolucionRepository
                                .findRespuestaComiteByGeneracionResolucionId(generacionResolucion.getId());
                RespuestaComiteGeneracionResolucion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                // Validar cambios en el concepto del comité y realizar acciones
                // correspondientes
                if (ultimoRegistro != null &&
                                !ultimoRegistro.getConceptoComite().equals(generacionResolucionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0).getConceptoComite())) {

                        ArrayList<String> correos = new ArrayList<>();

                        // Si pasa de aprobado a no aprobado
                        if (!generacionResolucionCoordinadorFase2Dto.getActaFechaRespuestaComite().get(0)
                                        .getConceptoComite().equals(Concepto.APROBADO)) {
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                generacionResolucionCoordinadorFase2Dto.getEnvioEmail().getAsunto(),
                                                generacionResolucionCoordinadorFase2Dto.getEnvioEmail().getMensaje());
                                FilesUtilities.deleteFileExample(generacionResolucion.getLinkSolicitudConsejo());
                                trabajoGrado.setNumeroEstado(21);
                        } else {
                                // Validar y procesar la documentación si el concepto es aprobado
                                validarLink(generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo());
                                ValidationUtils.validarBase64(generacionResolucionCoordinadorFase2Dto
                                                .getObtenerDocumentosParaEnvioConsejo().getB64FormatoBEv1());
                                ValidationUtils.validarBase64(generacionResolucionCoordinadorFase2Dto
                                                .getObtenerDocumentosParaEnvioConsejo().getB64FormatoBEv2());
                                ValidationUtils.validarBase64(generacionResolucionCoordinadorFase2Dto
                                                .getObtenerDocumentosParaEnvioConsejo().getB64AnteproyectoFinal());
                                ValidationUtils.validarBase64(generacionResolucionCoordinadorFase2Dto
                                                .getObtenerDocumentosParaEnvioConsejo().getB64SolicitudConsejo());
                                correos.add(Constants.correoConsejo);
                                Map<String, Object> documentosParaConsejo = generacionResolucionCoordinadorFase2Dto
                                                .getObtenerDocumentosParaEnvioConsejo().getDocumentos();
                                envioCorreos.enviarCorreoConAnexos(correos,
                                                generacionResolucionCoordinadorFase2Dto.getEnvioEmail().getAsunto(),
                                                generacionResolucionCoordinadorFase2Dto.getEnvioEmail().getMensaje(),
                                                documentosParaConsejo);
                                generacionResolucionCoordinadorFase2Dto.setLinkSolicitudConsejo(
                                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                                generacionResolucionCoordinadorFase2Dto
                                                                                .getLinkSolicitudConsejo()));
                                trabajoGrado.setNumeroEstado(22);
                        }
                } else {
                        // Verificar si el enlace de solicitud del consejo ha cambiado y actualizarlo
                        if (generacionResolucion != null
                                        && generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo() != null) {
                                if (!generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo()
                                                .equals(generacionResolucion.getLinkSolicitudConsejo())) {
                                        validarLink(generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo());
                                        generacionResolucionCoordinadorFase2Dto.setLinkSolicitudConsejo(
                                                        FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                                        generacionResolucionCoordinadorFase2Dto
                                                                                        .getLinkSolicitudConsejo()));
                                        FilesUtilities.deleteFileExample(
                                                        generacionResolucion.getLinkSolicitudConsejo());
                                }
                        }
                }

                // Actualizar los valores de la generación de resolución
                updateExamenValoracionCoordinadorValues(generacionResolucion, generacionResolucionCoordinadorFase2Dto,
                                trabajoGrado);

                // Guardar los cambios en la generación de resolución
                generacionResolucionActualizada = generacionResolucionRepository.save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta para la fase 2 del
                // coordinador
                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucionActualizada);
        }

        /**
         * Método auxiliar para actualizar los valores de la generación de resolución en
         * la fase 2 del coordinador,
         * incluyendo los detalles del acta del comité y la solicitud del consejo.
         * 
         * @param generacionResolucion                    la entidad de generación de
         *                                                resolución que se actualizará.
         * @param generacionResolucionCoordinadorFase2Dto objeto que contiene la
         *                                                información actualizada de la
         *                                                fase 2 del coordinador.
         * @param trabajoGrado                            el trabajo de grado asociado.
         */
        private void updateExamenValoracionCoordinadorValues(
                        GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto,
                        TrabajoGrado trabajoGrado) {

                RespuestaComiteGeneracionResolucion ultimoRegistro = respuestaComiteGeneracionResolucionRepository
                                .findFirstByGeneracionResolucionIdOrderByIdDesc(generacionResolucion.getId());

                if (ultimoRegistro != null) {
                        ultimoRegistro.setNumeroActa(generacionResolucionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getNumeroActa());
                        ultimoRegistro.setFechaActa(generacionResolucionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getFechaActa());
                        ultimoRegistro.setConceptoComite(generacionResolucionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getConceptoComite());

                        respuestaComiteGeneracionResolucionRepository.save(ultimoRegistro);
                }

                generacionResolucion.setLinkSolicitudConsejo(
                                generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejo());
        }

        /**
         * Método para actualizar la información del coordinador en la fase 3 de la
         * generación de resolución para un trabajo de grado.
         * Se valida la información proporcionada, incluyendo el enlace del oficio del
         * consejo y se actualizan los datos del acta del consejo.
         * 
         * @param idTrabajoGrado                          el identificador del trabajo
         *                                                de grado.
         * @param generacionResolucionCoordinadorFase3Dto objeto que contiene la
         *                                                información actualizada de la
         *                                                fase 3 del coordinador.
         * @param bindingResult                           resultado de la validación de
         *                                                los campos.
         * @return un objeto {@link GeneracionResolucionCoordinadorFase3ResponseDto} que
         *         contiene la información actualizada
         *         del coordinador en la fase 3 y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la generación de
         *                                   resolución no se encuentran.
         * @throws InformationException      si no se permite registrar la información o
         *                                   si no se han registrado los datos
         *                                   requeridos.
         */
        @Override
        public GeneracionResolucionCoordinadorFase3ResponseDto actualizarInformacionCoordinadorFase3(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionCoordinadorFase3Dto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validar el enlace del oficio del consejo
                validarLink(generacionResolucionCoordinadorFase3Dto.getLinkOficioConsejo());

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 21 && trabajoGrado.getNumeroEstado() != 23) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la generación de resolución asociada al trabajo de grado
                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Generación de resolución con id: "
                                                                + trabajoGrado.getGeneracionResolucion().getId()
                                                                + " no encontrada"));

                // Verificar si no se han registrado datos del acta del consejo
                if (generacionResolucion.getNumeroActaConsejo() == null
                                && generacionResolucion.getFechaActaConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Actualizar el estado del trabajo de grado
                trabajoGrado.setNumeroEstado(23);

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificarRutaArchivo(trabajoGrado);

                // Actualizar el enlace del oficio del consejo si ha cambiado
                if (!generacionResolucionCoordinadorFase3Dto.getLinkOficioConsejo()
                                .equals(generacionResolucion.getLinkOficioConsejo())) {
                        validarLink(generacionResolucionCoordinadorFase3Dto.getLinkOficioConsejo());
                        generacionResolucionCoordinadorFase3Dto.setLinkOficioConsejo(
                                        FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                        generacionResolucionCoordinadorFase3Dto
                                                                        .getLinkOficioConsejo()));
                        FilesUtilities.deleteFileExample(generacionResolucion.getLinkOficioConsejo());
                }

                // Actualizar los datos del acta del consejo en la generación de resolución
                generacionResolucion
                                .setNumeroActaConsejo(generacionResolucionCoordinadorFase3Dto.getNumeroActaConsejo());
                generacionResolucion.setFechaActaConsejo(generacionResolucionCoordinadorFase3Dto.getFechaActaConsejo());
                generacionResolucion
                                .setLinkOficioConsejo(generacionResolucionCoordinadorFase3Dto.getLinkOficioConsejo());

                // Guardar los cambios en la generación de resolución
                GeneracionResolucion generacionResolucionGuardada = generacionResolucionRepository
                                .save(generacionResolucion);

                // Convertir la entidad guardada en el DTO de respuesta para la fase 3 del
                // coordinador
                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucionGuardada);
        }

        /**
         * Método que genera la ruta del archivo donde se guardarán los documentos del
         * estudiante en el proceso de generación de resolución.
         * La ruta se construye en función de la información del estudiante, la fecha
         * actual y el proceso específico.
         * 
         * @param trabajoGrado el trabajo de grado asociado al estudiante.
         * @return la ruta donde se almacenarán los documentos del proceso de generación
         *         de resolución.
         */
        private String identificarRutaArchivo(TrabajoGrado trabajoGrado) {
                EstudianteResponseDtoAll informacionEstudiante = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                String proceso = "Generacion_Resolucion";
                LocalDate fechaActual = LocalDate.now();
                int anio = fechaActual.getYear();
                int mes = fechaActual.getMonthValue();

                Long identificacionEstudiante = informacionEstudiante.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiante.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiante.getPersona().getApellido();
                String informacionEstudiante2 = identificacionEstudiante + "-" + nombreEstudiante + "_"
                                + apellidoEstudiante;

                // Construir la ruta del archivo
                String rutaCarpeta = anio + "/" + mes + "/" + informacionEstudiante2 + "/" + proceso;
                return rutaCarpeta;
        }

        /**
         * Método que valida el formato de un enlace y su contenido codificado en
         * base64.
         * 
         * @param link el enlace que se va a validar.
         * @throws ValidationException si el enlace no cumple con el formato o contenido
         *                             esperado.
         */
        private void validarLink(String link) {
                ValidationUtils.validarFormatoLink(link);
                String base64 = link.substring(link.indexOf('-') + 1);
                ValidationUtils.validarBase64(base64);
        }

}