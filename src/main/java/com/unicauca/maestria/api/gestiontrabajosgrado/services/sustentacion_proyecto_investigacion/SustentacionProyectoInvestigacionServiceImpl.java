package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ValidationUtils;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.AnexoSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.RespuestaComiteSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.EmpresaSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.AnexoSustentacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionListDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSustentacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

        private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        private final SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        private final RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        private final AnexosSustentacionRepository anexosSustentacionRepository;
        private final AnexoSustentacionMapper anexoSustentacionMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final TiemposPendientesRepository tiemposPendientesRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientEgresados archivoClientEgresados;

        @Autowired
        private EnvioCorreos envioCorreos;

        /**
         * Método que obtiene una lista de todos los docentes registrados en el sistema.
         * Si no hay docentes registrados, lanza una excepción.
         * 
         * @return una lista de objetos {@link DocenteInfoDto} que contienen la
         *         información de los docentes.
         * @throws InformationException si no se encuentran docentes registrados.
         */
        @Override
        @Transactional(readOnly = true)
        public List<DocenteInfoDto> listarDocentes() {
                List<DocenteResponseDto> listadoDocentes = archivoClient.listarDocentesRes();
                if (listadoDocentes.isEmpty()) {
                        throw new InformationException("No hay docentes registrados");
                }
                List<DocenteInfoDto> docentes = listadoDocentes.stream()
                                .map(docente -> new DocenteInfoDto(
                                                docente.getId(),
                                                docente.getPersona().getNombre(),
                                                docente.getPersona().getApellido(),
                                                docente.getPersona().getCorreoElectronico(),
                                                "Universidad del Cauca"))
                                .collect(Collectors.toList());
                return docentes;
        }

        /**
         * Método que obtiene una lista de todos los expertos registrados en el sistema.
         * Si no hay expertos registrados, lanza una excepción.
         * 
         * @return una lista de objetos {@link ExpertoInfoDto} que contienen la
         *         información de los expertos.
         * @throws InformationException si no se encuentran expertos registrados.
         */
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

        /**
         * Método que obtiene la información de un docente específico por su ID.
         * 
         * @param id el identificador del docente.
         * @return un objeto {@link DocenteInfoDto} que contiene la información del
         *         docente.
         */
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

        /**
         * Método que obtiene la información de un experto específico por su ID.
         * 
         * @param id el identificador del experto.
         * @return un objeto {@link ExpertoInfoDto} que contiene la información del
         *         experto.
         */
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

        /**
         * Método para insertar la información del docente relacionada con la
         * sustentación de un proyecto de investigación.
         * Se valida la información proporcionada, incluyendo los enlaces de los
         * documentos y los anexos, y se actualizan los datos del trabajo de grado.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del proyecto de investigación.
         * @param bindingResult   resultado de la validación de los campos.
         * @return un objeto {@link SustentacionTrabajoInvestigacionDocenteResponseDto}
         *         que contiene la información registrada
         *         de la sustentación y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado.
         */
        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validar enlaces de monografía y formato F
                validarLink(sustentacionDto.getLinkMonografia());
                validarLink(sustentacionDto.getLinkFormatoF());

                // Validar enlaces de anexos
                for (AnexoSustentacionDto anexo : sustentacionDto.getAnexos()) {
                        validarLink(anexo.getLinkAnexo());
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 23) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Validar la existencia del jurado interno y externo
                archivoClient.obtenerDocentePorId(sustentacionDto.getIdJuradoInterno());
                archivoClient.obtenerExpertoPorId(sustentacionDto.getIdJuradoExterno());

                // Convertir el DTO en la entidad correspondiente
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Asignar el trabajo de grado a la sustentación y actualizar el estado
                sustentacionProyectoInvestigacion.setTrabajoGrado(trabajoGrado);
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);
                trabajoGrado.setNumeroEstado(24);

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Guardar los enlaces de monografía y formato F
                sustentacionProyectoInvestigacion.setLinkMonografia(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionProyectoInvestigacion.getLinkMonografia()));
                sustentacionProyectoInvestigacion.setLinkFormatoF(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionProyectoInvestigacion.getLinkFormatoF()));

                // Guardar los anexos si existen
                if (sustentacionProyectoInvestigacion.getAnexos() != null) {
                        List<AnexoSustentacion> anexosActualizados = new ArrayList<>();
                        for (AnexoSustentacionDto anexoDto : sustentacionDto.getAnexos()) {
                                String rutaAnexo = FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                anexoDto.getLinkAnexo());
                                AnexoSustentacion anexo = new AnexoSustentacion();
                                anexo.setLinkAnexo(rutaAnexo);
                                anexo.setSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);
                                anexosActualizados.add(anexo);
                        }
                        sustentacionProyectoInvestigacion.setAnexos(anexosActualizados);
                }

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionResponseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionGuardada);

                return sustentacionResponseDto;
        }

        /**
         * Método para insertar la información del coordinador en la fase 1 de la
         * sustentación de un proyecto de investigación.
         * Se valida la información proporcionada, incluyendo los conceptos del
         * coordinador y los enlaces de los documentos asociados.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del coordinador en la fase 1.
         * @param bindingResult   resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase1ResponseDto} que contiene la
         *         información registrada
         *         del coordinador y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la sustentación no
         *                                   se encuentran.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado.
         */
        @Override
        @Transactional
        public STICoordinadorFase1ResponseDto insertarInformacionCoordinadoFase1(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos según el concepto de verificación
                if ((sustentacionDto.getEnvioEmail() == null
                                || sustentacionDto.getLinkEstudioHojaVidaAcademica() != null)
                                && sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.RECHAZADO)) {
                        throw new InformationException("Faltan atributos para el registro");
                }

                if ((sustentacionDto.getLinkEstudioHojaVidaAcademica() == null
                                || sustentacionDto.getEnvioEmail() != null)
                                && sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                // Validar enlace de estudio de hoja de vida académica si el concepto es
                // aceptado
                if (sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademica());
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 24) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Actualizar la información según el concepto del coordinador
                if (sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        String directorioArchivos = identificacionArchivo(trabajoGrado);
                        sustentacionProyectoInvestigacion
                                        .setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew2(
                                                        directorioArchivos,
                                                        sustentacionDto.getLinkEstudioHojaVidaAcademica()));
                        trabajoGrado.setNumeroEstado(26);
                } else {
                        ArrayList<String> correos = new ArrayList<>();
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmail().getAsunto(),
                                        sustentacionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(25);
                }

                // Actualizar el concepto del coordinador en la sustentación
                sustentacionProyectoInvestigacion.setConceptoCoordinador(sustentacionDto.getConceptoCoordinador());

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método para insertar la información del coordinador en la fase 2 de la
         * sustentación de un proyecto de investigación.
         * Se valida la información proporcionada, incluyendo los conceptos del comité y
         * los enlaces de los documentos asociados.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del coordinador en la fase 2.
         * @param bindingResult   resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase2ResponseDto} que contiene la
         *         información registrada del coordinador y el estado del trabajo de
         *         grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la sustentación no
         *                                   se encuentran.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o si se proporcionan atributos
         *                                   incorrectos.
         */
        @Override
        @Transactional
        public STICoordinadorFase2ResponseDto insertarInformacionCoordinadoFase2(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos en función del concepto del comité
                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && (sustentacionDto.getLinkFormatoG() != null
                                                || sustentacionDto.getInformacionEnvioConsejo() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals(Concepto.APROBADO)
                                && (sustentacionDto.getLinkFormatoG() == null
                                                || sustentacionDto.getInformacionEnvioConsejo() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                // Validación de la fecha del acta del comité
                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getFechaActa() != null
                                && sustentacionDto.getActaFechaRespuestaComite().get(0).getFechaActa()
                                                .isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha de registro del comité no puede ser mayor a la fecha actual.");
                }

                // Validación de documentos en caso de concepto aprobado
                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        validarLink(sustentacionDto.getLinkFormatoG());
                        List<String> anexos = sustentacionDto.getInformacionEnvioConsejo().getB64Anexos();
                        if (anexos != null && !anexos.isEmpty()) {
                                for (String anexo : anexos) {
                                        ValidationUtils.validarBase64(anexo);
                                }
                        }
                }

                // Inicializar la lista de correos para notificaciones
                ArrayList<String> correos = new ArrayList<>();

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 26) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentacion con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Verificar si el concepto del comité ya ha sido aprobado
                for (RespuestaComiteSustentacion respuesta : sustentacionProyectoInvestigacion
                                .getActaFechaRespuestaComite()) {
                        if (respuesta.getConceptoComite().equals(Concepto.APROBADO)) {
                                throw new InformationException("El concepto ya es APROBADO");
                        }
                }

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Procesar la información según el concepto del comité
                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.APROBADO)) {
                        correos.add(Constants.correoConsejo);
                        Map<String, Object> documentosParaConsejo = sustentacionDto.getInformacionEnvioConsejo()
                                        .getDocumentos();
                        envioCorreos.enviarCorreoConAnexos(correos, sustentacionDto.getEnvioEmail().getAsunto(),
                                        sustentacionDto.getEnvioEmail().getMensaje(), documentosParaConsejo);

                        sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(
                                        directorioArchivos, sustentacionDto.getLinkFormatoG()));

                        trabajoGrado.setNumeroEstado(28);
                } else {
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getCorreoUniversidad());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());
                        envioCorreos.enviarCorreosCorrecion(correos,
                                        sustentacionDto.getEnvioEmail().getAsunto(),
                                        sustentacionDto.getEnvioEmail().getMensaje());
                        trabajoGrado.setNumeroEstado(27);
                }

                // Agregar la información del coordinador en la fase 2
                agregarInformacionCoordinadorFase2(sustentacionProyectoInvestigacion, sustentacionDto);

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método auxiliar para agregar la información del coordinador en la fase 2 a la
         * entidad de sustentación del proyecto de investigación.
         * Esta información incluye los detalles del acta de respuesta del comité y el
         * enlace al formato G.
         * 
         * @param sustentacionProyectoInvestigacion                   la entidad de
         *                                                            sustentación del
         *                                                            proyecto de
         *                                                            investigación a la
         *                                                            que se le agregará
         *                                                            la información.
         * @param sustentacionTrabajoInvestigacionCoordinadorFase2Dto objeto que
         *                                                            contiene la
         *                                                            información de la
         *                                                            fase 2 del
         *                                                            coordinador.
         */
        private void agregarInformacionCoordinadorFase2(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto) {

                // Crear una nueva respuesta del comité con la información proporcionada en el
                // DTO
                RespuestaComiteSustentacion respuestaComite = RespuestaComiteSustentacion.builder()
                                .conceptoComite(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0).getConceptoComite())
                                .numeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0).getNumeroActa())
                                .fechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                                .getActaFechaRespuestaComite().get(0).getFechaActa())
                                .sustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion)
                                .build();

                // Inicializar la lista de respuestas del comité si está vacía
                if (sustentacionProyectoInvestigacion.getActaFechaRespuestaComite() == null) {
                        sustentacionProyectoInvestigacion.setActaFechaRespuestaComite(new ArrayList<>());
                }

                // Agregar la nueva respuesta del comité a la lista existente
                sustentacionProyectoInvestigacion.getActaFechaRespuestaComite().add(respuestaComite);

                // Asignar el enlace al formato G en la sustentación
                sustentacionProyectoInvestigacion
                                .setLinkFormatoG(sustentacionTrabajoInvestigacionCoordinadorFase2Dto.getLinkFormatoG());
        }

        /**
         * Método para insertar la información del coordinador en la fase 3 de la
         * sustentación de un proyecto de investigación.
         * Se valida la información proporcionada, incluyendo los conceptos de
         * aceptación de jurados y los enlaces de los documentos asociados.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del coordinador en la fase 3.
         * @param bindingResult   resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase3ResponseDto} que contiene la
         *         información registrada del coordinador y el estado del trabajo de
         *         grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la sustentación no
         *                                   se encuentran.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o si se proporcionan atributos
         *                                   incorrectos.
         */
        @Override
        @Transactional
        public STICoordinadorFase3ResponseDto insertarInformacionCoordinadoFase3(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de atributos en función del estado de aceptación de los jurados
                if (sustentacionDto.getJuradosAceptados().equals(ConceptoVerificacion.ACEPTADO)
                                && (!sustentacionDto.getIdJuradoInterno().equals("Sin cambios")
                                                || !sustentacionDto.getIdJuradoExterno().equals("Sin cambios"))) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getJuradosAceptados().equals(ConceptoVerificacion.RECHAZADO)
                                && (sustentacionDto.getIdJuradoInterno().equals("Sin cambios")
                                                || sustentacionDto.getIdJuradoExterno().equals("Sin cambios"))) {
                        throw new InformationException("Atributos incorrectos");
                }

                // Validación de la fecha del acta del consejo
                if (sustentacionDto.getFechaActaConsejo() != null
                                && sustentacionDto.getFechaActaConsejo().isAfter(LocalDate.now())) {
                        throw new InformationException("La fecha del consejo no puede ser mayor a la fecha actual.");
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 28) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Guardar el enlace del oficio del consejo
                sustentacionDto.setLinkOficioConsejo(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkOficioConsejo()));

                // Agregar la información del coordinador en la fase 3
                agregarInformacionCoordinadorFase3(sustentacionProyectoInvestigacion, sustentacionDto, trabajoGrado);

                // Actualizar el estado del trabajo de grado
                trabajoGrado.setNumeroEstado(29);

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método auxiliar para agregar la información del coordinador en la fase 3 a la
         * entidad de sustentación del proyecto de investigación.
         * Esta información incluye los detalles de aceptación de jurados, el acta del
         * consejo, y el enlace del oficio del consejo.
         * 
         * @param sustentacionProyectoInvestigacion la entidad de sustentación del
         *                                          proyecto de investigación a la que
         *                                          se le agregará la información.
         * @param sustentacionDto                   objeto que contiene la información
         *                                          de la fase 3 del coordinador.
         * @param trabajoGrado                      el trabajo de grado asociado.
         */
        private void agregarInformacionCoordinadorFase3(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                // Si los jurados fueron rechazados, actualizar la información del jurado
                // interno y externo
                if (sustentacionDto.getJuradosAceptados().equals(ConceptoVerificacion.RECHAZADO)) {
                        archivoClient.obtenerDocentePorId(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                        archivoClient.obtenerExpertoPorId(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                        sustentacionProyectoInvestigacion
                                        .setIdJuradoInterno(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                        sustentacionProyectoInvestigacion
                                        .setIdJuradoExterno(Long.parseLong(sustentacionDto.getIdJuradoExterno()));
                }

                // Actualizar la información del acta del consejo y el oficio del consejo
                sustentacionProyectoInvestigacion.setJuradosAceptados(sustentacionDto.getJuradosAceptados());
                sustentacionProyectoInvestigacion.setNumeroActaConsejo(sustentacionDto.getNumeroActaConsejo());
                sustentacionProyectoInvestigacion.setFechaActaConsejo(sustentacionDto.getFechaActaConsejo());
                sustentacionProyectoInvestigacion.setLinkOficioConsejo(sustentacionDto.getLinkOficioConsejo());
        }

        /**
         * Método para insertar la información del estudiante relacionada con la
         * sustentación de un proyecto de investigación.
         * Se valida la información proporcionada, incluyendo los enlaces de los
         * documentos, y se actualizan los datos del trabajo de grado.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del estudiante.
         * @param bindingResult   resultado de la validación de los campos.
         * @return un objeto
         *         {@link SustentacionTrabajoInvestigacionEstudianteResponseDto} que
         *         contiene la información registrada
         *         de la sustentación y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado no se encuentra.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado
         *                                   o a que el estudiante no ha completado los
         *                                   datos de egresado.
         */
        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionEstudianteResponseDto insertarInformacionEstudiante(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Validación de la fecha de sustentación
                if (sustentacionDto.getFechaSustentacion() != null
                                && sustentacionDto.getFechaSustentacion().isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha de la sustentación no puede ser mayor a la fecha actual.");
                }

                // Validar los enlaces de los documentos
                validarLink(sustentacionDto.getLinkFormatoH());
                validarLink(sustentacionDto.getLinkFormatoI());
                validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 29) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Validar que el estudiante ha completado los datos de egresado
                List<EmpresaSaveDto> empresas = archivoClientEgresados
                                .obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante());
                if (empresas.isEmpty()) {
                        throw new InformationException(
                                        "No es permitido registrar la información debido a que el estudiante no ha completado los datos de egresado");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Actualizar la información de la sustentación
                trabajoGrado.setNumeroEstado(30);
                sustentacionProyectoInvestigacion.setFechaSustentacion(sustentacionDto.getFechaSustentacion());
                sustentacionProyectoInvestigacion.setLinkFormatoH(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkFormatoH()));
                sustentacionProyectoInvestigacion.setLinkFormatoI(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkFormatoI()));
                sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademicaGrado(
                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado()));

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método para insertar la información del coordinador en la fase 4 de la
         * sustentación de un proyecto de investigación.
         * Se actualizan los datos del trabajo de grado en función de la respuesta a la
         * sustentación.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del coordinador en la fase 4.
         * @param bindingResult   resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase4ResponseDto} que contiene la
         *         información registrada
         *         del coordinador y el estado del trabajo de grado.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si el trabajo de grado o la sustentación no
         *                                   se encuentran.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado.
         */
        @Override
        @Transactional
        public STICoordinadorFase4ResponseDto insertarInformacionCoordinadoFase4(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult bindingResult) {

                // Validación de errores en los campos
                if (bindingResult.hasErrors()) {
                        throw new FieldErrorException(bindingResult);
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 30) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Actualizar el estado del trabajo de grado en función de la respuesta a la
                // sustentación
                if (sustentacionDto.getRespuestaSustentacion().equals(ConceptoSustentacion.APROBADO)) {
                        Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                        .findByTrabajoGradoId(idTrabajoGrado);
                        tiemposPendientesOpt.ifPresent(tiemposPendientesRepository::delete);
                        trabajoGrado.setNumeroEstado(31);
                } else if (sustentacionDto.getRespuestaSustentacion()
                                .equals(ConceptoSustentacion.APROBADO_CON_OBSERVACIONES)) {
                        trabajoGrado.setNumeroEstado(36);
                        insertarInformacionTiempos(trabajoGrado, 15);
                } else if (sustentacionDto.getRespuestaSustentacion().equals(ConceptoSustentacion.NO_APROBADO)) {
                        trabajoGrado.setNumeroEstado(32);
                } else {
                        trabajoGrado.setNumeroEstado(33);
                        insertarInformacionTiempos(trabajoGrado, 60);
                }

                // Agregar la información del coordinador en la fase 4
                agregarInformacionCoordinadorFase4(sustentacionProyectoInvestigacion, sustentacionDto, trabajoGrado);

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método auxiliar para insertar o actualizar la información de los tiempos
         * pendientes para un trabajo de grado.
         * Se establece una fecha límite en función del número de días proporcionado.
         * 
         * @param trabajoGrado el trabajo de grado asociado.
         * @param tiempo       el número de días a partir de la fecha actual para
         *                     establecer la fecha límite.
         */
        private void insertarInformacionTiempos(TrabajoGrado trabajoGrado, int tiempo) {
                Optional<TiemposPendientes> optionalTiemposPendientes = tiemposPendientesRepository
                                .findByTrabajoGradoId(trabajoGrado.getId());

                TiemposPendientes tiemposPendientes;
                if (optionalTiemposPendientes.isPresent()) {
                        tiemposPendientes = optionalTiemposPendientes.get();
                } else {
                        tiemposPendientes = new TiemposPendientes();
                        tiemposPendientes.setTrabajoGrado(trabajoGrado);
                }

                LocalDate fechaActual = LocalDate.now();
                tiemposPendientes.setFechaRegistro(fechaActual);
                tiemposPendientes.setEstado(trabajoGrado.getNumeroEstado());
                tiemposPendientes.setFechaLimite(fechaActual.plusDays(tiempo));

                tiemposPendientesRepository.save(tiemposPendientes);
        }

        /**
         * Método auxiliar para agregar la información del coordinador en la fase 4 a la
         * entidad de sustentación del proyecto de investigación.
         * Esta información incluye la respuesta a la sustentación.
         * 
         * @param sustentacionProyectoInvestigacion la entidad de sustentación del
         *                                          proyecto de investigación a la que
         *                                          se le agregará la información.
         * @param sustentacionDto                   objeto que contiene la información
         *                                          de la fase 4 del coordinador.
         * @param trabajoGrado                      el trabajo de grado asociado.
         */
        private void agregarInformacionCoordinadorFase4(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                sustentacionProyectoInvestigacion.setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
        }

        /**
         * Método para listar la información del docente relacionada con la sustentación
         * de un proyecto de investigación.
         * Se obtiene la información del jurado interno y externo, así como los enlaces
         * de los documentos de la sustentación.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link SustentacionTrabajoInvestigacionListDocenteDto} que
         *         contiene la información del docente y de la sustentación.
         * @throws ResourceNotFoundException si no se encuentra la sustentación asociada
         *                                   al trabajo de grado.
         */
        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionListDocenteDto listarInformacionDocente(Long idTrabajoGrado) {

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con ID trabajo de grado " + idTrabajoGrado
                                                                + " no encontrado"));

                // Obtener la información del jurado interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(sustentacionProyectoInvestigacion.getIdJuradoInterno());
                String nombreDocente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("id", docente.getId().toString());
                evaluadorInternoMap.put("nombres", nombreDocente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener la información del jurado externo
                ExpertoResponseDto experto = archivoClient
                                .obtenerExpertoPorId(sustentacionProyectoInvestigacion.getIdJuradoExterno());
                String nombreExperto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("id", experto.getId().toString());
                evaluadorExternoMap.put("nombres", nombreExperto);
                evaluadorExternoMap.put("universidad", experto.getUniversidadtitexp());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                // Crear el DTO de respuesta
                SustentacionTrabajoInvestigacionListDocenteDto responseDto = new SustentacionTrabajoInvestigacionListDocenteDto();
                responseDto.setId(sustentacionProyectoInvestigacion.getId());
                responseDto.setLinkFormatoF(sustentacionProyectoInvestigacion.getLinkFormatoF());
                responseDto.setLinkMonografia(sustentacionProyectoInvestigacion.getLinkMonografia());

                // Obtener los anexos de la sustentación
                List<AnexoSustentacion> anexos = anexosSustentacionRepository.obtenerAnexosPorId(responseDto.getId());
                List<String> listaAnexos = anexos.stream()
                                .map(AnexoSustentacion::getLinkAnexo)
                                .collect(Collectors.toList());
                responseDto.setAnexos(listaAnexos);

                responseDto.setJuradoInterno(evaluadorInternoMap);
                responseDto.setJuradoExterno(evaluadorExternoMap);

                return responseDto;
        }

        /**
         * Método para listar la información del coordinador en la fase 1 de la
         * sustentación de un proyecto de investigación.
         * Se obtiene el concepto del coordinador y el enlace de estudio de la hoja de
         * vida académica.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link STICoordinadorFase1ResponseDto} que contiene la
         *         información del coordinador en la fase 1.
         * @throws ResourceNotFoundException si no se encuentra la sustentación asociada
         *                                   al trabajo de grado.
         * @throws InformationException      si no se han registrado datos de la fase 1.
         */
        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(Long idTrabajoGrado) {

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si se han registrado datos
                if (sustentacionProyectoInvestigacion.getConceptoCoordinador() == null
                                && sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademica() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionProyectoInvestigacion);
        }

        /**
         * Método para listar la información del coordinador en la fase 2 de la
         * sustentación de un proyecto de investigación.
         * Se obtiene la respuesta del comité y el enlace del formato G.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link STICoordinadorFase2ResponseDto} que contiene la
         *         información del coordinador en la fase 2.
         * @throws ResourceNotFoundException si no se encuentra la sustentación asociada
         *                                   al trabajo de grado.
         * @throws InformationException      si no se han registrado datos de la fase 2.
         */
        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si se han registrado datos
                boolean actaFechaRespuestaComiteEmpty = sustentacionProyectoInvestigacion
                                .getActaFechaRespuestaComite() == null
                                || sustentacionProyectoInvestigacion.getActaFechaRespuestaComite().isEmpty();
                if (actaFechaRespuestaComiteEmpty && sustentacionProyectoInvestigacion.getLinkFormatoG() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionProyectoInvestigacion);
        }

        /**
         * Método para listar la información del coordinador en la fase 3 de la
         * sustentación de un proyecto de investigación.
         * Se obtiene la aceptación de los jurados, el acta del consejo, y el enlace del
         * oficio del consejo.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link STICoordinadorFase3ResponseDto} que contiene la
         *         información del coordinador en la fase 3.
         * @throws ResourceNotFoundException si no se encuentra la sustentación asociada
         *                                   al trabajo de grado.
         * @throws InformationException      si no se han registrado datos de la fase 3.
         */
        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(Long idTrabajoGrado) {

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si se han registrado datos
                if (sustentacionProyectoInvestigacion.getJuradosAceptados() == null
                                && sustentacionProyectoInvestigacion.getNumeroActaConsejo() == null
                                && sustentacionProyectoInvestigacion.getFechaActaConsejo() == null
                                && sustentacionProyectoInvestigacion.getLinkOficioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionProyectoInvestigacion);
        }

        /**
         * Método para listar la información del estudiante relacionada con la
         * sustentación de un proyecto de investigación.
         * Se obtienen los enlaces de los formatos H e I, así como otros documentos
         * relacionados con la sustentación.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto
         *         {@link SustentacionTrabajoInvestigacionEstudianteResponseDto} que
         *         contiene la información del estudiante y de la sustentación.
         * @throws ResourceNotFoundException si no se encuentra la sustentación asociada
         *                                   al trabajo de grado.
         * @throws InformationException      si no se han registrado los datos de la
         *                                   sustentación.
         */
        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionEstudianteResponseDto listarInformacionEstudiante(Long idTrabajoGrado) {

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si se han registrado los enlaces de los formatos H e I
                if (sustentacionTrabajoInvestigacion.getLinkFormatoH() == null
                                && sustentacionTrabajoInvestigacion.getLinkFormatoI() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionTrabajoInvestigacion);
        }

        /**
         * Método para listar la información del coordinador en la fase 4 de la
         * sustentación de un proyecto de investigación.
         * Se obtiene la respuesta de la sustentación.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return un objeto {@link STICoordinadorFase4ResponseDto} que contiene la
         *         información del coordinador en la fase 4.
         * @throws ResourceNotFoundException si no se encuentra la sustentación asociada
         *                                   al trabajo de grado.
         * @throws InformationException      si no se han registrado los datos de la
         *                                   fase 4.
         */
        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase4ResponseDto listarInformacionCoordinadorFase4(Long idTrabajoGrado) {

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Verificar si se ha registrado la respuesta a la sustentación
                if (sustentacionTrabajoInvestigacion.getRespuestaSustentacion() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionTrabajoInvestigacion);
        }

        /**
         * Método para verificar si el estudiante asociado a un trabajo de grado ha
         * completado los datos de egresado.
         * 
         * @param idTrabajoGrado el identificador del trabajo de grado.
         * @return {@code true} si el estudiante ha completado los datos de egresado,
         *         {@code false} en caso contrario.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado.
         */
        @Override
        @Transactional(readOnly = true)
        public Boolean verificarEgresado(Long idTrabajoGrado) {

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                // Obtener las empresas asociadas al estudiante
                List<EmpresaSaveDto> empresas = archivoClientEgresados
                                .obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante());

                // Verificar si el estudiante ha completado los datos de egresado
                return !empresas.isEmpty();
        }

        /**
         * Método auxiliar para generar la ruta de almacenamiento de archivos para un
         * trabajo de grado.
         * La ruta se genera en función de la fecha actual y la información del
         * estudiante.
         * 
         * @param trabajoGrado el trabajo de grado asociado.
         * @return la ruta generada para almacenar los archivos.
         */
        private String identificacionArchivo(TrabajoGrado trabajoGrado) {

                // Obtener la información del estudiante asociado al trabajo de grado
                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Definir el proceso y la fecha actual
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                LocalDate fechaActual = LocalDate.now();
                int anio = fechaActual.getYear();
                int mes = fechaActual.getMonthValue();

                // Generar la ruta basada en la información del estudiante y la fecha
                Long identificacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String informacionEstudiante = identificacionEstudiante + "-" + nombreEstudiante + "_"
                                + apellidoEstudiante;
                return anio + "/" + mes + "/" + informacionEstudiante + "/" + procesoVa;
        }

        /**
         * Método para actualizar la información del docente relacionada con la
         * sustentación de un proyecto de investigación.
         * Se validan y actualizan los enlaces de los documentos y anexos, y se
         * actualiza el estado del trabajo de grado.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la sustentación
         *                        del docente.
         * @param result          resultado de la validación de los campos.
         * @return un objeto {@link SustentacionTrabajoInvestigacionDocenteResponseDto}
         *         que contiene la información actualizada del docente y la
         *         sustentación.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado o la
         *                                   sustentación asociada.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado.
         */
        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto actualizarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {

                // Validación de errores en los campos
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 24 && trabajoGrado.getNumeroEstado() != 25
                                && trabajoGrado.getNumeroEstado() != 27) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Validar la existencia de los jurados internos y externos
                archivoClient.obtenerDocentePorId(sustentacionDto.getIdJuradoInterno());
                archivoClient.obtenerExpertoPorId(sustentacionDto.getIdJuradoExterno());

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Actualizar el estado del trabajo de grado
                trabajoGrado.setNumeroEstado(24);

                // Validar y actualizar el enlace de la monografía
                if (!sustentacionDto.getLinkMonografia()
                                .equals(sustentacionProyectoInvestigacion.getLinkMonografia())) {
                        validarLink(sustentacionDto.getLinkMonografia());
                        sustentacionDto.setLinkMonografia(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                        sustentacionDto.getLinkMonografia()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacion.getLinkMonografia());
                }

                // Validar y actualizar el enlace del formato F
                if (!sustentacionDto.getLinkFormatoF().equals(sustentacionProyectoInvestigacion.getLinkFormatoF())) {
                        validarLink(sustentacionDto.getLinkFormatoF());
                        sustentacionDto.setLinkFormatoF(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                        sustentacionDto.getLinkFormatoF()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacion.getLinkFormatoF());
                }

                // Actualizar los anexos si se proporcionan
                if (sustentacionDto.getAnexos() != null) {
                        List<AnexoSustentacion> anexosEntidades = sustentacionDto.getAnexos().stream()
                                        .map(anexoSustentacionMapper::toEntity)
                                        .collect(Collectors.toList());

                        actualizarAnexos(sustentacionProyectoInvestigacion, anexosEntidades, directorioArchivos);
                }

                // Actualizar los valores de la sustentación del docente
                updateExamenValoracionDocenteValues(sustentacionProyectoInvestigacion, sustentacionDto, trabajoGrado);

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método auxiliar para actualizar los valores de la sustentación del docente.
         * Se actualizan los identificadores de los jurados y los enlaces de los
         * documentos de la sustentación.
         * 
         * @param sustentacionProyectoInvestigacion la entidad de sustentación del
         *                                          proyecto de investigación a
         *                                          actualizar.
         * @param sustentacionDto                   objeto que contiene la nueva
         *                                          información de la sustentación del
         *                                          docente.
         * @param trabajoGrado                      el trabajo de grado asociado.
         */
        private void updateExamenValoracionDocenteValues(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                sustentacionProyectoInvestigacion.setConceptoCoordinador(null);

                sustentacionProyectoInvestigacion.setIdJuradoInterno(sustentacionDto.getIdJuradoInterno());
                sustentacionProyectoInvestigacion.setIdJuradoExterno(sustentacionDto.getIdJuradoExterno());
                sustentacionProyectoInvestigacion.setLinkMonografia(sustentacionDto.getLinkMonografia());
                sustentacionProyectoInvestigacion.setLinkFormatoF(sustentacionDto.getLinkFormatoF());
        }

        /**
         * Método auxiliar para actualizar los anexos de la sustentación del proyecto de
         * investigación.
         * Se validan y actualizan los enlaces de los anexos, eliminando los anexos que
         * ya no son válidos.
         * 
         * @param sustentacionProyectoInvestigacion la entidad de sustentación del
         *                                          proyecto de investigación a
         *                                          actualizar.
         * @param anexosNuevos                      la lista de nuevos anexos que se
         *                                          agregarán o actualizarán.
         * @param rutaArchivo                       la ruta donde se almacenarán los
         *                                          archivos de los anexos.
         */
        private void actualizarAnexos(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        List<AnexoSustentacion> anexosNuevos, String rutaArchivo) {

                List<AnexoSustentacion> anexosActuales = sustentacionProyectoInvestigacion.getAnexos();

                // Crear un mapa de los anexos actuales para facilitar la búsqueda
                Map<String, AnexoSustentacion> mapaAnexosActuales = anexosActuales.stream()
                                .collect(Collectors.toMap(AnexoSustentacion::getLinkAnexo, Function.identity()));

                List<AnexoSustentacion> anexosActualizados = new ArrayList<>();

                // Actualizar o agregar nuevos anexos
                for (AnexoSustentacion anexoNuevo : anexosNuevos) {
                        AnexoSustentacion anexoActual = mapaAnexosActuales.get(anexoNuevo.getLinkAnexo());

                        if (anexoActual != null) {
                                anexosActualizados.add(anexoActual);
                        } else {
                                validarLink(anexoNuevo.getLinkAnexo());
                                String rutaAnexoNueva = FilesUtilities.guardarArchivoNew2(rutaArchivo,
                                                anexoNuevo.getLinkAnexo());
                                anexoNuevo.setLinkAnexo(rutaAnexoNueva);
                                anexoNuevo.setSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);
                                anexosActualizados.add(anexoNuevo);
                        }
                }

                // Eliminar los anexos que ya no son válidos
                anexosActuales.removeIf(anexoActual -> {
                        if (!anexosActualizados.contains(anexoActual)) {
                                FilesUtilities.deleteFileExample(anexoActual.getLinkAnexo());
                                return true;
                        }
                        return false;
                });

                // Actualizar la lista de anexos en la entidad de sustentación
                anexosActuales.clear();
                anexosActuales.addAll(anexosActualizados);
        }

        /**
         * Método para actualizar la información del coordinador en la fase 1 de la
         * sustentación de un proyecto de investigación.
         * Se validan los atributos proporcionados y se actualizan los datos del trabajo
         * de grado y la sustentación.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la fase 1 del
         *                        coordinador en la sustentación.
         * @param result          resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase1ResponseDto} que contiene la
         *         información actualizada del coordinador y la sustentación.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado o la
         *                                   sustentación asociada.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o falta de atributos requeridos.
         */
        @Override
        @Transactional
        public STICoordinadorFase1ResponseDto actualizarInformacionCoordinadoFase1(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {

                // Validación de errores en los campos
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Validaciones adicionales según el concepto del coordinador y atributos
                // proporcionados
                if ((sustentacionDto.getEnvioEmail() == null
                                || sustentacionDto.getLinkEstudioHojaVidaAcademica() != null)
                                && sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.RECHAZADO)) {
                        throw new InformationException("Faltan atributos para el registro");
                }

                if ((sustentacionDto.getLinkEstudioHojaVidaAcademica() == null
                                || sustentacionDto.getEnvioEmail() != null)
                                && sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                // Validar el enlace del estudio de hoja de vida académica si el concepto es
                // aceptado
                if (sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.ACEPTADO)) {
                        validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademica());
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 24 && trabajoGrado.getNumeroEstado() != 25
                                && trabajoGrado.getNumeroEstado() != 26) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Verificar si se han registrado datos previos
                if (sustentacionProyectoInvestigacion.getConceptoCoordinador() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Actualizar el concepto del coordinador y el estado del trabajo de grado
                if (!sustentacionDto.getConceptoCoordinador()
                                .equals(sustentacionProyectoInvestigacion.getConceptoCoordinador())) {
                        ArrayList<String> correos = new ArrayList<>();
                        if (sustentacionDto.getConceptoCoordinador().equals(ConceptoVerificacion.RECHAZADO)) {
                                // Enviar correos de corrección y eliminar archivo si el concepto cambia a
                                // rechazado
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmail().getAsunto(),
                                                sustentacionDto.getEnvioEmail().getMensaje());
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademica());
                                trabajoGrado.setNumeroEstado(25);
                        } else {
                                // Guardar el nuevo archivo si el concepto es aceptado
                                sustentacionDto.setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew2(
                                                directorioArchivos, sustentacionDto.getLinkEstudioHojaVidaAcademica()));
                                trabajoGrado.setNumeroEstado(26);
                        }
                } else {
                        // Validar y actualizar el archivo si el enlace ha cambiado
                        if (sustentacionDto.getLinkEstudioHojaVidaAcademica() != null
                                        && !sustentacionDto.getLinkEstudioHojaVidaAcademica()
                                                        .equals(sustentacionProyectoInvestigacion
                                                                        .getLinkEstudioHojaVidaAcademica())) {
                                validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademica());
                                sustentacionDto.setLinkEstudioHojaVidaAcademica(
                                                FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                                sustentacionDto.getLinkEstudioHojaVidaAcademica()));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademica());
                        }
                }

                // Actualizar los valores de la sustentación del proyecto de investigación
                sustentacionProyectoInvestigacion.setConceptoCoordinador(sustentacionDto.getConceptoCoordinador());
                sustentacionProyectoInvestigacion
                                .setLinkEstudioHojaVidaAcademica(sustentacionDto.getLinkEstudioHojaVidaAcademica());

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionProyectoInvestigacionGuardada);
        }

        /**
         * Método para actualizar la información del coordinador en la fase 2 de la
         * sustentación de un proyecto de investigación.
         * Se validan los atributos proporcionados y se actualizan los datos del trabajo
         * de grado y la sustentación.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la fase 2 del
         *                        coordinador en la sustentación.
         * @param result          resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase2ResponseDto} que contiene la
         *         información actualizada del coordinador y la sustentación.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado o la
         *                                   sustentación asociada.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o falta de atributos requeridos.
         */
        @Override
        @Transactional
        public STICoordinadorFase2ResponseDto actualizarInformacionCoordinadoFase2(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {

                // Validación de errores en los campos
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Validaciones adicionales según el concepto del comité y los atributos
                // proporcionados
                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                .equals(Concepto.NO_APROBADO)
                                && (sustentacionDto.getLinkFormatoG() != null
                                                || sustentacionDto.getInformacionEnvioConsejo() != null)) {
                        throw new InformationException("Envio de atributos no permitido");
                }

                if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite().equals(Concepto.APROBADO)
                                && (sustentacionDto.getLinkFormatoG() == null
                                                || sustentacionDto.getInformacionEnvioConsejo() == null)) {
                        throw new InformationException("Atributos incorrectos");
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 26 && trabajoGrado.getNumeroEstado() != 27
                                && trabajoGrado.getNumeroEstado() != 28) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacionOld = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Verificar si se han registrado datos previos
                boolean actaFechaRespuestaComiteEmpty = sustentacionProyectoInvestigacionOld
                                .getActaFechaRespuestaComite() == null
                                || sustentacionProyectoInvestigacionOld.getActaFechaRespuestaComite().isEmpty();
                if (actaFechaRespuestaComiteEmpty
                                && sustentacionProyectoInvestigacionOld.getLinkEstudioHojaVidaAcademica() == null
                                && sustentacionProyectoInvestigacionOld.getLinkFormatoG() == null
                                && sustentacionDto.getInformacionEnvioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Identificar el directorio para guardar los archivos
                String directorioArchivos = identificacionArchivo(trabajoGrado);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion = null;
                List<RespuestaComiteSustentacion> respuestaComiteList = sustentacionProyectoInvestigacionRepository
                                .findRespuestaComiteBySustentacionId(sustentacionProyectoInvestigacionOld.getId());
                RespuestaComiteSustentacion ultimoRegistro = respuestaComiteList.isEmpty() ? null
                                : respuestaComiteList.get(0);

                // Actualizar el estado del trabajo de grado y los archivos según el concepto
                // del comité
                if (ultimoRegistro != null
                                && !ultimoRegistro.getConceptoComite().equals(sustentacionDto
                                                .getActaFechaRespuestaComite().get(0).getConceptoComite())) {
                        ArrayList<String> correos = new ArrayList<>();
                        if (sustentacionDto.getActaFechaRespuestaComite().get(0).getConceptoComite()
                                        .equals(Concepto.NO_APROBADO)) {
                                // Enviar correos de corrección y eliminar archivo si el concepto cambia a no
                                // aprobado
                                EstudianteResponseDtoAll estudiante = archivoClient
                                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                                correos.add(estudiante.getCorreoUniversidad());
                                correos.add(trabajoGrado.getCorreoElectronicoTutor());
                                envioCorreos.enviarCorreosCorrecion(correos,
                                                sustentacionDto.getEnvioEmail().getAsunto(),
                                                sustentacionDto.getEnvioEmail().getMensaje());
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionOld.getLinkFormatoG());
                                trabajoGrado.setNumeroEstado(27);
                        } else {
                                // Guardar el nuevo archivo y enviar correos si el concepto es aprobado
                                validarLink(sustentacionDto.getLinkFormatoG());
                                correos.add(Constants.correoComite);
                                Map<String, Object> documentosParaConsejo = new HashMap<>();
                                documentosParaConsejo.put("formatoG", sustentacionDto.getLinkFormatoG().split("-")[1]);
                                envioCorreos.enviarCorreoConAnexos(correos, sustentacionDto.getEnvioEmail().getAsunto(),
                                                sustentacionDto.getEnvioEmail().getMensaje(), documentosParaConsejo);

                                sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkFormatoG()));
                                trabajoGrado.setNumeroEstado(28);
                        }
                } else {
                        // Validar y actualizar el archivo si el enlace ha cambiado
                        if (sustentacionDto.getLinkFormatoG() != null
                                        && !sustentacionDto.getLinkFormatoG().equals(
                                                        sustentacionProyectoInvestigacionOld.getLinkFormatoG())) {
                                validarLink(sustentacionDto.getLinkFormatoG());
                                sustentacionDto.setLinkFormatoG(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                sustentacionDto.getLinkFormatoG()));
                                FilesUtilities.deleteFileExample(
                                                sustentacionProyectoInvestigacionOld.getLinkFormatoG());
                        }
                        trabajoGrado.setNumeroEstado(26);
                }

                // Actualizar los valores de la sustentación del proyecto de investigación
                updateSustentacionCoordinadorFase2Values(sustentacionProyectoInvestigacionOld, sustentacionDto,
                                trabajoGrado);

                // Guardar la entidad en el repositorio
                sustentacionTrabajoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionOld);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacion);
        }

        /**
         * Método auxiliar para actualizar los valores de la sustentación del
         * coordinador en la fase 2.
         * Se actualizan los datos de la respuesta del comité y el enlace del formato G.
         * 
         * @param sustentacionTrabajoInvestigacion                    la entidad de
         *                                                            sustentación del
         *                                                            proyecto de
         *                                                            investigación a
         *                                                            actualizar.
         * @param sustentacionTrabajoInvestigacionCoordinadorFase2Dto objeto que
         *                                                            contiene la nueva
         *                                                            información de la
         *                                                            fase 2 del
         *                                                            coordinador.
         * @param trabajoGrado                                        el trabajo de
         *                                                            grado asociado.
         */
        private void updateSustentacionCoordinadorFase2Values(
                        SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                        TrabajoGrado trabajoGrado) {

                // Buscar el último registro de respuesta del comité
                RespuestaComiteSustentacion ultimoRegistro = respuestaComiteSustentacionRepository
                                .findFirstBySustentacionProyectoInvestigacionIdOrderByIdDesc(
                                                sustentacionTrabajoInvestigacion.getId());

                // Actualizar la información de la respuesta del comité
                if (ultimoRegistro != null) {
                        ultimoRegistro.setNumeroActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getNumeroActa());
                        ultimoRegistro.setFechaActa(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getFechaActa());
                        ultimoRegistro.setConceptoComite(sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                        .getActaFechaRespuestaComite().get(0).getConceptoComite());
                        respuestaComiteSustentacionRepository.save(ultimoRegistro);
                }

                // Actualizar el enlace del formato G
                sustentacionTrabajoInvestigacion
                                .setLinkFormatoG(sustentacionTrabajoInvestigacionCoordinadorFase2Dto.getLinkFormatoG());
        }

        /**
         * Método para actualizar la información del coordinador en la fase 3 de la
         * sustentación de un proyecto de investigación.
         * Se validan los atributos proporcionados y se actualizan los datos del trabajo
         * de grado y la sustentación.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la fase 3 del
         *                        coordinador en la sustentación.
         * @param result          resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase3ResponseDto} que contiene la
         *         información actualizada del coordinador y la sustentación.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado o la
         *                                   sustentación asociada.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o falta de atributos requeridos.
         */
        @Override
        @Transactional
        public STICoordinadorFase3ResponseDto actualizarInformacionCoordinadoFase3(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {

                // Validación de errores en los campos
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Validaciones adicionales según los atributos proporcionados
                if (sustentacionDto.getIdJuradoInterno().equals("Sin cambios")
                                && sustentacionDto.getIdJuradoExterno().equals("Sin cambios")) {
                        throw new InformationException("Atributos incorrectos");
                }

                if (sustentacionDto.getFechaActaConsejo() != null
                                && sustentacionDto.getFechaActaConsejo().isAfter(LocalDate.now())) {
                        throw new InformationException("La fecha del consejo no puede ser mayor a la fecha actual.");
                }

                // Validar la existencia de los jurados en el sistema
                archivoClient.obtenerDocentePorId(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                archivoClient.obtenerExpertoPorId(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 28 && trabajoGrado.getNumeroEstado() != 29
                                && trabajoGrado.getNumeroEstado() != 36) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Verificar si se han registrado datos previos
                if (sustentacionProyectoInvestigacion.getJuradosAceptados() == null
                                && sustentacionProyectoInvestigacion.getNumeroActaConsejo() == null
                                && sustentacionProyectoInvestigacion.getFechaActaConsejo() == null
                                && sustentacionProyectoInvestigacion.getLinkOficioConsejo() == null) {
                        throw new InformationException("No se han registrado datos");
                }

                // Actualizar los jurados y el enlace del oficio del consejo
                sustentacionProyectoInvestigacion
                                .setIdJuradoInterno(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                sustentacionProyectoInvestigacion
                                .setIdJuradoExterno(Long.parseLong(sustentacionDto.getIdJuradoExterno()));

                String directorioArchivos = identificacionArchivo(trabajoGrado);

                if (!sustentacionDto.getLinkOficioConsejo()
                                .equals(sustentacionProyectoInvestigacion.getLinkOficioConsejo())) {
                        validarLink(sustentacionDto.getLinkOficioConsejo());
                        sustentacionDto.setLinkOficioConsejo(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                        sustentacionDto.getLinkOficioConsejo()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacion.getLinkOficioConsejo());
                }

                // Actualizar los demás valores de la sustentación
                sustentacionProyectoInvestigacion.setJuradosAceptados(sustentacionDto.getJuradosAceptados());
                sustentacionProyectoInvestigacion.setNumeroActaConsejo(sustentacionDto.getNumeroActaConsejo());
                sustentacionProyectoInvestigacion.setFechaActaConsejo(sustentacionDto.getFechaActaConsejo());
                sustentacionProyectoInvestigacion.setLinkOficioConsejo(sustentacionDto.getLinkOficioConsejo());

                trabajoGrado.setNumeroEstado(29);

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper.toCoordinadorFase3Dto(sustentacionGuardada);
        }

        /**
         * Método para actualizar la información del estudiante en la sustentación de un
         * proyecto de investigación.
         * Se validan los atributos proporcionados y se actualizan los datos del trabajo
         * de grado y la sustentación.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información del estudiante en
         *                        la sustentación.
         * @param result          resultado de la validación de los campos.
         * @return un objeto
         *         {@link SustentacionTrabajoInvestigacionEstudianteResponseDto} que
         *         contiene la información actualizada de la sustentación.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado o la
         *                                   sustentación asociada.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o falta de atributos requeridos.
         */
        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionEstudianteResponseDto actualizarInformacionEstudiante(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {

                // Validación de errores en los campos
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Validaciones adicionales según los atributos proporcionados
                if (sustentacionDto.getFechaSustentacion() != null
                                && sustentacionDto.getFechaSustentacion().isAfter(LocalDate.now())) {
                        throw new InformationException(
                                        "La fecha de la sustentación no puede ser mayor a la fecha actual.");
                }

                // Validar los enlaces proporcionados
                validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());
                validarLink(sustentacionDto.getLinkFormatoH());
                validarLink(sustentacionDto.getLinkFormatoI());

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 30) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                String directorioArchivos = identificacionArchivo(trabajoGrado);

                // Actualizar los enlaces de los formatos y la hoja de vida académica
                if (!sustentacionDto.getLinkFormatoH().equals(sustentacionProyectoInvestigacion.getLinkFormatoH())) {
                        validarLink(sustentacionDto.getLinkFormatoH());
                        sustentacionDto.setLinkFormatoH(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                        sustentacionDto.getLinkFormatoH()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacion.getLinkFormatoH());
                }
                if (!sustentacionDto.getLinkFormatoI().equals(sustentacionProyectoInvestigacion.getLinkFormatoI())) {
                        validarLink(sustentacionDto.getLinkFormatoI());
                        sustentacionDto.setLinkFormatoI(FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                        sustentacionDto.getLinkFormatoI()));
                        FilesUtilities.deleteFileExample(sustentacionProyectoInvestigacion.getLinkFormatoI());
                }
                if (!sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado().equals(
                                sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademicaGrado())) {
                        validarLink(sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());
                        sustentacionDto.setLinkEstudioHojaVidaAcademicaGrado(
                                        FilesUtilities.guardarArchivoNew2(directorioArchivos,
                                                        sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado()));
                        FilesUtilities.deleteFileExample(
                                        sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademicaGrado());
                }

                // Actualizar la fecha de sustentación y los enlaces
                sustentacionProyectoInvestigacion.setFechaSustentacion(sustentacionDto.getFechaSustentacion());
                sustentacionProyectoInvestigacion.setLinkFormatoH(sustentacionDto.getLinkFormatoH());
                sustentacionProyectoInvestigacion.setLinkFormatoI(sustentacionDto.getLinkFormatoI());
                sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademicaGrado(
                                sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                trabajoGrado.setNumeroEstado(30);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper.toEstudianteDto(sustentacionGuardada);
        }

        /**
         * Método para actualizar la información del coordinador en la fase 4 de la
         * sustentación de un proyecto de investigación.
         * Se validan los atributos proporcionados y se actualizan los datos del trabajo
         * de grado y la sustentación.
         * 
         * @param idTrabajoGrado  el identificador del trabajo de grado.
         * @param sustentacionDto objeto que contiene la información de la fase 4 del
         *                        coordinador en la sustentación.
         * @param result          resultado de la validación de los campos.
         * @return un objeto {@link STICoordinadorFase4ResponseDto} que contiene la
         *         información actualizada del coordinador y la sustentación.
         * @throws FieldErrorException       si se encuentran errores de validación en
         *                                   los campos.
         * @throws ResourceNotFoundException si no se encuentra el trabajo de grado o la
         *                                   sustentación asociada.
         * @throws InformationException      si no se permite registrar la información
         *                                   debido a un estado incorrecto del trabajo
         *                                   de grado o falta de atributos requeridos.
         */
        @Override
        @Transactional
        public STICoordinadorFase4ResponseDto actualizarInformacionCoordinadoFase4(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result) {

                // Validación de errores en los campos
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                // Buscar el trabajo de grado por su ID
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Trabajo de grado con id " + idTrabajoGrado + " no encontrado"));

                // Validación del estado del trabajo de grado
                if (trabajoGrado.getNumeroEstado() != 30 && trabajoGrado.getNumeroEstado() != 31
                                && trabajoGrado.getNumeroEstado() != 32 && trabajoGrado.getNumeroEstado() != 33
                                && trabajoGrado.getNumeroEstado() != 36) {
                        throw new InformationException("No es permitido registrar la información");
                }

                // Buscar la sustentación asociada al trabajo de grado
                SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Sustentación con id: " + trabajoGrado
                                                                .getSustentacionProyectoInvestigacion().getId()
                                                                + " no encontrada"));

                // Verificar si la respuesta de la sustentación ha cambiado y actualizar el
                // estado del trabajo de grado en consecuencia
                if (!sustentacionProyectoInvestigacion.getRespuestaSustentacion()
                                .equals(sustentacionDto.getRespuestaSustentacion())) {
                        if (sustentacionDto.getRespuestaSustentacion().equals(ConceptoSustentacion.APROBADO)) {
                                Optional<TiemposPendientes> tiemposPendientesOpt = tiemposPendientesRepository
                                                .findByTrabajoGradoId(idTrabajoGrado);
                                tiemposPendientesOpt.ifPresent(tiemposPendientesRepository::delete);
                                trabajoGrado.setNumeroEstado(31);
                        } else if (sustentacionDto.getRespuestaSustentacion()
                                        .equals(ConceptoSustentacion.APROBADO_CON_OBSERVACIONES)) {
                                trabajoGrado.setNumeroEstado(36);
                                insertarInformacionTiempos(trabajoGrado, 15);
                        } else if (sustentacionDto.getRespuestaSustentacion()
                                        .equals(ConceptoSustentacion.NO_APROBADO)) {
                                trabajoGrado.setNumeroEstado(32);
                        } else {
                                trabajoGrado.setNumeroEstado(33);
                                insertarInformacionTiempos(trabajoGrado, 60);
                        }
                }

                // Actualizar tiempos pendientes si existen
                Optional<TiemposPendientes> tiemposPendientes = tiemposPendientesRepository
                                .findByTrabajoGradoId(idTrabajoGrado);
                tiemposPendientes.ifPresent(tp -> tp.setFechaLimite(LocalDate.now().plusDays(60)));

                // Actualizar la información de la sustentación
                actualizarInformacionCoordinadorFase4(sustentacionProyectoInvestigacion, sustentacionDto, trabajoGrado);

                // Guardar la entidad en el repositorio
                SustentacionProyectoInvestigacion sustentacionGuardada = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Convertir la entidad guardada en el DTO de respuesta
                return sustentacionProyectoInvestigacionResponseMapper.toCoordinadorFase4Dto(sustentacionGuardada);
        }

        /**
         * Método privado para actualizar la información del coordinador en la fase 4 de
         * la sustentación.
         * 
         * @param sustentacionProyectoInvestigacion la entidad de sustentación que se va
         *                                          a actualizar.
         * @param sustentacionDto                   el objeto DTO que contiene la nueva
         *                                          información para la actualización.
         * @param trabajoGrado                      la entidad del trabajo de grado
         *                                          asociada a la sustentación.
         */
        private void actualizarInformacionCoordinadorFase4(
                        SustentacionProyectoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        TrabajoGrado trabajoGrado) {

                sustentacionProyectoInvestigacion
                                .setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
        }

        /**
         * Método privado para validar el formato de un enlace y su contenido en base64.
         * 
         * @param link el enlace que se va a validar.
         * @throws InformationException si el enlace no tiene un formato válido o su
         *                              contenido no está codificado correctamente en
         *                              base64.
         */
        private void validarLink(String link) {
                // Validar el formato del enlace
                ValidationUtils.validarFormatoLink(link);

                // Extraer y validar la parte codificada en base64 del enlace
                String base64 = link.substring(link.indexOf('-') + 1);
                ValidationUtils.validarBase64(base64);
        }

}
