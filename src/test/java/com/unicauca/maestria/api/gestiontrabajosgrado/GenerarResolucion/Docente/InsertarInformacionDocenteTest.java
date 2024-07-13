package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Docente;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
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
public class InsertarInformacionDocenteTest {

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
        void InsertarInformacionDocenteTest_RegistroExitoso() {

                Long idTrabajoGrado = 1L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(1L);
                generacionResolucionDocenteDto.setIdCodirector(2L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(7);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                PersonaDto persona1Dto = new PersonaDto();
                persona1Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
                persona1Dto.setIdentificacion(1234L);
                persona1Dto.setNombre("Karla");
                persona1Dto.setApellido("Ramirez");

                PersonaDto persona2Dto = new PersonaDto();
                persona2Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
                persona2Dto.setIdentificacion(123L);
                persona2Dto.setNombre("Luis");
                persona2Dto.setApellido("Perez");

                DocenteResponseDto docenteResponse1Dto = new DocenteResponseDto();
                docenteResponse1Dto.setId(1L);
                docenteResponse1Dto.setPersona(persona1Dto);

                DocenteResponseDto docenteResponse2Dto = new DocenteResponseDto();
                docenteResponse2Dto.setId(2L);
                docenteResponse2Dto.setPersona(persona2Dto);

                when(archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdDirector()))
                                .thenReturn(docenteResponse1Dto);
                when(archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdCodirector()))
                                .thenReturn(docenteResponse2Dto);

                PersonaDto PersonaEstudianteDto = new PersonaDto();
                PersonaEstudianteDto.setIdentificacion(123L);
                PersonaEstudianteDto.setNombre("Juan");
                PersonaEstudianteDto.setApellido("Meneses");

                EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
                estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

                when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                                .thenReturn(estudianteResponseDtoAll);

                GeneracionResolucion generacionResolucion = new GeneracionResolucion();
                generacionResolucion.setDirector(1L);
                generacionResolucion.setCodirector(2L);
                generacionResolucion.setLinkAnteproyectoFinal(
                                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkAnteproyectoAprobado.txt");
                generacionResolucion.setLinkSolicitudComite(
                                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudComite.txt");

                when(generacionResolucionMapper.toEntity(generacionResolucionDocenteDto))
                                .thenReturn(generacionResolucion);
                when(generacionResolucionRepository.save(any(GeneracionResolucion.class)))
                                .thenReturn(generacionResolucion);

                GeneracionResolucionDocenteResponseDto generacionResolucionDocenteResponseDto = new GeneracionResolucionDocenteResponseDto();
                generacionResolucionDocenteResponseDto.setId(1L);
                generacionResolucionDocenteResponseDto.setTitulo("Prueba test");
                generacionResolucionDocenteResponseDto.setDirector(1L);
                generacionResolucionDocenteResponseDto.setCodirector(2L);
                generacionResolucionDocenteResponseDto.setLinkAnteproyectoFinal(
                                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkAnteproyectoAprobado.txt");
                generacionResolucionDocenteResponseDto.setLinkSolicitudComite(
                                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudComite.txt");

                when(generacionResolucionResponseMapper.toDocenteDto(generacionResolucion))
                                .thenReturn(generacionResolucionDocenteResponseDto);

                try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
                        utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                                        .thenReturn("path/to/new/file");

                        GeneracionResolucionDocenteResponseDto resultado = generacionResolucionServiceImpl
                                        .insertarInformacionDocente(idTrabajoGrado, generacionResolucionDocenteDto,
                                                        result);

                        assertNotNull(resultado);
                        assertEquals(1L, resultado.getId());
                        assertEquals("Prueba test", resultado.getTitulo());
                        assertEquals(1L, resultado.getDirector());
                        assertEquals(2L, resultado.getCodirector());
                        assertEquals(
                                        "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkAnteproyectoAprobado.txt",
                                        resultado.getLinkAnteproyectoFinal());
                        assertEquals(
                                        "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudComite.txt",
                                        resultado.getLinkSolicitudComite());
                }
        }

        @Test
        void InsertarInformacionDocenteTest_FaltanAtributos() {
                Long idTrabajoGrado = 1L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(1L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                FieldError fieldError = new FieldError("GeneracionResolucionDocenteDto", "idCodirector",
                                "no debe ser nulo");
                when(result.hasErrors()).thenReturn(true);
                when(result.getFieldErrors()).thenReturn(List.of(fieldError));

                FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
                        generacionResolucionServiceImpl.insertarInformacionDocente(idTrabajoGrado,
                                        generacionResolucionDocenteDto,
                                        result);
                });

                assertNotNull(exception.getResult());
                List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
                assertFalse(fieldErrors.isEmpty());
                String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                                + fieldError.getDefaultMessage();
                String expectedMessage = "El campo: idCodirector, no debe ser nulo";
                assertTrue(actualMessage.contains(expectedMessage));

        }

        @Test
        void InsertarInformacionDocenteTest_EstadoNoValido() {

                Long idTrabajoGrado = 1L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(1L);
                generacionResolucionDocenteDto.setIdCodirector(2L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(5);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        generacionResolucionServiceImpl.insertarInformacionDocente(idTrabajoGrado,
                                        generacionResolucionDocenteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No es permitido registrar la informacion";

                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionDocenteTest_DirectorNoExiste() {
                Long idTrabajoGrado = 1L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(3L);
                generacionResolucionDocenteDto.setIdCodirector(2L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(7);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                PersonaDto persona1Dto = new PersonaDto();
                persona1Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
                persona1Dto.setIdentificacion(1234L);
                persona1Dto.setNombre("Karla");
                persona1Dto.setApellido("Ramirez");

                DocenteResponseDto docenteResponse1Dto = new DocenteResponseDto();
                docenteResponse1Dto.setId(1L);
                docenteResponse1Dto.setPersona(persona1Dto);

                when(archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdDirector()))
                                .thenThrow(new ResourceNotFoundException("Docentes con id "
                                                + generacionResolucionDocenteDto.getIdDirector() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        generacionResolucionServiceImpl.insertarInformacionDocente(idTrabajoGrado,
                                        generacionResolucionDocenteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Docentes con id 3 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionDocenteTest_CoDirectorNoExiste() {
                Long idTrabajoGrado = 1L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(1L);
                generacionResolucionDocenteDto.setIdCodirector(2L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(7);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                PersonaDto persona1Dto = new PersonaDto();
                persona1Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
                persona1Dto.setIdentificacion(1234L);
                persona1Dto.setNombre("Karla");
                persona1Dto.setApellido("Ramirez");

                PersonaDto persona2Dto = new PersonaDto();
                persona2Dto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
                persona2Dto.setIdentificacion(123L);
                persona2Dto.setNombre("Luis");
                persona2Dto.setApellido("Perez");

                DocenteResponseDto docenteResponse1Dto = new DocenteResponseDto();
                docenteResponse1Dto.setId(1L);
                docenteResponse1Dto.setPersona(persona1Dto);

                DocenteResponseDto docenteResponse2Dto = new DocenteResponseDto();
                docenteResponse2Dto.setId(2L);
                docenteResponse2Dto.setPersona(persona2Dto);

                when(archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdDirector()))
                                .thenReturn(docenteResponse1Dto);

                when(archivoClient.obtenerDocentePorId(generacionResolucionDocenteDto.getIdCodirector()))
                                .thenThrow(new ResourceNotFoundException("Docentes con id "
                                                + generacionResolucionDocenteDto.getIdCodirector() + " no encontrado"));

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        generacionResolucionServiceImpl.insertarInformacionDocente(idTrabajoGrado,
                                        generacionResolucionDocenteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Docentes con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));

        }

        @Test
        void InsertarInformacionDocenteTest_TrabajoGradoNoExiste() {

                Long idTrabajoGrado = 2L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(1L);
                generacionResolucionDocenteDto.setIdCodirector(2L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                        generacionResolucionServiceImpl.insertarInformacionDocente(idTrabajoGrado,
                                        generacionResolucionDocenteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "Trabajo de grado con id 2 no encontrado";
                assertTrue(exception.getMessage().contains(expectedMessage));
        }

        @Test
        void InsertarInformacionDocenteTest_IgualDirectorCodirector() {
                Long idTrabajoGrado = 1L;

                GeneracionResolucionDocenteDto generacionResolucionDocenteDto = new GeneracionResolucionDocenteDto();
                generacionResolucionDocenteDto.setIdDirector(1L);
                generacionResolucionDocenteDto.setIdCodirector(1L);
                generacionResolucionDocenteDto
                                .setLinkAnteproyectoFinal("linkAnteproyectoAprobado.txt-cHJ1ZWJhIGRlIHRleHR");
                generacionResolucionDocenteDto.setLinkSolicitudComite("linkSolicitudComite.txt-cHJ1ZWJhIGRlIHRleHR");

                when(result.hasErrors()).thenReturn(false);

                TrabajoGrado trabajoGrado = new TrabajoGrado();
                trabajoGrado.setId(idTrabajoGrado);
                trabajoGrado.setTitulo("Prueba test");
                trabajoGrado.setNumeroEstado(7);
                trabajoGrado.setIdEstudiante(123L);
                trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

                when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

                InformationException exception = assertThrows(InformationException.class, () -> {
                        generacionResolucionServiceImpl.insertarInformacionDocente(idTrabajoGrado,
                                        generacionResolucionDocenteDto,
                                        result);
                });

                assertNotNull(exception.getMessage());
                String expectedMessage = "No se permite registrar al mismo docente como director y codirector";

                assertTrue(exception.getMessage().contains(expectedMessage));
        }
}
