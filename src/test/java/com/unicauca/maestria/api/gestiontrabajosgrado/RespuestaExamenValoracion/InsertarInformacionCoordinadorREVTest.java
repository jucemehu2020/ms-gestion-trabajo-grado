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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.AnexoRespuestaExamenValoracionDto;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InsertarInformacionCoordinadorREVTest {

        @Mock
        private RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
        @Mock
        private ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
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
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
        @Mock
        private TiemposPendientesRepository tiemposPendientesRepository;
        @Mock
        private ArchivoClient archivoClient;
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
                                respuestaExamenValoracionMapper,
                                respuestaExamenValoracionResponseMapper,
                                anexoRespuestaExamenValoracionMapper,
                                examenValoracionCanceladoMapper,
                                trabajoGradoRepository,
                                solicitudExamenValoracionRepository,
                                tiemposPendientesRepository,
                                archivoClient);
                ReflectionTestUtils.setField(respuestaExamenValoracionServiceImpl, "envioCorreos", envioCorreos);
        }

        @Test
        void InsertarInformacionCoordinadorREVTest_InsertarExitoso() {

                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setIdEvaluadorInterno(1L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);

                List<RespuestaExamenValoracion> listaExamenes = new ArrayList<>();
                listaExamenes.add(respuestaExamenValoracionOld);

                when(respuestaExamenValoracionRepository.findByTrabajoGrado(idTrabajoGrado))
                                .thenReturn(listaExamenes);

                List<AnexoRespuestaExamenValoracion> listaAnexos = new ArrayList<>();
                listaAnexos.add(AnexoRespuestaExamenValoracion.builder()
                                .linkAnexo(respuestaExamenValoracionDto.getAnexos().get(0).getLinkAnexo())
                                .build());

                RespuestaExamenValoracion respuestaExamenValoracion = new RespuestaExamenValoracion();
                respuestaExamenValoracion.setId(1L);
                respuestaExamenValoracion.setLinkFormatoB(respuestaExamenValoracionDto.getLinkFormatoB());
                respuestaExamenValoracion.setLinkFormatoC(respuestaExamenValoracionDto.getLinkFormatoC());
                respuestaExamenValoracion.setAnexos(listaAnexos);
                respuestaExamenValoracion
                                .setRespuestaExamenValoracion(
                                                respuestaExamenValoracionDto.getRespuestaExamenValoracion());
                respuestaExamenValoracion.setIdEvaluador(respuestaExamenValoracionDto.getIdEvaluador());
                respuestaExamenValoracion.setTipoEvaluador(respuestaExamenValoracionDto.getTipoEvaluador());

                when(respuestaExamenValoracionMapper.toEntity(respuestaExamenValoracionDto))
                                .thenReturn(respuestaExamenValoracion);

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(trabajoGradoRepository.obtenerEstadoTrabajoGrado(idTrabajoGrado))
                                .thenReturn(5);

                when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                                .thenReturn(true);

                when(respuestaExamenValoracionRepository.countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado))
                                .thenReturn(0L);

                when(respuestaExamenValoracionRepository.save(any(RespuestaExamenValoracion.class)))
                                .thenReturn(respuestaExamenValoracion);

                List<AnexoRespuestaExamenValoracionDto> listaAnexosResponseDto = new ArrayList<>();
                listaAnexosResponseDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701004050-Anexos.txt")
                                .build());

                RespuestaExamenValoracionResponseDto respuestaExamenValoracionResponseDto = new RespuestaExamenValoracionResponseDto();
                respuestaExamenValoracionResponseDto.setId(
                                respuestaExamenValoracion.getId());
                respuestaExamenValoracionResponseDto.setLinkFormatoB(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701004050-formatoB.txt");
                respuestaExamenValoracionResponseDto.setLinkFormatoC(
                                "./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701004050-formatoC.txt");
                respuestaExamenValoracionResponseDto.setAnexos(listaAnexosResponseDto);
                respuestaExamenValoracionResponseDto
                                .setRespuestaExamenValoracion(respuestaExamenValoracion.getRespuestaExamenValoracion());
                respuestaExamenValoracionResponseDto
                                .setFechaMaximaEntrega(respuestaExamenValoracion.getFechaMaximaEntrega());
                respuestaExamenValoracionResponseDto.setIdEvaluador(respuestaExamenValoracion.getIdEvaluador());
                respuestaExamenValoracionResponseDto.setTipoEvaluador(respuestaExamenValoracion.getTipoEvaluador());

                when(respuestaExamenValoracionResponseMapper.toDto(respuestaExamenValoracion))
                                .thenReturn(respuestaExamenValoracionResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");

                        RespuestaExamenValoracionResponseDto resultado = respuestaExamenValoracionServiceImpl
                                        .insertarInformacion(
                                                        idTrabajoGrado,
                                                        respuestaExamenValoracionDto, result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701004050-formatoB.txt",
                                        resultado.getLinkFormatoB());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701004050-formatoC.txt",
                                        resultado.getLinkFormatoC());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Respuesta_Examen_Valoracion/01-07-24/20240701004050-Anexos.txt",
                                        resultado.getAnexos().get(0).getLinkAnexo());
                        assertEquals(ConceptosVarios.APROBADO, resultado.getRespuestaExamenValoracion());
                        assertEquals(null, resultado.getFechaMaximaEntrega());
                        assertEquals(1L, resultado.getIdEvaluador());
                        assertEquals(TipoEvaluador.INTERNO, resultado.getTipoEvaluador());
                }

        }

        @Test
        void InsertarInformacionCoordinadorREVTest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                FieldError fieldError = new FieldError("RespuestaExamenValoracionDto", "linkFormatoC",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                        respuestaExamenValoracionDto,
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
        void InsertarInformacionCoordinadorREVTest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(4);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                        respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorREVTest_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                        respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorREVTest_ServidorDocenteCaido() {
                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(2L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setIdEvaluadorInterno(2L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerDocentePorId(respuestaExamenValoracionDto.getIdEvaluador()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                                respuestaExamenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void InsertarInformacionCoordinadorREVTest_ServidorExternoCaido() {
                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(2L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setIdEvaluadorInterno(1L);
                solicitudExamenValoracion.setIdEvaluadorExterno(2L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerExpertoPorId(respuestaExamenValoracionDto.getIdEvaluador()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                                respuestaExamenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void InsertarInformacionCoordinadorREVTest_RegistroYaAprobado() {

                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.NO_APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setIdEvaluadorInterno(2L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                RespuestaExamenValoracion respuestaExamenValoracionOld = new RespuestaExamenValoracion();
                respuestaExamenValoracionOld.setIdEvaluador(1L);
                respuestaExamenValoracionOld.setTipoEvaluador(TipoEvaluador.EXTERNO);
                respuestaExamenValoracionOld.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);

                List<RespuestaExamenValoracion> listaExamenes = new ArrayList<>();
                listaExamenes.add(respuestaExamenValoracionOld);

                when(respuestaExamenValoracionRepository.findByTrabajoGrado(idTrabajoGrado))
                                .thenReturn(listaExamenes);

                InformationException thrown = assertThrows(
                                InformationException.class,
                                () -> respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                                respuestaExamenValoracionDto,
                                                result),
                                "El evaluador previamente dio su concepto como APROBADO, no es permitido que realice nuevos registros");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains(
                                "El evaluador previamente dio su concepto como APROBADO, no es permitido que realice nuevos registros"));

        }

        @Test
        void InsertarInformacionCoordinadorREVTest_MismoEvaluador() {
                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                SolicitudExamenValoracion solicitudExamenValoracion = new SolicitudExamenValoracion();
                solicitudExamenValoracion.setIdEvaluadorInterno(2L);
                solicitudExamenValoracion.setIdEvaluadorExterno(1L);

                when(solicitudExamenValoracionRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(solicitudExamenValoracion));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                        respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "La información del evaluador no coincide con la registrada en la solicitud";
                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionCoordinadorREVTest_Maximo2RegistroNoAprobados() {
                Long idTrabajoGrado = 1L;

                List<AnexoRespuestaExamenValoracionDto> listaAnexosDto = new ArrayList<>();
                listaAnexosDto.add(AnexoRespuestaExamenValoracionDto.builder()
                                .linkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR")
                                .build());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Respuesta evaluadores");
                envioEmailDto.setMensaje("Envio documentos enviados por el evaluador Mage");

                RespuestaExamenValoracionDto respuestaExamenValoracionDto = new RespuestaExamenValoracionDto();
                respuestaExamenValoracionDto.setLinkFormatoB("formatoB.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setLinkFormatoC("formatoC.txt-cHJ1ZWJhIGRlIHRleHR");
                respuestaExamenValoracionDto.setAnexos(listaAnexosDto);
                respuestaExamenValoracionDto.setRespuestaExamenValoracion(ConceptosVarios.APROBADO);
                respuestaExamenValoracionDto.setIdEvaluador(1L);
                respuestaExamenValoracionDto.setTipoEvaluador(TipoEvaluador.INTERNO);
                respuestaExamenValoracionDto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(respuestaExamenValoracionRepository.countByTrabajoGradoIdAndRespuestaNoAprobadoAndEvaluador(
                                idTrabajoGrado, respuestaExamenValoracionDto.getIdEvaluador(),
                                respuestaExamenValoracionDto.getTipoEvaluador()))
                                .thenReturn(2L);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        respuestaExamenValoracionServiceImpl.insertarInformacion(idTrabajoGrado,
                                        respuestaExamenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No se permiten más registros para el evaluador";
                assertTrue(exception.getMessage().contains(expectedMessage));

        }

}
