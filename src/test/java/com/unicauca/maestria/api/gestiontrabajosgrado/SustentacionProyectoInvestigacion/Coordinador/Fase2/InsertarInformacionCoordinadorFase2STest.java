package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase2;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientEgresados;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.RespuestaComiteSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.InformacionEnvioConsejoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.RespuestaComiteSustentacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
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
public class InsertarInformacionCoordinadorFase2STest {

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
        void InsertarInformacionCoordinadorFase2STest_RegistroExitosoTrue() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteSustentacionDto.setNumeroActa("AX1-3445");
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                InformacionEnvioConsejoDto informacionEnvioConsejoDto = new InformacionEnvioConsejoDto();
                informacionEnvioConsejoDto.setB64Monografia("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64FormatoG("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64HistoriaAcademica("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64Anexos(new ArrayList<>());

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setLinkFormatoG("linkFormatoG.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setInformacionEnvioConsejo(informacionEnvioConsejoDto);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setActaFechaRespuestaComite(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(26);
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

                when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                                .thenReturn(true);

                RespuestaComiteSustentacion respuestaComiteSustentacion = new RespuestaComiteSustentacion();
                respuestaComiteSustentacion.setConceptoComite(respuestaComiteSustentacionDto.getConceptoComite());
                respuestaComiteSustentacion.setNumeroActa(respuestaComiteSustentacionDto.getNumeroActa());
                respuestaComiteSustentacion.setFechaActa(respuestaComiteSustentacionDto.getFechaActa());

                List<RespuestaComiteSustentacion> listaRespuestaComiteSustentacion = new ArrayList<>();
                listaRespuestaComiteSustentacion.add(respuestaComiteSustentacion);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(1L);
                sustentacionTrabajoInvestigacionNew.setActaFechaRespuestaComite(listaRespuestaComiteSustentacion);

                sustentacionTrabajoInvestigacionNew.setLinkFormatoG(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoG.txt");

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase2ResponseDto resCoordinadorFase2ResponseDto = new STICoordinadorFase2ResponseDto();
                resCoordinadorFase2ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                resCoordinadorFase2ResponseDto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
                resCoordinadorFase2ResponseDto.setLinkFormatoG(sustentacionTrabajoInvestigacionNew.getLinkFormatoG());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(resCoordinadorFase2ResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");

                        STICoordinadorFase2ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals(Concepto.APROBADO,
                                        resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
                        assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
                        assertEquals(LocalDate.parse("2023-05-24", formatter),
                                        resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkFormatoG.txt",
                                        resultado.getLinkFormatoG());
                }
        }

        @Test
        void InsertarInformacionCoordinadorFase2STest_RegistroExitosoFalse() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.NO_APROBADO);
                respuestaComiteSustentacionDto.setNumeroActa("AX1-3445");
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Informacion revision");
                envioEmailDto.setMensaje("La respuesta del comite ha sido rechazada, por favor corregir");

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setActaFechaRespuestaComite(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(26);
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

                RespuestaComiteSustentacion respuestaComiteSustentacion = new RespuestaComiteSustentacion();
                respuestaComiteSustentacion.setConceptoComite(respuestaComiteSustentacionDto.getConceptoComite());
                respuestaComiteSustentacion.setNumeroActa(respuestaComiteSustentacionDto.getNumeroActa());
                respuestaComiteSustentacion.setFechaActa(respuestaComiteSustentacionDto.getFechaActa());

                List<RespuestaComiteSustentacion> listaRespuestaComiteSustentacion = new ArrayList<>();
                listaRespuestaComiteSustentacion.add(respuestaComiteSustentacion);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(1L);
                sustentacionTrabajoInvestigacionNew.setActaFechaRespuestaComite(listaRespuestaComiteSustentacion);

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase2ResponseDto resCoordinadorFase2ResponseDto = new STICoordinadorFase2ResponseDto();
                resCoordinadorFase2ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                resCoordinadorFase2ResponseDto.setActaFechaRespuestaComite(listaRespuestaComiteDto);

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase2Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(resCoordinadorFase2ResponseDto);

                STICoordinadorFase2ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(Concepto.NO_APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
                assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
                assertEquals(LocalDate.parse("2023-05-24", formatter),
                                resultado.getActaFechaRespuestaComite().get(0).getFechaActa());

        }

        @Test
        void InsertarInformacionCoordinadorFase2STest_AtributosNoValidos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.NO_APROBADO);
                respuestaComiteSustentacionDto.setNumeroActa("AX1-3445");
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setLinkFormatoG("linkFormatoG.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Envio de atributos no permitido";

                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionCoordinadorFase2STest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setLinkFormatoG("linkFormatoG.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

                FieldError fieldError = new FieldError("SustentacionProyectoInvestigacionServiceImpl", "numeroActa",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: numeroActa, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase2STest_AtributosIncompletos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteSustentacionDto.setNumeroActa("AX1-3445");
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Atributos incorrectos";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase2STest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteSustentacionDto.setNumeroActa("AX1-3445");
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                InformacionEnvioConsejoDto informacionEnvioConsejoDto = new InformacionEnvioConsejoDto();
                informacionEnvioConsejoDto.setB64Monografia("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64FormatoG("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64HistoriaAcademica("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64Anexos(new ArrayList<>());

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setLinkFormatoG("linkFormatoG.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setInformacionEnvioConsejo(informacionEnvioConsejoDto);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(22);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionCoordinadorFase2STest_NoExisteTrabajoGrado() {
                Long idTrabajoGrado = 2L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                RespuestaComiteSustentacionDto respuestaComiteSustentacionDto = new RespuestaComiteSustentacionDto();
                respuestaComiteSustentacionDto.setConceptoComite(Concepto.APROBADO);
                respuestaComiteSustentacionDto.setNumeroActa("AX1-3445");
                respuestaComiteSustentacionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

                List<RespuestaComiteSustentacionDto> listaRespuestaComiteDto = new ArrayList<>();
                listaRespuestaComiteDto.add(respuestaComiteSustentacionDto);

                EnvioEmailDto envioEmailDto = new EnvioEmailDto();
                envioEmailDto.setAsunto("Solicitud revision comite");
                envioEmailDto.setMensaje("Comedidamente solicito revisar los documentos.");

                InformacionEnvioConsejoDto informacionEnvioConsejoDto = new InformacionEnvioConsejoDto();
                informacionEnvioConsejoDto.setB64Monografia("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64FormatoG("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64HistoriaAcademica("cHJ1ZWJhIGRlIHRleHR");
                informacionEnvioConsejoDto.setB64Anexos(new ArrayList<>());

                SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionTrabajoInvestigacionCoordinadorFase2Dto = new SustentacionTrabajoInvestigacionCoordinadorFase2Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setLinkFormatoG("linkFormatoG.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setActaFechaRespuestaComite(listaRespuestaComiteDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);
                sustentacionTrabajoInvestigacionCoordinadorFase2Dto
                                .setInformacionEnvioConsejo(informacionEnvioConsejoDto);

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase2(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase2Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
