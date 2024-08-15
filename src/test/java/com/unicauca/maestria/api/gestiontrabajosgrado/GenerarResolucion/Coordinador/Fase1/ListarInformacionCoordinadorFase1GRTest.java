package com.unicauca.maestria.api.gestiontrabajosgrado.GenerarResolucion.Coordinador.Fase1;

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
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.EnvioCorreos;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
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
public class ListarInformacionCoordinadorFase1GRTest {

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
    void ListarInformacionCoordinadorFase1GRTest_Exito() {

        Long idTrabajoGrado = 1L;

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();
        generacionResolucion.setId(1L);
        generacionResolucion.setDirector(1L);
        generacionResolucion.setCodirector(1L);
        generacionResolucion.setLinkAnteproyectoFinal(
                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkAnteproyectoAprobado.txt");
        generacionResolucion.setLinkSolicitudComite(
                "./files/2024/7/1084-Juan_Meneses/Generacion_Resolucion/02-07-24/20240702174506-linkSolicitudComite.txt");
        generacionResolucion.setConceptoDocumentosCoordinador(ConceptoVerificacion.ACEPTADO);

        when(generacionResolucionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(generacionResolucion));

        GeneracionResolucionCoordinadorFase1ResponseDto generacionResolucionCoordinadorFase1ResponseDto = new GeneracionResolucionCoordinadorFase1ResponseDto();
        generacionResolucionCoordinadorFase1ResponseDto
                .setId(generacionResolucion.getId());
        generacionResolucionCoordinadorFase1ResponseDto.setConceptoDocumentosCoordinador(
                generacionResolucion.getConceptoDocumentosCoordinador());

        when(generacionResolucionResponseMapper.toCoordinadorFase1Dto(generacionResolucion))
                .thenReturn(generacionResolucionCoordinadorFase1ResponseDto);

        GeneracionResolucionCoordinadorFase1ResponseDto resultado = generacionResolucionServiceImpl
                .listarInformacionCoordinadorFase1(idTrabajoGrado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(ConceptoVerificacion.ACEPTADO, resultado.getConceptoDocumentosCoordinador());

    }

    @Test
    void ListarInformacionCoordinadorFase1GRTest_NoHayDatos() {
        Long idTrabajoGrado = 1L;

        GeneracionResolucion generacionResolucion = new GeneracionResolucion();

        when(generacionResolucionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.of(generacionResolucion));

        InformationException exception = assertThrows(InformationException.class, () -> {
            generacionResolucionServiceImpl.listarInformacionCoordinadorFase1(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "No se han registrado datos";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void ListarInformacionCoordinadorFase1GRTest_TrabajoGradoNoExiste() {
        Long idTrabajoGrado = 2L;

        when(generacionResolucionRepository.findByTrabajoGradoId(idTrabajoGrado))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            generacionResolucionServiceImpl.listarInformacionCoordinadorFase1(idTrabajoGrado);
        });

        assertNotNull(exception.getMessage());
        String expectedMessage = "Trabajo de grado con id 2 no encontrado";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
