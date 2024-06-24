package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.CursoSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado.EmpresaSaveDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.CamposUnicosSustentacionProyectoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SustentacionProyectoInvestigacionServiceImpl implements SustentacionProyectoInvestigacionService {

        private final SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        private final SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        private final SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final InformacionUnicaSustentacionProyectoInvestigacion informacionUnicaSustentacionProyectoInvestigacion;
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;
        private final ArchivoClientEgresados archivoClientEgresados;

        @Autowired
        private JavaMailSender mailSender;

        @Autowired
        private SpringTemplateEngine templateEngine;

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                trabajoGrado.setNumeroEstado(12);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);

                // Guardar la entidad SustentacionProyectoInvestigacion
                sustentacionProyectoInvestigacion.setLinkFormatoF(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkFormatoF(),
                                                nombreCarpeta));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                // Obtener y construir información del evaluador interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener y construir información del evaluador externo
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(Long.parseLong(sustentacionDto.getIdJuradoExterno()));
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidad());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionResponseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionRes);

                sustentacionResponseDto.setJuradoInterno(evaluadorInternoMap);
                sustentacionResponseDto.setJuradoExterno(evaluadorExternoMap);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toDocenteDto(sustentacionProyectoInvestigacionRes);
        }

        @Override
        @Transactional
        public STICoordinadorFase1ResponseDto insertarInformacionCoordinadoFase1(
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(sustentacionDto.getIdTrabajoGrados());

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                // sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);
                trabajoGrado.setNumeroEstado(13);

                // Guardar la entidad SustentacionProyectoInvestigacion

                sustentacionProyectoInvestigacion.setLinkFormatoG(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkFormatoG(),
                                                nombreCarpeta));

                sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademica(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkEstudioHojaVidaAcademica(),
                                                nombreCarpeta));

                agregarInformacionCoordinadorFase1(sustentacionProyectoInvestigacionTmp, sustentacionDto);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                enviarCorreoConsejo(sustentacionDto);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase1(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto) {
                sustentacionProyectoInvestigacion
                                .setLinkFormatoG(sustentacionDto.getLinkFormatoG());
                sustentacionProyectoInvestigacion
                                .setLinkEstudioHojaVidaAcademica(sustentacionDto.getLinkEstudioHojaVidaAcademica());
        }

        private boolean enviarCorreoConsejo(
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto) {
                try {
                        Map<String, Object> templateModel = new HashMap<>();

                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true); 

                        // Configurar variables del contexto para la plantilla
                        // templateModel.put("nombreEvaluador", "Nombre del Evaluador");
                        templateModel.put("mensaje",
                                        sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                                        .getEnvioEmailComiteDto()
                                                        .getMensaje());

                        // Crear el contexto para el motor de plantillas
                        Context context = new Context();
                        context.setVariables(templateModel);

                        // Procesar la plantilla de correo electrónico
                        String html = templateEngine.process("emailTemplate", context);

                        helper.setTo(Constants.correoConsejo);
                        helper.setSubject(sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                        .getEnvioEmailComiteDto()
                                        .getAsunto());
                        helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

                        // Obtener documentos y adjuntarlos
                        Map<String, Object> documentosParaEvaluador = new HashMap<>();

                        // Agregar un String manualmente
                        String[] formatoG = sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                        .getLinkFormatoG()
                                        .split("-");
                        String[] estudioHojaVidaAcademica = sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                        .getLinkEstudioHojaVidaAcademica()
                                        .split("-");
                        documentosParaEvaluador.put("FormatoG", formatoG[1]);
                        documentosParaEvaluador.put("EstudioHojaVidaAcademica", estudioHojaVidaAcademica[1]);

                        for (Map.Entry<String, Object> entry : documentosParaEvaluador.entrySet()) {
                                String nombreDocumento = entry.getKey();
                                Object valorDocumento = entry.getValue();

                                // Manejar los documentos que son cadenas
                                String base64Documento = (String) valorDocumento;
                                byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
                                ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes,
                                                "application/pdf");
                                helper.addAttachment(nombreDocumento + ".pdf", dataSource);
                        }

                        // Enviar el mensaje
                        mailSender.send(message);

                        return true;
                } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                }
        }

        @Override
        @Transactional
        public STICoordinadorFase2ResponseDto insertarInformacionCoordinadoFase2(
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(sustentacionDto.getIdTrabajoGrados());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                trabajoGrado.setNumeroEstado(14);

                agregarInformacionCoordinadorFase2(sustentacionProyectoInvestigacion, sustentacionDto);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacion);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase2(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto) {

                if (!sustentacionDto.getJuradosAceptados()) {
                        sustentacionProyectoInvestigacion
                                        .setIdJuradoInterno(Long.parseLong(sustentacionDto.getIdJuradoInterno()));
                        sustentacionProyectoInvestigacion
                                        .setIdJuradoExterno(Long.parseLong(sustentacionDto.getIdJuradoExterno()));
                }
                sustentacionProyectoInvestigacion
                                .setNumeroActa(sustentacionDto.getNumeroActa());
                sustentacionProyectoInvestigacion
                                .setFechaActa(sustentacionDto.getFechaActa());
        }

        @Override
        @Transactional
        public SustentacionTrabajoInvestigacionEstudianteResponseDto insertarInformacionEstudiante(
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(sustentacionDto.getIdTrabajoGrados());

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;
                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                // sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);
                trabajoGrado.setNumeroEstado(16);

                // Guardar la entidad SustentacionProyectoInvestigacion

                sustentacionProyectoInvestigacion.setLinkFormatoH(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkFormatoH(),
                                                nombreCarpeta));

                sustentacionProyectoInvestigacion.setLinkFormatoI(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkFormatoI(),
                                                nombreCarpeta));

                agregarInformacionEstudiante(sustentacionProyectoInvestigacionTmp, sustentacionDto);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toEstudianteDto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionEstudiante(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto) {
                sustentacionProyectoInvestigacion
                                .setLinkFormatoH(sustentacionDto.getLinkFormatoH());
                sustentacionProyectoInvestigacion
                                .setLinkFormatoI(sustentacionDto.getLinkFormatoI());
        }

        @Override
        @Transactional
        public STICoordinadorFase3ResponseDto insertarInformacionCoordinadoFase3(
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(sustentacionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + sustentacionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp = sustentacionProyectoInvestigacionRepository
                                .findByTrabajoGradoId(sustentacionDto.getIdTrabajoGrados());

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(sustentacionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Sustentacion_Proyecto_Investigacion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion = sustentacionProyectoIngestigacionMapper
                                .toEntity(sustentacionDto);

                // Establecer la relación uno a uno
                // sustentacionProyectoInvestigacion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionProyectoInvestigacion);

                // Se cambia el numero de estado
                // int numEstado = validarEstado(sustentacionDto.getRespuestaSustentacion());
                // trabajoGrado.setNumeroEstado(numEstado);
                trabajoGrado.setNumeroEstado(17);

                // Guardar la entidad SustentacionProyectoInvestigacion

                sustentacionProyectoInvestigacion.setLinkActaSustentacionPublica(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion.getLinkActaSustentacionPublica(),
                                                nombreCarpeta));

                sustentacionProyectoInvestigacion.setLinkEstudioHojaVidaAcademicaGrado(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                sustentacionProyectoInvestigacion
                                                                .getLinkEstudioHojaVidaAcademicaGrado(),
                                                nombreCarpeta));

                agregarInformacionCoordinadorFase3(sustentacionProyectoInvestigacionTmp, sustentacionDto);

                SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionRes = sustentacionProyectoInvestigacionRepository
                                .save(sustentacionProyectoInvestigacionTmp);

                return sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionProyectoInvestigacionRes);
        }

        private void agregarInformacionCoordinadorFase3(
                        SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto) {
                sustentacionProyectoInvestigacion
                                .setLinkActaSustentacionPublica(sustentacionDto.getLinkActaSustentacionPublica());
                sustentacionProyectoInvestigacion
                                .setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
                sustentacionProyectoInvestigacion
                                .setLinkEstudioHojaVidaAcademicaGrado(
                                                sustentacionDto.getLinkEstudioHojaVidaAcademicaGrado());
                sustentacionProyectoInvestigacion
                                .setNumeroActaFinal(sustentacionDto.getNumeroActaFinal());
                sustentacionProyectoInvestigacion
                                .setFechaActaFinal(sustentacionDto.getFechaActaFinal());
        }

        // @Override
        // @Transactional(readOnly = true)
        // public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado) {
        // return
        // sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
        // .stream()
        // .map(sustentacionProyectoIngestigacionMapper::toDto)
        // .findFirst()
        // .orElse(null);
        // }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toDocenteDto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toCoordinadorFase1Dto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado) {

                Optional<SustentacionTrabajoInvestigacion> entityOptional = sustentacionProyectoInvestigacionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);

                // SolicitudExamenValoracion entity = entityOptional.get();
                STICoordinadorFase2ResponseDto responseDto = sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(entityOptional.get());

                // Obtener y construir información del evaluador interno
                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(entityOptional.get().getIdJuradoInterno());
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                Map<String, String> evaluadorInternoMap = new HashMap<>();
                evaluadorInternoMap.put("nombres", nombre_docente);
                evaluadorInternoMap.put("universidad", "Universidad del Cauca");
                evaluadorInternoMap.put("correo", docente.getPersona().getCorreoElectronico());

                // Obtener y construir información del evaluador externo
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(entityOptional.get().getIdJuradoExterno());
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();
                Map<String, String> evaluadorExternoMap = new HashMap<>();
                evaluadorExternoMap.put("nombres", nombre_experto);
                evaluadorExternoMap.put("universidad", experto.getUniversidad());
                evaluadorExternoMap.put("correo", experto.getPersona().getCorreoElectronico());

                // STICoordinadorFase2ResponseDto coordinadorFase2ResponseDto = new
                // STICoordinadorFase2ResponseDto();
                // coordinadorFase2ResponseDto.setIdSustentacionTI(idTrabajoGrado);
                // coordinadorFase2ResponseDto.setLinkFormatoF(nombre_experto);
                // coordinadorFase2ResponseDto.setUrlDocumentacion(nombre_experto);
                // coordinadorFase2ResponseDto.setLinkFormatoG(nombre_experto);
                // coordinadorFase2ResponseDto.setLinkEstudioHojaVidaAcademica(nombre_experto);
                responseDto.setJuradoInterno(evaluadorExternoMap);
                responseDto.setJuradoExterno(evaluadorExternoMap);
                // coordinadorFase2ResponseDto.setNumeroActa();
                // coordinadorFase2ResponseDto.setFechaActa(null);

                return responseDto;
        }

        @Override
        @Transactional(readOnly = true)
        public SustentacionTrabajoInvestigacionEstudianteResponseDto listarInformacionEstudiante(Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toEstudianteDto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public STICoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado) {
                return sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(sustentacionProyectoInvestigacionResponseMapper::toCoordinadorFase3Dto)
                                .findFirst()
                                .orElse(null);
        }
        // @Override
        // @Transactional(readOnly = true)
        // public SustentacionTrabajoInvestigacionComiteResponseDto
        // listarInformacionComite(Long idTrabajoGrado) {
        // Optional<SustentacionTrabajoInvestigacion> responseDto =
        // sustentacionProyectoInvestigacionRepository
        // .findByIdTrabajoGradoId(idTrabajoGrado);
        // if (responseDto.isPresent()) {
        // return
        // sustentacionProyectoInvestigacionResponseMapper.toComiteDto(responseDto.get());
        // } else {
        // return null;
        // }
        // }

        // @Override
        // @Transactional(readOnly = true)
        // public SustentacionTrabajoInvestigacionDto listarInformacionCoordinador(Long
        // idTrabajoGrado) {
        // return
        // sustentacionProyectoInvestigacionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
        // .stream()
        // .map(sustentacionProyectoIngestigacionMapper::toDto)
        // .findFirst()
        // .orElse(null);
        // }

        // @Override
        // public SustentacionTrabajoInvestigacionDto actualizar(Long id,
        // SustentacionTrabajoInvestigacionDto sustentacionDto, BindingResult result) {

        // if (result.hasErrors()) {
        // throw new FieldErrorException(result);
        // }

        // SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacionTmp =
        // sustentacionProyectoInvestigacionRepository
        // .findById(id)
        // .orElseThrow(() -> new ResourceNotFoundException(
        // "Sustentacion trabajo de investigacion con id: " + id
        // + " no encontrado"));

        // // Busca el trabajo de grado
        // TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
        // () -> new ResourceNotFoundException(
        // "Trabajo de grado con id: " + id + " no encontrado"));

        // String procesoVa = "Sustentacion_Proyecto_Investigacion";
        // String tituloTrabajoGrado =
        // ConvertString.obtenerIniciales(trabajoGrado.getTitulo());
        // Long idenficiacionEstudiante =
        // trabajoGrado.getEstudiante().getPersona().getIdentificacion();
        // String nombreEstudiante =
        // trabajoGrado.getEstudiante().getPersona().getNombre();
        // String apellidoEstudiante =
        // trabajoGrado.getEstudiante().getPersona().getApellido();
        // String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_"
        // + apellidoEstudiante;

        // SustentacionTrabajoInvestigacion responseSustentacionProyectoInvestigacion =
        // null;
        // if (sustentacionProyectoInvestigacionTmp != null) {
        // if (sustentacionDto.getLinkRemisionDocumentoFinal()
        // .compareTo(sustentacionProyectoInvestigacionTmp
        // .getLinkRemisionDocumentoFinal()) != 0) {
        // sustentacionDto
        // .setLinkRemisionDocumentoFinal(FilesUtilities.guardarArchivoNew(
        // tituloTrabajoGrado, procesoVa,
        // sustentacionDto.getLinkRemisionDocumentoFinal(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // sustentacionProyectoInvestigacionTmp.getLinkRemisionDocumentoFinal());
        // }
        // if (sustentacionDto.getLinkRemisionDocumentoFinalCF()
        // .compareTo(sustentacionProyectoInvestigacionTmp
        // .getLinkRemisionDocumentoFinalCF()) != 0) {
        // sustentacionDto
        // .setLinkRemisionDocumentoFinalCF(FilesUtilities.guardarArchivoNew(
        // tituloTrabajoGrado, procesoVa,
        // sustentacionDto.getLinkRemisionDocumentoFinalCF(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // sustentacionProyectoInvestigacionTmp.getLinkRemisionDocumentoFinalCF());
        // }
        // if (sustentacionDto.getLinkConstanciaDocumentoFinal()
        // .compareTo(sustentacionProyectoInvestigacionTmp
        // .getLinkConstanciaDocumentoFinal()) != 0) {
        // sustentacionDto
        // .setLinkConstanciaDocumentoFinal(FilesUtilities.guardarArchivoNew(
        // tituloTrabajoGrado, procesoVa,
        // sustentacionDto.getLinkConstanciaDocumentoFinal(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // sustentacionProyectoInvestigacionTmp.getLinkConstanciaDocumentoFinal());
        // }
        // if (sustentacionDto.getLinkActaSustentacion()
        // .compareTo(sustentacionProyectoInvestigacionTmp
        // .getLinkActaSustentacion()) != 0) {
        // sustentacionDto
        // .setLinkActaSustentacion(FilesUtilities.guardarArchivoNew(
        // tituloTrabajoGrado, procesoVa,
        // sustentacionDto.getLinkActaSustentacion(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // sustentacionProyectoInvestigacionTmp.getLinkActaSustentacion());
        // }
        // if (sustentacionDto.getLinkActaSustentacionPublica()
        // .compareTo(sustentacionProyectoInvestigacionTmp
        // .getLinkActaSustentacionPublica()) != 0) {
        // sustentacionDto
        // .setLinkActaSustentacionPublica(FilesUtilities.guardarArchivoNew(
        // tituloTrabajoGrado, procesoVa,
        // sustentacionDto.getLinkActaSustentacionPublica(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // sustentacionProyectoInvestigacionTmp.getLinkActaSustentacionPublica());
        // }
        // if (sustentacionDto.getLinkEstudioHojaVidaAcademica()
        // .compareTo(sustentacionProyectoInvestigacionTmp
        // .getLinkEstudioHojaVidaAcademica()) != 0) {
        // sustentacionDto
        // .setLinkEstudioHojaVidaAcademica(FilesUtilities.guardarArchivoNew(
        // tituloTrabajoGrado, procesoVa,
        // sustentacionDto.getLinkEstudioHojaVidaAcademica(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // sustentacionProyectoInvestigacionTmp.getLinkEstudioHojaVidaAcademica());
        // }
        // updateSustentacionProyectoInvestigacionValues(sustentacionProyectoInvestigacionTmp,
        // sustentacionDto);
        // responseSustentacionProyectoInvestigacion =
        // sustentacionProyectoInvestigacionRepository
        // .save(sustentacionProyectoInvestigacionTmp);
        // }
        // return
        // sustentacionProyectoIngestigacionMapper.toDto(responseSustentacionProyectoInvestigacion);
        // }

        // // Funciones privadas
        // private void updateSustentacionProyectoInvestigacionValues(
        // SustentacionTrabajoInvestigacion sustentacionProyectoInvestigacion,
        // SustentacionTrabajoInvestigacionDto sustentacionDto) {
        // sustentacionProyectoInvestigacion.setUrlDocumentacion(sustentacionDto.getUrlDocumentacion());
        // sustentacionProyectoInvestigacion.setRespuestaSustentacion(sustentacionDto.getRespuestaSustentacion());
        // sustentacionProyectoInvestigacion
        // .setNumeroActaTrabajoFinal(sustentacionDto.getNumeroActaTrabajoFinal());
        // sustentacionProyectoInvestigacion.setFechaActa(sustentacionDto.getFechaActa());
        // // Update archivos
        // sustentacionProyectoInvestigacion
        // .setLinkRemisionDocumentoFinal(sustentacionDto.getLinkRemisionDocumentoFinal());
        // sustentacionProyectoInvestigacion
        // .setLinkRemisionDocumentoFinalCF(sustentacionDto.getLinkRemisionDocumentoFinalCF());
        // sustentacionProyectoInvestigacion
        // .setLinkConstanciaDocumentoFinal(sustentacionDto.getLinkConstanciaDocumentoFinal());
        // sustentacionProyectoInvestigacion.setLinkActaSustentacion(sustentacionDto.getLinkActaSustentacion());
        // sustentacionProyectoInvestigacion
        // .setLinkActaSustentacionPublica(sustentacionDto.getLinkActaSustentacionPublica());
        // sustentacionProyectoInvestigacion
        // .setLinkEstudioHojaVidaAcademica(sustentacionDto.getLinkEstudioHojaVidaAcademica());
        // }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
        }

        private int validarEstado(Boolean estado) {
                int numEstado = 0;
                if (!estado) {
                        numEstado = 8;
                } else {
                        numEstado = 9;
                }
                return numEstado;
        }

        private CamposUnicosSustentacionProyectoInvestigacionDto obtenerCamposUnicos(
                        SustentacionTrabajoInvestigacionDto respuesta) {
                return informacionUnicaSustentacionProyectoInvestigacion.apply(respuesta);
        }

        private Map<String, String> validacionCampoUnicos(CamposUnicosSustentacionProyectoInvestigacionDto camposUnicos,
                        CamposUnicosSustentacionProyectoInvestigacionDto camposUnicosBD) {

                Map<String, Function<CamposUnicosSustentacionProyectoInvestigacionDto, Boolean>> mapCamposUnicos = new HashMap<>();

                mapCamposUnicos.put("idTrabajoGrados",
                                dto -> (camposUnicosBD == null || !dto.getIdTrabajoGrados()
                                                .equals(camposUnicosBD.getIdTrabajoGrados()))
                                                && sustentacionProyectoInvestigacionRepository
                                                                .countByTrabajoGradoId(dto.getIdTrabajoGrados()) > 0);

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
                return "Campo único, ya se ha registrado una SUSTENTACION PROYECTO DE INVESTIGACION al trabajo de grado: "
                                + valorCampo;
        }

        @Override
        @Transactional(readOnly = true)
        public Boolean verificarEgresado(Long idTrabajoGrado) {
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + idTrabajoGrado + " no encontrado"));

                List<CursoSaveDto> cursos = archivoClientEgresados
                                .obtenerCursosPorIdEstudiante(trabajoGrado.getIdEstudiante());
                List<EmpresaSaveDto> empresas = archivoClientEgresados
                                .obtenerEmpresasPorIdEstudiante(trabajoGrado.getIdEstudiante());
                
                if(cursos.size() == 0 || empresas.size() == 0){
                        return false;
                }

                return true;
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
                                        .titulo(trabajo.getTitulo() != null ? trabajo.getTitulo()
                                                        : "Título no disponible")
                                        .numeroEstado(trabajo.getNumeroEstado())
                                        .build();
                }).collect(Collectors.toList());
                return trabajosGradoDto;
        }
}
