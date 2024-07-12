package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Coordinador.Fase2;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.FilesUtilities;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.RespuestaComiteGeneracionResolucionDto;
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
public class InsertarInformacionCoordinadorFase2GRTest {

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
    void testInsertarInformacionCoordinadorFase2GRTest_RegistroExitosoTrue() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setConceptoComite(Concepto.APROBADO);
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto
                .setLinkSolicitudConsejoFacultad("linkSolicitudConcejoFacultad.txt-cHJ1ZWJhIGRlIHRleHR");
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setActaFechaRespuestaComite(new ArrayList<>());
        generacionResolucion.setLinkSolicitudConsejoFacultad(
                generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejoFacultad());

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(15);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucion);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(generacionResolucionRepository
                .findById(trabajoGrado.getGeneracionResolucion().getId()))
                .thenReturn(Optional.of(generacionResolucion));

        when(generacionResolucionMapper.toEntity(generacionResolucionCoordinadorFase2Dto))
                .thenReturn(generacionResolucion);

        when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                .thenReturn(true);

        PersonaDto PersonaEstudianteDto = new PersonaDto();
        PersonaEstudianteDto.setIdentificacion(123L);
        PersonaEstudianteDto.setNombre("Juan");
        PersonaEstudianteDto.setApellido("Meneses");

        EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

        when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                .thenReturn(estudianteResponseDtoAll);

        when(generacionResolucionRepository.save(any(GeneracionResolucion.class)))
                .thenReturn(generacionResolucion);

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionResponseDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionResponseDto.setConceptoComite(Concepto.APROBADO);
        respuestaComiteGeneracionResolucionResponseDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionResponseDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteResponseDto = new ArrayList<>();
        listaRespuestaComiteResponseDto.add(respuestaComiteGeneracionResolucionResponseDto);

        GeneracionResolucionCoordinadorFase2ResponseDto generacionResolucionCoordinadorFase2ResponseDto = new GeneracionResolucionCoordinadorFase2ResponseDto();
        generacionResolucionCoordinadorFase2ResponseDto
                .setId(generacionResolucion.getId());
        generacionResolucionCoordinadorFase2ResponseDto.setActaFechaRespuestaComite(listaRespuestaComiteResponseDto);
        generacionResolucionCoordinadorFase2ResponseDto.setLinkSolicitudConsejoFacultad(
                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudConcejoFacultad.txt");

        when(generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucion))
                .thenReturn(generacionResolucionCoordinadorFase2ResponseDto);

        try (MockedStatic<FilesUtilities> utilities = mockStatic(FilesUtilities.class)) {
            utilities.when(() -> FilesUtilities.guardarArchivoNew2(anyString(), anyString()))
                    .thenReturn("path/to/new/file");

            GeneracionResolucionCoordinadorFase2ResponseDto resultado = generacionResolucionServiceImpl
                    .insertarInformacionCoordinadorFase2(idTrabajoGrado, generacionResolucionCoordinadorFase2Dto,
                            result);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals(Concepto.APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
            assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
            assertEquals(LocalDate.parse("2023-05-24", formatter),
                    resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
            assertEquals(
                    "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudConcejoFacultad.txt",
                    resultado.getLinkSolicitudConsejoFacultad());
        }

    }

    @Test
    void InsertarInformacionCoordinadorFase1GRTest_RegistroExitosoFalse() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setConceptoComite(Concepto.NO_APROBADO);
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        RespuestaComiteGeneracionResolucion respuestaComiteGeneracionResolucion = new RespuestaComiteGeneracionResolucion();
        respuestaComiteGeneracionResolucion.setConceptoComite(Concepto.NO_APROBADO);
        respuestaComiteGeneracionResolucion.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucion.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucion> listaRespuestaComite = new ArrayList<>();
        listaRespuestaComite.add(respuestaComiteGeneracionResolucion);

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setActaFechaRespuestaComite(listaRespuestaComite);
        generacionResolucion.setLinkSolicitudConsejoFacultad(
                generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejoFacultad());

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(15);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucion);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(generacionResolucionRepository
                .findById(trabajoGrado.getGeneracionResolucion().getId()))
                .thenReturn(Optional.of(generacionResolucion));

        when(generacionResolucionMapper.toEntity(generacionResolucionCoordinadorFase2Dto))
                .thenReturn(generacionResolucion);

        PersonaDto PersonaEstudianteDto = new PersonaDto();
        PersonaEstudianteDto.setIdentificacion(123L);
        PersonaEstudianteDto.setNombre("Juan");
        PersonaEstudianteDto.setApellido("Meneses");

        EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

        when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                .thenReturn(estudianteResponseDtoAll);

        when(envioCorreos.enviarCorreosCorrecion(any(ArrayList.class), anyString(), anyString())).thenReturn(true);

        when(generacionResolucionRepository.save(any(GeneracionResolucion.class)))
                .thenReturn(generacionResolucion);

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionResponseDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionResponseDto.setConceptoComite(Concepto.NO_APROBADO);
        respuestaComiteGeneracionResolucionResponseDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionResponseDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteResponseDto = new ArrayList<>();
        listaRespuestaComiteResponseDto.add(respuestaComiteGeneracionResolucionResponseDto);

        GeneracionResolucionCoordinadorFase2ResponseDto generacionResolucionCoordinadorFase2ResponseDto = new GeneracionResolucionCoordinadorFase2ResponseDto();
        generacionResolucionCoordinadorFase2ResponseDto
                .setId(generacionResolucion.getId());
        generacionResolucionCoordinadorFase2ResponseDto.setActaFechaRespuestaComite(listaRespuestaComiteResponseDto);

        when(generacionResolucionResponseMapper.toCoordinadorFase2Dto(generacionResolucion))
                .thenReturn(generacionResolucionCoordinadorFase2ResponseDto);

        GeneracionResolucionCoordinadorFase2ResponseDto resultado = generacionResolucionServiceImpl
                .insertarInformacionCoordinadorFase2(idTrabajoGrado, generacionResolucionCoordinadorFase2Dto, result);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(Concepto.NO_APROBADO, resultado.getActaFechaRespuestaComite().get(0).getConceptoComite());
        assertEquals("AX1-3445", resultado.getActaFechaRespuestaComite().get(0).getNumeroActa());
        assertEquals(LocalDate.parse("2023-05-24", formatter),
                resultado.getActaFechaRespuestaComite().get(0).getFechaActa());
        assertEquals(null, resultado.getLinkSolicitudConsejoFacultad());

    }

    @Test
    void InsertarInformacionCoordinadorFase1GRTest_AtributosIncorrectos() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setConceptoComite(Concepto.NO_APROBADO);
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto
                .setLinkSolicitudConsejoFacultad("linkSolicitudConsejoFacultad.txt-cHJ1ZWJhIGRlIHRleHR");
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl
                    .insertarInformacionCoordinadorFase2(idTrabajoGrado, generacionResolucionCoordinadorFase2Dto,
                            result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Envio de atributos no permitido";

        assertTrue(exception.getMessage().contains(expectedMessage));

    }

    @Test
    void testInsertarInformacionCoordinadorFase2GRTest_AprobadoYaExiste() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setConceptoComite(Concepto.NO_APROBADO);
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        RespuestaComiteGeneracionResolucion respuestaComiteGeneracionResolucion = new RespuestaComiteGeneracionResolucion();
        respuestaComiteGeneracionResolucion.setConceptoComite(Concepto.APROBADO);
        respuestaComiteGeneracionResolucion.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucion.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucion> listaRespuestaComite = new ArrayList<>();
        listaRespuestaComite.add(respuestaComiteGeneracionResolucion);

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setActaFechaRespuestaComite(listaRespuestaComite);
        generacionResolucion.setLinkSolicitudConsejoFacultad(
                generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejoFacultad());

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(15);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucion);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(generacionResolucionRepository
                .findById(trabajoGrado.getGeneracionResolucion().getId()))
                .thenReturn(Optional.of(generacionResolucion));

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl
                    .insertarInformacionCoordinadorFase2(idTrabajoGrado, generacionResolucionCoordinadorFase2Dto,
                            result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "El concepto ya es APROBADO";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testInsertarInformacionCoordinadorFase2GRTest_FaltanAtributos() {

        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto
                .setLinkSolicitudConsejoFacultad("linkSolicitudConsejoFacultad.txt-cHJ1ZWJhIGRlIHRleHR");
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        FieldError fieldError = new FieldError("GeneracionResolucionDocenteDto", "conceptoComite",
                "no debe ser nulo");
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(fieldError));

        FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
            generacionResolucionServiceImpl.insertarInformacionCoordinadorFase2(idTrabajoGrado,
                    generacionResolucionCoordinadorFase2Dto,
                    result);
        });

        assertNotNull(exception.getResult());
        List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
        assertFalse(fieldErrors.isEmpty());
        String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                + fieldError.getDefaultMessage();
        String expectedMessage = "El campo: conceptoComite, no debe ser nulo";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInsertarInformacionCoordinadorFase2GRTest_EstadoNoValido() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setConceptoComite(Concepto.APROBADO);
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto
                .setLinkSolicitudConsejoFacultad("linkSolicitudConcejoFacultad.txt-cHJ1ZWJhIGRlIHRleHR");
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        RespuestaComiteGeneracionResolucion respuestaComiteGeneracionResolucion = new RespuestaComiteGeneracionResolucion();
        respuestaComiteGeneracionResolucion.setConceptoComite(Concepto.APROBADO);
        respuestaComiteGeneracionResolucion.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucion.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucion> listaRespuestaComite = new ArrayList<>();
        listaRespuestaComite.add(respuestaComiteGeneracionResolucion);

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setActaFechaRespuestaComite(listaRespuestaComite);
        generacionResolucion.setLinkSolicitudConsejoFacultad(
                generacionResolucionCoordinadorFase2Dto.getLinkSolicitudConsejoFacultad());

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(13);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucion);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.insertarInformacionCoordinadorFase2(idTrabajoGrado,
                    generacionResolucionCoordinadorFase2Dto,
                    result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No es permitido registrar la informacion";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testInsertarInformacionCoordinadorFase2GRTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        RespuestaComiteGeneracionResolucionDto respuestaComiteGeneracionResolucionDto = new RespuestaComiteGeneracionResolucionDto();
        respuestaComiteGeneracionResolucionDto.setConceptoComite(Concepto.APROBADO);
        respuestaComiteGeneracionResolucionDto.setNumeroActa("AX1-3445");
        respuestaComiteGeneracionResolucionDto.setFechaActa(LocalDate.parse("2023-05-24", formatter));

        List<RespuestaComiteGeneracionResolucionDto> listaRespuestaComiteDto = new ArrayList<>();
        listaRespuestaComiteDto.add(respuestaComiteGeneracionResolucionDto);

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al consejo");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase2Dto = new GeneracionResolucionCoordinadorFase2Dto();
        generacionResolucionCoordinadorFase2Dto.setActaFechaRespuestaComite(listaRespuestaComiteDto);
        generacionResolucionCoordinadorFase2Dto
                .setLinkSolicitudConsejoFacultad("linkSolicitudConcejoFacultad.txt-cHJ1ZWJhIGRlIHRleHR");
        generacionResolucionCoordinadorFase2Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            generacionResolucionServiceImpl.insertarInformacionCoordinadorFase2(idTrabajoGrado,
                    generacionResolucionCoordinadorFase2Dto,
                    result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
