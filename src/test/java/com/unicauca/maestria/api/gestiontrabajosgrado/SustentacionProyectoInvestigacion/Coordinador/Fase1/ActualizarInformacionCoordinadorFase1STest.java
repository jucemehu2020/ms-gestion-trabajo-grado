package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionCoordinadorFase1STest {

        @Mock
        private SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
        @Mock
        private SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
        @Mock
        private SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
        @Mock
        private RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
        @Mock
        private AnexosSustentacionRepository anexosSustentacionRepository;
        @Mock
        private AnexoSustentacionMapper anexoSustentacionMapper;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private TiemposPendientesRepository tiemposPendientesRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private ArchivoClientEgresados archivoClientEgresados;
        @Mock
        private BindingResult result;
        @Mock
        private EnvioCorreos envioCorreos;
        @InjectMocks
        private SustentacionProyectoInvestigacionServiceImpl sustentacionProyectoInvestigacionServiceImpl;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                sustentacionProyectoInvestigacionServiceImpl = new SustentacionProyectoInvestigacionServiceImpl(
                                sustentacionProyectoInvestigacionRepository,
                                sustentacionProyectoIngestigacionMapper,
                                sustentacionProyectoInvestigacionResponseMapper,
                                respuestaComiteSustentacionRepository,
                                anexosSustentacionRepository,
                                anexoSustentacionMapper,
                                trabajoGradoRepository,
                                tiemposPendientesRepository,
                                archivoClient,
                                archivoClientEgresados);
                ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                                envioCorreos);
        }

        @Test
        void ActualizarInformacionCoordinadorFase1STest_ActualizacionExitosaTrue() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setConceptoCoordinador(ConceptoVerificacion.ACEPTADO);
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setLinkEstudioHojaVidaAcademica(
                                                "linkEstudioHojaVidaAcademica.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setConceptoCoordinador(ConceptoVerificacion.RECHAZADO);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(24);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));
                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(1L);
                sustentacionTrabajoInvestigacionNew
                                .setConceptoCoordinador(sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                                .getConceptoCoordinador());
                sustentacionTrabajoInvestigacionNew.setLinkEstudioHojaVidaAcademica(
                                sustentacionTrabajoInvestigacionNew.getLinkEstudioHojaVidaAcademica());

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase1ResponseDto stiCoordinadorFase1ResponseDto = new STICoordinadorFase1ResponseDto();
                stiCoordinadorFase1ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                stiCoordinadorFase1ResponseDto
                                .setConceptoCoordinador(sustentacionTrabajoInvestigacionNew.getConceptoCoordinador());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(stiCoordinadorFase1ResponseDto);

                STICoordinadorFase1ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptoVerificacion.ACEPTADO, resultado.getConceptoCoordinador());
                assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkEstudioHojaVidaAcademica.txt",
                                resultado.getLinkEstudioHojaVidaAcademica());
        }

        @Test
        void ActualizarInformacionCoordinadorFase1STest_ActualizacionExitosaFalse() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setConceptoCoordinador(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setConceptoCoordinador(ConceptoVerificacion.ACEPTADO);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(24);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));
                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(envioCorreos.enviarCorreosCorrecion(any(ArrayList.class), anyString(), anyString()))
                                .thenReturn(true);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(1L);
                sustentacionTrabajoInvestigacionNew
                                .setConceptoCoordinador(sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                                .getConceptoCoordinador());

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase1ResponseDto stiCoordinadorFase1ResponseDto = new STICoordinadorFase1ResponseDto();
                stiCoordinadorFase1ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                stiCoordinadorFase1ResponseDto
                                .setConceptoCoordinador(sustentacionTrabajoInvestigacionNew.getConceptoCoordinador());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase1Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(stiCoordinadorFase1ResponseDto);

                STICoordinadorFase1ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptoVerificacion.RECHAZADO, resultado.getConceptoCoordinador());
        }

        @Test
        void ActualizarInformacionCoordinadorFase1STest_AtributosNoValidos() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setConceptoCoordinador(ConceptoVerificacion.ACEPTADO);
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setLinkEstudioHojaVidaAcademica(
                                                "linkEstudioHojaVidaAcademica.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Envio de atributos no permitido";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase1STest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setLinkEstudioHojaVidaAcademica(
                                                "linkEstudioHojaVidaAcademica.txt-cHJ1ZWJhIGRlIHRleHR");

                FieldError fieldError = new FieldError("SustentacionTrabajoInvestigacionCoordinadorFase1Dto",
                                "conceptoCoordinador",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: conceptoCoordinador, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase1STest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Revision errores documento");
                envioEmailDto.setMensaje(
                                "Comedidamente solicito revisar los documentos para poder enviarlos a comite.");

                SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setConceptoCoordinador(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setConceptoCoordinador(ConceptoVerificacion.RECHAZADO);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(22);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase1STest_NoExisteTrabajoGrado() {
                Long idTrabajoGrado = 2L;

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Revision errores documento");
                envioEmailDto.setMensaje(
                                "Comedidamente solicito revisar los documentos para poder enviarlos a comite.");

                SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionTrabajoInvestigacionCoordinadorFase1Dto = new SustentacionTrabajoInvestigacionCoordinadorFase1Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto
                                .setConceptoCoordinador(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase1(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase1Dto, result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
