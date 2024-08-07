package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Coordinador.Fase3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
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
public class InsertarInformacionCoordinadorFase3STest {

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
        void InsertarInformacionCoordinadorFase3STest_RegistroExitosoTrue() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.ACEPTADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(28);
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
                sustentacionTrabajoInvestigacionNew
                                .setJuradosAceptados(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getJuradosAceptados());
                sustentacionTrabajoInvestigacionNew
                                .setNumeroActaConsejo(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getNumeroActaConsejo());
                sustentacionTrabajoInvestigacionNew
                                .setFechaActaConsejo(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getFechaActaConsejo());
                sustentacionTrabajoInvestigacionNew.setLinkOficioConsejo(
                                "./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkOficioConsejo.txt");

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase3ResponseDto coordinadorFase3ResponseDto = new STICoordinadorFase3ResponseDto();
                coordinadorFase3ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                coordinadorFase3ResponseDto
                                .setJuradosAceptados(sustentacionTrabajoInvestigacionNew.getJuradosAceptados());
                coordinadorFase3ResponseDto
                                .setNumeroActaConsejo(sustentacionTrabajoInvestigacionNew.getNumeroActaConsejo());
                coordinadorFase3ResponseDto
                                .setFechaActaConsejo(sustentacionTrabajoInvestigacionNew.getFechaActaConsejo());
                coordinadorFase3ResponseDto
                                .setLinkOficioConsejo(sustentacionTrabajoInvestigacionNew.getLinkOficioConsejo());

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(coordinadorFase3ResponseDto);

                STICoordinadorFase3ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptoVerificacion.ACEPTADO, resultado.getJuradosAceptados());
                assertEquals("a-abc", resultado.getNumeroActaConsejo());
                assertEquals(LocalDate.parse("2024-05-24", formatter), resultado.getFechaActaConsejo());
                assertEquals("./files/2024/7/1084-Juan_Meneses/Sustentacion_Proyecto_Investigacion/12-07-24/20240712153209-linkOficioConsejo.txt",
                                resultado.getLinkOficioConsejo());

        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_RegistroExitosoFalse() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("2");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(28);
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
                sustentacionTrabajoInvestigacionNew
                                .setJuradosAceptados(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getJuradosAceptados());
                sustentacionTrabajoInvestigacionNew
                                .setNumeroActaConsejo(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getNumeroActaConsejo());
                sustentacionTrabajoInvestigacionNew
                                .setFechaActaConsejo(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getFechaActaConsejo());
                sustentacionTrabajoInvestigacionNew
                                .setIdJuradoInterno(
                                                Long.parseLong(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                                .getIdJuradoInterno()));
                sustentacionTrabajoInvestigacionNew
                                .setIdJuradoExterno(
                                                Long.parseLong(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                                .getIdJuradoExterno()));

                when(sustentacionProyectoInvestigacionRepository.save(sustentacionTrabajoInvestigacionOld))
                                .thenReturn(sustentacionTrabajoInvestigacionNew);

                STICoordinadorFase3ResponseDto coordinadorFase3ResponseDto = new STICoordinadorFase3ResponseDto();
                coordinadorFase3ResponseDto.setId(sustentacionTrabajoInvestigacionNew.getId());
                coordinadorFase3ResponseDto
                                .setJuradosAceptados(sustentacionTrabajoInvestigacionNew.getJuradosAceptados());
                coordinadorFase3ResponseDto
                                .setNumeroActaConsejo(sustentacionTrabajoInvestigacionNew.getNumeroActaConsejo());
                coordinadorFase3ResponseDto
                                .setFechaActaConsejo(sustentacionTrabajoInvestigacionNew.getFechaActaConsejo());
                coordinadorFase3ResponseDto
                                .setIdJuradoInterno(String
                                                .valueOf(sustentacionTrabajoInvestigacionNew.getIdJuradoInterno()));
                coordinadorFase3ResponseDto
                                .setIdJuradoExterno(String
                                                .valueOf(sustentacionTrabajoInvestigacionNew.getIdJuradoExterno()));

                when(sustentacionProyectoInvestigacionResponseMapper
                                .toCoordinadorFase3Dto(sustentacionTrabajoInvestigacionNew))
                                .thenReturn(coordinadorFase3ResponseDto);

                STICoordinadorFase3ResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                                .insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals(ConceptoVerificacion.RECHAZADO, resultado.getJuradosAceptados());
                assertEquals("a-abc", resultado.getNumeroActaConsejo());
                assertEquals(LocalDate.parse("2024-05-24", formatter), resultado.getFechaActaConsejo());
                assertEquals("2", resultado.getIdJuradoInterno());
                assertEquals("1", resultado.getIdJuradoExterno());
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_AtributosNoValidos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.ACEPTADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("2");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Envio de atributos no permitido";

                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.ACEPTADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("2");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                FieldError fieldError = new FieldError("SustentacionProyectoInvestigacionServiceImpl",
                                "fechaActaConsejo",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: fechaActaConsejo, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_AtributosIncompletos() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
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

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.ACEPTADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(25);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_JuradoInternoNoExiste() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("3");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(28);
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

                when(archivoClient.obtenerDocentePorId(
                                Long.parseLong(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getIdJuradoInterno())))
                                .thenThrow(new ResourceNotFoundException("Docentes con id "
                                                + sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                                .getIdJuradoInterno()
                                                + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Docentes con id 3 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_JuradoExternoNoExiste() {

                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("3");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(28);
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

                when(archivoClient.obtenerExpertoPorId(
                                Long.parseLong(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getIdJuradoExterno())))
                                .thenThrow(new ResourceNotFoundException("Expertos con id "
                                                + sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                                .getIdJuradoExterno()
                                                + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Expertos con id 3 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_ServidorDocenteCaido() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("2");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(28);
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

                when(archivoClient.obtenerDocentePorId(
                                Long.parseLong(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getIdJuradoInterno())))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(
                                                idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_ServidorExpertosCaido() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("2");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                SustentacionProyectoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionProyectoInvestigacion();
                sustentacionTrabajoInvestigacionOld.setId(1L);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(28);
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

                when(archivoClient.obtenerExpertoPorId(
                                Long.parseLong(sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                                .getIdJuradoExterno())))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(
                                                idTrabajoGrado,
                                                sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void InsertarInformacionCoordinadorFase3STest_NoExisteTrabajoGrado() {
                Long idTrabajoGrado = 2L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionTrabajoInvestigacionCoordinadorFase3Dto = new SustentacionTrabajoInvestigacionCoordinadorFase3Dto();
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setJuradosAceptados(ConceptoVerificacion.RECHAZADO);
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setNumeroActaConsejo("a-abc");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2024-05-24", formatter));
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoInterno("1");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto.setIdJuradoExterno("2");
                sustentacionTrabajoInvestigacionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        sustentacionProyectoInvestigacionServiceImpl.insertarInformacionCoordinadoFase3(idTrabajoGrado,
                                        sustentacionTrabajoInvestigacionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
