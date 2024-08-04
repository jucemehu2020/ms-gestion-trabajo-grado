package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Coordinador.Fase3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.GeneracionResolucionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.GeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteGeneracionResolucionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion.GeneracionResolucionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionCoordinadorFase3GRTest {

        @Mock
        private GeneracionResolucionRepository generacionResolucionRepository;
        @Mock
        private GeneracionResolucionMapper generacionResolucionMapper;
        @Mock
        private GeneracionResolucionResponseMapper generacionResolucionResponseMapper;
        @Mock
        private TrabajoGradoRepository trabajoGradoRepository;
        @Mock
        private RespuestaComiteGeneracionResolucionRepository respuestaComiteGeneracionResolucionRepository;
        @Mock
        private ArchivoClient archivoClient;
        @Mock
        private BindingResult result;
        @Mock
        private EnvioCorreos envioCorreos;
        @InjectMocks
        private GeneracionResolucionServiceImpl generacionResolucionServiceImpl;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                generacionResolucionServiceImpl = new GeneracionResolucionServiceImpl(
                                generacionResolucionRepository,
                                generacionResolucionMapper,
                                generacionResolucionResponseMapper,
                                trabajoGradoRepository,
                                respuestaComiteGeneracionResolucionRepository,
                                archivoClient);
                ReflectionTestUtils.setField(generacionResolucionServiceImpl, "envioCorreos", envioCorreos);
        }

        @Test
        void ActualizarInformacionCoordinadorFase3GRTest_ActualizacionExitosa() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                GeneracionResolucionCoordinadorFase3Dto generacionResolucionCoordinadorFase3Dto = new GeneracionResolucionCoordinadorFase3Dto();
                generacionResolucionCoordinadorFase3Dto.setNumeroActaConsejo("b-0433");
                generacionResolucionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2023-05-25", formatter));
                generacionResolucionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                GeneracionResolucion generacionResolucionOld = new GeneracionResolucion();
                generacionResolucionOld.setId(1L);
                generacionResolucionOld.setNumeroActaConsejo("b-0432");
                generacionResolucionOld.setFechaActaConsejo(LocalDate.parse("2023-05-24", formatter));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(21);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setGeneracionResolucion(generacionResolucionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(generacionResolucionRepository
                                .findById(trabajoGrado.getGeneracionResolucion().getId()))
                                .thenReturn(Optional.of(generacionResolucionOld));

                GeneracionResolucion generacionResolucionNew = new GeneracionResolucion();
                generacionResolucionNew.setId(1L);
                generacionResolucionNew
                                .setNumeroActaConsejo(
                                                generacionResolucionCoordinadorFase3Dto.getNumeroActaConsejo());
                generacionResolucionNew
                                .setFechaActaConsejo(
                                                generacionResolucionCoordinadorFase3Dto.getFechaActaConsejo());

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(generacionResolucionRepository.save(any(GeneracionResolucion.class)))
                                .thenReturn(generacionResolucionNew);

                GeneracionResolucionCoordinadorFase3ResponseDto generacionResolucionCoordinadorFase3ResponseDto = new GeneracionResolucionCoordinadorFase3ResponseDto();
                generacionResolucionCoordinadorFase3ResponseDto
                                .setId(generacionResolucionNew.getId());
                generacionResolucionCoordinadorFase3ResponseDto
                                .setNumeroActaConsejo(generacionResolucionNew.getNumeroActaConsejo());
                generacionResolucionCoordinadorFase3ResponseDto
                                .setFechaActaConsejo(generacionResolucionNew.getFechaActaConsejo());

                when(generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucionNew))
                                .thenReturn(generacionResolucionCoordinadorFase3ResponseDto);

                GeneracionResolucionCoordinadorFase3ResponseDto resultado = generacionResolucionServiceImpl
                                .actualizarInformacionCoordinadorFase3(idTrabajoGrado,
                                                generacionResolucionCoordinadorFase3Dto,
                                                result);

                assertNotNull(resultado);
                assertEquals(1L, resultado.getId());
                assertEquals("b-0433", resultado.getNumeroActaConsejo());
                assertEquals(LocalDate.parse("2023-05-25", formatter), resultado.getFechaActaConsejo());
        }

        @Test
        void ActualizarInformacionCoordinadorFase3GRTest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                GeneracionResolucionCoordinadorFase3Dto generacionResolucionCoordinadorFase3Dto = new GeneracionResolucionCoordinadorFase3Dto();
                generacionResolucionCoordinadorFase3Dto.setNumeroActaConsejo("b-0433");
                generacionResolucionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHRleHR");

                FieldError fieldError = new FieldError("GeneracionResolucionDocenteDto", "fechaActaConsejo",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase3(idTrabajoGrado,
                                        generacionResolucionCoordinadorFase3Dto,
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
        void ActualizarInformacionCoordinadorFase3GRTest_EstadoNoValido() {
                Long idTrabajoGrado = 1L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                GeneracionResolucionCoordinadorFase3Dto generacionResolucionCoordinadorFase3Dto = new GeneracionResolucionCoordinadorFase3Dto();
                generacionResolucionCoordinadorFase3Dto.setNumeroActaConsejo("b-0433");
                generacionResolucionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2023-05-25", formatter));
                generacionResolucionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                GeneracionResolucion generacionResolucionOld = new GeneracionResolucion();
                generacionResolucionOld.setId(1L);
                generacionResolucionOld.setNumeroActaConsejo("b-0432");
                generacionResolucionOld.setFechaActaConsejo(LocalDate.parse("2023-05-24", formatter));

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(17);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
                trabajoGrado.setGeneracionResolucion(generacionResolucionOld);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase3(idTrabajoGrado,
                                        generacionResolucionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la información";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void ActualizarInformacionCoordinadorFase3GRTest_TrabajoGradoNoExiste() {

                Long idTrabajoGrado = 2L;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                GeneracionResolucionCoordinadorFase3Dto generacionResolucionCoordinadorFase3Dto = new GeneracionResolucionCoordinadorFase3Dto();
                generacionResolucionCoordinadorFase3Dto.setNumeroActaConsejo("b-0433");
                generacionResolucionCoordinadorFase3Dto
                                .setFechaActaConsejo(LocalDate.parse("2023-05-25", formatter));
                generacionResolucionCoordinadorFase3Dto
                                .setLinkOficioConsejo("linkOficioConsejo.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase3(idTrabajoGrado,
                                        generacionResolucionCoordinadorFase3Dto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

}
