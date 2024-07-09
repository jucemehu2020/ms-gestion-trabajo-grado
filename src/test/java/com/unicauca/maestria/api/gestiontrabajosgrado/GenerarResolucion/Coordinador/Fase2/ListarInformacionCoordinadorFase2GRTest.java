package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Coordinador.Fase2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.client.ArchivoClient;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.RespuestaComiteGeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.RespuestaComiteGeneracionResolucionDto;
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
public class ListarInformacionCoordinadorFase2GRTest {

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
    void ListarInformacionCoordinadorFase2GRTest_Exito() {
        Long idTrabajoGrado = 1L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudConcejoFacultad.txt");

        when(generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(generacionResolucion));

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

        GeneracionResolucionCoordinadorFase2ResponseDto resultado = generacionResolucionServiceImpl
                .listarInformacionCoordinadorFase2(idTrabajoGrado);

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

    @Test
    void ListarInformacionCoordinadorFase2GRTest_NoHayDatos() {
        Long idTrabajoGrado = 1L;

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();

        when(generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(generacionResolucion));

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.listarInformacionCoordinadorFase2(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarInformacionCoordinadorFase2GRTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(generacionResolucionRepository.findByIdTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            generacionResolucionServiceImpl.listarInformacionCoordinadorFase2(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
