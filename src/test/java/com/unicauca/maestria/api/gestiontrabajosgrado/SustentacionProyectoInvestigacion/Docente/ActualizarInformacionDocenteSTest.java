package com.unicauca.maestria.api.gestiontrabajosgrado.SustentacionProyectoInvestigacion.Docente;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClientExpertos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ServiceUnavailableException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.SustentacionProyectoInvestigacionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaComiteSustentacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SustentacionProyectoInvestigacionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActualizarInformacionDocenteSTest {

    @Mock
    private SustentacionProyectoInvestigacionRepository sustentacionProyectoInvestigacionRepository;
    @Mock
    private SustentacionProyectoInvestigacionMapper sustentacionProyectoIngestigacionMapper;
    @Mock
    private SustentacionProyectoInvestigacionResponseMapper sustentacionProyectoInvestigacionResponseMapper;
    @Mock
    private RespuestaComiteSustentacionRepository respuestaComiteSustentacionRepository;
    @Mock
    private TrabajoGradoRepository trabajoGradoRepository;
    @Mock
    private ArchivoClient archivoClient;
    @Mock
    private ArchivoClientExpertos archivoClientExpertos;
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
                trabajoGradoRepository,
                archivoClient,
                archivoClientExpertos,
                archivoClientEgresados);
        ReflectionTestUtils.setField(sustentacionProyectoInvestigacionServiceImpl, "envioCorreos",
                envioCorreos);
    }

    @Test
    void ActualizarInformacionDocenteSTest_ActualizacionExitosaTrue() {

        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
        sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
        sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(19);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(sustentacionProyectoInvestigacionRepository
                .findById(
                        trabajoGrado.getIdSustentacionProyectoInvestigacion().getIdSustentacionTrabajoInvestigacion()))
                .thenReturn(Optional.of(sustentacionTrabajoInvestigacionOld));

        PersonaDto PersonaEstudianteDto = new PersonaDto();
        PersonaEstudianteDto.setIdentificacion(123L);
        PersonaEstudianteDto.setNombre("Juan");
        PersonaEstudianteDto.setApellido("Meneses");

        EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

        when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                .thenReturn(estudianteResponseDtoAll);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionNew = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionNew.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionNew
                .setLinkFormatoF(sustentacionTrabajoInvestigacionDocenteDto.getLinkFormatoF());
        sustentacionTrabajoInvestigacionNew
                .setUrlDocumentacion(sustentacionTrabajoInvestigacionDocenteDto.getUrlDocumentacion());
        sustentacionTrabajoInvestigacionNew
                .setIdJuradoInterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno());
        sustentacionTrabajoInvestigacionNew
                .setIdJuradoExterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno());

        when(sustentacionProyectoInvestigacionRepository.save(any(SustentacionTrabajoInvestigacion.class)))
                .thenReturn(sustentacionTrabajoInvestigacionNew);

        SustentacionTrabajoInvestigacionDocenteResponseDto sustentacionTrabajoInvestigacionDocenteResponseDto = new SustentacionTrabajoInvestigacionDocenteResponseDto();
        sustentacionTrabajoInvestigacionDocenteResponseDto.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionDocenteResponseDto
                .setLinkFormatoF(
                        "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
        sustentacionTrabajoInvestigacionDocenteResponseDto
                .setUrlDocumentacion(sustentacionTrabajoInvestigacionDocenteDto.getUrlDocumentacion());
        sustentacionTrabajoInvestigacionDocenteResponseDto
                .setIdJuradoInterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno());
        sustentacionTrabajoInvestigacionDocenteResponseDto
                .setIdJuradoExterno(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno());

        when(sustentacionProyectoInvestigacionResponseMapper.toDocenteDto(sustentacionTrabajoInvestigacionNew))
                .thenReturn(sustentacionTrabajoInvestigacionDocenteResponseDto);

        try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
            utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                    .thenReturn("path/to/new/file");
            utilities.when(() -> FilesUtilities.deleteFileExample(anyString()))
                    .thenReturn(true);

            SustentacionTrabajoInvestigacionDocenteResponseDto resultado = sustentacionProyectoInvestigacionServiceImpl
                    .actualizarInformacionDocente(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionDocenteDto, result);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdSustentacionTrabajoInvestigacion());
            assertEquals(
                    "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt",
                    resultado.getLinkFormatoF());
            assertEquals("www.archivoPrueba2.com", resultado.getUrlDocumentacion());
            assertEquals(1, resultado.getIdJuradoInterno());
            assertEquals(1, resultado.getIdJuradoExterno());
        }

    }

    @Test
    void ActualizarInformacionDocenteSTest_FaltanAtributos() {

        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

        FieldError fieldError = new FieldError("SustentacionTrabajoInvestigacionDocenteDto", "idJuradoInterno",
                "no debe ser nulo");
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(fieldError));

        FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl
                    .actualizarInformacionDocente(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionDocenteDto, result);
        });

        assertNotNull(exception.getResult());
        List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
        assertFalse(fieldErrors.isEmpty());
        String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                + fieldError.getDefaultMessage();
        String expectedMessage = "El campo: idJuradoInterno, no debe ser nulo";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void ActualizarInformacionDocenteSTest_EstadoNoValido() {

        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

        when(result.hasErrors()).thenReturn(false);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(17);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        InformationException exception = assertThrows(InformationException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl
                    .actualizarInformacionDocente(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionDocenteDto, result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No es permitido registrar la informacion";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ActualizarInformacionDocenteSTest_JuradoInternoNoExiste() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(2L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
        sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
        sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(19);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        // Simular la excepción lanzada cuando el docente no se encuentra
        when(archivoClient.obtenerDocentePorId(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno()))
                .thenThrow(new ResourceNotFoundException("Docentes con id "
                        + sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno()
                        + " no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl
                    .actualizarInformacionDocente(idTrabajoGrado,
                            sustentacionTrabajoInvestigacionDocenteDto, result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Docentes con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ActualizarInformacionDocenteSTest_JuradoExternoNoExiste() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(2L);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
        sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
        sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(19);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(archivoClientExpertos
                .obtenerExpertoPorId(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno()))
                .thenThrow(new ResourceNotFoundException("Experto con id "
                        + sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno()
                        + " no encontrado"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionDocente(idTrabajoGrado,
                    sustentacionTrabajoInvestigacionDocenteDto, result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Experto con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ActualizarInformacionDocenteSTest_ServidorDocenteCaido() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(2L);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
        sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
        sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(19);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(archivoClient.obtenerDocentePorId(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoInterno()))
                .thenThrow(new ServiceUnavailableException(
                        "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionDocente(idTrabajoGrado,
                        sustentacionTrabajoInvestigacionDocenteDto, result),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }

    @Test
    void SustentacionProyectoInvestigacionServiceImplTest_ServidorExpertoCaido() {
        Long idTrabajoGrado = 1L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(2L);

        when(result.hasErrors()).thenReturn(false);

        SustentacionTrabajoInvestigacion sustentacionTrabajoInvestigacionOld = new SustentacionTrabajoInvestigacion();
        sustentacionTrabajoInvestigacionOld.setIdSustentacionTrabajoInvestigacion(1L);
        sustentacionTrabajoInvestigacionOld.setLinkFormatoF(
                "./files/2024/6/1084-Juan_Meneses/Solicitud_Examen_Valoracion/27-06-24/20240627220507-linkFormatoF.txt");
        sustentacionTrabajoInvestigacionOld.setUrlDocumentacion("www.google.com");
        sustentacionTrabajoInvestigacionOld.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionOld.setIdJuradoExterno(1L);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(19);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setIdSustentacionProyectoInvestigacion(sustentacionTrabajoInvestigacionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(archivoClientExpertos
                .obtenerExpertoPorId(sustentacionTrabajoInvestigacionDocenteDto.getIdJuradoExterno()))
                .thenThrow(new ServiceUnavailableException(
                        "Servidor externo actualmente fuera de servicio"));

        ServiceUnavailableException thrown = assertThrows(
                ServiceUnavailableException.class,
                () -> sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionDocente(idTrabajoGrado,
                        sustentacionTrabajoInvestigacionDocenteDto, result),
                "Servidor externo actualmente fuera de servicio");

        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Servidor externo actualmente fuera de servicio"));
    }

    @Test
    void SustentacionProyectoInvestigacionServiceImpltTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        SustentacionTrabajoInvestigacionDocenteDto sustentacionTrabajoInvestigacionDocenteDto = new SustentacionTrabajoInvestigacionDocenteDto();
        sustentacionTrabajoInvestigacionDocenteDto.setLinkFormatoF("linkFormatoF.txt-cHJ1ZWJhIGRlIHRleHR");
        sustentacionTrabajoInvestigacionDocenteDto.setUrlDocumentacion("www.archivoPrueba2.com");
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoInterno(1L);
        sustentacionTrabajoInvestigacionDocenteDto.setIdJuradoExterno(1L);

        when(result.hasErrors()).thenReturn(false);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sustentacionProyectoInvestigacionServiceImpl.actualizarInformacionDocente(idTrabajoGrado,
                    sustentacionTrabajoInvestigacionDocenteDto, result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
