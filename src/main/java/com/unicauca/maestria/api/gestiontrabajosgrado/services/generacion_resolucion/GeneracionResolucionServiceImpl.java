package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.EstadoTrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.Constants;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.CamposUnicosGenerarResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.EnvioEmailCorrecionesDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.InformacionEnvioComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.ObtenerDocumentosParaEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneracionResolucionServiceImpl implements GeneracionResolucionService {

        private final GeneracionResolucionRepository generacionResolucionRepository;
        private final GeneracionResolucionMapper generacionResolucionMapper;
        private final GeneracionResolucionResponseMapper generacionResolucionResponseMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;
        private final InformacionUnicaGeneracionResolucion informacionUnicaGeneracionResolucion;

        @Autowired
        private JavaMailSender mailSender;

        @Autowired
        private SpringTemplateEngine templateEngine;

        @Override
        @Transactional(readOnly = true)
        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector() {
                List<DirectorAndCodirectorResponseDto> docentesYExpertos = new ArrayList<>();

                List<DocenteResponseDto> listadoDocentes = archivoClient.listarDocentesRes();
                List<ExpertoResponseDto> listadoExpertos = archivoClientExpertos.listar();
                List<DirectorAndCodirectorResponseDto> docentes = listadoDocentes.stream()
                                .map(docente -> new DirectorAndCodirectorResponseDto(
                                                docente.getPersona().getTipoIdentificacion(),
                                                docente.getPersona().getIdentificacion(),
                                                docente.getPersona().getNombre(),
                                                docente.getPersona().getApellido()))
                                .collect(Collectors.toList());
                List<DirectorAndCodirectorResponseDto> expertos = listadoExpertos.stream()
                                .map(experto -> new DirectorAndCodirectorResponseDto(
                                                experto.getPersona().getTipoIdentificacion(),
                                                experto.getPersona().getIdentificacion(),
                                                experto.getPersona().getNombre(),
                                                experto.getPersona().getApellido()))
                                .collect(Collectors.toList());

                docentesYExpertos.addAll(docentes);
                docentesYExpertos.addAll(expertos);

                return docentesYExpertos;
        }

        @Override
        @Transactional
        public GeneracionResolucionDocenteResponseDto insertarInformacionDocente(
                        GeneracionResolucionDocenteDto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Generacion_Resolucion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Establecer la relación uno a uno
                generarResolucion.setIdTrabajoGrado(trabajoGrado);
                trabajoGrado.setIdGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(8);

                // Guardar la entidad ExamenValoracion
                generarResolucion.setLinkAnteproyectoFinal(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                generarResolucion.getLinkAnteproyectoFinal(), nombreCarpeta));
                generarResolucion
                                .setLinkSolicitudComite(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                generarResolucion.getLinkSolicitudComite(), nombreCarpeta));

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository.save(generarResolucion);

                return generacionResolucionResponseMapper.toDocenteDto(generarResolucionRes);
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase1ResponseDto insertarInformacionCoordinadorFase1(
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(generacionResolucionDto.getIdTrabajoGrados());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Se cambia el numero de estado
                if (generacionResolucionDto.getConceptoDocumentosCoordinador()) {
                        trabajoGrado.setNumeroEstado(9);
                } else {
                        trabajoGrado.setNumeroEstado(9);
                }

                generacionResolucion.setConceptoDocumentosCoordinador(
                                generacionResolucionDto.getConceptoDocumentosCoordinador());

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucion);

                return generacionResolucionResponseMapper.toCoordinadorFase1Dto(generarResolucionRes);
        }

        @Override
        @Transactional(readOnly = true)
        public ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnviarAlComite(Long idGeneracionResolucion) {

                GeneracionResolucion generacionResolucion = generacionResolucionRepository
                                .findByTrabajoGradoId(idGeneracionResolucion);

                ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnviar = new ObtenerDocumentosParaEnvioDto(
                                FilesUtilities.recuperarArchivo(generacionResolucion.getLinkAnteproyectoFinal()),
                                FilesUtilities.recuperarArchivo(generacionResolucion.getLinkSolicitudComite()));

                return obtenerDocumentosParaEnviar;
        }

        @Override
        @Transactional(readOnly = true)
        public InformacionEnvioComiteDto enviarCorreoComite(InformacionEnvioComiteDto informacionEnvioComiteDto) {
                try {
                        Map<String, Object> templateModel = new HashMap<>();

                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true);

                        templateModel.put("mensaje", informacionEnvioComiteDto.getMensaje());

                        // Crear el contexto para el motor de plantillas
                        Context context = new Context();
                        context.setVariables(templateModel);

                        // Procesar la plantilla de correo electrónico
                        String html = templateEngine.process("emailTemplate", context);

                        helper.setTo(Constants.correoComite);
                        helper.setSubject(informacionEnvioComiteDto.getAsunto());
                        helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

                        // Obtener documentos y adjuntarlos
                        Map<String, Object> documentosParaEvaluador = informacionEnvioComiteDto.getDocumentos();

                        for (Map.Entry<String, Object> entry : documentosParaEvaluador.entrySet()) {
                                String nombreDocumento = entry.getKey();
                                Object valorDocumento = entry.getValue();

                                if (valorDocumento instanceof String) {
                                        // Manejar los documentos que son cadenas
                                        String base64Documento = (String) valorDocumento;
                                        byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
                                        ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes,
                                                        "application/pdf");
                                        helper.addAttachment(nombreDocumento + ".pdf", dataSource);
                                }
                        }

                        // Enviar el mensaje
                        mailSender.send(message);

                        return informacionEnvioComiteDto;
                } catch (MessagingException e) {
                        throw new InformationException(e.getMessage());
                }
        }

        @Override
        @Transactional(readOnly = true)
        public EnvioEmailCorrecionesDto enviarCorreoCorrecion(EnvioEmailCorrecionesDto envioEmailCorrecionesDto) {
                try {
                        ArrayList<String> correos = new ArrayList<>();

                        TrabajoGrado trabajoGrado = trabajoGradoRepository
                                        .findById(envioEmailCorrecionesDto.getIdTrabajoGrados())
                                        .orElseThrow(() -> new ResourceNotFoundException("Trabajo de grado con id: "
                                                        + envioEmailCorrecionesDto.getIdTrabajoGrados()
                                                        + " no encontrado"));

                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                        correos.add(estudiante.getPersona().getCorreoElectronico());
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());

                        // Iterar sobre cada correo electrónico para enviar los mensajes individualmente
                        for (String correo : correos) {
                                MimeMessage message = mailSender.createMimeMessage();
                                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                                Map<String, Object> templateModel = new HashMap<>();
                                templateModel.put("title",
                                                envioEmailCorrecionesDto.getAsunto());
                                templateModel.put("message", envioEmailCorrecionesDto.getMensaje());

                                Context context = new Context();
                                context.setVariables(templateModel);

                                String html = templateEngine.process("emailTemplate", context);

                                helper.setTo(correo);
                                helper.setSubject(envioEmailCorrecionesDto.getAsunto());
                                helper.setText(html, true);

                                mailSender.send(message);
                        }

                        return envioEmailCorrecionesDto;
                } catch (MessagingException e) {
                        throw new InformationException(e.getMessage());
                }
        }

        @Override
        @Transactional
        public GeneracionResolucionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(generacionResolucionDto.getIdTrabajoGrados());

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Generacion_Resolucion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                GeneracionResolucion generarResolucion = generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(9);

                // Guardar la entidad ExamenValoracion
                generarResolucion.setLinkSolicitudConsejoFacultad(
                                FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                generarResolucion.getLinkSolicitudConsejoFacultad(), nombreCarpeta));

                agregarInformacionCoordinadorFase2(generacionResolucionTmp, generacionResolucionDto);

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                enviarCorreoConsejo(generacionResolucionDto);

                return generacionResolucionResponseMapper.toCoordinadorFase2Dto(generarResolucionRes);
        }

        // Funciones privadas
        private void agregarInformacionCoordinadorFase2(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionDto) {
                generacionResolucion.setNumeroActaSolicitudComite(
                                generacionResolucionDto.getNumeroActaSolicitudComite());
                generacionResolucion.setFechaActaSolicitudComite(generacionResolucionDto.getFechaActaSolicitudComite());
                generacionResolucion.setLinkSolicitudConsejoFacultad(
                                generacionResolucionDto.getLinkSolicitudConsejoFacultad());
        }

        private boolean enviarCorreoConsejo(
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto) {
                try {
                        Map<String, Object> templateModel = new HashMap<>();

                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true); // habilitar modo
                                                                                         // multipart

                        // Configurar variables del contexto para la plantilla
                        // templateModel.put("nombreEvaluador", "Nombre del Evaluador");
                        templateModel.put("mensaje",
                                        generacionResolucionCoordinadorFase2Dto.getEnvioEmailCorrecionesDto()
                                                        .getMensaje());

                        // Crear el contexto para el motor de plantillas
                        Context context = new Context();
                        context.setVariables(templateModel);

                        // Procesar la plantilla de correo electrónico
                        String html = templateEngine.process("emailTemplate", context);

                        helper.setTo(Constants.correoConsejo);
                        helper.setSubject(generacionResolucionCoordinadorFase2Dto.getEnvioEmailCorrecionesDto()
                                        .getAsunto());
                        helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

                        // Obtener documentos y adjuntarlos
                        Map<String, Object> documentosParaEvaluador = new HashMap<>();

                        // Agregar un String manualmente
                        String[] solicitudConsejoFacultad = generacionResolucionCoordinadorFase2Dto
                                        .getLinkSolicitudConsejoFacultad()
                                        .split("-");
                        documentosParaEvaluador.put("Solicitud_Consejo_Facultad", solicitudConsejoFacultad[1]);

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
        public GeneracionResolucionCoordinadorFase3ResponseDto insertarInformacionCoordinadorFase3(
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(generacionResolucionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: " + generacionResolucionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                GeneracionResolucion generacionResolucionTmp = generacionResolucionRepository
                                .findByTrabajoGradoId(generacionResolucionDto.getIdTrabajoGrados());

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                // Map<String, String> validacionCamposUnicos = validacionCampoUnicos(
                // obtenerCamposUnicos(generacionResolucionDto),
                // null);
                // if (!validacionCamposUnicos.isEmpty()) {
                // throw new FieldUniqueException(validacionCamposUnicos);
                // }

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Generacion_Resolucion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                // GeneracionResolucion generarResolucion =
                // generacionResolucionMapper.toEntity(generacionResolucionDto);

                // Establecer la relación uno a uno
                // generarResolucion.setIdTrabajoGrado(trabajoGrado);
                // trabajoGrado.setIdGeneracionResolucion(generarResolucion);

                // Se cambia el numero de estado
                trabajoGrado.setNumeroEstado(10);

                agregarInformacionCoordinadorFase3(generacionResolucionTmp, generacionResolucionDto);

                GeneracionResolucion generarResolucionRes = generacionResolucionRepository
                                .save(generacionResolucionTmp);

                return generacionResolucionResponseMapper.toCoordinadorFase3Dto(generarResolucionRes);
        }

        // Funciones privadas
        private void agregarInformacionCoordinadorFase3(GeneracionResolucion generacionResolucion,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDto) {
                generacionResolucion.setNumeroActaConsejoFacultad(
                                generacionResolucionDto.getNumeroActaConsejoFacultad());
                generacionResolucion.setFechaActaConsejoFacultad(generacionResolucionDto.getFechaActaConsejoFacultad());
        }

        // @Override
        // @Transactional(readOnly = true)
        // public GeneracionResolucionDto buscarPorId(Long idTrabajoGrado) {
        // return generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
        // .stream()
        // .map(generacionResolucionMapper::toDto)
        // .findFirst()
        // .orElse(null);
        // }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado) {
                Optional<GeneracionResolucion> responseDto = generacionResolucionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);
                if (responseDto.isPresent()) {
                        return generacionResolucionResponseMapper.toDocenteDto(responseDto.get());
                } else {
                        return null;
                }
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado) {
                return generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(generacionResolucionResponseMapper::toCoordinadorFase2Dto)
                                .findFirst()
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public GeneracionResolucionCoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(Long idTrabajoGrado) {
                return generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(generacionResolucionResponseMapper::toCoordinadorFase3Dto)
                                .findFirst()
                                .orElse(null);
        }

        // @Override
        // public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto
        // generacionResolucionDto,
        // BindingResult result) {

        // if (result.hasErrors()) {
        // throw new FieldErrorException(result);
        // }

        // GeneracionResolucion generacionResolucionTmp =
        // generacionResolucionRepository.findById(id).orElseThrow(
        // () -> new ResourceNotFoundException(
        // "Generacion de resolucion con id: " + id + " no encontrado"));

        // // Busca el trabajo de grado
        // TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
        // () -> new ResourceNotFoundException(
        // "Trabajo de grado con id: " + id + " no encontrado"));

        // String procesoVa = "Generacion_Resolucion";
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

        // GeneracionResolucion responseExamenValoracion = null;
        // if (generacionResolucionTmp != null) {
        // if (generacionResolucionDto.getLinkAnteproyectoAprobado()
        // .compareTo(generacionResolucionTmp.getLinkAnteproyectoAprobado()) != 0) {
        // generacionResolucionDto
        // .setLinkAnteproyectoAprobado(FilesUtilities
        // .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generacionResolucionDto
        // .getLinkAnteproyectoAprobado(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkAnteproyectoAprobado());
        // }
        // if (generacionResolucionDto.getLinkSolicitudComite()
        // .compareTo(generacionResolucionTmp.getLinkSolicitudComite()) != 0) {
        // generacionResolucionDto
        // .setLinkSolicitudComite(FilesUtilities
        // .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generacionResolucionDto
        // .getLinkSolicitudComite(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkSolicitudComite());
        // }
        // if (generacionResolucionDto.getLinkSolicitudConcejoFacultad()
        // .compareTo(generacionResolucionTmp.getLinkSolicitudConcejoFacultad()) != 0) {
        // generacionResolucionDto
        // .setLinkSolicitudConcejoFacultad(FilesUtilities
        // .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generacionResolucionDto
        // .getLinkSolicitudConcejoFacultad(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(
        // generacionResolucionTmp.getLinkSolicitudConcejoFacultad());
        // }
        // if (generacionResolucionDto.getLinkResolucionGeneradaCF()
        // .compareTo(generacionResolucionTmp.getLinkResolucionGeneradaCF()) != 0) {
        // generacionResolucionDto
        // .setLinkResolucionGeneradaCF(FilesUtilities
        // .guardarArchivoNew(tituloTrabajoGrado, procesoVa,
        // generacionResolucionDto
        // .getLinkResolucionGeneradaCF(),
        // nombreCarpeta));
        // FilesUtilities.deleteFileExample(generacionResolucionTmp.getLinkResolucionGeneradaCF());
        // }
        // // Repetir esto
        // updateRtaExamenValoracionValues(generacionResolucionTmp,
        // generacionResolucionDto);
        // responseExamenValoracion =
        // generacionResolucionRepository.save(generacionResolucionTmp);
        // }
        // return generacionResolucionMapper.toDto(responseExamenValoracion);
        // }

        // // Funciones privadas
        // private void updateRtaExamenValoracionValues(GeneracionResolucion
        // generacionResolucion,
        // GeneracionResolucionDto generacionResolucionDto) {
        // generacionResolucion.setDirector(generacionResolucionDto.getDirector());
        // generacionResolucion.setCodirector(generacionResolucionDto.getCodirector());
        // generacionResolucion.setNumeroActaRevision(generacionResolucionDto.getNumeroActaRevision());
        // generacionResolucion.setFechaActa(generacionResolucionDto.getFechaActa());
        // generacionResolucion
        // .setNumeroResolucionGeneradaCF(
        // generacionResolucionDto.getNumeroResolucionGeneradaCF());
        // generacionResolucion.setFechaResolucion(generacionResolucionDto.getFechaResolucion());
        // // Update archivos
        // generacionResolucion.setLinkAnteproyectoAprobado(generacionResolucionDto.getLinkAnteproyectoAprobado());
        // generacionResolucion.setLinkSolicitudComite(generacionResolucionDto.getLinkAnteproyectoAprobado());
        // generacionResolucion.setLinkSolicitudConcejoFacultad(
        // generacionResolucionDto.getLinkSolicitudConcejoFacultad());
        // generacionResolucion.setLinkResolucionGeneradaCF(generacionResolucionDto.getLinkResolucionGeneradaCF());
        // }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
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

        private CamposUnicosGenerarResolucionDto obtenerCamposUnicos(
                        GeneracionResolucionDto respuesta) {
                return informacionUnicaGeneracionResolucion.apply(respuesta);
        }

        private Map<String, String> validacionCampoUnicos(CamposUnicosGenerarResolucionDto camposUnicos,
                        CamposUnicosGenerarResolucionDto camposUnicosBD) {

                Map<String, Function<CamposUnicosGenerarResolucionDto, Boolean>> mapCamposUnicos = new HashMap<>();

                mapCamposUnicos.put("idTrabajoGrados",
                                dto -> (camposUnicosBD == null || !dto.getIdTrabajoGrados()
                                                .equals(camposUnicosBD.getIdTrabajoGrados()))
                                                && generacionResolucionRepository
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
                return "Campo único, ya se ha registrado una GENERACION DE RESOLUCION al trabajo de grado: "
                                + valorCampo;
        }

}
