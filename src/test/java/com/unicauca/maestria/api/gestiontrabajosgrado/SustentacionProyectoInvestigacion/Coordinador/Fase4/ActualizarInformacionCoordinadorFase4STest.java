package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.TiemposPendientes;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
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
public class ActualizarInformacionCoordinadorFase4STest {

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
        void InsertarInformacionCoordinadorFase3STest_ActualizacionExitosaAprobado() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setLinkActaSustentacionPublica("linkActaSustentacionPublica.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setRespuestaSustentacion(ConceptosVarios.NO_APROBADO);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(31);
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

                when(tiemposPendientesRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(new TiemposPendientes()));

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(sustentacionTrabajoInvestigacionOld.getId());
                sustentacionTrabajoInvestigacionNew.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.getRespuestaSustentacion());
                sustentacionTrabajoInvestigacionNew.setLinkActaSustentacionPublica(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkActaSustentacionPublica.txt");
                sustentacionTrabajoInvestigacionNew
                                .setNumeroActaFinal(sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                                .getNumeroActaFinal());
                sustentacionTrabajoInvestigacionNew
                                .setFechaActaFinal(sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                                .getFechaActaFinal());

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase4ResponseDto coordinadorFase4ResponseDto = new STICoordinadorFase4ResponseDto();
                coordinadorFase4ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                coordinadorFase4ResponseDto.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionNew.getRespuestaSustentacion());
                coordinadorFase4ResponseDto.setLinkActaSustentacionPublica(
                                sustentacionTrabajoInvestigacionNew.getLinkActaSustentacionPublica());
                coordinadorFase4ResponseDto
                                .setNumeroActaFinal(sustentacionTrabajoInvestigacionNew.getNumeroActaFinal());
                coordinadorFase4ResponseDto.setFechaActaFinal(sustentacionTrabajoInvestigacionNew.getFechaActaFinal());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(coordinadorFase4ResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase4(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals(ConceptosVarios.APROBADO, resultado.getRespuestaSustentacion());
                        assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkActaSustentacionPublica.txt",
                                        resultado.getLinkActaSustentacionPublica());

                        assertEquals("a-abc", resultado.getNumeroActaFinal());
                        assertEquals(LocalDate.parse("2024-05-29", formatter), resultado.getFechaActaFinal());
                }
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_ActualizacionExitosaNoAprobado() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setRespuestaSustentacion(ConceptosVarios.NO_APROBADO);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionOld
                                .setLinkActaSustentacionPublica(
                                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkActaSustentacionPublica.txt");
                sustentacionTrabajoInvestigacionOld.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionOld.setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(31);
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

                when(tiemposPendientesRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(new TiemposPendientes()));

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(sustentacionTrabajoInvestigacionOld.getId());
                sustentacionTrabajoInvestigacionNew.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.getRespuestaSustentacion());

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase4ResponseDto coordinadorFase4ResponseDto = new STICoordinadorFase4ResponseDto();
                coordinadorFase4ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                coordinadorFase4ResponseDto.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionNew.getRespuestaSustentacion());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(coordinadorFase4ResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase4(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals(ConceptosVarios.NO_APROBADO, resultado.getRespuestaSustentacion());
                }
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_RegistroExitosoAplazado() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptosVarios.APLAZADO);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionOld
                                .setLinkActaSustentacionPublica(
                                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkActaSustentacionPublica.txt");
                sustentacionTrabajoInvestigacionOld.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionOld.setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(31);
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

                when(tiemposPendientesRepository.findByTrabajoGradoId(idTrabajoGrado))
                                .thenReturn(Optional.of(new TiemposPendientes()));

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionNew.setId(sustentacionTrabajoInvestigacionOld.getId());
                sustentacionTrabajoInvestigacionNew.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.getRespuestaSustentacion());

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase4ResponseDto coordinadorFase4ResponseDto = new STICoordinadorFase4ResponseDto();
                coordinadorFase4ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                coordinadorFase4ResponseDto.setRespuestaSustentacion(
                                sustentacionTrabajoInvestigacionNew.getRespuestaSustentacion());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase4Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(coordinadorFase4ResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .actualizarInformacionCoordinadoFase4(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals(ConceptosVarios.APLAZADO, resultado.getRespuestaSustentacion());
                }
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_AtributosNoValidos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setRespuestaSustentacion(ConceptosVarios.NO_APROBADO);
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setLinkActaSustentacionPublica("linkActaSustentacionPublica.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionCoordinadoFase4(
                                        idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Envio de atributos no permitido";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                FieldError fieldError = new FieldError("SustentacionProyectoInvestigacionServiceImpl",
                                "linkActaSustentacionPublica",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionCoordinadoFase4(
                                        idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: linkActaSustentacionPublica, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_AtributosIncompletos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setLinkActaSustentacionPublica("linkActaSustentacionPublica.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionCoordinadoFase4(
                                        idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Atributos incorrectos";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setLinkActaSustentacionPublica("linkActaSustentacionPublica.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);
                sustentacionTrabajoInvestigacionOld.setRespuestaSustentacion(ConceptosVarios.NO_APROBADO);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(25);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionCoordinadoFase4(
                                        idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la informacion";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_NoExisteTrabajoGrado() {
                Long idTrabajoGrado = 2L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptosVarios.APROBADO);
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setLinkActaSustentacionPublica("linkActaSustentacionPublica.txt-cHJ1ZWJhIGRlIHRleHR");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setNumeroActaFinal("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setFechaActaFinal(LocalDate.parse("2024-05-29", formatter));

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
