package com.unicauca.maestria.api.gestiontrabajosgrado.SolicitudExamenValoracion.Docente;

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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoSolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SolicitudExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion.SolicitudExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionDocenteTest {

        @Mock
        private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
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

        @InjectMocks
        private SolicitudExamenValoracionServiceImpl solicitudExamenValoracionService;

        @BeforeEach
        void setUp() {
                solicitudExamenValoracionService = new SolicitudExamenValoracionServiceImpl(
                                solicitudExamenValoracionRepository,
                                null,
                                null,
                                trabajoGradoRepository,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper, 
                                archivoClient,
                                archivoClientExpertos);
        }


        @Test
        void testActualizarInformacionDocente_RegistroExitoso() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Cambio nombre");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(2L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                SolicitudExamenValoracion solicitudExamenValoracionOld = new SolicitudExamenValoracion();
                solicitudExamenValoracionOld.setIdExamenValoracion(1L);
                solicitudExamenValoracionOld.setTitulo("Prueba test");
                solicitudExamenValoracionOld.setLinkFormatoA(
                                "./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240629163559-formatoA.txt");
                solicitudExamenValoracionOld.setLinkFormatoD(
                                "./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240629163559-formatoD.txt");
                solicitudExamenValoracionOld.setLinkFormatoE(
                                "./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240629163559-formatoE.txt");
                solicitudExamenValoracionOld.setIdEvaluadorInterno(1L);
                solicitudExamenValoracionOld.setIdEvaluadorExterno(1L);
                solicitudExamenValoracionOld.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(1);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setExamenValoracion(solicitudExamenValoracionOld);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));
                when(solicitudExamenValoracionRepository
                                .findById(trabajoGrado.getExamenValoracion().getIdExamenValoracion()))
                                .thenReturn(Optional.of(solicitudExamenValoracionOld));

                PersonaDto personaDto = new PersonaDto();
                personaDto.setIdentificacion(12345678L);
                personaDto.setNombre("Juan");
                personaDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(personaDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                when(solicitudExamenValoracionRepository.save(solicitudExamenValoracionOld))
                                .thenReturn(solicitudExamenValoracionOld);

                SolicitudExamenValoracionDocenteResponseDto solicitudExamenValoracionDocenteResponseDto = new SolicitudExamenValoracionDocenteResponseDto();
                solicitudExamenValoracionDocenteResponseDto.setIdExamenValoracion(1L);
                solicitudExamenValoracionDocenteResponseDto.setTitulo("Cambio nombre");
                solicitudExamenValoracionDocenteResponseDto.setLinkFormatoA(
                                "./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240627220507-formatoA.txt");
                solicitudExamenValoracionDocenteResponseDto.setLinkFormatoD(
                                "./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240627220507-formatoD.txt");
                solicitudExamenValoracionDocenteResponseDto.setLinkFormatoE(
                                "./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240627220507-formatoE.txt");
                solicitudExamenValoracionDocenteResponseDto.setIdEvaluadorInterno(1L);
                solicitudExamenValoracionDocenteResponseDto.setIdEvaluadorExterno(2L);

                when(examenValoracionResponseMapper.toDocenteDto(solicitudExamenValoracionOld))
                                .thenReturn(solicitudExamenValoracionDocenteResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        SolicitudExamenValoracionDocenteResponseDto resultado = solicitudExamenValoracionService
                                        .actualizarInformacionDocente(idTrabajoGrado, examenValoracionDto, result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getIdExamenValoracion());
                        assertEquals("Cambio nombre", resultado.getTitulo());
                        assertEquals("./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240627220507-formatoA.txt",
                                        resultado.getLinkFormatoA());
                        assertEquals("./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240627220507-formatoD.txt",
                                        resultado.getLinkFormatoD());
                        assertEquals("./files/2024/6/12345678-Juan_Meneses/Solicitud_Examen_Valoracion/29-06-24/20240627220507-formatoE.txt",
                                        resultado.getLinkFormatoE());
                        assertEquals(1L, resultado.getIdEvaluadorInterno());
                        assertEquals(2L, resultado.getIdEvaluadorExterno());
                }
        }

        @Test
        void testActualizarInformacionDocente_FaltanAtributos() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                // No se establece linkFormatoE
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);

                // Configurar el BindingResult para que contenga errores
                FieldError fieldError = new FieldError("SolicitudExamenValoracionDocenteDto", "linkFormatoE",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                        examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: linkFormatoE, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionDocente_EstadoNoValido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(2L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(3);
                trabajoGrado.setIdEstudiante(123L);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                        examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la informacion";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionDocente_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(2L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                // Configurar el mock para que devuelva Optional.empty()
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                // Lanzar la excepción y verificar el mensaje
                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                        examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionDocente_DocenteNoExiste() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(2L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(1);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                // Simular la excepción lanzada cuando el docente no se encuentra
                when(archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno()))
                                .thenThrow(new ResourceNotFoundException("Docentes con id "
                                                + examenValoracionDto.getIdEvaluadorInterno() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                        examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Docentes con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionDocente_ExpertoNoExiste() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(2L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(1);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                when(archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno()))
                                .thenReturn(docenteResponseDto);

                when(archivoClientExpertos.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno()))
                                .thenThrow(new ResourceNotFoundException("Experto con id "
                                                + examenValoracionDto.getIdEvaluadorExterno() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                        examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Experto con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testActualizarInformacionDocente_ServidorDocenteCaido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(1);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                                examenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void testActualizarInformacionDocente_ServidorExpertoCaido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHVwZGF0ZQ==");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(1);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClientExpertos.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.actualizarInformacionDocente(idTrabajoGrado,
                                                examenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }
}
