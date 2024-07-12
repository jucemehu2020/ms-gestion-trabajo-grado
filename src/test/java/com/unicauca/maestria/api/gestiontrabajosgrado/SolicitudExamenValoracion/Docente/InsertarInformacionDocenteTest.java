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
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.AnexoSolicitudExamenValoracionDto;
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
public class InsertarInformacionDocenteTest {

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
                                null,
                                examenValoracionMapper,
                                examenValoracionResponseMapper,
                                anexoSolicitudExamenValoracionMapper,
                                archivoClient,
                                archivoClientExpertos);
        }

        @Test
        void testInsertarInformacionDocente_RegistroExitoso() {
                // Configuración de datos y mocks
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(0);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                when(archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno()))
                                .thenReturn(docenteResponseDto);

                ExpertoResponseDto expertoResponseDto = new ExpertoResponseDto();
                when(archivoClientExpertos.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno()))
                                .thenReturn(expertoResponseDto);

                // Simulación de la llamada a obtenerInformacionEstudiante
                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                PersonaDto persona = new PersonaDto();
                persona.setIdentificacion(12345678L);
                persona.setNombre("Juan");
                persona.setApellido("Meneses");
                estudianteResponseDtoAll.setPersona(persona);
                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                // Aquí configuramos la conversión de DTO a entidad
                SolicitudExamenValoracion examenValoracion = new SolicitudExamenValoracion();
                examenValoracion.setTitulo("Prueba test");
                examenValoracion.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracion.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracion.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracion.setIdEvaluadorInterno(1L);
                examenValoracion.setIdEvaluadorExterno(1L);
                examenValoracion.setTrabajoGrado(trabajoGrado);

                AnexoSolicitudExamenValoracion anexo1 = new AnexoSolicitudExamenValoracion();
                anexo1.setId(10L);
                anexo1.setLinkAnexo("Anexos.txt-cHJ1ZWJhIGRlIHRleHR");

                AnexoSolicitudExamenValoracion anexo2 = new AnexoSolicitudExamenValoracion();
                anexo2.setId(11L);
                anexo2.setLinkAnexo("Anexos2.txt-cHJ1ZWJhIGRlIHRleHR");

                examenValoracion.setAnexos(List.of(anexo1, anexo2));

                when(examenValoracionMapper.toEntity(examenValoracionDto)).thenReturn(examenValoracion);

                when(solicitudExamenValoracionRepository.save(examenValoracion)).thenReturn(examenValoracion);

                SolicitudExamenValoracionDocenteResponseDto responseDto = new SolicitudExamenValoracionDocenteResponseDto();
                responseDto.setId(4L);
                responseDto.setTitulo("Prueba test");
                responseDto.setLinkFormatoA(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoA.txt");
                responseDto.setLinkFormatoD(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoD.txt");
                responseDto.setLinkFormatoE(
                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoE.txt");
                responseDto.setAnexos(List.of(
                                new AnexoSolicitudExamenValoracionDto(10L,
                                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-Anexos.txt"),
                                new AnexoSolicitudExamenValoracionDto(11L,
                                                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-Anexos2.txt")));
                responseDto.setIdEvaluadorInterno(1L);
                responseDto.setIdEvaluadorExterno(1L);

                when(examenValoracionResponseMapper.toDocenteDto(examenValoracion)).thenReturn(responseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");
                        utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                                        .thenReturn(true);

                        SolicitudExamenValoracionDocenteResponseDto resultado = solicitudExamenValoracionService
                                        .insertarInformacionDocente(idTrabajoGrado, examenValoracionDto, result);

                        assertNotNull(resultado);
                        assertEquals(4L, resultado.getId());
                        assertEquals("Prueba test", resultado.getTitulo());
                        assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoA.txt",
                                        resultado.getLinkFormatoA());
                        assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoD.txt",
                                        resultado.getLinkFormatoD());
                        assertEquals("./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-formatoE.txt",
                                        resultado.getLinkFormatoE());
                }
        }

        @Test
        void testInsertarInformacionDocente_FaltanAtributos() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                // No se establece linkFormatoE
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);

                FieldError fieldError = new FieldError("SolicitudExamenValoracionDocenteDto", "linkFormatoE",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado, examenValoracionDto,
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
        void testInsertarInformacionDocente_EstadoNoValido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(3);
                trabajoGrado.setIdEstudiante(123L);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado, examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la informacion";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testInsertarInformacionDocente_TrabajoGradoNoExiste() {
                Long idTrabajoGrado = 2L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado, examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testInsertarInformacionDocente_SolicitudExamenValoracionYaCreada() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(0);
                trabajoGrado.setIdEstudiante(123L);

                when(solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)).thenReturn(true);

                InformationException exception = assertThrows(InformationException.class, () -> {
                        solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado, examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Ya existe un examen de valoracion asociado al trabajo de grado";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testInsertarInformacionDocente_DocenteNoExiste() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(2L); // ID de docente que no existe
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(0);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                // Simular la excepción lanzada cuando el docente no se encuentra
                when(archivoClient.obtenerDocentePorId(2L))
                                .thenThrow(new ResourceNotFoundException("Docentes con id "
                                                + examenValoracionDto.getIdEvaluadorInterno() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado, examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Docentes con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testInsertarInformacionDocente_ExpertoNoExiste() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(2L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(0);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                DocenteResponseDto docenteResponseDto = new DocenteResponseDto();
                when(archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno()))
                                .thenReturn(docenteResponseDto);

                when(archivoClientExpertos.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno()))
                                .thenThrow(new ResourceNotFoundException("Experto con id "
                                                + examenValoracionDto.getIdEvaluadorExterno() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado, examenValoracionDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Experto con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void testInsertarInformacionDocente_ServidorDocenteCaido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(0);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClient.obtenerDocentePorId(examenValoracionDto.getIdEvaluadorInterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado,
                                                examenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }

        @Test
        void testInsertarInformacionDocente_ServidorExpertoCaido() {
                Long idTrabajoGrado = 1L;
                SolicitudExamenValoracionDocenteDto examenValoracionDto = new SolicitudExamenValoracionDocenteDto();
                examenValoracionDto.setTitulo("Prueba test");
                examenValoracionDto.setLinkFormatoA("formatoA.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoD("formatoD.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setLinkFormatoE("formatoE.txt-cHJ1ZWJhIGRlIHRleHR");
                examenValoracionDto.setIdEvaluadorInterno(1L);
                examenValoracionDto.setIdEvaluadorExterno(1L);
                examenValoracionDto.setAnexos(new ArrayList<>());

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setNumeroEstado(0);
                trabajoGrado.setIdEstudiante(123L);

                when(result.hasErrors()).thenReturn(false);
                when(solicitudExamenValoracionRepository.existsByTrabajoGradoId(idTrabajoGrado)).thenReturn(false);
                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                when(archivoClientExpertos.obtenerExpertoPorId(examenValoracionDto.getIdEvaluadorExterno()))
                                .thenThrow(new ServiceUnavailableException(
                                                "Servidor externo actualmente fuera de servicio"));

                ServiceUnavailableException thrown = assertThrows(
                                ServiceUnavailableException.class,
                                () -> solicitudExamenValoracionService.insertarInformacionDocente(idTrabajoGrado,
                                                examenValoracionDto,
                                                result),
                                "Servidor externo actualmente fuera de servicio");

                assertNotNull(thrown.getMessage());
                assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
        }
}
