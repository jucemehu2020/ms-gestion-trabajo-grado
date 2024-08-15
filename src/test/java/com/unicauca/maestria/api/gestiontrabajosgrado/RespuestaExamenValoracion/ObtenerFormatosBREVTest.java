package com.unicauca.maestria.api.gestiontrabajosgrado.RespuestaExamenValoracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RetornoFormatoBDto;
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
public class ObtenerFormatosBREVTest {

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
    void ObtenerFormatosBREVTest_ObtencionExitosa() {

        Long idTrabajoGrado = 1L;

        TrabajoGrado trabajoGrado = new TrabajoGrado();
        Mockito.when(trabajoGradoRepository.findById(idTrabajoGrado)).thenReturn(Optional.of(trabajoGrado));

        List<String> listaFormatosB = Arrays.asList(
                "./files/2024/8/1084-Juan_Meneses/Respuesta_Examen_Valoracion/10-08-24/20240810015536-formatoB.txt",
                "./files/2024/8/1084-Juan_Meneses/Respuesta_Examen_Valoracion/10-08-24/20240810015939-formatoB.txt");
        Mockito.when(respuestaExamenValoracionRepository
                .findLinkFormatoBByIdTrabajoGradoAndRespuestaExamenValoracion(idTrabajoGrado))
                .thenReturn(listaFormatosB);

        RetornoFormatoBDto resultado = respuestaExamenValoracionServiceImpl.obtenerFormatosB(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(2, resultado.getFormatosB().size());
        assertEquals(
                "./files/2024/8/1084-Juan_Meneses/Respuesta_Examen_Valoracion/10-08-24/20240810015536-formatoB.txt",
                resultado.getFormatosB().get("formatoBEv1"));
        assertEquals(
                "./files/2024/8/1084-Juan_Meneses/Respuesta_Examen_Valoracion/10-08-24/20240810015939-formatoB.txt",
                resultado.getFormatosB().get("formatoBEv2"));
    }

    @Test
    void ObtenerFormatosBREVTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            respuestaExamenValoracionServiceImpl.obtenerFormatosB(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
