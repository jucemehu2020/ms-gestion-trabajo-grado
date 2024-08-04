package com.unicauca.maestria.api.gestiontrabajosgrado.RespuestaExamenValoracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado.ExamenValoracionCanceladoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.FieldErrorException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.InformationException;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoRespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.AnexosRespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InsertarInformacionCanceladoREVTest {

    @Mock
    private RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
    @Mock
    private ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
    @Mock
    private AnexosRespuestaExamenValoracionRepository anexosRespuestaExamenValoracionRepository;
    @Mock
    private RespuestaExamenValoracionMapper respuestaExamenValoracionMapper;
    @Mock
    private RespuestaExamenValoracionResponseMapper respuestaExamenValoracionResponseMapper;
    @Mock
    private AnexoRespuestaExamenValoracionMapper anexoRespuestaExamenValoracionMapper;
    @Mock
    private ExamenValoracionCanceladoMapper examenValoracionCanceladoMapper;
    @Mock
    private TrabajoGradoRepository trabajoGradoRepository;
    @Mock
    private SolicitudExamenValoracionRepository solicitudExamenValoracionRepository;
    @Mock
    private TiemposPendientesRepository tiemposPendientesRepository;
    @Mock
    private ArchivoClient archivoClient;
    @Mock
    private BindingResult result;
    @Mock
    private EnvioCorreos envioCorreos;
    @InjectMocks
    private RespuestaExamenValoracionServiceImpl respuestaExamenValoracionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        respuestaExamenValoracionServiceImpl = new RespuestaExamenValoracionServiceImpl(
                respuestaExamenValoracionRepository,
                examenValoracionCanceladoRepository,
                anexosRespuestaExamenValoracionRepository,
                respuestaExamenValoracionMapper,
                respuestaExamenValoracionResponseMapper,
                anexoRespuestaExamenValoracionMapper,
                examenValoracionCanceladoMapper,
                trabajoGradoRepository,
                solicitudExamenValoracionRepository,
                tiemposPendientesRepository,
                archivoClient);
        ReflectionTestUtils.setField(respuestaExamenValoracionServiceImpl, "envioCorreos", envioCorreos);
    }

    @Test
    void InsertarInformacionCanceladoREVTest_InsertarExitoso() {
        Long idTrabajoGrado = 1L;

        ExamenValoracionCanceladoDto examenValoracionCanceladoDto = new ExamenValoracionCanceladoDto();
        examenValoracionCanceladoDto.setObservacion("Se reprobo dos veces por cada evaluador");

        when(result.hasErrors()).thenReturn(false);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(15);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(respuestaExamenValoracionRepository.countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado))
                .thenReturn(4L);

        ExamenValoracionCancelado examenValoracionCancelado = new ExamenValoracionCancelado();
        examenValoracionCancelado.setId(1L);
        examenValoracionCancelado.setObservacion(examenValoracionCanceladoDto.getObservacion());
        examenValoracionCancelado.setTrabajoGrado(trabajoGrado);

        when(examenValoracionCanceladoMapper.toEntity(examenValoracionCanceladoDto))
                .thenReturn(examenValoracionCancelado);

        when(examenValoracionCanceladoRepository.save(any(ExamenValoracionCancelado.class)))
                .thenReturn(examenValoracionCancelado);

        when(examenValoracionCanceladoMapper.toDto(examenValoracionCancelado))
                .thenReturn(examenValoracionCanceladoDto);

        ExamenValoracionCanceladoDto resultado = respuestaExamenValoracionServiceImpl
                .insertarInformacionCancelado(idTrabajoGrado, examenValoracionCanceladoDto, result);

        assertEquals("Se reprobo dos veces por cada evaluador", resultado.getObservacion());

    }

    @Test
    void InsertarInformacionCanceladoREVTest_FaltanAtributos() {
        Long idTrabajoGrado = 1L;

        ExamenValoracionCanceladoDto examenValoracionCanceladoDto = new ExamenValoracionCanceladoDto();

        FieldError fieldError = new FieldError("ExamenValoracionCanceladoDto", "observacion",
                "no debe ser nulo");
        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(fieldError));

        FieldErrorException exception = assertThrows(FieldErrorException.class, () -> {
            respuestaExamenValoracionServiceImpl
                    .insertarInformacionCancelado(idTrabajoGrado, examenValoracionCanceladoDto, result);
        });

        assertNotNull(exception.getResult());
        List<FieldError> fieldErrors = exception.getResult().getFieldErrors();
        assertFalse(fieldErrors.isEmpty());
        String actualMessage = "El campo: " + fieldErrors.get(0).getField() + ", "
                + fieldError.getDefaultMessage();
        String expectedMessage = "El campo: observacion, no debe ser nulo";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void InsertarInformacionCanceladoREVTest_NumeroDeNoAprobadosIncorrecto() {
        Long idTrabajoGrado = 1L;

        ExamenValoracionCanceladoDto examenValoracionCanceladoDto = new ExamenValoracionCanceladoDto();
        examenValoracionCanceladoDto.setObservacion("Se reprobo dos veces por cada evaluador");

        when(result.hasErrors()).thenReturn(false);

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(12);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(respuestaExamenValoracionRepository.countByTrabajoGradoIdAndRespuestaNoAprobado(idTrabajoGrado))
                .thenReturn(2L);

        InformationException exception = assertThrows(InformationException.class, () -> {
            respuestaExamenValoracionServiceImpl
                    .insertarInformacionCancelado(idTrabajoGrado, examenValoracionCanceladoDto, result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No es permitido registrar la información";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void InsertarInformacionCanceladoREVTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        ExamenValoracionCanceladoDto examenValoracionCanceladoDto = new ExamenValoracionCanceladoDto();
        examenValoracionCanceladoDto.setObservacion("Se reprobo dos veces por cada evaluador");

        when(result.hasErrors()).thenReturn(false);

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            respuestaExamenValoracionServiceImpl
                    .insertarInformacionCancelado(idTrabajoGrado, examenValoracionCanceladoDto, result);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));

    }

}
