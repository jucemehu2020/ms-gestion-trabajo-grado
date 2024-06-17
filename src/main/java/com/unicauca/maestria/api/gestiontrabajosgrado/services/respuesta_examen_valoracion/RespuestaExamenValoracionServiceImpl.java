package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.ObtenerDocumentosParaEnvioCorreoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionInformacionGeneralDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Fase2.ExamenValoracionCanceladoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.ObtenerDocumentosParaEvaluadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosRespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaExamenValoracionServiceImpl implements RespuestaExamenValoracionService {

        private final RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
        private final ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
        private final AnexosRespuestaExamenValoracionRepository anexosRespuestaExamenValoracionRepository;
        private final RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
        private final ExamenValoracionCanceladoMapper examenValoracionCanceladoMapper;
        private final TrabajoGradoRepository trabajoGradoRepository;

        // Extras
        private final SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        private final ArchivoClient archivoClient;
        private final ArchivoClientExpertos archivoClientExpertos;

        @Autowired
        private JavaMailSender mailSender;

        @Autowired
        private SpringTemplateEngine templateEngine;

        @Override
        @Transactional
        public RespuestaExamenValoracionDto crear(RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(
                                                respuestaExamenValoracionDto.getIdTrabajoGrados());

                if (numeroNoAprobado < 4) {

                        TrabajoGrado trabajoGrado = trabajoGradoRepository
                                        .findById(respuestaExamenValoracionDto.getIdTrabajoGrados())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "TrabajoGrado con id: "
                                                                        + respuestaExamenValoracionDto
                                                                                        .getIdTrabajoGrados()
                                                                        + " No encontrado"));

                        EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                        // Obtener iniciales del trabajo de grado
                        String procesoVa = "Respuesta_Examen_Valoracion";
                        String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                        Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                        String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                        String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                        String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_"
                                        + apellidoEstudiante;

                        // Mapear DTO a entidad
                        RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionMapper
                                        .toEntity(respuestaExamenValoracionDto);

                        // Se cambia el numero de estado
                        // int numEstado =
                        // validarEstado(respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                        // trabajoGrado.setNumeroEstado(numEstado);

                        int numEstado = validarEstado(respuestaExamenValoracionDto.getIdTrabajoGrados(),
                                        rtaExamenValoracion.getRespuestaExamenValoracion());
                        trabajoGrado.setNumeroEstado(numEstado);

                        // Guardar la entidad ExamenValoracion
                        rtaExamenValoracion
                                        .setLinkFormatoB(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                        rtaExamenValoracion.getLinkFormatoB(), nombreCarpeta));
                        rtaExamenValoracion
                                        .setLinkFormatoC(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                        rtaExamenValoracion.getLinkFormatoC(), nombreCarpeta));
                        rtaExamenValoracion.setLinkObservaciones(
                                        FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                        rtaExamenValoracion.getLinkObservaciones(), nombreCarpeta));

                        List<AnexoRespuestaExamenValoracion> updatedLinkAnexos = new ArrayList<>();
                        for (AnexoRespuestaExamenValoracion linkAnexoDto : respuestaExamenValoracionDto.getAnexos()) {
                                String updatedAnexo = FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                                linkAnexoDto.getLinkAnexo(),
                                                nombreCarpeta);
                                AnexoRespuestaExamenValoracion anexo = new AnexoRespuestaExamenValoracion();
                                anexo.setLinkAnexo(updatedAnexo);
                                anexo.setRespuestaExamenValoracion(rtaExamenValoracion);
                                updatedLinkAnexos.add(anexo);
                        }
                        rtaExamenValoracion.setAnexos(updatedLinkAnexos);

                        // Se asigna al trabajo de grado
                        rtaExamenValoracion.setTrabajoGrado(trabajoGrado);

                        enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                        RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository
                                        .save(rtaExamenValoracion);

                        return respuestaExamenValoracionMapper.toDto(examenValoracionRes);
                } else {
                        throw new InformationException("Ya no es permitido registrar mas respuestas");
                }
        }

        @Override
        @Transactional
        public ExamenValoracionCanceladoDto insertarInformacionCancelado(
                        ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result) {

                Long numeroNoAprobado = respuestaExamenValoracionRepository
                                .countByTrabajoGradoIdAndRespuestaNoAprobado(
                                                examenValoracionCanceladoDto.getIdTrabajoGrados());

                if (numeroNoAprobado == 4) {

                        if (result.hasErrors()) {
                                throw new FieldErrorException(result);
                        }

                        TrabajoGrado trabajoGrado = trabajoGradoRepository
                                        .findById(examenValoracionCanceladoDto.getIdTrabajoGrados())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "TrabajoGrado con id: "
                                                                        + examenValoracionCanceladoDto
                                                                                        .getIdTrabajoGrados()
                                                                        + " No encontrado"));

                        // Map<String, String> validacionCamposUnicos =
                        // validacionCampoUnicos(obtenerCamposUnicos(examenValoracionDto),
                        // null);
                        //
                        // if (!validacionCamposUnicos.isEmpty()) {
                        // throw new FieldUniqueException(validacionCamposUnicos);
                        // }
                        //

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
                try {
                        ArrayList<String> correos = new ArrayList<>();
                        Map<String, Object> templateModel = new HashMap<>();

                        // Obtener correo tutor
                        correos.add(trabajoGrado.getCorreoElectronicoTutor());

                        // Obtener correo estudiante
                        EstudianteResponseDtoAll estudiante = archivoClient
                                        .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());
                        correos.add(estudiante.getPersona().getCorreoElectronico());

                        // Iterar sobre cada correo electrónico para enviar los mensajes individualmente
                        for (String correo : correos) {
                                MimeMessage message = mailSender.createMimeMessage();
                                MimeMessageHelper helper = new MimeMessageHelper(message, true); // habilitar modo
                                                                                                 // multipart

                                // Configurar variables del contexto para la plantilla
                                // templateModel.put("nombreEvaluador", "Nombre del Evaluador");
                                templateModel.put("mensaje",
                                                respuestaExamenValoracionDto.getInformacionEnvioDto()
                                                                .getMensaje());

                                // Crear el contexto para el motor de plantillas
                                Context context = new Context();
                                context.setVariables(templateModel);

                                // Procesar la plantilla de correo electrónico
                                String html = templateEngine.process("emailTemplate", context);

                                helper.setTo(correo);
                                helper.setSubject(
                                                respuestaExamenValoracionDto.getInformacionEnvioDto().getAsunto());
                                helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

                                // Obtener documentos y adjuntarlos
                                Map<String, Object> documentosParaEvaluador = new HashMap<>();

                                // Agregar un String manualmente
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
                        }

                        return true;
                } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                }
        }

        @Override
        @Transactional(readOnly = true)
        public RespuestaExamenValoracionInformacionGeneralDto listarInformacionGeneral(Long idTrabajoGrado) {

                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(idTrabajoGrado).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Trabajo de grado con id: " + idTrabajoGrado + " no encontrado"));

                Optional<SolicitudExamenValoracion> responseDto = solicitudExamenValoracionRepository
                                .findByIdTrabajoGradoId(idTrabajoGrado);

                DocenteResponseDto docente = archivoClient
                                .obtenerDocentePorId(Long.parseLong(responseDto.get().getIdEvaluadorInterno()));
                String nombre_docente = docente.getPersona().getNombre() + " " + docente.getPersona().getApellido();
                ExpertoResponseDto experto = archivoClientExpertos
                                .obtenerExpertoPorId(Long.parseLong(responseDto.get().getIdEvaluadorExterno()));
                String nombre_experto = experto.getPersona().getNombre() + " " + experto.getPersona().getApellido();

                RespuestaExamenValoracionInformacionGeneralDto responseDtoInformacion = new RespuestaExamenValoracionInformacionGeneralDto();
                ;

                responseDtoInformacion.setIdTrabajoGrado(idTrabajoGrado);
                responseDtoInformacion.setTituloTrabajoGrado(trabajoGrado.getTitulo());
                responseDtoInformacion.setIdEvaluadorInterno(docente.getPersona().getId());
                responseDtoInformacion.setNombreEvaluadorInterno(nombre_docente);
                responseDtoInformacion.setUniversidadEvaluadorInterno("Universidad del Cauca");
                responseDtoInformacion.setIdEvaluadorExterno(idTrabajoGrado);
                responseDtoInformacion.setNombreEvaluadorExterno(nombre_experto);
                responseDtoInformacion.setUniversidadEvaluadorExterno(experto.getUniversidad());

                return responseDtoInformacion;
        }

        @Override
        @Transactional(readOnly = true)
        public Map<String, List<RespuestaExamenValoracionDto>> buscarPorId(Long idTrabajoGrado) {
                return respuestaExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado)
                                .stream()
                                .map(respuestaExamenValoracionMapper::toDto)
                                .collect(Collectors.groupingBy(
                                                respuesta -> "Interno".equalsIgnoreCase(respuesta.getTipoEvaluador())
                                                                ? "evaluador_interno"
                                                                : "evaluador_externo"));
        }

        @Override
        public RespuestaExamenValoracionDto actualizar(Long id,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result) {

                if (result.hasErrors()) {
                        throw new FieldErrorException(result);
                }

                RespuestaExamenValoracion respuestaExamenValoracionTmp = respuestaExamenValoracionRepository
                                .findById(id).orElseThrow(() -> new ResourceNotFoundException(
                                                "Respuesta examen de valoracion con id: " + id + " no encontrado"));

                // Busca el trabajo de grado
                TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                "Trabajo de grado con id: "
                                                                + respuestaExamenValoracionTmp.getTrabajoGrado().getId()
                                                                + " no encontrado"));

                EstudianteResponseDtoAll informacionEstudiantes = archivoClient
                                .obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante());

                String procesoVa = "Respuesta_Examen_Valoracion";
                String tituloTrabajoGrado = ConvertString
                                .obtenerIniciales(respuestaExamenValoracionTmp.getTrabajoGrado().getTitulo());
                Long idenficiacionEstudiante = informacionEstudiantes.getPersona().getIdentificacion();
                String nombreEstudiante = informacionEstudiantes.getPersona().getNombre();
                String apellidoEstudiante = informacionEstudiantes.getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                RespuestaExamenValoracion responseExamenValoracion = null;

                if (respuestaExamenValoracionTmp != null) {
                        if (respuestaExamenValoracionDto.getLinkFormatoB()
                                        .compareTo(respuestaExamenValoracionTmp.getLinkFormatoB()) != 0) {
                                respuestaExamenValoracionDto.setLinkFormatoB(FilesUtilities.guardarArchivoNew(
                                                tituloTrabajoGrado, procesoVa,
                                                respuestaExamenValoracionDto.getLinkFormatoB(), nombreCarpeta));
                                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoB());
                        }
                        if (respuestaExamenValoracionDto.getLinkFormatoC()
                                        .compareTo(respuestaExamenValoracionTmp.getLinkFormatoC()) != 0) {
                                respuestaExamenValoracionDto.setLinkFormatoC(FilesUtilities.guardarArchivoNew(
                                                tituloTrabajoGrado, procesoVa,
                                                respuestaExamenValoracionDto.getLinkFormatoC(), nombreCarpeta));
                                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkFormatoC());
                        }
                        if (respuestaExamenValoracionDto.getLinkObservaciones()
                                        .compareTo(respuestaExamenValoracionTmp.getLinkObservaciones()) != 0) {
                                respuestaExamenValoracionDto.setLinkObservaciones(FilesUtilities.guardarArchivoNew(
                                                tituloTrabajoGrado, procesoVa,
                                                respuestaExamenValoracionDto.getLinkObservaciones(), nombreCarpeta));
                                FilesUtilities.deleteFileExample(respuestaExamenValoracionTmp.getLinkObservaciones());
                        }
                        // Repetir esto
                        updateRtaExamenValoracionValues(respuestaExamenValoracionTmp, respuestaExamenValoracionDto);
                        responseExamenValoracion = respuestaExamenValoracionRepository
                                        .save(respuestaExamenValoracionTmp);
                }
                return respuestaExamenValoracionMapper.toDto(responseExamenValoracion);
        }

        @Override
        @Transactional(readOnly = true)
        public String descargarArchivo(RutaArchivoDto rutaArchivo) {
                return FilesUtilities.recuperarArchivo(rutaArchivo.getRutaArchivo());
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

                ArrayList<String> listaAnexos = new ArrayList();
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

        @Override
        @Transactional(readOnly = true)
        public List<TrabajoGradoResponseDto> listarEstadosRespuestaExamenValoracion(Integer numeroEstado) {

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

        private int validarEstado(Long idTrabajoGrado, String conceptoEvaluador) {
                Integer numeroEstadoActual = trabajoGradoRepository.obtenerEstadoTrabajoGrado(idTrabajoGrado);
                int numEstado = 0;
                switch (conceptoEvaluador) {
                        case "No aprobado":
                                if (numeroEstadoActual == 5 || numeroEstadoActual == 6 || numeroEstadoActual == 9) {
                                        numEstado = 8;
                                } else if (numeroEstadoActual == 8) {
                                        numEstado = 9;
                                }
                                break;
                        case "Aplazado":
                                if (numeroEstadoActual == 5 || numeroEstadoActual == 6) {
                                        numEstado = 10;
                                } else if (numeroEstadoActual == 10) {
                                        numEstado = 11;
                                }
                                break;
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

}
