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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoSustentacion;
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
public class InsertarInformacionCoordinadorFase4STest {

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
        void InsertarInformacionCoordinadorFase4STest_RegistroExitosoAprobado() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APROBADO);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(30);
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

                        STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                        .insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals(ConceptoSustentacion.APROBADO, resultado.getRespuestaSustentacion());
                }
        }

        @Test
        void InsertarInformacionCoordinadorFase4STest_RegistroExitosoNoAprobado() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto
                                .setRespuestaSustentacion(ConceptoSustentacion.NO_APROBADO);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(30);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

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

                STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptoSustentacion.NO_APROBADO, resultado.getRespuestaSustentacion());
        }

        @Test
        void InsertarInformacionCoordinadorFase4STest_RegistroExitosoAplazado() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APLAZADO);

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(30);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(sustentacionProyectoInvestigacionRepository
                                .findById(trabajoGrado.getSustentacionProyectoInvestigacion().getId()))
                                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

                when(tiemposPendientesRepository.save(any(TiemposPendientes.class)))
                                .thenReturn(new TiemposPendientes());

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

                STICoordinadorFase4ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptoSustentacion.APLAZADO, resultado.getRespuestaSustentacion());
        }

        @Test
        void InsertarInformacionCoordinadorFase4STest_AtributosNoValidos() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APLAZADO);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Envio de atributos no permitido";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase4STest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APROBADO);

                FieldError fieldError = new FieldError("SustentacionProyectoInvestigacionServiceImpl",
                                "linkActaSustentacionPublica",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase4(idTrabajoGrado,
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
        void InsertarInformacionCoordinadorFase4STest_AtributosIncompletos() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APROBADO);

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Atributos incorrectos";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase4STest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APROBADO);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(20);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase4(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase4Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase4STest_NoExisteTrabajoGrado() {
                Long idTrabajoGrado = 2L;

                SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionTrabajoInvestigacionCoordinadorFase4Dto = new SustentacionTrabajoInvestigacionCoordinadorFase4Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase4Dto.setRespuestaSustentacion(ConceptoSustentacion.APROBADO);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

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
