package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Coordinador.Fase3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
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
public class ListarInformacionCoordinadorFase3GRTest {

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
    void ListarInformacionCoordinadorFase3GRTest_Exito() {

        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        GeneracionResolucion generacionResolucionOld = new GeneracionResolucion();
        generacionResolucionOld.setId(1L);
        generacionResolucionOld.setNumeroActaConsejo("b-0432");
        generacionResolucionOld.setFechaActaConsejo(LocalDate.parse("2023-05-24", formatter));

        when(generacionResolucionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(generacionResolucionOld));

        GeneracionResolucionCoordinadorFase3ResponseDto generacionResolucionCoordinadorFase3ResponseDto = new GeneracionResolucionCoordinadorFase3ResponseDto();
        generacionResolucionCoordinadorFase3ResponseDto
                .setId(generacionResolucionOld.getId());
        generacionResolucionCoordinadorFase3ResponseDto
                .setNumeroActaConsejo(generacionResolucionOld.getNumeroActaConsejo());
        generacionResolucionCoordinadorFase3ResponseDto
                .setFechaActaConsejo(generacionResolucionOld.getFechaActaConsejo());

        when(generacionResolucionResponseMapper.toCoordinadorFase3Dto(generacionResolucionOld))
                .thenReturn(generacionResolucionCoordinadorFase3ResponseDto);

        GeneracionResolucionCoordinadorFase3ResponseDto resultado = generacionResolucionServiceImpl
                .listarInformacionCoordinadorFase3(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("b-0432", resultado.getNumeroActaConsejo());
        assertEquals(LocalDate.parse("2023-05-24", formatter), resultado.getFechaActaConsejo());
    }

    @Test
    void ListarInformacionCoordinadorFase3GRTest_NoHayDatos() {
        Long idTrabajoGrado = 1L;

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();

        when(generacionResolucionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(generacionResolucion));

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.listarInformacionCoordinadorFase3(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarInformacionCoordinadorFase3GRTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(generacionResolucionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            generacionResolucionServiceImpl.listarInformacionCoordinadorFase3(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
