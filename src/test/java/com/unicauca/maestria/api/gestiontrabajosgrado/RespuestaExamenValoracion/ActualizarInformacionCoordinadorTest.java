package com.unicauca.maestria.api.gestiontrabajosgrado.RespuestaExamenValoracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoRespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosRespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionCoordinadorTest {

        @Mock
        private RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
        @Mock
        private ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
        @Mock
        private AnexosRespuestaExamenValoracionRepository anexosRespuestaExamenValoracionRepository;
        @Mock
        private RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
        @Mock
        private RespuestaExamenValoracionResponseMapper respuestaExamenValoracionResponseMapper;
        @Mock
        private AnexoRespuestaExamenValoracionMapper anexoRespuestaExamenValoracionMapper;
        @Mock
        private ExamenValoracionCanceladoMapper examenValoracionCanceladoMapper;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private ArchivoClientExpertos archivoClientExpertos;
        @Mock
        private BindingResult result;
        @Mock
        private EnvioCorreos envioCorreos;
        @InjectMocks
        private RespuestaExamenValoracionServiceImpl respuestaExamenValoracionServiceImpl;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                respuestaExamenValoracionServiceImpl = new RespuestaExamenValoracionServiceImpl(
                                respuestaExamenValoracionRepository,
                                examenValoracionCanceladoRepository,
                                anexosRespuestaExamenValoracionRepository,
                                respuestaExamenValoracionMapper,
                                respuestaExamenValoracionResponseMapper,
                                anexoRespuestaExamenValoracionMapper,
                                examenValoracionCanceladoMapper,
                                trabajoGradoRepository,
                                archivoClient,
                                archivoClientExpertos);
                ReflectionTestUtils.setField(respuestaExamenValoracionServiceImpl, "envioCorreos", envioCorreos);
        }

        // Hacer esta prueba con anexos
        @Test
        void testActualizarInformacionCoordinadorTest_ActualizacionExitosa() {

                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                // List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                // listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                // .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                // .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                // List<AnexoRespuestaExamenValoracion> listaAnexos = new ArrayList<>();
                // listaAnexos.add(AnexoRespuestaExamenValoracion.builder()
                // .linkAnexo(
                // "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-Anexos.txt")
                // .build());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(6);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                when(trabajoGradoRepository.findById(respuestaExamenValoracionOld.getIdTrabajoGrado().getId()))
                                .thenReturn(Optional.of(trabajoGrado));

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                                .thenReturn(true);

                RespuestaExamenValoracion respuestaExamenValoracionNew = new RespuestaExamenValoracion();
                respuestaExamenValoracionNew.setIdRespuestaExamenValoracion(1L);
                respuestaExamenValoracionNew.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionNew.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionNew.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionNew.setAnexos(new ArrayList<>());
                respuestaExamenValoracionNew
                                .setRespuestaExamenValoracion(
                                                respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                respuestaExamenValoracionNew.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionNew.setIdEvaluador(respuestaExamenValoracionDto.getIdEvaluador());
                respuestaExamenValoracionNew.setTipoEvaluador(respuestaExamenValoracionDto.getTipoEvaluador());

                when(respuestaExamenValoracionRepository.save(respuestaExamenValoracionOld))
                                .thenReturn(respuestaExamenValoracionNew);

                RespuestaExamenValoracionResponseDto respuestaExamenValoracionResponseDto = new RespuestaExamenValoracionResponseDto();
                respuestaExamenValoracionResponseDto
                                .setIdRespuestaExamenValoracion(
                                                respuestaExamenValoracionNew.getIdRespuestaExamenValoracion());
                respuestaExamenValoracionResponseDto.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionResponseDto.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionResponseDto.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionResponseDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionResponseDto
                                .setRespuestaExamenValoracion(
                                                respuestaExamenValoracionNew.getRespuestaExamenValoracion());
                respuestaExamenValoracionResponseDto
                                .setFechaMaximaEntrega(respuestaExamenValoracionNew.getFechaMaximaEntrega());
                respuestaExamenValoracionResponseDto.setIdEvaluador(respuestaExamenValoracionNew.getIdEvaluador());
                respuestaExamenValoracionResponseDto.setTipoEvaluador(respuestaExamenValoracionNew.getTipoEvaluador());

                when(respuestaExamenValoracionResponseMapper.toDto(respuestaExamenValoracionNew))
                                .thenReturn(respuestaExamenValoracionResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        RespuestaExamenValoracionResponseDto resultado = respuestaExamenValoracionServiceImpl
                                        .actualizar(
                                                        idRespuestaExamen,
                                                        respuestaExamenValoracionDto, result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getIdRespuestaExamenValoracion());
                        assertEquals(
                                        "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt",
                                        resultado.getLinkFormatoB());
                        assertEquals(
                                        "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt",
                                        resultado.getLinkFormatoC());
                        assertEquals(
                                        "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt",
                                        resultado.getLinkObservaciones());
                        assertEquals(new ArrayList<>(), resultado.getAnexos());
                        assertEquals(ConceptosVarios.APROBADO, resultado.getRespuestaExamenValoracion());
                        assertEquals(LocalDate.parse("2023-05-29", formatter), resultado.getFechaMaximaEntrega());
                        assertEquals(1L, resultado.getIdEvaluador());
                        assertEquals(TipoEvaluador.INTERNO, resultado.getTipoEvaluador());
                }
        }

        @Test
        void testActualizarInformacionCoordinador_FaltanAtributos() {
                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                FieldError fieldError = new FieldError("RespuestaExamenValoracionDto", "linkFormatoC",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: linkFormatoC, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinador_EstadoNoValido() {
                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                when(trabajoGradoRepository.findById(respuestaExamenValoracionOld.getIdTrabajoGrado().getId()))
                                .thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la informacion";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinador_RespuestaExamenValoracionNoExiste() {
                Long idRespuestaExamen = 2L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Respuesta examen de valoracion con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinador_DatosEvaluadorNoCorresponden() {

                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(6);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(2L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Los datos del evaluador no corresponden";
                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void testActualizarInformacionCoordinador_DocenteNoExiste() {
                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(2L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(6);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                when(trabajoGradoRepository.findById(respuestaExamenValoracionOld.getIdTrabajoGrado().getId()))
                                .thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador()))
                                .thenThrow(new ResourceNotFoundException("Docentes con id "
                                                + respuestaExamenValoracionDto.getIdEvaluador() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Docentes con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinador_ExpertoNoExiste() {
                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(2L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(6);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                when(trabajoGradoRepository.findById(respuestaExamenValoracionOld.getIdTrabajoGrado().getId()))
                                .thenReturn(Optional.of(trabajoGrado));

                when(archivoClientExpertos.obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador()))
                                .thenThrow(new ResourceNotFoundException("Expertos con id "
                                                + respuestaExamenValoracionDto.getIdEvaluador() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Expertos con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionCoordinador_ServidorDocenteCaido() {
                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(6);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                when(trabajoGradoRepository.findById(respuestaExamenValoracionOld.getIdTrabajoGrado().getId()))
                                .thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen,
                                                respuestaExamenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void testActualizarInformacionCoordinador_ServidorExpertoCaido() {
                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(1L);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(6);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoB.txt");
                respuestaExamenValoracionOld.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-formatoC.txt");
                respuestaExamenValoracionOld.setLinkObservaciones(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701132302-observaciones.txt");
                respuestaExamenValoracionOld.setAnexos(new ArrayList<>());
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionOld.setIdTrabajoGrado(trabajoGrado);

                when(respuestaExamenValoracionRepository.findById(idRespuestaExamen))
                                .thenReturn(Optional.of(respuestaExamenValoracionOld));

                when(trabajoGradoRepository.findById(respuestaExamenValoracionOld.getIdTrabajoGrado().getId()))
                                .thenReturn(Optional.of(trabajoGrado));

                when(archivoClientExpertos.obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen,
                                                respuestaExamenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void testActualizarInformacionCoordinador_AtributoFechaMaximaNoPermitido() {

                Long idRespuestaExamen = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkObservaciones("observaciones.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(new ArrayList<>());
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setFechaMaximaEntrega(LocalDate.parse("2023-05-29", formatter));
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        respuestaExamenValoracionServiceImpl.actualizar(idRespuestaExamen, respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Atributo FECHA MAXIMA no permitido";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
