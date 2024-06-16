package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

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

import org.apache.poi.ss.formula.atp.Switch;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.ConvertString;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionInformacionGeneralDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldUniqueException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.InformacionUnicaSolicitudExamenValoracion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaExamenValoracionServiceImpl implements RespuestaExamenValoracionService {

        private final RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
        private final RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
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

                TrabajoGrado trabajoGrado = trabajoGradoRepository
                                .findById(respuestaExamenValoracionDto.getIdTrabajoGrados())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "TrabajoGrado con id: "
                                                                + respuestaExamenValoracionDto.getIdTrabajoGrados()
                                                                + " No encontrado"));

                // Obtener iniciales del trabajo de grado
                String procesoVa = "Respuesta_Examen_Valoracion";
                String tituloTrabajoGrado = ConvertString.obtenerIniciales(trabajoGrado.getTitulo());

                Long idenficiacionEstudiante = trabajoGrado.getEstudiante().getPersona().getIdentificacion();
                String nombreEstudiante = trabajoGrado.getEstudiante().getPersona().getNombre();
                String apellidoEstudiante = trabajoGrado.getEstudiante().getPersona().getApellido();
                String nombreCarpeta = idenficiacionEstudiante + "-" + nombreEstudiante + "_" + apellidoEstudiante;

                // Mapear DTO a entidad
                RespuestaExamenValoracion rtaExamenValoracion = respuestaExamenValoracionMapper
                                .toEntity(respuestaExamenValoracionDto);

                // Se cambia el numero de estado
                // int numEstado =
                // validarEstado(respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                // trabajoGrado.setNumeroEstado(numEstado);

                trabajoGrado.setNumeroEstado(4);

                // Guardar la entidad ExamenValoracion
                rtaExamenValoracion.setLinkFormatoB(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                rtaExamenValoracion.getLinkFormatoB(), nombreCarpeta));
                rtaExamenValoracion.setLinkFormatoC(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                rtaExamenValoracion.getLinkFormatoC(), nombreCarpeta));
                rtaExamenValoracion.setLinkObservaciones(FilesUtilities.guardarArchivoNew(tituloTrabajoGrado, procesoVa,
                                rtaExamenValoracion.getLinkObservaciones(), nombreCarpeta));

                // Se asigna al trabajo de grado
                rtaExamenValoracion.setTrabajoGrado(trabajoGrado);

                enviarCorreoTutorEstudiante(respuestaExamenValoracionDto, trabajoGrado);

                RespuestaExamenValoracion examenValoracionRes = respuestaExamenValoracionRepository
                                .save(rtaExamenValoracion);

                return respuestaExamenValoracionMapper.toDto(examenValoracionRes);
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
                                        .obtenerInformacionEstudiante(trabajoGrado.getEstudiante().getId());
                        correos.add(estudiante.getPersona().getCorreoElectronico());

                        // Iterar sobre cada correo electrónico para enviar los mensajes individualmente
                        for (String correo : correos) {
                                MimeMessage message = mailSender.createMimeMessage();
                                MimeMessageHelper helper = new MimeMessageHelper(message, true); // habilitar modo
                                                                                                 // multipart

                                // Configurar variables del contexto para la plantilla
                                // templateModel.put("nombreEvaluador", "Nombre del Evaluador");
                                templateModel.put("mensaje", respuestaExamenValoracionDto.getInformacionEnvioDto().getMensaje());

                                // Crear el contexto para el motor de plantillas
                                Context context = new Context();
                                context.setVariables(templateModel);

                                // Procesar la plantilla de correo electrónico
                                String html = templateEngine.process("emailTemplate", context);

                                helper.setTo(correo);
                                helper.setSubject(respuestaExamenValoracionDto.getInformacionEnvioDto().getAsunto());
                                helper.setText(html, true); // Establecer el cuerpo del mensaje HTML

                                // Obtener documentos y adjuntarlos
                                Map<String, String> documentosParaEvaluador = respuestaExamenValoracionDto
                                                .getInformacionEnvioDto().getDocumentos();
                                for (Map.Entry<String, String> entry : documentosParaEvaluador.entrySet()) {
                                        String nombreDocumento = entry.getKey();
                                        String base64Documento = entry.getValue();
                                        byte[] documentoBytes = Base64.getDecoder().decode(base64Documento);
                                        ByteArrayDataSource dataSource = new ByteArrayDataSource(documentoBytes,
                                                        "application/pdf");
                                        helper.addAttachment(nombreDocumento + ".pdf", dataSource); // Cambiar a .pdf si
                                                                                                    // es necesario
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
                // TrabajoGrado trabajoGrado = trabajoGradoRepository.findById(id).orElseThrow(
                // () -> new ResourceNotFoundException(
                // "Trabajo de grado con id: " + id + " no encontrado"));

                String procesoVa = "Respuesta_Examen_Valoracion";
                String tituloTrabajoGrado = ConvertString
                                .obtenerIniciales(respuestaExamenValoracionTmp.getTrabajoGrado().getTitulo());
                Long idenficiacionEstudiante = respuestaExamenValoracionTmp.getTrabajoGrado().getEstudiante()
                                .getPersona().getIdentificacion();
                String nombreEstudiante = respuestaExamenValoracionTmp.getTrabajoGrado().getEstudiante().getPersona()
                                .getNombre();
                String apellidoEstudiante = respuestaExamenValoracionTmp.getTrabajoGrado().getEstudiante().getPersona()
                                .getApellido();
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
                respuestaExamenValoracion.setObservacion(respuestaExamenValoracionDto.getObservacion());
                respuestaExamenValoracion.setEstadoFinalizado(respuestaExamenValoracionDto.getEstadoFinalizado());
                // Update archivos
                respuestaExamenValoracion.setLinkFormatoB(respuestaExamenValoracionDto.getLinkFormatoB());
                respuestaExamenValoracion.setLinkFormatoC(respuestaExamenValoracionDto.getLinkFormatoC());
                respuestaExamenValoracion.setLinkObservaciones(respuestaExamenValoracionDto.getLinkObservaciones());
        }

        private int validarEstado(String estado) {
                int numEstado = 0;
                switch (estado) {
                        case "No aprobado":
                                numEstado = 3;
                                break;
                        case "Aplazado":
                                numEstado = 4;
                                break;
                        default:
                                numEstado = 5;
                                break;
                }
                return numEstado;
        }

}
