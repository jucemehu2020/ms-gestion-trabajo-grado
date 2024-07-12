package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Coordinador.Fase1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDtoAll;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
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
public class ActualizarInformacionCoordinadorFase1GRTest {

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
    void testActualizarInformacionCoordinadorFase1GRTest_ActualizacionExitosaTrue() {
        Long idTrabajoGrado = 1L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al comite");
        envioEmailDto.setMensaje("Envio documento para revision");

        ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto = new ObtenerDocumentosParaEnvioDto();
        obtenerDocumentosParaEnvioDto.setBase64AnteproyectoFinal("cHJ1ZWJhIGRlIHRleHR");
        obtenerDocumentosParaEnvioDto.setBase64SolicitudComite("cHJ1ZWJhIGRlIHRleHR");

        GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto = new GeneracionResolucionCoordinadorFase1Dto();
        generacionResolucionCoordinadorFase1Dto.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);
        generacionResolucionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
        generacionResolucionCoordinadorFase1Dto.setObtenerDocumentosParaEnvio(obtenerDocumentosParaEnvioDto);

        when(result.hasErrors()).thenReturn(false);

        GeneracionResolucion generacionResolucionOld = new GeneracionResolucion();
        generacionResolucionOld.setId(1L);
        generacionResolucionOld.setConceptoDocumentosCoordinador(ConceptoVerificacion.RECHAZADO);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(14);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(generacionResolucionRepository
                .findById(trabajoGrado.getGeneracionResolucion().getId()))
                .thenReturn(Optional.of(generacionResolucionOld));

        when(envioCorreos.enviarCorreoConAnexos(any(ArrayList.class), anyString(), anyString(), anyMap()))
                .thenReturn(true);

        when(generacionResolucionRepository.save(any(GeneracionResolucion.class)))
                .thenReturn(generacionResolucionOld);

        GeneracionResolucionCoordinadorFase1ResponseDto generacionResolucionCoordinadorFase1ResponseDto = new GeneracionResolucionCoordinadorFase1ResponseDto();
        generacionResolucionCoordinadorFase1ResponseDto
                .setId(generacionResolucionOld.getId());
        generacionResolucionCoordinadorFase1ResponseDto.setConceptoDocumentosCoordinador(
                generacionResolucionCoordinadorFase1Dto.getConceptoDocumentosCoordinador());

        when(generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucionOld))
                .thenReturn(generacionResolucionCoordinadorFase1ResponseDto);

        GeneracionResolucionCoordinadorFase1ResponseDto resultado = generacionResolucionServiceImpl
                .actualizarInformacionCoordinadorFase1(idTrabajoGrado, generacionResolucionCoordinadorFase1Dto,
                        result);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(Concepto.APROBADO, resultado.getConceptoDocumentosCoordinador());

    }

    @Test
    void testActualizarInformacionCoordinadorFase1GRTest_ActualizacionExitosaFalse() {
        Long idTrabajoGrado = 1L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al comite");
        envioEmailDto.setMensaje("Envio documento para revision");

        GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto = new GeneracionResolucionCoordinadorFase1Dto();
        generacionResolucionCoordinadorFase1Dto.setConceptoDocumentosCoordinador(ConceptoVerificacion.RECHAZADO);
        generacionResolucionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);

        when(result.hasErrors()).thenReturn(false);

        GeneracionResolucion generacionResolucionOld = new GeneracionResolucion();
        generacionResolucionOld.setId(1L);
        generacionResolucionOld.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(14);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucionOld);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(generacionResolucionRepository
                .findById(trabajoGrado.getGeneracionResolucion().getId()))
                .thenReturn(Optional.of(generacionResolucionOld));

        PersonaDto PersonaEstudianteDto = new PersonaDto();
        PersonaEstudianteDto.setIdentificacion(123L);
        PersonaEstudianteDto.setNombre("Juan");
        PersonaEstudianteDto.setApellido("Meneses");

        EstudianteResponseDtoAll estudianteResponseDtoAll = new EstudianteResponseDtoAll();
        estudianteResponseDtoAll.setPersona(PersonaEstudianteDto);

        when(archivoClient.obtenerInformacionEstudiante(trabajoGrado.getIdEstudiante()))
                .thenReturn(estudianteResponseDtoAll);

        when(envioCorreos.enviarCorreosCorrecion(any(ArrayList.class),
                anyString(),
                anyString()))
                .thenReturn(true);

        when(generacionResolucionRepository.save(any(GeneracionResolucion.class)))
                .thenReturn(generacionResolucionOld);

        GeneracionResolucionCoordinadorFase1ResponseDto generacionResolucionCoordinadorFase1ResponseDto = new GeneracionResolucionCoordinadorFase1ResponseDto();
        generacionResolucionCoordinadorFase1ResponseDto
                .setId(generacionResolucionOld.getId());
        generacionResolucionCoordinadorFase1ResponseDto.setConceptoDocumentosCoordinador(
                generacionResolucionCoordinadorFase1Dto.getConceptoDocumentosCoordinador());

        when(generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucionOld))
                .thenReturn(generacionResolucionCoordinadorFase1ResponseDto);

        GeneracionResolucionCoordinadorFase1ResponseDto resultado = generacionResolucionServiceImpl
                .actualizarInformacionCoordinadorFase1(idTrabajoGrado, generacionResolucionCoordinadorFase1Dto,
                        result);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(Concepto.NO_APROBADO, resultado.getConceptoDocumentosCoordinador());

    }

    @Test
    void testActualizarInformacionCoordinadorFase1GRTest_AtributosIncorrectos() {
        Long idTrabajoGrado = 1L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al comite");
        envioEmailDto.setMensaje("Envio documento para revision");

        ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto = new ObtenerDocumentosParaEnvioDto();
        obtenerDocumentosParaEnvioDto.setBase64AnteproyectoFinal("cHJ1ZWJhIGRlIHRleHR");
        obtenerDocumentosParaEnvioDto.setBase64SolicitudComite("cHJ1ZWJhIGRlIHRleHR");

        GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto = new GeneracionResolucionCoordinadorFase1Dto();
        generacionResolucionCoordinadorFase1Dto.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);
        generacionResolucionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
        generacionResolucionCoordinadorFase1Dto.setObtenerDocumentosParaEnvio(obtenerDocumentosParaEnvioDto);

        when(result.hasErrors()).thenReturn(false);

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                    generacionResolucionCoordinadorFase1Dto,
                    result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Envio de atributos no permitido";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testActualizarInformacionCoordinadorFase1GRTest_FaltanAtributos() {

        Long idTrabajoGrado = 1L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al comite");
        envioEmailDto.setMensaje("Envio documento para revision");

        ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto = new ObtenerDocumentosParaEnvioDto();
        obtenerDocumentosParaEnvioDto.setBase64AnteproyectoFinal("cHJ1ZWJhIGRlIHRleHR");
        obtenerDocumentosParaEnvioDto.setBase64SolicitudComite("cHJ1ZWJhIGRlIHRleHR");

        GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto = new GeneracionResolucionCoordinadorFase1Dto();
        generacionResolucionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
        generacionResolucionCoordinadorFase1Dto.setObtenerDocumentosParaEnvio(obtenerDocumentosParaEnvioDto);

        FieldError fieldError = new FieldError("GeneracionResolucionDocenteDto", "conceptoDocumentosCoordinador",
                "no debe ser nulo");
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(fieldError));

        FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
            generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                    generacionResolucionCoordinadorFase1Dto,
                    result);
        });

        assertNotNull(exception.getResult());
        List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
        assertFalse(fieldErrors.isEmpty());
        String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                + fieldError.getDefaultMessage();
        String expectedMessage = "El campo: conceptoDocumentosCoordinador, no debe ser nulo";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testActualizarInformacionCoordinadorFase1GRTest_EstadoNoValido() {
        Long idTrabajoGrado = 1L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al comite");
        envioEmailDto.setMensaje("Envio documento para revision");

        ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto = new ObtenerDocumentosParaEnvioDto();
        obtenerDocumentosParaEnvioDto.setBase64AnteproyectoFinal("cHJ1ZWJhIGRlIHRleHR");
        obtenerDocumentosParaEnvioDto.setBase64SolicitudComite("cHJ1ZWJhIGRlIHRleHR");

        GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto = new GeneracionResolucionCoordinadorFase1Dto();
        generacionResolucionCoordinadorFase1Dto.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);
        generacionResolucionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
        generacionResolucionCoordinadorFase1Dto.setObtenerDocumentosParaEnvio(obtenerDocumentosParaEnvioDto);

        when(result.hasErrors()).thenReturn(false);

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(11);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucion);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                    generacionResolucionCoordinadorFase1Dto,
                    result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No es permitido registrar la informacion";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testActualizarInformacionCoordinadorFase1GRTest_NoExisteTrabajoGrado() {
        Long idTrabajoGrado = 2L;

        EnvioEmailDto envioEmailDto = new EnvioEmailDto();
        envioEmailDto.setAsunto("Revision documentos al comite");
        envioEmailDto.setMensaje("Envio documento para revision");

        ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto = new ObtenerDocumentosParaEnvioDto();
        obtenerDocumentosParaEnvioDto.setBase64AnteproyectoFinal("cHJ1ZWJhIGRlIHRleHR");
        obtenerDocumentosParaEnvioDto.setBase64SolicitudComite("cHJ1ZWJhIGRlIHRleHR");

        GeneracionResolucionCoordinadorFase1Dto generacionResolucionCoordinadorFase1Dto = new GeneracionResolucionCoordinadorFase1Dto();
        generacionResolucionCoordinadorFase1Dto.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);
        generacionResolucionCoordinadorFase1Dto.setEnvioEmail(envioEmailDto);
        generacionResolucionCoordinadorFase1Dto.setObtenerDocumentosParaEnvio(obtenerDocumentosParaEnvioDto);

        when(result.hasErrors()).thenReturn(false);

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(7);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");
        trabajoGrado.setGeneracionResolucion(generacionResolucion);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            generacionResolucionServiceImpl.actualizarInformacionCoordinadorFase1(idTrabajoGrado,
                    generacionResolucionCoordinadorFase1Dto,
                    result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}