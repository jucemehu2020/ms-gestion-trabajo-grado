package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Coordinador.Fase1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.DocumentosEnvioComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSolicitudRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionCoordinadorFase1Test {

        @Mock
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        @Mock
        private RespuestaComiteSolicitudRepository respuestaComiteSolicitudRepository;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private ArchivoClientExpertos archivoClientExpertos;
        @Mock
        private SolicitudExamenValoracionMapper examenValoracionMapper;
        @Mock
        private SolicitudExamenValoracionResponseMapper examenValoracionResponseMapper;
        @Mock
        private AnexoSolicitudExamenValoracionMapper anexoSolicitudExamenValoracionMapper;
        @Mock
        private BindingResult result;

        @Mock
        private EnvioCorreos envioCorreos;

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl solicitudExamenValoracionService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                solicitudExamenValoracionService = new SolicitudExamenValoracionServiceImpl(
                                solicitudExamenValoracionRepository,
                                respuestaComiteSolicitudRepository,
                                null,
                                trabajoGradoRepository,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper,
                                archivoClient,
                                archivoClientExpertos);
                ReflectionTestUtils.setField(solicitudExamenValoracionService, "envioCorreos", envioCorreos);
        }

        @Test
        void testActualizarInformacionCoordinadorFase1Test_ActualizacionExitosaModificacionTrue() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud de revision examen de valoracion");
                envioEmailDto.setMensaje(
                                "Solicito comedidamente revisar el examen de valoracion del estudiante Julio Mellizo para aprobacion");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                DocumentosEnvioComiteDto documentosEnvioComiteDto = new DocumentosEnvioComiteDto();
                documentosEnvioComiteDto.setB64FormatoA("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64Anexos(anexos);

                SolicitudExamenValoracionCoordinadorFase1Dto solicitudExamenValoracionCoordinadorFase1Dto = new SolicitudExamenValoracionCoordinadorFase1Dto();
                solicitudExamenValoracionCoordinadorFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);
                solicitudExamenValoracionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase1Dto.setDocumentosEnvioComite(documentosEnvioComiteDto);

                when(result.hasErrors()).thenReturn(false);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setId(1L);
                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(2);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setExamenValoracion(solicitudExamenValoracion);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();
                solicitudExamenValoracionOld.setConceptoCoordinadorDocumentos(ConceptoVerificacion.RECHAZADO);
                when(solicitudExamenValoracionRepository
                                .findById(trabajoGrado.getExamenValoracion().getId()))
                                .thenReturn(Optional.of(solicitudExamenValoracionOld));

                when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class),
                                anyString(),
                                anyString(), anyMap()))
                                .thenReturn(true);

                when(solicitudExamenValoracionRepository.save(any(SolicitudExamenValoracion.class)))
                                .thenReturn(solicitudExamenValoracion);

                SolicitudExamenValoracionResponseFase1Dto solicitudExamenValoracionResponseFase1Dto = new SolicitudExamenValoracionResponseFase1Dto();
                solicitudExamenValoracionResponseFase1Dto.setId(1L);
                solicitudExamenValoracionResponseFase1Dto.setConceptoCoordinadorDocumentos(
                                solicitudExamenValoracionCoordinadorFase1Dto.getConceptoCoordinadorDocumentos());

                when(examenValoracionResponseMapper.toCoordinadorFase1Dto(any(SolicitudExamenValoracion.class)))
                                .thenReturn(solicitudExamenValoracionResponseFase1Dto);

                SolicitudExamenValoracionResponseFase1Dto resultado = solicitudExamenValoracionService
                                .actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                                solicitudExamenValoracionCoordinadorFase1Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(true, resultado.getConceptoCoordinadorDocumentos());
        }

        @Test
        void testActualizarInformacionCoordinadorFase1Test_ActualizacionExitosaModificacionFalse() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Correcion solicitud examen de valoracion");
                envioEmailDto.setMensaje(
                                "Solicito comedidamente revisar el anteproyecto en el apartado de Introduccion");

                SolicitudExamenValoracionCoordinadorFase1Dto solicitudExamenValoracionCoordinadorFase1Dto = new SolicitudExamenValoracionCoordinadorFase1Dto();
                solicitudExamenValoracionCoordinadorFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.RECHAZADO);
                solicitudExamenValoracionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(2);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setExamenValoracion(solicitudExamenValoracion);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();
                solicitudExamenValoracionOld.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);
                when(solicitudExamenValoracionRepository
                                .findById(trabajoGrado.getExamenValoracion().getId()))
                                .thenReturn(Optional.of(solicitudExamenValoracionOld));

                PersonaDto personaDto = new PersonaDto();
                personaDto.setCorreoElectronico("mellizohurt@gmail.com");
                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(personaDto);
                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);
                when(envioCorreos.enviarCorreosCorrecion(any(ArrayList.class),
                                anyString(),
                                anyString()))
                                .thenReturn(true);

                when(solicitudExamenValoracionRepository.save(any(SolicitudExamenValoracion.class)))
                                .thenReturn(solicitudExamenValoracion);

                SolicitudExamenValoracionResponseFase1Dto solicitudExamenValoracionResponseFase1Dto = new SolicitudExamenValoracionResponseFase1Dto();
                solicitudExamenValoracionResponseFase1Dto.setId(1L);
                solicitudExamenValoracionResponseFase1Dto.setConceptoCoordinadorDocumentos(
                                solicitudExamenValoracionCoordinadorFase1Dto.getConceptoCoordinadorDocumentos());

                when(examenValoracionResponseMapper.toCoordinadorFase1Dto(any(SolicitudExamenValoracion.class)))
                                .thenReturn(solicitudExamenValoracionResponseFase1Dto);

                SolicitudExamenValoracionResponseFase1Dto resultado = solicitudExamenValoracionService
                                .actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                                solicitudExamenValoracionCoordinadorFase1Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(false, resultado.getConceptoCoordinadorDocumentos());
        }

        @Test
        void testActualizarInformacionCoordinadorFase1_ActualizarFalloPorFalse() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Correcion solicitud examen de valoracion");
                envioEmailDto.setMensaje(
                                "Solicito comedidamente revisar el anteproyecto en el apartado de Introduccion");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                DocumentosEnvioComiteDto documentosEnvioComiteDto = new DocumentosEnvioComiteDto();
                documentosEnvioComiteDto.setB64FormatoA("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoE("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64Anexos(anexos);

                SolicitudExamenValoracionCoordinadorFase1Dto solicitudExamenValoracionCoordinadorFase1Dto = new SolicitudExamenValoracionCoordinadorFase1Dto();
                solicitudExamenValoracionCoordinadorFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.RECHAZADO);
                solicitudExamenValoracionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase1Dto.setDocumentosEnvioComite(documentosEnvioComiteDto);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase1Dto,
                                        result);
                });

                String expectedMessage = "Envio de atributos no permitido";

                assertNotNull(exception.getMessage());
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinadorFase1_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud de revision examen de valoracion");
                envioEmailDto.setMensaje(
                                "Solicito comedidamente revisar el examen de valoracion del estudiante Julio Mellizo para aprobacion.");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                DocumentosEnvioComiteDto documentosEnvioComiteDto = new DocumentosEnvioComiteDto();
                documentosEnvioComiteDto.setB64FormatoA("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64Anexos(anexos);

                SolicitudExamenValoracionCoordinadorFase1Dto solicitudExamenValoracionCoordinadorFase1Dto = new SolicitudExamenValoracionCoordinadorFase1Dto();
                solicitudExamenValoracionCoordinadorFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);
                solicitudExamenValoracionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase1Dto.setDocumentosEnvioComite(documentosEnvioComiteDto);

                FieldError fieldError = new FieldError("SolicitudExamenValoracionCoordinadorFase1Dto",
                                "documentosEnvioComite.b64FormatoE",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase1Dto,
                                        result);
                });

                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                String actualMessage = "El campo " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo documentosEnvioComite.b64FormatoE, no debe ser nulo";

                assertNotNull(exception.getResult());
                assertFalse(fieldErrors.isEmpty());
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinadorFase1_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud de revision examen de valoracion");
                envioEmailDto.setMensaje(
                                "Solicito comedidamente revisar el examen de valoracion del estudiante Julio Mellizo para aprobacion.");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                DocumentosEnvioComiteDto documentosEnvioComiteDto = new DocumentosEnvioComiteDto();
                documentosEnvioComiteDto.setB64FormatoA("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64Anexos(anexos);

                SolicitudExamenValoracionCoordinadorFase1Dto solicitudExamenValoracionCoordinadorFase1Dto = new SolicitudExamenValoracionCoordinadorFase1Dto();
                solicitudExamenValoracionCoordinadorFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);
                solicitudExamenValoracionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase1Dto.setDocumentosEnvioComite(documentosEnvioComiteDto);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(1);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setExamenValoracion(solicitudExamenValoracion);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase1Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la informacion";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinadorFase1_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud de revision examen de valoracion");
                envioEmailDto.setMensaje(
                                "Solicito comedidamente revisar el examen de valoracion del estudiante Julio Mellizo para aprobacion.");

                ArrayList<String> anexos = new ArrayList<>();
                anexos.add("cHJ1ZWJhIGRlIHRleHR");
                anexos.add("cHJ1ZWJhIGRlIHRleHR");

                DocumentosEnvioComiteDto documentosEnvioComiteDto = new DocumentosEnvioComiteDto();
                documentosEnvioComiteDto.setB64FormatoA("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64FormatoD("cHJ1ZWJhIGRlIHRleHR");
                documentosEnvioComiteDto.setB64Anexos(anexos);

                SolicitudExamenValoracionCoordinadorFase1Dto solicitudExamenValoracionCoordinadorFase1Dto = new SolicitudExamenValoracionCoordinadorFase1Dto();
                solicitudExamenValoracionCoordinadorFase1Dto.setConceptoCoordinadorDocumentos(ConceptoVerificacion.ACEPTADO);
                solicitudExamenValoracionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
                solicitudExamenValoracionCoordinadorFase1Dto.setDocumentosEnvioComite(documentosEnvioComiteDto);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                                        solicitudExamenValoracionCoordinadorFase1Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }
}