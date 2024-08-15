package com.unicauca.maestria.api.gestiontrabajosgrado.RespuestaExamenValoracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.AnexoRespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.ExamenValoracionCanceladoMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.mappers.RespuestaExamenValoracionResponseMapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.ExamenValoracionCanceladoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.RespuestaExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.SolicitudExamenValoracionRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TiemposPendientesRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.repositories.TrabajoGradoRepository;
import com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion.RespuestaExamenValoracionServiceImpl;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EvaluadorNoRespondioREVTest {

    @Mock
    private RespuestaExamenValoracionRepository respuestaExamenValoracionRepository;
    @Mock
    private ExamenValoracionCanceladoRepository examenValoracionCanceladoRepository;
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
    void EvaluadorNoRespondioTest_Exito() {
        Long idTrabajoGrado = 1L;

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        trabajoGrado.setId(idTrabajoGrado);
        trabajoGrado.setTitulo("Prueba test");
        trabajoGrado.setNumeroEstado(6);
        trabajoGrado.setIdEstudiante(123L);
        trabajoGrado.setCorreoElectronicoTutor("juliomellizo24@gmail.com");

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        when(trabajoGradoRepository.save(trabajoGrado)).thenReturn(trabajoGrado);

        Boolean resultado = respuestaExamenValoracionServiceImpl.evaluadorNoRespondio(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(true, resultado);

    }

    @Test
    void EvaluadorNoRespondioTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            respuestaExamenValoracionServiceImpl.evaluadorNoRespondio(idTrabajoGrado);

        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));

    }

}
